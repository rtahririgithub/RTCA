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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class converts wildcard filename patterns into a Java
 * regular expression that can be used to to find matching filenames.
 * 
 * <p>
 * <b>NOTE:</b>
 * Currently only UNIX wildcard filename patterns are supported.
 * </p>
 * 
 */
public final class WildcardRegex
{
    private static final String  REGEX_SLASH = "\\\\";
    private static final String  PUNCTUATION1 = "[-#%,=@_~]";   // -#%,=@_~
    private static final String  PUNCTUATION2 = "[\\+\\.\\^]";  // +.^
    private static final String  ESCAPED_PUNCTUATION1 = REGEX_SLASH + "[\\(\\)\\{\\}\\[\\]\\$]"; // (){}[]$
    private static final String  ESCAPED_PUNCTUATION2 = REGEX_SLASH + "[ !';`]"; // <space>!';`
    private static final String  CHAR_SEQUENCE = "(?:\\p{Alnum}|" +
                                                      PUNCTUATION1 + "|" +
                                                      PUNCTUATION2 + "|" +
                                                      ESCAPED_PUNCTUATION1 + "|" +
                                                      ESCAPED_PUNCTUATION2 + ")+";
    private static final String  WILDCARD_ANY = "\\*";
    private static final String  WILDCARD_ONE = "\\?";
    private static final String  CHAR_CLASS = "\\[" + CHAR_SEQUENCE + "\\]";
    private static final String  EXCEPTION_CHAR_CLASS = "(\\[)(!)(" + CHAR_SEQUENCE + "\\])";
    private static final String  FILENAME_PATTERN = "^(?:" + CHAR_SEQUENCE + "|" +
                                                             WILDCARD_ANY + "|" +
                                                             WILDCARD_ONE + "|" +
                                                             CHAR_CLASS + "|" +
                                                             EXCEPTION_CHAR_CLASS + ")+$";


    /**
     * Used to see if the wildcard filename pattern is valid.
     *
     * @param  wildcardPattern  the wildcard filename pattern. 
     * 
     * @return  <code>true</code> iff the pattern is valid.
     */
    public static boolean isPatternValid(String wildcardPattern)
    {
        return Pattern.matches(FILENAME_PATTERN, wildcardPattern);
    }
    

    /**
     * Converts a valid wildcard filename pattern into a Java
     * regular expression. 
     *  
     * @param  wildcardPattern  the wildcard filename pattern. 
     * 
     * @return  The Java regular expression equivalent of the wildcard filename pattern.
     */
    public static Pattern toJavaPattern(String wildcardPattern)
    {
        assert isPatternValid(wildcardPattern) : "Pattern not valid";

        Matcher matcher;
        StringBuffer buffer;

        // convert unix non-escaped to regex escaped
        matcher = Pattern.compile(PUNCTUATION2).matcher(wildcardPattern);
        buffer = new StringBuffer();
        while( matcher.find() )
        {
            matcher.appendReplacement(buffer, REGEX_SLASH + matcher.group());
        }

        wildcardPattern = matcher.appendTail(buffer).toString();

        // convert unix escaped to regex non-escaped
        matcher = Pattern.compile(ESCAPED_PUNCTUATION2).matcher(wildcardPattern);
        buffer = new StringBuffer();
        while( matcher.find() )
        {
            matcher.appendReplacement(buffer, matcher.group());
        }

        wildcardPattern = matcher.appendTail(buffer).toString();

        // convert EXCEPTION_CHAR_CLASS
        matcher = Pattern.compile(EXCEPTION_CHAR_CLASS).matcher(wildcardPattern);
        buffer = new StringBuffer();
        while( matcher.find() )
        {
            String group1 = matcher.group(1);
            String group2 = matcher.group(3).replaceAll(REGEX_SLASH, REGEX_SLASH + REGEX_SLASH);
            matcher.appendReplacement(buffer, group1 + "^" + group2);
        }

        wildcardPattern = matcher.appendTail(buffer).toString();

        // convert WILDCARD_ONE + WILDCARD_ANY 
        wildcardPattern = wildcardPattern.replaceAll(WILDCARD_ONE, ".");
        wildcardPattern = wildcardPattern.replaceAll(WILDCARD_ANY, ".*");

        // return java regex of unix pattern
        return Pattern.compile(wildcardPattern);
    }
}
