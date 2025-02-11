/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.dao;


import com.telus.credit.domain.CreditAddress;
import com.telus.credit.service.dto.update.CreditProfileUpdates;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;


/**
 * 
 * <p><b>Description: </b> Abstracts access to the data source for 
 * CreditAddress domain object.</p>
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

public interface CreditAddressDao
{
    /**
     * <p><b>Description: </b> Retrieves CreditAddress domain object from the datastore. </p>
     * @param creditProfileId key for the credit profile where this CreditAddress belong.
     * @return CreditAddress domain object that has represents address of the owner of CreditProfile.
     * @throws ObjectNotFoundException in case there is no record in the database.
     */
    public CreditAddress getAddress(Long creditProfileId)
            throws ObjectNotFoundException;

    
    /**
     * <p><b>Description: </b> Persists CreditAddress domain object. </p>
     * @param creditAddress domain object that has represents address of the owner of CreditProfile.
     * @param userId of person who creates address record in the datastore. 
     * @param sourceId of application that creates address record in the datastore. 
     * @throws DuplicateKeyException is thrown if this address already exist in the database and 
     * it is effective address (not expired).
     */
    public void insertAddress(CreditAddress creditAddress, String userId, Integer sourceId)
            throws DuplicateKeyException;


    /**
     * <p><b>Description: </b> Updates CreditAddress in the data store with 
     * the new information provided by the caller.</p>
     * @param creditAddress domain object that has represents address of the owner of CreditProfile.
     * @param userId id of the person who performs update.
     * @param updates encapsulation of modifications to CreditAddress.
     * @param sourceId of application that updates address record in the datastore. 
     * @throws ConcurrencyConflictException 
     */
    public void updateAddress(CreditAddress creditAddress, String userId,
            CreditProfileUpdates updates, Integer sourceId)
            throws ConcurrencyConflictException;


    /**
     * <p><b>Description: </b> Deletes CreditAddress in the data store by 
     * setting EFF_STOP_DTM to CURRENTTIMESTAMP.</p>
     * @param creditProfileId
     * @param userId id of the person who performs update.
     * @param sourceId of application that deletes address record from the datastore. 
     * @throws ConcurrencyConflictException   
     */
    public void deleteAddress(long creditProfileId, String userId, Integer sourceId)
    	throws ConcurrencyConflictException;
}