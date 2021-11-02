package eu.europa.esig.dss.validation.process.bbb.sav.checks;

import eu.europa.esig.dss.detailedreport.jaxb.XmlCC;
import eu.europa.esig.dss.detailedreport.jaxb.XmlConstraintsConclusion;
import eu.europa.esig.dss.i18n.I18nProvider;
import eu.europa.esig.dss.i18n.MessageTag;
import eu.europa.esig.dss.policy.jaxb.LevelConstraint;
import eu.europa.esig.dss.utils.Utils;
import eu.europa.esig.dss.validation.process.ValidationProcessUtils;

import java.util.Date;

/**
 * Class used to verify a DigestMatcher
 *
 * @param <T> {@code XmlConstraintsConclusion}
 */
public class DigestMatcherCryptographicCheckerResultCheck<T extends XmlConstraintsConclusion>
        extends DigestCryptographicCheckerResultCheck<T> {

    /** The verifying reference name */
    private final String referenceName;

    /**
     * Default constructor
     *
     * @param i18nProvider {@link I18nProvider}
     * @param result the result
     * @param validationDate {@link Date}
     * @param position {@link MessageTag}
     * @param referenceName {@link String}
     * @param ccResult {@link XmlCC}
     * @param constraint {@link LevelConstraint}
     */
    public DigestMatcherCryptographicCheckerResultCheck(I18nProvider i18nProvider, T result, Date validationDate,
                                                        MessageTag position, String referenceName,
                                                        XmlCC ccResult, LevelConstraint constraint) {
        super(i18nProvider, result, validationDate, position, ccResult, constraint);
        this.referenceName = referenceName;
    }

    @Override
    protected String buildAdditionalInfo() {
        String dateTime = ValidationProcessUtils.getFormattedDate(validationDate);
        if (isValid(ccResult)) {
            if (Utils.isStringNotEmpty(referenceName)) {
                return i18nProvider.getMessage(MessageTag.CRYPTOGRAPHIC_CHECK_SUCCESS_DM_WITH_NAME,
                        ccResult.getVerifiedAlgorithm().getName(), dateTime, position, referenceName);
            } else {
                return i18nProvider.getMessage(MessageTag.CRYPTOGRAPHIC_CHECK_SUCCESS_DM,
                        ccResult.getVerifiedAlgorithm().getName(), dateTime, position);
            }
        } else {
            if (Utils.isStringNotEmpty(referenceName)) {
                return i18nProvider.getMessage(MessageTag.CRYPTOGRAPHIC_CHECK_FAILURE_WITH_REF_WITH_NAME,
                        getErrorMessage(), referenceName, dateTime);
            } else {
                return i18nProvider.getMessage(MessageTag.CRYPTOGRAPHIC_CHECK_FAILURE_WITH_REF,
                        getErrorMessage(), dateTime);
            }
        }
    }

}
