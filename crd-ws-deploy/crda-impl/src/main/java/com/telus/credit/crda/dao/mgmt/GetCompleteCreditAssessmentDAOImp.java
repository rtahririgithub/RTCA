package com.telus.credit.crda.dao.mgmt;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;
/*
 *  This DAO covers any FULL ASMT Types
 */
public class GetCompleteCreditAssessmentDAOImp
        extends com.telus.credit.crda.dao.EcrdaAbstractSqlMapDao
        implements
        GetCreditAssessmentByAsmtTypeDAO {
    public static final Log log = LogFactory.getLog(GetCompleteCreditAssessmentDAOImp.class);

    public void getCreditAssessmentByAsmtType(
            long creditAsmtID,
            HashMap<String, Object> baseCreditAssessmentMap,
            CreditAssessmentDetails returnedCompletedCreditAssessmentDetails)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        try {
        	HashMap<String, Object> completeCreditAssessmentMap =null;
             completeCreditAssessmentMap = (HashMap<String, Object>) queryForObject(CreditAssessmentDaoConstants.GET_FULL_CRD_ASSESSMENT_REQUEST, creditAsmtID);
             List<HashMap<String, Object>> creditDecisionResultDtlList = null;
            Long intCrdtDscnTrnRsltId = (Long) completeCreditAssessmentMap.get("intCrdtDscnTrnRsltId");
 
            if (intCrdtDscnTrnRsltId != null && intCrdtDscnTrnRsltId > 0) {
                //get fico result detail result from 
                creditDecisionResultDtlList = queryForList(CreditAssessmentDaoConstants.GET_CRD_DCSN_RESULT_BY_PARENT_ID, intCrdtDscnTrnRsltId);
            }
            //get bureua result detail result
            Long creditBureauTrnId = (Long) completeCreditAssessmentMap.get("creditBureauTrnId");
            List<HashMap<String, Object>> creditBureauTrnDtlList = null;
            if (creditBureauTrnId != null && creditBureauTrnId > 0) {
                //CREDIT_BUREAU_TRN_DTL
                creditBureauTrnDtlList = queryForList(CreditAssessmentDaoConstants.GET_CRD_BUREAU_TRN_DTL_BY_PARENT_ID, creditBureauTrnId);
            }
            GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();
            //creditAssessmentDetails
            m_getCreditAssessmentHelper.populateCreditAssessmentDetails(
                    baseCreditAssessmentMap,
                    completeCreditAssessmentMap,
                    creditDecisionResultDtlList,
                    creditBureauTrnDtlList,
                    returnedCompletedCreditAssessmentDetails);
            
            
            //determine CreditAssessmentDataSourceCd
            String CreditAssessmentDataSourceCd = InternalRules.determineCreditAssessmentDataSourceCd(returnedCompletedCreditAssessmentDetails);
            if (returnedCompletedCreditAssessmentDetails!= null){
            	returnedCompletedCreditAssessmentDetails.setCreditAssessmentDataSourceCd(CreditAssessmentDataSourceCd);
            }
            
        } catch (Throwable e) {
        	//com.telus.framework.exception.ObjectNotFoundException: {ID:176354171389398717617} No record for: get_credit_assessment.get_full_credit_assessment
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName()
                    , ". Credit Assessment not found or the credit Assessment does not have a successfull decisioning result. Credit Assessment id ", creditAsmtID, e);

        }


    }
}
