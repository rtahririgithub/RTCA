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
 * <p><b>Description :</b><class>EnterpriseCreditAssessmentExceptionCodes</class> defines the possible error codes for EnterpriseCreditAssessment Service and Application Exception</p>
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
 * @stereotype Constants
 * @version 1.0
 */
public class WLNCreditProfileDataManagementExceptionCodes 
{
    //
    // Message Forrmat should be: <application-id>-<type>-<code>
    //

    public static final String OBJECT_NOT_FOUND_EXCEPTION = "WLND-GEN-001";
    public static final String DUPLICATE_KEY_EXCEPTION = "WLND-GEN-002";
    public static final String PERSISTENT_EXCEPTION = "WLND-GEN-003";
    public static final String CREDIT_PROFILE_VALIDATION_EXCEPTION = "WLND-GEN-004";
    public static final String CREDIT_VALUE_VALIDATION_EXCEPTION = "WLND-GEN-005";
    public static final String CONCURRENCY_CONFLICT_EXCEPTION = "WLND-GEN-006";
    public static final String GUARANTOR_VALIDATION_EXCEPTION = "WLND-GEN-007";
    public static final String GUARANTOR_CUSTOMER_ID_VALIDATION_EXCEPTION = "WLND-GEN-008";
    public static final String CURRENT_PROVINCE_OF_RESIDENCY_VALIDATION_EXCEPTION = "WLND-GEN-009";
    public static final String AUDIT_INFO_VALIDATION_EXCEPTION = "WLND-GEN-010";
    
    public static final String CREDIT_ASSESSMENT_POLICY_EXCEPTION = "WLND-CRA-001";
    public static final String CREDIT_ASSESSMENT_SERVICE_EXCEPTION = "WLND-CRA-002";
    public static final String CREDIT_ASSESSMENT_RUNTIME_EXCEPTION = "WLND-CRA-003";
    
    public static final String CUSTOMER_MGMT_POLICY_EXCEPTION = "WLND-CCR-001";
    public static final String CUSTOMER_MGMT_SERVICE_EXCEPTION = "WLND-CCR-002";
    public static final String CUSTOMER_MGMT_RUNTIME_EXCEPTION = "WLND-CCR-003";
    
    public static final String UNKNOWN_EXCEPTION = "WLND-UNK-001";
    

}
