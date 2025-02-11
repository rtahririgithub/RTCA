package com.telus.credit.crda.adapter;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ManualInquiryCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.MonthlyUDCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.OverrideCreditAssessmentRequest;
import com.fico.telus.rtca.blaze.RuleServicesBean;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.TestFiles;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.framework.test.TestUtil;


import junit.framework.TestCase;
@RunWith(TelusJUnitClassRunner.class)
@ContextConfiguration("classpath:telus-crd-crda-impl-spring.xml")
public class DecisioningEngineAdapterTest extends TestCase {
	@Autowired
    private DozerBeanMapper mapper;
	@Autowired
	RuleServicesBean aRuleServicesBean;
	
	DecisioningEngineAdapter m_decisioningEngineAdapter;  

	@Before
	public void setUp() throws Exception {
		super.setUp();
		m_decisioningEngineAdapter = new DecisioningEngineAdapter();
		m_decisioningEngineAdapter.setMapper(mapper);
		m_decisioningEngineAdapter.setRuleServicesBean(aRuleServicesBean);
	}
	@Test
	public void testPerformCreditAssessmentCreditAssessmentRequest() throws Throwable {
		performFicoCreditAssessment(TestFiles.FULL_ASSESSMENT_NEW_ACC_ASSESSMENT, new ExistingCustomerCreditAssessmentRequest());
		performFicoCreditAssessment(TestFiles.FULL_ASSESSMENT_REOPEN_ASSESSMENT, new ExistingCustomerCreditAssessmentRequest());
		performFicoCreditAssessment(TestFiles.FULL_ASSESSMENT_MONTHLY_CVUD, new MonthlyUDCreditAssessmentRequest());
		performFicoCreditAssessment(TestFiles.FULL_ASSESSMENT_GET_BUREAU_DATA, new ManualInquiryCreditAssessmentRequest());
		performFicoCreditAssessment(TestFiles.FULL_ASSESSMENT_AUTO_ASSESSMENT, new NewCustomerCreditAssessmentRequest());
		performFicoCreditAssessment(TestFiles.OVERRIDE_ASSESSMENT_MANUAL_CREDITVALUE_OVERRIDE, new OverrideCreditAssessmentRequest());
		performFicoCreditAssessment(TestFiles.OVERRIDE_ASSESSMENT_CANCEL_DEPOSIT_INV, new OverrideCreditAssessmentRequest());
	    }
	private void performFicoCreditAssessment(String requestFilename,
			com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest  ficoRequest)
			throws JAXBException, Throwable,
			EnterpriseCreditAssessmentPolicyException,
			EnterpriseCreditAssessmentServiceException {
		 m_decisioningEngineAdapter.setFicoCreditAssessmentRequest(ficoRequest);
		 PerformCreditAssessment performCreditAssessment = XMLUtility.createPerformCreditAssessmentRequest(requestFilename);
		 CreditAssessmentRequest aCreditAssessmentRequest_Src=performCreditAssessment.getCreditAssessmentRequest();
		 CreditAssessmentResultWrapper aCreditAssessmentResult = m_decisioningEngineAdapter.performCreditAssessment(aCreditAssessmentRequest_Src);
		 TestUtil.dump(aCreditAssessmentResult);
	}



	public void testPerformCreditAssessmentCreditAssessmentRequestConsumerCreditReportResponse() {
		fail("Not yet implemented");
	}

}
