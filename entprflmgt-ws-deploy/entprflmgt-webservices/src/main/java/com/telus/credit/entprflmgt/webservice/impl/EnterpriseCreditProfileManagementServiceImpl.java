package com.telus.credit.entprflmgt.webservice.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.credit.entprflmgt.asyncprocessing.ARFN_HEADER_VALUES;
import com.telus.credit.entprflmgt.asyncprocessing.JMSCreditProfileUpdateSender;
import com.telus.credit.entprflmgt.client.CrdProfileUpdateFlags;
import com.telus.credit.entprflmgt.client.CreditProfileUpdateException;
import com.telus.credit.entprflmgt.client.CustomerManagementServiceIntermediator;
import com.telus.credit.entprflmgt.client.GetFullCustomerInfoException;
import com.telus.credit.entprflmgt.client.WLNCrdProfileUpdater;
import com.telus.credit.entprflmgt.client.WLNCreditProfileMgtProxyServiceIntermediator;
import com.telus.credit.entprflmgt.client.WLNCreditProfileMgtServiceIntermediator;
import com.telus.credit.entprflmgt.client.WLSCrdProfileUpdater;
import com.telus.credit.entprflmgt.domain.BillingAccountIdentification;
import com.telus.credit.entprflmgt.domain.ConsumerCreditIdentification;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfileResponse;
import com.telus.credit.entprflmgt.util.CreditAssessmentAnalyzer;
import com.telus.credit.entprflmgt.util.VersionUtils;
import com.telus.credit.wlnprfldmgt.domain.DriverLicense;
import com.telus.credit.wlnprfldmgt.domain.HealthCard;
import com.telus.credit.wlnprfldmgt.domain.Passport;
import com.telus.credit.wlnprfldmgt.domain.ProvincialIdCard;
import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.EnterpriseCreditProfileManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;

public class EnterpriseCreditProfileManagementServiceImpl  implements EnterpriseCreditProfileManagementServicePortType {

	
	private static final String MISSING_CUSTOMER_INFO_LOG = "MISSING_CUSTOMER_INFO_LOG";
	private static final Log log = LogFactory.getLog(EnterpriseCreditProfileManagementServiceImpl.class);
	private static final Log missingCustomerInfoLog = LogFactory.getLog(MISSING_CUSTOMER_INFO_LOG);
		
	//Flag indicating whether to synchronise credit profile updates WLN-WLS-WLN.
	private boolean m_crdProfileSyncEnabled;
	
	@Autowired
	protected JMSCreditProfileUpdateSender m_jmsCreditPrflUpdateSender;
	
	@Autowired
	protected WLNCrdProfileUpdater m_wlnCrdProfileUpdater;
	
	@Autowired
	protected WLSCrdProfileUpdater m_wlsCrdProfileUpdater;
	
	@Autowired
	protected CustomerManagementServiceIntermediator m_customerMgtIntermediator;
	
	@Autowired
	protected CreditAssessmentAnalyzer m_creditAssessmentAnalyzer;
	
	@Autowired
	protected WLNCreditProfileMgtServiceIntermediator m_wlnCreditProfileMgtSvcIntermediator;
	
	@Autowired
	protected WLNCreditProfileMgtProxyServiceIntermediator m_wlnCreditProfileMgtProxyServiceIntermediator;
	
	/**
	 * Constructor with external dependencies which will be wired by Spring.
	 * @param jmsCrdPrflUpdtSender provides methods to put messages on a JMS queue to update credit profile asynchronously.
	 * @param wlnProfileUpdater provides methods to update WLN Credit Profile directly.
	 * @param wlsProfileUpdater provides methods to update WLS Credit Profile directly.
	 */
	public EnterpriseCreditProfileManagementServiceImpl() {
		// by default sync of credit profiles is disabled.
		m_crdProfileSyncEnabled = false;
	}
	
	@Override
	public PingResponse ping( Ping parameters) throws ServiceException {
		// TODO Auto-generated method stub
		PingResponse aPingResponse = new PingResponse();		
		String txt = "Success EnterpriseCreditProfileManagementSvc_v2_0.";
		try{
			VersionUtils aVersionUtils = new VersionUtils();
			txt = txt + aVersionUtils.getBuildinfo();
			txt = txt + aVersionUtils.getConfigInfo();
			String wlsUpdaterPing = "[WLS_CreditProfileDataMgtService.ping() = ";
			try {
				wlsUpdaterPing = wlsUpdaterPing + m_wlsCrdProfileUpdater.ping() + "]";
			} catch( Throwable e ) {
				wlsUpdaterPing = wlsUpdaterPing + VersionUtils.getStackTrace(e) + "]";
			}
			txt = txt + wlsUpdaterPing;
			
			String wlnUpdaterPing = "[WLN_CreditProfileDataMgtService.ping() = ";
			try {
				wlnUpdaterPing = wlnUpdaterPing + m_wlnCrdProfileUpdater.ping() + "]";
			} catch( Throwable e ) {
				wlnUpdaterPing = wlnUpdaterPing + VersionUtils.getStackTrace(e) + "]";
			}
			txt = txt + wlnUpdaterPing;
			
			String customerMgtPing = "[ConsumerCustomerMgtService.ping() = ";
			try {
				customerMgtPing = m_customerMgtIntermediator.ping() + "]";
			} catch( Throwable e ) {
				customerMgtPing = customerMgtPing + VersionUtils.getStackTrace(e) + "]";
			}
			txt = txt + customerMgtPing;
			
			aPingResponse.setVersion(txt );
		}catch(Throwable e){
			log.error(this.getClass(), e); 	
			String stacktraceMsg =" [ERROR: stacktraceMsg [" +  e.getMessage() + "]]";
			aPingResponse.setVersion( txt + stacktraceMsg );
		}
		return aPingResponse;
	}
	
	@Override
	public UpdateCreditProfileResponse updateCreditProfile( UpdateCreditProfile parameters) throws ServiceException {
		//response 
		UpdateCreditProfileResponse aUpdateCreditProfileResponse= new UpdateCreditProfileResponse();
		
		ConsumerCreditProfileInfo consumerCreditProfileInfo = parameters.getConsumerCreditProfileInfo();
		AuditInfo auditInfo = parameters.getAuditInfo();
		final Identification id = consumerCreditProfileInfo.getIdentification();
		
		// some basic validation first.
		if (id == null || !(id.getCustomerId()!=null ^ id.getBillingAccountIdentification()!=null)){
			throw new IllegalArgumentException("identification cannot be null and either customerId or billing acc# has to be specified.");
		}
		
		// check audit info and if it's null create a new object.
		if (auditInfo== null) {
			auditInfo = new AuditInfo();
		}
		// Identify where the request is coming from
		boolean isRequestFromWireline  = ( (id.getCustomerId() != null && id.getCustomerId() != 0) || 
						   ( id.getBillingAccountIdentification() != null && 
						     id.getBillingAccountIdentification().getBillingSystemId() == CustomerManagementServiceIntermediator.ENABLER ) );
		
		validate( consumerCreditProfileInfo, isRequestFromWireline );
		// set audit info
		setAuditInfo(auditInfo);
		
		validateAuditInfo( auditInfo );
		
		// encrypt sensitive data as the first step.
		encryptSensitiveData(consumerCreditProfileInfo);
		
		//
		// Defect: 81640, Remove Timezone from the date information if passed to service
		//
		// TODO: Need to retest with time-zone related inputs.
		//
		/*if ( consumerCreditProfileInfo != null && 
			 consumerCreditProfileInfo.getPersonalInfo() != null && 
			 consumerCreditProfileInfo.getPersonalInfo().getBirthDate() != null ) {
			consumerCreditProfileInfo.getPersonalInfo().getBirthDate().setTimezone( DatatypeConstants.FIELD_UNDEFINED );
		}*/
		
		
		CrdProfileUpdateFlags profileUpdateFlags;
		// Additional information to pass to JMS queue (JMS headers).
		final HashMap<String, String> userInfo = new HashMap<String, String>();
		if (auditInfo != null && auditInfo.getCorrelationId() != null){
			userInfo.put(ARFN_HEADER_VALUES.ARFM_CORRELATION_ID.name(), auditInfo.getCorrelationId());
		}
	
		try {
			// === REQUEST FROM WIRELINE.
			if (isRequestFromWireline){
				log.debug("Request came from WIRELINE.");
				// get the customer structure from consumerCustomer service.
				profileUpdateFlags = m_customerMgtIntermediator.getCrdProfileUpdateFlags(id);

				// put user info in.
				userInfo.put(ARFN_HEADER_VALUES.ARFM_CUSTOMER_ID.name(), String.valueOf(profileUpdateFlags.getCustomerId()));

				// update consumerCreditProfileInfo with the customerId from ConsumerCustomerManagement service.
				consumerCreditProfileInfo.getIdentification().setCustomerId(profileUpdateFlags.getCustomerId());
				consumerCreditProfileInfo.getIdentification().setBillingAccountIdentification(null);

				// put user info in
				//userInfo.put(ARFN_HEADER_VALUES.ARFM_CUSTOMER_ID.name(), String.valueOf(id.getCustomerId()));
				
				if (log.isDebugEnabled()){
					log.debug("Update credit profile from WIRELINE. CustomerId: " + profileUpdateFlags.getCustomerId() + ". ProfileUpdateFlags: " +profileUpdateFlags);
				}
				// update WIRELINE credit profile directly.
				getWLNCrdProfileUpdater().updateCreditProfile(consumerCreditProfileInfo, auditInfo);
				
				// now we can update WIRELESS asynchronously if this functionality is enabled in config.
				if (profileUpdateFlags.hasWirelessProfile() && isCrdProfileSyncEnabled()){
					for (String ban: profileUpdateFlags.getWirelessBANs()){
						//change consumer credit profile info to contain BAN so we can update WIRELESS via JMS.
						BillingAccountIdentification bai = new BillingAccountIdentification();
						bai.setBillingAccountNumber(ban);
						id.setBillingAccountIdentification(bai);
						
						// build a JMS message for WIRELESS async update.
						final String wirelessJmsMessage = getWLSCrdProfileUpdater().convertToServicePayload(consumerCreditProfileInfo, auditInfo);
						// put the message to update *WIRELESS* side on the queue
						if ( log.isDebugEnabled() ) {
							log.debug( "Send message to wireless ban: " + ban +  ", JMS: " + wirelessJmsMessage);
						}
						this.m_jmsCreditPrflUpdateSender.sendCreditProfileInfoToWireless(wirelessJmsMessage, userInfo);
					}
				}
				try {
				    if ( (!getCreditAssessmentAnalyzer().isCreditValueBeingChangedInRequest(consumerCreditProfileInfo))
				            && getCreditAssessmentAnalyzer().customerDataMeetConditions(profileUpdateFlags) ) {
				        ConsumerCreditProfile consumerCreditProfile = m_wlnCreditProfileMgtSvcIntermediator.getCreditProfileByCustomerId( profileUpdateFlags.getCustomerId(), auditInfo.getUserId() );
				        if ( getCreditAssessmentAnalyzer().creditDataMeetConditions( consumerCreditProfile) ) {
				            m_wlnCreditProfileMgtProxyServiceIntermediator.assessCreditWorthiness( profileUpdateFlags.getCustomerId(), 
				            auditInfo.getOriginatorApplicationId(), auditInfo.getUserId() );
				        }
				    }
				}
				catch ( Throwable e ) {
				    log.error("Exception when doing assessCreditWorthiness(Ignored):" + e, e );
				}
			} //END OF: REQUEST FROM WIRELINE.
			
			// === REQUEST FROM WIRELESS.
			else {
				log.debug("Request came from WIRELESS.");
				
				String originalBAN = id.getBillingAccountIdentification().getBillingAccountNumber();
				// get the customer structure from consumerCustomer service.
				try {
				    profileUpdateFlags = m_customerMgtIntermediator.getCrdProfileUpdateFlags(id);
				    // in case if WIRELESS customer info wasn't found
					if (!profileUpdateFlags.hasWirelessProfile()) {
						throw new GetFullCustomerInfoException("Unable to find all wireless BANs in customerODS.");
					}
					// add master BAN so it can be updated as well.
					profileUpdateFlags.getWirelessBANs().add(id.getBillingAccountIdentification().getBillingAccountNumber());
				} catch (Exception e){
					// we want to go on and update WIRELESS profile that came in the request.
					log.warn("Unable to get full customer info from CustomerODS. Continue with the update of master WIRELESS credit profile.", e);
					HashSet<String> wirelessBANs = new HashSet<String>(){{
						add(id.getBillingAccountIdentification().getBillingAccountNumber());
					}};
					boolean hasWirelineProfile =false;
					boolean hasWirelessProfile = true;
					int noOfWirelessBANs = 1;
					int noOfWirelineBANs = 0;
					Date wirelineBANCreationDate = null;
					String wirelineBANStatus = null;
					Long customerId = null;
					
					// set profile update flags WIRELINE:false WIRELESS:true
					profileUpdateFlags = new CrdProfileUpdateFlags(
							hasWirelineProfile, 
							hasWirelessProfile, 
							wirelessBANs, 
							customerId, 
							noOfWirelessBANs, 
							noOfWirelineBANs, 
							wirelineBANCreationDate, 
							wirelineBANStatus);
					
			/*				new CrdProfileUpdateFlags(false, true, 
							new HashSet<String>(){{
								add(id.getBillingAccountIdentification().getBillingAccountNumber());
							}}, null,1,0,null,null);*/
					missingCustomerInfoLog.info("Unable to find customer info for BAN: " 
							+ id.getBillingAccountIdentification().getBillingAccountNumber());
				}
			
				if (log.isDebugEnabled()){
					log.debug("Update credit profile from WIRELESS. BAN: " + id.getBillingAccountIdentification().getBillingAccountNumber() + 
							" Master source id: " + id.getBillingAccountIdentification().getBillingSystemId() + 
							". ProfileUpdateFlags: " +profileUpdateFlags);
				}
				
				// update WIRELESS credit profile directly for every BAN
				for (String ban: profileUpdateFlags.getWirelessBANs()){
					// change consumer credit profile info to contain the BAN.
					id.getBillingAccountIdentification().setBillingAccountNumber(ban);
					
					// call WIRELESS update.
					getWLSCrdProfileUpdater().updateCreditProfile( consumerCreditProfileInfo, auditInfo, 
										       (ban.equals( originalBAN ) ) );
				}
				
				// now update WIRELINE via JMS if there is WLN profile and update is enabled in config.
				if (profileUpdateFlags.hasWirelineProfile() && isCrdProfileSyncEnabled()){
					// put user info in.
					userInfo.put(ARFN_HEADER_VALUES.ARFM_CUSTOMER_ID.name(), String.valueOf(profileUpdateFlags.getCustomerId()));
					
					// update consumerCreditProfileInfo with the customerId from ConsumerCustomerManagement service.
					consumerCreditProfileInfo.getIdentification().setCustomerId(profileUpdateFlags.getCustomerId());
					consumerCreditProfileInfo.getIdentification().setBillingAccountIdentification(null);
					
					final String wirelineJmsMessage = getWLNCrdProfileUpdater().convertToServicePayload(consumerCreditProfileInfo, auditInfo);
					// put the message to update *WIRELINE* side on the queue.
					if ( log.isDebugEnabled() ) {
						log.debug( "Send message to wireline customer id: " + profileUpdateFlags.getCustomerId() +  ", JMS: " + wirelineJmsMessage);
					}
					this.m_jmsCreditPrflUpdateSender.sendCreditProfileInfoToWireline( wirelineJmsMessage, userInfo);
				}
			} // END OF: REQUEST FROM WIRELESS
		}
		catch (GetFullCustomerInfoException cie){
			log.error("Unable to get full customer info from CustomerODS, Error Code: " + ExceptionHandler.getErrorCode(cie) +
				                                    "Error Message: " + ExceptionHandler.getErrorMessage(cie), cie);
			throw new ServiceException("Unable to get customer information from CustomerODS.", 
					ExceptionHandler.createFaultExceptionDetailsType(cie), cie);
		}
		catch (CreditProfileUpdateException cpe) {
			log.error("Unable to update credit profile, Error Code: " + ExceptionHandler.getErrorCode(cpe) +
				                                    "Error Message: " + ExceptionHandler.getErrorMessage(cpe), cpe);
			throw new ServiceException("Exception updating credit profile from service: " + cpe.getOrigin().name(),
					ExceptionHandler.createFaultExceptionDetailsType(cpe), cpe);
		}
		catch (RuntimeException e) {
			// for any runtime exception thrown like ones from Spring jmsTemplate
			log.error("Unknown Runtime exception for customer id: " + consumerCreditProfileInfo.getIdentification().getCustomerId() 
					+ " or BAN: " + ( consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification() != null ? 
			       "" + consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification().getBillingAccountNumber() : "") + e, e );
			
			throw new ServiceException("Runtime exception: " + e.getMessage() , ExceptionHandler.createFaultExceptionDetailsType(e), e);
		}
		
		return aUpdateCreditProfileResponse;
	}
	
	
    private void validateAuditInfo(AuditInfo auditInfo) throws ServiceException
    {
        if ( auditInfo != null 
                && auditInfo.getOriginatorApplicationId() != null 
                && auditInfo.getOriginatorApplicationId().length() > 0 ) {
            try {
                Long.parseLong( auditInfo.getOriginatorApplicationId() );
            }
            catch ( NumberFormatException e ) {
                log.error("Originator Application Id in AuditInfo must be a number, id: " +  auditInfo.getOriginatorApplicationId() );
                throw new ServiceException(
                        "Originator Application Id in AuditInfo must be a number.",
                        ExceptionHandler.createFaultExceptionDetailsType( "Originator Application Id in AuditInfo must be a number." ) );
            }
        }
    }

    /**
	 * Setting auditInfo's timestamp to the time of the request arrival to the service.
	 */
	private void setAuditInfo(AuditInfo info){
		info.setTimestamp(new Date());
	}
	
	private WLNCrdProfileUpdater getWLNCrdProfileUpdater() {
		return this.m_wlnCrdProfileUpdater;
	}
	
	private WLSCrdProfileUpdater getWLSCrdProfileUpdater() {
		return this.m_wlsCrdProfileUpdater;
	}
	
	private void encryptSensitiveData(ConsumerCreditProfileInfo consumerCrdProfileInfo){
		ConsumerCreditIdentification credId= consumerCrdProfileInfo.getCreditIdentification();
		if (credId == null) return;
		encryptDriverLicense(credId.getDriverLicense());
		encryptHealthCard(credId.getHealthCard());
		encryptPassport(credId.getPassport());
		encryptProvId(credId.getProvincialIdCard());
		// encrypt SIN
		if (credId.getSin() != null){
			credId.setSin(EncryptionUtil.encrypt(credId.getSin()));
		}
	}

	protected void validate( ConsumerCreditProfileInfo consumerCreditProfileInfo, boolean isRequestFromWireline ) 
	throws ServiceException
	{
	    if(  consumerCreditProfileInfo.getCreditIdentification() != null 
	            && consumerCreditProfileInfo.getCreditIdentification().getSin() != null
	            && consumerCreditProfileInfo.getCreditIdentification().getSin().trim().length() > 0 ) {
	        if ( !isValidSinNumber( consumerCreditProfileInfo.getCreditIdentification().getSin().trim() ) ) {
	            log.error( "Invalid SIN for customer id: " + consumerCreditProfileInfo.getIdentification().getCustomerId() + " or BAN: " 
	                    + ( consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification() != null ? 
	                            "" + consumerCreditProfileInfo.getIdentification().getBillingAccountIdentification().getBillingAccountNumber() : "" ) );
	            throw new ServiceException( "Invalid SIN",
	                    ExceptionHandler.createFaultExceptionDetailsType("Invalid SIN") );
	        }
	    }

	    if ( isRequestFromWireline ) {
	        final ConsumerCreditIdentification ci = consumerCreditProfileInfo
	        .getCreditIdentification();
	        if ( ci != null && ci.getCreditCardToken() != null ) {
	            log.error( "creditCardToken element is not valid for a wireline request for customer id: "
	                    + consumerCreditProfileInfo.getIdentification()
	                    .getCustomerId()
	                    + " or BAN: "
	                    + (consumerCreditProfileInfo.getIdentification()
	                            .getBillingAccountIdentification() != null ? ""
	                                    + consumerCreditProfileInfo.getIdentification()
	                                    .getBillingAccountIdentification()
	                                    .getBillingAccountNumber() : "") );
	            throw new ServiceException(
	                    "creditCardToken element is not valid for a wireline request.",
	                    ExceptionHandler
	                    .createFaultExceptionDetailsType( "creditCardToken element is not valid for a wireline request." ) );
	        }
	    }
	    else {
	        if ( consumerCreditProfileInfo.getCreditAddress() != null
	                || consumerCreditProfileInfo.getCreditValueCd() != null
	                || consumerCreditProfileInfo.getCreditCardCode() != null
	                || consumerCreditProfileInfo.getFraudIndicatorCd() != null
	                || consumerCreditProfileInfo.getApplicationProvinceCd() != null
	                || consumerCreditProfileInfo.getCustomerGuarantor() != null ) {
	            log.error( "Elements: creditAddress, creditValue, creditCardCode, fraud indicator code, app province, gurantor are not valid for a wireless request BAN : "
	                    + (consumerCreditProfileInfo.getIdentification()
	                            .getBillingAccountIdentification() != null ? ""
	                                    + consumerCreditProfileInfo.getIdentification()
	                                    .getBillingAccountIdentification()
	                                    .getBillingAccountNumber() : "") );
	            throw new ServiceException(
	                    "Elements: creditAddress, creditValue or creditCardCode are not valid for a wireless request.",
	                    ExceptionHandler
	                    .createFaultExceptionDetailsType( "Elements: creditAddress, creditValue, creditCardCode, fraud indicator code, app province, gurantor are not valid for a wireless request." ) );
	        }
	    }
	}

    /**
     * <p><b>Description</b> Validates SIN number. </p>
     * <p><b>Algorythm for SIN validation: </b></p>
     * <ol>
     * 		<li> Write the SIN on a sheet of paper, e.g. 440-968-592</li>
     *		<li> Insert a check mark over the 2nd, 4th, 6th and 8th digits</li> 
     *      <li> 
     * 			Write the SIN again, but this time doubling the digits that were check-marked, 
     * 			i.e. 480-18616-5182. 
     * 		</li> 
     *		<li> Where the doubling of a single digit results in a two-digit number, then: 
     *			<ol>
     *				<li> Add these two digits to form a single digit</li> 
     *				<li> Add all of these numbers, i.e. 4+8+0+9+6+7+5+9+2 = 50.</li>
     *			</ol>
     *		</li>
     *		<li>
     *			If the SIN is valid the resulting total must be of a multiple of ten. 
     *			Therefore the above SIN is valid in that the total is 50.
     *		</li>
     * </ol>
     * @param sinNumberStr SIN number.
     * @return true if SIN number is valid; false otherwise
     */
    public boolean isValidSinNumber(String sinNumberStr)
    {
        if ( sinNumberStr == null || !isSinInCorrectFormat( sinNumberStr ) ) {
            return false;
        }
        int[] digitsInSinNumber = new int[9];

        fill( digitsInSinNumber, sinNumberStr );

        int sumOfDigits = 0;

        for ( int i = 0; i < 9; i++ ) {
            if ( i == 1 || i == 3 || i == 5 || i == 7 ) {
                sumOfDigits += convertAndAddNumber( digitsInSinNumber[i] );
            }
            else {
                sumOfDigits += digitsInSinNumber[i];
            }
        }
        return sumOfDigits % 10 == 0 ? true : false;
    }

    protected void fill(int[] digits, String str)
    {
        for ( int i = 0; i < digits.length; i++ ) {
            digits[i] = Character.getNumericValue( str.charAt( i ) );
        }
    }
	
    protected boolean isSinInCorrectFormat(String num)
    {
        boolean result = true;
        if ( num.length() < 9 ) {
            result = false;
        }

        try {
            Integer.parseInt( num );
        }
        catch ( NumberFormatException e1 ) {
            result = false;
        }

        return result;
    }

    protected int convertAndAddNumber(int num)
    {
        int result = 0;
        int product = num * 2;
        int[] digits = new int[2];
        if ( product > 9 ) {
            fill( digits, product );
            result = addNumbersInArrayOfSize2( digits );
        }
        else {
            result = product;
        }
        return result;
    }


    private void fill(int[] digits, int num)
    {
        for ( int i = 0; i < digits.length; i++ ) {
            digits[i] = getDigit( num, i, digits.length );
        }
    }


    protected int getDigit(int num, int position, int numOfDigits)
    {
        String numString = String.valueOf( num );
        if ( numString.length() > numOfDigits ) {
            throw new IllegalArgumentException(
                    "Exception in CreditIDCard.getDigit(): num - " + num
                            + "; position - " + position );
        }
        return Character.getNumericValue( numString.charAt( position ) );

    }

    protected int addNumbersInArrayOfSize2(int[] nums)
    {
        return nums[0] + nums[1];
    }

	private void encryptDriverLicense(DriverLicense dl) {
		if (dl == null || dl.getDriverLicenseNum() == null) return;
		dl.setDriverLicenseNum(EncryptionUtil.encrypt(dl.getDriverLicenseNum()));
	}
	private void encryptHealthCard(HealthCard hc) {
		if (hc == null || hc.getHealthCardNum() == null) return;
		hc.setHealthCardNum(EncryptionUtil.encrypt(hc.getHealthCardNum()));
	}
	private void encryptPassport(Passport passport) {
		if (passport == null || passport.getPassportNum() == null) return;
		passport.setPassportNum(EncryptionUtil.encrypt(passport.getPassportNum()));
	}
	private void encryptProvId(ProvincialIdCard pic) {
		if (pic == null || pic.getProvincialIdNum() == null) return;
		pic.setProvincialIdNum(EncryptionUtil.encrypt(pic.getProvincialIdNum()));
	}
	
	public void setCrdProfileSyncEnabled(boolean syncFlag) {
		m_crdProfileSyncEnabled = syncFlag;
	}
	
	private boolean isCrdProfileSyncEnabled() {
		return m_crdProfileSyncEnabled;
	}
	
	public CreditAssessmentAnalyzer getCreditAssessmentAnalyzer()
    { 
        return m_creditAssessmentAnalyzer;
    }
	
	public void setCreditAssessmentAnalyzer( CreditAssessmentAnalyzer creditAssessmentAnalyzer ) {
	    m_creditAssessmentAnalyzer = creditAssessmentAnalyzer;
	}

 


}
