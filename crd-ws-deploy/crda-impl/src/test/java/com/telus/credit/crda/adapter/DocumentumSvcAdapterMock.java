package com.telus.credit.crda.adapter;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;

/**
 * this is for testing so we can call this method during testing
 * @author x158136
 *
 */
public class DocumentumSvcAdapterMock extends DocumentumSvcAdapter {

	@Override
	public String saveUnifiedCreditReportDocumentum(String printImageReport, Long customerID, Long carID)
			throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
		//String documentPathId = aCreditAssessmentTransactionDetails.getCustomerID() + "_" + dbCARID + ".txt";
		return customerID + "_" + carID + ".txt";
	}
	
}
