/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 */
package com.telus.credit.batch.dao;

import com.telus.credit.domain.CreditProfile;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;

/**
 * 
 * <p>
 * <b>Description: </b>  Abstracts access to the CPROFL_STATUS in creditPDS for BATCH PROCESS ONLY . 
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Lei Fan(x089748)
 *  
 */
public interface CreditStatusDao 
{
	
    /**
     * <p>
     * <b>Description: </b> inserts the credit status.
     * </p>
     * 
     * @param creditProfile domain object
     * @throws ConcurrencyConflictException
     */
	public void insertCreditStatus(CreditProfile creditProfile,String userId,String dataSrcId) throws DuplicateKeyException,ConcurrencyConflictException;
	

	 /**
     * <p>
     * <b>Description: </b> expires credit status.
     * </p>
     * 
     * @param creditProfile domain object
     * @throws ConcurrencyConflictException
     */
    public void expireCreditStatus(CreditProfile creditProfile,String userId) throws ConcurrencyConflictException;
    

}
