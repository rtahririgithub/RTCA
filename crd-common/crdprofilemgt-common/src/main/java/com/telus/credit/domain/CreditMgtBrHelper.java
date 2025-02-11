/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.domain;


import java.util.Collection;
import java.util.Iterator;

import com.telus.erm.referenceods.domain.ReferenceDecode;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;


/**
 * 
 * <p><b>Description: </b> Contains helper methods for Business Rules classes.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>All methods in this class are static.</li>		
 * 	</ul>
 *
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
public class CreditMgtBrHelper
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private CreditMgtBrHelper()
    {
        //Exists only to defeat instantiation.
    }


    /**
     * <p><b>Description</b> Validates code by looking it up in the codes table. </p>
     * @param codeValue
     * @param category
     * @return true if the code is valid; false otherwise. 
     */
    public static boolean isValidCode(String codeValue, String category)
    {   
        boolean foundMatch = false;
        
        Collection codeList = ReferencePdsAccess.getView( category, "EN" );
        A: for(Iterator i = codeList.iterator(); i.hasNext();){
            ReferenceDecode refDecode = (ReferenceDecode)i.next();
            if(refDecode.getCode().equals(codeValue)){
                foundMatch = true;
                break A;
            }
        }        

        return foundMatch;
    }


}
