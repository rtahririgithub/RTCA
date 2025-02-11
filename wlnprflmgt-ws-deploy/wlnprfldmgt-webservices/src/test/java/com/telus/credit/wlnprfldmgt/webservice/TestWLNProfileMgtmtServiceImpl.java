package com.telus.credit.wlnprfldmgt.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.telus.credit.dao.CreditAttributeDao;
import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.dao.CreditValueDao;
import com.telus.credit.domain.CreditAttribute;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.wlnprfldmgt.domain.CreditAssessmentResult;
import com.telus.credit.wlnprfldmgt.domain.UpdateCreditProfile;
import com.telus.credit.wlnprfldmgt.domain.UpdateCreditWorthiness;
import com.telus.credit.wlnprfldmgt.util.TestHelper;
import com.telus.credit.wlnprfldmgt.webservice.impl.WLNCreditProfileDataManagementServiceImpl;
import com.telus.tmi.xmlschema.xsd.customer.customer.enterprisecreditassessmenttypes_v2.CreditAssessmentTransaction;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;



public class TestWLNProfileMgtmtServiceImpl extends TestCase {

    private static final Log log = LogFactory
    .getLog(TestWLNProfileMgtmtServiceImpl.class);

    @Autowired
    WLNCreditProfileDataManagementServiceImpl m_wlnSvc;
    
    @Autowired
    CreditValueDao m_creditValueDao;

    @Autowired
    CreditProfileDao m_creditProfileDao;
    
    @Autowired
    CreditAttributeDao m_creditAttributeDao;
    
	protected void setUp() throws Exception {
		super.setUp();
		try {
			AbstractApplicationContext m_ApplicationContext = new ClassPathXmlApplicationContext("test-creditMgt-spring.xml");
			m_wlnSvc = (WLNCreditProfileDataManagementServiceImpl)m_ApplicationContext.getBean("CreditProfileMgtSvcImpl");
			m_creditProfileDao=(CreditProfileDao)m_ApplicationContext.getBean("CreditProfileDaoImpl");
			m_creditAttributeDao=(CreditAttributeDao)m_ApplicationContext.getBean("CreditAttributeDaoImpl");
			m_creditValueDao=(CreditValueDao)m_ApplicationContext.getBean("CreditValueDaoImpl");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

	}
	 public void testSetup() {
		 
	 }
    private AuditInfo createAuditInfo() {
	AuditInfo auditInfo = new AuditInfo();
	auditInfo.setUserId("Gurb");
	auditInfo.setOriginatorApplicationId("1234");
	auditInfo.setTimestamp( new Date() );
	return auditInfo;
    }

    
    /**
     * @deprecated TODO use binding   
     */
    private static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }
		
    
    /**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;
    static {
	try {
	    df = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException dce) {
	    throw new IllegalStateException(
					    "Exception while obtaining DatatypeFactory instance", dce);
	}
    }
    

     public void testUpdateCreditWorthiness() {
 		
        String filename = "src/test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthiness...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            if (updateCreditWorthiness.getCreditAssessmentTransactionResult() == null && updateCreditWorthiness.getAuditInfo() == null) {
            	updateCreditWorthiness.setCreditAssessmentTransactionResult(new CreditAssessmentTransaction());
            	updateCreditWorthiness.setAuditInfo(new AuditInfo());
            	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditDecisioningResult(new CreditAssessmentResult());
            	this.setUpdateCreditWorthiness(updateCreditWorthiness);
            }
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            this.setUpdateCreditWorthinessForCRIandCreditValueCd(updateCreditWorthiness);
            
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            this.showCreditWorthiness(creditValue, creditAttributeList);
            this.setUpdateCreditWorthinessForCRI(updateCreditWorthiness);
            
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            //System.out.println("Credit Value Fraud List: " + creditValue.getFraudMessageCodeList() );
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }
    
    /*@Ignore
     public void testUpdateCreditWorthinessCreditAttributes() {
        String filename = "test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthinessCreditAttributes...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            
            Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	        updateCreditWorthiness.getAuditInfo().setTimestamp(datePlusOne);
	    	
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            log.debug( "End  testUpdateCreditWorthinessCreditAttributes...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }*/
    
    /*@Ignore
     public void testUpdateCreditWorthinessNoChangeScenario() {
        String filename = "test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthinessNoChangeScenario...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            
            Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	        updateCreditWorthiness.getAuditInfo().setTimestamp(datePlusOne);
	        
	        updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setAssessmentResultReasonCd("NO_CHANGE");
	        
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            log.debug( "End  testUpdateCreditWorthinessNoChangeScenario...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }*/
    
    /*@Ignore
     public void testUpdateCreditWorthinessManualAssessment() {
        String filename = "test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthinessManualAssessment...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            log.debug( "End  testUpdateCreditWorthinessManualAssessment...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }*/
    
    /*@Ignore
     public void testUpdateCreditWorthinessCRIActiveAssessment() {
        String filename = "test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthinessCRIActiveAssessment...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            log.debug( "End  testUpdateCreditWorthinessCRIActiveAssessment...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }*/
    
    /*@Ignore
     public void testUpdateCreditWorthinessCRIVoidAssessment() {
        String filename = "test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthinessCRIVoidAssessment...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            log.debug( "End  testUpdateCreditWorthinessCRIVoidAssessment...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }*/
    
    /*@Ignore
     public void testUpdateCreditWorthinessNewCustomerComment() {
        String filename = "test/data/UpdateCreditWorthiness.xml";
        try {
            log.debug( "Start  testUpdateCreditWorthinessNewCustomerComment...");
            UpdateCreditWorthiness updateCreditWorthiness = (UpdateCreditWorthiness) TestHelper.convertXMLToObject( new UpdateCreditWorthiness(), filename );
            m_wlnSvc.updateCreditWorthiness( updateCreditWorthiness.getCreditAssessmentTransactionResult(),
                    updateCreditWorthiness.getAuditInfo() );
            long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId( 
            		new Integer((int)updateCreditWorthiness.getCreditAssessmentTransactionResult().getCustomerID() ) );
            CreditValue creditValue = m_creditValueDao.getCreditValue( creditProfileId );
            List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfileId);
            
            this.showCreditWorthiness(creditValue, creditAttributeList);
            
            log.debug( "End  testUpdateCreditWorthinessNewCustomerComment...");
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }  
    }*/

    
    public void testUpdateCreditProfile( ) {
	String filename = "src/test/data/AT4.1UpdateCreditProfile.xml";
	//String filename = "src/test/data/UpdateCreditProfile-provinceOfCurrentResidency.xml";
	//String filename = "src/test/data/UpdateCreditProfile-Guarantor.xml";
	try {
	    log.debug( "Start  testUpdateCreditProfile...");
	    UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    System.out.println("Customer ID : " + updateCreditProfile.getCreditProfile().getCustomerID());
	    System.out.println("Method Code : " + updateCreditProfile.getCreditProfile().getMethodCd());
	    System.out.println("Comment : " + updateCreditProfile.getCreditProfile().getCommentTxt());
	    m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
	    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
	            updateCreditProfile.getCreditValueCd(), 
	            updateCreditProfile.getFraudIndicatorCd(), 
	            updateCreditProfile.getAuditInfo() );
	}
	catch ( Exception e ) {
	    e.printStackTrace();
	}
    }
    
    /*@Ignore
    
    public void testUpdateProvinceofCurrentResidency () {
    	
    	String filename = "test/data/UpdateCreditProfile-provinceOfCurrentResidency.xml";
    	
    	try {
	    	log.debug("Start testUpdateProvinceofCurrentResidency...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	CreditProfile origCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	if (origCrdProfile.getProvinceOfCurrentResidenceCd().equals("AL")) {
	    		if (updateCreditProfile.getCreditProfile().getPersonalInfo().
	    				getProvinceOfCurrentResidenceCd().equals("AL"))
	    			updateCreditProfile.getCreditProfile().getPersonalInfo().
	    			setProvinceOfCurrentResidenceCd("BC");
	    	} else if (origCrdProfile.getProvinceOfCurrentResidenceCd().equals("BC")) {
	    		if (updateCreditProfile.getCreditProfile().getPersonalInfo().
	    				getProvinceOfCurrentResidenceCd().equals("BC"))
	    			updateCreditProfile.getCreditProfile().getPersonalInfo().
	    			setProvinceOfCurrentResidenceCd("AL");
	    	} else {
	    		updateCreditProfile.getCreditProfile().getPersonalInfo().
    			setProvinceOfCurrentResidenceCd("BC");
	    	}
	    	
	    	updateCreditProfile.getCreditProfile().getPersonalInfo().
			setProvinceOfCurrentResidenceCd("OTR");
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	Assert.assertEquals("Province of Current Residency should be... " + 
	    			updateCreditProfile.getCreditProfile().getPersonalInfo().getProvinceOfCurrentResidenceCd(), 
	    			updateCreditProfile.getCreditProfile().getPersonalInfo().getProvinceOfCurrentResidenceCd(), 
	    			updatedCrdProfile.getProvinceOfCurrentResidenceCd());
	    	log.debug("Successfully update the Province of Current Residency from " + origCrdProfile.getProvinceOfCurrentResidenceCd()
	    			+ " to " + updatedCrdProfile.getProvinceOfCurrentResidenceCd());
	    	log.debug("End testUpdateProvinceofCurrentResidency...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*//@Ignore
    
    public void testUpdateInvalidProvinceofCurrentResidency () {
    	
    	String filename = "test/data/UpdateCreditProfile-provinceOfCurrentResidency.xml";
    	
    	try {
	    	log.debug("Start testUpdateInvalidProvinceofCurrentResidency...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	updateCreditProfile.getCreditProfile().getPersonalInfo().
			setProvinceOfCurrentResidenceCd("OT");
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	
	    	log.debug("End testUpdateInvalidProvinceofCurrentResidency...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateInvalidGuarantor () {
    	
    	String filename = "test/data/UpdateCreditProfile-Guarantor.xml";
    	
    	try {
	    	log.debug("Start testUpdateInvalidGuarantor...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	
	    	log.debug("End testUpdateInvalidGuarantor...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateInvalidGuarantorId () {
    	
    	String filename = "test/data/UpdateCreditProfile-Guarantor.xml";
    	
    	try {
	    	log.debug("Start testUpdateInvalidGuarantor...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	updateCreditProfile.getCreditProfile().getCustomerGuarantor().setGuarantorCustomerID(11111111);
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	
	    	log.debug("End testUpdateInvalidGuarantor...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateGuarantor () {
    	
    	String filename = "test/data/UpdateCreditProfile-Guarantor.xml";
    	
    	try {
	    	log.debug("Start testUpdateGuarantor...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	CreditProfile origCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	updateCreditProfile.getCreditProfile().getCustomerGuarantor().setGuarantorCustomerID(22188264);
	    	updateCreditProfile.getCreditProfile().getCustomerGuarantor().setGuaranteedAmt(new BigDecimal(450.00));
	    	updateCreditProfile.getCreditProfile().getCustomerGuarantor().setId(origCrdProfile.getCustomerGuarantor().get_id());
	    	updateCreditProfile.getCreditProfile().getCustomerGuarantor().setGuarantorCreditProfileID(4509285008L);
	    	
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Original Guarantor : " + origCrdProfile.getCustomerGuarantor().toString());
	    	log.debug("Updated Guarantor : " + updatedCrdProfile.getCustomerGuarantor().toString());
	    	log.debug("End testUpdateGuarantor...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileConsentYtoN () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileConsentYtoN...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	PersonalInfo personalInfo = new PersonalInfo();
	    	personalInfo.setCreditCheckConsentCd("N");
	    	updateCreditProfile.getCreditProfile().setPersonalInfo(personalInfo);
	    	updateCreditProfile.getCreditProfile().setCommentTxt("Test- consent changed");
	    	
	    	Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	    	updateCreditProfile.getAuditInfo().setTimestamp(datePlusOne);
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileConsentYtoN...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileConsentNtoY () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileConsentNtoY...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	PersonalInfo personalInfo = new PersonalInfo();
	    	personalInfo.setCreditCheckConsentCd("Y");
	    	updateCreditProfile.getCreditProfile().setPersonalInfo(personalInfo);
	    	CreditAddress creditAddress = new CreditAddress();
	    	creditAddress.setAddressLineOne("8262 119 Street");
	    	creditAddress.setCityName("Delta");
	    	creditAddress.setPostalCd("V4C6M6");
	    	creditAddress.setCountryCd("CAN");
	    	creditAddress.setProvinceCd("BC");
	    	//creditAddress.setAddressTypeCd("CB");
	    	creditAddress.setCreditAddressTypeCd("CB");
	    	updateCreditProfile.getCreditProfile().setCreditAddress(creditAddress);
	    	updateCreditProfile.getCreditProfile().setCommentTxt("Test- consent changed");
	    	
	    	Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	    	updateCreditProfile.getAuditInfo().setTimestamp(datePlusOne);
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileConsentNtoY...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditAddress () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditAddress...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	CreditAddress creditAddress = new CreditAddress();
	    	creditAddress.setAddressLineOne("8265 119 Street");
	    	creditAddress.setCityName("Delta");
	    	creditAddress.setPostalCd("V4C6M6");
	    	creditAddress.setCountryCd("CAN");
	    	creditAddress.setProvinceCd("BC");
	    	creditAddress.setCreditAddressTypeCd("CB");
	    	updateCreditProfile.getCreditProfile().setCreditAddress(creditAddress);
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditAddress...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileFraudIndicator () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileFraudIndicator...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	updateCreditProfile.setFraudIndicatorCd("R");
	    	updateCreditProfile.setCreditValueCd("E");
	    	PersonalInfo personalInfo = new PersonalInfo();
	    	personalInfo.setProvinceOfCurrentResidenceCd("BC");
	    	updateCreditProfile.getCreditProfile().setPersonalInfo(personalInfo);
	    	Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	    	updateCreditProfile.getAuditInfo().setTimestamp(datePlusOne);
	    	updateCreditProfile.getCreditProfile().setCommentTxt("Test- fraud ind changed");
	    	updateCreditProfile.getCreditProfile().setCustomerID(22190647);
	    	
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileFraudIndicator...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileCreditValue () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileCreditValue...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	updateCreditProfile.setFraudIndicatorCd("R");
	    	updateCreditProfile.setCreditValueCd("E");
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileCreditValue...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileCRIN () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileCRIN...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	    	updateCreditProfile.getAuditInfo().setTimestamp(datePlusOne);
	    	CreditIdentification creditId = new CreditIdentification();
	    	creditId.setSin("440937928");
	    	updateCreditProfile.getCreditProfile().setCreditIdentification(creditId);
	    	
	    	
	    	if ( updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense() != null 
	    			 && updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense().getDriverLicenseNum() != null ) {
	    			updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense().setDriverLicenseNum(
	    			EncryptionUtil.encrypt(updateCreditProfile.getCreditProfile().getCreditIdentification().getDriverLicense().getDriverLicenseNum() ) );
	    	}
	    	if ( updateCreditProfile.getCreditProfile().getCreditIdentification().getSin() != null ) {
	    			updateCreditProfile.getCreditProfile().getCreditIdentification().setSin(
	    			EncryptionUtil.encrypt(updateCreditProfile.getCreditProfile().getCreditIdentification().getSin() ) );
	    	}
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileCRIN...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileAddresstoUS () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileAddresstoUS...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	CreditAddress creditAddress = new CreditAddress();
	    	creditAddress.setAddressLineOne("8262 119 Street");
	    	creditAddress.setCityName("Delta");
	    	creditAddress.setPostalCd("V4C6M6");
	    	creditAddress.setCountryCd("USA");
	    	creditAddress.setProvinceCd("GU");
	    	//creditAddress.setAddressTypeCd("CB");
	    	creditAddress.setCreditAddressTypeCd("CB");
	    	updateCreditProfile.getCreditProfile().setCreditAddress(creditAddress);
	    	updateCreditProfile.getCreditProfile().setCommentTxt("Test- consent changed");
	    	
	    	Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date());
	        cal.add(Calendar.DATE, 1);
	        Date datePlusOne = cal.getTime();
	    	updateCreditProfile.getAuditInfo().setTimestamp(datePlusOne);
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileAddresstoUS...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileEmptyConsentID () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileEmptyConsentID...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	//TODO: set US consent id here
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileEmptyConsentID...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileCreditValueConversion () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileCreditValueConversion...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	//TODO: set US consent id here
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileCreditValueConversion...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    /*@Ignore
    
    public void testUpdateCreditProfileCRINConversion () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileCRINConversion...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	//TODO: set US consent id here
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileCRINConversion...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
   /* @Ignore
    
    public void testUpdateCreditProfileFraudIndConversion () {
    	
    	String filename = "test/data/AT4.1UpdateCreditProfile.xml";
    	
    	try {
	    	log.debug("Start testUpdateCreditProfileFraudIndConversion...");
	    	UpdateCreditProfile updateCreditProfile = (UpdateCreditProfile) TestHelper.convertXMLToObject( new UpdateCreditProfile(), filename );
	    	
	    	//TODO: set US consent id here
	    	
	    	m_wlnSvc.updateCreditProfile( updateCreditProfile.getCreditProfile(), 
		    		updateCreditProfile.getRemoveWLNCreditIdentificationInfo(),
		            updateCreditProfile.getCreditValueCd(), 
		            updateCreditProfile.getFraudIndicatorCd(), 
		            updateCreditProfile.getAuditInfo() );
	    	
	    	CreditProfile updatedCrdProfile = m_creditProfileDao.getCreditProfile(
	    			new Long(updateCreditProfile.getCreditProfile().getCustomerID()).intValue());
	    	
	    	log.debug("Updated CreditProfile : " + updatedCrdProfile.toString());
	    	
	    	log.debug("End testUpdateCreditProfileFraudIndConversion...");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }*/
    
    private void showCreditWorthiness(CreditValue creditValue, List<CreditAttribute> creditAttributeList) {
    	System.out.println("Credit Value Code : " + creditValue.getCreditValueCode());
    	System.out.println("Telus Decision Code : " + creditValue.getDecisionCd());
    	System.out.println("Fraud Indicator : " + creditValue.getFraudIndicatorCd());
    	System.out.println("Risk Level : " + creditValue.getRiskLevelNum());
    	
    	for (CreditAttribute creditAttribute : creditAttributeList) {
    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.CREDIT_REPORT_INDICATOR)) 
    			System.out.println("Credit Report Indicator : " + creditAttribute.getAttributeValue());
    		
    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.FIRST_ASSESSMENT_DATE))
    			System.out.println("First Assessment Date : " + creditAttribute.getAttributeValue());
    		
    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.CREDIT_VALUE_EFFECTIVE_DATE))
    			System.out.println("Credit Value Effective Date : " + creditAttribute.getAttributeValue());
    		
    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.LATEST_ASSESSMENT_DATE))
    			System.out.println("Latest Assessment Date : " + creditAttribute.getAttributeValue());
    		
    	}
    }
    
    private void setUpdateCreditWorthinessForCRIandCreditValueCd(UpdateCreditWorthiness updateCreditWorthiness) {
    	// set Credit Report Indicator to true, credit value code to R and credit assessment date to current date
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditBureauReportInd(true);
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setCreditValueCd("R");
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditAssessmentDate(new Date());
    	
    }
    
    private void setUpdateCreditWorthinessForCRI(UpdateCreditWorthiness updateCreditWorthiness) {
    	// set Credit Report Indicator to false, credit value code to R and credit assessment date to current date
    	// No Change scenario
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditBureauReportInd(false);
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setAssessmentResultReasonCd("NO_CHANGE");
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setCreditValueCd("R");
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditAssessmentDate(new Date());
    	
    }
    
    private void setUpdateCreditWorthiness(UpdateCreditWorthiness updateCreditWorthiness) {
    	// set Credit Assessment Transaction Result if null
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditAssessmentID(1083);
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCustomerID(96572655);
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setCreditValueCd("E");
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setCreditRiskLevel(1001);
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().getCreditDecisioningResult().setDecisionCd("testCd");
    	String inputString = "2018-11-15";
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date inputDate=new Date();
		try {
			inputDate = dateFormat.parse(inputString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCreditAssessmentDate(inputDate);
    	updateCreditWorthiness.getCreditAssessmentTransactionResult().setCommentTxt("DV01 testing");
    	updateCreditWorthiness.getAuditInfo().setUserId("122365");
    	updateCreditWorthiness.getAuditInfo().setOriginatorApplicationId("1212");
    	updateCreditWorthiness.getAuditInfo().setTimestamp(new Date());
    }
}
