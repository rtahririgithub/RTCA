package com.telus.credit.crda.adapter;

import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.credit.crda.asmtclassification.dscn.FullAssessmentAutoUnifiedCreditTest;
import com.telus.credit.crda.util.EcrdaTestHelper;
import com.telus.credit.crda.util.EnvUtil;
import com.telus.credit.crda.webservice.impl.EnterpriseCreditAssessmentServiceImpl;
import com.telus.credit.domain.crda.CreditAssessmentTransaction;
import com.telus.credit.domain.crda.GetCreditAssessmentResponse.CreditAssessmentDetails;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.framework.config.ConfigContext;
import com.telus.framework.crypto.EncryptionUtil;


public class DocumentumSvcAdapterTest extends TestCase {

	private Log log;
	private DozerBeanMapper m_mapper;

	private EnterpriseCreditAssessmentServiceImpl pojo;
	private String dir = "src/test/resources/data/FullASmt/";
	private String responseFolder = dir + "response/";
	private String xmlFile = "tmp_UC_FULL_AUTO.xml";
    
	protected void setUp() throws Exception {
		try {
			//log.info( "all" + Thread.getContextClassLoader().Resource("log4j.xml") );
			super.setUp();
			System.getProperties().setProperty("oracle.jdbc.J2EE13Compliant", "true");
			//log.info("System.getProperties():" + System.getProperties() );
			EnvUtil.setupTestEnv();		
			
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("telus-crd-crda-impl-spring-test.xml");
			pojo = (EnterpriseCreditAssessmentServiceImpl)m_ApplicationContext.getBean("enterpriseCreditAssessmentServiceTarget");
			log = LogFactory.getLog(FullAssessmentAutoUnifiedCreditTest.class);
			log.info("pojo is " + pojo );
			com.telus.framework.crypto.impl.pilot.PilotCryptoImpl cryptoImpl = (com.telus.framework.crypto.impl.pilot.PilotCryptoImpl)m_ApplicationContext.getBean("cryptoImpl");
			m_mapper =  (DozerBeanMapper)m_ApplicationContext.getBean("EcrdaDozerBeanMapper");
			//EnvDetailUtil
			log.info(EncryptionUtil.encrypt("blabla"));
			log.info("ConfigContext.getApplicationId: " + ConfigContext.getApplicationId() );
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();			
		}
	}
	
	@Test
	public void testGetDocumentumDocuemnt() throws Exception {
		SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria = new SearchCreditAssessmentsRequestCriteria();
		searchCreditAssessmentsRequestCriteria.setCustomerID(95370295);
		//aDocumentumSvcAdapter.getDocumentumDocuemnt("1", EcrdaTestHelper.createAudiInfo());
		List<CreditAssessmentTransaction> creditAssessmentTransactionList = pojo.searchCreditAssessmentList(searchCreditAssessmentsRequestCriteria , EcrdaTestHelper.createAudiInfo());
		log.info("list: " + creditAssessmentTransactionList );
	}

	@Test
	public void testGetCAR_NextDocumentumDocuemnt() throws Exception {
		
		CreditAssessmentDetails aCreditAssessmentDetails = pojo.getCreditAssessment(8571171, EcrdaTestHelper.createAudiInfo());
		
		//aDocumentumSvcAdapter.getDocumentumDocuemnt("1", EcrdaTestHelper.createAudiInfo());
		log.info("list: " + aCreditAssessmentDetails);
	}
}
