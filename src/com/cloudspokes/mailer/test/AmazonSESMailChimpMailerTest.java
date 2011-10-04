package com.cloudspokes.mailer.test;

import java.util.ArrayList;

import java.util.List;

import com.cloudspokes.mailer.AmazonSESMailChimpMailer;
import com.cloudspokes.mailer.AmazonSESMailChimpMailerImpl;
import com.cloudspokes.mailer.MailInfoDTO;
import com.cloudspokes.mailer.MailerException;

import junit.framework.TestCase;

/**
 * @author Shashidhar Gurumurthy
 * Test class for testing AmazonSESMailChimpMailer sendMail. 
 */
public class AmazonSESMailChimpMailerTest extends TestCase {
	private final String HTML_EMAIL_CONTENT = "<h1><font color=\"green\">This is a mail from JUnit test class.</font></h1>";
	private final String SUBJECT = "Mail from JUnit";
	private final String FROM_EMAIL_ADDRESS = "shashidhar.gurumurthy@gmail.com";
	private final String MAIL_SENDER_NAME = "Shashi via JUnit";
	private final List<String> TO_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> TO_NAME_LIST = new ArrayList<String>();
	private final List<String> CC_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> CC_NAME_LIST = new ArrayList<String>();
	private final List<String> BCC_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> BCC_NAME_LIST = new ArrayList<String>();
	private final List<String> REPLYTO_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> TAGS = new ArrayList<String>();
	
	private static final String SENT_SUCCESS_MESSAGE = "\"status\":\"sent\"";
	private static final String SENT_QUEUED_MESSAGE = "\"status\":\"queued\"";
	private static final String MESSAGE_QUOTA_EXCEEDED = "\"status\":\"Daily message quota exceeded.\"";

	protected void setUp() throws Exception {
		super.setUp();
		for (int i = 0; i < 1; i++) {
			TO_EMAIL_ADDRESSES_LIST.add("shashidhar.gurumurthy@gmail.com");
			TO_NAME_LIST.add("Shashi-" + i);
		}

	}

	protected void tearDown() throws Exception {
		super.tearDown();
		for (int i = 0; i < 1; i++) {
			TO_EMAIL_ADDRESSES_LIST.remove(0);
			TO_NAME_LIST.remove(0);
		}

	}

	public void testSendMail() {
		//Send mail with minimum set of data
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER_NAME, FROM_EMAIL_ADDRESS, TO_EMAIL_ADDRESSES_LIST, TO_NAME_LIST);
			AmazonSESMailChimpMailer mailer = new AmazonSESMailChimpMailerImpl();
			String sendStatusMessage = mailer.sendMail(mailInfoDTO);
			assertTrue(true);
			assertTrue(sendStatusMessage.indexOf(SENT_SUCCESS_MESSAGE) != -1);
		} catch(MailerException me) {
			me.printStackTrace();
			//Fail if exception raised
			assertTrue(false);
		}
		
		//Send mail with all data
		try {
			addData();
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER_NAME, FROM_EMAIL_ADDRESS, TO_EMAIL_ADDRESSES_LIST, TO_NAME_LIST,
					"Text Email Content", REPLYTO_EMAIL_ADDRESSES_LIST, CC_EMAIL_ADDRESSES_LIST, CC_NAME_LIST, BCC_EMAIL_ADDRESSES_LIST, BCC_NAME_LIST, false, false, false, TAGS);
			AmazonSESMailChimpMailer mailer = new AmazonSESMailChimpMailerImpl();
			String sendStatusMessage = mailer.sendMail(mailInfoDTO);
			assertTrue(true);
			System.out.println(sendStatusMessage);
			assertTrue(sendStatusMessage.indexOf(SENT_SUCCESS_MESSAGE) != -1 ||
					sendStatusMessage.indexOf(SENT_QUEUED_MESSAGE) != -1);
		} catch(MailerException me) {
			me.printStackTrace();
			//Fail if exception raised
			assertTrue(false);
		}

	}
	
	private void addData() {
		REPLYTO_EMAIL_ADDRESSES_LIST.add("shashidhar.gurumurthy@gmail.com");
		
		for (int i = 0; i < 1; i++) {
			TO_EMAIL_ADDRESSES_LIST.add("shashidhar.gurumurthy@gmail.com");
			CC_EMAIL_ADDRESSES_LIST.add("shashidhar.gurumurthy@gmail.com");
			BCC_EMAIL_ADDRESSES_LIST.add("shashidhar.gurumurthy@gmail.com");
			TO_NAME_LIST.add("Shashi-" + i);
			CC_NAME_LIST.add("Shashi-" + i);
			BCC_NAME_LIST.add("Shashi-" + i);
			TAGS.add("SomeTag-"+i);
		}
	}
}
