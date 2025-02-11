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

package com.fico.telus.rtca.blaze;

import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.blazesoft.server.deploy.manager.NdDeploymentManagerException;
import com.blazesoft.server.local.NdLocalServerException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><b>Description :</b><code>RuleServicesExceptionHandler</code> provides the services to 
 * handle exceptions from FICO Rule Service.</p>
 * <ul>
 * <li>None</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">12-Sep-2012</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">Initial Draft</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Gurbirinder Sidhu
 * 
 * @stereotype Error Handler
 * @version 1.0
 */
public class RuleServicesExceptionHandler
{
    private static final Log log = LogFactory.getLog( RuleServicesExceptionHandler.class );
	
    /**
     * Get the specific error code based on the different type of source exceptions
     *  
     * @param throwable
     * @return error code   
     */
    public static String getErrorCode( Throwable throwable ) {
        String result = null;
        
        if ( throwable instanceof NdDeploymentManagerException ) {
            result = RulesServicesExceptionCodes.FICO_DEPLOYMENT_MGR_EXCEPTION;
        }
        else if ( throwable instanceof NdLocalServerException ) {
            result = RulesServicesExceptionCodes.FICO_LOCAL_SERVER_EXCEPTION;
        }
        else if ( throwable instanceof NdServiceSessionException ) {
            result = RulesServicesExceptionCodes.FICO_SERVICE_SESSION_EXCEPTION;
        }
        else if ( throwable instanceof NdServiceException ) {
            result = RulesServicesExceptionCodes.FICO_SERVICE_EXCEPTION;
        }
        else if ( throwable instanceof NdServerException ) {
            result = RulesServicesExceptionCodes.FICO_SERVER_EXCEPTION;
        }
        else {
            result = RulesServicesExceptionCodes.FICO_RUNTIME_EXCEPTION;
        }
        return result;
    }
    
    /**
     * <p><b>Description:</b> This method throws RulesServiceException<br>
     * 	based on an input exception.It also logs the original exception's <br>
     * 	stack trace in the error log.
     * 
     * @param exception Exception that is caught in the service layer.
     * @throws RuleServicesException Rule Services Exception
     */
    public static RuleServicesException throwServiceException( Throwable throwable) throws RuleServicesException
    {
        String message = "FICO Rules Service Exception has occurred. " +
	    ", Error Code: " + getErrorCode( throwable );
	
        				 
        RuleServicesException ruleServicesException = new RuleServicesException(message, 
                getErrorCode( throwable ) );
        
        log.error( message, throwable );

        throw ruleServicesException;
    }
    
    /**
     * <p><b>Description:</b> This method throws RulesServiceException<br>
     * 	based on an input exception.It also logs the original exception's <br>
     * 	stack trace in the error log.
     * 
     * @param exception Exception that is caught in the service layer.
     * @throws RuleServicesException Rule Services Exception
     */
    public static RuleServicesException throwServiceException( long customerId, String inputXml, Throwable throwable) throws RuleServicesException
    {
        String message = "FICO Rules Service Exception has occurred for customerId: " + customerId +
	    ", Error Code: " + getErrorCode( throwable );
	
        				 
        RuleServicesException ruleServicesException = new RuleServicesException(message, 
                getErrorCode( throwable ) );
        
        log.error("Input xml: "+ inputXml );
        
        log.error( message, throwable );

        throw ruleServicesException;
    }
    
    
    /**
     * <p><b>Description:</b> This method throws RulesServiceException<br>
     * 	based on an input exception.It also logs the original exception's <br>
     * 	stack trace in the error log.
     * 
     * @param exception Exception that is caught in the service layer.
     * @throws RuleServicesException Rule Services Exception
     */
    public static RuleServicesException throwServiceException( long customerId, Throwable throwable) throws RuleServicesException
    {
        String message = "FICO Rules Service Exception has occurred for customerId: " + customerId +
	    ", Error Code: " + getErrorCode( throwable );
	
        				 
        RuleServicesException ruleServicesException = new RuleServicesException(message, 
                getErrorCode( throwable ) );
        
        log.error( message, throwable );

        throw ruleServicesException;
    }
}
