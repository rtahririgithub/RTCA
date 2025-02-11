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
public class BridgingDataExtractModule implements DataExtractModule {

   private static final Log log = LogFactory.getLog(BridgingDataExtractModule.class);

   // holds the path to the data extract file
   private final String m_extractFilename = DataExtractModule.PATH + "CUSTODS_REF_BRIDGING.DAT";

   // holds the path to the audit file
   private final String m_auditFilename = DataExtractModule.PATH + "CUSTODS_REF_BRIDGING.AUD";

   // specifies the record width (in bytes) of each record in the extract file
   private int m_recordWidth = 0;

   /**
    * Executes this data extract module.
    *
    * @throws Exception if the data extract file or the audit file are invalid
    */
   public void execute() throws Exception
   {
      log.info(" **** BridgingDataExtractModule.execute: validating the bridging extract file. **** ");

      FileReader reader = new FileReader(m_extractFilename, m_auditFilename);
      try {
         int auditRecordCount = reader.getAuditRecordCount();
         reader.validateExtractFile(m_recordWidth, auditRecordCount);
      }
      catch (InvalidDataExtractException e) {
         throw new InvalidDataExtractException("BridgingDataExtractModule: the extract file is invalid, see stack trace for errors.", e);
      }
      catch (InvalidAuditFileException e) {
         throw new InvalidAuditFileException("BridgingDataExtractModule: the audit file is invalid (COD.AP481.002 - IA - Service Resource Bridging v2.4.doc) see stack trace for errors.", e);
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

