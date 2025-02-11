package com.telus.credit.wlnprflmgt.webservice.impl;

public class WLNCreditProfileManagementServiceExceptionCodes {
	
	public final static String EMPTY_CPDATA_OR_CUSTOMERID = "WLNP-GEN-001";
	
	public final static String EMPTY_CP_CHILD = "WLNP-GEN-002";

	public final static String CP_VALIDATION_ERROR= "WLNP-GEN-003";

	public final static String CP_EXIST="WLNP-GEN-004";
	
	public final static String INVALID_GUARANTOR="WLNP-GEN-005";
	
	public final static String EMPTY_USERID_OR_APPID="WLNP-GEN-006";
	
	public final static String FAILED_TO_RETRIEVE_LAST_UPDATE_TIMESTAMP="WLNP-GEN-007";
	
	public final static String FAILED_TO_INSERT_CREDIT_PROFILE="WLNP-GEN-008";
	
	public final static String FAILED_TO_CREATE_PRIMARY_LINK="WLNP-GEN-009";
	
	public final static String FAILED_TO_INSERT_GUARANTOR_RECORD="WLNP-GEN-010";
	
	public final static String UNKNOWN_EXCEPTION="WLNP-UNK-001";
	
	public final static String FAILED_TO_CREATE_SECONDARY_LINKS="WLNP-GEN-011";
	
	public final static String FAILED_TO_RETRIEVE_CREDIT_PROFILE="WLNP-GEN-012";
	
	public final static String NO_CREDIT_SEARCH_DATA="WLNP-GEN-013";
	
	public final static String INSUFFICIENT_DATA="WLNP-GEN-014";
	
	public final static String DB_EXCEPTION="WLNP-GEN-015";
	
	public final static String NOT_A_MERGED_CREDIT_PROFILE="WLNP-GEN-016";
	
	public final static String CIC_VALIDATION_ERROR="WLNP-GEN-017";
	
	public final static String MATCHED_CREDIT_PROFILE_IS_NOT_FOUND="WLNP-GEN-018";
	
	public final static String GUARANTOR_SAME_AS_GUARANTEED_CUSTOMER="WLNP-GEN-019";
	
	public final static String FAILED_TO_CREATE_CREDIT_ATTRIBUTE="WLNP-GEN-020";
	
	public final static String WLN_CRD_MGMT_PROXY_POLICY_EXCEPTION="WLNP-PRX-001";
	
	public final static String WLN_CRD_MGMT_PROXY_SERVICE_EXCEPTION="WLNP-PRX-002";
	
	public final static String WLN_CRD_MGMT_PROXY_RUNTIME_EXCEPTION="WLNP-PRX-003";
	
	public static final String CUSTOMER_MGMT_POLICY_EXCEPTION = "WLNP-CCR-001";
	
    public static final String CUSTOMER_MGMT_SERVICE_EXCEPTION = "WLNP-CCR-002";
    
    public static final String CUSTOMER_MGMT_RUNTIME_EXCEPTION = "WLNP-CCR-003";
	
}
