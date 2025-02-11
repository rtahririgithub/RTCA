/*
 *  Copyright (c) 2005 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 */

package com.telus.credit.service.dto.update;

import java.util.List;

import com.telus.credit.domain.CreditProfile;
import com.telus.credit.util.CreditMgtUtils;

/**
 * Utility class to determine what components of a CreditProfile have been
 * updated.
 * 
 * @author ypollock
 *  
 */
public class CreditProfileUpdates
{

    private CreditProfile m_modifiedProfile;
    private CreditProfile m_originalProfile;
    private long m_creditProfileId;
    private boolean m_isModified;
    private boolean m_isCreditValueModified;
    private boolean m_isPersonalInfoModified;
    private boolean m_isAddressModified;
    private boolean m_isGuarantorIdModified;
    private boolean m_isGuarantorModified;
    private boolean m_isCommentPresent=false;
    private IdCardUpdates m_idCardUpdates;
    
    // Address, Guarantor and IdCards are nor required, 
    // so an update could mean an Add, Delete or Update
    private String m_addressModType;
    private String m_guarantorModType;
    
    // Constants for modification type
    public final static String DELETED = "D";
    public final static String MODIFIED = "M";
    public final static String ADDED = "A";

    // keys into final map of modified CreditIDCards 
    public final static String ID_CARD_KEY = "id_card";
    public final static String MOD_TYPE_KEY = "mod_type";


    /**
     * Constructor calls all the methods to determine updates.
     * @param originalProfile
     * @param modifiedProfile   
     */
    public CreditProfileUpdates(CreditProfile originalProfile,
            CreditProfile modifiedProfile)
    {
        m_originalProfile = originalProfile;
        m_modifiedProfile = modifiedProfile;
        m_creditProfileId = originalProfile.get_id();
        m_idCardUpdates = new IdCardUpdates( originalProfile, modifiedProfile );
        setAddressModified();
        setCreditValueModified();
        setGuarantorModified();
        setPersonalInfoModified();
	if ( modifiedProfile.getComment() != null
	     && modifiedProfile.getComment().trim().length() > 0 ) {
	    m_isCommentPresent=true;
	}
        setModified();
    }

    /**
     * Sets the isAddressModified and addressModType instance variables.
     * As the address is not required upon creation of a CreditProfile,
     * the update could be an Add, Mod or Delete.
     */
    private void setAddressModified()
    {
        m_isAddressModified =
            !CreditMgtUtils.areEqual( m_originalProfile.getCreditAddress(),
                    m_modifiedProfile.getCreditAddress() );
        
        if(m_isAddressModified){
            
            // set the modification type
            //
            m_addressModType = getModType(m_originalProfile.getCreditAddress(),
                    m_modifiedProfile.getCreditAddress() );
            
            if(ADDED.equals(m_addressModType)){
                // set the profileId in the new object, as  
                // clients may not set it on an Add
                m_modifiedProfile.getCreditAddress().setCreditProfileId(
                        m_originalProfile.get_id());
            }
            
        }
    }


    /**
     * Sets the isCreditValueModified instance variable.  As CreditValue
     * is a required field, the modification type is alway Mod, never Add
     * or Delete.
     */
    private void setCreditValueModified()
    {
        m_isCreditValueModified = 
            !CreditMgtUtils.areEqual( m_originalProfile.getCreditValue(),
                    m_modifiedProfile.getCreditValue() );
    }


    /**
     * Sets the isGuarantorModified, isGuarantorIdModified and 
     * guarantorModType instance variables.
     * As a guarantor is not required upon creation of a CreditProfile,
     * the update could be an Add, Mod or Delete.
     */
    private void setGuarantorModified()
    {

        m_isGuarantorModified =
            !CreditMgtUtils.areEqual( m_originalProfile.getCustomerGuarantor(),
                    m_modifiedProfile.getCustomerGuarantor() );
        
        if(m_isGuarantorModified){

            // set the modification type
            //
            m_guarantorModType = getModType(m_originalProfile.getCustomerGuarantor(),
                    m_modifiedProfile.getCustomerGuarantor() );
        
            // Determine if the guarantor customer id has changed.  This
            // would indicate a new guarantor, not just a mod.
            //
            if(MODIFIED.equals(m_guarantorModType)){
                m_isGuarantorIdModified = ! (m_originalProfile.getCustomerGuarantor().getGuarantorCustomerId()
                    	== m_modifiedProfile.getCustomerGuarantor().getGuarantorCustomerId());
            }
            else if (ADDED.equals(m_guarantorModType))
            {
                m_isGuarantorIdModified = true;
            }
                
        }
    }


    /**
     * Checks each field of personal info data to determine if anything
     * has been changed.  Personal info has a required field
     * ( CreditCheckConsentCode ) so is always an update and never an Add or
     * Delete. 
     */
    private void setPersonalInfoModified()
    {
        m_isPersonalInfoModified =
            !(CreditMgtUtils.areEqual( m_originalProfile.getResidencyCode(),
                    m_modifiedProfile.getResidencyCode() )
            && CreditMgtUtils.areEqual( m_originalProfile.getUnderLegalCareCode(),
                    m_modifiedProfile.getUnderLegalCareCode() )
            && CreditMgtUtils.areEqual( m_originalProfile.getPrimaryCreditCardCode(),
                    m_modifiedProfile.getPrimaryCreditCardCode() )
            && CreditMgtUtils.areEqual( m_originalProfile.getSecondaryCreditCardCode(),
                    m_modifiedProfile.getSecondaryCreditCardCode() )
            && CreditMgtUtils.areEqual( m_originalProfile.getEmploymentStatusCode(),
                    m_modifiedProfile.getEmploymentStatusCode() )
            && CreditMgtUtils.areEqual( m_originalProfile.getBirthdate(),
                    m_modifiedProfile.getBirthdate() )
            && CreditMgtUtils.areEqual( m_originalProfile.getCreditCheckConsentCode(),
					m_modifiedProfile.getCreditCheckConsentCode() )
	      && CreditMgtUtils.areEqual( m_originalProfile.getApplicationProvinceCd(),
					  m_modifiedProfile.getApplicationProvinceCd() )
	      && ( m_originalProfile.isBypassMatchIndicator() == m_modifiedProfile.isBypassMatchIndicator() ) );
    }


    /**
     * Sets the isModified instance variable to true if any component 
     * of the credit profile has been changed.
     */
    private void setModified()
    {
        if ( m_isCreditValueModified || m_isPersonalInfoModified
                || m_isAddressModified || m_isGuarantorModified
                || m_idCardUpdates.isCardIdArrayModified() 
	     || m_isCommentPresent )
        {
            m_isModified = true;
        }
    }
    /**
     * Determines if update is an Addition, Deletion or Update 
     * @param original
     * @param modified
     * @return modification type Add, Delete or Update  
     */
    private String getModType(Object original, Object modified) 	
    {
        String modType = null;
        
            if(original == null){
                modType = CreditProfileUpdates.ADDED;
            }
            else if(modified == null){
                modType = CreditProfileUpdates.DELETED;
            }
            else{
                modType = CreditProfileUpdates.MODIFIED; 
            }
            
        return modType;
    }    


    public boolean isModified()
    {
        return m_isModified;
    }

    public boolean isCommentPresent() {
	return m_isCommentPresent;
    }


    public boolean isCreditValueModified()
    {
        return m_isCreditValueModified;
    }


    public boolean isAddressModified()
    {
        return m_isAddressModified;
    }


    public boolean isPersonalInfoModified()
    {
        return m_isPersonalInfoModified;
    }


    public boolean isGuarantorModified()
    {
        return m_isGuarantorModified;
    }


    public boolean isGuarantorIdModified()
    {
        return m_isGuarantorIdModified;
    }


    public boolean hasCardIdsInOriginal()
    {
        return m_idCardUpdates.hasCardIdsInOriginal();
    }


    public boolean isCardIdArrayModified()
    {
        return m_idCardUpdates.isCardIdArrayModified();
    }
    
    /**
     * Each element of the List contains a Map.  The
     * Map contains two keys:
     * <ol>
     * <li>ID_CARD_KEY which references the modified CreditIDCard</li>
     * <li>MOD_TYPE_KEY which contains the ADDED, DELETED or MOD constant</li>
     * </ol> 
     * @return list of modifications  
     */
    public List getIdCardUpdates(){
        return m_idCardUpdates.getModifiedIdList();
    }
    
    public String getAddressModType()
    {
        return m_addressModType;
    }

    public String getGuarantorModType()
    {
        return m_guarantorModType;
    }

    /**
     * @return Returns the creditProfileId.
     */
    public long getCreditProfileId()
    {
        return m_creditProfileId;
    } 

}
