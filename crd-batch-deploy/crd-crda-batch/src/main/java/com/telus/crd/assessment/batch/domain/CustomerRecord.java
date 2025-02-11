package com.telus.crd.assessment.batch.domain;

import java.util.Date;

public class CustomerRecord extends AbstractCustomerRecord
{
    private long customerMasterSourceId;
    private String title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nameSuffix;
    private Date createDate;
    private String customerType;
    private String customerSubType;
    private String customerStatusCode;
    private String revenueSegmentCode;
    private Boolean employeeIndicator;
    private String employeeIndicatorStr;

    private final static String blank = "";

    public long getCustomerMasterSourceId()
    {
        return customerMasterSourceId;
    }


    public void setCustomerMasterSourceId(long customerMasterSourceId)
    {
        this.customerMasterSourceId = customerMasterSourceId;
    }


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getFirstName()
    {
        return firstName;
    }


    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }


    public String getMiddleName()
    {
        return middleName;
    }


    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }


    public String getLastName()
    {
        return lastName;
    }


    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }


    public String getNameSuffix()
    {
        return nameSuffix;
    }


    public void setNameSuffix(String nameSuffix)
    {
        this.nameSuffix = nameSuffix;
    }


    public Date getCreateDate()
    {
        return createDate;
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }


    public String getCustomerType()
    {
        return customerType;
    }


    public void setCustomerType(String customerType)
    {
        this.customerType = customerType;
    }


    public String getCustomerSubType()
    {
        return customerSubType;
    }


    public void setCustomerSubType(String customerSubType)
    {
        this.customerSubType = customerSubType;
    }


    public String getCustomerStatusCode()
    {
        return customerStatusCode;
    }


    public void setCustomerStatusCode(String customerStatusCode)
    {
        this.customerStatusCode = customerStatusCode;
    }


    public String getRevenueSegmentCode()
    {
        return revenueSegmentCode;
    }


    public void setRevenueSegmentCode(String revenueSegmentCode)
    {
        this.revenueSegmentCode = revenueSegmentCode;
    }

    public Boolean getEmployeeIndicator()
    {
    	Boolean ind =null;
		if(employeeIndicatorStr != null && !blank.equalsIgnoreCase(employeeIndicatorStr.trim()))
		{
			
			ind = true;
     	}
		return ind;
       
    }
    public void setEmployeeIndicator(Boolean employeeIndicator)
    {
        this.employeeIndicator = employeeIndicator;
    }

    public String getEmployeeIndicatorStr() {
		return employeeIndicatorStr;
	}


	public void setEmployeeIndicatorStr(String employeeIndicatorStr) {
		this.employeeIndicatorStr = employeeIndicatorStr;
	}


	
   
}
