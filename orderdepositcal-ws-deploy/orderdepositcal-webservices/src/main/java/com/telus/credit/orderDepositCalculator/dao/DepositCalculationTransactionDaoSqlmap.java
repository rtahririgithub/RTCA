package com.telus.credit.orderDepositCalculator.dao;


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;





import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;
import com.telus.credit.orderDepositCalculator.common.domain.OrderData;
import com.telus.credit.orderDepositCalculator.common.domain.OrderDataDetail;
import com.telus.credit.orderDepositCalculator.common.domain.BaseProduct;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentProduct;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.PendingProduct;
import com.telus.credit.orderDepositCalculator.common.domain.PendingOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.AssignedProduct;
import com.telus.credit.orderDepositCalculator.common.domain.AssignedOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.PayChannelNumberList;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResultList;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResult;
import com.telus.credit.orderDepositCalculator.webservice.dto.OrderDepositCalculationDto;




import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * <p>
 * <b>Description: </b> This class represents access to the data store for
 * CreditProfile domain object. (iBatis implementation).
 * </p>
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
 *  
 */

public class DepositCalculationTransactionDaoSqlmap extends AbstractSqlMapDao  
               implements DepositCalculationTransactionDao

{
	 private static Log log = LogFactory.getLog(DepositCalculationTransactionDaoSqlmap.class);
	 
	 protected final static String STAT_ID_INSERT_ORDER_DEPOSIT_CALC_TRN = "insert_order_deposit_calc_trn";
	 
	 protected final static String STAT_ID_INSERT_CURRENT_PRODUCT_INSTANCE = "insert_current_order_product_instance";
	 protected final static String STAT_ID_INSERT_ASSIGNED_PENDING_PRODUCT_INSTANCE = "insert_assigned_pending_order_product_instance";
	 
	 protected final static String STAT_ID_INSERT_PRODUCT_PAY_CHANNEL = "insert_order_product_pay_channel";
	 
	 protected final static String CURRENT_ORDER_PRODUCT_STATUS="C";
	 protected final static String PENDING_ORDER_PRODUCT_STATUS="P";
	 protected final static String ASSIGNED_ORDER_PRODUCT_STATUS="A";
	 
	 public Long insertDepositCalculationTransaction(OrderDepositCalculationDto orderDepositCalculationDto)
	            throws DuplicateKeyException
	       {
		       Long depositCalculationTrnID= (Long) insert( STAT_ID_INSERT_ORDER_DEPOSIT_CALC_TRN, orderDepositCalculationDto);
		       return depositCalculationTrnID;
		 
	            }
	 
	 public Long insertCurrentOrderProductInstance(Map productInstanceMap)
	            throws DuplicateKeyException
	      {
		      Long productInstanceID = (Long) insert( STAT_ID_INSERT_CURRENT_PRODUCT_INSTANCE, productInstanceMap);
		      return productInstanceID;
		      
	            }
	 
	 public Long insertInvoicedOrderProductInstance(Map productInstanceMap)
	            throws DuplicateKeyException
	     {
		    Long productInstanceID = (Long) insert( STAT_ID_INSERT_ASSIGNED_PENDING_PRODUCT_INSTANCE, productInstanceMap);
	        return productInstanceID;
	       }
	 
	public void insertDepositCalculationTransaction(OrderDepositCalculationDto orderDepositCalculationDto, OrderData orderData)
	            throws DuplicateKeyException
	 {
		 Long depositCalculationTrnID= (Long) insert( STAT_ID_INSERT_ORDER_DEPOSIT_CALC_TRN, orderDepositCalculationDto);
		 String userID=orderDepositCalculationDto.getUserID();
		 //CurrentOrderProductList
		 CurrentOrderProductList currentOrderProductList=orderData.getCurrentOrderProductList();
		 ProductDepositResultList productDepositResultList=orderDepositCalculationDto.getProductDepositResultList();
		 if ( currentOrderProductList != null ) 
              {
			      if ( log.isDebugEnabled() )  log.debug("inserting current order product instance......");
			      insertCurrentOrderProductInstance(currentOrderProductList,productDepositResultList,depositCalculationTrnID,userID);
	        }
		 
		 //PendingOrderProductList
		 PendingOrderProductList pendingOrderProductList=orderData.getPendingOrderProductList();	 
		 if ( pendingOrderProductList != null ) 
              {
			     if ( log.isDebugEnabled() )  log.debug("inserting pending order product instance......");
			     insertPendingOrderProductInstance(pendingOrderProductList,depositCalculationTrnID,userID);
	        }
		 
		 //AssignedOrderProductList
		 AssignedOrderProductList assignedOrderProductList=orderData.getAssignedOrderProductList();	 
		 if ( assignedOrderProductList != null ) 
              {
			     if ( log.isDebugEnabled() )  log.debug("inserting assigned order product instance......");
			     insertAssignedOrderProductInstance(assignedOrderProductList,depositCalculationTrnID,userID);
	        }
	 
	 }//method closing
	 
	 private void insertCurrentOrderProductInstance(CurrentOrderProductList currentOrderProductList,ProductDepositResultList productDepositResultList,Long depositCalculationTrnID,String userID)
	            throws DuplicateKeyException
	  {
		Map productInstanceMap; 
		Long productInstanceID;
		
		
		List<ProductDepositResult> depositList=productDepositResultList.getProductDepositResult();
		ProductDepositResult productDepositResult=new ProductDepositResult();
		BigDecimal assessedDepositAmt;
		
		List<CurrentProduct> productList=currentOrderProductList.getCurrentProduct();
 	    CurrentProduct currentProduct=new CurrentProduct();
 	    
 	    if (!productList.isEmpty())
 	    {
 	    	for (Iterator<CurrentProduct> it = productList.iterator(); it.hasNext();) 
 	      	  {
 	    		 currentProduct=it.next();
 	    		 assessedDepositAmt=new BigDecimal("0");
 	    		 
 	    	     
 	    		 //get assesssed deposit amount for each product
 	    		 if (!depositList.isEmpty())
 	    	     {
 	    			for (Iterator<ProductDepositResult> itt = depositList.iterator(); itt.hasNext();) 
 	    			{
 	    				productDepositResult=itt.next();
 	    				if ( currentProduct.getAssignedProductID().equalsIgnoreCase(productDepositResult.getAssignedProductID()))
 	    					assessedDepositAmt = productDepositResult.getAssessedDepositAmt();
 	    			   }
 	    	        }

 	    		 //prepare data for ibatis 
 	    		 productInstanceMap=new HashMap();
 	    		 productInstanceMap.put("orderProductStatus", CURRENT_ORDER_PRODUCT_STATUS);
 	    		 productInstanceMap.put("currentProduct", currentProduct);
 	    		 productInstanceMap.put("depositCalculationTrnID", depositCalculationTrnID);
 	    		 productInstanceMap.put("assessedDepositAmt", assessedDepositAmt);
 	    		 productInstanceMap.put("userID", userID);
 	    		 productInstanceID = (Long) insert( STAT_ID_INSERT_CURRENT_PRODUCT_INSTANCE, productInstanceMap);
 	    		 
 	    		 
 	    		 //persist product pay channel
 	    		 PayChannelNumberList payChannelNumList=currentProduct.getPayChannelNumberList();
 	    		 if (payChannelNumList !=null)
 	    		   { 
 	    			 insertProductPayChannel(payChannelNumList, productInstanceID, userID );
 	    			   }
 	      	   }
 	    
 	    	}
	    }//method closing
	 
	 
	 private void insertPendingOrderProductInstance(PendingOrderProductList pendingOrderProductList,Long depositCalculationTrnID,String userID)
	            throws DuplicateKeyException
	  {
			Map productInstanceMap; 
			Long productInstanceID;
			
			List<PendingProduct> productList=pendingOrderProductList.getPendingProduct();
			PendingProduct pendingProduct=new PendingProduct();
	 	    
	 	    if (!productList.isEmpty())
	 	    {
	 	    	for (Iterator<PendingProduct> it = productList.iterator(); it.hasNext();) 
	 	      	  {
	 	    		pendingProduct=it.next();
	 	    		 //to do
	 	    		 productInstanceMap=new HashMap();
	 	    		 productInstanceMap.put("orderProductStatus", PENDING_ORDER_PRODUCT_STATUS);
	 	    		 productInstanceMap.put("baseProduct", pendingProduct);
	 	    		 productInstanceMap.put("depositCalculationTrnID", depositCalculationTrnID);
	 	    		 productInstanceMap.put("userID", userID);
	 	    		 productInstanceID = (Long) insert( STAT_ID_INSERT_ASSIGNED_PENDING_PRODUCT_INSTANCE, productInstanceMap);
	 	    		 
	 	    		 PayChannelNumberList payChannelNumList=pendingProduct.getPayChannelNumberList();
	 	    		 if (payChannelNumList !=null)
	 	    		   { 
	 	    			 insertProductPayChannel(payChannelNumList, productInstanceID, userID );
	 	    			   }
	 	      	   }
	 	    
	 	    	}
	    }//method closing
	 
	 
	 private void insertAssignedOrderProductInstance(AssignedOrderProductList assignedOrderProductList,Long depositCalculationTrnID,String userID)
	            throws DuplicateKeyException
	  {
		    Map productInstanceMap; 
			Long productInstanceID;
			
			List<AssignedProduct> productList=assignedOrderProductList.getAssignedProduct();
			AssignedProduct assignedProduct=new AssignedProduct();
	 	    
	 	    if (!productList.isEmpty())
	 	    {
	 	    	for (Iterator<AssignedProduct> it = productList.iterator(); it.hasNext();) 
	 	      	  {
	 	    		 assignedProduct=it.next();
	 	    		 //to do
	 	    		 productInstanceMap=new HashMap();
	 	    		 productInstanceMap.put("orderProductStatus", ASSIGNED_ORDER_PRODUCT_STATUS);
	 	    		 productInstanceMap.put("baseProduct", assignedProduct);
	 	    		 productInstanceMap.put("depositCalculationTrnID", depositCalculationTrnID);
	 	    		 productInstanceMap.put("userID", userID);
	 	    		 productInstanceID = (Long) insert( STAT_ID_INSERT_ASSIGNED_PENDING_PRODUCT_INSTANCE, productInstanceMap);
	 	    		 
	 	    		 PayChannelNumberList payChannelNumList=assignedProduct.getPayChannelNumberList();
	 	    		 if (payChannelNumList !=null)
	 	    		   { 
	 	    			 insertProductPayChannel(payChannelNumList, productInstanceID, userID );
	 	    			   }
	 	      	   }
	 	    
	 	    	}
	    }//method closing
	 
	 
	  public void insertProductPayChannel(PayChannelNumberList payChannelNumList,Long productInstanceID,String userID)
	            throws DuplicateKeyException
	  {  
		 Map productPayChannelMap;
		 Long payChannelNumber;
		 
		 List<Long> payChannelList=payChannelNumList.getPayChannelNum();
		 
	     if (!payChannelList.isEmpty())
	     {
	    	 for (Iterator<Long> itt= payChannelList.iterator(); itt.hasNext();)
	    	 {
	    		 payChannelNumber = itt.next();
	    		 productPayChannelMap = new HashMap();
	    		 productPayChannelMap.put("productInstanceID", productInstanceID);
	    		 productPayChannelMap.put("payChannelNumber", payChannelNumber);
	    		 productPayChannelMap.put("userID", userID);
	    		 
	    		//Persist data for PRODUCT_PAY_CHANNEL.
	    		 insert(STAT_ID_INSERT_PRODUCT_PAY_CHANNEL,productPayChannelMap);
	    	    }
	    	 
	        }
	    }//method closing
	 
}
