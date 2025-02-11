package com.telus.credit.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;




public class RequestResponseTestUtilBase {
	
	
	public static String readClasspathFile(String location) throws IOException {
        Resource resource = new ClassPathResource(location);
        File file = new File(resource.getURI());
        return readFileToString(file);
    }

    public static InputStream readClasspathFileToInputStream(String location) throws IOException {
        Resource resource = new ClassPathResource(location);
        return resource.getInputStream();
    }



    public static void stringToFile(File file, String str) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Errors occured when writing string to file", e);
        }
    }
    
    public static void printObject(String title , Object o){
    	System.out.println(title);
       // System.out.println(org.apache.commons.lang.builder.ReflectionToStringBuilder.reflectionToString(o));
    }


     public static Object convertXmlCommon(String xmlFilename,String nodeName,Class clazz ) throws JAXBException, XMLStreamException, IOException {	
    	String requestStr =  readFileToString(new File(xmlFilename));
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
    		String filename,  
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
       
        String responseFullPath = filename + "_ResponseFile.xml";
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  responseFullPath;
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
    public static  void writeResponseErrorToFilebyFileName(
    		String responseFolder,
    		String file,  //
			String response)
			throws Throwable {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ file;
        String filename= responseFullPath;
        FileWriter fw = new FileWriter(filename,true);  
        fw.write(response);
        fw.close();

	}     
    public static  String writeToFilebyFileName(
    		String responseFolder,
    		File file,  //
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ file.getName() + "_ResponseFile.xml";
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  responseFullPath;
	}    
    public static  String writeToFile(
    		String responseFolder,
    		String scen_num,  //
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
    	String scenanrioName			="Scenario_"+scen_num;
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ scenanrioName + "_ResponseFile.xml";
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  responseFullPath;
	}
    public static  String writeFailedToFile(
    		String responseFolder,
    		String scenanrioName,  
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
   	 	//scenanrioName			=scenanrioName + "Failed_";
        String responseFullPath = responseFolder+ scenanrioName + "_ResponseFile.xml";
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  responseFullPath;
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
		//return  responseFullPath;
	}  
    public static  String writeToFilebyFileName2(
    		String responseFolder,
    		String filename,   
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ filename ;
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  responseFullPath;
	}     
    public static  String writeRequestToFilebyFileName(
    		String responseFolder,
    		String filename,   
			Object response)
			throws FileNotFoundException, UnsupportedEncodingException, JAXBException {
        if (!new File(responseFolder).exists()) {
       	 new File(responseFolder).mkdirs(); 
        }    	
        
        String responseFullPath = responseFolder+ filename ;
		PrintWriter writer = new PrintWriter(responseFullPath, "UTF-8");
		writer.println(convertToXml(response));
		writer.close();
		return  responseFullPath;
	}    
    
  	public static void delete(File file) {
  		if(file!=null){
    		try {
  			if (file.exists()) {
  				if (file.isDirectory()) {

  					// directory is empty, then delete it
  					if (file!=null && file.list()!=null && file.list().length == 0) {

  						file.delete();
  						System.out.println("Directory is deleted : "
  								+ file.getAbsolutePath());
  						System.out.println("\n\n");
  					} else {

  						// list all the directory contents
  						String files[] = file.list();
  					 if(files!= null ){
  						for (String temp : files) {
  							// construct the file structure
  							File fileDelete = new File(file, temp);

  							// recursive delete
  							delete(fileDelete);
  						}

  						// check the directory again, if empty then delete it
  					if(file.list()!=null){
  						//if (  file.list().length == 0) 
  						{
  							file.delete();
  							System.out.println("Directory is deleted : "+ file.getAbsolutePath());
  							System.out.println("\n\n");
  						}
  						}
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
  	}
    
}
