package com.telus.credit.crda;

import junit.framework.TestCase;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.credit.crda.webservice.impl.CreditDocumentumServiceImpl;
import com.telus.credit.crda.webservice.util.EnvUtilWS;
import com.telus.credit.crddctm.ws.ServiceException;
import com.telus.credit.domain.crddctm.reqresp.RetrieveBureuaReportDocument;
import com.telus.credit.domain.crddctm.reqresp.RetrieveBureuaReportDocumentResponse;
import com.telus.credit.domain.crddctm.reqresp.SaveBureuaReportDocument;
import com.telus.credit.domain.crddctm.reqresp.SaveBureuaReportDocumentResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class CreditDocumentumServiceImplTest extends TestCase {
	CreditDocumentumServiceImpl pojo;
	protected void setUp() throws Exception {
		try {
			super.setUp();
			EnvUtilWS.setupTestEnv();
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("crd-documentum-ws-spring.xml");
			m_ApplicationContext.getBean("DocumentumDao");
			
			pojo = (CreditDocumentumServiceImpl)m_ApplicationContext.getBean("CreditDocumentumServiceTarget");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
    public void testPing(){
    	PingResponse pingResp;
		try {
			pingResp = pojo.ping(new Ping());
			System.out.println("pingResp = " + pingResp.getVersion());
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void testSaveBureuaReportDocument() throws Exception{
    
		try {
			SaveBureuaReportDocument parameters=new SaveBureuaReportDocument();
			parameters.setCreditAssessmentId(new Long(1));
			parameters.setCustomerId(new Long(2));
			String rptStr= "test data";
			byte[] rptByte = rptStr.getBytes();
			parameters.setDocumentContentBinary(rptByte);
			SaveBureuaReportDocumentResponse response = pojo.saveBureuaReportDocument(parameters);
			System.out.println("response = " + response.getDocumentPathTxt());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
    }   
    public void testretrieveBureuaReportDocument() throws Exception{
        
    		try {

    			RetrieveBureuaReportDocument parameters = new RetrieveBureuaReportDocument();
    			parameters.setDocumentPathTxt("DocPathTxt1");
				 RetrieveBureuaReportDocumentResponse response = pojo.retrieveBureuaReportDocument(parameters );
    			System.out.println("response = " + response.getCreditBureauDocument().getDocumentPathTxt());
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			throw e;
    		}
        }      
}
