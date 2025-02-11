package com.telus.credit.wlnprflmgt.webservice.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.telus.credit.dao.CreditAttributeDao;
import com.telus.credit.dao.CreditIDCardDao;
import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.dao.CreditStatusDao;
import com.telus.credit.dao.CustomerGuarantorDao;
import com.telus.credit.domain.CreditAttribute;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditIDCardBr;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditProfileBr;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.domain.ProductCategory;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.service.InvalidGuarantorIdException;
import com.telus.credit.service.dto.CreditProfileDto;
import com.telus.credit.service.dto.search.CreditMgtCustomerID;
import com.telus.credit.service.dto.search.CreditMgtCustomerIDs;
import com.telus.credit.util.CreateJSONMsg;
import com.telus.credit.util.CreditPubSubConnector;
import com.telus.credit.wlnprflmgt.client.WLNCreditProfileMgmtProxyServiceIntermediator;
import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;
import com.telus.credit.wlnprflmgt.domain.CreditProfileSearchResult;
import com.telus.credit.wlnprflmgt.domain.CreditWorthiness;
import com.telus.credit.wlnprflmgt.domain.credit.common.CreditIdentification;
import com.telus.credit.wlnprflmgt.domain.credit.common.CreditProfileData;
import com.telus.credit.wlnprflmgt.domain.enterprise.common.AuditInfo;
import com.telus.credit.wlnprflmgt.webservice.util.DataConvertUtil;
import com.telus.credit.wlnprflmgt.webservice.util.DataObjectUtil;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.validation.ValidationResult;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.WLNCreditProfileManagementServicePortType;
//import com.telus.credit.wlnprflmgt.client.CustomerManagementServiceIntermediator;
/**
 * <p>
 * WLNCreditProfileManagementService implementation.
 * 
 * @author Danil Glinenko
 *
 */
public class WLNCreditProfileManagementServiceImpl 
	implements WLNCreditProfileManagementServicePortType {
	private static final Log log = LogFactory.getLog(WLNCreditProfileManagementServiceImpl.class);

	// Dependencies(collaborators) wired by Spring using constructor injection.
	
	private CreditProfileDao m_creditProfileDao;
	private CreditIDCardDao m_creditIDCardDao;
	//private CreditProfileMapDao m_creditProfileMapDao;
	private CreditStatusDao m_creditStatusDao;
	private CustomerGuarantorDao m_customerGuarantorDao;
	private CreditAttributeDao m_creditAttributeDao;
	private CreditProfileBr m_creditProfileBr;
	private CreditIDCardBr m_creditIDCardBr;
	//private CrdProfileUpdater m_crdProfileUpdater;
	private PlatformTransactionManager transactionManager;
	private WLNCreditProfileMgmtProxyServiceIntermediator m_creditProfileMgmtProxyServiceMediator;
	private CreditPubSubConnector creditPubSubConnector;


	public CreditPubSubConnector getCreditPubSubConnector() {
		return creditPubSubConnector;
	}

	public void setCreditPubSubConnector(CreditPubSubConnector creditPubSubConnector) {
		this.creditPubSubConnector = creditPubSubConnector;
	}

    public final static String DEFAULT_CREDIT_ASSESSMENT_COMMENT = "Default - Credit Assessment Not Complete";// TODO change to CRDA-TEXT-001 when we implement RefPDS
    public final static String DEFAULT_UNMERGE_COMMENT = "Unmerged Credit Profile";// TODO change to CRDA-TEXT-003
    public final static String DEFAULT_CREDIT_VALUE_CODE="R";
    public final static String UNMERGE_METHOD_CODE="UM";
    public final static String DEFAULT_CREDIT_VALUE_DETAIL_CODE="1";
    public final static boolean DEFAULT_PRODUCT_CATEGORY_BOLT_ON = false;
    public final static String DEFAULT_CREDIT_ASSESSMENT_MESSAGE_CODE = "REGCA03";
    public final static String DEFAULT_TELUS_DECISION_CODE = "DFLTR0000001";
    public final static Integer DEFAULT_RISK_LEVEL_NUM=9000;


	/**
	 * Constructor with external dependencies which will be wired by Spring.
	 * 
	 */
	public WLNCreditProfileManagementServiceImpl() {
		
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
     * @param customerId
     * @param auditInfo
     * @return
     *     returns com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public ConsumerCreditProfile getCreditProfileByCustomerId(long customerId, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
	if ( log.isDebugEnabled() ) {
	    log.debug( "Enter getCreditProfileByCustomerId." );
	    log.debug( "passed in customer id is " + customerId);
	}
	String errorMsg ;
	CreditProfile creditProfile=new CreditProfile();
	CreditAttribute creditAttribute=new CreditAttribute();
	CreditWorthiness creditWorthiness=new CreditWorthiness();
	ConsumerCreditProfile consumerCreditProfile=new ConsumerCreditProfile();
	try{
		creditProfile=m_creditProfileDao.getCreditProfile(new Integer(String.valueOf(customerId)));
		DataObjectUtil.copyToCreditProfileData(creditProfile, consumerCreditProfile);
		consumerCreditProfile.setCustomerID(customerId);//setCustomerId(customerId);
		DataObjectUtil.copyToCreditWorthiness(creditProfile,creditWorthiness);
	 	creditWorthiness.setCustomerID(customerId);
		consumerCreditProfile.setCreditWorthiness(creditWorthiness);
		/*
		 * RTCA 1.5 
		 * get Credit profile attributes
		 */
		List<CreditAttribute> creditProfileAttributeList=new ArrayList<CreditAttribute>();
        try {
        	creditProfileAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfile.get_id());
        }
        catch ( ObjectNotFoundException ex ) {
            // THIS IS EXPECTED BEHAVIOUR FOR SOME CALLS SO IGNORE THIS EXCEPTION.
        }
        
        if (!creditProfileAttributeList.isEmpty())
           {   
        	  DataObjectUtil.copyToConsumerCreditProfile(creditProfileAttributeList, consumerCreditProfile);
              if ( log.isDebugEnabled() )
              {
            	  log.debug(" Credit Value Effective Date is "+ consumerCreditProfile.getCreditValueEffectiveDate());
            	  log.debug(" First Credit Assessment Date is "+ consumerCreditProfile.getFirstCreditAssessmentDate());
                }
             }
     }catch (ObjectNotFoundException notFoundEx) {	 
		 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 getCreditProfileByCustomerId: Unable to get credit profile for customer :").append(customerId).toString();
		 log.error( errorMsg);
		 throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_RETRIEVE_CREDIT_PROFILE));
	 }catch( Throwable e ){
		errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 getCreditProfileByCustomerId: System throws unchecked exceptions when getting credit profile for customer  :").append(customerId).toString();
		log.error(errorMsg + e);
		throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.UNKNOWN_EXCEPTION),e);
	}
		
	if ( log.isDebugEnabled() ) {
	    log.debug( "Exit getCreditProfileByCustomerId." );
	}
	
	return consumerCreditProfile;
    }

    /**
     * <p>
	 * <b>Description: </b> See interface.
	 * </p>
	 * <p>
	 * <b>High Level Design: </b>
	 * <ul>
	 * <li>From the input CreditProfile, pick out the values of the 5 key
	 * fields (SIN#, Provincial Id#, Driver License#, Health Care#, Passport#)
	 * <li>If not all of the 5 key fields are empty, call DAO method
	 * {@link CreditProfileDao#searchCustIdByCreditProfile}and pass the key
	 * fields in a Hashmap as parameter.
	 * <li>List of ids for customers who have primary credit profile that
	 * matches with the input credit profile on at least one of the 5 key fields
	 * will be returned by the DAO method.
	 * <li>Loop through the customers ids found from the previous step and call
	 * {@link getMatchingCustomerCreditProfiles}method passing in the id one by
	 * one as input parameter.
	 * <li>Collect the method call output of previous step one by one from the
	 * loop and store them in an ArrayList.
	 * <li>Cast the resulting ArrayList as type CreditProfileDto object and
	 * return it.
	 * </ul>
	 * </p>
      
     * @param creditIdentification
     * @param auditInfo
     * @return
     *     returns java.util.List<com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile>
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public List<CreditProfileSearchResult> searchCreditProfileByCreditId(CreditIdentification creditIdentification, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
	if ( log.isDebugEnabled() ) {
	    log.debug( "Enter searchCreditProfileByCreditId." );
	}
	
	List<CreditProfileSearchResult> creditProfileSearchResultList=new ArrayList<CreditProfileSearchResult>();
	try 
	  {
	   List<CreditIDCard> creditIDCardList=new ArrayList<CreditIDCard>();
	   DataObjectUtil.copyToCreditIDCards(creditIdentification,creditIDCardList);
	   CreditProfileDto[] creditProfileDtos=getCreditProfilesByIdCards(creditIDCardList.toArray(new CreditIDCard[0]));
	   CreditProfileSearchResult creditProfileSearchResult=null;
	   //ConsumerCreditProfile consumerCreditProfile=null;
	   
	   for (int i = 0; i < creditProfileDtos.length; i++) 
	   {
		   creditProfileSearchResult=new CreditProfileSearchResult();
		   DataObjectUtil.copyToCreditProfileData(creditProfileDtos[i].getCreditProfile(), creditProfileSearchResult);
		   creditProfileSearchResult.setCustomerID(new Long(String.valueOf(creditProfileDtos[i].getCustomerId()))); 
		   if (creditProfileDtos[i].getCreditProfile().getCreditValue()!=null)
		       creditProfileSearchResult.setCreditValueCd(creditProfileDtos[i].getCreditProfile().getCreditValue().getCreditValueCode());
		   creditProfileSearchResultList.add(creditProfileSearchResult);
	    }
	  }catch( Throwable e ){
			String errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 searchCreditProfileByCreditId: System throws unchecked exceptions when searching credit profile by credit ids ").toString();
			log.error(errorMsg + e);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.UNKNOWN_EXCEPTION),e);
		}
	if ( log.isDebugEnabled() ) {
	    log.debug( "Exit searchCreditProfileByCreditId." );
	}
	return creditProfileSearchResultList;
    }

    /**
     * 
     * <p>
     * <b>Description </b> Expires Credit profile
     * </p>
     * <p>
	 * <b>High Level Design: </b> Expires credit profile provided by customer id
	 * </p>
	 * <ul>
	 * <li>Remove any secondary links from this Customer to other credit profiles.</li>
	 * <li>Remove the primary link from this customer id to its credit profile.</li>
	 * <li>Check if the credit profile above is the primary credit profile of other customers</li>
	 * <ol>
	 * <li>Yes,no action.</li>
	 * <li>No,</li>
	 * <ol>
	 * <li>expire this credit profile.</li>
	 * <li>Remove any secondary links from other Customers to this credit profile.</li>
	 * <li>expire business ownership if any .</li>
	 * </ol>
	 * </ol>
	 * </ul>
	 * 
     * @param customerId
     * @param auditInfo
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public void expireCreditProfileByCustomerId(long customerId, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
		if ( log.isDebugEnabled() ) {
		    log.debug( "WLNCreditProfileManagementService 2.0: Enter expireCreditProfileByCustomerId." );
		    log.debug( "passed in customer id is " + customerId);
		    log.debug( "passed in auditInfo is " + auditInfo.toString());
		    log.debug( "passed in user id is " + auditInfo.getUserId());
		    log.debug( "passed in source app id is " + auditInfo.getOriginatorApplicationId());
		}
	    String errorMsg;
		Long primaryCreditProfileId;
		CreditProfileDto[] creditProfileArray;
		
	    if ((auditInfo==null) || (auditInfo.getUserId()==null) || ("".equals(auditInfo.getUserId()))
	    	|| (auditInfo.getOriginatorApplicationId()==null) || ("".equals(auditInfo.getOriginatorApplicationId())))
	      {
	    	 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 userID and source application id in AuditInfo can NOT be NULL when expiring customer's credit profile").toString();
			 log.error(errorMsg);
			 throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.EMPTY_USERID_OR_APPID));
	        }
	    	 
		String userId=auditInfo.getUserId();
		String strSourceId=auditInfo.getOriginatorApplicationId();
		
		Integer custId=new Integer(String.valueOf(customerId));
		Integer sourceId;
		
		try {
			sourceId=new Integer(strSourceId);
		}catch (NumberFormatException numFormatEx) {
			log.error("Number Format Exception when parsing source application id "+ strSourceId);	
			//use unknown application id in case of format exception
			sourceId=new Integer(-99);
		}
		
		try{
		    //get primary credit profile id by customer id
		    primaryCreditProfileId=m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId(custId);
		
		    //get a list of credit profiles as the credit profile could be shared by multiple customers
		    creditProfileArray=m_creditProfileDao.getCreditProfileDtoByCreditProfileId(primaryCreditProfileId);
		    
		    //remove all links for the given customer including any secondary links and one primary link
		    m_creditProfileDao.unlinkAllCreditProfiles(custId, userId, sourceId);
		    
		    //check if other customers exist
		    //expire the credit profile only if it is isolate, not shared
		    if ((creditProfileArray.length==1) && (creditProfileArray[0].getCustomerId()==custId.intValue()))
		     {
			   
			    //expire this credit profile. 
		    	CreditProfile expiredCreditProfile=creditProfileArray[0].getCreditProfile();
		    	m_creditStatusDao.expireCreditStatus(expiredCreditProfile,userId);
		        expiredCreditProfile.setStatus(CreditProfile.OBSOLETE_STATUS_KEY);
		    	m_creditStatusDao.insertCreditStatus(expiredCreditProfile,userId, strSourceId);
                
		    	//remove any secondary links from other Customers to this credit profile.
		    	m_creditProfileDao.deleteSecondaryLinks(primaryCreditProfileId, custId, userId, sourceId);
		        
		    	/*
		    	//expire business ownership if any .
		    	List creditProfileMapList=new ArrayList();
		        CreditProfileMappingDto creditProfileMap;
		        
		        	creditProfileMapList=m_creditProfileMapDao.getBusinessOwnershipByProfileId(String.valueOf(primaryCreditProfileId));
		        	if (!creditProfileMapList.isEmpty())
		        	{
		        	  for (Iterator it = creditProfileMapList.iterator(); it.hasNext();) 
		        	  {
		        		  creditProfileMap = (CreditProfileMappingDto) it.next();
		        		  creditProfileMap.setUserId(userId);
		        	  	  
		        		  m_creditProfileMapDao.expireBusinessOwnership(creditProfileMap);
		        		  //creditProfileMap.setCreditProfileFromId(String.valueOf(primaryCreditProfileId));
		        		  //creditProfileMap.setDataSrcId(strSourceId);
		        	  	 
		           	   } //end loop
		             }//end if
		             */

		     }

		 
		}catch (ObjectNotFoundException notFoundEx) {
			 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0,Unable to expire the credit profile for customer [").append(customerId).append("]").toString();
			 log.error(errorMsg, notFoundEx);	 
			 throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.DB_EXCEPTION));
		 }catch (ConcurrencyConflictException ce){
			 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0,unable to expire the credit profile for customer [").append(customerId).append("]").toString();
			 log.error(errorMsg, ce);
			 throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.DB_EXCEPTION),ce);
         }catch( Throwable e ){
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0,System throws unchecked exceptions when expiring credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, e);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.UNKNOWN_EXCEPTION),e);
		}
		if ( log.isDebugEnabled() ) {
		    log.debug( "Exit expireCreditProfileByCustomerId." );
		}
    }
    
    /**
     * new search operation to support the customer search by credit profile data
     * @param creditIdentification
     * @param customerId
     * @param birthDate
     * @param auditInfo
     * @return
     *     returns java.util.List<com.telus.credit.wlnprflmgt.domain.CreditProfileSearchResult>
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public List<CreditProfileSearchResult> searchCreditProfileByCreditAndCustomerData(List<Long> customerId, Date birthDate, CreditIdentification creditIdentification, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
        String errorMsg;
       
    	//validate search criteria
        //validate birth date
        if (birthDate!=null){
        	
        	if (( birthDate.getYear() < 1900 ) || ( birthDate.getYear() > DataConvertUtil.getXMLGregorianCalendarNow().getYear() ) ||
        	   (birthDate.getMonth() > 12) || (birthDate.getMonth() < 1) ||
        	   (birthDate.getDay() > 31) || (birthDate.getDay() < 1))
        	   {
        		 birthDate=null;
        	    }
        }
        
    	//if only customer id list provided, and no birth date and no credit ids, then throws exception
    	if ((birthDate==null ) && (creditIdentification==null)){
    		errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 searchCreditProfileByCreditAndCustomerData: No credit data provided").toString();
    		log.error( errorMsg);
    		throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.NO_CREDIT_SEARCH_DATA));
    	}
    		
    	//if only birth date provided, no customer id list and no credit ids, then throws exception
    	if ((customerId.isEmpty()) && (creditIdentification==null))
    	{
    		errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 searchCreditProfileByCreditAndCustomerData: Can not search for birth date only").toString();
    		log.error( errorMsg);
    		throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.INSUFFICIENT_DATA));
    	}
    	
    	//set birth date 
    	//java.util.Date birthDt=DataConvertUtil.asDate(birthDate); 
   
    	//set credit id cards for each type
    	int idCount=0;
    	CreditIDCard sinIdCard=null;
    	CreditIDCard dlIdCard=null;
    	CreditIDCard hcIdCard=null;
    	CreditIDCard pspIdCard=null;
    	CreditIDCard prvIdCard=null;
    	
    	if (creditIdentification != null) {
	    	
	    	if (!"".equals(creditIdentification.getSin()) && (creditIdentification.getSin()!=null))
	   	    {   
	    		sinIdCard=new CreditIDCard();
	    		sinIdCard.setIdTypeCode(CreditIDCard.SIN_KEY);
	    		sinIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getSin()));
	    		idCount++;
	   	     }
	
	    	if (creditIdentification.getDriverLicense()!=null)
	    	{
	        	if (!"".equals(creditIdentification.getDriverLicense().getDriverLicenseNum()) 
	        	&& (creditIdentification.getDriverLicense().getDriverLicenseNum() !=null))
	        	 {
	        		dlIdCard=new CreditIDCard();
	        		dlIdCard.setIdTypeCode(CreditIDCard.DRIVERS_LICENSE_KEY);
	        		dlIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getDriverLicense().getDriverLicenseNum()));
	        		idCount++;
	        	   }
	    	}
	
	    	if (creditIdentification.getHealthCard()!=null)
	    	{
	        	if (!"".equals(creditIdentification.getHealthCard().getHealthCardNum()) 
	                	&& (creditIdentification.getHealthCard().getHealthCardNum() !=null))
	        	 {
	        		hcIdCard=new CreditIDCard();
	        		hcIdCard.setIdTypeCode(CreditIDCard.HEALTH_CARE_NUMBER_KEY);
	        		hcIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getHealthCard().getHealthCardNum()));
	        		idCount++;
	        	  }
	    	}
	
	    	if (creditIdentification.getPassport()!=null)
	    	{
	        	if (!"".equals(creditIdentification.getPassport().getPassportNum()) 
	                	&& (creditIdentification.getPassport().getPassportNum() !=null))
	        	 {
	        		pspIdCard=new CreditIDCard();
	        		pspIdCard.setIdTypeCode(CreditIDCard.PASSPORT_NUMBER_KEY);
	        		pspIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getPassport().getPassportNum()));
	        		idCount++;
	        	   }
	    	}
	
	    	if (creditIdentification.getProvincialIdCard()!=null)
	    	{
	        	if (!"".equals(creditIdentification.getProvincialIdCard().getProvincialIdNum()) 
	                	&& (creditIdentification.getProvincialIdCard().getProvincialIdNum() !=null))
	        	 {
	        		prvIdCard=new CreditIDCard();
	        		prvIdCard.setIdTypeCode(CreditIDCard.PROVINCIAL_ID_KEY);
	        		prvIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getProvincialIdCard().getProvincialIdNum()));
	        		idCount++; 
	        	  }
	    	}
    	}// end of if(creditIdentification != null)
    	
    	Integer numOfIds=new Integer(idCount);
    	CreditProfileSearchResult creditProfileSearchResult=null;
  	    List<CreditProfileSearchResult> creditProfileSearchResultList=new ArrayList<CreditProfileSearchResult>();
    	
  	    
       try{
    	  //call dao to search
    	  CreditProfileDto[] creditProfileDtos=m_creditProfileDao.getCreditProfileDtoByCreditProfileAndCustomerIds
    	  (customerId,birthDate,numOfIds,sinIdCard,dlIdCard,hcIdCard,pspIdCard,prvIdCard);
    	  
    	
    	  for (int i = 0; i < creditProfileDtos.length; i++) 
  	      {
  		     creditProfileSearchResult=new CreditProfileSearchResult();
  		     DataObjectUtil.copyToCreditProfileData(creditProfileDtos[i].getCreditProfile(), creditProfileSearchResult);
  		     if (String.valueOf(creditProfileDtos[i].getCustomerId())!=null) 
  		         creditProfileSearchResult.setCustomerID(new Long(String.valueOf(creditProfileDtos[i].getCustomerId()))); 
  		     if (creditProfileDtos[i].getCreditProfile().getCreditValue()!=null)
		       creditProfileSearchResult.setCreditValueCd(creditProfileDtos[i].getCreditProfile().getCreditValue().getCreditValueCode());
  		     creditProfileSearchResultList.add(creditProfileSearchResult);
  	       }
    	 }catch( Throwable e ){
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 searchCreditProfileByCreditAndCustomerData: System throws unchecked exceptions when searching credit profile by credit and customer data ").toString();
			log.error(errorMsg + e);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.UNKNOWN_EXCEPTION),e);
		 }

    	 return creditProfileSearchResultList;
    }

    /**
	 * <p>
	 * <b>Description: </b> See interface.
	 * </p>
	 * <p>
	 * <b>High Level Design: </b> Creates primary credit profile for a given
	 * customer.
	 * </p>
	 * <ul>
	 * <li>Assert that CreditProfile object does not contain empty child
	 * objects (CreditValue, CreditIDCard, CustomerGuarantor, CreditAddress). If
	 * it does - throw AssertionError.</li>
	 * <li>Assert that CreditProfile is valid. If it is not - throw
	 * AssertionError.</li>
	 * <li>Call {@link CreditProfileDao#getPrimaryCreditProfileIdByCustomerId}
	 * to check if this customer already has primary credit profile. If she does -
	 * throw DuplicateCreditProfileException</li>
	 * <li>If the credit profile that we are passing has customer guarantor
	 * information, we need to check whether this customer guarantor is in fact
	 * a valid customer. Check that a record exists for the guarantor using
	 * {@link CreditProfileDao#getPrimaryCreditProfileIdByCustomerId}</li>
	 * <li>If no credit value was sent in, call
	 * {@link CreditProfileBr#getDefaultCreditValue}to generate the default
	 * credit value then set it in the credit profile.</li>
	 * <li>Create new credit profile record in the Credit Management PDS. The
	 * call to {@link CreditProfileDao#insertCreditProfile}also creates records
	 * in associated tables such as credit value and credit Id card.</li>
	 * <li>Call {@link getCreditProfilesByIdCards}to get all
	 * Customer/CustomerProfile pairs with matching attibutes in one or more of
	 * the CreditId fields</li>
	 * <li>Use {@link CreditProfileDao#addCreditProfileToCustomer}to:</li>
	 * <ul>
	 * <li>Create the Primary link between the newly created credit profile and
	 * its customerId</li>
	 * <li>Link the newly created credit profile as a secondary profile to all
	 * matching customers found.</li>
	 * <li>Link the new customerId with its secondary credit profiles.</li>
	 * </ul>
	 * <li>If a customerGuarantor exists, call
	 * {@link CustomerGuarantorDao#insertGuarantor}. This step is not done
	 * during the insertion of the new customer profile, as the foreign key to
	 * the guarantor is the Guarantor's CreditProfileId, and not the newly
	 * created CreditProfileId.</li>
	 * <li>Get the latest snapshot of the newly created CreditProfile from the
	 * database with businessLastUpdateTimestamp and return it to the caller.
	 * </li>
	 * </ul>
     * 
     * 
     * @param creditProfileData
     * @param auditInfo
     * @return
     *     returns com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public ConsumerCreditProfile createCreditProfile(CreditProfileData creditProfileData, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
    	String errorMsg ;
    	
    	if ( log.isDebugEnabled() ) {
    		log.debug( "Enter createCreditProfile in WLNCreditProfileManagementService 2.0 " );
    	 }
    	
    	if ((creditProfileData==null) ||(creditProfileData.getCustomerID()==0) || ("".equals(creditProfileData.getCustomerID())))
    	{
    		errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 createCreditProfile: credit profile data or customer id can not be empty").toString();
    		log.error( errorMsg);
    		throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.EMPTY_CPDATA_OR_CUSTOMERID));
    	 }
    	
    	int customerId=DataConvertUtil.LongToInt(creditProfileData.getCustomerID());
    	
    	if ( log.isDebugEnabled() ) {
			log.debug( "createCreditProfile: customer id: " + customerId );
		}    
    	//convert to credit profile
		CreditProfile creditProfile=new CreditProfile();
		DataObjectUtil.copyToCreditProfile(creditProfileData, creditProfile);
		
    	if ( log.isDebugEnabled() ) {
			log.debug( "createCreditProfile: data has been copied from xsd to domain obj, domain is: " + creditProfile );
		} 
    	
    	ConsumerCreditProfile consumerCreditProfile=new ConsumerCreditProfile();
    	CreditWorthiness creditWorthiness=new CreditWorthiness();
    	consumerCreditProfile.setCustomerID(customerId);
    	
    	
		//try {
			// Assert valid creditProfile.
			//CreditProfileBr creditProfileBr = getCreditProfileBr();
            
			if ( m_creditProfileBr.hasEmptyChildObjects(creditProfile) ) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 createCreditProfile: CreditProfile has Empty Child Object.").toString();
				log.error( errorMsg );
				throw new PolicyException( errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.EMPTY_CP_CHILD) );
			}
			
			ValidationResult creditProfileBrValidationResult = m_creditProfileBr.validate(creditProfile);
			if ( !creditProfileBrValidationResult.isValid() ) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 createCreditProfile: CreditProfile Validation error: ").append(creditProfileBrValidationResult).toString();
				log.error( errorMsg);
				throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.CP_VALIDATION_ERROR));
			}

			// Make sure a CreditProfile doesn't already exist.
			Long custProfileId = getPrimaryCreditProfileIdByCustomerId(customerId);
			if (custProfileId != null) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 createCreditProfile: CreditProfile exist for customer :").append(customerId).toString();
				log.error( errorMsg);
				throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.CP_EXIST));
			}
			
				
			// Set default credit worthiness if null.
			if (creditProfile.getCreditValue() == null) {
				setCreditValue(creditProfile);
			}
			DataObjectUtil.copyToCreditWorthiness(creditProfile,creditWorthiness);
		 	creditWorthiness.setCustomerID(customerId);
			consumerCreditProfile.setCreditWorthiness(creditWorthiness);
			
			//RTCA 1.5 Credit Value Effective Date = system date
			Date creditValueEffectiveDate=DataConvertUtil.asDate(DataConvertUtil.getXMLGregorianCalendarNow());
			creditProfile.setCreditValueEffectiveDate(creditValueEffectiveDate);
	    	consumerCreditProfile.setCreditValueEffectiveDate(creditValueEffectiveDate);

			// If there is a guarantor, check that the CustomerId
			// and Credit Profile exists in the CreditProfile to Customer Mapping
			// table.
			// 
			CustomerGuarantor customerGuarantor = creditProfile.getCustomerGuarantor();
			if (customerGuarantor != null) {
				
		    	if ( log.isDebugEnabled() ) {
					log.debug( "createCreditProfile: GuarantorCustomerId : " + customerGuarantor.getGuarantorCustomerId() );
				}
				validateAndSetGuarantorsCreditProfileId(customerGuarantor,customerId);
				
		    	if ( log.isDebugEnabled() ) {
					log.debug( "createCreditProfile: GuarantorCreditProfileId : " + customerGuarantor.getGuarantorCreditProfileId() );
				}
			}

			Long creditProfileId = null;
			//validateAuditInfo
			validateAuditInfo(auditInfo);
			    	 
			// Get userId from audit info
			String userId = auditInfo.getUserId();

			// Get source application Id from audit info.
			String strSourceId = auditInfo.getOriginatorApplicationId();
			Integer sourceId;
			
			try {
				sourceId=new Integer(strSourceId);
			}catch (NumberFormatException numFormatEx) {
				log.error("Number Format Exception when parsing source application id "+ strSourceId);	
				//use unknown application id in case of format exception
				sourceId=new Integer(-99);
			}

			try {
				creditProfileId = m_creditProfileDao.insertCreditProfile(
						creditProfile, userId, sourceId);
			} catch (DuplicateKeyException e1) {
				errorMsg=new StringBuffer("Insertion of new credit profile for customer ")
				             .append(customerId)
				             .append("  failed. CreditProfile content:")
				             .append(creditProfile).toString();
				log.error(errorMsg, e1);
				throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg,WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_INSERT_CREDIT_PROFILE),e1);
			} catch (ObjectNotFoundException e2) {
				// Unable to retrieve lastUpdateTimestamp for the newly created
				// credit profile
				errorMsg=new StringBuffer("Failed to retrieve lastUpdateTimestamp for CreditProfile ").toString();
				log.error(errorMsg, e2);
				throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_RETRIEVE_LAST_UPDATE_TIMESTAMP),e2);
			}
			
			//RTCA 1.5  Persist Credit Attributes-Current Province of Residency and CREDIT_VALUE_EFFECTIVE_DATE
			List<CreditAttribute> creditAttributeList=new ArrayList<CreditAttribute>();
			DataObjectUtil.copyToCreditAttribtes(creditProfile, creditAttributeList);
			CreditAttribute creditAttribute=new CreditAttribute();
			try{
				 
				 if (!creditAttributeList.isEmpty())
			    	{
			    	  for (Iterator it = creditAttributeList.iterator(); it.hasNext();) 
			    	  {
			    		  creditAttribute = (CreditAttribute) it.next();
			    		  creditAttribute.setCreditProfileId(creditProfileId);
			    		  m_creditAttributeDao.insertCreditAttribute(creditAttribute, userId);
			    	  }
			    	}
			} catch (DuplicateKeyException de) {
				  errorMsg=new StringBuffer("Insertion of new credit attribute for customer ")
	             .append(customerId)
	             .append("  failed. creditAttribute content:")
	             .append(creditAttribute).toString();
	          log.error(errorMsg, de);
	          throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg,WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_CREATE_CREDIT_ATTRIBUTE),de);
			}

			try {
				// Set up the primary Customer To Credit Profile link (always one).
				m_creditProfileDao.addCreditProfileToCustomer(creditProfileId,
						customerId, CreditProfile.PRIMARY_KEY, userId, sourceId);
			} catch (DuplicateKeyException e1) {
				errorMsg=new StringBuffer("Failed to link customer ")
				                         .append(customerId)
				                         .append(" to his primary credit profile. CreditProfile content: ")
				                         .append(creditProfile).toString();
				log.error(errorMsg, e1);
				throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_CREATE_PRIMARY_LINK),e1);
			}

			// Set up any secondary Customer To Credit Profile link(s) -
			// (zero, one, or many).
			//
			CreditProfileDto[] matchingCreditProfilesByCreditIDCards = null;
			if (creditProfile.getCreditIDCards().length > 0) {
				matchingCreditProfilesByCreditIDCards = getCreditProfilesByIdCards(creditProfile.getCreditIDCards());
			}
						 
			if (matchingCreditProfilesByCreditIDCards != null) {
				createSecondaryLinks(creditProfileId, customerId,matchingCreditProfilesByCreditIDCards, userId, sourceId);
			}

			try {
				// Insert record into CustomerGuarantor table.
				//
				if (customerGuarantor != null) {
					customerGuarantor.setGuaranteedCustomerId(customerId);
					m_customerGuarantorDao.insertGuarantor(customerGuarantor,
							userId, sourceId);
				}
			} catch (DuplicateKeyException e2) {
				errorMsg=new StringBuffer("Failed to insert customer guarantor record for guarantee ")
				                         .append(customerId)
				                         .append(" . CreditProfile content: ")
				                         .append(creditProfile).toString();
				log.error(errorMsg, e2);
				throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_INSERT_GUARANTOR_RECORD),e2);
			}
			
			
			JSONObject jsonObj;
		try {
			jsonObj = new CreateJSONMsg().createJSONMessageForCreditIds(creditProfile, customerId,userId,"CREATE" );
			log.debug("UpdateCreditProfile: Updated Credit Id's: JSONMsg :"+jsonObj.toString());
		//	System.out.println("*******" + jsonObj.toString());
			creditPubSubConnector.publishMsgToPubSub(jsonObj.toString());
		//} catch (JSONException e) {
		} catch (Throwable e) {
			//String errMsg=new StringBuffer("Failed to create a JSON Message for ").append(customerId);
	//		System.out.println("******************"+e.getMessage());	                         
			e.printStackTrace();
			log.error("Error while publishing message to PUBSUB for customer ID: "+customerId,e);
		} 




		return consumerCreditProfile;
    }

    /**
	 * <p>
	 * <b>Description: </b> <br>
	 * In a situation when two or more customers share a same primary Credit
	 * Profile (CreditProfile1), this service unlink the input customer
	 * (Customer1) from CreditProfile1.
	 * 
	 * </p>
	 * <b>Pre-conditions: </b> <br>
	 * CreditProfile1 is active. <br>
	 * CreditProfile1 has been linked with more than one customer as their
	 * primary credit profile; Customer1 is one of those customers.
	 * <p>
	 * <b>High Level Design: </b>
	 * <ol>
	 * <li>Check if this customer has primary CreditProfile. Throw
	 * ObjectNotFoundException if primary CreditProfile is can not be found.
	 * </li>
	 * <li>Check if the primary CreditProfile of this customer is "Merged"
	 * CreditProfile. Throw CreditProfileMgtSvcException if it's not the case.
	 * </li>
	 * <li>Disconnect Customer1 and CreditProfile1
	 * <ul>
	 * 
	 * <li>Expire all primary and secondary links associated with Customer1, by
	 * calling {@link CreditProfileDao#unlinkAllCreditProfiles}</li>
	 * </ul>
	 * <li>Create new primary credit profile for Customer1.
	 * <ul>
	 * <li>Create a new "blank" credit profile (CreditProfile2), with credit
	 * value "N". Persist by calling
	 * {@link CreditProfileDao.insertCreditProfile}</li>
	 * <li>Link CreditProfile2 as primary credit profile of Customer1 by
	 * calling {@link CreditProfileDao#addCreditProfileToCustomer}</li>
	 * </ul>
	 * </li>
	 * <li>Consolidate guarantor information.
	 * <ul>
	 * <li>If Customer1 was guarantor customer, transfer (expire and create new
	 * one) those guarantor records to CreditProfile2. ie. create a
	 * CUSTOMER_GUARANTOR record with all data fields same except
	 * CREDIT_PROFILE_ID. This is achieved by calling
	 * {@link CustomerGuarantorDao#transferUnmergedGuarantor}.</li>
	 * </ul>
	 * </li>
	 * <li>Maintain credit profile mapping information.
	 * <ul>
	 * <li>Create record in credit profile mapping table to indicate that
	 * CreditProfile2 is "unmerged" from CreditProfile1 , by calling
	 * {@link CreditProfileDao#insertUnmergeMapping}</li>
	 * </ul>
	 * </li>
	 * <li>Synchronize credit value of CreditProfile2 into Enabler CM by
	 * calling
	 * {@link  com.telus.credit.adapter.EnablerCmServiceAdapter#updateCreditValue(int, String)}.
	 * (This has to be done last because it cannot be rolled back.)</li>
	 * </ol>
	 * <p>
	 * <b>Post-condition: </b> <br>
	 * All key fields of CreditProfile2 are blank. i.e. CreditProfile2 has no
	 * link to any CreditIdCard record.
	 * <p>
	 * 
	 * @param customerId
	 *
	/* RTCA RELEASE 1 CODE
	    public void unmergeCreditProfile(long customerId, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
    	
    	if ( log.isDebugEnabled() ) {
		    log.debug( "WLNCreditProfileManagementService 2.0: Enter unmergeCreditProfile." );
		    log.debug( "passed in customer id is " + customerId);
		    log.debug( "passed in auditInfo is " + auditInfo.toString());
		    log.debug( "passed in user id is " + auditInfo.getUserId());
		    log.debug( "passed in source app id is " + auditInfo.getOriginatorApplicationId());
		}
    	
    	String errorMsg;
    	
    	// Get source application Id from audit info.
		String strSourceId = auditInfo.getOriginatorApplicationId();
		Integer sourceId;
		
		try {
			sourceId=new Integer(strSourceId);
		}catch (NumberFormatException numFormatEx) {
			log.error("Number Format Exception when parsing source application id "+ strSourceId);	
			//use unknown application id in case of format exception
			sourceId=new Integer(-99);
		}
    	
    	if ((auditInfo==null) || (auditInfo.getUserId()==null) || ("".equals(auditInfo.getUserId()))
    	    	|| (sourceId==null) || ("".equals(sourceId))){
    	    	 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 userID and source application id in AuditInfo can NOT be NULL when unmerging customer's credit profile").toString();
    			 log.error(errorMsg);
    			 throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.EMPTY_USERID_OR_APPID));
    	}
    	
    	CreditProfile originalCreditProfile = null;
    	
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {

			// Get Primary credit profile
			originalCreditProfile = m_creditProfileDao.getCreditProfile(new Integer(String.valueOf(customerId)));
			
			// Check whether the CreditProfile for this customer in fact is a
			// "merged" Credit Profile.
			CreditProfileDto[] dtos = m_creditProfileDao.getCreditProfileDtoByCreditProfileId(originalCreditProfile.get_id());
			
			// CreditProfile is considered to be merged if there are more than one
			// customer
			// linked to it with 'PRIMARY' link.
			if (dtos.length < 2) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, Credit Profile " + originalCreditProfile.get_id() + " is not a 'Merged' Credit Profile").toString();
				log.error(errorMsg);	 
				throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.NOT_A_MERGED_CREDIT_PROFILE));
			}
		
			//1.1. Unlink all links of customer. 
			m_creditProfileDao.unlinkAllCreditProfiles(new Integer(String.valueOf(customerId)),
					auditInfo.getUserId(), sourceId);
			
			//2.1. Create new primary credit profile for C1. 
			CreditProfile newCreditProfile = new CreditProfile();

			setCreditValue(newCreditProfile);

			Long newCreditProfileId = m_creditProfileDao.insertCreditProfile(newCreditProfile, auditInfo.getUserId(), 
					sourceId);
			
			//2.2. Create primary link from customer to his new credit profile 
			m_creditProfileDao.addCreditProfileToCustomer(newCreditProfileId, new Integer(String.valueOf(customerId)), CreditProfile.PRIMARY_KEY, 
					auditInfo.getUserId(), sourceId);
			
			// 3.1. If C1 was guarantor customer, transfer (expire and create
			// new one) those guarantor records to P2.
			m_customerGuarantorDao.transferUnmergedGuarantor(new Integer(String.valueOf(customerId)), new Long(dtos[0].getCreditProfile().get_id()),
					new Long(newCreditProfile.get_id()), auditInfo.getUserId(), sourceId);
			
			// 4.1. Create record in credit profile mapping table to indicate
			// that P2 is "unmerged" from P1.
			m_creditProfileDao.insertUnmergeMapping(new Long(dtos[0].getCreditProfile().get_id()), new Long(newCreditProfile.get_id()), 
					auditInfo.getUserId(), sourceId);	
			
			//5.1. Synchronize credit value of P2 into Enabler CM. 
			//updateCreditValueInCustomerProfile( newCreditProfile, customerId ); will not be implemented in RTCA
			
			try {
			// new logic, call updateCreditValue from customerManagementService
			m_customerManagementServiceMediator.updateCreditValue( customerId, newCreditProfile.getCreditValue().getCreditValueCode(),
					auditInfo.getUserId(), auditInfo.getOriginatorApplicationId() );
			} catch (PolicyException e) {
				transactionManager.rollback(status);
				throw e;
			} catch (ServiceException e) {
				transactionManager.rollback(status);
				throw e;
			}
			
			// we need to commit before the next step
			transactionManager.commit(status);
			
			// last step was changed to call the updateCreditProfile of CreditProfileDataManagementService
			CreditProfileData creditProfileData = new CreditProfileData();
			creditProfileData.setMethodCd(UNMERGE_METHOD_CODE);
			creditProfileData.setCustomerID(customerId);
			creditProfileData.setCommentTxt(DEFAULT_UNMERGE_COMMENT);
			
			// auditInfo Timestamp is required when calling updateCreditProfile
			if (auditInfo.getTimestamp() == null) {
				//auditInfo.setTimestamp(DataConvertUtil.getXMLGregorianCalendarNow());
				auditInfo.setTimestamp(DataConvertUtil.asDate(DataConvertUtil.getXMLGregorianCalendarNow()));
			}
			try {
				m_crdProfileUpdater.updateCreditProfile(creditProfileData, auditInfo);
			} catch(CreditProfileUpdateException cpue) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, CreditProfileUpdateException when unmerging credit profile for customer [").append(customerId).append("]").toString();
				log.error(errorMsg, cpue);
			} catch(RuntimeException e) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, System throws runtime exceptions when unmerging credit profile for customer [").append(customerId).append("]").toString();
				log.error(errorMsg, e);
			}
		} catch (ObjectNotFoundException ex) {
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, unable to unmerge the credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, ex);	
			transactionManager.rollback(status);
			throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.DB_EXCEPTION));
		} catch (ConcurrencyConflictException ex) {
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, unable to unmerge the credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, ex);
			transactionManager.rollback(status);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.DB_EXCEPTION),ex);
		} catch (Throwable e) {
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, System throws unchecked exceptions when unmerging credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, e);
			transactionManager.rollback(status);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.UNKNOWN_EXCEPTION),e);
		}

    	
        return;
    }

    
    /**
     * 
     * @param customerId
     * @param auditInfo
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public void unmergeCreditProfile(long customerId, AuditInfo auditInfo)
        throws PolicyException, ServiceException
    {
    	
    	if ( log.isDebugEnabled() ) {
		    log.debug( "WLNCreditProfileManagementService 2.0: Enter unmergeCreditProfile." );
		    log.debug( "passed in customer id is " + customerId);
		    log.debug( "passed in auditInfo is " + auditInfo.toString());
		    log.debug( "passed in user id is " + auditInfo.getUserId());
		    log.debug( "passed in source app id is " + auditInfo.getOriginatorApplicationId());
		}
    	
    	String errorMsg;
    	
    	// Get source application Id from audit info.
		String strSourceId = auditInfo.getOriginatorApplicationId();
		Integer sourceId;
		
		try {
			sourceId=new Integer(strSourceId);
		}catch (NumberFormatException numFormatEx) {
			log.error("Number Format Exception when parsing source application id "+ strSourceId);	
			//use unknown application id in case of format exception
			sourceId=new Integer(-99);
			//QC27621, set application id in audit info for credit profile proxy service
			auditInfo.setOriginatorApplicationId(sourceId.toString());
		}
    	
    	if ((auditInfo==null) || (auditInfo.getUserId()==null) || ("".equals(auditInfo.getUserId()))
    	    	|| (sourceId==null) || ("".equals(sourceId))){
    	    	 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 userID and source application id in AuditInfo can NOT be NULL when unmerging customer's credit profile").toString();
    			 log.error(errorMsg);
    			 throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.EMPTY_USERID_OR_APPID));
    	}
    	
    	CreditProfile originalCreditProfile = null;
    	
    	DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		try {

			// Get Primary credit profile
			originalCreditProfile = m_creditProfileDao.getCreditProfile(new Integer(String.valueOf(customerId)));
			
			// Check whether the CreditProfile for this customer in fact is a
			// "merged" Credit Profile.
			CreditProfileDto[] dtos = m_creditProfileDao.getCreditProfileDtoByCreditProfileId(originalCreditProfile.get_id());
			
			// CreditProfile is considered to be merged if there are more than one
			// customer
			// linked to it with 'PRIMARY' link.
			if (dtos.length < 2) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, Credit Profile " + originalCreditProfile.get_id() + " is not a 'Merged' Credit Profile").toString();
				log.error(errorMsg);	 
				throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.NOT_A_MERGED_CREDIT_PROFILE));
			}
		
			//1.1. Unlink all links of customer. 
			m_creditProfileDao.unlinkAllCreditProfiles(new Integer(String.valueOf(customerId)),
					auditInfo.getUserId(), sourceId);
			
			//2.1. Create new primary credit profile for C1. 
			CreditProfile newCreditProfile = new CreditProfile();
			/*CreditValue creditValue = new CreditValue();
			creditValue.setCreditValueCode(WLNCreditProfileManagementServiceImpl.DEFAULT_CREDIT_VALUE_CODE);
			newCreditProfile.setCreditValue(creditValue);*/
			setCreditValue(newCreditProfile);

			Long newCreditProfileId = m_creditProfileDao.insertCreditProfile(newCreditProfile, auditInfo.getUserId(), 
					sourceId);
			
			//2.2. Create primary link from customer to his new credit profile 
			m_creditProfileDao.addCreditProfileToCustomer(newCreditProfileId, new Integer(String.valueOf(customerId)), CreditProfile.PRIMARY_KEY, 
					auditInfo.getUserId(), sourceId);
			
			// 3.1. If C1 was guarantor customer, transfer (expire and create
			// new one) those guarantor records to P2.
			m_customerGuarantorDao.transferUnmergedGuarantor(new Integer(String.valueOf(customerId)), new Long(dtos[0].getCreditProfile().get_id()),
					new Long(newCreditProfile.get_id()), auditInfo.getUserId(), sourceId);
			
			// 4.1. Create record in credit profile mapping table to indicate
			// that P2 is "unmerged" from P1.
			m_creditProfileDao.insertUnmergeMapping(new Long(dtos[0].getCreditProfile().get_id()), new Long(newCreditProfile.get_id()), 
					auditInfo.getUserId(), sourceId);	
			
			//5.1. Synchronize credit value of P2 into Enabler CM. 
			//updateCreditValueInCustomerProfile( newCreditProfile, customerId ); will not be implemented in RTCA
			
            //  RTCA 1.5, Removed customerManagementService call, it would be handled by wln credit profile mgmt proxy service		
			//  commit before the next step
			transactionManager.commit(status);
			try {
				 m_creditProfileMgmtProxyServiceMediator.performOverrideCreditWorthiness(customerId, auditInfo);
			} catch(PolicyException pe) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, PolicyException in unmergeCreditProfile when unmerging credit profile for customer [").append(customerId).append("]").toString();
				log.error(errorMsg, pe);
			} catch(ServiceException se) {
				errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, ServiceException in unmergeCreditProfile when unmerging credit profile for customer [").append(customerId).append("]").toString();
				log.error(errorMsg, se);
			}
			
		} catch (ObjectNotFoundException ex) {
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, unable to unmerge the credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, ex);	
			transactionManager.rollback(status);
			throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.DB_EXCEPTION));
		} catch (ConcurrencyConflictException ex) {
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, unable to unmerge the credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, ex);
			transactionManager.rollback(status);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.DB_EXCEPTION),ex);
		} catch (Throwable e) {
			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0, System throws unchecked exceptions when unmerging credit profile for customer [").append(customerId).append("]").toString();
			log.error(errorMsg, e);
			transactionManager.rollback(status);
			throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.UNKNOWN_EXCEPTION),e);
		}

    	
		if ( log.isDebugEnabled() ) 
		    log.debug( "WLNCreditProfileManagementService 2.0: Exit unmergeCreditProfile." );
    }
    
    
    /**
     * 
     * @param customerId
     * @param auditInfo
     * @return
     *     returns java.util.List<com.telus.credit.wlnprflmgt.domain.CreditProfileSearchResult>
     * @throws ServiceException
     * @throws PolicyException
     */
	@Override
    public List<CreditProfileSearchResult> searchLinkedCreditProfileList(long customerId, AuditInfo auditInfo)
            throws PolicyException, ServiceException
        {
    	   if ( log.isDebugEnabled() ) {
    	       log.debug( "Enter searchLinkedCreditProfileList." );
    	       log.debug( "passed in customer id is " + customerId);
    	    }
    	   
    	    int intCustomerId=new  Integer(String.valueOf(customerId));
    	    List<CreditProfileSearchResult> creditProfileSearchResultList=new ArrayList<CreditProfileSearchResult>();
    	    CreditProfileSearchResult creditProfileSearchResult=null;
    	   
    	    String errorMsg;
    	    CreditMgtCustomerID customerID = new CreditMgtCustomerID(intCustomerId);
		    CreditMgtCustomerIDs matchedCustomerIDs = new CreditMgtCustomerIDs();
		    matchedCustomerIDs.add(customerID);
		    
			// Find customer ids of matched customers.
			//
			findMatchingCustomerIds(matchedCustomerIDs);

			CreditProfile creditProfile = null;

			Integer[] cids = matchedCustomerIDs.getIds();
			
			for (int i = 0; i < cids.length; i++) {
			    creditProfileSearchResult=new CreditProfileSearchResult();
				try {
					// Find "primary" Credit Profile for each matched customer id.
					creditProfile = m_creditProfileDao.getCreditProfile(cids[i]);
				} catch (ObjectNotFoundException ex) {
					 errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 searchLinkedCreditProfileList: Failed to retrieve matched CreditProfile by Customer Id :").append(intCustomerId).toString();
					 log.error( errorMsg);
					 throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.MATCHED_CREDIT_PROFILE_IS_NOT_FOUND));
				}
				
				DataObjectUtil.copyToCreditProfileData(creditProfile, creditProfileSearchResult);
				creditProfileSearchResult.setCustomerID(cids[i]);
				if (creditProfile.getCreditValue()!=null)
				       creditProfileSearchResult.setCreditValueCd(creditProfile.getCreditValue().getCreditValueCode());
				creditProfileSearchResultList.add(creditProfileSearchResult);
			}
			if ( log.isDebugEnabled() ) {
			    log.debug( "Exit searchLinkedCreditProfileList." );
			}
			
			return creditProfileSearchResultList;	    
        }

	/**
	 * 
	 * <p>
	 * <b>Description </b> Ping Operation
	 * </p>
	 * 
	 * @param  
	 * @return 
	 */
	@Override
     public String ping() throws PolicyException, ServiceException
     {
	    if ( log.isDebugEnabled() ) {
		log.debug( "Enter ping: " );
	    }
	   
        return "WLNCreditProfileManagementService 2.0 has been shaken down successfully";

	 }
        
    	/**
    	 * 
    	 * <p>
    	 * <b>Description </b> Gets CreditProfiles that match given CreditProfile
    	 * (by id cards)
    	 * </p>
    	 * 
    	 * @param CreditIDCard[]
    	 * @return CreditProfileDto []
    	 */
    	private CreditProfileDto[] getCreditProfilesByIdCards(CreditIDCard[] idCards) 
        throws PolicyException
    	{
    		CreditProfileDto[] creditProfileDtos = null;
	    		if (idCards != null) 
	      		 {
	    			Set matchedCustomerIds = new HashSet();
	    			for (int i = 0; i < idCards.length; i++) {
	    				List customerIds = m_creditIDCardDao.getConsumerCustomerIdByMatchingCreditIdCard(idCards[i]);
	    				matchedCustomerIds.addAll(customerIds);
	    			}
	    			creditProfileDtos = getMatchingCustomerCreditProfilesByCustomerIds(matchedCustomerIds);
	    		 } 
	      		else 
	    		   {
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
    	 * @param customerIDs_Matched_by_CreditIdCard
    	 * @return CreditProfileDto []
    	 */
 
    	private CreditProfileDto[] getMatchingCustomerCreditProfilesByCustomerIds(Set customerIDs_Matched_by_CreditIdCard) throws PolicyException{
    		Set matchedCustomerIDsForAllCustomers = new HashSet();
    		for (Iterator i = customerIDs_Matched_by_CreditIdCard.iterator(); i.hasNext();) {
    			Integer cid = (Integer) i.next();
    			
    			CreditMgtCustomerID customerID = new CreditMgtCustomerID(cid.intValue());
    			CreditMgtCustomerIDs customerIDs_Matched_by_CreditProfileID = new CreditMgtCustomerIDs();

    			// Check whether this customer id is in the set
    			// if yes - skip the next 3 lines.
    			// 
    			if (!customerIDs_Matched_by_CreditProfileID.containsId(customerID.getId().intValue())) {
    				
    				customerIDs_Matched_by_CreditProfileID.add(customerID);
    				//find other customer with same crediprofile as customerID 
    				findMatchingCustomerIds(customerIDs_Matched_by_CreditProfileID);
    				
    				matchedCustomerIDsForAllCustomers.addAll(customerIDs_Matched_by_CreditProfileID);
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
    				creditProfile = m_creditProfileDao.getCreditProfile(customerId.getId());
    			} catch (ObjectNotFoundException ex) 
    			 {
    				StringBuffer errorMsg=new StringBuffer("Failed to retrieve matched CreditProfile by Customer Id [").append(customerId).append("]");
    				log.error(errorMsg.toString(), ex);	 
    				 //throw new PolicyException
    				 com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo=null;
    				 //faultInfo.setErrorCode(value);
    				 //faultInfo.setErrorMessage(value);
    				 //faultInfo.setMessageId(value);
    				 throw new PolicyException(errorMsg.toString(),faultInfo,ex);
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
    	 * <li>Finds "unprocessed" CustomerID objects in the CustomerIDs object.
    	 * </li>
    	 * <li>Calls {@link CreditProfileDao#getMatchingCustomerIds}for each
    	 * unprocessed CustomerID.</li>*
    	 * <li>Eliminates duplicate CustomerID objects.</li>
    	 * <li>Call itself until all matched customerIds are found.</li>
    	 * </ol>
    	 * 
    	 * @param customerIDs_Matched_by_CreditProfileID -
    	 *            encapsulates ids of matched customers.
    	 */
    	private void findMatchingCustomerIds(CreditMgtCustomerIDs customerIDs_Matched_by_CreditProfileID) {
    		Set set = new HashSet();
    		for (Iterator it = customerIDs_Matched_by_CreditProfileID.iterator(); it.hasNext();) {
    			CreditMgtCustomerID customerID = (CreditMgtCustomerID) it.next();
    			if (!customerID.isProcessed()) {
    				//get customerIDs_Matched_by_CreditProfileID
    				Integer[] customerIds = m_creditProfileDao.getMatchingCustomerIds(customerID.getId());
    				int i = 0;
    				while (i < customerIds.length) {
    					int cid = customerIds[i].intValue();
    					if (!customerIDs_Matched_by_CreditProfileID.containsId(cid)) {
    						CreditMgtCustomerID cust = new CreditMgtCustomerID(cid);
    						set.add(cust);
    					}
    					i++;
    				}
    				i = 0;
    				customerID.setProcessed(true);
    			}
    		}
    		customerIDs_Matched_by_CreditProfileID.addAll(set);
    		if (customerIDs_Matched_by_CreditProfileID.containsUnprocessedId()) {
    			findMatchingCustomerIds(customerIDs_Matched_by_CreditProfileID);
    		}
    	}
    	
    	/*public CreditProfileBr getCreditProfileBr() {
    		CreditProfileBr br = null;
    		if (m_creditProfileBr != null) {
    			br = m_creditProfileBr;
    		} else {
    			br = CreditProfileBr.getInstance();
    		}
    		return br;
    	}*/
    	
    	/**
		 * @param m_creditProfileBr the m_creditProfileBr to set
		 */
		public void setCreditProfileBr(CreditProfileBr m_creditProfileBr) {
			this.m_creditProfileBr = m_creditProfileBr;
		}

		/**
		 * @param m_creditIDCardBr the m_creditIDCardBr to set
		 */
		public void setCreditIDCardBr(CreditIDCardBr m_creditIDCardBr) {
			this.m_creditIDCardBr = m_creditIDCardBr;
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
    	 * @param guaranteed customer id
    	 * @throws InvalidGuarantorIdException
    	 *             if CreditProfile doesn't exists.
    	 */
    	private void validateAndSetGuarantorsCreditProfileId(CustomerGuarantor customerGuarantor, int guaranteedCustomerId)
    	throws PolicyException {
    		String errorMsg;
    		Long guarantorCreditProfileId = getPrimaryCreditProfileIdByCustomerId(customerGuarantor
    				.getGuarantorCustomerId());

    		if (guarantorCreditProfileId == null) {
    			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 createCreditProfile: Invalid Guarantor").toString();
				log.error( errorMsg);
				throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.INVALID_GUARANTOR));
    		}
    		
    		if (customerGuarantor.getGuarantorCustomerId()==guaranteedCustomerId){
    			errorMsg=new StringBuffer("WLNCreditProfileManagementService 2.0 createCreditProfile: Guarantor Customer can NOT be same as Guaranteed Customer ").toString();
				log.error( errorMsg);
				throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.GUARANTOR_SAME_AS_GUARANTEED_CUSTOMER));
    		}
    		
    		//set GuarantorCreditProfileId
    		customerGuarantor.setGuarantorCreditProfileId(guarantorCreditProfileId.longValue());
    	}
    	
    	/**
    	 * Validate AuditInfo.
    	 * 
    	 * @param auditInfo
    	 * @throws PolicyException
    	 *     
    	 */
    	private void validateAuditInfo(AuditInfo auditInfo)
    	throws PolicyException
       {
    		
    		 if ((auditInfo==null) || (auditInfo.getUserId()==null) || ("".equals(auditInfo.getUserId()))
 			    	|| (auditInfo.getOriginatorApplicationId()==null) || ("".equals(auditInfo.getOriginatorApplicationId())))
 			      {
 			    	 String errorMsg=new StringBuffer("userID and source application id in AuditInfo can NOT be NULL").toString();
 					 log.error(errorMsg);	 
 					throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.EMPTY_USERID_OR_APPID));
 			        }

    	}
    	
    	/**
    	 * If matched profiles found, Create the secondary credit profile links in
    	 * the CreditProfileToCustomer map table: 
    	 * 1. link the new CreditProfile with the matching customer(s) 
    	 * 2. link the new Customer with secondary CreditProfile(s)
    	 * 
    	 * @param newCreditProfileId
    	 * @param newCustomerId
    	 * @param matchedByIDCard_CreditProfiles
    	 */
    	
    	private void createSecondaryLinks(Long newCreditProfileId, int newCustomerId,
    			CreditProfileDto[] matchedByIDCard_CreditProfiles, String userId, Integer sourceId) 
    	throws ServiceException{

    		try {
    			// keep track of secondary credit profiles as must avoid
    			// creating duplicate secondary links to it in the case where
    			// we match a merged customer.
    			//
    			Set profileIdSet = new HashSet();
    			//1-for each matchedByIDCard_CreditProfiles Customer, make the new crediprofile as a secondary credirpofile/
    			//1-for the new customer, add each matchedByIDCard_CreditProfiles as a secondary credirpofile/    			
    			for (int i = 0; i < matchedByIDCard_CreditProfiles.length; i++) {
    				int matchedCustomerId = matchedByIDCard_CreditProfiles[i].getCustomerId();
    				CreditProfile secondaryCreditProfile = matchedByIDCard_CreditProfiles[i].getCreditProfile();
    				Long secondaryCreditProfileId = new Long(secondaryCreditProfile.get_id());

    				// link the updated CreditProfile to the matching customer(s).
    				//add newCreditProfileId as the secondary crediprofile for matchedCustomerID
    				m_creditProfileDao.addCreditProfileToCustomer(
    						newCreditProfileId,
    						matchedCustomerId, 
    						CreditProfile.SECONDARY_KEY, 
    						userId,sourceId);
/*    				
    		  		insert into CPROFL_CUSTOMER_MAP
    				newCreditProfileId
    				matchedCustomerId
    				CPROFL_CUST_MAP_TYP_CD="SEC"
*/
    				
    				
    				// link the new CustomerId to the matching credit profiles
    				// - if a merged customer matches, avoid creating two
    				// links from the primary customer to the single shared profile
    				//
    				if (!profileIdSet.contains(secondaryCreditProfileId)) {
    					m_creditProfileDao.addCreditProfileToCustomer(
    							secondaryCreditProfileId, newCustomerId,
    							CreditProfile.SECONDARY_KEY, userId, sourceId);
    					profileIdSet.add(secondaryCreditProfileId);
    				}

    			}
    		} catch (DuplicateKeyException e1) {
    			String errorMsg=new StringBuffer("Failed to create secondary links for customer:  ")
                                         .append(newCustomerId)
                                         .append(" credit profile ID: ")
                                         .append(newCreditProfileId).toString();
                log.error(errorMsg, e1);
                throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.FAILED_TO_CREATE_SECONDARY_LINKS),e1);
    		}
    	}
    	
    	/**
    	 * Creates a new credit worthiness with default values.
    	 * 
    	 * Credit value = R
    	 * Product Set Qualifications for R (SL='Y' all other = 'N')
    	 * Comment ="Default - Credit Assessment not complete"
    	 * 
    	 * @param creditProfile
    	 */
    	private void setCreditValue(CreditProfile creditProfile) {
    		
    		//Credit value code
    		CreditValue creditValue = new CreditValue();
    		creditValue.setCreditValueCode(DEFAULT_CREDIT_VALUE_CODE);
    		creditValue.setCreditValueDetailTypeCd(DEFAULT_CREDIT_VALUE_DETAIL_CODE);
    		//RTCA 1.5 
    		creditValue.setAssessmentMsgCd(DEFAULT_CREDIT_ASSESSMENT_MESSAGE_CODE);
    		creditValue.setDecisionCd(DEFAULT_TELUS_DECISION_CODE);
    		//UC
    		creditValue.setRiskLevelNum(DEFAULT_RISK_LEVEL_NUM);
    		
    		List<ProductCategory> productCategoryList=new ArrayList<ProductCategory>();
    		
    		// Default Approved Product Category Code
    		// TODO: the default Approved Product Category list code can be retrieved from refPDS
    		// when refPDS changes are implemented
    		List<String> defProductCategoryList = Arrays.asList("SLR","HSIC","TTV");
    		
    		//Product Set Qualifications
    		//33029, we should create only 1 entry for “SLR” = true. No need to create entries that have false value.
    		for (String prodCat : defProductCategoryList) 
    		{
    			if ("SLR".equals(prodCat))
    			 {
    			   ProductCategory productCategory=new ProductCategory();
    			   productCategory.setCreditApprovedProductCategoryCd(prodCat);
    			   productCategory.setProductQualificationIndicator(true);
    			   productCategoryList.add(productCategory);
    			  }			
    		}
    		
    		ProductCategoryQualification productCategoryQualification=new ProductCategoryQualification();
    		productCategoryQualification.setProductCategoryList(productCategoryList);
    		productCategoryQualification.setBoltOn(DEFAULT_PRODUCT_CATEGORY_BOLT_ON);
    		creditValue.setProductCatQualification(productCategoryQualification);
    		
    		creditProfile.setCreditValue(creditValue);
    		creditProfile.setComment(DEFAULT_CREDIT_ASSESSMENT_COMMENT);
    		
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
    	 * <b>Description </b> Sets CreditStatusDao.
    	 * </p>
    	 * 
    	 * @param CreditStatusDao
    	 *         
    	 */

    	public void setCreditStatusDao(CreditStatusDao creditStatusDao) {
    		m_creditStatusDao = creditStatusDao;
    	} 
    	
    	/**
    	 * <p>
    	 * <b>Description </b> Sets CustomerGuarantorDao.
    	 * </p>
    	 * 
    	 * @param CustomerGuarantorDao
    	 *         
    	 */

    	public void setCustomerGuarantorDao(CustomerGuarantorDao customerGuarantorDao) {
    		m_customerGuarantorDao = customerGuarantorDao;
    	}


    	/**
    	 * <p>
    	 * <b>Description </b> Sets CreditAttributeDao.
    	 * </p>
    	 * 
    	 * @param CreditAttributeDao
    	 *         
    	 */
    	public void setCreditAttributeDao(CreditAttributeDao creditAttributeDao) {
			m_creditAttributeDao = creditAttributeDao;
		}



		/**
    	 * <p>
    	 * <b>Description </b> Sets CreditProfileMgmtProxyServiceMediator.
    	 * </p>
    	 * 
    	 * @param CreditProfileMgmtProxyServiceMediator
    	 *         
    	 */
		public void setCreditProfileMgmtProxyServiceMediator(
				WLNCreditProfileMgmtProxyServiceIntermediator creditProfileMgmtProxyServiceMediator) {
			  m_creditProfileMgmtProxyServiceMediator = creditProfileMgmtProxyServiceMediator;
		}



		/**
    	 * <p>
    	 * <b>Description </b> Sets WebLogicJtaTransactionManager.
    	 * </p>
    	 * 
    	 * @param wlnCrdProfileUpdater
    	 *         
    	 */
		public void setTransactionManager(
				PlatformTransactionManager transactionManager) {
			this.transactionManager = transactionManager;
		}
 	

}
