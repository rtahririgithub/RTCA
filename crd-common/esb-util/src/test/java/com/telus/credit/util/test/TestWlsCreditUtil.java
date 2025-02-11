/*package com.telus.credit.util.test;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.telus.credit.util.UnifiedCreditUtil;


public class TestWlsCreditUtil extends TestCase{
	String testDataPath 	="./../esb-util/src/test/data/";	
	
	String requestFolder	=testDataPath + "/request/";
	String responseFolder  	=testDataPath + "/response/";
	
	public void executeAll(){
    	System.out.println("executeAll");
		try{
			loadRefPdsforUC_Search_Exclusion();
			execute_uc_exclusion_rules_attributes();
			execute_uc_exclusion_rules_account();
			loadRefPdsforUC_Match_Inclusion();
			execute_uc_include_matching_account_rules();
			loadRefPdsforUC_top_match_rules();
			execute_top_match_rules();
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
	}

    public void loadRefPdsforUC_Search_Exclusion() {
    	System.out.println("loadRefPdsforUC_Search_Exclusion");
    	try {
    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    		UnifiedCreditUtil.loadData(XmlObject.Factory.parse(refPds));
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    }

    public void execute_uc_exclusion_rules_attributes() {
    	System.out.println("execute_uc_exclusion_rules_attributes");
    	File [] inputFiles = new File(requestFolder + "/exclude_attribute_rules/").listFiles();
    	for(int i=0; i<inputFiles.length; i++){
    		if(inputFiles[i].isFile() && inputFiles[i].getName().endsWith("xml")){
    	    	try {
    	    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    	    		XmlObject result =  UnifiedCreditUtil.execute_uc_exclusion_rules_attributes(XmlObject.Factory.parse(inputFiles[i]),XmlObject.Factory.parse(refPds));
    	    		File output = new File(responseFolder + "/exclude_attribute_rules/", inputFiles[i].getName() + "_output.xml");
    	    		if(output.exists()){
    	    			output.delete();
    	    		}
    	    		result.save(output);
    	    	} catch (Exception e){
    	    		System.out.println(e.getMessage());
    	    		File file = new File(responseFolder + "/exclude_attribute_rules/", inputFiles[i].getName() + "_error.xml");
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
    	}
    }

    public void execute_uc_exclusion_rules_account() {
    	System.out.println("execute_uc_exclusion_rules_account");
    	File [] inputFiles = new File(requestFolder + "/exculde_account_by_attribute_rules/").listFiles();
    	for(int i=0; i<inputFiles.length; i++){
    		if(inputFiles[i].isFile() && inputFiles[i].getName().endsWith("xml")){
    	    	try {
    	    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    	    		XmlObject result =  UnifiedCreditUtil.execute_uc_exclusion_rules_account(XmlObject.Factory.parse(inputFiles[i]),XmlObject.Factory.parse(refPds));
    	    		File output = new File(responseFolder + "/exculde_account_by_attribute_rules/", inputFiles[i].getName() + "_output.xml");
    	    		if(output.exists()){
    	    			output.delete();
    	    		}
    	    		result.save(output);
    	    	} catch (Exception e){
    	    		System.out.println(e.getMessage());
    	    		File file = new File(responseFolder + "/exculde_account_by_attribute_rules/", inputFiles[i].getName() + "_error.xml");
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
    	}
    }

    public void loadRefPdsforUC_Match_Inclusion() {
    	System.out.println("loadRefPdsforUC_Matching_Inclusion");
    	try {
    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    		UnifiedCreditUtil.loadMatchingAccountsData(XmlObject.Factory.parse(refPds));
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    }
    
    public void execute_uc_include_matching_account_rules() {
    	System.out.println("execute_uc_include_matching_account_rules");
    	File [] inputFiles = new File(requestFolder + "/matching_account_rules/").listFiles();
    	for(int i=0; i<inputFiles.length; i++){
    		if(inputFiles[i].isFile() && inputFiles[i].getName().endsWith("xml")){
    	    	try {
    	    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    	    		XmlObject result =  UnifiedCreditUtil.execute_uc_include_matching_account_rules(XmlObject.Factory.parse(inputFiles[i]),XmlObject.Factory.parse(refPds));
    	    		File output = new File(responseFolder + "/matching_account_rules/", inputFiles[i].getName() + "_output.xml");
    	    		if(output.exists()){
    	    			output.delete();
    	    		}
    	    		result.save(output);
    	    	} catch (Exception e){
    	    		System.out.println(e.getMessage());
    	    		File file = new File(responseFolder + "/matching_account_rules/", inputFiles[i].getName() + "_error.xml");
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
    	}
    }

    public void loadRefPdsforUC_top_match_rules() {
    	System.out.println("loadRefPdsforUC_top_match_rules");
    	try {
    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    		UnifiedCreditUtil.loadTopMatchRules(XmlObject.Factory.parse(refPds));
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    }

    public void validateMDMResponse() {
    	System.out.println("Validate MDM response");
    	try {
    		File request = new File(requestFolder + "/validate/", "request1.xml");
    		UnifiedCreditUtil.validateMDMResponse(XmlObject.Factory.parse(request));
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    }
    
    public void validateMDMInputOutput() {
    	System.out.println("Validate MDM Input Output");
    	try {
    		File request = new File(requestFolder + "/validate/", "request.xml");
    		File response = new File(requestFolder + "/validate/", "response.xml");
    		XmlOptions opts = new XmlOptions();
//    		opts.setCharacterEncoding("ISO-8859-1");
  //  		String s = new String(new byte[] {(byte)0xc3, 0x3c}, 
    //				"iso-8859-1");
//    				System.out.println(s);
    		UnifiedCreditUtil.validateMDMRequestResponse("Test",XmlObject.Factory.parse(response,opts));
    	} catch (Exception e){
    		System.out.println(e.getMessage());
    	}
    }
    public void execute_top_match_rules() {
    	System.out.println("execute_top_match_rules");
    	File [] inputFiles = new File(requestFolder + "/topMatch_rules/").listFiles();
    	for(int i=0; i<inputFiles.length; i++){
    		if(inputFiles[i].isFile() && inputFiles[i].getName().endsWith("xml")){
    	    	try {
    	    		File refPds = new File(requestFolder + "/refPds/", "refPds.xml");
    	    		XmlObject result = UnifiedCreditUtil.execute_top_match_account_rules(XmlObject.Factory.parse(inputFiles[i]), XmlObject.Factory.parse(refPds));
    	    		File output = new File(responseFolder + "/topMatch_rules/", inputFiles[i].getName() + "_output.xml");
    	    		if(output.exists()){
    	    			output.delete();
    	    		}
    	    		result.save(output);
    	    	} catch (Exception e){
    	    		System.out.println(e.getMessage());
    	    		File file = new File(responseFolder + "/topMatch_rules/", inputFiles[i].getName() + "_error.xml");
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
    	}
    }

}
*/