/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.fraud.batch.validate;

import com.telus.credit.fraud.batch.validate.module.DataExtractModule;
import com.telus.credit.fraud.batch.validate.module.ModuleFactory;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;

import java.util.Properties;

/**
 * Designed to act as the front side controller for the data extract validation
 * process.
 *
 * @author x106347
 */
public class IFFTValidateExtractFiles implements Module {

   // holds the specified record widths (in bytes) for the various data extracts
   private int m_billingExtractRecordWidth = 0;
   private int m_bridgingExtractRecordWidth = 0;
   private int m_creditExtractRecordWidth = 0;
   private int m_customerExtractRecordWidth = 0;
   private int m_serviceExtractRecordWidth = 0;

   // used to ensure that the batch execution framework executes this module exactly once
   private int m_executionCounter = 1;

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.DataExtractModule#execute()
   */
   public void execute() throws ModuleException
   {
      ModuleFactory moduleFactory = new ModuleFactory();
      try {
         moduleFactory.create(DataExtractModule.BILLING_EXTRACT, m_billingExtractRecordWidth).execute();
         moduleFactory.create(DataExtractModule.BRIDGING_EXTRACT, m_bridgingExtractRecordWidth).execute();
         moduleFactory.create(DataExtractModule.CREDIT_EXTRACT, m_creditExtractRecordWidth).execute();
         moduleFactory.create(DataExtractModule.CUSTOMER_EXTRACT, m_customerExtractRecordWidth).execute();
         moduleFactory.create(DataExtractModule.SERVICE_INSTANCE_EXTRACT, m_serviceExtractRecordWidth).execute();

         // increment the execution counter to ensure that this module is executed exactly once by the batch execution framework
         m_executionCounter ++;
      }
      catch (Exception e) {
         throw new ModuleException(e);
      }
   }

   /*
   * (non-Javadoc)
   *
   * @see com.telus.framework.batch.DataExtractModule#launch(com.telus.framework.batch.BatchContext)
   */
   public void launch(BatchContext batchContext) throws ModuleException {
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#hasNext()
    */
   public boolean hasNext() throws ModuleException {
      return m_executionCounter <= 1;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#onExit(boolean)
    */
   public int onExit(boolean success) throws ModuleException {
      if(success) {
         return RETURN_CODE_SUCCESS;
      }
      return RETURN_CODE_FAILURE;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#restoreState(java.util.Properties)
    */
   public void restoreState(Properties state) throws ModuleException {
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#getStateForRestart()
    */
   public Properties getStateForRestart() throws ModuleException {
      return null;
   }

   /*
    * (non-Javadoc)
    *
    * @see com.telus.framework.batch.DataExtractModule#getSummary()
    */
   public Properties getSummary() throws ModuleException {
        return null;
   }

   /**
    * Sets the billing extract record width.
    */
   public void setBillingExtractRecordWidth(String recordWidth) {
      m_billingExtractRecordWidth = Integer.parseInt(recordWidth);
   }

   /**
    * Sets the bridging extract record width.
    */
   public void setBridgingExtractRecordWidth(String recordWidth) {
      m_bridgingExtractRecordWidth = Integer.parseInt(recordWidth);
   }

   /**
    * Sets the credit extract record width.
    */
   public void setCreditExtractRecordWidth(String recordWidth) {
      m_creditExtractRecordWidth = Integer.parseInt(recordWidth);
   }

   /**
    * Sets the customer extract record width.
    */
   public void setCustomerExtractRecordWidth(String recordWidth) {
      m_customerExtractRecordWidth = Integer.parseInt(recordWidth);
   }

   /**
    * Sets the service instance extract record width.
    */
   public void setServiceExtractRecordWidth(String recordWidth) {
      m_serviceExtractRecordWidth = Integer.parseInt(recordWidth);
   }
}