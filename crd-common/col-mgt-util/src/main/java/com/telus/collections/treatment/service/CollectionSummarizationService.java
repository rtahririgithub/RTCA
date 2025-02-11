/*
 *  Copyright (c) 2013 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */
package com.telus.collections.treatment.service;

import java.util.List;

import com.telus.collections.treatment.service.dto.CollectionBillingAccountData;
import com.telus.collections.treatment.service.dto.CustomerCollectionData;
/**
 * <p><b>Description :</b><interface>CollectionSummarizationService</interface> provides the interface to summarize detailed collection data.</p>
 * <p><b>Design Observations : </b></p>
 * <ul>
 * <li>None</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">31-Jul-2013</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">Initial Creation</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Gurbirinder Sidhu
 * 
 * @stereotype Service Interface
 * @version 1.0
 */
public interface CollectionSummarizationService {
  
	/**
	 * Summarize given collection data at customer level based upon following logic:
	 * 
	 * 
	 * Attribute Logic to aggregate account level data 
	 * BDS Score 
	 *   If no active accounts exists for that customer - use latest closed account score, for
	 *   Monthly re-assessment batch process customers with no active accounts
	 *   will not be part of re-assessment. If at least one active account take
	 *   worst BDS score across all of the open accounts for that customer.
	 * 
	 * # external agency assigned accounts, Total Current AR Balance owing on
	 * external agency assigned accounts,Date of most recent external agency
	 * assignment
     *   Count only accounts assigned or sold to external agency. 
     *   Assigned to external agency accounts should be assigned within last 24 month with
	 *   Current AR balance greater than 0$ Sold to external agency accounts
	 *   should be sold within last 36 month and aggregated Agency Assignment
	 *   Amount across all these accounts should be greater than 100$.
	 * 
	 * 
	 * # involuntary cancelled accounts, Date of most recent involuntary
	 * cancelled account, Balance owing on involuntary cancelled accounts
	 *   Aggregate data on involuntary cancelled accounts within last 24 month.
	 * 
	 * latest In collection indicator, latest collection indicator start date,
	 * latest collection indicator end date
	 *   In Collection treatment indicator has to be summarized for all customers
	 *   active accounts based on below logic: indicator set to yes if at least
	 *   one account is currently in Collection treatment otherwise it will be set
	 *   to no.
	 * 
	 * @param customerBillingAccountDataList
	 * @return customerCollectionData
	 */
	public CustomerCollectionData summarizeCollectionDataByCustomer(List<CollectionBillingAccountData> customerBillingAccountDataList);

}
