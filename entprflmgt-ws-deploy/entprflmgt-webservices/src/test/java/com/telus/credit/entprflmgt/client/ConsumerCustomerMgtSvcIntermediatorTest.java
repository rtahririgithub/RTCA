/*package com.telus.credit.entprflmgt.client;


import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.entprflmgt.domain.BillingAccountIdentification;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.entprflmgt.util.TestHelper;
import com.telus.credit.entprflmgt.util.XmlUtils;
import com.telus.credit.entprflmgt.webservice.impl.EnterpriseCreditProfileManagementServiceImpl;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessRequest;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessResponse;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessResponse.AssessedCreditWorthiness;
import com.telus.credit.wlnprflmgtpxy.domain.enterprise.common.AuditInfo;
import com.telus.customer.consmgt.domain.FullCustomer;
import com.telus.customer.consmgt.domain.GetFullCustomerInfoResponse;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ConsumerCustomerManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx-enterpriseCrd.properties")
@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
public class ConsumerCustomerMgtSvcIntermediatorTest {

	private static final Log log = LogFactory.getLog(ConsumerCustomerMgtSvcIntermediatorTest.class);
	@Autowired
	private CustomerManagementServiceIntermediator m_customerManagementServiceIntermediator;
	
	@Autowired
	@Qualifier("consumerCustMgtService")
	private ConsumerCustomerManagementServicePortType m_consumerCustomerManagementServicePortType;
	
	
	//@Test
	public void test_ping() throws com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException, com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException {
		String pingResponse = m_consumerCustomerManagementServicePortType.ping();
		System.out.println("\n\n\n\n\nPing Response: " + pingResponse + "\n\n\n\n\n" );
	}
	
	@Test
	public void test_getBillingAccountsByCustomerId() throws com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException, com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException {
		FullCustomer fullCustomer = m_consumerCustomerManagementServicePortType.getFullCustomerInfo(8000); //118855431
		GetFullCustomerInfoResponse fullCustomerInfoResponse = new GetFullCustomerInfoResponse();
		fullCustomerInfoResponse.setFullCustomer(fullCustomer);
		String responseStr = TestHelper.convertObjectToXml(fullCustomerInfoResponse);
	    System.out.println("Response XML:" + responseStr );
	}
	
	//@Test
	public void test_getBillingAccountsByWLNBAN() throws com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException, com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException {
		FullCustomer fullCustomer = m_consumerCustomerManagementServicePortType.getFullCustomerInfoByBillingAccountNumber("600539119",1001);
		GetFullCustomerInfoResponse fullCustomerInfoResponse = new GetFullCustomerInfoResponse();
		fullCustomerInfoResponse.setFullCustomer(fullCustomer);
		String responseStr = TestHelper.convertObjectToXml(fullCustomerInfoResponse);
	    System.out.println("Response XML:" + responseStr );
	}
	
	//@Test
	public void test_getBillingAccountsByWLSBAN() throws com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException, com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException {
		FullCustomer fullCustomer = m_consumerCustomerManagementServicePortType.getFullCustomerInfoByBillingAccountNumber("70650600",130);
		GetFullCustomerInfoResponse fullCustomerInfoResponse = new GetFullCustomerInfoResponse();
		fullCustomerInfoResponse.setFullCustomer(fullCustomer);
		String responseStr = TestHelper.convertObjectToXml(fullCustomerInfoResponse);
	    System.out.println("Response XML:" + responseStr );
	}
	
	//@Test
	public void test_getCrdProfileUpdateFlagsByCustomerId() throws GetFullCustomerInfoException {
		Identification id = new Identification();
		id.setCustomerId(new Long(118855431) );
		CrdProfileUpdateFlags crdUpdateFlagsByCustomerId = m_customerManagementServiceIntermediator.getCrdProfileUpdateFlags(id);
		System.out.println("crdUpdateFlagsByCustomerId : " + crdUpdateFlagsByCustomerId );
		
		id = new Identification();
		BillingAccountIdentification billingAccountId = new BillingAccountIdentification();
		billingAccountId.setBillingSystemId(1001);
		billingAccountId.setBillingAccountNumber("600539119");
		id.setBillingAccountIdentification(billingAccountId);
		CrdProfileUpdateFlags crdUpdateFlagsByWLNBAN = m_customerManagementServiceIntermediator.getCrdProfileUpdateFlags(id);
		System.out.println("crdUpdateFlagsByWLNBAN : " + crdUpdateFlagsByWLNBAN );
		
		id = new Identification();
		billingAccountId = new BillingAccountIdentification();
		billingAccountId.setBillingSystemId(130);
		billingAccountId.setBillingAccountNumber("70650600");
		id.setBillingAccountIdentification(billingAccountId);
		CrdProfileUpdateFlags crdUpdateFlagsByWLSBAN = m_customerManagementServiceIntermediator.getCrdProfileUpdateFlags(id);
		System.out.println("crdUpdateFlagsByWLSBAN : " + crdUpdateFlagsByWLSBAN );
		boolean wlnFlag = crdUpdateFlagsByCustomerId.equals(crdUpdateFlagsByWLNBAN);
		boolean wlsFlag = crdUpdateFlagsByWLNBAN.equals(crdUpdateFlagsByWLSBAN);
		System.out.println(" wln flag: " + wlnFlag + ", wls flag: " + wlsFlag );
		assertTrue( crdUpdateFlagsByCustomerId.equals(crdUpdateFlagsByWLNBAN) );
		assertTrue( crdUpdateFlagsByWLNBAN.equals(crdUpdateFlagsByWLSBAN) );
		
	}
	
	
}
*/