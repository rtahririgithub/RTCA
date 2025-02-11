package com.telus.credit.fraud.batch.validate.module;

import com.telus.credit.fraud.batch.validate.file.FileReader;
import com.telus.credit.fraud.batch.validate.file.InvalidAuditFileException;
import com.telus.credit.fraud.batch.validate.file.InvalidDataExtractException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: x107469
 * Date: 19-Jan-2006
 * Time: 2:38:47 PM
 */
public class CustomerDataExtractModule implements DataExtractModule {

   private static final Log log = LogFactory.getLog(CustomerDataExtractModule.class);

   // holds the path to the data extract file
   private static final String m_extractFilename = DataExtractModule.PATH + "CUSTODS_REF_CUSTOMER.DAT";

   // holds the path to the audit file
   private static final String m_auditFilename = DataExtractModule.PATH + "CUSTODS_REF_CUSTOMER.AUD";

   // specifies the record width (in bytes) of each record in the extract file
   private int m_recordWidth = 0;

   // specifies the width (in bytes) of the primary key of each record
   private static final int PRIMARY_KEY_WIDTH = 9;

   /**
    * Executes this data extract module.
    *
    * @throws Exception if the data extract file or the audit file are invalid
    */
   public void execute() throws Exception
   {
      log.info(" **** CustomerDataExtractModule.execute: validating the customer extract file. **** ");

      FileReader reader = new FileReader(m_extractFilename, m_auditFilename);
      try {
         int auditRecordCount = reader.getAuditRecordCount();
         reader.validateExtractFile(m_recordWidth, PRIMARY_KEY_WIDTH, auditRecordCount);
      }
      catch (InvalidDataExtractException e) {
         throw new InvalidDataExtractException("CustomerDataExtractModule: the extract file is invalid, see stack trace for errors.", e);
      }
      catch (InvalidAuditFileException e) {
         throw new InvalidAuditFileException("CustomerDataExtractModule: the audit file is invalid (COD.AP481.005 - IA Customer ODS - Customer v2.1.doc) see stack trace for errors.", e);
      }
      finally {
         reader.close();
      }
   }

   /**
    * Sets the record width.
    */
   public void setRecordWidth(int recordWidth) {
      m_recordWidth = recordWidth;
   }
}


