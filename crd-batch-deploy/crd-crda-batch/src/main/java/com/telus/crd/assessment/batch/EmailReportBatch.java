/*
 * Created on 19-Oct-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.crd.assessment.batch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import com.telus.crd.assessment.util.DateUtil;
import com.telus.crd.assessment.util.FileExtensionFilter;
import com.telus.crd.assessment.util.CommonConstants;

import com.telus.framework.batch.BatchContext;
import com.telus.framework.batch.exception.ModuleException;
import com.telus.framework.batch.module.AbstractNonLoopingModule;
import com.telus.framework.config.ConfigContext;
import com.telus.framework.exception.RuntimeEnvironmentException;
import com.telus.framework.mail.MailBean;
import com.telus.framework.mail.MailSender;
import com.telus.framework.validation.CommonFormatValidator;


/**
 * @author x089470
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class EmailReportBatch extends AbstractNonLoopingModule {
	private static final Log s_log = LogFactory
			.getLog(EmailReportBatch.class);

	private String m_filePath;
	private String m_filePattern;

	public EmailReportBatch() {
	}

	public void launch(BatchContext batchContext) throws ModuleException {


		File emailFile = new File(getM_filePath());
		FileExtensionFilter filter = new FileExtensionFilter(getM_filePattern());

		if (emailFile.exists()) {
			//zip the file
           // try{
            File[] lstFile = emailFile.listFiles(filter);
            	//create and send the email
            sendEmail(lstFile);

           // }catch (FileNotFoundException e){
           //// 	throw new ModuleException(e.toString());
           // }catch (IOException e){
           // 	throw new ModuleException(e.toString());
          //  }
		}

	}

	private void sendEmail(File[] reportfiles) {
		{
			MailBean mailBean = new MailBean();

			try {
				// TO Recipients
				List emailRecipients = getEmailRecipients(CommonConstants.EMAIL_RECIPIENTS_TO);
				if (emailRecipients.isEmpty())
					throw new RuntimeEnvironmentException(
							"Missing receipent address");

				Iterator iter = emailRecipients.iterator();
				while (iter.hasNext()) {
					String toEmail = (String) iter.next();
					assert (CommonFormatValidator.isValidEmailAddress(toEmail)) == true : "Address must be a valid internet address";
					mailBean.addMailTo(toEmail);
				}
				// CC Recipients
				emailRecipients = getEmailRecipients(CommonConstants.EMAIL_RECIPIENTS_CC);
				iter = emailRecipients.iterator();
				while (iter.hasNext()) {
					String toEmail = (String) iter.next();
					assert (CommonFormatValidator.isValidEmailAddress(toEmail)) == true : "Address must be a valid internet address";
					mailBean.addMailCc(toEmail);
				}
				// BCC Recipients
				emailRecipients = getEmailRecipients(CommonConstants.EMAIL_RECIPIENTS_BCC);
				iter = emailRecipients.iterator();
				while (iter.hasNext()) {
					String toEmail = (String) iter.next();
					assert (CommonFormatValidator.isValidEmailAddress(toEmail)) == true : "Address must be a valid internet address";
					mailBean.addMailBcc(toEmail);

				}

				mailBean.setMailFrom(ConfigContext
						.getProperty(CommonConstants.EMAIL_FROM));
				//String todayStr =
				// com.telus.collections.ggateway.util.DateUtil.formatDateToString(new
				// Date(),
				//		Gatewayproperties.getReportFolderDateformat());

				mailBean.setSubject(ConfigContext
						.getProperty(CommonConstants.EMAIL_SUBJECT));
				mailBean.setTextMsg(ConfigContext
						.getProperty(CommonConstants.EMAIL_BODY));
				//File[] list filereportdir.listFiles();
				for (File f : reportfiles)
				{
					if(f !=null)
				        mailBean.addFileAttachment(f.getAbsolutePath());
				}
				MailSender.send(mailBean);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeEnvironmentException(e);
			} catch (MailException e) {
				throw new RuntimeEnvironmentException(e);
			} catch (MessagingException e) {
				throw new RuntimeEnvironmentException(e);
			} catch (IOException e) {
				throw new RuntimeEnvironmentException(e);
			}
		}
	}

	/**
	 * get TO email(s) when emailing agency referral report
	 *
	 */
	private List getEmailRecipients(String[] type) {
		return ConfigContext.getList(type);
	}

	/**
	 * @return Returns the m_filePath.
	 */
	public String getM_filePath() {
		return m_filePath;
	}

	/**
	 * @param path
	 *            The m_filePath to set.
	 */
	public void setM_filePath(String path) {
		m_filePath = path;
	}

	public String getM_filePattern() {
		return m_filePattern;
	}

	public void setM_filePattern(String pattern) {
		m_filePattern = pattern;
	}

}