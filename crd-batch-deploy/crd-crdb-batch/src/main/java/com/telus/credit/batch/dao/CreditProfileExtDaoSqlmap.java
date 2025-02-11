/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.batch.dao;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;


import com.telus.credit.domain.CreditProfileExt;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditValueExt;
import com.telus.credit.domain.CreditValue;

import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.batch.dto.CreditProfileDto;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * <p>
 * <b>Description: </b> This class represents access to the data store for
 * CreditProfile domain object. (iBatis implementation).
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

public class CreditProfileExtDaoSqlmap extends AbstractSqlMapDao
        implements
            CreditProfileExtDao
{

    private static Log log = LogFactory.getLog(CreditProfileDaoSqlmap.class);

    /**
     * Mapped statement Id for getting customer ids for matched customers.
     *
     */
    protected final static String STAT_ID_GET_MATCHING_CUSTOMER_IDS = "credit_profile.get_matching_customer_ids";
    protected final static String STAT_ID_GET_CREDIT_PROFILE = "credit_profile.get_credit_profile_by_credit_profile_id";
    protected final static String STAT_ID_GET_PRIMARY_CREDIT_PROFILE_ID_BY_CUSTOMER_ID = "credit_profile.get_primary_credit_profile_id_by_customer_id";
    protected final static String STAT_ID_INSERT_CREDIT_PROFILE = "credit_profile.insert_credit_profile";
    protected final static String STAT_ID_INSERT_CREDIT_PROFILE_CHARSTC_INDVDL = "credit_profile.insert_credit_profile_charstc_indvdl";
    protected final static String STAT_ID_INSERT_CREDIT_PROFILE_STATUS = "credit_profile.insert_credit_profile_status";
    protected final static String STAT_ID_GET_CUSTOMER_GUARANTOR_BY_GUARANTEE_ID = "customer_guarantor.get_customer_guarantor_by_guarantee_id";
    protected final static String STAT_ID_INSERT_LINK_INTO_MAPPING_TABLE = "credit_profile.insert_link_into_customer_credit_profile_mapping_table";
    protected final static String STAT_ID_GET_BUSINESS_LAST_UPDATE_TIMESTAMP_BY_CREDIT_PROFILE_ID = "credit_profile.get_business_last_update_timestamp_by_credit_profile_id";
    protected final static String STAT_ID_UPDATE_CREDIT_PROFILE = "credit_profile.update_credit_profile";
    protected final static String STAT_ID_DELETE_CHARSTC_INDVDL = "credit_profile.delete_charstc_indvdl";
    protected final static String STAT_ID_GET_CUSTOMER_ID_BY_PRIMARY_CREDIT_PROFILE_ID = "credit_profile.get_customer_id_by_primary_credit_profile_id";
    protected final static String STAT_ID_UPDATE_CREDITPROFILE_TIMESTAMP_STMT = "credit_profile.updateCreditProfileTimeStamp";
    
    // Not used
    //protected final static String STAT_ID_GET_CUSTOMER_ID_BY_MATCHING_CREDIT_FIELDS = "credit_profile.get_customer_id_by_matching_credit_fields";
    protected final static String STAT_ID_DELETE_SECONDARY_LINKS = "credit_profile.delete_secondary_links";
    protected final static String STAT_ID_UPDATE_UNLINK_ALL_CREDIT_PROFILES = "credit_profile.unlink_all_credit_profiles";
    protected final static String STAT_ID_INSERT_UNMERGE_MAPPING = "credit_profile.insert_unmerge_mapping";
    protected final static String STAT_ID_GET_CREDIT_PROFILE_DTO_BY_CREDIT_PROFILE_ID = "credit_profile.get_dto_by_credit_profile_id";
    protected final static String STAT_ID_DELETE_CRDMGT_COMMENT = "credit_profile.delete_crdmgt_comment";
    protected final static String STAT_ID_INSERT_CRDMGT_COMMENT = "credit_profile.insert_crdmgt_comment";

   // private CreditIDCardDao m_creditIDCardDao;

    private CreditValueExtDao m_creditValueDao;

  //  private CreditAddressDao m_creditAddressDao;

  //  private CustomerGuarantorDao m_customerGuarantorDao;



    /**
     *  No-argument constructor.
     */
    public CreditProfileExtDaoSqlmap()
    {
        super();
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.credit.dao.CreditProfileDao#getCreditProfile(java.lang.Integer)
     */
    public CreditProfile getCreditProfile(Integer customerId)
            throws ObjectNotFoundException
    {
        assert customerId != null : "customerId is null";
        Long creditProfileId = (Long) queryForObject(
                STAT_ID_GET_PRIMARY_CREDIT_PROFILE_ID_BY_CUSTOMER_ID,
                customerId );
        CreditProfile creditProfile = (CreditProfile) queryForObject(
                STAT_ID_GET_CREDIT_PROFILE, creditProfileId );

	    try 
        {
	        creditProfile.setCreditValue( m_creditValueDao.getCreditValue( creditProfileId ) );
	    }
	    catch ( ObjectNotFoundException ex ) 
        {
            log.info("Credit value record not found for customer id: " + customerId );
	    }
        CustomerGuarantor customerGuarantor = null;
        try 
        {
            customerGuarantor = (CustomerGuarantor) queryForObject(
                    STAT_ID_GET_CUSTOMER_GUARANTOR_BY_GUARANTEE_ID, customerId );
            creditProfile.setCustomerGuarantor( customerGuarantor );
        }
        catch ( ObjectNotFoundException ex ) 
        {
            // THIS IS EXPECTED BEHAVIOUR FOR SOME CALLS SO IGNORE THIS EXCEPTION.
        }
        return creditProfile;
    }  
    /*
     * (non-Javadoc)
     *
     * @see com.telus.credit.dao.CreditProfileDao#unlinkAllCreditProfiles
     * 	(java.lang.Integer, java.lang.Long)
     */
    public void unlinkAllCreditProfiles(Integer customerId, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
        Map paramMap = new HashMap();
        paramMap.put( "customerId", customerId );
        paramMap.put( "userId", userId );
        paramMap.put( "sourceId", sourceId);
        update( STAT_ID_UPDATE_UNLINK_ALL_CREDIT_PROFILES, paramMap );
    }

    /*
     * (non-Javadoc)
     *
     * @see com.telus.credit.dao.CreditProfileDao#updateCreditProfileExt()
     */
    public void updateCreditProfileExt(CreditProfile creditProfile, String userId, Integer sourceId, boolean cpupdated, boolean cvupdate) 
          throws ConcurrencyConflictException
    {
        CreditProfileExt creditProfileExt = (CreditProfileExt) creditProfile;
        Map map = new HashMap();
        map.put( "credit_profile", creditProfileExt );
        map.put( "userId", userId );
        map.put("sourceId", sourceId);

        // update the main credit profile table with the new timestamp
        // and ensure we have optimistic lock
        //

        // 1.  update credit worthiness
        if(cvupdate)
        {
            m_creditValueDao.updateCreditValue( creditProfileExt.getCreditValue(),
                    userId, sourceId);
        }
        if(cpupdated)
        {
            try
            {
                //2. delete profile record if there is any
                update(STAT_ID_DELETE_CHARSTC_INDVDL,map);
            }catch (ConcurrencyConflictException e)
            {
                log.error(" Error in expire CHARSTC_INDVDL, no record find, skip this, ConcurrencyConflictException! ", e);
            
                //update(STAT_ID_DELETE_CHARSTC_INDVDL,map);
                //No records updated for statement:
                // credit_profile.delete_charstc_indvdl
            }
            try
            {    
                //3. insert new record, and update timestamp
                insert(STAT_ID_INSERT_CREDIT_PROFILE_CHARSTC_INDVDL, map);          
                //update(STAT_ID_UPDATE_CREDITPROFILE_TIMESTAMP_STMT, map);
            } 
            catch ( DuplicateKeyException e ) 
            {
                log.error(" Error in insert CHARSTC_INDVDL, and update credit profile timestamp,  ConcurrencyConflictException! ", e);
                throw new ConcurrencyConflictException( e );
            }
        }
        // do a merge for both type of updating
        try
        {    
            //4. merge and update timestamp
                   
            update(STAT_ID_UPDATE_CREDITPROFILE_TIMESTAMP_STMT, map);
        } 
        catch (Exception e ) 
        {
            log.error(" Error in insert CHARSTC_INDVDL, and update credit profile timestamp,  ConcurrencyConflictException! ", e);
            throw new ConcurrencyConflictException( e );
        }
      
    }

    /*
     * (non-Javadoc)
     *
     * @see com.telus.credit.dao.CreditProfileDao#getPriCreditIdByCustomerId(java.lang.Integer)
     */
    public Long getPrimaryCreditProfileIdByCustomerId(Integer customerId)
            throws ObjectNotFoundException
    {
        assert customerId != null : "customerId is null";
        return (Long) queryForObject(
                STAT_ID_GET_PRIMARY_CREDIT_PROFILE_ID_BY_CUSTOMER_ID,
                customerId );
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.credit.dao.CreditProfileDao#getCustomerIdByPrimaryCreditProfileId(java.lang.Integer)
     */
    public Integer getCustomerIdByPrimaryCreditProfileId(
            Long primaryCreditProfileId) throws ObjectNotFoundException
    {
        return (Integer) queryForObject(
                STAT_ID_GET_CUSTOMER_ID_BY_PRIMARY_CREDIT_PROFILE_ID,
                primaryCreditProfileId );
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.credit.dao.CreditProfileDao#CustomerIdArrayByCreditProfileId(java.lang.Integer)
     */
    public CreditProfileDto[] getCreditProfileDtoByCreditProfileId(
            Long primaryCreditProfileId)
    {

        List dtoList = queryForList(
                STAT_ID_GET_CREDIT_PROFILE_DTO_BY_CREDIT_PROFILE_ID,
                primaryCreditProfileId );


        if ( dtoList != dtoList || dtoList.size() > 0 ) 
        {
           
            CreditProfile creditProfile = ((CreditProfileDto) dtoList.get( 0 ))
                    .getCreditProfile();
            CreditProfileExt creditProfilext = (CreditProfileExt) creditProfile;
            //System.out.println("Enter unite creditProfile dao gt creditprofile:"+creditProfilext.get_id());
            //System.out.println("Enter unite creditProfile dao gt creditprofile:"+creditProfilext.getApplicationProvinceCd());
            
           // int customerId = ((CreditProfileDto) dtoList.get( 0 ))
           //         .getCustomerId();

	        try {
                
		        creditProfile.setCreditValue( m_creditValueDao.getCreditValue( new Long(creditProfile.get_id()) ) );
	        }
	        catch ( ObjectNotFoundException ex ) 
            {
		        log.info("Credit value record not found for profile id: " + creditProfile.get_id() );
	        }
        }

        return (CreditProfileDto[]) dtoList
                .toArray( new CreditProfileDto[dtoList.size()] );
    }


    /**
     * <p>
     * <b>Description </b> Persist CreditProfile Object .
     * </p>
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Insert into table CREDIT_PROFILE.</li>
     * <li>Call CustomerGuarantorDao.insert</li>
     * <li>Call CreditAddress.insert</li>
     * <li>Call CreditIDCardCao.insert</li>
     * <li>Call CreditValueDao.insert</li>
     * </ol>
     *
     * @param creditProfile object
     * @param userId of the CSR who inserts Credit Profile
     * @throws DuplicateKeyException
     */
    public Long insertCreditProfile(CreditProfile creditProfile, String userId, Integer sourceId)
            throws DuplicateKeyException, ObjectNotFoundException
    {
        //create a map that will be passed to iBatis.
        //
        CreditProfileExt creditProfileExt = (CreditProfileExt)creditProfile;
        Map creditProfileMap = new HashMap();

        //"credit_profile" is a key for the CreditProfile object.
        //
        creditProfileMap.put( "credit_profile", creditProfileExt );

        //"userId" is a key for the CSR id.
        //
        creditProfileMap.put( "userId", userId );

        //"sourceId" is the key for application id.
        //
        creditProfileMap.put( "sourceId", sourceId);

        //Insert record into CREDIT_PROFILE table
        //
        Long creditProfileId = (Long) insert( STAT_ID_INSERT_CREDIT_PROFILE,
                creditProfileMap );

        creditProfileExt.set_id( creditProfileId.longValue() );

        //Insert record into CPROFL_CHARSTC_INDVDL table.
        //
        insert( STAT_ID_INSERT_CREDIT_PROFILE_CHARSTC_INDVDL, creditProfileMap );

        //Insert record into CPROFL_STATUS table that indicates that this credit profile is "Active".
        //
        insert( STAT_ID_INSERT_CREDIT_PROFILE_STATUS, creditProfileMap );

        //Persist CreditValue.
        //
        CreditValue creditValue = creditProfile.getCreditValue();
        creditValue.setCreditProfileId( creditProfileId.longValue() );
        m_creditValueDao.insertCreditValue( creditValue, userId, sourceId);

	if ( creditProfileExt.getComment() != null ) {
	    HashMap commentsMap = new HashMap();
	    commentsMap.put("objectId", new Long(creditProfile.get_id()) );
	    commentsMap.put( "userId", userId );
	    commentsMap.put("sourceId", sourceId);
	    commentsMap.put("objectTypeCd", "CRD_PROFILE_ID" );
	    if ( creditProfile.getFormat() != null
		 && (creditProfile.getFormat().equals("BP")
		     || creditProfile.getFormat().equals("UM"))) {
		commentsMap.put("commentTypeCd", "SYSTEM_COMMENT" );
	    }
	    else {
		commentsMap.put("commentTypeCd", "MANUAL_COMMENT");
	    }
	    commentsMap.put("commentText", creditProfileExt.getComment());
	    commentsMap.put("displaySeqNum", new Integer(1) );

	    insert( STAT_ID_INSERT_CRDMGT_COMMENT, commentsMap );
	}
        creditProfileExt
                .setBusinessLastUpdateTimestamp( getBusinessLastUpdateTimestamp( creditProfileId ) );

        return creditProfileId;
    }


    /**
     * <p>
     * <b>Description </b> Links the Credit Profiles to Customers.
     * </p>
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Insert into table CPROFL_CUSTOMER_MAP</li>
     * </ol>
     */
    public void addCreditProfileToCustomer(Long creditProfileId,
            int customerId, String mapType, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        Map map = new HashMap();
        map.put( "credit_profile_id", creditProfileId );
        map.put( "customer_id", String.valueOf( customerId ) );
        map.put( "mapping_type", mapType );
        map.put( "userId", userId );
        map.put( "sourceId", sourceId );
        insert( STAT_ID_INSERT_LINK_INTO_MAPPING_TABLE, map );
    }


    /**
     * <p>
     * <b>Description </b> Finds matching customer ids and returns them as an
     * array of Integers.
     * </p>
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Searches CPROFL_CUSTOMER_MAP table. </li>
     * </ol>
     *
     * @param customerId of the customer for who we find matching customers.
     * @return array of ids of matching customers
     */
    public Integer[] getMatchingCustomerIds(Integer customerId)
    {
        List matchedCustomers = queryForList(
                STAT_ID_GET_MATCHING_CUSTOMER_IDS, customerId );
        Integer[] cids = new Integer[matchedCustomers.size()];
        int i = 0;
        for ( Iterator j = matchedCustomers.iterator(); j.hasNext(); ) {
            cids[i++] = (Integer) j.next();
        }
        return cids;
    }


    /**
     * <p>
     * <b>Description </b> Maintain credit profile mapping information by
     * creating record in credit profile mapping table to indicate that the
     * newly created blank credit profile is "unmerged" from the existing
     * credit profile.
     * </p>
     * <p>
     * <b>High Level Design </b>
     * </p>
     * <ol>
     * <li>Insert a record into CPROFL_MAPPING table,
     * setting CPROFL_TO_ID field with the newly created credit profile id,
     * setting CPROFL_FROM_ID field with the existing credit profile id, and
     * setting CPROFL_MAPPING_TYP_CD with 'UM' which means unmerged.
     * </li>
     * </ol>
     */
    public void insertUnmergeMapping(Long creditProfileId,
            Long newCreditProfileId, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        HashMap paramMap = new HashMap();
        paramMap.put( "creditProfileId", creditProfileId );
        paramMap.put( "newCreditProfileId", newCreditProfileId );
        paramMap.put( "userId", userId );
        paramMap.put( "sourceId", sourceId );
        insert( STAT_ID_INSERT_UNMERGE_MAPPING, paramMap );
    }


    /**
     * <p><b>Description</b>
     * Called after inserting a new record so that the domain object returned
     * can be updated right away if necessary.
     */
    public Timestamp getBusinessLastUpdateTimestamp(Long creditProfileId)
            throws ObjectNotFoundException
    {
        return (Timestamp) queryForObject(
                STAT_ID_GET_BUSINESS_LAST_UPDATE_TIMESTAMP_BY_CREDIT_PROFILE_ID,
                creditProfileId );
    }



    /* (non-Javadoc)
     * @see com.telus.credit.dao.CreditProfileDao#deleteSecondaryLinks(java.lang.Long, java.lang.String)
     */
    public void deleteSecondaryLinks(Long creditProfileId, Integer customerId,
            String userId, Integer sourceId)
    {
        Map map = new HashMap();
        map.put( "credit_profile_id", creditProfileId );
        map.put( "customer_id", customerId );
        map.put( "userId", userId );
        map.put( "sourceId", sourceId );
        try {
            update( STAT_ID_DELETE_SECONDARY_LINKS, map );
        }
        catch ( ConcurrencyConflictException e ) {
            // is expected if no links are found
        }
    }


    


   


    /**
     * @param creditValueDao The creditValueDao to set.
     */
    public void setCreditValueDao(CreditValueExtDao creditValueDao)
    {
        m_creditValueDao = creditValueDao;
    }


   
}
