package com.telus.credit.util.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlObject;

import com.telus.credit.util.CONSTANTS;
import com.telus.credit.util.ESBCacheHelper;
import com.telus.credit.util.UnifiedCreditUtil_v2;

public class Test_UnifiedCreditUtil_v2 extends TestCase {

	private String testDataPath 	="./../esb-util/src/test/data/";		
	private String requestFolder	=testDataPath + "/request/";
	private String responseFolder  	=testDataPath + "/response/";
	
	protected void setUp() throws Exception {
		super.setUp(); 	
    	try {
    		ESBCacheHelper.addObjectToCache(CONSTANTS.WLN_WCDAP_LOGGING_ENABLED_CACHE_KEY, "Y");
    		
    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
			XmlObject refPdsXmlObject = XmlObject.Factory.parse(refPds);
			//load cache from refpds tables
			UnifiedCreditUtil_v2.loadUnifiedCreditCache(refPdsXmlObject);
			
			
			System.out.println("AllObjectFromCache=\n[" + ESBCacheHelper.getAllObjectFromCache() +"\n]");
			System.out.println("\nOperationalDataFromCache=\n[" + ESBCacheHelper.getOperationalDataFromCache() +"\n]");

			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
    }
    
    public void testSetup() {
    	System.out.println("DONE");
    	
    }
	

    public void refpdsTest() throws Exception {
    	try {
			File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
			XmlObject refpdsData = XmlObject.Factory.parse(refPds);
			
			String ns="declare namespace tns='http://xmlschema.tmi.telus.com/srv/ERM/RefPds/ReferencePDSDataServiceRequestResponse_v1_0'";
			String t=
				ns
				+"  .//tns:RefEntity"
				//+"  .//tns:RefEntity[@name='UC_MATCH_SELECTION_RULES']"
				
				//+"  .//tns:RefEntity[@name='UC_MATCH_SELECTION_RULES']/tns:Instance"
				;

				XmlObject[] refEntityArray = refpdsData.selectPath(t);
				for (int i = 0; i < refEntityArray.length; i++) {
					XmlObject xmlObject = refEntityArray[i];
					XmlObject nameAttribute = xmlObject.selectAttribute(new QName("name"));
					String nameAttributevalue = nameAttribute.newCursor().getTextValue();
					if("UC_MATCH_SELECTION_RULES".equalsIgnoreCase(nameAttributevalue)){
						XmlObject UC_MATCH_SELECTION_RULES_refEntity = xmlObject;
					}else{
						if("CREDIT_OPERATION_PARM".equalsIgnoreCase(nameAttributevalue)){
						XmlObject CREDIT_OPERATION_PARM_refEntity = xmlObject;
						
						}			
					}					
				}
				System.out.println("DONE" );
 
    	} catch (Exception e){
    		e.printStackTrace();
    		throw e;
    	}

    }  
    
    
/*	public void exclude_CreditAssessmentRequestAttributes_per_UC_SEARCH_EXCLUSION_RULES_test() throws Exception {
		
    	try {
    		String testFilename="";
    		testFilename="Execute_UC_Exclusion_Attributes_UT_01.xml";
			File  getCreditAssessmentRequestFile = new File(testDataPath + "/exclude_attribute_rules/request/"+ testFilename);
			XmlObject getCreditAssessmentRequestXmlObject = XmlObject.Factory.parse(getCreditAssessmentRequestFile);
						
			XmlObject result = UnifiedCreditUtil_v2.exclude_CreditAssessmentRequestAttributes_per_UC_SEARCH_EXCLUSION_RULES(getCreditAssessmentRequestXmlObject);    				
			File output = new File(testDataPath + "/exclude_attribute_rules/response/", getCreditAssessmentRequestFile.getName() + "_output.xml");
    		if(output.exists()){
    			output.delete();
    		}
    		result.save(output);			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
    }*/
	
	public void creditAssessmentRequest_contains_exclusionData_per_UC_SEARCH_EXCLUSION_RULES_test() throws Exception {
		
    	try {
    		String testFilename="";
    		testFilename="Execute_UC_Exclusion_Attributes_UT_01.xml";
    		testFilename="Execute_UC_Exclusion_Attributes_UT_02.xml";
    		
			File  getCreditAssessmentRequestFile = new File(testDataPath + "/exclude_attribute_rules/request/"+ testFilename);
			XmlObject getCreditAssessmentRequestXmlObject = XmlObject.Factory.parse(getCreditAssessmentRequestFile);
						
			 boolean exclusionDataFoundInd = UnifiedCreditUtil_v2.creditAssessmentRequest_contains_exclusionData_per_UC_SEARCH_EXCLUSION_RULES(getCreditAssessmentRequestXmlObject);    				
			 System.out.println("Test Result : exclusionDataFoundInd="+ exclusionDataFoundInd);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}	
    }
	public void exclude_MatchedAccounts_per_UC_MATCH_FILTERING_RULES_test() throws Exception {
    	try {
    		String testFilename;
    		//testFilename="findMatchingSearchResultList_BR_IR_Accounts.xml";
    		//testFilename="findMatchingSearchResultList_BR_AcctTypeSubtype.xml";
    		//testFilename="findMatchingSearchResultList_noAccounts.xml";
    		//testFilename="findMatchingSearchResultList_2_Customers_BR_IR_Accounts.xml";
    		testFilename="Execute_UC_Match_Filtering_Rules_UT_01.xml";
    		testFilename="Execute_UC_Match_Filtering_Rules_UT_02.xml";
    		testFilename="Execute_UC_Match_Filtering_Rules_UT_03.xml";
    		//testDataPath
//    		File  findMatchingSearchResultListFile = new File(requestFolder + "/matching_account_rules/"+ testFilename);
    		File  findMatchingSearchResultListFile = new File(testDataPath + "/matching_account_rules/request/"+ testFilename);
			XmlObject getCreditAssessmentRequestXmlObject = XmlObject.Factory.parse(findMatchingSearchResultListFile);
						
			XmlObject result = UnifiedCreditUtil_v2.exclude_MatchedAccounts_per_UC_MATCH_FILTERING_RULES(getCreditAssessmentRequestXmlObject);    					
			File output = new File(testDataPath + "/matching_account_rules/response", findMatchingSearchResultListFile.getName() + "_output.xml");
    		if(output.exists()){
    			output.delete();
    		}
    		result.save(output);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}	
    }	

	  public void select_top_MatchedAccount_per_UC_MATCH_SELECTION_RULES_test() {
	    	System.out.println("execute_top_match_rules");
    		String testFilename;
    		//testFilename="request_2OBAN.xml"; 
    		//testFilename="request_2TBAN.xml";    		
    		//testFilename="request_O_S_BANs.xml"; 
    		//testFilename="request_T_S_BANs.xml";
    		//testFilename="request_T_ANY_BANs.xml";
    		//testFilename="request_ANY_ANY_BANs.xml";
    		testFilename="selectTopMatchAccount_UT_01.xml";
    		testFilename="selectTopMatchAccount_UT_02.xml";
    		testFilename="selectTopMatchAccount_UT_03.xml";
    		testFilename="selectTopMatchAccount_UT_04.xml";
    		//testFilename="emptyfindMatchingSearchResultList.xml";
    		
    	    		//testFilename="issue.xml"; 
			File  testFile = new File(testDataPath + "/topMatch_rules/request/"+ testFilename);
	    	    	try {
	    	    		XmlObject result = UnifiedCreditUtil_v2.select_top_MatchedAccount_per_UC_MATCH_SELECTION_RULES(XmlObject.Factory.parse(testFile));
	    	    		System.out.println("result=" + result);
	    	    		File output = new File(testDataPath + "/topMatch_rules/response/", testFile.getName() + "_output.xml");
	    	    		if(output.exists()){
	    	    			output.delete();
	    	    		}
	    	    		if(result!=null){
	    	    			result.save(output);
	    	    		}
	    	    	} catch (Exception e){
	    	    		e.printStackTrace();
	    	    		System.out.println(e.getMessage());
	    	    		File file = new File(testDataPath + "/topMatch_rules/response/", testFile.getName() + "_error.xml");
	    	    		FileWriter writer;
						try {
							writer = new FileWriter(file);
		    	            writer.append(e.getMessage());
		    	            writer.flush();
		    	            writer.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
	    	    	}

	    }

	
	    public void exclude_MatchedAccounts_per_UC_SEARCH_EXCLUSION_RULES_test() throws Exception {
	    	System.out.println("execute_uc_exclusion_rules_account");
	    	//ESBCacheHelper.clearCache();
	    	String testFilename;
	    	testFilename ="Execute_UC_Exclusion_Account_UT_01.xml";
	    	File  testFile = new File(testDataPath + "/exculde_account_by_attribute_rules/request/"+ testFilename);
	    	XmlObject testFileXmlObject=null;
	    	try {
				testFileXmlObject = XmlObject.Factory.parse(testFile);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
	    	try {
	    		XmlObject result =  UnifiedCreditUtil_v2.exclude_MatchedAccounts_per_UC_SEARCH_EXCLUSION_RULES(testFileXmlObject);
	    		File output = new File(testDataPath + "/exculde_account_by_attribute_rules/response", testFile.getName() + "_output.xml");
	    		if(output.exists()){
	    			output.delete();
	    		}
	    		result.save(output);
	    	} catch (Exception e){
	    		System.out.println(e.getMessage());
	    		File file = new File(testDataPath + "/exculde_account_by_attribute_rules/response", testFile.getName() + "_error.xml");
	    		FileWriter writer;
				try {
					writer = new FileWriter(file);
		            writer.append(e.getMessage());
		            writer.flush();
		            writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					throw e;
				}
	    	}

	    }
	    
	    public void validateMDMResponse_test() throws Exception {
	        	try {
	        		String testFilename;
	        		testFilename="findMatchingSearchResultList_Template.xml";
	        		
	        		File  findMatchingSearchResultListFile = new File(requestFolder + "/validateMDMResponse/"+ testFilename);
	    			XmlObject testXmlObj = XmlObject.Factory.parse(findMatchingSearchResultListFile);
	    						
	    			 boolean result = UnifiedCreditUtil_v2.validateMDMResponse(testXmlObj);    					
			
	    		} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    			throw e;
	    		}	
	        }
}
