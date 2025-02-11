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

package com.telus.credit.crda.exception;

import com.telus.framework.exception.BaseException;

/**
 * <p><b>Description :</b><class>EnterpriseCreditAssessmentPolicyException</class> is the base class for EnterpriseCreditAssessmentPolicy Application Exception</p>
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
 * @version 1.0
 * @stereotype EnterpriseCreditAssessmentPolicyException
 */
public class EnterpriseCreditAssessmentPolicyException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * CONSTRUCTOR
     * <p/>
     * <n.b. for "@param" above <add a description after the field name>
     * <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    public EnterpriseCreditAssessmentPolicyException() {
        super();

    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public EnterpriseCreditAssessmentPolicyException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0
     * @param arg1 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public EnterpriseCreditAssessmentPolicyException(String errorMessage, String errorCode) {
        super(errorMessage, errorCode);

    }

    /**
     * Constructs a BaseException with a detail message, an error code and a
     * cause exception.
     *
     * @param message   the detail message
     * @param errorCode the error code
     * @param cause     a Thowable object representing the casue exception.
     */
    public EnterpriseCreditAssessmentPolicyException(String errorMessage, String errorCode, Throwable throwable) {
        super(errorMessage, errorCode, throwable);
    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0
     * @param arg1 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public EnterpriseCreditAssessmentPolicyException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);

    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public EnterpriseCreditAssessmentPolicyException(Throwable throwable) {
        super(throwable);

    }
}
