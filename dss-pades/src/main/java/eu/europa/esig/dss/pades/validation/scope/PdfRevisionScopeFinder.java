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
package eu.europa.esig.dss.pades.validation.scope;

import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.scope.SignatureScope;
import eu.europa.esig.dss.pades.PAdESUtils;
import eu.europa.esig.dss.pdf.PdfCMSRevision;
import eu.europa.esig.dss.spi.validation.scope.AbstractSignatureScopeFinder;

/**
 * An abstract class to find a PdfRevision scope
 *
 */
public abstract class PdfRevisionScopeFinder extends AbstractSignatureScopeFinder {

    /**
     * Default constructor
     */
    protected PdfRevisionScopeFinder() {
        // empty
    }

    /**
     * Finds signature scopes from a {@code PdfCMSRevision}
     *
     * @param pdfRevision {@link PdfCMSRevision}
     * @return {@link SignatureScope}
     */
    protected SignatureScope findSignatureScope(final PdfCMSRevision pdfRevision) {
        if (pdfRevision.areAllOriginalBytesCovered()) {
            return new FullPdfByteRangeSignatureScope(pdfRevision.getByteRange(), getOriginalPdfRevision(pdfRevision));
        } else {
            return new PartialPdfByteRangeSignatureScope(pdfRevision.getByteRange(), getOriginalPdfRevision(pdfRevision));
        }
    }

    private DSSDocument getOriginalPdfRevision(final PdfCMSRevision pdfRevision) {
        return PAdESUtils.getOriginalPDF(pdfRevision);
    }

}
