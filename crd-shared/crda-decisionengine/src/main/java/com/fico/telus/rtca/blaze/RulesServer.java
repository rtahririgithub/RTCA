//
// Blaze Advisor Server Deployment.
// Created by the Blaze Advisor Generate Deployment Wizard
//
// Copyright (1997-2011),Fair Isaac Corporation. All Rights Reserved.
// 
//

package com.fico.telus.rtca.blaze;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.blazesoft.server.config.NdServerConfig;
import com.blazesoft.server.deploy.NdStatelessServer;
import com.blazesoft.server.local.NdLocalServerException;
import com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.creditCommon.CreditBureauResult;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauRequest;
import com.fico.telus.blaze.depositCalculator.DepositRequest;
import com.fico.telus.blaze.depositCalculator.DepositResult;

/**
 *	This class implements a stateless server based on RTCA_RuleService
 *	It is a subclass of <code>com.blazesoft.server.deploy.NdStatelessServer</code>.
 */
public class RulesServer extends NdStatelessServer

{
    /*
     * Log object
     */
    private static final Log log = LogFactory.getLog(RulesServer.class);

    private String m_rtcaRuleServiceName = "RTCA_RuleService";

    private String m_simulatedBureauEntryPoint = null;
    
    private String m_creditAssessmentEntryPoint = null;

    private String m_exportAssessmentMessagesEntryPoint = null;

    private String m_exportErrorMessagesEntryPoint = null;

    private String m_exportFraudMessagesEntryPoint = null;
    
    private String m_calculateDepositEntryPoint = null;

    private String m_pingEntryPoint = null;
    
    private String m_ping = null;

	
    /**
     *	Constructor. Calls the constructor of the superclass.
     *	@param	serverConfig	Server configuration to use
     *
     *	@exception NdLocalServerException if construction of the server fails.
     */
    public RulesServer(NdServerConfig serverConfig)
	throws NdLocalServerException
    {
	super(serverConfig);
	log.info("Rules Server Created with: " + serverConfig);
    }

    /**
     *	Invokes a server through the entry point "getSimulatedCreditBureauResultEntryPoint"
     *	in the service "RTCA_RuleService".
     *
     *	@param	arg0		==> Enter a description here
     *	@return	com.fico.telus.blaze.webservice.CreditBureauResult	==> Enter a description of the return value
     */
    public CreditBureauResult getSimulatedCreditBureauResultEntryPoint(SimulatorCreditBureauRequest arg0)
	throws NdServerException, NdServiceException, NdServiceSessionException
    {
	// Build the argument list
	Object[] applicationArgs = new Object[1];
	applicationArgs[0] = arg0;
			
	
	// Invoke the service and returns its result, if any.
	CreditBureauResult retVal = (CreditBureauResult)invokeService(m_rtcaRuleServiceName, m_simulatedBureauEntryPoint, null, applicationArgs);
	return retVal;
    }
	/**
	 *	Invokes a server through the entry point "performCreditAssessmentEntryPoint"
	 *	in the service "RTCA_RuleService".
	 *
	 *	@param	arg0		==> Enter a description here
	 *	@return	com.fico.telus.blaze.webservice.CreditAssessmentResult	==> Enter a description of the return value
	 */
	public CreditAssessmentResult performCreditAssessmentEntryPoint(CreditAssessmentRequest arg0)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = arg0;
			

		// Invoke the service and returns its result, if any.
		CreditAssessmentResult retVal = (CreditAssessmentResult)invokeService(m_rtcaRuleServiceName, m_creditAssessmentEntryPoint, null, applicationArgs);
		return retVal;
	}
	
	/**
	 *	Invokes a server through the entry point "performCreditAssessmentEntryPoint"
	 *	in the service "RTCA_RuleService".
	 *
	 *	@param	arg0		==> Enter a description here
	 *	@return	com.fico.telus.blaze.webservice.CreditAssessmentResult	==> Enter a description of the return value
	 */
	public DepositResult calculateDepositEntryPoint(DepositRequest arg0)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = arg0;

		// Invoke the service and returns its result, if any.
		DepositResult retVal = (DepositResult)invokeService(m_rtcaRuleServiceName, m_calculateDepositEntryPoint, null, applicationArgs);
		return retVal;
	}
	
	/**
	 *	Invokes a server through the entry point "exportAssessmentMessageContents"
	 *	in the service "RTCA_RuleService".
	 *
	 *	
	 *	@return	String	==> Enter a description of the return value
	 */
	public String exportAssessmentMessageContents(String pDelimiter)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = pDelimiter;
		

		// Invoke the service and returns its result, if any.
		String retVal = (String)invokeService(m_rtcaRuleServiceName, m_exportAssessmentMessagesEntryPoint, null, applicationArgs);
		return retVal;
	}
	/**
	 *	Invokes a server through the entry point "exportErrorMessageContents"
	 *	in the service "RTCA_RuleService".
	 *
	 *	
	 *	@return	String	==> Enter a description of the return value
	 */
	public String exportErrorMessageContents(String pDelimiter)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = pDelimiter;
		

		// Invoke the service and returns its result, if any.
		String retVal = (String)invokeService(m_rtcaRuleServiceName, m_exportErrorMessagesEntryPoint, null, applicationArgs);
		return retVal;
	}
	/**
	 *	Invokes a server through the entry point "exportFraudMessageContents"
	 *	in the service "RTCA_RuleService".
	 *
	 *	
	 *	@return	String	==> Enter a description of the return value
	 */
	public String exportFraudMessageContents(String pDelimiter)
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = pDelimiter;

		// Invoke the service and returns its result, if any.
		String retVal = (String)invokeService(m_rtcaRuleServiceName, m_exportFraudMessagesEntryPoint, null, applicationArgs);
		return retVal;
	}
	
	/**
	 *	Invokes a server through the entry point "ping"
	 *	in the service "RTCA_RuleService".
	 *
	 *	Warms up the rule service
	 */
	public String ping()
			throws NdServerException, NdServiceException, NdServiceSessionException
	{
		// Build the argument list
		Object[] applicationArgs = new Object[0];

		// Invoke the service and returns its result, if any.
		
		String retVal = (String)invokeService(m_rtcaRuleServiceName, m_pingEntryPoint, null, applicationArgs);
		return retVal;		
	}

    /**
     * Get     m_rtcaRuleServiceName
     *
     * @return     m_rtcaRuleServiceName
     */
    public String getRtcaRuleServiceName() {
	return m_rtcaRuleServiceName;
    }

    /**
     * Set m_rtcaRuleServiceName
     *
     * @param m_rtcaRuleServiceName
     *
     */
    public void setRtcaRuleServiceName(String rtcaRuleServiceName) {
	m_rtcaRuleServiceName = rtcaRuleServiceName;
    }

    /**
     * Get m_simulatedBureauEntryPoint
     *
     * @return  m_simulatedBureauEntryPoint
     */
    public String getSimulatedBureauEntryPoint() {
	return m_simulatedBureauEntryPoint;
    }

    /**
     * Set m_simulatedBureauEntryPoint
     *
     * @param m_simulatedBureauEntryPoint
     *
     */
    public void setSimulatedBureauEntryPoint(String simulatedBureauEntryPoint) {
	m_simulatedBureauEntryPoint = simulatedBureauEntryPoint;
    }

    
    /**
     * Get m_creditAssessmentEntryPoint
     *
     * @return  m_creditAssessmentEntryPoint
     */
    public String getCreditAssessmentEntryPoint() {
	return m_creditAssessmentEntryPoint;
    }

    /**
     * Set m_creditAssessmentEntryPoint
     *
     * @param m_creditAssessmentEntryPoint
     *
     */
    public void setCreditAssessmentEntryPoint(String creditAssessmentEntryPoint) {
	m_creditAssessmentEntryPoint = creditAssessmentEntryPoint;
    }


    /**
     * Get m_exportAssessmentMessagesEntryPoint
     *
     * @return  m_exportAssessmentMessagesEntryPoint
     */
    public String getExportAssessmentMessagesEntryPoint() {
	return m_exportAssessmentMessagesEntryPoint;
    }

    /**
     * Set m_exportAssessmentMessagesEntryPoint
     *
     * @param m_exportAssessmentMessagesEntryPoint
     *
     */
    public void setExportAssessmentMessagesEntryPoint(String exportAssessmentMessagesEntryPoint) {
	m_exportAssessmentMessagesEntryPoint = exportAssessmentMessagesEntryPoint;
    }

    /**
     * Get m_exportErrorMessagesEntryPoint
     *
     * @return  m_exportErrorMessagesEntryPoint
     */
    public String getExportErrorMessagesEntryPoint() {
	return m_exportErrorMessagesEntryPoint;
    }

    /**
     * Set m_exportErrorMessagesEntryPoint
     *
     * @param m_exportErrorMessagesEntryPoint
     *
     */
    public void setExportErrorMessagesEntryPoint(String exportErrorMessagesEntryPoint) {
	m_exportErrorMessagesEntryPoint = exportErrorMessagesEntryPoint;
    }

    /**
     * Get m_exportFraudMessagesEntryPoint
     *
     * @return  m_exportFraudMessagesEntryPoint
     */
    public String getExportFraudMessagesEntryPoint() {
	return m_exportFraudMessagesEntryPoint;
    }

    /**
     * Set m_exportFraudMessagesEntryPoint
     *
     * @param m_exportFraudMessagesEntryPoint
     *
     */
    public void setExportFraudMessagesEntryPoint(String exportFraudMessagesEntryPoint) {
	m_exportFraudMessagesEntryPoint = exportFraudMessagesEntryPoint;
    }

   
	/**
	 * @return the m_calculateDepositEntryPoint
	 */
	public String getCalculateDepositEntryPoint() {
		return m_calculateDepositEntryPoint;
	}

	/**
	 * @param m_calculateDepositEntryPoint the m_calculateDepositEntryPoint to set
	 */
	public void setCalculateDepositEntryPoint(String calculateDepositEntryPoint) {
		this.m_calculateDepositEntryPoint = calculateDepositEntryPoint;
	}

	/**
     * Get m_pingEntryPoint
     *
     * @return  m_pingEntryPoint
     */
    public String getPingEntryPoint() {
	return m_pingEntryPoint;
    }

    /**
     * Set m_pingEntryPoint
     *
     * @param m_pingEntryPoint
     *
     */
    public void setPingEntryPoint(String pingEntryPoint) {
	m_pingEntryPoint = pingEntryPoint;
    }

}
