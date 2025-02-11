package com.fico.telus.rtca.util;

import java.io.File;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;

import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;


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
	
	public static String convertObjectToXml(Object obj) {
        final String result;
        final StringWriter sw= new StringWriter();
        try {
            JAXBContext jxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

	/*public static void convertObjectToXml(Object objClassName){
		  try {
			  
				JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		 
				// output pretty printed
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(objClassName, System.out);
		 
			      } catch (JAXBException e) {
				e.printStackTrace();
			      }
		
	}*/
	
	public static void main(String[] args) {
		//test convertXMLToObject
		String filename = "test/data/CreditAssessment/TestPerformCreditAssessmentRequest.xml";
		NewCustomerCreditAssessmentRequest newCreditAssessmentRequest = null;
		try {
			System.out.println("convertXMLToObject");
			newCreditAssessmentRequest = (NewCustomerCreditAssessmentRequest) TestHelper.convertXMLToObject(newCreditAssessmentRequest,filename);
			TestHelper.convertObjectToXml(newCreditAssessmentRequest);
			System.out.println("convertObjectToXml");
			TestHelper.convertObjectToXml(newCreditAssessmentRequest);
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}
