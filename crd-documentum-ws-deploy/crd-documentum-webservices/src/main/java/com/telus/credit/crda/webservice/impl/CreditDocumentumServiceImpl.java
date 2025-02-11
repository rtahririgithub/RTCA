package com.telus.credit.crda.webservice.impl;


import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.credit.crda.adapter.DocumentumSvcAdapter;
import com.telus.credit.crda.exception.CrddctmExceptionFactory;
import com.telus.credit.crda.exception.CrddctmPolicyException;
import com.telus.credit.crda.exception.CrddctmServiceException;
import com.telus.credit.crddctm.ws.CreditDocumentumServicePortType;
import com.telus.credit.crddctm.ws.ServiceException;
import com.telus.credit.domain.crddctm.reqresp.CreditBureauDocument;
import com.telus.credit.domain.crddctm.reqresp.RetrieveBureuaReportDocument;
import com.telus.credit.domain.crddctm.reqresp.RetrieveBureuaReportDocumentResponse;
import com.telus.credit.domain.crddctm.reqresp.SaveBureuaReportDocument;
import com.telus.credit.domain.crddctm.reqresp.SaveBureuaReportDocumentResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse;

public class CreditDocumentumServiceImpl implements CreditDocumentumServicePortType{

	//@Autowired 
	//protected PingUtil m_PingUtil;	

	public static final Log log = LogFactory.getLog(CreditDocumentumServiceImpl.class);

    @Autowired
    public DocumentumSvcAdapter m_DocumentumSvcAdapter;
 
	@Override
	@WebMethod(action = "ping")
	@WebResult(name = "pingResponse", targetNamespace = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/types/ping_v1", partName = "result")
	public PingResponse ping(
			@WebParam(name = "ping", targetNamespace = "http://xmlschema.tmi.telus.com/xsd/Enterprise/BaseTypes/types/ping_v1", partName = "parameters") Ping parameters)
			throws ServiceException {	
		PingResponse resp = new PingResponse();
		String pingResul = new com.telus.credit.crda.util.PingUtil().getpingResultTxt();
		resp.setVersion(pingResul);
		return resp;
	}



	@Override
	@WebMethod(action = "saveBureuaReportDocument")
	@WebResult(name = "saveBureuaReportDocumentResponse", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "result")
	public SaveBureuaReportDocumentResponse saveBureuaReportDocument(
			@WebParam(name = "saveBureuaReportDocument", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "parameters") SaveBureuaReportDocument params)
			throws ServiceException {
		SaveBureuaReportDocumentResponse respo= new SaveBureuaReportDocumentResponse();
		try {
			String docPath = m_DocumentumSvcAdapter.saveUnifiedCreditReportDocumentum(new String(params.getDocumentContentBinary()), params.getCustomerId(), params.getCreditAssessmentId());
			respo.setDocumentPathTxt(docPath);
		} catch (CrddctmPolicyException e) {
			CrddctmExceptionFactory.handlePolicyException(respo,null, e);

		} catch (CrddctmServiceException e) {
			throw CrddctmExceptionFactory.createCrddctmServiceException("saveBureuaReportDocument", e.getErrorCode(), e);
		}
		return respo;
	}



	@Override
	@WebMethod(action = "retrieveBureuaReportDocument")
	@WebResult(name = "retrieveBureuaReportDocumentResponse", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "result")
	public RetrieveBureuaReportDocumentResponse retrieveBureuaReportDocument(
			@WebParam(name = "retrieveBureuaReportDocument", targetNamespace = "http://xmlschema.tmi.telus.com/srv/CMO/OrderMgmt/CreditDocumentumSvcRequestResponse_v1", partName = "parameters") RetrieveBureuaReportDocument parameters)
			throws ServiceException {
		RetrieveBureuaReportDocumentResponse response= new RetrieveBureuaReportDocumentResponse();
		try {
			CreditBureauDocument internalCreditBureauDocument = m_DocumentumSvcAdapter.retrieveBureuaReportDocument(parameters.getDocumentPathTxt());
/*			com.telus.credit.domain.crddctm.reqresp.CreditBureauDocument svcCreditBureauDocument= new com.telus.credit.domain.crddctm.reqresp.CreditBureauDocument();
			svcCreditBureauDocument.setDocumentContentBinary(internalCreditBureauDocument.getDocumentContent());
			svcCreditBureauDocument.setDocumentId(internalCreditBureauDocument.getDocumentID());
			svcCreditBureauDocument.setDocumentPathTxt(internalCreditBureauDocument.getDocumentPath());
			svcCreditBureauDocument.setDocumentTypeCd(internalCreditBureauDocument.getDocumentType());
			response.setCreditBureauDocument(svcCreditBureauDocument);*/
			
			response.setCreditBureauDocument(internalCreditBureauDocument);
		} catch (CrddctmPolicyException e) {
			CrddctmExceptionFactory.handlePolicyException(response,null, e);

		} catch (CrddctmServiceException e) {
			throw CrddctmExceptionFactory.createCrddctmServiceException("retrieveBureuaReportDocument", e.getErrorCode(), e);
		}
		return response;
	}


}  