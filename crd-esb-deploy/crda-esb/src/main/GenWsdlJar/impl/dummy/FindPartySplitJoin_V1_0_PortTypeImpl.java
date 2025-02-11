package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * FindPartySplitJoin_V1_0_PortTypeImpl class implements web service endpoint interface FindPartySplitJoin_V1_0_PortType */

@WebService(
  serviceName="FindPartySplitJoin_v1_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/FindPartySplitJoin_1",
  endpointInterface="dummy.FindPartySplitJoin_V1_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/FindPartySplitJoin",serviceUri="FindPartySplitJoin_v1_0",portName="FindPartySplitJoinPort")
public class FindPartySplitJoin_V1_0_PortTypeImpl implements FindPartySplitJoin_V1_0_PortType {
 
  public FindPartySplitJoin_V1_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.findpartysplitjoinrequestresponse_v1.FindPartyParallelResponse findPartyParallel(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.findpartysplitjoinrequestresponse_v1.FindPartyParallelRequest parameters) 
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