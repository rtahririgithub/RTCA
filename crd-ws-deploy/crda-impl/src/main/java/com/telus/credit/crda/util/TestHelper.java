/*package com.telus.credit.crda.util;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.telus.credit.crda.domain.CreditAssessmentRequest;
import com.telus.credit.crda.domain.CreditAssessmentTransaction;
import com.telus.credit.crda.domain.FullCreditAssessmentRequest;
import com.telus.credit.crda.domain.OverrideCreditAssessmentRequest;
import com.telus.credit.crda.domain.PerformCreditAssessment;
import com.telus.credit.crdgw.domain.ConsumerCreditReportRequest;
import com.telus.credit.crdgw.domain.ExternalServiceProviderType;
import com.telus.credit.crdgw.domain.ServiceProviderType;
import com.telus.credit.domain.common.CreditAddress;
import com.telus.credit.domain.common.CreditCustomerInfo;
import com.telus.credit.domain.common.CreditIdentification;
import com.telus.credit.domain.common.CreditProfileData;
import com.telus.credit.domain.common.DriverLicense;
import com.telus.credit.domain.common.PersonName;
import com.telus.credit.domain.common.PersonalInfo;
import com.telus.credit.domain.common.PhoneNumber;
import com.telus.credit.domain.common.ProductCategory;
import com.telus.credit.ent.domain.common.AuditInfo;
import com.telus.wsdl.cmo.ordermgmt.enterprisecreditassessmentservice_1.EnterpriseCreditAssessmentServicePortType;


public class TestHelper {


		public static Object convertXMLToObject(Object objClassName, String filename) throws JAXBException{
			Object xmlobj = null;
			 try {
					File file = new File(filename);				 
					JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
					Unmarshaller u =jaxbContext.createUnmarshaller();
					JAXBElement<? extends Object> root = u.unmarshal(new StreamSource(new File(filename)),
						 								objClassName.getClass());
					xmlobj = root.getValue();
				  } catch (JAXBException e) {
					throw e;
				  }
			return xmlobj;
		}

		public static void convertObjectToXml(Object objClassName){
			  try {
				  
					JAXBContext jaxbContext = JAXBContext.newInstance(objClassName.getClass());
					Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			 
					// output pretty printed
					jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
					jaxbMarshaller.marshal(objClassName, System.out);
			 
				      } catch (JAXBException e) {
					e.printStackTrace();
				      }
			
		}
		
		public static void main(String[] args) {
			//test convertXMLToObject
			PerformCreditAssessment crasmtRequest=new PerformCreditAssessment();
			String filename = "test/data/performFullCreditAssessment.xml";
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
			String filename = "test/data/performOverrideCreditAssessment.xml";
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
}
*/