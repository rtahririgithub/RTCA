/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */
package com.telus.credit.orderDepositCalculator.webservice.dto;

import java.util.Iterator;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;

import com.telus.credit.orderDepositCalculator.common.domain.AssignedOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.AssignedProduct;
import com.telus.credit.orderDepositCalculator.common.domain.BaseProduct;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentProduct;
//import com.telus.credit.orderDepositCalculator.common.domain.DepositItem;
import com.telus.credit.orderDepositCalculator.common.domain.OrderDataDetail;
import com.telus.credit.orderDepositCalculator.common.domain.PendingOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.PendingProduct;
import com.telus.credit.orderDepositCalculator.webservice.impl.OrderDepositCalculatorConst;
import com.telus.framework.math.Money;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;

public class OrderDataBr {
	
	public static final String CALCULATION_TYPE_CD_IS_NULL="CALCULATION_TYPE_CD_IS_NULL";
	
	public static final String CALCULATION_TYPE_CD_IS_INVALID="CALCULATION_TYPE_CD_IS_INVALID";
	
	public static final String CUSTOMERID_IS_NULL="OrderDataBr.CUSTOMERID_IS_NULL";
	
	public static final String ORDERID_IS_NULL="OrderDataBr.ORDERID_IS_NULL";
	
	public static final String ORDERID_SRC_SYSTEM_CD_IS_NULL="OrderDataBr.ORDERID_SRC_SYSTEM_CD_IS_NULL";
	
	public static final String CURRENT_ORDER_PRODUCT_LIST_IS_NULL = "OrderDataBr.CURRENT_ORDER_PRODUCT_LIST_IS_NULL";
	
	public static final String CURRENT_ORDER_PRODUCT_LIST_IS_EMPTY = "OrderDataBr.CURRENT_ORDER_PRODUCT_LIST_IS_EMPTY";
    
    public static final String CURRENT_ORDER_PRODUCT_LABLE="CURRENT_ORDER_PRODUCT.";
    
    public static final String PENDING_ORDER_PRODUCT_LABLE="PENDING_ORDER_PRODUCT.";
    
    public static final String ASSIGNED_ORDER_PRODUCT_LABLE="ASSIGNED_ORDER_PRODUCT.";
    
    public static final String SERVICE_TYPE_IS_NULL = "SERVICE_TYPE_IS_NULL";
    
    public static final String ASSIGNED_PRODUCT_ID_IS_NULL = "ASSIGNED_PRODUCT_ID_IS_NULL";
    
    public static final String ASSIGNED_PRODUCT_ID_SRC_SYSTEM_CD_IS_NULL = "ASSIGNED_PRODUCT_ID_SRC_SYSTEM_CD_IS_NULL";
    
    public static final String RENTED_EQUIPMENT_CNT_IS_NULL = "RENTED_EQUIPMENT_CNT_IS_NULL";
    
    public static final String PURCHASED_EQUIPMENT_CNT_IS_NULL = "PURCHASED_EQUIPMENT_CNT_IS_NULL";
    
    public static final String OFFER_NAME_CD_IS_NULL = "OFFER_NAME_CD_IS_NULL";
    
    public static final String TOTAL_RC_IS_NULL = "CurrentOrderProuct.TOTAL_RC_IS_NULL";
    
    public static final String PRODUCT_NAME_IS_NULL = "CurrentOrderProuct.PRODUCT_NAME_IS_NULL";
    
    public static final String FORBORN_IND_IS_INVALID = "CurrentOrderProuct.FORBORN_IND_IS_INVALID";
	
	 /**
     * Signle instance of this class.
     */
    private static OrderDataBr s_bRule;

    @Autowired
	private OrderDepositProperties m_prop;
    /**
     * Private constructor. We don't want consumers
     * of this class to create instances using 'new' operator.
     */
    private OrderDataBr()
    {
        //Do nothing here.
    }


    /**
     * <p><b>Description</b> Returns single instance of this class.</p>
     * @return Single instance of this class.
     */
    public static OrderDataBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new OrderDataBr();
        }
        return s_bRule;
    }
    
    /**
     * <p>
     * <b>Description </b> Validates Order Data for FINAL/ESTIMATE calculation type  in the given order
     * </p>
     * 
     * @param  String calculationTypeCD
     * @param  OrderDataDetail
     * @return true if the object of this class is valid; false otherwise.
     */
    
    public ValidationResult validate(String calculationTypeCD, OrderDataDetail orderData) 
    {
        assert orderData != null : "OrderData is null";

        ValidationResult result = new ValidationResult();
        
        if ( (calculationTypeCD==null) || ("".equals(calculationTypeCD))) 
        {
        	result.put(new ValidationError(CALCULATION_TYPE_CD_IS_NULL));
         }
        
        if ( (!OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD)  &&  (!OrderDepositCalculatorConst.FICO_DEPOSIT_ESTIMATE_TYPE.equals(calculationTypeCD))))
        {
        	result.put(new ValidationError(CALCULATION_TYPE_CD_IS_INVALID));
         }
        
        if (orderData.getCustomerID() == 0) {
            result.put(new ValidationError(CUSTOMERID_IS_NULL));
        }
        
        //FINAL TYPE
        if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD))
        {
        
           if ( (orderData.getOrderID()==null) || ("".equals(orderData.getOrderID()))){
               result.put(new ValidationError(ORDERID_IS_NULL));
             }
     
           if ( (orderData.getOrderIdSourceSystemCd()==null) || ("".equals(orderData.getOrderIdSourceSystemCd()))){
               result.put(new ValidationError(ORDERID_SRC_SYSTEM_CD_IS_NULL));
            }
         }
        
        //VALIDATE CURRENT ORDER PRODUCT
        if (orderData.getCurrentOrderProductList() == null) {
            result.put(new ValidationError(CURRENT_ORDER_PRODUCT_LIST_IS_NULL));
        } else {
        	CurrentOrderProductList currentOrderProductList=orderData.getCurrentOrderProductList();
        	
        	List<CurrentProduct> cproductList=currentOrderProductList.getCurrentProduct();
     	    CurrentProduct currentProduct=new CurrentProduct();
     	    if ( (cproductList==null) || (cproductList.isEmpty()) )
     	    {
     	    	result.put(new ValidationError(CURRENT_ORDER_PRODUCT_LIST_IS_EMPTY));
     	    }else {
     	    	for (Iterator<CurrentProduct> it = cproductList.iterator(); it.hasNext();) 
     	      	  { 
     	    		currentProduct=it.next();
     	    		//convertServiceTypeCd(currentProduct);
     	    		
     	    		 //FINAL TYPE
     	            if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD))
     	               {
     	    		    result.merge(validateOrderProduct(currentProduct,CURRENT_ORDER_PRODUCT_LABLE));
     	    		    result.merge(validateCurrentOrderProduct(currentProduct));
     	               }
    	    		 //ESTIMATE TYPE
     	            if (OrderDepositCalculatorConst.FICO_DEPOSIT_ESTIMATE_TYPE.equals(calculationTypeCD))
     	               {
     	            	if (!isValidRentedEquipmentCnt(currentProduct))
     	            	      result.put(new ValidationError(CURRENT_ORDER_PRODUCT_LABLE + RENTED_EQUIPMENT_CNT_IS_NULL));
     	               }
     	           //QC 32149, converting service type code after validation
     	           convertServiceTypeCd(currentProduct);
     	      	  }
        	  }
        }
        
        //VALIDATE PENDING ORDER PRODUCT
        if (orderData.getPendingOrderProductList() != null) {
        	PendingOrderProductList pendingOrderProductList=orderData.getPendingOrderProductList();  	
        	List<PendingProduct> pproductList=pendingOrderProductList.getPendingProduct();
        	PendingProduct pendingProduct=new PendingProduct();
     	    if ( (pproductList !=null) && (pproductList.size() > 0) )
     	    {
     	       for (Iterator<PendingProduct> itt = pproductList.iterator(); itt.hasNext();) 
     	      	  { 
     	    	    pendingProduct=itt.next();
     	    	    convertServiceTypeCd(pendingProduct);
     	    	    //FINAL TYPE
     	    	    if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD))
     	    	     {
     	    		   result.merge(validateOrderProduct(pendingProduct,PENDING_ORDER_PRODUCT_LABLE));
     	    	     }
     	    		
     	    		 //ESTIMATE TYPE
     	            if (OrderDepositCalculatorConst.FICO_DEPOSIT_ESTIMATE_TYPE.equals(calculationTypeCD))
     	               {
     	            	if (!isValidRentedEquipmentCnt(pendingProduct))
   	            	         result.put(new ValidationError(PENDING_ORDER_PRODUCT_LABLE + RENTED_EQUIPMENT_CNT_IS_NULL));
     	               }
     	      	  }
        	  }
        }
        
        //VALIDATE ASSIGNED ORDER PRODUCT
        if (orderData.getAssignedOrderProductList() != null) {
        	AssignedOrderProductList assignedOrderProductList=orderData.getAssignedOrderProductList();  	
        	List<AssignedProduct> aproductList=assignedOrderProductList.getAssignedProduct();
        	AssignedProduct assignedProduct=new AssignedProduct();
     	    if ( (aproductList !=null) && (aproductList.size() > 0) )
     	    {
     	       for (Iterator<AssignedProduct> itt = aproductList.iterator(); itt.hasNext();) 
     	      	  { 
     	    	    assignedProduct=itt.next();
     	    	    convertServiceTypeCd(assignedProduct);
     	    	   //FINAL TYPE
     	    	    if (OrderDepositCalculatorConst.FICO_DEPOSIT_FINAL_TYPE.equals(calculationTypeCD))
   	    	        {
     	    	      result.merge(validateOrderProduct(assignedProduct,ASSIGNED_ORDER_PRODUCT_LABLE));
   	    	          }
     	    	    
     	    	   //ESTIMATE TYPE
     	            if (OrderDepositCalculatorConst.FICO_DEPOSIT_ESTIMATE_TYPE.equals(calculationTypeCD))
     	               {
     	            	if (!isValidRentedEquipmentCnt(assignedProduct))
  	            	         result.put(new ValidationError(ASSIGNED_ORDER_PRODUCT_LABLE + RENTED_EQUIPMENT_CNT_IS_NULL));
     	               }
     	      	  }
        	  }
        }
               
        return result;
    }
    
    
    /**
     * <p>
     * <b>Description </b>Validates all mandatory attributes in the object. 
     * </p>
     * @param CurrentProduct
     * @return ValidationResult.
     */
    public ValidationResult validateCurrentOrderProduct(CurrentProduct currentProduct) 
    {
    	//BigDecimal zero = new BigDecimal("0");
    	Boolean bTrue=new Boolean(true);
    	
    	ValidationResult result = new ValidationResult();
    	
    	if (currentProduct.getTotalRecurringChargeAmt()==null) 
    		result.put(new ValidationError(TOTAL_RC_IS_NULL));
    	
    	if  ((currentProduct.getProductName()==null) || ("".equals(currentProduct.getProductName())))
   		    result.put(new ValidationError(PRODUCT_NAME_IS_NULL));
    	
    	/*  serviceTypeCd is SING and
    	 *  Final type  and
         *  only for currentProductOrder
         */
    	if  (!"SING".equals(currentProduct.getServiceTypeCd()) && (currentProduct.isForborneInd() != null ) && (currentProduct.isForborneInd().compareTo(bTrue)==0))
    		result.put(new ValidationError(FORBORN_IND_IS_INVALID));
    		
    	return result;
     }
    
    
    /**
     * <p>
     * <b>Description </b>Validates all mandatory attributes in the object. 
     * </p>
     * @param BaseProduct,String
     * @return ValidationResult.
     */
    public ValidationResult validateOrderProduct(BaseProduct product,String productLable) 
    {

    	ValidationResult result = new ValidationResult();
    	
    	if  ((product.getServiceTypeCd()==null) || ("".equals(product.getServiceTypeCd())))
   		    result.put(new ValidationError(productLable+SERVICE_TYPE_IS_NULL));
    	
    	if ((product.getAssignedProductID()==null) || ("".equals(product.getAssignedProductID())))
    		 result.put(new ValidationError(productLable+ASSIGNED_PRODUCT_ID_IS_NULL));
    	
    	if ((product.getAssignedProductIDSourceSystemCd()==null) || ("".equals(product.getAssignedProductIDSourceSystemCd())))
   		    result.put(new ValidationError(productLable+ASSIGNED_PRODUCT_ID_SRC_SYSTEM_CD_IS_NULL));
    	
    	if  ((product.getProductName()==null) || ("".equals(product.getProductName())))
   		    result.put(new ValidationError(productLable+PRODUCT_NAME_IS_NULL));
    	
    	/*
    	 * Only for single line product 
         * type = final 
         * (for any current/assigned/pending product order)
         * if  (("SING".equals(product.getServiceTypeCd())) && ((product.getOfferNameCd()==null) || ("".equals(product.getOfferNameCd()))))
   		 *   result.put(new ValidationError(productLable+OFFER_NAME_CD_IS_NULL));
   		     */
    	
    	
    	if (product.getRentedEquipmentCnt()==null)
    		result.put(new ValidationError(productLable+RENTED_EQUIPMENT_CNT_IS_NULL));
    	
    	if (product.getPurchasedEquipmentCnt()==null)
    		result.put(new ValidationError(productLable+PURCHASED_EQUIPMENT_CNT_IS_NULL));
		
    	return result;
     }
    
    /**
     * <p>
     * <b>Description </b>Validates all mandatory attributes in the object. 
     * </p>
     * @param BaseProduct
     * @return boolean.
     */
    public boolean isValidRentedEquipmentCnt(BaseProduct product) 
    {
    	    	
    	if (product.getRentedEquipmentCnt()==null)
    		return false;
    	
    	return true;
    }
    
 	/*
	 * If offer name = “Single Line Credit Restrict”;
     * Then 
     *     set  Fico’s input serviceTypeCode to  SLR
     * Else 
     *     serviceTypeCode = same as the input 
	 */
    public void convertServiceTypeCd(BaseProduct product)
    {
    	if ((m_prop.getOfferNameCDForSINGRestrict().equals(product.getOfferNameCd())) && (OrderDepositCalculatorConst.SING.equals(product.getServiceTypeCd())))
  	  {
  		 product.setServiceTypeCd(OrderDepositCalculatorConst.FICO_SING_RESTRICT);
  	    }
     }
    

}
