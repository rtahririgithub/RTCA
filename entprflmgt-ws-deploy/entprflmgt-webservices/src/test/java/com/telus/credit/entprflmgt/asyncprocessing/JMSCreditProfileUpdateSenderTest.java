package com.telus.credit.entprflmgt.asyncprocessing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.core.JmsTemplate;

public class JMSCreditProfileUpdateSenderTest {
	private static JMSCreditProfileUpdateSender jmsSender;
	private static BeanFactory beanFactory;
	
	@BeforeClass
	public static void setUpOnce() {
		beanFactory = new XmlBeanFactory(new ClassPathResource("test-enterpriseProfileManagement-spring.xml"));
		JmsTemplate jmsTemplate = (JmsTemplate) beanFactory.getBean("jmsTemplate");
		jmsSender = new JMSCreditProfileUpdateSender(jmsTemplate);
	}

	//@Test
	public void testSendCreditProfileInfoToWireline() throws Exception {
		String wlnPaload = getResourceAsString("WLN_payload.xml");
		jmsSender.sendCreditProfileInfoToWireline(wlnPaload, null);
	}

	@Test
	public void testSendCreditProfileInfoToWireless() throws Throwable {
		String wlsPaload = getResourceAsString("WLS_payload.xml");
		jmsSender.sendCreditProfileInfoToWireless(wlsPaload, null);
	}
	
	private String getResourceAsString(String fileName) throws Exception {
		final File resourceFile = new File(getClass().getResource(fileName).getFile());
		return FileUtils.readFileToString(resourceFile);
	}

}
