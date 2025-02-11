package com.telus.credit.orderDepositCalculator.fico;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fico.telus.rtca.blaze.RuleServicesBean;
//import com.telus.credit.framework.test.TelusConfig;
//import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.orderDepositCalculator.common.domain.EquipmentCategory;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationList;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationListResponse;
import com.telus.credit.orderDepositCalculator.webservice.PolicyException;
import com.telus.credit.orderDepositCalculator.webservice.ServiceException;
import com.telus.credit.orderDepositCalculator.webservice.impl.OrderDepositCalculatorServiceImpl;
import com.telus.credit.wirelineCreditProfileManagement.domain.CreditWorthiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@SuppressWarnings("deprecation")
//@RunWith(TelusJUnitClassRunner.class)
//@TelusConfig(configAppCtxFile="appCtx-OrderDep.properties")
@ContextConfiguration("classpath:orderDepositCalculatorService-spring.xml")
public class FICOInterfaceTest {

	private static final Log log = LogFactory.getLog(FICOInterfaceTest.class);
	
	@Autowired
	private OrderDepositCalculatorServiceImpl m_OrderDepositCalculatorServiceImpl;
	
	@Test
	public void testEquipmentQualification( ) throws PolicyException, ServiceException {
		GetEquipmentQualificationList request = new GetEquipmentQualificationList();
		request.setCustomerID(123456);
		CreditWorthiness creditWorthiness = new CreditWorthiness();
		creditWorthiness.setCreditValueCd("E");
		creditWorthiness.setDecisionCd("LTAACMDFLT01");
		request.setCreditWorthiness(creditWorthiness);
		
		GetEquipmentQualificationListResponse depositResponse = m_OrderDepositCalculatorServiceImpl.getEquipmentQualificationList(request);
		if ( depositResponse.getEquipmentCategoryQualificationList() != null ) {
			for ( EquipmentCategory equipmentCategory: depositResponse.getEquipmentCategoryQualificationList().getEquipmentCategory() ) {
				log.debug( "Equipment Category Product Code: " + equipmentCategory.getProductCd() 
							+ ", Max Count: " + equipmentCategory.getMaxCount() );
			}
		}
		else {
			
		}
	}
}
