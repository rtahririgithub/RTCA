package com.telus.credit.entprflmgt.impl;

import java.io.File;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.core.JmsTemplate;

import com.telus.credit.entprflmgt.asyncprocessing.JMSCreditProfileUpdateSender;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfileResponse;
import com.telus.credit.entprflmgt.webservice.impl.EnterpriseCreditProfileManagementServiceImpl;
import com.telus.credit.util.EnvUtil;
import com.telus.credit.util.RequestResponseTestUtil;
import com.telus.customer.consmgt.domain.GetFullCustomerInfoByBillingAccountNumber;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ConsumerCustomerManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.WLNCreditProfileManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.WLSCreditProfileDataManagementServicePortType;



public class EnterpriseCreditProfileManagementServiceTest extends TestCase {
	public EnterpriseCreditProfileManagementServiceImpl m_EnterpriseCreditProfileManagementServiceImpl;
	public ConsumerCustomerManagementServicePortType m_ConsumerCustomerManagementServicePortType;
	public WLNCreditProfileDataManagementServicePortType m_WLNCreditProfileDataManagementServicePortType;
	public WLNCreditProfileManagementServicePortType m_WLNCreditProfileManagementServicePortType;
	
	public WLSCreditProfileDataManagementServicePortType m_wirelessCreditProfileDataMgtServiceTgt;
	public WLNCreditProfileManagementProxyServicePortType m_WLNCreditProfileManagementProxyServicePortType; 
	public JmsTemplate jmsTemplate;
	
	String testDataPath 	="./../entprflmgt-webservices/src/test/data/";	
	
	
	String responseFolder  	=testDataPath + "response/";
	String requestFolder	=testDataPath ;

	
	protected void setUp() throws Exception {
		try {
			//System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant","true");
			EnvUtil.setupTestEnv();
			ClassPathXmlApplicationContext m_ApplicationContext;
			//m_ApplicationContext = new ClassPathXmlApplicationContext(EnvUtil.resourcesFolder);
			m_ApplicationContext = new ClassPathXmlApplicationContext("enterpriseProfileManagement-spring.xml");
			m_EnterpriseCreditProfileManagementServiceImpl = (com.telus.credit.entprflmgt.webservice.impl.EnterpriseCreditProfileManagementServiceImpl) m_ApplicationContext.getBean("entCrdProfileMgtServicePojoTgt");
			 m_ConsumerCustomerManagementServicePortType = (com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ConsumerCustomerManagementServicePortType) m_ApplicationContext.getBean("consumerCustMgtServiceTgt");
			 m_WLNCreditProfileDataManagementServicePortType = (com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType) m_ApplicationContext.getBean("wirelineCreditProfileDataMgtServiceTgt");
			 m_wirelessCreditProfileDataMgtServiceTgt = (WLSCreditProfileDataManagementServicePortType) m_ApplicationContext.getBean("wirelessCreditProfileDataMgtServiceTgt");
			 m_WLNCreditProfileManagementServicePortType = (com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.WLNCreditProfileManagementServicePortType) m_ApplicationContext.getBean("wirelineCreditProfileMgtServiceTgt");
			m_WLNCreditProfileManagementProxyServicePortType = (com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType) m_ApplicationContext.getBean("wirelineCreditProfileMgtProxyServiceTgt");

			jmsTemplate = (JmsTemplate) m_ApplicationContext.getBean("jmsTemplate");
		 
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	private static JMSCreditProfileUpdateSender jmsSender;
	private static BeanFactory beanFactory;
	public void test_updateCreditProfile() throws Throwable 
	{
		try {
				
			sendCreditProfileInfoToWirelineTest();
			
			//ping_ConsumerCustomerManagementService();
			//String fname;
			//fname="updateDL_By_Cust_ID_EntCpMgmtSvc.xml";
			//UpdateCreditProfile request = RequestResponseTestUtil.createUpdateCreditProfileRequest(requestFolder, fname);
			//UpdateCreditProfileResponse aUpdateCreditProfileResponse = m_EnterpriseCreditProfileManagementServiceImpl.updateCreditProfile(request);

			
			//test_DownStream_Services_Ping();
			//test_ping();
/*
			ConsumerCreditProfileInfo consumerCreditProfileInfo = new ConsumerCreditProfileInfo();
			
			Identification id = new Identification();
			BusinessRegistration value = new BusinessRegistration();
			value.setBusinessRegistrationNumber("123123123");
			value.setBusinessRegistrationTypeCd("BIC");
			id.setBusinessRegistration(value);
			
			BillingAccountIdentification bi = new BillingAccountIdentification();
			bi.setBillingSystemId(130);
			bi.setBillingAccountNumber("70822294");
			id.setBillingAccountIdentification(bi);
			consumerCreditProfileInfo.setIdentification( id );
			AuditInfo auditInfo = new AuditInfo();
			auditInfo.setUserId("testUserId");
			auditInfo.setOriginatorApplicationId("1234");
			UpdateCreditProfile parameters = new UpdateCreditProfile();
			parameters.setAuditInfo(auditInfo);
			parameters.setConsumerCreditProfileInfo(consumerCreditProfileInfo);
			UpdateCreditProfileResponse aUpdateCreditProfileResponse = m_EnterpriseCreditProfileManagementServiceImpl.updateCreditProfile(parameters);
			System.out.println("aUpdateCreditProfileResponse = " + aUpdateCreditProfileResponse);*/
			
		} catch (Throwable  e) {
			e.printStackTrace();
			throw e;
		}
		
	}


	public void sendCreditProfileInfoToWirelineTest() throws Exception {
		jmsSender = new JMSCreditProfileUpdateSender(jmsTemplate);
		String fileName="C:/projects/GitRepository/RTCA/crdmgt_18_oct/entprflmgt-ws-deploy/entprflmgt-webservices/src/test/java/com/telus/credit/entprflmgt/asyncprocessing/WLS_payload.xml";
		String wlnPaload = getResourceAsString(fileName);
		jmsSender.sendCreditProfileInfoToWireline(wlnPaload, null);
		System.out.print("Completed sendCreditProfileInfoToWirelineTest");
	}


	public void test_ping() throws ServiceException {
		Ping ping = new  Ping();
		System.out.println("ping = " + m_EnterpriseCreditProfileManagementServiceImpl.ping(ping ));
	}


public  void test_DownStream_Services_Ping() throws Throwable {
		ping_ConsumerCustomerManagementService();
		ping_WLNCreditProfileDataManagementService(); 
		ping_wirelessCreditProfileDataMgtService();
		ping_WLNCreditProfileManagementService();
		ping_WLNCreditProfileManagementProxyServicePortType();
		
	}


	private void ping_WLNCreditProfileDataManagementService() throws Throwable {

		try {
			m_WLNCreditProfileDataManagementServicePortType.ping();
		} catch (Throwable  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
		System.out.println("DONE");
	}
	private void ping_wirelessCreditProfileDataMgtService() throws Throwable {

		try {
			m_wirelessCreditProfileDataMgtServiceTgt.ping();
		} catch (Throwable  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
		System.out.println("DONE");
	}
	private void ping_WLNCreditProfileManagementService() throws Throwable {

		try {
			
			m_WLNCreditProfileManagementServicePortType.ping();
		} catch (Throwable  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
		System.out.println("DONE");
	}

	private void ping_ConsumerCustomerManagementService() throws Throwable {
		GetFullCustomerInfoByBillingAccountNumber getFullCustomerInfoReq = new GetFullCustomerInfoByBillingAccountNumber();
		getFullCustomerInfoReq.setBillingAccountNumber("70822294");
		getFullCustomerInfoReq.setBillingSystemId(130);
		
		try {
			Ping parameters  = new Ping();
			m_ConsumerCustomerManagementServicePortType.ping(parameters);
//			m_ConsumerCustomerManagementServicePortType.getFullCustomerInfoByBillingAccountNumber(getFullCustomerInfoReq);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
	}
	
	private void ping_WLNCreditProfileManagementProxyServicePortType() throws Throwable {

		try {
			m_WLNCreditProfileManagementProxyServicePortType.ping();
		} catch (Throwable  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//throw e;
		}
		System.out.println("DONE");
	}
	private String getResourceAsString(String fileName) throws Exception {
		//final File resourceFile = new File(getClass().getResource(fileName).getFile());
		 File resourceFile = new File(fileName);
		return FileUtils.readFileToString(resourceFile);
	}	
}
