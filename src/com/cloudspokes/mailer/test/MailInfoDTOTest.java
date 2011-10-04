package com.cloudspokes.mailer.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.cloudspokes.mailer.MailInfoDTO;
import com.cloudspokes.mailer.MailerException;

/**
 * JUnit test class to test MailInfoDTO class methods. 
 * @author Shashidhar Gurumurthy
 */
public class MailInfoDTOTest extends TestCase {
	private final String ALL_NULLS_MESSAGE = "String htmlMailContent is null or blank, String subject is null or blank, String fromName is null or blank, String fromEmail is null or blank, List<String> toEmailAddresses is null or contains null or blank email addresses";
	private final String SUBJECT_NULL_MESSAGE = "String subject is null or blank";
	private final String SUBJECT_SENDER_EMAIL_NULL_MESSAGE = "String subject is null or blank, String fromEmail is null or blank";
	private final String TO_EMAIL_CONTAINS_NULL = "List<String> toEmailAddresses is null or contains null or blank email addresses";
	private final String TO_MAIL_ADDRESS_AND_NAME_SIZE_MISMATCH = "toEmailAddresses.size():1 != toName.size():0";
	private final String EXCESS_EMAIL_ADDRESSES_51 = "List<String> toEmailAddresses cannot have more than 50 email addresses. Count: 51";
	private final String NULL_NAME_MESSAGE = "List<String> toName contains null names";
	private final String INVALID_REPLY_TO_MESSAGE = "List<String> replyTo contains null or blank email addresses";
	private final String CC_EMAIL_COUNT_51 = "List<String> ccEmail cannot have more than 50 email addresses. Count: 51";
	private final String CC_MAIL_ADDRESS_AND_NAME_SIZE_MISMATCH = "ccEmail.size():1 != ccName.size():0";
	private final String BCC_MAIL_ADDRESS_AND_NAME_SIZE_MISMATCH = "bccEmail.size():1 != bccName.size():0";
	private final String TAG_MESG_NULL_OR_BLANK = "List<String> tags contains null or blank tag";
	private final String TAG_MESG_MORE_THAN_50_TAGS = "List<String> tags contains more than 50 tags. Count: 100";
	private final String TAG_MESG_INVALID_TAG = "List<String> tags contains tag starting with _. Tag: _invalidTag";
	
	private final List<String> EMPTY_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> FIFTY_ONE_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> ONE_EMAIL_ADDRESSES_LIST = new ArrayList<String>();
	private final List<String> TO_ADDRESSES_LIST_WITH_NULL = new ArrayList<String>();
	private final List<String> EMPTY_NAME_LIST = new ArrayList<String>();
	private final List<String> ONE_NAME_LIST = new ArrayList<String>();
	private final List<String> ONE_NAME_LIST_WITH_BLANK = new ArrayList<String>();
	private final List<String> ONE_NAME_LIST_WITH_NULL = new ArrayList<String>();
	private final String HTML_EMAIL_CONTENT = "<h1>This is a test mail</h1>";
	private final String SUBJECT = "This is the mail subject line";
	private final String ONE_EMAIL_ADDRESS = "as@as.com";
	private final String MAIL_SENDER = "Mail Sender";
	private final List<String> TAGS = new ArrayList<String>();
	
	protected void setUp() throws Exception {
		super.setUp();
		ONE_EMAIL_ADDRESSES_LIST.add("as@as.com");
		ONE_NAME_LIST.add("Just Me");
		ONE_NAME_LIST_WITH_BLANK.add("");
		ONE_NAME_LIST_WITH_NULL.add(null);

		TO_ADDRESSES_LIST_WITH_NULL.add("nonnull@email.com");
		TO_ADDRESSES_LIST_WITH_NULL.add(null);
		
		for (int i = 0; i <= 50; i++) {
			FIFTY_ONE_EMAIL_ADDRESSES_LIST.add("as" + i + "@as.com");
		}
	}
	
	/**
	 * Tests the minimum required data constructor of MailInfoDTO. 
	 */
	public void testMailInfoDTOStringStringStringStringListOfStringListOfString() {
		//Attempt to create MailInfoDTO with all nulls
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(null, null, null, null, null, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(ALL_NULLS_MESSAGE, me.getMessage());
		}

		//Attempt to create MailInfoDTO with only subject as null
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, null, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(SUBJECT_NULL_MESSAGE, me.getMessage());
		}

		//Attempt to create MailInfoDTO with two element blank or null - subject and sender email address
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, "", MAIL_SENDER, null, ONE_EMAIL_ADDRESSES_LIST, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(SUBJECT_SENDER_EMAIL_NULL_MESSAGE, me.getMessage());
		}

		//Attempt to create MailInfoDTO with two elements blank or null - subject and sender email address
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, "", MAIL_SENDER, null, ONE_EMAIL_ADDRESSES_LIST, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(SUBJECT_SENDER_EMAIL_NULL_MESSAGE, me.getMessage());
		}

		//Attempt to create MailInfoDTO with a null email address in toEmailAddresses list. 
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, TO_ADDRESSES_LIST_WITH_NULL, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(TO_EMAIL_CONTAINS_NULL, me.getMessage());
		}
		//Attempt to create MailInfoDTO with a non null name list and different number of elements than the email address list
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, EMPTY_NAME_LIST);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(TO_MAIL_ADDRESS_AND_NAME_SIZE_MISMATCH, me.getMessage());
		}
		//Attempt to create MailInfoDTO with a non null name list but with null names
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_NULL);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(NULL_NAME_MESSAGE, me.getMessage());
		}
		//Attempt to create MailInfoDTO with valid data - name list null 
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, null);
			assertTrue(true);
		} catch(MailerException me) {
			//Fail if exception raised
			assertTrue(false);
		}
		//Attempt to create MailInfoDTO with valid data - name list non null
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST);
			assertTrue(true);
		} catch(MailerException me) {
			//Fail if exception raised
			assertTrue(false);
		}
		//Attempt to create MailInfoDTO with valid data - name list non null and blank name
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK);
			assertTrue(true);
		} catch(MailerException me) {
			//Fail if exception raised
			assertTrue(false);
		}
		//Attempt to create MailInfoDTO with 51 email addresses in the toEmailAddresses list
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, FIFTY_ONE_EMAIL_ADDRESSES_LIST, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(EXCESS_EMAIL_ADDRESSES_51, me.getMessage());
		}
	}

	/**
	 * Tests the all data constructor of MailInfoDTO. 
	 */
	public void testMailInfoDTOAllElements() {
		//Attempt to create MailInfoDTO with valid data for minimum required fields and nulls for all non required fields
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, null, null, null, null, null, false, false, false, null);
		} catch(MailerException me) {
			//Fail if exception raised
			assertTrue(false);
		}

		//Attempt to create MailInfoDTO with blank subject
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, "", MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, null, null, null, null, null, false, false, false, null);
		} catch(MailerException me) {
			assertEquals(SUBJECT_NULL_MESSAGE, me.getMessage());
		}

		//Attempt to create MailInfoDTO with non null replyTo but blank email address
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, TO_ADDRESSES_LIST_WITH_NULL, null, null, null, null, false, false, false, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(INVALID_REPLY_TO_MESSAGE, me.getMessage());
		}

		//Attempt to create MailInfoDTO with valid replyTo and ccEmail with more than 50 email addresses
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, ONE_EMAIL_ADDRESSES_LIST, FIFTY_ONE_EMAIL_ADDRESSES_LIST, null, null, null, false, false, false, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(CC_EMAIL_COUNT_51, me.getMessage());
		}

		//Attempt to create MailInfoDTO with 1 email in ccEmail and empty ccName
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, ONE_EMAIL_ADDRESSES_LIST, ONE_EMAIL_ADDRESSES_LIST, EMPTY_NAME_LIST, null, null, false, false, false, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(CC_MAIL_ADDRESS_AND_NAME_SIZE_MISMATCH, me.getMessage());
		}

		//Attempt to create MailInfoDTO with 1 email in bccEmail and empty bccName
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, ONE_EMAIL_ADDRESSES_LIST, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST, ONE_EMAIL_ADDRESSES_LIST, EMPTY_NAME_LIST, false, false, false, null);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(BCC_MAIL_ADDRESS_AND_NAME_SIZE_MISMATCH, me.getMessage());
		}
		
		//Tags tests
		//Attempt to create MailInfoDTO with empty tags
		TAGS.add("");
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, ONE_EMAIL_ADDRESSES_LIST, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST, null, null, false, false, false, TAGS);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(TAG_MESG_NULL_OR_BLANK, me.getMessage());
		}
		//Attempt to create MailInfoDTO with invalid tags
		TAGS.remove(0);
		TAGS.add("_invalidTag");
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, ONE_EMAIL_ADDRESSES_LIST, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST, null, null, false, false, false, TAGS);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(TAG_MESG_INVALID_TAG, me.getMessage());
		}

		//Attempt to create MailInfoDTO with more than 50 tags
		TAGS.remove(0);
		for (int i = 0; i < 100; i++) {
			TAGS.add("Tag" + i);
		}
		try {
			MailInfoDTO mailInfoDTO = new MailInfoDTO(HTML_EMAIL_CONTENT, SUBJECT, MAIL_SENDER, ONE_EMAIL_ADDRESS, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST_WITH_BLANK,
					null, ONE_EMAIL_ADDRESSES_LIST, ONE_EMAIL_ADDRESSES_LIST, ONE_NAME_LIST, null, null, false, false, false, TAGS);
			assertTrue(false);
		} catch(MailerException me) {
			assertEquals(TAG_MESG_MORE_THAN_50_TAGS, me.getMessage());
		}
	}

}
