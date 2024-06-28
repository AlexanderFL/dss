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
package eu.europa.esig.dss.validation.process.bbb.xcv.sub;

import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraint;
import eu.europa.esig.dss.detailedreport.jaxb.XmlStatus;
import eu.europa.esig.dss.detailedreport.jaxb.XmlSubXCV;
import eu.europa.esig.dss.diagnostic.CertificateWrapper;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificate;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificatePolicies;
import eu.europa.esig.dss.diagnostic.jaxb.XmlCertificatePolicy;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcCompliance;
import eu.europa.esig.dss.diagnostic.jaxb.XmlQcStatements;
import eu.europa.esig.dss.enumerations.CertificateExtensionEnum;
import eu.europa.esig.dss.enumerations.CertificatePolicy;
import eu.europa.esig.dss.policy.jaxb.Level;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.validation.process.bbb.AbstractTestCheck;
import eu.europa.esig.dss.validation.process.bbb.xcv.sub.checks.CertificateQcComplianceCheck;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateQcComplianceCheckTest extends AbstractTestCheck {

    @Test
    void validTest() throws Exception {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlQcCompliance xmlQcCompliance = new XmlQcCompliance();
        xmlQcCompliance.setPresent(true);
        xmlQcStatements.setQcCompliance(xmlQcCompliance);

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcComplianceCheck cqccc = new CertificateQcComplianceCheck(i18nProvider, result,
                new CertificateWrapper(xc), constraint);
        cqccc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
    }

    @Test
    void invalidTest() throws Exception {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        XmlQcCompliance xmlQcCompliance = new XmlQcCompliance();
        xmlQcCompliance.setPresent(false);
        xmlQcStatements.setQcCompliance(xmlQcCompliance);

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcComplianceCheck cqccc = new CertificateQcComplianceCheck(i18nProvider, result,
                new CertificateWrapper(xc), constraint);
        cqccc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void certificateQualifiedCheckWithCertificate() throws Exception {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCertificate xc = new XmlCertificate();
        XmlCertificatePolicies certificatePolicies = new XmlCertificatePolicies();
        certificatePolicies.setOID(CertificateExtensionEnum.CERTIFICATE_POLICIES.getOid());
        XmlCertificatePolicy oid = new XmlCertificatePolicy();
        oid.setValue(CertificatePolicy.QCP_PUBLIC.getOid());
        certificatePolicies.getCertificatePolicy().add(oid);
        xc.getCertificateExtensions().add(certificatePolicies);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcComplianceCheck cqccc = new CertificateQcComplianceCheck(i18nProvider, result,
                new CertificateWrapper(xc), constraint);
        cqccc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qcComplianceNotPresentTest() throws Exception {
        XmlQcStatements xmlQcStatements = new XmlQcStatements();
        xmlQcStatements.setOID(CertificateExtensionEnum.QC_STATEMENTS.getOid());

        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCertificate xc = new XmlCertificate();
        xc.getCertificateExtensions().add(xmlQcStatements);

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcComplianceCheck cqccc = new CertificateQcComplianceCheck(i18nProvider, result,
                new CertificateWrapper(xc), constraint);
        cqccc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

    @Test
    void qcStatementsNotPresentTest() throws Exception {
        LevelConstraint constraint = new LevelConstraint();
        constraint.setLevel(Level.FAIL);

        XmlCertificate xc = new XmlCertificate();

        XmlSubXCV result = new XmlSubXCV();
        CertificateQcComplianceCheck cqccc = new CertificateQcComplianceCheck(i18nProvider, result,
                new CertificateWrapper(xc), constraint);
        cqccc.execute();

        List<XmlConstraint> constraints = result.getConstraint();
        assertEquals(1, constraints.size());
        assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
    }

}
