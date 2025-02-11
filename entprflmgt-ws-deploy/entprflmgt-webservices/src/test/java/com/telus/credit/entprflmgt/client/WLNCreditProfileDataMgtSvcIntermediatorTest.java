package com.telus.credit.entprflmgt.client;


import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.entprflmgt.util.TestHelper;
import com.telus.credit.util.EnvUtil;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;


@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
public class WLNCreditProfileDataMgtSvcIntermediatorTest {

	private static final Log log = LogFactory.getLog(WLNCreditProfileDataMgtSvcIntermediatorTest.class);
	@Autowired
	private WLNCrdProfileUpdater m_wlnCrdProfileUpdater;
	
	@Autowired
	//@Qualifier("wirelineCreditProfileDataMgtService")
	@Qualifier("wlnCreditProfileUpdater")
	private WLNCreditProfileDataManagementServicePortType m_WLNCreditProfileDataManagementServicePortType;
	
	public WLNCreditProfileDataMgtSvcIntermediatorTest() {
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
			//ClassPathXmlApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext(
			//		EnvUtil.resourcesFolder);
			//m_WLNCreditProfileDataManagementServicePortType = (WLNCreditProfileDataManagementServicePortType) m_ApplicationContext
				//	.getBean("entCrdProfileMgtServicePojoTgt");
			
			// aWCDMPojo= (WLSCreditManagmentServicePojo)
			// m_ApplicationContext.getBean("WLSCreditManagementServiceTarget");
			// EnvDetailUtil aEnvDetailUtil = (EnvDetailUtil)
			// m_ApplicationContext.getBean("EnvDetailUtil");
			// aEncryptUtil= (EncryptUtil)
			// m_ApplicationContext.getBean("Encryptor");

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void test_ping() throws PolicyException, ServiceException 
	{
		String responseStr = m_WLNCreditProfileDataManagementServicePortType.ping();
		System.out.println("Response String: " + responseStr);
	}
	
	//@Test
	public void test_UpdateCreditProfile() throws CreditProfileUpdateException, JAXBException {
		
		UpdateCreditProfile updateCreditProfile = new UpdateCreditProfile();
		UpdateCreditProfile updateCreditProfileObj = (UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/data/SampleEnterpriseCreditProfileManagementServiceRequest2.xml" );
		
		m_wlnCrdProfileUpdater.updateCreditProfile(updateCreditProfileObj.getConsumerCreditProfileInfo(),
				updateCreditProfileObj.getAuditInfo());
	}
	
	//@Test
	public void test_convertToServicePayload() throws JAXBException {
		UpdateCreditProfile updateCreditProfile = new UpdateCreditProfile();
		UpdateCreditProfile updateCreditProfileObj = (UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/data/SampleEnterpriseCreditProfileManagementServiceRequest.xml" );
		
		String servicePayload = m_wlnCrdProfileUpdater.convertToServicePayload(updateCreditProfileObj.getConsumerCreditProfileInfo(),
				updateCreditProfileObj.getAuditInfo());
		System.out.println("Service payload: " + servicePayload );
	}
	
	//@Test
	public void test_getAyncUpdateCreditProfile() throws JAXBException {
		UpdateCreditProfile updateCreditProfile = new UpdateCreditProfile();
		UpdateCreditProfile updateCreditProfileObj = (UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/data/SampleEnterpriseCreditProfileManagementServiceRequest.xml" );
		
		Object obj = m_wlnCrdProfileUpdater.buildAsyncServiceRequest(updateCreditProfileObj.getConsumerCreditProfileInfo(),
				updateCreditProfileObj.getAuditInfo() );
		String xmlObjStr = TestHelper.convertObjectToXml(obj);
		System.out.println("xml Obj: " + xmlObjStr );
	}
}
