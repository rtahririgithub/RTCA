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
import java.util.List;
import java.util.Map;

import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
//import com.telus.framework.exception.DuplicateKeyException;

import com.telus.credit.domain.CustomerGuarantor;
//import com.telus.framework.exception.ConcurrencyConflictException;
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

public class CustomerGuarantorDaoSqlmap extends AbstractSqlMapDao implements CustomerGuarantorDao
{
	private static final String GET_CUSTOMERGUARANTOR_BY_PROFILEID_STMT = "getGuarantorList";
	private static final String EXPIRE_CUSTOMERGUARANTOR_STMT = "expireCustomerGuarantor";
	private static final String INSERT_CUSTOMERGUARANTOR_STMT = "insertCustomerGuarantor";
	private static final String UPDATE_CREDITPROFILE_BY_GUARANTOR_STMT = "updateCreditProfileByGuarantor";
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method returns CustomerGuarantor from CUSTOMER_GUARANTOR
     * 			table using credit profile id.
     * 		</li> 	
     * </ol>
     */
	
	 public List getCustomerGuarantor(String profileId) throws ObjectNotFoundException
	 {
	 	return queryForList(GET_CUSTOMERGUARANTOR_BY_PROFILEID_STMT, profileId);
	 	}
	 
	 
	 /**
	   * <p><b>High Level Design</b></p>
	   * <ol>
	   * 		<li>
	   * 			This method expires CustomerGuarantor.
	   * 		</li> 	
	   * </ol>
	   */
	 public void expireCustomerGuarantor(CustomerGuarantor customerGuarantor,String userId) throws ConcurrencyConflictException
	 {
	 	Map customerGuarantorMap=new HashMap();
	 	customerGuarantorMap.put("customerGuarantor",customerGuarantor);
	 	customerGuarantorMap.put("userId",userId);
	 	update(EXPIRE_CUSTOMERGUARANTOR_STMT,customerGuarantorMap);
	 	}
	 
	 
	 /**
	   * <p><b>High Level Design</b></p>
	   * <ol>
	   * 		<li>
	   * 			This method inserts new CustomerGuarantor object into CreditPDS.
	   * 		</li> 	
	   * </ol>
	   */
	 public void insertCustomerGuarantor(CustomerGuarantor customerGuarantor,String userId,String dataSrcId) throws DuplicateKeyException
	 {
	 	Map customerGuarantorMap=new HashMap();
	 	customerGuarantorMap.put("customerGuarantor",customerGuarantor);
	 	customerGuarantorMap.put("userId",userId);
	 	customerGuarantorMap.put("dataSrcId",dataSrcId); 
	 	insert(INSERT_CUSTOMERGUARANTOR_STMT,customerGuarantorMap);
	  }
	 
	 /**
	   * <p><b>High Level Design</b></p>
	   * <ol>
	   * 		<li>
	   * 			This method update Credit Profile after updating Customer Guarantor.
	   * 		</li> 	
	   * </ol>
	   */
	 public void updateCreditProfileByGuarantor(String guaranteedCustomerId,String userId) throws ConcurrencyConflictException
	 {
	 	Map guaranteedCustomerMap=new HashMap();
	 	guaranteedCustomerMap.put("guaranteedCustomerId",guaranteedCustomerId);
	 	guaranteedCustomerMap.put("userId",userId); 
	 	update(UPDATE_CREDITPROFILE_BY_GUARANTOR_STMT,guaranteedCustomerMap);
	  }
	
}
