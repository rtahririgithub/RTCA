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
import java.util.Map;
import java.util.HashMap;

import com.telus.credit.batch.util.CreditProfileCustomerMap;
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

public class CreditProfileCustomerMapDaoSqlmap extends AbstractSqlMapDao implements CreditProfileCustomerMapDao
{
	private static final String INSERT_CREDITPROFILE_CUSTOMER_MAP_STMT = "insertCreditProfileCustomerMap";
	private static final String EXPIRE_CREDITPROFILE_CUSTOMER_MAP_STMT = "expireCreditProfileCustomerMap";
	private static final String GET_CUSTOMER_LIST_BY_PROFILEID_STMT = "getCustomerListByProfileId";
	private static final String GET_ALL_LINKS_BY_PROFILEID_STMT = "getAllLinksByProfileId";
	private static final String GET_PROFILE_LIST_BY_CUSTOMERID_STMT = "getProfileListByCustomerId";
	private static final String CHECK_CREDITPROFILE_CUSTOMER_MAP_STMT = "checkCreditProfileCustomerMap";
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			 get all profileIds by given customerId.
     * 		</li> 	
     * </ol>
     */ 
	public List getProfileListByCustomerId(String customerId,String mapType)throws ObjectNotFoundException
	{
		Map paramMap=new HashMap();
	 	paramMap.put("customerId",customerId);
	 	paramMap.put("mapType",mapType);
	 	return queryForList(GET_PROFILE_LIST_BY_CUSTOMERID_STMT,paramMap);
	}
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			 get all links that asssociated with given profileId.
     * 		</li> 	
     * </ol>
     */ 
	public List getAllLinksByProfileId(long profileId)throws ObjectNotFoundException
	{
		return queryForList(GET_ALL_LINKS_BY_PROFILEID_STMT,profileId+"");
	}
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method get all customer ids that point to either p1 or p2 as primary.
     * 		</li> 	
     * </ol>
     */ 
	public List getCustomerListByProfileId(String profileId,String mapType)throws ObjectNotFoundException
	 {
	 	Map paramMap=new HashMap();
	 	paramMap.put("profileId",profileId);
	 	paramMap.put("mapType",mapType);
	 	return queryForList(GET_CUSTOMER_LIST_BY_PROFILEID_STMT,paramMap);
	 }
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method inserts mapping infomation.
     * 		</li> 	
     * </ol>
     */
	public void insertCreditProfileCustomerMap(CreditProfileCustomerMap profileCustomerMap) 
	throws DuplicateKeyException
	 {
	 	 insert(INSERT_CREDITPROFILE_CUSTOMER_MAP_STMT,profileCustomerMap);
	 	}
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method updates mapping infomation.
     * 		</li> 	
     * </ol>
     */
	 public void expireCreditProfileCustomerMap(CreditProfileCustomerMap profileCustomerMap)
     throws ObjectNotFoundException,ConcurrencyConflictException
	 {
	 	 update(EXPIRE_CREDITPROFILE_CUSTOMER_MAP_STMT,profileCustomerMap);
	 	}
	 
	 /**
	   * <p><b>High Level Design</b></p>
	   * <ol>
	   * 		<li>
	   * 			This method checks if the object exists in database.
	   * 		</li> 	
	   * </ol>
	   */
	 public Integer checkCreditProfileCustomerMap(CreditProfileCustomerMap profileCustomerMap)throws ObjectNotFoundException
	 {
	 	return (Integer)queryForObject(CHECK_CREDITPROFILE_CUSTOMER_MAP_STMT,profileCustomerMap);
	  }
	
}


