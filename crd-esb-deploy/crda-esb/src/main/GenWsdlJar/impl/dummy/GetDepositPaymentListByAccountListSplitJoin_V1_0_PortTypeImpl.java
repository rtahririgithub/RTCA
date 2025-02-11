package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * GetDepositPaymentListByAccountListSplitJoin_V1_0_PortTypeImpl class implements web service endpoint interface GetDepositPaymentListByAccountListSplitJoin_V1_0_PortType */

@WebService(
  serviceName="GetDepositPaymentListByAccountListSplitJoin_v1_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/GetDepositPaymentListByAccountListSplitJoin_1",
  endpointInterface="dummy.GetDepositPaymentListByAccountListSplitJoin_V1_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/GetDepositPaymentListByAccountListSplitJoin",serviceUri="GetDepositPaymentListByAccountListSplitJoin_v1_0",portName="GetDepositPaymentListByAccountListSplitJoinPort")
public class GetDepositPaymentListByAccountListSplitJoin_V1_0_PortTypeImpl implements GetDepositPaymentListByAccountListSplitJoin_V1_0_PortType {
 
  public GetDepositPaymentListByAccountListSplitJoin_V1_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.getdepositpaymentlistbyaccountlistsplitjoinrequestresponse_v1.GetDepositPaymentListByAccountListResponse getDepositPaymentListByAccountList(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.getdepositpaymentlistbyaccountlistsplitjoinrequestresponse_v1.GetDepositPaymentListByAccountList parameters) 
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