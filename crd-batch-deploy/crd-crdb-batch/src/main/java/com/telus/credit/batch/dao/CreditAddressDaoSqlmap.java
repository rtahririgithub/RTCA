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

import com.telus.credit.domain.CreditAddress;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.persistence.AbstractSqlMapDao;

/**
 * 
 * <p>
 * <b>Description: </b>  This function implements interface CreditAddressDao. 
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
public class CreditAddressDaoSqlmap extends AbstractSqlMapDao implements CreditAddressDao
{
	private static final String INSERT_CREDITADDRESS_STMT = "insertCreditAddress";
	private static final String UPDATE_CREDITPROFILE_TIMESTAMP_STMT = "updateCreditProfileTimeStamp";

	/**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method inserts CreditAddress.
     * 		</li> 	
     * </ol>
     */
    public void insertCreditAddress(CreditAddress creditAddress,String userId,String dataSrcId) throws DuplicateKeyException,ConcurrencyConflictException
	{
    	Map creditAddressMap=new HashMap();
    	creditAddressMap.put("creditAddress",creditAddress);
    	creditAddressMap.put("userId",userId);
    	creditAddressMap.put("dataSrcId",dataSrcId);
    	insert(INSERT_CREDITADDRESS_STMT,creditAddressMap);
    	updateCreditProfileTimeStamp(creditAddress.getCreditProfileId(),userId);
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
