package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * GetWlsAccount_V2_0_PortTypeImpl class implements web service endpoint interface GetWlsAccount_V2_0_PortType */

@WebService(
  serviceName="GetWlsAccount_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/GetWlsAccount_2",
  endpointInterface="dummy.GetWlsAccount_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/GetWlsAccount",serviceUri="GetWlsAccount_v2_0",portName="GetWlsAccountPort")
public class GetWlsAccount_V2_0_PortTypeImpl implements GetWlsAccount_V2_0_PortType {
 
  public GetWlsAccount_V2_0_PortTypeImpl() {
  
  }

  public javax.xml.soap.SOAPElement[] getWlsAccount(java.lang.String billingAccountNumber) 
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