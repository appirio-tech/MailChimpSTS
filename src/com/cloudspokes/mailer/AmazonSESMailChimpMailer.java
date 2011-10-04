package com.cloudspokes.mailer;

/**
 * @author Shashidhar Gurumurthy
 * 
 */
public interface AmazonSESMailChimpMailer {
	/**
	 * This method takes the data in the parameter mailData and uses the Mail Chimp API 
	 * to send a mail. 
	 * @param mailData
	 * @return
	 * @throws MailerException if the Mail Chimp API Key is not registered in the environment variable, 
	 *         if there is any IO Exception and if the Mail Chimp API call results in any error. 
	 */
	public String sendMail(MailInfoDTO mailData) throws MailerException;
}
