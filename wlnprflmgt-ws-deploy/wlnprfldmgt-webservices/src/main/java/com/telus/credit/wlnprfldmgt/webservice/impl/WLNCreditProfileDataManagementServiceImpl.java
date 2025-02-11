package com.telus.credit.wlnprfldmgt.webservice.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.validation.ValidationResult;

import com.telus.credit.dao.CreditAddressDao;
import com.telus.credit.dao.CreditAttributeDao;
import com.telus.credit.dao.CreditIDCardDao;
import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.domain.CreditAttribute;
import com.telus.credit.domain.CreditAttributeBr;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditProfileBr;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.service.CreditProfileMgtSvcException;
import com.telus.credit.service.CreditProfileMgtSvcExceptionCodes;
import com.telus.credit.service.CreditProfileValidationException;
import com.telus.credit.service.CreditValueValidationException;
import com.telus.credit.service.InvalidGuarantorIdException;
import com.telus.credit.service.dto.CreditProfileDto;
import com.telus.credit.service.dto.search.CreditMgtCustomerID;
import com.telus.credit.service.dto.search.CreditMgtCustomerIDs;
import com.telus.credit.wlnprfldmgt.client.WLNCreditProfileMgmtProxyServiceIntermediator;
import com.telus.credit.wlnprfldmgt.domain.CreditProfileData;
import com.telus.credit.wlnprfldmgt.domain.RemoveWLNCreditIdentificationInfo;
import com.telus.credit.wlnprfldmgt.webservice.util.ExceptionFactory;

import com.telus.credit.wlnprfldmgt.webservice.util.UpdateCreditWorthinessImpl;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementExceptionCodes;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementPolicyException;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementServiceException;
import com.telus.credit.wlnprfldmgt.webservice.util.WlnPrflDMgtUtil;

import com.telus.tmi.xmlschema.xsd.customer.customer.enterprisecreditassessmenttypes_v2.CreditAssessmentTransaction;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.WLNCreditProfileDataManagementServicePortType;
import com.telus.credit.util.CreateJSONMsg;
import com.telus.credit.util.CreditPubSubConnector;
/**
 * <p>
 * EnterpriseCreditProfileManagementService implementation.
 *
 * @author Alan Huang
 *
 */
public class WLNCreditProfileDataManagementServiceImpl implements
            WLNCreditProfileDataManagementServicePortType {

	private CreditProfileDao m_creditProfileDao;

	private CreditIDCardDao m_creditIDCardDao;

	private CreditAddressDao m_creditAddressDao;
	
	private CreditAttributeDao m_creditAttributeDao;

	private CreditProfileBr m_creditProfileBr;

	private WLNCreditProfileMgmtProxyServiceIntermediator m_creditProfileMgmtProxySvcIntermediator;
	
	private UpdateCreditWorthinessImpl m_updateCreditWorthinessImpl;
	
	
	// RTCA1.5 will not call Customer Management Service
	//private CustomerManagementServiceIntermediator m_customerManagementServiceMediator;
	
	private CreditPubSubConnector creditPubSubConnector;

	public CreditPubSubConnector getCreditPubSubConnector() {
		return creditPubSubConnector;
	}

	public void setCreditPubSubConnector(CreditPubSubConnector creditPubSubConnector) {
		this.creditPubSubConnector = creditPubSubConnector;
	}
	
	public final static String ASMT_SUB_TYPE_AUTO_ASMT="AUTO_ASSESSMENT";
	
	public final static String AUDIT_CREDIT_ASSESSMENT_TYPE="AUDIT";
	
	public final static String OVERRIDE_CREDIT_ASSESSMENT_TYPE="OVRD_ASSESSMENT";

	private static final Log log = LogFactory
			.getLog(WLNCreditProfileDataManagementServiceImpl.class);

	// Dependencies(collaborators) wired by Spring using constructor injection.

	/**
	 * Constructor with external dependencies which will be wired by Spring.
	 *
	 */
	public WLNCreditProfileDataManagementServiceImpl() {

	}


    @Override
    public void updateCreditWorthiness(
    		CreditAssessmentTransaction creditAssessmentTransactionResult,
            AuditInfo auditInfo)
    		throws PolicyException, ServiceException
    {
    	m_updateCreditWorthinessImpl.updateCreditWorthiness( creditAssessmentTransactionResult,
    			auditInfo );
    }
    
    // for RTCA1.5, there is no need to call Customer Management Service
    /**
     * @throws PolicyException 
     * @throws ServiceException 
     * 
     *//*
    public void updateCustommerCreditValue (long customerId,
            CreditValue creditValue, AuditInfo auditInfo ) 
    throws PolicyException, ServiceException {
    	
    	String operationName = "updateCustommerCreditValue";
    	
    	try {
			m_customerManagementServiceMediator.updateCreditValue( customerId,
			        creditValue.getCreditValueCode(), auditInfo.getUserId(), auditInfo.getOriginatorApplicationId() );
    	}
        catch ( WLNCreditProfileDataManagementPolicyException e ) {
            throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );
        }
        catch ( WLNCreditProfileDataManagementServiceException e ) {
            throw ExceptionFactory.createServiceException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );
		}
    	
    }*/

	/**
	 *
	 * @param creditProfile
	 * @param auditInfo
	 * @throws ServiceException
	 * @throws PolicyException
	 */
    @Override
    public void updateCreditProfile(CreditProfileData creditProfile,
    		RemoveWLNCreditIdentificationInfo removeWLNCreditIdentificationInfo,
            String creditValueCd, String fraudIndicatorCd, AuditInfo auditInfo)
            throws PolicyException, ServiceException {
        String operationName = "updateCreditProfile";
//        System.out.println("******************************************");
		if (log.isDebugEnabled()) {
			log.debug("Enter updateCreditProfile. creditValueCd = " + creditValueCd);
			log.debug(creditProfile);
			log.debug(removeWLNCreditIdentificationInfo);
			log.debug(auditInfo);
		}
		
		if (auditInfo.getTimestamp() == null) {
			throw ExceptionFactory.createPolicyException( operationName, "Audit Info Timestamp in the request can NOT be null",
		            WLNCreditProfileDataManagementExceptionCodes.AUDIT_INFO_VALIDATION_EXCEPTION );
		}

		CreditProfile crdPrfl = WlnPrflDMgtUtil
				.mapSchemaObjToDomain(creditProfile,creditValueCd, fraudIndicatorCd, auditInfo);

		if ( creditProfile.getCustomerID() <= 0)
		{
		    throw ExceptionFactory.createPolicyException( operationName, "Customer ID in the request can NOT be null",
		            WLNCreditProfileDataManagementExceptionCodes.CREDIT_PROFILE_VALIDATION_EXCEPTION );
		}
		try {

                updateCreditProfile(crdPrfl, auditInfo, creditProfile
                		.getCustomerID());
		}
            catch ( WLNCreditProfileDataManagementServiceException e ) {
                throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                        + creditProfile.getCustomerID(), e );
            }
            catch ( WLNCreditProfileDataManagementPolicyException e ) {
                throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                        + creditProfile.getCustomerID(), e );
            }
		catch (CreditProfileValidationException e) {
		    throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditProfile.getCustomerID(), e );
		} catch (CreditValueValidationException e) {
		    throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditProfile.getCustomerID(), e );
		} catch (ConcurrencyConflictException e) {
		    throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditProfile.getCustomerID(), e );
		}  catch (ObjectNotFoundException e) {
		    throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditProfile.getCustomerID(), e );
		}
		catch (PolicyException e)
		{
		    throw e;
		}
		catch (ServiceException e)
		{
		    throw e;
		}
		catch (RuntimeException e)
		{
		    throw ExceptionFactory.createServiceException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditProfile.getCustomerID(), e );
		}
		if (log.isDebugEnabled()) {
			log.debug("Exit updateCreditProfile.");
		}
	}

	@Override
	public String ping() throws PolicyException, ServiceException {
		if (log.isDebugEnabled()) {
			log.debug("Enter ping.");
		}

		if (log.isDebugEnabled()) {
			log.debug("Exit ping.");
		}
		return "OK";
	}

	private void updateCreditProfile(CreditProfile modifiedCreditProfile,
			AuditInfo auditInfo, long customerID)
			throws CreditProfileValidationException,
			ConcurrencyConflictException,
			CreditValueValidationException, PolicyException, ServiceException,
			ObjectNotFoundException, WLNCreditProfileDataManagementServiceException, WLNCreditProfileDataManagementPolicyException {
		String operationName = "updateCreditProfile";
		log.info(" in update method");
		log.debug(modifiedCreditProfile);
		log.debug(auditInfo);
		String userId = auditInfo.getUserId();
		if (userId == null) {
		    throw ExceptionFactory.createPolicyException( operationName, "userId in the request can NOT be null",
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_PROFILE_VALIDATION_EXCEPTION );
		}

		if (auditInfo.getOriginatorApplicationId() == null) {
		    throw ExceptionFactory.createPolicyException( operationName, "Application ID should not be null",
		            WLNCreditProfileDataManagementExceptionCodes.CREDIT_PROFILE_VALIDATION_EXCEPTION );
		}
		Integer sourceId = Integer.valueOf(auditInfo
		.getOriginatorApplicationId());

		if ( sourceId == null || "".equals(sourceId))
			sourceId = Integer.valueOf(-99);


		if (customerID <= 0) {
		    throw ExceptionFactory.createPolicyException( operationName, "Customer ID in the request can NOT be null",
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_PROFILE_VALIDATION_EXCEPTION );
		}
		//validate requested update credit profile
		if ( modifiedCreditProfile.getCreditValue()!=null) {
			log.info("before validation, credit value code = " + modifiedCreditProfile.getCreditValue().getCreditValueCode());
	//		System.out.println("before validation, credit value code = " + modifiedCreditProfile.getCreditValue().getCreditValueCode());
		}
		else {
			log.info("before validation, credit value is null!!!!");
		//	System.out.println("before validation, credit value is null!!!!");
		}
	//	System.out.println("Address Province Code : " + modifiedCreditProfile.getCreditAddress().getProvinceCode());
		ValidationResult rslt = getCreditProfileBr().validate(modifiedCreditProfile);
		if ( !rslt.isValid())
		{
		//	System.out.println("Credit Profile is not valid, validation result: " + rslt.toString());
		    throw ExceptionFactory.createPolicyException( operationName, "Credit Profile is not valid, validation result: " + rslt.toString(),
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_PROFILE_VALIDATION_EXCEPTION );
		}
		// CreditProfile originalCreditProfile = null;
		// originalCreditProfile = m_creditProfileDao.getCreditProfile(Integer
		// .valueOf(customerID.intValue()));


		long creditProfileId = m_creditProfileDao
				.getPrimaryCreditProfileIdByCustomerId((int)customerID);
		// The next call should return one or many (in case of merged Credit
		// Profile) dtos.
		//
	//	System.out.println("Credit Profile Id = " + creditProfileId);
		CreditProfileDto[] dto = m_creditProfileDao
				.getCreditProfileDtoByCreditProfileId(creditProfileId);

		if (dto == null || dto.length < 1) {
			// Someone already updated this CreditProfile (expired as a result
			// of merge)
			//
			log
					.error("Someone already updated this CreditProfile (expired as a "
							+ " result of merge). Updated Credit Profile content:"
							+ modifiedCreditProfile);
			throw new ConcurrencyConflictException("credit profile not found");
		}

//		if (dto.length > 1) {
//			validateMergedCreditProfile(modifiedCreditProfile, dto);
//		}

		CreditProfile originalCreditProfile = dto[0].getCreditProfile();
		  log.debug("--originalCreditProfile--");
		  log.debug(originalCreditProfile);
		//  System.out.println(originalCreditProfile);
		  Date orginalDOB = originalCreditProfile.getBirthdate();
		
		CustomerGuarantor g = modifiedCreditProfile.getCustomerGuarantor();
		if (g != null) {
			g.setGuaranteedCustomerId(dto[0].getCustomerId());
			
			// FR636882, Guarantor Id validation
			if (g.getGuaranteedCustomerId() == g.getGuarantorCustomerId()) {
				throw ExceptionFactory.createPolicyException( operationName, 
						"Customer ID of the Guarantor can NOT be the same as the Guaranteed Customer ID",
			            WLNCreditProfileDataManagementExceptionCodes.GUARANTOR_VALIDATION_EXCEPTION );
			}
			// FR636882, check if Guarantor's Customer ID is a valid Telus ID
			try {
				m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId(g.getGuarantorCustomerId());
			} catch (ObjectNotFoundException ex) {
				throw ExceptionFactory.createPolicyException( operationName, 
						"Customer ID of the Guarantor is not a valid Telus ID",
			            WLNCreditProfileDataManagementExceptionCodes.GUARANTOR_CUSTOMER_ID_VALIDATION_EXCEPTION );
			}
		}
	//	System.out.println("Modified credit profile ==================");
	//	System.out.println(modifiedCreditProfile);
		//Copy attribute in Personal Info and Credit Card Code from original Credit profile to the request object,
		// Reason being that the user does not need to pass all personal info and CreditCardCode
		// but only pass in the Personal Info attribute or CreditCardCode which are to be updated
		// However to ensure data integration for record insertion in DB all fields are needed
		copyPersonalInfoAndCreditCardCodeAttributesFromOriginal( originalCreditProfile, modifiedCreditProfile );
		
		if (modifiedCreditProfile.getProvinceOfCurrentResidenceCd() != null) {
			// FR636884, return an appropriate error indicator if the Province of Current Residency 
			// attribute is not a valid Canadian province
			CreditAttributeBr attributeBr = CreditAttributeBr.getInstance();
			CreditAttribute crdAttribute = new CreditAttribute();
	//		System.out.println("Province of Current Residency : " + modifiedCreditProfile.getProvinceOfCurrentResidenceCd());
			crdAttribute.setAttributeValue(modifiedCreditProfile.getProvinceOfCurrentResidenceCd());
			crdAttribute.setAttributeCode(CreditAttribute.CURRENT_PROVINCE_RESIDENCY_CODE);
			if (!attributeBr.isValidProvinceCode(crdAttribute)) {
				throw ExceptionFactory.createPolicyException(operationName, 
						"Current Province of Residency Code is not valid", 
						WLNCreditProfileDataManagementExceptionCodes.CURRENT_PROVINCE_OF_RESIDENCY_VALIDATION_EXCEPTION);
			}
		}
		
		copyCreditValueData(originalCreditProfile, modifiedCreditProfile );
			

		//Copy credit profile ID from original credit profile to the change requested credit profile, DB update
		// to the sub elements need the credit profile ID as the key.
		copyCreditProfileIdFromOroginal( originalCreditProfile, modifiedCreditProfile );
		
		// Determine what has been updated
		CreditProfileUpdates updates = new CreditProfileUpdates(
				originalCreditProfile, modifiedCreditProfile);
          
		if (!updates.isModified()) {
			//TODO log
		//	System.out.println("Nothing modified..........******************");
			return;
		}

		// Validate Credit Value only if either 'credit value' or 'id cards' has
		// been modified
		//
		// RTCA 1.0: Validation of credit value update is disabled in the service
		//if (updates.isCreditValueModified()) {
		//	validateCreditValue(originalCreditProfile, modifiedCreditProfile);
		//}

		// If guarantorId has been updated, check that its id exists
		// and set the guarantor's credit profile Id
		//
		if (updates.isGuarantorModified()) {
			if (updates.isGuarantorIdModified()) {
				// only validate against db if the id has changed.
				validateAndSetGuarantorsCreditProfileId(modifiedCreditProfile
						.getCustomerGuarantor());
			}
		}

		// Check current CreditIdCards. If none, then there are
		// no links to matching customers.
		// If present, and they have been updated:
		// delete any secondary links to this credit profile and for
		// this customerId.
		//
		 log.debug("---- Updates is  modified  checkingisCardIdArrayModified --");
		if (updates.isCardIdArrayModified()) {
			for (int i = 0; i < dto.length; i++) {
				if (log.isInfoEnabled()) {
					log.info("Deleting link for credit profile "
							+ modifiedCreditProfile.get_id() + "; customer "
							+ dto[i].getCustomerId());
				}
				m_creditProfileDao.deleteSecondaryLinks(new Long(
						modifiedCreditProfile.get_id()), new Integer(dto[i]
						.getCustomerId()), userId, sourceId);
				if (log.isInfoEnabled()) {
					log.info("... deletion successful");
				}
			}
			
			
		}
	try{
		if (modifiedCreditProfile.getBirthdate() != null) {
			if (orginalDOB == null || !orginalDOB.equals(modifiedCreditProfile.getBirthdate())) {
				JSONObject jsonObj = new CreateJSONMsg().createJSONMessageForDOB(customerID, modifiedCreditProfile.getBirthdate(), userId);
				if (log.isInfoEnabled()) 
				log.info("UpdateCreditProfile: Updated Credit Id's for Brithdate: JSONMsg :" + jsonObj.toString());
				creditPubSubConnector.publishMsgToPubSub(jsonObj.toString());
			}
		}
	}catch(Throwable e){			
			e.printStackTrace();
			log.error("Error while publishing Birthdate message to PUBSUB for customer ID: "+customerID,e);
	}

if (log.isInfoEnabled()) {
			log.info("Updating credit profile " + modifiedCreditProfile);
		}
		// update the credit profile
		//Before updating, set the last update timestamp in request object to the one from original credit profile
		// reason being is that when update credit profile, the dao will check if the timestamp matches the existing
		// one in DB --- But why ?
		modifiedCreditProfile.setBusinessLastUpdateTimestamp( originalCreditProfile.getBusinessLastUpdateTimestamp());
		// it also takes care of updating comments.
		m_creditProfileDao.updateCreditProfile(modifiedCreditProfile, userId,
				updates, sourceId);
		
		
		// update attributes if there are changes
		if (updates.isAttributesModified()) {
			if (log.isInfoEnabled()) {
				log.info("Attributes were modified...");
			}
			
			List<CreditAttribute> creditAttributeList = updates.getAttributeModifiedList();
			this.updateCreditAttributes(creditAttributeList, userId);
		}
		if (updates.isCardIdArrayModified()) {
//			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			if (log.isInfoEnabled()) {
				log.info("Were credit ids modified?  ..."
						+ updates.isCardIdArrayModified());
			}
			// Search for matching Customers on the 5 credit profile attributes.
			//
			CreditProfileDto[] matchingProfilesDto = null;
			if (modifiedCreditProfile.getCreditIDCards().length > 0) {
				matchingProfilesDto = getCreditProfilesByIdCards(modifiedCreditProfile , originalCreditProfile);
				// remove current customerId/CreditProfile pair
				//
				if (log.isInfoEnabled()) {
					log.info("matchingProfilesDto before filtering: ");
					logMatchingProfilesDto(matchingProfilesDto);
				}
				matchingProfilesDto = filterCurrentCustomerIds(creditProfileId,
						matchingProfilesDto);
				if (log.isInfoEnabled()) {
					log.info("matchingProfilesDto after filtering: ");
					logMatchingProfilesDto(matchingProfilesDto);
				}
			}

			// Set up any Customer To Secondary Credit Profile links
			//
			if (matchingProfilesDto != null && matchingProfilesDto.length > 0) {
				if (log.isInfoEnabled()) {
					log
							.info("About to create secondary links for creditProfileId "
									+ creditProfileId);
				}
				if (dto.length > 1) {
					if (log.isInfoEnabled()) {
						log
								.info("Create secondary links for merged credit profile "
										+ creditProfileId);
						log.info(" dto: ");
						logMatchingProfilesDto(dto);
						log.info(" matchingProfilesDto: ");
						logMatchingProfilesDto(matchingProfilesDto);
					}
					createSecondaryLinksForMergedCustomer(creditProfileId, dto,
							matchingProfilesDto, userId, sourceId);
					if (log.isInfoEnabled()) {
						log.info("Secondary links for merged credit profile "
								+ creditProfileId
								+ " were created successfully");
					}
				} else {
					if (log.isInfoEnabled()) {
						log
								.info("Create secondary links for regular credit profile "
										+ creditProfileId);
						log.info("dto[0]");
						log.info(dto[0]);
						logMatchingProfilesDto(matchingProfilesDto);
					}
					createSecondaryLinks(creditProfileId, dto[0]
							.getCustomerId(), matchingProfilesDto, userId,
							sourceId);
					if (log.isInfoEnabled()) {
						log.info("Secondary links for regular credit profile "
								+ creditProfileId
								+ " were created successfully");
					}
				}
			}
			
	//		System.out.println("**************** MDM *************************");
			// TCM-MDM data synch
			try{
		//		System.out.println("**************** MDM In *************************");
				
				JSONObject jsonObj = new CreateJSONMsg().createJSONMessageForCreditIds(modifiedCreditProfile, customerID,userId,"UPDATE");
				if (log.isInfoEnabled()) 
				log.debug("UpdateCreditProfile: Updated Credit Id's: JSONMsg :" + jsonObj.toString());
				creditPubSubConnector.publishMsgToPubSub(jsonObj.toString());
			}catch(Throwable e){
				log.error("UpdateCreditProfile: Error while sending a message to PUBSUB for customer ID: "+customerID,e);
			}
		}
	//	System.out.println("**************** MDM Out*************************");

		if ( updates.isCreditCheckConsentModified() ) {
		    if ( updates.isCreditCheckConsentModifiedFromYToN() ) {
		        m_creditAddressDao.deleteAddress( creditProfileId, userId, sourceId );
		    }
		    // channelId is not populated, same as R1
		    this.performOverrideCreditWorthiness(customerID, modifiedCreditProfile, AUDIT_CREDIT_ASSESSMENT_TYPE, auditInfo);
		    // R1 code
		    /*.performAuditCreditAssessment(
		            customerID, originalCreditProfile, modifiedCreditProfile, userId, auditInfo
		            .getOriginatorApplicationId() );*/
		    
		}
		// Last Step - Call Customer Mgt service to update credit value
		if (updates.isCreditValueModified()) {
			this.performOverrideCreditWorthiness(customerID, modifiedCreditProfile, OVERRIDE_CREDIT_ASSESSMENT_TYPE, auditInfo);
		}

	}

    private void copyCreditValueData(
            CreditProfile originalCreditProfile,
            CreditProfile modifiedCreditProfile)
    {
       if ( originalCreditProfile != null && modifiedCreditProfile != null ) {
           CreditValue originalCreditValue = originalCreditProfile.getCreditValue();
           CreditValue modifiedCreditValue = modifiedCreditProfile.getCreditValue();
           if ( originalCreditValue == null ) {
               return;
           }
           if ( originalCreditValue != null && modifiedCreditValue == null ) {
               modifiedCreditValue = new CreditValue();
               modifiedCreditProfile.setCreditValue( modifiedCreditValue );
           }
           //
           // Copy all of the attributes from original to modified except fraud indicator
           // and credit value code, these are the only attributes that can be modified
           // do not copy credit assessment or derived attributes
           //
           modifiedCreditValue.setAssessmentMsgCd( originalCreditValue.getAssessmentMsgCd() );
           modifiedCreditValue.setCreditClassCd( originalCreditValue.getCreditClassCd() );
           modifiedCreditValue.setDecisionCd(originalCreditValue.getDecisionCd());
           modifiedCreditValue.setCreditDecisionCd( originalCreditValue.getCreditDecisionCd() );
           modifiedCreditValue.setCreditDecisionMsgTxt( originalCreditValue.getCreditDecisionMsgTxt() );
           modifiedCreditValue.setCreditScoreNumber( originalCreditValue.getCreditScoreNumber() );
           modifiedCreditValue.setCreditProfileId( originalCreditValue.getCreditProfileId() );
           modifiedCreditValue.setProdCategoryBoltOn( originalCreditValue.getProdCategoryBoltOn() );
           ProductCategoryQualification originalPC = originalCreditValue.getProductCatQualification();
           if ( originalPC != null ) {
               ProductCategoryQualification modifiedProductCQ = new ProductCategoryQualification();
               modifiedProductCQ.setBoltOn( originalPC.getBoltOn() );
               List modifiedList = new ArrayList();
               if ( originalPC.getProductCategoryList() != null ) {
                   modifiedList.addAll( originalPC.getProductCategoryList()  );
                   modifiedProductCQ.setProductCategoryList( modifiedList );
               }
               modifiedCreditValue.setProductCatQualification( modifiedProductCQ );
           }
           
           if ( originalCreditValue.getFraudMessageCodeList() != null ) {
               List modifiedFraudList = new ArrayList();
               modifiedFraudList.addAll( originalCreditValue.getFraudMessageCodeList() );
               modifiedCreditValue.setFraudMessageCodeList( modifiedFraudList );
           }
           if ( modifiedCreditValue.getLastUpdateTimestamp() == null ) {
               modifiedCreditValue.setLastUpdateTimestamp( originalCreditValue.getLastUpdateTimestamp() );
           }
           if ( modifiedCreditValue.getMethod() == null 
                || modifiedCreditValue.getMethod().trim().length()  == 0 ) {
               modifiedCreditValue.setMethod( originalCreditValue.getMethod() );
           }
           if ( modifiedCreditValue.getComment() == null 
                || modifiedCreditValue.getComment().trim().length()  == 0 ) {
               modifiedCreditValue.setComment( originalCreditValue.getComment() );
           }
           if ( modifiedCreditValue.getCreditValueCode() == null
                || modifiedCreditValue.getCreditValueCode().trim().length() == 0 ) {
               modifiedCreditValue.setCreditValueCode( originalCreditValue.getCreditValueCode() );
           }
           if ( modifiedCreditValue.getFraudIndicatorCd() == null
                || modifiedCreditValue.getFraudIndicatorCd().trim().length() == 0 ) {
               modifiedCreditValue.setFraudIndicatorCd( originalCreditValue.getFraudIndicatorCd() );
           }
       }
    }


    private void doCreditWorthinessUpdateProcess( long customerId, CreditProfile originalCreditProfile,
            CreditProfile modifiedCreditProfile, CreditProfileUpdates updates,
            AuditInfo auditInfo ) throws WLNCreditProfileDataManagementServiceException, WLNCreditProfileDataManagementPolicyException, PolicyException, ServiceException {

    	// channelId is not populated, same in R1            
    	/*m_creditProfileMgmtProxySvcIntermediator.performOverrideCreditWorthiness(
    			customerId, originalCreditProfile, modifiedCreditProfile, auditInfo.getUserId(), 
    			auditInfo.getOriginatorApplicationId(), OVERRIDE_CREDIT_ASSESSMENT_TYPE);*/
    	// R1 code
            /*CreditValue creditValue = m_creditProfileMgmtProxySvcMediator.performOverrideCreditAssessment(  customerId,
                        originalCreditProfile,
                        modifiedCreditProfile,
                        auditInfo.getUserId(),
                        auditInfo.getOriginatorApplicationId() );
            
            if ( updates.isOnlyFraudIndicatorModified() ) {
                // in that case, just apply the change in credit value for fraud only
                // rest remains original
                modifiedCreditProfile.getCreditValue().setCarId( creditValue.getCarId() );
                creditValue = modifiedCreditProfile.getCreditValue();
            }
            
            updateCreditValue( customerId, creditValue, auditInfo );*/
    }
    
    /**
     * 
     * @param customerId
     * @param modifiedCreditProfile
     * @param assessmentType TODO
     * @param auditInfo
     * @throws WLNCreditProfileDataManagementPolicyException 
     * @throws WLNCreditProfileDataManagementServiceException 
     */
    private void performOverrideCreditWorthiness (long customerId, CreditProfile modifiedCreditProfile,
            String assessmentType, AuditInfo auditInfo) 
    throws WLNCreditProfileDataManagementServiceException, 
    WLNCreditProfileDataManagementPolicyException {
    	
    	m_creditProfileMgmtProxySvcIntermediator.performOverrideCreditWorthiness(
    			customerId, modifiedCreditProfile, auditInfo.getUserId(), auditInfo.getOriginatorApplicationId(), 
    			assessmentType);
    	
    }

	private void logMatchingProfilesDto(CreditProfileDto[] matchingProfilesDto) {
		for (int i = 0; i < matchingProfilesDto.length; i++) {
			log.info(i + ") " + matchingProfilesDto[i]);
		}
	}


	/**
	 * <p>
	 * <b>Description: </b> See interface.
	 * </p>
	 * <p>
	 * <b>High Level Design: </b>
	 * </p>
	 * <ul>
	 * <li>Retrieves credit profile from the Credit Management PDS.</li>
	 * <li>Returns credit profile object to the caller.</li>
	 * </ul>
	 *
	 * @param customerId
	 * @return CreditProfile
	 */
	/*
	 * (non-Javadoc)
	 *
	 * @see com.telus.credit.service.CreditProfileMgtSvc#getCreditProfile(int)
	 */
	public CreditProfile getCreditProfile(int customerId)
			throws ObjectNotFoundException {
		return m_creditProfileDao.getCreditProfile(new Integer(customerId));
	}

	/**
	 *
	 * <p>
	 * <b>Description </b> Gets CreditProfiles that match given CreditProfile
	 * (by id cards)
	 * </p>
	 *
	 * @param creditProfile
	 * @return CreditProfileDto []
	 */
	private CreditProfileDto[] getCreditProfilesByIdCards(
			CreditProfile modifiedCreditProfile , CreditProfile originalCreditProfile) {
		CreditProfileDto[] creditProfileDtos = null;
		CreditIDCard[] modifiedIdCards = modifiedCreditProfile.getCreditIDCards();
		CreditIDCard[] originalIdCards = originalCreditProfile.getCreditIDCards();

		HashSet<String> modifiedIdCardType = new HashSet<String>();
		if (modifiedIdCards != null) {

			Set matchedCustomerIds = new HashSet();
			for (int i = 0; i < modifiedIdCards.length; i++) {
				// This must be added outside if so that we know that we have considered this modified type
				modifiedIdCardType.add( modifiedIdCards[i].getIdTypeCode());
				// 28645 Zero Credit Id Fix, Do not get related customers with zero credit ids
				if (!IdCardUpdates.isCreditIdWithZeroValue( modifiedIdCards[i].getIdNumber() ) ) {
					List customerIds = m_creditIDCardDao
							.getConsumerCustomerIdByMatchingCreditIdCard(modifiedIdCards[i]);
					matchedCustomerIds.addAll(customerIds);
				}
			}

			/* QC84394 - because only modified ID will be in the web service request, so when re-building the secondary links by ID we need
			 * to also take in account those IDs which are not not being changed (e.g. NOT in the web service request)
			 */
			if ( originalIdCards != null){
				for (int i = 0; i < originalIdCards.length; i++) {
					if ( !modifiedIdCardType.contains(originalIdCards[i].getIdTypeCode())
						// 28645 Zero Credit Id Fix, Do not get related customers with zero credit ids	
						 && (!IdCardUpdates.isCreditIdWithZeroValue(originalIdCards[i].getIdNumber()))){
						List customerIds = m_creditIDCardDao
								.getConsumerCustomerIdByMatchingCreditIdCard(originalIdCards[i]);
						matchedCustomerIds.addAll(customerIds);
					}
				}
			}

			creditProfileDtos = getMatchingCustomerCreditProfilesByCustomerIds(matchedCustomerIds);

		} else {
			creditProfileDtos = new CreditProfileDto[0];
		}

		return creditProfileDtos;
	}

	/**
	 *
	 * <p>
	 * <b>Description </b> Gets CreditProfiles that match CreditProfile of the
	 * given customer.
	 * </p>
	 *
	 * @param matchedCustomerIds
	 * @return CreditProfileDto []
	 */
	private CreditProfileDto[] getMatchingCustomerCreditProfilesByCustomerIds(
			Set matchedCustomerIds) {
		Set matchedCustomerIDsForAllCustomers = new HashSet();
		for (Iterator i = matchedCustomerIds.iterator(); i.hasNext();) {
			Integer cid = (Integer) i.next();
			CreditMgtCustomerID customerID = new CreditMgtCustomerID(cid
					.intValue());
			CreditMgtCustomerIDs matchedCustomerIDsForCustomer = new CreditMgtCustomerIDs();

			// Check whether this customer id is in the set
			// if yes - skip the next 3 lines.
			//
			if (!matchedCustomerIDsForCustomer.containsId(customerID.getId()
					.intValue())) {
				matchedCustomerIDsForCustomer.add(customerID);
				findMatchingCustomerIds(matchedCustomerIDsForCustomer);
				matchedCustomerIDsForAllCustomers
						.addAll(matchedCustomerIDsForCustomer);
			}
		}

		// Get PRIMARY CreditProfiles for each matched Customer.
		//
		CreditProfile creditProfile = null;
		List customerCreditProfiles = new ArrayList();

		for (Iterator i = matchedCustomerIDsForAllCustomers.iterator(); i
				.hasNext();) {
			CreditMgtCustomerID customerId = (CreditMgtCustomerID) i.next();
			try {
				creditProfile = m_creditProfileDao.getCreditProfile(customerId
						.getId());
			} catch (ObjectNotFoundException ex) {
				throw new CreditProfileMgtSvcException(
						"Failed to retrieve matched CreditProfile by Customer Id",
						CreditProfileMgtSvcExceptionCodes.MATCHED_CREDIT_PROFILE_IS_NOT_FOUND,
						ex);
			}
			customerCreditProfiles.add(new CreditProfileDto(customerId.getId()
					.intValue(), creditProfile));
		}
		return (CreditProfileDto[]) customerCreditProfiles
				.toArray(new CreditProfileDto[customerCreditProfiles.size()]);
	}

	/**
	 * <p>
	 * <b>Description: </b> This method searches recursively for all matching
	 * customer ids in the customer-credit profile mapping table.
	 * </p>
	 * <p>
	 * <b>High Level Design: </b>
	 * </p>
	 * <ol>
	 * <li>Finds "unprocessed" CustomerID objects in the CustomerIDs object.</li>
	 * <li>Calls {@link CreditProfileDao#getMatchingCustomerIds}for each
	 * unprocessed CustomerID.</li>*
	 * <li>Eliminates duplicate CustomerID objects.</li>
	 * <li>Call itself until all matched customerIds are found.</li>
	 * </ol>
	 *
	 * @param matchedCustomerIDs
	 *            - encapsulates ids of matched customers.
	 */
	private void findMatchingCustomerIds(CreditMgtCustomerIDs matchedCustomerIDs) {
		Set set = new HashSet();
		for (Iterator it = matchedCustomerIDs.iterator(); it.hasNext();) {
			CreditMgtCustomerID customerID = (CreditMgtCustomerID) it.next();
			if (!customerID.isProcessed()) {
				Integer[] customerIds = m_creditProfileDao
						.getMatchingCustomerIds(customerID.getId());
				int i = 0;
				while (i < customerIds.length) {
					int cid = customerIds[i].intValue();
					if (!matchedCustomerIDs.containsId(cid)) {
						CreditMgtCustomerID cust = new CreditMgtCustomerID(cid);
						set.add(cust);
					}
					i++;
				}
				i = 0;
				customerID.setProcessed(true);
			}
		}
		matchedCustomerIDs.addAll(set);
		if (matchedCustomerIDs.containsUnprocessedId()) {
			findMatchingCustomerIds(matchedCustomerIDs);
		}
	}

	/**
	 * <p>
	 * <b>Description </b> Sets CreditProfileDao.
	 * </p>
	 *
	 * @param creditProfileDao
	 *            that implement CreditProfileDao interface.
	 */

	public void setCreditProfileDao(CreditProfileDao creditProfileDao) {
		m_creditProfileDao = creditProfileDao;
	}

	/**
	 * <p>
	 * <b>Description </b> Sets CreditIDCardDao.
	 * </p>
	 *
	 * @param creditIDCardDao
	 *            that implement CreditIDCardDao interface.
	 */
	public void setCreditIDCardDao(CreditIDCardDao creditIDCardDao) {
		m_creditIDCardDao = creditIDCardDao;
	}

	
	
	/**
	 * <p>
	 * <b>Description </b> Sets CreditAttributeDao.
	 * </p>
	 * @param m_creditAttributeDao the m_creditAttributeDao to set
	 */
	public void setCreditAttributeDao(CreditAttributeDao m_creditAttributeDao) {
		this.m_creditAttributeDao = m_creditAttributeDao;
	}

	/**
	 * Gets the Primary Credit Profile Id from the CreditProfile to Customer Map
	 * table. If the CustomerId is not in the table, null is returned.
	 *
	 * @param customerId
	 * @return primary credit profile id
	 */
	private Long getPrimaryCreditProfileIdByCustomerId(int customerId) {
		Long custProfileId = null;

		try {
			custProfileId = m_creditProfileDao
					.getPrimaryCreditProfileIdByCustomerId(new Integer(
							customerId));
		} catch (ObjectNotFoundException e2) {
			// THIS IS EXPECTED BEHAVIOUR FOR SOME CALLS SO DON'T THROW
			// Client methods that wish to throw an objectNotFound can
			// check for null.
		}
		return custProfileId;
	}

    	/**
	 * Gets the Guarnator's Primary Credit Profile Id from the CreditProfile to
	 * Customer Map table. If it exists, it is set in the domain object.
	 *
	 * @param customerGuarantor
	 * @throws InvalidGuarantorIdException
	 *             if CreditProfile doesn't exists.
	 */
	private void validateAndSetGuarantorsCreditProfileId(
			CustomerGuarantor customerGuarantor)
			throws WLNCreditProfileDataManagementPolicyException {
		Long guarantorCreditProfileId = getPrimaryCreditProfileIdByCustomerId(customerGuarantor
				.getGuarantorCustomerId());

		if (guarantorCreditProfileId == null) {
		    throw new WLNCreditProfileDataManagementPolicyException( "Invalid Guranted customer id: " + customerGuarantor
	                .getGuarantorCustomerId(),
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_PROFILE_VALIDATION_EXCEPTION,null );
		}
		customerGuarantor.setGuarantorCreditProfileId(guarantorCreditProfileId
				.longValue());
	}

	/**
	 * Creates secondary links for the updateCreditProfile operation.
	 *
	 * @param creditProfileId
	 * @param dto
	 * @param matchingProfilesDto
	 * @param userId
	 */
	private void createSecondaryLinksForMergedCustomer(Long creditProfileId,
			CreditProfileDto[] dto, CreditProfileDto[] matchingProfilesDto,
			String userId, Integer sourceId) {

		Set profileIdSet = new HashSet();
		try {

			// link the merged customers to the matching credit profiles
			//
			for (int i = 0; i < dto.length; i++) {

				int customerId = dto[i].getCustomerId();

				for (int j = 0; j < matchingProfilesDto.length; j++) {

					CreditProfile secondaryCreditProfile = matchingProfilesDto[j]
							.getCreditProfile();
					Long secondaryCreditProfileId = new Long(
							secondaryCreditProfile.get_id());
					/*QC 84210
					 * Filter out the duplicate CreditePorfileId otherwise it will encounter a unique key
					 *  violation when create the seconday link
					 */
					if (!profileIdSet.contains(secondaryCreditProfileId)) {
						m_creditProfileDao.addCreditProfileToCustomer(
								secondaryCreditProfileId, customerId,
								CreditProfile.SECONDARY_KEY, userId, sourceId);
						profileIdSet.add(secondaryCreditProfileId);
					}
				}
			}

			// link the updated CreditProfile to the matching customer(s).
			//
			for (int j = 0; j < matchingProfilesDto.length; j++) {
				int matchedCustomerId = matchingProfilesDto[j].getCustomerId();

				m_creditProfileDao.addCreditProfileToCustomer(creditProfileId,
						matchedCustomerId, CreditProfile.SECONDARY_KEY, userId,
						sourceId);
			}
		} catch (DuplicateKeyException e) {
			CreditProfileMgtSvcException ex = new CreditProfileMgtSvcException(
					e);
			ex
					.setErrorCode(CreditProfileMgtSvcExceptionCodes.FAILED_TO_CREATE_SECONDARY_LINKS);
			throw ex;
		}

	}

	/**
	 * If matched profiles found, Create the secondary credit profile links in
	 * the CreditProfileToCustomer map table: 1. link the new CreditProfile with
	 * the matching customer(s) 2. link the new Customer with secondary
	 * CreditProfile(s)
	 *
	 * @param creditProfileId
	 * @param customerId
	 * @param creditProfileDto
	 */
	private void createSecondaryLinks(Long creditProfileId, int customerId,
			CreditProfileDto[] creditProfileDto, String userId, Integer sourceId) {

		try {
			// keep track of secondary credit profiles as must avoid
			// creating duplicate secondary links to it in the case where
			// we match a merged customer.
			//
			Set profileIdSet = new HashSet();

			for (int i = 0; i < creditProfileDto.length; i++) {
				int matchedCustomerId = creditProfileDto[i].getCustomerId();
				CreditProfile secondaryCreditProfile = creditProfileDto[i]
						.getCreditProfile();

				Long secondaryCreditProfileId = new Long(secondaryCreditProfile
						.get_id());

				// link the updated CreditProfile to the matching customer(s).
				//
				m_creditProfileDao.addCreditProfileToCustomer(creditProfileId,
						matchedCustomerId, CreditProfile.SECONDARY_KEY, userId,
						sourceId);

				// link the new CustomerId to the matching credit profiles
				// - if a merged customer matches, avoid creating two
				// links from the primary customer to the single shared profile
				//
				if (!profileIdSet.contains(secondaryCreditProfileId)) {
					m_creditProfileDao.addCreditProfileToCustomer(
							secondaryCreditProfileId, customerId,
							CreditProfile.SECONDARY_KEY, userId, sourceId);
					profileIdSet.add(secondaryCreditProfileId);
				}

			}
		} catch (DuplicateKeyException e1) {
			CreditProfileMgtSvcException ex = new CreditProfileMgtSvcException(
					"Failed to create secondary links", e1);
			ex
					.setErrorCode(CreditProfileMgtSvcExceptionCodes.FAILED_TO_CREATE_SECONDARY_LINKS);
			throw ex;
		}
	}



	/**
	 * Filters out the original, primary credit profile.
	 *
	 * @param primaryProfileId
	 * @param creditProfilesDto
	 * @return filtered CreditProfileDto array
	 */
	private CreditProfileDto[] filterCurrentCustomerIds(Long primaryProfileId,
			CreditProfileDto[] creditProfilesDto) {
		List list = new ArrayList();
		for (int i = 0; i < creditProfilesDto.length; i++) {
			if (!(primaryProfileId.longValue() == creditProfilesDto[i]
					.getCreditProfile().get_id())) {

				list.add(creditProfilesDto[i]);
			}
		}
		return (CreditProfileDto[]) list.toArray(new CreditProfileDto[list
				.size()]);
	}
	
	/**
     * @throws ConcurrencyConflictException 
     * 
     */
    private void updateCreditAttributes (List<CreditAttribute> listCreditAttribute, String userId) 
    throws ConcurrencyConflictException {
    	
    	for (CreditAttribute creditAttribute : listCreditAttribute) {
			m_creditAttributeDao.updateCreditAttribute(creditAttribute, userId);
		}
    }


    /**
     * @return Returns the m_creditAddressDao.
     */
    public CreditAddressDao getCreditAddressDao()
    {
        return m_creditAddressDao;
    }


    /**
     * @param m_creditAddressDao The m_creditAddressDao to set.
     */
    public void setCreditAddressDao(CreditAddressDao m_creditAddressDao)
    {
        this.m_creditAddressDao = m_creditAddressDao;
    }

	public CreditProfileBr getCreditProfileBr() {
		CreditProfileBr br = null;
		if (m_creditProfileBr != null) {
			br = m_creditProfileBr;
		} else {
			br = CreditProfileBr.getInstance();
		}
		return br;
	}

	private void copyPersonalInfoAndCreditCardCodeAttributesFromOriginal ( CreditProfile original , CreditProfile requested)
	{
		if ( requested.getBirthdate() == null )
			requested.setBirthdate( original.getBirthdate());
		if ( requested.getCreditCheckConsentCode() == null )
		{
			requested.setCreditCheckConsentCode( original.getCreditCheckConsentCode());
		}
		if ( requested.getEmploymentStatusCode() == null)
		{
			requested.setEmploymentStatusCode( original.getEmploymentStatusCode());
		}
		if ( requested.getResidencyCode() == null )
		{
			requested.setResidencyCode( original.getResidencyCode());
		}
		if ( requested.getUnderLegalCareCode() == null)
		{
			requested.setUnderLegalCareCode(original.getUnderLegalCareCode());
		}

		if ( requested.getPrimaryCreditCardCode() == null)
		{
			requested.setPrimaryCreditCardCode( original.getPrimaryCreditCardCode());
		}

		if ( requested.getSecondaryCreditCardCode() == null)
		{
			requested.setSecondaryCreditCardCode( original.getSecondaryCreditCardCode());
		}

		if ( requested.getApplicationProvinceCd() == null )
		{
		    requested.setApplicationProvinceCd( original.getApplicationProvinceCd() );
		}
		
		if (requested.getProvinceOfCurrentResidenceCd() == null) 
		{
			requested.setProvinceOfCurrentResidenceCd( original.getProvinceOfCurrentResidenceCd() );
		}

		// by pass match indicator cannot be updated, so, copy it from the original
		requested.setBypassMatchIndicator( original.isBypassMatchIndicator() );
	}

	private void copyCreditProfileIdFromOroginal ( CreditProfile original , CreditProfile requested)
	{
		requested.set_id( original.get_id());
		if ( requested.getCreditAddress()!=null)
			requested.getCreditAddress().setCreditProfileId(original.get_id());
		if ( requested.getCreditValue() !=null )
			requested.getCreditValue().setCreditProfileId( original.get_id());
		if ( requested.getCreditIDCards()!=null)
		{
			for ( CreditIDCard idCard : requested.getCreditIDCards())
				idCard.setCreditProfileId(original.get_id());
		}
	}

	public void setCreditProfileBr ( CreditProfileBr creditProfileBr )
	{
		this.m_creditProfileBr = creditProfileBr;
	}

	public WLNCreditProfileMgmtProxyServiceIntermediator getCreditProfileMgmtProxySvcIntermediator()
	{
		return this.m_creditProfileMgmtProxySvcIntermediator;
	}
	
	public void setCreditProfileMgmtProxySvcIntermediator ( WLNCreditProfileMgmtProxyServiceIntermediator creditProfileMgmtProxySvcIntermediator)
	{
		this.m_creditProfileMgmtProxySvcIntermediator = creditProfileMgmtProxySvcIntermediator;
	}


	/**
	 * @return the m_updateCreditWorthinessImpl
	 */
	public UpdateCreditWorthinessImpl getUpdateCreditWorthinessImpl() {
		return m_updateCreditWorthinessImpl;
	}


	/**
	 * @param m_updateCreditWorthinessImpl the m_updateCreditWorthinessImpl to set
	 */
	public void setUpdateCreditWorthinessImpl(
			UpdateCreditWorthinessImpl m_updateCreditWorthinessImpl) {
		this.m_updateCreditWorthinessImpl = m_updateCreditWorthinessImpl;
	}
	
}
