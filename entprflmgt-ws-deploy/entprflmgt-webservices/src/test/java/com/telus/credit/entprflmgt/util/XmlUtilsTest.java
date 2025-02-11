package com.telus.credit.entprflmgt.util;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;

import junit.framework.Assert;

import org.junit.Test;
import com.telus.credit.entprflmgt.TestConsumerCreditProfileInfoFactory;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.wlnprfldmgt.domain.CreditCardCode;

public class XmlUtilsTest {
	private static final String EXPECTED_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			"<ns4:updateCreditProfile xmlns:ns2=\"http://xmlschema.tmi.telus.com/xsd/Customer/Customer/WirelineCreditProfileManagementTypes_v1\" " +
			"xmlns=\"http://xmlschema.tmi.telus.com/xsd/Customer/Customer/EnterpriseCreditProfileManagementServiceTypes_v1\" " +
			"xmlns:ns4=\"http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditProfileManagementServiceRequestResponse_v1\" xmlns:ns3=\"http://xmlschema.tmi.telus.com/xsd/Customer/Customer/CustomerManagementCommonTypes_v3\" " +
			"xmlns:ns5=\"http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/EnterpriseCommonTypes_v3\">" +
			"<ns4:consumerCreditProfileInfo><identification><customerId>123</customerId></identification>" +
			"<creditCardCode><ns2:primaryCreditCardCode>primary cc code</ns2:primaryCreditCardCode>" +
			"<ns2:secondaryCreditCardCode>secondary cc code</ns2:secondaryCreditCardCode></creditCardCode>" +
			"</ns4:consumerCreditProfileInfo></ns4:updateCreditProfile>";

	@Test
	public void testConvertToXml() {
		CreditCardCode ccc = new CreditCardCode();
		ccc.setPrimaryCreditCardCd("primary cc code");
		ccc.setSecondaryCreditCardCd("secondary cc code");
		
		Identification id = new Identification();
		id.setCustomerId(123l);
		
		ConsumerCreditProfileInfo consumerInfo = new ConsumerCreditProfileInfo();
		consumerInfo.setCreditCardCode(ccc);
		consumerInfo.setIdentification(id);
		
		UpdateCreditProfile updateCrdPrflRequest = new UpdateCreditProfile(); 
		updateCrdPrflRequest.setConsumerCreditProfileInfo(consumerInfo);
		String xml = XmlUtils.convertToXml(updateCrdPrflRequest);
//		System.out.println(xml);
		
		assertEquals(EXPECTED_XML, xml);
	}
	
//	@Test 
	public void testGenerateEntirePayload() throws DatatypeConfigurationException {
		UpdateCreditProfile updateCrdPrflRequest = new UpdateCreditProfile(); 
		updateCrdPrflRequest.setConsumerCreditProfileInfo(TestConsumerCreditProfileInfoFactory.newInstance());
		String xml = XmlUtils.convertToXml(updateCrdPrflRequest);
		System.out.println(xml);
	}
	

}
