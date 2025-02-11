/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.entprflmgt.util;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.entprflmgt.client.CrdProfileUpdateFlags;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.wlnprflmgt.domain.ConsumerCreditProfile;

public class CreditAssessmentAnalyzer
{
    private static final Log log = LogFactory.getLog(CreditAssessmentAnalyzer.class);
    
    private static CreditAssessmentAnalyzer m_creditAssessmentAnalyzer;
    
    private boolean m_testMode=false;
    private boolean m_analyzerOn = true;
    private boolean m_checkCreditValueInRequest = true;
    private boolean m_checkCustomerStatus = true;
    private boolean m_checkNoOfWLNAccounts = true;
    private boolean m_checkNoOfWLSAccounts = true;
    private boolean m_checkWLNAccountCreationDate = true;
    private boolean m_checkCreditValueCondition = true;
    
    private Date m_rtcaLaunchDate = null;
    
    private CreditAssessmentAnalyzer() {
        
    }
    
    public static CreditAssessmentAnalyzer getInstance() {
        if ( m_creditAssessmentAnalyzer == null ) {
            m_creditAssessmentAnalyzer = new CreditAssessmentAnalyzer();
        }
        return m_creditAssessmentAnalyzer;
    }
    
    public boolean isCreditValueBeingChangedInRequest(
            ConsumerCreditProfileInfo consumerCreditProfileInfo)
    {
        boolean result =  (m_testMode? 
                false : (m_analyzerOn ? 
                            (m_checkCreditValueInRequest ? 
                                    ( (consumerCreditProfileInfo.getCreditValueCd() != null) 
                                       || (consumerCreditProfileInfo.getFraudIndicatorCd() != null ) )
                            : false) 
                         : false) );
        if ( log.isDebugEnabled() ) {
            log.debug("customer id: " + 
                ( (consumerCreditProfileInfo != null && consumerCreditProfileInfo.getIdentification() != null ) ? consumerCreditProfileInfo.getIdentification().getCustomerId() : null )
                + ", isCreditValueBeingChangedInRequest:" + result );
        }
        return result;
    }

    public boolean customerDataMeetConditions(
            CrdProfileUpdateFlags profileUpdateFlags)
    {
        boolean metConditions = false;
        boolean checkFurther = true;
        
        if ( log.isDebugEnabled() ) {
        log.debug( "test mode: " + m_testMode + ", m_analyzerOn: " + m_analyzerOn + ", m_checkNoOfWLNAccounts: " + m_checkNoOfWLNAccounts 
                   + ", m_checkNoOfWLSAccounts:  " + m_checkNoOfWLSAccounts + ",m_checkCustomerStatus:  " + m_checkCustomerStatus
                   + ", m_checkWLNAccountCreationDate: " + m_checkWLNAccountCreationDate 
                   + ", customer id: " + profileUpdateFlags.getCustomerId() + ", profileUpdateFlags.getNofWirelineBANs(): " + profileUpdateFlags.getNofWirelineBANs()
                   + ", profileUpdateFlags.getNoOfWirelessBANs(): "+ profileUpdateFlags.getNoOfWirelessBANs()
                   + ", profileUpdateFlags.getWirelineBANStatus(): " + profileUpdateFlags.getWirelineBANStatus()
                   + ", profileUpdateFlags.getWirelineBANCreationDate(): " + profileUpdateFlags.getWirelineBANCreationDate() );
        }
        if (m_testMode ) { return true; }
        
        if ( m_analyzerOn ) {
            if ( checkFurther ) {
                if ( m_checkNoOfWLNAccounts ) {
                    if ( profileUpdateFlags.getNofWirelineBANs() == 1 ) {
                        
                    }
                    else {
                        log.debug( "customer id: " + profileUpdateFlags.getCustomerId()
                                + ",customerDataMeetConditions: no of wireline bans is not equal to 1." ); 
                        checkFurther = false;
                    }
                }
            }
            
            if ( checkFurther ) {
                if ( m_checkNoOfWLSAccounts ) {
                    if ( profileUpdateFlags.getNoOfWirelessBANs() >= 1 ) {
                        
                    }
                    else {
                        log.debug( "customer id: " + profileUpdateFlags.getCustomerId()
                                + ", customerDataMeetConditions: no of wireless bans must be greater than 1." ); 
                        checkFurther = false;
                    }
                }
            }
            if ( checkFurther ) {
                if ( m_checkCustomerStatus ) {
                    if ( profileUpdateFlags.getWirelineBANStatus() != null
                         && profileUpdateFlags.getWirelineBANStatus().equals( "O" ) ) {
                    
                    }
                    else {
                        log.debug( "customer id: " + profileUpdateFlags.getCustomerId()
                                + ", customerDataMeetConditions: wireline BAN status must be O." );
                        checkFurther = false;
                    }
                }
            }
            
            if ( checkFurther ) {
                if ( m_checkWLNAccountCreationDate ) {
                    if ( profileUpdateFlags.getWirelineBANCreationDate() != null 
                         && profileUpdateFlags.getWirelineBANCreationDate().getTime() > m_rtcaLaunchDate.getTime() ) {
                        
                    }
                    else {
                        log.debug( "customer id: " + profileUpdateFlags.getCustomerId()
                                + ", customerDataMeetConditions: wireline ban creation date must be greater than RTCA launch date." );
                        checkFurther = false;
                    }
                }
            }
            if ( checkFurther ) {
                log.debug( "customer id: " + profileUpdateFlags.getCustomerId() + ", All conditions are met." );
                metConditions = true;
            }
        }
        else {
            return false;
        }
        log.debug( "customer id: " + profileUpdateFlags.getCustomerId()
                                + ", customerDataMeetConditions: + " + metConditions );
        return metConditions;
    }

    public boolean creditDataMeetConditions(
            ConsumerCreditProfile consumerCreditProfile)
    {
        boolean result = false;
        if ( m_testMode ) {  return true; }
        if ( m_analyzerOn ) {
            if ( m_checkCreditValueCondition ) {
                if ( consumerCreditProfile != null 
                     && consumerCreditProfile.getCreditWorthiness() != null
                     && (consumerCreditProfile.getCreditWorthiness().getCreditValueCd().equals("N")
                         || consumerCreditProfile.getCreditWorthiness().getCreditValueCd().equals("R"))
                     && (consumerCreditProfile.getCreditWorthiness().getCreditAssessmentId() == null
                         || consumerCreditProfile.getCreditWorthiness().getCreditAssessmentId().longValue() == 0 ) ) {
                  result=true;
               }
            }
        }
        log.debug( "credit profile id: " + consumerCreditProfile.getCreditProfileID() + ", creditDataMeetConditions: " + result );
        return result;
    }

    /**
     * @return Returns the m_testMode.
     */
    public boolean isTestMode()
    {
        return m_testMode;
    }

    /**
     * @param m_testMode The m_testMode to set.
     */
    public void setTestMode(boolean testMode)
    {
        this.m_testMode = testMode;
    }

    /**
     * @return Returns the m_analyzerOn.
     */
    public boolean isAnalyzerOn()
    {
        return m_analyzerOn;
    }

    /**
     * @param m_analyzerOn The m_analyzerOn to set.
     */
    public void setAnalyzerOn(boolean analyzerOn)
    {
        this.m_analyzerOn = analyzerOn;
    }

    /**
     * @return Returns the m_checkCreditValueInRequest.
     */
    public boolean isCheckCreditValueInRequest()
    {
        return m_checkCreditValueInRequest;
    }

    /**
     * @param m_checkCreditValueInRequest The m_checkCreditValueInRequest to set.
     */
    public void setCheckCreditValueInRequest(boolean checkCreditValueInRequest)
    {
        this.m_checkCreditValueInRequest = checkCreditValueInRequest;
    }

    /**
     * @return Returns the m_checkCustomerStatus.
     */
    public boolean isCheckCustomerStatus()
    {
        return m_checkCustomerStatus;
    }

    /**
     * @param m_checkCustomerStatus The m_checkCustomerStatus to set.
     */
    public void setCheckCustomerStatus(boolean checkCustomerStatus)
    {
        this.m_checkCustomerStatus = checkCustomerStatus;
    }

    /**
     * @return Returns the m_checkNoOfWLNAccounts.
     */
    public boolean isCheckNoOfWLNAccounts()
    {
        return m_checkNoOfWLNAccounts;
    }

    /**
     * @param m_checkNoOfWLNAccounts The m_checkNoOfWLNAccounts to set.
     */
    public void setCheckNoOfWLNAccounts(boolean checkNoOfWLNAccounts)
    {
        this.m_checkNoOfWLNAccounts = checkNoOfWLNAccounts;
    }

    /**
     * @return Returns the m_checkNoOfWLSAccounts.
     */
    public boolean isCheckNoOfWLSAccounts()
    {
        return m_checkNoOfWLSAccounts;
    }

    /**
     * @param m_checkNoOfWLSAccounts The m_checkNoOfWLSAccounts to set.
     */
    public void setCheckNoOfWLSAccounts(boolean checkNoOfWLSAccounts)
    {
        this.m_checkNoOfWLSAccounts = checkNoOfWLSAccounts;
    }

    /**
     * @return Returns the m_checkWLNAccountCreationDate.
     */
    public boolean isCheckWLNAccountCreationDate()
    {
        return m_checkWLNAccountCreationDate;
    }

    /**
     * @param m_checkWLNAccountCreationDate The m_checkWLNAccountCreationDate to set.
     */
    public void setCheckWLNAccountCreationDate(
            boolean checkWLNAccountCreationDate)
    {
        this.m_checkWLNAccountCreationDate = checkWLNAccountCreationDate;
    }

    /**
     * @return Returns the m_checkCreditValueCondition.
     */
    public boolean isCheckCreditValueCondition()
    {
        return m_checkCreditValueCondition;
    }

    /**
     * @param m_checkCreditValueCondition The m_checkCreditValueCondition to set.
     */
    public void setCheckCreditValueCondition(boolean checkCreditValueCondition)
    {
        this.m_checkCreditValueCondition = checkCreditValueCondition;
    }

    /**
     * @return Returns the m_rtcaLaunchDate.
     */
    public Date getRtcaLaunchDate()
    {
        return m_rtcaLaunchDate;
    }

    /**
     * @param m_rtcaLaunchDate The m_rtcaLaunchDate to set.
     */
    public void setRtcaLaunchDate(Date rtcaLaunchDate)
    {
        this.m_rtcaLaunchDate = rtcaLaunchDate;
    }
    
    public void setRtcaLaunchDateAsString(String rtcaLaunchDate) {
        m_rtcaLaunchDate = EntrCrdUtil.convertStringToDate( rtcaLaunchDate );
    }

}
