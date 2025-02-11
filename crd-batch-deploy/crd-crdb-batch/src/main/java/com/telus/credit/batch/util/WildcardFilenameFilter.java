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

/*
 * Created on 20-Sep-2005
 *
 * Class is created to facilitate multiple files processing in
 * a single job module.
 * Class must be extended to implement methods
 * readBatchHeader(), readRequest(), readBatchTrailer()
 */

package com.telus.credit.batch.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

/**
 * Class filters files based on a wildcard filename pattern.
 * 
 * <p>
 * <b>NOTE:</b>
 * Currently only UNIX wildcard filename patterns are supported.
 * </p>
 */
public class WildcardFilenameFilter implements FilenameFilter
{
    private Pattern m_pattern;


    /**
     * Creates a filter to find the files whose filenames
     * match the wildcard filename pattern.
     *
     * @param  wildcardPattern  the wildcard filename pattern. 
     */
    public WildcardFilenameFilter(String wildcardPattern)
    {
        m_pattern = WildcardRegex.toJavaPattern(wildcardPattern);
    }


    /*
     * (non-Javadoc)
     */
    public boolean accept(File dir, String filename)
    {
        return m_pattern.matcher(filename).matches();
    }
}
