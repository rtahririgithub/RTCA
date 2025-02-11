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

package com.telus.credit.fraud.batch.validate.module;

/**
 * Factory to create and assemble Data Extract Modules. 
 * 
 * @author x106347
 */
public class ModuleFactory {

   public DataExtractModule create(String identifier, int recordWidth) throws IllegalArgumentException
   {
      if(identifier == null || identifier.length() <= 0) {
         throw new IllegalArgumentException("The specified identifier is null or an empty string.");
      }
      if(identifier.equals(DataExtractModule.BILLING_EXTRACT)) {
         BillingDataExtractModule module = new BillingDataExtractModule();
         module.setRecordWidth(recordWidth);
         return module;
      }
      if(identifier.equals(DataExtractModule.BRIDGING_EXTRACT)) {
         BridgingDataExtractModule module = new BridgingDataExtractModule();
         module.setRecordWidth(recordWidth);
         return module;
      }
      if(identifier.equals(DataExtractModule.CUSTOMER_EXTRACT)) {
         CustomerDataExtractModule module = new CustomerDataExtractModule();
         module.setRecordWidth(recordWidth);
         return module;
      }
      if(identifier.equals(DataExtractModule.SERVICE_INSTANCE_EXTRACT)) {
         ServiceInstanceDataExtractModule module = new ServiceInstanceDataExtractModule();
         module.setRecordWidth(recordWidth);
         return module;
      }
      if(identifier.equals(DataExtractModule.CREDIT_EXTRACT)) {
         CreditDataExtractModule module = new CreditDataExtractModule();
         module.setRecordWidth(recordWidth);
         return module;
      }
      throw new IllegalArgumentException("Cannot create a module: the identifier [" + identifier + "] is not recognized.");
   }
}