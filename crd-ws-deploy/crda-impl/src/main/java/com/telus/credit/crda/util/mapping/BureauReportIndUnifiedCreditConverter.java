package com.telus.credit.crda.util.mapping;

import org.dozer.CustomConverter;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;

public class BureauReportIndUnifiedCreditConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null || !(source instanceof String)) {
            return false;
        } else {
        	String aCreditBureauReportSourceCd = (String)source;
        	if (aCreditBureauReportSourceCd.length() > 0
    				&& aCreditBureauReportSourceCd.equals(CreditAssessmentDaoConstants.REPORT_SOURCE_EQUIFAX)
    				|| aCreditBureauReportSourceCd.equals(CreditAssessmentDaoConstants.REPORT_SOURCE_TRANSUNION)
    				|| aCreditBureauReportSourceCd.equals(CreditAssessmentDaoConstants.REPORT_SOURCE_UNIFIED_CREDIT)
    				|| aCreditBureauReportSourceCd.equals("UNIFIED_CREDIT")) {
    			return true;
    		}
        }
        return false;
    }
}
