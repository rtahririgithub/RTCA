package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * DummySvc_V2_0_PortTypeImpl class implements web service endpoint interface DummySvc_V2_0_PortType */

@WebService(
  serviceName="DummySvc_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/DummySvc_2",
  endpointInterface="dummy.DummySvc_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/DummySvc",serviceUri="DummySvc_v2_0",portName="DummySvcPort")
public class DummySvc_V2_0_PortTypeImpl implements DummySvc_V2_0_PortType {
 
  public DummySvc_V2_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.dummysvcrequestresponse.DummySvcResponse dummySvc(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.dummysvcrequestresponse.DummySvc parameters) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping parameters) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }
}  