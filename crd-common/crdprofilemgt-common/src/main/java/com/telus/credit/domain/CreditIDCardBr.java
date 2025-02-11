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
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;


/**
 * 
 * <p><b>Description: </b>Encapsulates business rules assocuated with the 
 * CreditIDCard domain object.</p>
 * <p><b>Design Observations: </b></p>
 * 	<ul>
 * 		<li>Utilizes Singleton design pattern (GoF).</li>		
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
public class CreditIDCardBr
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /**
     * Error message code to signal that Credit Profile Identification type is invalid.
     */
    public static final String CREDIT_PROFILE_IDENTIFICATION_TYPE_INVALID = "CreditIDCardBr.CREDIT_PROFILE_IDENTIFICATION_TYPE_INVALID";

    /**
     * Error message code to signal that Credit Profile Identification number is invalid.
     */
    public static final String CREDIT_PROFILE_IDENTIFICATION_NUMBER_INVALID = "CreditIDCardBr.CREDIT_PROFILE_IDENTIFICATION_NUMBER_INVALID";

    /**
     * Error message code to signal that Province is invalid for this Provincial ID.
     */
    public static final String INVALID_PROVINCE_FOR_PROVINCIAL_ID = "CreditIDCardBr.INVALID_PROVINCE_FOR_PROVINCIAL_ID";

    /**
     * Error message code to signal that Province or State 
     * is invalid for this Drivers Licence.
     */
    public static final String INVALID_PROVINCE_OR_STATE_FOR_DRIVERS_LICENCE = "CreditIDCardBr.INVALID_PROVINCE_OR_STATE_FOR_DRIVERS_LICENCE";


    /**
     * Error message code to signal that Province is not required for SIN, 
     * HealthCare Card and Passport.
     */
    public static final String PROVINCE_NOT_REQUIRED_FOR_SIN_HCC_PSP = "CreditIDCardBr.PROVINCE_NOT_REQUIRED_FOR_SIN_HCC_PSP";


    /**
     * Error message code to signal that Country is invalid for this Passport.
     */
    public static final String COUNTRY_IS_INVALID_FOR_PASSPORT = "CreditIDCardBr.COUNTRY_IS_INVALID_FOR_PASSPORT";


    /**
     * Error message code to signal that  Country is not required for SIN, 
     * Drivers License, HealthCare Card and Provincial ID
     */
    public static final String COUNTRY_NOT_REQUIRED_FOR_SIN_DL_HCC_PID = "CreditIDCardBr.COUNTRY_NOT_REQUIRED_FOR_SIN_DL_HCC_PID";


    /**
     * Error message code to signal that SIN is invalid.
     */
    public static final String SIN_IS_INVALID = "CreditIDCardBr.SIN_IS_INVALID";


    /**
     * Category name for Credit Profile Identification types:
     * (eg. SIN, DL)
     */
    public static final String CREDIT_PROFILE_IDENTIFICATION_TYPE = "CREDIT_IDENTIFICATION_TYPE";


    private static CreditIDCardBr s_bRule;

    /**
     * Flag that indicates that object of this class is instantiated in the 
     * service layer.
     */
    private boolean m_isServiceLayer;

    /**
     * Constructor. 
     */
    protected CreditIDCardBr()
    {
        //do nothing here.
    }

    
    /**
     * Setter for the m_isServiceLayer
     */
    public void setIsServiceLayer(boolean b){
        m_isServiceLayer = b;
    }
    
    /**
     * @return Returns true if the object of this class is used in the service layer; 
     * false otherwise 
     */
    public boolean isServiceLayer(){
        return m_isServiceLayer;
    }
    

    /**
     * <p><b>Description</b> Returns single instance of this class. </p>
     * @return  single instance of this class.
     */
    public static CreditIDCardBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new CreditIDCardBr();
        }
        return s_bRule;
    }


    /**
     * <p><b>Description</b> Validates type of id Card against Reference ODS.</p>
     * @param creditIDCard that contains the type of the card.
     * @return  true if the id type is valid or null; false otherwise.
     */
    public boolean isValidCardType(CreditIDCard creditIDCard)
    {
        if ( creditIDCard.getIdTypeCode() == null ) {
            return false;
        }
        if ( CreditMgtBrHelper.isValidCode( creditIDCard.getIdTypeCode(),
                CreditIDCardBr.CREDIT_PROFILE_IDENTIFICATION_TYPE ) ) {
            return true;
        }
        return false;
    }


    /**
     * <p><b>Description</b> Validates province / state code against Reference ODS 
     *  and checks if id card requires it.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>"Provincial ID" requires province code.</li>
     * 		<li>"Drivers License" requires either province or state codes.</li>
     * 		<li>"SIN", "HealthCare card" and "Passport" does not require province 
     * 		or state.</li>
     * </ul>
     * @param creditIDCard.
     * @return  ValidationResult that contains zero or more validation errors.
     */

    public ValidationResult validateProvince(CreditIDCard creditIDCard)
    {
        ValidationResult result = new ValidationResult();

        if ( CreditIDCard.PROVINCIAL_ID_KEY.equals( creditIDCard
                .getIdTypeCode() ) ) {
            if ( !isValidProvince( creditIDCard ) ) {
                result.put( new ValidationError(
                        INVALID_PROVINCE_FOR_PROVINCIAL_ID ) );
            }
        }
        else if ( CreditIDCard.DRIVERS_LICENSE_KEY.equals( creditIDCard
                .getIdTypeCode() ) ) {
            if ( !(isValidProvince( creditIDCard ) || isValidState( creditIDCard )) ) {
                result.put( new ValidationError(
                        INVALID_PROVINCE_OR_STATE_FOR_DRIVERS_LICENCE ) );
            }
        }
        else {
            if ( creditIDCard.getProvinceCode() != null ) {
                result.put( new ValidationError( creditIDCard.getIdTypeCode()
                        + "." + CreditProfileBr.PROVINCE_NOT_REQUIRED ) );
            }
        }
        return result;
    }


    /**
     * <p><b>Description</b> Validates country code against Reference ODS and checks
     *  if id card requires it.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>"Passport" id type requires country code.</li>
     * 		<li>Other id types do not require country code.</li>
     * </ul>
     * @param creditIDCard.
     * @return  ValidationResult that contains validation errors.
     */


    public ValidationResult validateCountry(CreditIDCard creditIDCard)
    {
        ValidationResult result = new ValidationResult();

        if ( CreditIDCard.PASSPORT_NUMBER_KEY.equals( creditIDCard
                .getIdTypeCode() ) ) {
            if ( !isValidCountry( creditIDCard ) ) {
                result.put( new ValidationError(
                        COUNTRY_IS_INVALID_FOR_PASSPORT ) );
            }
        }
        else {
            if ( creditIDCard.getCountryCode() != null ) {
                result.put( new ValidationError(
                        COUNTRY_NOT_REQUIRED_FOR_SIN_DL_HCC_PID ) );
            }
        }
        return result;
    }


    /**
     * <p><b>Description</b> Checks if id card number is null.</p>
     * @param creditIDCard that has the number of the card.
     * @return  true if the number is not null; false otherwise.
     */
    public boolean isValidIdNumber(CreditIDCard creditIDCard)
    {
        if ( creditIDCard.getIdNumber() == null ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Validates Province.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>Validates against Reference ODS.</li>
     * </ul>
     * @param creditIDCard.
     * @return  true if the province is valid; false otherwise.
     */
    public boolean isValidProvince(CreditIDCard creditIDCard)
    {
        if ( !CreditMgtBrHelper.isValidCode( creditIDCard.getProvinceCode(),
                CreditProfileBr.PROVINCE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Validates state.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>Validates against Reference ODS</li>
     * </ul>
     * @param creditIDCard.
     * @return  true if the state is valid; false otherwise.
     */
    public boolean isValidState(CreditIDCard creditIDCard)
    {
        if ( !CreditMgtBrHelper.isValidCode( creditIDCard.getProvinceCode(),
                CreditProfileBr.STATE ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Validates Country.</p>
     * <p><b>High Level Design: </b></p>
     * <ul>
     * 		<li>Validates against Reference ODS</li>
     * </ul>
     * @param creditIDCard.
     * @return  true if the country is valid; false otherwise.
     */
    public boolean isValidCountry(CreditIDCard creditIDCard)
    {
        if ( !CreditMgtBrHelper.isValidCode( creditIDCard.getCountryCode(),
                CreditProfileBr.COUNTRY ) ) {
            return false;
        }
        return true;
    }


    /**
     * <p><b>Description</b> Executes all 'isValid*' methods of 
     * this class. </p>
     * <p><b>High Level Design: </b></p>
     * If the CreditIDCard domain object is not null, the following method(s)
     * are called:
     * <ol>
     * <li>isValidCardType</li>
     * <li>isValidIdNumber</li>
     * <li>isValidSinNumber in case id card is of type SIN</li>
     * <li>validateProvince</li>
     * <li>validateCountry</li>
     * </ol>
     * @param creditIDCard domain object.
     * @return ValidationResult that contains zero or more validation errors. 
     */
    public ValidationResult validate(CreditIDCard creditIDCard)
    {
        assert creditIDCard != null : "CreditIDCard is null";
        ValidationResult result = new ValidationResult();
        if ( !isValidCardType( creditIDCard ) ) {
            result.put( new ValidationError(
                    CREDIT_PROFILE_IDENTIFICATION_TYPE_INVALID ) );
        }


        if ( CreditIDCard.SIN_KEY.equals( creditIDCard.getIdTypeCode() ) ) {
            if ( !isValidSinNumber( creditIDCard.getIdNumber() ) ) {
                result.put( new ValidationError( SIN_IS_INVALID ) );
            }
        }

        result.merge( validateProvince( creditIDCard ) );
        result.merge( validateCountry( creditIDCard ) );

        return result;
    }


    protected int convertAndAddNumber(int num)
    {
        int result = 0;
        int product = num * 2;
        int[] digits = new int[2];
        if ( product > 9 ) {
            fill( digits, product );
            result = addNumbersInArrayOfSize2( digits );
        }
        else {
            result = product;
        }
        return result;
    }


    private void fill(int[] digits, int num)
    {
        for ( int i = 0; i < digits.length; i++ ) {
            digits[i] = getDigit( num, i, digits.length );
        }
    }


    protected int getDigit(int num, int position, int numOfDigits)
    {
        String numString = String.valueOf( num );
        if ( numString.length() > numOfDigits ) {
            throw new IllegalArgumentException(
                    "Exception in CreditIDCard.getDigit(): num - " + num
                            + "; position - " + position );
        }
        return Character.getNumericValue( numString.charAt( position ) );

    }


    protected int addNumbersInArrayOfSize2(int[] nums)
    {
        return nums[0] + nums[1];
    }


    /**
     * <p><b>Description</b> Validates SIN number. </p>
     * <p><b>Algorythm for SIN validation: </b></p>
     * <ol>
     * 		<li> Write the SIN on a sheet of paper, e.g. 440-968-592</li>
     *		<li> Insert a check mark over the 2nd, 4th, 6th and 8th digits</li> 
     *      <li> 
     * 			Write the SIN again, but this time doubling the digits that were check-marked, 
     * 			i.e. 480-18616-5182. 
     * 		</li> 
     *		<li> Where the doubling of a single digit results in a two-digit number, then: 
     *			<ol>
     *				<li> Add these two digits to form a single digit</li> 
     *				<li> Add all of these numbers, i.e. 4+8+0+9+6+7+5+9+2 = 50.</li>
     *			</ol>
     *		</li>
     *		<li>
     *			If the SIN is valid the resulting total must be of a multiple of ten. 
     *			Therefore the above SIN is valid in that the total is 50.
     *		</li>
     * </ol>
     * @param sinNumberStr SIN number.
     * @return true if SIN number is valid; false otherwise
     */
    public boolean isValidSinNumber(String sinNumberStr)
    {
        if(m_isServiceLayer){
            sinNumberStr = EncryptionUtil.decrypt(sinNumberStr);
        }
        
        if ( sinNumberStr == null || !isSinInCorrectFormat( sinNumberStr ) ) {
            return false;
        }
        int[] digitsInSinNumber = new int[9];

        fill( digitsInSinNumber, sinNumberStr );

        int sumOfDigits = 0;

        for ( int i = 0; i < 9; i++ ) {
            if ( i == 1 || i == 3 || i == 5 || i == 7 ) {
                sumOfDigits += convertAndAddNumber( digitsInSinNumber[i] );
            }
            else {
                sumOfDigits += digitsInSinNumber[i];
            }
        }
        return sumOfDigits % 10 == 0 ? true : false;
    }


    protected void fill(int[] digits, String str)
    {
        for ( int i = 0; i < digits.length; i++ ) {
            digits[i] = Character.getNumericValue( str.charAt( i ) );
        }
    }


    protected boolean isSinInCorrectFormat(String num)
    {
        boolean result = true;
        if ( num.length() < 9 ) {
            result = false;
        }

        try {
            Integer.parseInt( num );
        }
        catch ( NumberFormatException e1 ) {
            result = false;
        }

        return result;
    }


    /**
     * Checks if the CreditIDCard object has all fields equal to 0, null, or "".
     * @return true if the above condition is true; false otherwise.
     */
    public boolean isEmpty(CreditIDCard cidc)
    {
        assert cidc != null : "CreditIDCard is null";

        boolean result = false;

        if ( CreditMgtUtils.isEmptyOrNull( cidc.getCountryCode() )
                && CreditMgtUtils.isEmptyOrNull( cidc.getIdNumber() )
                && CreditMgtUtils.isEmptyOrNull( cidc.getIdTypeCode() )
                && CreditMgtUtils.isEmptyOrNull( cidc.getProvinceCode() ) ) {
            result = true;
        }
        return result;
    }

}