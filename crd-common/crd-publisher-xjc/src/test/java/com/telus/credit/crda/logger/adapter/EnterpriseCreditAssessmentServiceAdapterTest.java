package com.telus.credit.crda.logger.adapter;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.crda.logger.exception.CreditAssessmentLoggerServiceException;
import com.telus.credit.crda.logger.util.CreditAssessmentUtility;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.ServiceException;

import junit.framework.TestCase;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile = "test-logger-appCtx.properties")
@ContextConfiguration("classpath:test-eca-spring.xml")
public class EnterpriseCreditAssessmentServiceAdapterTest extends TestCase {

	@Autowired
	EnterpriseCreditAssessmentServiceAdapter m_enterpriseCreditAssessmentServiceAdapter;
	@Autowired
	CreditAssessmentUtility creditAssessmentUtility;

	// ApplicationContext context=null;

	protected void setUp() throws Exception {
		super.setUp();
		/*
		 * context = new
		 * ClassPathXmlApplicationContext("ws-cgw-client-test-spring);
		 * 
		 * m_creditGatewayServicePortType =
		 * (CreditGatewayServicePortType)context.getBean("CreditGatewayService")
		 * ;
		 */
	}

	
	@Test
	public void testGetLatestCreditAssessment()
			throws PolicyException, ServiceException, CreditAssessmentLoggerServiceException {
		// tests step 1 of logger service: Logs latest creditAssessmentDetails for a given custID.
		// creditAssessmentDetails are received from EnterpriseCreditAssessmentService and converted to XML.
		// Test 1: Assessment Type: FULL_ASSESSMENT, Assessment Sub-Type: AUTO_ASSESSMENT
		//CreditAssessmentDetails creditAssessmentDetails = m_enterpriseCreditAssessmentServiceAdapter
			//	.getLatestCreditAssessment(85118462);
		// Test 2: Assessment Type: FULL_ASSESSMENT, Assessment Sub-Type: NEW_ACC_ASSESSMENT
		//CreditAssessmentDetails creditAssessmentDetails = m_enterpriseCreditAssessmentServiceAdapter
			//	.getLatestCreditAssessment(31030112);
		// Test 3: Assessment Type: , Assessment Sub-Type: OVRD_ASSESSMENT
		CreditAssessmentDetails creditAssessmentDetails = m_enterpriseCreditAssessmentServiceAdapter
			.getLatestCreditAssessment(5197800);
		// Test 4: Assessment Type: , Assessment Sub-Type: MONTHLY_CVUD
		//CreditAssessmentDetails creditAssessmentDetails = m_enterpriseCreditAssessmentServiceAdapter
			//	.getLatestCreditAssessment(5197800);
		GetCreditAssessmentResponse getCreditAssessmentResponse = new GetCreditAssessmentResponse();
		getCreditAssessmentResponse.setCreditAssessmentDetails(creditAssessmentDetails);
		String creditAssessmentInStringForm = creditAssessmentUtility.convertObjectToXml(getCreditAssessmentResponse);
		//creditAssessmentUtility.log(creditAssessmentInStringForm, "_GetAsmt1.xml");
		//creditAssessmentUtility.log(creditAssessmentInStringForm, "_GetAsmt2.xml");
		//creditAssessmentUtility.log(creditAssessmentInStringForm, "_GetAsmt3.xml");
		creditAssessmentUtility.log(creditAssessmentInStringForm, "_GetAsmt4.xml");

	}

	public void testGetCreditAssessment() throws CreditAssessmentLoggerServiceException {
		// tests step 1 of logger service: Logs creditAssessmentDetails for a given creditAssessmentID.
		// creditAssessmentDetails are received from EnterpriseCreditAssessmentService and converted to XML.
		CreditAssessmentDetails creditAssessmentDetails = m_enterpriseCreditAssessmentServiceAdapter
				.getCreditAssessment(302809);
		GetCreditAssessmentResponse getCreditAssessmentResponse = new GetCreditAssessmentResponse();
		getCreditAssessmentResponse.setCreditAssessmentDetails(creditAssessmentDetails);
		String creditAssessmentInStringForm = creditAssessmentUtility.convertObjectToXml(getCreditAssessmentResponse);
		creditAssessmentUtility.log(creditAssessmentInStringForm, "_GetAsmt.xml");
	}

	@Test
	public void testPing() throws Throwable, Exception, FileNotFoundException, UnsupportedEncodingException {
		try {
			System.out.println("Ping result : \n" + m_enterpriseCreditAssessmentServiceAdapter.ping());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
