package hk.ebsl.mfms.email;


import hk.ebsl.mfms.exception.MFMSException;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

public class EmailSender {

	public static final Logger logger = Logger.getLogger(EmailSender.class);
	
	private String host;
	
	private String port;
	
	private String username;
	
	private String password;
	
	private boolean auth = false;
	
	private boolean enabletls = false;
	
	
	public EmailSender(String host, String port, String username, String password, boolean enabletls) {
		
		// only setup properties;
		
		logger.debug("New EmailSender[" + host + "," + port + "," + username + "," + password + "," + enabletls + "]");
		
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.enabletls = enabletls;
		
	}
	
	public void send(EmailMessage message, boolean debug) throws MFMSException
	{
		if (logger.isDebugEnabled()) logger.debug("Message [subject="+message.getSubject()+"]");
		Authenticator authenticator = null;
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		if (username != null) props.put("mail.smtp.user", username);
		if (password != null) {
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.starttls.enable", enabletls);
			
			auth = true;
			
			authenticator =  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  };
		} else {
			auth = false;
		}
		props.put("mail.smtp.auth", auth);
        
		// avoid hang by setting timeout; 60 seconds
        props.put("mail.smtp.timeout", "60000");
        props.put("mail.smtp.connectiontimeout", "60000");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
		//Session session = Session.getInstance(props, null);
		Session session = Session.getInstance(props, authenticator);
				 
		session.setDebug(debug);
		
		try {
			Message msg = compose(session, message);
	
			Transport transport = session.getTransport("smtp");
	
			if (logger.isDebugEnabled()) logger.debug("Going to connect to Smtp Server ["+host+"]");
			if (username != null && password != null) {
				transport.connect(host, username, password);
			} else {
				transport.connect();
			}
			if (logger.isDebugEnabled()) logger.debug("Going to send message ["+host+"]");
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (Exception e) {
			// wrap exception
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new MFMSException(e);
		}
		
		if (logger.isDebugEnabled()) logger.debug("Email Transported.");
		
	}
	
	private Message compose(Session session, EmailMessage msg) throws Exception, MFMSException 
	{
		if (logger.isDebugEnabled()) logger.debug("Message to send ["+msg+"]");
		
		Message message = new MimeMessage(session);
		
		/* Sender */
		if (logger.isDebugEnabled()) logger.debug("msg.sender ["+msg.getSender()+"]");
//		if (logger.isDebugEnabled()) logger.debug("cfgDefaultSourceAddres ["+cfgDefaultSourceAddress+"]");
		
		if (msg.getSender() != null)
			message.setFrom(new InternetAddress(msg.getSender()));
		else 
			throw new MFMSException("Sender of email message must be set");
		
		/* TO */
		if (msg.getRecipient() != null && msg.getRecipient().size() > 0)
		{
			InternetAddress[] addresses = new InternetAddress[msg.getRecipient().size()];
			for (int i=0; i<msg.getRecipient().size(); i++) addresses[i] = new InternetAddress(msg.getRecipient().get(i));
			message.addRecipients(Message.RecipientType.TO, addresses);	
		}
		/* Cc */
		if (msg.getCopyTo() != null && msg.getCopyTo().size() > 0)
		{
			InternetAddress[] addresses = new InternetAddress[msg.getCopyTo().size()];
			for (int i=0; i<msg.getCopyTo().size(); i++) addresses[i] = new InternetAddress(msg.getCopyTo().get(i));
			message.addRecipients(Message.RecipientType.CC, addresses);	
		}
		/* Bcc */
		if (msg.getBlindCopyTo() != null && msg.getBlindCopyTo().size() > 0)
		{
			InternetAddress[] addresses = new InternetAddress[msg.getBlindCopyTo().size()];
			for (int i=0; i<msg.getBlindCopyTo().size(); i++) addresses[i] = new InternetAddress(msg.getBlindCopyTo().get(i));
			message.addRecipients(Message.RecipientType.BCC, addresses);	
		}
		String subject = msg.getSubject();
		message.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
		message.setSentDate(new java.util.Date());
		
		Multipart multipart = new MimeMultipart();
		
		/* Body Content */
		String content = msg.getContent();
		
		if (logger.isDebugEnabled()) logger.debug("Content ["+content+"]");
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(content, "text/plain; charset=UTF-8");
        multipart.addBodyPart(messageBodyPart);

		/* Attachment */
        if (msg.getAttachment() != null && msg.getAttachment().size() > 0)
        {
        	for (EmailMessage.Attachment a : msg.getAttachment())
        	{
        		messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(a.getContent(), a.getType())));
                messageBodyPart.addHeader("Content-Type",messageBodyPart.getDataHandler().getContentType());				
                messageBodyPart.setFileName(a.getFilename());
                multipart.addBodyPart(messageBodyPart);
        	}
        }

        message.setContent(multipart);
		return message;
	}
}
