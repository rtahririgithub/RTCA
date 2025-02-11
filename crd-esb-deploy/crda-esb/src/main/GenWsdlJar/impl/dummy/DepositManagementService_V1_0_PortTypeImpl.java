package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * DepositManagementService_V1_0_PortTypeImpl class implements web service endpoint interface DepositManagementService_V1_0_PortType */

@WebService(
  serviceName="DepositManagementService_v1_0",
  targetNamespace="http://telus.com/wsdl/CMO/BillingAccountMgmt/DepositManagementService_1",
  endpointInterface="dummy.DepositManagementService_V1_0_PortType")
@WLHttpTransport(contextPath="v1/cmo/billingaccountmgmt/compass/dms",serviceUri="depositmanagementservice-v1-0",portName="DepositManagementServicePort")
public class DepositManagementService_V1_0_PortTypeImpl implements DepositManagementService_V1_0_PortType {
 
  public DepositManagementService_V1_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.depositmanagementservicerequestresponse_v1.CreateDepositOutput createDeposit(com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.depositmanagementservicerequestresponse_v1.CreateDepositInput input) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.depositmanagementservicerequestresponse_v1.CancelDepositOutput cancelDeposit(com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.depositmanagementservicerequestresponse_v1.CancelDepositInput input) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.depositmanagementservicerequestresponse_v1.SearchDepositOutput searchDeposit(com.telus.tmi.xmlschema.srv.cmo.billingaccountmgmt.depositmanagementservicerequestresponse_v1.SearchDepositInput input) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats ping(java.lang.String operationName,java.lang.Boolean deepPing) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }
}  