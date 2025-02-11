package com.telus.credit.crda.dao.dcn;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import com.telus.credit.framework.test.TelusJUnitClassRunner;

import junit.framework.TestCase;
@RunWith(TelusJUnitClassRunner.class)
@ContextConfiguration("classpath:telus-crd-crda-impl-spring.xml")

public class DecisiongDaoImpFullAssessmentTest extends TestCase {
	@Autowired
	DecisiongDaoImpFullAssessment m_DecisiongDaoImpFullAssessment;
	public void testStoreDecisioningEngineInput() {
		fail("Not yet implemented");
		//m_DecisiongDaoImpFullAssessment.storeDecisioningEngineInput(creditAssessmentRequest, auditInfo, aCreditAssessmentTransactionDetails)
	}

}
