
package com.telus.credit.crda.webservice.service;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

import com.telus.credit.crda.exception.ExceptionCodes;
import com.telus.credit.crddctm.ws.CreditDocumentumServicePortType;
import com.telus.credit.crddctm.ws.ServiceException;
import com.telus.credit.domain.crddctm.reqresp.RetrieveBureuaReportDocument;
import com.telus.credit.domain.crddctm.reqresp.RetrieveBureuaReportDocumentResponse;
import com.telus.credit.domain.crddctm.reqresp.SaveBureuaReportDocument;
import com.telus.credit.domain.crddctm.reqresp.SaveBureuaReportDocumentResponse;
import com.telus.framework.ws.jaxws.SpringBeanLookupHelper;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;
@WebService(
		portName = "CreditDocumentumSvcPort", 
		serviceName = "CreditDocumentumSvc_v1_0",
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/CreditDocumentumSvc_1", 
		
		wsdlLocation = "/wsdls/CreditDocumentumService_v1_0.wsdl", 
		endpointInterface = "com.telus.credit.crddctm.ws.CreditDocumentumServicePortType"
		)
	@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class CreditDocumentumServicePortImpl implements CreditDocumentumServicePortType
{

    @Resource
    private WebServiceContext m_webserviceCtx;
	private CreditDocumentumServicePortType m_ServiceImpl;

    public CreditDocumentumServicePortImpl() {
    }
	private CreditDocumentumServicePortType getService() throws ServiceException {
		if (m_ServiceImpl ==null) {
			try {
				m_ServiceImpl = (CreditDocumentumServicePortType) SpringBeanLookupHelper.getBean(m_webserviceCtx,"CreditDocumentumService");
			} catch (Exception e) {				
			        FaultExceptionDetailsType faultInfo = new FaultExceptionDetailsType();
			        faultInfo.setErrorCode(ExceptionCodes.SVC_LOAD_ERROR);
			        faultInfo.setErrorMessage(e.getMessage());
			        throw new ServiceException(e.getMessage(), faultInfo, e);				
			}
		}	
		return m_ServiceImpl;
	}
	@Override
	@WebMethod(action = "saveBureuaReportDocument")
	@WebResult(name = "saveBureuaReportDocumentResponse", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "result")
	public SaveBureuaReportDocumentResponse saveBureuaReportDocument(
			@WebParam(name = "saveBureuaReportDocument", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "parameters") SaveBureuaReportDocument parameters)
			throws ServiceException {
		return getService().saveBureuaReportDocument(parameters);
	}
	@Override
	@WebMethod(action = "retrieveBureuaReportDocument")
	@WebResult(name = "retrieveBureuaReportDocumentResponse", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "result")
	public RetrieveBureuaReportDocumentResponse retrieveBureuaReportDocument(
			@WebParam(name = "retrieveBureuaReportDocument", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "parameters") RetrieveBureuaReportDocument parameters)
			throws ServiceException {
		return getService().retrieveBureuaReportDocument(parameters);
	}
	@Override
	@WebMethod(action = "ping")
	@WebResult(name = "pingResponse", targetNamespace = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/types/ping_v1", partName = "result")
	public PingResponse ping(
			@WebParam(name = "ping", targetNamespace = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/types/ping_v1", partName = "parameters") Ping parameters)
			throws ServiceException {
		return getService().ping(parameters);
	}

	
}
