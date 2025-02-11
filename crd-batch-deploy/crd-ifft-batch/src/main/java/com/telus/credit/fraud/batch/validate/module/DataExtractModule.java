package com.telus.credit.fraud.batch.validate.module;

/**
 * Created by IntelliJ IDEA.
 * User: x107469
 * Date: 19-Jan-2006
 * Time: 2:38:47 PM
 */
public interface DataExtractModule {

   public static final String PATH = "./data/inbox/";

   public static final String BILLING_EXTRACT = "billing";
   public static final String BRIDGING_EXTRACT = "bridging";
   public static final String CUSTOMER_EXTRACT = "customer";
   public static final String CREDIT_EXTRACT = "credit";
   public static final String SERVICE_INSTANCE_EXTRACT = "serviceInstance";

   public void execute() throws Exception;

   public void setRecordWidth(int recordWidth);
}

