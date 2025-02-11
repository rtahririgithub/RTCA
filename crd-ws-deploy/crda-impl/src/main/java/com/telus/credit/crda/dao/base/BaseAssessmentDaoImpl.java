package com.telus.credit.crda.dao.base;


import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.OverrideCreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class BaseAssessmentDaoImpl
        extends com.telus.credit.crda.dao.EcrdaAbstractSqlMapDao
        implements BaseAssessmentDao {
    public static final Log log = LogFactory.getLog(BaseAssessmentDaoImpl.class);

    public BaseAssessmentDaoImpl() {
    }

    //  @Override
    public long createCreditAssessmentRequestTrx(
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo, String creditAssessmentStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        LogUtil.infolog("-->CREATE CAR  in  CAR table");
        long db_credAsmtID = 0;
        try {
            HashMap<String, Object> carData = populateCAR(aCreditAssessmentRequest, auditInfo);

            db_credAsmtID = (Long) insert(CreditAssessmentDaoConstants.INSERT_CAR, carData);

            LogUtil.infolog("-->CREATE CAR STATUS in  CAR_STATUS table");
            Map<String, Object> carStatusData = BaseAssessmentDaoDataMapper.populateCarStatusData(
                    auditInfo,
                    creditAssessmentStatus, db_credAsmtID);
            Long carStatusId = (Long) insert(CreditAssessmentDaoConstants.INSERT_CAR_STATUS, (HashMap<String, Object>) carStatusData);
            LogUtil.infolog("-->CREATE CAR and Customer relation ship CUST_CREDIT_ASSMNT_RQST table");
            Map<String, Object> carCustomerMap = BaseAssessmentDaoDataMapper.populateCarCustomerMap(aCreditAssessmentRequest, auditInfo, db_credAsmtID);
            Long cUST_CREDIT_ASSMNT_RQST_ID = (Long) insert(CreditAssessmentDaoConstants.INSERT_CUST_CREDIT_ASSMNT_RQST, (HashMap<String, Object>) carCustomerMap);
            LogUtil.infolog("-->CREATE CAR CREDIT PROFILE RELATIONSHIP. INSERT INTO CAR_CREDIT_PROFILE");
            Map<String, Object> carCreditMap = BaseAssessmentDaoDataMapper.populateCarCreditMap(aCreditAssessmentRequest, auditInfo, db_credAsmtID);

            Long cAR_CREDIT_PROFILE_ID = (Long) insert(CreditAssessmentDaoConstants.INSERT_CAR_CREDIT_PROFILE, (HashMap<String, Object>) carCreditMap);

        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
        return db_credAsmtID;
    }


    protected HashMap<String, Object> populateCAR(
            CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo) {
        HashMap<String, Object> carData = (HashMap<String, Object>) BaseAssessmentDaoDataMapper.populateCAR(creditAssessmentRequest, auditInfo);
        return carData;
    }

    @Override
    public void expireCreditAssessmentRequestStatus(
            long creditAssessmentRequestId, String userId) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("carId", creditAssessmentRequestId);
        param.put("userId", userId);
        try {
            update(CreditAssessmentDaoConstants.EXPIRE_CAR_STATUS, param);
        } catch (com.telus.framework.exception.ConcurrencyConflictException e) {
            log.info("Ignore update car status concurrency exception as there might not be any status to expire." + e, e);
        }
    }

    @Override
    public long createCreditAssessmentRequestStatus(
            long aCARID, String userId, String carStatus)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        HashMap<String, Object> carStatusData = new HashMap<String, Object>();
        carStatusData.put("carStatusType", carStatus);
        carStatusData.put("carId", aCARID);
        carStatusData.put("userId", userId);
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        long dBcarStatusID = 0;
        try {
            dBcarStatusID = (Long) insert(CreditAssessmentDaoConstants.INSERT_CAR_STATUS, carStatusData);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "cARID", aCARID, e);
        }

        return dBcarStatusID;
    }

    @Override
    public long createCreditAssessmentRequestActivity(long aCARID,
                                                      String userId,
                                                      String activityTypeCd,
                                                      long carStatusId,
                                                      String carActivityReasonCd)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> carActivityMap = new HashMap<String, Object>();
        carActivityMap.put("carId", aCARID);
        carActivityMap.put("userId", userId);
        carActivityMap.put("activityTypeCd", activityTypeCd);
        carActivityMap.put("carStatusId", carStatusId);
        carActivityMap.put("carActivityReasonCd", carActivityReasonCd);
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        Long carActivityID = new Long(0);
        try {
            carActivityID = (Long) insert(CreditAssessmentDaoConstants.INSERT_CAR_ACTIVITY, (HashMap<String, Object>) carActivityMap);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "cARID", aCARID, e);
        }

        return carActivityID;
    }

    @Override
    public void expireCreditBureauTransaction(long creditAssessmentID,
                                              String status, AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("carId", creditAssessmentID);
        param.put("userId", auditInfo.getUserId());
        param.put("status", status);
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        try {
            update(CreditAssessmentDaoConstants.EXPIRE_CREDIT_BUREAU_TRN, param);
        } catch (com.telus.framework.exception.ConcurrencyConflictException e) {
            log.info("Ignoring the exception to expire credit bureay transaction for car: " + creditAssessmentID, e);
        }

    }


    @Override
    public void createCreditAssessmentComment(
            long aCARID,
            String userID,
            String originatorApplicationId,
            String commentTxt) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));

        Map<String, Object> commentsMap = new HashMap<String, Object>();
        commentsMap.put("objectId", String.valueOf(aCARID));
        commentsMap.put("userId", userID);
        commentsMap.put("sourceId", originatorApplicationId);
        commentsMap.put("objectTypeCd", "CRD_ASMNT_REQUEST");

        commentsMap.put("commentTypeCd", "MANUAL_COMMENT");
        commentsMap.put("commentText", commentTxt);
        commentsMap.put("displaySeqNum", 1);

        LogUtil.infolog("-->expire comment");
        try {
            update(CreditAssessmentDaoConstants.STAT_ID_EXPIRE_CRDMGT_COMMENT, commentsMap);
        } catch (com.telus.framework.exception.ConcurrencyConflictException e) {
            log.info("Exception while expiring the comments: " + e.getMessage());
        }
        LogUtil.infolog("-->insert comment");
        try {
            insert(CreditAssessmentDaoConstants.STAT_ID_INSERT_CRDMGT_COMMENT, (HashMap<String, Object>) commentsMap);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "cARID", aCARID, e);
        }

    }

    @Override
    public long updateCreditAssessmentRequestStatus(
            long cARID,
            String userId,
            String carStatus) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        expireCreditAssessmentRequestStatus(cARID, userId);
        return createCreditAssessmentRequestStatus(cARID, userId, carStatus);

    }

    protected void storeOverrideDecisioningAttributes(
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo,
            long aCARID
    ) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        try {
            OverrideCreditAssessmentRequest overrideRequest = (OverrideCreditAssessmentRequest) aCreditAssessmentRequest;
            HashMap<String, Object> dscnAttributes = new HashMap<String, Object>();
            dscnAttributes.put("userId", auditInfo.getUserId());
            dscnAttributes.put("carId", aCARID);

            if(overrideRequest.getCreditProfileData()!= null){
            	com.telus.credit.domain.creditprofile.ConsumerCreditProfile aCreditProfileData= overrideRequest.getCreditProfileData();
                if (aCreditProfileData.getPersonalInfo() != null && 
                		aCreditProfileData.getPersonalInfo().getCreditCheckConsentCd() != null
                        && aCreditProfileData.getPersonalInfo().getCreditCheckConsentCd().trim().length() > 0) {
                    dscnAttributes.put("attrName", CreditAssessmentDaoConstants.CAR_ATTR_CURRENT_CREDIT_CHECK_CONSENT_CD);
                    dscnAttributes.put("attrVallue", aCreditProfileData.getPersonalInfo().getCreditCheckConsentCd());
                    insert(CreditAssessmentDaoConstants.INSERT_CAR_ATTR_VALUE, dscnAttributes);
                }
                if (aCreditProfileData.getCreditWorthiness() != null &&   
                		aCreditProfileData.getCreditWorthiness().getCreditValueCd() != null
                        && aCreditProfileData.getCreditWorthiness().getCreditValueCd().trim().length() > 0) {
                    dscnAttributes.put("attrName", CreditAssessmentDaoConstants.CAR_ATTR_CURRENT_CREDIT_VALUE_CD);
                    dscnAttributes.put("attrVallue", aCreditProfileData.getCreditWorthiness().getCreditValueCd());
                    insert(CreditAssessmentDaoConstants.INSERT_CAR_ATTR_VALUE, dscnAttributes);
                }
                if (aCreditProfileData.getCreditWorthiness() != null && 
                		aCreditProfileData.getCreditWorthiness().getFraudIndicatorCd() != null
                        && aCreditProfileData.getCreditWorthiness().getFraudIndicatorCd().trim().length() > 0) {
                    dscnAttributes.put("attrName", CreditAssessmentDaoConstants.CAR_ATTR_CURRENT_FRAUD_CD);
                    dscnAttributes.put("attrVallue", aCreditProfileData.getCreditWorthiness().getFraudIndicatorCd());
                    insert(CreditAssessmentDaoConstants.INSERT_CAR_ATTR_VALUE, dscnAttributes);
                }

                if (overrideRequest.getNewFraudIndicatorCd() != null
                        && overrideRequest.getNewFraudIndicatorCd().trim().length() > 0) {
                    dscnAttributes.put("attrName", CreditAssessmentDaoConstants.CAR_ATTR_NEW_FRAUD_CD);
                    dscnAttributes.put("attrVallue", overrideRequest.getNewFraudIndicatorCd());
                    insert(CreditAssessmentDaoConstants.INSERT_CAR_ATTR_VALUE, dscnAttributes);
                }
             }
           if (overrideRequest.getNewCreditCheckConsentCd() != null
                    && overrideRequest.getNewCreditCheckConsentCd().trim().length() > 0) {
                dscnAttributes.put("attrName", CreditAssessmentDaoConstants.CAR_ATTR_NEW_CREDIT_CHECK_CONSENT_CD);
                dscnAttributes.put("attrVallue", overrideRequest.getNewCreditCheckConsentCd());
                insert(CreditAssessmentDaoConstants.INSERT_CAR_ATTR_VALUE, dscnAttributes);
            }

 
            if (overrideRequest.getNewCreditValueCd() != null
                    && overrideRequest.getNewCreditValueCd().trim().length() > 0) {
                dscnAttributes.put("attrName", CreditAssessmentDaoConstants.CAR_ATTR_NEW_CREDIT_VALUE_CD);
                dscnAttributes.put("attrVallue", overrideRequest.getNewCreditValueCd());
                insert(CreditAssessmentDaoConstants.INSERT_CAR_ATTR_VALUE, dscnAttributes);
            }

          } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }


    }


}