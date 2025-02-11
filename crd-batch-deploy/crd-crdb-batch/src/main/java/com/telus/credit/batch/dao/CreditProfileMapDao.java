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

import com.telus.credit.batch.util.CreditProfileMap;
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
 * <li>DAO interface for managing the persistence of CPROFL_MAPPING table IN BATCH PROCESS ONLY</li>
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

public interface CreditProfileMapDao
{
	 /**
     * <p>
     * <b>Description: </b> Insert CreditProfileMap objects into CreditPDS.
     * </p>
     * 
     * @param  CreditProfileMap
     * @throws DuplicateKeyException
     *         
     */
    public void insertCreditProfileMap(CreditProfileMap profileMap)
            throws DuplicateKeyException;
    
	 /**
     * <p>
     * <b>Description: </b> get ownership between consumer and business from creditPDS
     * </p>
     * 
     * @param profileId- consumer credit profile for proprietor
     * @return list of CreditProfileMap object
     * @throws ObjectNotFoundException
     *           
     */
    public List getBusinessOwnershipByProfileId(String profileId)
            throws ObjectNotFoundException;
    

    /**
     * <p>
     * <b>Description: </b> expire BusinessOwnership by updating time stamp after Merge
     * </p>
     * 
     * @param CreditProfileMap object
     * @throws ConcurrencyConflictException
     */
    public void expireBusinessOwnership(CreditProfileMap creditProfileMap)
            throws ConcurrencyConflictException;
        
}

