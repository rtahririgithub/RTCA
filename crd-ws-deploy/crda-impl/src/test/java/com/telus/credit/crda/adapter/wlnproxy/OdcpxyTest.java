/*package com.telus.credit.crda.adapter.wlnproxy;

 
import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.wsdl.cmo.ordermgmt.orderdepositcalculatorproxyservice_1.OrderDepositCalculatorProxyServicePortType;
@RunWith(TelusJUnitClassRunner.class)


@TelusConfig(configAppCtxFile="crda-test-appCtx.properties")
@ContextConfiguration("classpath:test/ws-ordpxy-client-test-spring.xml")



public class OdcpxyTest extends TestCase {

     @Autowired
     OrderDepositCalculatorProxyServicePortType  m_OrderDepositCalculatorProxyServicePortType;

	@Test	
	public void testPing() throws Throwable, Exception{
		try {
			System.out.println( "Ping result : \n" + m_OrderDepositCalculatorProxyServicePortType.ping()

			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
*/