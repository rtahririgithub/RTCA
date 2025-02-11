package com.telus.credit.crda.dao.mgmt;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.rules.InternalRules;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;


public class GetOverrideCreditAssessmentDAOImp extends com.telus.credit.crda.dao.EcrdaAbstractSqlMapDao implements GetCreditAssessmentByAsmtTypeDAO {
    private static final Log log = LogFactory.getLog(GetOverrideCreditAssessmentDAOImp.class);

    @Override
    public void getCreditAssessmentByAsmtType(long creditAsmtID,
                                              HashMap<String, Object> baseCreditAssessmentMap,
                                              CreditAssessmentDetails returnedCompletedCreditAssessmentDetails)
            throws EnterpriseCreditAssessmentPolicyException,
            EnterpriseCreditAssessmentServiceException {
        List<HashMap<String, Object>> creditDecisionResultDtlList = null;
        HashMap<String, Object> overrideCreditAssessmentMap = null;

        try {
        	//get crd_decision_trn_result if fico was called for this request  .
            overrideCreditAssessmentMap = (HashMap<String, Object>) queryForObject(CreditAssessmentDaoConstants.GET_OVERRIDE_CRD_ASSESSMENT_REQUEST, creditAsmtID);
            Long intCrdtDscnTrnRsltId = (Long) overrideCreditAssessmentMap.get("intCrdtDscnTrnRsltId");
            if (intCrdtDscnTrnRsltId != null
                    && intCrdtDscnTrnRsltId.longValue() > 0) {
                creditDecisionResultDtlList = queryForList(CreditAssessmentDaoConstants.GET_CRD_DCSN_RESULT_BY_PARENT_ID, intCrdtDscnTrnRsltId);
                //set asmt source to FICO
            }
        } catch (com.telus.framework.exception.ObjectNotFoundException e) {
            //log.info( "Ignoring the exception for the scenario when only fraudind has changed as there is no entry in the above tables." +  e );
        }
        
        //get data from CAR_ATTR_VALUE for audit and all override credit assmt
        List<HashMap<String, Object>> carAttrValueMapList = (List<HashMap<String, Object>>) queryForList(CreditAssessmentDaoConstants.GET_CAR_ATTR_VALUE_BY_CAR_ID, creditAsmtID);

        GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();
        m_getCreditAssessmentHelper.populateOverrideCreditAssessment(
                baseCreditAssessmentMap,
                overrideCreditAssessmentMap,
                creditDecisionResultDtlList,
                carAttrValueMapList,
                returnedCompletedCreditAssessmentDetails);
        
         //determine CreditAssessmentDataSourceCd
        String CreditAssessmentDataSourceCd = InternalRules.determineCreditAssessmentDataSourceCd(returnedCompletedCreditAssessmentDetails);
        if (returnedCompletedCreditAssessmentDetails!= null){
        	returnedCompletedCreditAssessmentDetails.setCreditAssessmentDataSourceCd(CreditAssessmentDataSourceCd);
        }
        
    }


}
