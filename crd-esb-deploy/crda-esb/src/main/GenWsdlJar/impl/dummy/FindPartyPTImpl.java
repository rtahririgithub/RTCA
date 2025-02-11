package dummy;

import javax.jws.WebService;
import weblogic.jws.*;

/**
 * FindPartyPTImpl class implements web service endpoint interface FindPartyPT */

@WebService(
  serviceName="FindPartyService",
  targetNamespace="http://www.ibm.com/xmlns/prod/websphere/fabric/2009/12/telecom/operations/inventory/FindParty",
  endpointInterface="dummy.FindPartyPT")
@WLHttpTransport(contextPath="TelusSAMEBusinessServicesV2_0Web/sca",serviceUri="FindPartyWSExport",portName="FindPartySOAPPort")
public class FindPartyPTImpl implements FindPartyPT {
 
  public FindPartyPTImpl() {
  
  }

  public com.ibm.xmlns.prod.websphere.fabric.x2009.x12.telecom.operations.common.schema.partymessage.PartyCollectionMessage findParty(com.ibm.xmlns.prod.websphere.fabric.x2009.x12.telecom.operations.common.schema.partymessage.PartyMessage parameters) 
    throws com.ibm.xmlns.prod.websphere.fabric.x2009.x12.telecom.operations.common.schema.message.ExceptionMessage 
  {
    // TODO replace with your impl here
     return null;     
  }
}  