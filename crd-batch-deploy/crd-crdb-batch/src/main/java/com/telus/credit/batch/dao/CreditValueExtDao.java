/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.batch.dao;

import com.telus.credit.domain.CreditValue;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

/**
 *
 * <p>
 * <b>Description: </b> Abstracts access to the data source for CreditValue
 * domain object.
 * </p>
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
 * @author Roman Mikhailov
 *
 */

public interface CreditValueExtDao {
	/**
	 * <p>
	 * <b>Description: </b> Retrieves CreditValue domain object from the
	 * datastore.
	 * </p>
	 *
	 * @param creditProfileId
	 *            key for the credit profile where this CreditValue belong.
	 * @return CreditValue domain object that is a part of the customer's credit
	 *         profile.
	 * @throws ObjectNotFoundException
	 *             in case nothing is found.
	 */
	public CreditValue getCreditValue(Long creditProfileId)
			throws ObjectNotFoundException;

	/**
	 * <p>
	 * <b>Description: </b> Persists CreditValue domain object.
	 * </p>
	 *
	 * @param creditValue
	 *            domain object that will be inserted in the table.
	 * @param userId
	 *            of the person who creates Credit Value in the underlying
	 *            datastore.
	 * @param sourceId
	 *            of application which creates Credit Value in the underlying
	 *            datastore.
	 * @throws DuplicateKeyException
	 */
	public void insertCreditValue(CreditValue creditValue, String userId,
			Integer sourceId) throws DuplicateKeyException;

	/**
	 * <p>
	 * <b>Description: </b> Updates CreditValue in the data store with the new
	 * information provided by the caller.
	 * </p>
	 *
	 * @param creditValue
	 *            domain object that contains updated information.
	 * @param userId
	 *            of the person who updates Credit Value in the underlying
	 *            datastore.
	 * @param sourceId
	 *            of application which updates Credit Value in the underlying
	 *            datastore.
	 * @throws ConcurrencyConflictException
	 */
	public void updateCreditValue(CreditValue creditValue, String userId,
			Integer sourceId) throws ConcurrencyConflictException;

	public void updateCreditValue(CreditValue creditValue, String userId,
			Integer sourceId, Integer customerId)
			throws ConcurrencyConflictException, ObjectNotFoundException;

}