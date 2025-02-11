/**
 * 
 */
package com.telus.credit.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.service.dto.update.CreditProfileUpdates;
import com.telus.framework.crypto.EncryptionUtil;

/**
 * @author x158788
 *
 */
public class CreateJSONMsg {

	protected static final String ALTIDVALUE = "altIdVal";
	protected static final String ALTIDTYPE = "altIdType";
	protected static final String CREATOR = "creator";
	protected static final String CREATE = "CREATE";
	protected static final String UPDATEBY = "updatedby";
	protected static final String CMDBID = "9343";
	protected static final String SOURCEKEY = "sourceKey";
	protected static final String CREATEDATE = "createDate";
	protected static final String UPDATEDATE = "updateDate";
	protected static final String PROVINCE = "province";
	protected static final String COUNTRY = "country";
	protected static final String ITEMS = "items";
	protected static final String CUSTOMERID = "customerID";
	protected static final String UPDATEDDOB = "UpdatedDOB";
	protected static final String CUST_CREDIT_IDENTIFIER = "CustomerCreditIdentifiers";
	protected static final String CUST_CREDIT_DETAILS = "CustomerCreditDetails";
	protected static final String DOB = "birthDate";
	private static Log slog = LogFactory.getLog(CreateJSONMsg.class);
	/**
	 * Create a JSON message for credit ID's 
	 * @param creditProfile
	 * @param customerId
	 * @param userID
	 * @param typeCode - CREATE / UPDATE
	 * @return
	 * @throws JSONException
	 */
	public static boolean isCreditIdWithZeroValue(String creditIdValue) {
		boolean result = false;
		slog.debug("Checking for zeros in = " + creditIdValue);
		System.out.println("Checking for zeros in = " + creditIdValue);
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
		slog.debug("Result = " + result);
		System.out.println("Result = " + result);
		return result;
	}
	public JSONObject createJSONMessageForCreditIds(CreditProfile creditProfile, long customerId, String userID,
			String typeCode) throws JSONException {

		CreditIDCard[] creditcardIds = creditProfile.getCreditIDCards();
		JSONObject customerPersonIdentifiers = new JSONObject();
		slog.debug("createJSONMessageForCreditIds ->"+creditcardIds.length);
//		System.out.println("createJSONMessageForCreditIds ->"+creditcardIds.length);
		
	/*	for(int i=0; i<creditcardIds.length; i++) {
			System.out.println(creditcardIds[i].getIdTypeCode());
			System.out.println(creditcardIds[i].getIdNumber());
		}*/
		
		if (creditcardIds != null && creditcardIds.length>0) {
			
			customerPersonIdentifiers.put(CREATE.equals(typeCode) ? CREATEDATE : UPDATEDATE, Calendar.getInstance().getTime());
			customerPersonIdentifiers.put(CREATE.equals(typeCode) ? CREATOR : UPDATEBY, userID);
			for (CreditIDCard creditIDCard : creditcardIds) {
				
				if (CreditIDCard.SIN_KEY.equalsIgnoreCase(creditIDCard.getIdTypeCode()) && !isCreditIdWithZeroValue(creditIDCard.getIdNumber())) {
					HashMap<String, String> sin = new HashMap<String, String>();
					sin.put(ALTIDVALUE, EncryptionUtil.decrypt(creditIDCard.getIdNumber()));
					sin.put(ALTIDTYPE, CreditIDCard.SIN_KEY);
					sin.put(SOURCEKEY, customerId + "_" + CreditIDCard.SIN_KEY);
					customerPersonIdentifiers.append(ITEMS, sin);

				} else if (CreditIDCard.DRIVERS_LICENSE_KEY.equalsIgnoreCase(creditIDCard.getIdTypeCode())&& !isCreditIdWithZeroValue(creditIDCard.getIdNumber())) {
					HashMap<String, String> dl = new HashMap<String, String>();
//					System.out.println("Encrypted = " + creditIDCard.getIdNumber());
	//				System.out.println("Decrypted = " + EncryptionUtil.decrypt(creditIDCard.getIdNumber()));
					dl.put(ALTIDVALUE, EncryptionUtil.decrypt(creditIDCard.getIdNumber()));
					dl.put(ALTIDTYPE, CreditIDCard.DRIVERS_LICENSE_KEY);
					dl.put(PROVINCE, creditIDCard.getProvinceCode());// create
					dl.put(SOURCEKEY, customerId + "_" + CreditIDCard.DRIVERS_LICENSE_KEY);
					customerPersonIdentifiers.append(ITEMS, dl);

				} else if (CreditIDCard.HEALTH_CARE_NUMBER_KEY.equalsIgnoreCase(creditIDCard.getIdTypeCode())&& !isCreditIdWithZeroValue(creditIDCard.getIdNumber())) {
					HashMap<String, String> healthCard = new HashMap<String, String>();
					healthCard.put(ALTIDVALUE,  EncryptionUtil.decrypt(creditIDCard.getIdNumber()));
					healthCard.put(ALTIDTYPE, CreditIDCard.HEALTH_CARE_NUMBER_KEY);
					healthCard.put(PROVINCE, creditIDCard.getProvinceCode());
					healthCard.put(SOURCEKEY, customerId + "_" + CreditIDCard.HEALTH_CARE_NUMBER_KEY);
					customerPersonIdentifiers.append(ITEMS, healthCard);

				} else if (CreditIDCard.PASSPORT_NUMBER_KEY.equalsIgnoreCase(creditIDCard.getIdTypeCode())&& !isCreditIdWithZeroValue(creditIDCard.getIdNumber())) {
					HashMap<String, String> passport = new HashMap<String, String>();
					passport.put(ALTIDVALUE, EncryptionUtil.decrypt(creditIDCard.getIdNumber()));
					passport.put(ALTIDTYPE, CreditIDCard.PASSPORT_NUMBER_KEY);
					passport.put(COUNTRY, creditIDCard.getCountryCode());
					passport.put(SOURCEKEY, customerId + "_" + CreditIDCard.PASSPORT_NUMBER_KEY);
					customerPersonIdentifiers.append(ITEMS, passport);

				} else if (CreditIDCard.PROVINCIAL_ID_KEY.equalsIgnoreCase(creditIDCard.getIdTypeCode())&& !isCreditIdWithZeroValue(creditIDCard.getIdNumber())) {
					HashMap<String, String> provienceCode = new HashMap<String, String>();
					provienceCode.put(ALTIDVALUE,  EncryptionUtil.decrypt(creditIDCard.getIdNumber()));
					provienceCode.put(ALTIDTYPE, CreditIDCard.PROVINCIAL_ID_KEY);
					provienceCode.put(PROVINCE, creditIDCard.getProvinceCode());
					provienceCode.put(SOURCEKEY, customerId + "_" + CreditIDCard.PROVINCIAL_ID_KEY);
					customerPersonIdentifiers.append(ITEMS, provienceCode);

				}

			}

		}

		JSONObject customerNode = new JSONObject();
		customerNode.append(CUSTOMERID, customerId);
		customerNode.append(CUST_CREDIT_IDENTIFIER, customerPersonIdentifiers);

		JSONObject CustomerCreditDetails = new JSONObject();
		CustomerCreditDetails.append(CUST_CREDIT_DETAILS, customerNode);
        if(CustomerCreditDetails != null){
		  slog.debug("createJSONMessageForCreditIds ->CustomerCreditDetails->"+CustomerCreditDetails.toString());
//		  System.out.println("createJSONMessageForCreditIds ->CustomerCreditDetails->"+CustomerCreditDetails.toString());
        }
		return CustomerCreditDetails;
	}

	/**
	 * 
	 * @param customerId
	 * @param dateObirth
	 * @return
	 * @throws JSONException
	 */

	public JSONObject createJSONMessageForDOB(long customerId, Date dateObirth, String userID)  {

		JSONObject customerNode = null;
		try {
			slog.debug("createJSONMessageForDOB: dateObirth ->"+dateObirth);
			JSONObject dob = new JSONObject();
			dob.append(DOB, dateObirth);

			customerNode = new JSONObject();
			customerNode.append(CUSTOMERID, customerId);
			customerNode.append(UPDATEBY, userID);
			customerNode.append(UPDATEDATE, Calendar.getInstance().getTime());
			customerNode.append(UPDATEDDOB, dob);
			if(customerNode != null)
			slog.debug("createJSONMessageForDOB ->createJSONMessageForDOB->"+customerNode.toString());
		} catch (Throwable e) {
			slog.error(e);
		}
		
		return customerNode;

	}

}
