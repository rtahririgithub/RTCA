package com.telus.credit.crda.webservice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;


@ContextConfiguration("classpath:telus-crd-crda-ws-spring.xml")

public class TestEnterpriseCreditAssessmentServicePortImpl {

    private static final Log log = LogFactory.getLog(TestEnterpriseCreditAssessmentServicePortImpl.class);


    @Test 
    public void testSteup(){
    	//testPing();
    	log.info("Test" );
    }

    private void testPing(){
 		AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring-test.xml");
 		EnterpriseCreditAssessmentServiceImpl pojo = 
 				(EnterpriseCreditAssessmentServiceImpl)m_ApplicationContext.getBean("enterpriseCreditAssessmentServiceTarget");
 		try {
 			String pingrespo = pojo.ping();
 			log.info("pingrespo=" + pingrespo);
 		} catch (Throwable e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} 
     	log.info("Test" );
     }
}
