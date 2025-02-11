package com.telus.crd.assessment.batch.domain;

public abstract class AbstractCustomerBureauRecord extends AbstractCustomerRecord
{
	private long bureauTranId;
	
    public long getBureauTranId() {
		return bureauTranId;
	}

	public void setBureauTranId(long bureauTranId) {
		this.bureauTranId = bureauTranId;
	}

	

}
