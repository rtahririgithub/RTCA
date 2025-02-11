package com.telus.credit.domain.deposit;


import java.util.Date;


public class DepositItem {

    protected long depositID;
    protected Date dueDate;
    protected String depositDesignationID;
    protected double requestAmount;
    protected Date requestDate;
    protected String requestReasonCd;
    protected String requestReasonTxt;
    protected double paidAmount;
    protected Date paidDate;
    protected double cancelledAmount;
    protected Date cancelDate;
    
    protected String cancelReasonCd;
    
    protected String cancelReasonTxt;
    protected double releasedAmount;

    
    protected Date releaseDate;
    
    protected String releaseReasonCd;
    
    protected String releaseReasonTxt;
    
    protected String releaseMethodCd;
    
    protected String releaseMethodTxt;
    protected double interestAmount;
    protected long accountID;

    /**
     * Gets the value of the depositID property.
     * 
     */
    public long getDepositID() {
        return depositID;
    }

    /**
     * Sets the value of the depositID property.
     * 
     */
    public void setDepositID(long value) {
        this.depositID = value;
    }

    /**
     * Gets the value of the dueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Sets the value of the dueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDueDate(Date value) {
        this.dueDate = value;
    }

    /**
     * Gets the value of the depositDesignationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDepositDesignationID() {
        return depositDesignationID;
    }

    /**
     * Sets the value of the depositDesignationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDepositDesignationID(String value) {
        this.depositDesignationID = value;
    }

    /**
     * Gets the value of the requestAmount property.
     * 
     */
    public double getRequestAmount() {
        return requestAmount;
    }

    /**
     * Sets the value of the requestAmount property.
     * 
     */
    public void setRequestAmount(double value) {
        this.requestAmount = value;
    }

    /**
     * Gets the value of the requestDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the value of the requestDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestDate(Date value) {
        this.requestDate = value;
    }

    /**
     * Gets the value of the requestReasonCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestReasonCd() {
        return requestReasonCd;
    }

    /**
     * Sets the value of the requestReasonCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestReasonCd(String value) {
        this.requestReasonCd = value;
    }

    /**
     * Gets the value of the requestReasonTxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestReasonTxt() {
        return requestReasonTxt;
    }

    /**
     * Sets the value of the requestReasonTxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestReasonTxt(String value) {
        this.requestReasonTxt = value;
    }

    /**
     * Gets the value of the paidAmount property.
     * 
     */
    public double getPaidAmount() {
        return paidAmount;
    }

    /**
     * Sets the value of the paidAmount property.
     * 
     */
    public void setPaidAmount(double value) {
        this.paidAmount = value;
    }

    /**
     * Gets the value of the paidDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getPaidDate() {
        return paidDate;
    }

    /**
     * Sets the value of the paidDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaidDate(Date value) {
        this.paidDate = value;
    }

    /**
     * Gets the value of the cancelledAmount property.
     * 
     */
    public double getCancelledAmount() {
        return cancelledAmount;
    }

    /**
     * Sets the value of the cancelledAmount property.
     * 
     */
    public void setCancelledAmount(double value) {
        this.cancelledAmount = value;
    }

    /**
     * Gets the value of the cancelDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getCancelDate() {
        return cancelDate;
    }

    /**
     * Sets the value of the cancelDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelDate(Date value) {
        this.cancelDate = value;
    }

    /**
     * Gets the value of the cancelReasonCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelReasonCd() {
        return cancelReasonCd;
    }

    /**
     * Sets the value of the cancelReasonCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelReasonCd(String value) {
        this.cancelReasonCd = value;
    }

    /**
     * Gets the value of the cancelReasonTxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCancelReasonTxt() {
        return cancelReasonTxt;
    }

    /**
     * Sets the value of the cancelReasonTxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCancelReasonTxt(String value) {
        this.cancelReasonTxt = value;
    }

    /**
     * Gets the value of the releasedAmount property.
     * 
     */
    public double getReleasedAmount() {
        return releasedAmount;
    }

    /**
     * Sets the value of the releasedAmount property.
     * 
     */
    public void setReleasedAmount(double value) {
        this.releasedAmount = value;
    }

    /**
     * Gets the value of the releaseDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getReleaseDate() {
        return releaseDate;
    }

    /**
     * Sets the value of the releaseDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseDate(Date value) {
        this.releaseDate = value;
    }

    /**
     * Gets the value of the releaseReasonCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseReasonCd() {
        return releaseReasonCd;
    }

    /**
     * Sets the value of the releaseReasonCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseReasonCd(String value) {
        this.releaseReasonCd = value;
    }

    /**
     * Gets the value of the releaseReasonTxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseReasonTxt() {
        return releaseReasonTxt;
    }

    /**
     * Sets the value of the releaseReasonTxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseReasonTxt(String value) {
        this.releaseReasonTxt = value;
    }

    /**
     * Gets the value of the releaseMethodCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseMethodCd() {
        return releaseMethodCd;
    }

    /**
     * Sets the value of the releaseMethodCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseMethodCd(String value) {
        this.releaseMethodCd = value;
    }

    /**
     * Gets the value of the releaseMethodTxt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReleaseMethodTxt() {
        return releaseMethodTxt;
    }

    /**
     * Sets the value of the releaseMethodTxt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReleaseMethodTxt(String value) {
        this.releaseMethodTxt = value;
    }

    /**
     * Gets the value of the interestAmount property.
     * 
     */
    public double getInterestAmount() {
        return interestAmount;
    }

    /**
     * Sets the value of the interestAmount property.
     * 
     */
    public void setInterestAmount(double value) {
        this.interestAmount = value;
    }

    /**
     * Gets the value of the accountID property.
     * 
     */
    public long getAccountID() {
        return accountID;
    }

    /**
     * Sets the value of the accountID property.
     * 
     */
    public void setAccountID(long value) {
        this.accountID = value;
    }

}
