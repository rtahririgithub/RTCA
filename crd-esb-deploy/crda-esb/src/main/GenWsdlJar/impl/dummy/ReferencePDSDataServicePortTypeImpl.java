package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * ReferencePDSDataServicePortTypeImpl class implements web service endpoint interface ReferencePDSDataServicePortType */

@WebService(
  serviceName="ReferencePDSDataService",
  targetNamespace="http://telus.com/wsdl/ERM/RefPds/ReferencePDSDataService_1",
  endpointInterface="dummy.ReferencePDSDataServicePortType")
@WLHttpTransport(contextPath="telus-ref-rpds-webservices",serviceUri="ReferencePDSDataService",portName="ReferencePDSDataServicePort")
public class ReferencePDSDataServicePortTypeImpl implements ReferencePDSDataServicePortType {
 
  public ReferencePDSDataServicePortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetSubscriptionsResponse getSubscriptions(com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetSubscriptions parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetReferenceDataResponse getReferenceData(com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetReferenceData parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetInstancesResponse getInstances(com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetInstances parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetInstanceResponse getInstance(com.telus.tmi.xmlschema.srv.erm.refpds.referencepdsdataservicerequestresponse_v1_0.GetInstance parameters) 
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