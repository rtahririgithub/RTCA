
package com.telus.credit.crda.exception;

import com.telus.framework.exception.BaseException;

public class CrddctmPolicyException extends BaseException {

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
    public CrddctmPolicyException() {
        super();

    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public CrddctmPolicyException(String errorMessage) {
        super(errorMessage);
    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0
     * @param arg1 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public CrddctmPolicyException(String errorMessage, String errorCode) {
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
    public CrddctmPolicyException(String errorMessage, String errorCode, Throwable throwable) {
        super(errorMessage, errorCode, throwable);
    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0
     * @param arg1 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public CrddctmPolicyException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);

    }

    /**
     * CONSTRUCTOR
     *
     * @param arg0 <n.b. for "@param" above <add a description after the field name>
     *             <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */

    public CrddctmPolicyException(Throwable throwable) {
        super(throwable);

    }
}
