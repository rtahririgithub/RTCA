/*package com.telus.credit.wlnprfldmgt.webservice;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.dao.CreditValueDao;
import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.TelusJUnitClassRunner;
import com.telus.credit.wlnprfldmgt.domain.UpdateCreditProfile;
import com.telus.credit.wlnprfldmgt.util.TestHelper;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;

@RunWith(TelusJUnitClassRunner.class)
@TelusConfig(configAppCtxFile="appCtx-wlnCrdDMgmt.properties")
@ContextConfiguration("classpath:test-creditMgt-spring.xml")
public class TestWLNCreditProfileMgmtLinksBug {

	@Autowired
	@Qualifier("CreditProfileMgtSvcImpl")
    WLNCreditProfileDataManagementServicePortType m_wlnSvc;
    
    @Autowired
    CreditValueDao m_creditValueDao;

    @Autowired
    CreditProfileDao m_creditProfileDao;
    
    boolean m_encryptIds = true;
    
    @Test
    public void test_updateCreditProfile() throws JAXBException, PolicyException, ServiceException {
    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), "test/data/CreditIdUpdateRequest.xml" );
    	Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 1);
        Date datePlusOne = cal.getTime();
    	updateCreditProfile.getAuditInfo().setTimestamp(datePlusOne);
    	if ( m_encryptIds ) {
    		if ( updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense() != null 
    			 && updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense().getDriverLicenseNum() != null ) {
    			updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense().setDriverLicenseNum(
    			EncryptionUtil.encrypt(updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense().getDriverLicenseNum() ) );
    		}
    		if ( updateCreditProfile.getCreditProfile().getCreditIdentification().getSin() != null ) {
    			updateCreditProfile.getCreditProfile().getCreditIdentification().setSin(
    					EncryptionUtil.encrypt(updateCreditProfile.getCreditProfile().getCreditIdentification().getSin() ) );
    		}
    	}
    	m_wlnSvc.updateCreditProfile(updateCreditProfile.getCreditProfile(), 
    			updateCreditProfile.getRemoveWLNCreditIdentificationInfo(), 
    			updateCreditProfile.getCreditValueCd(),
    			updateCreditProfile.getFraudIndicatorCd(),
    			updateCreditProfile.getAuditInfo() );
    }
}*/