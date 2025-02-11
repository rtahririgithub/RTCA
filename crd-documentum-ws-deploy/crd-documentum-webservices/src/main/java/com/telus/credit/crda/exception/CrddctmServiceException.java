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

public class CrddctmServiceException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a EnterpriseCreditAssessmentServiceException with no detail message, no error code and no
     * cause exception.
     */
    public CrddctmServiceException() {
        super();
    }

    /**
     * Constructs a EnterpriseCreditAssessmentServiceException with a detail message but no error code and no
     * cause exception.
     *
     * @param message the detail message
     */
    public CrddctmServiceException(String message) {
        super(message);
    }

    /**
     * Constructs a EnterpriseCreditAssessmentServiceException with a detail message and an error code but
     * with no cause exception.
     *
     * @param message   the detail message
     * @param errorCode the error code
     */
    public CrddctmServiceException(String message, String errorCode) {
        super(message, errorCode);
    }

    /**
     * Constructs a EnterpriseCreditAssessmentServiceException with a detail message, an error code and a
     * cause exception.
     *
     * @param message   the detail message
     * @param errorCode the error code
     * @param cause     a Thowable object representing the casue exception.
     */
    public CrddctmServiceException(String errorMessage, String errorCode, Throwable throwable) {
        super(errorMessage, errorCode, throwable);
    }

    /**
     * Constructs a EnterpriseCreditAssessmentServiceException with a detail message and a cause exception.
     *
     * @param message the detail message
     * @param cause   a Throwable object representing the cause exception.
     */
    public CrddctmServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a EnterpriseCreditAssessmentServiceException with a cause exception.
     *
     * @param cause
     */
    public CrddctmServiceException(Throwable cause) {
        super(cause);
    }
}
