package com.telus.credit.crda.adapter;


import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.DozerBeanMapper;

import com.telus.credit.crda.domain.CreditAssessmentTransactionDetails;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.crdgw.domain.ConsumerCreditReportResponse;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.creditgatewayservice_1.CreditGatewayServicePortType;
import com.telus.wsdl.cmo.ordermgmt.creditgatewayservice_1.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.creditgatewayservice_1.ServiceException;

public class CreditGatewayAdapter {

    private CreditGatewayServicePortType m_creditGatewayServicePortType;
    public DozerBeanMapper m_mapper;

    /* 
     * Log object
     */
    private static final Log log = LogFactory.getLog(CreditGatewayAdapter.class);

    public CreditGatewayAdapter(CreditGatewayServicePortType aCreditGatewayServicePortType, DozerBeanMapper aMapper) {
        m_creditGatewayServicePortType = aCreditGatewayServicePortType;
        m_mapper = aMapper;
    }

    public ConsumerCreditReportResponse pullConsumerCreditReport(
            CreditAssessmentRequest creditAssessmentRequest,
            AuditInfo auditInfo,
            boolean failOverIndicator,
            CreditAssessmentTransactionDetails aCreditAssessmentTransactionResult) throws EnterpriseCreditAssessmentServiceException {

        com.telus.credit.crdgw.domain.ConsumerCreditReportRequest aConsumerCreditReportRequest = new com.telus.credit.crdgw.domain.ConsumerCreditReportRequest();        
        //dozer mapping creditAssessmentRequest to aConsumerCreditReportRequest
        m_mapper.map(creditAssessmentRequest, aConsumerCreditReportRequest);
        
        //dozer mapping to map  Cred asmt ID
        m_mapper.map(aCreditAssessmentTransactionResult, aConsumerCreditReportRequest);

        //dozer mapping to map  bureau/service provider  
         m_mapper.map(aCreditAssessmentTransactionResult.getCreditDecisioningResult(), aConsumerCreditReportRequest);        
         
        aConsumerCreditReportRequest.setLanguage("EN");
        aConsumerCreditReportRequest.setReferenceIdType("CARID");

        com.telus.credit.ent.crdgw.domain.common.AuditInfo cdgwAuditInfo = new com.telus.credit.ent.crdgw.domain.common.AuditInfo();
        m_mapper.map(auditInfo, cdgwAuditInfo);
        
        //populate failOverIndicator
        aConsumerCreditReportRequest.setFailOverIndicator(failOverIndicator);
        
        LogUtil.infolog("-->call crd gatway svc ");
        com.telus.credit.crdgw.domain.ConsumerCreditReportResponse aConsumerCreditReportResponse = null;
        try {
            long startTime = System.nanoTime();

            aConsumerCreditReportResponse = m_creditGatewayServicePortType.pullConsumerCreditReport(cdgwAuditInfo, aConsumerCreditReportRequest);
            
            long elapsedTime = System.nanoTime() - startTime;
            LogUtil.infolog("Total execution time to call m_creditGatewayServicePortType.pullConsumerCreditReport in millis: " + elapsedTime / 1000000);
            validateCreditReportResponse(aConsumerCreditReportResponse);
            //populate the creation date in the bureau result so that FICO will know it is recent and will not ask to go to bureau again.
             aConsumerCreditReportResponse.getCreditBureauResult().setCreationDate(new Date(Calendar.getInstance().getTimeInMillis()));

            

        } catch (PolicyException e) {
            com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = e.getFaultInfo();
            String errorCode = (faultInfo != null ? faultInfo.getErrorCode() : "");

            StringBuffer errorMessage = new StringBuffer("");
            errorMessage.append(
                    "PolicyException in calling credit gateway: customer id: " +
                            aConsumerCreditReportRequest.getCustomerRequestId() +
                            ", errorCode " +
                            errorCode +
                            "errorMessage: " +
                            e.getMessage() +
                            (faultInfo != null ? faultInfo.getErrorMessage() : "")
            );
             log.error(errorMessage, e);
            //
            // PolicyExceptions are not re-thrown,
            // we populate the credit bureau result and call fico
            //
            if (aConsumerCreditReportResponse == null) {
            	aConsumerCreditReportResponse = new ConsumerCreditReportResponse();
            }
            if (aConsumerCreditReportResponse.getCreditBureauResult() == null) {
                aConsumerCreditReportResponse.setCreditBureauResult(new com.telus.credit.domain.common.CreditBureauResult());                
            }
            //populate the creation date in the bureau result so that FICO will know it is recent and will not ask to go to bureau again.
            aConsumerCreditReportResponse.getCreditBureauResult().setCreationDate(new Date(Calendar.getInstance().getTimeInMillis()));
            aConsumerCreditReportResponse.getCreditBureauResult().setErrorCd(errorCode);
            
            
        } catch (ServiceException e) {
            com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = e.getFaultInfo();
            String errorCode = (faultInfo != null ? faultInfo.getErrorCode() : "");
            StringBuffer errorMessage = new StringBuffer("");
            errorMessage.append(
                    "ServiceException in calling credit gateway: customer id: " +
                            aConsumerCreditReportRequest.getCustomerRequestId() +
                            ", errorCode " +
                            errorCode +
                            "errorMessage: " +
                            e.getMessage() +
                            (faultInfo != null ? faultInfo.getErrorMessage() : "")
            );
            log.error(errorMessage, e);
            throw new EnterpriseCreditAssessmentServiceException(errorMessage.toString(), errorCode, e);
        } catch (Throwable e) {
        	log.error("Runtime Exception calling credit gateway: customer id: " + aConsumerCreditReportRequest.getCustomerRequestId() + e, e);
            throw new EnterpriseCreditAssessmentServiceException(
                    EnterpriseCreditAssessmentExceptionCodes.CREDIT_GATEWAY_RUNTIME_EXCEPTION_STR + " [customer id: " + aConsumerCreditReportRequest.getCustomerRequestId() + "]"
                    , EnterpriseCreditAssessmentExceptionCodes.CREDIT_GATEWAY_RUNTIME_EXCEPTION
                    , e);
        }

        
        return aConsumerCreditReportResponse;
    }

	
    private void validateCreditReportResponse(
            ConsumerCreditReportResponse creditReportResponse) throws EnterpriseCreditAssessmentServiceException {
        if (creditReportResponse == null || creditReportResponse.getCreditBureauResult() == null) {
            throw new EnterpriseCreditAssessmentServiceException(
                    EnterpriseCreditAssessmentExceptionCodes.CREDIT_GATEWAY_RESULT_EXCEPTION,
                    EnterpriseCreditAssessmentExceptionCodes.CREDIT_GATEWAY_RESULT_EXCEPTION_STR);
        }
    }
}
