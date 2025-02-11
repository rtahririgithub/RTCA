/**
 * 
 */
package com.telus.credit.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.telus.credit.util.CreditMgtUtils;

/**
 * @author x122365
 *
 */
public class CreditAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5900354754535107794L;
	
	/**
     * Code of "Current Province of Residency". 
     */
    public static final String CURRENT_PROVINCE_RESIDENCY_CODE = "CPR";
    
    /**
     * Code of "Latest Assessment Date" (optional). 
     */
    public static final String LATEST_ASSESSMENT_DATE = "LAD";
    
    /**
     * Code of "First Assessment Date" (optional). 
     */
    public static final String FIRST_ASSESSMENT_DATE = "FAD";
    
    /**
     * Code of "Credit Value Effective Date" (mandatory). 
     * Attribute shall be updated only if credit value was changed. 
     * If credit value stay same no change to the date will apply.
     */
    public static final String CREDIT_VALUE_EFFECTIVE_DATE = "CVED";
    
    /**
     * Code of "Credit Report Indicator". 
     */
    public static final String CREDIT_REPORT_INDICATOR = "CRI";
    
    /**
     * Represents unique identifier for object of this type, its "record id" in the database.
     * It is used by the DAO to persist this object to the database. 
     */
    private long m_id;

    /**
     * Represents unique identifier for the parent Credit Profile object, 
     * its "record id" in the database.
     */
    private long m_creditProfileId;
    
    private Timestamp m_lastUpdateTimestamp;
    
    /**
     * Code of attribute. Possible values are:
     * <ul>
     * 	<li>"CPR"  - Current Province of Residency.</li>
     *  <li>"FAD"  - First Assessment Date.</li>
     *  <li>"LAD"  - Last Assessment Date.</li>
     *  <li>"CVED" - Credit Value Effective Date.</li>
     *  <li>"CRI"  - Credit Report Indicator.</li>
     * </ul>
     */
    private String m_attributeCode;

    
    private String m_attributeValue;


	/**
	 * @return the m_id
	 */
	public long getId() {
		return m_id;
	}



	/**
	 * @param m_id the m_id to set
	 */
	public void setId(long m_id) {
		this.m_id = m_id;
	}



	/**
	 * @return the m_creditProfileId
	 */
	public long getCreditProfileId() {
		return m_creditProfileId;
	}



	/**
	 * @param m_creditProfileId the m_creditProfileId to set
	 */
	public void setCreditProfileId(long m_creditProfileId) {
		this.m_creditProfileId = m_creditProfileId;
	}



	/**
	 * @return the m_lastUpdateTimestamp
	 */
	public Timestamp getLastUpdateTimestamp() {
		return m_lastUpdateTimestamp;
	}



	/**
	 * @param m_lastUpdateTimestamp the m_lastUpdateTimestamp to set
	 */
	public void setLastUpdateTimestamp(Timestamp m_lastUpdateTimestamp) {
		this.m_lastUpdateTimestamp = m_lastUpdateTimestamp;
	}



	/**
	 * @return the m_attributeCode
	 */
	public String getAttributeCode() {
		return m_attributeCode;
	}



	/**
	 * @param m_attributeCode the m_attributeCode to set
	 */
	public void setAttributeCode(String m_attributeCode) {
		this.m_attributeCode = m_attributeCode;
	}



	/**
	 * @return the m_attributeValue
	 */
	public String getAttributeValue() {
		return m_attributeValue;
	}



	/**
	 * @param m_attributeValue the m_attributeValue to set
	 */
	public void setAttributeValue(String m_attributeValue) {
		this.m_attributeValue = m_attributeValue;
	}
	
	/**
     * Compares two objects of this type.
     * @param o object to compare with this object.
     * @return true if all fields in both objects are identical; false otherwise.
     */
    public boolean equals(Object o)
    {
        if ( !(o instanceof CreditAttribute) ) {
            return false;
        }
        CreditAttribute ca = (CreditAttribute) o;

        if ( CreditMgtUtils.areEqual( this.m_attributeCode, ca.m_attributeCode )
             && CreditMgtUtils.areEqual( this.m_attributeValue, ca.m_attributeValue )) 
        {
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
    
	/**
     * Output the domain object into a string
     */
	public String toString() {		
		StringBuffer sb = new StringBuffer();
		
		sb.append("\n");
		sb.append("CreditAttribute:\n");
		sb.append("CPROFL_ATTRIBUTE_ID: " + this.getId() + "\n");
		sb.append("CreditProfileId: " + this.getCreditProfileId() + "\n" );
		sb.append("AttributeCode: " + this.getAttributeCode() + "\n" );
		sb.append("AttributeValue: " + this.getAttributeValue() + "\n" );
		sb.append("LastUpdateTimestamp: " + this.getLastUpdateTimestamp() + "\n" );
		sb.append("\n");
						
		return sb.toString();		
	}

}
