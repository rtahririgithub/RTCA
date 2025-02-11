/**
 * 
 */
package com.telus.credit.domain;

import com.telus.credit.util.CreditMgtUtils;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;

/**
 * @author x122365
 *
 */
public class CreditAttributeBr {
	
	public static final String CREDIT_ATTRIBUTE_PROVINCE_CODE_INVALID = "CreditAttributeBr.CREDIT_ATTRIBUTE_PROVINCE_CODE_INVALID";

	private static CreditAttributeBr s_bRule;
	
	/**
     * Category name for Province:
     * (eg. BC, ON, OTR)
     */
    public static final String PROVINCE3 = "PROVINCE_3";
	
	
	/**
     * <p><b>Description</b> Returns single instance of this class. </p>
     * @return  single instance of this class.
     */
    public static CreditAttributeBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new CreditAttributeBr();
        }
        return s_bRule;
    }
    
    /**
     * <p><b>Description</b> Executes all isValid* methods of 
     * this class.</p>
     * <p><b>High Level Design: </b></p>
     * If the CreditAttribute domain object is not null, the following method(s)
     * are called:
     * <ol>
     * <li>isValidProvinceCode</li>
     * </ol>
     * @param creditAttribute domain object.
     * @return ValidationResult object, which contains zero or more ValidationError 
     * objects. 
     */
    public ValidationResult validate(CreditAttribute creditAttribute)
    {
        assert creditAttribute != null : "CreditAttribute is null";

        ValidationResult result = new ValidationResult();
        if (creditAttribute.getAttributeCode().equals(CreditAttribute.CURRENT_PROVINCE_RESIDENCY_CODE)) {
	        if ( !isValidProvinceCode( creditAttribute ) ) {
	            result.put( new ValidationError( CreditAttributeBr.CREDIT_ATTRIBUTE_PROVINCE_CODE_INVALID ) );
	        }
        }
        
        return result;
    }
    
    /**
     * <p><b>Description</b> Validates province code.</p>
     * @param creditAttribute domain object.
     * @return true if province is valid; false otherwise. 
     */

    public boolean isValidProvinceCode(CreditAttribute creditAttribute)
    {

        if (CreditMgtUtils.isEmptyOrNull(creditAttribute.getAttributeValue())) {
            //return true;
        	return false;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditAttribute.getAttributeValue(),
        		CreditAttributeBr.PROVINCE3 ) ) {
            return false;
        }
        return true;
    }
    
}
