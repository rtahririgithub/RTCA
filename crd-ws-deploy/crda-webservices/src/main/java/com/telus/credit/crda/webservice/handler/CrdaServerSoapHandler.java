package com.telus.credit.crda.webservice.handler;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CrdaServerSoapHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Log log = LogFactory.getLog(CrdaServerSoapHandler.class);
 
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    public void close(MessageContext context) {
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SOAPMessage message = messageContext.getMessage();
        if (outboundProperty.booleanValue()) {
        	 ByteArrayOutputStream out = new ByteArrayOutputStream();
        	try {	           
                message.writeTo(out);
                log.info("\n\n CrdaServerSoapHandler_Outbound_message: ");
                log.info("\n\n " + removeBrkLine(out.toString()) + "\n\n ");
                log.info("\n\n end_of_CrdaServerSoapHandler_Outbound_message. ");
	            
            } catch (Exception e) {
                log.error("***\n exception caught in CrdaServerSoapHandler.handleMessage() Outbound message \n");
                log.info(e);
            }finally{
            	if(out!=null){ 
	            	try {
						out.close();
					} catch (IOException e) {
						log.info(e);
					}
            	}
            }

        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
	        try{
	            message.writeTo(out);
	            log.info("\n\n CrdaServerSoapHandler_Inbound_message: ");
	            log.info("\n\n " + out.toString() + "\n\n ");
	            log.info("\n\n end_of_CrdaServerSoapHandler_Inbound_message. ");  
	            
	            printSOAPHeaders(messageContext);
	        } catch (Exception e) {
	            log.error("***\n exception caught in CrdaClientSoapHandler.handleMessage() Inbound message \n ");
	            log.info(e);	
	        }finally{
            	if(out!=null){ 
	            	try {
						out.close();
					} catch (IOException e) {
						log.info(e);
					}
            	}
            }          
        }
        return true;
    }
	private static String removeBrkLine(String str) {
		try{
			str = str.replaceAll("\\r\\n|\\r|\\n", " ");
		}catch (Throwable e){}
		return str;
	}
    private void printSOAPHeaders(SOAPMessageContext context) {
        try {
            SOAPHeader header = context.getMessage().getSOAPHeader();
            //Properties props = new Properties();
            Iterator<?> it = header.examineAllHeaderElements();
            while (it.hasNext()) {
                SOAPHeaderElement elem = (SOAPHeaderElement) it.next();
                log.info(elem.getNodeName() + "-" + elem.getTextContent());
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
