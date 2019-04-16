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

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;

import eu.europa.esig.dss.jaxb.detailedreport.XmlConstraint;
import eu.europa.esig.dss.jaxb.detailedreport.XmlStatus;
import eu.europa.esig.dss.jaxb.detailedreport.XmlSubXCV;
import eu.europa.esig.dss.jaxb.diagnostic.XmlCertificateRevocation;
import eu.europa.esig.dss.jaxb.diagnostic.XmlRevocation;
import eu.europa.esig.dss.validation.RevocationReason;
import eu.europa.esig.dss.validation.process.bbb.xcv.sub.checks.CertificateOnHoldCheck;
import eu.europa.esig.dss.validation.reports.wrapper.CertificateRevocationWrapper;
import eu.europa.esig.jaxb.policy.Level;
import eu.europa.esig.jaxb.policy.LevelConstraint;

public class CertificateOnHoldCheckTest {

	private static final Calendar CAL1 = DatatypeConverter.parseDate("2017-01-01");
	private static final Calendar CAL2 = DatatypeConverter.parseDate("2018-01-01");

	@Test
	public void certificateOnHoldCheck() throws Exception {
		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlCertificateRevocation xcr = new XmlCertificateRevocation();
		xcr.setStatus(true);
		XmlRevocation xr = new XmlRevocation();
		xcr.setRevocation(xr);

		XmlSubXCV result = new XmlSubXCV();
		CertificateOnHoldCheck cohc = new CertificateOnHoldCheck(result, new CertificateRevocationWrapper(xcr), CAL2.getTime(), constraint);
		cohc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.OK, constraints.get(0).getStatus());
	}

	@Test
	public void failedCertificateOnHoldCheck() throws Exception {
		LevelConstraint constraint = new LevelConstraint();
		constraint.setLevel(Level.FAIL);

		XmlCertificateRevocation xcr = new XmlCertificateRevocation();
		xcr.setStatus(false);
		xcr.setRevocationDate(CAL1.getTime());
		xcr.setReason(RevocationReason.certificateHold);
		XmlRevocation xr = new XmlRevocation();
		xcr.setRevocation(xr);

		XmlSubXCV result = new XmlSubXCV();
		CertificateOnHoldCheck cohc = new CertificateOnHoldCheck(result, new CertificateRevocationWrapper(xcr), CAL2.getTime(), constraint);
		cohc.execute();

		List<XmlConstraint> constraints = result.getConstraint();
		assertEquals(1, constraints.size());
		assertEquals(XmlStatus.NOT_OK, constraints.get(0).getStatus());
	}
}
