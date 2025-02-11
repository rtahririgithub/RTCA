/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */


package com.telus.credit.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;



/**
 * 
 * <p>
 * <b>Description: </b> Incapsulates business rules that form behavior of the
 * CreditProfile domain object.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>Utilizes Singleton design pattern.</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Roman Mikhailov
 *  
 */

public class CreditProfileBr
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    /**
     * Category name for Country:
     */
    public static final String COUNTRY = "COUNTRY_OVERSEAS";

    /**
     * Category name for States:
     * (eg. BC, ON)
     */
    public static final String STATE = "STATE";

    /**
     * Category name for Province:
     * (eg. BC, ON)
     */
    public static final String PROVINCE = "PROVINCE";
    
    /**
     * Category name for Province of Current Residency:
     * List of Canadian Provinces and Territories, and an additional code for OTR (Other). 
     * This table is derived from PROVINCE_STATE_1.
     */
    public static final String PROVINCE_3 = "PROVINCE_3";
    
    /**
     * Category name for Province and State.
     */
    public static final String PROVINCE_STATE = "PROVINCE_STATE";

    /**
     * Category name for Credit Profile type:
     * (eg. PRI, SEC)
     */
    public static final String CREDIT_PROFILE_TYPE = "CREDIT_PROFILE_TYPE";


    /**
     * Category name for Credit Profile Residency Status code:
     * (eg. EM, SE)
     */
    public static final String CREDIT_PROFILE_RESIDENCY_STATUS_CODE = "RESIDENCE_TYPE";


    /**
     * Category name for Credit Profile Primary Credit Card type:
     * (eg. VS, MC)
     */
    public static final String CREDIT_PROFILE_PRIMARY_CREDIT_CARD_TYPE = "CREDIT_CARD_TYPE";


    /**
     * Category name for Credit Profile Primary Credit Card type:
     * (eg. GAS, FI)
     */
    public static final String CREDIT_PROFILE_SECONDARY_CREDIT_CARD_TYPE = "SECONDARY_CREDIT_CARD_ISSUE_COMPANY_TYPE";


    /**
     * Category name for Credit Profile Legal Care code:
     * (eg. Y, N)
     */
    public static final String CREDIT_PROFILE_LEGAL_CARE_CODE = "YES_NO_NA";

    /**
     * Category name for Credit Profile Employment code:
     * (eg. EM, SE)
     */
    public static final String CREDIT_PROFILE_EMPLOYMENT_STATUS_CODE = "EMPLOYMENT_STATUS_CODE";

    /**
     * Category name for Credit Check Consent code:
     * (eg. Y, N)
     */
    public static final String CREDIT_CHECK_CONSENT_CODE = "YES_NO_NA";


    /**
     * Error message code to signal that Country is invalid.
     */
    public static final String COUNTRY_INVALID = "CreditProfileBr.COUNTRY_INVALID";

    /**
     * Error message code to signal that Country is not required.
     */
    public static final String COUNTRY_NOT_REQUIRED = "CreditProfileBr.COUNTRY_NOT_REQUIRED";


    /**
     * Error message code to signal that Province is invalid.
     */
    public static final String PROVINCE_INVALID = "CreditProfileBr.PROVINCE_INVALID";

    
    /**
     * Error message code to signal that Province is invalid.
     */
    public static final String PROVINCEOFCURRENTRESIDENCECD_INVALID = "CreditProfileBr.PROVINCEOFCURRENTRESIDENCECD_INVALID";   
    /**
     * Error message code to signal that Province or State is invalid.
     */
    public static final String PROVINCE_STATE_INVALID = "CreditProfileBr.PROVINCE_STATE_INVALID";

    /**
     * Error message code to signal that Province is not required.
     */
    public static final String PROVINCE_NOT_REQUIRED = "CreditProfileBr.PROVINCE_NOT_REQUIRED";


    /**
     * Error message code to signal that Credit Profile type is invalid.
     */
    public static final String CREDIT_PROFILE_TYPE_INVALID = "CreditProfileBr.CREDIT_PROFILE_TYPE_INVALID";


    /**
     * Error message code to signal that Credit Profile Residency Status code is invalid.
     */
    public static final String CREDIT_PROFILE_RESIDENCY_STATUS_CODE_INVALID = "CreditProfileBr.CREDIT_PROFILE_RESIDENCY_STATUS_CODE_INVALID";


    /**
     * Error message code to signal that Credit Profile Primary Credit Card type is invalid.
     */
    public static final String CREDIT_PROFILE_PRIMARY_CREDIT_CARD_TYPE_INVALID = "CreditProfileBr.CREDIT_PROFILE_PRIMARY_CREDIT_CARD_TYPE_INVALID";


    /**
     * Error message code to signal that Credit Profile Secondary Credit Card type is invalid.
     */
    public static final String CREDIT_PROFILE_SECONDARY_CREDIT_CARD_TYPE_INVALID = "CreditProfileBr.CREDIT_PROFILE_SECONDARY_CREDIT_CARD_TYPE_INVALID";


    /**
     * Error message code to signal that Credit Profile Legal Care code is invalid.
     */
    public static final String CREDIT_PROFILE_LEGAL_CARE_CODE_INVALID = "CreditProfileBr.CREDIT_PROFILE_LEGAL_CARE_CODE_INVALID";


    /**
     * Error message code to signal that Credit Check Consent code is invalid.
     */
    public static final String CREDIT_PROFILE_EMPLOYMENT_STATUS_CODE_INVALID = "CreditProfileBr.CREDIT_PROFILE_EMPLOYMENT_STATUS_CODE_INVALID";


    /**
     * Error message code to signal that Credit Check Consent code is invalid.
     */
    public static final String CREDIT_CHECK_CONSENT_CODE_INVALID = "CreditProfileBr.CREDIT_CHECK_CONSENT_CODE_INVALID";


    /**
     * Error message code to signal that ID Card is required with 
     * Guarantor information.
     */
    public static final String ID_CARD_IS_REQUIRED_WITH_GUARANTOR = "CreditProfileBr.ID_CARD_IS_REQUIRED_WITH_GUARANTOR";

    /**
     * Error message code to signal that 
     * Credit Value is required with Guarantor information.
     */
    public static final String CREDIT_VALUE_IS_REQUIRED_WITH_GUARANTOR = "CreditProfileBr.CREDIT_VALUE_IS_REQUIRED_WITH_GUARANTOR";

    /**
     * Error message code to signal that 
     * Credit Value G is required with Guarantor information.
     */
    public static final String CREDIT_VALUE_G_IS_REQUIRED_WITH_GUARANTOR = "CreditProfileBr.CREDIT_VALUE_G_IS_REQUIRED_WITH_GUARANTOR";

    /**
     * Error message code to signal that 
     * Birthdate is invalid.
     */
    public static final String BIRTHDATE_IS_INVALID = "CreditProfileBr.BIRTHDATE_IS_INVALID";


    /**
     * Error message code to signal that CreditValue object is null.
     */
    public static final String CREDIT_VALUE_IS_NULL = "CreditProfileBr.CREDIT_VALUE_IS_NULL";

    /**
     * Error message code to signal that creditProfileId for CreditAddress object is invalid.
     * (not equal to id of the parent CreditProfile object).
     */
    public static final String CREDIT_ADDRESS_CREDIT_PROFILE_ID_IS_INVALID = "CreditProfileBr.CREDIT_ADDRESS_CREDIT_PROFILE_ID_IS_INVALID";


    /**
     * Error message code to signal that creditProfileId for CreditValue object is invalid.
     * (not equal to id of the parent CreditProfile object).
     */
    public static final String CREDIT_VALUE_CREDIT_PROFILE_ID_IS_INVALID = "CreditProfileBr.CREDIT_VALUE_CREDIT_PROFILE_ID_IS_INVALID";

    /**
     * Error message code to signal that creditProfileId for CreditIDCard object is invalid.
     * (not equal to id of the parent CreditProfile object).
     */
    public static final String CREDIT_ID_CARD_CREDIT_PROFILE_ID_IS_INVALID = "CreditProfileBr.CREDIT_ID_CARD_CREDIT_PROFILE_ID_IS_INVALID";

    /**
     * Indicates that Credit Profile must have credit id information if Credit Value is not N ("No Credit Information")
     */
    public static final String NO_ID_CARDS_AND_CREDIT_VALUE_NOT_N_ERROR = "CreditProfileBr.NO_ID_CARDS_AND_CREDIT_VALUE_NOT_N_ERROR";
    
    /**
     * Indicates that Credit Profile must not have credit id information if Credit Value is N ("No Credit Information")
     */
    public static final String ID_CARDS_AND_CREDIT_VALUE_N_ERROR = "CreditProfileBr.ID_CARDS_AND_CREDIT_VALUE_N_ERROR";
    
    /**
     * Category name for Credit Profile statuses (A,C)
     */
    public static final String CREDIT_PROFILE_STATUS_CODE = "CREDIT_PROFILE_STATUS_CODE";
    
    /**
     * Signals that Credit Profile statuses is invalid.
     */
    public static final String CREDIT_PROFILE_STATUS_CODE_INVALID = "CreditProfileBr.CREDIT_PROFILE_STATUS_CODE_INVALID";
    
    /**
     * Category name for methods of population of Credit Management PDS (OO, BC, BP, etc)
     */
    public static final String CREDIT_PROFILE_POPULATE_METHOD_CODE = "POPULATE_METHOD_CODE";
    
    /**
     * Signals that Credit Management PDS population method is invalid.
     */
    public static final String CREDIT_PROFILE_POPULATE_METHOD_CODE_INVALID = "CreditProfileBr.CREDIT_PROFILE_POPULATE_METHOD_CODE_INVALID";
    
    /**
     * Category name for Credit Management format (P, O)
     */
    public static final String CREDIT_PROFILE_FORMAT_TYPE = "CREDIT_PROFILE_FORMAT_TYPE";
    
    /**
     * Signals that Credit Management format is invalid.
     */
    public static final String CREDIT_PROFILE_FORMAT_TYPE_INVALID = "CreditProfileBr.CREDIT_PROFILE_FORMAT_TYPE_INVALID";

    /**
     * Consumer service only support consumer credit profile
     */
	private static final String CREDIT_PROFILE_CONSUMER_FORMAT_CODE_REQUIRED = "CreditProfileBr.CREDIT_PROFILE_CONSUMER_FORMAT_CODE_REQUIRED";
    
    
    /**
     * Instance of CreditProfileBr
     */
    private static CreditProfileBr s_bRule;


    private CreditIDCardBr m_creditIDCardBr;



    /**
     * Constructor. 
     */
    public CreditProfileBr()
    {
        //do nothing here.
    }


    /**
     * <b>Description: </b> Returns single instance of this business rules class
     * @return CreditProfileBr
     */
    public static CreditProfileBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new CreditProfileBr();
        }
        return s_bRule;
    }


    /**
     * <b>Description: </b> Checks if the given Credit Profile has any
     * CreditIdCards eg. "SIN", "DL", etc.
     * 
     * @param creditProfile domain object.
     * @return true if this CreditProfile has id card(s) ("SIN", "DL", etc); false
     *         otherwise.
     */
    private boolean isCreditIdProvided(CreditProfile creditProfile)
    {
        if ( creditProfile.getCreditIDCards() == null
                || creditProfile.getCreditIDCards().length < 1 ) {
            return false;
        }
        return true;
    }


    /**
     * <p>
     * <b>Description: </b> Validates credit profile information during <b>creation</b>
     * of new credit profile. This method can be invoked either from Action or
     * from service operation itself.
     * <ol>
     * <li>If the CustomerGuarantor exists, then CreditId cards must also
     * be provided.  This is only required because of the logic to determine the
     * credit value - see logic for getting Default Credit Value.
     * </li> 
     * <li>If the CustomerGuarantor exists, all required fields int
     * the CustomerGuarantor domain object must be populated.</li>
     * </ol>
     * @param creditProfile
     *            domain object.
     * @return ValidationResult contains validation results.
     */
    public ValidationResult validate(CreditProfile creditProfile)
    {
        ValidationResult result = new ValidationResult();

        if ( creditProfile.getApplicationProvinceCd() != null
                && !isValidApplicationProvinceCd( creditProfile ) ) {
            result.put( new ValidationError( PROVINCE_INVALID + ". ApplicationProvinceCd =  <" + creditProfile.getApplicationProvinceCd() + ">")  );
        }
        if ( creditProfile.getProvinceOfCurrentResidenceCd() != null
                && !isValidProvinceOfCurrentResidenceCd( creditProfile ) ) {
            result.put( new ValidationError( PROVINCEOFCURRENTRESIDENCECD_INVALID + ". ProvinceOfCurrentResidenceCd =  <" + creditProfile.getProvinceOfCurrentResidenceCd() + ">")  );
        }
        
        if ( creditProfile.getBirthdate() != null
                && !isValidBirthdate( creditProfile ) ) {
            result.put( new ValidationError( BIRTHDATE_IS_INVALID + ". Birthdate =  <" + creditProfile.getBirthdate() + ">") );
        }
        if ( creditProfile.getCreditCheckConsentCode() != null
                && !isValidCreditCheckConsentCode( creditProfile ) ) {
            result.put( new ValidationError(
                            CREDIT_CHECK_CONSENT_CODE_INVALID + ". CreditCheckConsentCode =  <" + creditProfile.getCreditCheckConsentCode() + ">") );
        }

        if ( !isValidEmploymentStatusCode( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_EMPLOYMENT_STATUS_CODE_INVALID + ". EmploymentStatusCode =  <" + creditProfile.getEmploymentStatusCode() + ">") );
        }

        if ( !isValidResidencyStatusCode( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_RESIDENCY_STATUS_CODE_INVALID + ". ResidencyCode =  <" + creditProfile.getResidencyCode() + ">") );
        }

        if ( !isValidPrimaryCreditCardCode( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_PRIMARY_CREDIT_CARD_TYPE_INVALID + ". PrimaryCreditCardCode =  <" + creditProfile.getPrimaryCreditCardCode() + ">") );
        }

        if ( !isValidSecondaryCreditCardCode( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_SECONDARY_CREDIT_CARD_TYPE_INVALID + ". SecondaryCreditCardCode =  <" + creditProfile.getSecondaryCreditCardCode() + ">") );
        }

        if ( !isValidLegalCareCode( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_LEGAL_CARE_CODE_INVALID + ". UnderLegalCareCode =  <" + creditProfile.getUnderLegalCareCode() + ">") );
        }
        
        if ( !isConsumerCreditProfile( creditProfile ) ) {
        	result.put( new ValidationError( CREDIT_PROFILE_CONSUMER_FORMAT_CODE_REQUIRED + ". Format() =  <" + creditProfile.getFormat() + ">") );
        }

        CustomerGuarantor customerGuarantor = creditProfile
                .getCustomerGuarantor();
        
        /*
         * In RTCA1.0, Removed Validation rules below
         * 1, CreditId must be provided along with the CustomerGuarantor information
         * 2, CreditValue must be provided along with the CustomerGuarantor information, and must be of type "Guaranteed"
         */
        
        if ( customerGuarantor != null ) {
        	
        	// Guarantor is not null, so validate it.
            //
            CustomerGuarantorBr customerGuarantorBr = CustomerGuarantorBr
                    .getInstance();
            result.merge( customerGuarantorBr
                    .validateForCreate( customerGuarantor ) );
            
//            if ( !isCreditIdProvided( creditProfile ) ) {
//                //invalid combination, creditId must be provided along with a Guarantor
//                result.put( new ValidationError(
//                        ID_CARD_IS_REQUIRED_WITH_GUARANTOR ) );
//            }
//
//            // Guarantor is not null, so validate it.
//            //
//            CustomerGuarantorBr customerGuarantorBr = CustomerGuarantorBr
//                    .getInstance();
//            result.merge( customerGuarantorBr
//                    .validateForCreate( customerGuarantor ) );
//
//            if ( creditProfile.getCreditValue() == null ) {
//                //CreditValue must be provided along with the CustomerGuarantor information.
//                //
//                result.put( new ValidationError(
//                        CREDIT_VALUE_IS_REQUIRED_WITH_GUARANTOR ) );
//            }
//            else {
//                if ( !CreditValue.GUARANTEED_CREDIT_VALUE_KEY
//                        .equals( creditProfile.getCreditValue()
//                                .getCreditValueCode() ) ) {
//                    //CreditValue must be of type "Guaranteed".
//                    result.put( new ValidationError(
//                            CREDIT_VALUE_G_IS_REQUIRED_WITH_GUARANTOR ) );
//                }
//            }
        }

        //Merge validation results.
        //
        result
                .merge( validateCreditIDCards( creditProfile.getCreditIDCards() ) );

        //Validate Credit Value
        //
        CreditValueBr creditValueBr = CreditValueBr.getInstance();

        //Merge validation results.
        //
        if(creditProfile.getCreditValue()!=null){
            result.merge( creditValueBr.validate( creditProfile.getCreditValue() ) );
        }
        

        if ( creditProfile.getCreditAddress() != null ) {

            //Validate Credit Address.
            //
            CreditAddressBr creditAddressBr = CreditAddressBr.getInstance();

            //Merge validation results.
            //
            result.merge( creditAddressBr.validate( creditProfile
                    .getCreditAddress() ) );
        }
        /* enable after populating reference ods */
        
        //validate format (P,O).
        //
        if ( !isValidFormat( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_FORMAT_TYPE_INVALID ) );
        }
        
        //validate status (A, C)
        //
        if ( !isValidStatus( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_STATUS_CODE_INVALID ) );
        }
        
        //validate population method (OO, BC, ...)
        //
        if ( !this.isValidPopulationMethod( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_POPULATE_METHOD_CODE_INVALID ) );
        }
        /**/

        
        //validate format (P,O).
        //
        if ( !isValidFormat( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_FORMAT_TYPE_INVALID ) );
        }
        
        //validate status (A, C)
        //
        if ( !isValidStatus( creditProfile ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_STATUS_CODE_INVALID ) );
        }

        return result;
    }


    /**
     * Verify if format code is consumer
     * @param creditProfile
     * @return true if consumer credit profile false otherwise
     */
    private boolean isConsumerCreditProfile(CreditProfile creditProfile) {
		return  (( creditProfile.getFormat() != null && 
					creditProfile.getFormat().equals( CreditProfile.ORGANIZATION_KEY) ) ? false : true );
	}


	/**
     * <p><b>Description</b> Validates ID cards.</p>
     * @param creditIDCards array 
     * @return  ValidationResult that has zero or more ValidationError(s). 
     */
    public ValidationResult validateCreditIDCards(CreditIDCard[] creditIDCards)
    {

        ValidationResult result = new ValidationResult();

        if ( creditIDCards != null ) {
            CreditIDCardBr creditIDCardBr = getCreditIDCardBr();

            for ( int i = 0; i < creditIDCards.length; i++ ) {
                result.merge( creditIDCardBr.validate( creditIDCards[i] ) );
            }
        }
        return result;
    }


    /**
     * <p>
     * <b>Description: </b> Validates credit profile information during <b>update</b>
     * of new credit profile. This method can be invoked either from Action or
     * from service operation itself.
     * </p>
     * @param creditProfile domain object.
     * @return ValidationResult contains zero or more ValidationError(s).
     */
    public ValidationResult validateForUpdate(CreditProfile creditProfile)
    {
        ValidationResult result = validate( creditProfile );

        CreditAddress creditAddress = creditProfile.getCreditAddress();
        CreditIDCard[] idCards = creditProfile.getCreditIDCards();
        CreditValue creditValue = creditProfile.getCreditValue();

        if ( creditValue == null ) {
            result.put( new ValidationError( CREDIT_VALUE_IS_NULL ) );
        }
        else {
            CreditValueBr creditValueBr = CreditValueBr.getInstance();

            //Merge validation results.
            //
            result.merge( creditValueBr.validateForUpdate( creditProfile
                    .getCreditValue() ) );
        }

        if ( creditAddress != null
                && creditAddress.getCreditProfileId() != creditProfile.get_id() ) {
            result.put( new ValidationError(
                    CREDIT_ADDRESS_CREDIT_PROFILE_ID_IS_INVALID ) );
        }
        if ( creditValue != null
                && creditValue.getCreditProfileId() != creditProfile.get_id() ) {
            result.put( new ValidationError(
                    CREDIT_VALUE_CREDIT_PROFILE_ID_IS_INVALID ) );
        }
        if ( idCards != null && idCards.length > 0 ) {
            for ( int i = 0; i < idCards.length; i++ ) {
                if ( idCards[i].getCreditProfileId() != creditProfile.get_id() ) {
                    result.put( new ValidationError(
                            CREDIT_ID_CARD_CREDIT_PROFILE_ID_IS_INVALID ) );
                }
            }
        }
        
        //CR7496: move this validation to web, because we need to use differet
        //validation rules for create and update 
        /*
        if(hasNoIdCardsAndCreditValueNotN(creditProfile)){
            result.put( new ValidationError(
                    NO_ID_CARDS_AND_CREDIT_VALUE_NOT_N_ERROR ) );
        }
        */
        
        if(hasIdCardsAndCreditValueN(creditProfile)){
            result.put( new ValidationError(
                    ID_CARDS_AND_CREDIT_VALUE_N_ERROR ) );
        }
        
        return result;
    }


    
    /**
     * <b>Description: </b> Checks if Credit Profile has Credit Value other than 'N' AND no
     * credit id information.  
     * @param creditProfile domain object.
     * @return true if Credit Profile has Credit Value other than 'N' and no
     * 		credit id information; false otherwise.
     */
    //CR7496: change this method to public, to be used in web layer
    public boolean hasNoIdCardsAndCreditValueNotN(CreditProfile creditProfile) {
        assert creditProfile.getCreditValue()!=null : "CreditValue is null";
        if ( !StringUtils.equalsIgnoreCase(
                CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY, creditProfile
                        .getCreditValue().getCreditValueCode() )
                && ArrayUtils.isEmpty( creditProfile.getCreditIDCards() ) ) {
            return true;
        }
        return false;
    }
    
    
    /**
     * <b>Description: </b> Checks if Credit Profile has Credit Value 'N' AND
     * credit id information.  
     * @param creditProfile domain object.
     * @return true if Credit Profile has Credit Value 'N' and 
     * 		credit id information; false otherwise.
     */
    protected boolean hasIdCardsAndCreditValueN(CreditProfile creditProfile) {
        assert creditProfile.getCreditValue()!=null : "CreditValue is null";
        if ( StringUtils.equalsIgnoreCase(
                CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY, creditProfile
                        .getCreditValue().getCreditValueCode() )
                && !ArrayUtils.isEmpty( creditProfile.getCreditIDCards() ) ) {
            return true;
        }
        return false;
    }


    /**
     * <b>Description: </b> Checks if Credit Profile has primary
     * credit card information.  
     * @param creditProfile domain object.
     * @return true if the credit profile has primary credit card information;
     *         false otherwise.
     */
    private boolean hasValidPrimaryCreditCard(CreditProfile creditProfile)
    {
        if ( creditProfile.getPrimaryCreditCardCode() != null ) {
            return true;
        }
        return false;
    }
    
    /**
     * <b>Description: </b> Checks if Credit Profile has secondary
     * credit card information.  
     * @param creditProfile domain object.
     * @return true if the credit profile has secondary credit card information;
     *         false otherwise.
     */
    private boolean hasValidSecondaryCreditCard(CreditProfile creditProfile)
    {
        if ( creditProfile.getSecondaryCreditCardCode() != null ) {
            return true;
        }
        return false;
    }


    /**
     * Gets default credit value for newly created Credit Profile.
     * <ol>
     * <li>Default CreditValue = N</li>
     * <li>If CreditId is provided</li>
     * <ul>
     *  <li>If Guarantor is provided CreditValue = G</li>
     *  <li>else</li>
     *   <ul>
     *   <li>If PrimaryCreditCard provided - CreditValue = U</li>
     *   <li>Else CreditValue = D<li/>
     *  </ul>
     * </ul>
     * 
     * </ol>
     * 
     * @param creditProfile
     *            domain object.
     * @return String credit value code(eg. "D" - for Deposit CreditValue, etc)
     */
    public String getDefaultCreditValue(CreditProfile creditProfile)
    {
        CustomerGuarantor customerGuarantor = creditProfile
                .getCustomerGuarantor();
        String defaultCreditValue = CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY;
        if ( isCreditIdProvided( creditProfile ) ) {
            if ( customerGuarantor != null ) {
                defaultCreditValue = CreditValue.GUARANTEED_CREDIT_VALUE_KEY;
            }
            else {
            	//CR7993: The default of "Unestablished" will be presented when the following conditions are met:
            	//Two or more of the 5 hard match information is populated (SIN, DL, Passport #, Provincial Id, Health Care Card #)
            	//OR If only One of these are populated then one of credit cards must be selected as well.
                if ( hasValidPrimaryCreditCard( creditProfile ) || hasValidSecondaryCreditCard(creditProfile)
                								|| creditProfile.getCreditIDCards().length >= 2) {
                    defaultCreditValue = CreditValue.UN_ESTABLISHED_CREDIT_VALUE_KEY;
                }
                else {
                    defaultCreditValue = CreditValue.DEPOSIT_CREDIT_VALUE_KEY;
                }
            }
        }
        return defaultCreditValue;
    }


    /**
     * <p><b>Description</b> Checks the validity of CreditCheckConsentCode. 
     * The Customer consent is not mandatory until Phase 2. 
     * </p>
     * @param creditProfile
     * @return true if the code is valid or null; false otherwise.  
     */
    public boolean isValidCreditCheckConsentCode(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getCreditCheckConsentCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditProfile.getCreditCheckConsentCode(), CREDIT_CHECK_CONSENT_CODE ) ) {
            return false;
        }
        return true;
    }
    public boolean isValidProvinceOfCurrentResidenceCd(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getProvinceOfCurrentResidenceCd() == null ) {
            return false;
        }
        
        if ( "".equals(creditProfile.getProvinceOfCurrentResidenceCd())) 
          {
            return true;
            
         }else if ( !CreditMgtBrHelper.isValidCode( creditProfile.getProvinceOfCurrentResidenceCd(), PROVINCE_3 ) ) 
           {
            return false;
           }
        
        return true;
    }
    public boolean isValidApplicationProvinceCd(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getApplicationProvinceCd() == null ) {
            return false;
        }
        
        if ("".equals(creditProfile.getApplicationProvinceCd()))
        {
        	return true;
        }else if ( !CreditMgtBrHelper.isValidCode( creditProfile.getApplicationProvinceCd(), PROVINCE ) ) {
            return false;
        }
        return true;
    }
    /**
     * <p><b>Description</b> Checks the validity of EmploymentStatusCode. </p>
     * @param creditProfile
     * @return true if the code is valid or null; false otherwise.  
     */
    public boolean isValidEmploymentStatusCode(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getEmploymentStatusCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditProfile
                .getEmploymentStatusCode(),
                CREDIT_PROFILE_EMPLOYMENT_STATUS_CODE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Checks the validity of ResidencyStatusCode. </p>
     * @param creditProfile
     * @return true if the code is valid or null; false otherwise.  
     */
    public boolean isValidResidencyStatusCode(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getResidencyCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditProfile.getResidencyCode(),
                CREDIT_PROFILE_RESIDENCY_STATUS_CODE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Checks the validity of PrimaryCreditCardCode. </p>
     * @param creditProfile
     * @return true if the code is valid or null; false otherwise.  
     */
    public boolean isValidPrimaryCreditCardCode(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getPrimaryCreditCardCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditProfile
                .getPrimaryCreditCardCode(),
                CREDIT_PROFILE_PRIMARY_CREDIT_CARD_TYPE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Checks the validity of SecondaryCreditCardCode. </p>
     * @param creditProfile
     * @return true if the code is valid or null; false otherwise.  
     */
    public boolean isValidSecondaryCreditCardCode(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getSecondaryCreditCardCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditProfile
                .getSecondaryCreditCardCode(),
                CREDIT_PROFILE_SECONDARY_CREDIT_CARD_TYPE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Checks the validity of LegalCareCode. </p>
     * @param creditProfile
     * @return true if the code is valid or null; false otherwise.  
     */
    public boolean isValidLegalCareCode(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getUnderLegalCareCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditProfile.getUnderLegalCareCode(), CREDIT_PROFILE_LEGAL_CARE_CODE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Checks if any of CreditProfile children are empty objects.
     * Empty objects are objects that are not equal to null,
     * and whose data members are either null, 0 or empty.</p>
     * @param cp object of type CreditProfile.
     * @return true if any of the child objects is "empty"; false otherwise.  
     */
    public boolean hasEmptyChildObjects(CreditProfile cp)
    {
        assert cp != null : "CreditProfile is null";

        boolean result = false;

        CreditAddress creditAddress = cp.getCreditAddress();
        CreditIDCard[] idCards = cp.getCreditIDCards();
        CustomerGuarantor customerGuarantor = cp.getCustomerGuarantor();
        CreditValue creditValue = cp.getCreditValue();

        if ( creditAddress != null
                && CreditAddressBr.getInstance().isEmpty( creditAddress ) ) {
            result = true;
        }
        else if ( customerGuarantor != null
                && CustomerGuarantorBr.getInstance()
                        .isEmpty( customerGuarantor ) ) {
            result = true;
        }
        else if ( creditValue != null
                && CreditValueBr.getInstance().isEmpty( creditValue ) ) {
            result = true;
        }
        else {
            if ( idCards != null || !(idCards.length == 0) ) {
                A: for ( int i = 0; i < idCards.length; i++ ) {
                    if ( getCreditIDCardBr().isEmpty( idCards[i] ) ) {
                        result = true;
                        break A;
                    }
                }
            }
        }
        return result;
    }

    public ArrayList<String> getEmptyChildObjects(CreditProfile cp)
    {
        
        ArrayList<String> aEmptyList= new ArrayList<String>();
 
        CreditAddress creditAddress = cp.getCreditAddress();
        CreditIDCard[] idCards = cp.getCreditIDCards();
        CustomerGuarantor customerGuarantor = cp.getCustomerGuarantor();
        CreditValue creditValue = cp.getCreditValue();

        if ( creditAddress != null && CreditAddressBr.getInstance().isEmpty( creditAddress ) ) {
            aEmptyList.add("creditAddress");
        }
        else if ( customerGuarantor != null && CustomerGuarantorBr.getInstance().isEmpty( customerGuarantor ) ) {
            aEmptyList.add("customerGuarantor");
        }
        else if ( creditValue != null && CreditValueBr.getInstance().isEmpty( creditValue ) ) {
            aEmptyList.add("creditValue");
        }
        else {
            if ( idCards != null || !(idCards.length == 0) ) {
                A: for ( int i = 0; i < idCards.length; i++ ) {
                    if ( getCreditIDCardBr().isEmpty( idCards[i] ) ) {
                    	aEmptyList.add("idCards");
                        break A;
                    }
                }
            }
        }
        return aEmptyList;
    }
    
    /**
     * <p><b>Description</b> Checks the validity of Birthdate. </p>
     * @param cp of type creditProfile.
     * @return true if the current date is greater than birthdate; false otherwise.  
     */
    public boolean isValidBirthdate(CreditProfile cp)
    {
        assert cp.getBirthdate() != null : "Birthdate is null";

        Date now = Calendar.getInstance().getTime();
        if ( cp.getBirthdate().before( now ) ) {
            return true;
        }
        return false;
    }


    public void setCreditIDCardBr(CreditIDCardBr br)
    {
        m_creditIDCardBr = br;
    }


    public CreditIDCardBr getCreditIDCardBr()
    {
        CreditIDCardBr br = null;
        if ( m_creditIDCardBr != null ) {
            br = m_creditIDCardBr;
        }
        else {
            br = CreditIDCardBr.getInstance();
        }
        return br;
    }
    
    /**
     * <p><b>Description</b> Checks the validity of Credit Profile format. </p>
     * @param creditProfile
     * @return true if the format is valid; false otherwise.  
     */
    public boolean isValidFormat(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if  (creditProfile.getFormat() == null)
        	return true;
        if ( !CreditMgtBrHelper.isValidCode( creditProfile
                .getFormat(),
                CREDIT_PROFILE_FORMAT_TYPE ) ) {
            return false;
        }
        return true;
    }
    
    /**
     * <p><b>Description</b> Checks the validity of Credit Profile status. </p>
     * @param creditProfile
     * @return true if status is valid; false otherwise.  
     */
    public boolean isValidStatus(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getStatus() == null)
        	return true;
        if ( !CreditMgtBrHelper.isValidCode( creditProfile
                .getStatus(),
                CREDIT_PROFILE_STATUS_CODE ) ) {
            return false;
        }
        return true;
    }
    
    /**
     * <p><b>Description</b> Checks the validity of Credit Profile population method. </p>
     * @param creditProfile
     * @return true if method is valid; false otherwise.  
     */
    public boolean isValidPopulationMethod(CreditProfile creditProfile)
    {
        assert creditProfile != null : "CreditProfile is null";

        if ( creditProfile.getMethod() == null)
        	return true;
        if ( !CreditMgtBrHelper.isValidCode( creditProfile
                .getMethod(),
                CREDIT_PROFILE_POPULATE_METHOD_CODE ) ) {
            return false;
        }
        return true;
    }

}
