/**
 * 
 */
package com.telus.credit.domain;

import java.io.Serializable;

/**
 * @author x122365
 *
 */
public class ProductCategory implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3680360327698020581L;
	/**
	 * 
	 */
	private String m_creditApprovedProductCategoryCd;
	/**
	 * 
	 */
	private boolean m_productQualificationIndicator;
	/**
	 * @return the creditApprovedProductCategoryCd
	 */
	public String getCreditApprovedProductCategoryCd() {
		return m_creditApprovedProductCategoryCd;
	}
	/**
	 * @param creditApprovedProductCategoryCd the creditApprovedProductCategoryCd to set
	 */
	public void setCreditApprovedProductCategoryCd(
			String creditApprovedProductCategoryCd) {
		this.m_creditApprovedProductCategoryCd = creditApprovedProductCategoryCd;
	}
	/**
	 * @return the productQualificationIndicator
	 */
	public boolean getProductQualificationIndicator() {
		return m_productQualificationIndicator;
	}
	/**
	 * @param productQualificationIndicator the productQualificationIndicator to set
	 */
	public void setProductQualificationIndicator(
			boolean productQualificationIndicator) {
		this.m_productQualificationIndicator = productQualificationIndicator;
	}

}
