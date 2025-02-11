package com.telus.credit.orderDepositCalculator.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;



import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

//import com.telus.credit.framework.test.TelusJUnitClassRunner;
//import com.telus.credit.framework.test.TestUtil;

import org.junit.Test;

import com.telus.framework.math.Money;
import com.telus.credit.orderDepositCalculator.dao.DepositCalculationTransactionDao;
import com.telus.credit.orderDepositCalculator.dao.DepositCalculationTransactionDaoSqlmap;
import com.telus.credit.orderDepositCalculator.webservice.dto.OrderDepositCalculationDto;
import com.telus.credit.orderDepositCalculator.common.domain.OrderData;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentProduct;
import com.telus.credit.orderDepositCalculator.common.domain.CurrentOrderProductList;
import com.telus.credit.orderDepositCalculator.common.domain.PayChannelNumberList;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResultList;
import com.telus.credit.orderDepositCalculator.common.domain.ProductDepositResult;

//@RunWith(TelusJUnitClassRunner.class)
@ContextConfiguration("classpath:test-crd-dao-sqlmap-spring.xml")
public class DepositCalculationTrxDaoTest 
{
  @Autowired
   private DepositCalculationTransactionDao m_depositCalculationTransactionDao;
    

   @Test
    public void testGetCreditProfileDtoByCreditProfileAndCustomerIds() 
    {
            
    	    ProductDepositResult productDepositResult=new ProductDepositResult();
    	    productDepositResult.setAssessedDepositAmt(new BigDecimal("50"));
    	    productDepositResult.setAssignedProductID("productid");
    	    productDepositResult.setMonthlyChargeAmt(new BigDecimal("22.40"));
    	    productDepositResult.setProductNameCd("NAC");
    	    productDepositResult.setServiceTypeCd("serviceType");
    	    
    	    List<ProductDepositResult> depositResult=new ArrayList<ProductDepositResult>();
    	    depositResult.add(productDepositResult);
    	    ProductDepositResultList productDepositResultList=new ProductDepositResultList();
    	    productDepositResultList.setProductDepositResult(depositResult);
    	    
    	    OrderDepositCalculationDto dto=new OrderDepositCalculationDto();
    	    dto.setApplicationID("CREDITMGMT");
    	    dto.setChannelID("channelID");
    	    dto.setCustomerID(123000000);
    	    dto.setDecisionCD("DFLTR0000001");
    	    dto.setCalculationResultMsgCD("");
    	    dto.setCalculationResultReasonCD("");
    	    dto.setDepositAdjustmentAmt( new Money( 20 ));
    	    dto.setDepositOnHandAmt( new Money( 30 ));
    	    dto.setOrderID("orderid123");
    	    dto.setOrderMasterSrcID("4856");
    	    dto.setLastDepositPaidDate(new Date());
    	    dto.setLastDepositPendingDate(new Date());
    	    dto.setLastDepositReleaseDate(new Date());
    	    dto.setTotalAssessedDepositAmt(new Money( 500 ));
    	    dto.setTotalDepositPaidAmt(new Money( 100 ));
    	    dto.setTotalDepositPendingAmt(new Money( 200 ));
    	    dto.setTotalDepositReleaseAmt(new Money( 300 ));
    	    dto.setProductDepositResultList(productDepositResultList);
    	    dto.setUserID("T829939");
    	    
    	    List<Long> plist=new ArrayList<Long>();
    	    plist.add(new Long(456000000));
    	    PayChannelNumberList payChannelNumberList=new PayChannelNumberList();
    	    payChannelNumberList.setPayChannelNum(plist);
    	    
    	    CurrentProduct currentProduct=new CurrentProduct();
    	    currentProduct.setAssignedProductID("productid");
    	    currentProduct.setAssignedProductIDSourceSystemCd("5678");
    	    currentProduct.setEstimatedUsageChargeAmt(new BigDecimal("10"));
    	    currentProduct.setForborneInd(true);
    	    currentProduct.setLossRentalEquipmentChargeAmt(new BigDecimal("10"));
    	    currentProduct.setOfferNameCd("offname");
    	    currentProduct.setPreviouslyAssessedDepositAmt(new BigDecimal("10"));
    	    //DSL, IPT and STV
    	    currentProduct.setProductName("NAC");
    	    currentProduct.setProductOrderCanceledInd(true);
    	    currentProduct.setPurchasedEquipmentCnt(2);
    	    currentProduct.setRentedEquipmentCnt(3);
    	    currentProduct.setServiceTypeCd("serviceType");
    	    currentProduct.setTotalRecurringChargeAmt(new BigDecimal("10"));
    	    currentProduct.setPayChannelNumberList(payChannelNumberList);
    	    
    	    List<CurrentProduct> currentProductList=new ArrayList<CurrentProduct>();
    	    currentProductList.add(currentProduct);
    	    CurrentOrderProductList alist=new CurrentOrderProductList();
    	    alist.setCurrentProduct(currentProductList);
    	    
    	    OrderData orderData=new OrderData();
    	    orderData.setCurrentOrderProductList(alist);
    	    
    	    
    	    System.out.println("test Starting ....\n");
    	    
    	    try {
    	       m_depositCalculationTransactionDao.insertDepositCalculationTransaction(dto, orderData);
    	    }
    	    catch (DuplicateKeyException e)
    	    {
    	    	 System.out.println("Exception... ...."+ e.getMessage());
    	     }
            
            System.out.println("\nEnd!");
       }

	/**
	 * @return the depositCalculationTransactionDao
	 */
	public DepositCalculationTransactionDao getDepositCalculationTransactionDao() {
		return m_depositCalculationTransactionDao;
	}

	/**
	 * @param depositCalculationTransactionDao the depositCalculationTransactionDao to set
	 */
	public void setDepositCalculationTransactionDao(
			DepositCalculationTransactionDao depositCalculationTransactionDao) {
		m_depositCalculationTransactionDao = depositCalculationTransactionDao;
	}
    
    
}
