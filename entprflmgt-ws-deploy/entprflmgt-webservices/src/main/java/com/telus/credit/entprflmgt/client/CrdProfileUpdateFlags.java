package com.telus.credit.entprflmgt.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Used to transfer information about consumer's credit profiles from ConsumerCustomerManagementServise 
 * and EnterpriseCreditProfileManagementService.
 * <p>
 * This class contains flags indicating in what systems consumer has credit profiles
 * along with a list of billing account numbers from KB (if any).
 * 
 * @author Danil Glinenko
 *
 */
public class CrdProfileUpdateFlags {
	private boolean m_hasWirelineProfile;
	private boolean m_hasWirelessProfile;
	private Set<String> m_wirelessBANs;
	private Long m_customerId;
	private int m_noOfWirelessBANs;
	private int m_noOfWirelineBANs;
	private Date m_wirelineBANCreationDate;
	private String m_wirelineBANStatus;

	public CrdProfileUpdateFlags(boolean hasWirelineProfile, boolean hasWirelessProfile, 
			Set<String> wirelessBANs, Long customerId, int noOfWirelessBANs, int noOfWirelineBANs,
			Date wirelineBANCreationDate, String wirelineBANStatus ) {
		this.m_hasWirelineProfile = hasWirelineProfile;
		this.m_hasWirelessProfile = hasWirelessProfile;
		this.m_wirelessBANs = wirelessBANs;
		this.m_customerId = customerId;
		m_noOfWirelessBANs = noOfWirelessBANs;
		m_noOfWirelineBANs = noOfWirelineBANs;
		m_wirelineBANCreationDate =  wirelineBANCreationDate;
		m_wirelineBANStatus = wirelineBANStatus;
	}
	
	public int getNoOfWirelessBANs() {
	    return m_noOfWirelessBANs;
	}
	
	public int getNofWirelineBANs() {
	    return m_noOfWirelineBANs;
	}
	
	public Date getWirelineBANCreationDate() {
	    return m_wirelineBANCreationDate;
	}
	
	public String getWirelineBANStatus() {
	    return m_wirelineBANStatus;
	}

	public boolean hasWirelineProfile() {
		return m_hasWirelineProfile;
	}
	public boolean hasWirelessProfile() {
		return m_hasWirelessProfile;
	}
	public Set<String> getWirelessBANs(){
		if (m_wirelessBANs == null){
			return new HashSet<String>();
		}
		return m_wirelessBANs;
	}
	
	public Long getCustomerId() {
		return this.m_customerId;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder().append("WLS profile: " + m_hasWirelessProfile + "\n")
		.append("WLN profile: " + m_hasWirelineProfile + "\n");
		sb.append("Wireless BANs: ");
		for (String ban : getWirelessBANs()){
			sb.append(ban).append(", ");
		}
		sb.append("\n");
		sb.append( "\nCustomer id: " + m_customerId );
		sb.append( "\nNo of wireless BANs: " + m_noOfWirelessBANs );
		sb.append( "\nNo of wireline BANs: " +  m_noOfWirelineBANs );
		sb.append( "\nWireline BAN Creation Date: " +  m_wirelineBANCreationDate );
		sb.append( "\nWireline BAN Status: " + m_wirelineBANStatus );
		
		return sb.toString();
	}
	
	@Override
	public boolean equals( Object obj ) {
		boolean result = false;
		if ( obj != null && obj instanceof CrdProfileUpdateFlags ) {
			CrdProfileUpdateFlags crdProfileUpdateFlagsObj = (CrdProfileUpdateFlags) obj;
			result = ( m_hasWirelineProfile == crdProfileUpdateFlagsObj.m_hasWirelineProfile )
					 && ( m_hasWirelessProfile == crdProfileUpdateFlagsObj.m_hasWirelessProfile )
					 && ( ( m_customerId == null && crdProfileUpdateFlagsObj.m_customerId == null )
					      || (m_customerId!=null && crdProfileUpdateFlagsObj.m_customerId != null 
					          && m_customerId.longValue() == crdProfileUpdateFlagsObj.m_customerId.longValue() ) )
					 && ( m_noOfWirelessBANs == crdProfileUpdateFlagsObj.m_noOfWirelessBANs )
					 && ( ( m_wirelineBANCreationDate == null && crdProfileUpdateFlagsObj.m_wirelineBANCreationDate == null )
					 	  || (m_wirelineBANCreationDate != null && crdProfileUpdateFlagsObj.m_wirelineBANCreationDate != null
					 	       && m_wirelineBANCreationDate.equals(crdProfileUpdateFlagsObj.m_wirelineBANCreationDate ) ) )
					 && ( m_wirelineBANStatus.equals(crdProfileUpdateFlagsObj.m_wirelineBANStatus ) )
					 && ( ( m_wirelessBANs == null && crdProfileUpdateFlagsObj.m_wirelessBANs == null )
						  || ( m_wirelessBANs != null && crdProfileUpdateFlagsObj.m_wirelessBANs != null
						       && m_wirelessBANs.equals( crdProfileUpdateFlagsObj.m_wirelessBANs ) ) );
		}
		return result;
	}
}
