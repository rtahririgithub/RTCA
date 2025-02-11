package com.telus.credit.util;

import java.util.Date;

public class Account implements Comparable<Account> {
	private String accountStatus;
	private Date accountDate;
	private String accountNumber;
	
	
	public void setAccountNumber(String accountNumber){
		 this.accountNumber = accountNumber;
	}

	public void setAccountStatus(String accountStatus){
		this.accountStatus = accountStatus;
	}

	public void setAccountDate(Date accountDate){
		this.accountDate = accountDate;
	}

	public String getAccountStatus(){
		return accountStatus;
	}

	public String getAccountNumber(){
		return accountNumber;
	}

	public Date getAccountDate(){
		return accountDate;
	}
	
	  @Override
	  public int compareTo(Account o) {
	    if (getAccountDate() == null || o.getAccountDate() == null)
	      return 0;
	    return getAccountDate().compareTo(o.getAccountDate());
	  }
}
