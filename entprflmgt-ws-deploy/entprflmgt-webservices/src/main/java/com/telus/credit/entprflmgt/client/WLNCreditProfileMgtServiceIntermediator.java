package com.telus.credit.entprflmgt.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;
import com.telus.credit.wlnprflmgt.domain.enterprise.common.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilemanagementservice_2.WLNCreditProfileManagementServicePortType;

public class WLNCreditProfileMgtServiceIntermediator {
	private static final Log log = LogFactory.getLog(WLNCreditProfileMgtServiceIntermediator.class);
	private  WLNCreditProfileManagementServicePortType m_wlnCreditProfileMgtSvc;

	/**
	 * Constructor that takes external web-services interface.
	 */
	public WLNCreditProfileMgtServiceIntermediator(WLNCreditProfileManagementServicePortType wlnCreditProfileMgtSvc) {
		this.m_wlnCreditProfileMgtSvc = wlnCreditProfileMgtSvc;
	}

	public ConsumerCreditProfile getCreditProfileByCustomerId(
	        long customerId,
	        String userId ) throws GetCreditProfileInfoException
	{
	    AuditInfo auditInfo = new AuditInfo();
	    auditInfo.setUserId( userId );
	    try {
	        return m_wlnCreditProfileMgtSvc.getCreditProfileByCustomerId( customerId, auditInfo );
	    }
	    catch (PolicyException e) {
	        GetCreditProfileInfoException cie = new GetCreditProfileInfoException("Exception getting credit info, customer id: " + customerId , e);
            throw cie;
        } catch (ServiceException e) {
            GetCreditProfileInfoException cie = new GetCreditProfileInfoException("Exception getting credit info, customer id: " + customerId, e);
            throw cie;
        } catch (RuntimeException e) {
            log.error("Unknown exception getting credit info, customer id: " + customerId, e);
            throw e;
        }
	}
	

}
