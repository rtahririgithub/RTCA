package com.telus.credit.wlnprfldmgt.webservice.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import com.telus.credit.domain.CreditAddress;
import com.telus.credit.domain.CreditIDCard;
import com.telus.credit.domain.CreditProfile;
import com.telus.credit.domain.CreditValue;
import com.telus.credit.domain.CustomerGuarantor;
import com.telus.credit.domain.ProductCategory;
import com.telus.credit.domain.ProductCategoryQualification;
import com.telus.credit.wlnprfldmgt.domain.CreditProfileData;
import com.telus.erm.referenceods.domain.ReferenceMessageDecode;
import com.telus.erm.refpds.access.client.ReferencePdsAccess;
import com.telus.framework.math.Money;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyException;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceException;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v6.AuditInfo;

public class WlnPrflDMgtUtil {
	
	public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static CreditProfile mapSchemaObjToDomain( CreditProfileData  xsdObj , 
	        String creditValueCd, String fraudIndicatorCd, AuditInfo auditInfo)
	{
		Timestamp requestTimeStamp = new Timestamp(auditInfo.getTimestamp().getTime());
		if ( xsdObj == null)
			return null;
		else
		{
			CreditProfile crdPrfl = new CreditProfile();
			if ( xsdObj.getPersonalInfo() != null){
				crdPrfl.setBirthdate( xsdObj.getPersonalInfo().getBirthDate() );
				crdPrfl.setEmploymentStatusCode( xsdObj.getPersonalInfo().getEmploymentStatusCd() );
				crdPrfl.setCreditCheckConsentCode( xsdObj.getPersonalInfo().getCreditCheckConsentCd());
				crdPrfl.setResidencyCode( xsdObj.getPersonalInfo().getResidencyCd());
				crdPrfl.setUnderLegalCareCode( xsdObj.getPersonalInfo().getUnderLegalCareCd());
				crdPrfl.setProvinceOfCurrentResidenceCd(xsdObj.getPersonalInfo().getProvinceOfCurrentResidenceCd());
			}
			if ( xsdObj.getCreditCardCd() !=null ){
				crdPrfl.setPrimaryCreditCardCode( xsdObj.getCreditCardCd().getPrimaryCreditCardCd() );
				crdPrfl.setSecondaryCreditCardCode( xsdObj.getCreditCardCd().getSecondaryCreditCardCd());
				}
		
			crdPrfl.setBusinessLastUpdateTimestamp(requestTimeStamp);
			
			if ( xsdObj.getCreditIdentification()!=null && xsdObj.getCreditIdentification().getDriverLicense()!=null)
			{
				crdPrfl.setCreditIDCard( new CreditIDCard());
				CreditIDCard currentCard = crdPrfl.getCreditIDCards()[ crdPrfl.getCreditIDCards().length -1 ];
				currentCard.setIdNumber( xsdObj.getCreditIdentification().getDriverLicense().getDriverLicenseNum());
				currentCard.setProvinceCode( xsdObj.getCreditIdentification().getDriverLicense().getProvinceCd());
				currentCard.setIdTypeCode( CreditIDCard.DRIVERS_LICENSE_KEY );
				currentCard.setLastUpdateTimestamp(requestTimeStamp);
			}
			
			if ( xsdObj.getCreditIdentification()!=null && xsdObj.getCreditIdentification().getHealthCard()!=null)
			{
				crdPrfl.setCreditIDCard( new CreditIDCard());
				CreditIDCard currentCard = crdPrfl.getCreditIDCards()[ crdPrfl.getCreditIDCards().length -1 ];
				currentCard.setIdNumber( xsdObj.getCreditIdentification().getHealthCard().getHealthCardNum());
				currentCard.setProvinceCode( xsdObj.getCreditIdentification().getHealthCard().getProvinceCd());
				currentCard.setIdTypeCode( CreditIDCard.HEALTH_CARE_NUMBER_KEY );
				currentCard.setLastUpdateTimestamp(requestTimeStamp);
			}

			if ( xsdObj.getCreditIdentification()!=null && xsdObj.getCreditIdentification().getPassport()!=null)
			{
				crdPrfl.setCreditIDCard( new CreditIDCard());
				CreditIDCard currentCard = crdPrfl.getCreditIDCards()[ crdPrfl.getCreditIDCards().length -1 ];
				currentCard.setIdNumber( xsdObj.getCreditIdentification().getPassport().getPassportNum());
				currentCard.setIdTypeCode( CreditIDCard.PASSPORT_NUMBER_KEY );
				currentCard.setLastUpdateTimestamp(requestTimeStamp);
				currentCard.setCountryCode( xsdObj.getCreditIdentification().getPassport().getCountryCd());
			}
			
			if ( xsdObj.getCreditIdentification()!=null && xsdObj.getCreditIdentification().getProvincialIdCard()!=null)
			{
				crdPrfl.setCreditIDCard( new CreditIDCard());
				CreditIDCard currentCard = crdPrfl.getCreditIDCards()[ crdPrfl.getCreditIDCards().length -1 ];
				currentCard.setIdNumber( xsdObj.getCreditIdentification().getProvincialIdCard().getProvincialIdNum());
				currentCard.setProvinceCode( xsdObj.getCreditIdentification().getProvincialIdCard().getProvinceCd());
				currentCard.setIdTypeCode( CreditIDCard.PROVINCIAL_ID_KEY );
				currentCard.setLastUpdateTimestamp(requestTimeStamp);
			}
			
			if ( xsdObj.getCreditIdentification()!=null && xsdObj.getCreditIdentification().getSin()!=null)
			{
				crdPrfl.setCreditIDCard( new CreditIDCard());
				CreditIDCard currentCard = crdPrfl.getCreditIDCards()[ crdPrfl.getCreditIDCards().length -1 ];
				currentCard.setIdNumber( xsdObj.getCreditIdentification().getSin());
				currentCard.setIdTypeCode( CreditIDCard.SIN_KEY );
				currentCard.setLastUpdateTimestamp(requestTimeStamp);
			}
			
			if ( xsdObj.getCreditAddress()!=null)
			{
				crdPrfl.setCreditAddress( new CreditAddress());
				crdPrfl.getCreditAddress().setAddressLineOne( xsdObj.getCreditAddress().getAddressLineOne());
				crdPrfl.getCreditAddress().setAddressLineTwo( xsdObj.getCreditAddress().getAddressLineTwo());
				crdPrfl.getCreditAddress().setAddressLineThree( xsdObj.getCreditAddress().getAddressLineThree());
				crdPrfl.getCreditAddress().setAddressLineFour( xsdObj.getCreditAddress().getAddressLineFour());
				crdPrfl.getCreditAddress().setAddressLineFive( xsdObj.getCreditAddress().getAddressLineFive());
				crdPrfl.getCreditAddress().setAddressTypeCode(xsdObj.getCreditAddress().getAddressTypeCd());
				crdPrfl.getCreditAddress().setCity(xsdObj.getCreditAddress().getCityName());
				crdPrfl.getCreditAddress().setCountryCode(xsdObj.getCreditAddress().getCountryCd());
				crdPrfl.getCreditAddress().setPostalCode(xsdObj.getCreditAddress().getPostalCd());
				crdPrfl.getCreditAddress().setProvinceCode(xsdObj.getCreditAddress().getProvinceCd());
				crdPrfl.getCreditAddress().setType(xsdObj.getCreditAddress().getCreditAddressTypeCd());	
				crdPrfl.getCreditAddress().setLastUpdateTimestamp(requestTimeStamp);
			}
			
			if ( (creditValueCd != null && creditValueCd.trim().length() >0)
					|| (fraudIndicatorCd != null && fraudIndicatorCd.trim().length() >0 )) {
			    CreditValue cv = new CreditValue();
			    cv.setCreditValueCode( creditValueCd );
			    cv.setLastUpdateTimestamp( requestTimeStamp );
			    cv.setMethod( xsdObj.getMethodCd() );
			    cv.setComment( xsdObj.getCommentTxt() );
			    cv.setFraudIndicatorCd(fraudIndicatorCd);
			    crdPrfl.setCreditValue( cv );
			}
			crdPrfl.setComment( xsdObj.getCommentTxt() );
			crdPrfl.setApplicationProvinceCd( xsdObj.getApplicationProvinceCd() );
			
			if ( xsdObj.getCustomerGuarantor() != null ) {
			    
			    CustomerGuarantor customerGuarantor = new CustomerGuarantor();
			    com.telus.credit.wlnprfldmgt.domain.CustomerGuarantor  xsdCustomerGuarantor = xsdObj.getCustomerGuarantor();
			    customerGuarantor.setComment( xsdCustomerGuarantor.getCommentTxt() );
			    customerGuarantor.set_id( xsdCustomerGuarantor.getId() );
		        customerGuarantor.setExpiryDate( xsdCustomerGuarantor.getExpiryDate() );

		        if ( xsdCustomerGuarantor.getGuaranteedAmt() != null ) {
			        customerGuarantor.setGuaranteedAmount(  
			                new Money( xsdCustomerGuarantor.getGuaranteedAmt().doubleValue() ) );
			    }

		        if ( xsdCustomerGuarantor.getGuarantorCreditProfileID() > 0 ) {
			        customerGuarantor.setGuarantorCreditProfileId( xsdCustomerGuarantor.getGuarantorCreditProfileID() );
			    }
			    
			    if ( xsdCustomerGuarantor.getGuarantorCustomerID() > 0 ) {
			        //customerGuarantor.setGuaranteedCustomerId( (int)xsdCustomerGuarantor.getGuarantorCustomerId() );
			    	customerGuarantor.setGuarantorCustomerId((int)xsdCustomerGuarantor.getGuarantorCustomerID());
			    }
			    
			    customerGuarantor.setGuaranteedCustomerId((int)xsdObj.getCustomerID());
			    customerGuarantor.setGuarantorFullName( xsdCustomerGuarantor.getGuarantorFullName() );
			    customerGuarantor.setGuarantorPhoneNumber( xsdCustomerGuarantor.getGuarantorPhoneNum() );
			    customerGuarantor.setLastUpdateTimestamp(  requestTimeStamp );
			    customerGuarantor.setReferenceNumber( xsdCustomerGuarantor.getReferenceNum() );
			    crdPrfl.setCustomerGuarantor( customerGuarantor );
			}
			
			crdPrfl.setMethod( xsdObj.getMethodCd());
//			crdPrfl.setFormat( xsdObj.getFormatCd() );
//			crdPrfl.setStatus( xsdObj.getStatus());
			return crdPrfl;
		}
	}
	
	public static  ServiceException getServiceFaultInfo ( String errorCode , String errorMsg)
	{
		ServiceException fault = new ServiceException();
		fault.setErrorCode( errorCode);
		fault.setErrorMessage(errorMsg);
		return fault;
	}

	public static PolicyException  getPolicyFaultInfo ( String errorCode , String errorMsg)
	{
		PolicyException fault = new PolicyException();
		fault.setErrorCode( errorCode);
		fault.setErrorMessage(errorMsg);
		return fault;
	}
	
	public static Timestamp convertXMLGregorianCalendarToTimestamp (XMLGregorianCalendar xgc) {
		
		GregorianCalendar gregorianCalendar = xgc.toGregorianCalendar();
		Date date = gregorianCalendar.getTime();
		
		Timestamp timestamp = new Timestamp(date.getTime());
		
		return timestamp;
	}
	
	public static ProductCategoryQualification convertProductCategoryQualification (
			com.telus.credit.wlnprfldmgt.domain.ProductCategoryQualification prodCategoryQual) {
		
		if (prodCategoryQual == null)
			return null;
		
		ProductCategoryQualification prodCatQual = new ProductCategoryQualification();
		prodCatQual.setBoltOn(prodCategoryQual.isBoltOnInd());
		if (prodCategoryQual.getProductCategoryList() != null && prodCategoryQual.getProductCategoryList().size() > 0) {
			List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
			for (com.telus.credit.wlnprfldmgt.domain.ProductCategory prodCat : prodCategoryQual.getProductCategoryList()) {
				ProductCategory prodCategory = new ProductCategory();
				if (!prodCat.getCategoryCd().isEmpty() && prodCat.getCategoryCd() != null) {
					prodCategory.setCreditApprovedProductCategoryCd(prodCat.getCategoryCd());
					if (prodCat.isQualified() != null && prodCat.isQualified()) {
						prodCategory.setProductQualificationIndicator(true);
					} else {
						prodCategory.setProductQualificationIndicator(false);
					}
					productCategoryList.add(prodCategory);
				}
			}
			prodCatQual.setProductCategoryList(productCategoryList);
		}
		//prodCatQual.setProductCategoryList(prodCategoryQual.getProductCategoryList());
		
		return prodCatQual;
	}
	
	public static String getMessage( String messageId ) {
        ReferenceMessageDecode refMsg = ReferencePdsAccess.getMessage( messageId, ReferencePdsAccess.LANG_EN );
        return ( refMsg != null ? refMsg.getText() : null);
    }
	
	public static String convertDateToString(Date date) {
		DateFormat outputFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
		if (date == null)
			return null;
		return outputFormat.format(date);
	}
	
	/*
     * Converts a String to Date
     */
    public static Date convertStringToDate(String strDate)
    {
    	SimpleDateFormat formatter = new SimpleDateFormat( DATE_TIME_FORMAT );
    	Date convertedDate = null;
        try {
	              convertedDate = formatter.parse(strDate);
	        }
	    catch (ParseException pe) {
	            convertedDate = null;
	        }
	    return convertedDate;
      }
}
