package com.telus.credit.entprflmgt.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
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
	
    public static Object convertXmlCommon(String xmlFilename,String nodeName,Class clazz ) throws JAXBException, XMLStreamException, IOException {	
   	String requestStr =  readFileToString(new File(xmlFilename));
       XMLInputFactory xif = XMLInputFactory.newInstance(); 
       XMLStreamReader xmlReader = xif.createXMLStreamReader(new StringReader(requestStr));
       String LocalName="";
       int event = 0;
       for (event = xmlReader.next(); event != XMLStreamReader.END_DOCUMENT; event = xmlReader.next()) {
           if (event == XMLStreamReader.START_ELEMENT) {
           	 LocalName = 	xmlReader.getLocalName() ;
               if (xmlReader.getLocalName().equalsIgnoreCase(nodeName)) {
                   break;
               }
           }
       }
       Object aObject = null;
       if (event != XMLStreamReader.END_DOCUMENT) {
           JAXBContext jaxbContext = getJAXBContext(clazz);
           JAXBElement<Object> result = jaxbContext.createUnmarshaller().unmarshal(xmlReader, clazz);
           aObject = result.getValue();
       }
       return aObject;
   }
    public static JAXBContext getJAXBContext(Class clazz) {
        String packageName = clazz.getPackage().getName();
        try {
        	return JAXBContext.newInstance(packageName);
            //return jaxbContextsCache.get(packageName);
        } catch (Throwable e) {
            throw new RuntimeException("Error when getting JAXBContext from cache", e);
        }
    }
    public static String readFileToString(File file) throws IOException {
        Scanner scanner = new Scanner(file).useDelimiter("\\Z");
        String contents = scanner.next();
        return contents.replaceAll("[\\n\\t\\r]", "");
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

}
