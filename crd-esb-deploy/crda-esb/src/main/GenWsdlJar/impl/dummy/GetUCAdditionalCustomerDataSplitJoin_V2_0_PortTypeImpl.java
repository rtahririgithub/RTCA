package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * GetUCAdditionalCustomerDataSplitJoin_V2_0_PortTypeImpl class implements web service endpoint interface GetUCAdditionalCustomerDataSplitJoin_V2_0_PortType */

@WebService(
  serviceName="GetUCAdditionalCustomerDataSplitJoin_v2_0",
  targetNamespace="http://telus.com/wsdl/CMO/OrderMgmt/GetUCAdditionalCustomerDataSplitJoin_2",
  endpointInterface="dummy.GetUCAdditionalCustomerDataSplitJoin_V2_0_PortType")
@WLHttpTransport(contextPath="CMO/OrderMgmt/GetUCAdditionalCustomerDataSplitJoin",serviceUri="GetUCAdditionalCustomerDataSplitJoin_v2_0",portName="GetUCAdditionalCustomerDataSplitJoinPort")
public class GetUCAdditionalCustomerDataSplitJoin_V2_0_PortTypeImpl implements GetUCAdditionalCustomerDataSplitJoin_V2_0_PortType {
 
  public GetUCAdditionalCustomerDataSplitJoin_V2_0_PortTypeImpl() {
  
  }

  public com.telus.tmi.xmlschema.srv.cmo.ordermgmt.enterprisecreditassessmentservicerequestresponse_v2.WlsUnifiedCreditSearchResult getUCAdditionalCustomerData(com.telus.tmi.xmlschema.srv.cmo.ordermgmt.enterprisecreditassessmentservicerequestresponse_v2.WlsUnifiedCreditSearchResult unifiedCreditSearchResult) 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }

  public java.lang.String ping() 
    throws com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType 
  {
    // TODO replace with your impl here
     return null;     
  }
}  