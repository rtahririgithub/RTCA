package com.telus.credit.entprflmgt.client;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.entprflmgt.util.TestHelper;
import com.telus.credit.util.EnvUtil;
import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;
import com.telus.credit.wlnprflmgt.domain.GetCreditProfileByCustomerIdResponse;
import com.telus.credit.wlnprflmgt.domain.enterprise.common.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.WLNCreditProfileManagementServicePortType;

@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
public class WLNCreditProfileMgtSvcIntermediatorTest {

	private static final Log log = LogFactory.getLog(WLNCreditProfileMgtSvcIntermediatorTest.class);
	@Autowired
	private WLNCreditProfileMgtServiceIntermediator m_wlnCreditProfileMgtServiceIntermediator;
	
	@Autowired
	@Qualifier("wirelineCreditProfileMgtService")
	private WLNCreditProfileManagementServicePortType m_wlnCreditProfileManagementServicePortType;
	
	public WLNCreditProfileMgtSvcIntermediatorTest() {
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void setUp() throws Exception {
		try {
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant",
					"true");
			EnvUtil.setupTestEnv();
			ClassPathXmlApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext(
					EnvUtil.resourcesFolder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void test_ping() throws com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.PolicyException, com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.ServiceException {
		String responseStr = m_wlnCreditProfileManagementServicePortType.ping();
		System.out.println("Response String: " + responseStr);
	}
	
	@Test
	public void test_getCreditProfileByCustomerId() throws com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.PolicyException, com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.ServiceException {
		AuditInfo auditInfo = new AuditInfo();
		
		ConsumerCreditProfile consumerCreditProfile = m_wlnCreditProfileManagementServicePortType.getCreditProfileByCustomerId(22190647, auditInfo);
		GetCreditProfileByCustomerIdResponse response = new GetCreditProfileByCustomerIdResponse();
		response.setCreditProfile(consumerCreditProfile);
		String responseStr = TestHelper.convertObjectToXml(response);
		System.out.println("Response: " + responseStr );
	}
	
	@Test
	public void test_getCreditProfileByCustomerIdMediator() throws GetCreditProfileInfoException {
		ConsumerCreditProfile consumerCreditProfile = m_wlnCreditProfileMgtServiceIntermediator.getCreditProfileByCustomerId(22190647, "testUserId");
		GetCreditProfileByCustomerIdResponse response = new GetCreditProfileByCustomerIdResponse();
		response.setCreditProfile(consumerCreditProfile);
		String responseStr = TestHelper.convertObjectToXml(response);
		System.out.println("Response: " + responseStr );
	}
}
