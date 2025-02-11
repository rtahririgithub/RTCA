/*package com.telus.credit.crda.adapter;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fico.telus.rtca.blaze.RuleServicesBean;
import com.fico.telus.rtca.blaze.RuleServicesException;
import com.telus.credit.crda.domain.PerformCreditAssessment;
import com.telus.credit.crda.util.TestHelper;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_1.EnterpriseCreditAssessmentServicePortType;

import junit.framework.TestCase;

public class RuleServicesBeanTest extends TestCase {
	ApplicationContext context=null;
	RuleServicesBean m_RuleServicesBean;
	protected void setUp() throws Exception {
		super.setUp();
		context = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring.xml");
		m_RuleServicesBean = (RuleServicesBean)context.getBean("RuleServicesBean");
		

	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLastNameMismatch() {
		String testname ="testLastNameMismatch : ";
		try {
			String filename = "test/data/FicoPerformCreditAsmtInput.xml";
			
			
			com.fico.telus.blaze.webservice.PerformCreditAssessment performCreditAssessment1 = new com.fico.telus.blaze.webservice.PerformCreditAssessment();
			com.fico.telus.blaze.webservice.PerformCreditAssessment  performCreditAssessment2= null;

			performCreditAssessment2= (com.fico.telus.blaze.webservice.PerformCreditAssessment) TestHelper.convertXMLToObject(performCreditAssessment1, filename);
			
			com.fico.telus.blaze.webservice.CreditAssessmentInput creditAssessmentInput = performCreditAssessment2.getCreditAssessmentInput();
			
			//prepare test 
	
			com.fico.telus.blaze.webservice.CreditAssessmentResult ficocreditAssessmentResult ;



			//***************** SAME LASTNAME 
			creditAssessmentInput.getBaseCustomer().setLastName("LASTNAME");
			creditAssessmentInput.getCreditBureauResult().setLastName("LASTNAME");
			System.out.println("DT1 name = " + "LASTNAME");
			System.out.println("CreditBureauResult name = " + "LASTNAME");

			ficocreditAssessmentResult = m_RuleServicesBean.performCreditAssessment(creditAssessmentInput);
			assertFalse(
					testname + 
					"Fico AssessmentMessageCode =" + 
					ficocreditAssessmentResult.getAssessmentMessageCode() + 
					" for LastNames :"+
					"LASTNAME"+
					" and LASTNAME "
					,"DFNAM01".equals(ficocreditAssessmentResult.getAssessmentMessageCode()));
			
			

			//***************** DIFF lastnames
			System.out.println("DT1 name = " + "LASTNAME");
			System.out.println("CreditBureauResult name = " + "DIFFLASTNAME");
			
			creditAssessmentInput.getBaseCustomer().setLastName("LASTNAME");
			creditAssessmentInput.getCreditBureauResult().setLastName("DIFFLASTNAME");
			ficocreditAssessmentResult = m_RuleServicesBean.performCreditAssessment(creditAssessmentInput);
			assertTrue(
					testname + 
					"Fico AssessmentMessageCode =" + 
					ficocreditAssessmentResult.getAssessmentMessageCode() + 
					" for LastNames :"+
					"LASTNAME"+
					" and DIFFLASTNAME" 					
					, "DFNAM01".equals(ficocreditAssessmentResult.getAssessmentMessageCode()));


			String[] nonAlphCharList={"\"","~","`","!","@","#","$","%","^","","&","*","(",")","_","-","+","=","}","]","{","[","|",":",";","'","<",",",">",".","?","/","1"};
			for (int i = 0; i < nonAlphCharList.length; i++) {
				String DT1_lastnm = "PRESTR" + nonAlphCharList[i] + "POSTSTR";
				String creditBureauResult_lastnm = "PRESTRPOSTSTR";
				testLastnames_With_NoneAlphaChar(testname, creditAssessmentInput, DT1_lastnm,creditBureauResult_lastnm);

				//reverse it 
				DT1_lastnm = "PRESTRPOSTSTR";
				creditBureauResult_lastnm = "PRESTR" + nonAlphCharList[i] + "POSTSTR";
				testLastnames_With_NoneAlphaChar(testname, creditAssessmentInput, DT1_lastnm,creditBureauResult_lastnm);


			}
			
			//combo
			String DT1_lastnm = "PRESTR" + nonAlphCharList[1] + "POSTSTR";
			String creditBureauResult_lastnm = "PRESTR" + nonAlphCharList[2] + "POSTSTR";
			testLastnames_With_NoneAlphaChar(testname, creditAssessmentInput, DT1_lastnm,creditBureauResult_lastnm);						
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testLastnames_With_NoneAlphaChar(
			String testname,
			com.fico.telus.blaze.webservice.CreditAssessmentInput creditAssessmentInput,
			String DT1_lastnm, String creditBureauResult_lastnm)
			throws RuleServicesException {
		System.out.println("DT1 name = " + DT1_lastnm);
		System.out.println("CreditBureauResult name = " + creditBureauResult_lastnm);				

		com.fico.telus.blaze.webservice.CreditAssessmentResult ficocreditAssessmentResult;
		creditAssessmentInput.getBaseCustomer().setLastName(DT1_lastnm);
		creditAssessmentInput.getCreditBureauResult().setLastName(creditBureauResult_lastnm);
		ficocreditAssessmentResult = m_RuleServicesBean.performCreditAssessment(creditAssessmentInput);
		assertFalse(
				testname + 
				"Fico AssessmentMessageCode =" + 
				ficocreditAssessmentResult.getAssessmentMessageCode() + 
				" for DT1 Lasname ="+
				DT1_lastnm+
				" and creditBureauResult Lasname =" + 
				creditBureauResult_lastnm
				, 
				"DFNAM01".equals(ficocreditAssessmentResult.getAssessmentMessageCode()));
	}

}
*/