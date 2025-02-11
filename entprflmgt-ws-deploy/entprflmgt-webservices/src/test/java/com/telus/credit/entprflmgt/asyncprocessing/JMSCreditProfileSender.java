/*package com.telus.credit.entprflmgt.asyncprocessing;


import java.io.File;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;

import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.entprflmgt.util.TestHelper;
import com.telus.credit.entprflmgt.util.XmlUtils;
import com.telus.credit.entprflmgt.webservice.impl.RoutingHandler;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;


@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx-enterpriseCrd.properties")
@ContextConfiguration("classpath:enterpriseProfileManagement-spring.xml")
public class JMSCreditProfileSender {

	private static final Log log = LogFactory.getLog(JMSCreditProfileSender.class);
	
	@Autowired
	private JMSCreditProfileUpdateSender m_jmsCrdPrflUpdateSender;
	
	@Test
	public void test_sendMessage() throws Exception {
		UpdateCreditProfile updateCreditProfile = new UpdateCreditProfile();
		UpdateCreditProfile updateCreditProfileObj = (UpdateCreditProfile)TestHelper.convertXMLToObject( updateCreditProfile, "test/data/WLNCreditProfileUpdateRequest.xml" );
		String xmlString = TestHelper.convertObjectToXml(updateCreditProfileObj);
		//RoutingHandler.setEmHeader(getResourceAsString("test/data/testSOAPHeader.xml"));
		String xmlString = getResourceAsString( "test/data/WLNCreditProfileUpdateRequest.xml" );
		System.out.println("Sending: " + xmlString );
		m_jmsCrdPrflUpdateSender.setInjectEMHeader(false);
		m_jmsCrdPrflUpdateSender.sendCreditProfileInfoToWireline(xmlString, null);
		System.out.println("Sent");
	}
	
	private String getResourceAsString(String fileName) throws Exception {
		//final File resourceFile = new File(getClass().getResource(fileName).getFile());
		File resourceFile = new File(fileName);
		return FileUtils.readFileToString(resourceFile);
	}
}
*/