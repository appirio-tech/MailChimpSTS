package com.cloudspokes.mailer;

/**
 * @author Shashidhar Gurumurthy
 * Exception class which wraps all exceptions thrown within the Mailer system
 */
public class MailerException extends Exception {
	private static final long serialVersionUID = -45905236916227922L;

	String message;
	Throwable cause;

	public MailerException() {
		super();
	}
	public MailerException(String message, Throwable cause) {
		super(message, cause);
		this.cause = cause;
		this.message = message;
	}
	public MailerException(String message) {
		super(message);
		this.message = message;
	}
}
