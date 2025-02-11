package com.telus.credit.crda.factory;

import com.telus.credit.crda.dao.mgmt.GetCreditAssessmentByAsmtTypeDAO;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;

public class CreateGetCreditAssessmentByAsmtTypeDaoFactory extends BaseAssessmentFactory {


    public static GetCreditAssessmentByAsmtTypeDAO createGetCreditAssessmentByAsmtTypeDao(String asmttype, String asmtSubtype) {
        GetCreditAssessmentByAsmtTypeDAO getAsmtAdditionalDataDAO = null;
        if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT)) {
            getAsmtAdditionalDataDAO = (GetCreditAssessmentByAsmtTypeDAO) m_ApplicationContext.getBean("GetCompleteCreditAssessmentDAOImp");
        } else {
            if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT)) {
                getAsmtAdditionalDataDAO = (GetCreditAssessmentByAsmtTypeDAO) m_ApplicationContext.getBean("GetOverrideCreditAssessmentDAOImp");
            } else {
                if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_AUDIT)) {
                    getAsmtAdditionalDataDAO = (GetCreditAssessmentByAsmtTypeDAO) m_ApplicationContext.getBean("GetAuditCreditAssessmentDAOImp");
                }
            }
        }
        return getAsmtAdditionalDataDAO;
    }
   
}
