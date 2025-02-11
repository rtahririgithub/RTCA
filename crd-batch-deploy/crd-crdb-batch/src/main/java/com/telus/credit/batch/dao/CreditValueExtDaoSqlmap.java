/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.batch.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.CreditValueExt;
import com.telus.credit.domain.ProductCategory;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.batch.dto.CreditValueDetailDto;
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

public class CreditValueExtDaoSqlmap extends AbstractSqlMapDao
        implements
            CreditValueExtDao
{

    private static Log log = LogFactory.getLog(CreditValueExtDaoSqlmap.class);
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
        CreditValueExt creditValue = (CreditValueExt)queryForObject(STAT_ID_GET_CREDIT_VALUE, creditProfileId);

        // retrieve Credit Value Detail
        List CreditValueDetailDtoList = queryForList( STAT_ID_GET_CREDIT_VALUE_DETAIL,
        								new Long(creditValue.get_id()) );

        if (!CreditValueDetailDtoList.isEmpty()) {
            
	        CreditValueDetailDto dto = (CreditValueDetailDto) CreditValueDetailDtoList.get(0);

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

	        } else 
            {
	        	List fraudMessageCdList = new ArrayList();
	        	for (int i = 0; i < CreditValueDetailDtoList.size(); i++) 
                {
	        		CreditValueDetailDto creditValueDetailDto = (CreditValueDetailDto) CreditValueDetailDtoList.get(i);
	        		String fraudMessageCd = creditValueDetailDto.getFraudMessageCd();
	        		fraudMessageCdList.add(fraudMessageCd);
	        	}
	        	creditValue.setFraudMessageCodeList(fraudMessageCdList);
	        }
        }// end of if CreditValueDetailDtoList.isEmpty()

        // retrieve Credit Assessment Request
        try 
        {
            Long carid = creditValue.getCarId();
            if(carid != null)
            {
                CreditValueExt crdValue = (CreditValueExt) queryForObject(STAT_ID_GET_CREDIT_ASSESSMENT,
                        carid);

                creditValue.setCreditAssessmentTypeCd(crdValue.getCreditAssessmentTypeCd());
                creditValue.setCreditAssessmentCreationDate(crdValue.getCreditAssessmentCreationDate());
                creditValue.setCreditAssessmentSubTypeCd(crdValue.getCreditAssessmentSubTypeCd());

               // Retrieve Credit Bureau Report Indicator
        	    queryForObject (STAT_ID_GET_CREDIT_BUREAU_REPORT_IND, creditValue.getCarId());
        	    // if no exception it returns 1, then Credit Bureau Report Indicator is "Y"
        	    creditValue.setCreditBureauReportInd("true");
            } else
            {
                creditValue.setCreditBureauReportInd("false");                
            }
        } catch ( ObjectNotFoundException ex ) 
        {
        	// This is expected behavior, will ignore and set creditBureauReportInd="N"            
            creditValue.setCreditBureauReportInd("false");
        }catch (Exception e)
        {
            creditValue.setCreditBureauReportInd("false");
        }
        
        return creditValue;
    }


    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Inserts into table CREDIT_VALUE.</li>
     * </ol>
     */
  /*  public void insertCreditValue(CreditValue creditValue, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        CreditValueExt creditValueExt = (CreditValueExt) creditValue;
        Map creditValueMap = new HashMap();
        creditValueMap.put( "credit_value", creditValueExt );
        creditValueMap.put( "userId", userId );
        creditValueMap.put( "sourceId", sourceId );

        if (creditValueExt.getProductCatQualification() != null) 
        {
        	creditValueMap.put( "prodCatBoltOn", new Boolean(creditValueExt.getProductCatQualification().getBoltOn()) );
        }

      //Insert record into CREDIT_VALUE table
          
        Long creditValueId = (Long) insert( STAT_ID_INSERT_CREDIT_VALUE, creditValueMap );
         
        Map creditValueDtlMap = new HashMap();
        creditValueDtlMap.put( "userId", userId );
        creditValueDtlMap.put( "creditValueId", creditValueId );

        String creditValueDtlTypeCd = creditValueExt.getCreditValueDetailTypeCd();
        creditValueDtlMap.put( "creditValueDtlTypeCd", creditValueDtlTypeCd );

        if (creditValueDtlTypeCd != null && creditValueDtlTypeCd.equals(PRODUCT_CAT_TYP_CD)) 
        {
             
        	List productCategoryList = creditValueExt.getProductCatQualification().getProductCategoryList();

        	for (int i = 0; i < productCategoryList.size(); i++) 
            {
        		ProductCategory prodCat = (ProductCategory) productCategoryList.get(i);

        		creditValueDtlMap.put( "prodQualificationInd", new Boolean(prodCat.getProductQualificationIndicator()) );
        		creditValueDtlMap.put( "creditApprovedProdCategoryCd", prodCat.getCreditApprovedProductCategoryCd() );
                  
        		// insert record into CREDIT_VALUE_DTL with Detail Type Code "1"
        		insert( STAT_ID_INSERT_CREDIT_VALUE_DETAIL, creditValueDtlMap );
                 
        	}// end of for loop of productCategoryList

        } else 
        {

        	List fraudMsgCdList = creditValueExt.getFraudMessageCodeList();
            
        	for (int i = 0; i < fraudMsgCdList.size(); i++) 
            {
        		String fraudMsgCd = (String) fraudMsgCdList.get(i);

        		creditValueDtlMap.put( "fraudMsgCd", fraudMsgCd );

        		// insert record into CREDIT_VALUE_DTL with Detail Type Code "2"
                
        		insert( STAT_ID_INSERT_CREDIT_VALUE_DETAIL, creditValueDtlMap );
                
        	}// end of for loop of fraudMsgCdList

        }// end of if/else of creditValueDtlTypeCd
    } */

///////
    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     *      <li>Inserts into table CREDIT_VALUE.</li>
     * </ol>
     */
    public void insertCreditValue(CreditValue creditValue1, String userId, Integer sourceId)
            throws DuplicateKeyException
    {
        CreditValueExt creditValue = (CreditValueExt) creditValue1;
        Map creditValueMap = new HashMap();
        creditValueMap.put( "credit_value", creditValue );
        creditValueMap.put( "userId", userId );
        creditValueMap.put( "sourceId", sourceId );
        
        if (creditValue.getProductCatQualification() != null) {
            creditValueMap.put( "prodCatBoltOn", new Boolean(creditValue.getProductCatQualification().getBoltOn()) );
        }
        
      //Insert record into CREDIT_VALUE table
        Long creditValueId = (Long) insert( STAT_ID_INSERT_CREDIT_VALUE, creditValueMap );
        
        String creditValueDtlTypeCd = creditValue.getCreditValueDetailTypeCd();
        
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
        }
    }
    ///////

    /**
     * <p><b>High Level Design</b></p>
     * <ol>
     * 		<li>Updates CREDIT_VALUE table by setting the effective
     * 		stop date in the current row, and inserting a new row.</li>
     * </ol>
     */
    public void updateCreditValue(CreditValue creditValue, String userId, Integer sourceId)
            throws ConcurrencyConflictException
    {

        Map creditValueMap = new HashMap();
        creditValueMap.put( "credit_value",  creditValue );
        creditValueMap.put( "userId", userId );
        creditValueMap.put( "sourceId", sourceId );

        try
        {
//          out with the old
            // expire data in table CREDIT_VALUE and CREDIT_VALUE_DTL
            update( STAT_ID_DELETE_CREDIT_VALUE, creditValueMap );        

        } catch ( ConcurrencyConflictException e )
        {
            
            //new if nothing to expriring, skip it
            log.info("Exception while expiring credit worthiness, credit value: " + e, e );
        }
        try
        {
//          out with the old
            // expire data in table  CREDIT_VALUE_DTL            
            update( STAT_ID_DELETE_CREDIT_VALUE_DETAIL, creditValueMap );

        } catch ( ConcurrencyConflictException e )
        {
            
            //new if nothing to expriring, skip it
            log.info("Exception while expiring credit worthiness, credit value detail: " + e, e );
        }
        try
        {
        	
            // in with the new
            //insert( STAT_ID_INSERT_CREDIT_VALUE, creditValueMap );
            // call insertCreditWorthiness to handle logic for credit value detail
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
        
    	Long creditProfileId = (Long) queryForObject(
    							CreditProfileExtDaoSqlmap.STAT_ID_GET_PRIMARY_CREDIT_PROFILE_ID_BY_CUSTOMER_ID,
    							customerId );

    	(creditValue).setCreditProfileId(creditProfileId.longValue());

    	this.updateCreditValue(creditValue, userId, sourceId);
    }


}