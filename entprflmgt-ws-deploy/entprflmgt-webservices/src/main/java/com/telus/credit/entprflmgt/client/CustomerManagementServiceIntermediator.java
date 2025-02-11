package com.telus.credit.entprflmgt.client;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.entprflmgt.domain.BillingAccountIdentification;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.customer.consmgt.domain.FullCustomer;
import com.telus.customer.consmgt.domain.GetFullCustomerInfo;
import com.telus.customer.consmgt.domain.GetFullCustomerInfoByBillingAccountNumber;
import com.telus.customer.consmgt.domain.GetFullCustomerInfoByBillingAccountNumberResponse;
import com.telus.customer.consmgt.domain.GetFullCustomerInfoResponse;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ConsumerCustomerManagementServicePortType;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException;

public class CustomerManagementServiceIntermediator {
	private static final Log log = LogFactory.getLog(CustomerManagementServiceIntermediator.class);
	public static final long ENABLER = 1001;
	public static final long KB = 130;
	
	private  ConsumerCustomerManagementServicePortType m_consumerCustomerMgtService;

	/**
	 * Constructor that takes external web-services interface.
	 */
	public CustomerManagementServiceIntermediator(ConsumerCustomerManagementServicePortType clientServiceInterface) {
		this.m_consumerCustomerMgtService = clientServiceInterface;
	}

	public CrdProfileUpdateFlags getCrdProfileUpdateFlags(Identification id) throws GetFullCustomerInfoException {
		// some basic validation first.
		if (id == null || !(id.getCustomerId()!=null ^ id.getBillingAccountIdentification()!=null)){
			throw new IllegalArgumentException("identification cannot be null and either customerId or billing acc# has to be specified.");
		}

		//Make a web-service call to ConsumerCustomerManagementService.getFullCusomerInfo();
		final FullCustomer customerInfo;	
		String logMessageDetails = "";
		try {

			if (id.getCustomerId() != null && id.getCustomerId() != 0){
				GetFullCustomerInfo parameters = new GetFullCustomerInfo();
				parameters.setCustomerId(id.getCustomerId());
				GetFullCustomerInfoResponse  getFullCustomerInfoResponse= m_consumerCustomerMgtService.getFullCustomerInfo(parameters);
				customerInfo=getFullCustomerInfoResponse.getFullCustomer();
				//getFullCustomerInfo(id.getCustomerId());
				logMessageDetails = "CustomerId: " + id.getCustomerId();
			}
			else {
				BillingAccountIdentification bai = id.getBillingAccountIdentification();
				GetFullCustomerInfoByBillingAccountNumber parameters = new GetFullCustomerInfoByBillingAccountNumber();
				parameters.setBillingAccountNumber(bai.getBillingAccountNumber());
				parameters.setBillingSystemId(bai.getBillingSystemId());
				GetFullCustomerInfoByBillingAccountNumberResponse getFullCustomerInfoByBillingAccountNumberResponse = null ;
				getFullCustomerInfoByBillingAccountNumberResponse= m_consumerCustomerMgtService.getFullCustomerInfoByBillingAccountNumber(parameters);
				customerInfo=getFullCustomerInfoByBillingAccountNumberResponse.getFullCustomer();
				//getFullCustomerInfoByBillingAccountNumber(String.valueOf(bai.getBillingAccountNumber()),bai.getBillingSystemId());
				logMessageDetails = "BAN: " + bai.getBillingAccountNumber();
			}
		} catch (PolicyException e) {
			GetFullCustomerInfoException cie = new GetFullCustomerInfoException("Exception getting customer info. " + logMessageDetails , e);
			throw cie;
		} catch (ServiceException e) {
			GetFullCustomerInfoException cie = new GetFullCustomerInfoException("Exception getting customer info. " + logMessageDetails, e);
			throw cie;
		} catch (RuntimeException e) {
			log.error("Unknown exception getting FullCustomerInfo from custODS. Details: " + logMessageDetails, e);
			throw e;
		}
		
		// identify what credit profile this particular customer has.
		boolean hasWLNAccount = false;
		boolean hasWLSAccount = false;
		int noOfWirelineAccounts = 0;
		int noOfWirelessAccounts = 0;
		Date wirelineBANCreationDate = null;
		String wirelineBANStatus=null;
		
		List<BillingAccount> billingAccounts =customerInfo.getBillingAccountList();
		// From the response by looking into BillingAccount list identify whether we need to update WLN/WLS
		Set<String> wirelessBANs = new HashSet<String>();
		for (BillingAccount bAccount: billingAccounts) {
			// Check billingMasterSourceId for "1001" = Enabler = Wireline  "130" = KB = Wireless
			long billingSourceId = bAccount.getBillingMasterSourceId();
			if (billingSourceId == ENABLER) {
			    noOfWirelineAccounts++;
				hasWLNAccount = true;
				wirelineBANStatus = bAccount.getStatusCode();
                wirelineBANCreationDate = bAccount.getStatusDate();
			}
			if (billingSourceId == KB){
			    noOfWirelessAccounts++;
				hasWLSAccount = true;
				wirelessBANs.add(bAccount.getBillingAccountNumber());
			}
			
		}
		
		// Find customer id in customerInfo in case of WIRELESS request to pass it back with update flags.
		final Long customerId = id.getCustomerId()!=null?id.getCustomerId():customerInfo.getCustomerId();
		return new CrdProfileUpdateFlags(hasWLNAccount, hasWLSAccount, wirelessBANs, customerId,
		        noOfWirelessAccounts,noOfWirelineAccounts,wirelineBANCreationDate,wirelineBANStatus);
	}

	public String ping() throws PolicyException, ServiceException {
		return m_consumerCustomerMgtService.ping(new Ping()).getVersion();
	}
}
