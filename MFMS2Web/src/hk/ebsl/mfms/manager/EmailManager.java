package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.email.EmailMessage;
import hk.ebsl.mfms.email.EmailTemplate;
import hk.ebsl.mfms.exception.MFMSException;

import javax.mail.Message;

public interface EmailManager {

	public void send(EmailMessage emailMessage, Integer siteKey) throws MFMSException;
	
	public void sendTemplate(String templateId, Integer siteKey, String[] recipients, Object[] param) throws MFMSException;
	
	public void sendTemplateAsync(String templateId, Integer siteKey, String[] recipients, Object[] param) throws MFMSException;
	
	public EmailTemplate getEmailTemplateById(String templateId) throws MFMSException;
}
