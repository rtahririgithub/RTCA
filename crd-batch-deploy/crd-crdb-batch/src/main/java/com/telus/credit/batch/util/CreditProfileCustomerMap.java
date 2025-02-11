/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.batch.util;

/**
*
* <p>
* <b>Description: </b> Represents CreditProfile_Customer_MAP
* </p>
* <p>
* <b>Design Observations: </b>
* </p>
*
* <ul>
* <li></li>
* </ul>
*
*
* <p>
* <br>
* <b>Issues </b>
* </p>
* <table border="1" width="100%">
* <tr>
* <th width="10%">#</th>
* <th width="45%">Description</th>
* <th width="45%">Resolution</th>
* </tr>
* <tr>
* <td width="10%">&nbsp;</td>
* <td width="45%">&nbsp;</td>
* <td width="45%">&nbsp;</td>
* </tr>
* </table>
*
* <p>
* <br>
* <b>Revision History </b>
* </p>
* <table border="1" width="100%">
* <tr>
* <th width="15%">Date</th>
* <th width="15%">Revised By</th>
* <th width="55%">Description</th>
* <th width="15%">Reviewed By</th>
* </tr>
* <tr>
* <td width="15%">July 12,2005</td>
* <td width="15%">Lei Fan</td>
* <td width="55%">initial version</td>
* <td width="15%">&nbsp;</td>
* </tr>
* </table>
*
* @author Lei
*
*/
public class CreditProfileCustomerMap {
	/*
	 * Profile id
	 */
	private String m_creditProfileId;
	/*
	 * customer Id
	 */
	private String m_customerId;
	/**
     * CreditProfile type code associated with a customer. Possible values are:
      * <ul>
	  * 	<li>"PRI" - Primary</li>	
	  * 	<li>"SEC" - Secondary</li>		
	  * </ul>
     */
	private String m_profileCustomerMapTypeCode;
	/*
	 * user Id
	 */
	private String m_userId;
	/*
	 * dataSrc Id
	 */
	private String m_dataSrcId;

	/**
	 * @return Returns the creditProfileId.
	 */
	public String getCreditProfileId() {
		return m_creditProfileId;
	}
	/**
	 * @param creditProfileId The creditProfileId to set.
	 */
	public void setCreditProfileId(String creditProfileId) {
		m_creditProfileId = creditProfileId;
	}
	/**
	 * @return Returns the customerId.
	 */
	public String getCustomerId() {
		return m_customerId;
	}
	/**
	 * @param customerId The customerId to set.
	 */
	public void setCustomerId(String customerId) {
		m_customerId = customerId;
	}
	
	/**
	 * @return Returns the profileCustomerMapTypeCode.
	 */
	public String getProfileCustomerMapTypeCode() {
		return m_profileCustomerMapTypeCode;
	}
	/**
	 * @param profileCustomerMapTypeCode The profileCustomerMapTypeCode to set.
	 */
	public void setProfileCustomerMapTypeCode(String profileCustomerMapTypeCode) {
		m_profileCustomerMapTypeCode = profileCustomerMapTypeCode;
	}
	/**
	 * @return Returns the dataSrcId.
	 */
	public String getDataSrcId() {
		return m_dataSrcId;
	}
	/**
	 * @param dataSrcId The dataSrcId to set.
	 */
	public void setDataSrcId(String dataSrcId) {
		m_dataSrcId = dataSrcId;
	}
	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return m_userId;
	}
	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		m_userId = userId;
	}
}
