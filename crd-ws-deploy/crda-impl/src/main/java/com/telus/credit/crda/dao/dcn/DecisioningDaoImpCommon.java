package com.telus.credit.crda.dao.dcn;

import java.util.HashMap;
import java.util.Map;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.dao.base.BaseAssessmentDaoImpl;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.domain.delegate.dcn.CreditAssessmentResultWrapper;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.common.BureauInformation;
import com.telus.credit.domain.common.CreditBureauDocument;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.common.FraudWarning;
import com.telus.credit.domain.common.ProductCategory;
import com.telus.credit.domain.common.RiskIndicator;
import com.telus.credit.domain.common.ScoreCardAttribute;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.util.ThreadLocalUtil;

public abstract class DecisioningDaoImpCommon
        extends BaseAssessmentDaoImpl
        implements DecisioningDao {

    public DecisioningDaoImpCommon() {
        super();
    }


    //template method overriden by subclasses to populate the DecisioningEngineInput based on crd asmt type/subtype.
    @Override
    //@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void storeSingleCompletedDecisioningTrx(CreditAssessmentRequest aCreditAssessmentRequest,
                                                   AuditInfo aAuditInfo,
                                                   CreditAssessmentResultWrapper aCreditAssessmentResultWrapper,
                                                   CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
                                                   boolean failOverIndicator)
            throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException {

        startDecisioningTransaction(
                aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
                (aAuditInfo != null ? aAuditInfo.getUserId() : null),
                aCreditAssessmentTransactionDetails);

        storeDecisioningEngineInput(aCreditAssessmentRequest, aAuditInfo, aCreditAssessmentTransactionDetails, failOverIndicator);

        completeDecisioningTransaction(
                aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
                aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionId(),
                aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionStatusId(),
                (aAuditInfo != null ? aAuditInfo.getUserId() : null),
                aCreditAssessmentResultWrapper);

        storeDecisioningEngineResult(
                aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
                aCreditAssessmentTransactionDetails.getDbStgCreditDecisionTrnId(),
                aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionId(),
                aCreditAssessmentTransactionDetails.getDbCreditBureauTransactionId(),
                aCreditAssessmentRequest,
                aAuditInfo,
                aCreditAssessmentResultWrapper);
    }

    
    public void storeSingleSecondCompletedDecisioningTrx(CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo aAuditInfo,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator)
			throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException {
			
			startDecisioningTransaction(
			aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
			(aAuditInfo != null ? aAuditInfo.getUserId() : null),
			aCreditAssessmentTransactionDetails);

			completeDecisioningTransaction(
			aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
			aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionId(),
			aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionStatusId(),
			(aAuditInfo != null ? aAuditInfo.getUserId() : null),
			aCreditAssessmentResultWrapper);
			
			storeDecisioningEngineResult(
			aCreditAssessmentTransactionDetails.getCreditAssessmentID(),
			aCreditAssessmentTransactionDetails.getDbStgCreditDecisionTrnId(),
			aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionId(),
			aCreditAssessmentTransactionDetails.getDbCreditBureauTransactionId(),
			aCreditAssessmentRequest,
			aAuditInfo,
			aCreditAssessmentResultWrapper);
    }
    
    private void startDecisioningTransaction(long dbCarID,
                                             String userID,
                                             CreditAssessmentTransactionDetails creditAssessmentTransactionDetails) throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException {
        save_int_crdt_dcsn_trn(creditAssessmentTransactionDetails, userID);
        save_int_crdt_dcsn_trn_stat(creditAssessmentTransactionDetails, userID, CreditAssessmentDaoConstants.INT_CRD_DCSN_STAT_PENDING);
    }

    private long save_int_crdt_dcsn_trn_stat(
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails, String userId,
            String decisioningStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> intCreditDecisionTrnStat = new HashMap<String, Object>();
        intCreditDecisionTrnStat.put("userId", userId);
        intCreditDecisionTrnStat.put("intCrdDscnTranId", aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionId());
        intCreditDecisionTrnStat.put("creditDecisionStatusCd", decisioningStatus);

        long int_crdt_dcn_trn_stat_id = 0;
        try {
            int_crdt_dcn_trn_stat_id = (Long) insert(CreditAssessmentDaoConstants.SAVE_INT_CRDT_DCSN_TRN_STAT, (HashMap<String, Object>) intCreditDecisionTrnStat);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "intCrdDscnTranId  ", aCreditAssessmentTransactionDetails.getDbInternalCreditDecisionTransactionId(), e);
        }
        aCreditAssessmentTransactionDetails.setDbInternalCreditDecisionTransactionStatusId(int_crdt_dcn_trn_stat_id);
        return int_crdt_dcn_trn_stat_id;
    }

    protected void save_int_crdt_dcsn_trn(CreditAssessmentTransactionDetails creditAssessmentTransactionDetails,
                                          String userId) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> intCreditDecisionTrn = new HashMap<String, Object>();
        intCreditDecisionTrn.put("userId", userId);
        intCreditDecisionTrn.put("carId", creditAssessmentTransactionDetails.getCreditAssessmentID());
        //intCreditDecisionTrn.put("", creditAssessmentTransactionDetails.get)
        //TODO why ? fico has not been called yet.
            /*	    if ( creditAssessmentRequest.getDecisioningCreditAssessmentResult() != null ) {
                        intCreditDecisionTrn.put( "assessmentResultCd",
				                creditAssessmentRequest.getDecisioningCreditAssessmentResult().getAssessmentResultCd() );
				        intCreditDecisionTrn.put( "assessmentResultReasonCd", 
			                    creditAssessmentRequest.getDecisioningCreditAssessmentResult().getAssessmentResultReasonCd() );
				    }*/
        long dbInternalCreditDecisionTransactionId = 0;
        try {
            dbInternalCreditDecisionTransactionId = (Long) insert(CreditAssessmentDaoConstants.SAVE_INT_CRDT_DCSN_TRN, (HashMap<String, Object>) intCreditDecisionTrn);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", creditAssessmentTransactionDetails.getCustomerID(), e);
        }

        creditAssessmentTransactionDetails.setDbInternalCreditDecisionTransactionId(dbInternalCreditDecisionTransactionId);
    }

    @Override
    public abstract void storeDecisioningEngineInput(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;


    
    /*
     * update INT_CRDT_DCSN_TRN table with values from Fico (ASSESSMENT_RSLT_CD, ASSESSMENT_RSLT_REASON_CD)
     * update the CREDIT_DSCN_TRN_STAT_CD in INT_CRDT_DCSN_TRN_STAT table to mark this trx as completed. value = 2 (indicating completion)
     * */
    public void completeDecisioningTransaction(long carId,
                                        long dbInternalCreditDecisionTransactionId,
                                        long dbInternalCreditDecisionTransactionStatusId,
                                        String userId,
                                        CreditAssessmentResultWrapper aCreditAssessmentResultWrapper)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        updateInternalCreditDecisionTransaction(carId, dbInternalCreditDecisionTransactionId, userId, aCreditAssessmentResultWrapper);
        updateInternalCreditDecisionTransactionStatus(dbInternalCreditDecisionTransactionStatusId, userId, CreditAssessmentDaoConstants.INT_CRD_DCSN_STAT_COMPLETED);
    }

    private void updateInternalCreditDecisionTransaction(
            long carId, long intCrdDscnTranId, String userId,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> intCreditDecisionTrn = new HashMap<String, Object>();
        intCreditDecisionTrn.put("userId", userId);
        intCreditDecisionTrn.put("carId", carId);
        if (aCreditAssessmentResultWrapper != null) {
            intCreditDecisionTrn.put("assessmentResultCd",
                    aCreditAssessmentResultWrapper.getAssessmentResultCd());
            intCreditDecisionTrn.put("assessmentResultReasonCd",
                    aCreditAssessmentResultWrapper.getAssessmentResultReasonCd());
            if(aCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy()!=null && 
            		aCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy().getUnifiedCreditAssessmentIndicator()!=null){
            	intCreditDecisionTrn.put("ucAssessmentResultReasonCd",
                        aCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy().getUnifiedCreditAssessmentIndicator().getReasonCd());
            	intCreditDecisionTrn.put("ucAssessmentResult",
            			CrdaUtility.getBooleanStringValue(aCreditAssessmentResultWrapper.getUnifiedCreditAssessmentResultCopy().getUnifiedCreditAssessmentIndicator().getIndicator()));
            }
        }
        intCreditDecisionTrn.put("intCrdDscnTranId", intCrdDscnTranId);
        try {
            update(CreditAssessmentDaoConstants.UPDATE_INT_CRDT_DCSN_TRN, intCreditDecisionTrn);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "intCrdDscnTranId", intCrdDscnTranId, e);
        }
    }


    private void updateInternalCreditDecisionTransactionStatus(
            long dbInternalCreditDecisionTransactionStatusId,
            String userId,
            String decisioningStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> intCreditDecisionTrnStat = new HashMap<String, Object>();
        intCreditDecisionTrnStat.put("userId", userId);
        intCreditDecisionTrnStat.put("intCrdDscnTranStatId", dbInternalCreditDecisionTransactionStatusId);
        intCreditDecisionTrnStat.put("creditDecisionStatusCd", decisioningStatus);
        try {
            update(CreditAssessmentDaoConstants.UPDATE_INT_CRDT_DCSN_TRN_STAT, intCreditDecisionTrnStat);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "dbInternalCreditDecisionTransactionStatusId", dbInternalCreditDecisionTransactionStatusId, e);
        }

    }

    @Override
    public void storeDecisioningEngineResult(
            long dbCarID,
            long dbStgCreditDecisionTrnId,
            long dbInternalCreditDecisionTransactionId,
            long dbCreditBureauTransactionId,
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        long dbCreditDecisionTransactionResultId = createInternalCreditDecisionTransactionResult(dbStgCreditDecisionTrnId, dbInternalCreditDecisionTransactionId, dbCreditBureauTransactionId, creditAssessmentRequest, auditInfo, aCreditAssessmentResultWrapper);

        createInternalCreditDecisionTransactionResultDetails(dbCreditDecisionTransactionResultId, creditAssessmentRequest, auditInfo, aCreditAssessmentResultWrapper);
    }

    @Override
    public long createInternalCreditDecisionTransactionResult(
            long dbStgCreditDecisionTrnId,
            long dbInternalCreditDecisionTransactionId,
            long dbCreditBureauTransactionId,
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> intCreditDecisionTrnResult = new HashMap<String, Object>();
        intCreditDecisionTrnResult.put("userId", auditInfo.getUserId());
        //TODO instead of passing around consider geting StgCreditDecisionTrnId, InternalCreditDecisionTransactionId, CreditBureauTransactionId by cARID
		/*	    long StgCreditDecisionTrnId=0;
		        long creditBureauTransactionId=0;
			    long internalCreditDecisionTransactionId=0;*/

        if (dbStgCreditDecisionTrnId > 0) {
            intCreditDecisionTrnResult.put("stgCreditDscnTrnId", dbStgCreditDecisionTrnId);
        }
        intCreditDecisionTrnResult.put("intCrdDscnTranId", dbInternalCreditDecisionTransactionId);
        if (dbCreditBureauTransactionId > 0) {
            intCreditDecisionTrnResult.put("creditBureauTrnId", dbCreditBureauTransactionId);
        }
        intCreditDecisionTrnResult.put("assessmentMsgCd", aCreditAssessmentResultWrapper.getAssessmentMessageCd());
        intCreditDecisionTrnResult.put("creditValueCd", aCreditAssessmentResultWrapper.getCreditValueCd());
        intCreditDecisionTrnResult.put("fraudIndCd", aCreditAssessmentResultWrapper.getFraudIndicatorCd());
        if (aCreditAssessmentResultWrapper.getProductCategoryQualification() != null) {
            intCreditDecisionTrnResult.put("productCategoryBoltOn",
                    CrdaUtility.getBooleanStringValue(aCreditAssessmentResultWrapper.getProductCategoryQualification().isBoltOnInd()));
        }

        intCreditDecisionTrnResult.put("decisionCd", aCreditAssessmentResultWrapper.getDecisionCd());
/*        if(aCreditAssessmentResultWrapper.getCreditAssessmentStrategies()!= null){
	        intCreditDecisionTrnResult.put("depositLookupStgyCd", aCreditAssessmentResultWrapper.getCreditAssessmentStrategies().getDepositLookupStrategy());
	        intCreditDecisionTrnResult.put("customerActyStgyCd", aCreditAssessmentResultWrapper.getCreditAssessmentStrategies().getCustomerActivityStrategy());
	        intCreditDecisionTrnResult.put("creditValAsgmtStgyCd", aCreditAssessmentResultWrapper.getCreditAssessmentStrategies().getCreditValueAssignmentStrategy());
	        intCreditDecisionTrnResult.put("tenureStgyCd", aCreditAssessmentResultWrapper.getCreditAssessmentStrategies().getTenureStrategy());
	        intCreditDecisionTrnResult.put("cvudStgyCd", aCreditAssessmentResultWrapper.getCreditAssessmentStrategies().getCvudStrategy());
        }*/
        
        if (aCreditAssessmentResultWrapper.getCreditRiskLevel()!=null) {
        	intCreditDecisionTrnResult.put("riskLevelNum", aCreditAssessmentResultWrapper.getCreditRiskLevel());
        } else if(ThreadLocalUtil.getRiskLevelNum()!=null) {
        	intCreditDecisionTrnResult.put("riskLevelNum", ThreadLocalUtil.getRiskLevelNum());
        }
        long dbCreditDecisionTransactionResultId = 0;
        try {
            dbCreditDecisionTransactionResultId = (Long) insert(CreditAssessmentDaoConstants.SAVE_INT_CRDT_DCSN_TRN_RSLT, (HashMap<String, Object>) intCreditDecisionTrnResult);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
        return dbCreditDecisionTransactionResultId;
    }

    @Override
    public long createInternalCreditDecisionTransactionResultDetails(
            long dbInt_crdt_dcsn_trn_rslt_Id,
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentResultWrapper aCreditAssessmentResultWrapper) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        //TODO consider getting StgCreditDecisionTrnId, InternalCreditDecisionTransactionId, CreditBureauTransactionId by cARID
		/*	    long StgCreditDecisionTrnId=0;
		        long creditBureauTransactionId=0;
			    long internalCreditDecisionTransactionId=0;*/


        try {
            if (aCreditAssessmentResultWrapper.getFraudMessageCdList() != null && aCreditAssessmentResultWrapper.getFraudMessageCdList().size() > 0) {
                for (String fraudCd : aCreditAssessmentResultWrapper.getFraudMessageCdList()) {
                    Map<String, Object> fraudDtl = new HashMap<String, Object>();
                    fraudDtl.put("userId", auditInfo.getUserId());
                    fraudDtl.put("intCrdtDscnTrnRsltId", dbInt_crdt_dcsn_trn_rslt_Id);
                    fraudDtl.put("intCrdtDscnTrnRsltDtlCd", CreditAssessmentDaoConstants.RESULT_DTL_FRAUD_WARNING_CD);
                    fraudDtl.put("fraudMessageCd", fraudCd);
                    insert(CreditAssessmentDaoConstants.SAVE_INT_CRDT_DCSN_TRN_RSLT_DTL, (HashMap<String, Object>) fraudDtl);
                }
            }
            if (aCreditAssessmentResultWrapper.getProductCategoryQualification() != null
                    && aCreditAssessmentResultWrapper.getProductCategoryQualification().getProductCategoryList() != null
                    && aCreditAssessmentResultWrapper.getProductCategoryQualification().getProductCategoryList().size() > 0) {
                for (ProductCategory pc : aCreditAssessmentResultWrapper.getProductCategoryQualification().getProductCategoryList()) {

                    Map<String, Object> pcDtl = new HashMap<String, Object>();
                    pcDtl.put("userId", auditInfo.getUserId());
                    pcDtl.put("intCrdtDscnTrnRsltId", dbInt_crdt_dcsn_trn_rslt_Id);
                    pcDtl.put("intCrdtDscnTrnRsltDtlCd", CreditAssessmentDaoConstants.RESULT_DTL_PROD_QUALIFICATION_CD);
                    pcDtl.put("creditApprvdProdCatgyCd", pc.getCategoryCd());

                    pcDtl.put("productQualInd", CrdaUtility.getBooleanStringValue(pc.isQualified()));

                    insert(CreditAssessmentDaoConstants.SAVE_INT_CRDT_DCSN_TRN_RSLT_DTL, (HashMap<String, Object>) pcDtl);
                }
            }
            if (aCreditAssessmentResultWrapper.getBureauInformationList() != null
                    && aCreditAssessmentResultWrapper.getBureauInformationList().size() > 0) {
                for (BureauInformation bi : aCreditAssessmentResultWrapper.getBureauInformationList()) {
                    Map<String, Object> bureauDtl = new HashMap<String, Object>();
                    bureauDtl.put("userId", auditInfo.getUserId());
                    bureauDtl.put("intCrdtDscnTrnRsltId", dbInt_crdt_dcsn_trn_rslt_Id);
                    bureauDtl.put("intCrdtDscnTrnRsltDtlCd", CreditAssessmentDaoConstants.RESULT_DTL_BUREAU_INFO_CD);
                    bureauDtl.put("bureauInfoTypeCd", bi.getBureauType());
                    bureauDtl.put("bureauCd", bi.getBureauCd());
                    bureauDtl.put("bureauInfoPriorityCd", bi.getBureauPriority());
                    insert(CreditAssessmentDaoConstants.SAVE_INT_CRDT_DCSN_TRN_RSLT_DTL, (HashMap<String, Object>) bureauDtl);
                }
            }
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
        return 0;
    }

    @Override
    public long storeCreditBureauResult(
            ConsumerCreditReportResponse consumerCreditReportResponse,
            long dbCARID,
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException {

        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        long dbCreditBureauTransactionId = createCreditBureauTransaction(
                consumerCreditReportResponse,
                dbCARID,
                creditAssessmentRequest,
                auditInfo);


        createCreditBureauTransactionDetails(
                creditAssessmentRequest,
                auditInfo,
                dbCreditBureauTransactionId,
                consumerCreditReportResponse);

        return dbCreditBureauTransactionId;
    }

    @Override
    public long createCreditBureauTransaction(
            ConsumerCreditReportResponse consumerCreditReportResponse,
            long dbCARID,
            CreditAssessmentRequest creditAssessmentRequest,
            com.telus.credit.domain.ent.AuditInfo auditInfo)
            throws EnterpriseCreditAssessmentServiceException, EnterpriseCreditAssessmentPolicyException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        CreditBureauResult creditBureauResult = consumerCreditReportResponse.getCreditBureauResult();
        Map<String, Object> bureauData = new HashMap<String, Object>();
        bureauData.put("userId", auditInfo.getUserId());
        bureauData.put("carId", dbCARID);
        if (creditBureauResult.getPersonName() != null) {
            bureauData.put("firstName", creditBureauResult.getPersonName().getFirstName());
            bureauData.put("middleName", creditBureauResult.getPersonName().getMiddleName());
            bureauData.put("lastName", creditBureauResult.getPersonName().getLastName());
        }
        bureauData.put("errorCode", creditBureauResult.getErrorCd());
        bureauData.put("reportSourceCode", creditBureauResult.getReportSourceCd());
        bureauData.put("reportTypeCode", creditBureauResult.getReportType());
        if (creditBureauResult.getAdjudicationResult() != null) {
            bureauData.put("creditScore", creditBureauResult.getAdjudicationResult().getCreditScoreCd());
            bureauData.put("creditScoreTypeCd", creditBureauResult.getAdjudicationResult().getCreditScoreTypeCd());
            bureauData.put("creditClassCode", creditBureauResult.getAdjudicationResult().getCreditClass());
            if (creditBureauResult.getAdjudicationResult().getCreditLimitAmt() != null
                    && creditBureauResult.getAdjudicationResult().getCreditLimitAmt().doubleValue() > 0) {
                bureauData.put("creditLimit", creditBureauResult.getAdjudicationResult().getCreditLimitAmt());
            }
            if (creditBureauResult.getAdjudicationResult().getDepositAmt() != null
                    && creditBureauResult.getAdjudicationResult().getDepositAmt().doubleValue() > 0) {
                bureauData.put("depositAmount", creditBureauResult.getAdjudicationResult().getDepositAmt());
            }
            if (creditBureauResult.getAdjudicationResult().getCreditDecision() != null) {
                bureauData.put("decisionCode", creditBureauResult.getAdjudicationResult().getCreditDecision().getCreditDecisionCd());
                bureauData.put("decisionMessage", creditBureauResult.getAdjudicationResult().getCreditDecision().getCreditDecisionMessage());
            }
        }
        LogUtil.infolog("-->INSERT_CREDIT_BUREAU_TRN");
        long dbCreditBureauTransactionId = 0;
        try {
            dbCreditBureauTransactionId = (Long) insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_TRN, (HashMap<String, Object>) bureauData);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", creditAssessmentRequest.getCustomerID(), e);
        }
        return dbCreditBureauTransactionId;
    }

    @Override
    public void createCreditBureauTransactionDetails(
            CreditAssessmentRequest aCreditAssessmentRequest,
            com.telus.credit.domain.ent.AuditInfo auditInfo,
            long creditBureauTransactionId,
            ConsumerCreditReportResponse consumerCreditReportResponse) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        try {
            CreditBureauResult creditBureauResult = consumerCreditReportResponse.getCreditBureauResult();
            if (creditBureauResult.getFraudWarningList() != null) {
                for (FraudWarning fraudWarning : creditBureauResult.getFraudWarningList()) {
                    Map<String, Object> bureauDetailFraudData = new HashMap<String, Object>();
                    bureauDetailFraudData.put("userId", auditInfo.getUserId());
                    bureauDetailFraudData.put("creditBureauTrnId", creditBureauTransactionId);
                    bureauDetailFraudData.put("fraudTypeCode", fraudWarning.getFraudType());
                    bureauDetailFraudData.put("fraudCode", fraudWarning.getFraudCd());
                    bureauDetailFraudData.put("fraudMessage", fraudWarning.getFraudMessage());
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_FRAUD, (HashMap<String, Object>) bureauDetailFraudData);
                    LogUtil.infolog("-->INSERT_CREDIT_BUREAU_DTL_TRN_FRAUD");

                }
            }

            if (creditBureauResult.getRiskIndicator() != null) {
                RiskIndicator riskIndicator = creditBureauResult.getRiskIndicator();
                Map<String, Object> bureauDetailRiskData = new HashMap<String, Object>();
                bureauDetailRiskData.put("userId", auditInfo.getUserId());
                bureauDetailRiskData.put("creditBureauTrnId", creditBureauTransactionId);

                if (riskIndicator.getWriteOffInd() != null && riskIndicator.getWriteOffInd().trim().length() > 0) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.WRITE_OFF_INDICATOR_CD);
                    bureauDetailRiskData.put("riskIndicatorValue", riskIndicator.getWriteOffInd());
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.isBankcrupcyInd() != null) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.BANKCRUPCY_CD);
                    bureauDetailRiskData.put("riskIndicatorValue",
                            CrdaUtility.getBooleanStringValue(riskIndicator.isBankcrupcyInd()));
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.isHighRiskThinFileInd() != null) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.HIGH_RISK_THIN_FILE_CD);
                    bureauDetailRiskData.put("riskIndicatorValue",
                            CrdaUtility.getBooleanStringValue(riskIndicator.isHighRiskThinFileInd()));
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.isNoHitThinFileInd() != null) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.NO_HIT_THIN_FILE_CD);
                    bureauDetailRiskData.put("riskIndicatorValue",
                            CrdaUtility.getBooleanStringValue(riskIndicator.isNoHitThinFileInd()));
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.isTempSINInd() != null) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.TEMP_SIN_CD);
                    bureauDetailRiskData.put("riskIndicatorValue",
                            CrdaUtility.getBooleanStringValue(riskIndicator.isTempSINInd()));
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.isTrueThinFileInd() != null) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.TRUE_THIN_FILE_CD);
                    bureauDetailRiskData.put("riskIndicatorValue",
                            CrdaUtility.getBooleanStringValue(riskIndicator.isTrueThinFileInd()));
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.isUnderAgeInd() != null) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.UNDER_AGE_CD);
                    bureauDetailRiskData.put("riskIndicatorValue",
                            CrdaUtility.getBooleanStringValue(riskIndicator.isUnderAgeInd()));
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.getPrimaryRiskInd() != null && riskIndicator.getPrimaryRiskInd().trim().length() > 0) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.PRIMARY_RISK_CD);
                    bureauDetailRiskData.put("riskIndicatorValue", riskIndicator.getPrimaryRiskInd());
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
                if (riskIndicator.getSecondaryRiskInd() != null && riskIndicator.getSecondaryRiskInd().trim().length() > 0) {
                    bureauDetailRiskData.put("riskIndicatorCode", CreditAssessmentDaoConstants.SECONDARY_RISK_CD);
                    bureauDetailRiskData.put("riskIndicatorValue", riskIndicator.getSecondaryRiskInd());
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_RISK, (HashMap<String, Object>) bureauDetailRiskData);
                }
            }
            if (consumerCreditReportResponse.getReportDocument() != null) {
                if (consumerCreditReportResponse.getReportDocument().getPrintImageDocument() != null){
//                        && consumerCreditReportResponse.getReportDocument().getPrintImageDocument().getDocumentType() != null) {
                    Map<String, Object> bureauDetailDocData = new HashMap<String, Object>();
                    bureauDetailDocData.put("userId", auditInfo.getUserId());
                    bureauDetailDocData.put("creditBureauTrnId", creditBureauTransactionId);
                    // we don't need to save the document path for MVP
                    //bureauDetailDocData.put("documentPath", consumerCreditReportResponse.getReportDocument().getPrintImageDocument().getDocumentPath());
                    //if ( consumerCreditReportResponse.getReportDocument().getPrintImageDocument().getDocumentType()!= null ) {
                    bureauDetailDocData.put("documentFormatType", CreditAssessmentDaoConstants.REPORT_DOC_TYPE_TXT);
                    // TODO: it is hard-coded as txt as credit gateway response does not have format populated at this time
                    // it can also be dervied from file ext
                    //consumerCreditReportResponse.getReportDocument().getPrintImageDocument().getDocumentType().toUpperCase() );
                    //}
                    /*if (consumerCreditReportResponse.getReportDocument().getPrintImageDocument().getDocumentPath() != null) {
                        bureauDetailDocData.put("documentName",
                                (new File(consumerCreditReportResponse.getReportDocument().getPrintImageDocument().getDocumentPath())).getName());
                    }*/

                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_DOC_TYPE, (HashMap<String, Object>) bureauDetailDocData);
                }
                if (consumerCreditReportResponse.getReportDocument().getBureauReportDocument() != null){
//                        && consumerCreditReportResponse.getReportDocument().getBureauReportDocument().getDocumentType() != null) {
                    Map<String, Object> bureauDetailDocData = new HashMap<String, Object>();
                    bureauDetailDocData.put("userId", auditInfo.getUserId());
                    bureauDetailDocData.put("creditBureauTrnId", creditBureauTransactionId);
                    // we don't need to save the document path for MVP
                    //bureauDetailDocData.put("documentPath", consumerCreditReportResponse.getReportDocument().getBureauReportDocument().getDocumentPath());
                    //File docFile = new File(consumerCreditReportResponse.getReportDocument().getBureauReportDocument().getDocumentPath());
                    String fileExt = consumerCreditReportResponse.getReportDocument().getBureauReportDocument().getDocumentType();
                    if (fileExt == null) {
                        fileExt = CreditAssessmentDaoConstants.REPORT_DOC_TYPE_DAT;
                    }
                    bureauDetailDocData.put("documentFormatType", fileExt);
                    //bureauDetailDocData.put("documentName", docFile.getName());
                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_DOC_TYPE, (HashMap<String, Object>) bureauDetailDocData);
                }
            }
            if (creditBureauResult != null
                    && creditBureauResult.getAdjudicationResult() != null
                    && creditBureauResult.getAdjudicationResult().getScoreCardAttributeList() != null
                    && creditBureauResult.getAdjudicationResult().getScoreCardAttributeList().size() > 0) {
                for (ScoreCardAttribute score : creditBureauResult.getAdjudicationResult().getScoreCardAttributeList()) {
                    Map<String, Object> bureauDetailScoreData = new HashMap<String, Object>();
                    bureauDetailScoreData.put("userId", auditInfo.getUserId());
                    bureauDetailScoreData.put("creditBureauTrnId", creditBureauTransactionId);
                    bureauDetailScoreData.put("scoreName", score.getScoreName());
                    bureauDetailScoreData.put("scoreValue", score.getScoreValue());

                    insert(CreditAssessmentDaoConstants.INSERT_CREDIT_BUREAU_DTL_TRN_SCORE_TYPE, (HashMap<String, Object>) bureauDetailScoreData);
                }


            }
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
    }


}