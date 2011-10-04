package com.cloudspokes.mailer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shashidhar Gurumurthy
 * This is the main class used to send mail. 
 * It uses the Mailchimp URL and constructs the form url encoded post parameters to send the message. 
 * It makes the following assumptions: 
 * 1. The Mailchimp API Key is registered in the system environment under the key - MAILCHIMP_API_KEY. 
 */
public class AmazonSESMailChimpMailerImpl implements AmazonSESMailChimpMailer {
	private final String SINGLE_PARM_VALUE_PATTERN = "%s=%s&";
	private final String SERVER = "us2";
	private final String SEND_MAIL_URL = "http://" + SERVER
			+ ".sts.mailchimp.com/1.0/SendEmail";
	private final String API_KEY_ENV_VARIABLE = "MAILCHIMP_API_KEY";

	private final String KEY_APIKEY = "apikey";
	private final String KEY_MESG_HTML = "message[html]";
	private final String KEY_MESG_TEXT = "message[text]";
	private final String KEY_MESG_SUBJECT = "message[subject]";
	private final String KEY_MESG_FROM_NAME = "message[from_name]";
	private final String KEY_MESG_FROM_EMAIL = "message[from_email]";
	private final String KEY_MESG_REPLY_TO = "message[reply_to]";
	private final String KEY_MESG_TO_EMAIL = "message[to_email]";
	private final String KEY_MESG_TO_NAME = "message[to_name]";
	private final String KEY_MESG_CC_EMAIL = "message[cc_email]";
	private final String KEY_MESG_CC_NAME = "message[cc_name]";
	private final String KEY_MESG_BCC_EMAIL = "message[bcc_email]";
	private final String KEY_MESG_BCC_NAME = "message[bcc_name]";
	private final String KEY_AUTOGEN_HTML = "message[autogen_html]";
	private final String KEY_TRACK_OPENS = "track_opens";
	private final String KEY_TRACK_CLICKS = "track_clicks";
	private final String KEY_TAGS = "tags";

	/* (non-Javadoc)
	 * @see com.cloudspokes.mailer.AmazonSESMailChimpMailer#sendMail(com.cloudspokes.mailer.MailInfoDTO)
	 */
	@Override
	public String sendMail(MailInfoDTO mailData) throws MailerException {
		String apiKey = System.getenv(API_KEY_ENV_VARIABLE);
		String sendStatusMessage = null;
		
		if (apiKey == null) {
			throw new MailerException("Mail Chimp API KEY is not bound to System Environment Variable: " + API_KEY_ENV_VARIABLE);
		}
		
		try {
			String postData = getPostString(mailData, apiKey);
			sendStatusMessage = httpPost(SEND_MAIL_URL, postData);
		} catch (UnsupportedEncodingException e) {
			throw new MailerException(e.getMessage(), e);
		} catch (IOException e) {
			throw new MailerException(e.getMessage(), e);
		}
		
		return (sendStatusMessage);
	}

	/**
	 * This is a utility method to serialize the MailInfoDTO to a URL Encoded
	 * post data
	 * 
	 * @param mailData
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	private String getPostString(MailInfoDTO mailData, String apiKey) throws UnsupportedEncodingException {
		String postData = null;

		Map<String, String> stringData = new HashMap<String, String>();
		Map<String, List<String>> listData = new HashMap<String, List<String>>();

		stringData.put(KEY_APIKEY, apiKey);
		stringData.put(KEY_MESG_HTML, mailData.getHtmlEmailContent());
		stringData.put(KEY_MESG_TEXT, mailData.getTextEmailContent());
		stringData.put(KEY_MESG_SUBJECT, mailData.getSubject());
		stringData.put(KEY_MESG_FROM_NAME, mailData.getFromName());
		stringData.put(KEY_MESG_FROM_EMAIL, mailData.getFromEmail());
		stringData.put(KEY_AUTOGEN_HTML, String.valueOf(mailData
				.isAutogenHtml()));
		stringData
				.put(KEY_TRACK_OPENS, String.valueOf(mailData.isTrackOpens()));
		stringData.put(KEY_TRACK_CLICKS, String.valueOf(mailData
				.isTrackClicks()));

		listData.put(KEY_MESG_REPLY_TO, mailData.getReplyTo());
		listData.put(KEY_MESG_TO_EMAIL, mailData.getToEmail());
		listData.put(KEY_MESG_TO_NAME, mailData.getToName());
		listData.put(KEY_MESG_CC_EMAIL, mailData.getCcEmail());
		listData.put(KEY_MESG_CC_NAME, mailData.getCcName());
		listData.put(KEY_MESG_BCC_EMAIL, mailData.getBccEmail());
		listData.put(KEY_MESG_BCC_NAME, mailData.getBccName());
		listData.put(KEY_TAGS, mailData.getTags());

		return (processStringParms(stringData) + processListParms(listData));
	}

	/**
	 * This is a utility method to create a form url encoded post data. Any parameters with null values are ignored. 
	 * @param stringData - Map containing parameter keys and corresponding values.
	 * @return encoded post data for all the String parameters passed in. 
	 * @throws UnsupportedEncodingException
	 */
	private String processStringParms(Map<String, String> stringData)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();

		for (String key : stringData.keySet()) {
			if (stringData.get(key) != null) {
				sb.append(String.format(SINGLE_PARM_VALUE_PATTERN, key,
						URLEncoder.encode(stringData.get(key), "UTF-8")));
			}
		}

		return (sb.toString());
	}

	/**
	 * This is a utility method to create a form url encoded post data. Any parameters with null values are ignored. 
	 * @param listData - Map containing parameter keys and corresponding list values.
	 * @return encoded post data for all the List parameters passed in. 
	 * @throws UnsupportedEncodingException
	 */
	private String processListParms(Map<String, List<String>> listData)
			throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();

		for (String key : listData.keySet()) {
			List<String> data = listData.get(key);
			if (data != null) {
				for (int i = 0; i < data.size(); i++) {
					sb.append(String.format(SINGLE_PARM_VALUE_PATTERN, key
							+ "[" + i + "]", URLEncoder.encode(data.get(i),
							"UTF-8")));
				}
			}
		}

		return (sb.toString());
	}

	/**
	 * Utility method to take an URL String and corresponding postData and post the same to the URL. 
	 * @param urlStr 
	 * @param postData
	 * @return status of the call
	 * @throws IOException
	 */
	private String httpPost(String urlStr, String postData) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");

		OutputStream out = conn.getOutputStream();
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		writer.write(postData);
		writer.close();
		out.close();

		if (conn.getResponseCode() != 200) {
			System.out.println(conn.getResponseCode());
			System.out.println(postData);
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		return sb.toString();
	}
}
