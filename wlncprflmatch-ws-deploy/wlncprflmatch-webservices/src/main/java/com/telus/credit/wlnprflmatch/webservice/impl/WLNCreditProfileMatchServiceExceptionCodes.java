package com.telus.credit.wlnprflmatch.webservice.impl;
/**
 * @author x158788
 *
 */
public class WLNCreditProfileMatchServiceExceptionCodes {
	
	public final static String EN_LOCAL="EN";
	public final static String EMPTY_USERID_OR_APPID="WLNP-GEN-006";
	public final static String UNKNOWN_EXCEPTION="UNKNOWN_EXCEPTION";
	public final static String UNKNOWN_EXCEPTION_MSG ="Unchecked Exception encountered in checkCreditProfileByCreditId Method, Please check log for details";
	
	public final static String FAILED_TO_RETRIEVE_CREDIT_PROFILE="FAILED_TO_RETRIEVE_CREDIT_PROFILE";
	public final static String FAILED_TO_RETRIEVE_CREDIT_PROFILE_MSG="Credit profile entry is not present in the system for given Credit Identification field/field's";
	
	public final static String NO_CREDIT_SEARCH_DATA="NO_CREDIT_SEARCH_DATA";
	public final static String MATCHED_CREDIT_PROFILE_IS_NOT_FOUND="MATCHED_CREDIT_PROFILE_IS_NOT_FOUND";
	public final static String INCORRECT_REQUEST_FORMAT="INCORRECT_REQUEST_FORMAT";
	public final static String INCORRECT_REQUEST_FORMAT_MSG="Missing Mandatory Request Parameters.";
	
	public final static String AUDIT_INFO_VALIDATION_ERROR_MSG ="Audit Info: UserID and Source application Id can NOT be NULL";
	public final static String CREDIT_INFO_VALIDATION_ERROR_MSG ="Credit Identification :All credit identification fields are blank";
	public final static String PING_SUCCESS_MSG ="WLNCreditProfileMatchService 1.0 has been shaken down successfully";
}
