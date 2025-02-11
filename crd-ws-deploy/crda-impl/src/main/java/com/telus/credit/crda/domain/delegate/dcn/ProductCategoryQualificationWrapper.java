/*package com.telus.credit.crda.domain.delegate.dcn;

import java.util.ArrayList;
import java.util.List;


import com.fico.telus.blaze.creditCommon.ProductCategory;
import com.fico.telus.blaze.creditCommon.ProductCategoryQualification;
 

public class ProductCategoryQualificationWrapper {
	 
	private ProductCategoryQualification  m_delegate;
	public ProductCategoryQualificationWrapper (){}
	public ProductCategoryQualificationWrapper (ProductCategoryQualification productCategoryQualification) {	
		m_delegate = productCategoryQualification;	
		productCategoryList = new ArrayList<ProductCategoryWrapper>();
		
		
		// convert ProductCategory
		List<ProductCategory> productCategoryListTmp = m_delegate.getProductCategoryList();
		
		if( productCategoryListTmp != null )
		{
			for( ProductCategory productCategory : productCategoryListTmp )
			{
				productCategoryList.add(new ProductCategoryWrapper(productCategory));
			}
		}	
		
		this.boltOnInd =m_delegate.isBoltOnInd();
	}
	
    protected List<ProductCategoryWrapper> productCategoryList;
    protected boolean boltOnInd;

    *//**
     * Gets the value of the productCategoryList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the productCategoryList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProductCategoryList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProductCategory }
     * 
     * 
     *//*

    public List<ProductCategoryWrapper> getProductCategoryList() {
        if (productCategoryList == null) {
            productCategoryList = new ArrayList<ProductCategoryWrapper>();
        }
        return this.productCategoryList;
    }
    

    *//**
     * Gets the value of the boltOnInd property.
     * 
     *//*
    public boolean isBoltOnInd() {
        return this.boltOnInd; 

    }

    *//**
     * Sets the value of the boltOnInd property.
     * 
     *//*
    public void setBoltOnInd(boolean value) {
        this.boltOnInd = value;
    }

 


 

}
*/