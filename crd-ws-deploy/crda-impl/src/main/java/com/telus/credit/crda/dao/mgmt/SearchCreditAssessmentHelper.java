/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.crda.dao.mgmt;

 
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SearchCreditAssessmentHelper {
    private static SearchCreditAssessmentHelper m_searchCreditAssessmentHelper;

    private GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();

    private SearchCreditAssessmentHelper() {
    }

    public static SearchCreditAssessmentHelper instanceOf() {
        if (m_searchCreditAssessmentHelper == null) {
            m_searchCreditAssessmentHelper = new SearchCreditAssessmentHelper();
        }
        return m_searchCreditAssessmentHelper;
    }

    public List<CreditAssessmentTransaction> populateCreditAssessmentTransactionList(
            List<HashMap<String, Object>> searchCreditAssessmentResult,
            List<HashMap<String, Object>> searchCreditAssessmentDecisionResultDtl) throws EnterpriseCreditAssessmentServiceException {
        List<CreditAssessmentTransaction> result = new ArrayList<CreditAssessmentTransaction>();
        for (HashMap<String, Object> searchAssessmentMap : searchCreditAssessmentResult) {
            CreditAssessmentTransaction creditAssessmentTransaction = new CreditAssessmentTransaction();
            String carStatusTypeCd = (String) searchAssessmentMap.get("carStatusTypeCd");
            if (	carStatusTypeCd!= null && 
            			( 		carStatusTypeCd.equalsIgnoreCase(EnterpriseCreditAssessmentConsts.CAR_STATUS_CANCELLED) || 
            					carStatusTypeCd.equalsIgnoreCase(EnterpriseCreditAssessmentConsts.CAR_STATUS_COMPLETED) )
            	)
            {
	            m_getCreditAssessmentHelper.populateBaseCreditAssessmentDetails(searchAssessmentMap, creditAssessmentTransaction);
	            Long creditBureauTrnId = (Long) searchAssessmentMap.get("creditBureauTrnId");
	
	            if (creditBureauTrnId != null && creditBureauTrnId > 0) {
	                m_getCreditAssessmentHelper.populate_creditAssessmentDetails_Common_CreditBureauData(
	                        searchAssessmentMap,
	                        creditAssessmentTransaction);
	                boolean aCreditBureauReportInd = InternalRules.getCreditBureauReportInd(
					                		(String) searchAssessmentMap.get("extDocPathStr"), 
					                		creditAssessmentTransaction.getCreditBureauReportSourceCd() );
	                creditAssessmentTransaction.setCreditBureauReportInd(aCreditBureauReportInd);                  
	            }
	            m_getCreditAssessmentHelper.populateCreditDecisioningResult(searchAssessmentMap,
	                    searchCreditAssessmentDecisionResultDtl, creditAssessmentTransaction);
	            result.add(creditAssessmentTransaction);
	        }
        }
        return result;
    }

}
