/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.domain;

import org.apache.commons.lang3.StringUtils;

import com.telus.credit.util.CreditMgtUtils;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;


/**
 * 
 * <p><b>Description: </b> Encapsulates business rules that 
 * form behavior of the CreditValue domain object.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>Utilizes Singleton design pattern.</li>		
 * 	</ul>
 *
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

public class CreditValueBr
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Category name for Credit Value type:
     * (eg. D, E)
     */
    public static final String CREDIT_VALUE_TYPE = "CREDIT_VALUE_CODE";
    
    public static final String CREDIT_FRAUD_IND_TYPE = "CREDIT_FRAUD_IND";
    
    public static final String CREDIT_VALUE_DETAIL_TYPE = "CREDIT_VALUE_DTL_TYP";
    
    public static final String CREDIT_PRODUCT_CATEGORY_TYPE = "CREDIT_PRODUCT_CATEGORY";

    /**
     * Error message code to signal that Credit Value type is invalid.
     */
    public static final String CREDIT_VALUE_TYPE_INVALID = 
        "CreditValueBr.CREDIT_VALUE_TYPE_INVALID";
    
    /**
     * Error message code to signal that Credit Value comment is blank.
     */
    public static final String CREDIT_VALUE_COMMENT_IS_BLANK = 
        "CreditValueBr.CREDIT_VALUE_COMMENT_IS_BLANK";

    /**
     * Signals that Credit Management PDS population method is invalid.
     */
    public static final String CREDIT_VALUE_POPULATE_METHOD_CODE_INVALID = "CreditValueBr.CREDIT_VALUE_POPULATE_METHOD_CODE_INVALID";
      
    /**
     * Signals that Credit Value is unacceptable.
     */
    public static final String CREDIT_VALUE_IS_UNACCEPTABLE = "CreditValueBr.CREDIT_VALUE_IS_UNACCEPTABLE";
    
    /**
     * Instance of CreditValueBr class.
     */
    private static CreditValueBr s_bRule;


    /**
     * Private constructor. 
     */
    private CreditValueBr()
    {
        //Do nothing here.
    }


    /**
     * <p><b>Description: </b> Returns single instance of this class. </p>
     * @return CreditValueBr instance.
     */
    public static CreditValueBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new CreditValueBr();
        }
        return s_bRule;
    }


    /**
     * <p><b>Description</b> This method validates Credit Value code during 
     * createCreditProfile call</p>
     * @param creditValue domain object.
     * @return  true if the Credit Value code is valid or null; false otherwise.
     */
    public boolean isValidCreditValueCodeForCreate(CreditValue creditValue)
    {
        // It is acceptable to have CreditValue == null during 
        // creation of the new CreditProfile.
        //
        if ( creditValue == null ) {
            return true;
        }

        if ( !CreditMgtBrHelper.isValidCode( creditValue.getCreditValueCode(),
                CREDIT_VALUE_TYPE ) ) {
            return false;
        }
        
        //TODO uncomment following section once Ref PDS tables are available
        /**
        if (creditValue.getFraudIndicatorCd() != null && 
        		!CreditMgtBrHelper.isValidCode(creditValue.getCreditValueCode(),
        		CREDIT_FRAUD_IND_TYPE )) {
        	return false;
        }
        
        if (creditValue.getCreditValueDetailTypeCd() != null &&
        		!CreditMgtBrHelper.isValidCode(creditValue.getCreditValueCode(),
        		CREDIT_VALUE_DETAIL_TYPE )) {
        	return false;
        }
        
        if (creditValue.getProductCatQualification() != null && 
        		creditValue.getProductCatQualification().getProductCategoryList() != null &&
        		creditValue.getProductCatQualification().getProductCategoryList().size() > 0) {
        	
        	List prodCatList = creditValue.getProductCatQualification().getProductCategoryList();
	        for (int i = 0; i < prodCatList.size(); i++ ) {
	        	
	        	ProductCategory productCategory = (ProductCategory) prodCatList.get(i);
	        	if (!CreditMgtBrHelper.isValidCode(productCategory.getCreditApprovedProductCategoryCd(),
	        			CREDIT_PRODUCT_CATEGORY_TYPE )) {
	        		return false;
	        	}
	        }
        }
        **/
        return true;
    }


    /**
     * <p><b>Description: </b> Checks if the CreditValue provided by the caller 
     * is valid for createCreditProfile call</p>
     * <p><b>High Level Design: </b></p>
     * If the CreditValue domain object is not null, the following method(s)
     * are called:
     * <ol>
     * <li>isValidCreditValueCodeForCreate</li>
     * </ol>
     * @param creditValue domain object.
     * @return ValidationResult object, which contains 0 or more ValidationError 
     * objects. 
     */

    public ValidationResult validate(CreditValue creditValue)
    {

        ValidationResult result = new ValidationResult();
        
        if (creditValue.getCreditValueCode() != null && !creditValue.getCreditValueCode().isEmpty()) {
	        if ( !isValidCreditValueCodeForCreate( creditValue ) ) {
	            result.put( new ValidationError( CREDIT_VALUE_TYPE_INVALID ) );
	        }
        }
        

        if(!isValidPopulationMethod(creditValue)) {
            result.put( new ValidationError( CREDIT_VALUE_POPULATE_METHOD_CODE_INVALID ) );
        }

        return result;
    }
    
    /**
     * <p><b>Description: </b> Checks if the CreditValue provided by the caller 
     * is valid for updateCreditProfile call</p>
     * <p><b>High Level Design: </b></p>
     * If the CreditValue domain object is not null, the following method(s)
     * are called:
     * <ol>
     * <li>isValidCreditValueCodeForCreate</li>
     * <li>isCreditValueCommentBlank</li>
     * </ol>
     * @param creditValue domain object.
     * @return ValidationResult object, which contains 0 or more ValidationError 
     * objects. 
     */
    
    public ValidationResult validateForUpdate(CreditValue creditValue)
    {

        ValidationResult result = new ValidationResult();
        if ( !isValidCreditValueCodeForCreate( creditValue ) ) {
            result.put( new ValidationError( CREDIT_VALUE_TYPE_INVALID ) );
        }
        if(isCreditValueCommentBlank(creditValue)){
            result.put( new ValidationError( CREDIT_VALUE_COMMENT_IS_BLANK ) );
        }
        return result;
    }

    /**
 	* Checks if CreditValue is valid for <code>CreditProfileMgtSvc.checkDonorEligibility</code>
 	*  call.
 	* @param creditValue domain object.
 	* @return true if it's valid; false otherwise.
 	* @see com.telus.credit.service.CreditProfileMgtSvc#checkDonorEligibility(int)
 	*/
    public boolean isValidForCheckDonorEligibility(CreditValue creditValue)
    {
        assert creditValue != null : "Credit Value is null";
        String creditValueCode = creditValue.getCreditValueCode();
        if ( CreditValue.DEPOSIT_CREDIT_VALUE_KEY.equals( creditValueCode )
                || CreditValue.RESTRICTED_CREDIT_VALUE_KEY
                        .equals( creditValueCode )
                || CreditValue.NO_CREDIT_INFO_CREDIT_VALUE_KEY
                        .equals( creditValueCode ) ) {
            return false;
        }
        return true;
    }

    /**
     * Checks if the CreditValue object has all fields equal to 0, null, or "".
     * @return true if the above condition is true; false otherwise.
     */
    public boolean isEmpty(CreditValue cv)
    {
        assert cv!=null : "CreditValue is null";
        
        boolean result = false;

        if ( CreditMgtUtils.isEmptyOrNull( cv.getComment() )
                && CreditMgtUtils.isEmptyOrNull( cv.getCreditValueCode() ) ) {
            result = true;
        }
        return result;
    }
    
    /**
     * Checks if the CreditValue comment is blank. 
     * @return true is the comment is blank; false otherwise.
     */
    
    public boolean isCreditValueCommentBlank(CreditValue cv){
        assert cv!=null : "CreditValue is null";
        
        if(StringUtils.isBlank(cv.getComment())){
            return true;
        }
        return false;
    }
    
    /**
     * <p><b>Description</b> Checks the validity of Credit Value population method. </p>
     * @param creditValue
     * @return true if method is valid; false otherwise.  
     */
    public boolean isValidPopulationMethod(CreditValue creditValue)
    {
        assert creditValue != null : "CreditValue is null";

        if ( !CreditMgtBrHelper.isValidCode( creditValue
                .getMethod(),
                CreditProfileBr.CREDIT_PROFILE_POPULATE_METHOD_CODE ) ) {
            return false;
        }
        return true;
    }
}
