package com.telus.credit.entprflmgt.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.wlnprfldmgt.domain.CreditProfileData;
import com.telus.credit.wlnprfldmgt.domain.PersonalInfo;
import com.telus.credit.wlnprfldmgt.domain.RemoveWLNCreditIdentificationInfo;
import com.telus.credit.wlnprfldmgt.domain.UpdateCreditProfile;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;

/**
 * <p>
 * Helper class that deals with external web-service to update WIRELINE Credit Profile.
 * Provides methods to convert given {@link com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo} to the
 * format that external web-service understands and send request to that service.
 * 
 * @author Danil Glinenko
 *
 */
public class WLNCrdProfileUpdater extends CrdProfileUpdater {
	private static final Log log = LogFactory.getLog(WLNCrdProfileUpdater.class);
	private WLNCreditProfileDataManagementServicePortType m_wlnCreditProfileDataMgtService;
	
	/**
	 * Constructor that takes external web-services interface.
	 */
	public WLNCrdProfileUpdater(WLNCreditProfileDataManagementServicePortType clientServiceInterface) {
		this.m_wlnCreditProfileDataMgtService = clientServiceInterface;
	}

	/**
	 *<p>
	 * Sends request to update consumer credit profile to WLNCreditProfileDataManagementService.
	 * @param consumerCreditProfileInfo the source of data to be used to form request 
	 * to external web-service to update credit profile.
	 * 
	 * @throws CreditProfileUpdateException if the operation cannot be performed for various reasons.
	 */
	public void updateCreditProfile(
			ConsumerCreditProfileInfo consumerCreditProfileInfo, AuditInfo auditInfo)
			throws CreditProfileUpdateException {
		if (consumerCreditProfileInfo == null || consumerCreditProfileInfo.getIdentification().getCustomerId() == null) { 
			throw new IllegalArgumentException("consumerCreditProfileInfo argument cannot be null and customerId must be set.");
		}
		final UpdateCreditProfile creditProfileUpdateRequest = this.buildServiceRequest(consumerCreditProfileInfo, auditInfo);
		// send request to the wlnService.
		final String logMessageDetails = "CustomerId: "  + creditProfileUpdateRequest.getCreditProfile().getCustomerID();
		try {
			this.m_wlnCreditProfileDataMgtService.updateCreditProfile(creditProfileUpdateRequest.getCreditProfile(),
					new RemoveWLNCreditIdentificationInfo(),
			        creditProfileUpdateRequest.getCreditValueCd(),
			        creditProfileUpdateRequest.getFraudIndicatorCd(),
					creditProfileUpdateRequest.getAuditInfo());
		} 
		catch (PolicyException e) {
			CreditProfileUpdateException cpue = new CreditProfileUpdateException("Policy Exception updating WIRELINE Credit profile. " +logMessageDetails, e);
			throw cpue;
		} catch (ServiceException e) {
			CreditProfileUpdateException cpue = new CreditProfileUpdateException("Service Exception updating WIRELINE Credit profile. " +logMessageDetails, e);
			throw cpue;
		} catch (RuntimeException e){
			log.error("Unknown exception updating WIRELINE Credit profile. " +logMessageDetails,
					e);
			throw e;
		}
	}
	
	/**
     * <p>
     * Builds the target web-service specific async request object and return it.
     * This needs to be implemented in subclasses and contain web-service specific code.
     * @param consumerCreditProfileInfo the source data to be used to build web-service request object.
     * @return target web-service request object.
     */
    public Object buildAsyncServiceRequest(ConsumerCreditProfileInfo consumerCreditProfileInfo, 
            AuditInfo auditInfo)
    {
        if (consumerCreditProfileInfo == null || consumerCreditProfileInfo.getIdentification().getCustomerId() == null) { 
            throw new IllegalArgumentException("consumerCreditProfileInfo argument cannot be null and customerId must be set.");
        }
        UpdateCreditProfile updateCrdProfileRequest = new UpdateCreditProfile();
	
        // Use cosumerCreditProfileInfo to set client's domain objects for the request 
        CreditProfileData creditProfile = new CreditProfileData();
	creditProfile.setCustomerID(consumerCreditProfileInfo.getIdentification().getCustomerId());
	creditProfile.setMethodCd( "OO" );
	creditProfile.setCommentTxt( consumerCreditProfileInfo.getComment() );
        if ( consumerCreditProfileInfo.getCreditIdentification() != null ) {
	        com.telus.credit.wlnprfldmgt.domain.CreditIdentification creditId = new com.telus.credit.wlnprfldmgt.domain.CreditIdentification();
	        if ( consumerCreditProfileInfo.getCreditIdentification().getDriverLicense() != null &&
	        	 consumerCreditProfileInfo.getCreditIdentification().getDriverLicense().getDriverLicenseNum() != null	) {
	        	com.telus.credit.wlnprfldmgt.domain.DriverLicense drivingLicense = new com.telus.credit.wlnprfldmgt.domain.DriverLicense();
	        	drivingLicense.setDriverLicenseNum( consumerCreditProfileInfo.getCreditIdentification().getDriverLicense().getDriverLicenseNum() );
	        	drivingLicense.setProvinceCd(consumerCreditProfileInfo.getCreditIdentification().getDriverLicense().getProvinceCd() );
	        	drivingLicense.setExpiryDate(consumerCreditProfileInfo.getCreditIdentification().getDriverLicense().getExpiryDate());
	        	creditId.setDriverLicense(drivingLicense);
	        }
	        if ( consumerCreditProfileInfo.getCreditIdentification().getSin() != null 
	        	 && consumerCreditProfileInfo.getCreditIdentification().getSin().trim().length() > 0 ) {
	        	creditId.setSin(consumerCreditProfileInfo.getCreditIdentification().getSin());
	        }
	        creditProfile.setCreditIdentification( creditId );
	        convertProvinceCode( creditProfile );
        }
        if ( consumerCreditProfileInfo.getPersonalInfo() != null 
                && consumerCreditProfileInfo.getPersonalInfo().getBirthDate() != null ) {
            PersonalInfo p = new PersonalInfo();
            p.setBirthDate( consumerCreditProfileInfo.getPersonalInfo().getBirthDate() );
            creditProfile.setPersonalInfo( p );
        }
        // building UpdateCreditProfile request object
        updateCrdProfileRequest.setCreditProfile(creditProfile);
        // adding audit info
        updateCrdProfileRequest.setAuditInfo(auditInfo);
        
        return updateCrdProfileRequest;
    }
	
	
	/**
	 * <p>
	 * Builds the WLNCreditProfileDataManagementServise specific request object and returns it.
	 * @param consumerCreditProfileInfo the source data to be used to build web-service request object.
	 * @return target web-service request object.
	 */
	public UpdateCreditProfile buildServiceRequest(ConsumerCreditProfileInfo consumerCreditProfileInfo, AuditInfo auditInfo){
		if (consumerCreditProfileInfo == null || consumerCreditProfileInfo.getIdentification().getCustomerId() == null) { 
			throw new IllegalArgumentException("consumerCreditProfileInfo argument cannot be null and customerId must be set.");
		}
		UpdateCreditProfile updateCrdProfileRequest = new UpdateCreditProfile();
		// Use cosumerCreditProfileInfo to set client's domain objects for the request 
		CreditProfileData creditProfile = new CreditProfileData();
		creditProfile.setCreditAddress(consumerCreditProfileInfo.getCreditAddress());
		creditProfile.setCreditCardCd(consumerCreditProfileInfo.getCreditCardCode());
		creditProfile.setCreditIdentification(consumerCreditProfileInfo.getCreditIdentification());
		convertProvinceCode( creditProfile );
		// Method code must be passed for WLN validation to pass
		creditProfile.setMethodCd( "OO" );
		
		updateCrdProfileRequest.setCreditValueCd(consumerCreditProfileInfo.getCreditValueCd());
		updateCrdProfileRequest.setFraudIndicatorCd( consumerCreditProfileInfo.getFraudIndicatorCd() );
		
		creditProfile.setCustomerID(consumerCreditProfileInfo.getIdentification().getCustomerId());
		creditProfile.setPersonalInfo(consumerCreditProfileInfo.getPersonalInfo());
		creditProfile.setApplicationProvinceCd( consumerCreditProfileInfo.getApplicationProvinceCd() );
		creditProfile.setCustomerGuarantor( consumerCreditProfileInfo.getCustomerGuarantor() );
		creditProfile.setCommentTxt( consumerCreditProfileInfo.getComment() );
		// building UpdateCreditProfile request object
		
		updateCrdProfileRequest.setCreditProfile(creditProfile);
		// adding audit info
		updateCrdProfileRequest.setAuditInfo(auditInfo);
		
		return updateCrdProfileRequest;
	}

	private void convertProvinceCode(CreditProfileData creditProfile) {
		if ( creditProfile.getCreditIdentification() != null
			 &&	creditProfile.getCreditIdentification().getDriverLicense() != null
			 && creditProfile.getCreditIdentification().getDriverLicense().getProvinceCd() != null ) {
			String provinceCode = creditProfile.getCreditIdentification().getDriverLicense().getProvinceCd().trim().toUpperCase();
			if ( provinceCode.equals( "NF") ) {
				creditProfile.getCreditIdentification().getDriverLicense().setProvinceCd( "NL" );
			}
			else if ( provinceCode.equals( "PQ" ) ) {
				creditProfile.getCreditIdentification().getDriverLicense().setProvinceCd( "QC" );
			}
			
		}
		
	}
	
	public String ping() throws PolicyException, ServiceException {
		return this.m_wlnCreditProfileDataMgtService.ping();
	}
}
