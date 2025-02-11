package com.telus.credit.orderDepositCalculator.webservice.util;

import java.math.BigDecimal;
import org.dozer.CustomConverter;


public class ChargeAmtConverter implements CustomConverter
{
	@Override
	public Object convert(Object destination, Object source, 
		      Class destClass, Class sourceClass) 
	{
		BigDecimal destChargeAmt = (BigDecimal)destination;
		BigDecimal srcChargeAmt  = (BigDecimal)source;
		BigDecimal defautChargeAmt = new BigDecimal("0");
		
		return destChargeAmt = (srcChargeAmt != null ? srcChargeAmt : defautChargeAmt);
	}
}
