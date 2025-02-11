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


//import org.springframework.dao.DataAccessException;

import com.telus.credit.domain.CreditValue;

import com.telus.framework.exception.ConcurrencyConflictException;
//import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

/**
 * 
 * <p>
 * <b>Description: </b> Abstracts access to the data source for CreditValue
 * domain object.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>DAO interface for managing the persistence of Credit_Value table IN BATCH PROCESS ONLY</li>
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

public interface CreditValueDao
{
	 /**
     * <p>
     * <b>Description: </b> Retrieves CreditValue domain objects from the
     * datastore.
     * </p>
     * 
     * @param profileId
     * @return CreditValue domain object
     * @throws ObjectNotFoundException
     *             to indicate that CreditValue for this object can not be
     *             found.
     */
    public CreditValue getCreditValue(int profileId)
            throws ObjectNotFoundException;


    /**
     * <p>
     * <b>Description: </b> Updates CreditValue in the data store with the new
     * information provided by the caller.
     * </p>
     * 
     * @param creditValue
     *            domain object
     * @throws ConcurrencyConflictException
     */
    public void updateCreditValue(CreditValue creditValue)
            throws ConcurrencyConflictException;
}
