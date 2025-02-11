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
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.telus.credit.util.CreditMgtUtils;

/**
 * 
 * <p><b>Description:</b> Represents Credit Value bound to a Credit Profile </p>
 * <p><br><b>Revision History</b></p>
 * <table border="1" width="100%">
 * 	<tr>
 * 		<th width="15%">Date</th>
 * 		<th width="15%">Revised By</th>
 * 		<th width="55%">Description</th>
 * 		<th width="15%">Reviewed By</th>
 * 	</tr>
 * 	<tr>
 * 		<td width="15%">May 2, 2005</td>
 * 		<td width="15%">Roman Mikhailov</td>
 * 		<td width="55%">initial version</td>
 * 		<td width="15%">&nbsp;</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
*/ 
public class CreditValue implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 4229730322101984987L;

	public final static String INITIAL_CREDIT_VALUE_COMMENT = "System Default";

    public static final String VIP_CREDIT_VALUE_KEY = "V";
    
    public static final String ESTABLISHED_CREDIT_VALUE_KEY = "E";
    
    public static final String UN_ESTABLISHED_CREDIT_VALUE_KEY = "U";
    
    public static final String DEPOSIT_CREDIT_VALUE_KEY = "D";
    
    public static final String RESTRICTED_CREDIT_VALUE_KEY = "R";
    
    public static final String NO_CREDIT_INFO_CREDIT_VALUE_KEY = "N";
    
    public static final String GUARANTEED_CREDIT_VALUE_KEY = "G";
 
    
    /**
	 * Represents unique identifier for the object of this type, its "record id" in the database.
	 * It is used by the DAO to persist this object to the database. 
	 */
    private long m_id;
    
    
    
    private Timestamp m_lastUpdateTimestamp;
    
    
    /**
	 * Represents id of the CreditProfile object, which is a "parent" of the object of this type.
	 * It is used by the DAO to persist this object to the database. 
	 */
    private long m_creditProfileId;


    /**
	 * Represents credit value codes. Possible values are: <br>
	  * <ul>
	  * 	<li>"V" - VIP</li>	
	  * 	<li>"E" - Established</li>	
	  * 	<li>"U" - Un-established</li>	
	  * 	<li>"D" - Deposit</li>
	  * 	<li>"R" - Restricted</li>
	  * 	<li>"N" - No Credit Information</li>
	  * 	<li>"G" - Guaranteed</li>
	  * </ul>
	 */
	private String m_creditValueCode;
	
	/**
	 * Comment that explain why a certain credit value was assigned
	 */
	private String m_comment;
	
    /**
     * Method of creation of Credit Value in the Credit Management PDS. 
      * <ul>
	  * 	<li>"OO" - Online Override.</li>	
	  * 	<li>"BC" - Batch Conversion.</li>	
	  * 	<li>"BP" - Batch Process.</li>
	  *     <li>"UM" - UnMerge.</li>
	  * 	<li>"UN" - Unknown.</li>	
	  * 	<li>"NA" - Not Applicable. (will be inserted if m_method==null)</li>
	  * </ul>
	  * Default method  - "OO" (Online Override)
     */
	private String m_method;
	
	// Start of CreditWorthiness properties
	
	/**
	 * 
	 */
	private String m_createUserId;
	/**
	 * 
	 */
	private String m_lastUpdateUserId;
	/**
	 * 
	 */
	private long m_dataSourceId;
	/**
	 * 
	 */
	private Double m_depositAmount;
	/**
	 * 
	 */
	private String m_assessmentMsgCd;
	/**
	 *
	 */
	private boolean m_prodCategoryBoltOn;
	
	/**
	 * 
	 */
	private ProductCategoryQualification m_productCatQualification;
	
	/**
	 * 
	 */
	private String m_creditDecisionCd;
	/**
	 * 
	 */
	private String m_creditDecisionMsgTxt;
	/**
	 * 
	 */
	private String m_fraudIndicatorCd;
	/**
	 * 
	 */
	private String m_creditScoreNumber;
	/**
	 * 
	 */
	private String m_creditClassCd;
	/**
	 * 
	 */
	private Long m_carId;
	
	/**
	 * 
	 */
	private List m_fraudMessageCodeList;
	/**
	 * Represents credit value detail type codes. Possible values are: <br>
	  * <ul>
	  * 	<li>"1" - Product Category Code</li>	
	  * 	<li>"2" - Fraud Message Code</li>	
	  * </ul>
	 */
	private String m_creditValueDetailTypeCd;
	/**
	 * 
	 */
	private String m_creditAssessmentTypeCd;
	/**
	 * 
	 */
	private String m_creditAssessmentSubTypeCd;
	/**
	 * 
	 */
	private Timestamp m_creditAssessmentCreationDate;
	/**
	 * 
	 */
	private boolean m_creditBureauReportInd;
	/**
	 * 
	 */
	private String m_decisionCd;
	/**
	 * 
	 */
	private Integer m_riskLevelNum;
	
	// End of CreditWorthiness properties
	
	
	public CreditValue(){
	    m_method = CreditProfile.ONLINE_OVERRIDE_KEY;
	}
	
    /**
     * @return Returns the _id.
     */
    public long get_id() {
        return m_id;
    }
    /**
     * @param id The _id to set.
     */
    public void set_id(long id) {
        this.m_id = id;
    }
    /**
     * @return Returns the m_comment.
     */
    public String getComment() {
        return m_comment;
    }
    /**
     * @param comment The m_comment to set.
     */
    public void setComment(String comment) {
        this.m_comment = comment;
    }

    /**
     * @return Returns the m_creditValueCode.
     */
    public String getCreditValueCode() {
        return m_creditValueCode;
    }
    /**
     * @param valueCode The m_creditValueCode to set.
     */
    public void setCreditValueCode(String valueCode) {
        m_creditValueCode = valueCode;
    }
    
    /**
     * @return Returns the m_creditProfileId.
     */
    public long getCreditProfileId() {
        return m_creditProfileId;
    }
    /**
     * @param profileId The m_creditProfileId to set.
     */
    public void setCreditProfileId(long profileId) {
        m_creditProfileId = profileId;
    }
    
    
    /**
     * @return Returns the lastUpdateTimestamp.
     */
    public Timestamp getLastUpdateTimestamp()
    {
        return m_lastUpdateTimestamp;
    }
    /**
     * @param lastUpdateTimestamp The lastUpdateTimestamp to set.
     */
    public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp)
    {
        m_lastUpdateTimestamp = lastUpdateTimestamp;
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
	 * @return the m_createUserId
	 */
	public String getCreateUserId() {
		return m_createUserId;
	}

	/**
	 * @param m_createUserId the m_createUserId to set
	 */
	public void setCreateUserId(String m_createUserId) {
		this.m_createUserId = m_createUserId;
	}

	/**
	 * @return the m_lastUpdateUserId
	 */
	public String getLastUpdateUserId() {
		return m_lastUpdateUserId;
	}

	/**
	 * @param m_lastUpdateUserId the m_lastUpdateUserId to set
	 */
	public void setLastUpdateUserId(String m_lastUpdateUserId) {
		this.m_lastUpdateUserId = m_lastUpdateUserId;
	}

	/**
	 * @return the m_assessmentMsgCd
	 */
	public String getAssessmentMsgCd() {
		return m_assessmentMsgCd;
	}

	/**
	 * @param m_assessmentMsgCd the m_assessmentMsgCd to set
	 */
	public void setAssessmentMsgCd(String m_assessmentMsgCd) {
		this.m_assessmentMsgCd = m_assessmentMsgCd;
	}

	/**
	 * @return the m_prodCategoryBoltOn
	 */
	public boolean getProdCategoryBoltOn() {
		return m_prodCategoryBoltOn;
	}

	/**
	 * @param m_prodCategoryBoltOn the m_prodCategoryBoltOn to set
	 */
	public void setProdCategoryBoltOn(boolean m_prodCategoryBoltOn) {
		this.m_prodCategoryBoltOn = m_prodCategoryBoltOn;
	}

	/**
	 * @return the m_productCatQualification
	 */
	public ProductCategoryQualification getProductCatQualification() {
		return m_productCatQualification;
	}

	/**
	 * @param m_productCatQualification the m_productCatQualification to set
	 */
	public void setProductCatQualification(
			ProductCategoryQualification m_productCatQualification) {
		this.m_productCatQualification = m_productCatQualification;
	}

	/**
	 * @return the m_dataSourceId
	 */
	public long getDataSourceId() {
		return m_dataSourceId;
	}

	/**
	 * @param m_dataSourceId the m_dataSourceId to set
	 */
	public void setDataSourceId(long m_dataSourceId) {
		this.m_dataSourceId = m_dataSourceId;
	}

	/**
	 * @return the m_depositAmount
	 */
	public Double getDepositAmount() {
		return m_depositAmount;
	}

	/**
	 * @param m_depositAmount the m_depositAmount to set
	 */
	public void setDepositAmount(Double m_depositAmount) {
		this.m_depositAmount = m_depositAmount;
	}

	/**
	 * @return the m_creditDecisionCd
	 */
	public String getCreditDecisionCd() {
		return m_creditDecisionCd;
	}

	/**
	 * @param m_creditDecisionCd the m_creditDecisionCd to set
	 */
	public void setCreditDecisionCd(String m_creditDecisionCd) {
		this.m_creditDecisionCd = m_creditDecisionCd;
	}

	/**
	 * @return the m_fraudIndicatorCd
	 */
	public String getFraudIndicatorCd() {
		return m_fraudIndicatorCd;
	}

	/**
	 * @param m_fraudIndicatorCd the m_fraudIndicatorCd to set
	 */
	public void setFraudIndicatorCd(String m_fraudIndicatorCd) {
		this.m_fraudIndicatorCd = m_fraudIndicatorCd;
	}

	/**
	 * @return the m_creditDecisionMsgTxt
	 */
	public String getCreditDecisionMsgTxt() {
		return m_creditDecisionMsgTxt;
	}

	/**
	 * @param m_creditDecisionMsgTxt the m_creditDecisionMsgTxt to set
	 */
	public void setCreditDecisionMsgTxt(String m_creditDecisionMsgTxt) {
		this.m_creditDecisionMsgTxt = m_creditDecisionMsgTxt;
	}

	/**
	 * @return the m_creditScoreNumber
	 */
	public String getCreditScoreNumber() {
		return m_creditScoreNumber;
	}

	/**
	 * @param m_creditScoreNumber the m_creditScoreNumber to set
	 */
	public void setCreditScoreNumber(String m_creditScoreNumber) {
		this.m_creditScoreNumber = m_creditScoreNumber;
	}

	/**
	 * @return the m_creditClassCd
	 */
	public String getCreditClassCd() {
		return m_creditClassCd;
	}

	/**
	 * @param m_creditClassCd the m_creditClassCd to set
	 */
	public void setCreditClassCd(String m_creditClassCd) {
		this.m_creditClassCd = m_creditClassCd;
	}

	/**
	 * @return the m_carId
	 */
	public Long getCarId() {
		return m_carId;
	}

	/**
	 * @param m_carId the m_carId to set
	 */
	public void setCarId(Long m_carId) {
		this.m_carId = m_carId;
	}
	
	/**
	 * @return the m_fraudMessageCodeList
	 */
	public List getFraudMessageCodeList() {
		return m_fraudMessageCodeList;
	}

	/**
	 * @param m_fraudMessageCodeList the m_fraudMessageCodeList to set
	 */
	public void setFraudMessageCodeList(List m_fraudMessageCodeList) {
		this.m_fraudMessageCodeList = m_fraudMessageCodeList;
	}

	/**
	 * @return the m_creditValueDetailTypeCd
	 */
	public String getCreditValueDetailTypeCd() {
		return m_creditValueDetailTypeCd;
	}

	/**
	 * @param m_creditValueDetailTypeCd the m_creditValueDetailTypeCd to set
	 */
	public void setCreditValueDetailTypeCd(String m_creditValueDetailTypeCd) {
		this.m_creditValueDetailTypeCd = m_creditValueDetailTypeCd;
	}

	/**
	 * @return the m_creditAssessmentTypeCd
	 */
	public String getCreditAssessmentTypeCd() {
		return m_creditAssessmentTypeCd;
	}

	/**
	 * @param m_creditAssessmentTypeCd the m_creditAssessmentTypeCd to set
	 */
	public void setCreditAssessmentTypeCd(String m_creditAssessmentTypeCd) {
		this.m_creditAssessmentTypeCd = m_creditAssessmentTypeCd;
	}

	/**
	 * @return the m_creditAssessmentSubTypeCd
	 */
	public String getCreditAssessmentSubTypeCd() {
		return m_creditAssessmentSubTypeCd;
	}

	/**
	 * @param m_creditAssessmentSubTypeCd the m_creditAssessmentSubTypeCd to set
	 */
	public void setCreditAssessmentSubTypeCd(String m_creditAssessmentSubTypeCd) {
		this.m_creditAssessmentSubTypeCd = m_creditAssessmentSubTypeCd;
	}

	/**
	 * @return the m_creditAssessmentCreationDate
	 */
	public Timestamp getCreditAssessmentCreationDate() {
		return m_creditAssessmentCreationDate;
	}

	/**
	 * @param m_creditAssessmentCreationDate the m_creditAssessmentCreationDate to set
	 */
	public void setCreditAssessmentCreationDate(
			Timestamp m_creditAssessmentCreationDate) {
		this.m_creditAssessmentCreationDate = m_creditAssessmentCreationDate;
	}

	/**
	 * @return the m_creditBureauReportInd
	 */
	public boolean getCreditBureauReportInd() {
		return m_creditBureauReportInd;
	}

	/**
	 * @param m_creditBureauReportInd the m_creditBureauReportInd to set
	 */
	public void setCreditBureauReportInd(boolean m_creditBureauReportInd) {
		this.m_creditBureauReportInd = m_creditBureauReportInd;
	}
	
	/**
	 * @return the m_decisionCd
	 */
	public String getDecisionCd() {
		return m_decisionCd;
	}

	/**
	 * @param m_decisionCd the m_decisionCd to set
	 */
	public void setDecisionCd(String m_decisionCd) {
		this.m_decisionCd = m_decisionCd;
	}
	
	/**
	 * @return the m_riskLevelNum
	 */
	public Integer getRiskLevelNum() {
		return m_riskLevelNum;
	}

	/**
	 * @param m_riskLevelNum the m_riskLevelNum to set
	 */
	public void setRiskLevelNum(Integer riskLevelNum) {
		this.m_riskLevelNum = riskLevelNum;
	}

	/**
     * Compares two objects of this type.
     * @param o object to compare with this object.
     * @return true if <code>m_creditValueCode</code> and <code>m_comment</code> 
     * in both objects are equal; false otherwise.
     */
    public boolean equals(Object o)
    {
        if ( !(o instanceof CreditValue) ) {
            return false;
        }
        CreditValue cv = (CreditValue) o;
	if ( CreditMgtUtils.equals( this.m_fraudIndicatorCd, cv.m_fraudIndicatorCd )
	     && CreditMgtUtils.equals( this.m_creditValueCode, cv.getCreditValueCode() ) ) {
	    return true;
	}

        /**if ( CreditMgtUtils.equals( this.m_assessmentMsgCd, cv.getAssessmentMsgCd() )
	     && CreditMgtUtils.equals( this.m_prodCategoryBoltOn,cv.m_prodCategoryBoltOn )
	     && CreditMgtUtils.equals( this.m_productCatQualification, cv.m_productCatQualification )
	     && CreditMgtUtils.equals( this.m_creditDecisionCd, cv.m_creditDecisionCd )
	     && CreditMgtUtils.equals( this.m_creditDecisionMsgTxt, cv.m_creditDecisionMsgTxt )
	     && CreditMgtUtils.equals( this.m_fraudIndicatorCd, cv.m_fraudIndicatorCd )
	     && CreditMgtUtils.equals( this.m_creditScoreNumber, cv.m_creditScoreNumber )
	     && CreditMgtUtils.equals( this.m_creditClassCd, cv.m_creditClassCd )
	     && CreditMgtUtils.equals( this.m_carId, cv.m_carId )
	     && CreditMgtUtils.areListEquals( this.m_fraudMessageCodeList, cv.m_fraudMessageCodeList )
	     && CreditMgtUtils.equals( this.m_creditValueCode, cv.getCreditValueCode() ) ) {
            return true;
	    }**/
        return false;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("CreditValue { ");
        buf.append(" m_id: ").append(m_id);
        buf.append(" m_method: ").append(m_method);
        buf.append(", m_creditProfileId: ").append(m_creditProfileId);
        buf.append(", m_creditValueCode: ").append(m_creditValueCode);
        buf.append(", m_fraudIndicatorCd: ").append(m_fraudIndicatorCd);
        buf.append(", m_creditAssessmentTypeCd: ").append(m_creditAssessmentTypeCd);
        buf.append(", m_creditAssessmentSubTypeCd: ").append(m_creditAssessmentSubTypeCd);
        buf.append(", m_riskLevelNum: ").append(m_riskLevelNum);
        buf.append(", m_comment: ").append(m_comment);
        buf.append(", lastUpdateTimestamp: " ).append( m_lastUpdateTimestamp );
        buf.append(" }");
        return buf.toString();
    }
}
