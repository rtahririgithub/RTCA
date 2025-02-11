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


import java.util.HashMap;
import java.util.Map;

import com.telus.credit.domain.CreditIDCard;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.persistence.AbstractSqlMapDao;



/**
 * 
 * <p><b>Description: </b> This class represents access to the data store for CreditIDCard domain object. 
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

public class CreditIDCardDaoSqlmap extends AbstractSqlMapDao implements CreditIDCardDao
{
	private static final String INSERT_CREDITIDCARD_STMT = "insertCreditIDCard";
	private static final String UPDATE_CREDITPROFILE_TIMESTAMP_STMT = "updateCreditProfileTimeStamp";
	
    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method inserts CreditIDCard.
     * 		</li> 	
     * </ol>
     */
	public void insertCreditIDCard(CreditIDCard creditIDCard,String userId,String dataSrcId)throws DuplicateKeyException,ConcurrencyConflictException
	 {   
		 Map creditIDCardMap=new HashMap();
		 creditIDCardMap.put("creditIDCard",creditIDCard);
		 creditIDCardMap.put("userId",userId);
		 creditIDCardMap.put("dataSrcId",dataSrcId);
	 	 insert(INSERT_CREDITIDCARD_STMT, creditIDCardMap);
	 	 updateCreditProfileTimeStamp(creditIDCard.getCreditProfileId(),userId);
	
	   }
	
	  /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method updates business timestamp in table CREDIT_PROFILE after updating child table .
     * 		</li> 	
     * </ol>
     */
    private void updateCreditProfileTimeStamp(long creditProfileId,String userId) throws ConcurrencyConflictException
	{   
    	Map creditProfileMap=new HashMap();
	 	creditProfileMap.put("creditProfileId",creditProfileId+"");
	 	creditProfileMap.put("userId",userId);
    	update(UPDATE_CREDITPROFILE_TIMESTAMP_STMT, creditProfileMap);
    
	   }

}

