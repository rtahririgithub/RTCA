package com.telus.credit.crda.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConstants;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditProfileBr;
import com.telus.credit.domain.common.CreditAddress;
import com.telus.credit.domain.common.CreditIdentification;
import com.telus.credit.domain.common.CustomerGuarantor;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.OverrideCreditAssessmentRequest;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.credit.domain.creditprofile.ConsumerCreditProfile;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.erm.referenceods.domain.ReferenceDecode;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;
/*
 * 
 * Validate Mandatory methods checks for mandatory fields
 * Validate Values methods checks for values of any field provided (optional and mandatory)
 * */
public class CrdaValidationUtility {

	// true => comment is required in the request
	private static final Map<String, Boolean> isCommentRequired_Map;
	static {
		isCommentRequired_Map = new HashMap<String, Boolean>();
		isCommentRequired_Map.put("FULL_ASSESSMENT_AUTO_ASSESSMENT", false);
		isCommentRequired_Map.put("FULL_ASSESSMENT_GET_BUREAU_DATA", true);
		isCommentRequired_Map.put("FULL_ASSESSMENT_MONTHLY_CVUD", false);
		isCommentRequired_Map.put("FULL_ASSESSMENT_REOPEN_ASSESSMENT", false);
		isCommentRequired_Map.put("FULL_ASSESSMENT_NEW_ACC_ASSESSMENT", false);
		isCommentRequired_Map.put("FULL_ASSESSMENT_MANUAL_ASSESSMENT", true);

		isCommentRequired_Map.put("AUDIT_BUREAU_CONSENT", true);

		isCommentRequired_Map.put("OVRD_ASSESSMENT_CANCEL_DEPOSIT_INV", false);
		isCommentRequired_Map.put("OVRD_ASSESSMENT_MANUAL_OVERRIDE", true);
		isCommentRequired_Map.put("OVRD_ASSESSMENT_DEPOSIT_DOWNGRADE", false);
		isCommentRequired_Map.put("OVRD_ASSESSMENT_UNMERGED", false);

	}
	
	public static void validateCommentByAsmtTypeSubtype(
			CreditAssessmentRequest creditAssessmentRequest)
			throws EnterpriseCreditAssessmentPolicyException {
		String key = creditAssessmentRequest.getCreditAssessmentTypeCd() + "_"
				+ creditAssessmentRequest.getCreditAssessmentSubTypeCd();
		Boolean commentRequired = isCommentRequired_Map.get(key) != null ? isCommentRequired_Map
				.get(key)
				: false;
		// validate comment
		if (commentRequired) {
			if (creditAssessmentRequest.getCommentTxt() == null
					|| creditAssessmentRequest.getCommentTxt().trim().equals(
							EnterpriseCreditAssessmentConsts.EMPTY_STR)) {
				throw new EnterpriseCreditAssessmentPolicyException(
						EnterpriseCreditAssessmentExceptionCodes.MISSING_COMMENTTXT_STR
								+ " [ Credit assessment type_subtype: "
								+ key
								+ "] "
								+ "[ Customer ID ="
								+ creditAssessmentRequest.getCustomerID() + "]",
						EnterpriseCreditAssessmentExceptionCodes.MISSING_COMMENTTXT);
			}
		}

	}

	public static void validateMandatoryCreditCustomerInfoFields(
			long customerID,
			com.telus.credit.domain.common.CreditCustomerInfo creditCustomerInfo)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");

		if (creditCustomerInfo == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_STR
							+ extramsg + ". creditCustomerInfo is null",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER);
		}
		if (creditCustomerInfo.getCustomerCreationDate() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_INFO_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_INFO);
		}
		if (creditCustomerInfo.getCustomerID() == 0) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_ID_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_ID);
		}
		if (creditCustomerInfo.getCustomerMasterSourceID() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_MASTER_SOURCE_ID_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_MASTER_SOURCE_ID);
		}
		if (creditCustomerInfo.getCustomerStatusCd() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_STATUS_CODE_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_STATUS_CODE);
		}
		if (creditCustomerInfo.getCustomerTypeCd() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_TYPE_CODE_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_TYPE_CODE);
		}
		if (creditCustomerInfo.getCustomerSubTypeCd() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_SUBTYPE_CODE_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_SUBTYPE_CODE);
		}
		if (creditCustomerInfo.getPersonName() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_PERSON_NAME_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_PERSON_NAME);
		}

		if (creditCustomerInfo.getPersonName().getLastName() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_PERSON_NAME_LASTNAME_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_PERSON_NAME_LASTNAME);
		}
		if (creditCustomerInfo.getPersonName().getFirstName() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_PERSON_NAME_FIRSTNAME_STR
							+ extramsg,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_PERSON_NAME_FIRSTNAME);
		}
	}

	private static void validateCreditProfileVal(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			CreditProfileBr m_creditProfileBr,
			com.telus.credit.domain.CreditProfile creditProfileVal)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");
	if (aConsumerCreditProfile != null && m_creditProfileBr != null && creditProfileVal!= null) {
		// validate values
		ValidationResult creditProfileBrValidationResult = m_creditProfileBr.validate(creditProfileVal);
		if (!creditProfileBrValidationResult.isValid()) {
			// create extramsg
			ValidationError[] validationErrorList = creditProfileBrValidationResult
					.errors();
			StringBuffer validationErrorListMsg = new StringBuffer("");
			for (int i = 0; i < validationErrorList.length; i++) {
				validationErrorListMsg.append("["
						+ validationErrorList[i].getLabel()
						// + ":" + validationErrorList[i].get
						+ "]");
			}
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_PROFILE_STR
							+ extramsg + ":" + validationErrorListMsg,
					EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_PROFILE);

		}
	  }

	}

	static public void validate_CrdPrflID_CrdPRflStatusCd_CrdWorthiness_CustID_FieldsValues(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			CreditProfileBr m_creditProfileBr)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");
	if (aConsumerCreditProfile != null && m_creditProfileBr != null) {
		com.telus.credit.domain.CreditProfile creditProfileVal = new com.telus.credit.domain.CreditProfile();

		if (aConsumerCreditProfile.getCreditProfileID() != null ){
			creditProfileVal.set_id(aConsumerCreditProfile.getCreditProfileID());
		}
		creditProfileVal.set_id(aConsumerCreditProfile.getCustomerID());

			// Status
		if (aConsumerCreditProfile.getCreditProfileStatusCd() != null&& !aConsumerCreditProfile.getCreditProfileStatusCd().trim().equals("")) {
			creditProfileVal.setStatus(aConsumerCreditProfile.getCreditProfileStatusCd());
		}
		addCreditWorthinessToCreditProfileValue(aConsumerCreditProfile,creditProfileVal);
		
		validateCreditProfileVal(customerID, aConsumerCreditProfile,m_creditProfileBr, creditProfileVal);
	}
	}
	static public void validateCreditProfileFieldsValues(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			CreditProfileBr m_creditProfileBr)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");
	if (aConsumerCreditProfile != null && m_creditProfileBr != null) {
		com.telus.credit.domain.CreditProfile creditProfileVal = new com.telus.credit.domain.CreditProfile();

		// add to creditProfileVal :ConsumerCreditProfile attributes
		// ****************************
		// don't set bypassMatchIndicator if the input is null
		if (aConsumerCreditProfile.isBypassMatchIndicator() != null) {
			creditProfileVal.setBypassMatchIndicator(aConsumerCreditProfile.isBypassMatchIndicator().booleanValue());
		}

		if (aConsumerCreditProfile.getCreditProfileID() != null ){
			creditProfileVal.set_id(aConsumerCreditProfile.getCreditProfileID());
		}


		creditProfileVal.set_id(aConsumerCreditProfile.getCustomerID());

	
		// ApplicationProvinceCd
		if (aConsumerCreditProfile.getApplicationProvinceCd() != null&& !aConsumerCreditProfile.getApplicationProvinceCd().trim().equals("")) {
			creditProfileVal.setApplicationProvinceCd(aConsumerCreditProfile.getApplicationProvinceCd());
		}
		// Status
		if (aConsumerCreditProfile.getCreditProfileStatusCd() != null&& !aConsumerCreditProfile.getCreditProfileStatusCd().trim().equals("")) {
			creditProfileVal.setStatus(aConsumerCreditProfile.getCreditProfileStatusCd());
		}

		addPersonalInfoToCreditProfilevalue(aConsumerCreditProfile, creditProfileVal);
		//validateCreditAddressBycreditCheckConsentCd(aConsumerCreditProfile,extramsg);
		addCreditAddressToCreditProfileValue(aConsumerCreditProfile,creditProfileVal);
		
		// addCreditIdentificationToCreditProfileValue(aConsumerCreditProfile,creditProfileVal);
		// addCustomerGuarantorToCreditProfilevalue(aConsumerCreditProfile,creditProfileVal);
		// addCreditCardCdtoCreditProfilevalue(aConsumerCreditProfile,creditProfileVal);
		addCreditWorthinessToCreditProfileValue(aConsumerCreditProfile,creditProfileVal);
		
		validateCreditProfileVal(customerID, aConsumerCreditProfile,m_creditProfileBr, creditProfileVal);
	}
	}

	public static void validateCreditAddressBycreditCheckConsentCd(
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			StringBuffer extramsg)
			throws EnterpriseCreditAssessmentPolicyException {
		// validate address if creditCheckConsentCd = Y
		// commented out to support customer with no crd address and
		// creditCheckConsentCd = Y
		
		if (aConsumerCreditProfile != null ) {
			CreditAddress creditAddress = aConsumerCreditProfile.getCreditAddress();
			String creditCheckConsentCd = (aConsumerCreditProfile.getPersonalInfo() == null || aConsumerCreditProfile
					.getPersonalInfo().getCreditCheckConsentCd() == null) ? EnterpriseCreditAssessmentConsts.EMPTY_STR
					: aConsumerCreditProfile.getPersonalInfo()
							.getCreditCheckConsentCd().trim();
			if (("Y".equalsIgnoreCase(creditCheckConsentCd) && creditAddress == null)) {
				throw new EnterpriseCreditAssessmentPolicyException(
						EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_ADDRESS_STR
								+ extramsg,
						EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_ADDRESS);
			}
		}
	}

	private static void addCreditWorthinessToCreditProfileValue(
			ConsumerCreditProfile aConsumerCreditProfile,
			CreditProfile creditProfileVal) {
		if (aConsumerCreditProfile != null && creditProfileVal != null) {
			// Credit value value validation
			if (aConsumerCreditProfile.getCreditWorthiness()!= null
					&& aConsumerCreditProfile.getCreditWorthiness().getCreditValueCd() != null
					&& !aConsumerCreditProfile.getCreditWorthiness()
							.getCreditValueCd().trim().equals("")) {
				com.telus.credit.domain.CreditValue aCreditValue = new com.telus.credit.domain.CreditValue();
				aCreditValue.setCreditValueCode(aConsumerCreditProfile
						.getCreditWorthiness().getCreditValueCd().trim());
				creditProfileVal.setCreditValue(aCreditValue);
			}
		}

	}

	public static void validateMandatoryCreditProfileFields(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");

		if (aConsumerCreditProfile == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_DATA_STR
							+ extramsg + ". creditProfileData is null",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_DATA);
		}
	}
	
	public static void validateMandatoryCreditProfile_CreditAddress_Fields(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");

		validateMandatoryCreditProfile_Fields( customerID,aConsumerCreditProfile);
		
		
		if (aConsumerCreditProfile != null ) {
			CreditAddress creditAddress = aConsumerCreditProfile.getCreditAddress();
			String creditCheckConsentCd = (aConsumerCreditProfile.getPersonalInfo() == null || aConsumerCreditProfile
					.getPersonalInfo().getCreditCheckConsentCd() == null) ? EnterpriseCreditAssessmentConsts.EMPTY_STR
					: aConsumerCreditProfile.getPersonalInfo()
							.getCreditCheckConsentCd().trim();
			if (("Y".equalsIgnoreCase(creditCheckConsentCd) && creditAddress == null)) {
				throw new EnterpriseCreditAssessmentPolicyException(
						EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_ADDRESS_STR
								+ extramsg,
						EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_ADDRESS);
			}
		}

	
	}
	public static void validateMandatoryCreditProfile_Fields(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");

		if (aConsumerCreditProfile == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_DATA_STR
							+ extramsg + ". creditProfileData is null",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_DATA);
		}
		if (aConsumerCreditProfile.getCreditProfileID() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_ID_STR
							+ extramsg + ". creditProfileData CreditProfileID is null",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_PROFILE_ID);
		}		
		if (aConsumerCreditProfile.getCustomerID() == 0) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_ID_STR
							+ extramsg + ". creditProfileData CustomerID is 0",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CUSTOMER_CUSTOMER_ID);
		}				
		if (aConsumerCreditProfile.getCreditProfileStatusCd() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CRPRFL_STATUS_CODE_STR
							+ extramsg + ". creditProfileData CreditProfileStatusCd is null",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_CRPRFL_STATUS_CODE);
		}
	}	
	public static void validateMandatoryCreditProfile_CreditWorthiness_Fields(
			long customerID,
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile)
			throws EnterpriseCreditAssessmentPolicyException {
		StringBuffer extramsg = new StringBuffer("");
		extramsg.append("[" + "for Customer ID : " + customerID + "]");

		validateMandatoryCreditProfile_Fields( customerID,aConsumerCreditProfile);		
		
		if (aConsumerCreditProfile.getCreditWorthiness() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_WORTHINESS_STR
							+ extramsg + ". creditProfileData Credit Worthiness is null",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_WORTHINESS);
		}
		
		if (aConsumerCreditProfile.getCreditWorthiness().getCreditValueCd() == null) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_VALUE_CODE_STR
							+ extramsg + ". creditProfileData.CreditWorthiness Credit value is null",
					EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_VALUE_CODE);
		}
	}	
	private static void addPersonalInfoToCreditProfilevalue(
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			com.telus.credit.domain.CreditProfile creditProfileVal) {
	if (aConsumerCreditProfile != null && creditProfileVal != null) {		
		if (aConsumerCreditProfile.getPersonalInfo() != null) {
			// Birthdate
			creditProfileVal.setBirthdate(aConsumerCreditProfile
					.getPersonalInfo().getBirthDate());
			// CreditCheckConsentCode
			if (aConsumerCreditProfile.getPersonalInfo()
					.getCreditCheckConsentCd() != null
					&& !aConsumerCreditProfile.getPersonalInfo()
							.getCreditCheckConsentCd().trim().equals("")) {
				creditProfileVal
						.setCreditCheckConsentCode(aConsumerCreditProfile
								.getPersonalInfo().getCreditCheckConsentCd());
			}
			// EmploymentStatusCode
			if (aConsumerCreditProfile.getPersonalInfo()
					.getEmploymentStatusCd() != null
					&& !aConsumerCreditProfile.getPersonalInfo()
							.getEmploymentStatusCd().trim().equals("")) {
				creditProfileVal.setEmploymentStatusCode(aConsumerCreditProfile
						.getPersonalInfo().getEmploymentStatusCd());
			}
			// ResidencyCode
			if (aConsumerCreditProfile.getPersonalInfo().getResidencyCd() != null
					&& !aConsumerCreditProfile.getPersonalInfo()
							.getResidencyCd().trim().equals("")) {
				creditProfileVal.setResidencyCode(aConsumerCreditProfile
						.getPersonalInfo().getResidencyCd());
			}
			// UnderLegalCareCode
			if (aConsumerCreditProfile.getPersonalInfo().getUnderLegalCareCd() != null
					&& !aConsumerCreditProfile.getPersonalInfo()
							.getUnderLegalCareCd().trim().equals("")) {
				creditProfileVal.setUnderLegalCareCode(aConsumerCreditProfile
						.getPersonalInfo().getUnderLegalCareCd());
			}
			// ProvinceOfCurrentResidenceCd
			if (aConsumerCreditProfile.getPersonalInfo()
					.getProvinceOfCurrentResidenceCd() != null
					&& !aConsumerCreditProfile.getPersonalInfo()
							.getProvinceOfCurrentResidenceCd().trim()
							.equals("")) {
				creditProfileVal
						.setProvinceOfCurrentResidenceCd(aConsumerCreditProfile
								.getPersonalInfo()
								.getProvinceOfCurrentResidenceCd());
			}
		}
	 }
	}

	private static void addCreditAddressToCreditProfileValue(
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			com.telus.credit.domain.CreditProfile creditProfileVal) {
		if (aConsumerCreditProfile != null && creditProfileVal != null) { 
			CreditAddress creditAddress = aConsumerCreditProfile.getCreditAddress();
			if (creditAddress != null) {
				com.telus.credit.domain.CreditAddress creditAddressVal = new com.telus.credit.domain.CreditAddress();
				// no id in creditAddress
				// creditAddressVal.set_id();
				if (creditAddress.getAddressLineFive() != null
						&& !creditAddress.getAddressLineFive().trim().equals("")) {
					creditAddressVal.setAddressLineFive(creditAddress
							.getAddressLineFive());
				}
				if (creditAddress.getAddressLineFour() != null
						&& !creditAddress.getAddressLineFour().trim().equals("")) {
					creditAddressVal.setAddressLineFour(creditAddress
							.getAddressLineFour());
				}
				if (creditAddress.getAddressLineOne() != null
						&& !creditAddress.getAddressLineOne().trim().equals("")) {
					creditAddressVal.setAddressLineOne(creditAddress
							.getAddressLineOne());
				}
				if (creditAddress.getAddressLineThree() != null
						&& !creditAddress.getAddressLineThree().trim().equals("")) {
					creditAddressVal.setAddressLineThree(creditAddress
							.getAddressLineThree());
				}
				if (creditAddress.getAddressLineTwo() != null
						&& !creditAddress.getAddressLineTwo().trim().equals("")) {
					creditAddressVal.setAddressLineTwo(creditAddress
							.getAddressLineTwo());
				}
				if (creditAddress.getAddressTypeCd() != null
						&& !creditAddress.getAddressTypeCd().trim().equals("")) {
					creditAddressVal.setAddressTypeCode(creditAddress
							.getAddressTypeCd());
				}
				if (creditAddress.getCityName() != null
						&& !creditAddress.getCityName().trim().equals("")) {
					creditAddressVal.setCity(creditAddress.getCityName());
				}
				if (creditAddress.getCountryCd() != null
						&& !creditAddress.getCountryCd().trim().equals("")) {
					creditAddressVal.setCountryCode(creditAddress.getCountryCd());
				}

				// no LastUpdateTimestamp in creditAddress
				// creditAddressVal.setLastUpdateTimestamp(creditAddress.getLastUpdateTimestamp());
				if (creditAddress.getPostalCd() != null
						&& !creditAddress.getPostalCd().trim().equals("")) {
					creditAddressVal.setPostalCode(creditAddress.getPostalCd());
				}
				if (creditAddress.getProvinceCd() != null
						&& creditAddress.getProvinceCd().trim().equals("")) {
					creditAddressVal.setProvinceCode(creditAddress.getProvinceCd());
				}
				// creditAddressVal.setType(creditAddress.getAddressTypeCd());
				if (creditAddress.getCreditAddressTypeCd() != null
						&& !creditAddress.getCreditAddressTypeCd().trim()
								.equals("")) {
					creditAddressVal
							.setType(creditAddress.getCreditAddressTypeCd());
				}
				creditProfileVal.setCreditAddress(creditAddressVal);
			}
		}
	}

	private static void addCreditCardCdtoCreditProfilevalue(
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			com.telus.credit.domain.CreditProfile creditProfileVal) {
		if (aConsumerCreditProfile != null && creditProfileVal != null) { 		
			String secondaryCreditCardCd = (aConsumerCreditProfile
					.getCreditCardCd() == null || aConsumerCreditProfile
					.getCreditCardCd().getSecondaryCreditCardCd() == null) ? null
					: aConsumerCreditProfile.getCreditCardCd()
							.getSecondaryCreditCardCd().trim();
			creditProfileVal.setSecondaryCreditCardCode(secondaryCreditCardCd);
	
			String primaryCreditCardCd = (aConsumerCreditProfile.getCreditCardCd() == null || aConsumerCreditProfile
					.getCreditCardCd().getPrimaryCreditCardCd() == null) ? null
					: aConsumerCreditProfile.getCreditCardCd()
							.getPrimaryCreditCardCd().trim();
			creditProfileVal.setPrimaryCreditCardCode(primaryCreditCardCd);
		}
	}

	private static void addCustomerGuarantorToCreditProfilevalue(
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			com.telus.credit.domain.CreditProfile creditProfileVal) {
		
	if (aConsumerCreditProfile != null && creditProfileVal != null) {
		CustomerGuarantor customerGuarantor = aConsumerCreditProfile
				.getCustomerGuarantor();
		if (customerGuarantor != null) {
			com.telus.credit.domain.CustomerGuarantor customerGuarantorVal = new com.telus.credit.domain.CustomerGuarantor();
			// customerGuarantorVal.set_id()
			customerGuarantorVal.setComment(customerGuarantor.getCommentTxt());
			customerGuarantorVal.setExpiryDate(customerGuarantor
					.getExpiryDate());
			if (customerGuarantor.getGuaranteedAmt() != null) {
				customerGuarantorVal
						.setGuaranteedAmount(new com.telus.framework.math.Money(
								customerGuarantor.getGuaranteedAmt()
										.doubleValue()));
			}
			// customerGuarantorVal.setGuaranteedCustomerId();
			customerGuarantorVal.setGuarantorCustomerId((new Long(
					customerGuarantor.getGuarantorCustomerID()).intValue()));
			customerGuarantorVal.setGuarantorCreditProfileId(customerGuarantor
					.getGuarantorCreditProfileID());
			customerGuarantorVal.setGuarantorFullName(customerGuarantor
					.getGuarantorFullName());
			customerGuarantorVal.setGuarantorPhoneNumber(customerGuarantor
					.getGuarantorPhoneNum());
			// customerGuarantorVal.setLastUpdateTimestamp();

			customerGuarantorVal.setReferenceNumber(customerGuarantor
					.getReferenceNum());
			creditProfileVal.setCustomerGuarantor(customerGuarantorVal);
		}
	}
	}

	private static void addCreditIdentificationToCreditProfileValue(
			com.telus.credit.domain.creditprofile.ConsumerCreditProfile aConsumerCreditProfile,
			com.telus.credit.domain.CreditProfile creditProfileVal) {
		
	if (aConsumerCreditProfile != null && creditProfileVal != null) {
		CreditIdentification creditIdentification = aConsumerCreditProfile
				.getCreditIdentification();
		if (creditIdentification != null) {
			List<com.telus.credit.domain.CreditIDCard> creditIDCardList = new ArrayList<com.telus.credit.domain.CreditIDCard>();

			if (creditIdentification.getSin() != null
					&& !creditIdentification.getSin().trim().equals("")) {
				com.telus.credit.domain.CreditIDCard creditIDCardSinVal = new com.telus.credit.domain.CreditIDCard();
				creditIDCardSinVal.setCreditProfileId(aConsumerCreditProfile
						.getCreditProfileID());
				creditIDCardSinVal
						.setIdTypeCode(com.telus.credit.domain.CreditIDCard.SIN_KEY);
				creditIDCardSinVal.setIdNumber(creditIdentification.getSin());
				creditIDCardList.add(creditIDCardSinVal);
			}
			if (creditIdentification.getDriverLicense() != null) {
				com.telus.credit.domain.CreditIDCard creditIDCardDLVal = new com.telus.credit.domain.CreditIDCard();
				creditIDCardDLVal.setCreditProfileId(aConsumerCreditProfile
						.getCreditProfileID());
				creditIDCardDLVal
						.setIdTypeCode(com.telus.credit.domain.CreditIDCard.DRIVERS_LICENSE_KEY);
				if (creditIdentification.getDriverLicense() != null
						&& !creditIdentification.getDriverLicense()
								.getDriverLicenseNum().trim().equals("")) {
					creditIDCardDLVal.setIdNumber(creditIdentification
							.getDriverLicense().getDriverLicenseNum());
				}
				if (creditIdentification.getDriverLicense() != null
						&& !creditIdentification.getDriverLicense()
								.getProvinceCd().trim().equals("")) {
					creditIDCardDLVal.setProvinceCode(creditIdentification
							.getDriverLicense().getProvinceCd());
				}
				if (creditIdentification.getDriverLicense() != null
						&& (!creditIdentification.getDriverLicense()
								.getDriverLicenseNum().trim().equals("") || !creditIdentification
								.getDriverLicense().getProvinceCd().trim()
								.equals(""))) {
					creditIDCardList.add(creditIDCardDLVal);
				}

			}
			if (creditIdentification.getHealthCard() != null) {
				com.telus.credit.domain.CreditIDCard creditIDCardgetHealthCardVal = new com.telus.credit.domain.CreditIDCard();
				creditIDCardgetHealthCardVal
						.setCreditProfileId(aConsumerCreditProfile
								.getCreditProfileID());
				creditIDCardgetHealthCardVal
						.setIdTypeCode(com.telus.credit.domain.CreditIDCard.HEALTH_CARE_NUMBER_KEY);
				if (creditIdentification.getHealthCard().getHealthCardNum() != null
						&& !creditIdentification.getHealthCard()
								.getHealthCardNum().trim().equals("")) {
					creditIDCardgetHealthCardVal
							.setIdNumber(creditIdentification.getHealthCard()
									.getHealthCardNum());
					creditIDCardgetHealthCardVal
							.setProvinceCode(creditIdentification
									.getHealthCard().getProvinceCd());
					creditIDCardList.add(creditIDCardgetHealthCardVal);
				}
			}
			if (creditIdentification.getPassport() != null) {
				com.telus.credit.domain.CreditIDCard creditIDCardgetHealthCardVal = new com.telus.credit.domain.CreditIDCard();
				creditIDCardgetHealthCardVal
						.setCreditProfileId(aConsumerCreditProfile
								.getCreditProfileID());
				if (creditIdentification.getPassport().getPassportNum() != null
						&& !creditIdentification.getPassport().getPassportNum()
								.trim().equals("")) {
					creditIDCardgetHealthCardVal
							.setIdTypeCode(com.telus.credit.domain.CreditIDCard.PASSPORT_NUMBER_KEY);
					creditIDCardgetHealthCardVal
							.setIdNumber(creditIdentification.getPassport()
									.getPassportNum());
					creditIDCardgetHealthCardVal
							.setCountryCode(creditIdentification.getPassport()
									.getCountryCd());
					creditIDCardList.add(creditIDCardgetHealthCardVal);
				}
			}
			if (creditIdentification.getProvincialIdCard() != null) {
				com.telus.credit.domain.CreditIDCard creditIDCardgetHealthCardVal = new com.telus.credit.domain.CreditIDCard();
				creditIDCardgetHealthCardVal
						.setCreditProfileId(aConsumerCreditProfile
								.getCreditProfileID());
				if (creditIdentification.getProvincialIdCard()
						.getProvincialIdNum() != null
						&& !creditIdentification.getProvincialIdCard()
								.getProvincialIdNum().trim().equals("")) {
					creditIDCardgetHealthCardVal
							.setIdTypeCode(com.telus.credit.domain.CreditIDCard.PROVINCIAL_ID_KEY);
					creditIDCardgetHealthCardVal
							.setIdNumber(creditIdentification
									.getProvincialIdCard().getProvincialIdNum());
					creditIDCardgetHealthCardVal
							.setProvinceCode(creditIdentification
									.getProvincialIdCard().getProvinceCd());
					creditIDCardList.add(creditIDCardgetHealthCardVal);
				}
			}

			// add all creditIDCardList to creditProfileVal
			com.telus.credit.domain.CreditIDCard[] creditIDCards = creditIDCardList
					.toArray(new com.telus.credit.domain.CreditIDCard[creditIDCardList
							.size()]);
			creditProfileVal.setCreditIDCards(creditIDCards);
		}
	  }
	}

	static public void validateAuditInfo(AuditInfo auditInfo)
			throws EnterpriseCreditAssessmentPolicyException {
		String userid = (auditInfo == null || auditInfo.getUserId() == null) ? ""
				: auditInfo.getUserId().trim();

		if (EnterpriseCreditAssessmentConsts.EMPTY_STR.equalsIgnoreCase(userid)) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.AUDITINFO_USERID_VALIDATION_EXCEPTION_STR
							+ " : userid= <" + userid + ">",
					EnterpriseCreditAssessmentExceptionCodes.AUDITINFO_USERID_VALIDATION_EXCEPTION);
		}

		String originatorApplicationId = (auditInfo == null || auditInfo
				.getOriginatorApplicationId() == null) ? "" : auditInfo
				.getOriginatorApplicationId().trim();
		if (!originatorApplicationId
				.equals(EnterpriseCreditAssessmentConsts.EMPTY_STR)) {
			Long originatorApplicationIdNum = new Long(0);
			try {
				originatorApplicationIdNum = new Long(originatorApplicationId);
			} catch (Throwable e) {
				throw new EnterpriseCreditAssessmentPolicyException(
						EnterpriseCreditAssessmentExceptionCodes.AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION_STR
								+ " Must be a Number. : originatorApplicationId= <"
								+ originatorApplicationId,
						EnterpriseCreditAssessmentExceptionCodes.AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION);
			}
			if (originatorApplicationIdNum == 0) {
				throw new EnterpriseCreditAssessmentPolicyException(
						EnterpriseCreditAssessmentExceptionCodes.AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION_STR
								+ "  Must be a Number. : originatorApplicationId= <"
								+ originatorApplicationId + ">",
						EnterpriseCreditAssessmentExceptionCodes.AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION);
			}
		}
	}

	static public void validateGetCreditAssessment(long creditAssessmentID,
			AuditInfo auditInfo)
			throws EnterpriseCreditAssessmentPolicyException {

		try {
			creditAssessmentID = new Long(creditAssessmentID);
		} catch (Throwable e) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_ASSESSMENT_ID_STR
							+ "  : creditAssessmentID= <"
							+ creditAssessmentID
							+ ">",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_ASSESSMENT_ID);
		}

		if (creditAssessmentID == 0) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_ASSESSMENT_ID_STR
							+ "  : creditAssessmentID= <"
							+ creditAssessmentID
							+ ">",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_ASSESSMENT_ID);
		}

		validateAuditInfo(auditInfo);

	}

	static public void validateGetCreditBureauDocument(
			String creditBureauDocumentID, AuditInfo auditInfo)
			throws EnterpriseCreditAssessmentPolicyException {
		creditBureauDocumentID = (creditBureauDocumentID == null) ? ""
				: creditBureauDocumentID.trim();
		Long creditBureauDocumentIDNum = new Long(0);
		try {
			creditBureauDocumentIDNum = new Long(creditBureauDocumentID);
		} catch (Throwable e) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_BUREAU_DOCUMENT_ID_STR
							+ "  : creditBureauDocumentIDNum= <"
							+ creditBureauDocumentIDNum + ">",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_BUREAU_DOCUMENT_ID);
		}
		if (creditBureauDocumentIDNum == 0) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_BUREAU_DOCUMENT_ID_STR
							+ "  : creditBureauDocumentIDNum= <"
							+ creditBureauDocumentIDNum + ">",
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CREDIT_BUREAU_DOCUMENT_ID);
		}
		validateAuditInfo(auditInfo);

	}

	static public void validateSearchCreditAssessments(
			SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria,
			AuditInfo auditInfo)
			throws EnterpriseCreditAssessmentPolicyException,
			EnterpriseCreditAssessmentServiceException {

		// optional but if provided : is todate after fromdate ?
		Date assesssmentFromDate = searchCreditAssessmentsRequestCriteria.getAssesssmentFromDate();
		Date assesssmentToDate = searchCreditAssessmentsRequestCriteria
				.getAssesssmentToDate();
		// Verify assesssmentToDate is greater than assesssmentFromDate
		if ((assesssmentFromDate != null && assesssmentToDate != null)
				&& assesssmentToDate.compareTo(assesssmentFromDate) != DatatypeConstants.GREATER) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.INVALID_SEARCH_ASSESSMENT_TO_FROM_DATE_STR
							+ "  : assesssmentFromDate= <"
							+ assesssmentFromDate + ">",
					EnterpriseCreditAssessmentExceptionCodes.INVALID_SEARCH_ASSESSMENT_TO_FROM_DATE);

		}
		// validate customer ID
		Long customerId = searchCreditAssessmentsRequestCriteria.getCustomerID();
		if (customerId == 0) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CUSTOMER_ID_STR,
					EnterpriseCreditAssessmentExceptionCodes.MISSING_CUSTOMER_ID);
		}

		List<ReferencePDSValidateUnit> validationArray = new ArrayList<ReferencePDSValidateUnit>();
		validationArray
				.add(new ReferencePDSValidateUnit(
						searchCreditAssessmentsRequestCriteria
								.getCreditAssessmentTypeCd(),
						EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_TYP,
						false, // not required for search
						"Credit Assessment Type",
						EnterpriseCreditAssessmentExceptionCodes.ASMT_TYPE_VALIDATION_EXCEPTION));

		validationArray
				.add(new ReferencePDSValidateUnit(
						searchCreditAssessmentsRequestCriteria
								.getCreditAssessmentSubTypeCd(),
						EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_SUB_TYP,
						false, // not required for search
						"Credit Assessment Sub Type",
						EnterpriseCreditAssessmentExceptionCodes.ASMT_SUBTYPE_VALIDATION_EXCEPTION));

		validationArray
				.add(new ReferencePDSValidateUnit(
						searchCreditAssessmentsRequestCriteria
								.getCreditAssessmentStatus(),
						EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_STATUS,
						false, // not required for search
						"Credit Assessment Status",
						EnterpriseCreditAssessmentExceptionCodes.CREDIT_ASSESSMENT_STATUS_VALIDATION_EXCEPTION));

		refPds_Validate(validationArray);

		validateAuditInfo(auditInfo);
	}

	static public void validatePerformCreditAssessmentInput(
			CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo)
			throws EnterpriseCreditAssessmentPolicyException,
			EnterpriseCreditAssessmentServiceException {

		CrdaValidationUtility.validateCommentByAsmtTypeSubtype(creditAssessmentRequest);

		List<ReferencePDSValidateUnit> validationArray = new ArrayList<ReferencePDSValidateUnit>();

		String creditAssessmentTypeCd = (creditAssessmentRequest == null || creditAssessmentRequest
				.getCreditAssessmentTypeCd() == null) ? ""
				: creditAssessmentRequest.getCreditAssessmentTypeCd().trim();
		validationArray
				.add(new ReferencePDSValidateUnit(
						creditAssessmentTypeCd,
						EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_TYP,
						true,
						"Credit Assessment Type",
						EnterpriseCreditAssessmentExceptionCodes.ASMT_TYPE_VALIDATION_EXCEPTION));

		String creditAssessmentSubTypeCd = (creditAssessmentRequest == null || creditAssessmentRequest
				.getCreditAssessmentSubTypeCd() == null) ? ""
				: creditAssessmentRequest.getCreditAssessmentSubTypeCd().trim();
		validationArray
				.add(new ReferencePDSValidateUnit(
						creditAssessmentSubTypeCd,
						EnterpriseCreditAssessmentConsts.REF_PDS_T_CAR_SUB_TYP,
						true,
						"Credit Assessment Sub Type",
						EnterpriseCreditAssessmentExceptionCodes.ASMT_SUBTYPE_VALIDATION_EXCEPTION));

		String lineOfBusiness = (creditAssessmentRequest == null || creditAssessmentRequest
				.getLineOfBusiness() == null) ? "" : creditAssessmentRequest
				.getLineOfBusiness().trim();
		validationArray
				.add(new ReferencePDSValidateUnit(
						lineOfBusiness,
						EnterpriseCreditAssessmentConsts.REF_PDS_T_CREDIT_LOB,
						true, // assuming it is not required
						"Line of Business",
						EnterpriseCreditAssessmentExceptionCodes.INVALID_LINE_OF_BUSINESS));

		//refPds_Validate(validationArray);

		String applicationId = (creditAssessmentRequest == null || creditAssessmentRequest
				.getApplicationID() == null) ? "" : creditAssessmentRequest
				.getApplicationID().trim();
		Long applicationIdNum = new Long(0);
		try {
			applicationIdNum = new Long(applicationId);
		} catch (Throwable e) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.APPLICATION_ID_VALIDATION_EXCEPTION_STR
							+ "  : Must be a Number. applicationId= <"
							+ applicationId + ">",
					EnterpriseCreditAssessmentExceptionCodes.APPLICATION_ID_VALIDATION_EXCEPTION);
		}
		if (applicationIdNum == 0) {
			throw new EnterpriseCreditAssessmentPolicyException(
					EnterpriseCreditAssessmentExceptionCodes.APPLICATION_ID_VALIDATION_EXCEPTION_STR
							+ "  : Missing or is not a Number. applicationId= <"
							+ applicationId + ">",
					EnterpriseCreditAssessmentExceptionCodes.APPLICATION_ID_VALIDATION_EXCEPTION);
		}

		CrdaValidationUtility.validateAuditInfo(auditInfo);
	}

	static public void refPds_Validate(
			List<ReferencePDSValidateUnit> validationArray)
			throws EnterpriseCreditAssessmentPolicyException,
			EnterpriseCreditAssessmentServiceException {
		String referenceDecodeStr = "";
		String fieldName = "";
		String fieldNameValue = "";
		Collection<ReferenceDecode> validCodeList = new ArrayList<ReferenceDecode>();
		try {
			for (ReferencePDSValidateUnit refUnit : validationArray) {
				fieldName = refUnit.getFieldName();
				fieldNameValue = refUnit.getCodeValue();
				validCodeList = ReferencePDSUtils.instanceOf().getValidCode(
						refUnit.getRefTable());
				ReferencePDSUtils.instanceOf().validate(refUnit);
			}
		} catch (EnterpriseCreditAssessmentPolicyException e) {
			for (Iterator<ReferenceDecode> iterator = validCodeList.iterator(); iterator
					.hasNext();) {
				ReferenceDecode referenceDecode = (ReferenceDecode) iterator
						.next();
				referenceDecodeStr = referenceDecodeStr
						+ referenceDecode.getCode();
				if (iterator.hasNext())
					referenceDecodeStr = referenceDecodeStr + ",";
			}
			throw new EnterpriseCreditAssessmentPolicyException(
					"fieldName:" + fieldName + " , fieldNameValue:"
							+ fieldNameValue + " : Valid values ["
							+ referenceDecodeStr + "]",
					EnterpriseCreditAssessmentExceptionCodes.REFPDS_FAILED_EXCEPTION);
		} catch (java.lang.RuntimeException e) {
			throw new EnterpriseCreditAssessmentServiceException(
					e.getMessage(),
					EnterpriseCreditAssessmentExceptionCodes.REFPDS_FAILED_EXCEPTION);
		} catch (Throwable e) {
			throw new EnterpriseCreditAssessmentServiceException(
					EnterpriseCreditAssessmentExceptionCodes.REFPDS_FAILED_EXCEPTION_STR,
					EnterpriseCreditAssessmentExceptionCodes.REFPDS_FAILED_EXCEPTION);
		}
	}
	static public  void validate_new_CrdValue_FraudInd(
			CreditAssessmentRequest aCreditAssessmentRequest,
			OverrideCreditAssessmentRequest overrideCreditAssessmentRequest)
			throws EnterpriseCreditAssessmentPolicyException {
		List<ReferencePDSValidateUnit> validationArray = new ArrayList<ReferencePDSValidateUnit>();

        String newCreditValueCd = (
                overrideCreditAssessmentRequest == null ||
                        overrideCreditAssessmentRequest.getNewCreditValueCd() == null) ?
                EnterpriseCreditAssessmentConsts.EMPTY_STR : overrideCreditAssessmentRequest.getNewCreditValueCd().trim();

        //validate the value of newCreditValueCd       
        if (!newCreditValueCd.equals("")) {
	        validationArray.add(new ReferencePDSValidateUnit(
	                newCreditValueCd,//codeValue
	                EnterpriseCreditAssessmentConsts.REF_PDS_T_CREDIT_VALUE_CODE,// REF PDS entity (refTable)
	                false,//required
	                "newCreditValueCd",//Description (fieldName)
	                EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_VALUE_CODE//exceptionCode
	        ));
        }

        
        String newFraudIndictorCd = (
                overrideCreditAssessmentRequest == null ||
                        overrideCreditAssessmentRequest.getNewFraudIndicatorCd() == null) ? EnterpriseCreditAssessmentConsts.EMPTY_STR : overrideCreditAssessmentRequest.getNewFraudIndicatorCd().trim();
      //validate the value of newCreditValueCd  
        if (!newFraudIndictorCd.equals("")) {
            validationArray.add(new ReferencePDSValidateUnit(
                    newFraudIndictorCd,//codeValue
                    EnterpriseCreditAssessmentConsts.REF_PDS_T_CREDIT_FRAUD_IND,// REF PDS entity (refTable)
                    false,//required
                    "newFraudIndictorCd",//Description (fieldName)
                    EnterpriseCreditAssessmentExceptionCodes.INVALID_CREDIT_FRAUD_IND//exceptionCode
            ));
        }
        ReferencePDSUtils.instanceOf().validate(validationArray);
	}
}
