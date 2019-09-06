package eu.europa.esig.validationreport;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import eu.europa.esig.validationreport.jaxb.ValidationReportType;

public class ValidationReportFacadeTest {

	@Test
	public void unmarshallAndMarshall() throws IOException, JAXBException, XMLStreamException, SAXException {
		ValidationReportFacade facade = ValidationReportFacade.newFacade();
		ValidationReportType validationReportType = facade.unmarshall(new File("src/test/resources/vr.xml"));

		assertNotNull(validationReportType);
		assertFalse(validationReportType.getSignatureValidationObjects().getValidationObject().isEmpty());
		assertFalse(validationReportType.getSignatureValidationReport().isEmpty());
		assertNull(validationReportType.getSignature());

		String marshall = facade.marshall(validationReportType, true);
		assertNotNull(marshall);
	}

}
