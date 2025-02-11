package com.telus.credit.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UnifiedCreditUtil_v2 {

	static String namespacePrefix="tns";
		
//*** XPATH : findMatchingSearchResultList
	static String findMatchingSearchResultList_nsDeclaration="declare namespace " + namespacePrefix+"='http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/WCPMProxyMDMSearchResult'" ;
	static String findMatchingSearchResultList_xpath=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"findMatchingSearchResultList";
	static String findMatchingSearchResult_xpath=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"findMatchingSearchResult";	
	static String customerData_xpath 	=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"customerData";
	static String firstName_xpath 		=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"firstName";
	static String lastName_xpath 		=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"lastName";	
	static String ccToken_xpath 		=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"creditCardTokenTxt";
	static String sin_xpath 			=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"socialInsuranceNum";
	static String dl_xpath 				=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"driverLicenseNumber"; 
	static String dob_xpath 				=findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"birthDate";  	
	
	static String accountData_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"accountData";
	static String accountType_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"accountType";
	static String accountSubType_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"accountSubType";
	static String billingAccountNumber_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"billingAccountNumber";
	
	static String billingMasterSourceId_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"billingMasterSourceId";
	static String brandId_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"brandId";
	static String accountCreationDate_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"accountCreationDate";
	static String startServiceDate_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"startServiceDate";
	static String statusCode_xpath =findMatchingSearchResultList_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"statusCode";
	

//*** XPATH : performCreditAssessment
	static String performCreditAssessment_nsDeclaration="declare namespace " + namespacePrefix+"='http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1'" ;						
	static String firstName_ecrda_xpath =performCreditAssessment_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"firstName";
	static String lastName_ecrda_xpath 	=performCreditAssessment_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"lastName";
	static String dl_ecrda_xpath 		=performCreditAssessment_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"driverLicenseNum";
	static String sin_ecrda_xpath 		=performCreditAssessment_nsDeclaration +"  " + ".//" + namespacePrefix + ":" +"sin";
	
//*** REFPDS TABLE as Element
	static Element uC_MATCH_FILTERING_RULES_REFPDS_TABLE_Element=null;
	static Element uC_SEARCH_EXCLUSION_RULES_Element=null;
	static Element uC_MATCH_SELECTION_RULES_Element=null;
	static Element uC_MATCH_SIMULATOR_WARNINGS_Element=null;
	static Element uC_MATCH_SIMULATOR_WLS_Element=null;
	
	/*
	* Exclude CreditAssessmentRequest's attributes per UC_SEARCH_EXCLUSION_RULES 
	* before searching for matching customers.
	* ESB Proxy name = ???
	*/
/*	@SuppressWarnings("unchecked")
	public static XmlObject exclude_CreditAssessmentRequestAttributes_per_UC_SEARCH_EXCLUSION_RULES(XmlObject inputCreditAssessmentRequest){
		LogUtil.logInfo("************Starting exclude_CreditAssessmentRequestAttributes_per_UC_SEARCH_EXCLUSION_RULES**************");
		LogUtil.logInfo("*exclude_CreditAssessmentRequestAttributes_per_UC_SEARCH_EXCLUSION_RULES before searching for matching customers.***");
		if(inputCreditAssessmentRequest==null){
			LogUtil.logError("inputCreditAssessmentRequest is null ");
			return inputCreditAssessmentRequest;
		}		
		try{
		    //get Refpds values from cache
	        Vector<String> uC_SEARCH_EXCLUSION_RULES_DL   = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_DL_CACHE_KEY);
	        Vector<String> uC_SEARCH_EXCLUSION_RULES_SIN  = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_SIN_CACHE_KEY);
	        Vector<String> uC_SEARCH_EXCLUSION_RULES_NAME = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_NAME_CACHE_KEY);

			//get input attributes and apply rules
			XmlObject[] lastName_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(lastName_ecrda_xpath):null;
			XmlObject lastName_xmlObj = (lastName_xmlObjs!=null&& lastName_xmlObjs.length>0 && lastName_xmlObjs[0]!=null)?lastName_xmlObjs[0]:null;
			String 	   lastName_TxtVal=(lastName_xmlObj!=null )?lastName_xmlObj.newCursor().getTextValue():null;			
			
			XmlObject[] firstName_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(firstName_ecrda_xpath):null;			
			XmlObject firstName_xmlObj = (firstName_xmlObjs!=null&& firstName_xmlObjs.length>0 && firstName_xmlObjs[0]!=null)?firstName_xmlObjs[0]:null;
			String 	   firstName_TxtVal=(firstName_xmlObj!=null)?firstName_xmlObj.newCursor().getTextValue():null;
			
			String fullName = firstName_TxtVal+ "_" + lastName_TxtVal;
			boolean nameExlusionInd = foundInExclusion("NAME" ,uC_SEARCH_EXCLUSION_RULES_NAME, fullName);
			if (nameExlusionInd){
				if(firstName_xmlObj!=null){
					firstName_xmlObj.newCursor().removeXml();
				}
				if(lastName_xmlObj!=null){
					lastName_xmlObj.newCursor().removeXml();
				}
				LogUtil.logInfo("Removed name="+ fullName );
			}
			//SIN
			XmlObject[] sin_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(sin_ecrda_xpath):null;
			XmlObject sin_xmlObj = (sin_xmlObjs!=null&& sin_xmlObjs.length>0 && sin_xmlObjs[0]!=null)?sin_xmlObjs[0]:null;
			String aSocialInsuranceNum=(sin_xmlObj!=null)?sin_xmlObj.newCursor().getTextValue():null;
			boolean sinExlusionInd 	= foundInExclusion("SIN" ,uC_SEARCH_EXCLUSION_RULES_SIN, aSocialInsuranceNum);
			if (sinExlusionInd){
				if(sin_xmlObj!=null){
					sin_xmlObj.newCursor().removeXml();
				}
				LogUtil.logInfo("Removed SIN="+ aSocialInsuranceNum );
			}			
			//DL					
			XmlObject[] dl_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(dl_ecrda_xpath):null;
			XmlObject dl_xmlObj = (dl_xmlObjs!=null&& dl_xmlObjs.length>0 && dl_xmlObjs[0]!=null)?dl_xmlObjs[0]:null;
			String dl=(dl_xmlObj!=null)?dl_xmlObj.newCursor().getTextValue():null;
			boolean dlExlusionInd 	= foundInExclusion("DL" ,uC_SEARCH_EXCLUSION_RULES_DL, dl);	
			if (dlExlusionInd){
				if(dl_xmlObj!=null){
					dl_xmlObj.newCursor().removeXml();
				}
				LogUtil.logInfo("Removed DL="+ dl );
			}				
		} catch (Throwable e){
			LogUtil.logException(e);
		}
	LogUtil.logInfo("outputCreditAssessmentRequest=[\n"+ inputCreditAssessmentRequest +"\n]");
	LogUtil.logInfo("************End exclude_CreditAssessmentRequestAttributes_per_UC_SEARCH_EXCLUSION_RULES**************");		
	return inputCreditAssessmentRequest;
	}
	*/
	/*
	* Check whether the CreditAssessmentRequest contains any values from exclusion list( UC_SEARCH_EXCLUSION_RULES) .
	* System will not call MDM if the method return true.
	* match should not be returned in case the request cotnains values forom from exclusion list

	*/
	@SuppressWarnings("unchecked")
	public static boolean creditAssessmentRequest_contains_exclusionData_per_UC_SEARCH_EXCLUSION_RULES(XmlObject inputCreditAssessmentRequest){
		//Response
		boolean exclusionDataFoundInd=false;
		
		LogUtil.logInfo("************Start_creditAssessmentRequest_contains_exclusionData_per_UC_SEARCH_EXCLUSION_RULES**************");
		if(inputCreditAssessmentRequest==null){
			LogUtil.logError("inputCreditAssessmentRequest is null ");
			return true;
		}		
		try{
		    //get Refpds values from cache
	        Vector<String> uC_SEARCH_EXCLUSION_RULES_DL   = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_DL_CACHE_KEY);
	        Vector<String> uC_SEARCH_EXCLUSION_RULES_SIN  = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_SIN_CACHE_KEY);
	        Vector<String> uC_SEARCH_EXCLUSION_RULES_NAME = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_NAME_CACHE_KEY);

			//get input attributes and apply rules
			XmlObject[] lastName_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(lastName_ecrda_xpath):null;
			XmlObject lastName_xmlObj = (lastName_xmlObjs!=null&& lastName_xmlObjs.length>0 && lastName_xmlObjs[0]!=null)?lastName_xmlObjs[0]:null;
			String 	   lastName_TxtVal=(lastName_xmlObj!=null )?lastName_xmlObj.newCursor().getTextValue():null;			
			
			XmlObject[] firstName_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(firstName_ecrda_xpath):null;			
			XmlObject firstName_xmlObj = (firstName_xmlObjs!=null&& firstName_xmlObjs.length>0 && firstName_xmlObjs[0]!=null)?firstName_xmlObjs[0]:null;
			String 	   firstName_TxtVal=(firstName_xmlObj!=null)?firstName_xmlObj.newCursor().getTextValue():null;
			
			String fullName = firstName_TxtVal+ "_" + lastName_TxtVal;
			boolean nameExlusionInd = foundInExclusion("NAME" ,uC_SEARCH_EXCLUSION_RULES_NAME, fullName);

			//SIN
			XmlObject[] sin_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(sin_ecrda_xpath):null;
			XmlObject sin_xmlObj = (sin_xmlObjs!=null&& sin_xmlObjs.length>0 && sin_xmlObjs[0]!=null)?sin_xmlObjs[0]:null;
			String aSocialInsuranceNum=(sin_xmlObj!=null)?sin_xmlObj.newCursor().getTextValue():null;
			boolean sinExlusionInd 	= foundInExclusion("SIN" ,uC_SEARCH_EXCLUSION_RULES_SIN, aSocialInsuranceNum);
			//DL					
			XmlObject[] dl_xmlObjs=(inputCreditAssessmentRequest!=null)?inputCreditAssessmentRequest.selectPath(dl_ecrda_xpath):null;
			XmlObject dl_xmlObj = (dl_xmlObjs!=null&& dl_xmlObjs.length>0 && dl_xmlObjs[0]!=null)?dl_xmlObjs[0]:null;
			String dl=(dl_xmlObj!=null)?dl_xmlObj.newCursor().getTextValue():null;
			boolean dlExlusionInd 	= foundInExclusion("DL" ,uC_SEARCH_EXCLUSION_RULES_DL, dl);	
			
			
			if (sinExlusionInd){
				LogUtil.logInfo("exclusionDataFound SIN="+ aSocialInsuranceNum );
			}			
			if (nameExlusionInd){
				LogUtil.logInfo("exclusionDataFound name="+ fullName );
			}			
			if (dlExlusionInd){
				LogUtil.logInfo("exclusionDataFound DL="+ dl );
			}
			
			if (nameExlusionInd || dlExlusionInd || sinExlusionInd ){
				exclusionDataFoundInd=true;
			}else{
				exclusionDataFoundInd=false;
				LogUtil.logInfo("No exclusion performed for :" + "fullName="+ fullName+ ",SocialInsuranceNum="+ aSocialInsuranceNum+ ",dl="+ dl );
			}
		} catch (Throwable e){
			LogUtil.logException(e);
		}
	
	
	LogUtil.logInfo("exclusionDataFoundInd="+ exclusionDataFoundInd );
	LogUtil.logInfo("************End_creditAssessmentRequest_contains_exclusionData_per_UC_SEARCH_EXCLUSION_RULES**************");		
	return exclusionDataFoundInd;
	}
	

	private static boolean foundInExclusion(String idName ,Vector<String> vector, String toCompare){
		if(vector!=null && toCompare!=null){
			for (int i=0; i<vector.size(); i++){
				String in = (String) vector.elementAt(i);				
				if(toCompare.equalsIgnoreCase(in)){
					return true;
				}
			}
		}
		return false;
	}
	

	
	
	private static void load_cache_from_refpds_table(XmlObject refpdsData) throws Exception{		
		get_each_RefpdsTable(refpdsData);		
		loadData_from_UC_SEARCH_EXCLUSION_RULES2();
		loadData_from_UC_MATCH_FILTERING_RULES();
		loadData_from_UC_MATCH_SELECTION_RULES();
		ESBCacheHelper.addXmlObjectToCache(CONSTANTS.UC_REFPDS_APPID_CACHE_KEY, refpdsData);
	}
	
	private static void loadData_from_UC_SEARCH_EXCLUSION_RULES2() throws Exception{
		LogUtil.logInfo("-------------------------------------------");
		LogUtil.logInfo("start_loadData_from_UC_SEARCH_EXCLUSION_RULES2");
		
		NodeList instances = uC_SEARCH_EXCLUSION_RULES_Element.getElementsByTagNameNS("*", CONSTANTS.Instance); 
		
		Vector<String> uC_SEARCH_EXCLUSION_RULES_drivingLicenses = new Vector<String>();
		Vector<String> uC_SEARCH_EXCLUSION_RULES_sin = new Vector<String>();
		Vector<String> uC_SEARCH_EXCLUSION_RULES_cc = new Vector<String>();
		Vector<String> uC_SEARCH_EXCLUSION_RULES_name = new Vector<String>();
		
		LogUtil.logInfo("UC_SEARCH_EXCLUSION_RULES_TABLE Instances length = " + instances.getLength());
	    for (int j = 0; j < instances.getLength(); j++) {
	    	Node aInstance = instances.item(j);
	    	NodeList childNodes = aInstance.getChildNodes();
	    	String inputElm="";
	    	String outputElm="";
	    	for(int k=0;k<childNodes.getLength();k++){		
	        	String localName = childNodes.item(k).getLocalName();
	        	localName=(localName!=null)?localName.trim():null;
	        	if("Input".equalsIgnoreCase(localName)){
	        		inputElm = childNodes.item(k).getTextContent();
	        		inputElm=(inputElm!=null)?inputElm.trim():null;
	        	}
	        	if("Output".equalsIgnoreCase(localName)){
	        		outputElm = childNodes.item(k).getTextContent();
	        		outputElm=(outputElm!=null)?outputElm.trim():null;
	        	}			            	
	        }	
	        if("DL".equalsIgnoreCase(inputElm)){
	        	uC_SEARCH_EXCLUSION_RULES_drivingLicenses.add(outputElm);
	        }else{
	            if("SIN".equalsIgnoreCase(inputElm)){
	            	uC_SEARCH_EXCLUSION_RULES_sin.add(outputElm);
	            }else{
		            if("CC".equalsIgnoreCase(inputElm)){
		            	uC_SEARCH_EXCLUSION_RULES_cc.add(outputElm);
		            }else{
			            if("FIRST_LAST_NAME".equalsIgnoreCase(inputElm)){
			            	uC_SEARCH_EXCLUSION_RULES_name.add(outputElm);
			            }			            	
		            }			            	
	            }			            	
	        }
	    }
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_DL_CACHE_KEY,uC_SEARCH_EXCLUSION_RULES_drivingLicenses);
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_SIN_CACHE_KEY,uC_SEARCH_EXCLUSION_RULES_sin);
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_CC_CACHE_KEY,uC_SEARCH_EXCLUSION_RULES_cc);
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_NAME_CACHE_KEY,uC_SEARCH_EXCLUSION_RULES_name);
	    
	    LogUtil.logInfo("Driving licenses loaded = " + uC_SEARCH_EXCLUSION_RULES_drivingLicenses);
	    LogUtil.logInfo("SINs loaded = " + uC_SEARCH_EXCLUSION_RULES_sin);
	    LogUtil.logInfo("Cards loaded = " + uC_SEARCH_EXCLUSION_RULES_cc);
	    LogUtil.logInfo("Names loaded = " + uC_SEARCH_EXCLUSION_RULES_name);		    
	    LogUtil.logInfo("end_loadData_from_UC_SEARCH_EXCLUSION_RULES");
	    LogUtil.logInfo("-------------------------------------------");
	    
	}

	private static Element get_each_RefpdsTable( XmlObject refpdsData) throws Exception{
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setNamespaceAware(true);
	    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	    Document document = docBuilder.parse(refpdsData.newInputStream());
	    NodeList nodeList = document.getElementsByTagNameNS("*", CONSTANTS.RefEntity);
 	    for (int i = 0; i < nodeList.getLength(); i++) {
	    	Element element = (Element) nodeList.item(i);
	        if(element.getAttribute("name").equals(CONSTANTS.UC_MATCH_FILTERING_RULES_REFPDS_TABLE_NAME)){
	        	uC_MATCH_FILTERING_RULES_REFPDS_TABLE_Element = element;
	        }else{
		        if(element.getAttribute("name").equals(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_TABLE_NAME)){
		        	uC_SEARCH_EXCLUSION_RULES_Element = element;
		        }else{
			        if(element.getAttribute("name").equals(CONSTANTS.UC_MATCH_SELECTION_RULES_TABLE_NAME)){
			        	uC_MATCH_SELECTION_RULES_Element = element;
			        }else{
				        if(element.getAttribute("name").equals(CONSTANTS.UC_MATCH_SIMULATOR_WARNINGS_TABLE_NAME)){
				        	uC_MATCH_SIMULATOR_WARNINGS_Element = element;
				        }else{
					        if(element.getAttribute("name").equals(CONSTANTS.UC_MATCH_SIMULATOR_WLS_TABLE_NAME)){
					        	uC_MATCH_SIMULATOR_WLS_Element = element;					        	
					        }	        	
				        }	        	
			        }	        	
		        }	        	
	        }
	    }
		return null;   
		
		
}	
	

	public static boolean loadUnifiedCreditCache(XmlObject refpdsData){	
		LogUtil.logInfo("************Start_loadUnifiedCreditCache***************");
		boolean isCacheLoaded=false;
		try {
			if(refpdsData==null){
				LogUtil.logError("input refpdsData is null ");
				return isCacheLoaded;
			}			
			load_cache_from_refpds_table(refpdsData);
			ESBCacheHelper.addObjectToCache(CONSTANTS.UC_ENVID_CACHE_KEY,WeblogicRunTimeUtil.getEnvId());
			isCacheLoaded=true;
		} catch (Throwable e) {
			LogUtil.logException(e);
		}
		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			ESBCacheHelper.addObjectToCache(CONSTANTS.UC_CACHELOADTIMESTAMP_CACHE_KEY,timestamp);
			ESBCacheHelper.addObjectToCache(CONSTANTS.UC_ISCACHELOADED_CACHE_KEY,isCacheLoaded);
		} catch (Throwable e) {
			LogUtil.logException(e);
		}		
		LogUtil.logInfo("************End_loadUnifiedCreditCache***************");
		return isCacheLoaded;
	}


	public static XmlObject exclude_MatchedAccounts_per_UC_MATCH_FILTERING_RULES(XmlObject inputFindMatchingSearchResultList){
		LogUtil.logInfo("************Start_exclude_MatchedAccounts_per_UC_MATCH_FILTERING_RULES***************");
		if(inputFindMatchingSearchResultList==null){
			LogUtil.logError("inputFindMatchingSearchResultList is null ");
			return inputFindMatchingSearchResultList;
		}	
		boolean anAcctRemovedInd = false;
		try{			
			//get Refpds values from cache
			String[] refpds_wls_acct_type_subtype = (String[]) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_MATCH_FILTERING_RULES_WLS_ACCT_TYPE_SUBTYPE_CACHE_KEY);
			String[] refpds_WLN_UC_BILLING_MASTER_SOURCE_IDS = (String[]) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID_CACHE_KEY);
			
			//get list of accounts in a findMatchingSearchResult					
			XmlObject[] accountDataList_xmlObj=inputFindMatchingSearchResultList.selectPath(accountData_xpath);
			
			//Apply rules
			for (XmlObject accountData_xmlObj : accountDataList_xmlObj) {
				String billingAccountNumber		=getXmlObjectTextValue(accountData_xmlObj, billingAccountNumber_xpath);
				String accountType				=getXmlObjectTextValue(accountData_xmlObj, accountType_xpath);
				String accountSubType			=getXmlObjectTextValue(accountData_xmlObj, accountSubType_xpath);
				String wlsAccountTypeSubType=accountType + accountSubType;
				wlsAccountTypeSubType=(wlsAccountTypeSubType!=null)?wlsAccountTypeSubType.trim():"";
				LogUtil.logInfo("**Executing Rules for BAN=" + billingAccountNumber);
				LogUtil.logInfo("*****Executing Rule: WLN_UC_BILLING_MASTER_SOURCE_ID ***************");
				String  billingMasterSourceId		=getXmlObjectTextValue(accountData_xmlObj, billingMasterSourceId_xpath);
				boolean WLN_UC_BILLING_MASTER_SOURCE_ID_Ind = foundInInclusion(refpds_WLN_UC_BILLING_MASTER_SOURCE_IDS, billingMasterSourceId);
				if(!WLN_UC_BILLING_MASTER_SOURCE_ID_Ind){
					accountData_xmlObj.newCursor().removeXml();
					anAcctRemovedInd=true;
					LogUtil.logInfo("*******Removed findMatchingSearchResult's Account with BAN=" + billingAccountNumber +",billingMasterSourceId= " + billingMasterSourceId);
				}else{
					LogUtil.logInfo("*****Executing Rule: wls_acct_type_subtype  ***************");
					boolean accountTypeSubTypeInclusionInd = foundInInclusion(refpds_wls_acct_type_subtype, wlsAccountTypeSubType);
					if(!accountTypeSubTypeInclusionInd){
						accountData_xmlObj.newCursor().removeXml();
						anAcctRemovedInd=true;
						LogUtil.logInfo("*******Removed findMatchingSearchResult's Account with BAN=" + billingAccountNumber +",accountTypeSubType= " + wlsAccountTypeSubType);
					}					
				}
			}			
		} catch (Throwable e){			
			LogUtil.logException(e);
		}
		if(!anAcctRemovedInd){
			LogUtil.logInfo("\n UC_MATCH_FILTERING_RULES:No Account was removed.");
		}
 		LogUtil.logInfo("\n\noutput_FindMatchingSearchResultList=[\n" + inputFindMatchingSearchResultList+"\n]");
 		LogUtil.logInfo("************End_exclude_MatchedAccounts_per_UC_MATCH_FILTERING_RULES***************");
 		return inputFindMatchingSearchResultList;
	}	
	private static boolean foundInInclusion(String [] vector, String toCompare){
		if(vector!=null && toCompare!=null){
			for (int i=0; i<vector.length; i++){
				String in = vector[i];
				if(toCompare.equalsIgnoreCase(in)){
					return true;
				}
			}
		}
		return false;
	}
	
	private static void loadData_from_UC_MATCH_FILTERING_RULES() throws Exception{
		LogUtil.logInfo("-------------------------------------------");
		LogUtil.logInfo("*************Start_loadData_from_UC_MATCH_FILTERING_RULES***********");
		String[] uC_MATCH_FILTERING_RULES_wls_acct_type_subtype = new String[0];
		String[] uC_MATCH_FILTERING_RULES_wln_acct_type_subtype = new String[0];
		
		String[] uC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID = new String[0];
		String[] uC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID = new String[0];
		
    	NodeList instances = uC_MATCH_FILTERING_RULES_REFPDS_TABLE_Element.getElementsByTagNameNS("*", CONSTANTS.Instance); 
        LogUtil.logInfo("Instances length = " + instances.getLength());
	    for (int j = 0; j < instances.getLength(); j++) {
	    	Element singleInstance = (Element) instances.item(j);
	    	NodeList input = singleInstance.getElementsByTagNameNS("*",CONSTANTS.input);
	    	Element inputFilterName = (Element) input.item(0);
	    	Element inputFilterType = (Element) input.item(1);
	    	boolean includeFilterInd = inputFilterType.getAttribute("code").equals("FILTER_TYPE") && inputFilterType.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("I");
	    	if( includeFilterInd ){
    			NodeList output = singleInstance.getElementsByTagNameNS("*", CONSTANTS.output);
	            Element outputElement = (Element) output.item(0);
	            NodeList outputValues = outputElement.getElementsByTagNameNS("*", "value");		    		
	    		
	            boolean isWLN_ACCT_TYPE_SUBTYPE = inputFilterName.getAttribute("code").equals("FILTER_NAME") && inputFilterName.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("WLN_CUST_TYPE");
	    		if( isWLN_ACCT_TYPE_SUBTYPE ){
		            uC_MATCH_FILTERING_RULES_wln_acct_type_subtype = new String[outputValues.getLength()];
				    for (int k = 0; k < outputValues.getLength(); k++) {
				    	uC_MATCH_FILTERING_RULES_wln_acct_type_subtype[k] = outputValues.item(k).getTextContent();
				    }		    			
	    		}else{	    		
		    		boolean isWLS_ACCT_TYPE_SUBTYPE = inputFilterName.getAttribute("code").equals("FILTER_NAME") && inputFilterName.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("WLS_ACCT_TYPE_SUBTYPE");
		    		if( isWLS_ACCT_TYPE_SUBTYPE ){
			            uC_MATCH_FILTERING_RULES_wls_acct_type_subtype = new String[outputValues.getLength()];
					    for (int k = 0; k < outputValues.getLength(); k++) {
					    	uC_MATCH_FILTERING_RULES_wls_acct_type_subtype[k] = outputValues.item(k).getTextContent();
					    }		    			
		    		}else{
		    			boolean isWLN_UC_BILLING_MASTER_SOURCE_ID	 = inputFilterName.getAttribute("code").equals("FILTER_NAME") && inputFilterName.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("WLN_UC_BILLING_MASTER_SOURCE_ID");
			    		if( isWLN_UC_BILLING_MASTER_SOURCE_ID ){
				            uC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID = new String[outputValues.getLength()];
						    for (int k = 0; k < outputValues.getLength(); k++) {
						    	uC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID[k] = outputValues.item(k).getTextContent();
						    }		    			
			    		}else{
			    			boolean isWLS_UC_BILLING_MASTER_SOURCE_ID	 = inputFilterName.getAttribute("code").equals("FILTER_NAME") && inputFilterName.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("WLS_UC_BILLING_MASTER_SOURCE_ID");
				    		if( isWLS_UC_BILLING_MASTER_SOURCE_ID ){
					            uC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID = new String[outputValues.getLength()];
							    for (int k = 0; k < outputValues.getLength(); k++) {
							    	uC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID[k] = outputValues.item(k).getTextContent();
							    }		    			
				    		}		    		
			    		}		    		
		    		}
	    		}
	    	}
	    }
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_MATCH_FILTERING_RULES_WLS_ACCT_TYPE_SUBTYPE_CACHE_KEY,uC_MATCH_FILTERING_RULES_wls_acct_type_subtype);
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_MATCH_FILTERING_RULES_WLN_ACCT_TYPE_SUBTYPE_CACHE_KEY,uC_MATCH_FILTERING_RULES_wln_acct_type_subtype);
	    
	    
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID_CACHE_KEY,uC_MATCH_FILTERING_RULES_WLN_UC_BILLING_MASTER_SOURCE_ID);
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID_CACHE_KEY,uC_MATCH_FILTERING_RULES_WLS_UC_BILLING_MASTER_SOURCE_ID);
	    
		LogUtil.logInfo("*************End_loadData_from_UC_MATCH_FILTERING_RULES***********");
		LogUtil.logInfo("-------------------------------------------");
	}	

	private static void loadData_from_UC_MATCH_SELECTION_RULES() throws Exception{

		LogUtil.logInfo("*****************Start_loadData_from_UC_MATCH_SELECTION_RULES*****************");
		NodeList instances = uC_MATCH_SELECTION_RULES_Element.getElementsByTagNameNS("*", CONSTANTS.Instance); 
		
		String [][] topMatchRules;   	
        LogUtil.logInfo("Instances length = " + instances.getLength());
    	topMatchRules = new String[instances.getLength()][4];
	    for (int j = 0; j < instances.getLength(); j++) {			    	
	    	Element singleInstance = (Element) instances.item(j);
	    	NodeList input = singleInstance.getElementsByTagNameNS("*",CONSTANTS.input);
	    	Element ruleNumber = (Element) input.item(0);			    	
	    	if(ruleNumber.getAttribute("code").equals("RULE_ORDER_NUM")){
	            NodeList output = singleInstance.getElementsByTagNameNS("*", CONSTANTS.output);
	            Element accountStatusCode = (Element) output.item(0);
	            Element accountOpenDateRule = (Element) output.item(1);
	            Element accountCreationDateRule = (Element) output.item(2);
	            topMatchRules[j][0] = ruleNumber.getElementsByTagNameNS("*", "value").item(0).getTextContent().trim();
	            topMatchRules[j][1] = accountStatusCode.getElementsByTagNameNS("*", "value").item(0).getTextContent().trim();
	            topMatchRules[j][2] = accountOpenDateRule.getElementsByTagNameNS("*", "value").item(0).getTextContent().trim();
	            topMatchRules[j][3] = accountCreationDateRule.getElementsByTagNameNS("*", "value").item(0).getTextContent().trim();
	    	}			    	
	    }
	    LogUtil.logInfo("Top matching rules loaded = " + topMatchRules.length);
	    //Sorting the rules by Rule number
	    try {
			Arrays.sort(topMatchRules, new Comparator<String[]>() {
			    public int compare(String[] s1, String[] s2) {
				    if (s1 == null || s2 == null)
					    return 0;
				    else if (s1.length ==0 || s2.length ==0)
					    return 0;		    
				    else if (Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]))
			            return 1;
			        else if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]))
			            return -1;
			        else {
			            return 0;
			        }
			    }        
			});
		} catch (Throwable e) {
			LogUtil.logException(e);
		}		    
	    ESBCacheHelper.addObjectToCache(CONSTANTS.UC_MATCH_SELECTION_RULES_TOP_MATCH_RULES_CACHE_KEY,topMatchRules);
	    
		LogUtil.logInfo("*****************End_loadData_from_UC_MATCH_SELECTION_RULES*****************");	  

	}
	
	private static XmlObject remove_non_topMatchBan_xmlElements(XmlObject inputFindMatchingSearchResultList, String topMatchBan){	
		try{
//Step: Remove any findMatchingSearchResult and accountData that does not have same ban as topMatchBan.
			//get list of findMatchingSearchResult
			XmlObject[] findMatchingSearchResult_array1 =inputFindMatchingSearchResultList.selectPath(findMatchingSearchResult_xpath);
			for (int i = 0; i < findMatchingSearchResult_array1.length; i++) {
				boolean removeCurrentFindMatchingSearchResult=true;
				//Remove accountData that does not have the same ban as topMatchBan
				XmlObject[] accountDataList_xmlObj=findMatchingSearchResult_array1[i].selectPath(accountData_xpath);
				for (int j = 0; j < accountDataList_xmlObj.length; j++) {
					XmlObject accountData = accountDataList_xmlObj[j];
					String billingAccountNumber2=getXmlObjectTextValue(accountData, billingAccountNumber_xpath);
					if (!topMatchBan.equalsIgnoreCase(billingAccountNumber2)){
						accountData.newCursor().removeXml();
					}else{
						removeCurrentFindMatchingSearchResult=false;
					}				
				}
				//Remove findMatchingSearchResult that does not have an account with the same ban as topMatchBan.
				if(removeCurrentFindMatchingSearchResult){
					findMatchingSearchResult_array1[i].newCursor().removeXml();
				}
			}	
//Step: Select the topMatch_findMatchingSearchResult
			//get the updated list of findMatchingSearchResult 
			XmlObject[] findMatchingSearchResult_array2 =inputFindMatchingSearchResultList.selectPath(findMatchingSearchResult_xpath);
			if(findMatchingSearchResult_array2!=null && findMatchingSearchResult_array2.length>0){
				XmlObject topMatch_findMatchingSearchResult = findMatchingSearchResult_array2[0];						
//Step: Remove  duplicated accountData within the topMatch findMatchingSearchResult.			    
				XmlObject[] accountDataList_xmlObj=topMatch_findMatchingSearchResult.selectPath(accountData_xpath);
				if(accountDataList_xmlObj.length>1){
					for (int i = 1; i < accountDataList_xmlObj.length; i++) {
						accountDataList_xmlObj[i].newCursor().removeXml();
					}	
				}	
			}		
//Step: Remove other findMatchingSearchResult except topMatch_findMatchingSearchResult
			//get the updated list of findMatchingSearchResult 
			XmlObject[] findMatchingSearchResult_array3 =inputFindMatchingSearchResultList.selectPath(findMatchingSearchResult_xpath);	
			if(findMatchingSearchResult_array3!=null && findMatchingSearchResult_array3.length>1){
				for (int i = 1; i < findMatchingSearchResult_array3.length; i++) {
					findMatchingSearchResult_array3[i].newCursor().removeXml();
				}	
			}
		} catch (Throwable e){
			LogUtil.logException(e);
		}	
		//return  updated inputFindMatchingSearchResultList				
		return inputFindMatchingSearchResultList;
	}

	public static XmlObject select_top_MatchedAccount_per_UC_MATCH_SELECTION_RULES(XmlObject inputFindMatchingSearchResultList){
		LogUtil.logInfo("*****************Start_select_top_MatchedAccount_per_UC_MATCH_SELECTION_RULES*****************");
		XmlObject topMatch_FindMatchingSearchResultList=null;
		if(inputFindMatchingSearchResultList==null){
			LogUtil.logError("inputFindMatchingSearchResultList is null ");
			return topMatch_FindMatchingSearchResultList;
		}		
		try{
			//get REFPDS Rules from cache
			String [][] topMatchRules =(String [][] )ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_MATCH_SELECTION_RULES_TOP_MATCH_RULES_CACHE_KEY);
			if(topMatchRules==null){
				LogUtil.logInfo("Missing wls_acct_type_subtype cache value for key=[" + CONSTANTS.UC_MATCH_FILTERING_RULES_WLS_ACCT_TYPE_SUBTYPE_CACHE_KEY +"]");
				return topMatch_FindMatchingSearchResultList;
			}	
			//execute_TopMatchAccount_Rules
			String top_account_BAN = apply_UC_MATCH_SELECTION_RULES(inputFindMatchingSearchResultList,topMatchRules);
			//remove_non_topMatchBan_xmlElements
			if(top_account_BAN!=null && !"".equalsIgnoreCase(top_account_BAN)){
				topMatch_FindMatchingSearchResultList=remove_non_topMatchBan_xmlElements(inputFindMatchingSearchResultList, top_account_BAN);
			}
		} catch (Throwable e){
			LogUtil.logException(e);
		}
		LogUtil.logInfo("output_topMatch_findMatchingSearchResult=[" + topMatch_FindMatchingSearchResultList+"]");
		LogUtil.logInfo("*****************End_select_top_MatchedAccount_per_UC_MATCH_SELECTION_RULES*****************");
		return topMatch_FindMatchingSearchResultList;
	}



	private static Account convert_AccountDataXmlObj_to_InternalAccount(XmlObject accountData_xmlObj){
		Account account = new Account();
		
		String ban=getXmlObjectTextValue(accountData_xmlObj, billingAccountNumber_xpath);
		String accountStatus=getXmlObjectTextValue(accountData_xmlObj, statusCode_xpath);
		
		String accountDate= null;
		String accountCreationDate= null;
		String startServiceDate = null;
		if(accountStatus.equalsIgnoreCase("T")){
			accountCreationDate	=getXmlObjectTextValue(accountData_xmlObj, accountCreationDate_xpath);
			accountDate=accountCreationDate;
		}
		else{
			startServiceDate=getXmlObjectTextValue(accountData_xmlObj, startServiceDate_xpath);
			accountDate=startServiceDate;
		}			    	
		account.setAccountNumber(ban);
		account.setAccountStatus(accountStatus);
		
		Date accountDateParsed = null;
		try {
			accountDateParsed = new SimpleDateFormat("yyyy-MM-dd").parse(accountDate);
		} catch (Throwable e) {
			LogUtil.logException(e);
		}
		
		account.setAccountDate(accountDateParsed);
		LogUtil.logInfo(
				"[ban =" + ban 
				+",Status =" + accountStatus
				+",accountDate =  " + accountDate 
				+",accountCreationDate =  " + accountCreationDate
				+",startServiceDate =  " + startServiceDate
				+"]"
				);
				
		return account;				
	}
	
	/*
	 * Repds Rules UC_MATCH_SELECTION_RULES
	[10, O, OLDEST, NA]
	[20, S, OLDEST, NA]
	[30, T, NA, OLDEST]
	[40, ANY, OLDEST, NA]
	*/	
	// 
	private static String apply_UC_MATCH_SELECTION_RULES(XmlObject inputFindMatchingSearchResultList,String [][] topMatchRules){
		LogUtil.logInfo("*****************Start_apply_UC_MATCH_SELECTION_RULES*****************");
		String top_account_BAN = "";
		if(topMatchRules==null || topMatchRules.length==0){
			LogUtil.logError("topMatchRules is missing. ");
			return top_account_BAN;
		}
		

//Step: convert_AccountDataXmlObj_to_InternalAccount
		ArrayList<Account> internalAccountDataArray = convert_AccountDataXmlObjs_to_InternalAccountArray(inputFindMatchingSearchResultList);
		if(!(internalAccountDataArray.size()>0) ){
			LogUtil.logError("No FindMatchingSearchResult found in input FindMatchingSearchResultList. ");
			return top_account_BAN;			
		}
//Step: apply the rules					    
		ArrayList<Account> matchAccounts = new ArrayList<Account>();
		for(int i=0; i<topMatchRules.length;i++){
			matchAccounts.removeAll(internalAccountDataArray);
			String[] rule_row = topMatchRules[i];	
			if(topMatchRules==null || topMatchRules.length<4){
				LogUtil.logError("Invalid topMatchRules . ");
				break;
			}				
			String rULE_ORDER_NUM 			= rule_row[0];
			String aCCOUNT_STATUS_CD 		= rule_row[1];
			String aCCOUNT_OPEN_DT_RULE 	= rule_row[2];
			String aCCOUNT_CREATION_DT_RULE = rule_row[3];
			
			LogUtil.logInfo("Executing rule: [" + "rULE_ORDER_NUM=" + rULE_ORDER_NUM + ",aCCOUNT_STATUS_CD=" + aCCOUNT_STATUS_CD + ",aCCOUNT_OPEN_DT_RULE=" + aCCOUNT_OPEN_DT_RULE + ",aCCOUNT_CREATION_DT_RULE=" + aCCOUNT_CREATION_DT_RULE + "]");
			//Rule Cond ACCOUNT_STATUS_CD
			matchAccounts = getMatchingAccountsBy_RuleCond_ACCOUNT_STATUS_CD(internalAccountDataArray,aCCOUNT_STATUS_CD);
			
			//Rule Cond ACCOUNT_OPEN_DT_RULE or ACCOUNT_CREATION_DT_RULE
			if(matchAccounts.size()>0 ){
				if("T".equals(aCCOUNT_STATUS_CD) ){
					 // execute by account creation date
					 top_account_BAN = execute_TopMatchAccount_Tenure_Rule(matchAccounts,aCCOUNT_CREATION_DT_RULE);
				}else{
					// execute by account creation date
					top_account_BAN = execute_TopMatchAccount_Tenure_Rule(matchAccounts,aCCOUNT_OPEN_DT_RULE);
				}
				//Check if Top match account is found then stop executing rules
				top_account_BAN=(top_account_BAN!=null)?top_account_BAN.trim():"";
				if(!"".equalsIgnoreCase(top_account_BAN)){
					LogUtil.logInfo("Top match account " + top_account_BAN + " found. Stopping further rules execution.");
					break;
				}			
			}
		}
		LogUtil.logInfo("***************End_apply_UC_MATCH_SELECTION_RULES***************");
		return top_account_BAN;
	}


	private static ArrayList<Account> convert_AccountDataXmlObjs_to_InternalAccountArray(
			XmlObject inputFindMatchingSearchResultList) {
		XmlObject[] accountDataList_xmlObj=inputFindMatchingSearchResultList.selectPath(accountData_xpath);
		ArrayList<Account> internalAccountDataArray = new ArrayList<Account>();		    	
		for (int j = 0; j < accountDataList_xmlObj.length; j++) {	 	
			XmlObject accountData_xmlObj = accountDataList_xmlObj[j];
			Account account =convert_AccountDataXmlObj_to_InternalAccount(accountData_xmlObj);
			internalAccountDataArray.add(account);			    	
		}
		return internalAccountDataArray;
	}
	//Select all accounts from internalAccountDataArray where  account status =aCCOUNT_STATUS_CD
	private static ArrayList<Account> getMatchingAccountsBy_RuleCond_ACCOUNT_STATUS_CD(ArrayList<Account> internalAccountDataArray,String aCCOUNT_STATUS_CD) {
		if(!aCCOUNT_STATUS_CD.trim().equalsIgnoreCase("ANY")){
			ArrayList<Account> matchAccounts = new ArrayList<Account>();
			for(int j=0; j <internalAccountDataArray.size(); j++){
				Account internalAccountData = internalAccountDataArray.get(j);
				
				String accountStatus = (internalAccountData!=null &&internalAccountData.getAccountStatus()!=null)?internalAccountData.getAccountStatus().trim():null;
				if(aCCOUNT_STATUS_CD.equalsIgnoreCase(accountStatus)){
					matchAccounts.add(internalAccountData);
					String accountNumber = (internalAccountData.getAccountNumber()!=null)?internalAccountData.getAccountNumber().trim():null;
					LogUtil.logInfo("Found Matching BAN="+ accountNumber +" by BAN STATUS="+ aCCOUNT_STATUS_CD);
				}
			}
			return matchAccounts;
		}else{
			return internalAccountDataArray;
		}
	}


	// accounts BANS = 1(jan03) 2(jan01) 3(jan01)  ==> newest = 1 and oldest = 3	
	private static String execute_TopMatchAccount_Tenure_Rule(ArrayList<Account> accounts, String sortingRule){
		String outputAccountNumber=null;
		LogUtil.logInfo("***************execute_TopMatchAccount_Tenure_Rule***************");
		LogUtil.logInfo("Account tenure rule= " + sortingRule);	
		//sort by date ascending 
		Collections.sort(accounts, new Comparator<Account>() {
		  public int compare(Account o1, Account o2) {
		      if (o1 == null || o2 == null)
			        return 0;			  
		      if (o1.getAccountDate() == null || o2.getAccountDate() == null)
		        return 0;
		      return o1.getAccountDate().compareTo(o2.getAccountDate());
		  }
		});		

		if(accounts.size()>0){
			if(sortingRule.equalsIgnoreCase("NEWEST")){
				LogUtil.logInfo("Returning newest account");
				
				outputAccountNumber=accounts.get(accounts.size()-1).getAccountNumber();			
			}
			else{
				LogUtil.logInfo("Returning oldest account");
				outputAccountNumber=accounts.get(0).getAccountNumber();	
			}
		}
		return outputAccountNumber;
}	
	
	//Exclude account from findMatchingSearchResult by  SIN/DL/CC/Name from Refpds table UC_SEARCH_EXCLUSION_RULES
	@SuppressWarnings("unchecked")
	public static XmlObject exclude_MatchedAccounts_per_UC_SEARCH_EXCLUSION_RULES(XmlObject inputFindMatchingSearchResultList){
		LogUtil.logInfo("************Start_exclude_MatchedAccounts_per_UC_SEARCH_EXCLUSION_RULES**************");
		if(inputFindMatchingSearchResultList==null){
			LogUtil.logError("inputFindMatchingSearchResultList is null ");
			return inputFindMatchingSearchResultList;
		}
		boolean anAcctRemovedInd = false;
		try{
			//get list of findMatchingSearchResult	
			XmlObject[] findMatchingSearchResult_array = inputFindMatchingSearchResultList.selectPath(findMatchingSearchResult_xpath);				
			if(findMatchingSearchResult_array!=null && findMatchingSearchResult_array.length>0){
			    //get Refpds values from cache
		        Vector<String> uC_SEARCH_EXCLUSION_RULES_DL   = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_DL_CACHE_KEY);
		        Vector<String> uC_SEARCH_EXCLUSION_RULES_SIN  = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_SIN_CACHE_KEY);
		        Vector<String> uC_SEARCH_EXCLUSION_RULES_CC   = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_CC_CACHE_KEY);
		        Vector<String> uC_SEARCH_EXCLUSION_RULES_NAME = (Vector<String>) ESBCacheHelper.getObjectFromCache(CONSTANTS.UC_SEARCH_EXCLUSION_RULES_NAME_CACHE_KEY);
	
				for (XmlObject findMatchingSearchResult_xobj : findMatchingSearchResult_array) {
					//get CustomerData
					XmlObject[] customerData_List_xmlObj=findMatchingSearchResult_xobj.selectPath(customerData_xpath);
					XmlObject customerData_xmlObj =(customerData_List_xmlObj!=null&& customerData_List_xmlObj.length>0 && customerData_List_xmlObj[0]!=null)?customerData_List_xmlObj[0]:null;					
					
					//get input attributes
					String lastName_TxtVal=getXmlObjectTextValue(customerData_xmlObj, lastName_xpath);
					String firstName_TxtVal=getXmlObjectTextValue(customerData_xmlObj, firstName_xpath);
					String fullName = firstName_TxtVal+ "_" + lastName_TxtVal;					
					String input_CCTokenTxt=getXmlObjectTextValue(customerData_xmlObj, ccToken_xpath);																					
					String aSocialInsuranceNum=getXmlObjectTextValue(customerData_xmlObj, sin_xpath);					
					String[] dl_array=getXmlObjectTextValueArray(customerData_xmlObj, dl_xpath);										
					//apply rules
					boolean nameExlusionInd = foundInExclusion("NAME" ,uC_SEARCH_EXCLUSION_RULES_NAME, fullName);
					boolean ccExlusionInd 	= foundInExclusion("CC" ,uC_SEARCH_EXCLUSION_RULES_CC, input_CCTokenTxt);	
					boolean sinExlusionInd 	= foundInExclusion("SIN" ,uC_SEARCH_EXCLUSION_RULES_SIN, aSocialInsuranceNum);
					boolean dlExlusionInd 	= foundListInExclusion("DL",uC_SEARCH_EXCLUSION_RULES_DL, dl_array);
					//remove findMatchingSearchResult_xobj
					if (ccExlusionInd ||sinExlusionInd || nameExlusionInd ||dlExlusionInd){
						findMatchingSearchResult_xobj.newCursor().removeXml();
						anAcctRemovedInd=true;
						String msg ="";
						msg = msg + ( (nameExlusionInd)?",fullName="+fullName:"") ;
						msg = msg + ( (ccExlusionInd)?",CC="+input_CCTokenTxt:"") ;
						msg = msg + ( (sinExlusionInd)?",SIN="+aSocialInsuranceNum:"") ;
						if(dlExlusionInd){
							String dl_array_Str="";
							for (int i = 0; i < dl_array.length; i++) {
								dl_array_Str = dl_array_Str + ","+dl_array[i];	
							}
							msg = msg + dl_array_Str;
						}
						
						LogUtil.logInfo("Removed customer with matching attributes ["+msg+"]");
						
					}					
				}		
			}					
		} catch (Throwable e){
			LogUtil.logException(e);
		}	
		
		if(!anAcctRemovedInd){
			LogUtil.logInfo("\n UC_MATCH_FILTERING_RULES:No Account was removed.");
		}
		LogUtil.logInfo("\n output FindMatchingSearchResultList-=[\n"+ inputFindMatchingSearchResultList +"\n]");
		LogUtil.logInfo("************End_exclude_MatchedAccounts_per_UC_SEARCH_EXCLUSION_RULES**************");
		return inputFindMatchingSearchResultList;
	}
	

	private static boolean foundListInExclusion(String idName, Vector<String> uC_SEARCH_EXCLUSION_RULES_DL,String[] aDriverLicenseArray) {
		if(uC_SEARCH_EXCLUSION_RULES_DL!=null){
			for (int i=0; i<uC_SEARCH_EXCLUSION_RULES_DL.size(); i++){
				String in = (String) uC_SEARCH_EXCLUSION_RULES_DL.elementAt(i);
				if(aDriverLicenseArray!=null){
					for (int j=0;j<aDriverLicenseArray.length;j++){
						String toCompare = aDriverLicenseArray[j];
						if(toCompare.equalsIgnoreCase(in)){
							LogUtil.logInfo("[" +idName + " Match found: input=" + toCompare + " ,rule value= " + in +"]");
							return true;
						}
					}
				}
			}			
		}
		return false;
	}
	


	
	public static boolean validateMDMResponse(XmlObject findMatchingSearchResultList){
		LogUtil.logInfo("************Start_validateMDMResponse***************");
		boolean validateMDMResponseInd=true;
		if(findMatchingSearchResultList==null){
			LogUtil.logError("input findMatchingSearchResultList is null ");
			return validateMDMResponseInd;
		}
		try{
			XmlObject[] findMatchingSearchResult_xmlObjs = findMatchingSearchResultList.selectPath(findMatchingSearchResult_xpath);				
			if(findMatchingSearchResult_xmlObjs!=null && findMatchingSearchResult_xmlObjs.length>0){			
				for (XmlObject findMatchingSearchResult_xobj : findMatchingSearchResult_xmlObjs) {
					//validateCustomerData
					boolean isCustomerDataValidInd = validateCustomerData(findMatchingSearchResult_xobj);
					boolean isAccountDataValidInd  = validateAccountData(findMatchingSearchResult_xobj);
					if(!isCustomerDataValidInd || !isAccountDataValidInd){	
						validateMDMResponseInd=false;
						break;
					}														
				}	
			}		
		} catch (Throwable e){
			LogUtil.logException(e);
		}		
		LogUtil.logInfo("validateMDMResponse=["+ validateMDMResponseInd +"]");
		LogUtil.logInfo("************End_ValidateMDMResponse**************");
		return validateMDMResponseInd;
	}


	private static boolean validateAccountData( XmlObject findMatchingSearchResult_xobj) {
		boolean isAccountDataValidInd=true;
		XmlObject[] accountDataList_xmlObj=findMatchingSearchResult_xobj.selectPath(accountData_xpath);
		for (XmlObject accountData_xmlObj : accountDataList_xmlObj) {
			String billingMasterSourceId	=getXmlObjectTextValue(accountData_xmlObj, billingMasterSourceId_xpath);	
			String billingAccountNumber		=getXmlObjectTextValue(accountData_xmlObj, billingAccountNumber_xpath);				
			String brandId					=getXmlObjectTextValue(accountData_xmlObj, brandId_xpath);	
			String accountType				=getXmlObjectTextValue(accountData_xmlObj, accountType_xpath);					
			String accountSubType			=getXmlObjectTextValue(accountData_xmlObj, accountSubType_xpath);
			String statusCode				=getXmlObjectTextValue(accountData_xmlObj, statusCode_xpath);
			String accountCreationDate		=getXmlObjectTextValue(accountData_xmlObj, accountCreationDate_xpath);
			String startServiceDate			=getXmlObjectTextValue(accountData_xmlObj, startServiceDate_xpath);				
		    if( 
		    		"".equalsIgnoreCase(billingMasterSourceId)
		    	||  "".equalsIgnoreCase(billingAccountNumber)
		    	||  "".equalsIgnoreCase(brandId)
		    	||  "".equalsIgnoreCase(accountType)
		    	||  "".equalsIgnoreCase(accountSubType)		
		    	||  "".equalsIgnoreCase(statusCode)	
		    	||  "".equalsIgnoreCase(accountCreationDate)		
		    	|| (!"T".equalsIgnoreCase(statusCode) && "".equalsIgnoreCase(startServiceDate))		    				    		    	
		    ){
		    	LogUtil.logInfo("[Invalid MDMResponse.accountData:\n" + accountData_xmlObj +"\n]");
		    	isAccountDataValidInd=false;
		    	break;
		    }
		}
		return isAccountDataValidInd;	
	}


	private static String getXmlObjectTextValue(XmlObject xmlObj,String xpath) {
		String xmlObj_TxtVal="";
		XmlObject[] xpath_xmlObj = xmlObj.selectPath(xpath);
		xmlObj_TxtVal=(xpath_xmlObj!=null && xpath_xmlObj.length>0 && xpath_xmlObj[0]!=null)?xpath_xmlObj[0].newCursor().getTextValue():null;
		return xmlObj_TxtVal;
	}


	private static boolean validateCustomerData( XmlObject findMatchingSearchResult_xobj) {
		boolean isCustomerDataValidInd=true;
		//get CustomerData
		XmlObject[] customerData_List_xmlObj=findMatchingSearchResult_xobj.selectPath(customerData_xpath);
		XmlObject customerData_xmlObj =(customerData_List_xmlObj!=null&& customerData_List_xmlObj.length>0 && customerData_List_xmlObj[0]!=null)?customerData_List_xmlObj[0]:null;					
	
		String firstName_TxtVal=getXmlObjectTextValue(customerData_xmlObj, firstName_xpath);
		String lastName_TxtVal=getXmlObjectTextValue(customerData_xmlObj, lastName_xpath);
		//String input_CCTokenTxt=getXmlObjectTextValue(customerData_xmlObj, ccToken_xpath);
		String dob=getXmlObjectTextValue(customerData_xmlObj, dob_xpath);
		String aSocialInsuranceNum=getXmlObjectTextValue(customerData_xmlObj, sin_xpath);				
		String[] dl_array=getXmlObjectTextValueArray(customerData_xmlObj, dl_xpath);	
	    if( 
	    		"".equalsIgnoreCase(firstName_TxtVal.trim())
	    	||  "".equalsIgnoreCase(lastName_TxtVal.trim())
	    	||  (
	    		   "".equalsIgnoreCase(aSocialInsuranceNum.trim())
	    		&& "".equalsIgnoreCase(dob.trim())
	    		&& dl_array.length==0
	    		)
	    ){
	    	LogUtil.logInfo("[Invalid MDMResponse.customerData:\n" + customerData_xmlObj +"\n]");
	    	isCustomerDataValidInd=false;
	    }
		return isCustomerDataValidInd;
	}


	private static String[] getXmlObjectTextValueArray( XmlObject xmlObj, String xpath) {
		String[] txtArray=new String[0];			
		XmlObject[] xmlObjs=(xmlObj!=null)?xmlObj.selectPath(xpath):null;		
		if(xmlObjs!=null && xmlObjs.length>0){
			txtArray=new String[xmlObjs.length];
			for (int i = 0; i < xmlObjs.length; i++) {
				String dl=(xmlObjs[i]!=null)?xmlObjs[i].newCursor().getTextValue().trim():"";
				if(dl!=null && !"".equalsIgnoreCase(dl)){
					txtArray[i]=dl;	
				}					
			}			
		}
		return txtArray;
	}
	
}
