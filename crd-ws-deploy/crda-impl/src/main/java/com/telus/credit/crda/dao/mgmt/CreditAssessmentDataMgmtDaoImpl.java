package com.telus.credit.crda.dao.mgmt;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.dao.base.BaseAssessmentDaoDataMapper;
import com.telus.credit.crda.dao.base.BaseAssessmentDaoImpl;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.factory.CreateGetCreditAssessmentByAsmtTypeDaoFactory;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.framework.exception.ObjectNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;


public class CreditAssessmentDataMgmtDaoImpl
        extends BaseAssessmentDaoImpl
        implements CreditAssessmentDataMgmtDao {
    public static final Log log = LogFactory.getLog(CreditAssessmentDataMgmtDaoImpl.class);

    //	private GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();
    private SearchCreditAssessmentHelper m_searchCreditAssessmentHelper = SearchCreditAssessmentHelper.instanceOf();
    public GetCreditAssessmentByAsmtTypeDAO m_getFullCreditAssessmentDAOImp;

    @Override
    public CreditAssessmentDetails getCreditAssessment(long creditAsmtID) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
      CreditAssessmentDetails creditAssessmentDetails = new CreditAssessmentDetails();
        try {
            //get and populate base data
            HashMap<String, Object> baseCreditAssessmentMap = (HashMap<String, Object>) queryForObject(CreditAssessmentDaoConstants.GET_BASE_CREDIT_ASSESSMENT_BY_ID, creditAsmtID);

            //TODO refactor GetCreditAssessmentHelper
            GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();
            m_getCreditAssessmentHelper.populateBaseCreditAssessmentDetails(baseCreditAssessmentMap, creditAssessmentDetails);
            //create the proper DAO to get additional data
            GetCreditAssessmentByAsmtTypeDAO aGetAsmtAdditionalDataDAO = CreateGetCreditAssessmentByAsmtTypeDaoFactory.createGetCreditAssessmentByAsmtTypeDao(
                    creditAssessmentDetails.getCreditAssessmentTypeCd(),
                    creditAssessmentDetails.getCreditAssessmentSubTypeCd()
                    );
            aGetAsmtAdditionalDataDAO.getCreditAssessmentByAsmtType(creditAsmtID, baseCreditAssessmentMap, creditAssessmentDetails);
        } catch (ObjectNotFoundException e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Credit Assessment  id", creditAsmtID, e);

        }
        return creditAssessmentDetails;
    }    
    @Override
    public CreditAssessmentTransaction getBaseCreditAssessment(long aCARID) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        CreditAssessmentTransaction result = new CreditAssessmentTransaction();
        HashMap<String, Object> baseCreditAssessmentMap = null;
        try {
            baseCreditAssessmentMap = (HashMap<String, Object>) queryForObject(CreditAssessmentDaoConstants.GET_BASE_CREDIT_ASSESSMENT_BY_ID, aCARID);
        } catch (ObjectNotFoundException e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "CARID", aCARID, e);
        }
        BaseAssessmentDaoDataMapper.populateBaseCreditAssessmentDetails(baseCreditAssessmentMap, result);
        return result;
    }
/*    private GetAsmtAdditionalDataDAO createGetAdditionaldataDAO(String asmttype,String asmtSubtype) {
        GetAsmtAdditionalDataDAO getAsmtAdditionalDataDAO = null;
        if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT)) {
            getAsmtAdditionalDataDAO = new GetCompleteCreditAssessmentDAOImp();
         } else {
            if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT)) {
                getAsmtAdditionalDataDAO = new GetOverrideCreditAssessmentDAOImp();
            } else {
                if (asmttype.equals(EnterpriseCreditAssessmentConsts.ASMT_TYPE_AUDIT)) {
                    getAsmtAdditionalDataDAO = new GetAuditCreditAssessmentDAOImp();
                  }
            }
        }
        return getAsmtAdditionalDataDAO;
    }*/

    
    
    @Override
    public List<CreditAssessmentTransaction> searchCreditAssessments(
            SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria,
            AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        List<CreditAssessmentTransaction> creditAssessmentTransactionList = new ArrayList<CreditAssessmentTransaction>();
        Map<String, Object> searchCriteria = new HashMap<String, Object>();
        try {
            if (searchCreditAssessmentsRequestCriteria != null) {
                searchCriteria.put("customerId", searchCreditAssessmentsRequestCriteria.getCustomerID());
                if (searchCreditAssessmentsRequestCriteria.getAssesssmentFromDate() != null) {
                    searchCriteria.put("assessmentFromDate", (Date) searchCreditAssessmentsRequestCriteria.getAssesssmentFromDate());
                }
                if (searchCreditAssessmentsRequestCriteria.getAssesssmentToDate() != null) {
                    searchCriteria.put("assessmenteToDate", (Date) searchCreditAssessmentsRequestCriteria.getAssesssmentToDate());
                }
                if (searchCreditAssessmentsRequestCriteria.getCreditAssessmentTypeCd() != null) {
                    searchCriteria.put("creditAssessmentTypeCd", searchCreditAssessmentsRequestCriteria.getCreditAssessmentTypeCd());
                }
                if (searchCreditAssessmentsRequestCriteria.getCreditAssessmentSubTypeCd() != null) {
                    searchCriteria.put("creditAssessmentSubTypeCd", searchCreditAssessmentsRequestCriteria.getCreditAssessmentSubTypeCd());
                }
                if (searchCreditAssessmentsRequestCriteria.getCreditAssessmentStatus() != null) {
                    searchCriteria.put("creditAssessmentStatus", searchCreditAssessmentsRequestCriteria.getCreditAssessmentStatus());
                }
            }
            if (auditInfo != null) {
                if (auditInfo.getChannelOrganizationId() != null) {
                    searchCriteria.put("channelOrgId", auditInfo.getChannelOrganizationId());
                }
                if (auditInfo.getCorrelationId() != null) {
                    searchCriteria.put("correlationId", auditInfo.getCorrelationId());
                }
                if (auditInfo.getOriginatorApplicationId() != null) {
                    searchCriteria.put("orgApplicationId", auditInfo.getOriginatorApplicationId());
                }
                if (auditInfo.getOutletId() != null) {
                    searchCriteria.put("outletId", auditInfo.getOutletId());
                }
                if (auditInfo.getSalesRepresentativeId() != null) {
                    searchCriteria.put("salesRepId", auditInfo.getSalesRepresentativeId());
                }

                if (auditInfo.getUserId() != null) {
                    searchCriteria.put("userId", auditInfo.getUserId());
                }

            }
            
            
            
            List<HashMap<String, Object>> searchNonDncCreditAssessmentResult = queryForList("search_credit_assessment.search_nondcn_credit_assessments", searchCriteria);
            List<HashMap<String, Object>> searchDcnCreditAssessmentResult = queryForList(CreditAssessmentDaoConstants.SEARCH_CREDIT_ASSESSMENT, searchCriteria);

            List<HashMap<String, Object>> searchCreditAssessmentDecisionResultDtl = queryForList(CreditAssessmentDaoConstants.SEARCH_CREDIT_ASSESSMENT_DECISION_RESULT_DTL,
                    searchCriteria);
            searchDcnCreditAssessmentResult.addAll(searchNonDncCreditAssessmentResult);
            creditAssessmentTransactionList = m_searchCreditAssessmentHelper.populateCreditAssessmentTransactionList(searchDcnCreditAssessmentResult,
                    searchCreditAssessmentDecisionResultDtl);
            
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName()
                    , "Credit Assessment  id", searchCreditAssessmentsRequestCriteria, e);

        }
        return creditAssessmentTransactionList;
    }

    @Override
    public void voidCreditAssessment(long creditAssessmentID,
                                     String voidReasonCode, AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        validateVoidCreditAssessment(creditAssessmentID);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("carId", creditAssessmentID);
        param.put("userId", auditInfo.getUserId());
        param.put("carActivityType", EnterpriseCreditAssessmentConsts.CAR_ACTIVITY_VOID);
        param.put("carStatus", EnterpriseCreditAssessmentConsts.CAR_RESULT_STATUS_CANCELLED);
        param.put("carResult", EnterpriseCreditAssessmentConsts.CAR_RESULT_STATUS_CANCELLED);
        param.put("voidReasonCode", voidReasonCode);

        long carStatusId = updateCreditAssessmentRequestStatus(
                creditAssessmentID,
                (auditInfo != null ? auditInfo.getUserId() : null),
                EnterpriseCreditAssessmentConsts.CAR_STATUS_CANCELLED);

        createCreditAssessmentRequestActivity(creditAssessmentID,
                (auditInfo != null ? auditInfo.getUserId() : null),
                EnterpriseCreditAssessmentConsts.CAR_ACTIVITY_VOID,
                carStatusId,
                voidReasonCode);

        expireCreditBureauTransaction(creditAssessmentID, EnterpriseCreditAssessmentConsts.CREDIT_BUREAU_TRN_STATUS_VOID, auditInfo);
    }

    private void validateVoidCreditAssessment(
            long creditAssessmentID) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {


        if (creditAssessmentID <= 0) {
            throw new EnterpriseCreditAssessmentPolicyException(
                    EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_ASSESSMENT_ID_STR,
                    EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_ASSESSMENT_ID);
        }
        CreditAssessmentTransaction creditAssessmentTransaction = getBaseCreditAssessment(creditAssessmentID);
        if (creditAssessmentTransaction.getCreditAssessmentTypeCd() != null
                && 
                (
                		creditAssessmentTransaction.getCreditAssessmentSubTypeCd().equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_AUTO_ASMT)
                || creditAssessmentTransaction.getCreditAssessmentSubTypeCd().equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_GET_BUREAU_DATA)
                || creditAssessmentTransaction.getCreditAssessmentSubTypeCd().equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_NEW_ACC_ASSESSMENT)
                || creditAssessmentTransaction.getCreditAssessmentSubTypeCd().equals(EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_MANUAL_ASSESSMENT)
                )
        	) {
        	log.debug("CreditAssessmentSubTypeCd is supported for void operation ");
        } else {
            throw new EnterpriseCreditAssessmentPolicyException("Only Credit Assessment of sub type: " + EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_AUTO_ASMT
                    + " or: " + EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_GET_BUREAU_DATA
                    + " can be voided.",
                    EnterpriseCreditAssessmentExceptionCodes.VOID_NOT_ALLOWED_FOR_THIS_CAR);
        }
        if (creditAssessmentTransaction.getCreditAssessmentStatus() != null
                && creditAssessmentTransaction.getCreditAssessmentStatus().equals(EnterpriseCreditAssessmentConsts.CAR_RESULT_STATUS_CANCELLED)) {
            throw new EnterpriseCreditAssessmentPolicyException("This Credit Assessment, car id: " + creditAssessmentID + " is already CANCELLED and therefore cannot be voided.",
                    EnterpriseCreditAssessmentExceptionCodes.VOID_NOT_ALLOWED_FOR_THIS_CAR);
        }
    }
    
 
   
    @Override
    public CreditBureauResult getCreditBureauTransResultByCustID(long customerId) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

    	CreditBureauResult creditBureauResult = new CreditBureauResult();
        try {
            HashMap<String, Object> creditBureauTransResultMap = (HashMap<String, Object>) queryForObject("get_credit_assessment.get_credit_bureau_trn", customerId);
            //get bureua result detail result
            Long creditBureauTrnId = (Long) creditBureauTransResultMap.get("creditBureauTrnId");
            List<HashMap<String, Object>> creditBureauTrnDtlList = null;
            if (creditBureauTrnId != null && creditBureauTrnId > 0) {
                //CREDIT_BUREAU_TRN_DTL
                creditBureauTrnDtlList = queryForList(CreditAssessmentDaoConstants.GET_CRD_BUREAU_TRN_DTL_BY_PARENT_ID, creditBureauTrnId);
                GetCreditAssessmentHelper m_getCreditAssessmentHelper = GetCreditAssessmentHelper.instanceOf();
                //creditAssessmentDetails
                m_getCreditAssessmentHelper.populatetCreditBureauTransResult(
                        creditBureauTransResultMap,
                        creditBureauTrnDtlList,
                        creditBureauResult);
            
            }
  
        } 
        catch (ObjectNotFoundException e) {
            log.debug("No CreditBureauTransResult found for "+ "CustomerID =" +customerId);
        }
        catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName()
                    , "Credit Assessment  id", customerId, e);
        }
         
        return creditBureauResult;
    }


	@Override
	public String getDocumentumPathId( String creditBureauTransactionDetailId ) 
		throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException
	{  
		String gaDocumentumPathId="";
		try {
			gaDocumentumPathId=(String) queryForObject(CreditAssessmentDaoConstants.GET_CREDIT_BUREAU_TRN_DTL_DOC_PATH, new Long( creditBureauTransactionDetailId ));				
		}catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName()
                    , "creditBureauTransactionDetailId  id", creditBureauTransactionDetailId, e);
        }
		return gaDocumentumPathId;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public HashMap<String, Object> getCreditBureauMap(String creditBureauTransactionDetailId)
		throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException 
	{
		HashMap<String, Object> creditBureauTransMap = null;
		try {
			creditBureauTransMap = (HashMap<String, Object>) queryForObject(CreditAssessmentDaoConstants.GET_CREDIT_BUREAU_TRN_MAP, new Long( creditBureauTransactionDetailId ));
		} catch (Throwable e) {
			CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName()
                    , "creditBureauTransactionDetailId  id", creditBureauTransactionDetailId, e);
		} 
		return creditBureauTransMap;
	}
}
