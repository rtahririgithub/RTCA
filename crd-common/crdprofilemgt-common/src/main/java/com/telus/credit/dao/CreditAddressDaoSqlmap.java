/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;

import com.telus.credit.domain.CreditAddress;
import com.telus.credit.service.dto.update.CreditProfileUpdates;


/**
 * 
 * <p>
 * <b>Description: </b> This class represents access to the data store for
 * CreditAddress domain object. (iBatis ORM framework implementation).
 * </p>
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

public class CreditAddressDaoSqlmap extends AbstractSqlMapDao
        implements
            CreditAddressDao
{
	
	private static Log log = LogFactory.getLog(CreditAddressDaoSqlmap.class);
	
    protected final static String STAT_ID_INSERT_CREDIT_ADDRESS = "credit_address.insert_credit_address";

    protected final static String STAT_ID_GET_CREDIT_ADDRESS = "credit_address.get_credit_address_by_credit_profile_id";

    protected final static String STAT_ID_DELETE_CREDIT_ADDRESS = "credit_address.delete_credit_address";



    /**
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>This method gets "active" credit address from CPROFL_ADDRESS
     * table by credit profile id.</li>
     * </ol>
     */
    public CreditAddress getAddress(Long creditProfileId)
            throws ObjectNotFoundException
    {
        return (CreditAddress) queryForObject( STAT_ID_GET_CREDIT_ADDRESS,
                creditProfileId );
    }


    /**
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Inserts object of type  CreditAddress into table CPROFL_ADDRESS table.</li>
     * </ol>
     */
    public void insertAddress(CreditAddress creditAddress, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        Map creditAddressMap = new HashMap();
        creditAddressMap.put( "credit_address", creditAddress );
        creditAddressMap.put( "userId", userId );
        creditAddressMap.put( "sourceId", sourceId );
        insert( STAT_ID_INSERT_CREDIT_ADDRESS, creditAddressMap );
    }


    /**
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Updates CPROFL_ADDRESS table with information from CreditAddress object.</li>
     * </ol>
     * 
     * @throws ConcurrencyConflictException
     */
    public void updateAddress(CreditAddress creditAddress, String userId,
            CreditProfileUpdates updates, Integer sourceId) throws ConcurrencyConflictException
    {
        try {
            if ( CreditProfileUpdates.MODIFIED.equals( updates
                    .getAddressModType() ) ) {
                deleteAddress( creditAddress.getCreditProfileId(), userId, sourceId);
            }
            else {
                // else is an ADD no need to delete current record
                // and set credit profile Id
                creditAddress.setCreditProfileId( updates.getCreditProfileId() );
            }
            // insert new record
            insertAddress( creditAddress, userId, sourceId );
        }
        catch ( DuplicateKeyException e ) {
            throw new ConcurrencyConflictException( e );
        }

    }


    /**
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Deletes CreditAddress by updating EFF_STOP_DTM with current
     * timestamp.</li>
     * </ol>
     */
    public void deleteAddress(long creditProfileId, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
    	try {
    		Map map = new HashMap();
    		map.put( "credit_profile_id", new Long( creditProfileId ) );
    		map.put( "userId", userId );
    		map.put( "sourceId", sourceId );
    		update( STAT_ID_DELETE_CREDIT_ADDRESS, map );
    	}
    	catch (ConcurrencyConflictException e)
        {
    		log.info( "Address not expired for credit profile id (ignored): " + creditProfileId + " because it does not exist.");
        }
    }


}