package com.telus.credit.crda.stgy.eval.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.credit.crda.adapter.CreditGatewayAdapter;
import com.telus.credit.crda.dao.dcn.DecisiongDaoImpFullAssessment;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.ent.AuditInfo;

import junit.framework.TestCase;

public class BureauDataRequiredAssessmentEvaluationProcessorTest extends TestCase {
	@Autowired
	CreditGatewayAdapter m_creditGatewayAdapter;
	@Autowired
	DecisiongDaoImpFullAssessment m_decisioningDao;
	
    @Autowired 
    EnterpriseCreditAssessmentServiceImpl m_entAsmtServiceImpl;
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPerformAssessmentEvaluation() throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
         CreditAssessmentRequest aCreditAssessmentRequest=null;
	     AuditInfo auditInfo=null;
	     CreditAssessmentTransactionDetails creditAssessmentTransactionDetails=null;
	     boolean failOverIndicator=true;
	     try {
	        //call cgw
	        ConsumerCreditReportResponse consumerCreditReportResponse = m_creditGatewayAdapter.pullConsumerCreditReport(aCreditAssessmentRequest, auditInfo, failOverIndicator, creditAssessmentTransactionDetails);

	        //store cgw response
	            long mDbCreditBureauTransactionId = m_decisioningDao.storeCreditBureauResult(
	                    consumerCreditReportResponse,
	                    creditAssessmentTransactionDetails.getCreditAssessmentID(),
	                    aCreditAssessmentRequest,
	                    auditInfo);
	            creditAssessmentTransactionDetails.setDbCreditBureauTransactionId(mDbCreditBureauTransactionId);
	            
				CreditAssessmentDetails aGetCreditAssessment= m_entAsmtServiceImpl.getCreditAssessment(creditAssessmentTransactionDetails.getCreditAssessmentID(), auditInfo);
				//compare result
	        } catch (Throwable e) {
	            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
	                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
	                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
	        }
	}


}
