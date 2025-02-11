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

package com.telus.credit.batch.util;

/**
 * Error Constants for Batch application.
 */
public class BatchErrorConstants
{

    public static class Type
    {

        public final static String FILE = "FILE";
        public final static String DB = "DB";
        public final static String SVC = "SVC";
        public final static String APP = "APP";
        public final static String THRW = "THRW";

    }

    public static class Subtype
    {

        // file subtypes:
        public final static String FL_READWRITE = "RD_WR";
        public final static String FL_NOT_FOUND = "FL_NOT_FOUND";
        public final static String FL_REC_PARSE = "REC_PARSE";
        public final static String FL_DUPLICATE_FOUND = "DUPLICATE_FOUND";

        //db subtypes:
        public final static String DB_OBJ_NOT_FOUND = "OBJ_NF"; // object not found
        public final static String DB_DUP_KEY = "DUP_KEY";
        public final static String DB_CONCR_CONF = "CCR_CONF";
        public final static String DB_SQL = "SQL";

        //service subtypes:
        public final static String SVC_ACCESS = "ACCESS";
        public final static String SVC_LOGGER = "LOGGER";

        // app sybtypes:
        public final static String APP_ERR_THRESHOLD = "ERR_THRESHOLD";
        public final static String APP_VAL_NULL = "VAL_NULL";
        public final static String APP_TEMPLATE_NF = "TMPL_NF";
        public final static String APP_INVAL_PAR = "INVAL_PAR";
        public final static String APP_INVAL_REC = "INVAL_REC";
        public final static String APP_FORMAT = "FORMAT";


    }


}
