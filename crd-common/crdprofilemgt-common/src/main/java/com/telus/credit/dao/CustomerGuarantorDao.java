/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.dao;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.service.dto.update.CreditProfileUpdates;


/**
 * 
 * <p><b>Description: </b> Abstracts access to the data source for CustomerGuarantor
 *  domain object.</p>
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

public interface CustomerGuarantorDao
{
    /**
     * <p><b>Description: </b>  Retrieves CustomerGuarantor domain objects from the datastore. </p>
     * @param guaranteeId customer id of guarantee.
     * @return CustomerGuarantor domain object.
     * @throws ObjectNotFoundException
     */
    public CustomerGuarantor getGuarantor(Integer guaranteeId)
            throws ObjectNotFoundException;


    /**
     * <p><b>Description: </b> Inserts CustomerGuarantor domain object into the data store.  </p>
     * @param customerGuarantor domain object.
     * @param userId of the person who inserts Customer Guarantor into underlying datastore.
     * @param sourceId of application which inserts Credit Value into underlying datastore.
     * @throws DuplicateKeyException
     */
    public void insertGuarantor(CustomerGuarantor customerGuarantor,
            String userId, Integer sourceId) throws DuplicateKeyException;


    /**
     * <p><b>Description: </b> Updates CustomerGuarantor in the data store with the new information 
     * provided by the caller. </p>
     * @param customerGuarantor
     * @param updates encapsulation of changes to CreditProfile
     * @param userId of the person who updates Customer Guarantor in underlying datastore.
     * @param sourceId of application which updates Credit Value in underlying datastore.
     * @throws ConcurrencyConflictException
     */
    public void updateGuarantor(CustomerGuarantor customerGuarantor,
            CreditProfileUpdates updates, String userId, Integer sourceId)
            throws ConcurrencyConflictException;


    /**
     * <p><b>Description: </b> Deletes CustomerGuarantor in the data store with 
     * by setting the EFF_STOP_DTM to current timestamp. </p>
     * @param id unique key for record.
     * @param userId of the person who deletes Customer Guarantor from underlying datastore.
     * @param sourceId of application which deletes Credit Value from underlying datastore.
     * @throws ConcurrencyConflictException
     */
    public void deleteGuarantor(long id, String userId, Integer sourceId)
            throws ConcurrencyConflictException;


    /**
     * <p><b>Description: </b> In the unmergeCreditProfile service, this 
     * operation transfer the guarantor records of the guarantor customer to 
     * the newly created credit profile. </p>
     * Calling function: CreditProfileMgtSvc.unMergeCreditProfile
     * <p>
     * @param guarantorCustId customer id of Guarantor.
     * @param originalCreditProfileId original CreditProfile id of guarantor.
     * @param updatedCreditProfileId new CreditProfile id of guarantor.
     * @param userId of the user who performs this operation.
     * @param sourceId of application which performs this operation
     * @throws ObjectNotFoundException
     * @throws ConcurrencyConflictException
     * @throws DuplicateKeyException   
     */
    public void transferUnmergedGuarantor(Integer guarantorCustId,
            Long originalCreditProfileId, Long updatedCreditProfileId,
            String userId, Integer sourceId)
            throws ObjectNotFoundException, ConcurrencyConflictException,
            DuplicateKeyException;


}