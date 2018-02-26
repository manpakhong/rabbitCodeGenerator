package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.apache.log4j.Logger;

import hk.ebsl.mfms.dao.SystemParamDao;
import hk.ebsl.mfms.email.EmailMessage;
import hk.ebsl.mfms.email.EmailSender;
import hk.ebsl.mfms.email.EmailTemplate;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.EmailManager;
import hk.ebsl.mfms.manager.SystemParamManager;

public class EmailManagerImpl implements EmailManager {

	public static final Logger logger = Logger.getLogger(EmailManagerImpl.class);

	private SystemParamManager systemParamManager;

	private boolean debug;

	private long interval;

	private Map<String, EmailTemplate> emailTemplates;

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public long getInterval() {
		return interval;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	@Override
	public void send(EmailMessage emailMessage, Integer siteKey) throws MFMSException {

		logger.debug("send()[" + emailMessage.getSubject() + "," + emailMessage.getContent() + "," + siteKey + "]");

		String defaultSender = getDefaultSenderAddress(siteKey);
		if (defaultSender == null) {
			defaultSender = getDefaultSenderAddress(1);
		}

		if (emailMessage.getSender() == null || StringUtils.isEmpty(emailMessage.getSender())) {
			emailMessage.setSender(defaultSender);
		}

		getEmailSender(siteKey).send(emailMessage, debug);

		try {
			Thread.sleep(interval);

			logger.debug("Wake! Email submitted successfully.");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getDefaultSenderAddress(Integer siteKey) throws MFMSException {
		return systemParamManager.getSystemParamValueByKey("mail.smtp.sender", siteKey);
	}

	private EmailSender getEmailSender(Integer siteKey) throws MFMSException {

		// get properties from system param

		boolean enabletls = true;

		Integer defaultSiteConfig = 1;

		String host = systemParamManager.getSystemParamValueByKey("mail.smtp.host", siteKey);
		if (host == null) {
			logger.debug("Using defaultSiteConfig");
			host = systemParamManager.getSystemParamValueByKey("mail.smtp.host", defaultSiteConfig);
		}

		String port = systemParamManager.getSystemParamValueByKey("mail.smtp.port", siteKey);
		if (port == null) {
			port = systemParamManager.getSystemParamValueByKey("mail.smtp.port", defaultSiteConfig);
		}

		String username = systemParamManager.getSystemParamValueByKey("mail.smtp.user", siteKey);
		if (username == null) {
			username = systemParamManager.getSystemParamValueByKey("mail.smtp.user", defaultSiteConfig);
		}

		String password = systemParamManager.getSystemParamValueByKey("mail.smtp.password", siteKey);
		if (password == null) {
			password = systemParamManager.getSystemParamValueByKey("mail.smtp.password", defaultSiteConfig);
		}

		EmailSender sender = new EmailSender(host, port, username, password, enabletls);
		return sender;
	}

	public void postInit() {

	}

	public Map<String, EmailTemplate> getEmailTemplates() {
		return emailTemplates;
	}

	public void setEmailTemplates(Map<String, EmailTemplate> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}

	@Override
	public EmailTemplate getEmailTemplateById(String templateId) throws MFMSException {
		if (emailTemplates != null) {
			return emailTemplates.get(templateId);
		} else {
			return null;
		}
	}

	@Override
	public void sendTemplate(String templateId, Integer siteKey, String[] recipients, Object[] param)
			throws MFMSException {

		EmailTemplate template = getEmailTemplateById(templateId);

		if (template == null) {
			logger.debug("Cannot find template: " + templateId);
			throw new MFMSException("Failed to find template.");
		} else {
			logger.debug("Template found.");
			// format template
			EmailMessage message = template.formatEmail(param);
			message.setRecipient(Arrays.asList(recipients));

			send(message, siteKey);
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void sendTemplateAsync(String templateId, final Integer siteKey, String[] recipients, Object[] param)
			throws MFMSException {

		EmailTemplate template = getEmailTemplateById(templateId);

		if (template == null) {
			logger.debug("Cannot find template: " + templateId);
			throw new MFMSException("Failed to find template.");
		} else {
			logger.debug("Template found.");
			// format template
			final EmailMessage message = template.formatEmail(param);
			message.setRecipient(Arrays.asList(recipients));

			Runnable r = new Runnable() {
				public void run() {
					try {
						send(message, siteKey);
					} catch (MFMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};

			new Thread(r).start();

		}
	}

	public SystemParamManager getSystemParamManager() {
		return systemParamManager;
	}

	public void setSystemParamManager(SystemParamManager systemParamManager) {
		this.systemParamManager = systemParamManager;
	}

}
