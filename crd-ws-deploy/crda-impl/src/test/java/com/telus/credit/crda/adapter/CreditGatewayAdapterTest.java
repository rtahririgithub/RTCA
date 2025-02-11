package com.telus.credit.crda.adapter;

 
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.JAXBException;

 
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
 


import com.telus.credit.crda.util.CrdaUtility;
import com.telus.credit.crda.util.TestFiles;
import com.telus.credit.crda.util.XMLUtility;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.wsdl.cmo.ordermgmt.creditgatewayservice_1.CreditGatewayServicePortType;

import junit.framework.TestCase;
@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="crda-test-appCtx.properties")
@ContextConfiguration("classpath:test/ws-cgw-client-tst-spring.xml")



public class CreditGatewayAdapterTest extends TestCase {

     @Autowired
      CreditGatewayServicePortType m_creditGatewayServicePortType;
    
    //ApplicationContext context=null;
	
	 
	protected void setUp() throws Exception {
		super.setUp();
	/*	context = new ClassPathXmlApplicationContext("ws-cgw-client-test-spring);
		
		m_creditGatewayServicePortType = (CreditGatewayServicePortType)context.getBean("CreditGatewayService");
*/		
	}
		//@Test
	public void testPullConsumerCreditReportDirectly() throws JAXBException, Throwable{
	    com.telus.credit.crdgw.domain.PullConsumerCreditReport aPullConsumerCreditReport =  new com.telus.credit.crdgw.domain.PullConsumerCreditReport();
	   
	    
	    aPullConsumerCreditReport = XMLUtility.createPullConsumerCreditReportRequest(TestFiles.CGWpullConsumerReportRequest);
	   
	   // System.out.println(": " + CrdaUtility.convertToXml( aPullConsumerCreditReport ) );
		com.telus.credit.crdgw.domain.ConsumerCreditReportResponse aConsumerCreditReportResponse= m_creditGatewayServicePortType.pullConsumerCreditReport(
					aPullConsumerCreditReport.getAuditInfo(), 
					aPullConsumerCreditReport.getConsumerCreditReportRequest());
		 
	}
	@Test	
	public void testPing() throws Throwable, Exception,
		FileNotFoundException, UnsupportedEncodingException {
		try {
			System.out.println( "Ping result : \n" + 
					m_creditGatewayServicePortType.ping()
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
