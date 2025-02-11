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

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.telus.credit.util.CreditMgtUtils;

/**
 * 
 * <p><b>Description:</b> Represents id card (example: SIN, Drivers Licence, etc) 
 * bound to a credit profile</p>
 *
 * <p><br><b>Revision History</b></p>
 * <table border="1" width="100%">
 * 	<tr>
 * 		<th width="15%">Date</th>
 * 		<th width="15%">Revised By</th>
 * 		<th width="55%">Description</th>
 * 		<th width="15%">Reviewed By</th>
 * 	</tr>
 * 	<tr>
 * 		<td width="15%">May 2, 2005</td>
 * 		<td width="15%">Roman Mikhailov</td>
 * 		<td width="55%">initial version</td>
 * 		<td width="15%">&nbsp;</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
 */
public class CreditIDCard implements Serializable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3503542120885539884L;
	
	/**
     * Identification of type "Social Insurance Number". 
     */
    public static final String SIN_KEY = "SIN";
    /**
     * Identification of type "Driver's License". 
     */
    public static final String DRIVERS_LICENSE_KEY = "DL";
    /**
     * Identification of type "Health Care Card". 
     */
    public static final String HEALTH_CARE_NUMBER_KEY = "HC";
    /**
     * Identification of type "Passport". 
     */
    public static final String PASSPORT_NUMBER_KEY = "PSP";
    /**
     * Identification type "Provincial ID".
     */
    public static final String PROVINCIAL_ID_KEY = "PRV";

    /**
     * Represents unique identifier for object of this type, its "record id" in the database.
     * It is used by the DAO to persist this object to the database. 
     */
    private long m_id;


    
    private Timestamp m_lastUpdateTimestamp;
    
    
    
    /**
     * Represents unique identifier for the parent Credit Profile object, 
     * its "record id" in the database.
     */
    private long m_creditProfileId;


    /**
     * Code of id card. Possible values are:
     * <ul>
     * 	<li>"SIN" - Social Insurance Number.</li>
     * 	<li>"DL" - Driver's Licence.</li>
     * 	<li>"HC" - Health Care Number.</li>
     * 	<li>"PSP" - Passport Number.</li>
     * 	<li>"PRV" - Provincial Id.</li>					
     * </ul>
     */
    private String m_idTypeCode;

    /**
     * Code of the province where this card was issued. Possible values are:
     * <ul>
     * 		<li>"AB" - Alberta.</li>
     * 		<li>"BC" - British Columbia.</li>	
     * </ul>
     * 
     */
    private String m_provinceCode;
    /**
     * 
     * Code of the country where this card was issued. Possible values are:
     * <ul>
     * 		<li>"CAN" - Canada.</li>
     * 		<li>"USA" - United States.</li>	
     * </ul>
     */
    private String m_countryCode;

    /**
     * Identification number for this card.
     */
    private String m_idNumber;



    /**
     * @return Returns the _id.
     */
    public long get_id()
    {
        return m_id;
    }


    /**
     * @param id The id to set.
     */
    public void set_id(long id)
    {
        this.m_id = id;
    }


    /**
     * @return Returns the m_countryCode.
     */
    public String getCountryCode()
    {
        return m_countryCode;
    }


    /**
     * @param code The m_countryCode to set.
     */
    public void setCountryCode(String code)
    {
        m_countryCode = code;
    }


    /**
     * @return Returns the m_idNumber.
     */
    public String getIdNumber()
    {
        return m_idNumber;
    }


    /**
     * @param number The m_idNumber to set.
     */
    public void setIdNumber(String number)
    {
        m_idNumber = number;
    }


    /**
     * @return Returns the m_idTypeCode.
     */
    public String getIdTypeCode()
    {
        return m_idTypeCode;
    }


    /**
     * @param typeCode The m_idTypeCode to set.
     */
    public void setIdTypeCode(String typeCode)
    {
        m_idTypeCode = typeCode;
    }


    /**
     * @return Returns the m_provinceCode.
     */
    public String getProvinceCode()
    {
        return m_provinceCode;
    }


    /**
     * @param code The m_provinceCode to set.
     */
    public void setProvinceCode(String code)
    {
        m_provinceCode = code;
    }


    /**
     * @return Returns the m_creditProfileId.
     */
    public long getCreditProfileId()
    {
        return m_creditProfileId;
    }


    /**
     * @param profileId The m_creditProfileId to set.
     */
    public void setCreditProfileId(long profileId)
    {
        m_creditProfileId = profileId;
    }

    
    /**
     * @return Returns the lastUpdateTimestamp.
     */
    public Timestamp getLastUpdateTimestamp()
    {
        return m_lastUpdateTimestamp;
    }
    /**
     * @param lastUpdateTimestamp The lastUpdateTimestamp to set.
     */
    public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp)
    {
        m_lastUpdateTimestamp = lastUpdateTimestamp;
    }

    /**
     * Compares two objects of this type.
     * @param o object to compare with this object.
     * @return true if all fields in both objects are identical; false otherwise.
     */
    public boolean equals(Object o)
    {
        if ( !(o instanceof CreditIDCard) ) {
            return false;
        }
        CreditIDCard cidc = (CreditIDCard) o;

        if ( CreditMgtUtils.areEqual( this.m_countryCode, cidc.getCountryCode() )
                && CreditMgtUtils.areEqual( this.m_idNumber, cidc.getIdNumber() )
                && CreditMgtUtils.areEqual( this.m_idTypeCode, cidc
                        .getIdTypeCode() )
                && CreditMgtUtils.areEqual( this.m_provinceCode, cidc
                        .getProvinceCode() ) ) {
            return true;
        }
        return false;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return HashCodeBuilder.reflectionHashCode(this);
    }


    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "CreditIDCard { " );
        buf.append( " m_id: " ).append( m_id );
        buf.append( ", m_creditProfileId: " ).append( m_creditProfileId );
        buf.append( ", m_countryCode: " ).append( m_countryCode );
        buf.append( ", m_idNumber: " ).append( m_idNumber );
        buf.append( ", m_idTypeCode: " ).append( m_idTypeCode );
        buf.append( ", m_provinceCode: " ).append( m_provinceCode );
        buf.append( ", lastUpdateTimestamp: " ).append( m_lastUpdateTimestamp );
        buf.append( " }" );
        return buf.toString();
    }
}