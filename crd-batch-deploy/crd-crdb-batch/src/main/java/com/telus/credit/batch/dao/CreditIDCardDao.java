/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.batch.dao;

import com.telus.credit.domain.CreditIDCard;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ConcurrencyConflictException;

/**
 * 
 * <p>
 * <b>Description: </b> Abstracts access to the data source for CreditIDCard
 * domain object.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>DAO interface for managing the persistence of Credit_IDENTIFICATION table IN BATCH PROCESS ONLY</li>
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
 * @author x089748
 *  
 */

public interface CreditIDCardDao
{
	 /**
     * <p>
     * <b>Description: </b> Inserts CreditIDCard domain objects to the
     * datastore.
     * </p>
     * 
     * @param CreditIDCard domain object
     * @param userId
     * @param dataSrcId
     * @return void
     * @throws DuplicateKeyException
     *             
     */
	public void insertCreditIDCard(CreditIDCard creditIDCard,String userId,String dataSrcId)
    throws DuplicateKeyException,ConcurrencyConflictException;

}
