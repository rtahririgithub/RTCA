package com.telus.credit.wlnprfldmgt.webservice;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.wlnprfldmgt.webservice.impl.WLNCreditProfileDataManagementServiceImpl;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;

import junit.framework.TestCase;

@ContextConfiguration("classpath:creditMgt-spring.xml")

public class Test extends TestCase {

	protected void setUp() throws Exception {
 		try {
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("creditMgt-spring.xml");
			WLNCreditProfileDataManagementServiceImpl pojo = 
					(WLNCreditProfileDataManagementServiceImpl)m_ApplicationContext.getBean("CreditProfileMgtSvcImpl");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}
	
	public void testSetup(){
		
	}

}
