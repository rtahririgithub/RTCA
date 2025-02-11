/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
 */

package com.telus.credit.documentum.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CreditReportDocument extends UserAttachmentDocument {
	private final static String CARID_ATTR = "crd_carid";
	private final static String LEG_NAME_ATTR = "crd_legalname";
	private final static String ADDR_LINE_ATTR = "crd_addr_line";
	private final static String CITY_ATTR = "crd_city";
	private final static String COUNTRY_ATTR = "crd_country";
	private final static String PROVINCE_ATTR = "crd_province";
    private final static String CUSTOMER_ID_ATTR = "crd_customerid";

	private static final String[] localAttrNames = 
	    new String[]{CARID_ATTR, LEG_NAME_ATTR, ADDR_LINE_ATTR, CITY_ATTR, COUNTRY_ATTR, PROVINCE_ATTR,CUSTOMER_ID_ATTR};
	

	private long m_carId;
	private String m_legalName;
	private String m_addressLine;
	private String m_city;
	private String m_country;
	private String m_province;
        private long m_customerId;

	CreditReportDocument(String fileName) {
		super(fileName);
	}
	
	public long getCarId() {
		return m_carId;
	}
	public void setCarId(long carId) {
		this.m_carId = carId;
	}
    public long getCustomerId() {
	return m_customerId;
    }
    public void setCustomerId(long customerId) {
	m_customerId = customerId;
    }
    
	public String getLegalName() {
		return m_legalName;
	}
	public void setLegalName(String legalName) {
		this.m_legalName = legalName;
	}
	public String getAddressLine() {
		return m_addressLine;
	}
	public void setAddressLine(String addressLine) {
		this.m_addressLine = addressLine;
	}
	public String getCity() {
		return m_city;
	}
	public void setCity(String city) {
		this.m_city = city;
	}
	public String getCountry() {
		return m_country;
	}
	public void setCountry(String country) {
		this.m_country = country;
	}
	public String getProvince() {
		return m_province;
	}
	public void setProvince(String province) {
		this.m_province = province;
	}
	
	public List<String> getMetadataAttrNames() {
		List<String> attrNames = new ArrayList<String>();
		attrNames.addAll(Arrays.asList(localAttrNames));
		return attrNames;
	}

	public Object getValueForAttribute(String attrName) {
		if (attrName.equalsIgnoreCase(CARID_ATTR)){
			return getCarId();
		} else if (attrName.equalsIgnoreCase(LEG_NAME_ATTR)) {
			return getLegalName();
		} else if(attrName.equalsIgnoreCase(ADDR_LINE_ATTR)) {
			return getAddressLine();
		} else if(attrName.equalsIgnoreCase(CITY_ATTR)) {
			return getCity();
		} else if(attrName.equalsIgnoreCase(COUNTRY_ATTR)) {
			return getCountry();
		} else if(attrName.equalsIgnoreCase(PROVINCE_ATTR)) {
			return getProvince();
		} 
		else if(attrName.equalsIgnoreCase(CUSTOMER_ID_ATTR)) {
		    return getCustomerId();
		}
		else {
			return null;
		}
	}
	
	public void setValueForAttribute(String attrName, Object value) {
		if (attrName.equalsIgnoreCase(CARID_ATTR)){
			 setCarId(((Integer)value).longValue());
		} else if (attrName.equalsIgnoreCase(LEG_NAME_ATTR)) {
			setLegalName((String)value);
		} else if(attrName.equalsIgnoreCase(ADDR_LINE_ATTR)) {
			setAddressLine((String)value);
		} else if(attrName.equalsIgnoreCase(CITY_ATTR)) {
			setCity((String)value);
		} else if(attrName.equalsIgnoreCase(COUNTRY_ATTR)) {
			setCountry((String)value);
		} else if(attrName.equalsIgnoreCase(PROVINCE_ATTR)) {
			setProvince((String)value);
		} else if (attrName.equalsIgnoreCase(CUSTOMER_ID_ATTR)) {
		    setCustomerId(((Integer)value).longValue());
		}
	}
	
	
}
