
package com.telus.credit.entprflmgt.webservice.service;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

import com.telus.credit.entprflmgt.domain.UpdateCreditProfile;
import com.telus.credit.entprflmgt.domain.UpdateCreditProfileResponse;
import com.telus.framework.ws.jaxws.SpringBeanLookupHelper;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.EnterpriseCreditProfileManagementServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.ServiceException;


@WebService(portName = "EnterpriseCreditProfileManagementSvcPort", serviceName = "EnterpriseCreditProfileManagementSvc_v2_0", targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/EnterpriseCreditProfileManagementSvc_2", wsdlLocation = "/wsdls/EnterpriseCreditProfileManagementSvc_v2_0.wsdl", endpointInterface = "com.telus.wsdl.cmo.ordermgmt.enterprisecreditprofilemanagementsvc_2.EnterpriseCreditProfileManagementServicePortType")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class EnterpriseCreditProfileManagementServiceStub
    implements EnterpriseCreditProfileManagementServicePortType
    
{

    @Resource
    private WebServiceContext m_webserviceCtx;

    private EnterpriseCreditProfileManagementServicePortType m_entProfileMgtServiceImpl;

    public EnterpriseCreditProfileManagementServiceStub() {
    }



    private EnterpriseCreditProfileManagementServicePortType getEntProfileMgtService() {
        if (m_entProfileMgtServiceImpl ==null) {
            m_entProfileMgtServiceImpl = (EnterpriseCreditProfileManagementServicePortType) SpringBeanLookupHelper.getBean(m_webserviceCtx, "entCrdProfileMgtService");
        }
        return m_entProfileMgtServiceImpl;
    }



	@Override
	public UpdateCreditProfileResponse updateCreditProfile(
			@WebParam(name = "updateCreditProfile", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/EnterpriseCreditProfileManagementSvcRequestResponse_v2", partName = "parameters") UpdateCreditProfile consumerCreditProfileInfo)
			throws ServiceException {
        return getEntProfileMgtService().updateCreditProfile(consumerCreditProfileInfo);
	}



	@Override
	public PingResponse ping(
			@WebParam(name = "ping", targetNamespace = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/types/ping_v1", partName = "parameters") Ping pingRequest)
			throws ServiceException {
        return getEntProfileMgtService().ping(pingRequest);
        
	}

}
