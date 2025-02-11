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
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessRequest;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessResponse;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessResponse.AssessedCreditWorthiness;
import com.telus.credit.wlnprflmgtpxy.domain.enterprise.common.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType;

@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
public class WLNCreditProfileMgtProxySvcIntermediatorTest {

	private static final Log log = LogFactory.getLog(WLNCreditProfileMgtProxySvcIntermediatorTest.class);
	@Autowired
	private WLNCreditProfileMgtProxyServiceIntermediator m_wlnCreditProfileMgtProxyServiceIntermediator;
	
	@Autowired
	@Qualifier("wirelineCreditProfileMgtProxyService")
	private WLNCreditProfileManagementProxyServicePortType m_wlnCreditProfileManagementProxyServicePortType;
	
	@Test
    public void test_wlnCreditProfileMgtServiceIntermediator() throws Exception
    {
		m_wlnCreditProfileMgtProxyServiceIntermediator.assessCreditWorthiness(8000, "1234", "testUserId");
		System.out.println( "Call succeeded successfully." ); 
		
    }
	
	public WLNCreditProfileMgtProxySvcIntermediatorTest() {
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
	
	//@Test
	public void test_wlnCreditProfileMgtServiceProxy() throws Exception {
		AuditInfo auditInfo = new AuditInfo( );
	    auditInfo.setUserId( "testUserId" );
	    auditInfo.setOriginatorApplicationId( "1234" );


	    AssessCreditWorthinessRequest creditWorthinessRequest = new AssessCreditWorthinessRequest();
	    creditWorthinessRequest.setApplicationID("1234");
	    creditWorthinessRequest.setCreditAssessmentTypeCd("FULL_ASSESSMENT");
	    creditWorthinessRequest.setCreditAssessmentSubTypeCd("AUTO_ASSESSMENT");
	    creditWorthinessRequest.setCustomerID(8000);
	    creditWorthinessRequest.setLineOfBusiness("WIRELINE");
	    AssessedCreditWorthiness responseObj = m_wlnCreditProfileManagementProxyServicePortType.assessCreditWorthiness( creditWorthinessRequest, auditInfo );
	    AssessCreditWorthinessResponse response = new AssessCreditWorthinessResponse();
	    response.setAssessedCreditWorthiness(responseObj);
	    String responseStr = TestHelper.convertObjectToXml(response);
	    System.out.println("Response XML:" + responseStr );
	}
	
}
