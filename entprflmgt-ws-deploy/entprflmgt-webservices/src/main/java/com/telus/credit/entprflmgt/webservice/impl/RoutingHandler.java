package com.telus.credit.entprflmgt.webservice.impl;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RoutingHandler implements SOAPHandler<SOAPMessageContext>
{
    private static final Log s_log = LogFactory.getLog(RoutingHandler.class);

    private static final ThreadLocal<String> s_emHeader = new ThreadLocal<String>();
    
    public static String getEmHeader()
    {
        return s_emHeader.get();
    }
    
    public static String getSOAPHeader() {
    	return "<soap:Header xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +  getEmHeader() + "</soap:Header>";
    }
    
    /**
     * Called by junit tests
     * @param emHeader
     * @return
     */
    public static void setEmHeader(String emHeader) {
    	s_emHeader.set(emHeader);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean handleMessage(SOAPMessageContext soapContext)
    {
    	s_log.debug("Enter RoutingHandler.handleMessage" ); 
        Boolean outbound = (Boolean )soapContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if( !outbound.booleanValue() )
        {
            try
            {
                s_emHeader.set(null);

                QName qName = new QName("http://schemas.tmi.telus.com/Enterprise/BaseTypes/telus_em_header_v1", "emHeader");
                SOAPHeader header = soapContext.getMessage().getSOAPHeader();
                s_log.debug("Got soap header.");
                Iterator<SOAPElement> headers = header.getChildElements(qName);
                if( headers.hasNext() )
                {
                	String emHeaderToSet = headers.next().toString();
                	s_log.debug("Set EM Header ro: " + emHeaderToSet );
                    s_emHeader.set(emHeaderToSet);
                }
            }
            catch( Exception e )
            {
                s_log.error("Unable to obtain header information", e);
            }
        }
        s_log.debug("Exit RoutingHandler.handleMessage" ); 
        return true;
    }


    @Override
    public boolean handleFault(SOAPMessageContext context)
    {
        return true;
    }


    @Override
    public void close(MessageContext context)
    {
    }


    @Override
    public Set<QName> getHeaders()
    {
    	return Collections.emptySet();
    	//return null;
    }
}
