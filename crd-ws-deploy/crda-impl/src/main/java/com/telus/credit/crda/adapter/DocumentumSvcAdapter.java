/*
 *  Copyright (c) 2012 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 * $Id$
*/
package com.telus.credit.crda.adapter;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.crda.exception.CreditAssessmentExceptionFactory;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentPolicyException;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.crda.util.EnterpriseCreditAssessmentConsts;
import com.telus.credit.crda.util.LogUtil;
import com.telus.credit.documentum.DocumentDao;
import com.telus.credit.documentum.domain.ConsumerCreditReportDocument;
import com.telus.credit.domain.common.CreditBureauDocument;
import com.telus.credit.domain.ent.AuditInfo;



/**
 * <p><b>Description :</b> <code>DocumentumSvcAdapterImpl</code> interfaces with documentum apis.</p>
 * <p><b>Design Observations : </b></p>
 * <ul>
 * <li>None</li>
 * </ul>
 * <p><br><b>Revision History : </b></p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">18-Sep-2012</td>
 * <td width="15%">Gurbirinder Sidhu</td>
 * <td width="55%">New Class</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 *
 * @author Gurbirinder Sidhu
 * @version 1.0
 * @stereotype Adapter Class
 */
public class DocumentumSvcAdapter {
    private static final Log log = LogFactory.getLog(DocumentumSvcAdapter.class);
    private DocumentDao m_documentDao;
    private static final int LINE_LEN = 80;

    public DocumentumSvcAdapter() {

    }


    public CreditBureauDocument getDocumentumDocuemnt(String creditBureauDocumentPath, AuditInfo auditInfo) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {
        CreditBureauDocument result = new CreditBureauDocument();
        try {
            long startTime = System.nanoTime();

            if(m_documentDao == null){

        		throw new EnterpriseCreditAssessmentServiceException(
    					EnterpriseCreditAssessmentExceptionCodes.ERROR_DAO_INIT
    							+ "  Invalid Documentum dao="  + m_documentDao +  
    							"<"+ "creditBureauDocumentPath =" + creditBureauDocumentPath + ">" 
    							,
    					EnterpriseCreditAssessmentExceptionCodes.ERROR_DAO_INIT);

            }
            	
            ConsumerCreditReportDocument consumerCreditReportDoc = m_documentDao.retrieveConsumerReportDocument(creditBureauDocumentPath);
            log.info("IGNORE : Total execution time to call documentDao.retrieveConsumerReportDocument in millis: " + (System.nanoTime() - startTime) / 1000000);

            if (consumerCreditReportDoc != null) {
                if (consumerCreditReportDoc.getDocumentType() != null) {
                    result.setDocumentType(consumerCreditReportDoc.getDocumentType().code());
                }
                result.setDocumentContent(consumerCreditReportDoc.getDocumentContents());
                result.setDocumentPath(creditBureauDocumentPath);
            }
        } catch (Throwable e) {
            CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
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
    
    public String saveUnifiedCreditReportDocumentum(String printImageReport, Long customerID, Long carID) throws EnterpriseCreditAssessmentPolicyException, EnterpriseCreditAssessmentServiceException {            
    	// Format for the Documentum file name: 
    	// CarID_ReportType_UC_customerID.FileExt                                                                            
    	String printImageLocation =  carID + 
    			EnterpriseCreditAssessmentConsts.DOC_TEXT_SPACE + 
    			EnterpriseCreditAssessmentConsts.DOC_CONS_CRD_RPT + 
    			EnterpriseCreditAssessmentConsts.DOC_TEXT_SPACE +  
    			customerID + 
    			EnterpriseCreditAssessmentConsts.DOC_PRINTIMAGE_REPORT_EXT;

    	if (printImageReport.length() > LINE_LEN) {
            StringBuffer printImageReportSB = new StringBuffer();
            reFormatStr(printImageReport, printImageReportSB, LINE_LEN);
    	}
    	ConsumerCreditReportDocument printImageDocument = new ConsumerCreditReportDocument( printImageLocation );
        printImageDocument.setContentType( EnterpriseCreditAssessmentConsts.DOC_UC_CONTENT_TYPE );
        printImageDocument.setDocumentContents( printImageReport.getBytes() );
        
        printImageDocument.setCustomerId( customerID );
        printImageDocument.setCarId( carID );
        String printImageStr = "";
		try {
			printImageStr = m_documentDao.saveDocument(printImageDocument);
			log.info("Documentum persisted location: " + printImageStr);
		} catch (Throwable e) {
			//Defect 75306: dont throw exception just log
			log.error("Fatal Error while persisting documentum for customer id: " + customerID, e);
			/*CreditAssessmentExceptionFactory.throwEnterpriseCreditAssessmentException(
					Thread.currentThread().getStackTrace()[1].getMethodName() + "|"
							+ Thread.currentThread().getStackTrace()[1].getClassName(), "customer id",
							customerID.toString(), e);*/
		}
		return printImageStr;
    }
    
    private static void reFormatStr(String str, StringBuffer sb, int length) {    	    	
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
