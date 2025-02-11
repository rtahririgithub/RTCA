package com.fico.telus.rtca.blaze;

import java.io.File;

import junit.framework.TestCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.fico.telus.blaze.creditAsessment.CancelDepositCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ManualInquiryCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.MonthlyUDCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;//.CreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.OverrideCreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.rtca.testUtil.EnvUtil;
import com.fico.telus.rtca.testUtil.FicoTestRequestResponseMarshallingUtil;
import com.fico.telus.rtca.util.CompareXMLUtility;

public class SVC_RuleServicesBean_Test   extends TestCase{
	
	String testDataPath 	="./../crda-decisionengine/src/test/resources/data/";	

	String newCustomerCreditAssessmentRequestFolder	    =testDataPath + "SVC_Request/NewCustomerCreditAssessmentRequest/";
	String manualInquiryCreditAssessmentRequestFolder	=testDataPath + "SVC_Request/ManualInquiryCreditAssessmentRequest/";
	String existingCustomerCreditAssessmentRequestFolder	=testDataPath + "SVC_Request/ExistingCustomerCreditAssessmentRequest/";
	String overrideCreditAssessmentRequestFolder	=testDataPath + "SVC_Request/OverrideCreditAssessmentRequest/";
	String monthlyUDCreditAssessmentRequestFolder	=testDataPath + "SVC_Request/MonthlyUDCreditAssessmentRequest/";
	String cancelDepositCreditAssessmentRequestFolder	=testDataPath + "SVC_Request/CancelDepositCreditAssessmentRequest/";
	
	String responseFolder  	=testDataPath + "SVC_Request/response/";	
	String expectedFolder	=testDataPath + "SVC_Request/expected/";
	
    String[] includedElementNames ={
			"assessmentResultCd"
    		,
    		"assessmentResultReasonCd"
    		,
    		"decisionCd"
    		,
    		"assessmentMessageCd"
    		,
    		"creditValueCd"         		
    		}; 
	
	@Autowired
	RuleServicesBean rules;
	
	ClassPathXmlApplicationContext m_ApplicationContext;
	protected void setUp() throws Exception {
		try {
			super.setUp();
			EnvUtil.setupTestEnv();
			m_ApplicationContext = new ClassPathXmlApplicationContext("crda-decisionengine-spring.xml");
			rules = (com.fico.telus.rtca.blaze.RuleServicesBean) m_ApplicationContext.getBean("RuleServicesBean");	
				
			FicoTestRequestResponseMarshallingUtil.delete(new File(responseFolder)); 		
			} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	public void test_Single_File() throws Throwable  {		
		String filename="";
		filename = "NewCustomerCreditAssessmentRequestWithBureauRegular.xml";		
		try {
			String filenameFullPath = "" ;
			filenameFullPath = newCustomerCreditAssessmentRequestFolder + filename; 

			//create CreditAssessmentRequest
			NewCustomerCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createNewCustomerCreditAssessmentRequest(filenameFullPath);
			
			// call fico 
			CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
			
			//create PerformCreditAssessmentResponse xml file 
			String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
			System.out.println("repsponseFileName = " + repsponseFileName); 
			System.out.println("DONE");
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
	}
	
 	/*
 	 * NewCustomerCreditAssessmentRequest
     * ManualInquiryCreditAssessmentRequest
     * ExistingCustomerCreditAssessmentRequest
     * OverrideCreditAssessmentRequest
     * MonthlyUDCreditAssessmentRequest
     * CancelDepositCreditAssessmentRequest
 	 */
	public void test_AllFiles_Requests() throws Throwable  {
		
		run_AllFiles_NewCustomerCreditAssessmentRequest();
//       run_AllFiles_ManualInquiryCreditAssessmentRequest();
//       run_AllFiles_ExistingCustomerCreditAssessmentRequest();
//		run_AllFiles_OverrideCreditAssessmentRequest();
//		run_AllFiles_MonthlyUDCreditAssessmentRequest();
//		run_AllFiles_CancelDepositCreditAssessmentRequest();
	      
	}
	

	private void run_AllFiles_NewCustomerCreditAssessmentRequest() throws Throwable  {
        File directory = new File(newCustomerCreditAssessmentRequestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	String filenameFullPath = "" ;
        		filenameFullPath = newCustomerCreditAssessmentRequestFolder + filename; 
        		System.out.println("requestFileName = " + filenameFullPath);

        		//create CreditAssessmentRequest
        		NewCustomerCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createNewCustomerCreditAssessmentRequest(filenameFullPath);
        		
        		// call fico 
        		rules.ping();
        		System.out.println("rules.ping() = " + rules.ping());
        		CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
        		
        		//create PerformCreditAssessmentResponse xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
        		
        		CompareXMLUtility.compare_Expected_to_Actual_for_included_filtered(repsponseFileName,expectedFolder,responseFolder,includedElementNames);
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}


	private void run_AllFiles_ManualInquiryCreditAssessmentRequest() throws Throwable  {
        File directory = new File(manualInquiryCreditAssessmentRequestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	String filenameFullPath = "" ;
        		filenameFullPath = manualInquiryCreditAssessmentRequestFolder + filename; 

        		//create CreditAssessmentRequest
        		ManualInquiryCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createManualInquiryCreditAssessmentRequest(filenameFullPath);
        		
        		// call fico 
        		CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
        		
        		//create PerformCreditAssessmentResponse xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}


	private void run_AllFiles_ExistingCustomerCreditAssessmentRequest() throws Throwable  {
        File directory = new File(existingCustomerCreditAssessmentRequestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	String filenameFullPath = "" ;
        		filenameFullPath = existingCustomerCreditAssessmentRequestFolder + filename; 

        		//create CreditAssessmentRequest
        		ExistingCustomerCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createExistingCustomerCreditAssessmentRequest(filenameFullPath);
        		
        		// call fico 
        		CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
        		
        		//create PerformCreditAssessmentResponse xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}
	
	private void run_AllFiles_OverrideCreditAssessmentRequest() throws Throwable  {
        File directory = new File(overrideCreditAssessmentRequestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	String filenameFullPath = "" ;
        		filenameFullPath = overrideCreditAssessmentRequestFolder + filename; 

        		//create CreditAssessmentRequest
        		OverrideCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createOverrideCreditAssessmentRequest(filenameFullPath);
        		
        		// call fico 
        		CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
        		
        		//create PerformCreditAssessmentResponse xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}

	private void run_AllFiles_MonthlyUDCreditAssessmentRequest() throws Throwable  {
        File directory = new File(monthlyUDCreditAssessmentRequestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	String filenameFullPath = "" ;
        		filenameFullPath = monthlyUDCreditAssessmentRequestFolder + filename; 

        		//create CreditAssessmentRequest
        		MonthlyUDCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createMonthlyUDCreditAssessmentRequest(filenameFullPath);
        		
        		// call fico 
        		CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
        		
        		//create PerformCreditAssessmentResponse xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}

	private void run_AllFiles_CancelDepositCreditAssessmentRequest() throws Throwable  {
        File directory = new File(cancelDepositCreditAssessmentRequestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	String filenameFullPath = "" ;
        		filenameFullPath = cancelDepositCreditAssessmentRequestFolder + filename; 

        		//create CreditAssessmentRequest
        		CancelDepositCreditAssessmentRequest aCreditAssessmentRequest = FicoTestRequestResponseMarshallingUtil.createCancelDepositCreditAssessmentRequest(filenameFullPath);
        		
        		// call fico 
        		CreditAssessmentResult creditAssessmentResult  = rules.performCreditAssessment(123456,aCreditAssessmentRequest);
        		
        		//create PerformCreditAssessmentResponse xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,creditAssessmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}
	

}

