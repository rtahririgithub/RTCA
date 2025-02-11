
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
 * Collection Summary Data summarized at customer level.
 * 
 * <p>Java class for CustomerCollectionData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerCollectionData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="collectionInd" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="latestCollectionStartDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="latestCollectionEndDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="collectionScore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numberOfAccountsInAgency" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="accountsInAgencyBalance" type="{http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1}moneyType" minOccurs="0"/>
 *         &lt;element name="latestAgencyAssignmentDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="involuntaryCancelledAccounts" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="involuntaryCancelledAccountsBalance" type="{http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1}moneyType" minOccurs="0"/>
 *         &lt;element name="latestInvoluntaryCancelledDate" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
@XmlType(name = "CustomerCollectionData", namespace = "http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCollectionManagementTypes_v1", propOrder = {
    "collectionInd",
    "latestCollectionStartDate",
    "latestCollectionEndDate",
    "collectionScore",
    "numberOfAccountsInAgency",
    "accountsInAgencyBalance",
    "latestAgencyAssignmentDate",
    "involuntaryCancelledAccounts",
    "involuntaryCancelledAccountsBalance",
    "latestInvoluntaryCancelledDate",
    "numberOfNSFCheques"
})
public class CustomerCollectionData {

    protected Boolean collectionInd;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date latestCollectionStartDate;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date latestCollectionEndDate;
    protected String collectionScore;
    protected Integer numberOfAccountsInAgency;
    protected BigDecimal accountsInAgencyBalance;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date latestAgencyAssignmentDate;
    protected Integer involuntaryCancelledAccounts;
    protected BigDecimal involuntaryCancelledAccountsBalance;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "date")
    protected Date latestInvoluntaryCancelledDate;
    protected Integer numberOfNSFCheques;

    /**
     * Gets the value of the collectionInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCollectionInd() {
        return collectionInd;
    }

    /**
     * Sets the value of the collectionInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCollectionInd(Boolean value) {
        this.collectionInd = value;
    }

    /**
     * Gets the value of the latestCollectionStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getLatestCollectionStartDate() {
        return latestCollectionStartDate;
    }

    /**
     * Sets the value of the latestCollectionStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatestCollectionStartDate(Date value) {
        this.latestCollectionStartDate = value;
    }

    /**
     * Gets the value of the latestCollectionEndDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getLatestCollectionEndDate() {
        return latestCollectionEndDate;
    }

    /**
     * Sets the value of the latestCollectionEndDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatestCollectionEndDate(Date value) {
        this.latestCollectionEndDate = value;
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
     * Gets the value of the numberOfAccountsInAgency property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumberOfAccountsInAgency() {
        return numberOfAccountsInAgency;
    }

    /**
     * Sets the value of the numberOfAccountsInAgency property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumberOfAccountsInAgency(Integer value) {
        this.numberOfAccountsInAgency = value;
    }

    /**
     * Gets the value of the accountsInAgencyBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAccountsInAgencyBalance() {
        return accountsInAgencyBalance;
    }

    /**
     * Sets the value of the accountsInAgencyBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAccountsInAgencyBalance(BigDecimal value) {
        this.accountsInAgencyBalance = value;
    }

    /**
     * Gets the value of the latestAgencyAssignmentDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getLatestAgencyAssignmentDate() {
        return latestAgencyAssignmentDate;
    }

    /**
     * Sets the value of the latestAgencyAssignmentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatestAgencyAssignmentDate(Date value) {
        this.latestAgencyAssignmentDate = value;
    }

    /**
     * Gets the value of the involuntaryCancelledAccounts property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInvoluntaryCancelledAccounts() {
        return involuntaryCancelledAccounts;
    }

    /**
     * Sets the value of the involuntaryCancelledAccounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInvoluntaryCancelledAccounts(Integer value) {
        this.involuntaryCancelledAccounts = value;
    }

    /**
     * Gets the value of the involuntaryCancelledAccountsBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getInvoluntaryCancelledAccountsBalance() {
        return involuntaryCancelledAccountsBalance;
    }

    /**
     * Sets the value of the involuntaryCancelledAccountsBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setInvoluntaryCancelledAccountsBalance(BigDecimal value) {
        this.involuntaryCancelledAccountsBalance = value;
    }

    /**
     * Gets the value of the latestInvoluntaryCancelledDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getLatestInvoluntaryCancelledDate() {
        return latestInvoluntaryCancelledDate;
    }

    /**
     * Sets the value of the latestInvoluntaryCancelledDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatestInvoluntaryCancelledDate(Date value) {
        this.latestInvoluntaryCancelledDate = value;
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
