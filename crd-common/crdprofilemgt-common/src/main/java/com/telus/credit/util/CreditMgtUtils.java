/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.util;

import java.util.List;


/**
 * 
 * <p><b>Description: </b> Contains utility methods for credit management.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>All methods in this class are static.</li>		
 * 	</ul>
 *
 * <p><br><b>Revision History: </b></p>
 * <table border="1" width="100%">
 * 	<tr>
 * 		<th width="15%">Date</th>
 * 		<th width="15%">Revised By</th>
 * 		<th width="55%">Description</th>
 * 		<th width="15%">Reviewed By</th>
 * 	</tr>
 * 	<tr>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="55%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
 */
public class CreditMgtUtils
{

    private CreditMgtUtils()
    {
        // Exists only to defeat instantiation.
    }


    /**
     * <p><b>Description</b> Checks if two strings are equal. </p>
     * <p><b>High Level Design: </b></p>
     * Perform comparison if:
     * <ul>
     * 		<li>One of the strings is null.</li>
     * 		<li>Both strings are null.</li>
     * 		<li>Neither of two strings is null.</li>
     * </ul>
     * @param str1 to compare with str2.
     * @param str2 to compare with str1.
     * @return true if strings are equal; false otherwise.
     */
    public static boolean equals(String str1, String str2)
    {
        boolean result;
        if ( str1 == null && str2 == null ) {
            result = true;
        }
        else if ( str1 == null && str2 != null ) {
            result = false;
        }
        else if ( str1 != null && str2 == null ) {
            result = false;
        }
        else if ( !str1.equals( str2 ) ) {
            result = false;
        }
        else {
            result = true;
        }
        return result;
    }


    /**
     * Possibly-null object field.
     *
     * Includes type-safe enumerations and collections, but does not include
     * arrays.
     */
    static public boolean areEqual(Object aThis, Object aThat)
    {
        return aThis == null ? aThat == null : aThis.equals( aThat );
    }


    /**
     * <p><b>Description</b> Checks if the string is empty or null. </p>
     * @param str
     * @return true if the string is empty or null; false otherwise.
     */
    public static boolean isEmptyOrNull(String str)
    {
        boolean result = false;
        if ( str == null || str.trim().length() < 1 ) {
            result = true;
        }
        return result;
    }

    public static boolean areListEquals( List aList, List bList ) {
        boolean result=true;
        if ( aList == null && bList == null ) {
            result = true;
        }
        else if ( aList == null && bList != null ) {
            result = false;
        }
        else if ( aList != null && bList == null ) {
            result = false;
        }
        else {
            if ( aList.size() != bList.size() ) {
                result = false;
            }
            else {
                for ( int i = 0; i < aList.size(); i++ ) {
                    boolean matchFound=false;
                    for ( int j = 0; j < bList.size(); j++ ) {
                        if ( areEqual( bList.get( i ), bList.get(j) ) ) {
                            matchFound=true;
                            break;
                        }
                    }
                    if (!matchFound) {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }
}
