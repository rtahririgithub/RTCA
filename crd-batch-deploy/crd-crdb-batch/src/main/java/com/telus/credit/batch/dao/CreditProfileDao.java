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

import com.telus.credit.domain.CreditProfile;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;


/**
 * 
 * <p>
 * <b>Description: </b> Abstracts access to the data source for CreditProfile
 * domain object.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>DAO interface for managing the persistence of Credit_Profile table IN BATCH PROCESS ONLY</li>
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

public interface CreditProfileDao
{
	 /**
     * <p>
     * <b>Description: </b> Retrieves CreditProfile domain objects from the
     * datastore.
     * </p>
     * 
     * @param profileId
     * @return CreditProfile domain object
     * @throws ObjectNotFoundException
     *             to indicate that CreditProfile for this object can not be
     *             found.
     */
    public CreditProfile getCreditProfile(long profileId)
            throws ObjectNotFoundException;


    /**
     * <p>
     * <b>Description: </b> Updates CreditProfile in the data store with the new
     * information provided by the caller.
     * </p>
     * 
     * @param creditProfile domain object
     * @param String userId
     * @param String dataSrcId
     * @throws ConcurrencyConflictException
     */
    public void updateCreditProfile(CreditProfile creditProfile,String userId,String dataSrcId)
            throws ConcurrencyConflictException,DuplicateKeyException ;
}