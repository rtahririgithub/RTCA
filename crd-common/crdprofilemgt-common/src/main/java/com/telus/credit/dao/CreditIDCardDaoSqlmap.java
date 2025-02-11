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
import java.util.List;
import java.util.Map;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.persistence.AbstractSqlMapDao;

import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.service.dto.update.CreditProfileUpdates;


/**
 * 
 * <p><b>Description: </b> This class represents access to the data store 
 *  for CreditIDCard domain object. 
 *  (iBatis implementation).</p>
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

public class CreditIDCardDaoSqlmap extends AbstractSqlMapDao
        implements
            CreditIDCardDao
{
    protected final static String STAT_ID_INSERT_CREDIT_ID_CARD = 
        "credit_id_card.insert_credit_id_card";

    protected final static String STAT_ID_GET_CREDIT_ID_CARDS = 
        "credit_id_card.get_credit_id_cards_by_credit_profile_id";

    protected final static String STAT_ID_DELETE_CREDIT_ID_CARD = 
        "credit_id_card.delete_id_card";

    protected final static String STAT_ID_GET_CONSUMER_CUSTOMER_ID_BY_MATCHING_CREDIT_ID_CARD = 
        "credit_id_card.get_consumer_customer_id_by_matching_credit_id_card";

    protected final static String STAT_ID_GET_CONSUMER_CUSTOMER_ID_BY_MATCHING_CREDIT_ID_CARD_WITHOUT_PROV = 
        "credit_id_card.get_consumer_customer_id_by_matching_credit_id_card_without_prov";

    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			Gets idCards from CPROFL_IDENTIFICATION table.
     * 		</li> 	
     * </ol>
     */
    public List getIdCards(Long creditProfileId)
    {
        return queryForList( STAT_ID_GET_CREDIT_ID_CARDS, creditProfileId );
    }


    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Inserts into table CPROFL_IDENTIFICATION.</li>
     * </ol>
     */
    public void insertIdCard(CreditIDCard idCard, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        Map creditIDCardMap = new HashMap();
        creditIDCardMap.put( "credit_id_card", idCard );
        creditIDCardMap.put( "userId", userId );
        creditIDCardMap.put( "sourceId", sourceId );
        insert( STAT_ID_INSERT_CREDIT_ID_CARD, creditIDCardMap );
    }


    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Updates CPROFL_IDENTIFICATION table.</li>
     * </ol>
     */
    public void updateIdCards(CreditProfileUpdates updates, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
        List updatedIds = updates.getIdCardUpdates();

        for ( int i = 0; i < updatedIds.size(); i++ ) {
            Map updatedCardMap = (Map) updatedIds.get( i );

            String modType = (String) updatedCardMap
                    .get( CreditProfileUpdates.MOD_TYPE_KEY );
            CreditIDCard idCard = (CreditIDCard) updatedCardMap
                    .get( CreditProfileUpdates.ID_CARD_KEY );

            try {
                if ( CreditProfileUpdates.MODIFIED.equals( modType ) ) {
                    deleteIdCard( idCard, userId, sourceId );
                    insertIdCard( idCard, userId, sourceId );
                }
                else if ( CreditProfileUpdates.DELETED.equals( modType ) ) {
                    deleteIdCard( idCard, userId, sourceId );
                }
                else {
                    // is an ADD
                    idCard.setCreditProfileId( updates.getCreditProfileId() );
                    insertIdCard( idCard, userId, sourceId );
                }
            }
            catch ( DuplicateKeyException e ) {
                throw new ConcurrencyConflictException( e );
            }
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
    private void deleteIdCard(CreditIDCard idCard, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
        Map map = new HashMap();
        map.put( "credit_id_card", idCard );
        map.put( "userId", userId );
        map.put( "sourceId", sourceId );
        update( STAT_ID_DELETE_CREDIT_ID_CARD, map );
    }


    /**
     * <p><b>Description: </b> Finds all customer ids that belong to 
     * the customers whose idCards 
     * match idCard provided by the caller.</p>
     * @param idCard represents identification card (SIN, DL, etc).  
     * @return List of customerId objects.
     */
    public List getConsumerCustomerIdByMatchingCreditIdCard(CreditIDCard idCard)
    {
        return queryForList( STAT_ID_GET_CONSUMER_CUSTOMER_ID_BY_MATCHING_CREDIT_ID_CARD,
                idCard );
    }
	
	
	
    /**
     * <p><b>Description: </b> Finds all customer ids that belong to 
     * the customers whose idCards 
     * match idCard provided by the caller.</p>
     * @param idCard represents identification card (SIN, DL, etc).  
     * @return List of customerId objects.
     */
    public List getConsumerCustomerIdByMatchingCreditIdCardWithOutProv(CreditIDCard idCard)
    {
        return queryForList( STAT_ID_GET_CONSUMER_CUSTOMER_ID_BY_MATCHING_CREDIT_ID_CARD_WITHOUT_PROV,
                idCard );
    }
}