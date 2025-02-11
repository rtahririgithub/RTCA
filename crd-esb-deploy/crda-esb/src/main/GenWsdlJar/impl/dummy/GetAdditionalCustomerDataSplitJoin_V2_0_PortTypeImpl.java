package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * GetAdditionalCustomerDataSplitJoin_V2_0_PortTypeImpl class implements web service endpoint interface GetAdditionalCustomerDataSplitJoin_V2_0_PortType */

@WebService(
  serviceName="GetAdditionalCustomerDataSplitJoin_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/GetAdditionalCustomerDataSplitJoin_2",
  endpointInterface="dummy.GetAdditionalCustomerDataSplitJoin_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/GetAdditionalCustomerDataSplitJoin",serviceUri="GetAdditionalCustomerDataSplitJoin_v2_0",portName="GetAdditionalCustomerDataSplitJoinPort")
public class GetAdditionalCustomerDataSplitJoin_V2_0_PortTypeImpl implements GetAdditionalCustomerDataSplitJoin_V2_0_PortType {
 
  public GetAdditionalCustomerDataSplitJoin_V2_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.getadditionalcustomerdatasplitjoinrequestresponse_v2.GetAdditionalCustomerDataResponse getAdditionalCustomerData(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.getadditionalcustomerdatasplitjoinrequestresponse_v2.GetAdditionalCustomerData parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }
}  