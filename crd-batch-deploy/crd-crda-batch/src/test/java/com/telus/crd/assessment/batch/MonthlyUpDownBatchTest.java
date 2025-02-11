package com.telus.crd.assessment.batch;

import org.junit.Test;

import com.telus.credit.framework.test.TelusConfig;
import com.telus.credit.framework.test.AbstractBECTest;
import com.telus.credit.framework.test.BEC;
import com.telus.framework.batch.BaseExecutor;
import com.telus.framework.config.ConfigContext;

@BEC(appId = "CreditAssessment", level1Name = "crd", level2Name = "crda")
@TelusConfig(configAppCtxFile="JUnit/appCtx-mup.properties")
public class MonthlyUpDownBatchTest extends AbstractBECTest
{
//	@Test
    public void test_mupdg_init()
    {
		System.setProperty("L_CRDA_ODATE","20131112");
        exec("mupdg-init", BaseExecutor.MODE_NORMAL);
    }


    @Test
    public void test_mupdg()
    {
    	System.setProperty("L_CRDA_ODATE","20131112");
        System.setProperty("JOB_SUBGROUP", "-1");
        //System.out.println("PROP value: " + ConfigContext.getProperty(new String[] { "ficoConfig", "ficoCrdaAsmtConfigDir" } ) );
        
        exec("mupdg", "-1", BaseExecutor.MODE_FORCE_FROM_STEP01);
    }
}
