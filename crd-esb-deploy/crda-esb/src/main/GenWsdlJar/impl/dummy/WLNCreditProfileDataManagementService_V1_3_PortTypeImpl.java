package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * WLNCreditProfileDataManagementService_V1_3_PortTypeImpl class implements web service endpoint interface WLNCreditProfileDataManagementService_V1_3_PortType */

@WebService(
  serviceName="WLNCreditProfileDataManagementService_v1_3",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/WLNCreditProfileDataManagementService_1",
  endpointInterface="dummy.WLNCreditProfileDataManagementService_V1_3_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/WLNCreditProfileDataManagement",serviceUri="WLNCreditProfileDataManagementService_v1_3",portName="WLNCreditProfileDataManagementServicePort")
public class WLNCreditProfileDataManagementService_V1_3_PortTypeImpl implements WLNCreditProfileDataManagementService_V1_3_PortType {
 
  public WLNCreditProfileDataManagementService_V1_3_PortTypeImpl() {
  
  }

  public void updateCreditProfile(com.telus.tmi.xmlschema.xsd.customer.basetypes.creditcommon_v1.CreditProfileData creditProfile,com.telus.tmi.xmlschema.srv.cmo.ordermgmt.wlncreditprofiledatamanagementservicerequestresponse_v1.RemoveWLNCreditIdentificationInfo removeWLNCreditIdentificationInfo,java.lang.String creditValueCd,java.lang.String fraudIndicatorCd,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo auditInfo) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return;     
  }

  public void updateCreditWorthiness(com.telus.tmi.xmlschema.xsd.customer.customer.enterprisecreditassessmenttypes_v2.CreditAssessmentTransaction creditAssessmentTransactionResult,com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo auditInfo) 
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