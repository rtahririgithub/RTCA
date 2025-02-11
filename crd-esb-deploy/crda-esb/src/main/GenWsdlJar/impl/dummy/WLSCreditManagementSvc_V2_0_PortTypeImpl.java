package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * WLSCreditManagementSvc_V2_0_PortTypeImpl class implements web service endpoint interface WLSCreditManagementSvc_V2_0_PortType */

@WebService(
  serviceName="WLSCreditManagementSvc_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/WLSCreditManagementSvc_2",
  endpointInterface="dummy.WLSCreditManagementSvc_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/WLSCreditManagement",serviceUri="WLSCreditManagementSvc_v2_0",portName="WLSCreditManagementSvcPort")
public class WLSCreditManagementSvc_V2_0_PortTypeImpl implements WLSCreditManagementSvc_V2_0_PortType {
 
  public WLSCreditManagementSvc_V2_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditWorthinessResponse getCreditWorthiness(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditWorthiness parameters) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditProfileResponse getCreditProfile(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.GetCreditProfile parameters) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.UpdateConsumerCreditInformationResponse updateConsumerCreditInformation(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.UpdateConsumerCreditInformation parameters) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.ValidateCreditIdResponse validateCreditId(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlscreditmanagementsvcrequestresponse_v2.ValidateCreditId parameters) 
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