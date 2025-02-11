package com.telus.credit.crda.dao.dcn;


import java.util.Map;

//import com.fico.telus.blaze.creditCommon.DepositData;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crda.util.SummarizeARDepositDetail;
import com.telus.credit.domain.collection.CustomerCollectionData;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.deposit.DepositItemList;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.dto.AdditionalCollectionData;
import com.telus.credit.util.ThreadLocalUtil;

public class DecisiongDaoImpReAssessment
        extends DecisiongDaoImpFullAssessment {

    protected Map<String, Object> populateStagingCreditDecisionTransaction(
            CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo, long aCAR_ID, boolean failOverHandler) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        LogUtil.infolog("ExistingCrdAsmtDAOImpl: populateStagingCreditDecisionTransaction");

        ExistingCustomerCreditAssessmentRequest creditAssessmentRequest = (ExistingCustomerCreditAssessmentRequest) aCreditAssessmentRequest;
        //populate common attributes among all type of Full assessments
        Map<String, Object> stgCreditDecisionTrn = super.populateStagingCreditDecisionTransaction(
                creditAssessmentRequest,
                auditInfo, 
                aCAR_ID,
                failOverHandler);
        try {
            //populate CustomerCollectionData
            CustomerCollectionData customerCollectionData = creditAssessmentRequest.getCustomerCollectionData();
            populateCustomerCollectionData(stgCreditDecisionTrn,customerCollectionData);
            
          //populate DepositItemList
            DepositItemList aDepositItemList = creditAssessmentRequest.getDepositItemList();
            populateDepositItemList(creditAssessmentRequest, stgCreditDecisionTrn, aDepositItemList);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
        return stgCreditDecisionTrn;
    }
	private void populateDepositItemList(
			ExistingCustomerCreditAssessmentRequest creditAssessmentRequest,
			Map<String, Object> stgCreditDecisionTrn,
			DepositItemList aDepositItemList) {
/*		if (aDepositItemList != null) {
		    DepositData ficoDepositData = SummarizeARDepositDetail.summarizeARDepositDetail(creditAssessmentRequest.getDepositItemList().getDepositItem());
		    stgCreditDecisionTrn.put("totalArDepositPaidAmt", ficoDepositData.getDepositPaid());
		    stgCreditDecisionTrn.put("lastArDepositPaidTs", CrdaUtility.asDate(ficoDepositData.getMostRecentDepositPaidDate()));

		    stgCreditDecisionTrn.put("totalDepositReleasedAmt", ficoDepositData.getDepositReleased());
		    stgCreditDecisionTrn.put("lastArDepositReleasedTs", CrdaUtility.asDate(ficoDepositData.getMostRecentDepositReleaseDate()));

		    stgCreditDecisionTrn.put("totalArDepositPendingAmt", ficoDepositData.getDepositPending());
		    stgCreditDecisionTrn.put("lastArDepositPendingTs", CrdaUtility.asDate(ficoDepositData.getMostRecentDepositPendingDate()));
		}*/
	}
	//MAP Collection Data
	private void populateCustomerCollectionData(
			Map<String, Object> stgCreditDecisionTrn,
			CustomerCollectionData customerCollectionData) {
		if(customerCollectionData!= null){
			stgCreditDecisionTrn.put("involuntaryCancelledAccounts", customerCollectionData.getInvoluntaryCancelledAccounts());
			stgCreditDecisionTrn.put("accountsInAgencyBalance", customerCollectionData.getAccountsInAgencyBalance());
			stgCreditDecisionTrn.put("collectionScore", customerCollectionData.getCollectionScore());
			stgCreditDecisionTrn.put("involuntaryCancelledAccounts", customerCollectionData.getInvoluntaryCancelledAccounts());
			stgCreditDecisionTrn.put("involuntaryCancelledAccountsBalance", customerCollectionData.getInvoluntaryCancelledAccountsBalance());
			stgCreditDecisionTrn.put("latestAgencyAssignmentDate", customerCollectionData.getLatestAgencyAssignmentDate());
			stgCreditDecisionTrn.put("latestCollectionEndDate", customerCollectionData.getLatestCollectionEndDate());
			stgCreditDecisionTrn.put("latestCollectionStartDate", customerCollectionData.getLatestCollectionStartDate());
			stgCreditDecisionTrn.put("latestInvoluntaryCancelledDate", customerCollectionData.getLatestInvoluntaryCancelledDate());
			stgCreditDecisionTrn.put("numberOfAccountsInAgency", customerCollectionData.getNumberOfAccountsInAgency());
			stgCreditDecisionTrn.put("numberOfNSFCheques", customerCollectionData.getNumberOfNSFCheques());
			stgCreditDecisionTrn.put("collectionInd", customerCollectionData.isActiveAccountsCollectionInd());
			stgCreditDecisionTrn.put("involuntaryCancelledAccounts", customerCollectionData.getInvoluntaryCancelledAccounts());
		}
		
		//Added by Alan for RTCA 1.6 - getting BA attributes from ThreadLocal object
		if (ThreadLocalUtil.getAdditionalCollectionData()!=null)
		{
			AdditionalCollectionData additionalCollectionData = ThreadLocalUtil.getAdditionalCollectionData();
			stgCreditDecisionTrn.put("collectionSegment" , additionalCollectionData.getCollectionSegment() );
			stgCreditDecisionTrn.put("scorecardID" , additionalCollectionData.getScorecardID() );
			stgCreditDecisionTrn.put("delinquencyCycle" , additionalCollectionData.getDelinquencyCycle() );
			
		}
	}

}