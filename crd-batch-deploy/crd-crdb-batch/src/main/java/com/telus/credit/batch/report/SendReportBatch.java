/*
 * Copyright (c) 2005 TELUS Communications Inc., All Rights Reserved.
 * 
 * This document contains proprietary information that shall be distributed or
 * routed only within TELUS, and its authorized clients, except with written
 * permission of TELUS.
 * 
 */
package com.telus.credit.batch.report;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;

import com.telus.framework.config.ConfigContext;
import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.module.AbstractNonLoopingModule;
import com.telus.framework.exception.RuntimeEnvironmentException;
import com.telus.framework.mail.MailBean;
import com.telus.framework.mail.MailSender;
import com.telus.framework.validation.CommonFormatValidator;

/**
 * 
 * <p>
 * <b>Description: </b>  This function . 
 * </p>
 * <p>
 * <b>Design Observations: </b>
 * </p>
 * <ul>
 * <li></li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Issues: </b>
 * </p>
 * <ul>
 * <li>[Issues]</li>
 * </ul>
 * 
 * <p>
 * <br>
 * <b>Revision History: </b>
 * </p>
 * <table border="1" width="100%">
 * <tr>
 * <th width="15%">Date</th>
 * <th width="15%">Revised By</th>
 * <th width="55%">Description</th>
 * <th width="15%">Reviewed By</th>
 * </tr>
 * <tr>
 * <td width="15%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * <td width="55%">&nbsp;</td>
 * <td width="15%">&nbsp;</td>
 * </tr>
 * </table>
 * 
 * @author Lei Fan(x089748)
 *  
 */
public class SendReportBatch extends AbstractNonLoopingModule
{
	private static final Log log = LogFactory.getLog( SendReportBatch.class );
	private String m_inputFile;
	private String m_date;
	private SimpleDateFormat dateFormat;
	
	
	/** email property FROM */
    protected static final String[] EMAIL_FROM  = new String[] {
            "mailDiscrepancy", "email_From" };
       
    /** email property SUBJECT */
    protected static final String[] EMAIL_SUBJECT  = new String[] {
            "mailDiscrepancy", "email_subject" };    

    /** email property TEXT MSG */
    protected static final String[] EMAIL_BODY  = new String[] {
            "mailDiscrepancy", "email_Body" };    
    
    protected static final String[] EMAIL_RECIPIENTS_TO = 
        new String[]{"mailDiscrepancy","emailiRecipients_TO"};

    protected static final String[] EMAIL_RECIPIENTS_CC = 
        new String[]{"mailDiscrepancy","emailiRecipients_CC"};

    protected static final String[] EMAIL_RECIPIENTS_BCC = 
        new String[]{"mailDiscrepancy","emailiRecipients_BCC"};    
	
	public void launch(BatchContext batchContext) throws ModuleException
	{
		dateFormat= new SimpleDateFormat("yyyy-MM-dd");
		m_date=dateFormat.format(new Date());
		sendEmailReport();
	   }
	
	private void sendEmailReport()
	{  
		MailBean mailBean = new MailBean();
		String mailProperty;
		String[] mailIds;
		try
	    {
	      // TO Recipients
	      List emailRecipients = ConfigContext.getList(EMAIL_RECIPIENTS_TO);   
	      if (!emailRecipients.isEmpty())
	      {
		      Iterator iter = emailRecipients.iterator();
		      while (iter.hasNext())
		      {
		      	 mailProperty = (String)iter.next();
		      	 mailIds=mailProperty.trim().split("\\;");
		      	 for (int x=0;x<mailIds.length;x++)
		      	 {  
		      	 	if ((mailIds[x]!=null)&& !mailIds[x].equals(""))
		      	 	{
		              assert (CommonFormatValidator.isValidEmailAddress(mailIds[x])) == true : 
		        	    "Address must be a valid internet address";
		              mailBean.addMailTo(mailIds[x]);
		      	 	 }//ends if
		      	  }//ends for loop
		        }//ends while loop
		      
		      // CC Recipients        	    
		      emailRecipients = ConfigContext.getList(EMAIL_RECIPIENTS_CC); 
		      iter = emailRecipients.iterator();
		      while (iter.hasNext())
		      {
		      	 mailProperty = (String)iter.next();
		      	 mailIds=mailProperty.trim().split("\\;");
		      	 for (int x=0;x<mailIds.length;x++)
		      	 {   
		      	 	if ((mailIds[x]!=null)&& !mailIds[x].equals(""))
		      	 	{
		             assert (CommonFormatValidator.isValidEmailAddress(mailIds[x])) == true : 
		       	         "Address must be a valid internet address";
		             mailBean.addMailCc(mailIds[x]);   
		      	 	}
		      	  }
		        }
		      
		      // BCC Recipients  
		      emailRecipients = ConfigContext.getList(EMAIL_RECIPIENTS_BCC);   
		      iter = emailRecipients.iterator();
		      while (iter.hasNext())
		      {  
		      	mailProperty = (String)iter.next();
		      	mailIds=mailProperty.trim().split("\\;");
		      	for (int x=0;x<mailIds.length;x++)
		      	 {
		      		if ((mailIds[x]!=null)&& !mailIds[x].equals(""))
		      		{
		      		  assert (CommonFormatValidator.isValidEmailAddress(mailIds[x])) == true : 
		       	        "Address must be a valid internet address";        	        	
		              mailBean.addMailBcc(mailIds[x]);  
		      		}
		      	 }
		        }        	    
		                
		     mailBean.setMailFrom(ConfigContext.getProperty(EMAIL_FROM));
		     mailBean.setSubject(ConfigContext.getProperty(EMAIL_SUBJECT)+"-"+m_date);
		     mailBean.setTextMsg(ConfigContext.getProperty(EMAIL_BODY));
		     mailBean.addFileAttachment(m_inputFile);
		     MailSender.send(mailBean);
		     //String todayStr = DateUtil.formatDateToString(new Date(),
		     // Gatewayproperties.getReportFolderDateformat());
		     }
	     }
	    catch (UnsupportedEncodingException e)
	    { 
	      throw new RuntimeEnvironmentException(e);
	      }
	    catch (MailException e)
	    {
	      throw new RuntimeEnvironmentException(e);
	      }
	    catch (MessagingException e)
	    {
	      throw new RuntimeEnvironmentException(e);
	      }
	    catch (IOException e)
	    {
	      throw new RuntimeEnvironmentException(e);
	       }
	}

	/**
	 * @param inputFile The inputFile to set.
	 */
	public void setInputFile(String inputFile) {
		m_inputFile = inputFile;
	}
 }
