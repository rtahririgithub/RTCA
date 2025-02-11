package com.telus.credit.crda.logger.adapter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.crda.logger.util.CreditAssessmentUtility;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

import junit.framework.TestCase;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile = "test-logger-appCtx.properties")
@ContextConfiguration("classpath:test-eca-spring.xml")
public class Log4JTest extends TestCase {

	public static Log log = null;
	
	static {
		try {
			System.out.println("before log creation");
	log = LogFactory.getLog(Log4JTest.class);
	System.out.println("after log creation.");;
		}
		catch ( Throwable e){
			e.printStackTrace();
		}
	}
	@Autowired
	EnterpriseCreditAssessmentServiceAdapter m_enterpriseCreditAssessmentServiceAdapter;
	@Autowired
	CreditAssessmentUtility creditAssessmentUtility;

	// ApplicationContext context=null;

	protected void setUp() throws Exception {
		super.setUp();
		/*
		 * context = new
		 * ClassPathXmlApplicationContext("ws-cgw-client-test-spring);
		 * 
		 * m_creditGatewayServicePortType =
		 * (CreditGatewayServicePortType)context.getBean("CreditGatewayService")
		 * ;
		 */
	}
	
	@Test
	public void testLog4j() {
		try {
		
		
		System.out.println("before log.debug");
		log.debug("This is test");
		System.out.println("after log.debug");
		}
		catch ( Throwable e){
			e.printStackTrace();
		}
	}
	
}
