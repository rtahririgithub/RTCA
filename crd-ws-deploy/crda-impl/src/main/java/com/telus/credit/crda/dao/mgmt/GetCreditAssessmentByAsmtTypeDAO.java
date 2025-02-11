package com.telus.credit.crda.dao.mgmt;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;

import java.util.HashMap;


public interface GetCreditAssessmentByAsmtTypeDAO {


    void getCreditAssessmentByAsmtType(long creditAsmtID,
                                       HashMap<String, Object> baseCreditAssessmentMap, 
                                       CreditAssessmentDetails creditAssessmentDetails) 
    					throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException;
}
