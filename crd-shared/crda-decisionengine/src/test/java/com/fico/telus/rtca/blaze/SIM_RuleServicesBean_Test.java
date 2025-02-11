package com.fico.telus.rtca.blaze;

import java.io.File;
import java.io.InputStream;

import junit.framework.TestCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fico.telus.rtca.testUtil.EnvUtil;
import com.fico.telus.rtca.testUtil.FicoTestRequestResponseMarshallingUtil;

import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.creditCommon.CreditBureauResult;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauRequest;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauResponse;

public class SIM_RuleServicesBean_Test   extends TestCase{
	String testDataPath 	="./../crda-decisionengine/src/test/resources/data/";	
	
	String requestFolder	=testDataPath + "SIM_Request/";
	String responseFolder  	=testDataPath + "SIM_Request/response/";	
	String expectedFolder	=testDataPath + "SIM_Request/expected/";
	
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
	public void testSetup() throws Throwable{
		//test_Single_File_getSimulatedCreditBureauResultTest();
	}

	
	
	public void test_Single_File() throws Throwable  {		
		String filename="";
		filename = "SimulatorTestMode.xml";		
		try {
			test_ByFilename_getSimulatedCreditBureauResult(filename);
			System.out.println("DONE");
		} catch (Exception e) {
			// 
			e.printStackTrace();
		}
	}

	private void test_ByFilename_getSimulatedCreditBureauResult(String filename) throws Throwable{
	try {	
		System.out.println("filename under test = " + filename);
		String filenameFullPath = "" ;
		filenameFullPath = requestFolder + filename; 
		
		//InputStream isReq = this.getClass().getClassLoader().getResourceAsStream(filenameFullPath);
		
		//create CreditAssessmentRequest
		//SimulatorCreditBureauRequest aSimulatorCreditBureauRequest = (SimulatorCreditBureauRequest)XmlUtils.convertToObj(isReq);
		SimulatorCreditBureauRequest aSimulatorCreditBureauRequest = FicoTestRequestResponseMarshallingUtil.createSimulatorCreditBureauRequest(filenameFullPath);
		
		// call fico 
		 CreditBureauResult aCreditBureauResult = rules.getSimulatedCreditBureauResult(123456, aSimulatorCreditBureauRequest);
		 
		 SimulatorCreditBureauResponse aSimulatorCreditBureauResponse= new SimulatorCreditBureauResponse();
		 aSimulatorCreditBureauResponse.setSimulatedCreditBureauResult(aCreditBureauResult);
		//create Response xml file 
		String repsponseFileName=FicoTestRequestResponseMarshallingUtil.writeToFilebyFileName(responseFolder,filename,aSimulatorCreditBureauResponse);
		System.out.println("repsponseFileName = " + repsponseFileName); 
		
	}catch ( Throwable e ) {
		e.printStackTrace();
		throw e;
	}		
	}	


	public void test_AllFiles_Requests() throws Throwable  {
        File directory = new File(requestFolder);
        //get all the files from a directory

        File[] fList = directory.listFiles();
      
        int i=0;
        int stopCount = 0;
        stopCount=3;
        stopCount =(fList!=null)?fList.length+10:0;
        
        for (File file : fList){
            if (!file.isDirectory()){
            	String filename=file.getName();
            	test_ByFilename_getSimulatedCreditBureauResult(filename);
            }           
            
			if(i==stopCount){
            	break;
            }
			i++;
        }
	}
}

