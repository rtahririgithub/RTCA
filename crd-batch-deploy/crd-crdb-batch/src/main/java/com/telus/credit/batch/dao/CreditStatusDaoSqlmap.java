/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 */
package com.telus.credit.batch.dao;

import java.util.HashMap;
import java.util.Map;

import com.telus.credit.domain.CreditProfile;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.persistence.AbstractSqlMapDao;

/**
 * 
 * <p>
 * <b>Description: </b>  This function . 
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 * 
 * <p>
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
 * @author Lei Fan(x089748)
 *  
 */
public class CreditStatusDaoSqlmap extends AbstractSqlMapDao implements CreditStatusDao
{   
	private static final String INSERT_CREDITSTATUS_STMT = "insertCreditStatus";
	private static final String UPDATE_CREDITSTATUS_STMT = "updateCreditStatus";
	private static final String UPDATE_CREDITPROFILE_BY_STATUS_STMT = "updateCreditProfileByStatus";
	
	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method inserts data into CPROFL_STATUS.
     * 		</li> 	
     * </ol>
     */
	public void insertCreditStatus(CreditProfile creditProfile,String userId,String dataSrcId) throws DuplicateKeyException,ConcurrencyConflictException
	{   
		Map creditStatusMap=new HashMap();
		creditStatusMap.put("creditProfile",creditProfile);
		creditStatusMap.put("userId",userId);
		creditStatusMap.put("dataSrcId",dataSrcId);
		insert(INSERT_CREDITSTATUS_STMT,creditStatusMap);
		updateCreditProfileByStatus(creditProfile,userId);
	   }

	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method expires the old data in CPROFL_STATUS.
     * 		</li> 	
     * </ol>
     */
    public void expireCreditStatus(CreditProfile creditProfile,String userId) throws ConcurrencyConflictException
    {
    	Map creditStatusMap=new HashMap();
		creditStatusMap.put("creditProfile",creditProfile);
		creditStatusMap.put("userId",userId);
    	update(UPDATE_CREDITSTATUS_STMT,creditStatusMap);
   	    }
    
    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method updates CREDIT_PROFILE after updating CPROFL_STATUS.
     * 		</li> 	
     * </ol>
     */
    private void updateCreditProfileByStatus(CreditProfile creditProfile,String userId) throws ConcurrencyConflictException
	{
    	Map creditStatusMap=new HashMap();
		creditStatusMap.put("creditProfile",creditProfile);
		creditStatusMap.put("userId",userId);
    	update(UPDATE_CREDITPROFILE_BY_STATUS_STMT,creditStatusMap);
	    }
   
   
}
