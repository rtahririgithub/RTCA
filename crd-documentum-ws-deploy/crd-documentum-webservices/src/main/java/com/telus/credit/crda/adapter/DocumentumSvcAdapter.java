package com.telus.credit.crda.adapter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.crda.exception.CrddctmExceptionFactory;
import com.telus.credit.crda.exception.CrddctmPolicyException;
import com.telus.credit.crda.exception.CrddctmServiceException;
import com.telus.credit.crda.exception.ExceptionCodes;
import com.telus.credit.crda.util.CrddctmConsts;
import com.telus.credit.documentum.DocumentDao;
import com.telus.credit.documentum.domain.ConsumerCreditReportDocument;
import com.telus.credit.domain.crddctm.reqresp.CreditBureauDocument;


public class DocumentumSvcAdapter {
    private static final Log log = LogFactory.getLog(DocumentumSvcAdapter.class);

    private static final int LINE_LEN = 80;

    
    public DocumentDao m_documentDao;
    
    public DocumentumSvcAdapter() {

    }


    public CreditBureauDocument retrieveBureuaReportDocument(String creditBureauDocumentPath) throws CrddctmPolicyException, CrddctmServiceException {
        CreditBureauDocument result = new CreditBureauDocument();
        try {
            long startTime = System.nanoTime();

            if(m_documentDao == null){

        		throw new CrddctmServiceException(
    					ExceptionCodes.ERROR_DAO_INIT
    							+ "  Invalid Documentum dao="  + m_documentDao +  
    							"<"+ "creditBureauDocumentPath =" + creditBureauDocumentPath + ">" 
    							,
    					ExceptionCodes.ERROR_DAO_INIT);

            }
            ConsumerCreditReportDocument consumerCreditReportDoc = m_documentDao.retrieveConsumerReportDocument(creditBureauDocumentPath);
            log.info("IGNORE : Total execution time to call documentDao.retrieveConsumerReportDocument in millis: " + (System.nanoTime() - startTime) / 1000000);

            if (consumerCreditReportDoc != null) {
                if (consumerCreditReportDoc.getDocumentType() != null) {
                    result.setDocumentTypeCd(consumerCreditReportDoc.getDocumentType().code());
                }
                result.setDocumentContentBinary(consumerCreditReportDoc.getDocumentContents());
                result.setDocumentPathTxt(creditBureauDocumentPath);
            }
        } catch (Throwable e) {
        	CrddctmExceptionFactory.throwEnterpriseCreditAssessmentException(
                    Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                    "creditBureauDocumentPath", creditBureauDocumentPath, e);
        }

        return result;
    }

    public void setDocumentDao(DocumentDao dao) {
        this.m_documentDao = dao;
    }

    public DocumentDao getDocumentDao() {
        return m_documentDao;
    }
    
    public String saveUnifiedCreditReportDocumentum(String printImageReport, Long customerID, Long carID) throws CrddctmPolicyException, CrddctmServiceException 
    {
        log.debug("Calling saveUnifiedCreditReportDocumentum: customerID=" + customerID + ", carID=" + carID);
        
    	// Format for the Documentum file name: 
    	// CarID_ReportType_UC_customerID.FileExt                                                                            
    	String printImageLocation = carID + 
    			CrddctmConsts.DOC_TEXT_SPACE + 
    			CrddctmConsts.DOC_CONS_CRD_RPT + 
    			CrddctmConsts.DOC_TEXT_SPACE +  
    			customerID + 
    			CrddctmConsts.DOC_PRINTIMAGE_REPORT_EXT;

    	if (printImageReport.length() > LINE_LEN) {
            StringBuffer printImageReportSB = new StringBuffer();
            reFormatStr(printImageReport, printImageReportSB, LINE_LEN);
    	}
    	
    	ConsumerCreditReportDocument printImageDocument = new ConsumerCreditReportDocument( printImageLocation );
        printImageDocument.setContentType( CrddctmConsts.DOC_UC_CONTENT_TYPE );
        printImageDocument.setDocumentContents( printImageReport.getBytes() );
        
        printImageDocument.setCustomerId( customerID );
        printImageDocument.setCarId( carID );
        
        String docPath = "";        	
		try {
			docPath = m_documentDao.saveDocument(printImageDocument);
			log.info("Documentum persisted location: " + docPath);
		} 
		catch (Throwable e) {
			log.error("Fatal Error while persisting documentum for customer id: " + customerID, e);
		}
		return docPath;
    }
    
    private static void reFormatStr(String str, StringBuffer sb, int length) 
    {    	    	
    	if (str != null && !str.equals("")) {
    		String remain = "";
    		if (str.length() > length) {
    			sb.append(str.substring(0, length));
    			sb.append("\r\n");
    			remain = str.substring(length);
    			reFormatStr(remain, sb, length);
    		}
    		else
    			sb.append(str);  		
    	}
    }
}
