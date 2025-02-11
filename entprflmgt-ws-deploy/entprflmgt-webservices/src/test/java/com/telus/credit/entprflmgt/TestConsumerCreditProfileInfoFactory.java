package com.telus.credit.entprflmgt;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import com.telus.credit.customer.domain.common.CreditCard;
import com.telus.credit.customer.domain.common.ProvinceCode;
import com.telus.credit.entprflmgt.domain.ConsumerCreditIdentification;
import com.telus.credit.entprflmgt.domain.ConsumerCreditProfileInfo;
import com.telus.credit.entprflmgt.domain.Identification;
import com.telus.credit.wlnprfldmgt.domain.CreditAddress;
import com.telus.credit.wlnprfldmgt.domain.CreditCardCode;
import com.telus.credit.wlnprfldmgt.domain.DriverLicense;
import com.telus.credit.wlnprfldmgt.domain.PersonalInfo;

public final class TestConsumerCreditProfileInfoFactory {
	private TestConsumerCreditProfileInfoFactory(){
	}
	
	public static ConsumerCreditProfileInfo newInstance(){
		//identification
		Identification id = new Identification();
		id.setCustomerId(123l);
		
		// credit card code
		CreditCardCode ccc = new CreditCardCode();
		ccc.setPrimaryCreditCardCd("VISA");
		ccc.setSecondaryCreditCardCd("MASTER CARD");
		
		// credit address
		CreditAddress creditAddress = new CreditAddress();
		creditAddress.setAddressLineOne("220 Yonge st");
		creditAddress.setCityName("Toronto");
		creditAddress.setCountryCd("CA");
		creditAddress.setCreditAddressTypeCd("RESIDENTIAL");
		creditAddress.setPostalCd("M5B 2L7");
		creditAddress.setProvinceCd("ON");
		
		
		// credit id
		ConsumerCreditIdentification credId = new ConsumerCreditIdentification();
		DriverLicense dl = new DriverLicense();
		dl.setDriverLicenseNum("encryptedValueHere");
		dl.setProvinceCd("BC");
		
		CreditCard cc = new CreditCard();
		cc.setToken("encryptedValueHere");
		
		credId.setDriverLicense(dl);
		credId.setSin("encryptedValueHere");
		credId.setCreditCardToken(cc);
		
		
		
		PersonalInfo personalInfo = new PersonalInfo();
		personalInfo.setCreditCheckConsentCd("Y");
		personalInfo.setEmploymentStatusCd("IMP");
		
			personalInfo.setBirthDate(new Date());
		
		
		// Consumer info object
		ConsumerCreditProfileInfo consumerInfo = new ConsumerCreditProfileInfo();
		consumerInfo.setIdentification(id);
		consumerInfo.setCreditCardCode(ccc);
		consumerInfo.setCreditAddress(creditAddress);
		consumerInfo.setCreditIdentification(credId);
		consumerInfo.setCreditValueCd("E");
		consumerInfo.setPersonalInfo(personalInfo);
		
		return consumerInfo;
	}

}
