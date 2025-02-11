
package com.telus.credit.service;


/**
 * 
 * <p><b>Description: </b> Contains Exception Codes for Credit Mgt runtime exceptions. </p>
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
 * 		<td width="15%">Aug 31, 2005</td>
 * 		<td width="15%">Roman Mikhailov</td>
 * 		<td width="55%">initial version</td>
 * 		<td width="15%">Roman Mikhailov</td>
 * 	</tr>
 * </table>
 * @author Roman Mikhailov
 * 
 */

public final class CreditProfileMgtSvcExceptionCodes
{
	/**
     * Indicates failure to retrieve BUS_LAST_UPDT_TS timestamp 
     * for Credit Profile. (former EX_CODE_001)
     */
    public static final String FAILED_TO_RETRIEVE_LAST_UPDATE_TIMESTAMP = "CreditProfileMgtSvc.FAILED_TO_RETRIEVE_LAST_UPDATE_TIMESTAMP";

    /**
     * Indicates failure to create primary link in 
     * the Customer-CreditProfile mapping table.
     * (former EX_CODE_002)
     */
    public static final String FAILED_TO_CREATE_PRIMARY_LINK = "CreditProfileMgtSvc.FAILED_TO_CREATE_PRIMARY_LINK";

    /**
     * Indicates failure to link customer to secondary credit profile.
     * (former EX_CODE_003)
     */
    public static final String FAILED_TO_CREATE_SECONDARY_LINKS = "CreditProfileMgtSvc.FAILED_TO_CREATE_SECONDARY_LINKS";

    /**
     * Indicates failure to insert Guarantor record.
     * (former EX_CODE_004)
     */
    public static final String FAILED_TO_INSERT_GUARANTOR_RECORD = "CreditProfileMgtSvc.FAILED_TO_INSERT_GUARANTOR_RECORD";

    /**
     * Indicates EnablerCMException.
     * (former EX_CODE_101)
     */
    //public static final String ENABLER_EXCEPTION = "CreditProfileMgtSvc.ENABLER_EXCEPTION";
    /**
     * Indicates CustomerProfileException.
     * (former EX_CODE_101)
     */
    public static final String CUSTOMERPROFILE_EXCEPTION = "CreditProfileMgtSvc.CUSTOMERPROFILE_EXCEPTION";
    
    /**
     * Indicates Unmerge - ObjectNotFoundException.
     * (former EX_CODE_102)
     */
    public static final String OBJECT_NOT_FOUND_EXC_DURING_UNMERGE = "CreditProfileMgtSvc.OBJECT_NOT_FOUND_EXC_DURING_UNMERGE";

    /**
     * Indicates Unmerge - DuplicateKeyException.
     * (former EX_CODE_103)
     */
    public static final String DUPLICATE_KEY_EXC_DURING_UNMERGE = "CreditProfileMgtSvc.DUPLICATE_KEY_EXC_DURING_UNMERGE";

    /**
     * Indicates that Primary credit profile can not be found.
     * (former EX_CODE_104)
     */
    public static final String PRIMARY_CREDIT_PROFILE_IS_NOT_FOUND = "CreditProfileMgtSvc.PRIMARY_CREDIT_PROFILE_IS_NOT_FOUND";

    /**
     * Indicates "U" customer with billing account less than 3 months old.
     * (former EX_CODE_105)
     */
    public static final String UN_ESTABLISHED_CREDIT_VALUE_AND_NEW_ACCOUNT = "CreditProfileMgtSvc.UN_ESTABLISHED_CREDIT_VALUE_AND_NEW_ACCOUNT";


    /**
     * Indicates that Risk indicator not acceptable.
     * (former EX_CODE_106)
     */
    public static final String RISK_INDICATOR_NOT_ACCEPTABLE = "CreditProfileMgtSvc.RISK_INDICATOR_NOT_ACCEPTABLE";

    /**
     * Indicates that Account under treatment.
     * (former EX_CODE_107)
     */
    public static final String ACCOUNT_UNDER_TREATMENT = "CreditProfileMgtSvc.ACCOUNT_UNDER_TREATMENT";


    /**
     * Indicates that CreditProfile for matched customer can not be retrieved.
     * (former EX_CODE_108)
     */
    public static final String MATCHED_CREDIT_PROFILE_IS_NOT_FOUND = "CreditProfileMgtSvc.MATCHED_CREDIT_PROFILE_IS_NOT_FOUND";

    /**
     * Indicates that customer's CreditValue is not acceptable for this operation.
     */
    public static final String UNACCEPTABLE_CREDIT_VALUE = "CreditProfileMgtSvc.UNACCEPTABLE_CREDIT_VALUE";


    /**
     * Indicates that this Credit Profile is not of type "Merged". In other 
     * words it has only one Customer linked to it with the 'Primary' link.
     */
    public static final String NOT_A_MERGED_CREDIT_PROFILE = "CreditProfileMgtSvc.NOT_A_MERGED_CREDIT_PROFILE";


    /**
     * Indicates that no updates were made to Credit Profile. 
     * Set in updateCreditProfile service operation
     */
    public static final String NO_UPDATES_MADE_TO_CREDIT_PROFILE = "CreditProfileMgtSvc.NO_UPDATES_MADE_TO_CREDIT_PROFILE";

    /**
     * Modified Credit Profile must not have Customer Guarantor attached to it.
     */
    public static final String UPDATE_WITH_GUARANTOR_INFORMATION_ERROR_1 = "CreditProfileMgtSvc.UPDATE_WITH_GUARANTOR_INFORMATION_ERROR_1";

    /**
     * Credit Profile(s) merged with modified Credit Profile must not have 
     * Customer Guarantor attached to them.
     */
    public static final String UPDATE_WITH_GUARANTOR_INFORMATION_ERROR_2 = "CreditProfileMgtSvc.UPDATE_WITH_GUARANTOR_INFORMATION_ERROR_2";


    /**
     * Indicates that Credit Value can only change to 'N' if ID cards are removed from Credit Profile
     */
    public static final String CREDIT_VALUE_N_ERROR_1 = "CreditProfileMgtSvc.CREDIT_VALUE_N_ERROR_1";


    /**
     * Indicates that Credit Value can change from 'N' only if CreditProfile has ID cards
     */
    public static final String CREDIT_VALUE_N_ERROR_2 = "CreditProfileMgtSvc.CREDIT_VALUE_N_ERROR_2";
    
    /**
     * Indicated that this operation is not supported by this interface
     */
    public static final String OPERATION_NOT_SUPPORTED = "CreditProfileMgtSvc.OPERATION_NOT_SUPPORTED";


    private CreditProfileMgtSvcExceptionCodes()
    {
    }
}
