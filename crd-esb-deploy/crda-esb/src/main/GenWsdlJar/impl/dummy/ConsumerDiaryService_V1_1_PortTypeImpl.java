package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * ConsumerDiaryService_V1_1_PortTypeImpl class implements web service endpoint interface ConsumerDiaryService_V1_1_PortType */

@WebService(
  serviceName="ConsumerDiaryService_v1_1",
  targetNamespace="http://telus.com/wsdl/CMO/InformationMgmt/ConsumerDiaryService_1",
  endpointInterface="dummy.ConsumerDiaryService_V1_1_PortType")
@WLHttpTransport(contextPath="CMO/InformationMgmt/Diary",serviceUri="ConsumerDiaryService_v1_0",portName="ConsumerDiaryServicePort")
public class ConsumerDiaryService_V1_1_PortTypeImpl implements ConsumerDiaryService_V1_1_PortType {
 
  public ConsumerDiaryService_V1_1_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.CreateCustomerEventResponse createCustomerEvent(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.CreateCustomerEvent parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.UpdateCustomerEventListResponse updateCustomerEventList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.UpdateCustomerEventList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.FindCustomerEventSummaryListResponse findCustomerEventSummaryList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.FindCustomerEventSummaryList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.FindCustomerEventDetailListResponse findCustomerEventDetailList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.FindCustomerEventDetailList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.FindCustomerEventPaginatedListResponse findCustomerEventPaginatedList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerdiaryservicerequestresponse_v1.FindCustomerEventPaginatedList parameters) 
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