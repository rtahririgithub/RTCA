package com.telus.credit.orderDepositCalculator;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.aop.framework.ProxyFactoryBean;

import com.telus.credit.orderDepositCalculator.domain.CalculateDeposit;
import com.telus.credit.orderDepositCalculator.domain.CalculateDepositResponse;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationList;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationListResponse;
import com.telus.credit.orderDepositCalculator.webservice.OrderDepositCalculatorServiceV10PortType;
import com.telus.credit.orderDepositCalculator.webservice.impl.OrderDepositCalculatorServiceImpl;
import com.telus.credit.testUtil.EnvUtil;
import com.telus.credit.orderDepositCalculator.webservice.util.RequestResponseUtil;



import org.junit.Test;


@ContextConfiguration(locations = { "file:src/test/resources/conf/test-orderDespositCalculatorService-spring.xml" })


public class OrderDepositCalculatorServiceImplTest extends TestCase {
	
	
		ClassPathXmlApplicationContext m_ApplicationContext;
	    
		String testDataPath 	="./../orderdepositcal-webservices/src/test/data/";	
		String responseFolder  	=testDataPath + "OrderDeposit_Request/response/";
		String requestFolder	=testDataPath + "OrderDeposit_Request/";
		String expectedFolder	=testDataPath + "OrderDeposit_Request/expected/";

		@Autowired
		private OrderDepositCalculatorServiceV10PortType m_SvcImpl;
		
		@Autowired
		private OrderDepositCalculatorServiceImpl pojo;

		
		protected void setUp() throws Exception {
			try {
				super.setUp();
				System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
				EnvUtil.setupTestEnv();
				m_ApplicationContext = new ClassPathXmlApplicationContext("test-orderDepositCalculatorService-spring.xml");
				m_SvcImpl = (OrderDepositCalculatorServiceV10PortType) m_ApplicationContext.getBean("orderDepositCalculatorService");
				pojo = (OrderDepositCalculatorServiceImpl) m_ApplicationContext.getBean("orderDepositCalculatorServiceTarget");
				RequestResponseUtil.delete(new File(responseFolder));
		
				} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		public void test_ByFilename() throws Throwable {
			String fname="testFinal.xml";
			System.out.println("TestFileName=" +fname);
			CalculateDeposit request = RequestResponseUtil.createCalculateDepositRequest(requestFolder,fname);		
			//System.out.println("user id =" +request.getAuditInfo().getUserId());
			//System.out.println("app id =" +request.getAuditInfo().getOriginatorApplicationId());
			
			CalculateDepositResponse response=null;
			try {		
					response = pojo.calculateDeposit(request);					
					RequestResponseUtil.writeToFilebyFileName(responseFolder, fname, response);	 
					
			}catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}	
		}	
		
		public void test_getEquipmentQualificationList() throws Throwable {
			String fname="getEquipmentList.xml";
			System.out.println("TestFileName=" +fname);
			GetEquipmentQualificationList request = RequestResponseUtil.createGetEquipmentQualificationListRequest(requestFolder,fname);		
			//System.out.println("user id =" +request.getAuditInfo().getUserId());
			//System.out.println("app id =" +request.getAuditInfo().getOriginatorApplicationId());
			
			GetEquipmentQualificationListResponse response=null;
			try {		
					response = pojo.getEquipmentQualificationList(request);					
					RequestResponseUtil.writeToFilebyFileName(responseFolder, fname, response);	 
					
			}catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}	
		}	

		public void test_CalculateDeposit() throws Throwable {
			String fname="getEquipmentList.xml";
			System.out.println("TestFileName=" +fname);
			 CalculateDeposit request = RequestResponseUtil.createCalculateDepositRequest(requestFolder, fname)

			//System.out.println("user id =" +request.getAuditInfo().getUserId());
			//System.out.println("app id =" +request.getAuditInfo().getOriginatorApplicationId());
			
			 CalculateDepositResponse response = null;
			try {		
					response = pojo.calculateDeposit(request);					
					RequestResponseUtil.writeToFilebyFileName(responseFolder, fname, response);	 
					
			}catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}	
		}
		
		

}
