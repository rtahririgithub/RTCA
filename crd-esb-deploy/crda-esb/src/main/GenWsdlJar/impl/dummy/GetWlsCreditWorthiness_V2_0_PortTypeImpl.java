package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * GetWlsCreditWorthiness_V2_0_PortTypeImpl class implements web service endpoint interface GetWlsCreditWorthiness_V2_0_PortType */

@WebService(
  serviceName="GetWlsCreditWorthiness_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/GetWlsCreditWorthiness_2",
  endpointInterface="dummy.GetWlsCreditWorthiness_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/GetWlsCreditWorthiness",serviceUri="GetWlsCreditWorthiness_v2_0",portName="GetWlsCreditWorthinessPort")
public class GetWlsCreditWorthiness_V2_0_PortTypeImpl implements GetWlsCreditWorthiness_V2_0_PortType {
 
  public GetWlsCreditWorthiness_V2_0_PortTypeImpl() {
  
  }

  public javax.xml.soap.SOAPElement[] getWlsCreditWorthiness(java.lang.String billingAccountNumber) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }

  public java.lang.String ping() 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }
}  