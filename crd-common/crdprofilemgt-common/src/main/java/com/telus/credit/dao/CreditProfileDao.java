/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;

import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.service.dto.CreditProfileDto;
import com.telus.credit.service.dto.update.CreditProfileUpdates;


/**
 * 
 * <p>
 * <b>Description: </b> Abstracts access to the data source for CreditProfile
 * domain object.
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

public interface CreditProfileDao
{

    /**
     * <p>
     * <b>Description: </b> Retrieves CreditProfile object from the
     * datastore.
     * </p>
     * 
     * @param customerId for the customer whose CreditProfile we get.
     * @return CreditProfile domain object.
     * @throws ObjectNotFoundException to indicate that "active" CreditProfile. 
     * for this customer can not be found.
     */
    public CreditProfile getCreditProfile(Integer customerId)
            throws ObjectNotFoundException;


    /**
     * <p>
     * <b>Description: </b> Updates CreditProfile in the data store with the new
     * information provided by the caller.
     * </p>
     * @param creditProfile that is being updated.
     * @param userId of the person who updates creditProfile.
     * @param updates is an encapsulation of modified profile.
     * @param sourceId of application which updates creditProfile in the datastore. 
     * @throws ConcurrencyConflictException
     */
    public void updateCreditProfile(CreditProfile creditProfile, String userId,
            CreditProfileUpdates updates, Integer sourceId) throws ConcurrencyConflictException;


    /**
     * <b>Description: </b> Gets id of the "primary" credit profile for the customer
     * whose customerId is supplied by the caller.
     * 
     * @param customerId - Customer Id of customer
     * @return Credit Profile Id
     * @throws ObjectNotFoundException
     */
    public Long getPrimaryCreditProfileIdByCustomerId(Integer customerId)
            throws ObjectNotFoundException;


    /**
     * <b>Description: </b> Gets customer id
     * by id of his "primary" credit profile.
     * 
     * @param primaryCreditProfileId
     * @return Customer Id
     * @throws ObjectNotFoundException
     */
    public Integer getCustomerIdByPrimaryCreditProfileId(
            Long primaryCreditProfileId) throws ObjectNotFoundException;


    /**
     * <b>Description: </b> Get the CreditProfileDto array 
     * given the primary credit profile id.  It must return an array
     * to support merged customers where multiple customerIds have
     * the same primary credit profile. 
     * @param primaryCreditProfileId
     * @return array of CreditProfileDto objects
     */
    public CreditProfileDto[] getCreditProfileDtoByCreditProfileId(
            Long primaryCreditProfileId);
    
    
    /**
     * <b>Description: </b> new search operation to support the customer 
     * search by credit profile data,birth date and credit ids
     * @param List<Long> customerIds
     * @param Date BirthDT
     * @param Integer numOfIds
     * @param CreditIDCard sinIdCard
     * @param CreditIDCard dlIdCard
     * @param CreditIDCard hcIdCard
     * @param CreditIDCard pspIdCard
     * @param CreditIDCard prvIdCard
     * @return array of CreditProfileDto objects
     */
    public CreditProfileDto[] getCreditProfileDtoByCreditProfileAndCustomerIds(
    		List<Long> customerIds, Date birthDt, Integer numOfIds,
    		CreditIDCard sinIdCard, CreditIDCard dlIdCard,
    		CreditIDCard hcIdCard, CreditIDCard pspIdCard,CreditIDCard prvIdCard);


    /**
     * <b>Description: </b> Breaks the links between a customer record and all
     * credit profiles associated with it.  
     * <p>
     * <b>High Level Design: </b> Expire all the links between a customer
     * record and all associated credit profiles.
     * 
     * @param customerId Customer Id of customer
     * @param userId of the person who performs this operation.
     * @param sourceId of application which unlinks creditProfile(s).
     * @throws ObjectNotFoundException
     * @throws ConcurrencyConflictException
     */
    public void unlinkAllCreditProfiles(Integer customerId, String userId, Integer sourceId)
            throws ObjectNotFoundException, ConcurrencyConflictException;


    /**
     * <p>
     * <b>Description </b> Persist CreditProfile Object to the datastore.
     * </p>
     * @param creditProfile object.
     * @param userId of the person who inserts Credit Profile.
     * @param sourceId of application which inserts Credit Profile into the datastore.
     * @throws DuplicateKeyException
     * @throws ObjectNotFoundException
     */
    public Long insertCreditProfile(CreditProfile creditProfile, String userId, Integer sourceId)
            throws DuplicateKeyException, ObjectNotFoundException;


    /**
     * <p>
     * <b>Description </b> Links Credit Profile to Customer.
     * </p>
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Insert into table CPROFL_CUSTOMER_MAP</li>
     * <li>Used to create links to both primary and secondary
     * credit profiles</li> 
     * </ol>
     * 
     * @param creditProfileId
     * @param matchedCustomerId
     * @param mappingType indicates if credit profile is primary or secondary to the customerId
     * @param userId of the person who adds Credit Profile.
     * @param sourceId of application which adds Credit Profile.
     * @throws DuplicateKeyException
     */
    public void addCreditProfileToCustomer(Long creditProfileId,
            int matchedCustomerId, String mappingType, String userId, Integer sourceId)
            throws DuplicateKeyException;


    /**
     * <p>
     * <b>Description: </b> This method returns an array of customer ids whose
     * credit profiles match credit profile of a given customer
     * </p>
     * 
     * @param customerId represent customer for who we find matching customers
     * @return customer ids for the customers whose credit profiles match the
     *         credit profile of the given customer.
     */
    public Integer[] getMatchingCustomerIds(Integer customerId);


    /**
     * <p>
     * <b>Description </b> Maintain credit profile mapping information by 
     * creating record in credit profile mapping table to indicate that the 
     * newly created blank credit profile is "unmerged" from the existing
     * credit profile. </p>
     * Calling function: CreditProfileMgtSvc.unMergeCreditProfile
     * </p>  
     * @param creditProfileId identifies original Credit Profile
     * @param newCreditProfileId identifies updated Credit Profile
     * @param userId of the person who inserts "unmerge" mapping(s).
     * @param sourceId of application which inserts "unmerge" mapping(s).
     * @throws DuplicateKeyException   
     */
    public void insertUnmergeMapping(Long creditProfileId,
            Long newCreditProfileId, String userId, Integer sourceId)
            throws DuplicateKeyException;


    /**
     * Gets the last update timestamp on the CREDIT_PROFILE table 
     * @param creditProfileId
     * @return Timestamp of the last update
     * @throws ObjectNotFoundException   
     */
    public Timestamp getBusinessLastUpdateTimestamp(Long creditProfileId)
            throws ObjectNotFoundException;


    /**
     * Deletes a secondary link in the "Credit Profile to Customer" mapping table. 
     * @param creditProfileId
     * @param customerId
     * @param userId of the person who deletes "secondary" links.
     * @param sourceId of application which deletes "secondary" links.
     * @throws ConcurrencyConflictException   
     */
    public void deleteSecondaryLinks(Long creditProfileId, Integer customerId,
            String userId, Integer sourceId) throws ConcurrencyConflictException;
    
    /**
     * Updates the comment. 
     * @param creditProfileId
     * @param formatCd
     * @param commentTxt
     * @param userId of the person who updates the comment.
     * @param sourceId of application which updates the comment.
     * @throws ConcurrencyConflictException   
     */
    public void updateComment(long creditProfileId, String formatCd, String commentTxt, String userId,
			Integer sourceId) throws ConcurrencyConflictException;
    
}