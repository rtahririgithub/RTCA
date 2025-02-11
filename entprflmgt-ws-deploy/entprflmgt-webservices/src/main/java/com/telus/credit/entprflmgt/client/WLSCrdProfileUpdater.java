package com.telus.credit.entprflmgt.client;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.entprflmgt.domain.ConsumerCreditIdentification;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.util.EntrCrdUtil;
import com.telus.credit.wlnprfldmgt.domain.DriverLicense;
import com.telus.credit.wlnprfldmgt.domain.PersonalInfo;
import com.telus.credit.wlsprfldmgt.domain.UpdateWlsConsumerCreditProfile;
//import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.WLSCreditProfileDataManagementServicePortType;


/**
 * <p>
 * Helper class that deals with external web-service to update WIRELESS Credit Profile.
 * Provides methods to convert given {@link com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo} to the
 * format that external web-service understands and send request to that service.
 * 
 * @author Danil Glinenko
 *
 */
public class WLSCrdProfileUpdater extends CrdProfileUpdater {
	private static final Log log = LogFactory.getLog(WLSCrdProfileUpdater.class);
	
	private WLSCreditProfileDataManagementServicePortType m_wlsCreditProfileDataMgtService;
	
	public WLSCrdProfileUpdater(WLSCreditProfileDataManagementServicePortType clientServiceInterface) {
		this.m_wlsCreditProfileDataMgtService= clientServiceInterface;
	}
    
        @Override
        public void updateCreditProfile(
					ConsumerCreditProfileInfo consumerCreditProfileInfo, AuditInfo auditInfo ) throws CreditProfileUpdateException {
	    
	    updateCreditProfile( consumerCreditProfileInfo, auditInfo, false );
	}
	/**
	 *<p>
	 * Sends request to update consumer credit profile to WLSCreditProfileDataManagementService.
	 * @param consumerCreditProfileInfo the source of data to be used to form request 
	 * to external web-service to update credit profile.
	 * 
	 * @throws CreditProfileUpdateException if the operation cannot be performed for various reasons.
	 */
	public void updateCreditProfile(
			ConsumerCreditProfileInfo consumerCreditProfileInfo, AuditInfo auditInfo, boolean masterRequest)
			throws CreditProfileUpdateException {
		if (consumerCreditProfileInfo == null || consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification() == null) { 
			throw new IllegalArgumentException("consumerCreditProfileInfo argument cannot be null and billing account identification must be set.");
		}
		
		// Convert consumerCreditProfileInfo to the format understandable by the clientService and call it.
		UpdateWlsConsumerCreditProfile wlsCrdProfileUpdateRequest = this.buildServiceRequest(consumerCreditProfileInfo, auditInfo);
		if ( masterRequest ) {
			com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation personalCreditInfo = wlsCrdProfileUpdateRequest.getPersonalCreditInformation();
		    if ( personalCreditInfo != null && consumerCreditProfileInfo.getCreditIdentification() != null ) {
		    	com.telus.credit.customer.domain.common.CreditCard v4_creditCard  = consumerCreditProfileInfo.getCreditIdentification().getCreditCardToken();
		    	if ( v4_creditCard != null ) {
		    		com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCard creditCard = new com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCard();
		    		creditCard.setCardVerificationData(v4_creditCard.getCardVerificationData());
		    		creditCard.setExpiryMonth(v4_creditCard.getExpiryMonth());
		    		creditCard.setExpiryYear(v4_creditCard.getExpiryYear());
		    		creditCard.setFirst6(v4_creditCard.getFirst6());
		    		creditCard.setHolderName(v4_creditCard.getHolderName());
		    		creditCard.setLast4(v4_creditCard.getLast4());
		    		creditCard.setToken(v4_creditCard.getToken());
		    		personalCreditInfo.setCreditCard( creditCard );
		    	}
		    }
		}
		final String logMessageDetails = "BAN: " + wlsCrdProfileUpdateRequest.getAccountNumber();
		
		// send request to the wlsService.
		try {
			this.m_wlsCreditProfileDataMgtService.updateWlsConsumerCreditProfile(
					wlsCrdProfileUpdateRequest.getAuditInfo(),
					wlsCrdProfileUpdateRequest.getAccountNumber(), 
					wlsCrdProfileUpdateRequest.getPersonalCreditInformation(),
					wlsCrdProfileUpdateRequest.getBusinessRegistration(), 
					null, null, null, null, null
					);
		} catch (com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.ServiceException e) {
			CreditProfileUpdateException cpue = new CreditProfileUpdateException("Service Exception updating WIRELESS Credit profile. " +
					logMessageDetails,
					e);
			throw cpue;
		} catch (RuntimeException e) {
			log.error("Unknown exception updating WIRELESS Credit profile. " 
					+ logMessageDetails,
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
    public UpdateWlsConsumerCreditProfile buildAsyncServiceRequest(ConsumerCreditProfileInfo consumerCreditProfileInfo, 
            AuditInfo auditInfo)
    {
        return buildServiceRequest( consumerCreditProfileInfo, auditInfo);
    }
	
	/**
	 * <p>
	 * Builds the WLSCreditProfileDataManagementServise specific request object and returns it.
	 * @param consumerCreditProfileInfo the source data to be used to build web-service request object.
	 * @return target web-service request object.
	 */
	@Override
	public UpdateWlsConsumerCreditProfile buildServiceRequest(
			ConsumerCreditProfileInfo consumerCreditProfileInfo, AuditInfo auditInfo) {
		if (consumerCreditProfileInfo == null || consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification() == null) { 
			throw new IllegalArgumentException("consumerCreditProfileInfo argument cannot be null and billing account identification must be set.");
		}
		final String accountNumber =consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification().getBillingAccountNumber(); 
		if (accountNumber == null) throw new IllegalArgumentException("To update wireless account account number must be specified.");
		
		com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation personalCreditInfo = new com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PersonalCreditInformation();
		 PersonalInfo pi = consumerCreditProfileInfo.getPersonalInfo();
		if (pi != null && pi.getBirthDate()!=null){
			//XMLGregorianCalendar xCal = EntrCrdUtil.asXMLGregorianCalendar(pi.getBirthDate());
			//xCal.setTimezone( DatatypeConstants.FIELD_UNDEFINED );
			personalCreditInfo.setBirthDate(pi.getBirthDate());
		}
		final ConsumerCreditIdentification ci = consumerCreditProfileInfo.getCreditIdentification();
		if (ci != null){
			// setting SIN value
			personalCreditInfo.setSin(ci.getSin());
			// Credit Card Token is not populated here as we do not want to replicate credit card for all authenticated bans
			personalCreditInfo.setCreditCard( null );
			// Setting DL values
			 final DriverLicense dl = ci.getDriverLicense();
			if (dl != null){
				personalCreditInfo.setDriversLicense(dl.getDriverLicenseNum());
				//
		        // Defect: 81640, Remove Timezone from the date information if passed to service
		        //
				if ( dl.getExpiryDate() != null ) {
					//XMLGregorianCalendar  xCal = EntrCrdUtil.asXMLGregorianCalendar(dl.getExpiryDate());
					//xCal.setTimezone( DatatypeConstants.FIELD_UNDEFINED );
					personalCreditInfo.setDriversLicenseExpiry(dl.getExpiryDate());
				}
				 com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode dlProvinceCode = mapStringToProvinceCode(dl.getProvinceCd());
				personalCreditInfo.setDriversLicenseProvince(dlProvinceCode);
			}
		}
		
		UpdateWlsConsumerCreditProfile wlsCrdProfileUpdateRequest = new UpdateWlsConsumerCreditProfile();
		wlsCrdProfileUpdateRequest.setAccountNumber(accountNumber);
		com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v3.AuditInfo v3AuditInfo = null;
		if ( auditInfo != null ) {
		    v3AuditInfo =   new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v3.AuditInfo();
		    v3AuditInfo.setUserId( auditInfo.getUserId() );
		    //v3AuditInfo.setTimestamp( EntrCrdUtil.asXMLGregorianCalendar( auditInfo.getTimestamp() ) );
		    v3AuditInfo.setTimestamp( auditInfo.getTimestamp() );		    
		    v3AuditInfo.setOriginatorApplicationId( auditInfo.getOriginatorApplicationId() );
		    v3AuditInfo.setSalesRepresentativeId( auditInfo.getSalesRepresentativeId() );
		    v3AuditInfo.setOutletId( auditInfo.getOutletId() );
		    v3AuditInfo.setChannelOrganizationId( auditInfo.getChannelOrganizationId() );
		}
		wlsCrdProfileUpdateRequest.setAuditInfo(v3AuditInfo);
		wlsCrdProfileUpdateRequest.setPersonalCreditInformation(personalCreditInfo);
		
		com.telus.credit.entprflmgt.domain.BusinessRegistration entBusReg = consumerCreditProfileInfo.getIdentification().getBusinessRegistration();
		com.telus.credit.wlsprfldmgt.domain.BusinessRegistration wlsBusReg = new com.telus.credit.wlsprfldmgt.domain.BusinessRegistration();
		wlsCrdProfileUpdateRequest.setBusinessRegistration(wlsBusReg);		
		if ( entBusReg != null ) {
			wlsBusReg.setBusinessRegistrationNumber(entBusReg.getBusinessRegistrationNumber());
			wlsBusReg.setBusinessRegistrationTypeCd(entBusReg.getBusinessRegistrationTypeCd());
		}
		
		return wlsCrdProfileUpdateRequest;
	}
	
	private com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode mapStringToProvinceCode(String province) {
		try {
			if ( province != null && province.trim().length() > 0 ) {
				String convertedProvinceCode = province.trim().toUpperCase();
				if ( convertedProvinceCode.equals("NL") ) {
					convertedProvinceCode = com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode.NF.value();
				}
				else if ( convertedProvinceCode.equals( "QC" ) ) {
					convertedProvinceCode = com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode.PQ.value();
				}
			 return com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.ProvinceCode.fromValue(convertedProvinceCode);
			}
		} catch (Exception e) {
			log.info("Province code: " + province + " couldn't be converted to Canadian prov code.");
			// ignore and null will be returned.
		}
		return null;
	}

	public String ping() throws com.telus.wsdl.cmo.ordermgmt.wlscreditprofiledatamanagementsvc_2.ServiceException {
		return this.m_wlsCreditProfileDataMgtService.ping();
	}
}
