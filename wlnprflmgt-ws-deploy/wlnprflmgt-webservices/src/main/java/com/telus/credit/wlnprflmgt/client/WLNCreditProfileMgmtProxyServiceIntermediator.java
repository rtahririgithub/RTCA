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

package com.telus.credit.wlnprflmgt.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.wlnprflmgt.domain.enterprise.common.AuditInfo;
import com.telus.credit.wlnprflmgt.webservice.impl.ExceptionHandler;
import com.telus.credit.wlnprflmgt.webservice.impl.WLNCreditProfileManagementServiceExceptionCodes;
import com.telus.credit.wlnprflmgt.webservice.util.DataConvertUtil;
import com.telus.credit.wlnprflmgtpxy.domain.OverrideCreditWorthinessRequest;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.ServiceException;
	
public class WLNCreditProfileMgmtProxyServiceIntermediator {
		
	    
	    private static final Log log = LogFactory.getLog(WLNCreditProfileMgmtProxyServiceIntermediator.class);
	    
	    public final static String OVERRIDE_CREDIT_ASSESSMENT_TYPE="OVRD_ASSESSMENT";
	    
	    public final static String UNMERGED_CREDIT_ASSESSMENT_SUB_TYPE="UNMERGED";
	    
	    public final static String UNMERGED_COMMENT_TXT="Unmerged Credit Profile";
	    
	    public final static String CRD_APPLICATION_ID="CreditManagement";
	    
	    public final static String WIRELINE_BUSINESS="WIRELINE";
		
         
	    /**
		 * Credit Profile Mgmt Proxy Service Port Type
		 */
		private WLNCreditProfileManagementProxyServicePortType m_wlnCreditProfileMgmtProxyService;
		
		
		/**
		 * Constructor that takes external web-services interface.
		 */
		public WLNCreditProfileMgmtProxyServiceIntermediator(WLNCreditProfileManagementProxyServicePortType wlnCreditProfileMgmtProxyService) {
		    m_wlnCreditProfileMgmtProxyService = wlnCreditProfileMgmtProxyService;
		}
	    

	    public void performOverrideCreditWorthiness(long customerId, AuditInfo auditInfo ) 
	    		throws PolicyException,ServiceException
          {   	
	    	//SET UP REQUEST
	    	OverrideCreditWorthinessRequest overrideCreditWorthinessRequest = new OverrideCreditWorthinessRequest();
	    	overrideCreditWorthinessRequest.setCustomerID(customerId);
	    	overrideCreditWorthinessRequest.setCreditAssessmentTypeCd(OVERRIDE_CREDIT_ASSESSMENT_TYPE);
	    	overrideCreditWorthinessRequest.setCreditAssessmentSubTypeCd(UNMERGED_CREDIT_ASSESSMENT_SUB_TYPE);
		    overrideCreditWorthinessRequest.setCommentTxt(UNMERGED_COMMENT_TXT);
            overrideCreditWorthinessRequest.setApplicationID(auditInfo.getOriginatorApplicationId());
	    	overrideCreditWorthinessRequest.setLineOfBusiness(WIRELINE_BUSINESS);

	    	
	    	//SET UP AUDIT INFO
	    	com.telus.credit.wlnprflmgtpxy.domain.enterprise.common.AuditInfo proxyAuditInfo = 
	    			           new com.telus.credit.wlnprflmgtpxy.domain.enterprise.common.AuditInfo();
	    	proxyAuditInfo.setUserId( auditInfo.getUserId() );
	    	proxyAuditInfo.setUserTypeCode(auditInfo.getUserTypeCode());
	    	proxyAuditInfo.setSalesRepresentativeId(auditInfo.getSalesRepresentativeId());
	    	proxyAuditInfo.setChannelOrganizationId(auditInfo.getChannelOrganizationId());
	    	proxyAuditInfo.setOutletId(auditInfo.getOutletId());
	    	proxyAuditInfo.setOriginatorApplicationId( auditInfo.getOriginatorApplicationId() );
	    	proxyAuditInfo.setCorrelationId( auditInfo.getCorrelationId());
	    	proxyAuditInfo.setTimestamp(auditInfo.getTimestamp()==null ? DataConvertUtil.asDate(DataConvertUtil.getXMLGregorianCalendarNow()): auditInfo.getTimestamp());
	    	
	    	try {
				m_wlnCreditProfileMgmtProxyService.overrideCreditWorthiness(overrideCreditWorthinessRequest, proxyAuditInfo);
			} catch (com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException e) {
				String errorMsg=new StringBuffer("WLNCreditProfileMgmtProxyServiceIntermediator-performOverrideCreditWorthiness: Policy exception when calling WLNCreditProfileMgmtProxyService for customer: ")
				                  .append(customerId).append(" CreditAssessmentTypeCd is ").append(OVERRIDE_CREDIT_ASSESSMENT_TYPE)
				                  .append(" CreditAssessmentSubTypeCd is ").append(UNMERGED_CREDIT_ASSESSMENT_SUB_TYPE).toString();
				log.error(errorMsg);
	            throw new PolicyException(errorMsg, ExceptionHandler.createPolicyException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.WLN_CRD_MGMT_PROXY_POLICY_EXCEPTION));
			} catch (com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException e) {
				String errorMsg=new StringBuffer("WLNCreditProfileMgmtProxyServiceIntermediator-performOverrideCreditWorthiness: Service exception when calling WLNCreditProfileMgmtProxyService for customer  :")
				                   .append(customerId).append(" CreditAssessmentTypeCd is ").append(OVERRIDE_CREDIT_ASSESSMENT_TYPE)
                                   .append(" CreditAssessmentSubTypeCd is ").append(UNMERGED_CREDIT_ASSESSMENT_SUB_TYPE).toString();
				log.error(errorMsg + e);
				throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.WLN_CRD_MGMT_PROXY_SERVICE_EXCEPTION),e);
			} catch (Throwable e) {
				String errorMsg=new StringBuffer("WLNCreditProfileMgmtProxyServiceIntermediator-performOverrideCreditWorthiness: System throws unchecked exceptions when calling WLNCreditProfileMgmtProxyService for customer  :")
				                   .append(customerId).append(" CreditAssessmentTypeCd is ").append(OVERRIDE_CREDIT_ASSESSMENT_TYPE)
                                   .append(" CreditAssessmentSubTypeCd is ").append(UNMERGED_CREDIT_ASSESSMENT_SUB_TYPE).toString();
				log.error(errorMsg + e);
				throw new ServiceException(errorMsg,ExceptionHandler.createServiceException(errorMsg, WLNCreditProfileManagementServiceExceptionCodes.WLN_CRD_MGMT_PROXY_RUNTIME_EXCEPTION),e);
			}
	    }

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
				WLNCreditProfileManagementProxyServicePortType wlnCreditProfileMgmtProxyService) {
			  m_wlnCreditProfileMgmtProxyService = wlnCreditProfileMgmtProxyService;
		}


}
