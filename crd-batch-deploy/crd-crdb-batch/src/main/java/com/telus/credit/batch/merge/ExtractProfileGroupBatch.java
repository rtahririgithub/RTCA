/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 * $Id$
 */

package com.telus.credit.batch.merge;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;
import com.telus.credit.batch.util.CustomerGroup;


/**
 * 
 * <p>
 * <b>Description: </b> This batch loads customerIds and profileIds into memory from creditPDS dump file,
 *  process them to get a list of profile group and output the list to another file for next step.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>this batch will take the whole input file as a LUW in order to process the whole set of data to get a list of profile group.</li>
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
public class ExtractProfileGroupBatch implements Module
{
    private static final Log log = LogFactory.getLog( ExtractProfileGroupBatch.class );
    
    private String m_inputFileSortByProfileId;
    private String m_inputFileSortByCustomerId;
    private String m_outputFile;
    private int m_failurePoint;
    private LineNumberAwareReader m_reader;
    private PositionAwareWriter m_writer;
    private PrintWriter m_pWriter;
    private String m_inputStr;
    private int m_counter;
    
    private HashMap m_profileMap;
	private HashMap m_customerMap;
	private Iterator m_iteratorCustomerId;
	/*global hashset that contains all processed customer id so far*/
	private HashSet m_processedResultSet;
	/*output object array that contains matched customer id and profile count*/
	private List m_groupList;
	private CustomerGroup m_group;
	private String m_customerId;
	private boolean m_hasNext;

    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION";



    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#execute()
     */
    public void execute() throws ModuleException
    {
    	m_customerId=((String)m_iteratorCustomerId.next());
		if ((m_processedResultSet.isEmpty())||(!m_processedResultSet.contains(m_customerId)))
		  {  
			  m_group=new CustomerGroup();
			  m_group.setProfileCount(0);
			  m_groupList.add(m_group);
			  findRecursive(m_group,m_customerId);
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
       m_hasNext=m_iteratorCustomerId.hasNext();
       //output the result
       if (!m_hasNext)
     	{
			for(Iterator it=m_groupList.iterator();it.hasNext();)
			 {   
				CustomerGroup group=((CustomerGroup)it.next());
				if (group.getProfileCount()>1)
				    m_pWriter.println( new StringBuffer().append(group.getProfileIds()).append(",").append(group.getCustomerIds().toString()));
			 } 
     	}
   	   return m_hasNext;
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
            m_profileMap=new HashMap();
            m_customerMap=new HashMap();
            load(m_inputFileSortByProfileId,this.m_profileMap);
            load(m_inputFileSortByCustomerId,this.m_customerMap);
            
            m_iteratorCustomerId=m_customerMap.keySet().iterator();
            m_groupList=new ArrayList();
            m_processedResultSet=new HashSet();
            
            m_writer = new PositionAwareWriter( m_outputFile, append );
            m_pWriter = new PrintWriter( m_writer );
        }
        catch ( FileNotFoundException fnfe ) {
            throw new ModuleException( fnfe );
        }
    }
	/* Initialization
	 * load data into either profile hashMap or customer hashMap
	 * 
	 * */
	public void load(String filePath,HashMap dataMap) throws ModuleException
	{
		String record;
		String keyId=null;
		String valueId=null;
		String previousKeyId=null;
		String tempId=null;
			
		ArrayList valueList=new ArrayList();
		String[] valueArray;
		//HashMap dataMap=new HashMap();

		try
		{
			/*load data into profileMap and customerMap*/
			m_reader = new LineNumberAwareReader( filePath );
		    record=new String();

		    while((record=m_reader.readLine())!=null)
		    { 
		    	keyId=record.substring(0,record.indexOf(",")).trim();
		        valueId=record.substring(record.indexOf(",")+1).trim();

		    
		        if (previousKeyId!=null)
		        {	
			        if (keyId.equals(previousKeyId))
			             valueList.add(valueId);
			        else
			        {    valueArray=(String[])valueList.toArray(new String[0]);
			             dataMap.put(previousKeyId,valueArray);
			             valueList.clear();
			             valueList.add(valueId);
			             }
		          }
		        else
		        {
		            valueList.add(valueId);
		        	}
		    		        
		        previousKeyId=keyId;
			
		     }
		    valueArray=(String[])valueList.toArray(new String[0]);
		    dataMap.put(keyId,valueArray);
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
	
	private void findRecursive(CustomerGroup customerGroup,String customerId)
	{   
	    String[] profileArray;
	    String[] customerArray;
	    String[] matchedCustomerArray;
	    HashSet tempCustomerSet=new HashSet();
	    m_processedResultSet.add(customerId);
	    StringBuffer customerIdBuffer=new StringBuffer();
	    String currentCustomerList=customerGroup.getCustomerIds();
	    if ((currentCustomerList!=null)&&(!currentCustomerList.equals("")))
	       {
	        customerIdBuffer.append(currentCustomerList);
	        customerIdBuffer.append("|");
	        customerIdBuffer.append(customerId);
	        customerGroup.setCustomerIds(customerIdBuffer.toString());
	        }
	    else customerGroup.setCustomerIds(customerId);
	    
	    profileArray=(String[])m_customerMap.get(customerId);
	    for(int i=0;i<profileArray.length;i++)
	    {
	      if (m_profileMap.containsKey(profileArray[i])) 
	       {customerGroup.setProfileCount(customerGroup.getProfileCount()+1);
	        if ((customerGroup.getProfileIds()!=null)&& (!customerGroup.getProfileIds().equals("")))
	            customerGroup.setProfileIds(customerGroup.getProfileIds()+"|"+profileArray[i]);
	        else customerGroup.setProfileIds(profileArray[i]);
	       }
	      customerArray=(String[])m_profileMap.get(profileArray[i]);
	      if (customerArray!=null)
	      {
		      for(int j=0;j<customerArray.length;j++)
		      	  tempCustomerSet.add(customerArray[j]);
              m_profileMap.remove(profileArray[i]);
	       }
	    }
	   
	    //remove any processed customerIds
	    tempCustomerSet.removeAll(m_processedResultSet);
	    matchedCustomerArray=(String[])tempCustomerSet.toArray(new String[0]);
	    for(int k=0;k<matchedCustomerArray.length;k++)
	    {  
	    	findRecursive(customerGroup,matchedCustomerArray[k]);	    	
	    }
	}//end method

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
//	      String str = state.getProperty( INPUT_FILE_LINE_NO );
//	      int inLineNo = (str != null) ? Integer.parseInt( str ) : 0;
//	      str = state.getProperty( OUTPUT_FILE_POSITION );
//          long outPosition = (str != null) ? Long.parseLong( str ) : 0;
//        try {
//             m_reader.reposition( inLineNo );
//        	   m_writer.reposition( outPosition );
//        	
//        	    load(m_inputFileSortByProfileId, 0);
//              load(m_inputFileSortByProfileId, 1);
//              
//              m_iteratorCustomerId=m_customerMap.keySet().iterator();
//              m_groupList=new ArrayList();
//              m_processedResultSet=new HashSet();
//        	  m_writer = new PositionAwareWriter( m_outputFile, false);
//              m_pWriter = new PrintWriter( m_writer );
//        }
//        catch ( IOException ioe ) {
//            throw new ModuleException( ioe );
//        }
    	
    }


    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#getStateForRestart()
     */
    public Properties getStateForRestart() throws ModuleException
    {
        Properties state = new Properties();
//        try {
//            state.setProperty( INPUT_FILE_LINE_NO, m_reader.getLineNumber()
//                    + "" );
//            state.setProperty( OUTPUT_FILE_POSITION, m_writer.getPosition()
//                    + "" );
//        }
//        catch ( IOException ioe ) {
//            throw new ModuleException( ioe );
//        }
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
        summary.put( "PROCESSED_RECORD_COUNT", m_counter + "" );
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
     * @param outputFile
     *            The outputFile to set.
     */
    public void setOutputFile(String outputFile)
    {
        m_outputFile = outputFile;
    }


	/**
	 * @param inputFileSortByCustomerId The inputFileSortByCustomerId to set.
	 */
	public void setInputFileSortByCustomerId(String inputFileSortByCustomerId) {
		m_inputFileSortByCustomerId = inputFileSortByCustomerId;
	}
	
	/**
	 * @param inputFileSortByProfileId The inputFileSortByProfileId to set.
	 */
	public void setInputFileSortByProfileId(String inputFileSortByProfileId) {
		m_inputFileSortByProfileId = inputFileSortByProfileId;
	}
}

