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

import com.telus.framework.exception.ConcurrencyConflictException;
//import com.telus.framework.exception.DuplicateKeyException;

import com.telus.credit.domain.CreditValue;
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

public class CreditValueDaoSqlmap extends AbstractSqlMapDao implements CreditValueDao
{
	private static final String GET_CREDITVALUE_BY_PROFILEID_STMT = "getCreditIDCardList";
	private static final String UPDATE_CREDITVALUE_STMT = "updateCreditProfile";
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method returns CreditValue from CPROFL_VALUE
     * 			table using credit profile id.
     * 		</li> 	
     * </ol>
     */
	
	 public CreditValue getCreditValue(int profileId) throws ObjectNotFoundException
	 {
	 	return (CreditValue)queryForObject(
	 			GET_CREDITVALUE_BY_PROFILEID_STMT, profileId + "" );
	 	}
	 
	 public void updateCreditValue(CreditValue creditValue) throws ConcurrencyConflictException
	 {
	 	 update(UPDATE_CREDITVALUE_STMT,creditValue);
	 	}
	
}


