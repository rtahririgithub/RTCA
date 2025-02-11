package com.telus.credit.crda.dao.dcn;


import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;
import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.domain.common.CreditAddress;
import com.telus.credit.domain.common.CreditCustomerInfo;
import com.telus.credit.domain.common.CreditIdentification;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.WlsAccountFinancialHistory;
import com.telus.credit.domain.crda.WlsCreditBureauResult;
import com.telus.credit.domain.crda.WlsMatchedAccount;
import com.telus.credit.domain.crda.WlsUnifiedCreditSearchResult;
import com.telus.credit.domain.crda.WlsWarningHistory;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.credit.domain.creditprofile.ConsumerCreditProfile;
import com.telus.credit.domain.creditprofile.CreditWorthiness;
 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DecisiongDaoImpFullAssessment
        extends DecisioningDaoImpCommon {

    public void storeDecisioningEngineInput(
            CreditAssessmentRequest aCreditAssessmentRequest,
            AuditInfo auditInfo,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionDetails,
            boolean failOverIndicator) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        Map<String, Object> stgCreditDecisionTrn = populateStagingCreditDecisionTransaction(aCreditAssessmentRequest, auditInfo, aCreditAssessmentTransactionDetails.getCreditAssessmentID(), failOverIndicator);
        long dbStgCreditDecisionTrnId = 0;
        try {
            dbStgCreditDecisionTrnId = (Long) insert(CreditAssessmentDaoConstants.SAVE_STG_CREDIT_DCSN_TRN, (HashMap<String, Object>) stgCreditDecisionTrn);

            //All UC input should be saved if it exists regardless of FICO desc response
            if(aCreditAssessmentRequest instanceof FullCreditAssessmentRequest &&
            		((FullCreditAssessmentRequest)aCreditAssessmentRequest).getUnifiedCreditSearchResult()!=null ) {
                //save WLS match info: UC_DATA_INQUIRY_ERROR, UC_SEARCH_RESULT, UC_WLS_MATCH_ACCOUNT, UC_WARNING_HIST
                storeUnifiedCreditInput((FullCreditAssessmentRequest)aCreditAssessmentRequest, auditInfo.getUserId(), dbStgCreditDecisionTrnId);            	
            }
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "Customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
        aCreditAssessmentTransactionDetails.setDbStgCreditDecisionTrnId(dbStgCreditDecisionTrnId);
    }

    protected Map<String, Object> populateStagingCreditDecisionTransaction(CreditAssessmentRequest aCreditAssessmentRequest,
                                                                           AuditInfo auditInfo,
                                                                           long aCAR_ID,
                                                                           boolean failOverHandler)
            throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {

        LogUtil.traceCalllog(LogUtil.getClassMethodNames(Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getClassName()));
        
        FullCreditAssessmentRequest aFullCreditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
        Map<String, Object> stgCreditDecisionTrn = new HashMap<String, Object>();
        try {
            
            stgCreditDecisionTrn.put("userId", auditInfo.getUserId());
            stgCreditDecisionTrn.put("carId", aCAR_ID);
            stgCreditDecisionTrn.put("customerId", aFullCreditAssessmentRequest.getCustomerID());

          //Map creditCustomerInfo
            CreditCustomerInfo creditCustomerInfo = aFullCreditAssessmentRequest.getCreditCustomerInfo();
            populateCreditCustomerInfo(stgCreditDecisionTrn, creditCustomerInfo);
            
            ConsumerCreditProfile creditProfileData = aFullCreditAssessmentRequest.getCreditProfileData();
          //Map ConsumerCreditProfile            
            populateConsumerCreditProfile(failOverHandler, stgCreditDecisionTrn, creditProfileData);
 
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "customer id", aCreditAssessmentRequest.getCustomerID(), e);
        }
        return stgCreditDecisionTrn;
    }

	private void populateConsumerCreditProfile(boolean failOverHandler,
			Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		
		//Map ConsumerCreditProfile
		populateCreditProfile(failOverHandler, stgCreditDecisionTrn,creditProfileData);
		
		//MAP CreditIdentification
		populateCreditIdentification(stgCreditDecisionTrn,creditProfileData);
		
		//MAP credit profile Credit address
		populateCreditAddress(stgCreditDecisionTrn, creditProfileData);
		
		//Map CreditCard
		populateCreditCard(stgCreditDecisionTrn, creditProfileData);
		
		//Map creditProfileData.getPersonalInfo
		populateCreditProfileDataPersonalInfo(stgCreditDecisionTrn,creditProfileData);
		
         //Map creditProfileData.getCreditWorthiness
		populateExistingCreditWorthiness(stgCreditDecisionTrn,creditProfileData);
	}

	private void populateExistingCreditWorthiness(
			Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		if (creditProfileData!= null && creditProfileData.getCreditWorthiness() != null) {
		    stgCreditDecisionTrn.put("decisionCd", creditProfileData.getCreditWorthiness().getDecisionCd());
		    stgCreditDecisionTrn.put("fraudIndicatorCd", creditProfileData.getCreditWorthiness().getFraudIndicatorCd());
		    stgCreditDecisionTrn.put("creditValueCd", creditProfileData.getCreditWorthiness().getCreditValueCd());
		    //Adding Risk Level Num as part of Unified Credit 
		    stgCreditDecisionTrn.put("creditRiskLevel", creditProfileData.getCreditWorthiness().getCreditRiskLevel());
		}
	}

	private void populateCreditProfile(boolean failOverHandler,
			Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		if (creditProfileData.getCreditProfileID() != null) {
		    stgCreditDecisionTrn.put("creditProfileId", creditProfileData.getCreditProfileID());
		}
		if (creditProfileData.getCreditProfileStatusCd()!= null && 
				creditProfileData.getCreditProfileStatusCd().trim().length() > 0) {
		    stgCreditDecisionTrn.put("creditProfileStatusCd", creditProfileData.getCreditProfileStatusCd().trim());
		}            
		if (creditProfileData.getMethodCd() != null
		        && creditProfileData.getMethodCd().trim().length() > 0) {
		    stgCreditDecisionTrn.put("creditProfileMethodCd", creditProfileData.getMethodCd().trim());
		}
		if (creditProfileData.getApplicationProvinceCd() != null
		        && creditProfileData.getApplicationProvinceCd().trim().length() > 0) {
		    stgCreditDecisionTrn.put("applicationProvinceCd", creditProfileData.getApplicationProvinceCd().trim());
		}
		if (creditProfileData.getBusinessLastUpdateTimestamp() != null) {
		    stgCreditDecisionTrn.put("creditProfileBusinessLastUpdateTS", creditProfileData.getBusinessLastUpdateTimestamp());
		}
		stgCreditDecisionTrn.put("bypassMatchInd", CrdaUtility.getBooleanStringValue(creditProfileData.isBypassMatchIndicator()));
		stgCreditDecisionTrn.put("failoverIndicator", CrdaUtility.getBooleanStringValue(failOverHandler));
		
        //MAP new Creditprofile attributes
		if (creditProfileData.getFirstCreditAssessmentDate() != null) {
			stgCreditDecisionTrn.put("firstCreditAssessmentDate", creditProfileData.getFirstCreditAssessmentDate());
		}
		if (creditProfileData.getLatestCreditAssessmentDate() != null) {		
			stgCreditDecisionTrn.put("latestCreditAssessmentDate", creditProfileData.getLatestCreditAssessmentDate());
		}
		if (creditProfileData.getCreditValueEffectiveDate() != null) {
			stgCreditDecisionTrn.put("creditValueEffectiveDate", creditProfileData.getCreditValueEffectiveDate());		
		}
	}

    
	//Map creditCustomerInfo
	private void populateCreditCustomerInfo(
			Map<String, Object> stgCreditDecisionTrn,
			CreditCustomerInfo creditCustomerInfo) {
		if(creditCustomerInfo!= null ){
			if (creditCustomerInfo.getPersonName() != null) {
			    stgCreditDecisionTrn.put("firstName", creditCustomerInfo.getPersonName().getFirstName());
			    stgCreditDecisionTrn.put("middleName", creditCustomerInfo.getPersonName().getMiddleName());
			    stgCreditDecisionTrn.put("lastName", creditCustomerInfo.getPersonName().getLastName());
			}
			stgCreditDecisionTrn.put("customerCreationDate", (Date) creditCustomerInfo.getCustomerCreationDate());
			stgCreditDecisionTrn.put("customerMasterSrcId", creditCustomerInfo.getCustomerMasterSourceID());
			stgCreditDecisionTrn.put("customerStatusCd", creditCustomerInfo.getCustomerStatusCd());
			stgCreditDecisionTrn.put("customerSubTypeCd", creditCustomerInfo.getCustomerSubTypeCd());
			stgCreditDecisionTrn.put("customerTypeCd", creditCustomerInfo.getCustomerTypeCd());
			if (creditCustomerInfo.getTelecomContactList() != null
			        && creditCustomerInfo.getTelecomContactList().size() > 0) {
			    stgCreditDecisionTrn.put("contactPhoneNum", CrdaUtility.extractPhoneNumbers(creditCustomerInfo));
	
			}
            stgCreditDecisionTrn.put("employeeInd", CrdaUtility.getBooleanStringValue(creditCustomerInfo.isEmployeeInd()));
            stgCreditDecisionTrn.put("customerTypeCd", creditCustomerInfo.getCustomerTypeCd());
            stgCreditDecisionTrn.put("revenueSegmentCd", creditCustomerInfo.getRevenueSegmentCd());			
		}
	}
//MAP CreditIdentification
	private void populateCreditIdentification(
			Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		if (creditProfileData!=null && creditProfileData.getCreditIdentification() != null) {
			CreditIdentification creditIdentification = creditProfileData.getCreditIdentification();
		    if (creditIdentification.getDriverLicense() != null
		            && creditIdentification.getDriverLicense().getDriverLicenseNum() != null
		            && creditIdentification.getDriverLicense().getDriverLicenseNum().trim().length() > 0) {
		        stgCreditDecisionTrn.put("drivingLicenseNum", EncryptionUtil.encrypt(creditIdentification.getDriverLicense().getDriverLicenseNum()));
		        stgCreditDecisionTrn.put("drivingLicenseProvinceCd", creditIdentification.getDriverLicense().getProvinceCd());
		    }
		    if (creditIdentification.getSin() != null && creditIdentification.getSin().trim().length() > 0) {
		        stgCreditDecisionTrn.put("socialInsuranceNum", EncryptionUtil.encrypt(creditIdentification.getSin()));
		    }
		    if (creditIdentification.getHealthCard() != null
		            && creditIdentification.getHealthCard().getHealthCardNum() != null
		            && creditIdentification.getHealthCard().getHealthCardNum().trim().length() > 0) {
		        stgCreditDecisionTrn.put("healthCareNum", EncryptionUtil.encrypt(creditIdentification.getHealthCard().getHealthCardNum()));
		    }
		    if (creditIdentification.getHealthCard() != null
		            && creditIdentification.getHealthCard().getProvinceCd() != null
		            && creditIdentification.getHealthCard().getProvinceCd().trim().length() > 0) {
		        stgCreditDecisionTrn.put("healthCareProvinceCd", creditIdentification.getHealthCard().getProvinceCd());
		    }
		    if (creditIdentification.getPassport() != null
		            && creditIdentification.getPassport().getPassportNum() != null
		            && creditIdentification.getPassport().getPassportNum().trim().length() > 0) {
		        stgCreditDecisionTrn.put("passportNum", EncryptionUtil.encrypt(creditIdentification.getPassport().getPassportNum()));
		    }
		    if (creditIdentification.getPassport() != null
		            && creditIdentification.getPassport().getCountryCd() != null
		            && creditIdentification.getPassport().getCountryCd().trim().length() > 0) {
		        stgCreditDecisionTrn.put("passportCountryCd", creditIdentification.getPassport().getCountryCd());
		    }
		    if (creditIdentification.getProvincialIdCard() != null
		            && creditIdentification.getProvincialIdCard().getProvincialIdNum() != null
		            && creditIdentification.getProvincialIdCard().getProvincialIdNum().trim().length() > 0) {
		        stgCreditDecisionTrn.put("provincialId", EncryptionUtil.encrypt(creditIdentification.getProvincialIdCard().getProvincialIdNum()));
		    }
		    if (creditIdentification.getProvincialIdCard() != null
		            && creditIdentification.getProvincialIdCard().getProvinceCd() != null
		            && creditIdentification.getProvincialIdCard().getProvinceCd().trim().length() > 0) {
		        stgCreditDecisionTrn.put("provincialIdProvinceCd", creditIdentification.getProvincialIdCard().getProvinceCd());
		    }
		}
		
	}

  //Map CreditCard
	private void populateCreditCard(Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		if(creditProfileData!= null){
			if (creditProfileData.getCreditCardCd() != null
			        && creditProfileData.getCreditCardCd().getPrimaryCreditCardCd() != null
			        && creditProfileData.getCreditCardCd().getPrimaryCreditCardCd().trim().length() > 0) {
			    stgCreditDecisionTrn.put("creditCardTypeCd", creditProfileData.getCreditCardCd().getPrimaryCreditCardCd().trim());
			}
			if (creditProfileData.getCreditCardCd() != null
			        && creditProfileData.getCreditCardCd().getSecondaryCreditCardCd() != null
			        && creditProfileData.getCreditCardCd().getSecondaryCreditCardCd().trim().length() > 0) {
			    stgCreditDecisionTrn.put("secondaryCreditCardCd", creditProfileData.getCreditCardCd().getSecondaryCreditCardCd().trim());
			}
		}
	}

  //MAP credit profile Credit address
    private void populateCreditAddress(
			Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		
		if (creditProfileData!= null && creditProfileData.getCreditAddress() != null) {
			CreditAddress creditAddress = creditProfileData.getCreditAddress();
		    if (creditAddress.getAddressLineOne() != null
		            && creditAddress.getAddressLineOne().trim().length() > 0) {
		        stgCreditDecisionTrn.put("addressLine1", creditAddress.getAddressLineOne().trim());
		    }
		    if (creditAddress.getAddressLineTwo() != null
		            && creditAddress.getAddressLineTwo().trim().length() > 0) {
		        stgCreditDecisionTrn.put("addressLine2", creditAddress.getAddressLineTwo().trim());
		    }
		    if (creditAddress.getAddressLineThree() != null
		            && creditAddress.getAddressLineThree().trim().length() > 0) {
		        stgCreditDecisionTrn.put("addressLine3", creditAddress.getAddressLineThree().trim());
		    }
		    if (creditAddress.getAddressLineFour() != null
		            && creditAddress.getAddressLineFour().trim().length() > 0) {
		        stgCreditDecisionTrn.put("addressLine4", creditAddress.getAddressLineFour().trim());
		    }
		    if (creditAddress.getAddressLineFive() != null
		            && creditAddress.getAddressLineFive().trim().length() > 0) {
		        stgCreditDecisionTrn.put("addressLine5", creditAddress.getAddressLineFive().trim());
		    }
		    if (creditAddress.getCityName() != null
		            && creditAddress.getCityName().trim().length() > 0) {
		        stgCreditDecisionTrn.put("cityName", creditAddress.getCityName().trim());
		    }
		    if (creditAddress.getProvinceCd() != null
		            && creditAddress.getProvinceCd().trim().length() > 0) {
		        stgCreditDecisionTrn.put("provinceCd", creditAddress.getProvinceCd().trim());
		    }
		    if (creditAddress.getCountryCd() != null
		            && creditAddress.getCountryCd().trim().length() > 0) {
		        stgCreditDecisionTrn.put("countryCd", creditAddress.getCountryCd().trim());
		    }
		    if (creditAddress.getPostalCd() != null
		            && creditAddress.getPostalCd().trim().length() > 0) {
		        stgCreditDecisionTrn.put("postalCd", creditAddress.getPostalCd().trim());
		    }
		}
	}
  //Map creditprofile Personal info
	private void populateCreditProfileDataPersonalInfo(
			Map<String, Object> stgCreditDecisionTrn,
			ConsumerCreditProfile creditProfileData) {
		if (creditProfileData.getPersonalInfo() != null){
			if ((creditProfileData.getPersonalInfo().getUnderLegalCareCd() != null)
			        && (creditProfileData.getPersonalInfo().getUnderLegalCareCd().trim().length() > 0)
			        ) {
			    stgCreditDecisionTrn.put("legalCareCd", creditProfileData.getPersonalInfo().getUnderLegalCareCd().trim());
			}
			if ((creditProfileData.getPersonalInfo().getBirthDate() != null)
			        ) {
			    stgCreditDecisionTrn.put("birthDate", creditProfileData.getPersonalInfo().getBirthDate());
			}
			
			if ((creditProfileData.getPersonalInfo().getResidencyCd() != null)
			        && (creditProfileData.getPersonalInfo().getResidencyCd().trim().length() > 0)
			        ) {
			    stgCreditDecisionTrn.put("residencyCd", creditProfileData.getPersonalInfo().getResidencyCd().trim());
			}
			if ((creditProfileData.getPersonalInfo().getEmploymentStatusCd() != null)
			        && (creditProfileData.getPersonalInfo().getEmploymentStatusCd().trim().length() > 0)
			        ) {
			    stgCreditDecisionTrn.put("employmentStatusCd", creditProfileData.getPersonalInfo().getEmploymentStatusCd().trim());
			}
			if (creditProfileData.getPersonalInfo().getCreditCheckConsentCd() != null
			        && creditProfileData.getPersonalInfo().getCreditCheckConsentCd().trim().length() > 0) {
			    stgCreditDecisionTrn.put("creditCheckConsentCd", creditProfileData.getPersonalInfo().getCreditCheckConsentCd());
			}
			if (creditProfileData.getPersonalInfo().getCreditCheckConsentCd() != null
			        && creditProfileData.getPersonalInfo().getCreditCheckConsentCd().trim().length() > 0) {
			    stgCreditDecisionTrn.put("provinceOfCurrentResidenceCd", creditProfileData.getPersonalInfo().getProvinceOfCurrentResidenceCd());
			}
		}		
	}


	private void storeUnifiedCreditInput(FullCreditAssessmentRequest aCreditAssessmentRequest,
			String userId, Long dbStgCreditDecisionTrnId) throws Throwable {
		if(aCreditAssessmentRequest.getUnifiedCreditSearchResult() != null &&
				aCreditAssessmentRequest.getUnifiedCreditSearchResult() instanceof WlsUnifiedCreditSearchResult) {
			storeUC_SEARCH_RESULT((WlsUnifiedCreditSearchResult)aCreditAssessmentRequest.getUnifiedCreditSearchResult(), userId, dbStgCreditDecisionTrnId);
		}
	}

	private void storeUC_SEARCH_RESULT(WlsUnifiedCreditSearchResult aWlsUnifiedCreditSearchResult, String userId,
			Long dbStgCreditDecisionTrnId) throws Throwable {
		Map<String, Object> ucSearchResultTrn = new HashMap<String, Object>();
        ucSearchResultTrn.put("userId", userId);
        ucSearchResultTrn.put("STG_CREDIT_DSCN_TRN_ID", dbStgCreditDecisionTrnId);
        ucSearchResultTrn.put("MATCH_FOUND_IND", (aWlsUnifiedCreditSearchResult.getMatchFound()==null)?"N":aWlsUnifiedCreditSearchResult.getMatchFound().isIndicator()==false?"N":"Y");
        ucSearchResultTrn.put("MATCH_FOUND_REASON_CD", (aWlsUnifiedCreditSearchResult.getMatchFound()==null)?null:aWlsUnifiedCreditSearchResult.getMatchFound().getReasonCd());
        ucSearchResultTrn.put("LINE_OF_BUSINESS", aWlsUnifiedCreditSearchResult.getLineOfBusiness());
        ucSearchResultTrn.put("UC_DORMANT_IND", (aWlsUnifiedCreditSearchResult.isUnifiedCreditDormantInd()==false)?"N":"Y");
        if(aWlsUnifiedCreditSearchResult.getMatchedAccount()!=null) {
        	ucSearchResultTrn.put("BRAND_ID", (aWlsUnifiedCreditSearchResult.getMatchedAccount().getAccountData()==null)?null:aWlsUnifiedCreditSearchResult.getMatchedAccount().getAccountData().getBrandId());
        	WlsCreditBureauResult aWlsCreditBureauResult = aWlsUnifiedCreditSearchResult.getMatchedAccount().getCreditBureauResult();
        	ucSearchResultTrn.put("BUREAU_REPORT_SOURCE_CD", (aWlsCreditBureauResult==null)?null:aWlsCreditBureauResult.getReportSourceCd());
        	ucSearchResultTrn.put("BUREAU_ERROR_CD", (aWlsCreditBureauResult==null)?null:aWlsCreditBureauResult.getErrorCd());
        	if(aWlsCreditBureauResult.getAdjudicationResult() != null && aWlsCreditBureauResult.getAdjudicationResult().getCreditDecision() != null) {
        		ucSearchResultTrn.put("BUREAU_DECISION_CD", aWlsCreditBureauResult.getAdjudicationResult().getCreditDecision().getCreditDecisionCd());
        	} else {
        		ucSearchResultTrn.put("BUREAU_DECISION_CD", null);
        	}
        	ucSearchResultTrn.put("BUREAU_CREATION_DT", (aWlsCreditBureauResult==null)?null:aWlsCreditBureauResult.getCreationDate());
        }
        
        Long dbUC_SEARCH_RESULT_ID = (Long) insert(CreditAssessmentDaoConstants.SAVE_UC_SEARCH_RESULT, (HashMap<String, Object>)ucSearchResultTrn);
        
		//store UC_DATA_INQUIRY_ERROR
		if(aWlsUnifiedCreditSearchResult.getDataInquiryErrorCodeList() != null) {
			List<String> aDataInquiryErrorCode = aWlsUnifiedCreditSearchResult.getDataInquiryErrorCodeList();
			for(String errorCode: aDataInquiryErrorCode) {
				if(errorCode!=null && errorCode.length()!=0) {
					storeUC_DATA_INQUIRY_ERROR(errorCode, userId, dbUC_SEARCH_RESULT_ID);
				}
			}
		}
		//store UC_WLS_MATCH_ACCOUNT
        if(aWlsUnifiedCreditSearchResult.getMatchedAccount()!=null) {
        	storeUC_WLS_MATCH_ACCOUNT(aWlsUnifiedCreditSearchResult.getMatchedAccount(), userId, dbUC_SEARCH_RESULT_ID);
        }
	}
	private void storeUC_DATA_INQUIRY_ERROR(String errorCode, String userId, Long dbUC_SEARCH_RESULT_ID) throws Throwable {
		Map<String, Object> errorCodeTrn = new HashMap<String, Object>();
		errorCodeTrn.put("userId", userId);
		errorCodeTrn.put("DATA_INQUIRY_ERROR_CD", errorCode);
		errorCodeTrn.put("UC_SEARCH_RESULT_ID", dbUC_SEARCH_RESULT_ID);
		
		Long dbUC_DATA_INQUIRY_ERROR = (Long) insert(CreditAssessmentDaoConstants.SAVE_UC_DATA_INQUIRY_ERROR, (HashMap<String, Object>)errorCodeTrn);
		
	}
	private void storeUC_WLS_MATCH_ACCOUNT(WlsMatchedAccount matchedAccount, String userId, Long dbUC_SEARCH_RESULT_ID) throws Throwable {
		Map<String, Object> ucWlsMatchAccountTrn = new HashMap<String, Object>();
		ucWlsMatchAccountTrn.put("userId", userId);
		 ucWlsMatchAccountTrn.put("UC_SEARCH_RESULT_ID", dbUC_SEARCH_RESULT_ID);
		 ucWlsMatchAccountTrn.put("ACCOUNT_ID", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getBillingAccountNumber());
		 ucWlsMatchAccountTrn.put("ACCOUNT_TYPE", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getAccountType());
		 ucWlsMatchAccountTrn.put("ACCOUNT_SUB_TYP", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getAccountSubType());
		 ucWlsMatchAccountTrn.put("ACCOUNT_CREATION_DT", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getAccountCreationDate());
		 ucWlsMatchAccountTrn.put("START_SERVICE_DT", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getStartServiceDate());
		 ucWlsMatchAccountTrn.put("ACCOUNT_STATUS", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getAccountStatus());
		 ucWlsMatchAccountTrn.put("ACCOUNT_STATUS_DT", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getAccountStatusDate());
		 ucWlsMatchAccountTrn.put("STATUS_ACTV_CD", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getStatusActivityCode());
		 ucWlsMatchAccountTrn.put("STATUS_ACTV_RSN_CD", (matchedAccount.getAccountData()==null)?null:matchedAccount.getAccountData().getStatusActivityReasonCode());
		 ucWlsMatchAccountTrn.put("RISK_LEVEL_NUM", (matchedAccount.getCreditWorthinessData()==null)?null:matchedAccount.getCreditWorthinessData().getRiskLevelNumber());
		 ucWlsMatchAccountTrn.put("RISK_LEVEL_DECISION_CD", (matchedAccount.getCreditWorthinessData()==null)?null:matchedAccount.getCreditWorthinessData().getRiskLevelDecisionCd());
		 ucWlsMatchAccountTrn.put("CREDIT_CLASS_CD", (matchedAccount.getCreditWorthinessData()==null)?null:matchedAccount.getCreditWorthinessData().getCreditClassCd());
		 WlsAccountFinancialHistory aWlsAccountFinancialHistory = matchedAccount.getAccountFinancialHistory();
		 ucWlsMatchAccountTrn.put("DELINQUENT_IND", (aWlsAccountFinancialHistory==null)?null:(aWlsAccountFinancialHistory.isDelinquentInd()==null)?null:(aWlsAccountFinancialHistory.isDelinquentInd())?"Y":"N");
		 ucWlsMatchAccountTrn.put("TOTAL_ACTIVE_SUB", (matchedAccount.getTotalSubscribers()==null)?null:matchedAccount.getTotalSubscribers().getTotalActiveSubscribersNumber());
		 ucWlsMatchAccountTrn.put("TOTAL_RESERVED_SUB", (matchedAccount.getTotalSubscribers()==null)?null:matchedAccount.getTotalSubscribers().getTotalReservedSubscribersNumber());
		 ucWlsMatchAccountTrn.put("TOTAL_SUSPEND_SUB", (matchedAccount.getTotalSubscribers()==null)?null:matchedAccount.getTotalSubscribers().getTotalSuspendedSubscribersNumber());
        Long dbUC_WLS_MATCH_ACCOUNT_ID = (Long) insert(CreditAssessmentDaoConstants.SAVE_UC_WLS_MATCH_ACCOUNT, (HashMap<String, Object>)ucWlsMatchAccountTrn);
        
		//store UC_WARNING_HIST
        if(matchedAccount.getWarningHistoryList() != null && matchedAccount.getWarningHistoryList().size() != 0) {
        	List<WlsWarningHistory> aWarningHistoryList = matchedAccount.getWarningHistoryList();
        	for(WlsWarningHistory aWlsWarningHistory: aWarningHistoryList) {
        		if((aWlsWarningHistory.getWarningCd()!=null && !aWlsWarningHistory.getWarningCd().isEmpty()) ||
        				(aWlsWarningHistory.getWarningCategoryCd()!=null && !aWlsWarningHistory.getWarningCategoryCd().isEmpty()) ||
        				(aWlsWarningHistory.getWarningTypeCd()!=null && !aWlsWarningHistory.getWarningTypeCd().isEmpty()) ){
                	storeUC_WARNING_HIST(aWlsWarningHistory, userId, dbUC_WLS_MATCH_ACCOUNT_ID);        		        			
        		}
        	}
        }
	}

	private void storeUC_WARNING_HIST(WlsWarningHistory aWlsWarningHistory, String userId, Long dbUC_WLS_MATCH_ACCOUNT_ID) throws Throwable {
		Map<String, Object> wlsWarningHistoryTrn = new HashMap<String, Object>();
		wlsWarningHistoryTrn.put("userId", userId);
		wlsWarningHistoryTrn.put("UC_WLS_MATCH_ACCOUNT_ID", dbUC_WLS_MATCH_ACCOUNT_ID);
		wlsWarningHistoryTrn.put("WARNING_CATEGORY_CD", aWlsWarningHistory.getWarningCategoryCd());
		wlsWarningHistoryTrn.put("WARNING_CD", aWlsWarningHistory.getWarningCd());
		wlsWarningHistoryTrn.put("WARNING_TYPE_CD", aWlsWarningHistory.getWarningTypeCd());
		wlsWarningHistoryTrn.put("WARNING_STATUS_CD", aWlsWarningHistory.getWarningStatusCd());
		Long dbUC_WARNING_HIST_ID = (Long) insert(CreditAssessmentDaoConstants.SAVE_UC_WARNING_HIST, (HashMap<String, Object>)wlsWarningHistoryTrn);
	}


    /*  @Autowired
        private DozerBeanMapper mapper;
        private Map<String, Object> test(FullCreditAssessmentRequest aFullCreditAssessmentRequest,
                AuditInfo auditInfo,
                long aCAR_ID,
                boolean failOverHandler) {
           Map<String, Object> stgCreditDecisionTrn = new HashMap<String, Object>();

            mapper.map(auditInfo,stgCreditDecisionTrn);
            mapper.map(aCAR_ID,stgCreditDecisionTrn);
            mapper.map(failOverHandler,stgCreditDecisionTrn);

            mapper.map(aFullCreditAssessmentRequest,stgCreditDecisionTrn);
            //TODO exclude  object
            mapper.map(aFullCreditAssessmentRequest.getCreditCustomerInfo(),stgCreditDecisionTrn);
          //TODO custom mapping for   EMPLOYEE_IND
           // stgCreditDecisionTrn.put( "EMPLOYEE_IND", CrdaUtility.getBooleanStringValue( creditCustomerInfo.isEmployeeInd() ) );
            if (aFullCreditAssessmentRequest.getCreditCustomerInfo().getPersonName() != null){
                mapper.map(aFullCreditAssessmentRequest.getCreditCustomerInfo().getPersonName(),stgCreditDecisionTrn);
                if (aFullCreditAssessmentRequest.getCreditCustomerInfo().getTelecomContactList() != null){
                    mapper.map(aFullCreditAssessmentRequest.getCreditCustomerInfo().getTelecomContactList(),stgCreditDecisionTrn);
                }
            }

          //Map ConsumerCreditProfile
            mapper.map(aFullCreditAssessmentRequest.getCreditProfileData(),stgCreditDecisionTrn);
            if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification()!= null) {
                mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification(),stgCreditDecisionTrn);
                if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getDriverLicense() != null ){
                    mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getDriverLicense(),stgCreditDecisionTrn);
                }
                if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getProvincialIdCard() != null ){
                    mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getProvincialIdCard(),stgCreditDecisionTrn);
                }
                if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getHealthCard() != null ){
                    mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getHealthCard(),stgCreditDecisionTrn);
                }
                if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getPassport() != null ){
                    mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditIdentification().getPassport(),stgCreditDecisionTrn);
                }
             }

            if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditAddress() != null ){
                mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditAddress(),stgCreditDecisionTrn);
            }
            if ( aFullCreditAssessmentRequest.getCreditProfileData().getCreditCardCd() != null){
                mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getCreditCardCd(),stgCreditDecisionTrn);

            }
            if ( aFullCreditAssessmentRequest.getCreditProfileData().getPersonalInfo()!= null){
                mapper.map(aFullCreditAssessmentRequest.getCreditProfileData().getPersonalInfo(),stgCreditDecisionTrn);

            }
            //**********************
            return stgCreditDecisionTrn;

        }
    */
    //adds fullassment request specific attributes
    protected HashMap<String, Object> populateCAR(CreditAssessmentRequest aCreditAssessmentRequest, AuditInfo auditInfo) {

        HashMap<String, Object> carData = (HashMap<String, Object>) super.populateCAR(aCreditAssessmentRequest, auditInfo);
        FullCreditAssessmentRequest creditAssessmentRequest = (FullCreditAssessmentRequest) aCreditAssessmentRequest;
        carData.put("customerTypeCode", creditAssessmentRequest.getCreditCustomerInfo().getCustomerTypeCd());
        carData.put("customerId", creditAssessmentRequest.getCreditCustomerInfo().getCustomerID());
        return carData;
    }

}