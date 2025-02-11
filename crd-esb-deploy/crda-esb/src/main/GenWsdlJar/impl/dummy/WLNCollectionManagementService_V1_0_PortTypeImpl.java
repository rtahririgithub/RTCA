package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * WLNCollectionManagementService_V1_0_PortTypeImpl class implements web service endpoint interface WLNCollectionManagementService_V1_0_PortType */

@WebService(
  serviceName="WLNCollectionManagementService_v1_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/WLNCollectionManagementService_1",
  endpointInterface="dummy.WLNCollectionManagementService_V1_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/WLNCollectionManagement",serviceUri="WLNCollectionManagementService_v1_0",portName="WLNCollectionManagementServicePort")
public class WLNCollectionManagementService_V1_0_PortTypeImpl implements WLNCollectionManagementService_V1_0_PortType {
 
  public WLNCollectionManagementService_V1_0_PortTypeImpl() {
  
  }

  public boolean getCollectionIndicatorByCustomerId(long customerId,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return false;     
  }

  public com.telus.tmi.xmlschema.xsd.customer.customer.wirelinecollectionmanagementtypes_v1.CustomerCollectionData getCustomerCollectionData(long customerId,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo auditInfo) 
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