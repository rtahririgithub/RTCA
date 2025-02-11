package com.telus.credit.fraud.batch;

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

import java.io.*;
import java.io.FileReader;
import java.util.TreeSet;
import java.util.Iterator;

/**
 * Processes the candidate Target Feed File for delivery to NDS.
 *    1. Formats the City and Region fields.
 *    2. Writes a candidate target file to the master box
 *
 * Please consult the Cust Prof Connectwave Design and Interface
 * Specification (v1.0.5) for more details.
 * 
 * @author x107469
 */
public class Tester {

   // file path constants
   private String INPUT = "filename";
   private String OUTPUT = "filename" + "1";

   // references to the input and output files
   private BufferedReader m_input = null;
   private PrintStream m_output = null;

   /* by deleting a record from the master list we force an add to NDS */
   private static TreeSet addSet = new TreeSet();
   static {
      addSet.add(new Integer(4));
      addSet.add(new Integer(5));
      addSet.add(new Integer(6));
      addSet.add(new Integer(7));
      addSet.add(new Integer(8));
      addSet.add(new Integer(9));
      addSet.add(new Integer(10));
      addSet.add(new Integer(11));
      addSet.add(new Integer(12));
   }

   /* by changeing a record in the master list we force a change to NDS */
   private static TreeSet changeSet = new TreeSet();
   static {
      changeSet.add(new Integer(1));
      changeSet.add(new Integer(2));
      changeSet.add(new Integer(3));
   }

   /* by adding a record to the master list we force a delete to NDS */
   private static TreeSet deleteSet = new TreeSet();
   static {
      deleteSet.add(" 7809890562TEMP1                                     HOWERT                                               CALGARY AB                    T2C1Y6                                                                                            1997111919971119RE      TelusXXXXXN WADE                                      SLUGOSKI                                63343979 97814990                                                           4032419619                      ");
      deleteSet.add(" 7809890563TEMP2                                     HOWERT                                               CALGARY AB                    T2C1Y6                                                                                            1997111919971119RE      TelusXXXXXN WADE                                      SLUGOSKI                                63343979 97814990                                                           4032419619                      ");
      deleteSet.add(" 7809890564TEMP3                                     HOWERT                                               CALGARY AB                    T2C1Y6                                                                                            1997111919971119RE      TelusXXXXXN WADE                                      SLUGOSKI                                63343979 97814990                                                           4032419619                      ");
   }

   /* dictates whether to set up the master file for deletions */
   private boolean delete = true;

   /**
    * Constructs a public instance of this class.
    *
    * @throws IOException
    */
   public Tester() throws IOException {
      try {
         // configure the file streams
         m_input = new BufferedReader(new FileReader(INPUT));
         m_output = new PrintStream(new FileOutputStream(OUTPUT));

         // execute this class
         execute();
      }
      catch(IOException e) {
         e.printStackTrace();
        }
      finally {
         if(m_input != null) {
            m_input.close();
         }
         if(m_output != null) {
            m_output.close();
         }
      }
   }

   /**
    * Executes the business logic.
    *
    * @throws IOException
    */
   private void execute() throws IOException {

      int count = 1;
      for(;;) {
         String line = m_input.readLine();
         if(line == null) break;
         if( ! addSet.contains(new Integer(count))) {

            if(changeSet.contains(new Integer(count))) {
               StringBuffer buffer = new StringBuffer(line);
               buffer.replace(11,12,"A");
               m_output.println(buffer);
            }
            else {
               m_output.println(line);
            }
         }
         count ++;
      }

      if(delete) {
         Iterator iter = deleteSet.iterator();
         while(iter.hasNext()) {
            m_output.println((String)iter.next());
         }
      }

      if( ! renameFiles()) {
         System.out.println("  --> error: could not rename the Master File");
      }
   }

   private boolean renameFiles() {
      File input = new File(INPUT);
      if(input.exists()) {
         input.delete();
      }
      File output = new File(OUTPUT);
      return output.renameTo(input);
   }

   /**
    * Entry point.
    */
   public static void main(String[] args) {
      try {
         new Tester();
      }
      catch(Exception e) {
         e.printStackTrace();
      }
   }
}
