package com.telus.credit.crda.asmtclassification.dscn;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.util.EcrdaTestHelper;
import com.telus.credit.crda.util.EnvUtil;
import com.telus.credit.crda.util.TestFiles;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.framework.test.TestUtil;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.ServiceException;

import junit.framework.TestCase;

public class FullAssessmentAutoTest extends TestCase {

	private static String dir = "src/test/resources/";
	private static String filename = dir + TestFiles.FULL_ASSESSMENT_AUTO_ASSESSMENT; //"data/FullASmt/tmp_UC_FULL_AUTO.xml"; //TestFiles.FULL_ASSESSMENT_AUTO_ASSESSMENT_MinData;

	private Log log;
	FullAssessmentAuto m_aAssessment;
	EnterpriseCreditAssessmentServiceImpl pojo;
	
	protected void setUp() throws Exception {
		try {
			super.setUp();
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
			EnvUtil.setupTestEnv();		
			
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring-test.xml");
			m_aAssessment = (FullAssessmentAuto) m_ApplicationContext.getBean("FULL_ASSESSMENT_AUTO_ASSESSMENT");
			System.out.println("m_aAssessment is initilized: " + m_aAssessment );
			
			pojo = (EnterpriseCreditAssessmentServiceImpl)m_ApplicationContext.getBean("enterpriseCreditAssessmentServiceTarget");
			log = LogFactory.getLog(FullAssessmentAutoUnifiedCreditTest.class);
			log.info("pojo is " + pojo );

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();			
		}
	}
	
	public void testPerformCreditAssessment() throws Exception {
		String dir = "src/test/resources/data/FullASmt/";
        String responseFolder = dir + "response/";
        //String xmlFile = "tmp_UC_FULL_AUTO.xml";
        //xmlFile = "FULL_ASSESSMENT_AUTO_ASSESSMENT_MinData.xml";
        //xmlFile = "FULL_ASSESSMENT_AUTO_ASSESSMENT.xml";
        PerformCreditAssessment aPerformCreditAssessment = EcrdaTestHelper.createPerformCreditAssessmentRequest(filename);
        int custID = 841382;
        long cpID = 2304345590L;
        aPerformCreditAssessment.getCreditAssessmentRequest().setCustomerID(custID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).getCreditCustomerInfo().setCustomerID(custID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).getCreditProfileData().setCustomerID(custID);
        ((FullCreditAssessmentRequest) aPerformCreditAssessment.getCreditAssessmentRequest()).getCreditProfileData().setCreditProfileID(cpID);
        
		CreditAssessmentTransactionResult aCreditAssessmentTrxResult = pojo.performCreditAssessment(
				aPerformCreditAssessment.getCreditAssessmentRequest(), 
				aPerformCreditAssessment.getAuditInfo());
		
		PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = new PerformCreditAssessmentResponse();
		aPerformCreditAssessmentResponse.setCreditAssessmentTransactionResult(aCreditAssessmentTrxResult);
		log.info("Response: " + EcrdaTestHelper.convertToXml(aPerformCreditAssessmentResponse));

	}

	@Test
	public void testPerformDecisioningEngineCreditAssessment() throws JAXBException, Throwable{
    	PerformCreditAssessment performCreditAssessment = (PerformCreditAssessment) XMLUtility.convertXMLToObject(PerformCreditAssessment.class, filename);
		CreditAssessmentRequest creditAssessmentRequest = performCreditAssessment.getCreditAssessmentRequest();
		CreditAssessmentResultWrapper aCreditAssessmentResult = m_aAssessment.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
		TestUtil.dump(aCreditAssessmentResult);
    }
}
