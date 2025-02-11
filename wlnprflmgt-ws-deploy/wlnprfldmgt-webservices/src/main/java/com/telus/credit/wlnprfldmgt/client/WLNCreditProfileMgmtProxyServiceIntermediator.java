/*
 *  Copyright (c) 2012 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */
package com.telus.credit.wlnprfldmgt.client;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.domain.CreditProfile;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementExceptionCodes;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementPolicyException;
import com.telus.credit.wlnprfldmgt.webservice.util.WLNCreditProfileDataManagementServiceException;
import com.telus.credit.wlnprflmgtpxy.domain.OverrideCreditWorthinessRequest;
import com.telus.credit.wlnprflmgtpxy.domain.enterprise.common.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType;
//import com.telus.credit.ent.domain.common.AuditInfo;
/**
 * <p><b>Description :</b> <code>EnterpriseCreditAssessmentServiceIntermediator</code> interfaces with Credit Assessment Service interface</p>
 * <p><b>Design Observations : </b></p>
 * <ul>
 * <li>None</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">3-Oct-2012</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">New Class</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Gurbirinder Sidhu
 * 
 * @stereotype Adapter Class
 * @version 1.0
 */
public class WLNCreditProfileMgmtProxyServiceIntermediator {
	
    
    private static final Log log = LogFactory.getLog(WLNCreditProfileMgmtProxyServiceIntermediator.class);
	
	/**
	 * Credit Assessment Service Port Type
	 */
	//private EnterpriseCreditAssessmentServicePortType m_enterpriseCreditAssessmentService;
    
    /**
	 * Credit Profile Mgmt Proxy Service Port Type
	 */
	private WLNCreditProfileManagementProxyServicePortType m_wlnCreditProfileMgmtProxyService;
	
	/**
	 * Mapping from credit profile method code to Credit assessment sub type code
	 */
	private Map m_methodCodeToCrdaSubTypeCodeMap;
	
	/**
	 * Default Override Credit Assessment Sub Type code
	 */
	private String m_defaultOverrideAssessmentSubType;
	
	/**
	 * Override Credit Assessment Type code
	 */
	private String m_overrideCreditAssessmentType;
	
	/**
	 * Audit Credit Assessment Type code
	 */
	private String m_auditCreditAssessmentType;
	
	/**
	 * Bureau Consent Sub Type code
	 */
	private String m_bureauConsentSubType;
	
	/**
	 * Manual Override Sub Type code
	 */
	private String m_manualOverrideSubType;
	
	/**
	 * Constructor that takes external web-services interface.
	 */
	public WLNCreditProfileMgmtProxyServiceIntermediator(WLNCreditProfileManagementProxyServicePortType wlnCreditProfileMgmtProxyService) {
	    m_wlnCreditProfileMgmtProxyService = wlnCreditProfileMgmtProxyService;
	}

	/**
	 * Perform Override Credit Assessment
	 *  
	 * @param customerId
	 * @param originalCreditProfile
	 * @param modifiedCreditProfile
	 * @param userId
	 * @param applicationId
	 * @return CreditWorthiness
	 * @throws WLNCreditProfileDataManagementServiceException
	 * @throws WLNCreditProfileDataManagementPolicyException   
	 *  
	 */
    /*public CreditValue performOverrideCreditAssessment( long customerId, CreditProfile originalCreditProfile, CreditProfile modifiedCreditProfile,
            String userId, String applicationId ) throws WLNCreditProfileDataManagementServiceException,
            WLNCreditProfileDataManagementPolicyException {
        CreditValue result = null;
        OverrideCreditAssessmentRequest overrideCreditAssessmentRequest = new OverrideCreditAssessmentRequest();
        overrideCreditAssessmentRequest.setCreditAssessmentTypeCd( m_overrideCreditAssessmentType );
        overrideCreditAssessmentRequest.setLineOfBusiness( "WIRELINE" );
        overrideCreditAssessmentRequest.setCreditAssessmentSubTypeCd( 
                    getCreditAssessmentSubTypeCdFromMethodCd(modifiedCreditProfile.getMethod() ) );
        
        overrideCreditAssessmentRequest.setCustomerID( customerId );
        //overrideCreditAssessmentRequest.setApplicationId( applicationId );
        if ( originalCreditProfile.getCreditValue() != null 
             && originalCreditProfile.getCreditValue().getCreditValueCode() != null ) {
            //overrideCreditAssessmentRequest.setCurrentCreditValueCd( originalCreditProfile.getCreditValue().getCreditValueCode() );
        }
        if ( originalCreditProfile.getCreditValue() != null
             && originalCreditProfile.getCreditValue().getFraudIndicatorCd() != null ) {
            //overrideCreditAssessmentRequest.setCurrentFraudIndictorCd( originalCreditProfile.getCreditValue().getFraudIndicatorCd() );
        }
        
        overrideCreditAssessmentRequest.setNewCreditValueCd( modifiedCreditProfile.getCreditValue().getCreditValueCode() );
        overrideCreditAssessmentRequest.setNewFraudIndictorCd( modifiedCreditProfile.getCreditValue().getFraudIndicatorCd() );
        //overrideCreditAssessmentRequest.setOverrideComment( modifiedCreditProfile.getComment() );
        
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setUserId( userId );
        auditInfo.setOriginatorApplicationId( applicationId );
        
        try {
            CreditAssessmentTransaction creditAssessmentTransaction = m_enterpriseCreditAssessmentService.performCreditAssessment( overrideCreditAssessmentRequest, auditInfo );
            CreditAssessmentResult creditAssessmentResult = creditAssessmentTransaction.getCreditDecisioningResult();
            result = new CreditValue();
            // we only set carId when creditAssessmentResult is null
            // this is if isOnlyFraudIndicatorModified is true
            if (creditAssessmentResult == null) {
            	result.setCarId( creditAssessmentTransaction.getCreditAssessmentID() );
            } else {
	            result.setAssessmentMsgCd( creditAssessmentResult.getAssessmentMessageCd() );
	            result.setCarId( creditAssessmentTransaction.getCreditAssessmentID() );
	            result.setCreditClassCd( creditAssessmentTransaction.getCreditClass() );
	            result.setCreditScoreNumber( creditAssessmentTransaction.getCreditScore() );
	            if ( creditAssessmentTransaction.getCreditDecision() != null ) {
	                result.setCreditDecisionCd(  creditAssessmentTransaction.getCreditDecision().getCreditDecisionCd() );
	                result.setCreditDecisionMsgTxt( creditAssessmentTransaction.getCreditDecision().getCreditDecisionMessage() );
	            }
	            result.setCreditValueCode( creditAssessmentResult.getCreditValueCd() );
	            //result.setDepositAmount( creditAssessmentResult.getDepositAmount() );
	            result.setFraudIndicatorCd( creditAssessmentResult.getFraudIndicatorCd() );
	            result.setFraudMessageCodeList( creditAssessmentResult.getFraudMessageCdList() );
	            if ( creditAssessmentResult.getProductCategoryQualification() != null ) {
	                ProductCategoryQualification productCategoryQualification = new ProductCategoryQualification();
	                productCategoryQualification.setBoltOn( creditAssessmentResult.getProductCategoryQualification().isBoltOnInd() );
	                List<ProductCategory> prodCategoryList = new ArrayList<ProductCategory>();
	                for ( com.telus.credit.domain.common.ProductCategory pc: creditAssessmentResult.getProductCategoryQualification().getProductCategoryList() ) {
	                    ProductCategory prodCategory = new ProductCategory();
	                    prodCategory.setCreditApprovedProductCategoryCd( pc.getCategoryCd() );
	                    prodCategory.setProductQualificationIndicator( pc.isQualified() );
	                    prodCategoryList.add( prodCategory );
	                }
	                productCategoryQualification.setProductCategoryList( prodCategoryList );
	                result.setProductCatQualification( productCategoryQualification );
	            }
	            result.setMethod( modifiedCreditProfile.getMethod() );
            }
            
        }
        catch ( PolicyException e ) {
            com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = e.getFaultInfo();
            String errorMessage = "PolicyException during performCreditAssessment: message: " + e.getMessage() + ", " + 
                ( faultInfo != null ?  "Fault Error Code: " + faultInfo.getErrorCode() + ", Fault Error Message: " + faultInfo.getErrorMessage()
                        : "" );
            log.error( errorMessage + e, e );

            throw new WLNCreditProfileDataManagementPolicyException( errorMessage,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_POLICY_EXCEPTION,
                    e );
        }
        catch ( ServiceException e ) {
            com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = e.getFaultInfo();
            String errorMessage = "ServiceException during performCreditAssessment: message: " + e.getMessage() + ", " + 
                ( faultInfo != null ?  "Fault Error Code: " + faultInfo.getErrorCode() + ", Fault Error Message: " + faultInfo.getErrorMessage()
                        : "" );
            log.error( errorMessage + e, e );

            throw new WLNCreditProfileDataManagementServiceException( errorMessage,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_SERVICE_EXCEPTION,
                    e );
        }
        catch ( Throwable e ) {
            log.error("Runtime Exception during performCreditAssessment: " + e, e );
            throw new WLNCreditProfileDataManagementServiceException( "Runtime Exception during performCreditAssessment: " + e,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_RUNTIME_EXCEPTION,
                    e );
        }
        return result;
    }*/

    
    /**
     * Retrieve Credit Assessment Sub Type Code from Credit Profile Method Code
     * @param method
     *  
     * @return   Credit Assessment Sub Type Code
     *  
     */
    /*private String getCreditAssessmentSubTypeCdFromMethodCd(String methodCode)
    {
        return ( (String) m_methodCodeToCrdaSubTypeCodeMap.get( methodCode ) == null 
                ? m_defaultOverrideAssessmentSubType 
                        : (String)m_methodCodeToCrdaSubTypeCodeMap.get( methodCode ) );
    }*/

   /* public void performAuditCreditAssessment( long customerId, CreditProfile originalCreditProfile, CreditProfile modifiedCreditProfile,
            String userId, String applicationId  ) throws WLNCreditProfileDataManagementServiceException,
            WLNCreditProfileDataManagementPolicyException  {
        OverrideCreditAssessmentRequest auditCreditAssessment = new OverrideCreditAssessmentRequest();
        auditCreditAssessment.setCreditAssessmentTypeCd( m_auditCreditAssessmentType );
        auditCreditAssessment.setLineOfBusiness("WIRELINE");
        auditCreditAssessment.setCreditAssessmentSubTypeCd( m_bureauConsentSubType );
        
        auditCreditAssessment.setCustomerID( customerId );
        //auditCreditAssessment.setApplicationId( applicationId );
        
        //auditCreditAssessment.setCurrentCreditCheckConsentCd( originalCreditProfile.getCreditCheckConsentCode() );
        auditCreditAssessment.setNewCreditCheckConsentCd( modifiedCreditProfile.getCreditCheckConsentCode() );
        
        //auditCreditAssessment.setOverrideComment( modifiedCreditProfile.getComment() );
        
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setUserId( userId );
        auditInfo.setOriginatorApplicationId( applicationId );
        try {
            m_enterpriseCreditAssessmentService.performCreditAssessment( auditCreditAssessment, auditInfo );
        }
        catch ( PolicyException e ) {
            com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = e.getFaultInfo();
            String errorMessage = "PolicyException during performAuditCreditAssessment: message: " + e.getMessage() + ", " + 
                ( faultInfo != null ?  "Fault Error Code: " + faultInfo.getErrorCode() + ", Fault Error Message: " + faultInfo.getErrorMessage()
                        : "" );
            log.error( errorMessage + e, e );

            throw new WLNCreditProfileDataManagementPolicyException( errorMessage,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_POLICY_EXCEPTION,
                    e );
        }
        catch ( ServiceException e ) {
            com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = e.getFaultInfo();
            String errorMessage = "ServiceException during performAuditCreditAssessment: message: " + e.getMessage() + ", " + 
                ( faultInfo != null ?  "Fault Error Code: " + faultInfo.getErrorCode() + ", Fault Error Message: " + faultInfo.getErrorMessage()
                        : "" );
            log.error( errorMessage + e, e );

            throw new WLNCreditProfileDataManagementServiceException( errorMessage,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_SERVICE_EXCEPTION,
                    e );
        }
        catch ( Throwable e ) {
            log.error("Runtime Exception during performAuditCreditAssessment: " + e, e );
            throw new WLNCreditProfileDataManagementServiceException( "Runtime Exception during performAuditCreditAssessment: " + e,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_RUNTIME_EXCEPTION,
                    e );
        }
    }*/
    
    // TODO: confirm if the parameters are ok
    public void performOverrideCreditWorthiness(long customerId, CreditProfile modifiedCreditProfile, String userId,
            String applicationId, String creditAssessmentType) 
    throws WLNCreditProfileDataManagementServiceException, WLNCreditProfileDataManagementPolicyException {
    	
    	OverrideCreditWorthinessRequest overrideCreditWorthinessRequest = new OverrideCreditWorthinessRequest();
    	
    	overrideCreditWorthinessRequest.setCreditAssessmentTypeCd(creditAssessmentType);
    	if (creditAssessmentType.equals(m_overrideCreditAssessmentType)) {
    		
    		overrideCreditWorthinessRequest.setNewCreditValueCd( modifiedCreditProfile.getCreditValue().getCreditValueCode() );
        	overrideCreditWorthinessRequest.setNewFraudIndictorCd( modifiedCreditProfile.getCreditValue().getFraudIndicatorCd() );
        	overrideCreditWorthinessRequest.setCreditAssessmentSubTypeCd(m_manualOverrideSubType);
    		
    	} else if(creditAssessmentType.equals(this.m_auditCreditAssessmentType)) {
    		
    		overrideCreditWorthinessRequest.setCreditAssessmentSubTypeCd(m_bureauConsentSubType);
    		overrideCreditWorthinessRequest.setNewCreditCheckConsentCd( modifiedCreditProfile.getCreditCheckConsentCode() );
    		
    	}
    	
    	overrideCreditWorthinessRequest.setApplicationID(applicationId);
    	overrideCreditWorthinessRequest.setLineOfBusiness("WIRELINE");
    	// TODO: confirm how to get the channelID property
    	//overrideCreditWorthinessRequest.setChannelID(channelId);
    	overrideCreditWorthinessRequest.setCustomerID(customerId);
    	overrideCreditWorthinessRequest.setCommentTxt( modifiedCreditProfile.getComment() );
    	
    	
    	AuditInfo auditInfo = new AuditInfo();
    	auditInfo.setUserId( userId );
        auditInfo.setOriginatorApplicationId( applicationId );
    	
    	try {
			m_wlnCreditProfileMgmtProxyService.overrideCreditWorthiness(overrideCreditWorthinessRequest, auditInfo);
		} catch (PolicyException e) {
			com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = e.getFaultInfo();
            String errorMessage = "PolicyException during performOverrideCreditWorthiness: message: " + e.getMessage() + ", " + 
                ( faultInfo != null ?  "Fault Error Code: " + faultInfo.getErrorCode() + ", Fault Error Message: " + faultInfo.getErrorMessage()
                        : "" );
            log.error( errorMessage + e, e );

            throw new WLNCreditProfileDataManagementPolicyException( errorMessage,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_POLICY_EXCEPTION,
                    e );
		} catch (ServiceException e) {
			com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = e.getFaultInfo();
            String errorMessage = "ServiceException during performOverrideCreditWorthiness: message: " + e.getMessage() + ", " + 
                ( faultInfo != null ?  "Fault Error Code: " + faultInfo.getErrorCode() + ", Fault Error Message: " + faultInfo.getErrorMessage()
                        : "" );
            log.error( errorMessage + e, e );

            throw new WLNCreditProfileDataManagementServiceException( errorMessage,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_SERVICE_EXCEPTION,
                    e );
		} catch ( Throwable e ) {
            log.error("Runtime Exception during performOverrideCreditWorthiness: " + e, e );
            throw new WLNCreditProfileDataManagementServiceException( "Runtime Exception during performOverrideCreditWorthiness: " + e,
                    WLNCreditProfileDataManagementExceptionCodes.CREDIT_ASSESSMENT_RUNTIME_EXCEPTION,
                    e );
        }
    	
    }

    /**
     * @return Returns the m_enterpriseCreditAssessmentService.
     */
    /*public EnterpriseCreditAssessmentServicePortType getEnterpriseCreditAssessmentService()
    {
        return m_enterpriseCreditAssessmentService;
    }*/

    /**
     * @param m_enterpriseCreditAssessmentService The m_enterpriseCreditAssessmentService to set.
     */
    /*public void setEnterpriseCreditAssessmentService(
            EnterpriseCreditAssessmentServicePortType m_enterpriseCreditAssessmentService)
    {
        this.m_enterpriseCreditAssessmentService = m_enterpriseCreditAssessmentService;
    }*/

    /**
	 * @return the m_wlnCreditProfileMgmtProxyService
	 */
	public WLNCreditProfileManagementProxyServicePortType getWlnCreditProfileMgmtProxyService() {
		return m_wlnCreditProfileMgmtProxyService;
	}

	/**
	 * @param m_wlnCreditProfileMgmtProxyService the m_wlnCreditProfileMgmtProxyService to set
	 */
	public void setWlnCreditProfileMgmtProxyService(
			WLNCreditProfileManagementProxyServicePortType m_wlnCreditProfileMgmtProxyService) {
		this.m_wlnCreditProfileMgmtProxyService = m_wlnCreditProfileMgmtProxyService;
	}

	/**
     * @return Returns the m_methodCodeToCrdaSubTypeCodeMap.
     */
    public Map getMethodCodeToCrdaSubTypeCodeMap()
    {
        return m_methodCodeToCrdaSubTypeCodeMap;
    }

    /**
     * @param m_methodCodeToCrdaSubTypeCodeMap The m_methodCodeToCrdaSubTypeCodeMap to set.
     */
    public void setMethodCodeToCrdaSubTypeCodeMap(
            Map methodCodeToCrdaSubTypeCodeMap)
    {
        this.m_methodCodeToCrdaSubTypeCodeMap = methodCodeToCrdaSubTypeCodeMap;
    }

    /**
     * @return Returns the m_defaultOverrideAssessmentSubType.
     */
    public String getDefaultOverrideAssessmentSubType()
    {
        return m_defaultOverrideAssessmentSubType;
    }

    /**
     * @param m_defaultOverrideAssessmentSubType The m_defaultOverrideAssessmentSubType to set.
     */
    public void setDefaultOverrideAssessmentSubType(
            String defaultOverrideAssessmentSubType)
    {
        this.m_defaultOverrideAssessmentSubType = defaultOverrideAssessmentSubType;
    }

    /**
     * @return Returns the m_overrideCreditAssessmentType.
     */
    public String getOverrideCreditAssessmentType()
    {
        return m_overrideCreditAssessmentType;
    }

    /**
     * @param m_overrideCreditAssessmentType The m_overrideCreditAssessmentType to set.
     */
    public void setOverrideCreditAssessmentType(
            String overrideCreditAssessmentType)
    {
        this.m_overrideCreditAssessmentType = overrideCreditAssessmentType;
    }

    /**
	 * @return the m_manualOverrideSubType
	 */
	public String getManualOverrideSubType() {
		return m_manualOverrideSubType;
	}

	/**
	 * @param m_manualOverrideSubType the m_manualOverrideSubType to set
	 */
	public void setManualOverrideSubType(String m_manualOverrideSubType) {
		this.m_manualOverrideSubType = m_manualOverrideSubType;
	}

	/**
     * @return Returns the m_auditCreditAssessmentType.
     */
    public String getAuditCreditAssessmentType()
    {
        return m_auditCreditAssessmentType;
    }

    /**
     * @param m_auditCreditAssessmentType The m_auditCreditAssessmentType to set.
     */
    public void setAuditCreditAssessmentType(String auditCreditAssessmentType)
    {
        this.m_auditCreditAssessmentType = auditCreditAssessmentType;
    }

    /**
     * @return Returns the m_bureauConsentSubType.
     */
    public String getBureauConsentSubType()
    {
        return m_bureauConsentSubType;
    }

    /**
     * @param m_bureauConsentSubType The m_bureauConsentSubType to set.
     */
    public void setBureauConsentSubType(String bureauConsentSubType)
    {
        this.m_bureauConsentSubType = bureauConsentSubType;
    }

}
