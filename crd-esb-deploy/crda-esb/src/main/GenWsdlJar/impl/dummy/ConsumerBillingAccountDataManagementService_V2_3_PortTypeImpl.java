package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * ConsumerBillingAccountDataManagementService_V2_3_PortTypeImpl class implements web service endpoint interface ConsumerBillingAccountDataManagementService_V2_3_PortType */

@WebService(
  serviceName="ConsumerBillingAccountDataManagementService_v2_3",
  targetNamespace="http://telus.com/wsdl/CMO/InformationMgmt/ConsumerBillingAccountDataManagementService_2",
  endpointInterface="dummy.ConsumerBillingAccountDataManagementService_V2_3_PortType")
@WLHttpTransport(contextPath="CMO/InformationMgmt/BillingAccountManagement",serviceUri="ConsumerBillingAccountDataManagementService_v2_3",portName="ConsumerBillingAccountDataManagementServicePort")
public class ConsumerBillingAccountDataManagementService_V2_3_PortTypeImpl implements ConsumerBillingAccountDataManagementService_V2_3_PortType {
 
  public ConsumerBillingAccountDataManagementService_V2_3_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount insertBillingAccount(com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount newBillingAccount,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public void updateBillingAccount(com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount modifiedBillingAccount,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return;     
  }

  public void updateBillingAccountStatus(java.lang.String billingAccountNumber,long billingMasterSourceId,java.lang.String status,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return;     
  }

  public void updateBillCycle(java.lang.String billingAccountNumber,long billingMasterSourceId,java.lang.String newBillCycleCode,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return;     
  }

  public com.telus.tmi.xmlschema.xsd.customer.customer.customersubdomain_v3.Customer insertCustomerWithBillingAccount(com.telus.tmi.xmlschema.xsd.customer.customer.customersubdomain_v3.Customer newCustomer,com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.BillingAccount newAccount,com.telus.tmi.xmlschema.xsd.customer.basetypes.creditcommon_v1.CreditProfileData creditProfile,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public void updatePayChannel(com.telus.tmi.xmlschema.xsd.customer.customerbill.customer_billing_sub_domain_v2.PayChannel modifiedPayChannel,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return;     
  }

  public java.lang.String ping() 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }
}  