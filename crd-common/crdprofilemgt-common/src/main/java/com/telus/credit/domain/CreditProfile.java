/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *
 * <p>
 * <b>Description: </b> Represents Credit Profile of a customer
 * </p>
 * <br>
 * <b>Revision History </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">May 2, 2005</td>
 * <td width="15%">Roman Mikhailov</td>
 * <td width="55%">initial version</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 *
 * @author Roman Mikhailov
 *
 */
public class CreditProfile implements Serializable {

	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4552158720841501768L;

	//"primary-secondary" constants.
    //
    public static final String PRIMARY_KEY = "PRI";
    
    public static final String SECONDARY_KEY = "SEC";
    
    //"employment status" constants 
    //
    public static final String EMPLOYED_KEY = "EM";
    public static final String SELF_EMPLOYED_KEY = "SE";
    public static final String UNEMPLOYED_KEY = "UE";
    public static final String STUDENT_KEY = "ST";
    public static final String SOCIAL_ASSISTANCE_KEY = "SA";

    //"resident status" constants
    public static final String OWN_KEY = "O";
    public static final String RENT_KEY = "R";
    public static final String LIVING_WITH_PARENTS_KEY = "LP";

    //"primary credit card" constants
    public static final String VISA_KEY = "VS";
    public static final String MASTERCARD_KEY = "MC";
    public static final String AMEX_KEY = "AE";

    //"secondary credit card" constants
    public static final String DEPARTMENT_STORE_CARD_KEY = "DPT";
    public static final String FINANCIAL_INSTITUTION_CARD_KEY = "FI";
    public static final String GAS_CARD_KEY = "GAS";
    
    public static final String BATCH_CONVERSION_KEY = "BC";
    public static final String BATCH_PROCESSING_KEY = "BP";
    public static final String ONLINE_OVERRIDE_KEY = "OO";
    public static final String UNKNOWN_PROCESSING_KEY = "UN";
    public static final String NOT_APPLICABLE_PROCESSING_KEY = "UN";
    
    //Credit profile status
    /**
     * This status is assigned to "active" credit profile. 
     */
    public static final String ACTIVE_STATUS_KEY = "A";
    /**
     * "Consolidated" credit profile. This status is assigned to credit profile 
     * that is "expired" after merge of two credit profiles.
     */
    public static final String CONSOLIDATED_STATUS_KEY = "C";
    
    /**
     * "Expired" credit profile. This status is assigned to the credit profile 
     *  as a part of process of expiring customer. 
     */
    public static final String OBSOLETE_STATUS_KEY = "O";
    
    public static final String PERSON_KEY = "P";
    public static final String ORGANIZATION_KEY = "O";
    

    
    /** 
     * BillingAccount High Risk Indicators
     */
    public static final String RISK_INDICATOR_HIGH_1 = "H1";
    public static final String RISK_INDICATOR_HIGH_2 = "H2";

    /**
     * Cuarantor for the customer this credit profile belongs to. 
     * @link aggregationByValue
     * @supplierCardinality 0..1
     */
    private CustomerGuarantor m_customerGuarantor = null;

    /**
     * List of id cards that identify the owner of this credit profile. ID cards are :
     * SIN, Driver License, 
     * Passport Number, Provincial ID, and Healthcare Number)
     * @link aggregation <{com.telus.credit.domain.CreditIDCard}>
     * @directed directed
     * @supplierCardinality 0..*
     */
    private List m_creditIDCards = null;

    /**
     * Credit value that belong to this credit profile. 
     * @link aggregation <{com.telus.credit.domain.CreditValue}>
     * @directed directed
     * @supplierCardinality 0..*
     */
    private CreditValue m_creditValue = null;

    /**
     * Address for the customer who owns this credit profile.
     * @link aggregationByValue
     * @supplierCardinality 1
     */
    private CreditAddress m_creditAddress = null;

    
    /**
	 * Represents unique identifier for the object of this type, its "record id" in the database.
	 */
    private long m_id;
    
    


    /**
     * Employment status code. Possible values are:
      * <ul>
	  * 	<li>"EM" - Employee.</li>	
	  * 	<li>"SE" - Self-Employeed.</li>		
	  * 	<li>"UE" - Unemployeed.</li>
	  * 	<li>"ST" - Student.</li>
	  * 	<li>"SA" - Social Assistance.</li>
	  * 	<li>"NA" - Non Applicable.</li>		
	  * </ul>
     */
    private String m_employmentStatusCode;

    /**
     * Residency code. Possible values are:
      * <ul>
	  * 	<li>"O" - Own.</li>	
	  * 	<li>"R" - Rent.</li>		
	  * 	<li>"LP" - Living with Parents.</li>
	  * 	<li>"NA" - Non Applicable.</li>		
	  * </ul>
     */
    private String m_residencyCode;

    /**
     * Credit check consent code. Possible values are:
      * <ul>
	  * 	<li>"Y" - Yes.</li>	
	  * 	<li>"N" - No.</li>		
	  * 	<li>"NA" - Non Applicable.</li>		
	  * </ul>
     */
    private String m_creditCheckConsentCode;

    /**
     * Date of birth for the owner of this credit profile.
     */
    private Date m_birthdate;

    /**
     * Primary credit card code. Possible values are:
      * <ul>
	  * 	<li>"VS" - Visa.</li>	
	  * 	<li>"MC" - Mastercard.</li>		
	  * 	<li>"AM" - American Express.</li>	
	  * 	<li>"OT" - Other.</li>
	  * 	<li>"NA" - Non Applicable.</li>	
	  * </ul>
     */
    private String m_primaryCreditCardCode;

    /**
     * Secondary credit card code. Possible values are:
      * <ul>
	  * 	<li>"DPT" - Department Store Card.</li>	
	  * 	<li>"FI" - Financial Institution.</li>		
	  * 	<li>"GAS" - Gas</li>	
	  * 	<li>"OT" - Other.</li>
	  * 	<li>"NA" - Non Applicable.</li>	
	  * </ul>
     */
    private String m_secondaryCreditCardCode;

    /**
     * Code describing if the owner of the credit profile is under legal care. 
     * Possible values are:
      * <ul>
	  * 	<li>"Y" - Yes.</li>	
	  * 	<li>"N" - No.</li>		
	  * 	<li>"NA" - Non Applicable.</li>		
	  * </ul>
     */
    private String m_underLegalCareCode;
    
    
    /**
     * Status of credit profile in the Credit Management PDS:
      * <ul>
	  * 	<li>"A" - Active.</li>	
	  * 	<li>"C" - Consolidated.</li>			
	  * </ul>
	  * Default status - "A" (Active)
     */
    private String m_status;

    
    /**
     * m_businessLastUpdateTimestamp indicates the time of the 
     * last update of CreditProfile and/or any of its children
     * (CreditValue, CreditIDCard, CustomerGuarantor, and CreditAddress)
     */
    private Timestamp m_businessLastUpdateTimestamp;
    
    /**
     * Credit profile attributes
     */
    
    /**
     * Province of Current Resindecy. Possible values are:
      * <ul>
	  * 	<li>"AB" - Alberta.</li>
      * 	<li>"BC" - British Columbia.</li>	
      * 	<li>"ON" - Ontario.</li>	
	  * </ul>
     */
    private String m_provinceOfCurrentResidenceCd;
    
    /**
     * m_firstAssessmentDate is optional, it indicates
     * the very first Assessment Date
     */
    private Date m_firstAssessmentDate;
    
    /**
     * m_lastAssessmentDate is optional, it indicates
     * the very last Assessment Date
     */
    private Date m_latestAssessmentDate;
    
    /**
     * m_creditValueEffectiveDate is mandatory, if
     * credit value was changed
     */
    private Date m_creditValueEffectiveDate;
    
    /**
     * m_creditReportIndicator indicates
     * whether the customer has ever had a report pulled.
     */
    private boolean m_creditReportIndicator=false;
    // end of credit profile attributes
    
    /**
     * Format of credit profile in the Credit Management PDS. 
      * <ul>
	  * 	<li>"P" - Person.</li>	
	  * 	<li>"O" - Organization.</li>			
	  * </ul>
	  * Default format - "P" (Person)
     */
    private String m_format;
 
    /**
     * Method of creation of Credit Profile in the Credit Management PDS. 
      * <ul>
	  * 	<li>"OO" - Online Override.</li>	
	  * 	<li>"BC" - Batch Conversion.</li>	
	  * 	<li>"BP" - Batch Process.</li>	
	  * 	<li>"UN" - Unknown.</li>	
	  * 	<li>"NA" - Not Applicable. (will be inserted if m_method==null)</li>
	  * </ul>
	  * Default method  - "OO" (Online Override)
     */
    private String m_method;
    
    
    /**
     * added in RTCA
     * Province to identify which scorecard to use in bureau.
     */
    private String m_applicationProvinceCd;
    
    
    /**
     * added in RTCA
     * indicates user has decided to create a new customer even though the sys found matches
     */
    private boolean m_bypassMatchIndicator=false;
    
    /*
     * added in RTCA
     * credit profile level comments
     */
    private String m_comment;
    
    /**
     *Default Constructor.
     *    
     */
    public CreditProfile()
    {
        m_creditIDCards = new ArrayList();
        m_format = PERSON_KEY;
        m_method = ONLINE_OVERRIDE_KEY;
        m_status = ACTIVE_STATUS_KEY;
    }
    

    /**
     * @return Returns the _id.
     */
    public long get_id() {
        return m_id;
    }

    /**
     * @param id
     *            The _id to set.
     */
    public void set_id(long id) {
        this.m_id = id;
    }

    /**
     * @return Returns the creditAddress.
     */
    public CreditAddress getCreditAddress() {
        return m_creditAddress;
    }

    /**
     * <p><h4>Description</h4> Sets Credit Address.</p>
     * @param creditAddress The creditAddress to set.
     */
    public void setCreditAddress(CreditAddress creditAddress) {
        this.m_creditAddress = creditAddress;
    }

    /**
     * <p><h4>Description</h4> Adds new idCard without removing exesting IDCards</p>
     * @param idCard of type CreditIdCard
     */
    public void setCreditIDCard(CreditIDCard idCard) {
        m_creditIDCards.add(idCard);
    }

    /**
     * <p><h4>Description</h4> Replaces the Credit ID Cards in the Credit 
     * Profile.</p>
     * <p>
     * Use this method on an update when one or more ID cards have been deleted.  
     * If all cards have been deleted, null may be sent as an arguement.  
     * </p>
     * <p>To add additional cards, use {@link CreditProfile#setCreditIDCard}
     * </p>
     * @param creditIDCards array of ID Cards
     */
    public void setCreditIDCards(CreditIDCard[] creditIDCards) {
        m_creditIDCards = new ArrayList();
        
        if(creditIDCards!=null && creditIDCards.length!=0){
            for(int i = 0; i<creditIDCards.length; i++){
                m_creditIDCards.add(creditIDCards[i]);
            }
        }
    }
      
    /**
     * <p><h4>Description</h4> Gets array of CreditIDCard objects </p>
     * @return array of CreditIDCard objects
     */
    public CreditIDCard[] getCreditIDCards() {
        return (CreditIDCard[]) m_creditIDCards
                .toArray(new CreditIDCard[0]);
    }

    /**
     * <p><h4>Description</h4> Adds Credit Value  to Credit Profile</p>
     * @param creditValue of type CreditValue
     */
    public void setCreditValue(CreditValue creditValue) {
        m_creditValue = creditValue;
    }

    /**
     * <p><h4>Description</h4> Gets CreditValue object.</p>
     * @return CreditValue object.
     */
    public CreditValue getCreditValue() {
        return m_creditValue;
    }

    /**
     * @return Returns the m_birthdate.
     */
    public Date getBirthdate() {
        return m_birthdate;
    }

    /**
     * @param birthdate
     *            The m_birthdate to set.
     */
    public void setBirthdate(Date birthdate) {
        this.m_birthdate = birthdate;
    }

    /**
     * @return Returns the m_creditCheckConsentCode.
     */
    public String getCreditCheckConsentCode() {
        return m_creditCheckConsentCode;
    }

    /**
     * @param checkConsentCode
     *            The m_creditCheckConsentCode to set.
     */
    public void setCreditCheckConsentCode(String checkConsentCode) {
        m_creditCheckConsentCode = checkConsentCode;
    }

    /**
     * @return Returns the m_employmentStatusCode.
     */
    public String getEmploymentStatusCode() {
        return m_employmentStatusCode;
    }

    /**
     * @param statusCode
     *            The m_employmentStatusCode to set.
     */
    public void setEmploymentStatusCode(String statusCode) {
        m_employmentStatusCode = statusCode;
    }

    /**
     * @return Returns the m_primaryCreditCardCode.
     */
    public String getPrimaryCreditCardCode() {
        return m_primaryCreditCardCode;
    }

    /**
     * @param creditCardCode
     *            The m_primaryCreditCardCode to set.
     */
    public void setPrimaryCreditCardCode(String creditCardCode) {
        m_primaryCreditCardCode = creditCardCode;
    }

    /**
     * @return Returns the m_residencyCode.
     */
    public String getResidencyCode() {
        return m_residencyCode;
    }

    /**
     * @param code
     *            The m_residencyCode to set.
     */
    public void setResidencyCode(String code) {
        m_residencyCode = code;
    }

    /**
	 * @return the m_provinceCurrentResidencyCode
	 */
	public String getProvinceOfCurrentResidenceCd() {
		return m_provinceOfCurrentResidenceCd;
	}


	/**
	 * @param m_provinceCurrentResidencyCode the m_provinceCurrentResidencyCode to set
	 */
	public void setProvinceOfCurrentResidenceCd(
			String m_provinceOfCurrentResidenceCd) {
		this.m_provinceOfCurrentResidenceCd = m_provinceOfCurrentResidenceCd;
	}


	/**
	 * @return the m_firstAssessmentDate
	 */
	public Date getFirstAssessmentDate() {
		return m_firstAssessmentDate;
	}


	/**
	 * @param m_firstAssessmentDate the m_firstAssessmentDate to set
	 */
	public void setFirstAssessmentDate(Date m_firstAssessmentDate) {
		this.m_firstAssessmentDate = m_firstAssessmentDate;
	}


	/**
	 * @return the m_lastAssessmentDate
	 */
	public Date getLatestAssessmentDate() {
		return m_latestAssessmentDate;
	}


	/**
	 * @param m_lastAssessmentDate the m_lastAssessmentDate to set
	 */
	public void setLatestAssessmentDate(Date m_lastAssessmentDate) {
		this.m_latestAssessmentDate = m_lastAssessmentDate;
	}


	/**
	 * @return the m_creditValueEffectiveDate
	 */
	public Date getCreditValueEffectiveDate() {
		return m_creditValueEffectiveDate;
	}


	/**
	 * @param m_creditValueEffectiveDate the m_creditValueEffectiveDate to set
	 */
	public void setCreditValueEffectiveDate(Date m_creditValueEffectiveDate) {
		this.m_creditValueEffectiveDate = m_creditValueEffectiveDate;
	}


	


	/**
	 * @return the m_creditReportIndicator
	 */
	public boolean isCreditReportIndicator() {
		return m_creditReportIndicator;
	}


	/**
	 * @param m_creditReportIndicator the m_creditReportIndicator to set
	 */
	public void setCreditReportIndicator(boolean m_creditReportIndicator) {
		this.m_creditReportIndicator = m_creditReportIndicator;
	}


	/**
     * @return Returns the m_secondaryCreditCardCode.
     */
    public String getSecondaryCreditCardCode() {
        return m_secondaryCreditCardCode;
    }

    /**
     * @param creditCardCode
     *            The m_secondaryCreditCardCode to set.
     */
    public void setSecondaryCreditCardCode(String creditCardCode) {
        m_secondaryCreditCardCode = creditCardCode;
    }

    /**
     * @return Returns the m_underLegalCareCode.
     */
    public String getUnderLegalCareCode() {
        return m_underLegalCareCode;
    }

    /**
     * @param legalCareCode
     *            The m_underLegalCareCode to set.
     */
    public void setUnderLegalCareCode(String legalCareCode) {
        m_underLegalCareCode = legalCareCode;
    }
    
    /**
     * @return Returns the m_customerGuarantor.
     */
    public CustomerGuarantor getCustomerGuarantor() {
        return m_customerGuarantor;
    }
    /**
     * @param guarantor The m_customerGuarantor to set.
     */
    public void setCustomerGuarantor(CustomerGuarantor guarantor) {
        m_customerGuarantor = guarantor;
    }
    
    /**
     * 
     * <p><b>Description</b> Sets status for credit profile.</p>
     * @param status of the credit profile.
     */
    public void setStatus(String status){
        m_status = status;
    }
    
    /**
     * <p><b>Description</b> Gets credit profile status.</p>
     * @return String status.  
     */
    public String getStatus(){
        return m_status;
    }
    
    /**
     * @return Returns the businessLastUpdateTimestamp.
     */
    public Timestamp getBusinessLastUpdateTimestamp()
    {
        return m_businessLastUpdateTimestamp;
    }
    /**
     * @param businessLastUpdateTimestamp to set.
     */
    public void setBusinessLastUpdateTimestamp(Timestamp businessLastUpdateTimestamp)
    {
        m_businessLastUpdateTimestamp = businessLastUpdateTimestamp;
    }
    
    /**
     * @return Returns the format.
     */
    public String getFormat()
    {
        return m_format;
    }
    /**
     * @param format The format to set.
     */
    public void setFormat(String format)
    {
        m_format = format;
    }
    /**
     * @return Returns the method.
     */
    public String getMethod()
    {
        return m_method;
    }
    /**
     * @param method The method to set.
     */
    public void setMethod(String method)
    {
        m_method = method;
    }
    


	/**
	 * @return the applicationProvinceCd
	 */
	public String getApplicationProvinceCd() {
		return m_applicationProvinceCd;
	}


	/**
	 * @param applicationProvinceCd the applicationProvinceCd to set
	 */
	public void setApplicationProvinceCd(String applicationProvinceCd) {
		m_applicationProvinceCd = applicationProvinceCd;
	}


	/**
	 * @return the bypassMatchIndicator
	 */
	public boolean isBypassMatchIndicator() {
		return m_bypassMatchIndicator;
	}


	/**
	 * @param bypassMatchIndicator the bypassMatchIndicator to set
	 */
	public void setBypassMatchIndicator(boolean bypassMatchIndicator) {
		m_bypassMatchIndicator = bypassMatchIndicator;
	}


	/**
	 * @return the comment
	 */
	public String getComment() {
		return m_comment;
	}


	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		m_comment = comment;
	}


	public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("CreditProfile { ");
        buf.append(" m_id: ").append(m_id);
        buf.append(" m_format: ").append(m_format);
        buf.append(" m_method: ").append(m_method);
        buf.append(" m_status: ").append(m_status);
        buf.append(", m_birthdate: ").append(m_birthdate);
        buf.append(", m_businessLastUpdateTimestamp: ").append(m_businessLastUpdateTimestamp);
        buf.append(", m_creditCheckConsentCode: ").append(
                m_creditCheckConsentCode);
        buf.append(", m_employmentStatusCode: ").append(m_employmentStatusCode);
        buf.append(", m_primaryCreditCardCode: ").append(
                m_primaryCreditCardCode);
        buf.append(", m_residencyCode: ").append(m_residencyCode);
        buf.append(", m_secondaryCreditCardCode: ").append(
                m_secondaryCreditCardCode);
        buf.append(", m_underLegalCareCode: ").append(m_underLegalCareCode);
        buf.append(", m_applicationProvinceCd: ").append(m_applicationProvinceCd);
        buf.append(", m_bypassMatchIndicator: ").append(m_bypassMatchIndicator);
        buf.append(", m_comment: ").append(m_comment);
        buf.append(", m_provinceOfCurrentResidenceCd: ").append(m_provinceOfCurrentResidenceCd);
        buf.append("\n").append(m_creditAddress);
        buf.append("\n").append(m_customerGuarantor);
        for (Iterator it = m_creditIDCards.iterator(); it.hasNext();) {
            CreditIDCard id = (CreditIDCard) it.next();
            buf.append("\n").append(id);
        }
        buf.append("\n").append(m_creditValue);
        buf.append(" }");
        return buf.toString();
    }
}
