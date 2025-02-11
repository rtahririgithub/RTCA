package com.telus.credit.crda.asmtclassification.dscn;

import javax.xml.bind.JAXBException;

import junit.framework.TestCase;

import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rsa.cryptoj.c.aP;
import com.telus.credit.crda.util.EcrdaTestHelper;
import com.telus.credit.crda.util.EnvUtil;
import com.telus.credit.crda.util.TestFiles;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;

//@ContextConfiguration("classpath:telus-crd-crda-impl-spring.xml")
public class FullAssessmentManualAssessmentTest extends TestCase {
	private static String dir = "src/test/resources/";
	private static String filename = dir + TestFiles.FULL_ASSESSMENT_MANUAL_ASSESSMENT;
	
	EnterpriseCreditAssessmentServiceImpl pojo;

	protected void setUp() throws Exception {
		try {
			super.setUp();
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
			EnvUtil.setupTestEnv();		
			
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring-test.xml");
			pojo = (EnterpriseCreditAssessmentServiceImpl)m_ApplicationContext.getBean("enterpriseCreditAssessmentServiceTarget");
			System.out.println("EnterpriseCreditAssessmentServiceImpl pojo initilized: " + pojo );

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();			
		}
	}
	
	@Test
	public void testPerformCreditAssessment() throws JAXBException, Throwable {
		PerformCreditAssessment performCreditAssessment = EcrdaTestHelper.createPerformCreditAssessmentRequest(filename);
		CreditAssessmentRequest creditAssessmentRequest = performCreditAssessment.getCreditAssessmentRequest();		
		AuditInfo auditInfo = performCreditAssessment.getAuditInfo();
		CreditAssessmentTransactionResult aCreditAssessmentTrxResult = pojo.performCreditAssessment(creditAssessmentRequest, auditInfo );
		PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = new PerformCreditAssessmentResponse();
		aPerformCreditAssessmentResponse.setCreditAssessmentTransactionResult(aCreditAssessmentTrxResult);
		System.out.println( "output: " + EcrdaTestHelper.convertToXml(aPerformCreditAssessmentResponse));
	}
	//@Test
	public void testPerformDecisioning() {
		fail("Not yet implemented");
	}
	//@Test
	public void testPerformDecisioningEngineCreditAssessment() throws JAXBException, Throwable{
/*    	PerformCreditAssessment performCreditAssessment = new PerformCreditAssessment();
    	performCreditAssessment = (PerformCreditAssessment) XMLUtility.convertXMLToObject(performCreditAssessment, filename);
		CreditAssessmentRequest creditAssessmentRequest = performCreditAssessment.getCreditAssessmentRequest();
		//CreditAssessmentResultWrapper aCreditAssessmentResult = m_ReAssessment.performDecisioningEngineCreditAssessment(creditAssessmentRequest);
		CreditAssessmentTransactionResult aCreditAssessmentTrxResult = pojo.performCreditAssessment(creditAssessmentRequest, auditInfo );
		PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = new PerformCreditAssessmentResponse();
		aPerformCreditAssessmentResponse.setCreditAssessmentTransactionResult(aCreditAssessmentTrxResult);
		EcrdaTestHelper.writeToFile(dir + "response/" + filename, aPerformCreditAssessmentResponse);
		System.out.println( "Response was created: " + dir + "response/" + filename );
*/    }

}
