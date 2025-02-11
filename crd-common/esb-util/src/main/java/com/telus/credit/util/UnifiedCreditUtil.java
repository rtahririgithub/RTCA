/*package com.telus.credit.util;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UnifiedCreditUtil {
	public static Vector drivingLicenses;
	public static Vector sin;
	public static Vector cc;
	public static Vector name;
	public static String[] wls_acct_type_subtype;
	public static String [][] topMatchRules;
	
	
	public static XmlObject execute_uc_exclusion_rules_attributes(XmlObject input, XmlObject refpdsData){
		try{
			loadData(refpdsData);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
		            .newInstance();
			docBuilderFactory.setNamespaceAware(true);
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(input.newInputStream());

		    NodeList dlNodeList = document.getElementsByTagNameNS("*", CONSTANTS.driverLicenseNum);
		    System.out.println("********");
		    System.out.println("Total Driving licenses in Request = " + dlNodeList.getLength());

		    for (int i = 0; i < dlNodeList.getLength(); i++) {
		        Node node = dlNodeList.item(i);
		            System.out.println("DL Before = " + node.getTextContent());
		            if(foundInExclusion(drivingLicenses, node.getTextContent())){
		            	node.setTextContent("");
		            }
		            System.out.println("DL After = " + node.getTextContent());
		    }

		    NodeList sinNodeList = document.getElementsByTagNameNS("*", CONSTANTS.sin);
		    System.out.println("********");
		    System.out.println("Total SIN found in Request = " + sinNodeList.getLength());

		    for (int i = 0; i < sinNodeList.getLength(); i++) {
		        Node node = sinNodeList.item(i);
		            System.out.println("SIN Before = " + node.getTextContent());
		            if(foundInExclusion(sin, node.getTextContent())){
		            	node.setTextContent("");
		            }
		            System.out.println("SIN After = " + node.getTextContent());
		    }

		    NodeList ccNodeList = document.getElementsByTagNameNS("*", "ccToken");
		    System.out.println("********");
		    System.out.println("Total CC token found in Request = " + ccNodeList.getLength());

		    for (int i = 0; i < ccNodeList.getLength(); i++) {
		        Node node = ccNodeList.item(i);
		            System.out.println("CC Token Before = " + node.getTextContent());
		            if(foundInExclusion(cc, node.getTextContent())){
		            	node.setTextContent("");
		            }
		            System.out.println("CC Token After = " + node.getTextContent());
		    }
		    
		    NodeList fnNodeList = document.getElementsByTagNameNS("*", CONSTANTS.firstName);
		    NodeList lnNodeList = document.getElementsByTagNameNS("*", CONSTANTS.lastName);
		    System.out.println("********");
		    System.out.println("Total firstName found in Request = " + fnNodeList.getLength());
		    System.out.println("Total lastName found in Request = " + lnNodeList.getLength());

		    for (int i = 0; i < fnNodeList.getLength(); i++) {
		        Node nodeFN = fnNodeList.item(i);
		        Node nodeLN = lnNodeList.item(i);
		        String fullName = nodeFN.getTextContent() + "_" + nodeLN.getTextContent();
		            System.out.println("Name Before = " + fullName);
		            if(foundInExclusion(name, fullName)){
		            	nodeFN.setTextContent("");
		            	nodeLN.setTextContent("");
		            }
		            System.out.println("First Name After = " + nodeFN.getTextContent());
		            System.out.println("Last Name After = " + nodeLN.getTextContent());
		    }
		    
		    
		    return XmlObject.Factory.parse(document);
		} catch (Exception e){
			System.out.println("Error :" + e.getMessage());
			return input;
		}

	}
	
//	public static XmlObject execute_uc_exclusion_rules_account(){
	public static XmlObject execute_uc_exclusion_rules_account(XmlObject input, XmlObject refpdsData){
		try{
			loadData(refpdsData);
//			loadData();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
		            .newInstance();
			docBuilderFactory.setNamespaceAware(true);
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(input.newInputStream());
//		    Document document = docBuilder.parse(new File("C:\\input.xml"));

		    NodeList findMatchingSearchResultNodeList = document.getElementsByTagNameNS("*", CONSTANTS.findMatchingSearchResult);
		    System.out.println("********");
		    System.out.println("Total findMatchingSearchResult in Request = " + findMatchingSearchResultNodeList.getLength());

		    for (int i = 1; i <=findMatchingSearchResultNodeList.getLength(); i++) {
		    	
		    	Element findMatchingSearchResultNode = (Element) findMatchingSearchResultNodeList.item(i-1);
		    	NodeList dlNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.driverLicenseNumber);
			    NodeList sinNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.socialInsuranceNum);
			    NodeList ccNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.creditCardTokenTxt);
			    NodeList fnNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.firstName);
			    NodeList lnNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.lastName);
		        String fullName = null;
		        if(fnNodeList.getLength()>0 && lnNodeList.getLength()>0){
		        	fullName = fnNodeList.item(0).getTextContent() + "_" + lnNodeList.item(0).getTextContent();
		        }
		    	
		    	
		        if(dlNodeList.getLength()>0 && foundDLInExclusion(drivingLicenses, dlNodeList)){
		        	System.out.println("Account excluded due to DL = " + dlNodeList.item(0).getTextContent());
		        	findMatchingSearchResultNode.getParentNode().removeChild(findMatchingSearchResultNode);
		        	document.normalize();
		        	i = i-1;
		        } else if(sinNodeList.getLength()>0 && foundInExclusion(sin, sinNodeList.item(0).getTextContent())){
		        	System.out.println("Account excluded due to SIN = " + sinNodeList.item(0).getTextContent());
		        	findMatchingSearchResultNode.getParentNode().removeChild(findMatchingSearchResultNode);
		        	document.normalize();
		        	i = i-1;
		        } else if(ccNodeList.getLength()>0 && foundInExclusion(cc, ccNodeList.item(0).getTextContent())){
		        	System.out.println("Account excluded due to CC = " + ccNodeList.item(0).getTextContent());
		        	findMatchingSearchResultNode.getParentNode().removeChild(findMatchingSearchResultNode);
		        	document.normalize();
		        	i = i-1;
		        } else if(fullName!=null && foundInExclusion(name, fullName)){
		        	System.out.println("Account excluded due to name = " + fullName);
		        	findMatchingSearchResultNode.getParentNode().removeChild(findMatchingSearchResultNode);
		        	document.normalize();
		        	i = i-1;
		        }
		    }
		    System.out.println(getStringFromDocument(document));
	    
		    return XmlObject.Factory.parse(document);
		} catch (Exception e){
			System.out.println("Error :" + e.getMessage());
			return input;
		}

	}

	private static boolean foundInExclusion(Vector vector, String toCompare){
		for (int i=0; i<vector.size(); i++){
			String in = (String) vector.elementAt(i);
			System.out.println("Comparing " + toCompare + " with " + in);
			if(toCompare.equalsIgnoreCase(in)){
				return true;
			}
		}
		return false;
	}
	
	private static boolean foundDLInExclusion(Vector vector, NodeList dlNodeList){
		for (int i=0; i<vector.size(); i++){
			String in = (String) vector.elementAt(i);
			for (int j=0;j<dlNodeList.getLength();j++){
				String toCompare = dlNodeList.item(j).getTextContent();
				if(toCompare.equalsIgnoreCase(in)){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean foundInInclusion(String [] vector, String toCompare){
		for (int i=0; i<vector.length; i++){
			String in = vector[i];
			if(toCompare.equalsIgnoreCase(in)){
				return true;
			}
		}
		return false;
	}

	public static void loadData(XmlObject refpdsData) throws Exception{
		
		drivingLicenses = new Vector();
		sin = new Vector();
		cc = new Vector();
		name = new Vector();
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
		            .newInstance();
			docBuilderFactory.setNamespaceAware(true);
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(refpdsData.newInputStream());
		    NodeList nodeList = document.getElementsByTagNameNS("*", CONSTANTS.RefEntity);

            System.out.println("RefEntity Length = " + nodeList.getLength());
            
		    for (int i = 0; i < nodeList.getLength(); i++) {
		    	Element element = (Element) nodeList.item(i);
		        if(element.getAttribute("name").equals("UC_SEARCH_EXCLUSION_RULES")){

		        	NodeList instances = element.getElementsByTagNameNS("*", CONSTANTS.Instance); 
		            System.out.println("Instances length = " + instances.getLength());
				    for (int j = 0; j < instances.getLength(); j++) {
				    	
//			            System.out.println(instances.item(j).getChildNodes().item(1).getTextContent().trim());
//			            System.out.println(instances.item(j).getChildNodes().item(3).getTextContent().trim());
			            for(int k=0;k<instances.item(j).getChildNodes().getLength();k++){
				            System.out.println("***********************************************************");
				            System.out.println(k +" = " + instances.item(j).getChildNodes().item(k).getTextContent().trim());
				            System.out.println("***********************************************************");
			   
				            if(instances.item(j).getChildNodes().item(k).getTextContent().trim().equalsIgnoreCase("DL")){
				            	System.out.println(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            	drivingLicenses.add(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            } else if(instances.item(j).getChildNodes().item(k).getTextContent().trim().equalsIgnoreCase("SIN")){
				            	System.out.println(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            	sin.add(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            } else if(instances.item(j).getChildNodes().item(k).getTextContent().trim().equalsIgnoreCase("CC")){
				            	System.out.println(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            	cc.add(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            } else if(instances.item(j).getChildNodes().item(k).getTextContent().trim().equalsIgnoreCase("FIRST_LAST_NAME")){
				            	System.out.println(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            	name.add(instances.item(j).getChildNodes().item(k+1).getTextContent().trim());
				            }
			            }
				    }
		        }
		    }
		    
		    System.out.println("Driving licenses loaded = " + drivingLicenses.size());
		    System.out.println("SINs loaded = " + sin.size());
		    System.out.println("Cards loaded = " + cc.size());
		    System.out.println("Names loaded = " + name.size());
		    
	}
	
	public static void loadMatchingAccountsData(XmlObject refpdsData) throws Exception{
//	public static void loadMatchingAccountsData() throws Exception{
			

				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
			            .newInstance();
				docBuilderFactory.setNamespaceAware(true);
			    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			    Document document = docBuilder.parse(refpdsData.newInputStream());
//			    Document document = docBuilder.parse(new File("C:\\req.xml"));
			    
			    NodeList nodeList = document.getElementsByTagNameNS("*", CONSTANTS.RefEntity);

	            System.out.println("RefEntity Length = " + nodeList.getLength());
	            
			    for (int i = 0; i < nodeList.getLength(); i++) {
			    	Element element = (Element) nodeList.item(i);
			        if(element.getAttribute("name").equals("UC_MATCH_FILTERING_RULES")){

			        	NodeList instances = element.getElementsByTagNameNS("*", CONSTANTS.Instance); 
			            System.out.println("Instances length = " + instances.getLength());
					    for (int j = 0; j < instances.getLength(); j++) {
					    	
//				            System.out.println(instances.item(j).getChildNodes().item(1).getTextContent().trim());
//				            System.out.println(instances.item(j).getChildNodes().item(3).getTextContent().trim());
					    	Element singleInstance = (Element) instances.item(j);
					    	NodeList input = singleInstance.getElementsByTagNameNS("*",CONSTANTS.input);
					    	Element inputFilterName = (Element) input.item(0);
					    	Element inputFilterType = (Element) input.item(1);
					    	
					    	if(inputFilterName.getAttribute("code").equals("FILTER_NAME") && inputFilterName.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("WLS_ACCT_TYPE_SUBTYPE") && inputFilterType.getAttribute("code").equals("FILTER_TYPE") && inputFilterType.getElementsByTagNameNS("*", "value").item(0).getTextContent().equalsIgnoreCase("I")){
					            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
					            NodeList output = singleInstance.getElementsByTagNameNS("*", CONSTANTS.output);
					            System.out.println(output.getLength());
					            Element outputElement = (Element) output.item(0);
					            NodeList outputValues = outputElement.getElementsByTagNameNS("*", "value");
					            wls_acct_type_subtype = new String[outputValues.getLength()];
							    for (int k = 0; k < outputValues.getLength(); k++) {
//							    	System.out.println(outputValues.item(k).getTextContent());
							    	wls_acct_type_subtype[k] = outputValues.item(k).getTextContent();
							    }
					    	}
					    	
					    }
			        }
			    }
			    
			    System.out.println("Account Types Subtypes loaded = " + wls_acct_type_subtype.length);
			    for(int u=0;u<wls_acct_type_subtype.length;u++){
			    	System.out.println("'" + wls_acct_type_subtype[u].trim() + "'");
			    }
			    
		}

	public static XmlObject execute_uc_include_matching_account_rules(XmlObject input, XmlObject refpdsData){
		try{
			System.out.println("************Starting execute_uc_include_matching_account_rules***************");
			loadMatchingAccountsData(refpdsData);
			System.out.println(input);
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
		            .newInstance();
			docBuilderFactory.setNamespaceAware(true);
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(input.newInputStream());

		    NodeList findMatchingSearchResultNodeList = document.getElementsByTagNameNS("*", CONSTANTS.findMatchingSearchResult);
		    System.out.println("********");
		    System.out.println("Total findMatchingSearchResult in Request = " + findMatchingSearchResultNodeList.getLength());

		    for (int i = 0; i <findMatchingSearchResultNodeList.getLength(); i++) {
		    	
		    	Element findMatchingSearchResultNode = (Element) findMatchingSearchResultNodeList.item(i);
		    	NodeList accountSubTypeNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.accountSubType);

		    	if(accountSubTypeNodeList.getLength()>0){
		        	for (int j = 1; j <=accountSubTypeNodeList.getLength(); j++) {
		        		Element account = (Element) accountSubTypeNodeList.item(j-1);
			        	if(!foundInInclusion(wls_acct_type_subtype, account.getTextContent())){
				        	System.out.println("Account SubType in result node NOT found in refpds match = " + accountSubTypeNodeList.item(0).getTextContent());
				        	account.getParentNode().removeChild(account);
				        	document.normalize();
				        	j = j-1;
			        	}
		        	}
		        }
		    }
		    System.out.println(getStringFromDocument(document));
	    
			System.out.println("************Ending execute_uc_include_matching_account_rules***************");
		    return XmlObject.Factory.parse(document);
		} catch (Exception e){
			System.out.println("Error :" + e.getMessage());
			System.out.println("************Exception execute_uc_include_matching_account_rules***************");
			return input;
		}

	}

	
	//method to convert Document to String
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	}

	//Used for testing if XMLObject contents need to be written to a file
	public static void writeXmlFile(XmlObject docObject, String targetFilePath) {
		System.out.println(docObject.toString());
        File xmlFile = new File(targetFilePath);


        XmlOptions xmlOptions = new XmlOptions();
        xmlOptions.setSavePrettyPrint();


        try {
            docObject.save(xmlFile, xmlOptions);
        } catch (IOException e) {
			System.out.println("Write File method Error " + e.getMessage());
            e.printStackTrace();
        }
    }

	public static void loadTopMatchRules(XmlObject refpdsData) throws Exception{
				
	
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				            .newInstance();
					docBuilderFactory.setNamespaceAware(true);
				    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
				    Document document = docBuilder.parse(refpdsData.newInputStream());
				    
				    NodeList nodeList = document.getElementsByTagNameNS("*", CONSTANTS.RefEntity);
	
		            System.out.println("RefEntity Length = " + nodeList.getLength());
		            
				    for (int i = 0; i < nodeList.getLength(); i++) {
				    	Element element = (Element) nodeList.item(i);
				        if(element.getAttribute("name").equals("UC_MATCH_SELECTION_RULES")){
	
				        	NodeList instances = element.getElementsByTagNameNS("*", CONSTANTS.Instance); 
				            System.out.println("Instances length = " + instances.getLength());
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
				        }
				    }
				    
				    System.out.println("Top matching rules loaded = " + topMatchRules.length);
				    //Sorting the rules by Rule number
				    Arrays.sort(topMatchRules, new Comparator<String[]>() {
				        public int compare(String[] s1, String[] s2) {
				        	System.out.println("Array Sort Checking " + s1[0] + " and " + s2[0]);
				            if (Integer.parseInt(s1[0]) > Integer.parseInt(s2[0]))
				                return 1;
				            else if (Integer.parseInt(s1[0]) < Integer.parseInt(s2[0]))
				                return -1;
				            else {
				                return 0;
				            }
				        }
				    });
				    
				    for (int x = 0; x < topMatchRules.length; x ++) {
				        String subArray[] = topMatchRules[x]; 
				        System.out.println( "Length of array " + x + " is " + subArray.length );
				        for (int y = 0; y < subArray.length; y ++) {
				            String item = subArray[y];
				            System.out.println( "  Item " +y + " is " + item );
				        }
				    }				    
			}

	public static String validateMDMRequestResponse(String lastNameInInput, XmlObject output){
		System.out.println("validateMDMRequestResponse");
		String response = "true";
		System.out.println("Last name in input = " + lastNameInInput);
		String lastNameInOutput = findLastName(output);
		System.out.println("Last name in output = " + lastNameInOutput);

		if(lastNameInInput==null || lastNameInOutput==null || !lastNameInInput.equals(lastNameInOutput)){
			response = "false";
		}
		System.out.println(response);
		return response;
	}
	
	public static String findLastName(XmlObject xmlObject){
		String response = null;
		try{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
		            .newInstance();
			docBuilderFactory.setNamespaceAware(true);
		    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		    Document document = docBuilder.parse(xmlObject.newInputStream());
		    NodeList characteristicValues = document.getElementsByTagNameNS("*", CONSTANTS.characteristicValue);
		    for (int i = 0; i <characteristicValues.getLength(); i++) {
		    	Element characteristicValue = (Element) characteristicValues.item(i);
			    NodeList lnNodeList = characteristicValue.getElementsByTagNameNS("*", CONSTANTS.name);
			    if(lnNodeList.getLength()>0 && lnNodeList.item(0).getTextContent().equals(CONSTANTS.lastName)){
			    	response = characteristicValue.getElementsByTagNameNS("*", "Value").item(0).getTextContent();
			    	break;
			    }
		    }
		} catch(Exception e){
			e.printStackTrace();
			return response;
		}
		return response;
	}
		  
	public static String validateMDMResponse(XmlObject input){
			String response = "true";
			
			try{
					DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
			            .newInstance();
				docBuilderFactory.setNamespaceAware(true);
			    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			    Document document = docBuilder.parse(input.newInputStream());
	
			    NodeList findMatchingSearchResultNodes = document.getElementsByTagNameNS("*", CONSTANTS.findMatchingSearchResult);
			    for (int i = 0; i <findMatchingSearchResultNodes.getLength(); i++) {
			    	
			    	Element findMatchingSearchResultNode = (Element) findMatchingSearchResultNodes.item(i);
				    NodeList fnNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.firstName);
				    NodeList lnNodeList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.lastName);
				    NodeList dlList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.driverLicenseNumber);
				    NodeList sinList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.socialInsuranceNum);
				    NodeList birthDateList = findMatchingSearchResultNode.getElementsByTagNameNS("*", CONSTANTS.birthDate);
				    NodeList acctList = findMatchingSearchResultNode.getElementsByTagNameNS("*", "accountData");
				    if(fnNodeList.getLength()==0 || fnNodeList.item(0)==null || fnNodeList.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    	System.out.println("Missing First Name");
				    	response = "false";
				    }
				    if(lnNodeList.getLength()==0 || lnNodeList.item(0)==null || lnNodeList.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    	System.out.println("Missing Last Name");
				    	response = "false";
				    }
				    if(dlList.getLength()==0 && sinList.getLength()==0 && birthDateList.getLength()==0){
				    	System.out.println("Missing DL, SIN or BirthDate");
				    	response = "false";
				    }
				    if(acctList.getLength()>0){
				    for (int j=0; j<acctList.getLength(); j++){
				    	Element account = (Element) acctList.item(j);
				    	NodeList accountMasterSrcId = account.getElementsByTagNameNS("*", "billingMasterSourceId");
				    	NodeList accountNumber = account.getElementsByTagNameNS("*", "billingAccountNumber");
				    	NodeList brandId = account.getElementsByTagNameNS("*", "brandId");
				    	NodeList accountType = account.getElementsByTagNameNS("*", "accountType");
				    	NodeList accountSubType = account.getElementsByTagNameNS("*", "accountSubType");
				    	NodeList accountCreationDate = account.getElementsByTagNameNS("*", "accountCreationDate");
				    	NodeList startServiceDate = account.getElementsByTagNameNS("*", "startServiceDate");
				    	NodeList statusCode = account.getElementsByTagNameNS("*", "statusCode");
				    	if(accountMasterSrcId.getLength()==0 || accountMasterSrcId.item(0)==null || accountMasterSrcId.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing accountMasterSrcId");
				    	}
				    	if(accountNumber.getLength()==0 || accountNumber.item(0)==null || accountNumber.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing accountNumber");
				    	}
				    	if(brandId.getLength()==0 || brandId.item(0)==null || brandId.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing BrandId");
				    	}
				    	if(accountType.getLength()==0 || accountType.item(0)==null || accountType.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing accounType");
				    	}
				    	if(accountSubType.getLength()==0 || accountSubType.item(0)==null || accountSubType.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing accountSubType");
				    	}
				    	if(statusCode.getLength()==0 || statusCode.item(0)==null || statusCode.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing statusCode");
				    	}
				    	if(accountCreationDate.getLength()==0 || accountCreationDate.item(0)==null || accountCreationDate.item(0).getTextContent().trim().equalsIgnoreCase("")){
				    		response = "false";
					    	System.out.println("Missing accountCreationDate");
				    	}
				    	if(!statusCode.item(0).getTextContent().trim().equalsIgnoreCase("T") && (startServiceDate.getLength()==0 || startServiceDate.item(0)==null || startServiceDate.item(0).getTextContent().trim().equalsIgnoreCase(""))){
				    		response = "false";
					    	System.out.println("Missing startServiceDate");
				    	}
				    }
				    }
				    else{
			    		response = "false";
				    	System.out.println("Missing accountData");
				    }
			    }
			} catch(Exception e){
				e.printStackTrace();
				return response;
			}
			System.out.println(response);
			return response;
		}
		public static XmlObject execute_top_match_account_rules(XmlObject input, XmlObject refpdsData){
			try{
				loadTopMatchRules(refpdsData);
				DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
			            .newInstance();
				docBuilderFactory.setNamespaceAware(true);
			    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			    Document document = docBuilder.parse(input.newInputStream());
			    
			    NodeList findMatchingSearchResultNodeList = document.getElementsByTagNameNS("*", CONSTANTS.findMatchingSearchResult);
//			    System.out.println("********");
			    System.out.println("Total findMatchingSearchResult in Request = " + findMatchingSearchResultNodeList.getLength());
		    	NodeList accountsList = document.getElementsByTagNameNS("*", "accountData");
			    System.out.println("Total accounts in Request before duplicate check = " + accountsList.getLength());
//Duplicate Check
			    for (int i = 0; i < accountsList.getLength(); i++) {
			    	Element singleAccount1 = (Element) accountsList.item(i);
			    	String ban1 = singleAccount1.getElementsByTagNameNS("*", CONSTANTS.billingAccountNumber).item(0).getTextContent().trim();
			        for (int j = i+1; j <accountsList.getLength(); j++) {
				    	Element singleAccount2 = (Element) accountsList.item(j);
				    	String ban2 = singleAccount2.getElementsByTagNameNS("*", CONSTANTS.billingAccountNumber).item(0).getTextContent().trim();
			            if (ban1.equals(ban2)) {
			            	System.out.println("Removing Duplicate Account " + ban2);
			            	singleAccount2.getParentNode().removeChild(singleAccount2);
			            	j=j-1;
			            }
			        }
			    }
//**************************************************************************	
			    System.out.println("Total accounts in Request after duplicate check = " + accountsList.getLength());
			    	ArrayList<Account> accountsInResultNode = new ArrayList<Account>();
//			    	System.out.println("Accounts in findMatchingSearchResultNode = "+accountsList.getLength());


				    for (int j = 0; j < accountsList.getLength(); j++) {
				    	
				    	Element singleAccount = (Element) accountsList.item(j);
				    	Account account = new Account();
				    	String ban = singleAccount.getElementsByTagNameNS("*", CONSTANTS.billingAccountNumber).item(0).getTextContent().trim();
				    	String accountStatus = singleAccount.getElementsByTagNameNS("*", "statusCode").item(0).getTextContent().trim();
				    	System.out.println("Status =  " + accountStatus);
				    	String accountDate;
				    	if(accountStatus.equalsIgnoreCase("T")){
				    		accountDate = singleAccount.getElementsByTagNameNS("*", "accountCreationDate").item(0).getTextContent().trim();
				    	}
				    	else{
				    		accountDate = singleAccount.getElementsByTagNameNS("*", "startServiceDate").item(0).getTextContent().trim();
				    	}
				    	account.setAccountNumber(ban);
				    	account.setAccountStatus(accountStatus);
				    	System.out.println("Date =  " + accountDate);
				    	account.setAccountDate(new SimpleDateFormat("yyyy-MM-dd").parse(accountDate));
				    	accountsInResultNode.add(account);
				    }
				    
				    String topMatchAccount = execute_TopMatchAccount_Status_Rule(accountsInResultNode);
			    	
			    	if(topMatchAccount!=null){
					    for (int z = 1; z<=accountsList.getLength(); z++) {
					    	Element singleAccount = (Element) accountsList.item(z-1);
					    	String ban = singleAccount.getElementsByTagNameNS("*", CONSTANTS.billingAccountNumber).item(0).getTextContent().trim();
					    	System.out.println("Checking for BAN = " + ban);
					    	if(!ban.equals(topMatchAccount)){
					    		System.out.println("Deleting Node for BAN = " + ban);
					    		singleAccount.getParentNode().removeChild(singleAccount);
					        	document.normalize();
					        	z = z-1;
					    	}
					    	else{
					    		System.out.println("Not deleting for BAN = " + ban);
					    	}
					    }
//					    break;
				    }
			    	
			    	
				    for (int i = 1; i <=findMatchingSearchResultNodeList.getLength(); i++) {
				    	
				    	Element findMatchingSearchResultNode = (Element) findMatchingSearchResultNodeList.item(i-1);
				    	NodeList availableAccounts = findMatchingSearchResultNode.getElementsByTagNameNS("*", "accountData");
				    	if(availableAccounts.getLength()==0){
				    		findMatchingSearchResultNode.getParentNode().removeChild(findMatchingSearchResultNode);
				    		document.normalize();
				    		i = i-1;
				    	}

				    }
			    	System.out.println(getStringFromDocument(document));
		    
			    return XmlObject.Factory.parse(document);
			} catch (Exception e){
				System.out.println("Error :" + e.getMessage());
				return input;
			}
	
		}
		public static String execute_TopMatchAccount_Status_Rule(ArrayList<Account> accounts){
			System.out.println("Executing status rules");
			String top_account = "";
			ArrayList<Account> accts = new ArrayList<Account>();
			for(int i=0; i<topMatchRules.length;i++){
				System.out.println("Executing rule for account status = " + topMatchRules[i][1]);
				accts.removeAll(accounts);
				if(!topMatchRules[i][1].trim().equalsIgnoreCase("ANY")){
					for(int j=0; j <accounts.size(); j++){
						Account acct = accounts.get(j);
						System.out.println("Account = " + acct.getAccountNumber() + " Status = " + acct.getAccountStatus());
						if(acct.getAccountStatus().trim().equalsIgnoreCase(topMatchRules[i][1])){
							accts.add(acct);
							System.out.println("Selecting account number for status rules = " + acct.getAccountNumber());
						}
					}
					
					if(accts.size()>0 && topMatchRules[i][1].equals("T")){
						// execute by account creation date
						top_account = execute_TopMatchAccount_Tenure_Rule(accts,topMatchRules[i][3]);
					}
					else if(accts.size()>0){
						top_account = execute_TopMatchAccount_Tenure_Rule(accts,topMatchRules[i][2]);
					}
				}
				else{
					top_account = execute_TopMatchAccount_Tenure_Rule(accounts,topMatchRules[i][2]);
				}

				//Check if Top match account is found then stop executing rules
				if(!top_account.equalsIgnoreCase("")){
					System.out.println("Top match account " + top_account + " found. Stopping further rules execution.");
					break;
				}
			}
			
			return top_account;
		}

		public static String execute_TopMatchAccount_Tenure_Rule(ArrayList<Account> accounts, String sortingRule){
			
				System.out.println("Executing rule for account tenure = " + sortingRule);
				System.out.println("Accounts list Before sorting by Date");
				for(int i=0;i<accounts.size();i++){
					Account a = (Account) accounts.get(i);
					System.out.println("Account Number = "+ a.getAccountNumber());
				}
				
				Collections.sort(accounts);
				
				System.out.println("Accounts list After sorting by Date");
				for(int i=0;i<accounts.size();i++){
					Account a = (Account) accounts.get(i);
					System.out.println("Account Number = "+ a.getAccountNumber());
				}
				
				if(sortingRule.equalsIgnoreCase("NEWEST")){
					System.out.println("Returning newest account");
					return accounts.get(accounts.size()-1).getAccountNumber();
				}
				else{
					System.out.println("Returning oldest account");
					return accounts.get(0).getAccountNumber();
				}
		}

}
*/