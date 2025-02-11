package com.telus.credit.crda.dao.dcn;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


import com.telus.credit.crda.dao.mgmt.CreditAssessmentDataMgmtDaoImpl;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.framework.test.TelusJUnitClassRunner;


public class DecisioningDaoImpCommonTest  {
	@Autowired
	DecisioningDaoImpCommon m_DecisioningDaoImpCommon;
	@Autowired
	CreditAssessmentDataMgmtDaoImpl m_CreditAssessmentDataMgmtDaoImpl;
	
	
	@Test
	public void testStoreCreditBureauResult() throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException {
		//TODO test storeCreditBureauResult
		long CARID=1;

		//from xml files : create consumerCreditReportResponse, creditAssessmentRequest, auditInfo
		//use an exist CARID
		//store data
		//m_DecisioningDaoImpCommon.storeCreditBureauResult(consumerCreditReportResponse, dbCARID, creditAssessmentRequest, auditInfo);
		m_DecisioningDaoImpCommon.storeCreditBureauResult(null, CARID, null, null);
		//check the db tables
		//call get the data
		CreditBureauResult creditBureauResult_InDB = m_CreditAssessmentDataMgmtDaoImpl.getCreditBureauTransResultByCustID(CARID);
		//compare with input
		
		CreditAssessmentDetails aCreditAssessmentDetails = m_CreditAssessmentDataMgmtDaoImpl.getCreditAssessment(CARID);
		fail("Not yet implemented");
	}

	/*
	
	@Test
	public void testStoreSingleCompletedDecisioningTrx() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreDecisioningEngineInput() {
		fail("Not yet implemented");
	}

	@Test
	public void testStoreDecisioningEngineResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateInternalCreditDecisionTransactionResult() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateInternalCreditDecisionTransactionResultDetails() {
		fail("Not yet implemented");
	}


	@Test
	public void testCreateCreditBureauTransaction() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateCreditBureauTransactionDetails() {
		fail("Not yet implemented");
	}
*/

}
