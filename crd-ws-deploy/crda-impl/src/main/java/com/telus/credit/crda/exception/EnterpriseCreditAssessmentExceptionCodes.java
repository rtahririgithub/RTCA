/*
 *  Copyright (c) 2012 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.crda.exception;

/**
 * <p><b>Description :</b><class>EnterpriseCreditAssessmentExceptionCodes</class> defines the possible error codes for EnterpriseCreditAssessment Service and Application Exception</p>
 * <p><b>Design Observations : </b></p>
 * <ul>
 * <li>None</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">13-Sep-2012</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">New Class</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 *
 * @author Gurbirinder Sidhu
 * @version 1.0
 * @stereotype Constants
 */
public class EnterpriseCreditAssessmentExceptionCodes {
    //
    // Message Format should be: <application-id>-<type>-<code>
    //where application id is CRDA
    //where type is DOC(Documentum), GEN(General), UNK( Unknow), DAT (Data (validation,...), SYS( system ), AUT(Authentication), WAR(Warning)

	
//WARNING CODES
	   public static final String RESILIENCE_WARNING_CODE = "CRDA-WAR-001";
	   public static final String RESILIENCE_WARNING_MSG = "RESILLENCE SOLUTION WAS APPLIED.";

	   
	   public static final String FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_CODE= "CRDA-WAR-002";
	   public static final String FICO_INVALID_ASSESSMENT_RESULTCD_OR_REASONCD_MSG 	= "FICO INVALID ASSESSMENT_RESULTCD OR REASONCD.";	   
	   
	   public static final String FICO_RESULT_VALIDATION_WARNING_CODE 	= "CRDA-WAR-003";
	   public static final String FICO_RESULT_VALIDATION_WARNING_MSG 	= "FICO RESULT VALIDATION FAILED.";	 	   
//SYS ERROR CODES

    public static final String MISSING_DB_ID_INTERNALCREDITDECISIONTRX = "CRDA-SYS-001";
    public static final String MISSING_DB_ID_INTERNALCREDITDECISIONTRX_STR = "MISSING_DB_ID_INTERNALCREDITDECISIONTRX";


    public static final String MISSING_DB_ID_CARID = "CRDA-SYS-002";
    public static final String MISSING_DB_ID_CARID_STR = "MISSING_DB_ID_CARID";


    public static final String ERROR_LOADING_SPRING = "CRDA-SYS-003";
    public static final String ERROR_DAO_INIT = "CRDA-SYS-004";
    
    public static final String REFPDS_FAILED_EXCEPTION = "CRDA-SYS-005";
    public static final String REFPDS_FAILED_EXCEPTION_STR = "REFPDS_FAILED_EXCEPTION";
        
    
    public static final String INVALIDA_REQUEST_SCHEMA_TYPE = "CRDA-SYS-006"; 
    public static final String INVALIDA_REQUEST_SCHEMA_TYPE_STR = "INVALIDA_REQUEST_SCHEMA_TYPE";    
    //************
    public static final String DOCUMENT_COULD_NOT_BE_RETRIEVED = "CRDA-DOC-001";
    public static final String DOCUMENT_OBJECT_NOT_FOUND = "CRDA-DOC-002";
    public static final String DOCUMENT_ENCRYPTION_ERROR = "CRDA-DOC-003";
    public static final String DOCUMENT_RUNTIME_EXCEPTION = "CRDA-DOC-004";

    public static final String OBJECT_NOT_FOUND_EXCEPTION = "CRDA-GEN-001";
    public static final String DUPLICATE_KEY_EXCEPTION = "CRDA-GEN-002";
    public static final String PERSISTENT_EXCEPTION = "CRDA-GEN-003";
    public static final String CONCURRENCY_EXCEPTION = "CRDA-GEN-004";

    public static final String REFPDS_VALIDATION_FAILED_EXCEPTION = "CRDA-GEN-005";
   
    public static final String GENERAL_MISING_MANDATORY_FIELD_EXCEPTION = "CRDA-GEN-006";
    public static final String SQLException_EXCEPTION = "CRDA-GEN-007";
    public static final String InternalErrorException_EXCEPTION = "CRDA-GEN-008";
    public static final String DatatypeConfigurationException_EXCEPTION = "CRDA-GEN-009";

    public static final String UNKNOWN_EXCEPTION = "CRDA-UNK-001";

    // Assessment type/subtype
    public static final String ASMT_TYPE_VALIDATION_EXCEPTION = "CRDA-DAT-001";
    public static final String ASMT_TYPE_VALIDATION_EXCEPTION_STR = "ASMT_TYPE_VALIDATION_EXCEPTION";

    public static final String ASMT_SUBTYPE_VALIDATION_EXCEPTION = "CRDA-DAT-002";
    public static final String ASMT_SUBTYPE_VALIDATION_EXCEPTION_STR = "ASMT_SUBTYPE_VALIDATION_EXCEPTION";

    //GEN 
    public static final String MISSING_CUSTOMER_ID = "CRDA-DAT-003";
    public static final String MISSING_CUSTOMER_ID_STR = "MISSING_CUSTOMER_ID";

    public static final String MISSING_COMMENTTXT = "CRDA-DAT-004";
    public static final String MISSING_COMMENTTXT_STR = "MISSING_COMMENT";


    public static final String MISSING_CREDIT_CUSTOMER_INFO = "CRDA-DAT-005";
    public static final String MISSING_CREDIT_CUSTOMER_INFO_STR = "MISSING_CREDIT_CUSTOMER_INFO";


    public static final String MISSING_CREDIT_PROFILE_DATA = "CRDA-DAT-006";
    public static final String MISSING_CREDIT_PROFILE_DATA_STR = "MISSING_CREDIT_PROFILE_DATA";

    // Audit info 
    public static final String AUDITINFO_USERID_VALIDATION_EXCEPTION = "CRDA-DAT-007";
    public static final String AUDITINFO_USERID_VALIDATION_EXCEPTION_STR = "AUDITINFO_USERID_VALIDATION_EXCEPTION";


    //override
    public static final String INVALID_OVERRIDE_ASSESSMENT_REQUEST = "CRDA-DAT-008";
    public static final String INVALID_OVERRIDE_ASSESSMENT_REQUEST_STR = "INVALID_OVERRIDE_ASSESSMENT_REQUEST";


    //credit asmt id 
    public static final String MISSING_CREDIT_ASSESSMENT_ID = "CRDA-DAT-009";
    public static final String MISSING_CREDIT_ASSESSMENT_ID_STR = "MISSING_CREDIT_ASSESSMENT_ID";

    //credit asmt STATUS
    public static final String CREDIT_ASSESSMENT_STATUS_VALIDATION_EXCEPTION = "CRDA-DAT-010";
    public static final String CREDIT_ASSESSMENT_STATUS_EXCEPTION_STR = "INVALID_CREDIT_ASSESSMENT_STATUS";

    //search asmt to , from dates
    public static final String INVALID_SEARCH_ASSESSMENT_TO_FROM_DATE = "CRDA-DAT-011";
    public static final String INVALID_SEARCH_ASSESSMENT_TO_FROM_DATE_STR = "INVALID_SEARCH_ASSESSMENT_TO_FROM_DATE";


    //MISSING_VOID_REASON_CODE
    public static final String MISSING_VOID_REASON_CODE = "CRDA-DAT-012";
    public static final String MISSING_VOID_REASON_CODE_STR = "MISSING_VOID_REASON_CODE";

    //MISSING_CREDIT_BUREAU_DOCUMENT_ID
    public static final String MISSING_CREDIT_BUREAU_DOCUMENT_ID = "CRDA-DAT-013";
    public static final String MISSING_CREDIT_BUREAU_DOCUMENT_ID_STR = "MISSING_CREDIT_BUREAU_DOCUMENT_ID";

    public static final String AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION = "CRDA-DAT-014";
    public static final String AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION_STR = "AUDITINFO_ORIGNATORAPPLICATION_ID_VALIDATION_EXCEPTION";


    public static final String APPLICATION_ID_VALIDATION_EXCEPTION = "CRDA-DAT-015";
    public static final String APPLICATION_ID_VALIDATION_EXCEPTION_STR = "APPLICATION_ID_VALIDATION_EXCEPTION";

    public static final String INVALID_LINE_OF_BUSINESS = "CRDA-DAT-016";
    public static final String INVALID_LINE_OF_BUSINESS_STR = "INVALID_LINE_OF_BUSINESS";

    public static final String INVALID_CREDIT_FRAUD_IND = "CRDA-DAT-017";
    
    public static final String INVALID_CREDIT_VALUE_CODE = "CRDA-DAT-018";
    public static final String INVALID_CREDIT_VALUE_CODE_STR = "INVALID_CREDIT_VALUE_CODE";

   
    
    public static final String INVALID_CREDIT_PROFILE = "CRDA-DAT-019";
    public static final String INVALID_CREDIT_PROFILE_STR = "INVALID_CREDIT_PROFILE";

    public static final String MISSING_CREDIT_PROFILE_ADDRESS = "CRDA-DAT-020";
    public static final String MISSING_CREDIT_PROFILE_ADDRESS_STR = "MISSING_CREDIT_PROFILE_ADDRESS";

    public static final String MISSING_CREDIT_PROFILE_PERSONAL_INFO = "CRDA-DAT-021";
    public static final String MISSING_CREDIT_PROFILE_PERSONAL_INFO_STR = "MISSING_CREDIT_PROFILE_PERSONAL_INFO";


    public static final String MISSING_CREDIT_CUSTOMER_CREATION_DATE = "CRDA-DAT-022";
    public static final String MISSING_CREDIT_CUSTOMER_CREATION_DATE_STR = "MISSING_CREDIT_CUSTOMER_CREATION_DATE";

    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_ID = "CRDA-DAT-023";
    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_ID_STR = "MISSING_CREDIT_CUSTOMER_CUSTOMER_ID";

    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_MASTER_SOURCE_ID = "CRDA-DAT-024";
    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_MASTER_SOURCE_ID_STR = "MISSING_CREDIT_CUSTOMER_CUSTOMER_MASTER_SOURCE_ID";

    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_STATUS_CODE = "CRDA-DAT-025";
    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_STATUS_CODE_STR = "MISSING_CREDIT_CUSTOMER_CUSTOMER_STATUS_CODE";

    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_TYPE_CODE = "CRDA-DAT-026";
    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_TYPE_CODE_STR = "MISSING_CREDIT_CUSTOMER_CUSTOMER_TYPE_CODE";

    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_SUBTYPE_CODE = "CRDA-DAT-027";
    public static final String MISSING_CREDIT_CUSTOMER_CUSTOMER_SUBTYPE_CODE_STR = "MISSING_CREDIT_CUSTOMER_CUSTOMER_SUBTYPE_CODE";

    public static final String MISSING_CREDIT_CUSTOMER_PERSON_NAME = "CRDA-DAT-028";
    public static final String MISSING_CREDIT_CUSTOMER_PERSON_NAME_STR = "MISSING_CREDIT_CUSTOMER_PERSON_NAME";

    public static final String MISSING_CREDIT_CUSTOMER_PERSON_NAME_LASTNAME = "CRDA-DAT-029";
    public static final String MISSING_CREDIT_CUSTOMER_PERSON_NAME_LASTNAME_STR = "MISSING_CREDIT_CUSTOMER_PERSON_NAME_LASTNAME";

    public static final String MISSING_CREDIT_CUSTOMER_PERSON_NAME_FIRSTNAME = "CRDA-DAT-030";
    public static final String MISSING_CREDIT_CUSTOMER_PERSON_NAME_FIRSTNAME_STR = "MISSING_CREDIT_CUSTOMER_PERSON_NAME_FIRSTNAME";

    public static final String INVALID_CREDIT_CHECK_CONCENT_CODE = "CRDA-DAT-031";

    public static final String VOID_NOT_ALLOWED_FOR_THIS_CAR = "CRDA-DAT-032";


    public static final String MISSING_GET_BUREAU_DATA_ASMT_COMMENT = "CRDA-DAT-033";
    public static final String MISSING_GET_BUREAU_DATA_ASMT_COMMENT_STR = "MISSING_GET_BUREAU_DATA_ASMT_COMMENT";
    
    public static final String GENERAL_INVALID_DB_DATA = "CRDA-DAT-034";
    public static final String GENERAL_INVALID_DB_DATA_STR = "GENERAL_INVALID_DB_DATA";  
    
    public static final String INVALID_DECISION_CODE = "CRDA-DAT-35";
    public static final String INVALID_DECISION_CODE_STR = "INVALID_DECISION_CODE"; 
    
    public static final String MISSING_CREDIT_CUSTOMER = "CRDA-DAT-036";
    public static final String MISSING_CREDIT_CUSTOMER_STR = "MISSING_CREDIT_CUSTOMER";

    public static final String MISSING_CREDIT_PROFILE_ID = "CRDA-DAT-037";
    public static final String MISSING_CREDIT_PROFILE_ID_STR = "MISSING_CREDIT_PROFILE_ID";
   
    
    public static final String MISSING_CREDIT_WORTHINESS = "CRDA-DAT-038";
    public static final String MISSING_CREDIT_WORTHINESS_STR = "MISSING_CREDIT_WORTHINESS";

    public static final String MISSING_CREDIT_CRPRFL_STATUS_CODE= "CRDA-DAT-039";
    public static final String MISSING_CREDIT_CRPRFL_STATUS_CODE_STR = "MISSING_CREDIT_CRPRFL_STATUS_CODE";
    
    // Decisioning(FICO)
    public static final String DECISIONING_INVALID_ASMT_RESULT_CD_EXCEPTION = "CRDA-DEC-001";
    public static final String DECISIONING_INVALID_ASMT_RESULT_CD_EXCEPTION_STR = "DECISIONING_INVALID_ASMT_RESULT_CD_EXCEPTION";
    public static final String DECISIONING_INVALID_ASMT_REQUEST_CD_EXCEPTION = "CRDA-DEC-002";
    public static final String DECISIONING_INVALID_ASMT_REQUEST_CD_EXCEPTION_STR = "DECISIONING_INVALID_ASMT_REQUEST_EXCEPTION";
    public static final String RuleServicesException_EXCEPTION = "CRDA-DEC-003";
    
    public static final String DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION = "CRDA-DEC-004";
    public static final String DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION_STR = "DECISIONING_INVALID_ASSESSMENT_RESULT_REASON_CODE_EXCEPTION";
  
    

    //Asmt Factory 
    public static final String ASMT_FACTORY_INVALID_ASMT_EXCEPTION = "CRDA-CNF-001";
    public static final String ASMT_FACTORY_INVALID_ASMT_EXCEPTION_STR = "Invalid assessment type,subtype.";


    // Credit Gateway ( CGW)
    public static final String CREDIT_GATEWAY_INVALID_INPUT = "CRDA-CGW-001";

    public static final String CREDIT_GATEWAY_RESULT_EXCEPTION = "CRDA-CGW-002";
    public static final String CREDIT_GATEWAY_RESULT_EXCEPTION_STR = "CREDUT_GATEWAY_RESULT_EXCEPTION";
    public static final String CREDIT_GATEWAY_RUNTIME_EXCEPTION = "CRDA-CGW-003";
    public static final String CREDIT_GATEWAY_RUNTIME_EXCEPTION_STR = "CREDIT_GATEWAY_RUNTIME_EXCEPTION";//"Runtime Exception calling credit gateway:
	
	


}
