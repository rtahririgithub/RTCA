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

//import java.util.List;

import java.util.HashMap;
import java.util.Map;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.credit.domain.CreditProfile;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;

//import com.telus.credit.domain.CreditIDCard;


/**
 * 
 * <p><b>Description: </b> This class represents access to the data store for CreditProfile domain object. 
 * It is implemented using iBatis ORM framework.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>[Design Observation Documentations]</li>		
 * 	</ul>
 *
 * <p><br><b>Issues: </b></p>
 * 	<ul>
 * 		<li>[Issues]</li>		
 * 	</ul>
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
 * @author x089748
 * 
 */

public class CreditProfileDaoSqlmap extends AbstractSqlMapDao implements CreditProfileDao
{
	private static final String GET_CREDITPROFILE_BY_PROFILEID_STMT = "getCreditProfileByProfileId";
	private static final String INSERT_CREDITPROFILE_STMT = "insertCreditProfile";
	private static final String EXPIRE_CREDITPROFILE_STMT = "expireCreditProfile";
	private static final String UPDATE_CREDITPROFILE_TIMESTAMP_STMT = "updateCreditProfileTimeStamp";
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method returns Credit Profile from CPROFL_PROFILE
     * 			table using credit profile id.
     * 		</li> 	
     * </ol>
     */
	 public CreditProfile getCreditProfile(long profileId) throws ObjectNotFoundException
	 {
	 	return (CreditProfile)queryForObject(
	 			GET_CREDITPROFILE_BY_PROFILEID_STMT, profileId+"");
	 	}
	 
	 /**
	   * <p><b>High Level Design</b></p>
	   * <ol>
	   * 		<li>
	   * 			This method expires the old and inserts the new.
	   * 		</li> 	
	   * </ol>
	   */
	 public void updateCreditProfile(CreditProfile creditProfile,String userId,String dataSrcId) throws ConcurrencyConflictException,DuplicateKeyException
	 {
	 	 expireCreditProfile(creditProfile,userId);
	 	 insertCreditProfile(creditProfile,userId,dataSrcId);
	 	 updateCreditProfileTimeStamp(creditProfile.get_id(),userId);
	 	}
	 
	 /**
	   * <p><b>High Level Design</b></p>
	   * <ol>
	   * 		<li>
	   * 			This method inserts creditProfile.
	   * 		</li> 	
	   * </ol>
	   */
	 private void insertCreditProfile(CreditProfile creditProfile,String userId,String dataSrcId) throws DuplicateKeyException
	 {
	 	Map creditProfileMap=new HashMap();
	 	creditProfileMap.put("creditProfile",creditProfile);
	 	creditProfileMap.put("userId",userId);
	 	creditProfileMap.put("dataSrcId",dataSrcId);
	 	insert(INSERT_CREDITPROFILE_STMT,creditProfileMap);
		}

		/**
	     * <p><b>High Level Design</b></p>
	     * <ol>
	     * 		<li>
	     * 			This method expires creditProfile.
	     * 		</li> 	
	     * </ol>
	     */
	    private void expireCreditProfile(CreditProfile creditProfile,String userId) throws ConcurrencyConflictException
	    {
	    	Map creditProfileMap=new HashMap();
		 	creditProfileMap.put("creditProfile",creditProfile);
		 	creditProfileMap.put("userId",userId);
	    	update(EXPIRE_CREDITPROFILE_STMT,creditProfileMap);
	   	    }
	    
	    /**
	     * <p><b>High Level Design</b></p>
	     * <ol>
	     * 		<li>
	     * 			This method updates business timestamp in table CREDIT_PROFILE after updating child table .
	     * 		</li> 	
	     * </ol>
	     */
	    protected void updateCreditProfileTimeStamp(long creditProfileId,String userId) throws ConcurrencyConflictException
		{
	    	Map creditProfileMap=new HashMap();
		 	creditProfileMap.put("creditProfileId",creditProfileId+"");
		 	creditProfileMap.put("userId",userId);
	    	update(UPDATE_CREDITPROFILE_TIMESTAMP_STMT, creditProfileMap);
		   }
}

