/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.ProductCategory;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.service.dto.CreditValueDetailDto;
import com.telus.framework.exception.ConcurrencyConflictException;
import com.telus.framework.exception.DuplicateKeyException;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.framework.persistence.AbstractSqlMapDao;


/**
 * 
 * <p><b>Description: </b> This class represents access to the data store for CreditValue domain object. 
 * It is implemented using iBatis ORM framework.</p>
 *
 * <p><br><b>Revision History: </b></p>
 * <table border="1" width="100%">
 * 	<tr>
 * 		<th width="15%">Date</th>
 * 		<th width="15%">Revised By</th>
 * 		<th width="55%">Description</th>
 * 		<th width="15%">Reviewed By</th>
 * 	</tr>
 * 	<tr>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 		<td width="55%">&nbsp;</td>
 * 		<td width="15%">&nbsp;</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
 */

public class CreditValueDaoSqlmap extends AbstractSqlMapDao
        implements
            CreditValueDao
{
	
	private static Log log = LogFactory.getLog(CreditValueDaoSqlmap.class);
	
	final String PRODUCT_CAT_TYP_CD = "1";
	final String FRAUD_MSG_TYP_CD = "2";

    protected final static String STAT_ID_INSERT_CREDIT_VALUE =  
        "credit_value.insert_credit_value";
    
    protected final static String STAT_ID_INSERT_CREDIT_VALUE_DETAIL =  
        "credit_value.insert_credit_value_detail";
    
    protected final static String STAT_ID_GET_CREDIT_VALUE =  
        "credit_value.get_credit_value_by_credit_profile_id";
    
    protected final static String STAT_ID_GET_CREDIT_VALUE_DETAIL =  
        "credit_value.get_credit_value_detail_by_credit_value_id";
    
    protected final static String STAT_ID_GET_CREDIT_ASSESSMENT =  
        "credit_value.get_credit_assessment_by_car_id";
    
    protected final static String STAT_ID_GET_CREDIT_BUREAU_REPORT_IND =  
        "credit_value.get_credit_bureau_report_indicator";
    
    protected final static String STAT_ID_DELETE_CREDIT_VALUE =  
        "credit_value.delete_credit_value";   
    
    protected final static String STAT_ID_DELETE_CREDIT_VALUE_DETAIL =  
        "credit_value.delete_credit_value_detail";
    
    protected final static String STAT_ID_GET_CREDIT_VALUE_EFFECTIVE_START_DTM =  
        "credit_value.get_credit_value_effective_start_dtm_by_credit_profile_id";
    
    private CreditProfileDao m_creditProfileDao;
    
    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>
     * 			This method returns "active" credit value from CREDIT_VALUE 
     * 			table using credit profile id.
     * 		</li> 	
     * </ol>
     */
    public CreditValue getCreditValue(Long creditProfileId)
            throws ObjectNotFoundException
    {
        // Retrieve Credit Value
        CreditValue creditValue = (CreditValue)queryForObject(STAT_ID_GET_CREDIT_VALUE, creditProfileId);
                
        // retrieve Credit Value Detail
        List<CreditValueDetailDto> CreditValueDetailDtoList = queryForList( STAT_ID_GET_CREDIT_VALUE_DETAIL,
        								new Long(creditValue.get_id()) );
        
        if (!CreditValueDetailDtoList.isEmpty()) {
        
	        /*CreditValueDetailDto dto = (CreditValueDetailDto) CreditValueDetailDtoList.get(0);
	        
	        String creditValueDetailTypeCd = dto.getCreditValueDetailTypeCd();
	        
	        creditValue.setCreditValueDetailTypeCd(creditValueDetailTypeCd);
	        
	        if (creditValueDetailTypeCd.equals(PRODUCT_CAT_TYP_CD)) {
	        	
	        	ProductCategoryQualification prodCategoryQualification = new ProductCategoryQualification();
	        	List productCategoryList = new ArrayList();
	        	
	        	for (int i = 0; i < CreditValueDetailDtoList.size(); i++) {
	        		
	        		CreditValueDetailDto creditValueDetailDto = (CreditValueDetailDto) CreditValueDetailDtoList.get(i);
	        		
	        		ProductCategory prodCat = new ProductCategory();
	        		prodCat.setCreditApprovedProductCategoryCd(creditValueDetailDto.getCreditApprvdProductCatCd());
	        		prodCat.setProductQualificationIndicator(creditValueDetailDto.getProductQualificationInd());
	        		
	        		productCategoryList.add(prodCat);
	        	}
	        	
	        	prodCategoryQualification.setBoltOn(creditValue.getProdCategoryBoltOn());
	        	prodCategoryQualification.setProductCategoryList(productCategoryList);
	        	
	        	creditValue.setProductCatQualification(prodCategoryQualification);
	        	
	        } else {
	        	
	        	List fraudMessageCdList = new ArrayList();
	        	
	        	for (int i = 0; i < CreditValueDetailDtoList.size(); i++) {
	        		CreditValueDetailDto creditValueDetailDto = (CreditValueDetailDto) CreditValueDetailDtoList.get(i);
	        		
	        		String fraudMessageCd = creditValueDetailDto.getFraudMessageCd();
	        		fraudMessageCdList.add(fraudMessageCd);
	        	}
	        	
	        	creditValue.setFraudMessageCodeList(fraudMessageCdList);
	        }*/
        	List fraudMessageCdList = new ArrayList();
        	List productCategoryList = new ArrayList();
        	ProductCategoryQualification prodCategoryQualification = new ProductCategoryQualification();
        	
        	for (CreditValueDetailDto dto : CreditValueDetailDtoList) {
        		if(dto.getCreditValueDetailTypeCd().equals(PRODUCT_CAT_TYP_CD)) {
        			ProductCategory prodCat = new ProductCategory();
	        		prodCat.setCreditApprovedProductCategoryCd(dto.getCreditApprvdProductCatCd());
	        		prodCat.setProductQualificationIndicator(dto.getProductQualificationInd());
	        		productCategoryList.add(prodCat);
        		} else if (dto.getCreditValueDetailTypeCd().equals(FRAUD_MSG_TYP_CD)) {
        			fraudMessageCdList.add(dto.getFraudMessageCd());
        		}
        	}
        	
        	if (productCategoryList.size() > 0) 
        		prodCategoryQualification.setProductCategoryList(productCategoryList);
        	prodCategoryQualification.setBoltOn(creditValue.getProdCategoryBoltOn());
        	
        	// set ProductionCategoryQualification
        	creditValue.setProductCatQualification(prodCategoryQualification);
        	
        	// set fraudMessageCodeList
        	if (fraudMessageCdList.size() > 0)
        		creditValue.setFraudMessageCodeList(fraudMessageCdList);
        	
        }// end of if CreditValueDetailDtoList.isEmpty()
        
        // retrieve Credit Assessment Request 
        if (creditValue.getCarId() != null) {
        	
        	try {
		        CreditValue crdValue = (CreditValue) queryForObject(STAT_ID_GET_CREDIT_ASSESSMENT, 
		        													new Long(creditValue.getCarId()));
		        
		        creditValue.setCreditAssessmentTypeCd(crdValue.getCreditAssessmentTypeCd());
		        creditValue.setCreditAssessmentCreationDate(crdValue.getCreditAssessmentCreationDate());
		        creditValue.setCreditAssessmentSubTypeCd(crdValue.getCreditAssessmentSubTypeCd());
        	} catch (ObjectNotFoundException ex) {
        		log.info("Credit Assessment record not found for Credit Profile ID: " + creditProfileId +
        				" (invalid carID)");
        	}
	        
	        // Retrieve Credit Bureau Report Indicator
	        try {
	        	
	        	queryForObject(STAT_ID_GET_CREDIT_BUREAU_REPORT_IND, new Long(creditValue.getCarId()));
	        	// if no exception it returns 1, then Credit Bureau Report Indicator is "Y"
	        	creditValue.setCreditBureauReportInd(true);
	        	
	        } catch ( ObjectNotFoundException ex ) {
	        	// This is expected behavior, will ignore and set creditBureauReportInd="N"
	        	creditValue.setCreditBureauReportInd(false);
	        }
        }
        
        return creditValue;
    }
    
    
    @Override
	public String getCreditValueEffectiveStartDate(Long creditProfileId)
			throws ObjectNotFoundException {
		
		String effectiveStartDatetime = (String)queryForObject(STAT_ID_GET_CREDIT_VALUE_EFFECTIVE_START_DTM,
											creditProfileId);
		
		return effectiveStartDatetime;
	}


    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Inserts into table CREDIT_VALUE.</li>
     * </ol>
     */
    public void insertCreditValue(CreditValue creditValue, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        Map creditValueMap = new HashMap();
        creditValueMap.put( "credit_value", creditValue );
        creditValueMap.put( "userId", userId );
        creditValueMap.put( "sourceId", sourceId );
        
        if (creditValue.getProductCatQualification() != null) {
        	creditValueMap.put( "prodCatBoltOn", new Boolean(creditValue.getProductCatQualification().getBoltOn()) );
        }
        
        //removed in Unified Credit, due to interface change
        //Added by Alan for RTCA 1.6
        /*if ( ThreadLocalUtil.getRiskLevelNum()!=null)
        	creditValueMap.put("riskLevelNum",ThreadLocalUtil.getRiskLevelNum());*/
        //
      //Insert record into CREDIT_VALUE table
        Long creditValueId = (Long) insert( STAT_ID_INSERT_CREDIT_VALUE, creditValueMap );
        
        /*String creditValueDtlTypeCd = creditValue.getCreditValueDetailTypeCd();
        
        if (creditValueDtlTypeCd == null) {
        	if (creditValue.getFraudMessageCodeList() != null && creditValue.getFraudMessageCodeList().size() > 0){
        		creditValueDtlTypeCd = FRAUD_MSG_TYP_CD;
        	} else if(creditValue.getProductCatQualification() != null && 
        			creditValue.getProductCatQualification().getProductCategoryList() != null &&
        			creditValue.getProductCatQualification().getProductCategoryList().size() > 0) {
        		creditValueDtlTypeCd = PRODUCT_CAT_TYP_CD;
        	}
        }
        	
        if (creditValueDtlTypeCd != null) {
	        Map creditValueDtlMap = new HashMap();
	        creditValueDtlMap.put( "userId", userId );
	        creditValueDtlMap.put( "creditValueId", creditValueId );
	        creditValueDtlMap.put( "creditValueDtlTypeCd", creditValueDtlTypeCd );
		    if (creditValueDtlTypeCd.equals(PRODUCT_CAT_TYP_CD)) {
		        	
		       	List productCategoryList = creditValue.getProductCatQualification().getProductCategoryList();
		        	
		       	for (int i = 0; i < productCategoryList.size(); i++) {
		       		ProductCategory prodCat = (ProductCategory) productCategoryList.get(i);
		        		
		       		creditValueDtlMap.put( "prodQualificationInd", new Boolean(prodCat.getProductQualificationIndicator()) );
		       		creditValueDtlMap.put( "creditApprovedProdCategoryCd", prodCat.getCreditApprovedProductCategoryCd() );
		        		
		       		// insert record into CREDIT_VALUE_DTL with Detail Type Code "1"
		       		insert( STAT_ID_INSERT_CREDIT_VALUE_DETAIL, creditValueDtlMap );
		       	}// end of for loop of productCategoryList
		        	
		    } else {
		        	
		       	List fraudMsgCdList = creditValue.getFraudMessageCodeList();
		        	
		       	for (int i = 0; i < fraudMsgCdList.size(); i++) {
		       		String fraudMsgCd = (String) fraudMsgCdList.get(i); 
		        		
		       		creditValueDtlMap.put( "fraudMsgCd", fraudMsgCd );
		        		
		       		// insert record into CREDIT_VALUE_DTL with Detail Type Code "2"
		       		insert( STAT_ID_INSERT_CREDIT_VALUE_DETAIL, creditValueDtlMap );
		       	}// end of for loop of fraudMsgCdList
		        	
		    }// end of if/else of creditValueDtlTypeCd 
        }// end of if(creditValueDtlTypeCd != null)
*/        
        Map creditValueDtlMap = new HashMap();
        creditValueDtlMap.put( "userId", userId );
        creditValueDtlMap.put( "creditValueId", creditValueId );
        
        if (creditValue.getProductCatQualification() != null && creditValue.getProductCatQualification().getProductCategoryList() != null
        		&& creditValue.getProductCatQualification().getProductCategoryList().size() > 0) {
        	
        	List<ProductCategory> ProdCatList = creditValue.getProductCatQualification().getProductCategoryList();
	        creditValueDtlMap.put( "creditValueDtlTypeCd", PRODUCT_CAT_TYP_CD );
	        
	        for (ProductCategory prodCat : ProdCatList) {
	        	creditValueDtlMap.put( "prodQualificationInd", new Boolean(prodCat.getProductQualificationIndicator()) );
	       		creditValueDtlMap.put( "creditApprovedProdCategoryCd", prodCat.getCreditApprovedProductCategoryCd() );	
	       		
	       		// insert record into CREDIT_VALUE_DTL with Detail Type Code "1"
	       		insert( STAT_ID_INSERT_CREDIT_VALUE_DETAIL, creditValueDtlMap );
	        }
        }
        
        if (creditValue.getFraudMessageCodeList() != null && creditValue.getFraudMessageCodeList().size() > 0) {
        	//
            // Recreate the credit value map, as we do not want product qualification to be populated when fraud is present
            //
            creditValueDtlMap = new HashMap();
            creditValueDtlMap.put( "userId", userId );
            creditValueDtlMap.put( "creditValueId", creditValueId );
        	List<String> fraudMsgCdList = creditValue.getFraudMessageCodeList();
        	creditValueDtlMap.put( "creditValueDtlTypeCd", FRAUD_MSG_TYP_CD );
        	
        	for (String fraudMsgCd : fraudMsgCdList) {
        		creditValueDtlMap.put( "fraudMsgCd", fraudMsgCd );
        		
	       		// insert record into CREDIT_VALUE_DTL with Detail Type Code "2"
	       		insert( STAT_ID_INSERT_CREDIT_VALUE_DETAIL, creditValueDtlMap );
        	}
        	
        }
    }


    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Updates CREDIT_VALUE table by setting the effective
     * 		stop date in the current row, and inserting a new row.</li>
     * </ol>
     * @throws ObjectNotFoundException 
     */
    public void updateCreditValue(CreditValue creditValue, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {
        Map creditValueMap = new HashMap();
        creditValueMap.put( "credit_value", creditValue );
        creditValueMap.put( "userId", userId );
        creditValueMap.put( "sourceId", sourceId );
        
        try
        {
        	try 
            {
        		// if we have an existing credit value, expire it first
        		if (creditValue.get_id() > 0) {
		        	// out with the old
		        	// expire data in table CREDIT_VALUE and CREDIT_VALUE_DTL
		            update( STAT_ID_DELETE_CREDIT_VALUE, creditValueMap );
		            update( STAT_ID_DELETE_CREDIT_VALUE_DETAIL, creditValueMap );
        		}
            }
            catch (ConcurrencyConflictException e)
            {
            	if (e.getMessage().contains("No records updated for statement: credit_value.delete_credit_value_detail")) {
            		// do nothing, it's a normal scenario
            		log.info("Credit Value Detail not existing for Credit Profile ID " + creditValue.getCreditProfileId());
            	}
            }
            
            // in with the new
            //insert( STAT_ID_INSERT_CREDIT_VALUE, creditValueMap );
            this.insertCreditValue(creditValue, userId, sourceId);
        }
        catch ( DuplicateKeyException e )
        {
        	new ConcurrencyConflictException(e);
        }

    }
    
    public void updateCreditValue(CreditValue creditValue, String userId, 
    								Integer sourceId, Integer customerId)
    throws ConcurrencyConflictException, ObjectNotFoundException
    {
    	Long creditProfileId = m_creditProfileDao.getPrimaryCreditProfileIdByCustomerId(customerId);
    	try {
	    	// we need to set credit value id for updating credit_value_dtl table
	    	CreditValue crdValue = (CreditValue)queryForObject(STAT_ID_GET_CREDIT_VALUE, creditProfileId);
	    	creditValue.set_id(crdValue.get_id());
    	} catch (ObjectNotFoundException ex) {
    		log.info("Credit Profile ID has no active credit value, inserting a new credit value...");
    	}
    	creditValue.setCreditProfileId(creditProfileId.longValue());
    	
    	this.updateCreditValue(creditValue, userId, sourceId);
    }


	/**
	 * @return the m_creditProfileDao
	 */
	public CreditProfileDao getCreditProfileDao() {
		return m_creditProfileDao;
	}


	/**
	 * @param m_creditProfileDao the m_creditProfileDao to set
	 */
	public void setCreditProfileDao(CreditProfileDao m_creditProfileDao) {
		this.m_creditProfileDao = m_creditProfileDao;
	}

}