package com.telus.credit.fraud.batch.validate.file;

import java.io.Serializable;
import com.telus.framework.exception.BaseException;

/**
 * Exception to be thrown when an invalid extract file is encountered.
 * 
 * User: x107469
 */
public class InvalidDataExtractException extends BaseException implements Serializable {

   public InvalidDataExtractException() {
      super();
   }

   public InvalidDataExtractException(String message) {
      super(message);
   }

   public InvalidDataExtractException(Throwable cause) {
      super(cause);
   }

   public InvalidDataExtractException(String message, String errorCode) {
      super(message);
      setErrorCode(errorCode);
   }

   public InvalidDataExtractException(String message, Throwable cause) {
      super(message, cause);
   }

   public InvalidDataExtractException(String message, String errorCode, Throwable cause) {
      super(message, cause);
      setErrorCode(errorCode);
   }
}
