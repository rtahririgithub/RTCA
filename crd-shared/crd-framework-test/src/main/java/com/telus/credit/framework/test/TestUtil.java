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
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

/**
 * Helper operations for testing.
 *  
 * @author Trevor Baker (x145637)
 */
public final class TestUtil
{
    //-------------------------------------------------------------------------
    // Dates
    //-------------------------------------------------------------------------
    public static Date newDate(int year, int month, int day)
    {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static Date newServerDate()
    {
        long time = System.currentTimeMillis();
        int offset = TimeZone.getDefault().getOffset(time);
        return new Date(time - offset);
    }

    
    //-------------------------------------------------------------------------
    // File
    //-------------------------------------------------------------------------
    public static byte[] readFile(String fileName) throws IOException
    {
        File file = new File(fileName);
        FileInputStream in = new FileInputStream(file);

        try
        {
            byte[] data = new byte[(int )file.length()];
            in.read(data);
            return data;
        }
        finally
        {
            in.close();
        }
    }


    //-------------------------------------------------------------------------
    // Dump
    //-------------------------------------------------------------------------
    public static void dump(Object obj) throws Exception
    {
        System.out.println("------------------------------------------------");
        System.out.println(getCaller() + "()");
        System.out.println("------------------------------------------------");
        dump(obj, 0, null);
    }


    private static String getCaller()
    {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        return stacktrace[3].getMethodName();
    }


    private static void dump(Object obj, int indent, Field field) throws Exception
    {
        String pad = StringUtils.repeat(' ', indent);

        System.out.format("%s", pad);
        if( field != null )
        {
            System.out.format("%s %s = ", getTypeName(field.getGenericType()), field.getName());
        }

        if( obj == null || 
            obj.getClass().isPrimitive() ||
            obj instanceof Enum ||
            obj instanceof Number ||
            obj instanceof String ||
            obj instanceof Boolean ||
            obj instanceof Date )
        {
            String sep = (field == null)? "" : ";";
            obj = (obj instanceof String)? "\"" + obj + "\"" : obj;
            System.out.format("%s%s\n", obj, sep);
            return;
        }

        Class<?> clazz = obj.getClass();

        if( field == null )
        {
            System.out.format("%s\n%s", getTypeName(clazz), pad);
        }

        System.out.format("{\n");
        if( obj instanceof List )
        {
            for( Object item : (List<?> )obj )
            {
                dump(item, indent + 3, null);
            }
        }
        else
        {
            dumpFields(obj, clazz, indent + 3);

            Class<?> sup = clazz.getSuperclass();
            while( !sup.equals(Object.class) )
            {
                dumpFields(obj, sup, indent + 3);
                sup = sup.getSuperclass();
            }
        }

        System.out.format("%s}\n\n", pad);
    }


    private static void dumpFields(Object obj, Class<?> clazz, int indent) throws Exception
    {
        Field[] fields = clazz.getDeclaredFields();
        for( Field field : fields )
        {
            if( !Modifier.isStatic(field.getModifiers()) )
            {
                field.setAccessible(true);
                dump(field.get(obj), indent, field);
            }
        }
    }
    

    private static String getTypeName(Type type) throws Exception
    {
        if( type instanceof Class )
        {
            return ((Class<?> )type).getSimpleName();
        }

        if( type instanceof ParameterizedType )
        {
            ParameterizedType t = (ParameterizedType )type;

            StringBuilder sb = new StringBuilder();
            Type[] ts = t.getActualTypeArguments();

            sb.append(((Class<?> )t.getRawType()).getSimpleName());

            for( int i = 0; i < ts.length; i++ )
            {
                if( i == 0 )
                {
                    sb.append("<");
                }

                sb.append(((Class<?> )ts[i]).getSimpleName());

                if( i == ts.length - 1 )
                {
                    sb.append(">");
                }
                else
                {
                    sb.append(", ");
                }
            }

            return sb.toString();
        }

        throw new Exception("Unknown getTypeName(): "  + type);
    }


    //-------------------------------------------------------------------------
    // Telus Frameworks Classpath
    //-------------------------------------------------------------------------
    static void addClasspath(String path)
    {
        try
        {
            URL url = new File(path).getAbsoluteFile().toURI().toURL();
            URLClassLoader classLoader = (URLClassLoader )ClassLoader.getSystemClassLoader();

            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            method.setAccessible(true);
            method.invoke(classLoader, url);
        }
        catch( Exception e )
        {
            // shouldn't happen
            throw new Error(e);
        }
    }


    //-------------------------------------------------------------------------
    private TestUtil()
    {
    }
}
