/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.service.dto.search;


/**
 * 
 * <p><b>Description: </b> This class represents customer id. 
 * It knows whether the customer id is processed or not.
 * </p>
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

public class CreditMgtCustomerID
{

    /**
     * Represents customer id
     */
    private Integer m_customerId;
    /**
     * Indicates that matching customers for this customer id are found.
     */
    private boolean m_processed;


    /**
     * CONSTRUCTOR
     * @param customerId
     */
    public CreditMgtCustomerID(int customerId)
    {
        super();
        m_customerId = new Integer( customerId );
    }


    private CreditMgtCustomerID()
    {
        // Disabled no-argument constructor.
    }


    /**
     * @return Returns the customerId.
     */
    public Integer getId()
    {
        return m_customerId;
    }


    /**
     * @return Returns the processed.
     */
    public boolean isProcessed()
    {
        return m_processed;
    }


    /**
     * @param processed The processed to set.
     */
    public void setProcessed(boolean processed)
    {
        m_processed = processed;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if ( !(o instanceof CreditMgtCustomerID) ) {
            return false;
        }
        CreditMgtCustomerID cid = (CreditMgtCustomerID) o;
        if ( this.m_customerId.equals( cid.getId() ) ) {
            return true;
        }
        return false;
    }


    /**
     * <p><b>Description</b> Checks if two objects of this type have equal customerId </p>
     * @param customerId
     * @return true if customerId are equal; false otherwise.
     */
    public boolean equalCustomerId(int customerId)
    {
        if ( customerId == m_customerId.intValue() ) {
            return true;
        }
        return false;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return m_customerId.hashCode();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "CreditMgtCustomerID {" ).append( " m_customerId:" ).append( m_customerId );
        buf.append( "; m_processed: " ).append( m_processed ).append( "}" );
        return buf.toString();
    }

}