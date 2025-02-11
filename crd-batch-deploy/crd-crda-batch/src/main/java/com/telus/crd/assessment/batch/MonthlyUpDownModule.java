package com.telus.crd.assessment.batch;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;

import com.telus.crd.assessment.batch.domain.BillingAccountAgencyRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountCollectionRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountDepositRecord;
import com.telus.crd.assessment.batch.domain.BillingAccountRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditBureauDtlRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditBureauRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditProfileFraudRecord;
import com.telus.crd.assessment.batch.domain.CustomerCreditProfileRecord;
import com.telus.crd.assessment.batch.domain.CustomerRecord;
import com.telus.crd.assessment.util.DateUtil;
import com.telus.crd.assessment.util.XmlUtils;
import com.telus.credit.domain.crda.ExistingCustomerCreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse.CreditAssessmentTransactionResult;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.dto.AdditionalCollectionData;
import com.telus.credit.util.ThreadLocalUtil;
import com.telus.credit.wlnprfldmgt.webservice.util.UpdateCreditWorthinessImpl;
import com.telus.formletters.framework.batch.AbstractModule;
import com.telus.formletters.framework.batch.io.AbstractFileRecordReader;
import com.telus.formletters.framework.batch.io.LineRecordReader;
import com.telus.formletters.framework.batch.io.LineRecordWriter;
import com.telus.formletters.framework.batch.io.RecordCollector;
import com.telus.formletters.framework.batch.io.RecordCollectorReader;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.EnterpriseCreditAssessmentServicePortType;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.PolicyException;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_2.ServiceException;
//import com.telus.credit.domain.common.CreditAssessmentResult;
//import com.telus.credit.wlnprfldmgt.webservice.impl.WLNCreditProfileDataManagementServiceImpl;
//import com.telus.credit.wlnprfldmgt.webservice.impl.CreditProfileUpdates;
//import com.telus.tmi.xmlschema.xsd.customer.customer.enterprisecreditassessmenttypes_v2.CreditAssessmentTransaction;


public class MonthlyUpDownModule extends AbstractModule
{
	private static final Log s_log = LogFactory
			.getLog(MonthlyUpDownModule.class);
    private static final String RESTART_KEY_RECORD_COUNT = "RECORD_COUNT";
    private static final String RESTART_KEY_RECORD_TOTAL = "RECORD_TOTAL";
    private static final String RESTART_KEY_RESTART_COUNT = "RESTART_COUNT";
    private static final String RESTART_KEY_START_TIME = "START_TIME";
    private static final String RESTART_KEY_RUN_TIME = "RUN_TIME";
    private static final String RESTART_KEY_START_CUSTOMER_ID = "START_CUSTOMER_ID";
    private static final String RESTART_KEY_SVC_ERR_CNT = "SVC_ERR_CNT";

    private static final String SUMMARY_KEY_PROCESSED_COUNT = "Number of processed records";
    private static final String SUMMARY_KEY_ERROR_COUNT = "Number of error records";
    private static final String SUMMARY_KEY_RECORD_TOTAL = "Total number of records";
    private static final String SUMMARY_KEY_RESTART_COUNT = "Number of restarts";
    private static final String SUMMARY_KEY_START_TIME = "Start Time";
    private static final String SUMMARY_KEY_END_TIME = "End Time";
    private static final String SUMMARY_KEY_RUN_TIME = "Run Time";
    private static final String NO_CHANGE_RESULT_REASON_CODE = "NO_CHANGE";
    private final Map<String, AbstractFileRecordReader<?>> m_readers = new HashMap<String, AbstractFileRecordReader<?>>();
    private final MonthlyUpDownRecordCollector m_collector = new MonthlyUpDownRecordCollector();
    private final static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	private static final Object SUCCESS_RESULT_CD = "SUCCESS";
    // spring injected
    private File m_controlFile;
    private String m_orderDate;
    private String m_outputFile;
    private int m_errorThreshold;
    private boolean m_cvudTrialRun=false;
    private int m_svcErrorCount;
    
    @Autowired
    @Qualifier("driverReader")
    private LineRecordReader m_driverReader;

    @Autowired
    @Qualifier("customerReader")
    private RecordCollectorReader<CustomerRecord> m_customerReader;

    @Autowired
    @Qualifier("customerCreditBureauReader")
    private RecordCollectorReader<CustomerCreditBureauRecord> m_customerCreditBureauReader;

    @Autowired
    @Qualifier("customerCreditBureauDtlReader")
    private RecordCollectorReader<CustomerCreditBureauDtlRecord> m_customerCreditBureauDtlReader;

    @Autowired
    @Qualifier("customerCreditProfileReader")
    private RecordCollectorReader<CustomerCreditProfileRecord> m_customerCreditProfileReader;

    @Autowired
    @Qualifier("customerCreditProfileFraudReader")
    private RecordCollectorReader<CustomerCreditProfileFraudRecord> m_customerCreditProfileFraudReader;

    @Autowired
    @Qualifier("billingAccountReader")
    private RecordCollectorReader<BillingAccountRecord> m_billingAccountReader;

    @Autowired
    @Qualifier("billingAccountAgencyReader")
    private RecordCollectorReader<BillingAccountAgencyRecord> m_billingAccountAgencyReader;

    @Autowired
    @Qualifier("billingAccountCollectionReader")
    private RecordCollectorReader<BillingAccountCollectionRecord> m_billingAccountCollectionReader;

    @Autowired
    @Qualifier("billingAccountDepositReader")
    private RecordCollectorReader<BillingAccountDepositRecord> m_billingAccountDepositReader;

    @Autowired
    @Qualifier("errorWriter")
    private LineRecordWriter m_errorWriter;

    @Autowired
    @Qualifier("errorReportWriter")
    private LineRecordWriter m_errorReportWriter;

    @Autowired
    @Qualifier("creditValueWriter")
    private LineRecordWriter m_creditValueWriter;
    
    @Autowired
    @Qualifier("leftoverWriter")
    private LineRecordWriter m_leftoverWriter;
    
    @Autowired
    private EnterpriseCreditAssessmentServicePortType m_svc;
    
    @Autowired
    private UpdateCreditWorthinessImpl m_data_svc;
    
    @Autowired
    private MonthlyUpDownModuleHelper m_helper;

    private int m_recordCount;
    private int m_recordTotal;

    private int m_errorCount;
    private int m_processedCount;
    private int m_restartCount;
    private Long m_startCustomerId;
    private Long m_lastCustomerId;
    private long m_startTime;
    private long m_totalStartTime;
    private long m_totalRunTime;
    private long m_totalEndTime;
    private final String BLANK = "";
    private BatchContext m_batchContext;


    @PostConstruct
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void init()
    {
        m_readers.put(m_driverReader.getName(), m_driverReader);
        m_readers.put(m_customerReader.getName(), m_customerReader);
        m_readers.put(m_customerCreditBureauReader.getName(), m_customerCreditBureauReader);
        m_readers.put(m_customerCreditBureauDtlReader.getName(), m_customerCreditBureauDtlReader);
        m_readers.put(m_customerCreditProfileReader.getName(), m_customerCreditProfileReader);
        m_readers.put(m_customerCreditProfileFraudReader.getName(), m_customerCreditProfileFraudReader);
        
        m_readers.put(m_billingAccountReader.getName(), m_billingAccountReader);
        m_readers.put(m_billingAccountAgencyReader.getName(), m_billingAccountAgencyReader);
        m_readers.put(m_billingAccountCollectionReader.getName(), m_billingAccountCollectionReader);        
        m_readers.put(m_billingAccountDepositReader.getName(), m_billingAccountDepositReader);

        m_customerReader.setRecordCollector((RecordCollector )m_collector);
        m_customerCreditBureauReader.setRecordCollector((RecordCollector )m_collector);
        m_customerCreditBureauDtlReader.setRecordCollector((RecordCollector )m_collector);
        m_customerCreditProfileReader.setRecordCollector((RecordCollector )m_collector);
        m_customerCreditProfileFraudReader.setRecordCollector((RecordCollector )m_collector);
        m_billingAccountReader.setRecordCollector((RecordCollector )m_collector);
        m_billingAccountAgencyReader.setRecordCollector((RecordCollector )m_collector);
        m_billingAccountCollectionReader.setRecordCollector((RecordCollector )m_collector);        
        m_billingAccountDepositReader.setRecordCollector((RecordCollector )m_collector);
        m_svcErrorCount = 0;
    }


    @Override
    public void launch(BatchContext batchContext) throws ModuleException
    {
    	try {    
    		m_batchContext = batchContext;
    	}
    	catch( Exception e ) {    		
           e.printStackTrace();
        }
    }


    @Override
    protected void init(Properties restoreState) throws ModuleException
    {
        m_startTime = System.currentTimeMillis();

        m_creditValueWriter.init(restoreState);
        m_leftoverWriter.init(restoreState);
        m_errorWriter.init(restoreState);
        m_errorReportWriter.init(restoreState);

        if( restoreState == null )
        {
            // init normal mode
            Properties controlProps = readControlFile();

            m_recordTotal = Integer.parseInt(controlProps.getProperty(MonthlyUpDownConstants.CONTROL_PROP_KEY_COUNT));
            m_totalStartTime = m_startTime;

            // init position in driver file
            String lineNum = controlProps.getProperty(MonthlyUpDownConstants.CONTROL_PROP_KEY_LINE_NUM);

            restoreState = new Properties();
            restoreState.setProperty(m_driverReader.qualifyKey(AbstractFileRecordReader.RESTART_KEY_INPUT_FILE_RECORD_NUM), lineNum);
        }
        else
        {
            // init restart mode
            m_recordCount = Integer.parseInt(restoreState.getProperty(RESTART_KEY_RECORD_COUNT));
            m_recordTotal = Integer.parseInt(restoreState.getProperty(RESTART_KEY_RECORD_TOTAL));
            m_restartCount = Integer.parseInt(restoreState.getProperty(RESTART_KEY_RESTART_COUNT)) + 1;
            m_startCustomerId = Long.parseLong(restoreState.getProperty(RESTART_KEY_START_CUSTOMER_ID));
            m_totalStartTime = Long.parseLong(restoreState.getProperty(RESTART_KEY_START_TIME));
            m_totalRunTime = Long.parseLong(restoreState.getProperty(RESTART_KEY_RUN_TIME));
            m_svcErrorCount = Integer.parseInt(restoreState.getProperty(RESTART_KEY_SVC_ERR_CNT));
        }

        for( AbstractFileRecordReader<?> reader : m_readers.values() )
        {
           reader.init(restoreState);
            
        }
        m_log.info("Service error threshold: " + m_errorThreshold + ", service error count: " + m_svcErrorCount);
    }


    @Override
    protected boolean next() throws ModuleException
    {
        m_recordCount++;
        return m_recordCount <= m_recordTotal;
    }


    @Override
    public void execute() throws ModuleException
    {    	
        Object savePoint = null;
        
        try {
        	savePoint = m_batchContext.createSavepoint();
        }
        catch ( Exception e)
        {
        	// Ignore the savePoint creation error
        	 m_log.error("Error in creating save point" + e.getMessage());
        }
        String cutmid = m_driverReader.readRecord();
        
        try
        {        	
            if(cutmid != null && !BLANK.equalsIgnoreCase(cutmid.trim()) ) 
            {
            	m_lastCustomerId = Long.parseLong(cutmid);
            	if (m_startCustomerId == null)
            	{
            		m_startCustomerId = m_lastCustomerId;
            	}
            	
           // }            	
                m_collector.customerId = m_lastCustomerId;
                m_collector.customers = m_customerReader.readRecord();
                m_collector.customerCreditBureaus = m_customerCreditBureauReader.readRecord();
                m_collector.customerCreditBureausDtl = m_customerCreditBureauDtlReader.readRecord();
                m_collector.customerCreditProfiles = m_customerCreditProfileReader.readRecord();
                m_collector.customerCreditProfilesFraud = m_customerCreditProfileFraudReader.readRecord();
                
                m_collector.billingAccounts = m_billingAccountReader.readRecord();
                m_collector.billingAccountAgencies = m_billingAccountAgencyReader.readRecord();
                m_collector.billingAccountCollections = m_billingAccountCollectionReader.readRecord();
                m_collector.billingAccountDeposits = m_billingAccountDepositReader.readRecord();
                m_collector.init();
           
                ExistingCustomerCreditAssessmentRequest car = (ExistingCustomerCreditAssessmentRequest) m_helper.newCreditAssessmentRequest(m_collector);
                
                ////////////////////////////////////////////////////////////////////
            	// RTCA1.6-CVUD, TODO testing only, remove them after ATs
        		AdditionalCollectionData additionalCollData = (AdditionalCollectionData)ThreadLocalUtil.getAdditionalCollectionData();
        		
        		s_log.debug("RTCA1.6, DelinquencyCycle(input_to_FICO) = " + additionalCollData.getDelinquencyCycle());
        		s_log.debug("RTCA1.6, CollectionSegment(input_to_FICO) = " + additionalCollData.getCollectionSegment());        		
        		s_log.debug("RTCA1.6, ScorecardID(input_to_FICO) = " + additionalCollData.getScorecardID());
        		s_log.debug("RTCA1.6, BA_Score(input_to_FICO) = " + car.getCustomerCollectionData().getCollectionScore());
         		////////////////////////////////////////////////////////////////////
        		
               /* List<String> attlist = car.getCreditProfileData().getCreditWorthiness().getFraudMessageCdList();
                for (String st : attlist ){
                System.out.println("TEST Fraud Cd:" + st );
                } */
                AuditInfo auditInfo = m_helper.newAuditInfo();
                
                if ( s_log.isInfoEnabled() ) {
                	PerformCreditAssessment performCreditAssessment = new PerformCreditAssessment();
                	performCreditAssessment.setCreditAssessmentRequest(car);
                	performCreditAssessment.setAuditInfo(auditInfo);
                	String inputXml = XmlUtils.convertObjectToXml(performCreditAssessment);
                	
                	s_log.info("Input XML for customer id: " + m_lastCustomerId + "\n" + inputXml );
                }
                
                CreditAssessmentTransactionResult result = m_svc.performCreditAssessment(car, auditInfo);
                if ( s_log.isInfoEnabled() ) {
                	PerformCreditAssessmentResponse performCreditAssessmentResponse = new PerformCreditAssessmentResponse();
                	performCreditAssessmentResponse.setCreditAssessmentTransactionResult ( result );
                	String outputXml = XmlUtils.convertObjectToXml(performCreditAssessmentResponse);
                	s_log.info("Output XML for customer id: " + m_lastCustomerId + "\n" + outputXml );
                }
                
           
                Date newDate= new Date();
                
                if ( m_cvudTrialRun ) {
                	if ( result != null 
   	                	 && result.getCreditDecisioningResult() != null
   	                	 && result.getCreditDecisioningResult().getAssessmentResultCd()!=null
   	                	 && result.getCreditDecisioningResult().getAssessmentResultCd().equals(SUCCESS_RESULT_CD) )
   	                {
                		s_log.error( "CVUD Trial Run: custid: " + result.getCustomerID() 
                				+ ",reason cd: " + result.getCreditDecisioningResult().getAssessmentResultReasonCd()
                				+ ", cv: " + result.getCreditDecisioningResult().getCreditValueCd() );
   	                }
                	else {
                		m_log.error("Credit Assesment Result is not successful for customer: " +  m_lastCustomerId );
                	}
                }
                else {
	                // update credit worthiness if reason code is changed, otherwise update timestamp only
	                if ( result != null 
	                	 && result.getCreditDecisioningResult() != null
	                	 && result.getCreditDecisioningResult().getAssessmentResultCd()!=null
	                	 && result.getCreditDecisioningResult().getAssessmentResultCd().equals(SUCCESS_RESULT_CD) )
	                {
	            	    String vu = result.getCreditDecisioningResult() == null? "" : result.getCreditDecisioningResult().getCreditValueCd();
	                
	            	    if ( result.getCreditDecisioningResult().getAssessmentResultReasonCd() != null
	            	    	 && result.getCreditDecisioningResult().getAssessmentResultReasonCd().equals(NO_CHANGE_RESULT_REASON_CODE) ) 
	            	    {
	            	    	m_data_svc.updateCreditWorthiness(m_helper.newCrdTransaction(result), m_helper.newPfdAuditInfo());
	            	        m_creditValueWriter.writeRecord(result.getCustomerID() +"|"+ vu + "|N|" + DateUtil.formatDateToString(newDate, DATE_FORMAT_YYYYMMDD));
	            	    }
	            	    else if ( result.getCreditDecisioningResult().getAssessmentResultReasonCd() == null
	            	    		  || result.getCreditDecisioningResult().getAssessmentResultReasonCd().trim().length() == 0 ) 
	            	    {
	            	    	m_data_svc.updateCreditWorthiness(m_helper.newCrdTransaction(result), m_helper.newPfdAuditInfo());
	                        m_creditValueWriter.writeRecord(result.getCustomerID() +"|"+ vu + "|Y|" + DateUtil.formatDateToString(newDate, DATE_FORMAT_YYYYMMDD) );
	            	    }
	            	    else {
	            	    	m_log.error( "Invalid Result Reason code for customer:  " 
	            	    				+ m_lastCustomerId + ",result reason code: " + result.getCreditDecisioningResult().getAssessmentResultReasonCd());
	            	    	m_errorWriter.writeRecord(m_driverReader.getCurrentRecord());
	                        m_errorReportWriter.writeRecord(m_driverReader.getCurrentRecord());
	                        m_leftoverWriter.writeRecord(cutmid);
	            	    }
	                }
	                else {
	                	m_log.error("Credit Assesment Result is not successful for customer: " +  m_lastCustomerId );
	                	m_errorWriter.writeRecord(m_driverReader.getCurrentRecord());
	                    m_errorReportWriter.writeRecord(m_driverReader.getCurrentRecord());
	                    m_leftoverWriter.writeRecord(cutmid);
	                }
                }
            }
        }
        catch( PolicyException e )
        {
        	if ( savePoint != null )
        		m_batchContext.rollbackToSavepoint(savePoint);
        	
        	m_leftoverWriter.writeRecord(cutmid);
            logError(e);
        }
        catch( ServiceException e )
        {
        	if ( savePoint != null )
        		m_batchContext.rollbackToSavepoint(savePoint);
        	
        	m_leftoverWriter.writeRecord(cutmid);
            logError(e);
            m_svcErrorCount ++;
            
            if ( m_svcErrorCount >= m_errorThreshold)
            	throw new ModuleException(e);
        }
        catch( Exception e )
        {
        	m_leftoverWriter.writeRecord(cutmid);
            logError(e);
            throw new ModuleException(e);
        }
    }

    @Override
    protected void last() throws ModuleException
    {
        m_errorCount = m_errorWriter.getOutputRecordCount();
        m_processedCount = m_recordTotal - m_errorCount;
        m_totalEndTime = System.currentTimeMillis();
        m_totalRunTime += (m_totalEndTime - m_startTime);
    }

    @Override
    public int onExit(boolean success) throws ModuleException
    {
        for( AbstractFileRecordReader<?> reader : m_readers.values() )
        {
            reader.close();
        }

        m_creditValueWriter.close();
        boolean delete = isNormalLaunchFailure(success);
        m_leftoverWriter.close();
        m_errorWriter.close(delete);
        m_errorReportWriter.close(delete);

        if( success )
        {
            // m_controlFile.delete(); // TODO: delete control file?
            sendEmailSummaryReport();
        }

        return super.onExit(success);
    }

    @Override
    public Properties getStateForRestart() throws ModuleException
    {
        Properties props = super.getStateForRestart();

        long runTime = m_totalRunTime + (System.currentTimeMillis() - m_startTime);

        props.setProperty(RESTART_KEY_RECORD_COUNT, String.valueOf(m_recordCount));
        props.setProperty(RESTART_KEY_RECORD_TOTAL, String.valueOf(m_recordTotal));
        props.setProperty(RESTART_KEY_RESTART_COUNT, String.valueOf(m_restartCount));
        props.setProperty(RESTART_KEY_START_TIME, String.valueOf(m_totalStartTime));
        props.setProperty(RESTART_KEY_RUN_TIME, String.valueOf(runTime));
        props.setProperty(RESTART_KEY_START_CUSTOMER_ID, String.valueOf(m_startCustomerId));
        props.setProperty(RESTART_KEY_SVC_ERR_CNT, String.valueOf(m_svcErrorCount));

        for( AbstractFileRecordReader<?> reader : m_readers.values() )
        {
            reader.addStateForRestart(props);
        }
        
        m_creditValueWriter.addStateForRestart(props);
        m_leftoverWriter.addStateForRestart(props);
        m_errorWriter.addStateForRestart(props);
        m_errorReportWriter.addStateForRestart(props);
        
        return props;
    }

    @Override
    public Properties getSummary() throws ModuleException
    {
        Properties props = super.getSummary();

        long seconds = (m_totalRunTime / 1000) % 60;
        long minutes = (m_totalRunTime / (1000 * 60)) % 60;
        String runTime = String.format("%d mins; %d secs", minutes, seconds);

        props.setProperty(SUMMARY_KEY_ERROR_COUNT, String.valueOf(m_errorCount));
        props.setProperty(SUMMARY_KEY_PROCESSED_COUNT, String.valueOf(m_processedCount));
        props.setProperty(SUMMARY_KEY_RECORD_TOTAL, String.valueOf(m_recordTotal));
        props.setProperty(SUMMARY_KEY_RESTART_COUNT, String.valueOf(m_restartCount));
        props.setProperty(SUMMARY_KEY_START_TIME, new Date(m_totalStartTime).toString());
        props.setProperty(SUMMARY_KEY_END_TIME, new Date(m_totalEndTime).toString());
        props.setProperty(SUMMARY_KEY_RUN_TIME, runTime);

        for( AbstractFileRecordReader<?> reader : m_readers.values() )
        {
            reader.addSummary(props);
        }

       // m_creditValueWriter.addSummary(props);
        
        m_errorWriter.addSummary(props);
        return props;
    }

    private Properties readControlFile() throws ModuleException
    {
        Properties props = new Properties();

        FileReader reader = null;

        try
        {
            reader = new FileReader(m_controlFile);
            props.load(reader);
        }
        catch( IOException e )
        {
            throw new ModuleException(e);
        }
        finally
        {
            try
            {
                if( reader != null )
                {
                    reader.close();
                }
            }
            catch( Exception e )
            {
                m_log.error(null, e);
            }
        }

        return props;
    }

    private void sendEmailSummaryReport()
    {
        try
        {
            StandardEvaluationContext context = new StandardEvaluationContext();
            context.setVariable("errorCount", m_errorCount);
            context.setVariable("processedCount", m_processedCount);
            context.setVariable("recordTotal", m_recordTotal);

            context.setVariable("startCustomerId", m_startCustomerId);
            context.setVariable("lastCustomerId", m_lastCustomerId);

            context.setVariable("orderDate", m_orderDate);
            context.setVariable("restartCount", m_restartCount);
            context.setVariable("startTime", m_totalStartTime);
            context.setVariable("endTime", m_totalEndTime);
            context.setVariable("runTime", m_totalRunTime);

            m_helper.sendEmailSummaryReport(context);
        }
        catch( Exception e )
        {
            m_log.error("Error sending summary report.", e);
        }
    }

    private void logError(Exception e) throws ModuleException
    {
        m_log.error(null, e);
        m_errorWriter.writeRecord(m_driverReader.getCurrentRecord());
        m_errorReportWriter.writeRecord(m_driverReader.getCurrentRecord());
    }

    //-------------------------------------------------------------------------
    // Spring Injected
    //-------------------------------------------------------------------------
    public void setControlFile(File controlFile)
    {
        m_controlFile = controlFile;
    }
    
    public void setOutputFile(String outputfile)
    {
        m_outputFile = outputfile;
    }

    public void setOrderDate(String orderDate)
    {
        m_orderDate = orderDate;
    }
    
    public int getErrorThreshold()
    {
    	return this.m_errorThreshold;
    }
    
    public void setErrorThreshold( int errorThreshold)
    {
    	this.m_errorThreshold = errorThreshold;
    }

	/**
	 * @return the m_cvudTrialRun
	 */
	public boolean isCvudTrialRun() {
		return m_cvudTrialRun;
	}

	/**
	 * @param m_cvudTrialRun the m_cvudTrialRun to set
	 */
	public void setCvudTrialRun(boolean m_cvudTrialRun) {
		this.m_cvudTrialRun = m_cvudTrialRun;
	}
}
