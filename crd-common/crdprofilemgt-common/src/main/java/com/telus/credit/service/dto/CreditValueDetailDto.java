/**
 * 
 */
package com.telus.credit.service.dto;

import java.io.Serializable;

/**
 * @author x122365
 *
 */
public class CreditValueDetailDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7538234222129006479L;
	
	/**
	 * 
	 */
	private String m_fraudMessageCd;
	/**
	 * 
	 */
	private boolean m_productQualificationInd;
	/**
	 * Represents credit value detail type codes. Possible values are: <br>
	  * <ul>
	  * 	<li>"1" - Product Set Qualification</li>	
	  * 	<li>"2" - Fraud Warning</li>	
	  * </ul>
	 */
	private String m_creditValueDetailTypeCd;
	/**
	 * 
	 */
	private String m_creditApprvdProductCatCd;
	/**
	 * @return the m_fraudMessageCd
	 */
	public String getFraudMessageCd() {
		return m_fraudMessageCd;
	}
	/**
	 * @param m_fraudMessageCd the m_fraudMessageCd to set
	 */
	public void setFraudMessageCd(String m_fraudMessageCd) {
		this.m_fraudMessageCd = m_fraudMessageCd;
	}
	/**
	 * @return the m_productQualificationInd
	 */
	public boolean getProductQualificationInd() {
		return m_productQualificationInd;
	}
	/**
	 * @param m_productQualificationInd the m_productQualificationInd to set
	 */
	public void setProductQualificationInd(boolean m_productQualificationInd) {
		this.m_productQualificationInd = m_productQualificationInd;
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
	 * @return the m_creditApprvdProductCatCd
	 */
	public String getCreditApprvdProductCatCd() {
		return m_creditApprvdProductCatCd;
	}
	/**
	 * @param m_creditApprvdProductCatCd the m_creditApprvdProductCatCd to set
	 */
	public void setCreditApprvdProductCatCd(String m_creditApprvdProductCatCd) {
		this.m_creditApprvdProductCatCd = m_creditApprvdProductCatCd;
	}

}
