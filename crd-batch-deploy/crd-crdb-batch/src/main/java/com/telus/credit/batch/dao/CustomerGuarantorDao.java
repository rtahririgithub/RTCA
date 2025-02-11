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

import java.util.List;


import com.telus.credit.domain.CustomerGuarantor;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
//import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

/**
 * 
 * <p>
 * <b>Description: </b> Abstracts access to the data source for CustomerGuarantor
 * domain object.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>DAO interface for managing the persistence of Customer_Guarantor table IN BATCH PROCESS ONLY</li>
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

public interface CustomerGuarantorDao
{
	 /**
     * <p>
     * <b>Description: </b> Retrieves CustomerGuarantor domain objects from the
     * datastore.
     * </p>
     * 
     * @param profileId
     * @return CustomerGuarantor domain object
     * @throws ObjectNotFoundException
     *             to indicate that CustomerGuarantor for this object can not be
     *             found.
     */
    public List getCustomerGuarantor(String profileId)
            throws ObjectNotFoundException;


    /**
     * <p>
     * <b>Description: </b> Updates CustomerGuarantor in the data store with the new
     * information provided by the caller.
     * </p>
     * 
     * @param CustomerGuarantor
     *            domain object
     * @throws ConcurrencyConflictException
     */
    public void expireCustomerGuarantor(CustomerGuarantor customerGuarantor,String userId)
            throws ConcurrencyConflictException;
    
    /**
     * <p>
     * <b>Description: </b> Insert CustomerGuarantor objects into CreditPDS.
     * </p>
     * 
     * @param CustomerGuarantor,domain object
     * @throws DuplicateKeyException
     *         
     */
    public void insertCustomerGuarantor(CustomerGuarantor customerGuarantor,String userId,String dataSrcId)
            throws DuplicateKeyException;
    
    /**
     * <p>
     * <b>Description: </b> update Credit Profile after updating Customer Guarantor.
     * </p>
     * 
     * @param String guaranteedCustomerId
     * @throws ConcurrencyConflictException
     *         
     */
    public void updateCreditProfileByGuarantor(String guaranteedCustomerId,String userId)
            throws ConcurrencyConflictException;

}
