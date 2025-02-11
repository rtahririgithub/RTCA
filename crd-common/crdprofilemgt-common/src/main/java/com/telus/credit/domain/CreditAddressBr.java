/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.domain;

import com.telus.credit.util.CreditMgtUtils;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;


/**
 * 
 * <p><b>Description: </b> Encapsulates business rules assocuated with the 
 * CreditAddress domain object.</p>
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
public class CreditAddressBr
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public static final String CREDIT_PROFILE_ADDRESS_TYPE = "CREDIT_PROFILE_ADDRESS_TYPE";
    public static final String CREDIT_PROFILE_ADDRESS_TYPE_INVALID = "CreditAddressBr.CREDIT_PROFILE_ADDRESS_TYPE_INVALID";
    
    /**
     * Signle instance of this class.
     */
    private static CreditAddressBr s_bRule;



    /**
     * Constructor.
     */
    private CreditAddressBr()
    {
        //do nothing here.
    }


    /**
     * <p><b>Description</b> Returns single instance of this class.</p>
     * @return Single instance of this class.
     */
    public static CreditAddressBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new CreditAddressBr();
        }
        return s_bRule;
    }


    /**
     * <p><b>Description</b> Validates province or state.</p>
     * @param creditAddress domain object.
     * @return true if province is valid or null; false otherwise. 
     */

    public boolean isValidProvinceOrState(CreditAddress creditAddress)
    {
        assert creditAddress != null : "CreditAddress is null";

        if ( creditAddress.getProvinceCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditAddress.getProvinceCode(),
                CreditProfileBr.PROVINCE_STATE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Validates country.</p>
     * @param creditAddress domain object.
     * @return  true if country is valid or null; false otherwise. 
     */
    public boolean isValidCountry(CreditAddress creditAddress)
    {
        assert creditAddress != null : "CreditAddress is null";

        if ( creditAddress.getCountryCode() == null ) {
            return true;
        }
        if ( !CreditMgtBrHelper.isValidCode( creditAddress.getCountryCode(),
                CreditProfileBr.COUNTRY ) ) {
            return false;
        }
        return true;
    }
    
    public boolean isValidType(CreditAddress creditAddress){
        assert creditAddress != null : "CreditAddress is null";
        
        // creditAddress.getType returns CreditAddress.CREDIT_BUREAU_ADDRESS_KEY by default.
        //
        if ( !CreditMgtBrHelper.isValidCode( creditAddress.getType(),
                CreditAddressBr.CREDIT_PROFILE_ADDRESS_TYPE ) ) {
            return false;
        }
        return true;
        
    }


    /**
     * <p><b>Description</b> Executes all isValid* methods of 
     * this class.</p>
     * <p><b>High Level Design: </b></p>
     * If the CreditAddress domain object is not null, the following method(s)
     * are called:
     * <ol>
     * <li>isValidProvince</li>
     * <li>isValidCountry</li>
     * </ol>
     * @param creditAddress domain object.
     * @return ValidationResult object, which contains zero or more ValidationError 
     * objects. 
     */
    public ValidationResult validate(CreditAddress creditAddress)
    {
        assert creditAddress != null : "CreditAddress is null";

        ValidationResult result = new ValidationResult();
        if ( !isValidProvinceOrState( creditAddress ) ) {
            result
                    .put( new ValidationError( CreditProfileBr.PROVINCE_STATE_INVALID+ ". creditAddress ProvinceCode =  <" + creditAddress.getProvinceCode() + ">" ) );
        }
        if ( !isValidCountry( creditAddress ) ) {
            result.put( new ValidationError( CreditProfileBr.COUNTRY_INVALID+ ". creditAddress CountryCode =  <" + creditAddress.getCountryCode() + ">"  ) );
        }
        
        if ( !isValidType( creditAddress ) ) {
            result.put( new ValidationError( CREDIT_PROFILE_ADDRESS_TYPE_INVALID+ ". creditAddress Type =  <" + creditAddress.getType() + ">"  ) );
        }
        return result;
    }



    /**
     * Checks if the CreditAddress object has all fields equal to 0, null, or "".
     * @return true if the above condition is true; false otherwise.
     */
    public boolean isEmpty(CreditAddress a)
    {
        assert a!=null : "CreditAddress is null";
        
        boolean result = false;
        if ( CreditMgtUtils.isEmptyOrNull( a.getAddressLineFive() )
                && CreditMgtUtils.isEmptyOrNull( a.getAddressLineFour() )
                && CreditMgtUtils.isEmptyOrNull( a.getAddressLineThree() )
                && CreditMgtUtils.isEmptyOrNull( a.getAddressLineTwo() )
                && CreditMgtUtils.isEmptyOrNull( a.getAddressLineOne() )
                && CreditMgtUtils.isEmptyOrNull( a.getAddressTypeCode() )
                && CreditMgtUtils.isEmptyOrNull( a.getCity() )
                && CreditMgtUtils.isEmptyOrNull( a.getCountryCode() )
                && CreditMgtUtils.isEmptyOrNull( a.getPostalCode() )
                && CreditMgtUtils.isEmptyOrNull( a.getProvinceCode() ) ) {
            result = true;
        }
        return result;
    }


}