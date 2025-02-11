/*package com.fico.telus.rtca.blaze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.xml.sax.SAXException;

import com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ExistingBaseCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.AdjudicationResult;
import com.fico.telus.blaze.creditCommon.CollectionData;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.creditCommon.CreditBureauResult;
import com.fico.telus.blaze.creditCommon.CreditCustomerInfo;
import com.fico.telus.blaze.creditCommon.CreditDecision;
import com.fico.telus.blaze.creditCommon.CreditProfileData;
import com.fico.telus.blaze.creditCommon.CreditWorthinessData;
import com.fico.telus.blaze.creditCommon.DepositData;
import com.fico.telus.blaze.creditCommon.PersonName;
import com.fico.telus.blaze.creditCommon.RiskIndicator;
import com.fico.telus.blaze.depositCalculator.DepositEquipmentResult;
import com.fico.telus.blaze.depositCalculator.DepositResult;
import com.fico.telus.blaze.webservice.CalculateDeposit;
import com.fico.telus.blaze.webservice.CalculateDepositResponse;
import com.fico.telus.blaze.webservice.GetSimulatedCreditBureauResult;
import com.fico.telus.blaze.webservice.GetSimulatedCreditBureauResultResponse;
import com.fico.telus.blaze.webservice.PerformCreditAssessment;
import com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse;
import com.fico.telus.rtca.util.ReadWriteTextFile;
import com.fico.telus.rtca.util.TestHelper;
import com.fico.telus.rtca.util.VariableProcessor;
import com.fico.telus.rtca.util.XPathEvaluator;
import com.fico.telus.rtca.util.XPathTestVerifier;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;

@SuppressWarnings("deprecation")
@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx-Decisioning.properties")
@ContextConfiguration("classpath:crda-decisionengine-spring.xml")
public class RuleServerTest {
	
	
     * Log object
      
    private static final Log log = LogFactory.getLog(RuleServerTest.class);
    
	@Autowired
	RuleServicesBean rules;
	
	private List<XmlTestSuite> m_testSuites = new ArrayList<XmlTestSuite>();

	private String m_logsDirectory;
	private BufferedWriter m_testSummaryLogWriter;
	
	@Test
    public void test_ruleServiceBean() throws Exception
    {
		initialize();
		//long startTime = System.currentTimeMillis();
		//System.out.println( "Elapsed time in milliseconds: Initialization    = " + (System.currentTimeMillis()- startTime));
		//startTime = System.currentTimeMillis();
		try { 
			for ( XmlTestSuite testSuite : m_testSuites ) {
				if ( testSuite.getTestSuiteName().equals("CreditAssessment") ) {
					runCreditAssessmentTests(testSuite);
				}
				if ( testSuite.getTestSuiteName().equals("Deposit") ) {
					runDepositTests(testSuite);
				}
				if ( testSuite.getTestSuiteName().equals("Simulator") ) {
					runSimulatorTests(testSuite);
				}
			}
			shutdown();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("*** ERROR: " + ex.getMessage());
		}
		//System.out.println( "Elapsed time in milliseconds: First invocation  = " + (System.currentTimeMillis()- startTime));
    }



	private void runSimulatorTests(XmlTestSuite testSuite) throws IOException, JAXBException, ParserConfigurationException, SAXException, XPathExpressionException {
		for ( XmlTest test: testSuite.getTests() ) {
			String inputData = ReadWriteTextFile.getContents(new File(testSuite.getInputFilePath(test)));
			String varProcessedInputData = VariableProcessor.processVariables(inputData);
			testSuite.logInputFile( test, varProcessedInputData );
			GetSimulatedCreditBureauResult simulatedResult = new GetSimulatedCreditBureauResult();
			GetSimulatedCreditBureauResult simulatedResultObject = (GetSimulatedCreditBureauResult) TestHelper.convertXMLToObject( simulatedResult, testSuite.getLogInputFilePath(test) );
			
			CreditBureauResult creditBureauResult = null;
			try {
				creditBureauResult = rules.getSimulatedCreditBureauResult(123456, simulatedResultObject.getSimulatedCreditBureauRequest() );
						//calculateDeposit(123456, calculateDepositObject.getCalculateDepositRequest() );
			}
			catch ( Exception e ) {
				log.error("Error calling FICO : " + e, e );
				testSuite.logErrorFile( test, "Error calling FICO : " + e );
				testSuite.logErrorResult( test, false );
				continue;
			}
			GetSimulatedCreditBureauResultResponse response = new GetSimulatedCreditBureauResultResponse();
			response.setSimulatedCreditBureauResult(creditBureauResult);
			String  xmlResultDocument = TestHelper.convertObjectToXml( response );
			testSuite.logOutputFile( test, xmlResultDocument );
			XPathEvaluator xPathEvaluator = new XPathEvaluator( xmlResultDocument );
			
			XPathTestVerifier xPathTestVerifier  = new XPathTestVerifier( xPathEvaluator, testSuite.getTestVerificationFile(test), testSuite.getTestVerificationLogFilePath(test) );
			boolean result = xPathTestVerifier.verifyResults();
			testSuite.logResult( test, result );
		}
	}



	private void runDepositTests(XmlTestSuite testSuite) throws IOException, JAXBException, ParserConfigurationException, SAXException, XPathExpressionException {
		for ( XmlTest test: testSuite.getTests() ) {
			String inputData = ReadWriteTextFile.getContents(new File(testSuite.getInputFilePath(test)));
			String varProcessedInputData = VariableProcessor.processVariables(inputData);
			testSuite.logInputFile( test, varProcessedInputData );
			
			CalculateDeposit calculateDeposit = new CalculateDeposit();
			CalculateDeposit calculateDepositObject = (CalculateDeposit) TestHelper.convertXMLToObject( calculateDeposit, testSuite.getLogInputFilePath(test) );
			
			DepositResult depositResult = null;
			try {
				depositResult = rules.calculateDeposit(123456, calculateDepositObject.getCalculateDepositRequest() );
			}
			catch ( Exception e ) {
				log.error("Error calling FICO : " + e, e );
				testSuite.logErrorFile( test, "Error calling FICO : " + e );
				testSuite.logErrorResult( test, false );
				continue;
			}
			CalculateDepositResponse response = new CalculateDepositResponse();
			response.setCalculateDepositResult(depositResult);
			String  xmlResultDocument = TestHelper.convertObjectToXml( response );
			testSuite.logOutputFile( test, xmlResultDocument );
			XPathEvaluator xPathEvaluator = new XPathEvaluator( xmlResultDocument );
			
			XPathTestVerifier xPathTestVerifier  = new XPathTestVerifier( xPathEvaluator, testSuite.getTestVerificationFile(test), testSuite.getTestVerificationLogFilePath(test) );
			boolean result = xPathTestVerifier.verifyResults();
			testSuite.logResult( test, result );
		}
		
	}



	private void runCreditAssessmentTests(XmlTestSuite testSuite)
			throws IOException, JAXBException, FileNotFoundException,
			ParserConfigurationException, UnsupportedEncodingException,
			SAXException, XPathExpressionException {
		for ( XmlTest test: testSuite.getTests() ) {
			String inputData = ReadWriteTextFile.getContents(new File(testSuite.getInputFilePath(test)));
			String varProcessedInputData = VariableProcessor.processVariables(inputData);
			testSuite.logInputFile( test, varProcessedInputData );
			
			PerformCreditAssessment creditAssessmentRequest = new PerformCreditAssessment();
			PerformCreditAssessment creditAssessmentRequestObject = 
					(PerformCreditAssessment) TestHelper.convertXMLToObject(creditAssessmentRequest,testSuite.getLogInputFilePath(test) );
			CreditAssessmentResult creditAssessmentResult = null;
			try {
			 creditAssessmentResult = rules.performCreditAssessment(123456,creditAssessmentRequestObject.getCreditAssessmentRequest());
			}
			catch ( Exception e ) {
				log.error("Error calling FICO : " + e, e );
				testSuite.logErrorFile( test, "Error calling FICO : " + e );
				testSuite.logErrorResult( test, false );
				continue;
			}
			PerformCreditAssessmentResponse response = new PerformCreditAssessmentResponse();
			response.setCreditAssessmentResult(creditAssessmentResult);
			String  xmlResultDocument = TestHelper.convertObjectToXml( response );
			testSuite.logOutputFile( test, xmlResultDocument );
			XPathEvaluator xPathEvaluator = new XPathEvaluator( xmlResultDocument );
			
			XPathTestVerifier xPathTestVerifier  = new XPathTestVerifier( xPathEvaluator, testSuite.getTestVerificationFile(test), testSuite.getTestVerificationLogFilePath(test) );
			boolean result = xPathTestVerifier.verifyResults();
			testSuite.logResult( test, result );
		}
	}
	
	
		
	private void initialize() throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(new File("test/data/test.properties")));
		m_logsDirectory = props.getProperty("TestLogsDirectory");
		m_testSummaryLogWriter = new BufferedWriter(new FileWriter(m_logsDirectory + File.separator + "testSummaryLog.csv", false));
		m_testSummaryLogWriter.write("Test Name,Result,Log" + "\n");
		String testSuites = props.getProperty("Tests");
		StringTokenizer testStuiteTokenizer = new StringTokenizer(testSuites, ",");
		while ( testStuiteTokenizer.hasMoreTokens() ) {
			String testSuiteName = testStuiteTokenizer.nextToken();
			XmlTestSuite xmlTestSuite = new XmlTestSuite(testSuiteName);
			String testSuiteDirectory = props.getProperty(testSuiteName+".dir");
			xmlTestSuite.setTestDirectory( testSuiteDirectory );
			String tests = props.getProperty( testSuiteName +".tests");
			StringTokenizer testTokenizer = new StringTokenizer(tests,",");
			while ( testTokenizer.hasMoreTokens() ) {
				xmlTestSuite.addTest( testTokenizer.nextToken());
			}
			m_testSuites.add( xmlTestSuite );
		}
	}
	
	private void shutdown() throws RuleServicesException, IOException {
		m_testSummaryLogWriter.flush();
		m_testSummaryLogWriter.close();
		rules.shutdown();
	}
	
	public class XmlTestSuite {
		private String m_testSuiteName;
		private String m_testSuiteDirectory;
		private List<XmlTest> m_tests = new ArrayList<XmlTest>();
		
		public XmlTestSuite(String testSuiteName) {
			m_testSuiteName = testSuiteName;
		}

		

		public void logErrorFile(XmlTest test, String error) throws FileNotFoundException, IOException {
			ReadWriteTextFile.setContents( new File(getLogErrorFilePath(test)), error);
			
		}

		public void logErrorResult(XmlTest test, boolean result) throws IOException {
			m_testSummaryLogWriter.write( test.getTestName() + ", " + (result ? "Passed" : "Failed") + ", " + test.getTestName() + "Error.txt" + "\n" );
		}
		
		public void logResult(XmlTest test, boolean result) throws IOException {
			m_testSummaryLogWriter.write( test.getTestName() + ", " + (result ? "Passed" : "Failed") + ", " + test.getTestName() + "Log.csv" + "\n" );
			
		}

		public String getTestVerificationLogFilePath(XmlTest test) {
			return m_logsDirectory + File.separator + m_testSuiteName + "/" + test.getTestName() + "Log.csv";
		}

		public void logOutputFile(XmlTest test,
				String xmlResultDocument) throws FileNotFoundException, IOException {
			ReadWriteTextFile.setContents( new File(m_logsDirectory + File.separator + m_testSuiteName + "/" + test.getTestName() + "Response.xml" ), xmlResultDocument );
		}

		public void logInputFile(XmlTest test, String contents) throws IOException {
			ReadWriteTextFile.setContents(new File(getLogInputFilePath(test)), contents );
		}
		
		public String getLogErrorFilePath(XmlTest test) {
			return m_logsDirectory + "/" + m_testSuiteName + "/" + test.getTestName() + "Error.txt";
		}
		
		public String getLogInputFilePath(XmlTest test) {
			return m_logsDirectory + "/" + m_testSuiteName + "/" + test.getTestName()  + "Request.xml";
		}

		public String getTestVerificationFile(XmlTest test) {
			return m_testSuiteDirectory + "/" + test.getTestName() + ".verify";
		}

		public String getInputFilePath(XmlTest test) {
			return m_testSuiteDirectory + "/" + test.getTestName() + ".xml";
		}

		public void setTestDirectory(String testSuiteDirectory) {
			m_testSuiteDirectory = testSuiteDirectory;
		}

		public void addTest(String test) {
			m_tests.add( new XmlTest(test) );
		}
		
		public List<XmlTest> getTests() {
			return m_tests;
		}
		
		public String getTestSuiteName() {
			return m_testSuiteName;
		}
		
	}

	public class XmlTest {
		private String m_testName;
		
		public XmlTest(String testName ) {
		 m_testName = testName;
		}

		public String getTestName() {
			return m_testName;
		}
	}
	
}

*/