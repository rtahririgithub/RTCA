package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * SearchDepositPaymentByCustomerID_V1_0_PortTypeImpl class implements web service endpoint interface SearchDepositPaymentByCustomerID_V1_0_PortType */

@WebService(
  serviceName="SearchDepositPaymentByCustomerID_v1_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/SearchDepositPaymentByCustomerID_1",
  endpointInterface="dummy.SearchDepositPaymentByCustomerID_V1_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/SearchDepositPaymentByCustomerID",serviceUri="SearchDepositPaymentByCustomerID_v1_0",portName="SearchDepositPaymentByCustomerIDPort")
public class SearchDepositPaymentByCustomerID_V1_0_PortTypeImpl implements SearchDepositPaymentByCustomerID_V1_0_PortType {
 
  public SearchDepositPaymentByCustomerID_V1_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.searchdepositpaymentbycustomeridrequestresponse_v1.SearchDepositResponseList searchDepositPaymentByCustomerID(long customerID) 
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