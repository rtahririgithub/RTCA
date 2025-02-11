/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 *
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 *
 */
package com.telus.crd.assessment.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.security.SecurityContext;

import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.dao.CreditValueDao;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.ProductCategory;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.formletters.framework.batch.AbstractFileReaderModule;
import com.telus.formletters.framework.batch.io.LineRecordReader;
import com.telus.formletters.framework.batch.io.LineRecordWriter;
import com.telus.tmi.xmlschema.xsd.customer.customer.customersubdomain_v3.Customer;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v4.AuditInfo;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ConsumerCustomerManagementServicePortType;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.PolicyException;
import com.telus.wsdl.cmo.informationmgmt.consumercustomermanagementservice_2.ServiceException;
//import org.springframework.beans.factory.annotation.Autowired;
//import com.telus.credit.wlnprfldmgt.webservice.impl.UpdateCreditWorthinessImpl;



/**
 *
 * <p>
 * <b>Description: </b>  This function calls customer mgmt services to updates credit value according to review result from previous job.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>use existing service objects as local objects in order to reuse the business logic encapsulated therein</li>
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
 * @author
 *
 */
public class UpdateCreditValueBulkModule extends AbstractFileReaderModule<LineRecordReader>
{
	private static final Log log = LogFactory.getLog( UpdateCreditValueBulkModule.class );

	//@Autowired
	//private UpdateCreditWorthinessImpl m_data_svc;
	    
//    @Autowired
//	private MonthlyUpDownModuleHelper m_helper;
    
    
   //Injected
    private LineRecordWriter m_writer;
    
    //Injected
    private LineRecordWriter m_writerSucc;
    
//    private String m_userId;
//    private Integer m_sourceId;
    
   //@Autowired
    //Injected
   private CreditValueDao m_creditValueDao;
   
   private CreditProfileDao m_creditProfileDao;

   private ConsumerCustomerManagementServicePortType m_consumerCustomerManagementService;

//    private static final String S_CRDA_BATCH_USER ="CRDA-BATCH";
//    private static final String INPUT_FILE_LINE_NO = "INPUT_FILE_LINE_NO";
//    private static final String OUTPUT_FILE_POSITION = "OUPUT_FILE_POSITION";
//    private static final String OUTPUT_FILE_POSITION_SUCC = "OUPUT_FILE_POSITION_SUCC";
//    private static final String RESULT_REASON_CODE = "NO_CHANGE";

    public UpdateCreditValueBulkModule() {
    }


    @Override
    protected void init(Properties restoreState) throws ModuleException
    {
        super.init(restoreState);
        m_writer.init(restoreState);
        m_writerSucc.init(restoreState);        
    }
    
    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#execute()
     */
    @Override
    public void execute() throws ModuleException
    {
    	String inputStr = m_reader.getCurrentRecord();
    	
    	List<ProductCategory> l_prodCategory = new ArrayList<ProductCategory>();
   		String customerIdStr = null; 
   		String creditValueCode = null;
   		String decisionCode = null;
   		Long customerid = null;
   		String prodCategory1 = null;
		String prodCategory2 = null;
		String prodCategory3 = null;
		String prodCategory4 = null;
		String prodCategory5 = null;
		String prodQulificationIndStr1 = null;
		String prodQulificationIndStr2 = null;
		String prodQulificationIndStr3 = null;
		String prodQulificationIndStr4 = null;
		String prodQulificationIndStr5 = null;
		
       	if(inputStr != null )
		{
       		String[] result = inputStr.split("[|]");
       		if(result.length>=2)
       		{
       		                 
       			for ( int i=0 ; i<result.length ; i++)
       			{
       			
       				switch (i)
       				{
       				case 0:
       					customerIdStr = result[i];
       					break;
       				case 1:
       					creditValueCode = result[i];
       					break;
       				case 2:
       					decisionCode = result[i];
       					break;
       				case 3:
       					prodCategory1 = result[i];
       					break;
       				case 4:
       					prodQulificationIndStr1 =  result[i];
       					if ( prodCategory1!=null && prodQulificationIndStr1!=null && !"".equals(prodCategory1) && !"".equals(prodQulificationIndStr1))
       					{
       						ProductCategory prodCat = new ProductCategory();
       						prodCat.setCreditApprovedProductCategoryCd(prodCategory1);
       						if ( "1".equals(prodQulificationIndStr1) || "Y".equalsIgnoreCase(prodQulificationIndStr1) ||
       								"true".equalsIgnoreCase(prodQulificationIndStr1))
       							prodCat.setProductQualificationIndicator(true);
       						else
       							prodCat.setProductQualificationIndicator(false);
       						l_prodCategory.add(prodCat);
       					}
       					break;
       				case 5:
       					prodCategory2 = result[i];
       					break;
       				case 6:
       					prodQulificationIndStr2 =  result[i];
       					if ( prodCategory2!=null && prodQulificationIndStr2!=null 
       							&& !"".equals(prodCategory2) && !"".equals(prodQulificationIndStr2))
       					{
       						ProductCategory prodCat = new ProductCategory();
       						prodCat.setCreditApprovedProductCategoryCd(prodCategory2);
       						if ( "1".equals(prodQulificationIndStr2) || "Y".equalsIgnoreCase(prodQulificationIndStr2) ||
       								"true".equalsIgnoreCase(prodQulificationIndStr2))
       							prodCat.setProductQualificationIndicator(true);
       						else
       							prodCat.setProductQualificationIndicator(false);
       						l_prodCategory.add(prodCat);
       					}
       					break;
       					
       				case 7:
       					prodCategory3 = result[i];
       					break;
       				case 8:
       					prodQulificationIndStr3 =  result[i];
       					if ( prodCategory3!=null && prodQulificationIndStr3!=null 
       							&& !"".equals(prodCategory3) && !"".equals(prodQulificationIndStr3))
       					{
       						ProductCategory prodCat = new ProductCategory();
       						prodCat.setCreditApprovedProductCategoryCd(prodCategory3);
       						if ( "1".equals(prodQulificationIndStr3) || "Y".equalsIgnoreCase(prodQulificationIndStr3) ||
       								"true".equalsIgnoreCase(prodQulificationIndStr3))
       							prodCat.setProductQualificationIndicator(true);
       						else
       							prodCat.setProductQualificationIndicator(false);
       						l_prodCategory.add(prodCat);
       					}
       					break;
       					
       				case 9:
       					prodCategory4 = result[i];
       					break;
       				case 10:
       					prodQulificationIndStr4 =  result[i];
       					if ( prodCategory4!=null && prodQulificationIndStr4!=null 
       							&& !"".equals(prodCategory4) && !"".equals(prodQulificationIndStr4))
       					{
       						ProductCategory prodCat = new ProductCategory();
       						prodCat.setCreditApprovedProductCategoryCd(prodCategory4);
       						if ( "1".equals(prodQulificationIndStr4) || "Y".equalsIgnoreCase(prodQulificationIndStr4) ||
       								"true".equalsIgnoreCase(prodQulificationIndStr4))
       							prodCat.setProductQualificationIndicator(true);
       						else
       							prodCat.setProductQualificationIndicator(false);
       						l_prodCategory.add(prodCat);
       					}
       					break;
       				case 11:
       					prodCategory5 = result[i];
       					break;
       				case 12:
       					prodQulificationIndStr5 =  result[i];
       					if ( prodCategory5!=null && prodQulificationIndStr5!=null 
       							&& !"".equals(prodCategory5) && !"".equals(prodQulificationIndStr5))
       					{
       						ProductCategory prodCat = new ProductCategory();
       						prodCat.setCreditApprovedProductCategoryCd(prodCategory5);
       						if ( "1".equals(prodQulificationIndStr5) || "Y".equalsIgnoreCase(prodQulificationIndStr5) ||
       								"true".equalsIgnoreCase(prodQulificationIndStr5))
       							prodCat.setProductQualificationIndicator(true);
       						else
       							prodCat.setProductQualificationIndicator(false);
       						l_prodCategory.add(prodCat);
       					}
       					break;       					
       					
       				}
       					
       					
       			}
                		
                if(customerIdStr !=null && !"".equals(customerIdStr))
                {
            	    log.info("Processing string " + inputStr);
            	    log.info("Processing customer with ID" + customerIdStr);
            	               	    
     
                    try
                    {
                        customerid = new Long(customerIdStr);
            
                        if(customerid != null )
                        {
                        	AuditInfo auditInfo = new AuditInfo();
                            auditInfo.setUserId(SecurityContext.getPrincipal().getName());
                            auditInfo.setOriginatorApplicationId((String.valueOf(SecurityContext.getSystemSourceId() <= 0 ? "1497" : SecurityContext.getSystemSourceId())));
                            auditInfo.setTimestamp(new Date());
                            //update customer management db

                            Customer customer = m_consumerCustomerManagementService.getCustomer(customerid);

                           // String oldCreditValue = m_customer.getCreditValueCode();
                        
                            if ( creditValueCode!=null && !creditValueCode.equals(customer.getCreditValueCode())){
               
                            	customer.setCreditValueCode(creditValueCode);
                            	m_consumerCustomerManagementService.updateCustomer(customer, auditInfo) ; //.updateCreditProfile(dt, CREDIT_VALUE_CODE, null, m_aud);
                               	log.info("update request is sent to customer Management web service with customer id:"+ customer.getCustomerId());
                            }
                	        // update credit worthiness
                           
                           // CreditValueDao cvDao = m_data_svc.getCreditValueDao();

                            	Long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId(customerid.intValue());
                            	
                            	log.info( "CustomerId = " + customerIdStr + ", Credit Profile Id = " + creditProfileId);
                                CreditValue creditValue = m_creditValueDao.getCreditValue(creditProfileId);
                                
                                if (creditValueCode!=null && !"".equals(creditValueCode))
                                	creditValue.setCreditValueCode(creditValueCode);
                                
                                if (decisionCode!=null && !"".equals(decisionCode))
                                	creditValue.setCreditDecisionCd(decisionCode);
                                
                                if ( creditValue.getProductCatQualification()!=null && 
                                		creditValue.getProductCatQualification().getProductCategoryList() != null
                                		&& creditValue.getProductCatQualification().getProductCategoryList().size() >0
                                		&& l_prodCategory.size() >0)
                                {
	                                for ( ProductCategory newProdCat : l_prodCategory)
	                                {
	                                	boolean newProdCatIncluded = false;
	                                	for ( Object oldProdCatObj : creditValue.getProductCatQualification().getProductCategoryList() )
	                                	{
	                                		ProductCategory oldProdCat = (ProductCategory)oldProdCatObj ;
	                                		if ( oldProdCat.getCreditApprovedProductCategoryCd().equals(newProdCat.getCreditApprovedProductCategoryCd()))
	                                		{
	                                			
	                                			oldProdCat.setProductQualificationIndicator(newProdCat.getProductQualificationIndicator());
	                                			log.info("updating prod qualification - customer " + customerid + " , prod code " + oldProdCat.getCreditApprovedProductCategoryCd() );
	                                			newProdCatIncluded = true;
	                                		}
	                                		
	                                	}
	                                	
	                                	if ( !newProdCatIncluded )
                                		{
                                			creditValue.getProductCatQualification().getProductCategoryList().add(newProdCat);
                                			log.info("adding prod qualification - customer " + customerid + " , prod code " + newProdCat.getCreditApprovedProductCategoryCd() );
                                		}
	                                	
	                                }
                                }
                                else if ( creditValue.getProductCatQualification()!=null &&  l_prodCategory.size() >0)
                                {
                                	creditValue.getProductCatQualification().setProductCategoryList(l_prodCategory);
                                }
                                else if (  creditValue.getProductCatQualification()==null &&  l_prodCategory.size() >0)
                                {
                                	creditValue.setProductCatQualification(new ProductCategoryQualification());
                                	creditValue.getProductCatQualification().setProductCategoryList(l_prodCategory);
                                	
                                }
                                
                                m_creditValueDao.updateCreditValue(creditValue,  "mupdg-bulk-batch", 0);
                               // m_data_svc.updateCreditWorthiness(m_helper.newCrdTransactionBulk(creditValue,customerid), m_helper.newPfdAuditInfo());
                            }
                            
                        	m_writerSucc.writeRecord( inputStr );
                
                 }
                 catch (PolicyException e)
                 {
        	         com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException faultInfo = e.getFaultInfo();
                     log.error("Updating Credit Value remote customer id problem " + customerid + ",[Credit Value updating failed, web service PolicyException. Error code:"+
        	             (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" +(faultInfo != null ? faultInfo.getErrorMessage() :"") );
                     m_writer.writeRecord("** Customer "+customerid+", whose credit value could NOT be updated because of  Customer Management Service PolicyException. Error code:"+
            		   (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" + (faultInfo != null ? faultInfo.getErrorMessage() :""));
                     
                 }  
                 catch (ServiceException e)
                 {
        	         com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException faultInfo = e.getFaultInfo();
                     log.error("Updating Credit Value with customer id problem "+customerid+", failed,  web service Exception. Error code:"+
            		     (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" + (faultInfo != null ? faultInfo.getErrorMessage() :""));
                     m_writer.writeRecord("** Customer "+customerid + ", whose credit value could NOT be updated because of Customer Management Service ServiceException. Error code:"+
            		 (faultInfo != null ? faultInfo.getErrorCode() : "") + ". Error Msg:" + (faultInfo != null ? faultInfo.getErrorMessage() :"") );  
                     throw new ModuleException( e );
                 }
                 catch (ObjectNotFoundException e)
                 {
                     log.error("Updating Credit Profile with customer id problem "+ customerid+", failed, Credit Profile DAO Exception. Error Msg:" +e.getMessage());
                     m_writer.writeRecord("** Customer "+ customerid + ", whose credit value could NOT be updated because of  Credit Profile DAO Exception. Error Msg:" +e.getMessage());
                    // throw new ModuleException( e );
                  } 
                  
                 catch (ConcurrencyConflictException e) 
                 {
                	 log.error("Updating Credit Profile with customer id problem "+ customerid+", failed, Credit Profile DAO Exception. Error Msg:" +e.getMessage());
                     m_writer.writeRecord("** Customer "+ customerid + ", whose credit value could NOT be updated because of Credit Profile DAO Exception. Error Msg:" +e.getMessage());
                 
                 }
                 
              } 
           }
		}
//        //simulate failure...
//        if ( m_failurePoint != 0 && m_counter >= m_failurePoint ) {
//            throw new ModuleException(
//                    "Simulated Exception. Failure point reached" );
//        }
    }


   
    @Override
    public void launch(BatchContext batchContext) throws ModuleException
    {
        super.launch(batchContext);
    }


    /*
     * (non-Javadoc)
     *
     * @see com.telus.framework.batch.Module#onExit(boolean)
     */
    @Override
    public int onExit(boolean success) throws ModuleException
    {
        m_writer.close();
        m_writerSucc.close();
        return super.onExit(success);
    }



    @Override
    public Properties getStateForRestart() throws ModuleException
    {
        Properties props = super.getStateForRestart();

        m_writer.addStateForRestart(props);
        m_writerSucc.addStateForRestart(props);
        return props;
    }


    @Override
    public Properties getSummary() throws ModuleException
    {
        Properties props = super.getSummary();

        m_writer.addSummary(props);
        m_writerSucc.addSummary(props);
        return props;
    }

    public ConsumerCustomerManagementServicePortType getConsumerCustomerManagementService()
    {
    	return this.m_consumerCustomerManagementService;
    }
    
    public void setConsumerCustomerManagementService ( ConsumerCustomerManagementServicePortType consumerCustomerManagementService)
    {
    	this.m_consumerCustomerManagementService = consumerCustomerManagementService;
    }
    
    public CreditValueDao getCreditValueDao()
    {
    	return this.m_creditValueDao;
    }
    
    public void setCreditValueDao( CreditValueDao creditValueDao)
    {
    	this.m_creditValueDao = creditValueDao;
    }
    
    public LineRecordWriter getWriter()
    {
    	return this.m_writer;
    }
    
    public void setWriter ( LineRecordWriter writer)
    {
    	this.m_writer = writer;
    }
    
    public LineRecordWriter getWriterSucc()
    {
    	return this.m_writerSucc;
    }
    
    public void setWriterSucc( LineRecordWriter writerSucc)
    {
    	this.m_writerSucc = writerSucc;
    }
    
    public CreditProfileDao getCreditProfileDao()
    {
    	return this.m_creditProfileDao;
    }
    
    public void setCreditProfileDao( CreditProfileDao creditProfileDo)
    {
    	this.m_creditProfileDao = creditProfileDo;
    }
}
