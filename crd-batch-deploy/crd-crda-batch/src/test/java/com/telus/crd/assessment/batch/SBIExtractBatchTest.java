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

package com.telus.crd.assessment.batch;

import org.junit.Test;

import com.telus.framework.batch.BaseExecutor;

import com.telus.credit.framework.test.AbstractBECTest;
import com.telus.credit.framework.test.BEC;

@BEC(appId = "CreditAssessment", level1Name = "crd", level2Name = "crda")
public class SBIExtractBatchTest extends AbstractBECTest
{
    
    @Test
    public void test_sbiextract_init() {
        System.setProperty("L_SBI_START_DATE", "20121112000000");
        System.setProperty("L_SBI_END_DATE", "20131126000000");
        System.setProperty("L_SBI_PREV_START", "20121112000000");
        System.setProperty("L_SBI_PREV_END", "20131126000000");
        System.setProperty("L_CURRENT_DATE", "20131126000000");
        System.setProperty("L_YESTERDAY", "20131125000000");
        System.setProperty("L_LIBMEM-SBI", "/apps/common/libmemsym/wk05/CreditAssessment/SBI-EXTRACT-DATE-PARAM.txt");
        System.setProperty("JOB_SUBGROUP", "-1");
        exec("rtca-sbi-extract-init",  BaseExecutor.MODE_NORMAL);
    }
    
    @Test
    public void test_sbiextract_crdt() {
        System.setProperty("L_SBI_START_DATE", "20121112000000");
        System.setProperty("L_SBI_END_DATE", "20131126000000");
        System.setProperty("L_SBI_PREV_START", "20121112000000");
        System.setProperty("L_SBI_PREV_END", "20131126000000");
        System.setProperty("L_CURRENT_DATE", "20131126000000");
        System.setProperty("L_YESTERDAY", "20131125000000");
        System.setProperty("L_LIBMEM-SBI", "/apps/common/libmemsym/wk05/CreditAssessment/SBI-EXTRACT-DATE-PARAM.txt");
        System.setProperty("JOB_SUBGROUP", "-1");
        exec("rtca-sbi-extract-crdt",  BaseExecutor.MODE_NORMAL);
    }
    
    @Test
    public void test_sbiextract_calc() {
        System.setProperty("L_SBI_START_DATE", "20121112000000");
        System.setProperty("L_SBI_END_DATE", "20131126000000");
        System.setProperty("L_SBI_PREV_START", "20121112000000");
        System.setProperty("L_SBI_PREV_END", "20131126000000");
        System.setProperty("L_CURRENT_DATE", "20131126000000");
        System.setProperty("L_YESTERDAY", "20131125000000");
        System.setProperty("L_LIBMEM-SBI", "/apps/common/libmemsym/wk05/CreditAssessment/SBI-EXTRACT-DATE-PARAM.txt");
        System.setProperty("JOB_SUBGROUP", "-1");
        exec("rtca-sbi-extract-calc",  BaseExecutor.MODE_NORMAL);
    }
    
    @Test
    public void test_sbiextract_merge() {
        System.setProperty("L_SBI_START_DATE", "20121112000000");
        System.setProperty("L_SBI_END_DATE", "20131126000000");
        System.setProperty("L_SBI_PREV_START", "20121112000000");
        System.setProperty("L_SBI_PREV_END", "20131126000000");
        System.setProperty("L_CURRENT_DATE", "20131126000000");
        System.setProperty("L_YESTERDAY", "20131125000000");
        System.setProperty("L_LIBMEM-SBI", "/apps/common/libmemsym/wk05/CreditAssessment/SBI-EXTRACT-DATE-PARAM.txt");
        System.setProperty("JOB_SUBGROUP", "-1");
        exec("rtca-sbi-merge-calc",  BaseExecutor.MODE_NORMAL);
        exec("rtca-sbi-merge-crdt",  BaseExecutor.MODE_NORMAL);
    }
    
    public void test_sbiextract_sftp() {
        System.setProperty("configAppCtxFile", "appCtx-sftx.properties");
        exec("rtca-sbi-sftp",  BaseExecutor.MODE_NORMAL);
    }
    
    public void test_sbiextract_clean() {
//        System.setProperty("configAppCtxFile", "appCtx-sftx.properties");
//        exec("rtca-sbi-clean",  BaseExecutor.MODE_NORMAL);
    }

}
