package com.fico.telus.rtca.blaze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.fico.telus.blaze.creditAsessment.CancelDepositCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.CreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ExistingCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.ManualInquiryCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.MonthlyUDCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.NewCustomerCreditAssessmentRequest;
import com.fico.telus.blaze.creditAsessment.OverrideCreditAssessmentRequest;
import com.fico.telus.blaze.creditSimulator.SimulatorCreditBureauRequest;






public class FicoRequestResponseMarshallingUtil {
	
 	public static CreditAssessmentRequest createCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
 		 String nodeName = "creditAssessmentRequest";
	 	 CreditAssessmentRequest re = (CreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, CreditAssessmentRequest.class);
	      
	      return re;
	  }

 	public static SimulatorCreditBureauRequest createSimulatorCreditBureauRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	 String nodeName = "SimulatorCreditBureauRequest";
	 	 SimulatorCreditBureauRequest re = (SimulatorCreditBureauRequest)createRequestCommon(xmlfilename, nodeName, SimulatorCreditBureauRequest.class);
	      
	      return re;
	  }
 	
 	/*
 	 * NewCustomerCreditAssessmentRequest
     * ManualInquiryCreditAssessmentRequest
     * ExistingCustomerCreditAssessmentRequest
     * OverrideCreditAssessmentRequest
     * MonthlyUDCreditAssessmentRequest
     * CancelDepositCreditAssessmentRequest
 	 */
 	public static NewCustomerCreditAssessmentRequest createNewCustomerCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	 String nodeName = "NewCustomerCreditAssessmentRequest";
	 	 NewCustomerCreditAssessmentRequest re = (NewCustomerCreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, NewCustomerCreditAssessmentRequest.class);
	      
	      return re;
	  }	
 	public static ManualInquiryCreditAssessmentRequest createManualInquiryCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	 String nodeName = "ManualInquiryCreditAssessmentRequest";
	 	 ManualInquiryCreditAssessmentRequest re = (ManualInquiryCreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, ManualInquiryCreditAssessmentRequest.class);
	      
	      return re;
	  }	
 	public static ExistingCustomerCreditAssessmentRequest createExistingCustomerCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	 String nodeName = "ExistingCustomerCreditAssessmentRequest";
	 	 ExistingCustomerCreditAssessmentRequest re = (ExistingCustomerCreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, ExistingCustomerCreditAssessmentRequest.class);
	      
	      return re;
	  }
	public static OverrideCreditAssessmentRequest createOverrideCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	 String nodeName = "OverrideCreditAssessmentRequest";
	 	OverrideCreditAssessmentRequest re = (OverrideCreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, OverrideCreditAssessmentRequest.class);
	      
	      return re;
	  }
	public static MonthlyUDCreditAssessmentRequest createMonthlyUDCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	String nodeName = "MonthlyUDCreditAssessmentRequest";
	 	MonthlyUDCreditAssessmentRequest re = (MonthlyUDCreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, MonthlyUDCreditAssessmentRequest.class);
	      
	      return re;
	  }
	public static CancelDepositCreditAssessmentRequest createCancelDepositCreditAssessmentRequest( String  xmlfilename) throws JAXBException, XMLStreamException, IOException {	
	 	 String nodeName = "CancelDepositCreditAssessmentRequest";
	 	CancelDepositCreditAssessmentRequest re = (CancelDepositCreditAssessmentRequest)createRequestCommon(xmlfilename, nodeName, CancelDepositCreditAssessmentRequest.class);
	      
	      return re;
	  }
 	

 	public static void delete(File file) {
  		try {
  			if (file.exists()) {
  				if (file.isDirectory()) {

  					// directory is empty, then delete it
  					if (file.list().length == 0) {

  						file.delete();
  						System.out.println("Directory is deleted : "
  								+ file.getAbsolutePath());
  						System.out.println("\n\n");
  					} else {

  						// list all the directory contents
  						String files[] = file.list();

  						for (String temp : files) {
  							// construct the file structure
  							File fileDelete = new File(file, temp);

  							// recursive delete
  							delete(fileDelete);
  						}

  						// check the directory again, if empty then delete it
  						if (file.list().length == 0) {
  							file.delete();
  							System.out.println("Directory is deleted : "
  									+ file.getAbsolutePath());
  							System.out.println("\n\n");
  						}
  					}

  				} else {
  					// if file, then delete it
  					file.delete();
  					System.out.println("File is deleted : "
  							+ file.getAbsolutePath());
  				}
  			}
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
  	}	
 	
 	
 	
	
    public static Object createRequestCommon(String xmlFilename,String nodeName,Class clazz ) throws JAXBException, XMLStreamException, IOException {	
    	String requestStr =  FicoRequestResponseMarshallingUtil.readFileToString(new File(xmlFilename));
        XMLInputFactory xif = XMLInputFactory.newInstance(); 
        XMLStreamReader xmlReader = xif.createXMLStreamReader(new StringReader(requestStr));
        String LocalName="";
        int event = 0;
        for (event = xmlReader.next(); event != XMLStreamReader.END_DOCUMENT; event = xmlReader.next()) {
            if (event == XMLStreamReader.START_ELEMENT) {
            	 LocalName = 	xmlReader.getLocalName() ;
                if (xmlReader.getLocalName().equalsIgnoreCase(nodeName)) {
                    break;
                }
            }
        }
        Object aObject = null;
        if (event != XMLStreamReader.END_DOCUMENT) {
            JAXBContext jaxbContext = getJAXBContext(clazz);
            JAXBElement<Object> result = jaxbContext.createUnmarshaller().unmarshal(xmlReader, clazz);
            aObject = result.getValue();
        }
        return aObject;
    }
    public static JAXBContext getJAXBContext(Class clazz) {
        String packageName = clazz.getPackage().getName();
        try {
        	return JAXBContext.newInstance(packageName);
            //return jaxbContextsCache.get(packageName);
        } catch (Throwable e) {
            throw new RuntimeException("Error when getting JAXBContext from cache", e);
        }
    }
    public static String readFileToString(File file) throws IOException {
        Scanner scanner = new Scanner(file).useDelimiter("\\Z");
        String contents = scanner.next();
        return contents.replaceAll("[\\n\\t\\r]", "");
    }





    public static  String writeToFilebyFileName(
    		String responseFolder,
    		String filename,   
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ filename + "_ResponseFile.xml";
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  filename + "_ResponseFile.xml";
	}  
    
    public static  void writeResponseErrorToFilebyFileName(
    		String responseFolder,
    		String file,  
			String errorTxt)
			throws Throwable {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ file;
        String filename= responseFullPath;
        FileWriter fw = new FileWriter(filename,true);  
        fw.write(errorTxt);
        fw.close();

	}
  
    public static String convertToXml(Object obj) throws JAXBException {
        if (obj == null) { throw new IllegalArgumentException("obj cannot be null."); }

        final String result;
        final StringWriter sw= new StringWriter();
            JAXBContext jxbContext = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = jxbContext.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal(obj, sw);
            result = sw.toString();

        return result;
    }


   
}
