package com.telus.credit.wlnprfldmgt.webservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.telus.credit.dao.CreditAddressDao;
import com.telus.credit.dao.CreditValueDao;
import com.telus.credit.domain.CreditAttribute;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.util.CreditMgtUtils;
import com.telus.credit.wlnprfldmgt.webservice.util.WlnPrflDMgtUtil;

public class CreditProfileUpdates extends
		com.telus.credit.service.dto.update.CreditProfileUpdates {

	private CreditProfile m_modifiedProfile;
	private CreditProfile m_originalProfile;
	private long m_creditProfileId;
	private boolean m_isModified;
	private boolean m_isCreditValueModified;
	private boolean m_isOnlyFraudIndicatorModified=false;
	private boolean m_isPersonalInfoModified;
	private boolean m_isAttributesModified;
	private boolean m_isAddressModified;
	private boolean m_isCreditCheckConsentModified=false;
    private boolean m_isCommentPresent=false;
	private IdCardUpdates m_idCardUpdates;
	private List<CreditAttribute> m_attributeModifiedList;
	//private AttributeUpdates m_attributeUpdates;

	private CreditValueDao m_creditValueDao;

	private CreditAddressDao m_creditAddressDao;

	// Address, Guarantor and IdCards are nor required,
	// so an update could mean an Add, Delete or Update
	private String m_addressModType;
	private String m_guarantorModType;
	
	private boolean m_isGuarantorModified;
	private boolean m_isGuarantorIdModified;
    private boolean m_isCreditCheckConsentModifiedFromYToN=false;
	// Constants for modification type
	public final static String DELETED = "D";
	public final static String MODIFIED = "M";
	public final static String ADDED = "A";

	// keys into final map of modified CreditIDCards
	public final static String ID_CARD_KEY = "id_card";
	public final static String MOD_TYPE_KEY = "mod_type";

	/**
	 * Constructor calls all the methods to determine updates.
	 * 
	 * @param originalProfile
	 * @param modifiedProfile
	 */

	public CreditProfileUpdates(CreditProfile originalProfile,
			CreditProfile modifiedProfile) {
		super(originalProfile, modifiedProfile);
		m_originalProfile = originalProfile;
		m_modifiedProfile = modifiedProfile;
		m_creditProfileId = originalProfile.get_id();
		m_idCardUpdates = new IdCardUpdates(originalProfile, modifiedProfile);
		//m_attributeUpdates = new AttributeUpdates(originalProfile, modifiedProfile);
		setAddressModified();
		setCreditValueModified();
		setGuarantorModified();
		setPersonalInfoModified();
		setAttributesModified();
		if ( modifiedProfile.getComment() != null
		     && modifiedProfile.getComment().trim().length() > 0 ) {
		    m_isCommentPresent=true;
		}
		setModified();
	}

	/**
     * Sets the isGuarantorModified, isGuarantorIdModified and 
     * guarantorModType instance variables.
     * As a guarantor is not required upon creation of a CreditProfile,
     * the update could be an Add, Mod or Delete.
     */
    private void setGuarantorModified()
    {
	if ( m_modifiedProfile.getCustomerGuarantor() == null ) {
	    this.m_isGuarantorModified = false;
	}
	else if (m_modifiedProfile.getCustomerGuarantor() != null
		 && m_originalProfile.getCustomerGuarantor() != null
		 && m_originalProfile.getCustomerGuarantor()
		 .getLastUpdateTimestamp().compareTo(
						     m_modifiedProfile
						     .getCustomerGuarantor().getLastUpdateTimestamp()) > 0) {
	    this.m_isGuarantorModified = false;
	}
	else {
	    this.m_isGuarantorModified = !CreditMgtUtils.areEqual(m_originalProfile
							     .getCustomerGuarantor(), m_modifiedProfile.getCustomerGuarantor());
	}

        
        if(this.m_isGuarantorModified){

            // set the modification type
            //
            m_guarantorModType = getModType(m_originalProfile.getCustomerGuarantor(),
                    m_modifiedProfile.getCustomerGuarantor() );
        
            // Determine if the guarantor customer id has changed.  This
            // would indicate a new guarantor, not just a mod.
            //
            if(MODIFIED.equals(m_guarantorModType)){
                this.m_isGuarantorIdModified = ! (m_originalProfile.getCustomerGuarantor().getGuarantorCustomerId()
                        == m_modifiedProfile.getCustomerGuarantor().getGuarantorCustomerId());
            }
            else if (ADDED.equals(m_guarantorModType))
            {
                this.m_isGuarantorIdModified = true;
            }
                
        }
    }
    
	/**
	 * Sets the isAddressModified and addressModType instance variables. As the
	 * address is not required upon creation of a CreditProfile, the update
	 * could be an Add, Mod or Delete.
	 */
	private void setAddressModified() {
		if (m_modifiedProfile.getCreditAddress() == null)
			m_isAddressModified = false;
		else if (m_modifiedProfile.getCreditAddress() != null
				&& m_originalProfile.getCreditAddress() != null
				&& m_originalProfile.getCreditAddress()
						.getLastUpdateTimestamp().compareTo(
								m_modifiedProfile
										.getCreditAddress().getLastUpdateTimestamp()) > 0)
			m_isAddressModified = false;
		else
			m_isAddressModified = !CreditMgtUtils.areEqual(m_originalProfile
					.getCreditAddress(), m_modifiedProfile.getCreditAddress());

		if (m_isAddressModified) {

			// set the modification type
			//
			m_addressModType = getModType(m_originalProfile.getCreditAddress(),
					m_modifiedProfile.getCreditAddress());

			if (ADDED.equals(m_addressModType)) {
				// set the profileId in the new object, as
				// clients may not set it on an Add
				m_modifiedProfile.getCreditAddress().setCreditProfileId(
						m_originalProfile.get_id());
			}

		}
	}

	/**
	 * Sets the isCreditValueModified instance variable. As CreditValue is a
	 * required field, the modification type is alway Mod, never Add or Delete.
	 */
	private void setCreditValueModified() {
	    if ( m_modifiedProfile.getCreditValue() != null
	         &&  m_modifiedProfile.getCreditValue().getCreditValueCode() != null
	         &&  m_modifiedProfile.getCreditValue().getCreditValueCode().trim().length() > 0 
	         && m_modifiedProfile.getMethod() != null
	         && m_modifiedProfile.getMethod().equals( "UM"  ) ) {
	        //
	        // this is to allow credit value to be modified 
	        // when unmerge is called so that credit assessment can be triggered for this
	        //
	        m_isCreditValueModified = true;
	    }
	    else if (m_modifiedProfile.getCreditValue() == null)
			m_isCreditValueModified = false;
		else if (m_modifiedProfile.getCreditValue() != null
				&& m_originalProfile.getCreditValue() != null
				&& m_originalProfile.getCreditValue().getLastUpdateTimestamp()
						.compareTo(
								m_modifiedProfile
										.getCreditValue().getLastUpdateTimestamp()) > 0)
			m_isCreditValueModified = false;
		else {
		    m_isCreditValueModified = !CreditMgtUtils.areEqual(
								       m_originalProfile.getCreditValue(), m_modifiedProfile
								       .getCreditValue());
		    if ( m_originalProfile.getCreditValue() == null ) {
		        if ( (m_modifiedProfile.getCreditValue().getCreditValueCode() == null
		             || m_modifiedProfile.getCreditValue().getCreditValueCode().trim().length() == 0 ) 
		             && m_modifiedProfile.getCreditValue().getFraudIndicatorCd() != null
		             && m_modifiedProfile.getCreditValue().getFraudIndicatorCd().trim().length() > 0 ) {
		            m_isOnlyFraudIndicatorModified = true;
		        }
		    }
		    else if ( CreditMgtUtils.equals( m_originalProfile.getCreditValue().getCreditValueCode(),
		            m_modifiedProfile.getCreditValue().getCreditValueCode() )
		         && (!CreditMgtUtils.equals( m_originalProfile.getCreditValue().getFraudIndicatorCd(),
		                 m_modifiedProfile.getCreditValue().getFraudIndicatorCd() ) ) ) {
		        m_isOnlyFraudIndicatorModified = true;
		    }
		}
	}

	/**
	 * Checks each field of personal info data to determine if anything has been
	 * changed. Personal info has a required field ( CreditCheckConsentCode ) so
	 * is always an update and never an Add or Delete.
	 */
	private void setPersonalInfoModified() {
		if (m_originalProfile.getBusinessLastUpdateTimestamp().compareTo(
				m_modifiedProfile.getBusinessLastUpdateTimestamp()) > 0)
			m_isPersonalInfoModified = false;
		else
			m_isPersonalInfoModified = !(CreditMgtUtils.areEqual(
					m_originalProfile.getResidencyCode(), m_modifiedProfile
							.getResidencyCode())
					&& CreditMgtUtils.areEqual(m_originalProfile
							.getUnderLegalCareCode(), m_modifiedProfile
							.getUnderLegalCareCode())
					&& CreditMgtUtils.areEqual(m_originalProfile
							.getPrimaryCreditCardCode(), m_modifiedProfile
							.getPrimaryCreditCardCode())
					&& CreditMgtUtils.areEqual(m_originalProfile
							.getSecondaryCreditCardCode(), m_modifiedProfile
							.getSecondaryCreditCardCode())
					&& CreditMgtUtils.areEqual(m_originalProfile
							.getEmploymentStatusCode(), m_modifiedProfile
							.getEmploymentStatusCode())
					&& CreditMgtUtils.areEqual(m_originalProfile
							.getBirthdate(), m_modifiedProfile
							.getBirthdate()) 
					&& CreditMgtUtils.areEqual(m_originalProfile
							.getCreditCheckConsentCode(),m_modifiedProfile
							.getCreditCheckConsentCode())
					&& CreditMgtUtils.areEqual( m_originalProfile
							.getApplicationProvinceCd(),m_modifiedProfile
							.getApplicationProvinceCd() )
					&& ( m_originalProfile.isBypassMatchIndicator() == m_modifiedProfile.isBypassMatchIndicator() ) );
		
		m_isCreditCheckConsentModified = !CreditMgtUtils
        .areEqual(m_originalProfile.getCreditCheckConsentCode(),
                m_modifiedProfile.getCreditCheckConsentCode());
		if ( m_originalProfile.getCreditCheckConsentCode() != null
		     && m_originalProfile.getCreditCheckConsentCode().equals( "Y" )
		     && m_modifiedProfile.getCreditCheckConsentCode() != null
		     && m_modifiedProfile.getCreditCheckConsentCode().equals( "N" ) ) {
		    m_isCreditCheckConsentModifiedFromYToN = true;
		}
	}
	
	private void setAttributesModified() {
    	m_isAttributesModified = false;
    	m_attributeModifiedList = new ArrayList<CreditAttribute>();
    	CreditAttribute creditAttribute = new CreditAttribute();
    	if (!(CreditMgtUtils.areEqual(m_originalProfile.getProvinceOfCurrentResidenceCd(), 
    			m_modifiedProfile.getProvinceOfCurrentResidenceCd())))
    	{
    		m_isAttributesModified = true;
    		creditAttribute.setCreditProfileId(m_creditProfileId);
    		creditAttribute.setAttributeCode(CreditAttribute.CURRENT_PROVINCE_RESIDENCY_CODE);
    		creditAttribute.setAttributeValue(m_modifiedProfile.getProvinceOfCurrentResidenceCd());
    		m_attributeModifiedList.add(creditAttribute);
    	}
    	// first assessment date will only be set once
    	if (m_originalProfile.getFirstAssessmentDate() == null &&
    		m_modifiedProfile.getFirstAssessmentDate() != null)
    	{
    		m_isAttributesModified = true;
    		creditAttribute.setCreditProfileId(m_creditProfileId);
    		creditAttribute.setAttributeCode(CreditAttribute.FIRST_ASSESSMENT_DATE);
    		creditAttribute.setAttributeValue(WlnPrflDMgtUtil.convertDateToString(m_modifiedProfile.getFirstAssessmentDate()));
    		m_attributeModifiedList.add(creditAttribute);
    	}
    	if (!(CreditMgtUtils.areEqual(m_originalProfile.getLatestAssessmentDate(), 
    			m_modifiedProfile.getLatestAssessmentDate())))
    	{
    		m_isAttributesModified = true;
    		creditAttribute.setCreditProfileId(m_creditProfileId);
    		creditAttribute.setAttributeCode(CreditAttribute.LATEST_ASSESSMENT_DATE);
    		creditAttribute.setAttributeValue(WlnPrflDMgtUtil.convertDateToString(m_modifiedProfile.getLatestAssessmentDate()));
    		m_attributeModifiedList.add(creditAttribute);
    	}
    	if (!(CreditMgtUtils.areEqual(m_originalProfile.getCreditValue().getCreditValueCode(), 
    			m_modifiedProfile.getCreditValue().getCreditValueCode())) &&
    			!(CreditMgtUtils.areEqual(m_originalProfile.getCreditValueEffectiveDate(), 
    			m_modifiedProfile.getCreditValueEffectiveDate())))
    	{
    		m_isAttributesModified = true;
    		creditAttribute.setCreditProfileId(m_creditProfileId);
    		creditAttribute.setAttributeCode(CreditAttribute.CREDIT_VALUE_EFFECTIVE_DATE);
    		creditAttribute.setAttributeValue(WlnPrflDMgtUtil.convertDateToString(m_modifiedProfile.getCreditValueEffectiveDate()));
    		m_attributeModifiedList.add(creditAttribute);
    	}
    	// FR631084, Report Indicator will only be set once.
    	// It will reflect whether the customer has ever had a report pulled.
    	if (!m_originalProfile.isCreditReportIndicator() && m_modifiedProfile.isCreditReportIndicator())
    	{
    		m_isAttributesModified = true;
    		creditAttribute.setCreditProfileId(m_creditProfileId);
    		creditAttribute.setAttributeCode(CreditAttribute.CREDIT_REPORT_INDICATOR);
    		creditAttribute.setAttributeValue(m_modifiedProfile.isCreditReportIndicator() ? "Y" : "N");
    		m_attributeModifiedList.add(creditAttribute);
    	}
    }

	/**
	 * Sets the isModified instance variable to true if any component of the
	 * credit profile has been changed.
	 */
	private void setModified() {
		if (m_isCreditValueModified || m_isPersonalInfoModified
				|| m_isAddressModified || m_isGuarantorModified
				|| m_idCardUpdates.isCardIdArrayModified()
				|| m_isAttributesModified
		    || m_isCommentPresent ) {
			m_isModified = true;
		}
	}

	/**
	 * Determines if update is an Addition, Deletion or Update
	 * 
	 * @param original
	 * @param modified
	 * @return modification type Add, Delete or Update
	 */
	private String getModType(Object original, Object modified) {
		String modType = null;

		if (original == null) {
			modType = CreditProfileUpdates.ADDED;
		}
		// else if(modified == null){
		// modType = CreditProfileUpdates.DELETED;
		// }
		else {
			modType = CreditProfileUpdates.MODIFIED;
		}

		return modType;
	}

	public boolean isModified() {
		return m_isModified;
	}

	public boolean isCreditValueModified() {
		return m_isCreditValueModified;
	}

     public boolean isCommentPresent() {
	return m_isCommentPresent;
    }

	public boolean isAddressModified() {
		return m_isAddressModified;
	}

	public boolean isPersonalInfoModified() {
		return m_isPersonalInfoModified;
	}
	
	public boolean isAttributesModified()
    {
    	return m_isAttributesModified;
    }

	public boolean hasCardIdsInOriginal() {
		return m_idCardUpdates.hasCardIdsInOriginal();
	}

	public boolean isCardIdArrayModified() {
		return m_idCardUpdates.isCardIdArrayModified();
	}
	
	public boolean isCreditCheckConsentModified() {
	    return m_isCreditCheckConsentModified;
	}
	
	public boolean isCreditCheckConsentModifiedFromYToN() {
	    return m_isCreditCheckConsentModifiedFromYToN;
	}

	/**
	 * Each element of the List contains a Map. The Map contains two keys:
	 * <ol>
	 * <li>ID_CARD_KEY which references the modified CreditIDCard</li>
	 * <li>MOD_TYPE_KEY which contains the ADDED, DELETED or MOD constant</li>
	 * </ol>
	 * 
	 * @return list of modifications
	 */
	public List getIdCardUpdates() {
		return m_idCardUpdates.getModifiedIdList();
	}

	public String getAddressModType() {
		return m_addressModType;
	}

	public String getGuarantorModType() {
		return m_guarantorModType;
	}

	/**
	 * @return Returns the creditProfileId.
	 */
	public long getCreditProfileId() {
		return m_creditProfileId;
	}
	
	public List<CreditAttribute> getAttributeModifiedList() {
		return m_attributeModifiedList;
	}
	
	public boolean isGuarantorModified()
    {
        return m_isGuarantorModified;
    }


    public boolean isGuarantorIdModified()
    {
        return m_isGuarantorIdModified;
    }

	/**
	 * @param creditAddressDao
	 *            The creditAddressDao to set.
	 */
	public void setCreditAddressDao(CreditAddressDao creditAddressDao) {
		m_creditAddressDao = creditAddressDao;
	}

	/**
	 * @param creditValueDao
	 *            The creditValueDao to set.
	 */
	public void setCreditValueDao(CreditValueDao creditValueDao) {
		m_creditValueDao = creditValueDao;
	}

    /**
     * @return Returns the m_isOnlyFraudIndicatorModified.
     */
    public boolean isOnlyFraudIndicatorModified()
    {
        return m_isOnlyFraudIndicatorModified;
    }

    /**
     * @param m_isOnlyFraudIndicatorModified The m_isOnlyFraudIndicatorModified to set.
     */
    public void setIsOnlyFraudIndicatorModified(
            boolean m_isOnlyFraudIndicatorModified)
    {
        this.m_isOnlyFraudIndicatorModified = m_isOnlyFraudIndicatorModified;
    }

}
