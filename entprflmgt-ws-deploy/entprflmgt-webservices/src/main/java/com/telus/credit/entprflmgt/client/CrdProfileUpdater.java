package com.telus.credit.entprflmgt.client;

import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.util.XmlUtils;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;

/**
 *<p>
 * Abstract class that specifies interface for the objects responsible for updating Consumer credit profile in 
 * WIRELESS and WIRELINE system using external web-services.
 * 
 * <p> This class mainly deals with {@link com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo} as an input that will be transformed to the format 
 * understandable by the external web-service.
 * 
 * @author Danil Glinenko
 *
 */
public abstract class CrdProfileUpdater {
	
	/**
	 * To be implemented in subclasses to send actual request to update credit profile to external web-service.
	 * @param consumerCreditProfileInfo used as source of data to be sent to the external web-service to update credit profile.
	 * @throws CreditProfileUpdateException if the operation cannot be performed for various reasons.
	 */
	public abstract void updateCreditProfile(ConsumerCreditProfileInfo consumerCreditProfileInfo, 
			AuditInfo auditInfo)
	throws CreditProfileUpdateException;
	
	/**
	 * <p>
	 * Builds the target web-service specific request object and return it.
	 * This needs to be implemented in subclasses and contain web-service specific code.
	 * @param consumerCreditProfileInfo the source data to be used to build web-service request object.
	 * @return target web-service request object.
	 */
	public abstract Object buildServiceRequest(ConsumerCreditProfileInfo consumerCreditProfileInfo, 
			AuditInfo auditInfo);
	
	/**
     * <p>
     * Builds the target web-service specific async request object and return it.
     * This needs to be implemented in subclasses and contain web-service specific code.
     * @param consumerCreditProfileInfo the source data to be used to build web-service request object.
     * @return target web-service request object.
     */
    public abstract Object buildAsyncServiceRequest(ConsumerCreditProfileInfo consumerCreditProfileInfo, 
            AuditInfo auditInfo);
		
	
	/**
	 * <p>
	 * Converts given ConsumerCreditProfileInfo object to the XML payload that target web-service understands.
	 * This method is used to convert given input and then put it in the JMS queue for async processing.
	 * 
	 * @param consumerCreditProfileInfo object to convert to the target web-service payload.
	 * @return xml as String that can be sent and received by the target web-service. 
	 */
	public String convertToServicePayload(ConsumerCreditProfileInfo consumerCreditProfileInfo, AuditInfo auditInfo) {
		if (consumerCreditProfileInfo == null) { 
			throw new IllegalArgumentException("consumerCreditProfileInfo argument cannot be null.");
		}
		Object request = this.buildAsyncServiceRequest(consumerCreditProfileInfo, auditInfo);
		// get the xml representation of the service payload 
		return XmlUtils.convertToXml(request);
	}
		

}
