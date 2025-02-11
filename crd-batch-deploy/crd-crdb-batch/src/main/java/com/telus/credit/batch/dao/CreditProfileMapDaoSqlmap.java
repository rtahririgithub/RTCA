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
import com.telus.framework.persistence.AbstractSqlMapDao;



/**
 * 
 * <p><b>Description: </b> This class represents access to the data store. 
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

public class CreditProfileMapDaoSqlmap extends AbstractSqlMapDao implements CreditProfileMapDao
{
	private static final String INSERT_CREDITPROFILE_MAP_STMT = "insertCreditProfileMap";
	private static final String GET_BUSINESSOWNERSHIP_BY_PROFILEID_STMT = "getCreditProfileMapList";
	private static final String EXPIRE_BUSINESSOWNERSHIP_STMT = "updateCreditProfileMap";
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method inserts mapping infomation.
     * 		</li> 	
     * </ol>
     */
	
	 public void insertCreditProfileMap(CreditProfileMap profileMap) throws DuplicateKeyException
	 {
	 	 insert(INSERT_CREDITPROFILE_MAP_STMT,profileMap);
	 	}
	 
	 
	/**
	  * <p><b>High Level Design</b></p>
	  * <ol>
	  * 		<li>
	  * 		  get ownership between consumer and business from creditPDS
	  * 		</li> 	
	  * </ol>
	  */
	  public List getBusinessOwnershipByProfileId(String profileId)
	            throws ObjectNotFoundException
	  {
		  return queryForList(GET_BUSINESSOWNERSHIP_BY_PROFILEID_STMT, profileId);
	      }          
	    
	/**
	  * <p><b>High Level Design</b></p>
      * <ol>
	  * 		<li>
	  * 		  expire BusinessOwnership by updating time stamp after Merge
	  * 		</li> 	
	  * </ol>
	  */
	   public void expireBusinessOwnership(CreditProfileMap creditProfileMap)
	            throws ConcurrencyConflictException
	   {
		   update(EXPIRE_BUSINESSOWNERSHIP_STMT,creditProfileMap);
	       }   

	
}


