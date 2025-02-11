package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * UnitTest_V1_0_PortTypeImpl class implements web service endpoint interface UnitTest_V1_0_PortType */

@WebService(
  serviceName="UnitTest_v1_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/UnitTest_1",
  endpointInterface="dummy.UnitTest_V1_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/UnitTest",serviceUri="UnitTest_v1_0",portName="UnitTestPort")
public class UnitTest_V1_0_PortTypeImpl implements UnitTest_V1_0_PortType {
 
  public UnitTest_V1_0_PortTypeImpl() {
  
  }

  public java.lang.Object performUnitTest(java.lang.Object parameters) 
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