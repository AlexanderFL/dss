package eu.europa.esig.dss.validation.process.art32.qualification;

import eu.europa.esig.dss.validation.SignatureQualification;

public final class QualificationMatrix {

	private QualificationMatrix() {
	}

	private static final int NOT_ADES = 0;
	private static final int ADES = 1;
	private static final int INDETERMINATE_ADES = 2;

	private static final int NOT_QC = 0;
	private static final int QC = 1;

	private static final int NOT_ESIG = 0; // eseal,... ?
	private static final int ESIG = 1;

	private static final int NOT_QSCD = 0;
	private static final int QSCD = 1;

	private static final SignatureQualification[][][][] QUALIFS = new SignatureQualification[3][2][2][2];

	static {

		// AdES

		QUALIFS[ADES][QC][ESIG][QSCD] = SignatureQualification.QESIG;
		QUALIFS[ADES][QC][NOT_ESIG][QSCD] = SignatureQualification.QES;

		QUALIFS[ADES][QC][ESIG][NOT_QSCD] = SignatureQualification.ADESIG_QC;
		QUALIFS[ADES][QC][NOT_ESIG][NOT_QSCD] = SignatureQualification.ADES_QC;

		QUALIFS[ADES][NOT_QC][ESIG][NOT_QSCD] = SignatureQualification.ADESIG;
		QUALIFS[ADES][NOT_QC][NOT_ESIG][NOT_QSCD] = SignatureQualification.ADES;

		QUALIFS[ADES][NOT_QC][ESIG][QSCD] = SignatureQualification.ADESIG;
		QUALIFS[ADES][NOT_QC][NOT_ESIG][QSCD] = SignatureQualification.ADES;

		// Indeterminate AdES

		QUALIFS[INDETERMINATE_ADES][QC][ESIG][QSCD] = SignatureQualification.INDETERMINATE_QESIG;
		QUALIFS[INDETERMINATE_ADES][QC][NOT_ESIG][QSCD] = SignatureQualification.INDETERMINATE_QES;

		QUALIFS[INDETERMINATE_ADES][QC][ESIG][NOT_QSCD] = SignatureQualification.INDETERMINATE_ADESIG_QC;
		QUALIFS[INDETERMINATE_ADES][QC][NOT_ESIG][NOT_QSCD] = SignatureQualification.INDETERMINATE_ADES_QC;

		QUALIFS[INDETERMINATE_ADES][NOT_QC][ESIG][NOT_QSCD] = SignatureQualification.INDETERMINATE_ADESIG;
		QUALIFS[INDETERMINATE_ADES][NOT_QC][NOT_ESIG][NOT_QSCD] = SignatureQualification.INDETERMINATE_ADES;

		QUALIFS[INDETERMINATE_ADES][NOT_QC][ESIG][QSCD] = SignatureQualification.INDETERMINATE_ADESIG;
		QUALIFS[INDETERMINATE_ADES][NOT_QC][NOT_ESIG][QSCD] = SignatureQualification.INDETERMINATE_ADES;

		// Not AdES

		QUALIFS[NOT_ADES][QC][ESIG][QSCD] = SignatureQualification.NOT_ADES_QC_QSCD;
		QUALIFS[NOT_ADES][QC][NOT_ESIG][QSCD] = SignatureQualification.NOT_ADES_QC_QSCD;

		QUALIFS[NOT_ADES][QC][ESIG][NOT_QSCD] = SignatureQualification.NOT_ADES_QC;
		QUALIFS[NOT_ADES][QC][NOT_ESIG][NOT_QSCD] = SignatureQualification.NOT_ADES_QC;

		QUALIFS[NOT_ADES][NOT_QC][ESIG][NOT_QSCD] = SignatureQualification.NOT_ADES;
		QUALIFS[NOT_ADES][NOT_QC][NOT_ESIG][NOT_QSCD] = SignatureQualification.NOT_ADES;

		QUALIFS[NOT_ADES][NOT_QC][ESIG][QSCD] = SignatureQualification.NOT_ADES;
		QUALIFS[NOT_ADES][NOT_QC][NOT_ESIG][QSCD] = SignatureQualification.NOT_ADES;

	}

	public static SignatureQualification getSignatureQualification(boolean ades, boolean qc, boolean esig, boolean qscd) {
		return QUALIFS[getInt(ades)][getInt(qc)][getInt(esig)][getInt(qscd)];
	}

	private static int getInt(boolean bool) {
		return bool ? 1 : 0;
	}
}
