/*
 * Copyright (c) 2004 TELUS Communications Inc., All Rights Reserved.
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
import java.util.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.telus.framework.security.SecurityContext;
import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.Module;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.batch.io.LineNumberAwareReader;
import com.telus.framework.batch.io.PositionAwareWriter;

import com.telus.credit.batch.dao.CreditProfileExtDao;

import com.telus.credit.batch.dao.CreditIDCardDao;
import com.telus.credit.batch.dao.CreditProfileDao;
import com.telus.credit.batch.dao.CreditProfileCustomerMapDao;
import com.telus.credit.batch.dao.CustomerGuarantorDao;
import com.telus.credit.batch.dao.CreditProfileMapDao;
import com.telus.credit.batch.dao.CreditAddressDao;
import com.telus.credit.batch.dao.CreditStatusDao;
import com.telus.credit.batch.dto.CreditProfileDto;
import com.telus.credit.domain.CreditAddress;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditProfileExt;
import com.telus.credit.domain.CreditValueExt;
import com.telus.credit.domain.ProductCategoryQualification;

import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.batch.util.CreditProfileCustomerMap;
import com.telus.credit.batch.util.CreditProfileMap;
import com.telus.credit.batch.util.DateUtil;


/**
 * @author Lei Fan
 */
public class MergeCreditProfileBatch  implements Module
{
    private static final Log log = LogFactory.getLog( MergeCreditProfileBatch.class );
    
    private String m_inputFile;
    private String m_outputFile;
    private String m_outputWSFile;
    private String m_unMergedCidListOutputFile;
    private int m_failurePoint;
    private LineNumberAwareReader m_reader;
    private PositionAwareWriter m_writer;
    private PositionAwareWriter m_writer2;
    private PositionAwareWriter m_writerWs;
    private PrintWriter m_pWriter;
    private PrintWriter m_pWriter2;
    private PrintWriter m_pWriterWs;
    private String m_inputStr;
    private int m_counter;
    
    private List m_profileList;
    private List m_processedList;
	private List m_deletedList;
	private String m_profileIds;
	private String m_customerIds;
	private String[] m_profileIdArray;
	//private SimpleDateFormat m_dateFormat;
	private String m_userId;
	private String m_dataSrcId;
    
    private CreditProfileExtDao m_creditProfileDao;
    private CreditProfileCustomerMapDao m_profileCustomerMapDao;
    private CreditAddressDao m_creditAddressDao;
    private CreditIDCardDao m_creditIDCardDao;
    private CreditStatusDao m_creditStatusDao;
    private CustomerGuarantorDao m_guarantorDao;
    private CreditProfileMapDao m_creditProfileMapDao;
    //private CreditProfileExt m_creditProfile;
    private CreditIDCard[] m_creditIDCardArray1,m_creditIDCardArray2;
    private CreditProfileExt m_activeCreditProfile,m_mergedCreditProfile;
 
    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION";
    private static final String OUTPUT_FILE_POSITION2 = "OUPUT_FILE_POSITION2";
    private static final String OUTPUT_FILE_POSITIONWS = "OUPUT_FILE_POSITIONWS";
    private static final String USER_ID = "SYS-crdb-merge";

   
    /*
     * (non-Javadoc)
     * 
     * @see com.telus.framework.batch.Module#execute()
     */
    public void execute() throws ModuleException
    {
    	m_profileList=new ArrayList();
        m_processedList=new ArrayList();
       	m_deletedList=new ArrayList();
    	
    	m_profileIds=m_inputStr.substring(0,m_inputStr.indexOf(",")).trim();
    	m_customerIds=m_inputStr.substring(m_inputStr.indexOf(",")+1).trim();
    	
    	m_profileIdArray=m_profileIds.split("\\|");
    	for (int i=0;i<m_profileIdArray.length;i++)
    		 m_profileList.add(m_profileIdArray[i]);
    	mergeList(m_profileList);
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
            
            m_writer2= new PositionAwareWriter(m_unMergedCidListOutputFile,append);
            m_pWriter2 = new PrintWriter( m_writer2 );
            
            m_writerWs= new PositionAwareWriter(m_outputWSFile,append);
            m_pWriterWs = new PrintWriter( m_writerWs );
         
            //set audit fields
            m_userId=(SecurityContext.getPrincipal()==null)? USER_ID: SecurityContext.getPrincipal().getName();
            m_dataSrcId=String.valueOf(SecurityContext.getSystemSourceId());
                        
        	//m_dateFormat= new SimpleDateFormat("yyyy-MM-dd");            
        }
        catch ( FileNotFoundException fnfe ) {
            throw new ModuleException( fnfe );
        }
        catch ( IOException e)
        {
            log.error(" Error in initializing file writer! ", e);
            throw new ModuleException(e);       
        }
        catch ( Exception e)
        {
                    log.error(" Error in initializing file writers! ", e);
                    throw new ModuleException(e);       
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
            m_writer2.close();
            m_writerWs.close();
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
        String str = state.getProperty( INPUT_FILE_LINE_NO );
        int inLineNo = (str != null) ? Integer.parseInt( str ) : 0;
        str = state.getProperty( OUTPUT_FILE_POSITION );
        long outPosition = (str != null) ? Long.parseLong( str ) : 0;
        str = state.getProperty( OUTPUT_FILE_POSITION2 );
        long outPosition2 = (str != null) ? Long.parseLong( str ) : 0;
        str = state.getProperty( OUTPUT_FILE_POSITIONWS );
        long outPositionws = (str != null) ? Long.parseLong( str ) : 0;
        try {
            m_reader.reposition( inLineNo );
            m_writer.reposition( outPosition );
            m_writer2.reposition( outPosition2 );
            m_writerWs.reposition( outPositionws );
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
            state.setProperty( OUTPUT_FILE_POSITION2, m_writer2.getPosition()
                    + "" );
            state.setProperty( OUTPUT_FILE_POSITIONWS, m_writerWs.getPosition()
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
     
    /**
     * @param unMergedCidListOutputFile
     *            The unMergedCidListOutputFile to set.
     */ 
	public void setUnMergedCidListOutputFile(String unMergedCidListOutputFile) {
		m_unMergedCidListOutputFile = unMergedCidListOutputFile;
	}
	
	/**
	 * @param creditProfileDao The creditProfileDao to set.
	 */
	public void setCreditProfileDao(CreditProfileExtDao creditProfileDao) {
		m_creditProfileDao = creditProfileDao;
	}
	/**
	 * @param creditProfileMapDao The creditProfileMapDao to set.
	 */
	public void setCreditProfileMapDao(CreditProfileMapDao creditProfileMapDao) {
		m_creditProfileMapDao = creditProfileMapDao;
	}
	/**
	 * @param guarantorDao The guarantorDao to set.
	 */
	public void setGuarantorDao(CustomerGuarantorDao guarantorDao) {
		m_guarantorDao = guarantorDao;
	}
	/**
	 * @param profileCustomerMapDao The profileCustomerMapDao to set.
	 */
	public void setProfileCustomerMapDao(
			CreditProfileCustomerMapDao profileCustomerMapDao) {
		m_profileCustomerMapDao = profileCustomerMapDao;
	}
	
	/**
	 * @param creditAddressDao The creditAddressDao to set.
	 */
	public void setCreditAddressDao(CreditAddressDao creditAddressDao) {
		m_creditAddressDao = creditAddressDao;
	}
	/**
	 * @param creditIDCardDao The creditIDCardDao to set.
	 */
	public void setCreditIDCardDao(CreditIDCardDao creditIDCardDao) {
		m_creditIDCardDao = creditIDCardDao;
	}
	/**
	 * @param creditStatusDao The creditStatusDao to set.
	 */
	public void setCreditStatusDao(CreditStatusDao creditStatusDao) {
		m_creditStatusDao = creditStatusDao;
	}
	
	private void mergeList(List profileList) throws ModuleException
	{
	   String strFirstElement;
	   String strNextElement;
	   long   firstProfileId;
	   long   nextProfileId;
	   boolean hasMerged=false;
	   
	   //exit recursive call if input list contains 0 or 1 element
	   if (profileList.isEmpty())
	      {
	       printList(m_processedList);
	       return;
	       }
	   else if(profileList.size()==1)
	      {
		 m_processedList.addAll(profileList);
		 printList(m_processedList);
		 return;
		  }
	   	   
	   m_deletedList.clear();	  
	   //get first element from profile list
	   Iterator it = profileList.iterator();
	   strFirstElement=(String)it.next();
	   firstProfileId=Long.parseLong(strFirstElement);
	   while(it.hasNext())
	   {
	     strNextElement=(String)it.next();
	     nextProfileId=Long.parseLong(strNextElement);
	     if(isMergeable(firstProfileId,nextProfileId))
	       { 
		     if (mergeCreditProfiles(firstProfileId,nextProfileId)==firstProfileId) 
		        {
		         m_deletedList.add(strNextElement);
		         }
		     else 
		        {
		         m_deletedList.add(strFirstElement);
		         firstProfileId=nextProfileId;
		         strFirstElement=strNextElement;
		         }
		     hasMerged=true;     
		   }
	     }//end with while
	    profileList.removeAll(m_deletedList);
	    if ((!hasMerged)&&(profileList.contains(strFirstElement)))
	       {   
	         m_processedList.add(String.valueOf(firstProfileId));
		     profileList.remove(String.valueOf(firstProfileId));
		 } 
	    mergeList(profileList);
	}
	
	private void printList(List profileList)
    {   
		if (profileList.size()>1)
		 {	
	    	StringBuffer strBuffer=new StringBuffer();
	    	strBuffer.append(m_counter).append(",");
			for(Iterator profileId=profileList.iterator();profileId.hasNext();)
	    	 {
	    		strBuffer.append(profileId.next()).append("|");
	    	   }
			strBuffer.deleteCharAt(strBuffer.lastIndexOf("|"));
			strBuffer.append(",").append(m_customerIds);
			m_pWriter.println(strBuffer.toString());
			
			//phase2a performance tuning due to defect 37882
			String[] customerId=m_customerIds.split("\\|");
			for (int i=0; i< customerId.length; i++)
			        m_pWriter2.println(customerId[i]);
		 }	
      }
	
	 /*
     * check if two credit profiles are mergeable
     */
    private boolean isMergeable(long profileId1,long profileId2) throws ModuleException
    {
    	CreditProfile creditProfile1=null,creditProfile2=null;
       
    	try
		{
    	   //creditProfile1=m_creditProfileDao.getCreditProfile(profileId1);
    	   //creditProfile2=m_creditProfileDao.getCreditProfile(profileId2);
           // RTCA change 1, using new query to get creditworthiness 
           Long prfid1 = new Long(profileId1);
           Long prfid2 = new Long(profileId2);
           CreditProfileDto[] cfl1 = m_creditProfileDao.getCreditProfileDtoByCreditProfileId(prfid1);
           CreditProfileDto[] cfl2 = m_creditProfileDao.getCreditProfileDtoByCreditProfileId(prfid2);
           if(cfl1 != null && cfl1.length>0) 
             creditProfile1= (CreditProfile)cfl1[0].getCreditProfile();
           if(cfl2 != null && cfl2.length>0) 
            creditProfile2=(CreditProfile)cfl2[0].getCreditProfile();
         
    	   assert isActiveProfile(creditProfile1): "Credit Profile is not active";
	       assert isActiveProfile(creditProfile2): "Credit Profile is not active";
		}
    	catch(Exception e)
		{  
    		throw new ModuleException(e);
    	}
    	return isHardmatchedAndNoConflicts(creditProfile1, creditProfile2);
    
    }
    
    protected boolean isHardmatchedAndNoConflicts(CreditProfile creditProfile1, CreditProfile creditProfile2)
    {
    	return isHardMatched(creditProfile1,creditProfile2)&& hasNoConflictingValues(creditProfile1,creditProfile2);
    }
    
    /*
     * merge two credit profiles if they are both active and mergeable
     * <ul>
	 * 	<li>Choose active profile</li>	
	 * 	<li>Consolidate content</li>		
	 * 	<li>Consolidate links</li>
	 *  <li>Consolidate guarantor information</li>
	 *  <li>Maintain credit profile infomation</li>
	 *  <li>Transfer Ownership</li>
     * </ul>
     */
    private long mergeCreditProfiles(long creditProfileId1,long creditProfileId2)throws ModuleException
    {
    	CreditProfile creditProfile1=null,creditProfile2=null;
    	long mergedProfileId,activeProfileId;
	       
	    try
		 {  //creditProfile1=m_creditProfileDao.getCreditProfile(creditProfileId1);
	    	//creditProfile2=m_creditProfileDao.getCreditProfile(creditProfileId2);
            // rtca change 2, using new query to retrieve creditwothiness
            Long pfid1 = new Long(creditProfileId1);
            Long pfid2 = new Long(creditProfileId2);
            CreditProfileDto[] cfl1 = m_creditProfileDao.getCreditProfileDtoByCreditProfileId(pfid1);
            CreditProfileDto[] cfl2 = m_creditProfileDao.getCreditProfileDtoByCreditProfileId(pfid2);
            if(cfl1 != null && cfl1.length>0) 
               creditProfile1=cfl1[0].getCreditProfile();
            if(cfl2 != null && cfl2.length>0) 
               creditProfile2=cfl2[0].getCreditProfile();
           
	    	assert isActiveProfile(creditProfile1):"Credit Profile is not active";
	    	assert isActiveProfile(creditProfile2):"Credit Profile is not active";
	    	
	    	chooseProfile(creditProfile1,creditProfile2);
	    	mergedProfileId=m_mergedCreditProfile.get_id();
	    	activeProfileId=m_activeCreditProfile.get_id();
	    	consolidateContentRC();
	    	consolidateLinks(mergedProfileId,activeProfileId);
	    	consolidateGuarantor(mergedProfileId,activeProfileId);
	    	maintainCreditProfile();
	    	//transferOwnership(mergedProfileId,activeProfileId);
			}
	    	catch(Exception e)
			{  
	    		throw new ModuleException(e);
	    	}
	    return 	m_activeCreditProfile.get_id();
      }
    
    /*
     * check if a credit profile is active
     */
    private boolean isActiveProfile(CreditProfile creditProfile)
    {  	
    	return creditProfile.getStatus().equals("A");
    }
    
    /*
     * check if two credit profiles are hard-matched
     */
    private boolean isHardMatched(CreditProfile creditProfile1,CreditProfile creditProfile2)
    {
    	m_creditIDCardArray1=creditProfile1.getCreditIDCards();
    	m_creditIDCardArray2=creditProfile2.getCreditIDCards();
   
    	for (int i=0; i< m_creditIDCardArray1.length; i++) 
    	{
    		for(int j=0; j< m_creditIDCardArray2.length;j++)
    		{   
    			if(m_creditIDCardArray2[j].equals(m_creditIDCardArray1[i]))
    				return true;
    			}
    		
        }
    	return false;
    }
    
    /*
     * check no conflictions on any of 7 attributes: SIN,DL,HC,PSP,PRV,CreditValue and Birth date 
     */
    private boolean hasNoConflictingValues(CreditProfile creditProfile1,CreditProfile creditProfile2)
    {
    	//check credit value
        if(creditProfile1.getCreditValue()==null || creditProfile2.getCreditValue() ==null )
        {
            return false;
        }
    	if ( hasConflict(creditProfile1.getCreditValue().getCreditValueCode(), creditProfile2.getCreditValue().getCreditValueCode()))
	        return false;
    	
    	//check birth date
    	if ( hasConflict(creditProfile1.getBirthdate(), creditProfile2.getBirthdate()))
    		return false;
    	
    	//check idCard
    	m_creditIDCardArray1=creditProfile1.getCreditIDCards();
    	m_creditIDCardArray2=creditProfile2.getCreditIDCards();
    	for (int i=0; i< m_creditIDCardArray1.length; i++) 
    	{   
    		for(int j=0; j< m_creditIDCardArray2.length;j++)
    		{   
    			if(m_creditIDCardArray2[j].getIdTypeCode().equals(m_creditIDCardArray1[i].getIdTypeCode()))
    			{	
    			    if(hasConflict(m_creditIDCardArray2[j],m_creditIDCardArray1[i]))
    				    return false;
    			  }
    		  }
        }
    	return true;
    }
    
    /*
     * check confliction of two CreditIDCards
     */
    private boolean hasConflict(CreditIDCard creditIDCard1,CreditIDCard creditIDCard2)
    {
        return (!(hasConflict(creditIDCard1.getIdNumber(), creditIDCard2.getIdNumber()))&&
        		!(hasConflict(creditIDCard1.getProvinceCode(), creditIDCard2.getProvinceCode()))&&
        		!(hasConflict(creditIDCard1.getCountryCode(), creditIDCard2.getCountryCode()))) ? false : true; 
     }
    
    /*
     * check confliction of two strings
     */
    private boolean hasConflict(String str1,String str2)
    {
      return ((str1!= null) && (str2!= null) && (!str1.equals(str2))) ? true : false;
     }
    
    /*
     * check confliction of two java.util.Date
     */
    private boolean hasConflict(Date date1,Date date2)
    {
      return ((date1!= null) && (date2!= null) && (!compareBirthDate(date1,date2))) ? true : false;
     }
    
    /*
     * compare two Birth Date
     */
    private boolean compareBirthDate(Date date1,Date date2)
    {
      return ((date1.getYear()==date2.getYear()) && (date1.getMonth()==date2.getMonth()) && (date1.getDate()==date2.getDate())) ? true : false;
     }
    
    /*
     * select the newer one as active, the other one will be retired
     */
    private void chooseProfile(CreditProfile creditProfile1,CreditProfile creditProfile2)
    {
    	//return (creditProfile1.getBusinessLastUpdateTimestamp().compareTo(creditProfile2.getBusinessLastUpdateTimestamp())>0) ? creditProfile1 : creditProfile2;
    	if(creditProfile1.getBusinessLastUpdateTimestamp().compareTo(creditProfile2.getBusinessLastUpdateTimestamp())>0)
    	 {
    		 m_activeCreditProfile=(CreditProfileExt)creditProfile1;
    		 m_mergedCreditProfile=(CreditProfileExt)creditProfile2;
    	    }
    	else
    	 {  
    		 m_activeCreditProfile= (CreditProfileExt) creditProfile2;
    		 m_mergedCreditProfile= (CreditProfileExt) creditProfile1;
    	 }
    }
    
    /**
     * Consolidate content of two profiles:
      * <ul>
	  * 	<li>if p1 has some id cards but p2 doesn't,copy to p2</li>	
	  * 	<li>if p1 has some characteristics attributes but p2 doesn't, copy to p2 </li>		
	  * 	<li>if p1 has address but p2 doesn't, copy address info to p2</li>
	  *     <li>update p2<li>
	  * </ul>
     */
   /* private void consolidateContent() throws ModuleException
    {   //copy credit address
    	CreditAddress tempCreditAddress;
    	//transaction 1
    	if ((m_activeCreditProfile.getCreditAddress()==null) && (m_mergedCreditProfile.getCreditAddress()!=null))
    	  { tempCreditAddress=m_mergedCreditProfile.getCreditAddress();
    	    tempCreditAddress.setCreditProfileId(m_activeCreditProfile.get_id());
    		m_activeCreditProfile.setCreditAddress(tempCreditAddress);
    	    try
			{ m_creditAddressDao.insertCreditAddress(tempCreditAddress,m_userId,m_dataSrcId);
			    }
    	    catch (DuplicateKeyException dupke)
			{ throw new ModuleException(dupke);
    	    	}
    	    catch (ConcurrencyConflictException cone)
			{  throw new ModuleException(cone); 
    	    	}
    	    }
    	
     	//copy IdCards
    	boolean isFound;
    	CreditIDCard tempIDCard;
    	m_creditIDCardArray1=m_mergedCreditProfile.getCreditIDCards();
    	m_creditIDCardArray2=m_activeCreditProfile.getCreditIDCards();
    	for (int i=0; i< m_creditIDCardArray1.length; i++) 
    	{   isFound=false;
    		for(int j=0; j< m_creditIDCardArray2.length;j++)
    		{   
    			if(m_creditIDCardArray2[j].getIdTypeCode().equals(m_creditIDCardArray1[i].getIdTypeCode()))
    			  isFound=true;
    			}
    		if(!isFound) 
    		{	tempIDCard=m_creditIDCardArray1[i];
    		    tempIDCard.setCreditProfileId(m_activeCreditProfile.get_id());
    			m_activeCreditProfile.setCreditIDCard(tempIDCard);
    			try
				{  //transaction 2
    			   m_creditIDCardDao.insertCreditIDCard(m_creditIDCardArray1[i],m_userId,m_dataSrcId);
    	           }
    	        catch (DuplicateKeyException dupke)
			    { 
    	           throw new ModuleException(dupke);
    	    	  }	
    	        catch (ConcurrencyConflictException cone)
				{  throw new ModuleException(cone); 
	    	    	}
    		 }
        }
    	
    	//some characteristics of credit profile
    	//copy check_consent_code
    	boolean isModified=false;
    	if ((m_activeCreditProfile.getCreditCheckConsentCode().equals("NA")) && (!m_mergedCreditProfile.getCreditCheckConsentCode().equals("NA")))
    	   { 
    		 m_activeCreditProfile.setCreditCheckConsentCode(m_mergedCreditProfile.getCreditCheckConsentCode());
    	     isModified=true;
    	     }
    	//copy employment_status
    	if ((m_activeCreditProfile.getEmploymentStatusCode().equals("NA")) && (!m_mergedCreditProfile.getEmploymentStatusCode().equals("NA")))
    	   {
    		 m_activeCreditProfile.setEmploymentStatusCode(m_mergedCreditProfile.getEmploymentStatusCode());
    	     isModified=true;
    	     }
    	//copy residency_code
    	if ((m_activeCreditProfile.getResidencyCode().equals("NA")) && (!m_mergedCreditProfile.getResidencyCode().equals("NA")))
    	   { 
    		 m_activeCreditProfile.setResidencyCode(m_mergedCreditProfile.getResidencyCode());
    	     isModified=true;
    	     }
    	//copy primary_credit_card_type_code
    	if ((m_activeCreditProfile.getPrimaryCreditCardCode().equals("NA")) && (!m_mergedCreditProfile.getPrimaryCreditCardCode().equals("NA")))
    	   {
    		 m_activeCreditProfile.setPrimaryCreditCardCode(m_mergedCreditProfile.getPrimaryCreditCardCode());
    	     isModified=true;
    	     }
    	//copy secondary_credit_card_type_code
    	if ((m_activeCreditProfile.getSecondaryCreditCardCode().equals("NA")) && (!m_mergedCreditProfile.getSecondaryCreditCardCode().equals("NA")))
    	   { 
    		 m_activeCreditProfile.setSecondaryCreditCardCode(m_mergedCreditProfile.getSecondaryCreditCardCode());
    	     isModified=true;
    	     }
    	//copy under_legal_care_code
    	if ((m_activeCreditProfile.getUnderLegalCareCode().equals("NA")) && (!m_mergedCreditProfile.getUnderLegalCareCode().equals("NA")))
    	   {	
    		 m_activeCreditProfile.setUnderLegalCareCode(m_mergedCreditProfile.getUnderLegalCareCode());
    	     isModified=true;
    	     }
        //copy birth date
    	//if (((m_activeCreditProfile.getBirthdate()==null)||(m_activeCreditProfile.getBirthdate().toString().equals(""))) && (m_mergedCreditProfile.getBirthdate().getDay()>0))
    	if ((m_activeCreditProfile.getBirthdate()==null) && (m_mergedCreditProfile.getBirthdate()!=null))  
    	  {	
 		     m_activeCreditProfile.setBirthdate(m_mergedCreditProfile.getBirthdate());
 	         isModified=true;
 	     }

        
    	//update credit profile
    	//transaction 3
    	if (isModified)
    	try
		{   // RTCA chage 3, update credit profile and credit worthiness as well
            m_creditProfileDao.updateCreditProfileExt(m_activeCreditProfile,m_userId,m_dataSrcId, isModified,cvupdated);
		}
    	catch (ConcurrencyConflictException cone)
		{  
            throw new ModuleException(cone); 
    	}
    	catch (DuplicateKeyException dupke)
		{  
            throw new ModuleException(dupke); 
    	}

     } */
    /**
     * Consolidate content of two profiles:
      * <ul>
      *     <li>if p1 has some id cards but p2 doesn't,copy to p2</li>  
      *     <li>if p1 has some characteristics attributes but p2 doesn't, copy to p2 </li>      
      *     <li>if p1 has address but p2 doesn't, copy address info to p2</li>
      *     <li>update p2<li>
      * </ul>
     */
    private void consolidateContentRC() throws ModuleException
    {   
        boolean idChg = false;
        boolean dobChg = false;
        long pid =0; 
        String record = "";
        String dl = "";
        String provcd ="";
        String sin = "";
        String noUpdatingAddress ="N";
        //Nov. 29 had meeting, that if m_activeCreditProfile.getCreditCheckConsentCode() = "N", we do not update Address
        //copy credit address
        CreditAddress tempCreditAddress;
        //transaction 1
        if(!noUpdatingAddress.equalsIgnoreCase(m_activeCreditProfile.getCreditCheckConsentCode()))
        {
            if ((m_activeCreditProfile.getCreditAddress()==null) && (m_mergedCreditProfile.getCreditAddress()!=null))
            { 
                tempCreditAddress=m_mergedCreditProfile.getCreditAddress();
                tempCreditAddress.setCreditProfileId(m_activeCreditProfile.get_id());
                m_activeCreditProfile.setCreditAddress(tempCreditAddress);
                try
                { 
                    m_creditAddressDao.insertCreditAddress(tempCreditAddress,m_userId,m_dataSrcId);
                }
                catch (DuplicateKeyException dupke)
                { 
                    throw new ModuleException(dupke);
                }
                catch ( ConcurrencyConflictException cone)
                {  
                    throw new ModuleException(cone); 
                }
            }
        }
        //copy IdCards
        boolean isFound;
        CreditIDCard tempIDCard;
        m_creditIDCardArray1=m_mergedCreditProfile.getCreditIDCards();
        m_creditIDCardArray2=m_activeCreditProfile.getCreditIDCards();
        for (int i=0; i< m_creditIDCardArray1.length; i++) 
        {   isFound=false;
            for(int j=0; j< m_creditIDCardArray2.length;j++)
            {   
                if(m_creditIDCardArray2[j].getIdTypeCode().equals(m_creditIDCardArray1[i].getIdTypeCode()))
                  isFound=true;
                }
            if(!isFound) 
            {   tempIDCard=m_creditIDCardArray1[i];
                tempIDCard.setCreditProfileId(m_activeCreditProfile.get_id());
                m_activeCreditProfile.setCreditIDCard(tempIDCard);           
                
                String typecd = tempIDCard.getIdTypeCode();  // merge id card type, 

                if(CreditIDCard.DRIVERS_LICENSE_KEY.equalsIgnoreCase(typecd))
                {
                    dl =  tempIDCard.getIdNumber();
                    provcd = tempIDCard.getProvinceCode(); 

                    idChg = true;  
                    
                }
                else if(CreditIDCard.SIN_KEY.equalsIgnoreCase(typecd))
                {
                    sin = tempIDCard.getIdNumber();
                    idChg = true;  
                } 
               
               
                try
                {  //transaction 2
                   m_creditIDCardDao.insertCreditIDCard(m_creditIDCardArray1[i],m_userId,m_dataSrcId);
                   }
                catch (DuplicateKeyException dupke)
                { 
                   throw new ModuleException(dupke);
                  } 
                catch (ConcurrencyConflictException cone)
                {  throw new ModuleException(cone); 
                    }
             }
        }
        String dob ="";

//      some characteristics of credit profile
        //copy check_consent_code
        boolean isModified=false;
        if ((m_activeCreditProfile.getCreditCheckConsentCode().equals("NA")) && (!m_mergedCreditProfile.getCreditCheckConsentCode().equals("NA")))
           { 
             m_activeCreditProfile.setCreditCheckConsentCode(m_mergedCreditProfile.getCreditCheckConsentCode());
             isModified=true;
             }
        //copy employment_status
        if ((m_activeCreditProfile.getEmploymentStatusCode().equals("NA")) && (!m_mergedCreditProfile.getEmploymentStatusCode().equals("NA")))
           {
             m_activeCreditProfile.setEmploymentStatusCode(m_mergedCreditProfile.getEmploymentStatusCode());
             isModified=true;
             }
        //copy residency_code
        if ((m_activeCreditProfile.getResidencyCode().equals("NA")) && (!m_mergedCreditProfile.getResidencyCode().equals("NA")))
           { 
             m_activeCreditProfile.setResidencyCode(m_mergedCreditProfile.getResidencyCode());
             isModified=true;
             }
        //copy primary_credit_card_type_code
        if ((m_activeCreditProfile.getPrimaryCreditCardCode().equals("NA")) && (!m_mergedCreditProfile.getPrimaryCreditCardCode().equals("NA")))
           {
             m_activeCreditProfile.setPrimaryCreditCardCode(m_mergedCreditProfile.getPrimaryCreditCardCode());
             isModified=true;
             }
        //copy secondary_credit_card_type_code
        if ((m_activeCreditProfile.getSecondaryCreditCardCode().equals("NA")) && (!m_mergedCreditProfile.getSecondaryCreditCardCode().equals("NA")))
           { 
             m_activeCreditProfile.setSecondaryCreditCardCode(m_mergedCreditProfile.getSecondaryCreditCardCode());
             isModified=true;
             }
        //copy under_legal_care_code
        if ((m_activeCreditProfile.getUnderLegalCareCode().equals("NA")) && (!m_mergedCreditProfile.getUnderLegalCareCode().equals("NA")))
           {    
             m_activeCreditProfile.setUnderLegalCareCode(m_mergedCreditProfile.getUnderLegalCareCode());
             isModified=true;
             }
        //copy birth date
        //if (((m_activeCreditProfile.getBirthdate()==null)||(m_activeCreditProfile.getBirthdate().toString().equals(""))) && (m_mergedCreditProfile.getBirthdate().getDay()>0))
        if ((m_activeCreditProfile.getBirthdate()==null) && (m_mergedCreditProfile.getBirthdate()!=null))  
          { 
             m_activeCreditProfile.setBirthdate(m_mergedCreditProfile.getBirthdate());
             dob = DateUtil.convertDateToString(m_mergedCreditProfile.getBirthdate(),DateUtil.SIMPLE_DATE_FORMAT_YYYY_MM_DD);
             dobChg = true;
             isModified=true;
         }
        //RTCA credit profile parameters copy over.       
        //copy province code
        if ((m_activeCreditProfile.getApplicationProvinceCd()== null || m_activeCreditProfile.getApplicationProvinceCd().trim().equals("")) && (m_mergedCreditProfile.getApplicationProvinceCd() !=null && !m_mergedCreditProfile.getApplicationProvinceCd().trim().equals("")))
        { 
           m_activeCreditProfile.setApplicationProvinceCd(m_mergedCreditProfile.getApplicationProvinceCd());
           isModified=true;
        }
        // copy CreditCheckConsentCode code
        if ((m_activeCreditProfile.getCreditCheckConsentCode()== null || m_activeCreditProfile.getCreditCheckConsentCode().trim().equals("")) && (m_mergedCreditProfile.getCreditCheckConsentCode() !=null && !m_mergedCreditProfile.getCreditCheckConsentCode().trim().equals("")))
        { 
           m_activeCreditProfile.setCreditCheckConsentCode(m_mergedCreditProfile.getCreditCheckConsentCode());
           isModified=true;
        } 
        //copy by pass code; discuss with team how it should be?
        //if (!(m_activeCreditProfile.isBypassMatchIndicator()) && (m_mergedCreditProfile.isBypassMatchIndicator()))
        //{ 
        //  m_activeCreditProfile.setBypassMatchIndicator(m_mergedCreditProfile.isBypassMatchIndicator());
        // isModified=true;
        //}           
        //
          
        if(idChg || dobChg)
        {
            pid = m_activeCreditProfile.get_id();        
            StringBuffer buf = new StringBuffer();
            String spid = pid+"";            
            String fpid= strfm(spid, 20);
            buf.append(fpid);
            String fdl = strfm(dl, 50);
            buf.append(fdl);
            String fprovcd = strfm(provcd, 10);
            buf.append(fprovcd);
            String fsin = strfm(sin, 50);
            buf.append(fsin);
            if(dobChg)
                buf.append(dob);
            record = new String(buf);
           
            m_pWriterWs.println(record);
            
        }
//      RTCA change 3
        // update Credit Worthiness
        // 1. find active credit profile worthiness Product Set Qualifications  
        // 2. if active Product Set Qualifications  is null, find merge credit profile worthiness Product Set Qualifications  
        // 3. if active Product Set Qualifications  is null, and merge Product Set Qualifications  is not null,
        // 4. dao call to do an insert the credit worthiness with the master credit profile id.
        boolean cvupdated = false;
        CreditValueExt active_creditValue = (CreditValueExt) m_activeCreditProfile.getCreditValue();
        ProductCategoryQualification pq = new ProductCategoryQualification();
      
        //active_creditValue.getProductCatQualification().getProductCategoryList().get(0);
        if(active_creditValue == null || ((active_creditValue !=null) && active_creditValue.getProductCatQualification() ==null))
        {   
               CreditValueExt merge_creditValue = (CreditValueExt) m_mergedCreditProfile.getCreditValue();
               if((merge_creditValue !=null) && merge_creditValue.getProductCatQualification() != null)
               {    
                   merge_creditValue.setCreditProfileId(m_activeCreditProfile.get_id());
                   merge_creditValue.setLastUpdateUserId(m_userId);
                   Integer sid = SecurityContext.getSystemSourceId();
                   merge_creditValue.setDataSourceId(sid.longValue());
                   m_activeCreditProfile.setCreditValue(merge_creditValue);
                   cvupdated = true;
                   //isModified = true;
                    
               }
            
        }
        //update credit profile
        //transaction 3
        if (isModified || cvupdated)
            try
            {  
                m_creditProfileDao.updateCreditProfileExt(m_activeCreditProfile,m_userId,SecurityContext.getSystemSourceId(),isModified, cvupdated);
                
            }
            catch (ConcurrencyConflictException cone)
            {  throw new ModuleException(cone); 
                }
            catch (Exception dupke)
            {  throw new ModuleException(dupke); 
                }

     } 
   
    /**
     * Consolidate links:(p2 is selected as an active profile)
      * <ul>
	  * 	<li>get all customer ids that point to either p1 or p2 as primary</li>	
	  * 	<li>expire all secondary links(if there is any)between p1/p2 and any one those customers</li>		
	  * 	<li>transfer(expire and create new one)all links associated with p1 to p2</li>
	  *     <li>get all customer ids that are secondary linked with p2. if the result is not null,go proceed steps below</li>
	  *     <li>get primary credit profiles of those customers</li>
	  *     <li>create secondary links(if not linked yet)between those credit profiles and all customers under p2(linked as primary)</li>
	  * </ul>
     */
    private void consolidateLinks(long profileId1,long profileId2) throws ModuleException
    {
    	expireSecondaryLinks(profileId1,profileId2);
    	transferLinks(profileId1,profileId2);
    	createSecondaryLinks(profileId1,profileId2);
    }
    public static final String PRIMARY_KEY="PRI";
    public static final String SECONDARY_KEY="SEC";
    /**
     * Expire secondary links:(p2 is selected as an active profile)
      * <ul>
	  * 	<li>get all customer ids that point to either p1 or p2 as primary</li>	
	  * 	<li>expire all secondary links(if there is any)between p1/p2 and any one those customers</li>		
	  * </ul>
     */
    private void expireSecondaryLinks(long profileId1,long profileId2) throws ModuleException
    {
     List customerList1=new ArrayList();
     List customerList2=new ArrayList();
     Set  customerSet=new HashSet();
     CreditProfileCustomerMap tempMap;
     CreditProfileCustomerMap customerMap=new CreditProfileCustomerMap();
     String customerId;
     try
	 {
     	customerList1=m_profileCustomerMapDao.getCustomerListByProfileId(profileId1+"",PRIMARY_KEY);
     	customerList2=m_profileCustomerMapDao.getCustomerListByProfileId(profileId2+"",PRIMARY_KEY);
     	customerSet.addAll(customerList1);
     	customerSet.addAll(customerList2);
     	for (Iterator it = customerSet.iterator(); it.hasNext();) 
     	{
     		tempMap = (CreditProfileCustomerMap)it.next();
     		customerId=tempMap.getCustomerId();
            customerMap.setCreditProfileId(String.valueOf(profileId1));
            customerMap.setCustomerId(customerId);
            customerMap.setProfileCustomerMapTypeCode(SECONDARY_KEY);
            customerMap.setUserId(m_userId);
            customerMap.setDataSrcId(m_dataSrcId);
            //expire links
            //transaction 4
            if (m_profileCustomerMapDao.checkCreditProfileCustomerMap(customerMap).intValue()!=0)
                m_profileCustomerMapDao.expireCreditProfileCustomerMap(customerMap);
            customerMap.setCreditProfileId(String.valueOf(profileId2));
            //transaction 5
            if (m_profileCustomerMapDao.checkCreditProfileCustomerMap(customerMap).intValue()!=0)
                m_profileCustomerMapDao.expireCreditProfileCustomerMap(customerMap);
        }
	 }
     catch(ObjectNotFoundException e1)
	  {  
 	    throw new ModuleException(e1);
 	   }
     catch(ConcurrencyConflictException e2)
	  {  
	    throw new ModuleException(e2);
	   }	
    }
    
    /**
     * Transfer(expire and create new one)all links associated with p1 to p2.(p2 is selected as an active profile)
     */
    private void transferLinks(long profileId1,long profileId2) throws ModuleException
    {
     List links=new ArrayList();
     CreditProfileCustomerMap expiredLink;
     try
	 {
     	links=m_profileCustomerMapDao.getAllLinksByProfileId(profileId1);
     	for (Iterator it = links.iterator(); it.hasNext();) 
     	{
     		expiredLink = (CreditProfileCustomerMap) it.next();
     		expiredLink.setUserId(m_userId);
     		expiredLink.setDataSrcId(m_dataSrcId);
     		//transaction 6
     		m_profileCustomerMapDao.expireCreditProfileCustomerMap(expiredLink);
     		expiredLink.setCreditProfileId(String.valueOf(profileId2));
     		//transaction 7
     		if (m_profileCustomerMapDao.checkCreditProfileCustomerMap(expiredLink).intValue()==0)
     		    m_profileCustomerMapDao.insertCreditProfileCustomerMap(expiredLink);
           
        }
	 }
     catch(ObjectNotFoundException e1)
	  {  
 	    throw new ModuleException(e1);
 	   }
     catch(ConcurrencyConflictException e2)
	  {  
	    throw new ModuleException(e2);
	   }
 	 catch(DuplicateKeyException e3)
	  {  
	    throw new ModuleException(e3);
	   }
    }
    
    
    /**
     * Create secondary links:(p2 is selected as an active profile)
      * <ul>
	  * 	 <li>get all customer ids that are secondary linked with p2. if the result is not null,go proceed steps below</li>
	  *      <li>get primary credit profiles of those customers</li>
	  *      <li>create secondary links(if not linked yet)between those credit profiles and all customers under p2(linked as primary)</li>		
	  * </ul>
     */
    private void createSecondaryLinks(long profileId1,long profileId2) throws ModuleException
    {
     List tempCustomerList=new ArrayList();
     List profileList=new ArrayList();
     List customerList=new ArrayList();
     CreditProfileCustomerMap secondaryLink=new CreditProfileCustomerMap();
     CreditProfileCustomerMap resultTempMap1,resultTempMap2,resultTempMap3;
     String tempCustomerId;
     String profileId;
     String customerId;
     try
	 {
     	tempCustomerList=m_profileCustomerMapDao.getCustomerListByProfileId(profileId2+"",SECONDARY_KEY);
     	if (!tempCustomerList.isEmpty())
     	{ 
     	  for (Iterator it = tempCustomerList.iterator(); it.hasNext();) 
     	  {  resultTempMap1 = (CreditProfileCustomerMap)it.next();
     	  	 tempCustomerId = resultTempMap1.getCustomerId();
     	  	 profileList=m_profileCustomerMapDao.getProfileListByCustomerId(tempCustomerId,PRIMARY_KEY);
     	  	 for (Iterator iter = profileList.iterator(); iter.hasNext();) 
        	  {   resultTempMap2 = (CreditProfileCustomerMap)iter.next();
     	  	      profileId = resultTempMap2.getCreditProfileId();
     	  	      secondaryLink.setCreditProfileId(profileId);
     	  	      secondaryLink.setProfileCustomerMapTypeCode(SECONDARY_KEY);
     	  	      secondaryLink.setUserId(m_userId);
     	  	      secondaryLink.setDataSrcId(m_dataSrcId);
     	  	      customerList=m_profileCustomerMapDao.getCustomerListByProfileId(profileId2+"",PRIMARY_KEY);
     	  	      for (Iterator iterator = customerList.iterator(); iterator.hasNext();) 
         	      {  resultTempMap3 = (CreditProfileCustomerMap)iterator.next();
      	  	         customerId = resultTempMap3.getCustomerId();
      	  	         secondaryLink.setCustomerId(customerId);
                     //transaction 8
      	  	         if (m_profileCustomerMapDao.checkCreditProfileCustomerMap(secondaryLink).intValue()==0)
      	  	                 m_profileCustomerMapDao.insertCreditProfileCustomerMap(secondaryLink); 
         	      }
        	   } 
          }
     	}
	 }
     catch(ObjectNotFoundException e1)
	  {  
 	    throw new ModuleException(e1);
 	   }
 	 catch(DuplicateKeyException e2)
	  {  
	    throw new ModuleException(e2);
	   }
    }
    
    public static final String CONSOLIDATED_STATUS_KEY = "C";
    public static final String CPROFL_MAPPING_TYP_KEY = "MI";
    public static final String CPROFL_BUSINESS_MAPPING_TYP_KEY="CO";
    
    /**
     * maintain credit profile status and mapping infomation:(p2 is selected as an active profile)
      * <ul>
	  * 	<li>update status code of p1 from "Active" to "Consolidate"</li>	
	  * 	<li>create record in credit profile mapping table to indicate that p1 has been "merged" into p2</li>
	  *     <li>transfer relationship between consumer credit profile and business credit profile</li>
	  * </ul>
     */
    private void maintainCreditProfile() throws ModuleException
    {
       CreditProfileMap creditProfileMap=new CreditProfileMap();
       creditProfileMap.setCreditProfileFromId(String.valueOf(m_mergedCreditProfile.get_id()));
       creditProfileMap.setCreditProfileToId(String.valueOf(m_activeCreditProfile.get_id()));
       creditProfileMap.setProfileMapTypeCode(CPROFL_MAPPING_TYP_KEY);
       creditProfileMap.setUserId(m_userId);
       creditProfileMap.setDataSrcId(m_dataSrcId);
       try
	   {
       //transaction 11
       	m_creditProfileMapDao.insertCreditProfileMap(creditProfileMap);
       	
       //transaction 12
       	m_creditStatusDao.expireCreditStatus(m_mergedCreditProfile,m_userId);
        m_mergedCreditProfile.setStatus(CONSOLIDATED_STATUS_KEY);
    	m_creditStatusDao.insertCreditStatus(m_mergedCreditProfile,m_userId,m_dataSrcId);
	   }
       catch (DuplicateKeyException dupke)
	   {
       	 throw new ModuleException(dupke);
	      }
       catch (ConcurrencyConflictException conce)
	   {
       	 throw new ModuleException(conce);
	      }
     }
    
    /**
     * Consolidate guarantor information:(p2 is selected as an active profile)
     * if p1 was guarantor of any customer, transfer(expire and create new one)
     */
    private void consolidateGuarantor(long profileId1,long profileId2)throws ModuleException
    {
        List guarantorList=new ArrayList();
        CustomerGuarantor guarantor;
        try
   	    {
        	guarantorList=m_guarantorDao.getCustomerGuarantor(profileId1+"");
        	if (!guarantorList.isEmpty())
        	{
        	  for (Iterator it = guarantorList.iterator(); it.hasNext();) 
        	  {
        	  	 guarantor = (CustomerGuarantor) it.next();
        	  	 //transaction 9
        	  	 m_guarantorDao.expireCustomerGuarantor(guarantor,m_userId);
        	  	 guarantor.setGuarantorCreditProfileId(profileId2);
                 //transaction 10
        	  	 m_guarantorDao.insertCustomerGuarantor(guarantor,m_userId,m_dataSrcId);
        	  	 m_guarantorDao.updateCreditProfileByGuarantor(guarantor.getGuaranteedCustomerId()+"",m_userId);
           	   } 
             }
        	}
        catch(ObjectNotFoundException e1)
   	    {  
    	    throw new ModuleException(e1);
    	   }
        catch(ConcurrencyConflictException e2)
   	    {  
   	        throw new ModuleException(e2);
   	    }
    	catch(DuplicateKeyException e3)
   	    {  
   	        throw new ModuleException(e3);
   	    }	
    }
    
    
    /**
     * Transfer ownership between consumer credit profile for proprietor and business credit profile
     * if the merged profile was owner of any business, transfer(expire and create new one)ownership 
     * from merged profile to active profile
     */
    private void transferOwnership(long mergedProfileId,long activeProfileId)throws ModuleException
    {
        List creditProfileMapList=new ArrayList();
        CreditProfileMap creditProfileMap;
        try
   	    {
        	creditProfileMapList=m_creditProfileMapDao.getBusinessOwnershipByProfileId(String.valueOf(mergedProfileId));
        	if (!creditProfileMapList.isEmpty())
        	{
        	  for (Iterator it = creditProfileMapList.iterator(); it.hasNext();) 
        	  {
        		  creditProfileMap = (CreditProfileMap) it.next();
        		  creditProfileMap.setUserId(m_userId);
        	  	  //transaction 13
        		  m_creditProfileMapDao.expireBusinessOwnership(creditProfileMap);
        		  creditProfileMap.setCreditProfileFromId(String.valueOf(activeProfileId));
        		  creditProfileMap.setDataSrcId(m_dataSrcId);
                  //transaction 14
        	      m_creditProfileMapDao.insertCreditProfileMap(creditProfileMap);
        	  	 
           	   } //end loop
             }//end if 
         }//end try
        catch(ObjectNotFoundException e1)
   	    {  
    	    throw new ModuleException(e1);
    	   }
        catch(ConcurrencyConflictException e2)
   	    {  
   	        throw new ModuleException(e2);
   	    }
    	catch(DuplicateKeyException e3)
   	    {  
   	        throw new ModuleException(e3);
   	    }
    	
    }

    public String getOutputWSFile()
    {
        return m_outputWSFile;
    }


    public void setOutputWSFile(String file)
    {
        m_outputWSFile = file;
    }
   
    private  String strfm(String str, int len)
    {
        if(str == null)
            str ="";
        StringBuffer buf = new StringBuffer(str);
        if (len > 0)
        {
            while (buf.length() < len)
            {
                buf.append(" ");
            }
        }
        return new String(buf);
    }

	
}

