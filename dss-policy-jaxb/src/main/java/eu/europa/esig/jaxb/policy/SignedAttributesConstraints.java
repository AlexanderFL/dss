//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.08 at 09:21:49 AM CET 
//


package eu.europa.esig.jaxb.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SignedAttributesConstraints complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignedAttributesConstraints">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SigningTime" type="{http://dss.esig.europa.eu/validation/diagnostic}LevelConstraint" minOccurs="0"/>
 *         &lt;element name="ContentType" type="{http://dss.esig.europa.eu/validation/diagnostic}ValueConstraint" minOccurs="0"/>
 *         &lt;element name="ContentHints" type="{http://dss.esig.europa.eu/validation/diagnostic}ValueConstraint" minOccurs="0"/>
 *         &lt;element name="ContentIdentifier" type="{http://dss.esig.europa.eu/validation/diagnostic}ValueConstraint" minOccurs="0"/>
 *         &lt;element name="CommitmentTypeIndication" type="{http://dss.esig.europa.eu/validation/diagnostic}MultiValuesConstraint" minOccurs="0"/>
 *         &lt;element name="SignerLocation" type="{http://dss.esig.europa.eu/validation/diagnostic}LevelConstraint" minOccurs="0"/>
 *         &lt;element name="ClaimedRoles" type="{http://dss.esig.europa.eu/validation/diagnostic}MultiValuesConstraint" minOccurs="0"/>
 *         &lt;element name="CertifiedRoles" type="{http://dss.esig.europa.eu/validation/diagnostic}MultiValuesConstraint" minOccurs="0"/>
 *         &lt;element name="ContentTimeStamp" type="{http://dss.esig.europa.eu/validation/diagnostic}LevelConstraint" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignedAttributesConstraints", propOrder = {
    "signingTime",
    "contentType",
    "contentHints",
    "contentIdentifier",
    "commitmentTypeIndication",
    "signerLocation",
    "claimedRoles",
    "certifiedRoles",
    "contentTimeStamp"
})
public class SignedAttributesConstraints {

    @XmlElement(name = "SigningTime")
    protected LevelConstraint signingTime;
    @XmlElement(name = "ContentType")
    protected ValueConstraint contentType;
    @XmlElement(name = "ContentHints")
    protected ValueConstraint contentHints;
    @XmlElement(name = "ContentIdentifier")
    protected ValueConstraint contentIdentifier;
    @XmlElement(name = "CommitmentTypeIndication")
    protected MultiValuesConstraint commitmentTypeIndication;
    @XmlElement(name = "SignerLocation")
    protected LevelConstraint signerLocation;
    @XmlElement(name = "ClaimedRoles")
    protected MultiValuesConstraint claimedRoles;
    @XmlElement(name = "CertifiedRoles")
    protected MultiValuesConstraint certifiedRoles;
    @XmlElement(name = "ContentTimeStamp")
    protected LevelConstraint contentTimeStamp;

    /**
     * Gets the value of the signingTime property.
     * 
     * @return
     *     possible object is
     *     {@link LevelConstraint }
     *     
     */
    public LevelConstraint getSigningTime() {
        return signingTime;
    }

    /**
     * Sets the value of the signingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelConstraint }
     *     
     */
    public void setSigningTime(LevelConstraint value) {
        this.signingTime = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link ValueConstraint }
     *     
     */
    public ValueConstraint getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueConstraint }
     *     
     */
    public void setContentType(ValueConstraint value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the contentHints property.
     * 
     * @return
     *     possible object is
     *     {@link ValueConstraint }
     *     
     */
    public ValueConstraint getContentHints() {
        return contentHints;
    }

    /**
     * Sets the value of the contentHints property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueConstraint }
     *     
     */
    public void setContentHints(ValueConstraint value) {
        this.contentHints = value;
    }

    /**
     * Gets the value of the contentIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link ValueConstraint }
     *     
     */
    public ValueConstraint getContentIdentifier() {
        return contentIdentifier;
    }

    /**
     * Sets the value of the contentIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueConstraint }
     *     
     */
    public void setContentIdentifier(ValueConstraint value) {
        this.contentIdentifier = value;
    }

    /**
     * Gets the value of the commitmentTypeIndication property.
     * 
     * @return
     *     possible object is
     *     {@link MultiValuesConstraint }
     *     
     */
    public MultiValuesConstraint getCommitmentTypeIndication() {
        return commitmentTypeIndication;
    }

    /**
     * Sets the value of the commitmentTypeIndication property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiValuesConstraint }
     *     
     */
    public void setCommitmentTypeIndication(MultiValuesConstraint value) {
        this.commitmentTypeIndication = value;
    }

    /**
     * Gets the value of the signerLocation property.
     * 
     * @return
     *     possible object is
     *     {@link LevelConstraint }
     *     
     */
    public LevelConstraint getSignerLocation() {
        return signerLocation;
    }

    /**
     * Sets the value of the signerLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelConstraint }
     *     
     */
    public void setSignerLocation(LevelConstraint value) {
        this.signerLocation = value;
    }

    /**
     * Gets the value of the claimedRoles property.
     * 
     * @return
     *     possible object is
     *     {@link MultiValuesConstraint }
     *     
     */
    public MultiValuesConstraint getClaimedRoles() {
        return claimedRoles;
    }

    /**
     * Sets the value of the claimedRoles property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiValuesConstraint }
     *     
     */
    public void setClaimedRoles(MultiValuesConstraint value) {
        this.claimedRoles = value;
    }

    /**
     * Gets the value of the certifiedRoles property.
     * 
     * @return
     *     possible object is
     *     {@link MultiValuesConstraint }
     *     
     */
    public MultiValuesConstraint getCertifiedRoles() {
        return certifiedRoles;
    }

    /**
     * Sets the value of the certifiedRoles property.
     * 
     * @param value
     *     allowed object is
     *     {@link MultiValuesConstraint }
     *     
     */
    public void setCertifiedRoles(MultiValuesConstraint value) {
        this.certifiedRoles = value;
    }

    /**
     * Gets the value of the contentTimeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link LevelConstraint }
     *     
     */
    public LevelConstraint getContentTimeStamp() {
        return contentTimeStamp;
    }

    /**
     * Sets the value of the contentTimeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link LevelConstraint }
     *     
     */
    public void setContentTimeStamp(LevelConstraint value) {
        this.contentTimeStamp = value;
    }

}
