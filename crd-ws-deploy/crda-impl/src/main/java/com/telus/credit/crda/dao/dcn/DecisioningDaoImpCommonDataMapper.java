/*package com.telus.credit.crda.dao.dcn;

import com.telus.credit.crda.dao.base.BaseAssessmentDaoDataMapper;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;

import java.util.HashMap;
import java.util.Map;

public class DecisioningDaoImpCommonDataMapper {
    public static Map<String, Object> populateCAR(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo) {

        HashMap<String, Object> carData = (HashMap<String, Object>) BaseAssessmentDaoDataMapper.populateCAR(aCreditAssessmentRequest, auditInfo);
        FullCreditAssessmentRequest creditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
        carData.put("customerTypeCode", creditAssessmentRequest.getCreditCustomerInfo().getCustomerTypeCd());
        carData.put("customerId", creditAssessmentRequest.getCreditCustomerInfo().getCustomerID());
        return carData;
    }


    public static Map<String, Object> populateCarCreditMap(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo, long aCARID) {
        Map<String, Object> carCreditMap = BaseAssessmentDaoDataMapper.populateCarCreditMap(aCreditAssessmentRequest, auditInfo, aCARID);
        FullCreditAssessmentRequest creditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
        carCreditMap.put("customerId", creditAssessmentRequest.getCreditCustomerInfo().getCustomerID());
        return carCreditMap;
    }

    public static Map<String, Object> populateCarCustomerMap(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo,
                                                             long aCARID) {
        Map<String, Object> carCustomerMap = populateCarCustomerMap(aCreditAssessmentRequest, auditInfo, aCARID);
        FullCreditAssessmentRequest creditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
        carCustomerMap.put("customerId", creditAssessmentRequest.getCreditCustomerInfo().getCustomerID());
        return carCustomerMap;
    }
}
*/