package com.telus.credit.entprflmgt.asyncprocessing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.support.converter.SimpleMessageConverter;

import com.telus.framework.config.ConfigContext;
import com.telus.framework.security.SecurityContext;


/**
 * <p>
 * Helper class that deals with putting messages on JMS queues to update WLN/WLS credit profiles.
 */
public class JMSCreditProfileUpdateSender {
	private static final String APP_ID= "EntCrdPrflDataMgtService";
	private JmsTemplate m_jmsTemplate;
	private boolean m_injectEMHeader=true;
	private static String m_wlnCreditProfileMgmtHeader = null;

	/**
	 * Constructor
	 * @param jmsTemplate external dependency wired by Spring.
	 */
	public JMSCreditProfileUpdateSender(JmsTemplate jmsTemplate) {
		m_jmsTemplate = jmsTemplate;
		m_jmsTemplate.setMessageConverter(new SimpleMessageConverter());
	}

	/**
	 * Puts given JMS message payload on the queue to be processed by ESB for WIRELESS credit profile update.
	 * @param messagePayload string representing SOAP payload to be sent to WLSCreditProfileDataManagementService
	 * by ESB to update credit profile.
	 */
	public void sendCreditProfileInfoToWireless(final String messagePayload, HashMap<String, String> userInfo) {
		if (messagePayload == null) { throw new IllegalArgumentException("messagePayload argument cannot be null");}
		this.sendCrdProfileInfoToUpdate(messagePayload, CRD_PROFILE_UPDATE_DIRECTION.WIRELESS, userInfo);
	}

	/**
	 * Puts given JMS message payload on the queue to be processed by ESB for WIRELINE credit profile update.
	 * @param messagePayload string representing SOAP payload to be sent to WLNCreditProfileDataManagementService
	 * by ESB to update credit profile.
	 */
	public void sendCreditProfileInfoToWireline(final String messagePayload, HashMap<String, String> userInfo) {
		if (messagePayload == null) { throw new IllegalArgumentException("messagePayload argument cannot be null");}
		this.sendCrdProfileInfoToUpdate(messagePayload, CRD_PROFILE_UPDATE_DIRECTION.WIRELINE, userInfo);
	}

	
	private void sendCrdProfileInfoToUpdate(final String messagePayload, final CRD_PROFILE_UPDATE_DIRECTION direction, final HashMap<String, String> userInfo) {
		getJmsTemplate().convertAndSend((Object)messagePayload, new MessagePostProcessor() {
			public Message postProcessMessage(Message message) throws JMSException {
				message.setStringProperty(CRD_JMS_HEADERS.CRD_PROFILE_UPDATE_DIRECTION.name(), direction.name());
				if (  isInjectEMHeader() && direction == CRD_PROFILE_UPDATE_DIRECTION.WIRELINE ) { //&& (!isEmHeaderDisabled() )
					message.setStringProperty(CRD_JMS_HEADERS.CRD_PROFILE_ROUTING_HEADER.name(), getWLNCreditProfileDataMgmtEmHeader() );
				}
				message.setStringProperty(ARFN_HEADER_VALUES.ARFM_APPLICATION_ID.name(), getSystemSourceId());
				message.setStringProperty(ARFN_HEADER_VALUES.ARFM_RESOURCE_ID.name(), direction.name());
				
				// put user info data as jms headers
				if (userInfo != null){
					for (String key: userInfo.keySet()){
						message.setStringProperty(key, userInfo.get(key));
					}
				}
				
				return message;
			}
		});
	}
	
	private String getSystemSourceId() {
		Integer secId = SecurityContext.getSystemSourceId();
		return secId !=null? String.valueOf(secId): APP_ID;
	}


	private JmsTemplate getJmsTemplate() {
		return this.m_jmsTemplate;
	}
	
	private String getWLNCreditProfileDataMgmtEmHeader() {
		if ( m_wlnCreditProfileMgmtHeader == null ) {
			StringBuffer result = new StringBuffer("<emh:emHeader xmlns:emh=\"http://schemas.tmi.telus.com/Enterprise/BaseTypes/telus_em_header_v1\"><emh:routingRules><emh:transport>http</emh:transport>");
			String urlString=ConfigContext.getProperty(new String[] { "connections","servers","WirelineCreditProfileDataManagement"});
			URL url=null;
			try {
				url = new URL(urlString);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			
			String port="80";
			if ( url!=null && (url.getPort() != -1) ) {
				port = String.valueOf(url.getPort());
			}
			String envString=ConfigContext.getProperty(new String[] { "emHeader", "envString" });
			String wlNode=ConfigContext.getProperty(new String[] { "emHeader", "wlNode" });

			result.append("\n<emh:host>").append(url.getHost()).append("</emh:host>");
			result.append("\n<emh:port>").append(port).append("</emh:port>");
			result.append("\n<emh:uri/>");
			result.append("\n<emh:envString>").append(envString).append("</emh:envString>");
			result.append("\n</emh:routingRules><emh:containerInfo>");
			if ( wlNode == null || wlNode.trim().length() == 0 ) {
				result.append("\n<emh:wlNode/>");
			}
			else {
				result.append("\n<emh:wlNode>").append(wlNode).append("</emh:wlNode>");
			}
			result.append("\n</emh:containerInfo></emh:emHeader>");
			m_wlnCreditProfileMgmtHeader =  result.toString();
		}
		//System.out.println("EM Header: " + m_wlnCreditProfileMgmtHeader);
		return m_wlnCreditProfileMgmtHeader;
	}
	
	private boolean isEmHeaderDisabled() {
		return ConfigContext.getPropertyAsBoolean(new String[] { "emHeader", "disabled" });
	}

	/**
	 * @return the m_injectEMHeader
	 */
	public boolean isInjectEMHeader() {
		return m_injectEMHeader;
	}

	/**
	 * @param m_injectEMHeader the m_injectEMHeader to set
	 */
	public void setInjectEMHeader(boolean m_injectEMHeader) {
		this.m_injectEMHeader = m_injectEMHeader;
	}

}
