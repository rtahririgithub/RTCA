package com.telus.credit.wlnprfldmgt.util;
import java.io.File;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;

public class TestHelper {


		public static Object convertXMLToObject(Object objClassName, String filename) throws JAXBException{
			Object xmlobj = null;
			 try {
					File file = new File(filename);				 
					JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
					Unmarshaller u =jaxbContext.createUnmarshaller();
					JAXBElement<? extends Object> root = u.unmarshal(new StreamSource(new File(filename)),
						 								objClassName.getClass());
					xmlobj = root.getValue();
				  } catch (JAXBException e) {
					throw e;
				  }
			return xmlobj;
		}

		public static void convertObjectToXml(Object objClassName){
			  try {
				  
					JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			 
					// output pretty printed
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					jaxbMarshaller.marshal(objClassName, System.out);
			 
				      } catch (JAXBException e) {
					e.printStackTrace();
				      }
			
		}
		
		public static void main(String[] args) {
		
		}

	
}
