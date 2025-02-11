
package com.telus.credit.publisher.util;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import com.telus.credit.publisher.Characteristic;
import com.telus.credit.publisher.Event;

/**
 * Utility methods for creating and managing Event Object. 
 * 
 * @author t837068
 *
 */
public class EventUtils {
	/**
	 * Helper method for creating a Characteristic property with single value for the Object.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Characteristic createCharacteristicForSingleValue(String name, String value) {
		return CreateCharacteristic(name, Arrays.asList(value));
	}
	
	/**
	 * Helper method for creating a Characteristic property with multiple values for the Object.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public static Characteristic CreateCharacteristic(String name, List<String> value) {
		Characteristic chara = new Characteristic();
		chara.setName(name);
		chara.getValue().addAll(value);
		
		return chara;
	}
	
	
	/**
	 * Helper method for manual xml date printing
	 * 
	 * @param dt
	 * @return
	 */
	public static String printDatetime(Date dt) {
		Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return DatatypeConverter.printDateTime( cal );
	}
	
	/**
     * Helper method to convert EventObject to xml string
     * 
     * @param event
     * @return
     * @throws JAXBException
     */
    public static String convertEventObjToXml(Event event) throws JAXBException
    {
        StringWriter writer = new StringWriter();

        JAXBContext jaxbContext = JAXBContext.newInstance( Event.class );
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, true );

        QName qName = new QName( "", "Event" );
        JAXBElement<Event> root = new JAXBElement<Event>( qName, Event.class,
                event );

        jaxbMarshaller.marshal( root, writer );

        return writer.toString();
    }
}
