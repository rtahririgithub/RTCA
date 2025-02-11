package com.telus.credit.crda.dao.mgmt;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;


public class GetAuditCreditAssessmentDAOImp extends com.telus.credit.crda.dao.EcrdaAbstractSqlMapDao
        implements
        GetCreditAssessmentByAsmtTypeDAO {
    private static final Log log = LogFactory.getLog(GetAuditCreditAssessmentDAOImp.class);

    @Override
    public void getCreditAssessmentByAsmtType(
            long creditAsmtID,
            HashMap<String, Object> baseCreditAssessmentMap,
            CreditAssessmentDetails creditAssessmentDetails)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        try {

            List<HashMap<String, Object>> carAttrValueMapList = (List<HashMap<String, Object>>) queryForList(CreditAssessmentDaoConstants.GET_CAR_ATTR_VALUE_BY_CAR_ID, creditAsmtID);

            GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();
            m_getCreditAssessmentHelper.populateAuditCreditAssessment(
                    baseCreditAssessmentMap,
                    carAttrValueMapList,
                    creditAssessmentDetails);
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Credit Assessment  id", creditAsmtID, e);

        }
    }
}


