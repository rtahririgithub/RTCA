
package com.telus.collections.treatment.service.dto;

import java.math.BigDecimal;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.telus.framework.xml.bind.DateAdapter;


/**
 * Collection Summary Data per billing account level
 * 
 * <p>Java class for CollectionBillingAccountData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CollectionBillingAccountData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collectionIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="collectionStartDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="collectionEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="collectionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="involuntaryCeasedIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="collectionScore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="collectionScoreDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="agencyAssignmentCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="agencyAssignmentAmount" type="{http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1}moneyType" minOccurs="0"/>
 *         &lt;element name="agencyAssignmentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="accountStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accountStatusDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="accountBalance" type="{http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1}moneyType" minOccurs="0"/>
 *         &lt;element name="numberOfNSFCheques" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CollectionBillingAccountData", namespace = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1", propOrder = {
    "collectionIndicator",
    "collectionStartDate",
    "collectionEndDate",
    "collectionStatus",
    "involuntaryCeasedIndicator",
    "collectionScore",
    "collectionScoreDate",
    "agencyAssignmentCode",
    "agencyAssignmentAmount",
    "agencyAssignmentDate",
    "accountStatus",
    "accountStatusDate",
    "accountBalance",
    "numberOfNSFCheques"
})
public class CollectionBillingAccountData {

    protected Boolean collectionIndicator;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date collectionStartDate;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date collectionEndDate;
    protected String collectionStatus;
    protected Boolean involuntaryCeasedIndicator;
    protected String collectionScore;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date collectionScoreDate;
    protected String agencyAssignmentCode;
    protected BigDecimal agencyAssignmentAmount;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date agencyAssignmentDate;
    protected String accountStatus;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date accountStatusDate;
    protected BigDecimal accountBalance;
    protected Integer numberOfNSFCheques;

    /**
     * Gets the value of the collectionIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCollectionIndicator() {
        return collectionIndicator;
    }

    /**
     * Sets the value of the collectionIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCollectionIndicator(Boolean value) {
        this.collectionIndicator = value;
    }

    /**
     * Gets the value of the collectionStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getCollectionStartDate() {
        return collectionStartDate;
    }

    /**
     * Sets the value of the collectionStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionStartDate(Date value) {
        this.collectionStartDate = value;
    }

    /**
     * Gets the value of the collectionEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getCollectionEndDate() {
        return collectionEndDate;
    }

    /**
     * Sets the value of the collectionEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionEndDate(Date value) {
        this.collectionEndDate = value;
    }

    /**
     * Gets the value of the collectionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollectionStatus() {
        return collectionStatus;
    }

    /**
     * Sets the value of the collectionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionStatus(String value) {
        this.collectionStatus = value;
    }

    /**
     * Gets the value of the involuntaryCeasedIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isInvoluntaryCeasedIndicator() {
        return involuntaryCeasedIndicator;
    }

    /**
     * Sets the value of the involuntaryCeasedIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInvoluntaryCeasedIndicator(Boolean value) {
        this.involuntaryCeasedIndicator = value;
    }

    /**
     * Gets the value of the collectionScore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCollectionScore() {
        return collectionScore;
    }

    /**
     * Sets the value of the collectionScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionScore(String value) {
        this.collectionScore = value;
    }

    /**
     * Gets the value of the collectionScoreDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getCollectionScoreDate() {
        return collectionScoreDate;
    }

    /**
     * Sets the value of the collectionScoreDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCollectionScoreDate(Date value) {
        this.collectionScoreDate = value;
    }

    /**
     * Gets the value of the agencyAssignmentCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyAssignmentCode() {
        return agencyAssignmentCode;
    }

    /**
     * Sets the value of the agencyAssignmentCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyAssignmentCode(String value) {
        this.agencyAssignmentCode = value;
    }

    /**
     * Gets the value of the agencyAssignmentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAgencyAssignmentAmount() {
        return agencyAssignmentAmount;
    }

    /**
     * Sets the value of the agencyAssignmentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAgencyAssignmentAmount(BigDecimal value) {
        this.agencyAssignmentAmount = value;
    }

    /**
     * Gets the value of the agencyAssignmentDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getAgencyAssignmentDate() {
        return agencyAssignmentDate;
    }

    /**
     * Sets the value of the agencyAssignmentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyAssignmentDate(Date value) {
        this.agencyAssignmentDate = value;
    }

    /**
     * Gets the value of the accountStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountStatus() {
        return accountStatus;
    }

    /**
     * Sets the value of the accountStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountStatus(String value) {
        this.accountStatus = value;
    }

    /**
     * Gets the value of the accountStatusDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getAccountStatusDate() {
        return accountStatusDate;
    }

    /**
     * Sets the value of the accountStatusDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountStatusDate(Date value) {
        this.accountStatusDate = value;
    }

    /**
     * Gets the value of the accountBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    /**
     * Sets the value of the accountBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAccountBalance(BigDecimal value) {
        this.accountBalance = value;
    }

    /**
     * Gets the value of the numberOfNSFCheques property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfNSFCheques() {
        return numberOfNSFCheques;
    }

    /**
     * Sets the value of the numberOfNSFCheques property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfNSFCheques(Integer value) {
        this.numberOfNSFCheques = value;
    }

}
