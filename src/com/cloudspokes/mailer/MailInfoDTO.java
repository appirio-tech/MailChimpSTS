package com.cloudspokes.mailer;

import java.util.List;

/**
 * @author Shashidhar Gurumurthy
 * This is a data transfer object containing all the data required to send a mail. 
 */
public class MailInfoDTO {
	//The first five fields are minimal required fields to send a mail
	String htmlEmailContent;
	String subject;
	String fromName;
	String fromEmail;
	List<String> toEmail;
	List<String> toName;

	//Optional fields 
	String textEmailContent;
	List<String> replyTo;
	List<String> ccEmail;
	List<String> ccName;
	List<String> bccEmail;
	List<String> bccName;
	boolean autogenHtml = true;
	boolean trackOpens;
	boolean trackClicks;
	List<String> tags;
	
	/**
	 * enum to flag the type of List being validated. Emails & tags cannot be blank but names can be blank. 
	 */
	enum ParmType {EMAIL_OR_TAG, NAME};
	
	/**
	 * @param htmlMailContent
	 * @param subject
	 * @param fromName
	 * @param fromEmail
	 * @param toEmailAddresses
	 * @param toName - List of names corresponding to each of the toEmailAddresses. If null, this data is not used. 
	 * @throws MailerException if 
	 * 1. Any of the elements is null or spaces except toName list. toName list can be null but if provided should have 
	 *    the same number of elements as the toEmailAddresses list.  
	 * 2. toEmailAddresses list contains zero elements.
	 * 3. toName list is non null and does not contain same number of elements as toEmailAddresses list.
	 * 
	 * Constructor to create the MailInfoDTO with the minimum set of required
	 * fields to send mail.   
	 */
	public MailInfoDTO(String htmlMailContent, String subject, String fromName, 
			String fromEmail, List<String> toEmailAddresses, List<String> toName) throws MailerException {
		StringBuilder exceptionMessage = new StringBuilder();
		
		if (isNullOrBlank(htmlMailContent)) {
			exceptionMessage = append(exceptionMessage, "String htmlMailContent is null or blank");
		} else {
			this.htmlEmailContent = htmlMailContent;
		}
		if (isNullOrBlank(subject)) {
			exceptionMessage = append(exceptionMessage, "String subject is null or blank");
		} else {
			this.subject = subject;
		}
		if (isNullOrBlank(fromName)) {
			exceptionMessage = append(exceptionMessage, "String fromName is null or blank");
		} else {
			this.fromName = fromName;
		}
		if (isNullOrBlank(fromEmail)) {
			exceptionMessage = append(exceptionMessage, "String fromEmail is null or blank");
		} else {
			this.fromEmail = fromEmail;
		}
		if (isNullOrBlank(toEmailAddresses, ParmType.EMAIL_OR_TAG)) {
			exceptionMessage = append(exceptionMessage, "List<String> toEmailAddresses is null or contains null or blank email addresses");
		} else {
			//At least one email address should be provided
			if (toEmailAddresses.size() < 1) {
				exceptionMessage = append(exceptionMessage, "List<String> toEmailAddresses should contain at least one valid email address");
			} else if (toEmailAddresses.size() > 50) {
				exceptionMessage = append(exceptionMessage, "List<String> toEmailAddresses cannot have more than 50 email addresses. Count: " + toEmailAddresses.size());
			} else {
				this.toEmail = toEmailAddresses;

				//Validate toNames if not null
				if (null != toName) {
					if (toName.size() != toEmailAddresses.size()) {
						exceptionMessage = append(exceptionMessage, "toEmailAddresses.size():" + toEmailAddresses.size() + " != " + 
								"toName.size():" + toName.size());
					} else {
						if (isNullOrBlank(toName, ParmType.NAME)) {
							exceptionMessage = append(exceptionMessage, "List<String> toName contains null names");
						} else {
							this.toName = toName;
						}
					}  
				}
			}
		}

		//If there are any errors, raise an exception
		if (exceptionMessage.length() != 0) {
			throw new MailerException(exceptionMessage.toString());
		}
	}

	/**
	 * @param htmlEmailContent
	 * @param subject
	 * @param fromName
	 * @param fromEmail
	 * @param toEmail
	 * @param textEmailContent
	 * @param replyTo
	 * @param toName
	 * @param ccEmail
	 * @param ccName
	 * @param bccEmail
	 * @param bccName
	 * @param autogenHtml
	 * @param trackOpens
	 * @param trackClicks
	 * @param tags
	 * 
	 * Constructor to create MailInfoDTO with values for all data elements. This constructor delegates to the previous
	 * constructor to create the object with minimal required values and then initializes the optional data elements. 
	 */
	public MailInfoDTO(String htmlEmailContent, String subject,
			String fromName, String fromEmail, List<String> toEmail, List<String> toName, 
			String textEmailContent, List<String> replyTo,
			List<String> ccEmail, List<String> ccName, List<String> bccEmail,
			List<String> bccName, boolean autogenHtml, boolean trackOpens,
			boolean trackClicks, List<String> tags) throws MailerException {
		
		//Initialize with the minimum required fields
		this(htmlEmailContent, subject, fromName, fromEmail, toEmail, toName);
		
		//If the creation of the DTO with the minimum required fields succeeds, then check if remaining data is valid
		StringBuilder exceptionMessage = new StringBuilder();
		
		//No need to check for validity of the following 4 data elements
		this.textEmailContent = textEmailContent;
		this.autogenHtml = autogenHtml;
		this.trackOpens = trackOpens;
		this.trackClicks = trackClicks;

		//If replyTo is non null check if it contains any null or blank email addresses.
		if (replyTo != null) {
			if (isNullOrBlank(replyTo, ParmType.EMAIL_OR_TAG)) {
				exceptionMessage = append(exceptionMessage, "List<String> replyTo contains null or blank email addresses");
			} else {
				this.replyTo = replyTo;
			}
		}
		
		//If ccEmail is non null check if it contains any null or blank email messages or if it has more than 50 email addresses
		if (ccEmail != null) {
			if (ccEmail.size() > 50) {
				exceptionMessage = append(exceptionMessage, "List<String> ccEmail cannot have more than 50 email addresses. Count: " + ccEmail.size());
			} else if (isNullOrBlank(ccEmail, ParmType.EMAIL_OR_TAG)) {
				exceptionMessage = append(exceptionMessage, "List<String> ccEmail contains null or blank email addresses");
			} else {
				this.ccEmail = ccEmail;
				
				//Validate ccName if not null
				if (null != ccName) {
					if (ccName.size() != ccEmail.size()) {
						exceptionMessage = append(exceptionMessage, "ccEmail.size():" + ccEmail.size() + " != " + 
								"ccName.size():" + ccName.size());
					} else {
						if (isNullOrBlank(ccName, ParmType.NAME)) {
							exceptionMessage = append(exceptionMessage, "List<String> ccName contains null names");
						} else {
							this.ccName = ccName;
						}
					}  
				}
			}
		}
		
		//If bccEmail is non null check if it contains any null or blank email messages or if it has more than 50 email addresses
		if (bccEmail != null) {
			if (bccEmail.size() > 50) {
				exceptionMessage = append(exceptionMessage, "List<String> bccEmail cannot have more than 50 email addresses. Count: " + bccEmail.size());
			} else if (isNullOrBlank(bccEmail, ParmType.EMAIL_OR_TAG)) {
				exceptionMessage = append(exceptionMessage, "List<String> bccEmail contains null or blank email addresses");
			} else {
				this.bccEmail = bccEmail;
				
				//Validate bccName if not null
				if (null != bccName) {
					if (bccName.size() != bccEmail.size()) {
						exceptionMessage = append(exceptionMessage, "bccEmail.size():" + bccEmail.size() + " != " + 
								"bccName.size():" + bccName.size());
					} else {
						if (isNullOrBlank(bccName, ParmType.NAME)) {
							exceptionMessage = append(exceptionMessage, "List<String> bccName contains null names");
						} else {
							this.bccName = bccName;
						}
					}  
				}
			}
		}

		//If tags is non null check if it contains any null or blank email addresses. Also check if any tag is greater 
		//than 50 characters or starting with _. 
		if (tags != null) {
			if (isNullOrBlank(tags, ParmType.EMAIL_OR_TAG)) {
				exceptionMessage = append(exceptionMessage, "List<String> tags contains null or blank tag");
			} else {
				if (tags.size() > 50) {
					exceptionMessage = append(exceptionMessage, "List<String> tags contains more than 50 tags. Count: " + tags.size());
				} else {
					boolean invalidTag = false;
					for (String tag : tags) {
						if (tag.startsWith("_")) {
							invalidTag = true;
							exceptionMessage = append(exceptionMessage, "List<String> tags contains tag starting with _. Tag: " + tag);
							break;
						}
					}
					if (!invalidTag) {
						this.tags = tags;
					}
				}
			}
		}

		//If there are any errors, raise an exception
		if (exceptionMessage.length() != 0) {
			throw new MailerException(exceptionMessage.toString());
		}
	}

	//Getters and setters for all fields follow
	public String getHtmlEmailContent() {
		return htmlEmailContent;
	}

	public void setHtmlEmailContent(String htmlEmailContent) {
		this.htmlEmailContent = htmlEmailContent;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public List<String> getToEmail() {
		return toEmail;
	}

	public void setToEmail(List<String> toEmail) {
		this.toEmail = toEmail;
	}

	public String getTextEmailContent() {
		return textEmailContent;
	}

	public void setTextEmailContent(String textEmailContent) {
		this.textEmailContent = textEmailContent;
	}

	public List<String> getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(List<String> replyTo) {
		this.replyTo = replyTo;
	}

	public List<String> getToName() {
		return toName;
	}

	public void setToName(List<String> toName) {
		this.toName = toName;
	}

	public List<String> getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(List<String> ccEmail) {
		this.ccEmail = ccEmail;
	}

	public List<String> getCcName() {
		return ccName;
	}

	public void setCcName(List<String> ccName) {
		this.ccName = ccName;
	}

	public List<String> getBccEmail() {
		return bccEmail;
	}

	public void setBccEmail(List<String> bccEmail) {
		this.bccEmail = bccEmail;
	}

	public List<String> getBccName() {
		return bccName;
	}

	public void setBccName(List<String> bccName) {
		this.bccName = bccName;
	}

	public boolean isAutogenHtml() {
		return autogenHtml;
	}

	public void setAutogenHtml(boolean autogenHtml) {
		this.autogenHtml = autogenHtml;
	}

	public boolean isTrackOpens() {
		return trackOpens;
	}

	public void setTrackOpens(boolean trackOpens) {
		this.trackOpens = trackOpens;
	}

	public boolean isTrackClicks() {
		return trackClicks;
	}

	public void setTrackClicks(boolean trackClicks) {
		this.trackClicks = trackClicks;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Returns true if the String parameter is null or blank. 
	 * @param parm - A String parameter
	 * @return true if input String parm is null or blank and false otherwise. 
	 */
	private boolean isNullOrBlank(String parm) {
		boolean returnVal = false;
		
		if (null == parm || parm.equals("")) {
			returnVal = true;
		}
		
		return (returnVal);
	}

	/**
	 * For email and tag lists, returns true if any of the elements are blank. For name lists, blanks are allowed
	 * and hence false is returned even if any of the name elements are blank.   
	 * @param parm - A list of Strings parameter
	 * @param parmType - specifies whether the parm is an email or name
	 * @return true if input List is null or if any of its elements is null.
	 */
	private boolean isNullOrBlank(List<String> parm, ParmType parmType) {
		boolean returnVal = false;
		
		if (null == parm) {
			returnVal = true;
		} else {
			for (String parmElement : parm) {
				//An email cannot be null or blank but name can be blank but not null. 
				if (parmType == ParmType.EMAIL_OR_TAG) {
					returnVal = isNullOrBlank(parmElement);
				} else {
					returnVal = (parmElement == null);
				}
				
				//If a empty or null value found, break out of the loop
				if (returnVal) {
					break;
				}
			}
		}
		
		return (returnVal);
	}
	
	/**
	 * Utility method to comma separate multiple error messages 
	 * @param exceptionMessage - current message
	 * @param message - additional message to be added
	 * @return - concatenated error message
	 */
	private StringBuilder append(StringBuilder exceptionMessage, String message) {
		if (exceptionMessage.length() == 0) {
			return (exceptionMessage.append(message));
		} else {
			return (exceptionMessage.append(", " + message));
		}
	}
}
