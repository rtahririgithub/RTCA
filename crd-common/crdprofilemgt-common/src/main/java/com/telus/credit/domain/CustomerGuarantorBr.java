/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.domain;


import java.util.Date;

import com.telus.credit.util.CreditMgtUtils;
import com.telus.framework.validation.ValidationError;
import com.telus.framework.validation.ValidationResult;


/**
 * 
 * <p>
 * <b>Description: </b> Incapsulates business rules that form behavior of the
 * CustomerGuarantor domain object.
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li>Utilizes Singleton design pattern.</li>
 * </ul>
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
 * @author Roman Mikhailov
 *  
 */

public class CustomerGuarantorBr
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    /**
     * Error message code to signal that Guarantor ID is missing.
     */
    public static final String GUARANTOR_ID_MISSING = 
        "CustomerGuarantorBr.GUARANTOR_ID_MISSING";
    
    /**
     * Error message code to signal that Customer Id for is invalid.
     */
    public static final String CUSTOMER_ID_FOR_GUARANTOR_IS_INVALID = 
        "CustomerGuarantorBr.CUSTOMER_ID_FOR_GUARANTOR_IS_INVALID";
    
    /**
     * Error message code to signal that Guarantor full name is missing.
     */
    public static final String GUARANTOR_FULL_NAME_MISSING = 
        "CustomerGuarantorBr.GUARANTOR_FULL_NAME_MISSING";
    
    /**
     * Error message code to signal that Guarantor phone number is missing.
     */
    public static final String GUARANTOR_PHONE_NUMBER_MISSING = 
        "CustomerGuarantorBr.GUARANTOR_PHONE_NUMBER_MISSING";

    
    /**
     * Error message code to signal that Guaranteed amount is missing.
     */
    public static final String GUARANTEED_AMOUNT_MISSING = 
        "CustomerGuarantorBr.GUARANTEED_AMOUNT_MISSING";
            
    /**
     * Error message code to signal that Guarantor expiry date is invalid.
     */
    public static final String GUARANTOR_EXPIRY_DATE_INVALID = 
        "CustomerGuarantorBr.GUARANTOR_EXPIRY_DATE_INVALID";
    
    /**
     * Instance of CustomerGuarantorBr class.
     */
    private static CustomerGuarantorBr s_bRule;



    /**
     * Private constructor. Exists only to defeat instantiation.
     */
    private CustomerGuarantorBr()
    {
        //Do nothing here.
    }


    /**
     * <p>
     * <b>Description: </b> Returns single instance of this class.
     * </p>
     * 
     * @return CustomerGuarantorBr instance.
     */
    public static CustomerGuarantorBr getInstance()
    {
        if ( s_bRule == null ) {
            s_bRule = new CustomerGuarantorBr();
        }
        return s_bRule;
    }


    /**
     * Checks the value for "guarantor customer id" attribute in the
     * CustomerGuarantor object is not equal to zero.
     * 
     * @param customerGuarantor
     *            domain object.
     * @return true if the value is not zero; false otherwise.  
     */
    public boolean isValidGuarantorCustomerId(
            CustomerGuarantor customerGuarantor)
    {
        assert customerGuarantor != null : "CustomerGuarantor is null";
        // GuarantorCustomerId is an int, can only check that
        // it is not zero!!!

        if ( customerGuarantor.getGuarantorCustomerId() == 0 ) {
            return false;
        }
        return true;
    }



    /**
     * Checks the value for "guarantor full name" attribute
     * in the CustomerGuarantor object.
     * 
     * @param customerGuarantor
     *            domain object.
     * @return true if the value is not empty or null; false otherwise.
     */
    public boolean isValidGuarantorFullName(CustomerGuarantor customerGuarantor)
    {
        assert customerGuarantor != null : "CustomerGuarantor is null";
        //check if it's not null or empty
        if ( customerGuarantor.getGuarantorFullName() != null
                && customerGuarantor.getGuarantorFullName().length() > 0 ) {
            return true;
        }
        return false;
    }


    /**
     * <p>
     * <b>Description: </b> Checks the value for "guarantor phone number"
     * attribute in the CustomerGuarantor object.
     * </p>
     * 
     * @param customerGuarantor
     *            domain object.
     * @return true if the value is not empty or null; false otherwise.
     */
    public boolean isValidGuarantorPhoneNumber(
            CustomerGuarantor customerGuarantor)
    {
        assert customerGuarantor != null : "CustomerGuarantor is null";
        //check if it's not null or empty
        if ( customerGuarantor.getGuarantorPhoneNumber() != null
                && customerGuarantor.getGuarantorPhoneNumber().length() > 0 ) {
            return true;
        }
        return false;
    }


    /**
     * <p>
     * <b>Description: </b> Checks the value for "guaranteed amount" attribute
     * in the CustomerGuarantor object.
     * </p>
     * 
     * @param customerGuarantor
     *            domain object.
     * @return true if the value is greater than zero; false otherwise.
     */
    public boolean isValidGuaranteedAmount(CustomerGuarantor customerGuarantor)
    {
        assert customerGuarantor != null : "CustomerGuarantor is null";

        if ( customerGuarantor.getGuaranteedAmount() == null ) {
            return false;
        }

        if ( customerGuarantor.getGuaranteedAmount().doubleValue() > 0 ) {
            return true;
        }
        return false;
    }


    /**
     * <b>Description: </b> Checks that "expiry date" is not null and 
     * that it's not before 'now' 
     * 
     * @param customerGuarantor
     *            domain object.
     * @return true if expiry date is not null or before "now";
     *         false otherwise.
     */
    public boolean isValidExpiryDate(CustomerGuarantor customerGuarantor) {
		assert customerGuarantor != null : "CustomerGuarantor is null";
		boolean result = true;

		if (customerGuarantor.getExpiryDate() == null
				|| customerGuarantor.getExpiryDate().before(new Date())) {
			result = false;
		}

		return result;
	}


    /**
	 * <p>
	 * <b>Description: </b> Validates the CustomerGuarantor domain object during
	 * creation of a new credit profile.
	 * </p>
	 * High-Level Design: </br></br> If the CustomerGuarantor domain object is
	 * not null, the following methods will be called called.
	 * <ol>
	 * <li>isValidGuarantorCustomerId</li>
	 * <li>isValidGuaranteedCustomerId</li>
	 * <li>isValidGuarantorFullName</li>
	 * <li>isValidGuarantorPhoneNumber</li>
	 * <li>isValidExpiryDate</li>
	 * </ol>
	 * 
	 * @param customerGuarantor
	 *            domain object.
	 * @return ValidationResult that contains validation results.
	 */
    public ValidationResult validateForCreate(
            CustomerGuarantor customerGuarantor)
    {
        ValidationResult result = new ValidationResult();
       

        if ( !isValidGuarantorCustomerId( customerGuarantor ) ) {
            result.put( new ValidationError(
                    GUARANTOR_ID_MISSING ) );
        }
        if ( !isValidGuarantorFullName( customerGuarantor ) ) {
            result.put( new ValidationError(
                    GUARANTOR_FULL_NAME_MISSING ) );
        }
        if ( !isValidGuarantorPhoneNumber( customerGuarantor ) ) {
            result.put( new ValidationError(
                    GUARANTOR_PHONE_NUMBER_MISSING ) );
        }
        if ( !isValidGuaranteedAmount( customerGuarantor ) ) {
            result.put( new ValidationError(
                    GUARANTEED_AMOUNT_MISSING  ) );
        }
        if ( !isValidExpiryDate( customerGuarantor ) ) {
            result.put( new ValidationError(
                    GUARANTOR_EXPIRY_DATE_INVALID ) );
        }

        return result;
    }


    /**
     * <p>
     * <b>Description: </b> Validates CustomerGuarantor object during updateCreditProfile call.
     * </p>
     * 
     * @param customerGuarantor
     *            domain object.
     * @return ValidationResult that contains zero or more ValidationError(s).
     */
    public ValidationResult validateForUpdate(
            CustomerGuarantor customerGuarantor)
    {
        return validateForCreate( customerGuarantor );
    }
    
    /**
     * Checks if the CustomerGuarantor object has all fields equal to 0, 
     * null, or "".
     * @return <code>true</code> if the above condition is true; <code>false</code> 
     * otherwise.
     */
    public boolean isEmpty(CustomerGuarantor g)
    {
        assert g!=null : "CustomerGuarantor is null";
        
        boolean result = false;

        if ( CreditMgtUtils.isEmptyOrNull( g.getGuarantorFullName() )
                && CreditMgtUtils.isEmptyOrNull( g.getGuarantorPhoneNumber() )
                && g.getGuarantorCustomerId()==0 
                && g.getExpiryDate() == null && g.getGuaranteedAmount() == null ) {
            result = true;
        }
        return result;
    }

}