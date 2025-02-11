package com.telus.credit.crda.util;


import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.credit.crda.exception.EnterpriseCreditAssessmentExceptionCodes;
import com.telus.credit.crda.exception.EnterpriseCreditAssessmentServiceException;
import com.telus.credit.domain.common.CreditCustomerInfo;
import com.telus.credit.domain.common.TelecomContact;
import com.telus.credit.domain.crda.CreditAssessmentRequest;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.SearchCreditAssessmentList;
import com.telus.credit.domain.crda.SearchCreditAssessmentsRequestCriteria;
import com.telus.credit.domain.ent.AuditInfo;

public class CrdaUtility { 
    private static final Log log = LogFactory.getLog(CrdaUtility.class);

    
/*    public static String convertToXml(Object obj) {
        if (obj == null) { throw new IllegalArgumentException("obj cannot be null."); }

        final String result;
        final StringWriter sw= new StringWriter();
        try {
            JAXBContext jxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jxbContext.createMarshaller();
            marshaller.marshal(obj, sw);
            result = sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }

        return result;
    }*/


    /**
     * Needed to create XMLGregorianCalendar instances
     */
    private static DatatypeFactory df = null;

    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                    "Exception while obtaining DatatypeFactory instance", dce);
        }

    }

    /**
     * @deprecated TODO use binding
     */
    public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
  			GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            gc.setTime(new Timestamp(date.getTime()));
            return df.newXMLGregorianCalendar(gc);
        }
    }

    /**
     * @deprecated TODO use binding
     */
    public static java.util.Date asDate(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {
            return xgc.toGregorianCalendar().getTime();
        }
    }

    /**
     * @deprecated TODO use binding
     */
    public static java.sql.Timestamp asTimestamp(XMLGregorianCalendar xgc) {
        if (xgc == null) {
            return null;
        } else {

            GregorianCalendar g = xgc.toGregorianCalendar();
            Date d = g.getTime();
            java.sql.Timestamp t = (d instanceof java.sql.Timestamp) ? (java.sql.Timestamp) d : new java.sql.Timestamp(d.getTime());
            return t;
        }
    }

   
    // if we use primitive boolean then java calls booleanValue which results in null pointer if Boolean object is null
    public static String getBooleanStringValue(Boolean b) {
        return (b!= null && b.booleanValue()) ? "Y" : "N";
    }

    public static boolean getBooleanValue(String b) {
        return (b != null && ("Y".equals(b) || "1".equals(b) || "true".equalsIgnoreCase(b))) ? true : false;
    }

  /*  public static String extractPhoneNumbers(CreditCustomerInfo creditCustomerInfo) {
        StringBuilder phoneNumbers = new StringBuilder();
        for (PhoneNumber telecomContact : creditCustomerInfo
                .getPhoneNumberList()) {
            String phoneType = null;
            String phoneNumber = null;
            String unparsedNumber = null;
            String extension = null;
            String areaCode = null;
            String countryCode = null;
            for (Iterator<JAXBElement<String>> i = telecomContact.getContent()
                    .iterator(); i.hasNext(); ) {
                JAXBElement<String> jax = i.next();

                if (PHONE_PROP_PHONE_TYP_CD
                        .equals(jax.getName().getLocalPart())) {
                    phoneType = jax.getValue();
                } else if (PHONE_PROP_NUMBER.equals(jax.getName()
                        .getLocalPart())) {
                    phoneNumber = jax.getValue();
                } else if (PHONE_PROP_UNPARSED_NUMBER.equals(jax.getName()
                        .getLocalPart())) {
                    unparsedNumber = jax.getValue();
                } else if (PHONE_PROP_EXTENSION.equals(jax.getName()
                        .getLocalPart())) {
                    extension = jax.getValue();
                } else if (PHONE_PROP_AREA_CD.equals(jax.getName()
                        .getLocalPart())) {
                    areaCode = jax.getValue();
                } else if (PHONE_PROP_COUNTRY_CD.equals(jax.getName()
                        .getLocalPart())) {
                    countryCode = jax.getValue();
                }
                if (phoneType != null && phoneType.trim().length() > 0) {
                    phoneNumbers.append(phoneType);
                    phoneNumbers.append(":");
                }
                if (unparsedNumber != null && unparsedNumber.trim().length() > 0) {
                    phoneNumbers.append(unparsedNumber);
                } else {
                    if (countryCode != null) {
                        phoneNumbers.append(countryCode);
                        phoneNumbers.append("-");
                    }
                    if (areaCode != null) {
                        phoneNumbers.append(areaCode);
                        phoneNumbers.append("-");
                    }
                    if (phoneNumber != null) {
                        phoneNumbers.append(phoneNumber);
                        phoneNumbers.append("-");
                    }
                    if (extension != null) {
                        phoneNumbers.append(extension);
                    }
                }
                if (i.hasNext()) {
                    phoneNumbers.append(",");
                }
            }
        }
        return phoneNumbers.toString();
    }*/

    public static String extractPhoneNumbers(CreditCustomerInfo creditCustomerInfo) {
        StringBuilder concatenatedPhonesDetails = new StringBuilder();
        List<TelecomContact> telecomContactList = creditCustomerInfo.getTelecomContactList();
        for (Iterator<TelecomContact> iterator = telecomContactList.iterator(); iterator.hasNext(); ) {
            TelecomContact telecomContact = (TelecomContact) iterator.next();
            String contactTypeCode = (telecomContact.getTelecomContactTypeCode() == null) ? "" : telecomContact.getTelecomContactTypeCode().trim();
            //String contactSubTypeCode = (telecomContact.getTelecomContactSubTypeCode() == null) ? "" : telecomContact.getTelecomContactSubTypeCode().trim();
            //Long telecomContactId = (telecomContact.getTelecomContactId() == null) ? 0 : telecomContact.getTelecomContactId();
            String extension = (telecomContact.getTelephoneExtensionNumber() == null) ? "" : telecomContact.getTelephoneExtensionNumber().trim();
            String phoneNumber = (telecomContact.getTelephoneNumber() == null) ? "" : telecomContact.getTelephoneNumber().trim();
            if (phoneNumber.length() > 0) {
                concatenatedPhonesDetails.append(phoneNumber);
                concatenatedPhonesDetails.append(":");
            }
            if (extension.length() > 0) {            	
                concatenatedPhonesDetails.append(extension);
                concatenatedPhonesDetails.append(":");
            }
            if (contactTypeCode.length() > 0) {            	
                concatenatedPhonesDetails.append(contactTypeCode);
                concatenatedPhonesDetails.append(":");
            }             
            if (iterator.hasNext())
                concatenatedPhonesDetails.append(",");
        }
        //TODO DB column STG_CREDIT_DCSN_TRN"."CONTACT_PHONE_NUM" length restriction
        String concatenatedPhonesDetailsStr = concatenatedPhonesDetails.toString();
        if (concatenatedPhonesDetailsStr != null && concatenatedPhonesDetailsStr.length() > 20)
        	concatenatedPhonesDetailsStr = concatenatedPhonesDetailsStr.substring(0, 20);
        return concatenatedPhonesDetailsStr;
    }

    /*
     * Cast List to object safely
     */
    public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {
        List<T> r = new ArrayList<T>(c.size());
        for (Object o : c)
            r.add(clazz.cast(o));
        return r;
    }

    public static int LongToInt(long l) {
        int i = (int) l;
        if ((long) i != l) {
            throw new IllegalArgumentException(l + " cannot be casted to int.");
        }
        return i;
    }

    public static void printIMapData(
            String aStmt, Map<String, Object> aMap) {
        log.debug("\n" + aStmt);
        log.debug("***********validateIbatisMapData************");
        if (aMap != null) {
            for (Map.Entry<String, Object> entry : aMap.entrySet()) {
                String Key = entry.getKey();
                Object Value = entry.getValue();
                if (Value != null) {
                    log.debug("Key = " + Key + " , Value=" + Value + ", Type = " + Value.getClass());
                } else
                    log.debug("Value = " + Value);

            }
        } else
            log.debug("Map = " + aMap);
    }
    
    public static BigDecimal getBigDecimalVal(Object obj, String objName) throws EnterpriseCreditAssessmentServiceException {
		try {
			BigDecimal bg=null;       
			if (obj != null) {
				if( obj instanceof Double){
					 bg = BigDecimal.valueOf((Double) obj);
				}else
					if( obj instanceof BigDecimal){		
						 bg = new BigDecimal(((BigDecimal)obj).doubleValue()+"");
					}
					else
						if( obj instanceof Integer){		
							 bg = new BigDecimal(((Integer)obj).doubleValue()+"");
						}
						else
							if( obj instanceof String){		
								 bg = new BigDecimal(((String)obj));
							}				
							else{					
				     	 throw new EnterpriseCreditAssessmentServiceException(
			                     EnterpriseCreditAssessmentExceptionCodes.GENERAL_INVALID_DB_DATA_STR + ": Attribute " + objName + ": \n " +
			                     Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
			                     EnterpriseCreditAssessmentExceptionCodes.GENERAL_INVALID_DB_DATA);
					}
			}
			return bg;
		} catch (Throwable e) {
	     	 throw new EnterpriseCreditAssessmentServiceException(
                     EnterpriseCreditAssessmentExceptionCodes.GENERAL_INVALID_DB_DATA_STR + ": Attribute " + objName + ": \n " +
                     Thread.currentThread().getStackTrace()[1].getMethodName() + "|" + Thread.currentThread().getStackTrace()[1].getClassName(),
                     EnterpriseCreditAssessmentExceptionCodes.GENERAL_INVALID_DB_DATA);
		}
	}

/*    public static  void print_aSearchCreditAssessmentList_Request(
			SearchCreditAssessmentsRequestCriteria searchCreditAssessmentsRequestCriteria,
			AuditInfo auditInfo) {
		SearchCreditAssessmentList aSearchCreditAssessmentList= new SearchCreditAssessmentList();
    	aSearchCreditAssessmentList.setSearchCreditAssessmentsRequestCriteria(searchCreditAssessmentsRequestCriteria);
    	aSearchCreditAssessmentList.setAuditInfo(auditInfo);
    	LogUtil.infolog("EnterpriseCreditAssessmentServiceImpl.SearchCreditAssessmentList : \n " + CrdaUtility.convertToXml(aSearchCreditAssessmentList));
	}*/
/*    public static  void print_PerformCreditAssessment_Request(
			CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo) {
		PerformCreditAssessment aPerformCreditAssessment= new PerformCreditAssessment();
    	aPerformCreditAssessment.setCreditAssessmentRequest(creditAssessmentRequest);
    	aPerformCreditAssessment.setAuditInfo(auditInfo);
    	LogUtil.infolog("EnterpriseCreditAssessmentServiceImpl.PerformCreditAssessment : \n " + CrdaUtility.convertToXml(aPerformCreditAssessment));
	}*/
/*    public static  void print_GetCreditAssessment_Request(long creditAssessmentID,
			AuditInfo auditInfo) {
		GetCreditAssessment aGetCreditAssessment= new GetCreditAssessment();
    	aGetCreditAssessment.setCreditAssessmentID(creditAssessmentID);
    	aGetCreditAssessment.setAuditInfo(auditInfo);
    	LogUtil.infolog("EnterpriseCreditAssessmentServiceImpl.GetCreditAssessment : \n " + CrdaUtility.convertToXml(aGetCreditAssessment));
	}*/

/*    public static  void print_VoidCreditAssessment_Request(long creditAssessmentID,
			String voidReasonCd, AuditInfo auditInfo) {
		VoidCreditAssessment aVoidCreditAssessment= new VoidCreditAssessment();
    	aVoidCreditAssessment.setCreditAssessmentID(creditAssessmentID);
    	aVoidCreditAssessment.setVoidReasonCd(voidReasonCd);
    	aVoidCreditAssessment.setAuditInfo(auditInfo);
    	LogUtil.infolog("EnterpriseCreditAssessmentServiceImpl.VoidCreditAssessment : \n " + CrdaUtility.convertToXml(aVoidCreditAssessment));
	}*/


}
