package com.telus.credit.orderDepositCalculator.webservice.dto;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;


import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResultList;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResult;

/**
 * 
 * <p>
 * <b>Description: </b> DTO for order deposit calculation 
 * </p>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 *  @author T829939
 */


public class OrderDepositCalculationDto implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -554547365433793779L;
	/**
	 * 
	 */
	private long m_depositCalculationTrnID;
	
	/**
	 * 
	 */
	private long m_customerID;
	/**
	 * 
	 */
	private String m_orderID;
	/**
	 * 
	 */
	private String m_orderMasterSrcID;
	/**
	 * 
	 */
	private String m_decisionCD;
	/**
	 * 
	 */
	private Integer m_riskLevelNum;
	
	private String m_applicationID;
	/**
	 * 
	 */
	private String m_channelID;
	/**
	 * 
	 */
	private BigDecimal m_totalDepositPaidAmt;
	/**
	 * 
	 */
	private Date m_lastDepositPaidDate;
	/**
	 * 
	 */
	private BigDecimal m_totalDepositReleaseAmt;
	/**
	 * 
	 */
	private Date m_lastDepositReleaseDate;
	/**
	 * 
	 */
	private BigDecimal m_totalDepositPendingAmt;
	/**
	 * 
	 */
	private Date m_lastDepositPendingDate;
	/**
	 * 
	 */
	private BigDecimal m_depositAdjustmentAmt;
	/**
	 * 
	 */
	private BigDecimal m_depositOnHandAmt;
	/**
	 * 
	 */
	private BigDecimal m_totalAssessedDepositAmt;
	/**
	 * 
	 */
	private String m_calculationResultMsgCD;
	/**
	 * 
	 */
	private String m_calculationResultReasonCD;	
	/**
	 * 
	 */
	private String m_userID;
	/**
	 * 
	 */
	private ProductDepositResultList m_productDepositResultList;
	
	
	public OrderDepositCalculationDto()
	{
		
	 }
	
	
	/**
	 * @return the depositCalculationTrnID
	 */
	public long getDepositCalculationTrnID() {
		return m_depositCalculationTrnID;
	}


	/**
	 * @param depositCalculationTrnID the depositCalculationTrnID to set
	 */
	public void setDepositCalculationTrnID(long depositCalculationTrnID) {
		m_depositCalculationTrnID = depositCalculationTrnID;
	}


	/**
	 * @return the customerID
	 */
	public long getCustomerID() {
		return m_customerID;
	}
	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(long customerID) {
		m_customerID = customerID;
	}
	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return m_orderID;
	}
	/**
	 * @param orderID the orderID to set
	 */
	public void setOrderID(String orderID) {
		m_orderID = orderID;
	}
	/**
	 * @return the orderMasterSrcID
	 */
	public String getOrderMasterSrcID() {
		return m_orderMasterSrcID;
	}
	/**
	 * @param orderMasterSrcID the orderMasterSrcID to set
	 */
	public void setOrderMasterSrcID(String orderMasterSrcID) {
		m_orderMasterSrcID = orderMasterSrcID;
	}
	/**
	 * @return the decisionCD
	 */
	public String getDecisionCD() {
		return m_decisionCD;
	}
	/**
	 * @param decisionCD the decisionCD to set
	 */
	public void setDecisionCD(String decisionCD) {
		m_decisionCD = decisionCD;
	}
	/**
	 * @return the applicationID
	 */
	public String getApplicationID() {
		return m_applicationID;
	}
	/**
	 * @param applicationID the applicationID to set
	 */
	public void setApplicationID(String applicationID) {
		m_applicationID = applicationID;
	}
	/**
	 * @return the channelID
	 */
	public String getChannelID() {
		return m_channelID;
	}
	/**
	 * @param channelID the channelID to set
	 */
	public void setChannelID(String channelID) {
		m_channelID = channelID;
	}
	/**
	 * @return the totalDepositPaidAmt
	 */
	public BigDecimal getTotalDepositPaidAmt() {
		return m_totalDepositPaidAmt;
	}
	/**
	 * @param totalDepositPaidAmt the totalDepositPaidAmt to set
	 */
	public void setTotalDepositPaidAmt(BigDecimal totalDepositPaidAmt) {
		m_totalDepositPaidAmt = totalDepositPaidAmt;
	}
	/**
	 * @return the lastDepositPaidDate
	 */
	public Date getLastDepositPaidDate() {
		return m_lastDepositPaidDate;
	}
	/**
	 * @param lastDepositPaidDate the lastDepositPaidDate to set
	 */
	public void setLastDepositPaidDate(Date lastDepositPaidDate) {
		m_lastDepositPaidDate = lastDepositPaidDate;
	}
	/**
	 * @return the totalDepositReleaseAmt
	 */
	public BigDecimal getTotalDepositReleaseAmt() {
		return m_totalDepositReleaseAmt;
	}
	/**
	 * @param totalDepositReleaseAmt the totalDepositReleaseAmt to set
	 */
	public void setTotalDepositReleaseAmt(BigDecimal totalDepositReleaseAmt) {
		m_totalDepositReleaseAmt = totalDepositReleaseAmt;
	}
	/**
	 * @return the lastDepositReleaseDate
	 */
	public Date getLastDepositReleaseDate() {
		return m_lastDepositReleaseDate;
	}
	/**
	 * @param lastDepositReleaseDate the lastDepositReleaseDate to set
	 */
	public void setLastDepositReleaseDate(Date lastDepositReleaseDate) {
		m_lastDepositReleaseDate = lastDepositReleaseDate;
	}
	/**
	 * @return the totalDepositPendingAmt
	 */
	public BigDecimal getTotalDepositPendingAmt() {
		return m_totalDepositPendingAmt;
	}
	/**
	 * @param totalDepositPendingAmt the totalDepositPendingAmt to set
	 */
	public void setTotalDepositPendingAmt(BigDecimal totalDepositPendingAmt) {
		m_totalDepositPendingAmt = totalDepositPendingAmt;
	}
	/**
	 * @return the lastDepositPendingDate
	 */
	public Date getLastDepositPendingDate() {
		return m_lastDepositPendingDate;
	}
	/**
	 * @param lastDepositPendingDate the lastDepositPendingDate to set
	 */
	public void setLastDepositPendingDate(Date lastDepositPendingDate) {
		m_lastDepositPendingDate = lastDepositPendingDate;
	}
	/**
	 * @return the depositAdjustmentAmt
	 */
	public BigDecimal getDepositAdjustmentAmt() {
		return m_depositAdjustmentAmt;
	}
	/**
	 * @param depositAdjustmentAmt the depositAdjustmentAmt to set
	 */
	public void setDepositAdjustmentAmt(BigDecimal depositAdjustmentAmt) {
		m_depositAdjustmentAmt = depositAdjustmentAmt;
	}
	/**
	 * @return the depositOnHandAmt
	 */
	public BigDecimal getDepositOnHandAmt() {
		return m_depositOnHandAmt;
	}
	/**
	 * @param depositOnHandAmt the depositOnHandAmt to set
	 */
	public void setDepositOnHandAmt(BigDecimal depositOnHandAmt) {
		m_depositOnHandAmt = depositOnHandAmt;
	}
	/**
	 * @return the totalAssessedDepositAmt
	 */
	public BigDecimal getTotalAssessedDepositAmt() {
		return m_totalAssessedDepositAmt;
	}
	/**
	 * @param totalAssessedDepositAmt the totalAssessedDepositAmt to set
	 */
	public void setTotalAssessedDepositAmt(BigDecimal totalAssessedDepositAmt) {
		m_totalAssessedDepositAmt = totalAssessedDepositAmt;
	}
	/**
	 * @return the calculationResultMsgCD
	 */
	public String getCalculationResultMsgCD() {
		return m_calculationResultMsgCD;
	}
	/**
	 * @param calculationResultMsgCD the calculationResultMsgCD to set
	 */
	public void setCalculationResultMsgCD(String calculationResultMsgCD) {
		m_calculationResultMsgCD = calculationResultMsgCD;
	}
	/**
	 * @return the calculationResultReasonCD
	 */
	public String getCalculationResultReasonCD() {
		return m_calculationResultReasonCD;
	}
	/**
	 * @param calculationResultReasonCD the calculationResultReasonCD to set
	 */
	public void setCalculationResultReasonCD(String calculationResultReasonCD) {
		m_calculationResultReasonCD = calculationResultReasonCD;
	}
	/**
	 * @return the userID
	 */
	public String getUserID() {
		return m_userID;
	}
	/**
	 * @param userID the userID to set
	 */
	public void setUserID(String userID) {
		this.m_userID = userID;
	}
	
	/**
	 * @return the productDepositResultList
	 */
	public ProductDepositResultList getProductDepositResultList() {
		return m_productDepositResultList;
	}
	/**
	 * @param productDepositResultList the productDepositResultList to set
	 */
	public void setProductDepositResultList(
			ProductDepositResultList productDepositResultList) {
		m_productDepositResultList = productDepositResultList;
	}
	
	
	public Integer getRiskLevelNum() {
		return m_riskLevelNum;
	}


	public void setRiskLevelNum(Integer riskLevelNum) {
		this.m_riskLevelNum = riskLevelNum;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OrderDepositCalculationDto [m_customerID=" + m_customerID
				+ ", m_orderID=" + m_orderID + ", m_orderMasterSrcID="
				+ m_orderMasterSrcID + ", m_decisionCD=" + m_decisionCD
				+ " , m_riskLevelNum=" + m_riskLevelNum
				+ ", m_applicationID=" + m_applicationID + ", m_channelID="
				+ m_channelID + ", m_totalDepositPaidAmt="
				+ m_totalDepositPaidAmt + ", m_lastDepositPaidDate="
				+ m_lastDepositPaidDate + ", m_totalDepositReleaseAmt="
				+ m_totalDepositReleaseAmt + ", m_lastDepositReleaseDate="
				+ m_lastDepositReleaseDate + ", m_totalDepositPendingAmt="
				+ m_totalDepositPendingAmt + ", m_lastDepositPendingDate="
				+ m_lastDepositPendingDate + ", m_depositAdjustmentAmt="
				+ m_depositAdjustmentAmt + ", m_depositOnHandAmt="
				+ m_depositOnHandAmt + ", m_totalAssessedDepositAmt="
				+ m_totalAssessedDepositAmt + ", m_calculationResultMsgCD="
				+ m_calculationResultMsgCD + ", m_calculationResultReasonCD="
				+ m_calculationResultReasonCD + ",m_userID=" + m_userID
				+ ", m_productDepositResultList=" + m_productDepositResultList
				+ "]";
	}
	
	
	
 }
