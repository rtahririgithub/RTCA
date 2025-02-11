/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package org.springframework.test.context.junit4;

import java.util.Collections;
import java.util.List;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.telus.credit.framework.test.TelusJUnitClassRunner;

/**
 * Stupid class to expose protected ops for {@link TelusJUnitClassRunner}.
 * Also executes test in order they appear in the source code.
 * 
 * @author Trevor Baker (x145637)
 */
public class SpringJUnit4ClassRunnerWrapper extends SpringJUnit4ClassRunner
{
    public SpringJUnit4ClassRunnerWrapper(Class<?> clazz) throws InitializationError
    {
        super(clazz);
    }


    @Override
    protected List<FrameworkMethod> computeTestMethods()
    {
        List<FrameworkMethod> list = super.computeTestMethods();
        Collections.sort(list, new MethodComparator(list));
        return list;
    }


    @Override
    public Statement withBeforeClasses(Statement statement)
    {
        return super.withBeforeClasses(statement);
    }


    @Override
    public Statement withAfterClasses(Statement statement)
    {
        return super.withAfterClasses(statement);
    }


    @Override
    public Object createTest() throws Exception
    {
        return super.createTest();
    }


    @Override
    public void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier)
    {
        super.runChild(frameworkMethod, notifier);
    }


    @Override
    public Statement methodBlock(FrameworkMethod frameworkMethod)
    {
        return super.methodBlock(frameworkMethod);
    }


    @Override
    public Statement possiblyExpectingExceptions(FrameworkMethod frameworkMethod, Object testInstance, Statement next)
    {
        return super.possiblyExpectingExceptions(frameworkMethod, testInstance, next);
    }


    @Override
    public Statement withPotentialTimeout(FrameworkMethod frameworkMethod, Object testInstance, Statement next)
    {
        return super.withPotentialTimeout(frameworkMethod, testInstance, next);
    }


    @Override
    public Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement)
    {
        return super.withBefores(frameworkMethod, testInstance, statement);
    }


    @Override
    public Statement withAfters(FrameworkMethod frameworkMethod, Object testInstance, Statement statement)
    {
        return super.withAfters(frameworkMethod, testInstance, statement);
    }
}
