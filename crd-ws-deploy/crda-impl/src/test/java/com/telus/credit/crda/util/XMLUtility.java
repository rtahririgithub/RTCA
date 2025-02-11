package com.telus.credit.crda.util;


import com.telus.credit.domain.common.CreditAssessmentResult;
import com.telus.credit.domain.crda.PerformCreditAssessment;
import com.telus.credit.domain.crda.PerformCreditAssessmentResponse;
import com.telus.credit.domain.ent.AuditInfo;
import com.telus.credit.framework.test.TestUtil;
/*import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthiness;
import com.telus.credit.wlnprflmgtpxy.domain.OverrideCreditWorthiness;*/
/*import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthiness;
import com.telus.credit.wlnprflmgtpxy.domain.AssessCreditWorthinessRequest;*/

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;


public class XMLUtility {
 

   
//    public static CreditAssessmentResult createMappedFicoCreditAssessmentResult(String filename,DozerBeanMapper mapper) throws Throwable{
//    	if(filename==null)
//    		filename=TestFiles.FicoperformCreditAssessmentResponse;
//    	com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse aFicoPerformCreditAssessmentResponse = XMLUtility.createFicoPerformCreditAssessmentRequest(filename);    
//    	CreditAssessmentResult aCreditAssessmentResult =  mapper.map(aFicoPerformCreditAssessmentResponse.getCreditAssessmentResult(), com.telus.credit.domain.common.CreditAssessmentResult.class);
//    	return aCreditAssessmentResult; 
//    	}

	public  static AuditInfo createAuditInfo() {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setUserId("12");
		auditInfo.setOriginatorApplicationId("1");
		auditInfo.setChannelOrganizationId("1");
		auditInfo.setCorrelationId("1");
		auditInfo.setOriginatorApplicationId("1");
		auditInfo.setOutletId("1");
		auditInfo.setSalesRepresentativeId("1");
		auditInfo.setTimestamp(new Date());
		auditInfo.setUserId("1");
		auditInfo.setUserTypeCode("1");
		return auditInfo;
	}
    public static Object convertXMLToObject(Object objClassName, String filename) throws JAXBException {
        Object xmlobj = null;
        try {
        	//JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory);
        	//DocumentType documentType = ((JAXBElement<DocumentType>) jaxbContext.createUnmarshaller().unmarshal(inputStream)).getValue();
        	
            JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
            Unmarshaller u = jaxbContext.createUnmarshaller();
            u.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent arg0) {
            	// TODO Auto-generated method stub
            	System.out.println( "++++event: " + arg0.getMessage() );
            	System.out.println( "++++event: " + arg0.getLinkedException() );
            	return super.handleEvent(arg0);
            }
            });
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
            JAXBElement<? extends Object> root = u.unmarshal(new StreamSource(new File(filename)),
                    objClassName.getClass());
            xmlobj = root.getValue();
        } catch (JAXBException e) {
        	System.err.println( "e xception: " + e );
            throw e;
        } 
        return xmlobj;
    }

    public static String convertObjectToXml(Object objClassName) {
        try {
            final StringWriter sw= new StringWriter();
            
            JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(objClassName, sw);
            jaxbMarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler() {
            @Override
            public boolean handleEvent(ValidationEvent arg0) {
            	// TODO Auto-generated method stub
            	System.out.println( "++++Marshalling event: " + arg0.getMessage() );
            	System.out.println( "++++Marshalling event: " + arg0.getLinkedException() );
            	return super.handleEvent(arg0);
            }
            });
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse createPullConsumerCreditReportResponse(String filename)
//            throws JAXBException, Throwable {
//
//        com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse pullConsumerCreditReportResponse = new com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse();
//        com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse convertedPullConsumerCreditReportResponse = null;
//        try {
//            convertedPullConsumerCreditReportResponse = (com.telus.credit.crdgw.domain.PullConsumerCreditReportResponse) XMLUtility.convertXMLToObject(pullConsumerCreditReportResponse, filename);
//        } catch (JAXBException e1) {
//            e1.printStackTrace();
//            throw e1;
//        } catch (Throwable e) {
//
//            e.printStackTrace();
//            throw e;
//        }
//        return convertedPullConsumerCreditReportResponse;
//    }

//    public static com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse createFicoPerformCreditAssessmentResponse (String filename)
//    throws JAXBException, Throwable {
//
//			com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse ficoPerformCreditAssessmentResponse =
//			        new com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse();
//			com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse performCreditAssessmentResponse = null;
//			try {
//			    performCreditAssessmentResponse = (com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse) XMLUtility.convertXMLToObject(ficoPerformCreditAssessmentResponse, filename);
//			} catch (JAXBException e1) {
//			    e1.printStackTrace();
//			    throw e1;
//			} catch (Throwable e) {
//			
//			    e.printStackTrace();
//			    throw e;
//}
//return performCreditAssessmentResponse;
//}
//    public static com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse createFicoPerformCreditAssessmentRequest(String filename)
//            throws JAXBException, Throwable {
//  
//        com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse ficoPerformCreditAssessmentResponse =
//                new com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse();
//        com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse performCreditAssessmentResponse = null;
//        try {
//            performCreditAssessmentResponse = (com.fico.telus.blaze.webservice.PerformCreditAssessmentResponse) XMLUtility.convertXMLToObject(ficoPerformCreditAssessmentResponse, filename);
//        } catch (JAXBException e1) {
//            e1.printStackTrace();
//            throw e1;
//        } catch (Throwable e) {
//
//            e.printStackTrace();
//            throw e;
//        }
//        return performCreditAssessmentResponse;
//    }

    public static com.telus.credit.crdgw.domain.PullConsumerCreditReport createPullConsumerCreditReportRequest(String filename)
    throws JAXBException, Throwable {

    	com.telus.credit.crdgw.domain.PullConsumerCreditReport aPullConsumerCreditReport= new com.telus.credit.crdgw.domain.PullConsumerCreditReport();
    	com.telus.credit.crdgw.domain.PullConsumerCreditReport pullConsumerCreditReport = null;
		try {
			pullConsumerCreditReport = (com.telus.credit.crdgw.domain.PullConsumerCreditReport) XMLUtility.convertXMLToObject(aPullConsumerCreditReport, filename);
		} catch (JAXBException e1) {
		    e1.printStackTrace();
		    throw e1;
		} catch (Throwable e) {
		
		    e.printStackTrace();
		    throw e;
		}
		return pullConsumerCreditReport;
}
    
    public static PerformCreditAssessment createPerformCreditAssessmentRequest(String filename)
            throws JAXBException, Throwable {

        PerformCreditAssessment crasmtRequest = new PerformCreditAssessment();
        PerformCreditAssessment performCreditAssessment = null;
        try {
            performCreditAssessment = (PerformCreditAssessment) XMLUtility.convertXMLToObject(crasmtRequest, filename);
        } catch (JAXBException e1) {
            e1.printStackTrace();
            throw e1;
        } catch (Throwable e) {

            e.printStackTrace();
            throw e;
        }
        return performCreditAssessment;
    }

	public static PerformCreditAssessmentResponse createaPerformCreditAssessmentResponse(
			String aPerformCreditAssessmentResponseFilename) throws Throwable {

		PerformCreditAssessmentResponse aPerformCreditAssessmentResponse = new PerformCreditAssessmentResponse();
		PerformCreditAssessmentResponse performCreditAssessmentResponse = null;
		try {
			performCreditAssessmentResponse = (PerformCreditAssessmentResponse) XMLUtility
					.convertXMLToObject(aPerformCreditAssessmentResponse,
							aPerformCreditAssessmentResponseFilename);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			throw e1;
		} catch (Throwable e) {

			e.printStackTrace();
			throw e;
		}
		return performCreditAssessmentResponse;
	}

			/*		
            public static void main(String[] args) {
				//test convertXMLToObject
				PerformCreditAssessment crasmtRequest=new PerformCreditAssessment();
				String filename = TestFiles.performFullCreditAssessment.xml";
				PerformCreditAssessment xmlobj=null;
				try {
					System.out.println("convertXMLToObject");
					xmlobj = (PerformCreditAssessment) TestHelper.convertXMLToObject(crasmtRequest,filename);
					TestHelper.convertObjectToXml(xmlobj);
					System.out.println("FullCreditAssessmentRequest");
					com.telus.credit.crda.domain.FullCreditAssessmentRequest fc= 
						(com.telus.credit.crda.domain.FullCreditAssessmentRequest)xmlobj.getCreditAssessmentRequest();
					//TestHelper.convertObjectToXml(xmlobj.getCreditAssessmentRequest());
					
				} catch (JAXBException e) {
					
					e.printStackTrace();
				}
			}

		
			public static ConsumerCreditReportRequest createCrdGWTestInput() throws DatatypeConfigurationException{
		
				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setUserId("11111");
				auditInfo.setOriginatorApplicationId("11111");
				
				ConsumerCreditReportRequest req = new ConsumerCreditReportRequest();
				req.setApplicationProvinceCode("BC");
				req.setLineOfBusiness("Wireline");
				req.setChannelId("Internet");
				req.setPersonalInfo(new PersonalInfo());
				
				DatatypeFactory factory = DatatypeFactory.newInstance();
				XMLGregorianCalendar cal = factory.newXMLGregorianCalendarDate(1959, 12, 01 , -8);
				req.getPersonalInfo().setBirthDate(cal);
				
				req.setCreditCustomerInfo( new CreditCustomerInfo());
				req.getCreditCustomerInfo().setCustomerId(111111111);
				req.getCreditCustomerInfo().setPersonName( new PersonName());
				req.getCreditCustomerInfo().getPersonName().setFirstName("POLAR");
				req.getCreditCustomerInfo().getPersonName().setLastName("BEAR");
				req.getCreditCustomerInfo().setCustomerSubTypeCd("R");
				
				req.setCreditIdentification( new CreditIdentification());
				req.getCreditIdentification().setDriverLicense(new DriverLicense());
				req.getCreditIdentification().getDriverLicense().setDriverLicenseNumber("C1231929323923");
				
				req.setCreditAddress(new CreditAddress());
				req.getCreditAddress().setAddressLineOne("11 GLICK CRES");
				req.getCreditAddress().setCityName("YELLOWKNIFE");
				req.getCreditAddress().setProvinceCd("NT");
				req.getCreditAddress().setPostalCd("X1A3K6");
				
				QName qname = new QName("http://xmlschema.tmi.telus.com/xsd/Customer/BaseTypes/CreditCommon_v1","number");
				PhoneNumber phoneNumber= new PhoneNumber();
				JAXBElement<String> ele = new JAXBElement<String>( qname, String.class, "6043122234");
				phoneNumber.getContent().add(ele);
				//req.getCreditCustomerInfo().getPhoneNumberList().add(phoneNumber);
				
				req.setFailOverIndicator(false);
				req.setExternalServiceProvider( new ExternalServiceProviderType());
				req.getExternalServiceProvider().setPrimaryProvider( ServiceProviderType.EQFXIC);
				req.setReferenceId("1234567");
				req.setReferenceIdType("C");
				req.setLanguage("EN");

				return req;
			}
			
			
			public static void printCreditAssessmentDetails(
					com.telus.credit.crda.domain.GetCreditAssessmentResponse.CreditAssessmentDetails creditAssessmentDetails) {
				System.out.println("=============================================\n");
				System.out.println("creditAssessmentDetails : \n");
				if(creditAssessmentDetails.getCreditDecisioningInput()!= null){
					//System.out.println("creditAssessmentDetails.getCreditDecisioningInput : \n" + ToStringBuilder.reflectionToString(creditAssessmentDetails.getCreditDecisioningInput(),ToStringStyle.MULTI_LINE_STYLE));
					if(creditAssessmentDetails.getCreditAssessmentTypeCd().equalsIgnoreCase(EnterpriseCreditAssessmentConsts.ASMT_TYPE_FULL_ASMT)){
						FullCreditAssessmentRequest f = ((FullCreditAssessmentRequest)creditAssessmentDetails.getCreditDecisioningInput());
						if(f!= null){
							System.out.println("creditAssessmentDetails.getCreditDecisioningInput : \n" + ToStringBuilder.reflectionToString(f,ToStringStyle.MULTI_LINE_STYLE));
							if(f.getCreditCustomerInfo()!= null )
								printCreditCustomerInfo(f.getCreditCustomerInfo());
							if(f.getCreditProfileData()!= null )
								printCreditProfileData(f.getCreditProfileData());

						}
					}
					else{
						if(
								creditAssessmentDetails.getCreditAssessmentTypeCd().equalsIgnoreCase(EnterpriseCreditAssessmentConsts.ASMT_TYPE_OVRD_ASMT )
								||
								creditAssessmentDetails.getCreditAssessmentTypeCd().equalsIgnoreCase(EnterpriseCreditAssessmentConsts.ASMT_TYPE_AUDIT )
								){
							OverrideCreditAssessmentRequest f = ((OverrideCreditAssessmentRequest)creditAssessmentDetails.getCreditDecisioningInput());
							if(f!= null){
								System.out.println("creditAssessmentDetails.getCreditDecisioningInput : \n" + ToStringBuilder.reflectionToString(f,ToStringStyle.MULTI_LINE_STYLE));
							}
						
						}	
					}				
				}else
				{
					System.out.println( "creditAssessmentDetails.getCreditDecisioningInput is null" );
				}
				
				if(creditAssessmentDetails.getCreditBureauDataResult()!= null){
					System.out.println("creditAssessmentDetails.getCreditBureauDataResult().getCreditBureauDataResult : \n" + ToStringBuilder.reflectionToString(creditAssessmentDetails.getCreditBureauDataResult(),ToStringStyle.MULTI_LINE_STYLE));
				}else
				{
					System.out.println( "creditAssessmentDetails.getCreditBureauDataResult().getCreditBureauDataResult is null" );
				}

				if(creditAssessmentDetails.getCreditBureauDataResultDocumentList()!= null){
					System.out.println("creditAssessmentDetails.getCreditBureauDataResult().getCreditBureauDataResultDocumentList : \n" + ToStringBuilder.reflectionToString(creditAssessmentDetails.getCreditBureauDataResultDocumentList(),ToStringStyle.MULTI_LINE_STYLE));
				}else
				{
					System.out.println( "creditAssessmentDetails.getCreditBureauDataResult().getCreditBureauDataResultDocumentList is null" );
				}
				
				if(creditAssessmentDetails.getCreditDecisioningResult()!= null){
					System.out.println("creditAssessmentDetails.getCreditDecisioningResult(). : \n" +ToStringBuilder.reflectionToString(creditAssessmentDetails.getCreditDecisioningResult(),ToStringStyle.MULTI_LINE_STYLE));
					if(creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification()!= null && 
							creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification().getProductCategoryList()!= null){
						List<ProductCategory>  l = creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification().getProductCategoryList();
						for (Iterator iterator = l.iterator(); iterator.hasNext();) {
							ProductCategory productCategory = (ProductCategory) iterator.next();
							System.out.println("creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification().productCategory : \n" +ToStringBuilder.reflectionToString(productCategory,ToStringStyle.MULTI_LINE_STYLE));
						}
					//productCategoryQualification
					}
				}else
				{
					System.out.println( "creditAssessmentDetails.getCreditDecisioningResult is null" );
				}
				
				System.out.println("=============================================\n");
			}

				
				private static void printCreditProfileData(
					CreditProfileData creditProfileData) {
					System.out.println("creditProfileData : \n" + ToStringBuilder.reflectionToString(creditProfileData,ToStringStyle.MULTI_LINE_STYLE));
					if (creditProfileData.getCreditAddress()!= null)
							System.out.println("creditProfileData.getCreditAddress : \n" + ToStringBuilder.reflectionToString(creditProfileData.getCreditAddress(),ToStringStyle.MULTI_LINE_STYLE));	
						
					if (creditProfileData.getCreditCardCd()!= null)
						System.out.println("creditProfileData.getCreditCardCd : \n" + ToStringBuilder.reflectionToString(creditProfileData.getCreditCardCd(),ToStringStyle.MULTI_LINE_STYLE));	

					if (creditProfileData.getCreditIdentification()!= null)
						System.out.println("creditProfileData.getCreditIdentification : \n" + ToStringBuilder.reflectionToString(creditProfileData.getCreditIdentification(),ToStringStyle.MULTI_LINE_STYLE));	
					if (creditProfileData.getCustomerGuarantor()!= null)
						System.out.println("creditProfileData.getCustomerGuarantor : \n" + ToStringBuilder.reflectionToString(creditProfileData.getCustomerGuarantor(),ToStringStyle.MULTI_LINE_STYLE));	

			}

				private static void printCreditCustomerInfo(
					CreditCustomerInfo creditCustomerInfo) {
					System.out.println("creditCustomerInfo : \n" + ToStringBuilder.reflectionToString(creditCustomerInfo,ToStringStyle.MULTI_LINE_STYLE));
					if (creditCustomerInfo.getPersonName()!= null)
						System.out.println("creditCustomerInfo.getCreditCustomerInfo.getPersonName : \n" + ToStringBuilder.reflectionToString(creditCustomerInfo.getPersonName(),ToStringStyle.MULTI_LINE_STYLE));
				
			}

				public static void printCreditAssessmentTransaction(CreditAssessmentTransaction creditAssessmentDetails) {
					System.out.println("=============================================\n");
				System.out.println("CreditAssessmentTransaction : \n" + ToStringBuilder.reflectionToString(creditAssessmentDetails,ToStringStyle.MULTI_LINE_STYLE));
					 
					
				if(creditAssessmentDetails.getCreditDecisioningResult()!= null){
					System.out.println("CreditAssessmentTransaction.getCreditDecisioningResult : \n" + ToStringBuilder.reflectionToString(creditAssessmentDetails.getCreditDecisioningResult(),ToStringStyle.MULTI_LINE_STYLE));

					if(creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification()!= null && 
							creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification().getProductCategoryList()!= null){
						List<ProductCategory>  l = creditAssessmentDetails.getCreditDecisioningResult().getProductCategoryQualification().getProductCategoryList();
						for (Iterator iterator = l.iterator(); iterator.hasNext();) {
							ProductCategory productCategory = (ProductCategory) iterator.next();
							System.out.println("productCategory : \n" +ToStringBuilder.reflectionToString(productCategory,ToStringStyle.MULTI_LINE_STYLE));
						}
					
					}	
				}else
				{
					System.out.println( "CreditAssessmentTransaction.getCreditDecisioningResult is null" );
				}
				System.out.println("=============================================\n");
			}
			
	 
			public static OverrideCreditAssessmentRequest getOverrideCreditAssessmentRequest() {
				PerformCreditAssessment crasmtRequest=new PerformCreditAssessment();
				String filename = TestFiles.performOverrideCreditAssessment.xml";
				PerformCreditAssessment performCreditAssessment=null;
				try {
					performCreditAssessment = (PerformCreditAssessment) TestHelper.convertXMLToObject(crasmtRequest,filename);
				} catch (JAXBException e) {
					e.printStackTrace();
				}
				System.out.println(performCreditAssessment.getCreditAssessmentRequest().getCreditAssessmentTypeCd());
				System.out.println(performCreditAssessment.getCreditAssessmentRequest().getCreditAssessmentSubTypeCd());
				OverrideCreditAssessmentRequest creditAssessmentRequest=
					(OverrideCreditAssessmentRequest)performCreditAssessment.getCreditAssessmentRequest();
				return creditAssessmentRequest;
			}
			
			public static CreditAssessmentTransaction testPerformCreditAssessmet(EnterpriseCreditAssessmentServicePortType m_entAsmtServiceImpl, CreditAssessmentRequest creditAssessmentRequest, AuditInfo auditInfo) throws Throwable{
				try {
	 		        CreditAssessmentTransaction creditAssessmentTransaction =m_entAsmtServiceImpl.performCreditAssessment(creditAssessmentRequest, auditInfo);
					return creditAssessmentTransaction;
				} catch (Throwable e) {
					
					e.printStackTrace();
					throw e;
				} 	
				
			}

			public static CreditAssessmentTransaction testPerformOverrideCreditAssessmet(
					EnterpriseCreditAssessmentServicePortType m_entAsmtServiceImpl) throws Throwable {
				OverrideCreditAssessmentRequest overrideCreditAssessmentRequest = TestHelper.getOverrideCreditAssessmentRequest();
				CreditAssessmentTransaction creditAssessmentTransaction =TestHelper.testPerformCreditAssessmet(
							m_entAsmtServiceImpl, 
							overrideCreditAssessmentRequest, 
							createAuditInfo());
				return creditAssessmentTransaction;
			}
			public  static AuditInfo createAuditInfo() {
				AuditInfo auditInfo = new AuditInfo();
				auditInfo.setUserId("12");
				auditInfo.setOriginatorApplicationId("1");
				return auditInfo;
			}
	*/
    
//    @SuppressWarnings("unchecked")
    //   TargetClass src = read(filename, TargetClass.class);
//
//    private <T> T convertXMLToObject(String filename, Class<T> clazz) throws Exception
//    {
//        JAXBContext ctx = JAXBContext.newInstance(clazz);
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
//        return (T ) ctx.createUnmarshaller().unmarshal(is);
//    }

 /*public static AssessCreditWorthiness createAssessCreditWorthiness(
			String filename) throws Throwable {

		AssessCreditWorthiness assessCreditWorthinessTmp = new AssessCreditWorthiness();
		AssessCreditWorthiness assessCreditWorthiness = null;
        try {
        	assessCreditWorthiness = (AssessCreditWorthiness) XMLUtility.convertXMLToObject(assessCreditWorthinessTmp, filename);
        } catch (JAXBException e1) {
            e1.printStackTrace(); 
            throw e1;
        } catch (Throwable e) {

            e.printStackTrace();
            throw e;
        }
        return assessCreditWorthiness;
    }
 public static OverrideCreditWorthiness createOverrideCreditWorthiness(
			String filename) throws Throwable {

	 OverrideCreditWorthiness assessCreditWorthinessTmp = new OverrideCreditWorthiness();
	 OverrideCreditWorthiness assessCreditWorthiness = null;
     try {
     	assessCreditWorthiness = (OverrideCreditWorthiness) XMLUtility.convertXMLToObject(assessCreditWorthinessTmp, filename);
     } catch (JAXBException e1) {
         e1.printStackTrace(); 
         throw e1;
     } catch (Throwable e) {

         e.printStackTrace();
         throw e;
     }
     return assessCreditWorthiness;
 }

 */


}

