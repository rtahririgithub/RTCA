/*package com.telus.credit.wlnprflmatch;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;





import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.framework.test.TestUtil;


import com.telus.credit.wlnprflmatch.domain.CreditIdentification;
import com.telus.credit.wlnprflmatch.webservice.impl.WLNCreditProfileMatchServiceImpl;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;

import org.junit.Test;

@SuppressWarnings("deprecation")
@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx_WLNCrdPrflMgtWs.properties")
@ContextConfiguration("classpath:wlnCreditProfileManagement-spring.xml")
public class WLNCreditProfileMgmtServiceImplTest {

	private static final Log s_log = LogFactory.getLog(WLNCreditProfileMgmtServiceImplTest.class);

	@Autowired
	private WLNCreditProfileMatchServiceImpl m_SvcImpl;

//	@Test
//	public void testCreateProfile() throws PolicyException, ServiceException 
//	{
//		AuditInfo auditInfo=new AuditInfo();
//		auditInfo.setUserId("T829939");
//		auditInfo.setOriginatorApplicationId("Credit");
//		
//		CreditProfileData creditProfileData=new CreditProfileData();
//		creditProfileData.setCustomerID(999999915);
//		
//        CustomerGuarantor guarantor=new CustomerGuarantor();
//        guarantor.setGuarantorCustomerID(999999915);
//        guarantor.setExpiryDate(new Date());
//        
//        creditProfileData.setCustomerGuarantor(guarantor);
//        m_SvcImpl.createCreditProfile(creditProfileData, auditInfo);
//        
//	}
	
	@Test
	public void testcheckCreditProfile() throws ServiceException 
	{
		AuditInfo auditInfo=new AuditInfo();
		auditInfo.setUserId("T829939");
		auditInfo.setOriginatorApplicationId("Credit");
		
        long customerId=19231681;
        
        CreditIdentification creditIdentification  = new CreditIdentification();
        creditIdentification.setSin("3475785876");	
        
        
        m_SvcImpl.checkCreditProfileByCreditId(creditIdentification, auditInfo);
        
	} 
	
}

*/