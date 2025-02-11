package com.telus.credit.entprflmgt.test;


import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.EnterpriseCreditProfileManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;

//@TelusConfig(configAppCtxFile="appCtx-enterpriseCrd.properties")
@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
public class EnterpriseCreditProfileMgtSvc1_1Test {

	private static final Log log = LogFactory.getLog(EnterpriseCreditProfileMgtSvc1_1Test.class);
	
	@Autowired
	private EnterpriseCreditProfileManagementServicePortType m_impl;
	
	@Autowired
    private DozerBeanMapper m_mapper;
    
	
	@Test
	public void test_ping() throws ServiceException 
	{
		String responseStr = m_impl.ping();
		System.out.println("Response String: " + responseStr);
	}
	
	@Test
	public void test_dozer() throws JAXBException {
		com.telus.credit.entprflmgt.domain_v1_1.UpdateCreditProfile updateCreditProfile = new com.telus.credit.entprflmgt.domain_v1_1.UpdateCreditProfile();
		com.telus.credit.entprflmgt.domain_v1_1.UpdateCreditProfile updateCreditProfileObj = (com.telus.credit.entprflmgt.domain_v1_1.UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/conf/CreditProfile_v1_1.xml" );
		
		 com.telus.credit.entprflmgt.domain_2_2.ConsumerCreditProfileInfo consumerCreditProfileInfo_v2 = m_mapper.map(updateCreditProfileObj.getConsumerCreditProfileInfo(), com.telus.credit.entprflmgt.domain_2_2.ConsumerCreditProfileInfo.class);
		 UpdateCreditProfile updateCreditProfile2 = new UpdateCreditProfile();
		 updateCreditProfile2.setConsumerCreditProfileInfo(consumerCreditProfileInfo_v2);
		 updateCreditProfile2.setAuditInfo(updateCreditProfileObj.getAuditInfo());
		 
		 String xml2 = TestHelper.convertObjectToXml(updateCreditProfile2);
		 System.out.println("v2_2 xml:" + xml2 );
		 
	}
	
	/*//@Test
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
	}*/
}
