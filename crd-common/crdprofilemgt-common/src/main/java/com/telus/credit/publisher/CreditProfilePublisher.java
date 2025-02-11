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

package com.telus.credit.publisher;

import java.util.Date;

import javax.ws.rs.client.WebTarget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.publisher.util.ConvertToGenericUtil;
import com.telus.credit.publisher.util.PublisherConstants;


public class CreditProfilePublisher extends AbstractPublisher
{
    private String m_creditProfilePublishTopic;
    
    private static Log log = LogFactory.getLog( CreditProfilePublisher.class );
    

    
    
    public CreditProfilePublisher(String serverUrl,
            String credentialUsername, String credentialPassword, int connectTimeout, int readTimeout)
    {
        super( serverUrl, credentialUsername, credentialPassword );
        
    }
    
    public CreditProfilePublisher(String serverUrl,
            String credentialUsername, String credentialPassword)
    {
        super( serverUrl, credentialUsername, credentialPassword );
        
    }
    
    public void publishCreditProfile(String messageType){

        WebTarget creditProfileTarget = getTarget();
        Event creditProfileEvent;
        
       
        
    }

   
    
}
