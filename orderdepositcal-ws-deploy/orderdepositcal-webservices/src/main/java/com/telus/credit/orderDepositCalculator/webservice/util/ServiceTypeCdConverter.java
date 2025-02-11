package com.telus.credit.orderDepositCalculator.webservice.util;

import org.dozer.CustomConverter;

import com.telus.credit.orderDepositCalculator.webservice.impl.OrderDepositCalculatorConst;

public class ServiceTypeCdConverter implements CustomConverter  
{

	@Override
	public Object convert(Object destination, Object source, 
		      Class destClass, Class sourceClass) 
	{
		String destServiceTypeCd = (String)destination;
		String srcServiceTypeCd  = (String)source;
		
		if ((OrderDepositCalculatorConst.FICO_SING_RESTRICT).equals(srcServiceTypeCd))
			destServiceTypeCd=OrderDepositCalculatorConst.SING;
		else 
			destServiceTypeCd=srcServiceTypeCd;
		
		return destServiceTypeCd;
	}
 }

