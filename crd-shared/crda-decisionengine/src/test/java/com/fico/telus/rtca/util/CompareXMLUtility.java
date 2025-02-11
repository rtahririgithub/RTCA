package com.fico.telus.rtca.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.custommonkey.xmlunit.DifferenceConstants;
import org.custommonkey.xmlunit.DifferenceListener;
import org.w3c.dom.Node;

import com.fico.telus.rtca.testUtil.FicoTestRequestResponseMarshallingUtil;

public class CompareXMLUtility {

	
	public static void compare_Expected_to_Actual_With_filter(
					String exeptectFileName,
					String expectedFolder,
					String actualFileName,
					String actualFileFolder,
					String[] includedElementNames) throws Throwable {
				
		      	String  exeptectFileFullPath=expectedFolder+ exeptectFileName;
		      	
		    	String  actualFileFullPath= actualFileFolder  + actualFileName;
		    	
		        FileReader expectedFileReader = null;
		        FileReader responseFileReader = null;
		        
		        if (!new File(exeptectFileFullPath).exists()) {
		        	System.out.println("exeptectFile < " + exeptectFileFullPath +"> does not exist.");
		        	return;
		        }
		        try {
		            expectedFileReader = new FileReader(exeptectFileFullPath);
		            responseFileReader = new FileReader(actualFileFullPath);
		        } catch (FileNotFoundException e) {
		            throw e;
		        }
		 
		        try {

		            DifferenceListener myDifferenceListener = new IncludeNamedElementsDifferenceListener(includedElementNames);
		            Diff myDiff = new Diff(expectedFileReader, responseFileReader);
		            myDiff.overrideDifferenceListener(myDifferenceListener);
		            

		            DetailedDiff detDiff = new DetailedDiff(myDiff);
		            List differences = detDiff.getAllDifferences(); //calls differenceFound
		            
		            for (Object object : differences) {
		                Difference difference = (Difference)object;
		                int differenceId = difference.getId();
		                if(
		                	//differenceId==DifferenceConstants.TEXT_VALUE_ID //14
		                	//|| 
		                	differenceId==DifferenceConstants.ATTR_SEQUENCE_ID
		                	|| differenceId==DifferenceConstants.ATTR_NAME_NOT_FOUND_ID 
		                	|| differenceId==DifferenceConstants.COMMENT_VALUE_ID
		                	
		                    || differenceId==DifferenceConstants.NAMESPACE_PREFIX_ID          
		                    || differenceId==DifferenceConstants.NAMESPACE_URI_ID     
		                    || differenceId==DifferenceConstants.NO_NAMESPACE_SCHEMA_LOCATION_ID  
		                    		
		                    || differenceId==DifferenceConstants.CHILD_NODELIST_SEQUENCE.getId()  
		                	|| differenceId==DifferenceConstants.CHILD_NODELIST_LENGTH_ID 
		                    
		                    || differenceId==DifferenceConstants.ELEMENT_TAG_NAME_ID   
		                    || differenceId==DifferenceConstants.ELEMENT_NUM_ATTRIBUTES_ID 
			        		){
			        	continue;
			        }
		                

		                
		                

		                Node expectedNode =difference.getControlNodeDetail().getNode();
		                Node actualNode =difference.getTestNodeDetail().getNode();
		                
		                String diffStr="";
		                diffStr = diffStr+"\n Difference :" ;
		                diffStr = diffStr+  difference.getDescription() ;
		                //diffStr = diffStr+"[ ID=" +difference.getId() + "]" ; 
						diffStr = diffStr+"\n Expected Node Name=" + (expectedNode!=null?expectedNode.getNodeName():null);
						diffStr = diffStr+"\n Actual Node Name=" + (actualNode!=null?actualNode.getNodeName():null);
						    
						diffStr = diffStr+"\n Expected Node Value =" + difference.getControlNodeDetail().getValue();
						diffStr = diffStr+"\n Actual Node Value   =" + difference.getTestNodeDetail().getValue();
						    
						diffStr = diffStr+"\n Expected Node Location=" + difference.getControlNodeDetail().getXpathLocation();
						diffStr = diffStr+"\n Actual Node Location=" + difference.getTestNodeDetail().getXpathLocation(); 
						diffStr = "\n" +diffStr;           
						String diff_filename   = "Diff_" + "Actual_" + actualFileName + "Expected_" +exeptectFileName;
						FicoTestRequestResponseMarshallingUtil.writeResponseErrorToFilebyFileName(
									actualFileFolder, 
									diff_filename,  
									diffStr);
		    
		      
		            }

		        } catch (Throwable e) {
		            throw e;
		        } 
		        
		    }	
	public static void compare_Expected_to_Actual_for_included_filtered(
			String filname,
			String expectedFolder,
			String responseFolder,
			String[] includedElementNames) throws Throwable {
		
		String  exeptectFileName=filname;
      	String  exeptectFileFullPath=expectedFolder+ exeptectFileName;
      	
      	String  actualFileName=filname;
    	String  actualFileFullPath= responseFolder  + actualFileName;
    	
        FileReader expectedFileReader = null;
        FileReader responseFileReader = null;
        
        if (!new File(exeptectFileFullPath).exists()) {
        	System.out.println("exeptectFile < " + exeptectFileFullPath +"> does not exist.");
        	return;
        }
        try {
            expectedFileReader = new FileReader(exeptectFileFullPath);
            responseFileReader = new FileReader(actualFileFullPath);
        } catch (FileNotFoundException e) {
            throw e;
        }
 
        try {

            DifferenceListener myDifferenceListener = new IncludeNamedElementsDifferenceListener(includedElementNames);
            Diff myDiff = new Diff(expectedFileReader, responseFileReader);
            myDiff.overrideDifferenceListener(myDifferenceListener);
            

            DetailedDiff detDiff = new DetailedDiff(myDiff);
            List differences = detDiff.getAllDifferences(); //calls differenceFound
            
            for (Object object : differences) {
                Difference difference = (Difference)object;
                int differenceId = difference.getId();
                if(
                	//differenceId==DifferenceConstants.TEXT_VALUE_ID //14
                	//|| 
                	differenceId==DifferenceConstants.ATTR_SEQUENCE_ID
                	|| differenceId==DifferenceConstants.ATTR_NAME_NOT_FOUND_ID 
                	|| differenceId==DifferenceConstants.COMMENT_VALUE_ID
                	
                    || differenceId==DifferenceConstants.NAMESPACE_PREFIX_ID          
                    || differenceId==DifferenceConstants.NAMESPACE_URI_ID     
                    || differenceId==DifferenceConstants.NO_NAMESPACE_SCHEMA_LOCATION_ID  
                    		
                    || differenceId==DifferenceConstants.CHILD_NODELIST_SEQUENCE.getId()  
                	|| differenceId==DifferenceConstants.CHILD_NODELIST_LENGTH_ID 
                    
                    || differenceId==DifferenceConstants.ELEMENT_TAG_NAME_ID   
                    || differenceId==DifferenceConstants.ELEMENT_NUM_ATTRIBUTES_ID 
	        		){
	        	continue;
	        }
                

                
                

                Node expectedNode =difference.getControlNodeDetail().getNode();
                Node actualNode =difference.getTestNodeDetail().getNode();
                
                String diffStr="";
                diffStr = diffStr+"\n Difference :" ;
                diffStr = diffStr+  difference.getDescription() ;
                //diffStr = diffStr+"[ ID=" +difference.getId() + "]" ; 
				diffStr = diffStr+"\n Expected Node Name=" + (expectedNode!=null?expectedNode.getNodeName():null);
				diffStr = diffStr+"\n Actual Node Name=" + (actualNode!=null?actualNode.getNodeName():null);
				    
				diffStr = diffStr+"\n Expected Node Value =" + difference.getControlNodeDetail().getValue();
				diffStr = diffStr+"\n Actual Node Value   =" + difference.getTestNodeDetail().getValue();
				    
				diffStr = diffStr+"\n Expected Node Location=" + difference.getControlNodeDetail().getXpathLocation();
				diffStr = diffStr+"\n Actual Node Location=" + difference.getTestNodeDetail().getXpathLocation(); 
				                
				                
				FicoTestRequestResponseMarshallingUtil.writeResponseErrorToFilebyFileName(responseFolder, "Diff_Actual_Expected_" +filname ,  "\n"+diffStr);
            
      
            }

        } catch (Throwable e) {
            throw e;
        } 
        
    }	
	
	
	public static void compare_Expected_to_Actual_for_included_filtered2(
			String  exeptectFileFullPath,
			String  actualFileFullPath,
			String  responseFolder,
			String[] includedElementNames) throws Throwable {
		String filname="";
		String diff_filname ="Diff_Actual_Expected_" +filname;
		FileReader expectedFileReader = null;
        FileReader responseFileReader = null;
        
        if (!new File(exeptectFileFullPath).exists()) {
        	System.out.println("exeptectFile < " + exeptectFileFullPath +"> does not exist.");
        	return;
        }
        try {
            expectedFileReader = new FileReader(exeptectFileFullPath);
            responseFileReader = new FileReader(actualFileFullPath);            
        } catch (FileNotFoundException e) {
            throw e;
        }
 
        try {

            DifferenceListener myDifferenceListener = new IncludeNamedElementsDifferenceListener(includedElementNames);
            Diff myDiff = new Diff(expectedFileReader, responseFileReader);
            myDiff.overrideDifferenceListener(myDifferenceListener);
            

            DetailedDiff detDiff = new DetailedDiff(myDiff);
            List differences = detDiff.getAllDifferences(); //calls differenceFound
            
            for (Object object : differences) {
                Difference difference = (Difference)object;
                int differenceId = difference.getId();
                if(
                	//differenceId==DifferenceConstants.TEXT_VALUE_ID //14
                	//|| 
                	differenceId==DifferenceConstants.ATTR_SEQUENCE_ID
                	|| differenceId==DifferenceConstants.ATTR_NAME_NOT_FOUND_ID 
                	|| differenceId==DifferenceConstants.COMMENT_VALUE_ID
                	
                    || differenceId==DifferenceConstants.NAMESPACE_PREFIX_ID          
                    || differenceId==DifferenceConstants.NAMESPACE_URI_ID     
                    || differenceId==DifferenceConstants.NO_NAMESPACE_SCHEMA_LOCATION_ID  
                    		
                    || differenceId==DifferenceConstants.CHILD_NODELIST_SEQUENCE.getId()  
                	|| differenceId==DifferenceConstants.CHILD_NODELIST_LENGTH_ID 
                    
                    || differenceId==DifferenceConstants.ELEMENT_TAG_NAME_ID   
                    || differenceId==DifferenceConstants.ELEMENT_NUM_ATTRIBUTES_ID 
	        		){
	        	continue;
	        }
                

                
                

                Node expectedNode =difference.getControlNodeDetail().getNode();
                Node actualNode =difference.getTestNodeDetail().getNode();
                
                String diffStr="";
                diffStr = diffStr+"\n Difference :" ;
                diffStr = diffStr+  difference.getDescription() ;
                //diffStr = diffStr+"[ ID=" +difference.getId() + "]" ; 
				diffStr = diffStr+"\n Expected Node Name=" + (expectedNode!=null?expectedNode.getNodeName():null);
				diffStr = diffStr+"\n Actual Node Name=" + (actualNode!=null?actualNode.getNodeName():null);
				    
				diffStr = diffStr+"\n Expected Node Value =" + difference.getControlNodeDetail().getValue();
				diffStr = diffStr+"\n Actual Node Value   =" + difference.getTestNodeDetail().getValue();
				    
				diffStr = diffStr+"\n Expected Node Location=" + difference.getControlNodeDetail().getXpathLocation();
				diffStr = diffStr+"\n Actual Node Location=" + difference.getTestNodeDetail().getXpathLocation(); 
				                
				
				FicoTestRequestResponseMarshallingUtil.writeResponseErrorToFilebyFileName(responseFolder, diff_filname ,  "\n"+diffStr);
            
      
            }

        } catch (Throwable e) {
            throw e;
        } 
        
    }
}
	 
	 
/*	class MatchTrackerImpl implements MatchTracker {
		 
	    public void matchFound(Difference difference) {
	        if (difference != null) {
	            NodeDetail controlNode = difference.getControlNodeDetail();
	            NodeDetail testNode = difference.getTestNodeDetail();
	 
	            String controlNodeValue = printNode(controlNode.getNode());
	            String testNodeValue = printNode(testNode.getNode());
	 
	            if (controlNodeValue != null) {
	                System.out.println("####################");
	                System.out.println("Control Node: " + controlNodeValue);
	            }
	            if (testNodeValue != null) {
	                System.out.println("Test Node: " + testNodeValue);
	                System.out.println("####################");
	            }
	        }
	    }
	 
	    private static String printNode(Node node) {
	        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
	            StringWriter sw = new StringWriter();
	            try {
	                Transformer t = TransformerFactory.newInstance().newTransformer();
	                t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	                t.transform(new DOMSource(node), new StreamResult(sw));
	            } catch (TransformerException te) {
	                System.out.println("nodeToString Transformer Exception");
	            }
	            return sw.toString();
	 
	        }
	        return null;
	    }
	    
	    
	}*/
	
/*	 class IgnoreNamedElementsDifferenceListener implements DifferenceListener {
	    private Set<String> blackList = new HashSet<String>();
	    // list of node names to ignore 
	    public IgnoreNamedElementsDifferenceListener(String[] elementNames) {
	        for (String name : elementNames) {
	            blackList.add(name);
	        }
	    }

	    public int differenceFound(Difference difference) {
	        if (difference.getId() == DifferenceConstants.TEXT_VALUE_ID) {
	            if (blackList.contains(difference.getControlNodeDetail().getNode().getParentNode().getNodeName())) {
	                return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
	            }
	        }

	        return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
	    }

	    public void skippedComparison(Node node, Node node1) {
	    	int i=0;
	    	i++;
	    }
	}*/

	 class IncludeNamedElementsDifferenceListener implements DifferenceListener {
		    private Set<String> includeList = new HashSet<String>();
		    // list of node names to ignore 
		    public IncludeNamedElementsDifferenceListener(String[] elementNames) {
		        for (String name : elementNames) {
		        	includeList.add(name);
		        }
		    }

		    public int differenceFound(Difference difference) {

		    	String aNodeName ="";
		    	try{
		    	aNodeName =difference.getControlNodeDetail().getNode().getParentNode().getNodeName();
		    	}catch(Throwable e){}
		    	System.out.println("======aNodeName=========");
		    	System.out.println(aNodeName);
		    	System.out.println("===============");		    	
		            if (includeList.contains(aNodeName) ) {
		            	return DifferenceListener.RETURN_ACCEPT_DIFFERENCE;
		            }
		        return DifferenceListener.RETURN_IGNORE_DIFFERENCE_NODES_IDENTICAL;
		    }
		    
		    public void skippedComparison(Node node, Node node1) {
		    	int i=0;
		    	i++;
		    }
	
	 
	 }
	 