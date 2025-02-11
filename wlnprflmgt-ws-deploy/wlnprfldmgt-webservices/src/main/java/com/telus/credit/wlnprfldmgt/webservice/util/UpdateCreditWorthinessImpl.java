package com.telus.credit.wlnprfldmgt.webservice.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.dao.CreditAttributeDao;
import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.dao.CreditValueDao;
import com.telus.credit.domain.CreditAttribute;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.CreditValueBr;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.wlnprfldmgt.domain.CreditAssessmentResult;
import com.telus.credit.wlnprfldmgt.webservice.util.ExceptionFactory;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementExceptionCodes;
import com.telus.credit.wlnprfldmgt.webservice.util.WlnPrflDMgtUtil;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.validation.ValidationResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.enterprisecreditassessmenttypes_v2.CreditAssessmentTransaction;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofiledatamanagementservice_1.ServiceException;

public class UpdateCreditWorthinessImpl {


	public final static String NO_CHANGE_REASON_CODE="NO_CHANGE";
	
	private static final Log log = LogFactory
			.getLog(UpdateCreditWorthinessImpl.class);
	
	private CreditProfileDao m_creditProfileDao;
	private CreditValueDao m_creditValueDao;
	private CreditAttributeDao m_creditAttributeDao;
	
	public void updateCreditWorthiness(
    		CreditAssessmentTransaction creditAssessmentTransactionResult,
            AuditInfo auditInfo)
    		throws PolicyException, ServiceException
    {
        String operationName = "updateCreditWorthiness";
        
        if (log.isDebugEnabled()) {
			log.debug("Enter updateCreditWorthiness.");
		}

        CreditValue creditValue = this.setCreditValue(creditAssessmentTransactionResult);
        List<CreditAttribute> listCreditAttribute = new ArrayList<CreditAttribute>();
        
        Long creditProfileId = null;
        CreditValue origCreditValue = null;
        try {
	        creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId(new Long(creditAssessmentTransactionResult.getCustomerID()).intValue());
			origCreditValue = m_creditValueDao.getCreditValue(creditProfileId);
			
			listCreditAttribute = this.setCreditAttributes(creditAssessmentTransactionResult, origCreditValue);
			
			CreditAssessmentResult creditAssessmentResult = creditAssessmentTransactionResult.getCreditDecisioningResult();
        
			if ( creditAssessmentResult != null &&
					creditAssessmentResult.getAssessmentResultReasonCd() != null &&
					creditAssessmentResult.getAssessmentResultReasonCd().equals(NO_CHANGE_REASON_CODE)) {
		        // we will not update credit value for NO CHANGE scenario
				if (log.isInfoEnabled())
					log.info("Update Credit Value will not be called for NO CHANGE scenario");
			} else {
				this.updateCreditValue(creditAssessmentTransactionResult.getCustomerID(), 
		        		creditValue, auditInfo );
			}
	        
        } catch (ObjectNotFoundException e) {
			throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditAssessmentTransactionResult.getCustomerID(), e );
		}
        
        try {
        	if (!listCreditAttribute.isEmpty())
        		this.updateCreditAttributes(listCreditAttribute, auditInfo.getUserId()); 
		} catch (ConcurrencyConflictException e) {
			throw ExceptionFactory.createServiceException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + creditAssessmentTransactionResult.getCustomerID(), e );
		}
        
		if (log.isDebugEnabled()) {
			log.debug("Exit updateCreditWorthiness.");
		}
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

    private void updateCreditValue (long customerId,
            CreditValue creditValue, AuditInfo auditInfo )
    		throws PolicyException, ServiceException
    {
    	String operationName = "updateCreditValue";
    	try {
    	    if ( customerId <= 0)
            {
                throw ExceptionFactory.createPolicyException( operationName, "Customer ID in the request can NOT be null",
                        WLNCreditProfileDataManagementExceptionCodes.CREDIT_VALUE_VALIDATION_EXCEPTION );
            }
        	String userId = auditInfo.getUserId();
        	
            if (userId == null || userId.isEmpty()) {
                throw ExceptionFactory.createPolicyException( operationName, "userId in the request can NOT be null",
                        WLNCreditProfileDataManagementExceptionCodes.CREDIT_VALUE_VALIDATION_EXCEPTION );
            }

            if (auditInfo.getOriginatorApplicationId() == null || auditInfo.getOriginatorApplicationId().isEmpty()) {
                throw ExceptionFactory.createPolicyException( operationName, "Application ID should not be null",
                        WLNCreditProfileDataManagementExceptionCodes.CREDIT_VALUE_VALIDATION_EXCEPTION );
            }
            Integer sourceId;
            try {
            	sourceId = Integer.valueOf(auditInfo.getOriginatorApplicationId());
            } catch (NumberFormatException e) {
            	log.error("Number Format Exception when parsing source application id "+ auditInfo.getOriginatorApplicationId());	
    			//use unknown application id in case of format exception
    			sourceId=new Integer(-99);
            }
            
        	CreditValueBr creditValueBr = CreditValueBr.getInstance();

        	ValidationResult rslt = creditValueBr.validate(creditValue);
            if ( !rslt.isValid())
            {
                throw ExceptionFactory.createPolicyException( operationName, "Credit Value is not valid, validation result: " + rslt.toString(),
                        WLNCreditProfileDataManagementExceptionCodes.CREDIT_VALUE_VALIDATION_EXCEPTION );
            }

        	m_creditValueDao.updateCreditValue(creditValue, userId, sourceId, new Long(customerId).intValue());
        	
        	String completionMessage = null;
        	
        	/*if ( creditValue.getCreditAssessmentSubTypeCd() != null && creditValue.getCreditAssessmentSubTypeCd().trim().length() > 0 &&
        			creditValue.getCreditAssessmentSubTypeCd().equals( ASMT_SUB_TYPE_AUTO_ASMT ) ) {
        		
	            completionMessage = WlnPrflDMgtUtil.getMessage("CRDA-TEXT-002");
	            
	        } else*/ 
        	// comment will be passed from proxy service regardless if system generated or user input
        	if (creditValue.getComment() != null && !creditValue.getComment().trim().isEmpty()) {
	        	
	        	completionMessage = creditValue.getComment();
	        	
	        }
        	
        	if ( completionMessage != null ) {
                
            	m_creditProfileDao.updateComment(creditValue.getCreditProfileId(), creditValue.getMethod(), completionMessage, userId, sourceId);
            }

            /*m_customerManagementServiceMediator.updateCreditValue( customerId,
                    creditValue.getCreditValueCode(), auditInfo.getUserId(), auditInfo.getOriginatorApplicationId() );
        }
        catch ( WLNCreditProfileDataManagementPolicyException e ) {
            throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );
        }
        catch ( WLNCreditProfileDataManagementServiceException e ) {
            throw ExceptionFactory.createServiceException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );*/
        } catch (ConcurrencyConflictException e) {
            throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );
		} catch (ObjectNotFoundException e) {
		    throw ExceptionFactory.createPolicyException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );
		}
		catch (RuntimeException e)
        {
            throw ExceptionFactory.createServiceException( operationName, "Exception: " + e.getClass().getName() + "for customer id: "
                    + customerId, e );
        }

    }
    
    private void copyToCreditProfile(List<CreditAttribute> lstCreditAttribute, CreditProfile creditProfile) {
		
		if (!lstCreditAttribute.isEmpty()) {
			for (CreditAttribute creditAttribute : lstCreditAttribute) {
				
				if (creditAttribute.getAttributeCode().equals(CreditAttribute.CREDIT_REPORT_INDICATOR))
					creditProfile.setCreditReportIndicator(creditAttribute.getAttributeValue().equalsIgnoreCase("YES") ? true : false);
	    	  	  
	    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.CREDIT_VALUE_EFFECTIVE_DATE))
	    			  creditProfile.setCreditValueEffectiveDate(WlnPrflDMgtUtil.convertStringToDate(creditAttribute.getAttributeValue()));  
	    		  
	    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.FIRST_ASSESSMENT_DATE))
	    			  creditProfile.setFirstAssessmentDate(WlnPrflDMgtUtil.convertStringToDate(creditAttribute.getAttributeValue()));
	    		  
	    		if (creditAttribute.getAttributeCode().equals(CreditAttribute.LATEST_ASSESSMENT_DATE))
	    			  creditProfile.setLatestAssessmentDate(WlnPrflDMgtUtil.convertStringToDate(creditAttribute.getAttributeValue()));
				
			}
		}
		
	}
    
    private CreditValue setCreditValue(
			CreditAssessmentTransaction creditAssessmentTransactionResult)
			throws ServiceException, PolicyException{
		
		String operationName = "setCreditValue";

		CreditValue creditValue = new CreditValue();

		try {
			
			Long carId = creditAssessmentTransactionResult.getCreditAssessmentID() == 0 ? null : 
				creditAssessmentTransactionResult.getCreditAssessmentID();
			creditValue.setCarId(carId);
			Timestamp creditAssessmentCreationdate = null;
			if (creditAssessmentTransactionResult.getCreditAssessmentDate()!=null) {
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				gregorianCalendar.setTime(creditAssessmentTransactionResult.getCreditAssessmentDate());
				try {
					creditAssessmentCreationdate =	WlnPrflDMgtUtil.convertXMLGregorianCalendarToTimestamp(
							DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
				} catch (DatatypeConfigurationException e) {
					throw ExceptionFactory.createPolicyException(operationName, "Exception: " + e.getClass().getName() + "for credit profile id: "
		                    + creditValue.getCreditProfileId(), e );
				}
			}
			creditValue.setCreditAssessmentCreationDate(creditAssessmentCreationdate);
			creditValue.setCreditAssessmentSubTypeCd(creditAssessmentTransactionResult.getCreditAssessmentSubTypeCd());
			creditValue.setCreditAssessmentTypeCd(creditAssessmentTransactionResult.getCreditAssessmentTypeCd());
			if ( creditAssessmentTransactionResult.isCreditBureauReportInd() != null)
				creditValue.setCreditBureauReportInd(creditAssessmentTransactionResult.isCreditBureauReportInd());
			creditValue.setCreditClassCd(creditAssessmentTransactionResult.getCreditClass());
	
			if (creditAssessmentTransactionResult.getCreditDecisionCd() != null) {
				creditValue.setCreditDecisionCd(creditAssessmentTransactionResult.getCreditDecisionCd().getCreditDecisionCd());
				creditValue.setCreditDecisionMsgTxt(creditAssessmentTransactionResult.getCreditDecisionCd().getCreditDecisionMessage());
			}
			creditValue.setCreditScoreNumber(creditAssessmentTransactionResult.getCreditScoreCd());
			
			if (creditAssessmentTransactionResult.getCreditDecisioningResult() != null) {
				creditValue.setCreditValueCode(creditAssessmentTransactionResult.getCreditDecisioningResult().getCreditValueCd());
				creditValue.setAssessmentMsgCd(creditAssessmentTransactionResult.getCreditDecisioningResult().getAssessmentMessageCd());
				if ( creditAssessmentTransactionResult.getCreditDecisioningResult().getDepositAmt()!=null) {
					creditValue.setDepositAmount(Double.valueOf( creditAssessmentTransactionResult.getCreditDecisioningResult().getDepositAmt().doubleValue() ) );
				}
				creditValue.setFraudIndicatorCd(creditAssessmentTransactionResult.getCreditDecisioningResult().getFraudIndicatorCd());
				creditValue.setFraudMessageCodeList(creditAssessmentTransactionResult.getCreditDecisioningResult().getFraudMessageCdList());
		
				ProductCategoryQualification prodCategoryQual = 
					WlnPrflDMgtUtil.convertProductCategoryQualification(
							creditAssessmentTransactionResult.getCreditDecisioningResult().getProductCategoryQualification());
				creditValue.setProductCatQualification(prodCategoryQual);
				
			    creditValue.setDecisionCd(creditAssessmentTransactionResult.getCreditDecisioningResult().getDecisionCd());
			    //set the riskLevelNum, introduced in Unified Credit for FR895107
			    creditValue.setRiskLevelNum(creditAssessmentTransactionResult.getCreditDecisioningResult().getCreditRiskLevel());
			}
			
			creditValue.setComment(creditAssessmentTransactionResult.getCommentTxt());
		} catch (RuntimeException e) {
			
			throw ExceptionFactory.createServiceException( operationName, "Exception: " + e.getClass().getName() + "for credit profile id: "
                    + creditValue.getCreditProfileId(), e );
		}

		return creditValue;
	}
    
    /**
	 * @param creditAssessmentTransactionResult
	 * @param creditValue
     * @throws PolicyException  
	 * 
	 */
	private List<CreditAttribute> setCreditAttributes (CreditAssessmentTransaction creditAssessmentTransactionResult, 
			CreditValue creditValue) throws PolicyException 
	{
		String operationName = "setCreditAttributes";
		
		// the list of credit attributes that is returned
		List<CreditAttribute> lstCreditAttribute = new ArrayList<CreditAttribute>();
		
		CreditAttribute creditAttribute = null;
		CreditProfile creditProfile = new CreditProfile();
		
		creditProfile.set_id(creditValue.getCreditProfileId());
		creditProfile.setCreditValue(creditValue);
		
		try {			
			// the list of credit attributes that will be put in credit profile
			List<CreditAttribute> creditAttributeList = m_creditAttributeDao.getCreditAttributes(creditProfile.get_id());
			
			this.copyToCreditProfile(creditAttributeList, creditProfile);
			
		} catch (ObjectNotFoundException e) {
			log.info("There are no attributes for Customer ID : " + creditAssessmentTransactionResult.getCustomerID());
		}
		

		if (!creditProfile.isCreditReportIndicator() && creditAssessmentTransactionResult.isCreditBureauReportInd() != null 
				&& creditAssessmentTransactionResult.isCreditBureauReportInd()) {
			if (log.isDebugEnabled())
				log.debug("New Credit Report Indicator: " + creditAssessmentTransactionResult.isCreditBureauReportInd().toString());
			creditAttribute = new CreditAttribute();
			creditAttribute.setCreditProfileId(creditProfile.get_id());
			creditAttribute.setAttributeCode(CreditAttribute.CREDIT_REPORT_INDICATOR);
			creditAttribute.setAttributeValue(creditAssessmentTransactionResult.isCreditBureauReportInd() ? "Y" : "N");
			lstCreditAttribute.add(creditAttribute);
		}
			
		if (creditAssessmentTransactionResult.getCreditAssessmentDate() != null ) {
			if (creditProfile.getFirstAssessmentDate() == null){
				creditAttribute = new CreditAttribute();
				creditAttribute.setCreditProfileId(creditProfile.get_id());
				creditAttribute.setAttributeCode(CreditAttribute.FIRST_ASSESSMENT_DATE);
				if (log.isDebugEnabled())
					log.debug("First Assessment Date : " + 
							WlnPrflDMgtUtil.convertDateToString(creditAssessmentTransactionResult.getCreditAssessmentDate()));
				creditAttribute.setAttributeValue(WlnPrflDMgtUtil.convertDateToString(creditAssessmentTransactionResult.getCreditAssessmentDate()));
				lstCreditAttribute.add(creditAttribute);
			}	
			
			if (creditAssessmentTransactionResult.getCreditDecisioningResult() == null ||
					creditAssessmentTransactionResult.getCreditDecisioningResult().getCreditValueCd() == null) {
				throw ExceptionFactory.createPolicyException( operationName, "Credit Value Code in the request can NOT be null", 
						WLNCreditProfileDataManagementExceptionCodes.CREDIT_VALUE_VALIDATION_EXCEPTION);
			}

			if (!creditProfile.getCreditValue().getCreditValueCode().trim().equals(
					creditAssessmentTransactionResult.getCreditDecisioningResult().getCreditValueCd().trim())) {
				creditAttribute = new CreditAttribute();
				creditAttribute.setCreditProfileId(creditProfile.get_id());
				creditAttribute.setAttributeCode(CreditAttribute.CREDIT_VALUE_EFFECTIVE_DATE);
				creditAttribute.setAttributeValue(WlnPrflDMgtUtil.convertDateToString(new Date()));
				lstCreditAttribute.add(creditAttribute);
			}
				
			creditAttribute = new CreditAttribute();
			creditAttribute.setCreditProfileId(creditProfile.get_id());
			creditAttribute.setAttributeCode(CreditAttribute.LATEST_ASSESSMENT_DATE);
			creditAttribute.setAttributeValue(WlnPrflDMgtUtil.convertDateToString(creditAssessmentTransactionResult.getCreditAssessmentDate()));
			lstCreditAttribute.add(creditAttribute);
		}
		
		return lstCreditAttribute;
		
	}

    /**
	 * <p>
	 * <b>Description </b> Sets CreditValueDao.
	 * </p>
	 * @param m_creditValueDao the m_creditValueDao to set
	 */
	public void setCreditValueDao(CreditValueDao m_creditValueDao) {
		this.m_creditValueDao = m_creditValueDao;
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
	 * <b>Description </b> Sets CreditAttributeDao.
	 * </p>
	 * @param m_creditAttributeDao the m_creditAttributeDao to set
	 */
	public void setCreditAttributeDao(CreditAttributeDao m_creditAttributeDao) {
		this.m_creditAttributeDao = m_creditAttributeDao;
	}
	
	public CreditValueDao getCreditValueDao( ) {
		return m_creditValueDao;
	}
	
	public CreditProfileDao getCreditProfileDao( ) {
		return m_creditProfileDao;
	}
	
	public CreditAttributeDao getCreditAttributeDao( ) {
		return m_creditAttributeDao;
	}
}
