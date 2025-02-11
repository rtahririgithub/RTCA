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
* <b>Description: </b> Represents Customer Group.Each Group represents a unique person
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
* <td width="15%">May 20,2005</td>
* <td width="15%">Lei Fan</td>
* <td width="55%">initial version</td>
* <td width="15%">&nbsp;</td>
* </tr>
* </table>
*
* @author Lei
*
*/
public class CustomerGroup {
    
    /*
     * all the matched customer ids
     */
	private String m_customerIds;
	/*
	 * shared credit profiles by customer goup
	 */
    private String m_profileIds; 
    /*
     * number of profile shared by matched customers of each group
     */
    private int m_profileCount; 
    /**
	 * @return Returns the profileIds.
	 */
	public String getProfileIds() {
		return m_profileIds;
	}
	/**
	 * @param profileIds The profileIds to set.
	 */
	public void setProfileIds(String profileIds) {
		m_profileIds = profileIds;
	}
    

    /**
     * @return Returns the customerIds.
     */
    public String getCustomerIds() {
        return m_customerIds;
    }
    /**
     * @param customerIds The customerIds to set.
     */
    public void setCustomerIds(String customerIds) {
        m_customerIds = customerIds;
    }
    /**
     * @return Returns the profileCount.
     */
    public int getProfileCount() {
        return m_profileCount;
    }
    /**
     * @param profileCount The profileCount to set.
     */
    public void setProfileCount(int profileCount) {
        m_profileCount = profileCount;
    }
}
