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
import com.telus.credit.batch.util.CreditProfileCustomerMap;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

/**
 * 
 * <p>
 * <b>Description: </b> Abstracts access to the data source.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>DAO interface for managing the persistence of CPROFL_CUSTOMER_MAP table IN BATCH PROCESS ONLY</li>
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

public interface CreditProfileCustomerMapDao
{
		
	/**
     * <p>
     * <b>Description: </b> get all profileIds by given customerId.
     * </p>
     * 
     * @param int customerId
     * @throws ObjectNotFoundException
     *         
     */
    public List getProfileListByCustomerId(String customerId,String mapType)
            throws ObjectNotFoundException;
    
    
	/**
     * <p>
     * <b>Description: </b> get all links that asssociated with given profileId.
     * </p>
     * 
     * @param int profileId
     * @throws ObjectNotFoundException
     *         
     */
    public List getAllLinksByProfileId(long profileId)
            throws ObjectNotFoundException;
	/**
     * <p>
     * <b>Description: </b> get all customer ids that point to either p1 or p2 as primary.
     * </p>
     * 
     * @param int profileId1
     * @param int profileId2
     * @throws ObjectNotFoundException
     *         
     */
    public List getCustomerListByProfileId(String profileId,String mapType)
            throws ObjectNotFoundException;
    
	/**
     * <p>
     * <b>Description: </b> Insert CreditProfileCustomerMap objects into CreditPDS.
     * </p>
     * 
     * @param CreditProfileCustomerMap
     * @throws DuplicateKeyException
     *         
     */
    public void insertCreditProfileCustomerMap(CreditProfileCustomerMap profileCustomerMap)
            throws DuplicateKeyException;


    /**
     * <p>
     * <b>Description: </b> Expire CPROFL_CUSTOMER_MAP in CreditPDS.
     * </p>
     * 
     * @param CreditProfileCustomerMap
     * @throws ObjectNotFoundException,ConcurrencyConflictException
     */
    public void expireCreditProfileCustomerMap(CreditProfileCustomerMap profileCustomerMap)
            throws ObjectNotFoundException,ConcurrencyConflictException;
    
    
    /**
     * <p>
     * <b>Description: </b> check if the object exists .
     * </p>
     * 
     * @param CreditProfileCustomerMap profileCustomerMap
     *         
     */
    public Integer checkCreditProfileCustomerMap(CreditProfileCustomerMap profileCustomerMap)
    throws ObjectNotFoundException; 
}
