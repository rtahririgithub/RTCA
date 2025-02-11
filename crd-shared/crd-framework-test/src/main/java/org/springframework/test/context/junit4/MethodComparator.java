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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;

/**
 * Sorts the tests based on their position in the source code.
 * 
 * @author Trevor Baker (x145637)
 */
class MethodComparator implements Comparator<FrameworkMethod>
{
    private HashMap<String, MethodPosition> cache = new HashMap<String, MethodPosition>();


    public MethodComparator(List<FrameworkMethod> methods)
    {
        try
        {
            for( FrameworkMethod method : methods )
            {
                MethodPosition position = getIndexOfMethodPosition(method);
                cache.put(method.getMethod().getName(), position);
            }
        }
        catch( IOException e )
        {
            throw new RuntimeException(e);
        }
    }


    @Override
    public int compare(FrameworkMethod o1, FrameworkMethod o2)
    {
        MethodPosition methodPosition1 = cache.get(o1.getMethod().getName());
        MethodPosition methodPosition2 = cache.get(o2.getMethod().getName());
        return methodPosition1.compareTo(methodPosition2);
    }


    private MethodPosition getIndexOfMethodPosition(FrameworkMethod fMethod) throws IOException
    {
        Method method = fMethod.getMethod();
        Class<?> clazz = method.getDeclaringClass();
        String methodName = fMethod.getName();

        return getIndexOfMethodPosition(clazz, methodName);
    }


    private MethodPosition getIndexOfMethodPosition(Class<?> clazz, String methodName) throws IOException
    {
        InputStream inputStream = clazz.getResourceAsStream(clazz.getSimpleName() + ".class");
        LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(inputStream));

        try
        {
            String line;
            while( (line = lineNumberReader.readLine()) != null )
            {
                int index = line.indexOf(methodName);
                while( index != -1 )
                {
                    int checkIndex = index + methodName.length();
                    if( checkIndex == line.length() )
                    {
                        return new MethodPosition(lineNumberReader.getLineNumber(), index);
                    }

                    if( !isMethodNamePart(line.charAt(checkIndex)) )
                    {
                        return new MethodPosition(lineNumberReader.getLineNumber(), index);
                    }

                    index = line.indexOf(methodName, checkIndex);
                }
            }
        }
        finally
        {
            lineNumberReader.close();
        }

        throw new IOException("Cannot find methodName: " + methodName);
    }


    private boolean isMethodNamePart(char ch)
    {
        return Character.isLetterOrDigit(ch) || ch == '_'; 
    }


    //-------------------------------------------------------------------------
    // Helper Class
    //-------------------------------------------------------------------------
    private static class MethodPosition implements Comparable<MethodPosition>
    {
        private Integer lineNumber;
        private Integer indexInLine;


        public MethodPosition(int lineNumber, int indexInLine)
        {
            this.lineNumber = lineNumber;
            this.indexInLine = indexInLine;
        }


        @Override
        public int compareTo(MethodPosition o)
        {
            // If line numbers are equal, then compare by indexes in this line.
            if( lineNumber.equals(o.lineNumber) )
            {
                return indexInLine.compareTo(o.indexInLine);
            }

            return lineNumber.compareTo(o.lineNumber);
        }
    }
}
