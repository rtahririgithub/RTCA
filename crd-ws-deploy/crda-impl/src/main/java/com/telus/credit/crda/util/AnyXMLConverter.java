package com.telus.credit.crda.util;
import java.io.File;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;

public class AnyXMLConverter{
	@XmlRootElement(name = "pullConsumerCreditReport") 


	public static class Foo {
		@XmlAnyElement(lax = true)
		protected List any;
		public List getAny() { return any; }
	}
	
	public static class Foo2 {
		@XmlAnyElement(lax = true)
		/* lax = true means : when dealing with this property the @XmlRootElement of the referenced object will be used.  
		 * If lax is set to false then the content will be unmarshalled as DOM nodes.
*/		protected List any;
		public List getAny() { return any; }
	}

	public static Foo analyseXml2(String filename) {
		try {
			JAXBContext jc = JAXBContext.newInstance(Foo.class);
			Unmarshaller u = jc.createUnmarshaller();
			Foo foo = (Foo) u.unmarshal(new File(filename));
	       return  foo;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    public static Object convertXMLToObject(Object objClassName, String filename) throws JAXBException {
        Object xmlobj = null;
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
            Unmarshaller u = jaxbContext.createUnmarshaller();
            JAXBElement<? extends Object> root = u.unmarshal(new StreamSource(new File(filename)),
                    objClassName.getClass());
            xmlobj = root.getValue();
        } catch (JAXBException e) {
            throw e;
        } 
        return xmlobj;
    }

    public static String convertToXml(Object obj) {
        if (obj == null) { return "obj is null."; }

        final String result;
        final StringWriter sw= new StringWriter();
        try {
            JAXBContext jxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jxbContext.createMarshaller();
           // marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(obj, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

		public static void main(String[] args) {

			Foo aFoo2 = AnyXMLConverter.analyseXml2("test/data/CGW/pullConsumerReportRequest2.xml");
			System.out.println(aFoo2.getAny());
		}
}



