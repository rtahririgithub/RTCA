package com.telus.credit.crda.asmtclassification.nondcsn;


import java.util.Date;

import org.dozer.DozerBeanMapper;

import com.telus.credit.crda.asmtclassification.Assessment;
import com.telus.credit.crda.dao.nondcn.NonDecisioningTrxDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaValidationUtility;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.CreditProfileBr;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;


public class NonDecisioningAssessment extends Assessment {
    public NonDecisioningTrxDao m_overrideDao;
    public DozerBeanMapper m_mapper;

    public void setMapper(DozerBeanMapper aMapper) {
        this.m_mapper = aMapper;
    }

    public NonDecisioningAssessment(NonDecisioningTrxDao aOverrideDao) {
        this.m_overrideDao = aOverrideDao;
    }

    public CreditProfileBr m_creditProfileBr;

    public void setCreditProfileBr(CreditProfileBr mCreditProfileBr) {
        m_creditProfileBr = mCreditProfileBr;
    }

    @Override
    public CreditAssessmentTransactionResult performCreditAssessment(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        
        CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails = new CreditAssessmentTransactionDetails();
        //store the non dcn trx in DB
        populateCrdAsmtTrxCommonAttributes(aCreditAssessmentRequest, auditInfo,aCreditAssessmentTransactionDetails);
        
        LogUtil.infolog("-->perform NonDecisioningEngine CreditAssessment ");
        CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper = performNonDecisioningEngineCreditAssessment(aCreditAssessmentRequest);
        aCreditAssessmentTransactionDetails.setCreditDecisioningResult(decisioningCreditAssessmentResultWrapper);
        
        //lookup comment
        String commentTxt = InternalRules.lookupCommentByAsmtTypeSubtype(aCreditAssessmentRequest,aCreditAssessmentTransactionDetails);
        aCreditAssessmentTransactionDetails.setCommentTxt(commentTxt);
        
        //store trx in db
        m_overrideDao.storeCompleteNonDecisioningTrx(aCreditAssessmentRequest, auditInfo, aCreditAssessmentTransactionDetails);
        
        //dozer mapping eCrda aCreditAssessmentRequest to creditAssessmentTransactionDetails (to carry on input from input in the response)
        m_mapper.map(aCreditAssessmentRequest, aCreditAssessmentTransactionDetails);

        //populate aCreditAssessmentTransactionDetails to be returned to caller
        aCreditAssessmentTransactionDetails.setCreditAssessmentStatus(EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);
        aCreditAssessmentTransactionDetails.setMapper(m_mapper);
        aCreditAssessmentTransactionDetails.setCreditAssessmentStatusReasonCd(EnterpriseCreditAssessmentConsts.EMPTY_STR);

        return aCreditAssessmentTransactionDetails;
    }

    
    //Subclasses can override this method
    CreditAssessmentResultWrapper performNonDecisioningEngineCreditAssessment(CreditAssessmentRequest creditAssessmentRequest) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        return null;
    }
	private void populateCrdAsmtTrxCommonAttributes(
			CreditAssessmentRequest aCreditAssessmentRequest,
			AuditInfo auditInfo,
			CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails) {
		aCreditAssessmentTransactionDetails.setCreditAssessmentStatusDate(new Date());
        aCreditAssessmentTransactionDetails.setUserID((auditInfo != null ? auditInfo.getUserId() : null));
        aCreditAssessmentTransactionDetails.setCreditAssessmentDate(new Date());

        aCreditAssessmentTransactionDetails.setCustomerID(aCreditAssessmentRequest.getCustomerID());
        aCreditAssessmentTransactionDetails.setCreditAssessmentTypeCd(aCreditAssessmentRequest.getCreditAssessmentTypeCd());
        aCreditAssessmentTransactionDetails.setCreditAssessmentSubTypeCd(aCreditAssessmentRequest.getCreditAssessmentSubTypeCd());
        aCreditAssessmentTransactionDetails.setChannelID(aCreditAssessmentRequest.getChannelID());
        
        aCreditAssessmentTransactionDetails.setCommentTxt(aCreditAssessmentRequest.getCommentTxt());
	}
}
