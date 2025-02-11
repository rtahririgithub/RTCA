package com.telus.credit.orderDepositCalculator.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.credit.orderDepositCalculator.webservice.dto.OrderDepositCalculationDto;
import com.telus.credit.orderDepositCalculator.common.domain.OrderData;
import com.telus.credit.orderDepositCalculator.common.domain.PayChannelNumberList;

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
 *  
 */

public interface DepositCalculationTransactionDao {
	
	
	 /**
     * <p>
     * <b>Description </b> Persist Order Deposit Calculation Transaction to the datastore.
     * </p>
     * @param  OrderDepositCalculationDto.
     * @param  OrderData
     * @throws DuplicateKeyException
     * 
     */
    public void insertDepositCalculationTransaction(OrderDepositCalculationDto orderDepositCalculationDto, OrderData orderData)
            throws DuplicateKeyException;
    
	 /**
     * <p>
     * <b>Description </b> Persist Order Deposit Calculation Transaction to the datastore.
     * </p>
     * @param  OrderDepositCalculationDto.
     * @throws DuplicateKeyException
     * 
     */
    public Long insertDepositCalculationTransaction(OrderDepositCalculationDto orderDepositCalculationDto)
            throws DuplicateKeyException;
    
    
	 /**
     * <p>
     * <b>Description </b> Persist Order Product instance to the datastore.
     * </p>
     * @param  Map productInstanceMap
     * @throws DuplicateKeyException
     * 
     */
    public Long insertCurrentOrderProductInstance(Map productInstanceMap)
            throws DuplicateKeyException;
    
	 /**
     * <p>
     * <b>Description </b> Persist Order Product instance to the datastore.
     * </p>
     * @param  Map productInstanceMap
     * @throws DuplicateKeyException
     * 
     */
    public Long insertInvoicedOrderProductInstance(Map productInstanceMap)
            throws DuplicateKeyException;
    
	 /**
     * <p>
     * <b>Description </b> Persist Product Pay Channel to the datastore.
     * </p>
     * @param  PayChannelNumberList payChannelNumList
     * @param  Long productInstanceID
     * @param  String userID
     * @throws DuplicateKeyException
     * 
     */
    public void insertProductPayChannel(PayChannelNumberList payChannelNumList,Long productInstanceID,String userID)
            throws DuplicateKeyException;
 
}
