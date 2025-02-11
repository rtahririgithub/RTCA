package com.telus.credit.crda.dao.base;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.common.CreditDecision;
import com.telus.credit.domain.common.ProductCategory;
import com.telus.credit.domain.common.ProductCategoryQualification;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.ent.AuditInfo;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class BaseAssessmentDaoDataMapper {


    public static Map<String, Object> populateCAR(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo) {
        HashMap<String, Object> carData = new HashMap<String, Object>();
        carData.put("carTypeCode", aCreditAssessmentRequest.getCreditAssessmentTypeCd());
        carData.put("carSubTypeCode", aCreditAssessmentRequest.getCreditAssessmentSubTypeCd());
        carData.put("userId", auditInfo.getUserId());
        String dataSourceId = (aCreditAssessmentRequest.getApplicationID() != null) ? aCreditAssessmentRequest.getApplicationID() : auditInfo.getOriginatorApplicationId();
        carData.put("datasourceId", dataSourceId);
        carData.put("lineOfBusinessCode", aCreditAssessmentRequest.getLineOfBusiness());
        carData.put("channelOrgCode", aCreditAssessmentRequest.getChannelID());

        return carData;
    }

    public static Map<String, Object> populateCarCustomerMap(CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo,
                                                             long aCAR_ID) {
        Map<String, Object> carCustomerMap = new HashMap<String, Object>();
        carCustomerMap.put("customerId", creditAssessmentRequest.getCustomerID());
        carCustomerMap.put("carId", aCAR_ID);
        carCustomerMap.put("userId", auditInfo.getUserId());
        return carCustomerMap;
    }

    protected static Map<String, Object> populateCarStatusData(AuditInfo auditInfo, String creditAssessmentStatus,
                                                               long aCAR_ID) {
        Map<String, Object> carStatusData = new HashMap<String, Object>();
        carStatusData.put("carStatusType", creditAssessmentStatus);
        carStatusData.put("carId", aCAR_ID);
        carStatusData.put("userId", auditInfo.getUserId());
        return carStatusData;
    }


    public static Map<String, Object> populateCarCreditMap(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo, long aCAR_ID) {
        Map<String, Object> carCreditMap = new HashMap<String, Object>();
        carCreditMap.put("carId", aCAR_ID);
        carCreditMap.put("customerId", aCreditAssessmentRequest.getCustomerID());
        carCreditMap.put("userId", auditInfo.getUserId());
        carCreditMap.put("creditProfileType", "PRIMARY");
        return carCreditMap;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        if (s != null) {
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1)
                ext = s.substring(i + 1).toUpperCase();
        }
        return ext;
    }

    public static List<CreditAssessmentTransaction> populateCreditAssessmentTransactionList(
            List<HashMap<String, Object>> searchCreditAssessmentResult,
            List<HashMap<String, Object>> searchCreditAssessmentDecisionResultDtl) {
        List<CreditAssessmentTransaction> result = new ArrayList<CreditAssessmentTransaction>();
        for (HashMap<String, Object> searchAssessmentMap : searchCreditAssessmentResult) {
            CreditAssessmentTransaction creditAssessmentTransaction = new CreditAssessmentTransaction();
            populateBaseCreditAssessmentDetails(searchAssessmentMap, creditAssessmentTransaction);
            Long creditBureauTrnId = (Long) searchAssessmentMap.get("creditBureauTrnId");

            if (creditBureauTrnId != null && creditBureauTrnId.longValue() > 0) {
                populateCreditAssessmentTransactionCreditBureauData(
                        searchAssessmentMap,
                        creditAssessmentTransaction);
                boolean aCreditBureauReportInd = InternalRules.getCreditBureauReportInd(
								                		(String) searchAssessmentMap.get("extDocPathStr"), 
								                		creditAssessmentTransaction.getCreditBureauReportSourceCd() );
                creditAssessmentTransaction.setCreditBureauReportInd(aCreditBureauReportInd);
            }
            populateCreditDecisioningResult(searchAssessmentMap,
                    searchCreditAssessmentDecisionResultDtl, creditAssessmentTransaction);
            result.add(creditAssessmentTransaction);
        }

        return result;
    }

    public static void populateBaseCreditAssessmentDetails(
            HashMap<String, Object> baseCreditAssessmentMap,
            CreditAssessmentTransaction creditAssessmentDetails) {
        if (baseCreditAssessmentMap != null) {
            creditAssessmentDetails.setChannelID((String) baseCreditAssessmentMap.get("channelOrgCd"));
            creditAssessmentDetails.setCommentTxt((String) baseCreditAssessmentMap.get("commentTxt"));

            creditAssessmentDetails.setCreditAssessmentDate((Date) baseCreditAssessmentMap.get("createTs"));
            creditAssessmentDetails.setCreditAssessmentID((Long) baseCreditAssessmentMap.get("carId"));
            creditAssessmentDetails.setCreditAssessmentStatus((String) baseCreditAssessmentMap.get("carStatusTypeCd"));
            creditAssessmentDetails.setCreditAssessmentStatusDate((Date) baseCreditAssessmentMap.get("carStatusEffectiveDate"));
            creditAssessmentDetails.setCreditAssessmentStatusReasonCd((String) baseCreditAssessmentMap.get("carActivityReasonCd"));
            creditAssessmentDetails.setCreditAssessmentSubTypeCd((String) baseCreditAssessmentMap.get("carSubtypeCd"));
            creditAssessmentDetails.setCreditAssessmentTypeCd((String) baseCreditAssessmentMap.get("carTypCd"));
            creditAssessmentDetails.setCustomerID((Long) baseCreditAssessmentMap.get("customerId"));
            creditAssessmentDetails.setUserID((String) baseCreditAssessmentMap.get("createUserId"));
        }
    }

    public static void populateCreditAssessmentTransactionCreditBureauData(
            HashMap<String, Object> creditAssessmentMap,
            CreditAssessmentTransaction creditAssessmentTransaction) {
        creditAssessmentTransaction.setCreditBureauReportSourceCd((String) creditAssessmentMap.get("reportSourceCd"));
        creditAssessmentTransaction.setCreditClass((String) creditAssessmentMap.get("adjdctnCreditClassCd"));
        creditAssessmentTransaction.setCreditScoreCd((String) creditAssessmentMap.get("adjdctnCreditScoreTxt"));
        creditAssessmentTransaction.setCreditScoreTypeCd((String) creditAssessmentMap.get("adjdctnCreditScoreTypeCd"));
        
        CreditDecision creditDecision = new CreditDecision();
        creditDecision.setCreditDecisionCd((String) creditAssessmentMap.get("adjdctnDscnCd"));
        creditDecision.setCreditDecisionMessage((String) creditAssessmentMap.get("adjdctnDscnMsgTxt"));

        Double depositAmt = (Double) creditAssessmentMap.get("adjdctnDepositAmt");
        if (depositAmt != null) {
            creditAssessmentTransaction.setDepositAmt(BigDecimal.valueOf(depositAmt));
        }
    }

    public static void populateCreditDecisioningResult(
            HashMap<String, Object> creditAssessmentMap,
            List<HashMap<String, Object>> creditDecisionResultDtlList,
            CreditAssessmentTransaction creditAssessmentDetails) {
        Long intCrdtDscnTrnRsltId = (Long) creditAssessmentMap.get("intCrdtDscnTrnRsltId");
        if (intCrdtDscnTrnRsltId != null && intCrdtDscnTrnRsltId > 0) {
            CreditAssessmentResult creditAssessmentResult = new CreditAssessmentResult();

            creditAssessmentResult.setAssessmentMessageCd((String) creditAssessmentMap.get("assessmentMsgCd"));
            creditAssessmentResult.setAssessmentResultCd((String) creditAssessmentMap.get("assessmentRsltCd"));
            creditAssessmentResult.setAssessmentResultReasonCd((String) creditAssessmentMap.get("assessmentRsltReasonCd"));
            creditAssessmentResult.setCreditValueCd((String) creditAssessmentMap.get("creditValueCd"));
            creditAssessmentResult.setFraudIndicatorCd((String) creditAssessmentMap.get("fraudIndCd"));
            ProductCategoryQualification productCategoryQ = new ProductCategoryQualification();
            productCategoryQ.setBoltOnInd(CrdaUtility.getBooleanValue((String) creditAssessmentMap.get("productCategoryBoltOn")));
            List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
            List<String> fraudCodeList = new ArrayList<String>();
            for (HashMap<String, Object> intCrdDscnRsltDtlMap : creditDecisionResultDtlList) {
                Long intCrdtDscnTrnRsltId_1 = (Long) intCrdDscnRsltDtlMap.get("intCrdtDscnTrnRsltId");
                // this condition is important for it to be re-used by search credit assessment
                if (intCrdtDscnTrnRsltId_1 != null &&
                        (intCrdtDscnTrnRsltId_1.longValue() == intCrdtDscnTrnRsltId.longValue())) {
                    String typeOfResult = (String) intCrdDscnRsltDtlMap.get("intCrdtDscnTrnRsltDtlCd");
                    if (typeOfResult != null) {
                        if (typeOfResult.equals(CreditAssessmentDaoConstants.RESULT_DTL_PROD_QUALIFICATION_CD)) {
                            ProductCategory pc = new ProductCategory();
                            pc.setCategoryCd((String) intCrdDscnRsltDtlMap.get("creditApprvdProdCatgyCd"));
                            pc.setQualified(CrdaUtility.getBooleanValue((String) intCrdDscnRsltDtlMap.get("productQualInd")));
                            productCategoryList.add(pc);
                        } else if (typeOfResult.equals(CreditAssessmentDaoConstants.RESULT_DTL_FRAUD_WARNING_CD)) {
                            fraudCodeList.add((String) intCrdDscnRsltDtlMap.get("fraudMsgCd"));
                        }
                    }
                }
            }
            if (productCategoryList.size() > 0) {
                productCategoryQ.getProductCategoryList().addAll(productCategoryList);
            }
            creditAssessmentResult.setProductCategoryQualification(productCategoryQ);
            if (fraudCodeList.size() > 0) {
                creditAssessmentResult.getFraudMessageCdList().addAll(fraudCodeList);
            }
            creditAssessmentDetails.setCreditDecisioningResult(creditAssessmentResult);
        }

    }
}