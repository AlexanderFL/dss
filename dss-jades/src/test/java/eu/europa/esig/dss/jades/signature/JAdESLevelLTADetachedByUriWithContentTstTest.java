package eu.europa.esig.dss.jades.signature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignerDataWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SigDMechanism;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.enumerations.TimestampType;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.JAdESTimestampParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.validation.timestamp.TimestampToken;

public class JAdESLevelLTADetachedByUriWithContentTstTest extends AbstractJAdESTestSignature {

	private DocumentSignatureService<JAdESSignatureParameters, JAdESTimestampParameters> service;
	private DSSDocument documentToSign;
	private Date signingDate;

	@BeforeEach
	public void init() throws Exception {
		service = new JAdESService(getCompleteCertificateVerifier());
		service.setTspSource(getGoodTsa());
		documentToSign = new FileDocument(new File("src/test/resources/sample.json"));
		signingDate = new Date();
	}

	@Override
	protected JAdESSignatureParameters getSignatureParameters() {
		JAdESSignatureParameters signatureParameters = new JAdESSignatureParameters();
		signatureParameters.bLevel().setSigningDate(signingDate);
		signatureParameters.setSigningCertificate(getSigningCert());
		signatureParameters.setCertificateChain(getCertificateChain());
		signatureParameters.setSignaturePackaging(SignaturePackaging.DETACHED);
		signatureParameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_LTA);
		signatureParameters.setJwsSerializationType(JWSSerializationType.FLATTENED_JSON_SERIALIZATION);
		
		signatureParameters.setSigDMechanism(SigDMechanism.OBJECT_ID_BY_URI);

		TimestampToken contentTimestamp = service.getContentTimestamp(documentToSign, signatureParameters);
		signatureParameters.setContentTimestamps(Arrays.asList(contentTimestamp));
		
		return signatureParameters;
	}
	
	@Override
	protected void checkTimestamps(DiagnosticData diagnosticData) {
		super.checkTimestamps(diagnosticData);
		
		List<TimestampWrapper> timestampList = diagnosticData.getTimestampList();
		assertEquals(3, timestampList.size());
		
		boolean contentTstFound = false;
		boolean archiveTstFound = false;
		for (TimestampWrapper timestampWrapper : timestampList) {
			if (TimestampType.CONTENT_TIMESTAMP.equals(timestampWrapper.getType())) {
				contentTstFound = true;
			} else if (TimestampType.ARCHIVE_TIMESTAMP.equals(timestampWrapper.getType())) {
				archiveTstFound = true;
			}
			assertEquals(1, timestampWrapper.getTimestampedSignedData().size());
		}
		assertTrue(contentTstFound);
		assertTrue(archiveTstFound);
	}
	
	@Override
	protected void checkSignatureScopes(DiagnosticData diagnosticData) {
		super.checkSignatureScopes(diagnosticData);
		
		List<SignerDataWrapper> originalSignerDocuments = diagnosticData.getOriginalSignerDocuments();
		assertEquals(1, originalSignerDocuments.size());
		
		SignerDataWrapper signerDataWrapper = originalSignerDocuments.get(0);
		assertEquals(documentToSign.getName(), signerDataWrapper.getReferencedName());
		assertNotNull(signerDataWrapper.getId());
		assertNotNull(signerDataWrapper.getDigestAlgoAndValue());
		assertNotNull(signerDataWrapper.getDigestAlgoAndValue().getDigestMethod());
		assertNotNull(signerDataWrapper.getDigestAlgoAndValue().getDigestValue());
	}
	
	@Override
	protected List<DSSDocument> getDetachedContents() {
		return Arrays.asList(documentToSign);
	}

	@Override
	protected DSSDocument getDocumentToSign() {
		return documentToSign;
	}

	@Override
	protected DocumentSignatureService<JAdESSignatureParameters, JAdESTimestampParameters> getService() {
		return service;
	}

	@Override
	protected String getSigningAlias() {
		return GOOD_USER;
	}

}
