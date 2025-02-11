package com.telus.credit.wlnprflmgt;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.aop.framework.ProxyFactoryBean;

import com.telus.credit.testUtil.EnvUtil;
import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;

import com.telus.credit.wlnprflmgt.domain.credit.common.CreditProfileData;

import com.telus.credit.wlnprflmgt.domain.enterprise.common.AuditInfo;
import com.telus.credit.wlnprflmgt.webservice.impl.WLNCreditProfileManagementServiceImpl;
import com.telus.credit.wlnprflmgt.domain.credit.common.CustomerGuarantor;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.WLNCreditProfileManagementServicePortType;

import org.junit.Test;

//@SuppressWarnings("deprecation")
//@RunWith(TelusJUnitClassRunner.class)
//@TelusConfig(configAppCtxFile="appCtx_WLNCrdPrflMgtWs.properties")
//@ContextConfiguration("classpath:wlnCreditProfileManagement-spring.xml")
@ContextConfiguration(locations = { "file:src/test/resources/test_wlnCreditProfileManagement-spring.xml" })
//@RunWith(SpringJUnit4ClassRunner.class)
public class WLNCreditProfileMgmtServiceImplTest extends TestCase {

	//private static final Log s_log = LogFactory.getLog(WLNCreditProfileMgmtServiceImplTest.class);
	ClassPathXmlApplicationContext m_ApplicationContext;
	ConsumerCreditProfile cp=new ConsumerCreditProfile();

	@Autowired
	private WLNCreditProfileManagementServicePortType m_SvcImpl;

	
	protected void setUp() throws Exception {
		try {
			super.setUp();
			EnvUtil.setupTestEnv();
			m_ApplicationContext = new ClassPathXmlApplicationContext("test-wlnCreditProfileManagement-spring.xml");
			m_SvcImpl = (WLNCreditProfileManagementServicePortType) m_ApplicationContext.getBean("wlnCrdProfileMgtService");	
	
			} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	
	@Test
	public void testGetCreditProfileByCustomerId() throws PolicyException, ServiceException 
	{
		Integer expectNum=new Integer(2000);
		AuditInfo auditInfo=new AuditInfo();
		auditInfo.setUserId("T829939");
		auditInfo.setOriginatorApplicationId("Credit");
      
		long customerId=1834029;//customer has risk level
		long customerId2=565200;//no risk level customer
		
		cp=m_SvcImpl.getCreditProfileByCustomerId(customerId,auditInfo);
		assertEquals("Junit test completed, risk level num is ", expectNum, cp.getCreditWorthiness().getCreditRiskLevel()); 
        cp=m_SvcImpl.getCreditProfileByCustomerId(customerId2,auditInfo);
        assertEquals("Junit test2 completed, risk level num is ",  null, cp.getCreditWorthiness().getCreditRiskLevel()); 
        
	}
	

//	@Test
//	public void testCreateProfile() throws PolicyException, ServiceException 
//	{
//		AuditInfo auditInfo=new AuditInfo();
//		auditInfo.setUserId("T829939");
//		auditInfo.setOriginatorApplicationId("Credit");
//		
//		CreditProfileData creditProfileData=new CreditProfileData();
//		creditProfileData.setCustomerID(999999920);
//		       
//        m_SvcImpl.createCreditProfile(creditProfileData, auditInfo);
//        
//	}
	
//	@Test
//	public void testUnmergeCreditProfile() throws PolicyException, ServiceException 
//	{
//		AuditInfo auditInfo=new AuditInfo();
//		auditInfo.setUserId("T829939");
//		auditInfo.setOriginatorApplicationId("Credit");
//		
//        long customerId=19231681;
//        
//        m_SvcImpl.unmergeCreditProfile(customerId, auditInfo);
//        
//	}
	
}
