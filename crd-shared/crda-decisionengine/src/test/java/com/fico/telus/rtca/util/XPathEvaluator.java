package com.fico.telus.rtca.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

public class XPathEvaluator {

	private static DocumentBuilderFactory DOM_FACTORY = DocumentBuilderFactory
			.newInstance();
	static {
		DOM_FACTORY.setNamespaceAware(false); // never forget this!
	}
	
	Document m_document = null;
	
	XPathFactory m_xPathFactory = XPathFactory.newInstance();
	
	public XPathEvaluator(String xmlAsString ) throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException {
		DocumentBuilder documentBuilder = DOM_FACTORY.newDocumentBuilder();
		m_document = documentBuilder.parse( new ByteArrayInputStream(xmlAsString.getBytes("UTF-8")) );
	}
	public String evaluate(String xPathExpression) throws XPathExpressionException {
		return m_xPathFactory.newXPath().compile(xPathExpression).evaluate(m_document);
	}
	
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException, XPathExpressionException {

		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(false); // never forget this!
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse("test/data/CreditAssessment/CreditAssessmentResponse.xml");

		XPathFactory factory = XPathFactory.newInstance();
		
		XPath xpath = factory.newXPath();
		/*XPathExpression expr = xpath
				.compile("//assessmentResultCd/text()");
		//assessmentResultCd/text()
		System.out.println("eval expression");
		// //book[author='Neal Stephenson']/title/text()
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
			System.out.println(nodes.item(i).getNodeValue());
		}*/
		System.out.println(xpath.compile("//assessmentResultCd").evaluate(doc));
	}

}

