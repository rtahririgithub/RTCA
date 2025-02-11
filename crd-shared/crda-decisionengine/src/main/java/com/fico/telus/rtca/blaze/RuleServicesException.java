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

import com.telus.framework.exception.BaseException;

/**
 * <p><b>Description :</b><class>RuleServicesException</class> is the base class for RuleServices Application Exception</p>
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
 * @stereotype RuleServicesException
 * @version 1.0
 */
public class RuleServicesException extends BaseException
{

    /**
     * CONSTRUCTOR
     *    
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public RuleServicesException()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    /**
     * CONSTRUCTOR
     * @param arg0   
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public RuleServicesException(String arg0)
    {
        super( arg0 );
        // TODO Auto-generated constructor stub
    }
    /**
     * CONSTRUCTOR
     * @param arg0
     * @param arg1   
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public RuleServicesException(String arg0, String arg1)
    {
        super( arg0, arg1 );
        // TODO Auto-generated constructor stub
    }
    /**
     * CONSTRUCTOR
     * @param arg0
     * @param arg1
     * @param arg2   
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public RuleServicesException(String arg0, String arg1, Throwable arg2)
    {
        super( arg0, arg1, arg2 );
        // TODO Auto-generated constructor stub
    }
    /**
     * CONSTRUCTOR
     * @param arg0
     * @param arg1   
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public RuleServicesException(String arg0, Throwable arg1)
    {
        super( arg0, arg1 );
        // TODO Auto-generated constructor stub
    }
    /**
     * CONSTRUCTOR
     * @param arg0   
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public RuleServicesException(Throwable arg0)
    {
        super( arg0 );
        // TODO Auto-generated constructor stub
    }
}
