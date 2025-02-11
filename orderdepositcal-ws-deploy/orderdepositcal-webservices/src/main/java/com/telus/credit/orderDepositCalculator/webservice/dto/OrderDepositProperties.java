package com.telus.credit.orderDepositCalculator.webservice.dto;

import java.io.Serializable;


/**
 * <p>
 * <b>Description </b> Provides properties for order deposit calculator,  is configurable by using spring configuration
 * </p>
 */

public class OrderDepositProperties  implements Serializable 
{

	private static final long serialVersionUID = -4432124112127577702L;
	
    private String m_offerNameCDForSINGRestrict;

	/**
	 * @return the offerNameCDForSINGRestrict
	 */
	public String getOfferNameCDForSINGRestrict() {
		return m_offerNameCDForSINGRestrict;
	}

	/**
	 * @param offerNameCDForSINGRestrict the offerNameCDForSINGRestrict to set
	 */
	public void setOfferNameCDForSINGRestrict(String offerNameCDForSINGRestrict) {
		m_offerNameCDForSINGRestrict = offerNameCDForSINGRestrict;
	}
    
    
    
 }
