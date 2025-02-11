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
 * <p><b>Description:</b> Represents customer address bound to a Credit Profile</p>
 *
 *
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

public class CreditAddress implements Serializable
{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -4607154588488764217L;


	public static final String CREDIT_BUREAU_ADDRESS_KEY = "CB";

    
    /**
     * Represents unique identifier of this object, 
     * its "record id" in the database.
     * It is used by the DAO to persist this object to the database. 
     */
    private long m_id;
    
    
    private Timestamp m_lastUpdateTimestamp;


    /**
     * Represents unique identifier for the parent - Credit Profile object, 
     * its "record id" in the database.
     * It is used by the DAO to persist this object to the database. 
     */
    private long m_creditProfileId;

    /**
     * Fourth line of customer address.
     */
    private String m_addressLineFour;

    /**
     * Fifth line of customer address.
     */
    private String m_addressLineFive;

    /**
     * Customer postal code.
     */
    private String m_postalCode;

    /**
     * Credit address type code. Possible values are:
     * <ul>
     * 	<li>"CB" - Credit Bureau Address.</li>	
     * </ul>
     */
    private String m_addressTypeCode;

    /**
     * First line of customer address.
     */
    private String m_addressLineOne;

    /**
     * Second line of customer address.
     */
    private String m_addressLineTwo;

    /**
     * Third line of customer address.
     */
    private String m_addressLineThree;

    /**
     * City.
     */
    private String m_city;

    /**
     * Province or state code. Possible values are:
     * <ul>
     * 		<li>"AB" - Alberta.</li>
     * 		<li>"BC" - British Columbia.</li>	
     * 		<li>"CA" - California.</li>
     * </ul>
     */
    private String m_provinceCode;

    /**
     * Country code. Possible values are:
     * <ul>
     * 		<li>"CA" - Canada.</li>
     * 		<li>"US" - United States.</li>	
     * </ul>
     */
    private String m_countryCode;


    private String m_type;
    
    
    
    public CreditAddress(){
        m_type = CREDIT_BUREAU_ADDRESS_KEY;
    }

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
     * @return Returns the m_addressLineFive.
     */
    public String getAddressLineFive()
    {
        return m_addressLineFive;
    }


    /**
     * @param lineFive The m_addressLineFive to set.
     */
    public void setAddressLineFive(String lineFive)
    {
        m_addressLineFive = lineFive;
    }


    /**
     * @return Returns the m_addressLineFour.
     */
    public String getAddressLineFour()
    {
        return m_addressLineFour;
    }


    /**
     * @param lineFour The m_addressLineFour to set.
     */
    public void setAddressLineFour(String lineFour)
    {
        m_addressLineFour = lineFour;
    }


    /**
     * @return Returns the m_addressLineOne.
     */
    public String getAddressLineOne()
    {
        return m_addressLineOne;
    }


    /**
     * @param lineOne The m_addressLineOne to set.
     */
    public void setAddressLineOne(String lineOne)
    {
        m_addressLineOne = lineOne;
    }


    /**
     * @return Returns the m_addressLineThree.
     */
    public String getAddressLineThree()
    {
        return m_addressLineThree;
    }


    /**
     * @param lineThree The m_addressLineThree to set.
     */
    public void setAddressLineThree(String lineThree)
    {
        m_addressLineThree = lineThree;
    }


    /**
     * @return Returns the m_addressLineTwo.
     */
    public String getAddressLineTwo()
    {
        return m_addressLineTwo;
    }


    /**
     * @param lineTwo The m_addressLineTwo to set.
     */
    public void setAddressLineTwo(String lineTwo)
    {
        m_addressLineTwo = lineTwo;
    }


    /**
     * @return Returns the m_addressTypeCode.
     */
    public String getAddressTypeCode()
    {
        return m_addressTypeCode;
    }


    /**
     * @param typeCode The m_addressTypeCode to set.
     */
    public void setAddressTypeCode(String typeCode)
    {
        m_addressTypeCode = typeCode;
    }


    /**
     * @return Returns the m_city.
     */
    public String getCity()
    {
        return m_city;
    }


    /**
     * @param city The city to set.
     */
    public void setCity(String city)
    {
        this.m_city = city;
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
     * @return Returns the m_zipCode.
     */
    public String getPostalCode()
    {
        return m_postalCode;
    }


    /**
     * @param code The m_zipCode to set.
     */
    public void setPostalCode(String code)
    {
        m_postalCode = code;
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
     * @param type CreditAddress type (example: Credit Bureau Address).
     */
    public void setType(String type){
        m_type = type;
    }
    
    /**
     * @return CreditAddress type.
     */
    public String getType(){
        return m_type;
    }
    
    
    /**
     * Compares two objects of this type.
     * @param o - object to compare with this object.
     * @return true if all fields in both objects are identical; false otherwise.
     */
    public boolean equals(Object o)
    {
        if ( !(o instanceof CreditAddress) ) {
            return false;
        }
        CreditAddress ca = (CreditAddress) o;

        if ( CreditMgtUtils.equals( this.m_addressLineOne, ca.getAddressLineOne() )
                && CreditMgtUtils.equals( this.m_addressLineTwo, ca.getAddressLineTwo() )
                && CreditMgtUtils.equals( this.m_addressLineThree, ca.getAddressLineThree() )
                && CreditMgtUtils.equals( this.m_addressLineFour, ca.getAddressLineFour() )
                && CreditMgtUtils.equals( this.m_addressLineFive, ca.getAddressLineFive() )
                && CreditMgtUtils.equals( this.m_city, ca.getCity() )
                && CreditMgtUtils.equals( this.m_countryCode, ca.getCountryCode() )
                && CreditMgtUtils.equals( this.m_postalCode, ca.getPostalCode() )
                && CreditMgtUtils.equals( this.m_type, ca.getType() )
                && CreditMgtUtils.equals( this.m_provinceCode, ca.getProvinceCode() ) ) {
            return true;
        }
        return false;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode(){
        return HashCodeBuilder.reflectionHashCode(this);
    }


    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "CreditAddress { " );
        buf.append( " m_id: " ).append( m_id );
        buf.append( ", creditProfileId:  " ).append( m_creditProfileId );
        buf.append( ", type: " ).append( m_type );
        buf.append( ", addressLineOne: " ).append( m_addressLineOne );
        buf.append( ", addressLineTwo: " ).append( m_addressLineTwo );
        buf.append( ", addressLineThree: " ).append( m_addressLineThree );
        buf.append( ", addressLineFour: " ).append( m_addressLineFour );
        buf.append( ", addressLineFive: " ).append( m_addressLineFive );
        buf.append( ", city: " ).append( m_city );
        buf.append( ". countryCode: " ).append( m_countryCode );
        buf.append( ", provinceCode: " ).append( m_provinceCode );
        buf.append( ", postalCode: " ).append( m_postalCode );
        buf.append( ", addressTypeCode: " ).append( m_addressTypeCode );
        buf.append( ", lastUpdateTimestamp: " ).append( m_lastUpdateTimestamp );
        buf.append( " }" );
        return buf.toString();
    }

}