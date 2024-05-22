/**
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * 
 * This file is part of the "DSS - Digital Signature Services" project.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package eu.europa.esig.dss.xades.validation.dss1987;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import eu.europa.esig.dss.detailedreport.DetailedReport;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSignature;
import eu.europa.esig.dss.diagnostic.DiagnosticData;
import eu.europa.esig.dss.diagnostic.SignatureWrapper;
import eu.europa.esig.dss.diagnostic.TimestampWrapper;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.spi.DSSUtils;
import eu.europa.esig.dss.spi.x509.CommonTrustedCertificateSource;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.spi.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.SignedDocumentValidator;
import eu.europa.esig.dss.xades.validation.AbstractXAdESTestValidation;
import eu.europa.esig.validationreport.jaxb.SignatureValidationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationReportType;
import eu.europa.esig.validationreport.jaxb.ValidationTimeInfoType;

public abstract class AbstractDSS1987Test extends AbstractXAdESTestValidation {
	
	private CertificateToken trustAnchor1 = DSSUtils.loadCertificateFromBase64EncodedString(
			"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");
	private CertificateToken trustAnchor2 = DSSUtils.loadCertificateFromBase64EncodedString(
			"MIIFjjCCA3agAwIBAgIITzMgjMWUvzgwDQYJKoZIhvcNAQELBQAwKDELMAkGA1UEBhMCQkUxGTAXBgNVBAMTEEJlbGdpdW0gUm9vdCBDQTQwHhcNMTMwNjI2MTIwMDAwWhcNMzIxMDIyMTIwMDAwWjAoMQswCQYDVQQGEwJCRTEZMBcGA1UEAxMQQmVsZ2l1bSBSb290IENBNDCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAJiQrvrHHm+O4AU6syN4TNHWL911PFsY6E9euwVml5NAWTdw9p2mcmEOYGx424jFLpSQVNxxxoh3LsIpdWUMRQfuiDqzvZx/4dCBaeKL/AMRJuL1d6wU73XKSkdDr5uH6H2Yf19zSiUOm2x4k3aNLyT+VryF11b1Prp67CBk63OBmG0WUaB+ExtBHOkfPaHRHFA04MigoVFt3gLQRGh1V+H1rm1hydTzd6zzpoJHp3ujWD4r4kLCrxVFV0QZ44usvAPlhKoecF0feiKtegS1pS+FjGHA9S85yxZknEV8N6bbK5YP7kgNLDDCNFJ6G7MMpf8MEygXWMb+WrynTetWnIV6jTzZA1RmaZuqmIMDvWTA7JNkiDJQOJBWQ3Ehp+Vn7li1MCIjXlEDYJ2wRmcRZQ0bsUzaM/V3p+Q+j8S3osma3Pc6+dDzxL+Og/lnRnLlDapXx28XB9urUR5H03Ozm77B9/mYgIeM8Y1XntlCCELBeuJeEYJUqc0FsGxWNwjsBtRoZ4dva1rvzkXmjJuNIR4YILg8G4kKLhr9JDrtyCkvI9Xm8GDjqQIJ2KpQiJHBLJA0gKxlYem8CSO/an3AOxqTNZjWbQx6E32OPB/rsU28ldadi9c8yeRyXLWpUF4Ghjyoc4OdrAkXmljnkzLMC459xGL8gj6LyNb6UzX0eYA9AgMBAAGjgbswgbgwDgYDVR0PAQH/BAQDAgEGMA8GA1UdEwEB/wQFMAMBAf8wQgYDVR0gBDswOTA3BgVgOAwBATAuMCwGCCsGAQUFBwIBFiBodHRwOi8vcmVwb3NpdG9yeS5laWQuYmVsZ2l1bS5iZTAdBgNVHQ4EFgQUZ+jxTk+ztfMHbwicDIPZetlb50kwEQYJYIZIAYb4QgEBBAQDAgAHMB8GA1UdIwQYMBaAFGfo8U5Ps7XzB28InAyD2XrZW+dJMA0GCSqGSIb3DQEBCwUAA4ICAQBe3CQAZrNwVZ9Ll3nFWkaKDvMwOE2s1NysTfocGUwyd6c01qsSN52BhRqpaSEWLeSXAfPQK+f57M1hXLNVE8VMf1Vtc0ge+VgjKOWLJ+4d0CAk8VIAK55NUkrSbu4pn+osfD/He0jfECKyq9xrhbn4yxZ/d5qj8RSj+aPmCoX/kaODZmug+AfzY+TXeJgjn8eEQGO8zDJoV/hdUuotkf8eQXeuRhoCuvipBm7vHqEA946NuVtRUmaztLUR9CkbSZ1plWWmqKC+QKErWzvBeswrWxzaRoW9Un7qCSmiO9ddkEHVRHibkUQvPn8kGdG/uOmmRQsbjFuARNCMWS4nHc6TTw7dJgkeZjZiqPl22ifsWJsR/w/VuJMA4kSot/h6qQV9Eglo4ClRlEk3yzbKkcJkLKk6lA90/u46KsqSC5MgUeFjER398iXqpDpT8BzIMovMzHlK7pxTJA5cWXN2a8OMhYCA/Kb6dqIXIi8NKsqzVMXJfX65DM2gWA8rjicJWoooqLhUKuZ6tSWA6If2TRr7MfQsVDhwwUk6mvEIaBJBcyOWH8XgyY6uuHuvGe8CkK+Yk4X2TiE+7GuQe4YVJ/MOGdS3V1eZwPmWSu++azOOFrwoZpIPKOwjbsuLbs0xt6BwWW2XFP025BDh/OD6UE4VsyznnUCkb4AbS947UX6NGA==");
	private CertificateToken trustAnchor3 = DSSUtils.loadCertificateFromBase64EncodedString(
			"MIIE7jCCA9agAwIBAgILBAAAAAABQaHhPSYwDQYJKoZIhvcNAQELBQAwOzEYMBYGA1UEChMPQ3liZXJ0cnVzdCwgSW5jMR8wHQYDVQQDExZDeWJlcnRydXN0IEdsb2JhbCBSb290MB4XDTEzMTAxMDExMDAwMFoXDTI1MDUxMjIyNTkwMFowKDELMAkGA1UEBhMCQkUxGTAXBgNVBAMTEEJlbGdpdW0gUm9vdCBDQTQwggIiMA0GCSqGSIb3DQEBAQUAA4ICDwAwggIKAoICAQCYkK76xx5vjuAFOrMjeEzR1i/ddTxbGOhPXrsFZpeTQFk3cPadpnJhDmBseNuIxS6UkFTcccaIdy7CKXVlDEUH7og6s72cf+HQgWnii/wDESbi9XesFO91ykpHQ6+bh+h9mH9fc0olDptseJN2jS8k/la8hddW9T66euwgZOtzgZhtFlGgfhMbQRzpHz2h0RxQNODIoKFRbd4C0ERodVfh9a5tYcnU83es86aCR6d7o1g+K+JCwq8VRVdEGeOLrLwD5YSqHnBdH3oirXoEtaUvhYxhwPUvOcsWZJxFfDem2yuWD+5IDSwwwjRSehuzDKX/DBMoF1jG/lq8p03rVpyFeo082QNUZmmbqpiDA71kwOyTZIgyUDiQVkNxIaflZ+5YtTAiI15RA2CdsEZnEWUNG7FM2jP1d6fkPo/Et6LJmtz3OvnQ88S/joP5Z0Zy5Q2qV8dvFwfbq1EeR9Nzs5u+wff5mICHjPGNV57ZQghCwXriXhGCVKnNBbBsVjcI7AbUaGeHb2ta785F5oybjSEeGCC4PBuJCi4a/SQ67cgpLyPV5vBg46kCCdiqUIiRwSyQNICsZWHpvAkjv2p9wDsakzWY1m0MehN9jjwf67FNvJXWnYvXPMnkcly1qVBeBoY8qHODnawJF5pY55MyzAuOfcRi/II+i8jW+lM19HmAPQIDAQABo4IBBDCCAQAwDgYDVR0PAQH/BAQDAgEGMBIGA1UdEwEB/wQIMAYBAf8CAQEwUAYDVR0gBEkwRzBFBgorBgEEAbE+AWQBMDcwNQYIKwYBBQUHAgEWKWh0dHA6Ly9jeWJlcnRydXN0Lm9tbmlyb290LmNvbS9yZXBvc2l0b3J5MB0GA1UdDgQWBBRn6PFOT7O18wdvCJwMg9l62VvnSTA1BgNVHR8ELjAsMCqgKKAmhiRodHRwOi8vY3JsLm9tbmlyb290LmNvbS9jdGdsb2JhbC5jcmwwEQYJYIZIAYb4QgEBBAQDAgAHMB8GA1UdIwQYMBaAFLYIew16zKwgTIZWMl7Pq26FLXBXMA0GCSqGSIb3DQEBCwUAA4IBAQBcwXlO+W1txa4MNqD0Tq5ofwESbZ0Rrxoh0RYNqXafZR7Yjj6sEKSSz/WgN8CQYzEWPB6BaScM+VYgOFq9lqr5aedzcsjStqq5uAUXlgnpWXLVBsvKLKR9cKLXE6i7T+juFaKYSO0fNGtPm6KJ/7qvNO3KhocJiDjhykUxV92UDgNXMMH2FhBM/7+k14UgIEllq8zds225jqz9fuS8Srv2lutEv2BLsG6V4GmU9yz2nuLt8X3McwPhh6X5Ae0Pd1gvofOQF+qDtuR4d3NuUt/dHXLPzQfd0t4gMRIM/09GE+dNzRB8p1YRaT4zHRlhH+CHDGjHMIBbb69s9wcD+s/t");
	private CertificateToken trustAnchor4 = DSSUtils.loadCertificateFromBase64EncodedString(
			"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");
	
	private String signatureTimestampId = null;
	
	@Override
	protected SignedDocumentValidator getValidator(DSSDocument signedDocument) {
		SignedDocumentValidator validator = super.getValidator(signedDocument);
		validator.setCertificateVerifier(getCertificateVerifier());
		return validator;
	}
	
	private CertificateVerifier getCertificateVerifier() {
		CertificateVerifier offlineCertificateVerifier = getOfflineCertificateVerifier();
		CommonTrustedCertificateSource certSource = new CommonTrustedCertificateSource();
		certSource.addCertificate(trustAnchor1);
		certSource.addCertificate(trustAnchor2);
		certSource.addCertificate(trustAnchor3);
		certSource.addCertificate(trustAnchor4);
		offlineCertificateVerifier.setTrustedCertSources(certSource);
		return offlineCertificateVerifier;
	}
	
	@Override
	protected void checkTimestamps(DiagnosticData diagnosticData) {
		super.checkTimestamps(diagnosticData);
		
		SignatureWrapper signatureWrapper = diagnosticData.getSignatureById(diagnosticData.getFirstSignatureId());
		List<TimestampWrapper> signatureTimestamps = signatureWrapper.getSignatureTimestamps();
		assertEquals(1, signatureTimestamps.size());
		
		signatureTimestampId = signatureTimestamps.get(0).getId();
	}
	
	@Override
	protected void verifyDetailedReport(DetailedReport detailedReport) {
		super.verifyDetailedReport(detailedReport);
		
		XmlSignature xmlSignature = detailedReport.getXmlSignatureById(detailedReport.getFirstSignatureId());
		assertNotNull(xmlSignature);

		assertNotNull(xmlSignature.getValidationProcessLongTermData().getProofOfExistence());
		assertNotNull(xmlSignature.getValidationProcessLongTermData().getProofOfExistence().getTimestampId());
		assertEquals(signatureTimestampId, xmlSignature.getValidationProcessLongTermData().getProofOfExistence().getTimestampId());
		assertNotNull(xmlSignature.getValidationProcessArchivalData().getProofOfExistence());
		assertNotNull(xmlSignature.getValidationProcessArchivalData().getProofOfExistence().getTimestampId());
		assertEquals(signatureTimestampId, xmlSignature.getValidationProcessArchivalData().getProofOfExistence().getTimestampId());
	}
	
	@Override
	protected void verifyETSIValidationReport(ValidationReportType etsiValidationReportJaxb) {
		super.verifyETSIValidationReport(etsiValidationReportJaxb);
		
		List<SignatureValidationReportType> signatureValidationReports = etsiValidationReportJaxb
				.getSignatureValidationReport();
		assertEquals(1, signatureValidationReports.size());

		SignatureValidationReportType signatureValidationReportType = signatureValidationReports.get(0);
		assertNotNull(signatureValidationReportType);

		ValidationTimeInfoType validationTimeInfo = signatureValidationReportType.getValidationTimeInfo();
		assertNotNull(validationTimeInfo);
		assertNotNull(validationTimeInfo.getBestSignatureTime());
		assertNotNull(validationTimeInfo.getBestSignatureTime().getPOETime());
		assertNotNull(validationTimeInfo.getBestSignatureTime().getPOEObject());
		assertFalse(Utils.isCollectionEmpty(validationTimeInfo.getBestSignatureTime().getPOEObject().getVOReference()));
	}

}
