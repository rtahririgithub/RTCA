package com.telus.credit.wlnprflmatch.webservice.util;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.telus.credit.domain.CreditIDCard;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.credit.wlnprflmatch.domain.CreditIdentification;

/**
 * @author x158788
 *
 */

public class DataObjectUtil {

	/**
	 * 
	 * @param creditIdentification
	 * @param creditIDCardList
	 */
	public static void copyToCreditIDCards(	CreditIdentification creditIdentification,List<CreditIDCard> creditIDCardList) {
		CreditIDCard anIdCard;

		if (StringUtils.isNotBlank(creditIdentification.getSin())) {
			anIdCard = new CreditIDCard();
			anIdCard.setIdTypeCode(CreditIDCard.SIN_KEY);
			anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getSin()));
			creditIDCardList.add(anIdCard);
		}

		if (creditIdentification.getDriverLicense() != null
				&& StringUtils.isNotBlank(creditIdentification.getDriverLicense().getDriverLicenseNum())) {
			anIdCard = new CreditIDCard();
			anIdCard.setIdTypeCode(CreditIDCard.DRIVERS_LICENSE_KEY);
			anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getDriverLicense().getDriverLicenseNum()));
			if(StringUtils.isNotBlank(creditIdentification.getDriverLicense().getProvinceCd())){
			 anIdCard.setProvinceCode(creditIdentification.getDriverLicense().getProvinceCd());
			}
			creditIDCardList.add(anIdCard);
		}

		if (creditIdentification.getHealthCard() != null &&
				StringUtils.isNotBlank(creditIdentification.getHealthCard().getHealthCardNum())) {
				anIdCard = new CreditIDCard();
				anIdCard.setIdTypeCode(CreditIDCard.HEALTH_CARE_NUMBER_KEY);
				anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getHealthCard().getHealthCardNum()));
				if(StringUtils.isNotBlank(creditIdentification.getHealthCard().getProvinceCd())){
				  anIdCard.setProvinceCode(creditIdentification.getHealthCard().getProvinceCd());
				}
				creditIDCardList.add(anIdCard);
		}

		if (creditIdentification.getPassport() != null &&
				StringUtils.isNotBlank(creditIdentification.getPassport().getPassportNum()) ) {
				anIdCard = new CreditIDCard();
				anIdCard.setIdTypeCode(CreditIDCard.PASSPORT_NUMBER_KEY);
				anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getPassport().getPassportNum()));
				if(StringUtils.isNotBlank(creditIdentification.getPassport().getCountryCd())){
				anIdCard.setCountryCode(creditIdentification.getPassport().getCountryCd());
				}
				creditIDCardList.add(anIdCard);
		}

		if (creditIdentification.getProvincialIdCard() != null &&
				StringUtils.isNotBlank(creditIdentification.getProvincialIdCard().getProvincialIdNum())) {			
				anIdCard = new CreditIDCard();
				anIdCard.setIdTypeCode(CreditIDCard.PROVINCIAL_ID_KEY);
				anIdCard.setIdNumber(EncryptionUtil.encrypt(creditIdentification.getProvincialIdCard()
						.getProvincialIdNum()));
				if(StringUtils.isNotBlank(creditIdentification.getProvincialIdCard().getProvinceCd())){
					anIdCard.setProvinceCode(creditIdentification.getProvincialIdCard().getProvinceCd());
				}
				creditIDCardList.add(anIdCard);
			
		}
	}	
}