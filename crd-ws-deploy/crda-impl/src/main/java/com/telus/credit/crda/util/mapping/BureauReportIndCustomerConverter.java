package com.telus.credit.crda.util.mapping;

import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.crdgw.domain.BaseReportResponse.ReportDocument;
import org.dozer.CustomConverter;


public class BureauReportIndCustomerConverter implements CustomConverter {

    public Object convert(Object destination, Object source, Class destClass, Class sourceClass) {
        if (source == null) {
            return false;
        }
        if (source instanceof ReportDocument) {
            ReportDocument aReportDocument = ((ReportDocument) source);
            return InternalRules.getCreditBureauReportInd(aReportDocument);
        }
        return false;
    }
}


	
	


