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

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TreeSet;

/**
 * <Replace this with a short description of the class.> 
 * 
 * @author x106347
 */
public class IFFTValidatationTest
{
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

    private TreeSet set = new TreeSet();

    private static int RECORD_LENGTH = 460;

    private String m_inputFile = null;

    private long counter = 0;

    public IFFTValidatationTest(String inputFile) {
        m_inputFile = inputFile;
    }

    public void validate( ) throws Exception {

      System.out.println("Validating [" + m_inputFile + "]...");
      BufferedReader in = new BufferedReader(new FileReader(m_inputFile));
      String str;
      while ((str = in.readLine()) != null) {
          counter++;
          char firstChar = str.charAt(0);
          if (firstChar == 'H') {
              // ignore the header record
          }
          else if ( firstChar == 'T' ) {
            String countStr = str.substring(2,9);
            long countValue = Long.parseLong(countStr.trim());
            if ((counter - 2) != countValue) {
                throw new Exception( "Number of records do not match."
                   + "\n Trailer Record Count: " + countStr.trim() + ", Actual Count: " + (counter -2));
            }
          }
          else {
            process(str);
          }
      }
      in.close();
      System.out.println("Successfully validated.");
    }

    /**<Replace this with one clearly defined responsibility this method does.>
     *  
     * @param str
     *   <n.b. for "@param" above <add a description after the field name>
     *   <n.b. for "@return" above <add the return_Type & the description>
     *   <n.b. for "@throws" (i.e. @exception) above, add "If" & description of when it happens>
     */
    private void process(String str) throws Exception
    {
      this.checkRecordLength(str);
      this.checkCommandLetter(str);
      this.checkWTN(str);
      this.checkServiceInstanceActivationDate(str);
      this.checkServiceInstanceStatDate(str);
      this.checkBTN(str);
      this.ensureRecordIsUnique(str);
    }


    private void checkRecordLength(String str) throws Exception {
       if (str.length() != RECORD_LENGTH ) {
           throw new Exception("The record length must be [" + RECORD_LENGTH
                             + "] bytes. \n where it is [" + str.length()
                             + "] bytes for line no: " +  counter
                             + "\n [" + str + "]");
       }
    }

   private void checkCommandLetter(String str) throws Exception {
      char firstChar = str.charAt(0);
      if (firstChar != 'A' && firstChar != 'D' && firstChar != 'C') {
          throw new Exception("The command letter must be one of 'A', 'D' or 'C'."
                            + "\n where it is [" + firstChar
                            + "] for line no: " +  counter
                            + "\n [" + str + "]");
      }
   }

   private void checkWTN(String str) throws Exception {
      String wtn = str.substring(1, 11);
//      System.out.println("WTN [" + wtn + "]");
      try {
        Long.parseLong(wtn);
      }
      catch (NumberFormatException n) {
          throw new Exception("The WTN must be a 10 digit number."
                           + "\n where it is [" + wtn
                           + "] for line no: " +  counter
                           + "\n [" + str + "]");
      }
   }

   private void checkServiceInstanceActivationDate(String str) throws Exception {
      String serviceInstanceActivationDt = str.substring(234, 242);
//      System.out.println("Serivce Instance Activation Date [" + serviceInstanceActivationDt + "]");
      try {
         DATE_FORMAT.parse(serviceInstanceActivationDt);
      }
      catch (ParseException pe) {
         throw new Exception("The Service Instance Activation Date must be in yyyyMMdd format."
                           + "\n where it is [" + serviceInstanceActivationDt
                           + "] for line no: " +  counter
                           + "\n [" + str + "]");
      }
   }

   private void checkServiceInstanceStatDate(String str) throws Exception {
      String serviceInstanceStatDt = str.substring(242, 250);
//      System.out.println("Serivce Instance Stat Date [" + serviceInstanceStatDt + "]");
      try {
         DATE_FORMAT.parse(serviceInstanceStatDt);
      }
      catch (ParseException pe) {
         throw new Exception("The Service Instance Stat Date must be in yyyyMMdd format."
                            + "\n where it is [" + serviceInstanceStatDt
                            + "] for line no: " +  counter
                            + "\n [" + str + "]");
      }
   }

   private void checkBTN(String str) throws Exception {
      String btn = str.substring(428, 438);
//      System.out.println("BTN [" + btn + "]");
      try {
         Long.parseLong(btn);
      }
      catch (NumberFormatException n) {
           throw new Exception("The BTN must be a 10 digit number."
                            + "\n where it is: [" + btn
                            + "] for line no: " +  counter
                            + "\n [" + str + "]");
      }
   }

   private void ensureRecordIsUnique(String str) throws Exception {
     String wtn = str.substring(1, 11);
//     System.out.println("WTN [" + wtn + "]");
     if( ! set.add(wtn)) {
        throw new Exception("This record is not unique."
                         + "\n WTN is [" + wtn + "] for line no: " +  counter
                         + "\n [" + str + "]");
     }
   }

    public static void main(String[] args)
    {
      try {
         if ( args.length != 1 ) {
            System.err.println("Usage: IFFTValidatationTest <input-file>");
         }

         IFFTValidatationTest ifftValidationTest = new IFFTValidatationTest( args[0] );
         ifftValidationTest.validate();
      }
      catch ( Exception e) {
         e.printStackTrace();
      }
    }
}