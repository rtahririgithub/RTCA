/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 */
package com.telus.credit.batch.merge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.crypto.EncryptionUtil;

//import com.telus.customermgt.service.BillingAccountMgtSvc;
//import com.telus.customermgt.domain.BillingAccount;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.batch.dao.CreditProfileDao;

/**
 * 
 * <p>
 * <b>Description: </b>  This function generates discrepancy report . 
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Lei Fan(x089748)
 *  
 */
public class GenerateDiscrepancyReportBatch implements Module
{
	private static final Log log = LogFactory.getLog( GenerateDiscrepancyReportBatch.class );
    
    
	private String m_inputFile;
    private String m_outputFile;
    private int m_failurePoint;
    private LineNumberAwareReader m_reader;
    private PositionAwareWriter m_writer;
    private PrintWriter m_pWriter;
    private String m_inputStr;
    private int m_counter;
    
    private CreditProfileDao m_creditProfileDao;
    private CreditProfile m_creditProfile;
   // private BillingAccountMgtSvc m_billingAccountMgtSvc;
   // private BillingAccount[] m_billingAccount;
    private long m_profileId;
    private SimpleDateFormat dateFormat;
    
    private HashMap m_customerBillingAccountMap;
    private String m_customerBillingAccountMappingFile;
    private LineNumberAwareReader m_mappingFileReader;
    
    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION";
    
    private StringBuffer parseCustomers(String cutomerIdLine)throws ModuleException
    {
       	//int customerId;
    	String[] customerIdItem=cutomerIdLine.split("\\|");
    	StringBuffer customerBuffer;
    	
    	//customerId=Integer.parseInt(customerIdItem[0]);
  	    customerBuffer=new StringBuffer().append(customerIdItem[0]).append(",")
			    .append(getBillingAccountNumber(customerIdItem[0])).append(",");
	    for (int i=1; i< customerIdItem.length; i++)
	       {
	         //customerId=Integer.parseInt(customerIdItem[i]);
	         customerBuffer=customerBuffer.append(customerIdItem[i]).append("/")
			         .append(getBillingAccountNumber(customerIdItem[i])).append(";");
	         }
	    /*      
    	try
		{
    	  customerBuffer=new StringBuffer().append(customerIdItem[0]).append(",")
		    .append(m_customerMgtSvc.getCustomer(Integer.parseInt(customerIdItem[0])).getBillingAccounts()[0].getBillingAccountNumber());
          for (int i=1; i< customerIdItem.length; i++)
           {
        	customerId=Integer.parseInt(customerIdItem[i]);
        	m_customer=m_customerMgtSvc.getCustomer(customerId);
        	m_billingAccount=m_customer.getBillingAccounts();
            customerBuffer=customerBuffer.append(customerId).append("/")
		       .append(m_billingAccount[0].getBillingAccountNumber()).append(";");
        	} 
   		  }
    	catch (ObjectNotFoundException obnfe)
		  {
            throw new ModuleException( obnfe );
            }*/
        return customerBuffer;
     }
    
    private String getBillingAccountNumber(String customerId)
    {  
    	String strBillingAccountNumber=(String)m_customerBillingAccountMap.get(customerId);
    	return (strBillingAccountNumber==null)? "" : strBillingAccountNumber;
    	//m_billingAccount=m_billingAccountMgtSvc.getBillingAccountsByCustomerId(customerId);
    	//return (m_billingAccount.length==0) ? "" : String.valueOf(m_billingAccount[0].getBillingAccountNumber());
    	}
    
    
    private StringBuffer parseIDcards(CreditIDCard[]  creditIDCard)
    { 
      String sinCode="";
      String dlCode="";
      String hcCode="";
      String pspCode="";
      String prvCode="";
      
      String dlPrvCd="";
      String hcPrvCd="";
      String prvPrvCd="";
      String pspCountryCd="";
      
      for (int i=0; i< creditIDCard.length; i++)
      {
      	if (creditIDCard[i].getIdTypeCode().trim().equals("SIN"))
      		sinCode=EncryptionUtil.decrypt(creditIDCard[i].getIdNumber());
      	if (creditIDCard[i].getIdTypeCode().trim().equals("DL"))
      	{	
      		dlCode=EncryptionUtil.decrypt(creditIDCard[i].getIdNumber());
      		dlPrvCd=creditIDCard[i].getProvinceCode();
      	}
      	if (creditIDCard[i].getIdTypeCode().trim().equals("HC"))
      	{	
      		hcCode=EncryptionUtil.decrypt(creditIDCard[i].getIdNumber());
      		hcPrvCd=creditIDCard[i].getProvinceCode();
      	}
      	if (creditIDCard[i].getIdTypeCode().trim().equals("PSP"))
      	{
      		pspCode=EncryptionUtil.decrypt(creditIDCard[i].getIdNumber());
      		pspCountryCd=creditIDCard[i].getCountryCode();
      	}
      	if (creditIDCard[i].getIdTypeCode().trim().equals("PRV"))
      	{
      		prvCode=EncryptionUtil.decrypt(creditIDCard[i].getIdNumber());
      		prvPrvCd=creditIDCard[i].getProvinceCode();
      	}
       }
      return new StringBuffer().append(sinCode).append(",")
	         .append(dlCode).append(",").append(dlPrvCd).append(",")
			 .append(pspCode).append(",").append(pspCountryCd).append(",")
			 .append(prvCode).append(",").append(prvPrvCd).append(",")
             .append(hcCode).append(",").append(hcPrvCd);
     }
    
    protected StringBuffer parseBirthdate(Date birthDt)
    {  
    	return new StringBuffer().append((birthDt != null) ? dateFormat.format(birthDt) : "");
     }

    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#execute()
     */
    public void execute() throws ModuleException
    {   
    	String[] inputItems;
    	inputItems=m_inputStr.split("\\,");
    	String lineNum=inputItems[0];
    	String profileIds=inputItems[1];
    	String customerIds=inputItems[2];
    	    	 
		inputItems=profileIds.split("\\|");
		for (int i=0; i<inputItems.length; i++)
		{	
			m_profileId=Long.parseLong(inputItems[i]);
			try
			 { 
	           m_creditProfile=m_creditProfileDao.getCreditProfile(m_profileId);
	           if(m_creditProfile.getCreditValue() !=null)
               m_pWriter.println(new StringBuffer().append(lineNum).append(",")
	            		         .append(m_profileId).append(",")
								 .append(dateFormat.format(m_creditProfile.getBusinessLastUpdateTimestamp())).append(",")
								 .append(parseIDcards(m_creditProfile.getCreditIDCards())).append(",")
								 .append(m_creditProfile.getCreditValue().getCreditValueCode()).append(",")
								 .append(parseBirthdate(m_creditProfile.getBirthdate())).append(",")
								 .append(parseCustomers(customerIds))
								 .toString());
	           }
			catch (ObjectNotFoundException obnfe)
			 {  
	            throw new ModuleException( obnfe );
	            }
		}    
          
        m_counter++;
        //simulate failure...
        if ( m_failurePoint != 0 && m_counter >= m_failurePoint ) {
            throw new ModuleException(
                    "Simulated Exception. Failure point reached" );
        }
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#hasNext()
     */
    public boolean hasNext() throws ModuleException
    {
        try {
            m_inputStr = m_reader.readLine();
        }
        catch ( IOException ioe ) {
            throw new ModuleException( ioe );
        }
        //no more records to process...
        if ( m_inputStr == null ) {
            //execute method will not be called
            return false;
        }

        //call execute method
        return true;

    }

    
    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#launch(com.telus.framework.batch.BatchContext) 
     */

    public void launch(BatchContext batchContext) throws ModuleException
    {
        try {
            log.debug( "Job instance ID is " + batchContext.getJobId() );

            //open writer in append mode on 'RESTART'
            boolean append = MODE_RESTART.equals( batchContext.getStartMode() );
            m_reader = new LineNumberAwareReader( m_inputFile );
            m_writer = new PositionAwareWriter( m_outputFile, append );
            m_pWriter = new PrintWriter( m_writer );
            dateFormat= new SimpleDateFormat("yyyy-MM-dd");
            
            if (!append)
              m_pWriter.println("GROUP_ID,CREDIT_PROFILE_ID,LAST_UPDATE_TS,SIN,DL,PROV_CD_FOR_DL,PASSPORT,COUNTRY_CD_FOR_PASSPORT,PROVINCIAL_ID,PROV_CD_FOR_PRV_ID,HEALTH_CARE,PROV_CD_FOR_HC,CREDIT_VALUE,BIRTH_DATE,CUSTOMER_ID,BAN,CUSTOMER_ID_BAN_2");
            
            m_customerBillingAccountMap=new HashMap();
            loadCustomerBillingAccountMapping(m_customerBillingAccountMappingFile);
        }
        catch ( FileNotFoundException fnfe ) {
            throw new ModuleException( fnfe );
        }
    }

    /*
     * load customer id and billing account number 
     * from file generated by previous step for  
     * performance tuning.
     */
    private void loadCustomerBillingAccountMapping(String mappingFile) throws ModuleException
	{
    	String inputRecord;
		String cutomerId=null;
		String billingAccountNumber=null;
		
		try
		{
			m_mappingFileReader = new LineNumberAwareReader( mappingFile );
			inputRecord=new String();
  
		    while((inputRecord=m_mappingFileReader.readLine())!=null)
		    { 
		    	cutomerId=inputRecord.substring(0,inputRecord.indexOf("|")).trim();
		    	billingAccountNumber=inputRecord.substring(inputRecord.indexOf("|")+1).trim();   
		        m_customerBillingAccountMap.put(cutomerId,billingAccountNumber);
		    }
	   } 
	  catch (FileNotFoundException fnfe) 
	  {
		throw new ModuleException(fnfe);
	    }
	  catch (IOException ioe) 
	  {
		throw new ModuleException(ioe);
	    }
	 }

    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#onExit(boolean)
     */
    public int onExit(boolean success) throws ModuleException
    {
        try {
            //close writer & reader.
        	m_writer.close();
            m_reader.close();
            m_mappingFileReader.close();
        }
        catch ( IOException e ) {
            //ignore...
        }
        if ( success ) {
            return RETURN_CODE_SUCCESS;
        }

        return RETURN_CODE_FAILURE;

    }


    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#restoreState(java.util.Properties)
     */
    public void restoreState(Properties state) throws ModuleException
    {
        String str = state.getProperty( INPUT_FILE_LINE_NO );
        int inLineNo = (str != null) ? Integer.parseInt( str ) : 0;
        str = state.getProperty( OUTPUT_FILE_POSITION );
        long outPosition = (str != null) ? Long.parseLong( str ) : 0;
        try {
            m_reader.reposition( inLineNo );
            m_writer.reposition( outPosition );
        }
        catch ( IOException ioe ) {
            throw new ModuleException( ioe );
        }
    	
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#getStateForRestart()
     */
    public Properties getStateForRestart() throws ModuleException
    {
    	  Properties state = new Properties();
          try {
              state.setProperty( INPUT_FILE_LINE_NO, m_reader.getLineNumber()
                      + "" );
              state.setProperty( OUTPUT_FILE_POSITION, m_writer.getPosition()
                      + "" );
          }
          catch ( IOException ioe ) {
              throw new ModuleException( ioe );
          }
          return state;
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#getSummary()
     */
    public Properties getSummary() throws ModuleException
    {
        Properties summary = new Properties();
        summary.put( "INPUT_RECORD_COUNT", m_counter + "" );
        summary.put( "OUTPUT_RECORD_COUNT", m_counter + "" );
        return summary;
    }


    /**
     * @param failurePoint
     *            The failurePoint to set.
     */
    public void setFailurePoint(int failurePoint)
    {
        m_failurePoint = failurePoint;
    }


    /**
     * @param inputFile
     *            The inputFile to set.
     */
    public void setInputFile(String inputFile)
    {
        m_inputFile = inputFile;
    }
    
    /**
     * @param outputFile
     *            The outputFile to set.
     */
    public void setOutputFile(String outputFile)
    {
        m_outputFile = outputFile;
    }
	
	public void setCustomerBillingAccountMappingFile(
			String customerBillingAccountMappingFile) {
		m_customerBillingAccountMappingFile = customerBillingAccountMappingFile;
	}
	
	/**
	 * @param creditProfileDao The creditProfileDao to set.
	 */
	public void setCreditProfileDao(CreditProfileDao creditProfileDao) {
		m_creditProfileDao = creditProfileDao;
	}
	
	/**
	 * @param billingAccountMgtSvc The billingAccountMgtSvc to set.
	 */
	/*
	public void setBillingAccountMgtSvc(
			BillingAccountMgtSvc billingAccountMgtSvc) {
		m_billingAccountMgtSvc = billingAccountMgtSvc;
	}
	*/
}
