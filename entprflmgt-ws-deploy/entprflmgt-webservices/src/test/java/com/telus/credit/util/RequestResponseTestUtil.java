package com.telus.credit.util;



import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;

import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;

public class RequestResponseTestUtil extends RequestResponseTestUtilBase{
	
 	public static UpdateCreditProfile createUpdateCreditProfileRequest( String requestFolder ,String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
 		 xmlfilename = requestFolder  + xmlfilename;  
 		String nodeName = "updateCreditProfile";
	      return (UpdateCreditProfile)convertXmlCommon(xmlfilename, nodeName, UpdateCreditProfile.class);
	  }	
}
