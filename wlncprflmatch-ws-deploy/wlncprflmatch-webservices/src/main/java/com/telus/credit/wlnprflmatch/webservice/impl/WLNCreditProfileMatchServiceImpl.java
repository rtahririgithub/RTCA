package com.telus.credit.wlnprflmatch.webservice.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.dao.CreditIDCardDao;
import com.telus.credit.dao.CreditProfileDao;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.service.dto.CreditProfileDto;
import com.telus.credit.service.dto.search.CreditMgtCustomerID;
import com.telus.credit.service.dto.search.CreditMgtCustomerIDs;
import com.telus.credit.wlnprflmatch.webservice.util.DataObjectUtil;
import com.telus.framework.crypto.EncryptionUtil;
import com.telus.framework.exception.ObjectNotFoundException;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilematchsvc_1.ServiceException;
import com.telus.wsdl.cmo.ordermgmt.wlncreditprofilematchsvc_1.WLNCreditProfileMatchSvcV10PortType;
import com.telus.credit.wlnprflmatch.domain.CheckCreditProfileResponse;
import com.telus.credit.wlnprflmatch.domain.CreditIdentification;
import com.telus.credit.wlnprflmatch.domain.CreditProfileVerificationResult;

public class WLNCreditProfileMatchServiceImpl implements WLNCreditProfileMatchSvcV10PortType {
	private static final Log log = LogFactory.getLog(WLNCreditProfileMatchServiceImpl.class);
	private CreditProfileDao m_creditProfileDao;
	private CreditIDCardDao m_creditIDCardDao;

	/**
	 * Constructor with external dependencies which will be wired by Spring.
	 * 
	 */
	public WLNCreditProfileMatchServiceImpl() {
	}

	/**
	 * Gets CreditProfiles that match given CreditProfile
	 * (by id cards)
	 * @param CreditIDCard []
	 * @return CreditProfileDto []
	 */
	@SuppressWarnings("unused")
	private CreditProfileDto[] getCreditProfilesByIdCards(CreditIDCard[] idCards) {
		CreditProfileDto[] creditProfileDtos = null;
		try {
			if (log.isDebugEnabled()) {
				log.debug("Inside getCreditProfilesByIdCards method: " + idCards.length);
			}
			if (idCards != null) {
				Set matchedCustomerIds = new HashSet();
				for (int i = 0; i < idCards.length; i++) {
					
					List<Integer> customerIdList = m_creditIDCardDao.getConsumerCustomerIdByMatchingCreditIdCardWithOutProv(idCards[i]);
					if (log.isDebugEnabled()) {
						log.debug("getCreditProfilesByIdCards customerIdList size: " + customerIdList.size());
					}
					for (Integer custID : customerIdList) {
						matchedCustomerIds.add(custID);
					}

				}
				creditProfileDtos = getMatchingCustomerCreditProfilesByCustomerIds(matchedCustomerIds);
			} else {
				creditProfileDtos = new CreditProfileDto[0];
			}
		} catch(Exception e){
			log.error("Exception in getCreditProfilesByIdCards :"+e.getMessage());
			log.error(e);
		}
		if (log.isDebugEnabled()) {
			log.debug("Enbd of getCreditProfilesByIdCards method");
		}
		return creditProfileDtos;
	}

	/**
     *Gets CreditProfiles that match CreditProfile of the given customer.
	 *
	 * @param matchedCustomerIds
	 * @return CreditProfileDto []
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private CreditProfileDto[] getMatchingCustomerCreditProfilesByCustomerIds(Set matchedCustomerIds) {
		if (log.isDebugEnabled()) {
			log.debug("Inside getMatchingCustomerCreditProfilesByCustomerIds method :" + matchedCustomerIds.size());
		}
		Set matchedCustomerIDsForAllCustomers = new HashSet();
		for (Iterator i = matchedCustomerIds.iterator(); i.hasNext();) {
			Integer cid = (Integer) i.next();
			if (log.isDebugEnabled()) {
				log.debug("getMatchingCustomerCreditProfilesByCustomerIds cid" + cid);
			}
			CreditMgtCustomerID customerID = new CreditMgtCustomerID(cid.intValue());
			CreditMgtCustomerIDs matchedCustomerIDsForCustomer = new CreditMgtCustomerIDs();
			if (!matchedCustomerIDsForCustomer.containsId(customerID.getId().intValue())) {
				matchedCustomerIDsForCustomer.add(customerID);
				findMatchingCustomerIds(matchedCustomerIDsForCustomer);
				matchedCustomerIDsForAllCustomers.addAll(matchedCustomerIDsForCustomer);
			}
		}

		CreditProfile creditProfile = null;
		List<CreditProfileDto> customerCreditProfiles = new ArrayList<CreditProfileDto>();
		for (Iterator i = matchedCustomerIDsForAllCustomers.iterator(); i.hasNext();) {
			CreditMgtCustomerID customerId = (CreditMgtCustomerID) i.next();
			try {
				creditProfile = m_creditProfileDao.getCreditProfile(customerId.getId());
			} catch (ObjectNotFoundException ex) {
				String errorMsg = "Failed to retrieve matched CreditProfile by Customer Id ["+customerId+"]";
				log.error(errorMsg.toString(), ex);

			}
			customerCreditProfiles.add(new CreditProfileDto(customerId.getId().intValue(), creditProfile));
		}
		if (log.isDebugEnabled()) {
			log.debug("end of  getMatchingCustomerCreditProfilesByCustomerIds method" + customerCreditProfiles.size());
		}
		return (CreditProfileDto[]) customerCreditProfiles.toArray(new CreditProfileDto[customerCreditProfiles.size()]);
	}

	/**
	 * <p>
	 * <b>Description: </b> This method searches recursively for all matching
	 * customer ids in the customer-credit profile mapping table.
	 * </p>
	 * <li>Finds "unprocessed" CustomerID objects in the CustomerIDs object.</li>
	 * <li>Calls {@link CreditProfileDao#getMatchingCustomerIds}for each
	 * unprocessed CustomerID.</li>*
	 * <li>Eliminates duplicate CustomerID objects.</li>
	 * <li>Call itself until all matched customerIds are found.</li>
	 * </ol>
	 * 
	 * @param matchedCustomerIDs
	 *            - encapsulates ids of matched customers.
	 */
	@SuppressWarnings("unchecked")
	private void findMatchingCustomerIds(CreditMgtCustomerIDs matchedCustomerIDs) {
		@SuppressWarnings("rawtypes")
		Set set = new HashSet();
		for (@SuppressWarnings("rawtypes")
		Iterator it = matchedCustomerIDs.iterator(); it.hasNext();) {
			CreditMgtCustomerID customerID = (CreditMgtCustomerID) it.next();
			if (!customerID.isProcessed()) {
				Integer[] customerIds = m_creditProfileDao.getMatchingCustomerIds(customerID.getId());
				int i = 0;
				while (i < customerIds.length) {
					int cid = customerIds[i].intValue();
					if (!matchedCustomerIDs.containsId(cid)) {
						CreditMgtCustomerID cust = new CreditMgtCustomerID(cid);
						set.add(cust);
					}
					i++;
				}
				i = 0;
				customerID.setProcessed(true);
			}
		}
		matchedCustomerIDs.addAll(set);
		if (matchedCustomerIDs.containsUnprocessedId()) {
			findMatchingCustomerIds(matchedCustomerIDs);
		}
	}

	/**
	 * Validate AuditInfo fields.
	 * User id , Original application id and time-stamp fields are mandatory fields 
	 * @param auditInfo
	 * @throws PolicyException
	 * 
	 */
	private boolean validateAuditInfo(AuditInfo auditInfo) {
		if ((auditInfo != null) && StringUtils.isNotBlank(auditInfo.getUserId())
				&& StringUtils.isNotBlank((auditInfo.getOriginatorApplicationId()))) {
			return true;
		} else {
			log.error(WLNCreditProfileMatchServiceExceptionCodes.AUDIT_INFO_VALIDATION_ERROR_MSG);
			return false;
		}

	}

	/**
	 * Validate Credit Identification fields
	 * Either DL or SIN or passport or Heath card or Province Heath card should present in request
	 * @param creditIdentification
	 * @return
	 */
	@SuppressWarnings("null")
	private boolean validateCreditIdentificationRequest(CreditIdentification creditIdentification) {
		boolean isValidRequest = false;
		if (creditIdentification != null
				&& ((creditIdentification.getDriverLicense() != null && StringUtils.isNotBlank(creditIdentification.getDriverLicense().getDriverLicenseNum()))
						|| (creditIdentification.getHealthCard() != null && StringUtils.isNotBlank(creditIdentification.getHealthCard().getHealthCardNum()))
						|| (creditIdentification.getPassport() != null && StringUtils.isNotBlank(creditIdentification.getPassport().getPassportNum()))
						|| (creditIdentification.getProvincialIdCard() != null && StringUtils.isNotBlank(creditIdentification.getProvincialIdCard().getProvincialIdNum())) 
						|| (creditIdentification.getSin() != null && StringUtils.isNotBlank(creditIdentification.getSin())))) {
			isValidRequest = true;
		}else{
			log.error(WLNCreditProfileMatchServiceExceptionCodes.CREDIT_INFO_VALIDATION_ERROR_MSG);
		}
		if (log.isDebugEnabled()) {
		log.debug("Result  validateCreditIdentificationRequest function" + isValidRequest);
		}
		return isValidRequest;
	}

	/**
	 * <p>
	 * <b>Description </b> Sets CreditProfileDao.
	 * </p>
	 * 
	 * @param creditProfileDao
	 *            that implement CreditProfileDao interface.
	 */

	public void setCreditProfileDao(CreditProfileDao creditProfileDao) {
		m_creditProfileDao = creditProfileDao;
	}

	/**
	 * <p>
	 * <b>Description </b> Sets CreditIDCardDao.
	 * </p>
	 * 
	 * @param creditIDCardDao
	 *            that implement CreditIDCardDao interface.
	 */

	public void setCreditIDCardDao(CreditIDCardDao creditIDCardDao) {
		m_creditIDCardDao = creditIDCardDao;
	}

	/**
	 * 
	 * <p>
	 * <b>Description </b> Ping Operation
	 * </p>
	 * 
	 * @param
	 * @return
	 */
	@Override
	public String ping() throws ServiceException {
		return WLNCreditProfileMatchServiceExceptionCodes.PING_SUCCESS_MSG;
	}
	

	@Override
	public CheckCreditProfileResponse checkCreditProfileByCreditId(CreditIdentification creditIdentification,
			com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.AuditInfo auditInfo)
			throws ServiceException {

		if (log.isDebugEnabled()) {
			log.debug("Enter in checkCreditProfileByCreditId function");
		}
		CheckCreditProfileResponse checkCreditProfileResponse = new CheckCreditProfileResponse();
		List<CreditProfileVerificationResult> verifyCreditProfileResultList = new ArrayList<CreditProfileVerificationResult>();
		//decryptString();
		try {
			List<CreditIDCard> creditIDCardList = new ArrayList<CreditIDCard>();

			if (validateCreditIdentificationRequest(creditIdentification) && validateAuditInfo(auditInfo)) {
				DataObjectUtil.copyToCreditIDCards(creditIdentification, creditIDCardList);
				CreditProfileDto[] creditProfileDtos = getCreditProfilesByIdCards(creditIDCardList
						.toArray(new CreditIDCard[0]));
				if (log.isDebugEnabled()){
					log.debug("creditProfileDtos length : " + creditProfileDtos.length);
				}
				long totalCreditAccounts = 0;
				if (creditProfileDtos != null && creditProfileDtos.length > 0) {
					for (int i = 0; i < creditProfileDtos.length; i++) {
						CreditProfileVerificationResult creditProfileVerificationResult = new CreditProfileVerificationResult();
						if (creditProfileDtos[i].getCustomerId() != 0 &&
							creditProfileDtos[i].getCreditProfile().get_id() != 0) {
							creditProfileVerificationResult.setCustomerId((new Long(String.valueOf(creditProfileDtos[i].getCustomerId()))));
							creditProfileVerificationResult.setCreditProfileId(creditProfileDtos[i].getCreditProfile().get_id());
							checkCreditProfileResponse.getCheckCreditProfileResultList().add(creditProfileVerificationResult);
							totalCreditAccounts++;
						}
					}
					//checkCreditProfileResponse.getCheckCreditProfileResultList.add(verifyCreditProfileResultList);
					checkCreditProfileResponse.setTotalCreditAccountNum(totalCreditAccounts);
					if (log.isDebugEnabled()){
						log.debug("setTotalCreditAccountNum: " + totalCreditAccounts);
						log.debug("setCheckCreditProfileResultList: " + verifyCreditProfileResultList.size());
					}
				} else {
					checkCreditProfileResponse.setTotalCreditAccountNum((long) 0);
					//checkCreditProfileResponse.getCheckCreditProfileResultList.add(verifyCreditProfileResultList);
				}
			} else {
				Message errorMessage = new Message();
				errorMessage.setMessage(WLNCreditProfileMatchServiceExceptionCodes.INCORRECT_REQUEST_FORMAT_MSG);
				errorMessage.setLocale(WLNCreditProfileMatchServiceExceptionCodes.EN_LOCAL);
				createErrorResponse(WLNCreditProfileMatchServiceExceptionCodes.INCORRECT_REQUEST_FORMAT, errorMessage,
						checkCreditProfileResponse);
			}

		} catch (Exception e) {
			log.error(e);
			Message errorMessage = new Message();
			errorMessage.setMessage(e.getMessage());
			errorMessage.setLocale(WLNCreditProfileMatchServiceExceptionCodes.EN_LOCAL);
			createErrorResponse(WLNCreditProfileMatchServiceExceptionCodes.UNKNOWN_EXCEPTION, errorMessage,	checkCreditProfileResponse);
		}
		if (log.isDebugEnabled()) {
			log.debug("Exit checkCreditProfileByCreditId.");
		}
		checkCreditProfileResponse.setDateTimeStamp(Calendar.getInstance().getTime());
		return checkCreditProfileResponse;

	}

	/**
	 * Generate Error response based on the input parameters 
	 * @param error_id
	 * @param errorMessage
	 * @param checkCreditProfileResponse
	 * @return
	 */

	@SuppressWarnings("unused")
	private CheckCreditProfileResponse createErrorResponse(String error_id,
			com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message errorMessage,
			CheckCreditProfileResponse checkCreditProfileResponse) {
		checkCreditProfileResponse.setErrorCode(error_id);
		checkCreditProfileResponse.setMessageType("ERROR");
		checkCreditProfileResponse.setTotalCreditAccountNum((long) 0);
		checkCreditProfileResponse.getMessageList().add(errorMessage);
		return checkCreditProfileResponse;
	}

	
}
