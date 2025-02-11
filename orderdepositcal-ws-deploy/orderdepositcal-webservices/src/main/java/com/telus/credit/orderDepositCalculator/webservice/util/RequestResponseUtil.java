package com.telus.credit.orderDepositCalculator.webservice.util;



import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.telus.credit.orderDepositCalculator.domain.CalculateDeposit;
import com.telus.credit.orderDepositCalculator.domain.CalculateDepositResponse;

import com.fico.telus.blaze.depositCalculator.DepositCalculatorRequest;
import com.fico.telus.blaze.depositCalculator.DepositCalculationResult;

import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationList;
import com.telus.credit.orderDepositCalculator.domain.GetEquipmentQualificationListResponse;


public class RequestResponseUtil extends RequestResponseUtilBase{
	
 	public static CalculateDeposit createCalculateDepositRequest( String requestFolder ,String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
 		 xmlfilename = requestFolder  + xmlfilename;  
 		String nodeName = "calculateDeposit";
	      return (CalculateDeposit)convertXmlCommon(xmlfilename, nodeName, CalculateDeposit.class);
	  }
 	
 	public static DepositCalculatorRequest createDepositCalculatorRequest( String requestFolder ,String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
		 xmlfilename = requestFolder  + xmlfilename;  
		String nodeName = "depositCalculatorRequest";
	      return (DepositCalculatorRequest)convertXmlCommon(xmlfilename, nodeName, DepositCalculatorRequest.class);
	  }
 	
 	public static DepositCalculationResult createDepositCalculationResult( String requestFolder ,String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
		 xmlfilename = requestFolder  + xmlfilename;  
		String nodeName = "depositCalculationResult";
	      return (DepositCalculationResult)convertXmlCommon(xmlfilename, nodeName, DepositCalculationResult.class);
	  }

	public static CalculateDepositResponse createCalculateDepositResponse(String requestFolder, String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
		xmlfilename = requestFolder  + xmlfilename; 
		  String nodeName = "calculateDepositResponse";
	    	return (CalculateDepositResponse)convertXmlCommon(xmlfilename, nodeName, CalculateDepositResponse.class);
	 }
	
 	public static GetEquipmentQualificationList createGetEquipmentQualificationListRequest( String requestFolder ,String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
		 xmlfilename = requestFolder  + xmlfilename;  
		 String nodeName = "getEquipmentQualificationList";
	      return (GetEquipmentQualificationList)convertXmlCommon(xmlfilename, nodeName, GetEquipmentQualificationList.class);
	  }
	
	public static GetEquipmentQualificationListResponse createGetEquipmentQualificationListResponse( String requestFolder ,String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
		 xmlfilename = requestFolder  + xmlfilename;  
		String nodeName = "getEquipmentQualificationListResponse";
	      return (GetEquipmentQualificationListResponse)convertXmlCommon(xmlfilename, nodeName, GetEquipmentQualificationListResponse.class);
	  }
	
	

}
