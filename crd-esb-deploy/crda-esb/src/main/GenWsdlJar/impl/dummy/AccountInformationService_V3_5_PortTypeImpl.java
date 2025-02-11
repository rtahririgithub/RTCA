package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * AccountInformationService_V3_5_PortTypeImpl class implements web service endpoint interface AccountInformationService_V3_5_PortType */

@WebService(
  serviceName="AccountInformationService_v3_5",
  targetNamespace="http://telus.com/wsdl/CMO/InformationMgmt/AccountInformationService_3",
  endpointInterface="dummy.AccountInformationService_V3_5_PortType")
@WLHttpTransport(contextPath="CMO/InformationMgmt/AccountInformation",serviceUri="AccountInformationService_v3_5",portName="AccountInformationServicePort")
public class AccountInformationService_V3_5_PortTypeImpl implements AccountInformationService_V3_5_PortType {
 
  public AccountInformationService_V3_5_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountByAccountNumberResponse getAccountByAccountNumber(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountByAccountNumber parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountByPhoneNumberResponse getAccountByPhoneNumber(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountByPhoneNumber parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountListByAccountNumbersResponse getAccountListByAccountNumbers(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountListByAccountNumbers parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountListByIMSIResponse getAccountListByIMSI(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetAccountListByIMSI parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateEmailAddressResponse updateEmailAddress(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateEmailAddress parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetEmailAddressResponse getEmailAddress(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetEmailAddress parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateInvoicePropertyListResponse updateInvoicePropertyList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateInvoicePropertyList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetInvoicePropertyListResponse getInvoicePropertyList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetInvoicePropertyList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetBillingInformationResponse getBillingInformation(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetBillingInformation parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateContactInformationResponse updateContactInformation(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateContactInformation parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetContactInformationResponse getContactInformation(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetContactInformation parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetBillCyclePropertyList getBillCyclePropertyList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetBillCyclePropertyList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetLastMemoResponse getLastMemo(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetLastMemo parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CreateMemoResponse createMemo(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CreateMemo parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CheckInternationalServiceEligibilityResponse checkInternationalServiceEligibility(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CheckInternationalServiceEligibility parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetPersonalCreditInformationResponse getPersonalCreditInformation(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetPersonalCreditInformation parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetBusinessCreditInformationResponse getBusinessCreditInformation(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetBusinessCreditInformation parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdatePaymentMethodResponse updatePaymentMethod(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdatePaymentMethod parameters,com.telus.schemas.avalon.common.v1_0.OriginatingUserType updatePaymentMethodInSoapHdr) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.RegisterTopUpCreditCardResponse registerTopUpCreditCard(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.RegisterTopUpCreditCard parameters,com.telus.schemas.avalon.common.v1_0.OriginatingUserType registerTopUpCreditCardInSoapHdr) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.RemoveTopUpCreditCardResponse removeTopUpCreditCard(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.RemoveTopUpCreditCard parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CreateFollowUpResponse createFollowUp(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CreateFollowUp parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetSubscriberEligibilitySupportingInfoResponse getSubscriberEligibilitySupportingInfo(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetSubscriberEligibilitySupportingInfo parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetCustomerNotificationPreferenceListResponse getCustomerNotificationPreferenceList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetCustomerNotificationPreferenceList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateCustomerNotificationPreferenceListResponse updateCustomerNotificationPreferenceList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateCustomerNotificationPreferenceList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetNextSeatGroupIdResponse getNextSeatGroupId(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetNextSeatGroupId parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetMaxVoipLineListResponse getMaxVoipLineList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.GetMaxVoipLineList parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CreateMaxVoipLineResponse createMaxVoipLine(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.CreateMaxVoipLine parameters) 
    throws com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException, com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException 
  {
    // TODO replace with your impl here
     return null;     
  }

  public com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateMaxVoipLineListResponse updateMaxVoipLineList(com.telus.tmi.xmlschema.srv.cmo.informationmgmt.accountinformationservicerequestresponse_v3.UpdateMaxVoipLineList parameters) 
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