package eu.europa.esig.dss.jades.signature;

import eu.europa.esig.dss.alert.ExceptionOnStatusAlert;
import eu.europa.esig.dss.alert.SilentOnStatusAlert;
import eu.europa.esig.dss.alert.exception.AlertException;
import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.JWSSerializationType;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.jades.JAdESSignatureParameters;
import eu.europa.esig.dss.jades.JAdESTimestampParameters;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.signature.DocumentSignatureService;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JAdESAllSelfSignedCertsTest extends AbstractJAdESTestSignature {

    private DSSDocument documentToSign;
    private JAdESSignatureParameters parameters;
    private JAdESService service;
    private CertificateVerifier certificateVerifier;

    @BeforeEach
    public void init() {
        documentToSign = new FileDocument("src/test/resources/sample.json");

        parameters = new JAdESSignatureParameters();
        parameters.setSignaturePackaging(SignaturePackaging.ENVELOPING);
        parameters.setSigningCertificate(getSigningCert());
        parameters.setCertificateChain(getCertificateChain());
        parameters.setDigestAlgorithm(DigestAlgorithm.SHA256);
        parameters.setJwsSerializationType(JWSSerializationType.JSON_SERIALIZATION);

        certificateVerifier = getCompleteCertificateVerifier();
        service = new JAdESService(certificateVerifier);
        service.setTspSource(getSelfSignedTsa());
    }

    @Test
    public void bLevelTest() {
        parameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_B);
        DSSDocument signedDocument = sign();
        assertNotNull(signedDocument);
    }

    @Test
    public void tLevelTest() {
        parameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_T);
        DSSDocument signedDocument = sign();
        assertNotNull(signedDocument);
    }

    @Test
    public void ltLevelTest() {
        certificateVerifier.setAugmentationAlertOnSelfSignedCertificateChains(new ExceptionOnStatusAlert());

        parameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_LT);
        Exception exception = assertThrows(AlertException.class, () -> super.signAndVerify());
        assertTrue(exception.getMessage().contains("Error on signature augmentation to LT-level."));
        assertTrue(exception.getMessage().contains("The signature contains only self-signed certificate chains."));

        certificateVerifier.setAugmentationAlertOnSelfSignedCertificateChains(new SilentOnStatusAlert());

        DSSDocument signedDocument = sign();
        assertNotNull(signedDocument);
    }

    @Test
    public void ltaLevelTest() {
        certificateVerifier.setAugmentationAlertOnSelfSignedCertificateChains(new ExceptionOnStatusAlert());

        parameters.setSignatureLevel(SignatureLevel.JAdES_BASELINE_LTA);
        Exception exception = assertThrows(AlertException.class, () -> super.signAndVerify());
        assertTrue(exception.getMessage().contains("Error on signature augmentation to LT-level."));
        assertTrue(exception.getMessage().contains("The signature contains only self-signed certificate chains."));

        certificateVerifier.setAugmentationAlertOnSelfSignedCertificateChains(new SilentOnStatusAlert());

        DSSDocument signedDocument = sign();
        assertNotNull(signedDocument);
    }

    @Override
    protected String getSigningAlias() {
        return SELF_SIGNED_USER;
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
    protected JAdESSignatureParameters getSignatureParameters() {
        return parameters;
    }

    @Override
    public void signAndVerify() {
        // do nothing
    }

}
