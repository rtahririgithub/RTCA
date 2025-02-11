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

package com.telus.credit.wlnprfldmgt.webservice.util;

import com.telus.framework.exception.BaseException;

/**
 * <p><b>Description :</b><class>WLNCreditProfileDataManagementServiceServiceException</class> is the base class for EnterpriseCreditAssessmentPolicy Application Exception</p>
 * <p><b>Design Observations : </b></p>
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
 * <td width="15%">13-Sep-2012</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">New Class</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Gurbirinder Sidhu
 * 
 * @stereotype WLNCreditProfileDataManagementServiceServiceException
 * @version 1.0
 */
public class WLNCreditProfileDataManagementServiceException extends BaseException
{

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Default Constructor
     */
    public WLNCreditProfileDataManagementServiceException()
    {
        super();
    }
    
    /**
     * Constructs a WLNCreditProfileDataManagementServicePolicyException with a detail message but no error code and no
     * cause exception.
     * 
     * @param message
     *            the detail message
     */
    public WLNCreditProfileDataManagementServiceException(String message)
    {
        super( message );
    }
    /**
     * Constructs a WLNCreditProfileDataManagementServicePolicyException with a detail message and an error code but
     * with no cause exception.
     * 
     * @param message
     *            the detail message
     * @param errorCode
     *            the error code
     */
    public WLNCreditProfileDataManagementServiceException(String message, String errorCode)
    {
        super( message, errorCode );
    }
    /**
     * Constructs a WLNCreditProfileDataManagementServicePolicyException with a detail message, an error code and a
     * cause exception.
     * 
     * @param message
     *            the detail message
     * @param errorCode
     *            the error code
     * @param cause
     *            a Thowable object representing the casue exception.
     */
    public WLNCreditProfileDataManagementServiceException(String errorMessage, String errorCode, Throwable throwable)
    {
        super( errorMessage, errorCode, throwable );
    }
    /**
     * Constructs a BaseException with a detail message and a cause exception.
     * 
     * @param message
     *            the detail message
     * @param cause
     *            a Throwable object representing the cause exception.
     */
    public WLNCreditProfileDataManagementServiceException(String message, Throwable cause)
    {
        super( message, cause );
    }
    /**
     * Constructs a BaseException with a cause exception.
     * 
     * @param cause
     */
    public WLNCreditProfileDataManagementServiceException(Throwable cause)
    {
        super( cause );
    }
}
