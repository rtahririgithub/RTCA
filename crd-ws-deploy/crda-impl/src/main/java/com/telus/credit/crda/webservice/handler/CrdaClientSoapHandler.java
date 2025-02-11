package com.telus.credit.crda.webservice.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Set;
 

public class CrdaClientSoapHandler implements SOAPHandler<SOAPMessageContext> {
    private static final Log log = LogFactory.getLog(CrdaClientSoapHandler.class);

    public CrdaClientSoapHandler() {
    }


    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }


    public boolean handleMessage(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean) messageContext
                .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        SOAPMessage message = messageContext.getMessage();

        if (outboundProperty) {
            try {
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                message.writeTo(out);
                log.info("\n\n CrdaClientSoapHandler : Outbound message: ");
                log.info("\n\n " + out.toString() + "\n\n ");
                log.info("\n\n end of CrdaClientSoapHandler : Outbound message: ");

            } catch (Exception e) {

                log.error("***exception caught in CrdaClientSoapHandler.handleMessage() Outbound message");
                log.info(e);
            }
        } else {
            try {

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                message.writeTo(out);
                log.info("\n\n CrdaClientSoapHandler : Inbound message: ");
                log.info("\n\n " + out.toString() + "\n\n ");
                log.info("\n\n end of CrdaClientSoapHandler : Inbound message: ");
            } catch (Exception e) {

                log.error("***\n\n exception caught in CrdaClientSoapHandler.handleMessage() Inbound message \n\n ");
                log.info(e);


            }
        }


        return true;
    }


    public boolean handleFault(SOAPMessageContext messageContext) {
        return true;
    }


    public void close(MessageContext messageContext) {
    }
}
