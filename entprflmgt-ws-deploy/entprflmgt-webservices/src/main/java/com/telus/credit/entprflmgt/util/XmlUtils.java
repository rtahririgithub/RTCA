package com.telus.credit.entprflmgt.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;


/**
 * Utility class that helps to deal with converting JAXB-annotated POJOs to XML.
 */
public final class XmlUtils {
    private XmlUtils(){
        //private constructor to prevent instantiation.
    }

    /**
     * Converts given JAXB-annotated POJO to an XML represented as String. 
     * @param obj - JAXB annotated object to be converted to xml.
     * @return result - xml as String representing given java object.
     */
    public static String convertToXml(Object obj) {
        if (obj == null) { throw new IllegalArgumentException("obj cannot be null."); }

        final String result;
        final StringWriter sw= new StringWriter();
        try {
            JAXBContext jxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jxbContext.createMarshaller();
            marshaller.marshal(obj, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
