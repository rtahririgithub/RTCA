package com.telus.credit.entprflmgt.client;


import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.credit.entprflmgt.domain.BillingAccountIdentification;
import com.telus.credit.entprflmgt.domain.BusinessRegistration;
import com.telus.credit.entprflmgt.domain.ConsumerCreditIdentification;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.entprflmgt.util.TestHelper;
import com.telus.credit.entprflmgt.webservice.impl.EnterpriseCreditProfileManagementServiceImpl;
import com.telus.credit.util.EnvUtil;
import com.telus.credit.wlnprfldmgt.domain.DriverLicense;
import com.telus.credit.wlsprfldmgt.domain.UpdateWlsConsumerCreditProfile;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;

@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestWLSCreditProfileDataMgmtSvcIntermediatorTest {

	private static final Log log = LogFactory.getLog(TestWLSCreditProfileDataMgmtSvcIntermediatorTest.class);
	
	@Autowired
	@Qualifier("wlsCreditProfileUpdater")
	private WLSCrdProfileUpdater m_wlsCrdProfileUpdater;
	
	@Autowired
	//@Qualifier("wirelessCreditProfileDataMgtService")
	private EnterpriseCreditProfileManagementServiceImpl m_EnterpriseCreditProfileManagementServiceImpl;
	
	
	public TestWLSCreditProfileDataMgmtSvcIntermediatorTest() {
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
			//ClassPathXmlApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext(EnvUtil.resourcesFolder);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void test_ping() throws ServiceException 
	{
		PingResponse responseStr = m_EnterpriseCreditProfileManagementServiceImpl.ping(new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping());
		System.out.println("Response String: " + responseStr);
	}
	
	
	@Test
	public void test_WLSCreditProfileDataManagementServiceFromXMLfile() 
			throws Throwable {
		String nodeName = "updateCreditProfile";
		String xmlFile = "C:/Users/x158136/Documents/telusProjectSrc/Credit-crdmgt/entprflmgt-ws-deploy/entprflmgt-webservices/src/test/data/WLSCrdPrfUpdateReqest.xml";

		UpdateCreditProfile aUpdateCreditProfile = (UpdateCreditProfile)TestHelper.
				convertXmlCommon(xmlFile, nodeName, UpdateCreditProfile.class);
		
		
		/*aUpdateCreditProfile = 
				(UpdateCreditProfile) TestHelper.convertXMLToObject( UpdateCreditProfile.class,
						"C:/Users/x158136/Documents/telusProjectSrc/Credit-crdmgt/entprflmgt-ws-deploy/entprflmgt-webservices/src/test/data/WLSCrdPrfUpdateReqest.xml");
					*/
		//"src/test/data/WLSCrdPrfUpdateReqest.xml");
		/*AuditInfo auditInfo = new AuditInfo();
		auditInfo.setUserId("testUserId");
		auditInfo.setOriginatorApplicationId("1234");
		UpdateCreditProfile parameters = new UpdateCreditProfile();
		
		parameters.setConsumerCreditProfileInfo(consumerCreditProfileInfo);
		parameters.setAuditInfo(auditInfo);*/
		m_EnterpriseCreditProfileManagementServiceImpl.updateCreditProfile(aUpdateCreditProfile);
	}
	
	@Test
	public void test_WLSCreditProfileDataManagementServicePortType() 
			throws CreditProfileUpdateException, JAXBException, ServiceException {
		
		UpdateWlsConsumerCreditProfile updateWLSConsumerCreditProfile = 
				new UpdateWlsConsumerCreditProfile();
		/*UpdateWlsConsumerCreditProfile wlsCrdProfileUpdateRequest = 
				(UpdateWlsConsumerCreditProfile) TestHelper.
				convertXMLToObject( updateWLSConsumerCreditProfile,
						"src/test/data/WLSCreditProfileUpdateRequest.xml");
		
		 //test_ping();
		 m_wlsCrdProfileUpdater.updateCreditProfile(
				 wlsCrdProfileUpdateRequest.getPersonalCreditInformation(), 
				 wlsCrdProfileUpdateRequest.getAuditInfo(), 
				 false);
		aWLSCrdProfileUpdater.updateCreditProfile(
				updateWLSConsumerCreditProfileObj.getAuditInfo(), 
				updateWLSConsumerCreditProfileObj.getAccountNumber(),
				updateWLSConsumerCreditProfileObj.getBusinessRegistration(),
				updateWLSConsumerCreditProfileObj.getPersonalCreditInformation() );*/
		
		ConsumerCreditProfileInfo consumerCreditProfileInfo = new ConsumerCreditProfileInfo();
		
		Identification id = new Identification();
		BusinessRegistration value = new BusinessRegistration();
		value.setBusinessRegistrationNumber("123123123");
		value.setBusinessRegistrationTypeCd("BIC");
		id.setBusinessRegistration(value);
		
		BillingAccountIdentification bi = new BillingAccountIdentification();
		bi.setBillingSystemId(130);
		bi.setBillingAccountNumber("70397074");
		id.setBillingAccountIdentification(bi);
		
		ConsumerCreditIdentification cid = new ConsumerCreditIdentification();
		
		DriverLicense dl = new DriverLicense();
		dl.setDriverLicenseNum("11111112");
		dl.setProvinceCd("BC");
		cid.setDriverLicense(dl);
		consumerCreditProfileInfo.setCreditIdentification(cid );
		consumerCreditProfileInfo.setIdentification( id );
		
		//consumerCreditProfileInfo.setCreditValueCd("E");
		//consumerCreditProfileInfo.setComment("Update Credit Value Code");
		//com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setUserId("testUserId");
		auditInfo.setOriginatorApplicationId("1234");
		UpdateCreditProfile parameters = new UpdateCreditProfile();
		parameters.setConsumerCreditProfileInfo(consumerCreditProfileInfo);
		parameters.setAuditInfo(auditInfo);
		
		String xmlObjStr = TestHelper.convertObjectToXml(parameters);
		System.out.println("xml Obj: " + xmlObjStr );
		//m_EnterpriseCreditProfileManagementServiceImpl.updateCreditProfile(parameters );

		//m_wlsCrdProfileUpdater.updateCreditProfile(consumerCreditProfileInfo, auditInfo);
	}
	
	//@Test
	public void test_convertToServicePayload() throws JAXBException {
		UpdateCreditProfile updateCreditProfile = new UpdateCreditProfile();
		UpdateCreditProfile updateCreditProfileObj = (UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/data/SampleEnterpriseCreditProfileManagementServiceRequestWithWLSBAN.xml" );
		
		String servicePayload = m_wlsCrdProfileUpdater.convertToServicePayload(updateCreditProfileObj.getConsumerCreditProfileInfo(),
				updateCreditProfileObj.getAuditInfo());
		System.out.println("Service payload: " + servicePayload );
	}
	
	//@Test
	public void test_getAyncUpdateCreditProfile() throws JAXBException {
		UpdateCreditProfile updateCreditProfile = new UpdateCreditProfile();
		UpdateCreditProfile updateCreditProfileObj = (UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/data/SampleEnterpriseCreditProfileManagementServiceRequestWithWLSBAN.xml" );
		
		Object obj = m_wlsCrdProfileUpdater.buildAsyncServiceRequest(updateCreditProfileObj.getConsumerCreditProfileInfo(),
				updateCreditProfileObj.getAuditInfo() );
		String xmlObjStr = TestHelper.convertObjectToXml(obj);
		System.out.println("xml Obj: " + xmlObjStr );
	}
}
