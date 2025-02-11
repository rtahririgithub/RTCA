package com.telus.credit.crda.logger.adapter;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.webservice.PerformCreditAssessment;
import com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse;
import com.telus.credit.crda.logger.exception.CreditAssessmentLoggerServiceException;
import com.telus.credit.crda.logger.util.CreditAssessmentUtility;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

import junit.framework.TestCase;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile = "test-logger-appCtx.properties")
@ContextConfiguration("classpath:test-decision-engine-spring.xml")
public class CrdaDecisionEngineServiceAdapterTest extends TestCase {

	@Autowired
	CRDADecisionEngineServiceAdapter crdaDecisionEngineServiceAdapter;
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
	public void testPerformCreditAssessment() throws JAXBException, CreditAssessmentLoggerServiceException {
		// Step 4: Call FICO using FICO input (CreditAssessmentRequest) and get
		// FICO output (CreditAssessmentResult) need creditAssessmentRequest
		//String ficoInputFilePath = creditAssessmentUtility.getFilePathGivenAppender("_FicoInput1.xml");
		String ficoInputFilePath = creditAssessmentUtility.getFilePathGivenAppender("_FicoInput2.xml");
		//String ficoInputFilePath = creditAssessmentUtility.getFilePathGivenAppender("_FicoInput3.xml");
		
		PerformCreditAssessment performCreditAssessment = new PerformCreditAssessment();
		performCreditAssessment = 
				(PerformCreditAssessment) creditAssessmentUtility.convertXMLToObject(performCreditAssessment, ficoInputFilePath);
		
		//CreditAssessmentResult ficoCreditAssessmentResult = 
			//	crdaDecisionEngineServiceAdapter.performCreditAssessment(85118462, performCreditAssessment.getCreditAssessmentRequest());
		CreditAssessmentResult ficoCreditAssessmentResult = 
				crdaDecisionEngineServiceAdapter.performCreditAssessment(2872183, performCreditAssessment.getCreditAssessmentRequest());
		//CreditAssessmentResult ficoCreditAssessmentResult = 
			//	crdaDecisionEngineServiceAdapter.performCreditAssessment(2430998, performCreditAssessment.getCreditAssessmentRequest());
		//CreditAssessmentResult ficoCreditAssessmentResult = 
		//	crdaDecisionEngineServiceAdapter.performCreditAssessment(5197800, performCreditAssessment.getCreditAssessmentRequest());
		
		// Step 5: Convert FICO output (ficoCreditAssessmentResult) to string/XML and log this
		PerformCreditAssessmentResponse ficoResponse = new PerformCreditAssessmentResponse();
		ficoResponse.setCreditAssessmentResult(ficoCreditAssessmentResult);
		String ficoOutputInStringForm = creditAssessmentUtility.convertObjectToXml(ficoResponse);
		
		//creditAssessmentUtility.log(ficoOutputInStringForm, "_FICOOutput1.xml");
		creditAssessmentUtility.log(ficoOutputInStringForm, "_FICOOutput2.xml");
		//creditAssessmentUtility.log(ficoOutputInStringForm, "_FICOOutput3.xml");
		//creditAssessmentUtility.log(ficoOutputInStringForm, "_FICOOutput4.xml");
	}

}
