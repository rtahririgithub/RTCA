/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.crda.dao.mgmt;

import com.telus.credit.crda.dao.CreditAssessmentDaoConstants;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.rules.InternalRules;

import com.telus.credit.domain.common.AdjudicationResult;
import com.telus.credit.domain.common.BureauInformation;
import com.telus.credit.domain.common.CreditAddress;
import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.common.CreditBureauDocument;
import com.telus.credit.domain.common.CreditBureauResult;
import com.telus.credit.domain.common.CreditCardCode;
import com.telus.credit.domain.common.CreditCustomerInfo;
import com.telus.credit.domain.common.CreditDecision;
import com.telus.credit.domain.common.CreditIdentification;
import com.telus.credit.domain.common.DriverLicense;
import com.telus.credit.domain.common.FraudWarning;
import com.telus.credit.domain.common.HealthCard;
import com.telus.credit.domain.common.Passport;
import com.telus.credit.domain.common.PersonName;
import com.telus.credit.domain.common.PersonalInfo;
import com.telus.credit.domain.common.ProductCategory;
import com.telus.credit.domain.common.ProductCategoryQualification;
import com.telus.credit.domain.common.ProvincialIdCard;
import com.telus.credit.domain.common.RiskIndicator;
import com.telus.credit.domain.common.ScoreCardAttribute;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.CreditDecisioningExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.CreditDecisioningFullCreditAssessmentRequest;
import com.telus.credit.domain.crda.CreditDecisioningIOverrideCreditAssessmentRequest;
import com.telus.credit.domain.crda.FullCreditAssessmentRequest;
import com.telus.credit.domain.crda.OverrideCreditAssessmentBaseRequest;

import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.OverrideCreditAssessmentRequest;
import com.telus.framework.crypto.EncryptionUtil;


import com.telus.credit.domain.collection.CustomerCollectionData;
import com.telus.credit.domain.creditprofile.ConsumerCreditProfile;
import com.telus.credit.domain.creditprofile.CreditWorthiness;
import com.telus.credit.domain.deposit.SummarizedDepositData;

import java.math.BigDecimal;
import java.util.*;

public class GetCreditAssessmentHelper {
    private static GetCreditAssessmentHelper m_getCreditAssessmentHelper;

    private GetCreditAssessmentHelper() {
    }

    public static GetCreditAssessmentHelper instanceOf() {
        if (m_getCreditAssessmentHelper == null) {
            m_getCreditAssessmentHelper = new GetCreditAssessmentHelper();
        }
        return m_getCreditAssessmentHelper;
    }

    public void populateBaseCreditAssessmentDetails(
            HashMap<String, Object> baseCreditAssessmentMap,
            CreditAssessmentTransaction creditAssessmentDetails) {
        if (baseCreditAssessmentMap != null) {
            printHashMap(baseCreditAssessmentMap);
            creditAssessmentDetails.setChannelID((String) baseCreditAssessmentMap.get("channelOrgCd"));
            creditAssessmentDetails.setCommentTxt((String) baseCreditAssessmentMap.get("commentTxt"));

            creditAssessmentDetails.setCreditAssessmentDate((Date) baseCreditAssessmentMap.get("createTs"));
            creditAssessmentDetails.setCreditAssessmentID((Long) baseCreditAssessmentMap.get("carId"));
            creditAssessmentDetails.setCreditAssessmentStatus((String) baseCreditAssessmentMap.get("carStatusTypeCd"));
            creditAssessmentDetails.setCreditAssessmentStatusDate((Date) baseCreditAssessmentMap.get("carStatusEffectiveDate"));
            creditAssessmentDetails.setCreditAssessmentStatusReasonCd((String) baseCreditAssessmentMap.get("carActivityReasonCd"));
            creditAssessmentDetails.setCreditAssessmentSubTypeCd((String) baseCreditAssessmentMap.get("carSubtypeCd"));
            creditAssessmentDetails.setCreditAssessmentTypeCd((String) baseCreditAssessmentMap.get("carTypCd"));
            creditAssessmentDetails.setCustomerID((Long) baseCreditAssessmentMap.get("customerId"));
            creditAssessmentDetails.setUserID((String) baseCreditAssessmentMap.get("createUserId"));
             
        }

    }

    private void printHashMap(Map<String, Object> stgCreditDecisionTrn) {
        /*Iterator it = stgCreditDecisionTrn.entrySet().iterator();
        while ( it.hasNext() ) {
            Map.Entry entry = (Map.Entry)it.next();
            System.out.println( "Key: " + entry.getKey()
                        + " Value class: " 
                        + (entry.getValue() != null ? entry.getValue().getClass().getName() : "no class" )
                        + ", value: " + entry.getValue() );
        }*/
    }

    public void populateCreditAssessmentDetails(
            HashMap<String, Object> baseCreditAssessmentMap,
            HashMap<String, Object> completeCreditAssessmentMap,
            List<HashMap<String, Object>> creditDecisionResultDtlList,
            List<HashMap<String, Object>> creditBureauTrnDtlList,
            CreditAssessmentDetails completedCreditAssessmentDetails) throws EnterpriseCreditAssessmentServiceException {
        if (completeCreditAssessmentMap != null) {
             //************** populate  CreditDecisioningInput including CreditCustomerInfo, CreditProfileData CollectionData, deposit data in completeCreditAssessmentRequest **************
            populateCreditDecisioningInput(
            		baseCreditAssessmentMap,
					completeCreditAssessmentMap,
					completedCreditAssessmentDetails);
  
          //************** populate CreditDecisioningResult in CompletedCreditAssessmentDetails**************
            populateCreditDecisioningResult(completeCreditAssessmentMap, 
            		creditDecisionResultDtlList, 
            		completedCreditAssessmentDetails);

          //************** populate CreditBureauDataResult and creditBureauDataResultDocumentList in CompletedCreditAssessmentDetails **************
            populate_CreditBureauDataResult_CreditBureauDataResultDocumentList_creditAssessmentDetails_Common_CreditBureauData(
					baseCreditAssessmentMap, 
					completeCreditAssessmentMap,
					creditBureauTrnDtlList,
					completedCreditAssessmentDetails);
            
            //TODO find out what these old todos are for 
            /**
             * TODO: What to do with simulator or fraud indicator
             *
             <result property="creditBureauSimFoInd" column="CREDIT_BUREAU_SIM_FO_IND"/>
             **/
            /**
             * TODO: No Mapping for credit value code and fraud indicator code for full assessment
             * <result property="creditValueCd" column="CREDIT_VALUE_CD"/>
             <result property="fraudIndCd" column="FRAUD_IND_CD"/>
             */
        }

    }

    
    
	//populate  completedCreditAssessmentDetails.CreditDecisioningInput with common data, CreditCustomerInfo, CreditProfileData , CollectionData, deposit data
    private void populateCreditDecisioningInput(
			HashMap<String, Object> baseCreditAssessmentMap,
			HashMap<String, Object> fullCreditAssessmentMap,
			CreditAssessmentDetails completedCreditAssessmentDetails) throws EnterpriseCreditAssessmentServiceException {
    	CreditAssessmentRequest completeCreditAssessmentRequest;
    	
    	
    	if (!EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_AUTO_ASMT.equalsIgnoreCase(completedCreditAssessmentDetails.getCreditAssessmentSubTypeCd())
    			&&
    			!EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_GET_BUREAU_DATA.equalsIgnoreCase(completedCreditAssessmentDetails.getCreditAssessmentSubTypeCd())
    			){   		
    		completeCreditAssessmentRequest = new CreditDecisioningExistingCustomerCreditAssessmentRequest();
    	}else{
    		completeCreditAssessmentRequest = new CreditDecisioningFullCreditAssessmentRequest();
    	}
  
    	
        populateCreditAssessmentRequest(completeCreditAssessmentRequest, baseCreditAssessmentMap, completedCreditAssessmentDetails);
      //CreditDecisioningFullCreditAssessmentRequest (parent class)
		populateCreditCustomer(baseCreditAssessmentMap,
				fullCreditAssessmentMap,
				(FullCreditAssessmentRequest)completeCreditAssessmentRequest);
		populateCreditProfileData(baseCreditAssessmentMap,
				fullCreditAssessmentMap,
				(FullCreditAssessmentRequest)completeCreditAssessmentRequest);


       	if (!EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_AUTO_ASMT.equalsIgnoreCase(completedCreditAssessmentDetails.getCreditAssessmentSubTypeCd())
    			&&
    			!EnterpriseCreditAssessmentConsts.ASMT_SUB_TYPE_GET_BUREAU_DATA.equalsIgnoreCase(completedCreditAssessmentDetails.getCreditAssessmentSubTypeCd())
       		){  	
				populateCustomerCollectionData(
						fullCreditAssessmentMap, (CreditDecisioningExistingCustomerCreditAssessmentRequest)completeCreditAssessmentRequest);   		
				populateDepositData(
						fullCreditAssessmentMap, (CreditDecisioningExistingCustomerCreditAssessmentRequest)completeCreditAssessmentRequest);
		        //TODO populate existing bureauResultData used as input to Fico
				//....................
	   	}
		completedCreditAssessmentDetails.setCreditDecisioningInput(completeCreditAssessmentRequest);
	}

	private void populateDepositData(
			HashMap<String, Object> fullCreditAssessmentMap,
			CreditDecisioningExistingCustomerCreditAssessmentRequest completeCreditAssessmentRequest) throws EnterpriseCreditAssessmentServiceException {
		SummarizedDepositData aSummarizedDepositData = new SummarizedDepositData(); 
        
		BigDecimal depositPaidDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("totalArDepositPaidAmt"),"totalArDepositPaidAmt");
		if (depositPaidDec != null) {
			aSummarizedDepositData.setDepositPaid(depositPaidDec);
		}
		//aSummarizedDepositData.setDepositPaid(BigDecimal.valueOf((Double) fullCreditAssessmentMap.get("totalArDepositPaidAmt")));
		
		BigDecimal depositPendingDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("totalArDepositPendingAmt"),"totalArDepositPendingAmt");
		if (depositPendingDec != null) {
			aSummarizedDepositData.setDepositPending(depositPendingDec);
		}		
		//aSummarizedDepositData.setDepositPending(BigDecimal.valueOf((Double) fullCreditAssessmentMap.get("totalArDepositPendingAmt")));
		BigDecimal depositReleasedDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("totalDepositReleasedAmt"),"totalDepositReleasedAmt");
		if (depositReleasedDec != null) {
			aSummarizedDepositData.setDepositReleased(depositReleasedDec);
		}
		//aSummarizedDepositData.setDepositReleased(BigDecimal.valueOf((Double) fullCreditAssessmentMap.get("totalDepositReleasedAmt")));
		
		
		aSummarizedDepositData.setMostRecentDepositPaidDate((Date)fullCreditAssessmentMap.get("lastArDepositPaidTs"));
		aSummarizedDepositData.setMostRecentDepositPendingDate((Date)fullCreditAssessmentMap.get("lastArDepositPendingTs"));
		aSummarizedDepositData.setMostRecentDepositReleaseDate((Date)fullCreditAssessmentMap.get("lastArDepositReleasedTs"));
		completeCreditAssessmentRequest.setDepositData(aSummarizedDepositData);
	}

	private void populateCustomerCollectionData(
			HashMap<String, Object> fullCreditAssessmentMap,
			CreditDecisioningExistingCustomerCreditAssessmentRequest completeCreditAssessmentRequest) throws EnterpriseCreditAssessmentServiceException {
		CustomerCollectionData aCollectionData = new CustomerCollectionData();
		aCollectionData.setInvoluntaryCancelledAccounts((Integer) fullCreditAssessmentMap.get("involuntaryCancelledAccounts"));
	    
		
		BigDecimal InvoluntaryCancelledAccountsBalanceBigDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("involuntaryCancelledAccountsBalance"),"involuntaryCancelledAccountsBalance");
		if (InvoluntaryCancelledAccountsBalanceBigDec != null) {
			aCollectionData.setInvoluntaryCancelledAccountsBalance(InvoluntaryCancelledAccountsBalanceBigDec);
		}
		
/*	    if (null != (Double) fullCreditAssessmentMap.get("involuntaryCancelledAccountsBalance")) {
	    	aCollectionData.setInvoluntaryCancelledAccountsBalance(BigDecimal.valueOf((Double) fullCreditAssessmentMap.get("involuntaryCancelledAccountsBalance")));
	    }*/		
		
		
		aCollectionData.setLatestInvoluntaryCancelledDate((Date) fullCreditAssessmentMap.get("latestInvoluntaryCancelledDate")); 
		                
		aCollectionData.setNumberOfAccountsInAgency((Integer) fullCreditAssessmentMap.get("numberOfAccountsInAgency"));
		
		
		
		BigDecimal AccountsInAgencyBalanceBigDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("accountsInAgencyBalance"),"accountsInAgencyBalance");
		if (AccountsInAgencyBalanceBigDec != null) {
			aCollectionData.setAccountsInAgencyBalance(AccountsInAgencyBalanceBigDec);
		}
/*	    if (null != (Double) fullCreditAssessmentMap.get("accountsInAgencyBalance")) {
	    	aCollectionData.setAccountsInAgencyBalance(BigDecimal.valueOf((Double) fullCreditAssessmentMap.get("accountsInAgencyBalance")));
	    }*/		

		aCollectionData.setLatestAgencyAssignmentDate((Date) fullCreditAssessmentMap.get("latestAgencyAssignmentDate"));
		        
		aCollectionData.setCollectionScore((String) fullCreditAssessmentMap.get("collectionScore"));                
		aCollectionData.setLatestCollectionEndDate((Date) fullCreditAssessmentMap.get("latestCollectionEndDate"));
		aCollectionData.setLatestCollectionStartDate((Date) fullCreditAssessmentMap.get("latestCollectionStartDate"));
		
		aCollectionData.setNumberOfNSFCheques((Integer) fullCreditAssessmentMap.get("numberOfNSFCheques"));
		aCollectionData.setActiveAccountsCollectionInd(CrdaUtility.getBooleanValue((String) fullCreditAssessmentMap.get("collectionInd")));
		completeCreditAssessmentRequest.setCustomerCollectionData(aCollectionData);
	}

	private void populate_CreditBureauDataResult_CreditBureauDataResultDocumentList_creditAssessmentDetails_Common_CreditBureauData(
			HashMap<String, Object> baseCreditAssessmentMap,
			HashMap<String, Object> fullCreditAssessmentMap,
			List<HashMap<String, Object>> creditBureauTrnDtlList,
			CreditAssessmentDetails returnedCompletedCreditAssessmentDetails) throws EnterpriseCreditAssessmentServiceException {
		Long creditBureauTrnId = (Long) fullCreditAssessmentMap.get("creditBureauTrnId");
		if (creditBureauTrnId != null && creditBureauTrnId > 0) {
 
			//set credit bureau data attributes in creditAssessmentDetails
		    populate_creditAssessmentDetails_Common_CreditBureauData(
		            fullCreditAssessmentMap, 
		            returnedCompletedCreditAssessmentDetails);
		    
		    
		  //set creditBureauDataResult in creditAssessmentDetails
		    CreditBureauResult creditBureauResult = new CreditBureauResult();
		    
		   // set AdjudicationResult on creditBureauDataResult
		    populate_AdjudicationResult(fullCreditAssessmentMap,creditBureauResult);

		    //set riskIndicator, ScoreCardAttribute FraudWarning
		    populate_RiskIndicator_ScoreCardAttribute_FraudWarning(
					fullCreditAssessmentMap, creditBureauTrnDtlList,
					returnedCompletedCreditAssessmentDetails,
					creditBureauTrnId, creditBureauResult);
		}
	}

	private void populate_RiskIndicator_ScoreCardAttribute_FraudWarning(
			HashMap<String, Object> fullCreditAssessmentMap,
			List<HashMap<String, Object>> creditBureauTrnDtlList,
			CreditAssessmentDetails returnedCompletedCreditAssessmentDetails,
			Long creditBureauTrnId, CreditBureauResult creditBureauResult) {
		//cgw does not populate BureauReportStatusCd. It is maintaned by eCrda. For a successfull  ecrda stores 1 in CREDIT_BUREAU_TRN_RSLT_STAT_CD . void operation updates this value to 2
		creditBureauResult.setBureauReportStatusCd((String) fullCreditAssessmentMap.get("creditBureauTrnRsltStatCd"));
		
		
		creditBureauResult.setBureauReportStatusDate((Date) fullCreditAssessmentMap.get("crBruTrnRsltStatUpdtDt"));
		creditBureauResult.setCreationDate((Date) fullCreditAssessmentMap.get("rsltCreationDt"));
		creditBureauResult.setErrorCd((String) fullCreditAssessmentMap.get("trnErrorCd"));
	   	//CGW does not populate CreditBureauDataResult.Id . ecrda sets ID to  CREDIT_BUREAU_TRN_ID		 
		creditBureauResult.setId(creditBureauTrnId);
		PersonName creditBureauPersonName = new PersonName();
		creditBureauPersonName.setFirstName((String) fullCreditAssessmentMap.get("firstNm"));
		creditBureauPersonName.setMiddleName((String) fullCreditAssessmentMap.get("middleNm"));
		creditBureauPersonName.setLastName((String) fullCreditAssessmentMap.get("lastNm"));
		if (creditBureauPersonName.getFirstName() != null
		        || creditBureauPersonName.getMiddleName() != null
		        || creditBureauPersonName.getLastName() != null) {
		    creditBureauResult.setPersonName(creditBureauPersonName);
		}
		// creditBureauResult.setReportSourceCd(returnedCompletedCreditAssessmentDetails.getCreditBureauReportSourceCd());
		creditBureauResult.setReportSourceCd((String) fullCreditAssessmentMap.get("reportSourceCd"));
		creditBureauResult.setReportType((String) fullCreditAssessmentMap.get("reportTypTxt"));
		
		RiskIndicator riskIndicator = new RiskIndicator();
		boolean riskPresent = false;
		List<FraudWarning> fraudList = new ArrayList<FraudWarning>();
		boolean fraudPresent = false;
		List<ScoreCardAttribute> scoreCardAttributeList = new ArrayList<ScoreCardAttribute>();
		boolean scoreCardPresent = false;
		returnedCompletedCreditAssessmentDetails.setCreditBureauReportInd(false);
		for (HashMap<String, Object> creditBureauTrnDtl : creditBureauTrnDtlList) {
		    String creditBureauTrnDtlType = (String) creditBureauTrnDtl.get("creditBureauTrnDtlCd");
		    
		    if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_RISK_TYPE)) {
		        riskPresent = true;
		        String riskIndicatorType = (String) creditBureauTrnDtl.get("riskIndTypCd");
		        String riskIndicatorValue = (String) creditBureauTrnDtl.get("riskIndValueTxt");
		        if (riskIndicatorType.equals(CreditAssessmentDaoConstants.NO_HIT_THIN_FILE_CD)) {
		            riskIndicator.setNoHitThinFileInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
		        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.TRUE_THIN_FILE_CD)) {
		            riskIndicator.setTrueThinFileInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
		        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.HIGH_RISK_THIN_FILE_CD)) {
		            riskIndicator.setHighRiskThinFileInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
		        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.TEMP_SIN_CD)) {
		            riskIndicator.setTempSINInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
		        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.UNDER_AGE_CD)) {
		            riskIndicator.setUnderAgeInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
		        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.PRIMARY_RISK_CD)) {
		            riskIndicator.setPrimaryRiskInd(riskIndicatorValue);
		        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.SECONDARY_RISK_CD)) {
		            riskIndicator.setSecondaryRiskInd(riskIndicatorValue);
		        }
		    } else if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_FRAUD_TYPE)) {
		        fraudPresent = true;
		        FraudWarning fraudWarning = new FraudWarning();
		        fraudWarning.setFraudType((String) creditBureauTrnDtl.get("fraudTypCd"));
		        fraudWarning.setFraudMessage((String) creditBureauTrnDtl.get("fraudMsgTxt"));
		        fraudWarning.setFraudCd((String) creditBureauTrnDtl.get("fraudCd"));
		        fraudList.add(fraudWarning);
		    } else if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_SCORE_TYPE)) {
		        scoreCardPresent = true;
		        ScoreCardAttribute scoreCardAttribute = new ScoreCardAttribute();
		        scoreCardAttribute.setScoreName((String) creditBureauTrnDtl.get("scoreNm"));
		        scoreCardAttribute.setScoreValue((String) creditBureauTrnDtl.get("scoreValueTxt"));
		        scoreCardAttributeList.add(scoreCardAttribute);
		    } else if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_DOC_TYPE)) {
		        String docType = (String) creditBureauTrnDtl.get("extDocFormatTyp");
	            boolean aCreditBureauReportInd = 
            		InternalRules.getCreditBureauReportInd(
            				(String) creditBureauTrnDtl.get("extDocPathStr"), 
            				returnedCompletedCreditAssessmentDetails.getCreditBureauReportSourceCd() );     
	            
	            returnedCompletedCreditAssessmentDetails.setCreditBureauReportInd(aCreditBureauReportInd);    		        
		        if (docType != null && docType.toUpperCase().equals(CreditAssessmentDaoConstants.REPORT_DOC_TYPE_TXT)) {
		            CreditBureauDocument creditBureauDocument = new CreditBureauDocument();
		            creditBureauDocument.setDocumentID(String.valueOf((Long) creditBureauTrnDtl.get("creditBureauTrnDtlId")));
		            creditBureauDocument.setDocumentPath((String) creditBureauTrnDtl.get("extDocPathStr"));
		            creditBureauDocument.setDocumentType(docType.toLowerCase());
		            returnedCompletedCreditAssessmentDetails.getCreditBureauDataResultDocumentList().add(creditBureauDocument);

                         
		        }
		    }
		} 
		if (riskPresent) {
		    creditBureauResult.setRiskIndicator(riskIndicator);
		}
		if (fraudPresent) {
		    creditBureauResult.getFraudWarningList().addAll(fraudList);
		}
		if (scoreCardPresent) {
		    if (creditBureauResult.getAdjudicationResult() == null) {
		        creditBureauResult.setAdjudicationResult(new AdjudicationResult());
		    }
		    creditBureauResult.getAdjudicationResult().getScoreCardAttributeList().addAll(scoreCardAttributeList);
		}
		returnedCompletedCreditAssessmentDetails.setCreditBureauDataResult(creditBureauResult);
	}

	private void populate_AdjudicationResult(
			HashMap<String, Object> fullCreditAssessmentMap,
			CreditBureauResult creditBureauResult) throws EnterpriseCreditAssessmentServiceException {
		AdjudicationResult adjResult = new AdjudicationResult();
		//adjResult.setCreditClass(returnedCompletedCreditAssessmentDetails.getCreditClass());
		adjResult.setCreditClass((String) fullCreditAssessmentMap.get("adjdctnCreditClassCd"));
		
		  CreditDecision creditDecision = new CreditDecision();
		    creditDecision.setCreditDecisionCd((String) fullCreditAssessmentMap.get("adjdctnDscnCd"));
		    creditDecision.setCreditDecisionMessage((String) fullCreditAssessmentMap.get("adjdctnDscnMsgTxt"));	
		//adjResult.setCreditDecision(returnedCompletedCreditAssessmentDetails.getCreditDecisionCd());
		adjResult.setCreditDecision(creditDecision);
   
		
		BigDecimal CreditLimitAmtBigDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("adjdctnCreditLimitAmt"),"adjdctnCreditLimitAmt");
		if (CreditLimitAmtBigDec != null) {
			adjResult.setCreditLimitAmt(CreditLimitAmtBigDec);
		}
/*		if (fullCreditAssessmentMap.get("adjdctnCreditLimitAmt") != null) {
			Double creditLimit = (Double) fullCreditAssessmentMap.get("adjdctnCreditLimitAmt");
			if (creditLimit != null) {
			    adjResult.setCreditLimitAmt(BigDecimal.valueOf(creditLimit));
			}
		}*/
		//adjResult.setCreditScoreCd(returnedCompletedCreditAssessmentDetails.getCreditScoreCd());
		//adjResult.setCreditScoreTypeCd(returnedCompletedCreditAssessmentDetails.getCreditScoreTypeCd());
		adjResult.setCreditScoreCd((String) fullCreditAssessmentMap.get("adjdctnCreditScoreTxt"));
		adjResult.setCreditScoreTypeCd((String) fullCreditAssessmentMap.get("adjdctnCreditScoreTypeCd"));
		
		
		BigDecimal DepositAmtBigDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("adjdctnDepositAmt"),"adjdctnDepositAmt");
		if (DepositAmtBigDec != null) {
			adjResult.setDepositAmt(DepositAmtBigDec);
		} 
/*		BigDecimal depositAmt = (BigDecimal) fullCreditAssessmentMap.get("adjdctnDepositAmt");
 		if (depositAmt != null) {
			//adjResult.setDepositAmt(BigDecimal.valueOf(depositAmt));
			adjResult.setDepositAmt(depositAmt);
		} */   
		if (adjResult.getCreditClass() != null
		        || adjResult.getCreditDecision() != null
		        || adjResult.getCreditLimitAmt() != null
		        || adjResult.getCreditScoreCd() != null
		        || adjResult.getDepositAmt() != null) {
		    creditBureauResult.setAdjudicationResult(adjResult);
		}
	}

	private void populateCreditProfileData(
			HashMap<String, Object> baseCreditAssessmentMap,
			HashMap<String, Object> completeCreditAssessmentMap,
			FullCreditAssessmentRequest returnedCompleteCreditAssessmentRequest) {
		ConsumerCreditProfile aConsumerCreditProfile = new ConsumerCreditProfile();

		
/*	 
 		follwoing not populated as they are not store in stg table 
		 CommentTxt 
		 CustomerGuarantor 
		 FormatCd 
		*/
		
		aConsumerCreditProfile.setCustomerID((Long) completeCreditAssessmentMap.get("customerId"));
		aConsumerCreditProfile.setApplicationProvinceCd((String) completeCreditAssessmentMap.get("applicationSubProvCd"));
		aConsumerCreditProfile.setBusinessLastUpdateTimestamp((Date) completeCreditAssessmentMap.get("businessLastUpdtTs"));
		
		aConsumerCreditProfile.setBypassMatchIndicator(CrdaUtility.getBooleanValue((String)completeCreditAssessmentMap.get("bypassMatchInd")));
		Long creditProfileId = (Long) completeCreditAssessmentMap.get("creditProfileId");
		if (creditProfileId != null && creditProfileId.longValue() > 0) {
		    aConsumerCreditProfile.setCreditProfileID(creditProfileId);
		}
		aConsumerCreditProfile.setCreditProfileStatusCd((String) completeCreditAssessmentMap.get("creditProfileStatusCd"));
		aConsumerCreditProfile.setMethodCd((String) completeCreditAssessmentMap.get("creditProfileMethodCd"));

		
		populatePersonalInfo(baseCreditAssessmentMap, completeCreditAssessmentMap,aConsumerCreditProfile);

		populateCreditAddress(completeCreditAssessmentMap, aConsumerCreditProfile);
		
		populateCreditCardCode(completeCreditAssessmentMap, aConsumerCreditProfile);

		populateCreditIdentification(completeCreditAssessmentMap,aConsumerCreditProfile);
		
		
		
		aConsumerCreditProfile.setCreditValueEffectiveDate((Date) completeCreditAssessmentMap.get("creditValueEffectiveDate"));
		aConsumerCreditProfile.setFirstCreditAssessmentDate((Date) completeCreditAssessmentMap.get("firstCreditAssessmentDate"));
		aConsumerCreditProfile.setLatestCreditAssessmentDate((Date) completeCreditAssessmentMap.get("latestCreditAssessmentDate"));
		
		CreditWorthiness aCreditWorthiness = new CreditWorthiness();
		aCreditWorthiness.setCreditValueCd((String) completeCreditAssessmentMap.get("stgCreditValueCd"));
		aCreditWorthiness.setCustomerID((Long) completeCreditAssessmentMap.get("customerId"));
		aCreditWorthiness.setDecisionCd((String) completeCreditAssessmentMap.get("stgdecisionCd"));
		aCreditWorthiness.setFraudIndicatorCd((String) completeCreditAssessmentMap.get("stgfraudIndicatorCd"));
		
		//follwoing not populated as they are not store in stg table 
		//AssessmentMessageCd 
		//CreditAssessmentCreationDate 
		//CreditAssessmentId 
		//CreditAssessmentSubTypeCd 
		//CreditAssessmentTypeCd 
		//FraudIndicatorCd 
		//FraudMessageCdList 
		//ProductCategoryQualification 
		//setReportIndicator 
		
		aConsumerCreditProfile.setCreditWorthiness(aCreditWorthiness);
		 
/*
stored in stg table
FIRST_CREDIT_ASSESMENT_DT
LAST_CREDIT_ASSESMENT_DT
DECISION_CD
CREDIT_VALUE_EFFECTIVE_DT
FRAUD_IND_CD
*/
		
/*		creditWorthiness  :STG_DECISION_CD
		reportIndicator 
		>creditValueEffectiveDate 
		>firstCreditAssessmentDate 
		>latestCreditAssessmentDate*/
		
		returnedCompleteCreditAssessmentRequest.setCreditProfileData((ConsumerCreditProfile) aConsumerCreditProfile);
	}

	private void populateCreditIdentification(
			HashMap<String, Object> fullCreditAssessmentMap,
			ConsumerCreditProfile aConsumerCreditProfile) {
		CreditIdentification creditIdentification = new CreditIdentification();
		String drivingLicense = (String) fullCreditAssessmentMap.get("driverLicenseStr");
		if (drivingLicense != null && drivingLicense.trim().length() > 0) {
		    DriverLicense driverLicense = new DriverLicense();
		    driverLicense.setDriverLicenseNum(EncryptionUtil.decrypt(drivingLicense));
		    driverLicense.setProvinceCd((String) fullCreditAssessmentMap.get("driverLicenseProvCd"));
		    creditIdentification.setDriverLicense(driverLicense);
		}
		String socialInsuranceNum = (String) fullCreditAssessmentMap.get("socialInsuranceNum");
		if (socialInsuranceNum != null && socialInsuranceNum.trim().length() > 0) {
		    creditIdentification.setSin(EncryptionUtil.decrypt(socialInsuranceNum));
		}
		String healthcareNum = (String) fullCreditAssessmentMap.get("healthcareNum");
		if (healthcareNum != null && healthcareNum.trim().length() > 0) {
		    HealthCard healthCard = new HealthCard();
		    healthCard.setHealthCardNum(EncryptionUtil.decrypt(healthcareNum));
		    healthCard.setProvinceCd((String) fullCreditAssessmentMap.get("healthcareProvCd"));
		}
		String passportNum = (String) fullCreditAssessmentMap.get("passportStr");
		if (passportNum != null && passportNum.trim().length() > 0) {
		    Passport passport = new Passport();
		    passport.setPassportNum(EncryptionUtil.decrypt(passportNum));
		    passport.setCountryCd((String) fullCreditAssessmentMap.get("passportCountryCd"));
		}
		String provincialIdCard = (String) fullCreditAssessmentMap.get("provincialIdStr");
		if (provincialIdCard != null && provincialIdCard.trim().length() > 0) {
		    ProvincialIdCard provincialIdCardObj = new ProvincialIdCard();
		    provincialIdCardObj.setProvincialIdNum(EncryptionUtil.decrypt(provincialIdCard));
		    provincialIdCardObj.setProvinceCd((String) fullCreditAssessmentMap.get("provincialIdProvCd"));
		}
		if (creditIdentification.getDriverLicense() != null
		        || creditIdentification.getSin() != null
		        || creditIdentification.getHealthCard() != null
		        || creditIdentification.getPassport() != null
		        || creditIdentification.getProvincialIdCard() != null) {
		    aConsumerCreditProfile.setCreditIdentification(creditIdentification);
		}
	}

	private void populateCreditCardCode(
			HashMap<String, Object> fullCreditAssessmentMap,
			ConsumerCreditProfile aConsumerCreditProfile) {
		CreditCardCode creditCardCode = new CreditCardCode();
		creditCardCode.setPrimaryCreditCardCd((String) fullCreditAssessmentMap.get("creditcardTypCd"));
		creditCardCode.setSecondaryCreditCardCd((String) fullCreditAssessmentMap.get("secondaryCreditCardCd"));
		if (creditCardCode.getPrimaryCreditCardCd() != null
		        && creditCardCode.getPrimaryCreditCardCd().trim().length() > 0) {
		    aConsumerCreditProfile.setCreditCardCd(creditCardCode);
		}
	}

	private void populateCreditAddress(
			HashMap<String, Object> fullCreditAssessmentMap,
			ConsumerCreditProfile aConsumerCreditProfile) {
		CreditAddress creditAddress = new CreditAddress();
		creditAddress.setProvinceCd((String) fullCreditAssessmentMap.get("provinceCd"));
		creditAddress.setPostalCd((String) fullCreditAssessmentMap.get("postalZipCd"));
		creditAddress.setCountryCd((String) fullCreditAssessmentMap.get("countryCd"));
		creditAddress.setCityName((String) fullCreditAssessmentMap.get("cityNm"));
		creditAddress.setAddressLineOne((String) fullCreditAssessmentMap.get("addressLine1Txt"));
		creditAddress.setAddressLineTwo((String) fullCreditAssessmentMap.get("addressLine2Txt"));
		creditAddress.setAddressLineThree((String) fullCreditAssessmentMap.get("addressLine3Txt"));
		creditAddress.setAddressLineFour((String) fullCreditAssessmentMap.get("addressLine4Txt"));
		creditAddress.setAddressLineFive((String) fullCreditAssessmentMap.get("addressLine5Txt"));
		aConsumerCreditProfile.setCreditAddress(creditAddress);
		//
		// Following fields must be populated in order for credit address to be valid
		//
		if (creditAddress.getCityName() != null
		        && creditAddress.getCityName().trim().length() > 0
		        && creditAddress.getProvinceCd() != null
		        && creditAddress.getProvinceCd().trim().length() > 0
		        && creditAddress.getCountryCd() != null
		        && creditAddress.getCountryCd().trim().length() > 0
		        && creditAddress.getPostalCd() != null
		        && creditAddress.getPostalCd().trim().length() > 0) {
		    aConsumerCreditProfile.setCreditAddress(creditAddress);
		}
	}

	private void populatePersonalInfo(
			HashMap<String, Object> baseCreditAssessmentMap,
			HashMap<String, Object> fullCreditAssessmentMap,
			ConsumerCreditProfile aConsumerCreditProfile) {
		PersonalInfo personalInfo = new PersonalInfo();
		personalInfo.setCreditCheckConsentCd((String) fullCreditAssessmentMap.get("creditChkConsentCd"));
		personalInfo.setBirthDate((Date) fullCreditAssessmentMap.get("birthDate"));
		personalInfo.setUnderLegalCareCd((String) fullCreditAssessmentMap.get("legalCareCd"));
		personalInfo.setResidencyCd((String) fullCreditAssessmentMap.get("residencyCd"));
		
		//new attributes
		personalInfo.setProvinceOfCurrentResidenceCd((String) fullCreditAssessmentMap.get("provinceOfCurrentResidenceCd"));
		personalInfo.setEmploymentStatusCd((String) fullCreditAssessmentMap.get("employmentStatusCd"));
		
		if (personalInfo.getCreditCheckConsentCd() != null
		        && personalInfo.getCreditCheckConsentCd().trim().length() > 0) {
		    aConsumerCreditProfile.setPersonalInfo(personalInfo);
		}
	}

	private void populateCreditCustomer(
			HashMap<String, Object> baseCreditAssessmentMap,
			HashMap<String, Object> fullCreditAssessmentMap,
			FullCreditAssessmentRequest returnedCompleteCreditAssessmentRequest) {
		CreditCustomerInfo creditCustomerInfo = new CreditCustomerInfo();
		creditCustomerInfo.setCustomerCreationDate((Date) fullCreditAssessmentMap.get("customerCreationDt"));
		creditCustomerInfo.setCustomerID((Long) fullCreditAssessmentMap.get("customerId"));
		creditCustomerInfo.setCustomerMasterSourceID((Long) fullCreditAssessmentMap.get("customerMasterSrcId"));
		creditCustomerInfo.setCustomerStatusCd((String) fullCreditAssessmentMap.get("customerStatusCd"));
		creditCustomerInfo.setCustomerSubTypeCd((String) fullCreditAssessmentMap.get("custSubtypCd"));
		creditCustomerInfo.setCustomerTypeCd((String) fullCreditAssessmentMap.get("custTypCd"));
		creditCustomerInfo.setRevenueSegmentCd((String) fullCreditAssessmentMap.get("revenueSegmentCd"));
		creditCustomerInfo.setEmployeeInd(CrdaUtility.getBooleanValue((String) fullCreditAssessmentMap.get("employeeInd")));
		
		 
		PersonName personName = new PersonName();
		personName.setFirstName((String) fullCreditAssessmentMap.get("stgFirstNm"));
		personName.setMiddleName((String) fullCreditAssessmentMap.get("stgMiddleNm"));
		personName.setLastName((String) fullCreditAssessmentMap.get("stgLastNm"));
		
		if (personName.getFirstName() != null
		        || personName.getMiddleName() != null
		        || personName.getLastName() != null) {
		    creditCustomerInfo.setPersonName(personName);
		}
		returnedCompleteCreditAssessmentRequest.setCreditCustomerInfo(creditCustomerInfo);
	}

    public void populate_creditAssessmentDetails_Common_CreditBureauData(
            HashMap<String, Object> fullCreditAssessmentMap,
            CreditAssessmentTransaction creditAssessmentTransaction) throws EnterpriseCreditAssessmentServiceException {
        creditAssessmentTransaction.setCreditBureauReportSourceCd((String) fullCreditAssessmentMap.get("reportSourceCd"));
        creditAssessmentTransaction.setCreditClass((String) fullCreditAssessmentMap.get("adjdctnCreditClassCd"));
        creditAssessmentTransaction.setCreditScoreCd((String) fullCreditAssessmentMap.get("adjdctnCreditScoreTxt"));
        creditAssessmentTransaction.setCreditScoreTypeCd((String) fullCreditAssessmentMap.get("adjdctnCreditScoreTypeCd"));

        CreditDecision creditDecision = new CreditDecision();
        creditDecision.setCreditDecisionCd((String) fullCreditAssessmentMap.get("adjdctnDscnCd"));
        creditDecision.setCreditDecisionMessage((String) fullCreditAssessmentMap.get("adjdctnDscnMsgTxt"));
    
        
        if (creditDecision.getCreditDecisionCd() != null
                && creditDecision.getCreditDecisionCd().trim().length() > 0
                && creditDecision.getCreditDecisionMessage() != null
                && creditDecision.getCreditDecisionMessage().trim().length() > 0) {
            creditAssessmentTransaction.setCreditDecisionCd( creditDecision );
        }
        
        
        BigDecimal depositAmtBigDec = CrdaUtility.getBigDecimalVal(fullCreditAssessmentMap.get("adjdctnDepositAmt"),"adjdctnDepositAmt");
		if (depositAmtBigDec != null) {
			creditAssessmentTransaction.setDepositAmt(depositAmtBigDec);
		}

          

    }


    protected void populateCreditAssessmentRequest(CreditAssessmentRequest creditAssessmentRequest,
                                                  HashMap<String, Object> baseCreditAssessmentMap,
                                                  CreditAssessmentDetails creditAssessmentDetails) {
        Long dataSourceId = (Long) baseCreditAssessmentMap.get("dataSourceId");
        if (dataSourceId != null && dataSourceId > 0) {
            creditAssessmentRequest.setApplicationID(String.valueOf(dataSourceId.longValue()));
        }
        creditAssessmentRequest.setCustomerID(creditAssessmentDetails.getCustomerID());
        creditAssessmentRequest.setCommentTxt(creditAssessmentDetails.getCommentTxt());        
        creditAssessmentRequest.setChannelID(creditAssessmentDetails.getChannelID());
        creditAssessmentRequest.setCreditAssessmentTypeCd(creditAssessmentDetails.getCreditAssessmentTypeCd());
        creditAssessmentRequest.setCreditAssessmentSubTypeCd(creditAssessmentDetails.getCreditAssessmentSubTypeCd());
        creditAssessmentRequest.setLineOfBusiness((String) baseCreditAssessmentMap.get("lobCd"));
    }

    protected void populateCarAttrValue(OverrideCreditAssessmentBaseRequest ovedrrideCreditAssessment,
                                        List<HashMap<String, Object>> carAttrValueMapList) {
        for (HashMap<String, Object> carAttrValueMap : carAttrValueMapList) {
            String carAttrType = (String) carAttrValueMap.get("carAttrTypCd");
            String carAttrValue = (String) carAttrValueMap.get("carAttrValueTxt");
            if (carAttrType.equals(CreditAssessmentDaoConstants.CAR_ATTR_CURRENT_CREDIT_CHECK_CONSENT_CD)) {
                //ovedrrideCreditAssessment.setNewCreditCheckConsentCd(carAttrValue);
            } else if (carAttrType.equals(CreditAssessmentDaoConstants.CAR_ATTR_NEW_CREDIT_CHECK_CONSENT_CD)) {
                ovedrrideCreditAssessment.setNewCreditCheckConsentCd(carAttrValue);
            } else if (carAttrType.equals(CreditAssessmentDaoConstants.CAR_ATTR_CURRENT_CREDIT_VALUE_CD)) {
                //ovedrrideCreditAssessment.setCurrentCreditValueCd( carAttrValue );
            } else if (carAttrType.equals(CreditAssessmentDaoConstants.CAR_ATTR_NEW_CREDIT_VALUE_CD)) {
                ovedrrideCreditAssessment.setNewCreditValueCd(carAttrValue);
            } else if (carAttrType.equals(CreditAssessmentDaoConstants.CAR_ATTR_CURRENT_FRAUD_CD)) {
                //ovedrrideCreditAssessment.setCurrentFraudIndictorCd( carAttrValue );
            } else if (carAttrType.equals(CreditAssessmentDaoConstants.CAR_ATTR_NEW_FRAUD_CD)) {
                ovedrrideCreditAssessment.setNewFraudIndicatorCd(carAttrValue);
            }
        }

    }

    public void populateAuditCreditAssessment(HashMap<String, Object> baseCreditAssessmentMap,
                                              List<HashMap<String, Object>> carAttrValueMapList,
                                              CreditAssessmentDetails creditAssessmentDetails) {
    	CreditDecisioningIOverrideCreditAssessmentRequest ovedrrideCreditAssessment = new CreditDecisioningIOverrideCreditAssessmentRequest();

        populateCreditAssessmentRequest(ovedrrideCreditAssessment, baseCreditAssessmentMap, creditAssessmentDetails);

        ovedrrideCreditAssessment.setCustomerID(creditAssessmentDetails.getCustomerID());
        ovedrrideCreditAssessment.setCommentTxt((String) baseCreditAssessmentMap.get("commentTxt"));

        populateCarAttrValue(ovedrrideCreditAssessment, carAttrValueMapList);

        creditAssessmentDetails.setCreditDecisioningInput(ovedrrideCreditAssessment);
    }

    public void populateOverrideCreditAssessment(
            HashMap<String, Object> baseCreditAssessmentMap,
            HashMap<String, Object> overrideCreditAssessmentMap,
            List<HashMap<String, Object>> creditDecisionResultDtlList,
            List<HashMap<String, Object>> carAttrValueMapList,
            CreditAssessmentDetails creditAssessmentDetails) {
    	CreditDecisioningIOverrideCreditAssessmentRequest ovedrrideCreditAssessment = new CreditDecisioningIOverrideCreditAssessmentRequest();
        populateCreditAssessmentRequest(ovedrrideCreditAssessment, baseCreditAssessmentMap, creditAssessmentDetails);
        if (creditAssessmentDetails != null) {
            ovedrrideCreditAssessment.setCustomerID(creditAssessmentDetails.getCustomerID());
        }
        if (baseCreditAssessmentMap != null) {
            ovedrrideCreditAssessment.setCommentTxt((String) baseCreditAssessmentMap.get("commentTxt"));
        } 
        populateCarAttrValue(ovedrrideCreditAssessment, carAttrValueMapList);
        if (creditAssessmentDetails != null) {
            creditAssessmentDetails.setCreditDecisioningInput(ovedrrideCreditAssessment);
        }

        if (overrideCreditAssessmentMap != null) {
            populateCreditDecisioningResult(overrideCreditAssessmentMap, creditDecisionResultDtlList, creditAssessmentDetails);
        }
    }

    public void populateCreditDecisioningResult(
            HashMap<String, Object> completeCreditAssessmentMap,
            List<HashMap<String, Object>> creditDecisionResultDtlList,
            CreditAssessmentTransaction creditAssessmentDetails) {
        Long intCrdtDscnTrnRsltId = (Long) completeCreditAssessmentMap.get("intCrdtDscnTrnRsltId");
        if (intCrdtDscnTrnRsltId != null && intCrdtDscnTrnRsltId > 0) {
            CreditAssessmentResult creditAssessmentResult = new CreditAssessmentResult();

            creditAssessmentResult.setAssessmentMessageCd((String) completeCreditAssessmentMap.get("assessmentMsgCd"));
            creditAssessmentResult.setAssessmentResultCd((String) completeCreditAssessmentMap.get("assessmentRsltCd"));
            creditAssessmentResult.setAssessmentResultReasonCd((String) completeCreditAssessmentMap.get("assessmentRsltReasonCd"));
            creditAssessmentResult.setCreditValueCd((String) completeCreditAssessmentMap.get("creditValueCd"));
            creditAssessmentResult.setFraudIndicatorCd((String) completeCreditAssessmentMap.get("fraudIndCd"));
            creditAssessmentResult.setDecisionCd((String) completeCreditAssessmentMap.get("decisionCd"));
            ProductCategoryQualification productCategoryQ = new ProductCategoryQualification();
            productCategoryQ.setBoltOnInd(CrdaUtility.getBooleanValue((String) completeCreditAssessmentMap.get("productCategoryBoltOn")));
            
            populateCreditDecisionResultDtlList(creditDecisionResultDtlList,
					intCrdtDscnTrnRsltId, creditAssessmentResult,
					productCategoryQ);

            
            creditAssessmentDetails.setCreditDecisioningResult(creditAssessmentResult);
        }
    }

	private void populateCreditDecisionResultDtlList(
			List<HashMap<String, Object>> creditDecisionResultDtlList,
			Long intCrdtDscnTrnRsltId,
			CreditAssessmentResult creditAssessmentResult,
			ProductCategoryQualification productCategoryQ) {
		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		List<String> fraudCodeList = new ArrayList<String>();
		List<BureauInformation> bureauInformationList = new ArrayList<BureauInformation>();
		for (HashMap<String, Object> intCrdDscnRsltDtlMap : creditDecisionResultDtlList) {
		    Long intCrdtDscnTrnRsltId_1 = (Long) intCrdDscnRsltDtlMap.get("intCrdtDscnTrnRsltId");
		    // this condition is important for it to be re-used by search credit assessment
		    if (intCrdtDscnTrnRsltId_1 != null &&
		            (intCrdtDscnTrnRsltId_1.longValue() == intCrdtDscnTrnRsltId.longValue())) {
		        // TODO: depositAmt is at a parent level
		       /*<result property="depositAmt" javaType="double" column="DEPOSIT_AMT"/>*/
		        String typeOfResult = (String) intCrdDscnRsltDtlMap.get("intCrdtDscnTrnRsltDtlCd");
		        if (typeOfResult != null) {
		            if (typeOfResult.equals(CreditAssessmentDaoConstants.RESULT_DTL_PROD_QUALIFICATION_CD)) {
		                ProductCategory pc = new ProductCategory();
		                pc.setCategoryCd((String) intCrdDscnRsltDtlMap.get("creditApprvdProdCatgyCd"));
		                pc.setQualified(CrdaUtility.getBooleanValue((String) intCrdDscnRsltDtlMap.get("productQualInd")));
		                productCategoryList.add(pc);
		            } else 
		            	if (typeOfResult.equals(CreditAssessmentDaoConstants.RESULT_DTL_FRAUD_WARNING_CD)) {
		                fraudCodeList.add((String) intCrdDscnRsltDtlMap.get("fraudMsgCd"));
		            	}else 
			            	if (typeOfResult.equals(CreditAssessmentDaoConstants.RESULT_DTL_BUREAU_INFO_CD)) {
				                BureauInformation aBureauInformation= new BureauInformation();
				                aBureauInformation.setBureauCd((String) intCrdDscnRsltDtlMap.get("bureauCd"));
				                aBureauInformation.setBureauType((String) intCrdDscnRsltDtlMap.get("bureauInfoTypCd"));
				                aBureauInformation.setBureauPriority((String) intCrdDscnRsltDtlMap.get("bureauInfoPriorityCd"));            
				                bureauInformationList.add(aBureauInformation);
				            } 
		        }
		    }
		}
		 
		if (bureauInformationList.size() > 0) {
		    creditAssessmentResult.setBureauInformationList(bureauInformationList);
		}		 
		if (productCategoryList.size() > 0) {
		    productCategoryQ.getProductCategoryList().addAll(productCategoryList);
		}
		creditAssessmentResult.setProductCategoryQualification(productCategoryQ);
		if (fraudCodeList.size() > 0) {
		    creditAssessmentResult.getFraudMessageCdList().addAll(fraudCodeList);
		}
	}
    public void populatetCreditBureauTransResult(
            HashMap<String, Object> creditBureauTrnMap,
            List<HashMap<String, Object>> creditBureauTrnDtlMapList,
            CreditBureauResult creditBureauResult) throws EnterpriseCreditAssessmentServiceException {
        if (creditBureauTrnMap != null) {

            Long creditBureauTrnId = (Long) creditBureauTrnMap.get("creditBureauTrnId");
            if (creditBureauTrnId != null && creditBureauTrnId > 0) {

               // populateCreditAssessmentTransactionCreditBureauData(creditBureauTrn, creditBureauResult);

                populate_AdjudicationResult(creditBureauTrnMap,creditBureauResult);
                
        		//cgw does not populate BureauReportStatusCd. It is maintaned by eCrda. For a successfull  ecrda stores 1 in CREDIT_BUREAU_TRN_RSLT_STAT_CD . void operation updates this value to 2
                creditBureauResult.setBureauReportStatusCd((String) creditBureauTrnMap.get("creditBureauTrnRsltStatCd"));
                
                creditBureauResult.setBureauReportStatusDate((Date) creditBureauTrnMap.get("crBruTrnRsltStatUpdtDt"));
                creditBureauResult.setCreationDate((Date) creditBureauTrnMap.get("rsltCreationDt"));
                creditBureauResult.setErrorCd((String) creditBureauTrnMap.get("trnErrorCd"));
               	//CGW does not popualte CreditBureauDataResult.Id . ecrda sets ID to  CREDIT_BUREAU_TRN_ID                
                creditBureauResult.setId(creditBureauTrnId);
                PersonName creditBureauPersonName = new PersonName();
                creditBureauPersonName.setFirstName((String) creditBureauTrnMap.get("firstNm"));
                creditBureauPersonName.setMiddleName((String) creditBureauTrnMap.get("middleNm"));
                creditBureauPersonName.setLastName((String) creditBureauTrnMap.get("lastNm"));
                if (creditBureauPersonName.getFirstName() != null
                        || creditBureauPersonName.getMiddleName() != null
                        || creditBureauPersonName.getLastName() != null) {
                    creditBureauResult.setPersonName(creditBureauPersonName);
                }
               
                creditBureauResult.setReportSourceCd((String) creditBureauTrnMap.get("reportSourceCd"));
                creditBureauResult.setReportType((String) creditBureauTrnMap.get("reportTypTxt"));
             

                
                RiskIndicator riskIndicator = new RiskIndicator();
                boolean riskPresent = false;
                List<FraudWarning> fraudList = new ArrayList<FraudWarning>();
                boolean fraudPresent = false;
                List<ScoreCardAttribute> scoreCardAttributeList = new ArrayList<ScoreCardAttribute>();
                boolean scoreCardPresent = false;
                for (HashMap<String, Object> creditBureauTrnDtl : creditBureauTrnDtlMapList) {
                    String creditBureauTrnDtlType = (String) creditBureauTrnDtl.get("creditBureauTrnDtlCd");
                    if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_RISK_TYPE)) {
                        riskPresent = true;
                        String riskIndicatorType = (String) creditBureauTrnDtl.get("riskIndTypCd");
                        String riskIndicatorValue = (String) creditBureauTrnDtl.get("riskIndValueTxt");
                        if (riskIndicatorType.equals(CreditAssessmentDaoConstants.NO_HIT_THIN_FILE_CD)) {
                            riskIndicator.setNoHitThinFileInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
                        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.TRUE_THIN_FILE_CD)) {
                            riskIndicator.setTrueThinFileInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
                        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.HIGH_RISK_THIN_FILE_CD)) {
                            riskIndicator.setHighRiskThinFileInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
                        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.TEMP_SIN_CD)) {
                            riskIndicator.setTempSINInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
                        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.UNDER_AGE_CD)) {
                            riskIndicator.setUnderAgeInd(CrdaUtility.getBooleanValue(riskIndicatorValue));
                        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.PRIMARY_RISK_CD)) {
                            riskIndicator.setPrimaryRiskInd(riskIndicatorValue);
                        } else if (riskIndicatorType.equals(CreditAssessmentDaoConstants.SECONDARY_RISK_CD)) {
                            riskIndicator.setSecondaryRiskInd(riskIndicatorValue);
                        }
                    } else if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_FRAUD_TYPE)) {
                        fraudPresent = true;
                        FraudWarning fraudWarning = new FraudWarning();
                        fraudWarning.setFraudType((String) creditBureauTrnDtl.get("fraudTypCd"));
                        fraudWarning.setFraudMessage((String) creditBureauTrnDtl.get("fraudMsgTxt"));
                        fraudWarning.setFraudCd((String) creditBureauTrnDtl.get("fraudCd"));
                        fraudList.add(fraudWarning);
                    } else if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_SCORE_TYPE)) {
                        scoreCardPresent = true;
                        ScoreCardAttribute scoreCardAttribute = new ScoreCardAttribute();
                        scoreCardAttribute.setScoreName((String) creditBureauTrnDtl.get("scoreNm"));
                        scoreCardAttribute.setScoreValue((String) creditBureauTrnDtl.get("scoreValueTxt"));
                        scoreCardAttributeList.add(scoreCardAttribute);
                    } else if (creditBureauTrnDtlType.equals(CreditAssessmentDaoConstants.CREDIT_BUREAU_DTL_DOC_TYPE)) {
                        String docType = (String) creditBureauTrnDtl.get("extDocFormatTyp");
                        if (docType != null && docType.toUpperCase().equals(CreditAssessmentDaoConstants.REPORT_DOC_TYPE_TXT)) {
                            CreditBureauDocument creditBureauDocument = new CreditBureauDocument();
                            creditBureauDocument.setDocumentID(String.valueOf((Long) creditBureauTrnDtl.get("creditBureauTrnDtlId")));
                            creditBureauDocument.setDocumentPath((String) creditBureauTrnDtl.get("extDocPathStr"));
                            creditBureauDocument.setDocumentType(docType.toLowerCase());
                        }
                    }
                }
                if (riskPresent) {
                    creditBureauResult.setRiskIndicator(riskIndicator);
                }
                if (fraudPresent) {
                    creditBureauResult.getFraudWarningList().addAll(fraudList);
                }
                if (scoreCardPresent) {
                    if (creditBureauResult.getAdjudicationResult() == null) {
                        creditBureauResult.setAdjudicationResult(new AdjudicationResult());
                    }
                    creditBureauResult.getAdjudicationResult().getScoreCardAttributeList().addAll(scoreCardAttributeList);
                }
            }
    
        }
    }
    
    
}