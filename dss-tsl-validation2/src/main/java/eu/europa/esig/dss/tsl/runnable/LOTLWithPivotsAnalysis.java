package eu.europa.esig.dss.tsl.runnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.service.http.commons.DSSFileLoader;
import eu.europa.esig.dss.tsl.cache.CacheAccessByKey;
import eu.europa.esig.dss.tsl.cache.CacheAccessFactory;
import eu.europa.esig.dss.tsl.cache.CacheKey;
import eu.europa.esig.dss.tsl.cache.ReadOnlyCacheAccess;
import eu.europa.esig.dss.tsl.parsing.LOTLParsingResult;
import eu.europa.esig.dss.tsl.source.LOTLSource;
import eu.europa.esig.dss.tsl.validation.ValidationResult;
import eu.europa.esig.dss.utils.Utils;

public class LOTLWithPivotsAnalysis extends AbstractAnalysis implements Runnable {

	private static final Logger LOG = LoggerFactory.getLogger(LOTLWithPivotsAnalysis.class);

	private final LOTLSource source;
	private final CacheAccessByKey cacheAccess;
	private final DSSFileLoader dssFileLoader;
	private final CountDownLatch latch;

	public LOTLWithPivotsAnalysis(LOTLSource source, CacheAccessByKey cacheAccess, DSSFileLoader dssFileLoader, CountDownLatch latch) {
		super(cacheAccess, dssFileLoader);
		this.source = source;
		this.cacheAccess = cacheAccess;
		this.dssFileLoader = dssFileLoader;
		this.latch = latch;
	}

	@Override
	public void run() {

		DSSDocument document = download(source.getUrl());

		if (document != null) {

			lotlParsing(source, document);

			validation(document, getCurrentLOTLSigCertificates());
		}

		latch.countDown();
	}

	private List<CertificateToken> getCurrentLOTLSigCertificates() {
		final List<CertificateToken> initialSigCerts = source.getCertificateSource().getCertificates();

		List<CertificateToken> currentLOTLSigners = new ArrayList<CertificateToken>();

		LOTLParsingResult currentLOTLParsing = (LOTLParsingResult) cacheAccess.getParsingResult();
		if (currentLOTLParsing != null) {
			List<String> pivotURLs = currentLOTLParsing.getPivotURLs();
			if (Utils.isCollectionEmpty(pivotURLs)) {
				LOG.trace("No pivot LOTL found");
				currentLOTLSigners.addAll(initialSigCerts);
			} else {
				currentLOTLSigners.addAll(getCurrentLOTLSigCertificatesFromPivots(initialSigCerts, pivotURLs));
			}
		} else {
			LOG.warn("Unable to retrieve the parsing result for the current LOTL (allowed signing certificates set from the configuration)");
			currentLOTLSigners.addAll(initialSigCerts);
		}

		return currentLOTLSigners;
	}

	private List<CertificateToken> getCurrentLOTLSigCertificatesFromPivots(List<CertificateToken> initialSigCerts, List<String> pivotURLs) {

		/*-
		* current 																						-> Signed with pivot 226 certificates
		* https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-pivot-226-mp.xml	-> Signed with pivot 191 certificates
		* https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-pivot-191-mp.xml	-> Signed with pivot 172 certificates
		* https://ec.europa.eu/information_society/policy/esignature/trusted-list/tl-pivot-172-mp.xml 	-> Signed with OJ Certs
		* http://eur-lex.europa.eu/legal-content/EN/TXT/?uri=uriserv:OJ.C_.2016.233.01.0001.01.ENG		-> OJ
		*/

		Map<String, PivotProcessingResult> processingResults = downloadAndParseAllPivots(pivotURLs);

		ReadOnlyCacheAccess readOnlyCacheAccess = CacheAccessFactory.getReadOnlyCacheAccess();

		Collections.reverse(pivotURLs); // -> 172, 191,..

		List<CertificateToken> currentSigCerts = new ArrayList<CertificateToken>(initialSigCerts);
		for (String pivotUrl : pivotURLs) {
			CacheKey cacheKey = new CacheKey(pivotUrl);

			PivotProcessingResult pivotProcessingResult = processingResults.get(pivotUrl);
			if (pivotProcessingResult != null) {
				validation(pivotProcessingResult.getPivot(), currentSigCerts);

				ValidationResult validationResult = readOnlyCacheAccess.getValidationResult(cacheKey);
				if (validationResult != null && validationResult.isValid()) {
					currentSigCerts = pivotProcessingResult.getLotlSigCerts();
				} else {
					LOG.warn(String.format("Pivot '%s' cannot be validated", pivotUrl));
				}
			} else {
				LOG.warn(String.format("Empty processing result for pivot '%s'", pivotUrl));
			}
		}

		return currentSigCerts;
	}

	private Map<String, PivotProcessingResult> downloadAndParseAllPivots(List<String> pivotURLs) {
		ExecutorService executorService = Executors.newFixedThreadPool(pivotURLs.size());

		Map<String, Future<PivotProcessingResult>> futures = new HashMap<String, Future<PivotProcessingResult>>();
		for (String pivotUrl : pivotURLs) {
			LOTLSource pivotSource = new LOTLSource();
			pivotSource.setUrl(pivotUrl);
			pivotSource.setLotlPredicate(source.getLotlPredicate());
			pivotSource.setTlPredicate(source.getTlPredicate());
			pivotSource.setPivotSupport(source.isPivotSupport());
			futures.put(pivotUrl, executorService.submit(new PivotProcessing(pivotSource, cacheAccess, dssFileLoader)));
		}

		Map<String, PivotProcessingResult> processingResults = new HashMap<String, PivotProcessingResult>();
		for (Entry<String, Future<PivotProcessingResult>> entry : futures.entrySet()) {
			try {
				processingResults.put(entry.getKey(), entry.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				LOG.error(String.format("Unable to retrieve the PivotProcessingResult for url '%s'", entry.getKey()), e);
			}
		}

		shutdownAndAwaitTermination(executorService);

		return processingResults;
	}

	private void shutdownAndAwaitTermination(ExecutorService executorService) {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				shutdownNowAndAwaitTermination(executorService);
			}
		} catch (InterruptedException e) {
			shutdownNowAndAwaitTermination(executorService);
		}
	}

	private void shutdownNowAndAwaitTermination(ExecutorService executorService) {
		executorService.shutdownNow();
		try {
			if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
				LOG.warn("More than 10s to terminate the service executor");
			}
		} catch (InterruptedException e) {
			LOG.warn("Unable to interrupt the service executor", e);
		}
	}

}
