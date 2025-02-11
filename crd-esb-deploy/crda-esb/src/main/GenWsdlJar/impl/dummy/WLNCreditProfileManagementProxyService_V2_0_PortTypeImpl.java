package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * WLNCreditProfileManagementProxyService_V2_0_PortTypeImpl class implements web service endpoint interface WLNCreditProfileManagementProxyService_V2_0_PortType */

@WebService(
  serviceName="WLNCreditProfileManagementProxyService_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/WLNCreditProfileManagementProxyService_2",
  endpointInterface="dummy.WLNCreditProfileManagementProxyService_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/WLNCreditProfileManagementProxy",serviceUri="WLNCreditProfileManagementProxyService_v2_0",portName="WLNCreditProfileManagementProxyServicePort")
public class WLNCreditProfileManagementProxyService_V2_0_PortTypeImpl implements WLNCreditProfileManagementProxyService_V2_0_PortType {
 
  public WLNCreditProfileManagementProxyService_V2_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlncreditprofilemanagementproxyservicerequestresponse_v2.AssessedCreditWorthiness assessCreditWorthiness(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlncreditprofilemanagementproxyservicerequestresponse_v2.BaseCreditWorthinessRequest assessCreditWorthinessRequest,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlncreditprofilemanagementproxyservicerequestresponse_v2.OverridenCreditWorthiness overrideCreditWorthiness(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlncreditprofilemanagementproxyservicerequestresponse_v2.OverrideCreditWorthinessRequest overrideCreditWorthinessRequest,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public java.lang.String ping() 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }
}  