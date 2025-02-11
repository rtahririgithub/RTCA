/*package com.telus.credit.entprflmgt.client;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.credit.entprflmgt.TestConsumerCreditProfileInfoFactory;
import com.telus.credit.entprflmgt.webservice.impl.EnterpriseCreditProfileManagementServiceImpl;
//import com.telus.credit.util.EnvUtil;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;


public class WLNCrdProfileUpdaterTest {
	
	private EnterpriseCreditProfileManagementServiceImpl pojo;

	public WLNCrdProfileUpdaterTest(){
		try {
			setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void setUp() throws Exception {
		try {
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
			//EnvUtil.setupTestEnv();			
			ClassPathXmlApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext(EnvUtil.resourcesFolder);
			pojo = (EnterpriseCreditProfileManagementServiceImpl) m_ApplicationContext.getBean("entCrdProfileMgtServicePojoTgt");
			//aWCDMPojo= (WLSCreditManagmentServicePojo) m_ApplicationContext.getBean("WLSCreditManagementServiceTarget");
			//EnvDetailUtil aEnvDetailUtil = (EnvDetailUtil) m_ApplicationContext.getBean("EnvDetailUtil");
			//aEncryptUtil= (EncryptUtil) m_ApplicationContext.getBean("Encryptor");

			} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Test
	public void testConvertToServicePayload() throws IOException, ServiceException {
		final File resourceFile = new File(getClass().getResource("WLNDataMgtPayload.xml").getFile());
		String expected = FileUtils.readFileToString(resourceFile).replace("\r\n", "");
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setChannelOrganizationId("channelId");
		
		WLNCrdProfileUpdater wlnUpdater = new WLNCrdProfileUpdater(null);
		String requestXml = wlnUpdater.convertToServicePayload(TestConsumerCreditProfileInfoFactory.newInstance(), auditInfo);
		Assert.assertEquals(expected, requestXml);
		
		System.out.println("ping: " + pojo.ping() );
	}

}
*/