package com.telus.credit.entprflmgt.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementproxyservice_2.WLNCreditProfileManagementProxyServicePortType;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessRequest;
import com.telus.credit.wlnprflmgtpxy.domain.enterprise.common.AuditInfo;

public class WLNCreditProfileMgtProxyServiceIntermediator {
	private static final String WIRELINE = "WIRELINE";
	private static final String AUTO_ASSESSMENT = "AUTO_ASSESSMENT";
	private static final String FULL_ASSESSMENT = "FULL_ASSESSMENT";
	private static final Log log = LogFactory.getLog(WLNCreditProfileMgtProxyServiceIntermediator.class);
	public static final long ENABLER = 1001;
	public static final long KB = 130;
	
	private  WLNCreditProfileManagementProxyServicePortType m_wlnCreditProfileMgmtProxySvc;

	/**
	 * Constructor that takes external web-services interface.
	 */
	public WLNCreditProfileMgtProxyServiceIntermediator(WLNCreditProfileManagementProxyServicePortType wlnCreditProfileMgmtProxySvc) {
	    m_wlnCreditProfileMgmtProxySvc = wlnCreditProfileMgmtProxySvc;
	}

	public void assessCreditWorthiness( long customerId, String applicationId, String userId ) throws AssessCreditWorthinessException {
	    
	    AuditInfo auditInfo = new AuditInfo( );
	    auditInfo.setUserId( userId );
	    auditInfo.setOriginatorApplicationId( applicationId );
	    
	    try {
	    	AssessCreditWorthinessRequest creditWorthinessRequest = new AssessCreditWorthinessRequest();
	    	creditWorthinessRequest.setApplicationID(applicationId);
	    	creditWorthinessRequest.setCreditAssessmentTypeCd(FULL_ASSESSMENT);
	    	creditWorthinessRequest.setCreditAssessmentSubTypeCd(AUTO_ASSESSMENT);
	    	creditWorthinessRequest.setCustomerID(customerId);
	    	creditWorthinessRequest.setLineOfBusiness(WIRELINE);
	        m_wlnCreditProfileMgmtProxySvc.assessCreditWorthiness( creditWorthinessRequest, auditInfo );
        }
        catch (PolicyException e) {
            AssessCreditWorthinessException cie = new AssessCreditWorthinessException("Exception calling assess credit worthiness, customer id: " + customerId , e);
            throw cie;
        } catch (ServiceException e) {
            AssessCreditWorthinessException cie = new AssessCreditWorthinessException("Exception calling assess credit worthiness, customer id: " + customerId, e);
            throw cie;
        } catch (RuntimeException e) {
            log.error("Unknown exception calling assess credit worthiness, customer id: " + customerId, e);
            throw e;
        }
	}


}
