/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.batch.dto;

import com.telus.credit.domain.CreditProfileExt;
import java.io.Serializable;

/**
 *
 * <p><b>Description: </b> This class encapsulates customer id for the
 * customer and credit profile object that contains credit information for this customer.</p>
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
 * @author Roman Mikhailov
 *
 */

public class CreditProfileDto implements Serializable
{

    /**
	 *
	 */
	private static final long serialVersionUID = 3855490732685720993L;

	/**
     * Customer identification.
     */
    private int m_customerId;

    /**
     * Contains credit profile information for the customer.
     */
    private CreditProfileExt m_creditProfile;

    /**
     * CONSTRUCTOR
     * @param customerId for the customer.
     * @param creditProfile  primary credit profile.
     */
    public CreditProfileDto(int customerId, CreditProfileExt creditProfile)
    {
        m_customerId = customerId;
        m_creditProfile = creditProfile;
    }

    /**
     * CONSTRUCTOR
     */
    public CreditProfileDto()
    {
        //default constructor needed by iBATIS.
    }

    /**
     * <p><b>Description</b> Returns  credit profile.</p>
     * @return  CreditProfile object.
     */
    public CreditProfileExt getCreditProfile()
    {
        return m_creditProfile;
    }

    /**
     * <p><b>Description</b> Returns customer id. </p>
     * @return  customer id
     */
    public int getCustomerId()
    {
        return m_customerId;
    }

    /**
     * <p><b>Description</b> Sets credit profile. </p>
     * @param  creditProfile to set.
     */
    public void setCreditProfile(CreditProfileExt creditProfile)
    {
        m_creditProfile = creditProfile;
    }

    /**
     * <p><b>Description</b> Sets customer id. </p>
     * @param  customerId to set.
     */
    public void setCustomerId(int customerId)
    {
        m_customerId = customerId;
    }

    /**
     * <p><b>Description</b> Returns a string representation of the CreditProfileDto object. </p>
     * @return  String.
     */
    public String toString(){
        StringBuffer buf = new StringBuffer();
        buf.append("CreditProfileDto { ");
        buf.append("m_customerId: ").append(m_customerId);
        buf.append("m_creditProfile: ").append(m_creditProfile);
        buf.append("}");
        return buf.toString();
    }

}