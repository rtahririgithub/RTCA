/*package com.telus.credit.crda.domain.delegate.dcn;

import com.fico.telus.blaze.creditCommon.ProductCategory;

public class ProductCategoryWrapper extends ProductCategory {

	
	private ProductCategory  m_delegate;
	public ProductCategoryWrapper(){}
	public ProductCategoryWrapper(ProductCategory productCategory) {
		m_delegate=productCategory;
		
	this.categoryCd = m_delegate.getCategoryCd();
	this.qualified = m_delegate.isQualified();
	}

    protected String categoryCd;
    protected Boolean qualified;

    *//**
     * Gets the value of the categoryCd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     *//*
    public String getCategoryCd() {
        return m_delegate.getCategoryCd();
    }

    *//**
     * Sets the value of the categoryCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     *//*
    public void setCategoryCd(String value) {
        this.categoryCd = value;
    }

    *//**
     * Gets the value of the qualified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     *//*
    public Boolean isQualified() {
        return m_delegate.isQualified();
    }

    *//**
     * Sets the value of the qualified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     *//*
    public void setQualified(Boolean value) {
        this.qualified = value;
    }
 

}
*/