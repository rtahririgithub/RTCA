package com.telus.credit.domain.deposit;



import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

public class DepositData {

    protected BigDecimal depositPaid;
    protected BigDecimal depositPending;
    protected BigDecimal depositReleased;
    protected XMLGregorianCalendar mostRecentDepositPaidDate;
    protected XMLGregorianCalendar mostRecentDepositPendingDate;
    protected XMLGregorianCalendar mostRecentDepositReleaseDate;

    /**
     * Gets the value of the depositPaid property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDepositPaid() {
        return depositPaid;
    }

    /**
     * Sets the value of the depositPaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDepositPaid(BigDecimal value) {
        this.depositPaid = value;
    }

    /**
     * Gets the value of the depositPending property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDepositPending() {
        return depositPending;
    }

    /**
     * Sets the value of the depositPending property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDepositPending(BigDecimal value) {
        this.depositPending = value;
    }

    /**
     * Gets the value of the depositReleased property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDepositReleased() {
        return depositReleased;
    }

    /**
     * Sets the value of the depositReleased property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDepositReleased(BigDecimal value) {
        this.depositReleased = value;
    }

    /**
     * Gets the value of the mostRecentDepositPaidDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMostRecentDepositPaidDate() {
        return mostRecentDepositPaidDate;
    }

    /**
     * Sets the value of the mostRecentDepositPaidDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMostRecentDepositPaidDate(XMLGregorianCalendar value) {
        this.mostRecentDepositPaidDate = value;
    }

    /**
     * Gets the value of the mostRecentDepositPendingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMostRecentDepositPendingDate() {
        return mostRecentDepositPendingDate;
    }

    /**
     * Sets the value of the mostRecentDepositPendingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMostRecentDepositPendingDate(XMLGregorianCalendar value) {
        this.mostRecentDepositPendingDate = value;
    }

    /**
     * Gets the value of the mostRecentDepositReleaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMostRecentDepositReleaseDate() {
        return mostRecentDepositReleaseDate;
    }

    /**
     * Sets the value of the mostRecentDepositReleaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMostRecentDepositReleaseDate(XMLGregorianCalendar value) {
        this.mostRecentDepositReleaseDate = value;
    }

}
