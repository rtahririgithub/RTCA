package com.telus.credit.util;

public class CONSTANTS {
	
	//Schema elements/attributes
	public static final String firstName = "firstName";
	public static final String lastName = "lastName";
	public static final String name = "Name";
	public static final String birthDate = "birthDate";
	public static final String driverLicenseNumber = "driverLicenseNumber";
	public static final String socialInsuranceNum = "socialInsuranceNum";
	public static final String creditCardTokenTxt = "creditCardTokenTxt";
	public static final String findMatchingSearchResult = "findMatchingSearchResult";
	public static final String driverLicenseNum = "driverLicenseNum";
	public static final String sin = "sin";
	public static final String RefEntity = "RefEntity";
	public static final String Instance = "Instance";
	public static final String accountType = "accountType";
	public static final String accountSubType = "accountSubType";
	public static final String input = "Input";
	public static final String output = "Output";
	public static final String value = "value";
	public static final String billingAccountNumber = "billingAccountNumber";
	public static final String characteristicValue = "CharacteristicValue";

	//Refpds Table names 
	public static final String UC_MATCH_FILTERING_RULES_REFPDS_TABLE_NAME	="UC_MATCH_FILTERING_RULES";
	public static final String UC_SEARCH_EXCLUSION_RULES_TABLE_NAME			="UC_SEARCH_EXCLUSION_RULES";
	public static final String UC_MATCH_SELECTION_RULES_TABLE_NAME			="UC_MATCH_SELECTION_RULES";
	public static final String UC_MATCH_SIMULATOR_WARNINGS_TABLE_NAME		="UC_MATCH_SIMULATOR_WARNINGS";
	public static final String UC_MATCH_SIMULATOR_WLS_TABLE_NAME			="UC_MATCH_SIMULATOR_WLS";	

	//Cache keys populated from proxy	
	public static final String UCDORMANTFLAG_CACHE_KEY 					="UCDORMANTFLAG";	
	public static final String UCTOPMATCHSIMFLAG_CACHE_KEY 				="UCTOPMATCHSIMFLAG";
	public static final String WLN_WCDAP_LOGGING_ENABLED_CACHE_KEY 		="WLN_WCDAP_LOGGING_ENABLED";
	
	//Cache keys populated from java
	public static final String UC_REFPDS_APPID_CACHE_KEY  				="WLNCrdElgProxySvc";
	public static final String UC_ISCACHELOADED_CACHE_KEY  				="UC_ISCACHELOADED";
	public static final String UC_CACHELOADTIMESTAMP_CACHE_KEY 			="UC_CACHELOADTIMESTAMP";
	public static final String UC_ENVID_CACHE_KEY 						="EnvId";		
	public static final String UC_SEARCH_EXCLUSION_RULES_DL_CACHE_KEY	="UC_SEARCH_EXCLUSION_RULES_DL";
	public static final String UC_SEARCH_EXCLUSION_RULES_SIN_CACHE_KEY	="UC_SEARCH_EXCLUSION_RULES_sin";
	public static final String UC_SEARCH_EXCLUSION_RULES_CC_CACHE_KEY 	="UC_SEARCH_EXCLUSION_RULES_cc";
	public static final String UC_SEARCH_EXCLUSION_RULES_NAME_CACHE_KEY ="UC_SEARCH_EXCLUSION_RULES_name";	
	public static final String UC_MATCH_FILTERING_RULES_WLS_ACCT_TYPE_SUBTYPE_CACHE_KEY ="UC_MATCH_FILTERING_RULES_WLS_ACCT_TYPE_SUBTYPE";
	public static final String UC_MATCH_FILTERING_RULES_WLN_ACCT_TYPE_SUBTYPE_CACHE_KEY ="UC_MATCH_FILTERING_RULES_WLN_ACCT_TYPE_SUBTYPE";
	
	public static final String UC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID_CACHE_KEY ="UC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID";
	public static final String UC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID_CACHE_KEY ="UC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID";
	
	public static final String UC_MATCH_SELECTION_RULES_TOP_MATCH_RULES_CACHE_KEY 		="UC_MATCH_SELECTION_RULES_TOP_MATCH_RULES";
	

	
	
	
	
}
