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

package com.telus.crd.assessment.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Methods to create content from a template using Spring's Expression Language.
 * Use like a simplified version of Velocity or FreeMarker.
 * 
 * @author Trevor Baker (x145637)
 */
public final class TemplateUtil
{
    private static final SpelExpressionParser s_parser = new SpelExpressionParser();
    private static final TemplateParserContext s_context = new TemplateParserContext();


    public static String merge(String template, Object rootModel)
    {
        return s_parser.parseExpression(template, s_context).getValue(rootModel, String.class);
    }


    public static String merge(String template, EvaluationContext context)
    {
        return s_parser.parseExpression(template, s_context).getValue(context, String.class);
    }


    public static String merge(String template, EvaluationContext context, Object rootModel)
    {
        return s_parser.parseExpression(template, s_context).getValue(context, rootModel, String.class);
    }


    public static String mergeFromFile(String fileName, Object rootModel) throws IOException
    {
        return mergeFromFile(new File(fileName), rootModel);
    }


    public static String mergeFromFile(String fileName, EvaluationContext context) throws IOException
    {
        return mergeFromFile(new File(fileName), context);
    }


    public static String mergeFromFile(String fileName, EvaluationContext context, Object rootModel) throws IOException
    {
        return mergeFromFile(new File(fileName), context, rootModel);
    }


    public static String mergeFromFile(File file, Object rootModel) throws IOException
    {
        return merge(FileUtils.readFileToString(file), rootModel);
    }


    public static String mergeFromFile(File file, EvaluationContext context) throws IOException
    {
        return merge(FileUtils.readFileToString(file), context);
    }


    public static String mergeFromFile(File file, EvaluationContext context, Object rootModel) throws IOException
    {
        return merge(FileUtils.readFileToString(file), context, rootModel);
    }


    public static String mergeFromResource(String path, Object rootModel) throws IOException
    {
        return merge(readResourceToString(path), rootModel);
    }


    public static String mergeFromResource(String path, EvaluationContext context) throws IOException
    {
        return merge(readResourceToString(path), context);
    }


    public static String mergeFromResource(String path, EvaluationContext context, Object rootModel) throws IOException
    {
        return merge(readResourceToString(path), context, rootModel);
    }


    private static String readResourceToString(String path) throws IOException
    {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        if( in == null )
        {
            throw new IOException("Resource '" + path + "' does not exist.");
        }

        return IOUtils.toString(in);
    }
    
    
    //-------------------------------------------------------------------------
    private TemplateUtil()
    {
    }


    //-------------------------------------------------------------------------
    // TemplateParserContext
    //-------------------------------------------------------------------------
    private static class TemplateParserContext implements ParserContext
    {
        @Override
        public boolean isTemplate()
        {
            return true;
        }


        @Override
        public String getExpressionPrefix()
        {
            return "%{";
        }

        @Override
        public String getExpressionSuffix()
        {
            return "}";
        }
    }
}
