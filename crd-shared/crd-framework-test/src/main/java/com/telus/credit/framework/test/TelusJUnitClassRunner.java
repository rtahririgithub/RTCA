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

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunnerWrapper;

import com.telus.framework.security.SecurityContext;
import com.telus.framework.security.authentication.TelusPrincipal;

/**
 * Stupid class to setup Telus specific classpaths for JUnit.
 * Do this so developers don't have to manually.
 * 
 * @author Trevor Baker (x145637)
 */
public class TelusJUnitClassRunner extends BlockJUnit4ClassRunner
{
    private final SpringJUnit4ClassRunnerWrapper m_runner;


    public TelusJUnitClassRunner(Class<?> clazz) throws InitializationError
    {
        super(clazz);

        String configAppCtxFile = TelusConfig.DEFAULT_CONFIG_APP_CTX;
        
        TelusConfig config = clazz.getAnnotation(TelusConfig.class);
        if( config != null )
        {
            configAppCtxFile = config.configAppCtxFile();
        }

        System.setProperty("configAppCtxFile", configAppCtxFile);
        TestUtil.addClasspath("../../app-config/test"); // root of test config
       
        // init security context
        System.setProperty("java.security.auth.login.config", "../../app-config/test/jaas.config");
        TelusPrincipal principal = (TelusPrincipal )SecurityContext.getPrincipal();
        principal.setUUID("junit");

        m_runner = new SpringJUnit4ClassRunnerWrapper(clazz);
    }


    @Override
    public Description getDescription()
    {
        return m_runner.getDescription();
    }


    @Override
    public void run(RunNotifier notifier)
    {
        m_runner.run(notifier);
    }


    @Override
    protected Statement withBeforeClasses(Statement statement)
    {
        return m_runner.withBeforeClasses(statement);
    }


    @Override
    protected Statement withAfterClasses(Statement statement)
    {
        return m_runner.withAfterClasses(statement);
    }


    @Override
    protected Object createTest() throws Exception
    {
        return m_runner.createTest();
    }


    @Override
    protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier)
    {
        m_runner.runChild(frameworkMethod, notifier);
    }


    @Override
    protected Statement methodBlock(FrameworkMethod frameworkMethod)
    {
        return m_runner.methodBlock(frameworkMethod);
    }


    @Override
    protected Statement possiblyExpectingExceptions(FrameworkMethod frameworkMethod, Object testInstance, Statement next)
    {
        return m_runner.possiblyExpectingExceptions(frameworkMethod, testInstance, next);
    }


    @Override
    protected Statement withPotentialTimeout(FrameworkMethod frameworkMethod, Object testInstance, Statement next)
    {
        return m_runner.withPotentialTimeout(frameworkMethod, testInstance, next);
    }


    @Override
    protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement)
    {
        return m_runner.withBefores(frameworkMethod, testInstance, statement);
    }


    @Override
    protected Statement withAfters(FrameworkMethod frameworkMethod, Object testInstance, Statement statement)
    {
        return m_runner.withAfters(frameworkMethod, testInstance, statement);
    }
}
