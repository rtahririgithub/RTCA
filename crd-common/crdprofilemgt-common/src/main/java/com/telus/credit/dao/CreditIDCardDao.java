/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.dao;

import java.util.List;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.service.dto.update.CreditProfileUpdates;


/**
 * 
 * <p><b>Description: </b> Abstracts access to the data source 
 * for CreditIDCard domain object.</p>
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

public interface CreditIDCardDao
{
    /**
     * <p><b>Description: </b> Retrieves CreditIDCard domain objects from the datastore. </p>
     * @param creditProfileId id for the credit profile where this CreditIDCards belong
     * @return list of CreditIDCard domain objects that are part of the customer's credit profile
     * @throws ObjectNotFoundException   
     */
    public List getIdCards(Long creditProfileId) throws ObjectNotFoundException;


    /**
     * <p><b>Description: </b> Persists CreditIDCard domain object. </p>
     * @param idCard represents identification card (SIN, DL, etc)
     * @param userId id of the person who performs insert.
     * @param sourceId of application which inserts new id card in the datastore. 
     * @throws DuplicateKeyException   
     */
    public void insertIdCard(CreditIDCard idCard, String userId, Integer sourceId)
            throws DuplicateKeyException;


    /**
     * <p><b>Description: </b> Updates CreditIDCard in the data store with 
     * the new information provided by the caller.</p>
     * @param updates hold the modified credit profile
     * @param userId id of the person who performs update.
     * @param sourceId of application which updates id card in the datastore. 
     * @throws ConcurrencyConflictException   
     */
    public void updateIdCards(CreditProfileUpdates updates, String userId, Integer sourceId)
            throws ConcurrencyConflictException;


    /**
     * <p><b>Description: </b> Finds all consumer customer ids that belong to the customers whose idCards 
     * match idCard provided by the caller.</p>
     * @param idCard represents identification card (SIN, DL, etc).  
     * @return List of customerId objects.
     */
    public List getConsumerCustomerIdByMatchingCreditIdCard(CreditIDCard idCard);
	
	/**
     * <p><b>Description: </b> Finds all consumer customer ids that belong to the customers whose idCards 
     * match idCard provided by the caller.</p>
     * @param idCard represents identification card (SIN, DL, etc).  
     * @return List of customerId objects.
     */
	public List getConsumerCustomerIdByMatchingCreditIdCardWithOutProv(CreditIDCard idCard);
}