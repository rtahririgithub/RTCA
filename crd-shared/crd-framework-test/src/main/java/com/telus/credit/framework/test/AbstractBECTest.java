/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.framework.test;

import java.io.File;
import java.util.TimeZone;

import org.junit.Before;

import com.telus.framework.batch.JobController;
//import com.telus.framework.logging.Log4JLogger;

public abstract class AbstractBECTest
{
    @Before
    public void init()
    {
        BEC bec = getClass().getAnnotation(BEC.class);
        if( bec == null )
        {
            throw new IllegalStateException("Missing @BEC annotation.");
        }
        
        String configAppCtxFile = "appCtx.properties";
        
        TelusConfig config = getClass().getAnnotation(TelusConfig.class);
        if( config != null )
        {
            configAppCtxFile = config.configAppCtxFile();
        }

        // Added by Alan - set default timezone to UTC
        //to be aligned with the timezone in application and DB servers
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        
        System.out.println("appctx:" + configAppCtxFile);
        System.setProperty("configAppCtxFile", configAppCtxFile);

        String workingDir = new File("").getAbsolutePath();

        System.setProperty("APPID", bec.appId());
        System.setProperty("APP_HOME", workingDir);
        System.setProperty("DATA_HOME", workingDir+ "/test");
        System.setProperty("ENV", "local");
        System.setProperty("LEVEL1_SHORTNAME", bec.level1Name());
        System.setProperty("LEVEL2_SHORTNAME", bec.level2Name());
        System.setProperty("SCHEDULER_INSTNC_ID", System.getProperty("user.name"));

        // fw-bec.xml work-arounds
        System.setProperty("oraclehome", "/dev/null");
        System.setProperty("syncsorthome", "/dev/null");
        System.setProperty("JAVA_HOME", "/dev/null");
        System.setProperty("java.security.auth.login.config", "../../app-config/test/jaas.config");
        
        //Amdocs related properties
        System.setProperty("amdocs.uams.config.print", "false");
        System.setProperty("amdocs.system.home", ".");
        
        
        TestUtil.addClasspath("../../app-config/test/" + bec.appId()); // test appCtx.properties (if any)
        TestUtil.addClasspath("../../app-config/batch/" + bec.appId() + "/conf-app"); // conf-app
        TestUtil.addClasspath("../../app-config/test"); // root of test config
    }


    protected void exec(String jobName, String mode)
    {
        exec(jobName, "", mode);
    }


    protected void exec(String jobName, String jobSubGroup, String mode)
    {
        String level2Name = System.getProperty("LEVEL2_SHORTNAME");
        String batchUserId = level2Name + "-" + jobName + jobSubGroup;
        String file = "jobdefinition/" + level2Name + "-" + jobName + ".jdef";

        System.setProperty("BATCH_USERID", batchUserId);
        System.setProperty("JOB", jobName);
        System.setProperty("JOB_SUBGROUP", jobSubGroup);
        System.setProperty("NormalExit", "true");
//        Log4JLogger.getConfigurator().refresh();
        JobController.main(new String[] {file, mode});
    }
}
