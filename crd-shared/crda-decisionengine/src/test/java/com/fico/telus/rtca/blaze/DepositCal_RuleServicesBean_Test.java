package com.fico.telus.rtca.blaze;

import java.io.File;

import junit.framework.TestCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.depositCalculator.DepositCalculatorRequest;
import com.fico.telus.blaze.depositCalculator.DepositCalculationResult;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentRequest;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentResult;
import com.fico.telus.blaze.depositCalculator.DepositResult;
import com.fico.telus.rtca.testUtil.EnvUtil;
import com.fico.telus.rtca.testUtil.FicoTestRequestResponseMarshallingUtil;


public class DepositCal_RuleServicesBean_Test extends TestCase {
	
	String testDataPath 	="./../crda-decisionengine/src/test/resources/data/";	

	String depositCalculatorRequestFolder=testDataPath+ "Deposit/DepositCalculatorRequest/";
	String depositEquipmentRequestFolder=testDataPath+ "Deposit/DepositEquipmentRequest/";
	String responseFolder  	=testDataPath + "Deposit/response/";	
	String expectedFolder	=testDataPath + "Deposit/expected/";
	
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
	
	//DepositCalculatorRequest
	//DepositEquipmentRequest
    public void test_AllFiles_Requests() throws Throwable  {
		
		run_AllFiles_DepositCalculatorRequest();
        run_AllFiles_DepositEquipmentRequest();      
	}
	

	private void run_AllFiles_DepositCalculatorRequest() throws Throwable  {
        File directory = new File(depositCalculatorRequestFolder);
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
        		filenameFullPath = depositCalculatorRequestFolder + filename; 

        		//create Request
    			DepositCalculatorRequest aDepositCalculatorRequest = FicoTestRequestResponseMarshallingUtil.createDepositCalculatorRequest(filenameFullPath);
   
        		
    			// call fico 
    			DepositCalculationResult depositCalculationResult  =(DepositCalculationResult) rules.calculateDeposit(123456,aDepositCalculatorRequest);
    			 
        		//create Response xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,depositCalculationResult);
        		
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}
	
	private void run_AllFiles_DepositEquipmentRequest() throws Throwable  {
        File directory = new File(depositEquipmentRequestFolder);
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
        		filenameFullPath = depositEquipmentRequestFolder + filename; 

        		//create Request
        		DepositEquipmentRequest aDepositEquipmentRequest = FicoTestRequestResponseMarshallingUtil.createDepositEquipmentRequest(filenameFullPath);
    			
    			// call fico 
        		DepositEquipmentResult depositEquipmentResult  = (DepositEquipmentResult)rules.calculateDeposit(123456,aDepositEquipmentRequest);
        		
        		//create Response xml file 
        		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,depositEquipmentResult);
        		System.out.println("repsponseFileName = " + repsponseFileName); 
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}


	public void test_Single_File() throws Throwable  {		
		String filename="";
		//filename = "CalculateDepositRegular.xml";	
		filename="CalculateDepositRegularFinal.xml";
		//filename="CalculateDepositRestricted.xml";

		try {
			String filenameFullPath = "" ;
			filenameFullPath = depositCalculatorRequestFolder + filename; 

			//create CreditAssessmentRequest
			DepositCalculatorRequest aDepositCalculatorRequest = FicoTestRequestResponseMarshallingUtil.createDepositCalculatorRequest(filenameFullPath);
			
			// call fico 
			DepositCalculationResult depositCalculationResult  =(DepositCalculationResult)  rules.calculateDeposit(123456,aDepositCalculatorRequest);
			
			//create PerformCreditAssessmentResponse xml file 
			String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename, depositCalculationResult);
			System.out.println("repsponseFileName = " + repsponseFileName); 
			System.out.println("DONE");
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
	}
}
