/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.telus.credit.util.CreditMgtUtils;
import com.telus.framework.math.Money;

/**
 * 
 * <p>
 * <b>Description: </b> Represents customer guarantor bound to the credit
 * profile
 * </p>
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
 * <td width="15%">May 2, 2005</td>
 * <td width="15%">Roman Mikhailov</td>
 * <td width="55%">initial version</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * <tr>
 * <td width="15%">July 4, 2005</td>
 * <td width="15%">Yvette Pollock</td>
 * <td width="55%">Updated due to further info from Jason Yan</td>
 * <td width="15%">For clarity, changed name of creditProfile to
 * guarantorCreditProfile.</td>
 * </tr>
 * </table>
 * 
 * @author Roman Mikhailov
 *  
 */
public class CustomerGuarantor implements Serializable
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 8489400752957805025L;


	/**
     * Represents unique identifier for the object of this type, its "record id"
     * in the database. It is used by the DAO to persist this object to the
     * database.
     */
    private long m_id;


    private Timestamp m_lastUpdateTimestamp;


    /**
     * Customer id for guarantor.
     */
    private int m_guarantorCustomerId;

    /**
     * Customer id for guarantee.
     */
    private int m_guaranteedCustomerId;

    /**
     * Guarantor credit profile id.
     */
    private long m_guarantorCreditProfileId;
    /**
     * Guarantor phone number.
     */
    private String m_guarantorPhoneNumber;

    /**
     * C90 Reference number. Number of the document that is signed by the
     * guarantor. Reserved for future use.
     */
    private String m_referenceNumber;
    /**
     * Guaranteed amount.
     */
    private Money m_guaranteedAmount;
    /**
     * Date the gaurantor agreement expires.
     */
    private Date m_expiryDate;
    /**
     * Gurantor full name. Maps to C90_FULL_NM in db.
     */
    private String m_guarantorFullName;
    /**
     * Comments. Maps to REMARKS_TXT. Reserved for future use.
     */
    private String m_comment;



    /**
     * @return Returns the _id.
     */
    public long get_id()
    {
        return m_id;
    }


    /**
     * @param id
     *            The _id to set.
     */
    public void set_id(long id)
    {
        this.m_id = id;
    }


    /**
     * @return Returns the m_comment.
     */
    public String getComment()
    {
        return m_comment;
    }


    /**
     * @param comment
     *            The m_comment to set.
     */
    public void setComment(String comment)
    {
        this.m_comment = comment;
    }


    /**
     * @return Returns the m_expiryDate.
     */
    public Date getExpiryDate()
    {
        return m_expiryDate;
    }


    /**
     * @param date
     *            The m_expiryDate to set.
     */
    public void setExpiryDate(Date date)
    {
        m_expiryDate = date;
    }


    /**
     * @return Returns the m_guaranteedAmount.
     */
    public Money getGuaranteedAmount()
    {
        return m_guaranteedAmount;
    }


    /**
     * @param amount
     *            The m_guaranteedAmount to set.
     */
    public void setGuaranteedAmount(Money amount)
    {
        m_guaranteedAmount = amount;
    }


    /**
     * @return Returns the m_guarantorPhoneNumber.
     */
    public String getGuarantorPhoneNumber()
    {
        return m_guarantorPhoneNumber;
    }


    /**
     * @param phoneNumber
     *            The m_guarantorPhoneNumber to set.
     */
    public void setGuarantorPhoneNumber(String phoneNumber)
    {
        m_guarantorPhoneNumber = phoneNumber;
    }


    /**
     * @return Returns the m_gurantorFullName.
     */
    public String getGuarantorFullName()
    {
        return m_guarantorFullName;
    }


    /**
     * @param fullName
     *            The m_gurantorFullName to set.
     */
    public void setGuarantorFullName(String fullName)
    {
        m_guarantorFullName = fullName;
    }


    /**
     * @return Returns the m_referenceNumber.
     */
    public String getReferenceNumber()
    {
        return m_referenceNumber;
    }


    /**
     * @param number
     *            The m_referenceNumber to set.
     */
    public void setReferenceNumber(String number)
    {
        m_referenceNumber = number;
    }


    /**
     * @return Returns the m_guarantorCustomerId.
     */
    public int getGuarantorCustomerId()
    {
        return m_guarantorCustomerId;
    }


    /**
     * @param id
     *            The guarantor customer Id to set.
     */
    public void setGuarantorCustomerId(int id)
    {
        m_guarantorCustomerId = id;
    }


    /**
     * @return Returns the m_guaranteedCustomerId.
     */
    public int getGuaranteedCustomerId()
    {
        return m_guaranteedCustomerId;
    }


    /**
     * @param id
     *            The guaranteed customer Id to set.
     */
    public void setGuaranteedCustomerId(int id)
    {
        m_guaranteedCustomerId = id;
    }


    /**
     * @return Returns the guarantorCreditProfileId.
     */
    public long getGuarantorCreditProfileId()
    {
        return m_guarantorCreditProfileId;
    }


    /**
     * @param guarantorCreditProfileId
     *            The guarantorCreditProfileId to set.
     */
    public void setGuarantorCreditProfileId(long guarantorCreditProfileId)
    {
        m_guarantorCreditProfileId = guarantorCreditProfileId;
    }


    /**
     * @return Returns the lastUpdateTimestamp.
     */
    public Timestamp getLastUpdateTimestamp()
    {
        return m_lastUpdateTimestamp;
    }


    /**
     * @param lastUpdateTimestamp
     *            The lastUpdateTimestamp to set.
     */
    public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp)
    {
        m_lastUpdateTimestamp = lastUpdateTimestamp;
    }


    /**
     * Compares two objects of this type.
     * 
     * @param o object
     *            to compare with this object.
     * @return true if all fields in both objects are identical; false
     *         otherwise.
     */
    public boolean equals(Object o)
    {
        if ( !(o instanceof CustomerGuarantor) ) {
            return false;
        }
        CustomerGuarantor cg = (CustomerGuarantor) o;

        if ( this.m_guarantorCustomerId == cg.m_guarantorCustomerId
                && this.m_guaranteedCustomerId == cg.m_guaranteedCustomerId
                && this.m_guarantorCreditProfileId == cg.m_guarantorCreditProfileId
                && CreditMgtUtils.areEqual( this.m_comment, cg.getComment() )
                && CreditMgtUtils.areEqual( this.m_guarantorFullName, cg
                        .getGuarantorFullName() )
                && CreditMgtUtils.areEqual( this.m_guarantorPhoneNumber, cg
                        .getGuarantorPhoneNumber() )
                && CreditMgtUtils.areEqual( this.m_referenceNumber, cg
                        .getReferenceNumber() )
                && CreditMgtUtils.areEqual( this.m_guaranteedAmount,
                        cg.m_guaranteedAmount )
                && CreditMgtUtils.areEqual( this.m_expiryDate, cg.m_expiryDate )

        ) {
            return true;
        }
        return false;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }





    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "CustomerGuarantor { " );
        buf.append( " m_id: " ).append( m_id );
        buf.append( ", m_guarantorCustomerId: " )
                .append( m_guarantorCustomerId );
        buf.append( ", m_guaranteedCustomerId: " ).append(
                m_guaranteedCustomerId );
        buf.append( ", m_guarantorCreditProfileId: " ).append(
                m_guarantorCreditProfileId );
        buf.append( ", m_expiryDate: " ).append( m_expiryDate!=null? m_expiryDate.toString(): null );
        buf.append( ", m_comment: " ).append( m_comment );
        buf.append( ", m_guaranteedAmount: " ).append( m_guaranteedAmount );
        buf.append( ", m_guarantorPhoneNumber: " ).append(
                m_guarantorPhoneNumber );
        buf.append( ", m_guarantorFullName: " ).append( m_guarantorFullName );
        buf.append( ", m_referenceNumber: " ).append( m_referenceNumber );
        buf.append( ", lastUpdateTimestamp: " ).append( m_lastUpdateTimestamp );
        buf.append( " }" );
        return buf.toString();
    }

}