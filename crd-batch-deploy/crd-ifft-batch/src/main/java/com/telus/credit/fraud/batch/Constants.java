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

/**
 * Convenience class to hold the system constants.
 * 
 * User: x107469
 */

public class Constants {

   // NDS Feed File Operations constants (see step 15: File Comparator)
   public final static String DELETE_OPERATION = "D";
   public final static String ADD_OPERATION = "A";
   public final static String CHANGE_OPERATION = "C";

   // unique telephone company code for the header of the NDS Feed File
   public static final String TELEPHONE_CODE = "";

   // models the Unix newline format (ie: /n)
   public static final int LINE_WIDTH_OFFSET = 1;
}

