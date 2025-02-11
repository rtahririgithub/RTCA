package com.fico.telus.rtca.blaze;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.blazesoft.server.base.NdServerException;
import com.blazesoft.server.base.NdServiceException;
import com.blazesoft.server.base.NdServiceSessionException;
import com.blazesoft.server.deploy.manager.NdDeploymentManagerException;
import com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest;
import com.fico.telus.blaze.creditCommon.CreditAssessmentResult;
import com.fico.telus.blaze.creditCommon.CreditBureauResult;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauRequest;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauResponse;
import com.fico.telus.blaze.depositCalculator.DepositRequest;
import com.fico.telus.blaze.depositCalculator.DepositResult;


public class RuleServicesBean {

    /*
     * Log object
     */
    private static final Log log = LogFactory.getLog(RuleServicesBean.class);
    
    /*
     * RULES SERVER AND DEPLOYMENT MANAGER
     */
    private  RulesServer  m_rulesServer = null;
    private  DManager 	  m_dManager 	= null;
    
    /**
     * CSV Delimiter
     */
    private String m_csvDelimiter = null;

    /**
     * Constructor
     *
     */
    public RuleServicesBean()
    {
	
    }

    public DManager getDManager() {
	return m_dManager;
    }
    
    public void setdManager( DManager dManager ) {
	m_dManager = dManager;
    }
      
    public RulesServer getRulesServer() {
	return m_rulesServer;
    }

    public void setrulesServer( RulesServer rulesServer ) {
	m_rulesServer = rulesServer;
    }
    
    /**
     * Warms up the rule service
     */
    public void initServer() throws RuleServicesException {
	ping();
    }

    /**
     *	Invokes a server through the entry point "ping"
     *	in the service "RTCA_RuleService".
     *
     */
    public String ping() throws RuleServicesException
    {
	// Invoke the service's entry point.
	try {
	    return getRulesServer().ping();
	} catch (NdServiceSessionException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServiceException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServerException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	catch (Exception e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	return "ping";
    }

    /**
     *	Invokes a server through the entry point "exportAssessmentMessageContents"
     *	in the service "RTCA_RuleService".
     *
     *	
     *	@return	String	==> Enter a description of the return value
     */
    public String exportAssessmentMessageContents() throws RuleServicesException
    {
	// Invoke the service's entry point.
	String retVal = null;
	try {
	    retVal = (String)getRulesServer().exportAssessmentMessageContents(getcsvDelimiter());
	} catch (NdServiceSessionException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServiceException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServerException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	catch (Exception e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	log.info("*** ASSESSMENT MESSAGE EXPORT FOLLOWS ***\n" + retVal);
	return retVal;
    }
    /**
     *	Invokes the Server server through the
     *	"exportErrorMessageContents" entry point.
     *
     
     *	@return	String	==> Enter a description of the return value
     */
    public String exportErrorMessageContents() throws RuleServicesException
    {
	// Invoke the service's entry point.
	String retVal = null;
	try {
	    retVal = (String)getRulesServer().exportErrorMessageContents(getcsvDelimiter());
	} catch (NdServiceSessionException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServiceException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServerException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	catch (Exception e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	log.info("*** ERROR MESSAGE EXPORT FOLLOWS ***\n" + retVal);
	return retVal;
    }
    /**
     *	Invokes the Server server through the
     *	"exportFraudMessageContents" entry point.
     *
     
     *	@return	String	==> Enter a description of the return value
     */
    public String exportFraudMessageContents()  throws RuleServicesException
    {
	// Invoke the service's entry point.
	String retVal = null;
	try {
	    retVal = (String)getRulesServer().exportFraudMessageContents(getcsvDelimiter());
	} 
	catch (NdServiceSessionException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServiceException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	} catch (NdServerException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	catch (Exception e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	log.info("*** FRAUD MESSAGE EXPORT FOLLOWS ***\n" + retVal);
	return retVal;
    }
    
    
    public void shutdown() throws RuleServicesException
    {
	try {
	    log.info("FICO Server Shutting Shutdown initiated...");
	    
	    getRulesServer().shutdown();
	    getDManager().shutdown();
	    
	    log.info("FICO Server Shutdown complete...");
	} catch (NdServerException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	catch (NdDeploymentManagerException e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
	catch (Exception e) {
	    RuleServicesExceptionHandler.throwServiceException( e );
	}
    }
	

    /**
     *	Invokes a server through the entry point "getSimulatedCreditBureauResultEntryPoint"
     *	in the service "getSimulatedCreditBureauResult".
     *
     *	@param	arg0		==> Enter a description here
     *	@return	com.fico.telus.blaze.webservice.CreditBureauResult	==> Enter a description of the return value
     */
    public CreditBureauResult getSimulatedCreditBureauResult(long customerId, SimulatorCreditBureauRequest simulatorCreditBureauRequest) throws RuleServicesException
	
    {

    	if ( log.isDebugEnabled() ) {
    		try {
				log.debug("Customer id: "+ customerId + ", Rule Services Bean: (getSimulatedCreditBureauResult) input: " + 
								com.fico.telus.rtca.blaze.FicoRequestResponseMarshallingUtil.convertToXml( simulatorCreditBureauRequest ) );
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	long startTime = System.currentTimeMillis();
    	// Build the argument list
    	Object[] applicationArgs = new Object[1];
    	applicationArgs[0] = simulatorCreditBureauRequest;


    	// Invoke the service and returns its result, if any.
    	CreditBureauResult retVal =null;
    	try {
    		retVal = (CreditBureauResult)  getRulesServer().getSimulatedCreditBureauResultEntryPoint(simulatorCreditBureauRequest);
    	} catch (NdServiceSessionException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	} catch (NdServiceException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	} catch (NdServerException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	}
    	catch (Exception e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	}
		 SimulatorCreditBureauResponse aSimulatorCreditBureauResponse= new SimulatorCreditBureauResponse();
		 aSimulatorCreditBureauResponse.setSimulatedCreditBureauResult(retVal);
    	
    	if ( log.isDebugEnabled() ) {
    		log.debug( "Customer id: "+ customerId + "Rule Services Bean: (getSimulatedCreditBureauResult) : Elapsed: " + (System.currentTimeMillis()- startTime) + "ms   " );

        	try {
            	//log.debug( "Customer id: "+ customerId + "Bureau Result: " + XmlUtils.convertToXml( aSimulatorCreditBureauResponse));
				log.debug( "Customer id: "+ customerId + "Bureau Result: " + com.fico.telus.rtca.blaze.FicoRequestResponseMarshallingUtil.convertToXml(aSimulatorCreditBureauResponse));
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
    	}
    	
    	return retVal;
    }

	

    
    public CreditAssessmentResult performCreditAssessment(long customerId, CreditAssessmentRequest creditAssessmentRequest) throws RuleServicesException
	
    {
    	if ( log.isInfoEnabled() ) {
    		log.info("Customer id: "+ customerId + ", Rule Services Bean: (performCreditAssessment) input: " + XmlUtils.convertToXml( creditAssessmentRequest ) );
    	}
    	Object[] applicationArgs = new Object[1];
    	applicationArgs[0] = creditAssessmentRequest;
    	long startTime = System.currentTimeMillis();

    	CreditAssessmentResult retVal = null;
    	try {
    		retVal = (CreditAssessmentResult) getRulesServer().performCreditAssessmentEntryPoint(creditAssessmentRequest);
    	} 
    	catch (NdServiceSessionException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, XmlUtils.convertToXml( creditAssessmentRequest ), e );
    	} catch (NdServiceException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, XmlUtils.convertToXml( creditAssessmentRequest ), e );
    	} catch (NdServerException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId,XmlUtils.convertToXml( creditAssessmentRequest ), e );
    	}
    	catch (Exception e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, XmlUtils.convertToXml( creditAssessmentRequest ), e );
    	}
    	if ( log.isInfoEnabled() ) {
    		log.info( "Customer id: "+ customerId + "Rule Services Bean: (performCreditAssessment) : Elapsed: " + (System.currentTimeMillis()- startTime) + "ms   " 
    			+ creditAssessmentRequest.getCreditAssessmentTypeCd() + "/" + creditAssessmentRequest.getCreditAssessmentSubTypeCd() 
    			+ ", Rules Executed by FICO: " + retVal.getRulesExecuted() );
        	log.info( "Customer id: "+ customerId + "Credit Assessment Result: " + XmlUtils.convertToXml( retVal ));
    	}
    	
    	return retVal;
    }

    public DepositResult calculateDeposit( long customerId, DepositRequest depositRequest ) 
    		throws RuleServicesException
    {
    	if ( log.isDebugEnabled() ) {
    		log.debug("Customer id: "+ customerId + ", Rule Services Bean: (calculateDeposit) input: " + XmlUtils.convertToXml( depositRequest ) );
    	}
    	Object[] applicationArgs = new Object[1];
    	applicationArgs[0] = depositRequest;
    	long startTime = System.currentTimeMillis();

    	DepositResult retVal = null;
    	try {
    		retVal = (DepositResult) getRulesServer().calculateDepositEntryPoint(depositRequest);
    	} 
    	catch (NdServiceSessionException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	} catch (NdServiceException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	} catch (NdServerException e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId,e );
    	}
    	catch (Exception e) {
    		RuleServicesExceptionHandler.throwServiceException( customerId, e );
    	}
    	if ( log.isDebugEnabled() ) {
    		log.debug( "Customer id: "+ customerId + "Rule Services Bean: (calculateDeposit) : Elapsed: " + (System.currentTimeMillis()- startTime) + "ms   " 
    			+ depositRequest.getDepositRequestType()
    			+ ", Rules Executed by FICO: " + retVal.getRulesExecuted() );
        	log.debug( "Customer id: "+ customerId + "Calculate Deposit Result: " + XmlUtils.convertToXml( retVal ));
    	}
    	
    	return retVal;
    	
    }

    /**
     * CSV Delimiter
     * 
     * @return csv delimiter
     */
    public String getcsvDelimiter()
    {
	return m_csvDelimiter;
    }

    /**
     * Set CSV Delimiter
     *
     * @param csvDelimiter - CSV Delimiter
     */
    public void setcsvDelimiter( String csvDelimiter ) {
	m_csvDelimiter = csvDelimiter;
    }
	

}
