package com.fico.telus.rtca.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fico.telus.rtca.blaze.RuleServicesBean;

public class XPathTestVerifier {

	/*
     * Log object
     */
    private static final Log log = LogFactory.getLog(XPathTestVerifier.class);
    
	private XPathEvaluator m_xPathEvaluator;
	private Properties m_properties;
	private Writer m_testVerificationLog;
	public XPathTestVerifier(XPathEvaluator evaluator, String xPathResultVerificationFile, String testVerificationLogFile ) throws FileNotFoundException, IOException {
		m_properties = new Properties();
		m_properties.load(new FileInputStream(new File(xPathResultVerificationFile)));
		m_xPathEvaluator = evaluator;
		m_testVerificationLog = new BufferedWriter(new FileWriter(testVerificationLogFile));
		m_testVerificationLog.write("Field,Expected,Found,Match" + "\n");
	}
	
	public boolean verifyResults() throws XPathExpressionException, IOException {
		boolean result = true;
		Iterator<Map.Entry<Object,Object>> iter = (Iterator<Map.Entry<Object,Object>>) m_properties.entrySet().iterator();
		while ( iter.hasNext() ) {
			Map.Entry<Object,Object> nextEntry = iter.next();
			String xPathExpression = (String) nextEntry.getKey();
			String value = (String) nextEntry.getValue();
			String xPathValue = m_xPathEvaluator.evaluate(xPathExpression);
			if ( (value == null && xPathValue == null )
				 || ( value != null && xPathValue != null && value.trim().equals(xPathValue.trim() ) ) ) {
				log.debug("Verification passed for : Xpath Expression: " + xPathExpression
						  + ", Found: " + xPathValue + " value should be: " + value );
				m_testVerificationLog.write( xPathExpression + "," + value + "," + xPathValue + "," + "Y" + "\n");
			}
			else {
				log.error("Verification failed for: Xpath Expression: " + xPathExpression
						  + ", Found: " + xPathValue + " value should be: " + value );
				m_testVerificationLog.write( xPathExpression + "," + value + "," + xPathValue + "," + "N" + "\n");
				result = false;
			}
		}
		m_testVerificationLog.flush();
		m_testVerificationLog.close();
		return result;
	}
}
