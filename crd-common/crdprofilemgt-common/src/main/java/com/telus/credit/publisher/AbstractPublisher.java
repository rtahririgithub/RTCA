/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.publisher;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import com.telus.credit.publisher.util.EventUtils;

/**
 * Base abstract class for all the Publisher classes.
 * 
 * NOTE:
 * 1) The retry log name is defined in the "RETRY_LOGGER_NAME", which can be used for log4j configuration
 * 
 * 2) All the child class is forced to implement the constructor. Reason being to let developer be aware
 *      of initializing the publisher explicitly before using.
 * 
 * 3) logPayLoadForRetry method can be overridden to customize the retry log formatting.
 * 
 * 4) The publishXXX methods throws exceptions which need to be captured.
 * 
 * @author t837068
 *
 */
public abstract class AbstractPublisher
{
    private static final int PUBLISH_CONNECT_TIMEOUT = 15000;
    private static final int PUBLISH_READ_TIMEOUT = 15000;

    /**
     * This is the log name to be defined if a Retry store is needed.
     */
    public static final String RETRY_LOGGER_NAME = "Pub_Sub_Persist_Store";

    /**
     * logging messages for retrying later.
     */
  //  protected static Log logRetry = LogFactory.getLog( RETRY_LOGGER_NAME );

    private WebTarget m_target;

    protected AbstractPublisher(String serverUrl, String credentialUsername,
            String credentialPassword, int connectTimeout, int readTimeout)
    {
        ClientConfig configuration = new ClientConfig();
        configuration = configuration.property(
                ClientProperties.CONNECT_TIMEOUT, connectTimeout );
        configuration = configuration.property(
                ClientProperties.READ_TIMEOUT, readTimeout );

        Client client = ClientBuilder.newClient( configuration );
        HttpAuthenticationFeature feature = HttpAuthenticationFeature
                .basic( credentialUsername, credentialPassword );

        m_target = client.register( feature ).target( serverUrl );
    }
    
    protected AbstractPublisher(String serverUrl, String credentialUsername,
            String credentialPassword)
    {
        this(serverUrl, credentialUsername, credentialPassword, PUBLISH_CONNECT_TIMEOUT, PUBLISH_READ_TIMEOUT);
    }


    /**
     * @return return the target.
     */
    public WebTarget getTarget()
    {
        return m_target;
    }


    /**
     *  Publish String directly to the PubSub server
     *  
     *  
     * @param target
     * @param payload
     * @return
     * @throws Exception   Any exception could happen during the Http Posting
     */
    public boolean publishString(WebTarget target, String payload)
            throws Exception
    {
        Response response = null;
        boolean success = false;

        try
        {
            response = target.request( MediaType.APPLICATION_JSON_TYPE ).post(
                    Entity.entity( payload, MediaType.APPLICATION_XML ),
                    Response.class );

            success = response.getStatus() == 200;
        }
        catch ( Exception e )
        {
            throw e;
        }
        finally
        {
            if ( !success && payload != null )
            {
                // Error response from Pub/sub server or exception captured, log payload to the persistent retry store
               // logRetry.error( logPayLoadForRetry( target, response, payload ) );
            }
            
            if ( response != null ) response.close();
        }
        return success;
    }


    /**
     * Publish Event object to PubSub server: by converting the Event object into XML format first.
     *  
     * @param target
     * @param event
     * @return
     * @throws Exception   
     */
    public boolean publishEvent(WebTarget target, Event event) throws Exception
    {
        String payload = EventUtils.convertEventObjToXml( event );

        return publishString( target, payload );
    }


    /**
     * Generate retry messages including server response and the post payload into retry store.
     * The extended class can override this method to include additional information.
     * 
     * NOTE: response.readEntity will cause the inputStream to be closed. So it's important to NOT use response.readEntity() in the overridden method.
     *  
     * @param response
     * @param payload   
     */
    protected String logPayLoadForRetry(WebTarget target, Response response, String payload)
    {
        String retMessage = "";
        String requestUrl = target.getUri().toString();
        if (response != null)
        {
            retMessage = response.readEntity( String.class );
        }
        
        return String.format( "%s"
                + "\r\n=====================SERVER RESPONSE=====================\r\n%s"
                + "\r\n=========================PAYLOAD=========================\r\n%s"
                + "===========================End===========================",
                requestUrl, retMessage, payload );
    }


    
}
