package com.telus.credit.crda.stgy.dcsn.impl;


import org.springframework.beans.factory.annotation.Autowired;

import com.fico.telus.blaze.creditCommon.CreditBureauResult;
import com.telus.credit.crda.adapter.DocumentumSvcAdapter;
import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.dao.dcn.DecisioningDao;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.crdgw.domain.BaseReportResponse.ReportDocument;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.common.CreditBureauDocument;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.UnifiedCreditAssessmentResult;
import com.telus.credit.domain.crda.WlsUnifiedCreditSearchResult;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.repo.dao.CreditRepoDao;
import com.telus.credit.repo.domain.CreditDoc;

public class UnifiedCreditSuccessPostDecisioningProcessor extends SuccessPostDecisioningProcessor {
    public DecisioningDao m_decisioningDao;

    @Override
    public void setDecisioningDao(DecisioningDao aDecisioningDao) {
        m_decisioningDao = aDecisioningDao;
    }

    @Autowired
    public DocumentumSvcAdapter m_documentumSvcAdapter;

    public void setDocumentumSvcAdapter(
            DocumentumSvcAdapter aDocumentumSvcAdapter) {
        this.m_documentumSvcAdapter = aDocumentumSvcAdapter;
    }
    
    @Autowired 
    protected CreditRepoDao m_creditRepoDao;

    public void setCreditRepo(CreditRepoDao aCreditRepoDao) {
		this.m_creditRepoDao = aCreditRepoDao;
	}

    @Override
    public String performPostDecisioning(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper decisioningCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator)
            throws EnterpriseCreditAssessmentServiceException,
            EnterpriseCreditAssessmentPolicyException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        String userID = (auditInfo != null ? auditInfo.getUserId() : null);
        String originatorApplicationID = (auditInfo != null ? auditInfo.getOriginatorApplicationId() : null);
        if (m_decisioningDao == null)
            throw new EnterpriseCreditAssessmentServiceException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + " |" + Thread.currentThread().getStackTrace()[1].getClassName() +
                            " customer id=" +
                            creditAssessmentRequest.getCustomerID() + " m_decisioningDao = " + m_decisioningDao
                    ,
                    EnterpriseCreditAssessmentExceptionCodes.ERROR_DAO_INIT);
        try {
            //create car
            long dbCARID = m_decisioningDao.createCreditAssessmentRequestTrx(
                    creditAssessmentRequest,
                    auditInfo,
                    EnterpriseCreditAssessmentConsts.CAR_STATUS_PENDING
            );
            aCreditAssessmentTransactionDetails.setCreditAssessmentID(dbCARID);


            //store a single decision-ing trx:including  decision-ing trx, fico input/output
            m_decisioningDao.storeSingleCompletedDecisioningTrx(
                    creditAssessmentRequest,
                    auditInfo,
                    decisioningCreditAssessmentResultWrapper,
                    aCreditAssessmentTransactionDetails,
                    failOverIndicator);


            //update car
            LogUtil.traceCalllog("dao.updateCreditAssessmentRequestStatus");
            m_decisioningDao.updateCreditAssessmentRequestStatus(
                    aCreditAssessmentTransactionDetails.getCreditAssessmentID(), userID, EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED);

            //Unified Credit Comment
            com.fico.telus.blaze.creditCommon.UnifiedCreditAssessmentResult ficoUnifiedCreditAssessmentResult = decisioningCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy();
            //Add to the comment WLS account BAN
            Long ban = null;
            if( ((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult() != null &&
            		(WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult() != null &&
            		((WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult()).getMatchedAccount() != null &&
            				((WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult()).getMatchedAccount().getAccountData() != null ) {
            	ban = ((WlsUnifiedCreditSearchResult)((FullCreditAssessmentRequest)creditAssessmentRequest).getUnifiedCreditSearchResult()).getMatchedAccount().getAccountData().getBillingAccountNumber();
            }
            String ucCommentTxt = "";
            if( !(ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getIndicator()==null ||
            		ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getReasonCd()==null) ) {
    			ucCommentTxt = InternalRules.getUnifiedCreditCommentText(
    					ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getIndicator().toString(), 
    							ficoUnifiedCreditAssessmentResult.getUnifiedCreditAssessmentIndicator().getReasonCd(),
    							ban ); 
            }
            
            //Mapping and adding comments from UnifiedCreditReulst to CAR Result obj
            UnifiedCreditAssessmentResult aUnifiedCreditAssessmentResult = new UnifiedCreditAssessmentResult();            
            aCreditAssessmentTransactionDetails.m_mapper.map(ficoUnifiedCreditAssessmentResult, aUnifiedCreditAssessmentResult);
            aUnifiedCreditAssessmentResult.setDiaryComment(ucCommentTxt);
            
            aCreditAssessmentTransactionDetails.setUnifiedCreditAssessmentResult(aUnifiedCreditAssessmentResult);

            //create comment
            String commentTxt = InternalRules.lookupCommentByAsmtTypeSubtype(creditAssessmentRequest, aCreditAssessmentTransactionDetails);
            aCreditAssessmentTransactionDetails.setCommentTxt(commentTxt);
                        
            LogUtil.traceCalllog("dao.createCreditAssessmentComment");
            m_decisioningDao.createCreditAssessmentComment(
								            		aCreditAssessmentTransactionDetails.getCreditAssessmentID(), 
								            		userID, originatorApplicationID, 
								            		aCreditAssessmentTransactionDetails.getCommentTxt());

			if (ficoUnifiedCreditAssessmentResult.getUnifiedCreditSimulatedBureauResultData() != null
					&& ficoUnifiedCreditAssessmentResult.getUnifiedCreditSimulatedBureauResultData().getPrintImageReportTxt() != null ) {

				String documentPathId = null;
				//will not save to documentum any longer, will save doc to credit repo instead(MVP)
				/*documentPathId = m_documentumSvcAdapter.saveUnifiedCreditReportDocumentum(
							ficoUnifiedCreditAssessmentResult.getUnifiedCreditSimulatedBureauResultData().getPrintImageReportTxt(),
							aCreditAssessmentTransactionDetails.getCustomerID(), dbCARID);*/
				
				CreditDoc creditDoc = new CreditDoc();
				creditDoc = this.setCreditDoc(ficoUnifiedCreditAssessmentResult.getUnifiedCreditSimulatedBureauResultData().getPrintImageReportTxt(),
						aCreditAssessmentTransactionDetails.getCustomerID(), dbCARID, auditInfo);
				
				m_creditRepoDao.storeCreditDocument(creditDoc);

				ConsumerCreditReportResponse consumerCreditReportResponse = new ConsumerCreditReportResponse();
				consumerCreditReportResponse.setCreditBureauResult(
						aCreditAssessmentTransactionDetails.getUnifiedCreditAssessmentResult().getUnifiedCreditSimulatedBureauResultData());
				ReportDocument reportDocument = new ReportDocument();
				CreditBureauDocument aBureauReportDocument = new CreditBureauDocument();
				// Saving to db
				Long dbCreditBureauTransactionId = m_decisioningDao.createCreditBureauTransaction(
						consumerCreditReportResponse, dbCARID, creditAssessmentRequest, auditInfo);

				// Saving the path to interm obj
				/*if (documentPathId != null && documentPathId.length() > 0) {
					aBureauReportDocument.setDocumentPath(documentPathId);
				}*/
				aBureauReportDocument.setDocumentType(EnterpriseCreditAssessmentConsts.DOC_UC_CONTENT_TYPE);
				aBureauReportDocument.setDocumentID(String.valueOf(dbCreditBureauTransactionId));
				reportDocument.setBureauReportDocument(aBureauReportDocument);
				consumerCreditReportResponse.setReportDocument(reportDocument);
				m_decisioningDao.createCreditBureauTransactionDetails(creditAssessmentRequest, auditInfo,
						dbCreditBureauTransactionId, consumerCreditReportResponse);
				aCreditAssessmentTransactionDetails.m_mapper.map(aUnifiedCreditAssessmentResult.getUnifiedCreditSimulatedBureauResultData(),  aCreditAssessmentTransactionDetails);
			}   
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "customer id", creditAssessmentRequest.getCustomerID(), e);
        }
        return returnPostProcessorResult();
    }

    public String returnPostProcessorResult() {
        LogUtil.infolog("-->Return performPostDecisioning result = " + EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS);
        return EnterpriseCreditAssessmentConsts.DECISIONING_RESULT_CD_SUCCESS;
    }
    
    private CreditDoc setCreditDoc(String docContent, Long customerId, Long carId, AuditInfo auditInfo) {
    	
    	CreditDoc creditDoc = new CreditDoc();
    	creditDoc.setCreateUserId(auditInfo.getUserId());
		creditDoc.setDataSourceId(CreditDoc.APPLICATION_ID_RTCA);
		creditDoc.setDocType(CreditDoc.DOC_TYP_CONS_CRD_RPT_IMG);
		creditDoc.setDocContent(docContent.getBytes());
		creditDoc.setExternalEntityId(customerId);
		creditDoc.setExternalEntityTypeCode(CreditDoc.EXT_ENT_TYPE_CSUTOMER_ID);
		creditDoc.setExternalResourceTypeCode(CreditDoc.EXT_RSC_TYPE_CAR_ID);
		creditDoc.setExternalResourceId(carId);
		
		return creditDoc;
    }
}
