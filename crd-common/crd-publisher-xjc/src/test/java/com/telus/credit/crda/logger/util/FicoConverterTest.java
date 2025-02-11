package com.telus.credit.crda.logger.util;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest;
import com.fico.telus.blaze.webservice.PerformCreditAssessment;
import com.telus.credit.crda.logger.exception.CreditAssessmentLoggerServiceException;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

import junit.framework.TestCase;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile = "test-logger-appCtx.properties")
@ContextConfiguration("classpath:test-fico-converter-spring.xml")
public class FicoConverterTest extends TestCase {

	@Autowired
	EnterpriseAsmtToFICOAsmtInputConverterUtil enterpriseAsmtToFICOAsmtInputConverterUtil;
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
	public void testConvertCreditAssessmentDetailsToFICOInput() throws CreditAssessmentLoggerServiceException, JAXBException {
		// test Step 2: Convert EnterpriseCreditAssessmentService output (CreditAssessmentDetails)
		// to FICO input (CreditAssessmentRequest). Also testing dozer mapping conversion here by doing this.
		
		//String getAssessmentResponseXmlFilePath = creditAssessmentUtility.getFilePathGivenAppender("_GetAsmt1.xml");
		String getAssessmentResponseXmlFilePath = creditAssessmentUtility.getFilePathGivenAppender("_GetAsmt2.xml");
		//String getAssessmentResponseXmlFilePath = creditAssessmentUtility.getFilePathGivenAppender("_GetAsmt3.xml");
		//String getAssessmentResponseXmlFilePath = creditAssessmentUtility.getFilePathGivenAppender("_GetAsmt4.xml");
		
		GetCreditAssessmentResponse getCreditAssessmentResponse = new GetCreditAssessmentResponse();
		
		getCreditAssessmentResponse =
				(GetCreditAssessmentResponse)creditAssessmentUtility.convertXMLToObject(getCreditAssessmentResponse, getAssessmentResponseXmlFilePath);
		
		CreditAssessmentDetails creditAssessmentDetails = getCreditAssessmentResponse.getCreditAssessmentDetails();
		
		CreditAssessmentRequest ficoInput = enterpriseAsmtToFICOAsmtInputConverterUtil.convertCreditAssessmentDetailsToFICOInput(creditAssessmentDetails);
		PerformCreditAssessment performCreditAssessment = new PerformCreditAssessment();
		performCreditAssessment.setCreditAssessmentRequest(ficoInput);
		String ficoInputinStringForm = creditAssessmentUtility.convertObjectToXml(performCreditAssessment);
		//creditAssessmentUtility.log(ficoInputinStringForm, "_FicoInput1.xml");
		creditAssessmentUtility.log(ficoInputinStringForm, "_FicoInput2.xml");
		//creditAssessmentUtility.log(ficoInputinStringForm, "_FicoInput3.xml");
		//creditAssessmentUtility.log(ficoInputinStringForm, "_FicoInput4.xml");

	}

}

