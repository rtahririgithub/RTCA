package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * ConsumerBillingAccountManagementServicePortTypeImpl class implements web service endpoint interface ConsumerBillingAccountManagementServicePortType */

@WebService(
  serviceName="ConsumerBillingAccountManagementService_v1_1",
  targetNamespace="http://telus.com/wsdl/CMO/InformationMgmt/ConsumerBillingAccountManagementService_1",
  endpointInterface="dummy.ConsumerBillingAccountManagementServicePortType")
@WLHttpTransport(contextPath="CMO/InformationMgmt/BillingAccountManagement",serviceUri="ConsumerBillingAccountManagementService_v1_1",portName="ConsumerBillingAccountManagementServicePort")
public class ConsumerBillingAccountManagementServicePortTypeImpl implements ConsumerBillingAccountManagementServicePortType {
 
  public ConsumerBillingAccountManagementServicePortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.CreateBillingAccountResponse createBillingAccount(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.CreateBillingAccount parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.GetBillingAccountResponse getBillingAccount(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.GetBillingAccount parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.GetBillingAccountListByCustomerIdResponse getBillingAccountListByCustomerId(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.GetBillingAccountListByCustomerId parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.RegisterBillDeliveryMethodResponse registerBillDeliveryMethod(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.RegisterBillDeliveryMethod parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.DeregisterBillDeliveryMethodResponse deregisterBillDeliveryMethod(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.consumerbillingaccountmgmtsvcrequestresponse_v1.DeregisterBillDeliveryMethod parameters) 
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