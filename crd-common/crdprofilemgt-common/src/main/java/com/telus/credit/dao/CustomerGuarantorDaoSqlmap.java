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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;

import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.service.dto.update.CreditProfileUpdates;


/**
 * 
 * <p><b>Description: </b> This class represents access to the data store for CustomerGuarantor domain object. 
 * It is implemented using iBatis ORM framework.</p>
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

public class CustomerGuarantorDaoSqlmap extends AbstractSqlMapDao
        implements
            CustomerGuarantorDao
{
    protected final static String STAT_ID_INSERT_CUSTOMER_GUARANTOR = 
        "customer_guarantor.insert_customer_guarantor";

    protected final static String STAT_ID_DELETE_CUSTOMER_GUARANTOR = 
        "customer_guarantor.delete_customer_guarantor";

    protected final static String STAT_ID_GET_GUAR_BY_CUST_N_CREDIT = 
        "customer_guarantor.get_guar_by_cust_n_credit";

    protected final static String STAT_ID_UPDATE_GUAR_STOPDATE = 
        "customer_guarantor.update_guar_stop_date";

    protected final static String STAT_ID_GET_CUSTOMER_GUARANTOR_BY_GUARANTEE_ID = 
        "customer_guarantor.get_customer_guarantor_by_guarantee_id";



    /* (non-Javadoc)
     * @see com.telus.credit.dao.CustomerGuarantorDao#getGuarantor(java.lang.Integer)
     */
    public CustomerGuarantor getGuarantor(Integer guaranteeId)
            throws ObjectNotFoundException
    {
        return (CustomerGuarantor) queryForObject(
                STAT_ID_GET_CUSTOMER_GUARANTOR_BY_GUARANTEE_ID, guaranteeId );
    }


    /* (non-Javadoc)
     * @see com.telus.credit.dao.CustomerGuarantorDao#insertGuarantor(com.telus.credit.domain.CustomerGuarantor)
     */
    public void insertGuarantor(CustomerGuarantor customerGuarantor,
            String userId, Integer sourceId) throws DuplicateKeyException
    {
        Map map = new HashMap();
        map.put( "customer_guarantor", customerGuarantor );
        map.put( "userId", userId );
        map.put( "sourceId", sourceId );
        insert( STAT_ID_INSERT_CUSTOMER_GUARANTOR, map );
    }


    /* (non-Javadoc)
     * @see com.telus.credit.dao.CustomerGuarantorDao#updateGuarantor(com.telus.credit.domain.CustomerGuarantor)
     */
    public void updateGuarantor(CustomerGuarantor guarantor,
            CreditProfileUpdates updates, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
        try {
            if ( CreditProfileUpdates.MODIFIED.equals( updates
                    .getGuarantorModType() ) ) {
                deleteGuarantor( guarantor.get_id(), userId, sourceId );
            }
            // else is an ADD and no need to delete current record
            insertGuarantor( guarantor, userId, sourceId );
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
    public void deleteGuarantor(long id, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
        Map map = new HashMap();
        map.put( "id", new Long( id ) );
        map.put( "userId", userId );
        map.put( "sourceId", sourceId );
        update( STAT_ID_DELETE_CUSTOMER_GUARANTOR, map );
    }


    /**
     * <p><b>Description: </b> See interface CustomerGuarantorDao</p>
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Retrieve guarantor list.</li>
     * 		<li>Expire each guarantor record in the guarantor list.</li>
     * 		<li>For each guarantor record expired in the previous step,
     * 			create a new guarantor record with same data except the 
     * 			CREDIT_PROFILE_ID field which will now contain the newly created
     * 			credit profile id.</li>
     * </ol>
     */
    public void transferUnmergedGuarantor(Integer guarantorCustId,
            Long expireCreditProfileId, Long newCreditProfileId, String userId, Integer sourceId)
            throws ConcurrencyConflictException, DuplicateKeyException
    {
        //Retrieve guarantor list.
        Map paramMap = new HashMap();
        paramMap.put( "guarantorCustId", guarantorCustId );
        paramMap.put( "creditProfileId", expireCreditProfileId );
        List guarantorList = getSqlMapClientTemplate().queryForList(
                STAT_ID_GET_GUAR_BY_CUST_N_CREDIT, paramMap );

        if ( guarantorList != null ) {
            for ( Iterator iter = guarantorList.iterator(); iter.hasNext(); ) {

                //Expire guarantor record in the guarantor list.
                CustomerGuarantor guarantor = (CustomerGuarantor) iter.next();
                Map paramMap2 = new HashMap();
                paramMap2.put( "guarantorIdPrimaryKey", new Long( guarantor
                        .get_id() ) );
                paramMap2.put( "userId", userId );
                paramMap2.put( "sourceId", sourceId );
                update( STAT_ID_UPDATE_GUAR_STOPDATE, paramMap2 );

                //set new credit profile id
                guarantor.setGuarantorCreditProfileId( newCreditProfileId
                        .longValue() );

                //create new guarantor record with same data except now it 
                //  contains the new credit profile id
                insertGuarantor( guarantor, userId, sourceId );
            }
        }
    }


}