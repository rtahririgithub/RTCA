package com.telus.credit.wlnprfldmgt.webservice.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.telus.credit.dao.CreditIDCardDao;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.util.CreditMgtUtils;
import com.telus.framework.crypto.EncryptionUtil;

public class IdCardUpdates extends
		com.telus.credit.service.dto.update.IdCardUpdates {
	private CreditIDCard[] m_originalIdsArray;
	private CreditIDCard[] m_modifiedIdsArray;

	private List m_modifiedIdList;
	private boolean m_hasCardIdsInOriginal;
	private boolean m_isCardIdArrayModified;

	private boolean m_isSinModified;
	private boolean m_isDriversLicenceModified;
	private boolean m_isHealthCardModified;
	private boolean m_isPassportModified;
	private boolean m_isProvincialIdModified;

	private CreditIDCardDao m_creditIDCardDao;
	private CreditProfile m_modifiedProfile;

	/**
	 * constructor
	 */
	public IdCardUpdates(CreditProfile originalProfile,
			CreditProfile modifiedProfile) {
		super(originalProfile, modifiedProfile);
		m_modifiedProfile = modifiedProfile;
		m_originalIdsArray = originalProfile.getCreditIDCards();
		m_modifiedIdsArray = modifiedProfile.getCreditIDCards();
		setHasCardIdsInOriginal();
		setCardIdArrayModified();
	}

	/**
	 * Sets m_hasCardIdArray to true if the original profile has card ids. If
	 * there were none in original profile we don't have to go through the
	 * unlinking process.
	 * 
	 */
	private void setHasCardIdsInOriginal() {
		if (m_originalIdsArray != null && m_originalIdsArray.length > 0) {
			m_hasCardIdsInOriginal = true;
		}
	}

	/**
	 * Compare the ID cards in original vs modified profile
	 */
	private void setCardIdArrayModified() {

		// TODO can I use Array.equals here?
		// were none and are none

		if ( m_modifiedIdsArray == null || m_modifiedIdsArray.length == 0) {

			m_isCardIdArrayModified = false;

		} else if (m_modifiedIdsArray != null && m_modifiedIdsArray.length > 0) {
			if (m_hasCardIdsInOriginal) {

				// m_isCardIdArrayModified = isModified();
				m_isCardIdArrayModified = isCardTypeUpdated();

			} else {
				// no cards in original but now there are some
				//
				m_isCardIdArrayModified = true;
				loadModifiedList(CreditProfileUpdates.ADDED);
			}
		}
	}

	/**
	 * Compare each type of ID card for update, SIN, DL, etc
	 * 
	 * @return true if there are modifications
	 */
	private boolean isCardTypeUpdated() {
		Map originalsMap = new HashMap();
		Map modsMap = new HashMap();
		loadCardIdMaps(originalsMap, modsMap);

		CreditIDCard original = null;
		CreditIDCard modified = null;

		original = (CreditIDCard) originalsMap.get(CreditIDCard.SIN_KEY);
		modified = (CreditIDCard) modsMap.get(CreditIDCard.SIN_KEY);
		if (modified == null)
			m_isSinModified = false;
		else if (original != null
				&& original.getLastUpdateTimestamp().compareTo(
						modified.getLastUpdateTimestamp()) > 0)
			m_isSinModified = false;
		else
			m_isSinModified = doCompareAndLoadIdCard(original, modified);

		original = (CreditIDCard) originalsMap
				.get(CreditIDCard.DRIVERS_LICENSE_KEY);
		modified = (CreditIDCard) modsMap.get(CreditIDCard.DRIVERS_LICENSE_KEY);
		if (modified == null)
			m_isDriversLicenceModified = false;
		else if (original != null
				&& original.getLastUpdateTimestamp().compareTo(
						modified.getLastUpdateTimestamp()) > 0)
			m_isDriversLicenceModified = false;
		else
			m_isDriversLicenceModified = doCompareAndLoadIdCard(original,
					modified);

		original = (CreditIDCard) originalsMap
				.get(CreditIDCard.HEALTH_CARE_NUMBER_KEY);
		modified = (CreditIDCard) modsMap
				.get(CreditIDCard.HEALTH_CARE_NUMBER_KEY);
		if (modified == null)
			m_isHealthCardModified = false;
		else if (original != null
				&& original.getLastUpdateTimestamp().compareTo(
						modified.getLastUpdateTimestamp()) > 0)
			m_isHealthCardModified = false;
		else
			m_isHealthCardModified = doCompareAndLoadIdCard(original, modified);

		original = (CreditIDCard) originalsMap
				.get(CreditIDCard.PASSPORT_NUMBER_KEY);
		modified = (CreditIDCard) modsMap.get(CreditIDCard.PASSPORT_NUMBER_KEY);
		if (modified == null)
			m_isPassportModified = false;
		else if (original != null
				&& original.getLastUpdateTimestamp().compareTo(
						modified.getLastUpdateTimestamp()) > 0)
			m_isPassportModified = false;
		else
			m_isPassportModified = doCompareAndLoadIdCard(original, modified);

		original = (CreditIDCard) originalsMap
				.get(CreditIDCard.PROVINCIAL_ID_KEY);
		modified = (CreditIDCard) modsMap.get(CreditIDCard.PROVINCIAL_ID_KEY);
		if (modified == null)
			m_isProvincialIdModified = false;
		else if (original != null
				&& original.getLastUpdateTimestamp().compareTo(
						modified.getLastUpdateTimestamp()) > 0)
			m_isProvincialIdModified = false;
		else
			m_isProvincialIdModified = doCompareAndLoadIdCard(original,
					modified);

		return m_isSinModified || m_isDriversLicenceModified
				|| m_isHealthCardModified || m_isPassportModified
				|| m_isProvincialIdModified;

	}

	/**
	 * Creates HashMaps of Original and Modified CreditIDCards so they can be
	 * comapred by type. Key to maps is IdType (eg, SIN, DL).
	 * 
	 * @param originals
	 *            map of CreditIDCards in original CreditProfile
	 * @param mods
	 *            map of CreditIDCards in modified CreditProfile
	 */
	private void loadCardIdMaps(Map originals, Map mods) {

		for (int i = 0; i < m_originalIdsArray.length; i++) {

			originals.put(m_originalIdsArray[i].getIdTypeCode(),
					m_originalIdsArray[i]);
		}

		for (int i = 0; i < m_modifiedIdsArray.length; i++) {

			mods.put(m_modifiedIdsArray[i].getIdTypeCode(),
					m_modifiedIdsArray[i]);
		}

	}

	/**
	 * Does the comparison of CreditIDCards to see if like types are equal. If
	 * not equal, then determines if an Add, Delete or Update and adds it to a
	 * list of modified IdCards.
	 * 
	 * @param modified
	 * @param original
	 * @return true if original differs from modified
	 */
	private boolean doCompareAndLoadIdCard(CreditIDCard original,
			CreditIDCard modified) {

		boolean isModified = false;

		if (modified == null)
			isModified = false;
		else
			isModified = !CreditMgtUtils.areEqual(original, modified);

		if (isModified) {
			boolean expireCreditId = isCreditIdWithZeroValue(modified.getIdNumber());
			if (original == null) {
				if ( !expireCreditId ) {
					loadModifiedList(modified, CreditProfileUpdates.ADDED);
				}
				else {
					isModified = false;
				}
			} else {
				// an update
				loadModifiedList(modified, (expireCreditId? CreditProfileUpdates.DELETED : CreditProfileUpdates.MODIFIED) );
			}
		}

		return isModified;
	}
	
	public static boolean isCreditIdWithZeroValue(String creditIdValue) {
		boolean result = false;
		if ( creditIdValue != null && creditIdValue.length() > 0 ) {
			String decryptedId = EncryptionUtil.decrypt(creditIdValue);
			try {
				int decryptedIdValue = Integer.parseInt(decryptedId);
				if (decryptedIdValue==0) {
					result = true;
				}
			}
			catch (NumberFormatException ne) {
			}
		}
		return result;
	}

	/**
	 * Loads a list of modified IdCards. Modified IdCard is loaded if is an Add
	 * or Update, the original IdCard is loaded if is a Delete. Each list
	 * element is a HashMap holding the IdCard and the type of modification.
	 * 
	 * @param modType
	 */
	private void loadModifiedList(String modType) {

		if (CreditProfileUpdates.ADDED.equals(modType)) {
			for (int i = 0; i < m_modifiedIdsArray.length; i++) {
				if ( m_modifiedIdsArray[i] != null
					 && (!isCreditIdWithZeroValue(m_modifiedIdsArray[i].getIdNumber())) ) {
					loadModifiedList(m_modifiedIdsArray[i], modType);
				}
			}
		} else {
			// is deletion
			for (int i = 0; i < m_originalIdsArray.length; i++) {
				loadModifiedList(m_originalIdsArray[i], modType);
			}
		}

	}

	/**
	 * Creates the modifiedList array element - a HashMap holding the IdCard and
	 * the modification type - and loads it in to List.
	 * 
	 * @param modifiedCard
	 * @param modType
	 */
	private void loadModifiedList(CreditIDCard modifiedCard, String modType) {
		Map cardIdMod = new HashMap();

		if (m_modifiedIdList == null) {
			m_modifiedIdList = new ArrayList();
		}

		cardIdMod.put(CreditProfileUpdates.ID_CARD_KEY, modifiedCard);
		cardIdMod.put(CreditProfileUpdates.MOD_TYPE_KEY, modType);

		m_modifiedIdList.add(cardIdMod);
	}

	/**
	 * 
	 * @return true if any CreditIDCards have been modified
	 */
	public boolean isCardIdArrayModified() {
		return m_isCardIdArrayModified;
	}

	/**
	 * 
	 * @return true if there were CreditIDCards in original CreditProfile
	 */
	public boolean hasCardIdsInOriginal() {
		return m_hasCardIdsInOriginal;
	}

	/**
	 * @return Returns the isDriversLicenceModified.
	 */
	public boolean isDriversLicenceModified() {
		return m_isDriversLicenceModified;
	}

	/**
	 * @return Returns the isHealthCardModified.
	 */
	public boolean isHealthCardModified() {
		return m_isHealthCardModified;
	}

	/**
	 * @return Returns the isPassportModified.
	 */
	public boolean isPassportModified() {
		return m_isPassportModified;
	}

	/**
	 * @return Returns the isProvincialIdModified.
	 */
	public boolean isProvincialIdModified() {
		return m_isProvincialIdModified;
	}

	/**
	 * @return Returns the isSinModified.
	 */
	public boolean isSinModified() {
		return m_isSinModified;
	}

	/**
	 * @return Returns the list of modified CreditIDCards.
	 */
	public List getModifiedIdList() {
		return m_modifiedIdList;
	}

	/**
	 * @param creditIDCardDao
	 *            The creditIDCardDao to set.
	 */
	public void setCreditIDCardDao(CreditIDCardDao creditIDCardDao) {
		m_creditIDCardDao = creditIDCardDao;
	}
}
