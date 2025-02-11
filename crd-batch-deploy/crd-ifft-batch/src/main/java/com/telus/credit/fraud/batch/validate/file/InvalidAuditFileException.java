package com.telus.credit.fraud.batch.validate.file;

import java.io.Serializable;
import com.telus.framework.exception.BaseException;

/**
 * Exception to be thrown when an invalid audit file is encountered.
 *
 * User: x107469
 */
public class InvalidAuditFileException extends BaseException implements Serializable {

   public InvalidAuditFileException() {
      super();
   }

   public InvalidAuditFileException(String message) {
      super(message);
   }

   public InvalidAuditFileException(Throwable cause) {
      super(cause);
   }

   public InvalidAuditFileException(String message, String errorCode) {
      super(message);
      setErrorCode(errorCode);
   }

   public InvalidAuditFileException(String message, Throwable cause) {
      super(message, cause);
   }

   public InvalidAuditFileException(String message, String errorCode, Throwable cause) {
      super(message, cause);
      setErrorCode(errorCode);
   }
}
