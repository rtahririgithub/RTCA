package com.telus.credit.crda.asmtclassification.dscn;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.util.EnvUtil;
import com.telus.credit.crda.util.TestFiles;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.framework.test.TestUtil;

public class FullAssessmentReopenAssessmentTest extends TestCase {

	private static String dir = "src/test/resources/";
	private static String filename = dir + TestFiles.FULL_ASSESSMENT_REOPEN_ASSESSMENT;
	
	EnterpriseCreditAssessmentServiceImpl pojo;
	FullAssessmentReopenAssessment m_aAssessment;

	protected void setUp() throws Exception {
		try {
			super.setUp();
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
			EnvUtil.setupTestEnv();		
			
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring-test.xml");
			m_aAssessment = (FullAssessmentReopenAssessment) m_ApplicationContext.getBean("FULL_ASSESSMENT_REOPEN_ASSESSMENT");
			System.out.println("m_aAssessment is initilized: " + m_aAssessment );

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();			
		}
	}
	
	public void testPerformDecisioning() {
		fail("Not yet implemented");
	}
	

	@Test
	public void testPerformDecisioningEngineCreditAssessment() throws JAXBException, Throwable{
    	PerformCreditAssessment performCreditAssessment = XMLUtility.createPerformCreditAssessmentRequest(filename);
		CreditAssessmentRequest creditAssessmentRequest = performCreditAssessment.getCreditAssessmentRequest();
		CreditAssessmentResultWrapper aCreditAssessmentResult = m_aAssessment.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
		TestUtil.dump(aCreditAssessmentResult);
    }
}
