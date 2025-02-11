/**
 * 
 */
package com.telus.credit.domain;

import java.io.Serializable;
import java.util.List;

import com.telus.credit.util.CreditMgtUtils;

/**
 * @author x122365
 *
 */
public class ProductCategoryQualification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2276132615152243721L;
	/**
	 * 
	 */
	private boolean m_boltOn;
	/**
	 * 
	 */
	private List m_productCategoryList;
	/**
	 * @return the boltOn
	 */
	public boolean getBoltOn() {
		return m_boltOn;
	}
	/**
	 * @param boltOn the boltOn to set
	 */
	public void setBoltOn(boolean boltOn) {
		this.m_boltOn = boltOn;
	}
	/**
	 * @return the productCategoryList
	 */
	public List getProductCategoryList() {
		return m_productCategoryList;
	}
	/**
	 * @param productCategoryList the productCategoryList to set
	 */
	public void setProductCategoryList(List productCategoryList) {
		this.m_productCategoryList = productCategoryList;
	}

    /**
     * Compares two objects of this type.
     * @param o object to compare with this object.
     * @return true if <code>m_creditValueCode</code> and <code>m_comment</code> 
     * in both objects are equal; false otherwise.
     */
    public boolean equals(Object o)
    {
	if ( !(o instanceof ProductCategoryQualification) ) {
            return false;
        }
        ProductCategoryQualification pq = (ProductCategoryQualification) o;
	if ( CreditMgtUtils.areEqual( new Boolean(this.m_boltOn), new Boolean(pq.m_boltOn) )
	     && CreditMgtUtils.areListEquals( this.m_productCategoryList, pq.m_productCategoryList ) ) {
	    return true;
	}
	return false;
    }
}
