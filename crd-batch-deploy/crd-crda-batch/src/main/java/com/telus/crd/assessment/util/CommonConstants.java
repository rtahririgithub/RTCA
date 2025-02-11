/*
 * Created on Jun 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.telus.crd.assessment.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author x089759
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CommonConstants {


    	public static String VALUE = "Value";
    	public static String ATTRIBUTE = "Attribute";
    	public static String DATETIME = "datetime";
    	public static String MULTI = "multi";
    	public static String CUSTCONTACT_PHONE = "CustContactPhone";
    	public static String CALL_DATETIME = "CallDateTime";
    	public static String MEMO_MESSAGE = "MemoMessage";
		public static String FEEDBACK = "FeedBack";
		public static String CAR_DATEFORMAT = "yyyy/MM/dd";

		/** FROM EMAIL WHEN SENDING AGENCY REFERRAL REPORTS */
	    public static final String[] EMAIL_FROM  = new String[] {
	            "mail", "email_From" };

	    /** EMAIL SUBJECT WHEN SENDING AGENCY REFERRAL REPORTS */
	    public static final String[] EMAIL_SUBJECT  = new String[] {
	            "mail", "email_subject" };

	    /** EMAIL BODY WHEN SENDING AGENCY REFERRAL REPORTS */
	    public static final String[] EMAIL_BODY  = new String[] {
	            "mail", "email_Body" };

	   public static final String[] EMAIL_RECIPIENTS_TO =
	        new String[]{"mail","emailiRecipients_TO"};

	    public static final String[] EMAIL_RECIPIENTS_CC =
	        new String[]{"mail","emailiRecipients_CC"};

	    public static final String[] EMAIL_RECIPIENTS_BCC =
	        new String[]{"mail","emailiRecipients_BCC"};

}
