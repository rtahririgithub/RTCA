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
* <b>Description: </b> Represents CreditProfile_MAPPING 
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
public class CreditProfileMap {
	/*
	 * Profile id which mapping to another profile
	 */
	private String m_creditProfileToId;
	/*
	 * Profile id which mapping from another profile
	 */
	private String m_creditProfileFromId;
	
	/**
     * CreditProfile type code associated with a customer. Possible values are:
      * <ul>
	  * 	<li>"MI" - Merged into</li>	
	  * 	<li>"UM" - Unmerged from</li>
	  *     <li>"CO" - company owner (for individual owning corporation)</li>
      * 	<li>"AP" - alternate profile (fraudulent equivalent)</li>
	  * </ul>
     */
	private String m_profileMapTypeCode;
	/*
	 * user Id
	 */
	private String m_userId;
	/*
	 * data source id
	 */
	private String m_dataSrcId;
	

	
	/**
	 * @return Returns the creditProfileFromId.
	 */
	public String getCreditProfileFromId() {
		return m_creditProfileFromId;
	}
	/**
	 * @param creditProfileFromId The creditProfileFromId to set.
	 */
	public void setCreditProfileFromId(String creditProfileFromId) {
		m_creditProfileFromId = creditProfileFromId;
	}
	/**
	 * @return Returns the creditProfileToId.
	 */
	public String getCreditProfileToId() {
		return m_creditProfileToId;
	}
	/**
	 * @param creditProfileToId The creditProfileToId to set.
	 */
	public void setCreditProfileToId(String creditProfileToId) {
		m_creditProfileToId = creditProfileToId;
	}
	/**
	 * @return Returns the profileMapTypeCode.
	 */
	public String getProfileMapTypeCode() {
		return m_profileMapTypeCode;
	}
	/**
	 * @param profileMapTypeCode The profileMapTypeCode to set.
	 */
	public void setProfileMapTypeCode(String profileMapTypeCode) {
		m_profileMapTypeCode = profileMapTypeCode;
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


