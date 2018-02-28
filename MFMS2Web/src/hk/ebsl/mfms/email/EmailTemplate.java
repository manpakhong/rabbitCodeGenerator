package hk.ebsl.mfms.email;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;


public class EmailTemplate {

	public static final Logger logger = Logger.getLogger(EmailTemplate.class);
	/**
     * Filename of the email template.
     */
    private String path;
    
    private List<String> toList = new ArrayList<String>();
    
    private List<String> ccList = new ArrayList<String>();
    
    private List<String> bccList = new ArrayList<String>();
    
    private String to = "";
    
    private String cc = "";
    
    private String bcc = "";
    
    /**
     * file content: 1st line = subject, remaining = content
     */
    private String templateString = "";

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		// TODO: load template from file
		DefaultResourceLoader loader = new DefaultResourceLoader(); 
		Resource resource = loader.getResource(path);
		//File file = new File(path);
		try {
			File file = resource.getFile();
			
			templateString = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			logger.error("Unable to load template from file: " + path);
			e.printStackTrace();
		}
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
		//TODO: split to list
		String[] recipients = to.split(",");
		if (recipients != null)
			toList = Arrays.asList(recipients);
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
		
		String[] recipients = cc.split(",");
		
		if (recipients != null)
			ccList = Arrays.asList(recipients);
		
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;

		String[] recipients = bcc.split(",");
		
		if (recipients != null)
			bccList = Arrays.asList(recipients);
	}

	public List<String> getToList() {
		return toList;
	}

	public List<String> getCcList() {
		return ccList;
	}

	public List<String> getBccList() {
		return bccList;
	}

	public String getTemplateString() {
		return templateString;
	}
	
	public EmailMessage formatEmail(Object[] param) {
		
		EmailMessage email = new EmailMessage();
		
		email.setRecipient(toList);
		email.setCopyTo(ccList);
		email.setBlindCopyTo(bccList);
		
		// format 
		
		logger.debug(templateString);
		
		MessageFormat mf = new MessageFormat(templateString);
		
		String formattedString = mf.format(param);
		
		String[] result = formattedString.split("\r\n|\r|\n", 2);
		
		String subject = result[0];
		String content = "";
		if (result.length > 1) {
			content = result[1];
		}
		
		email.setSubject(subject);
		email.setContent(content);
		
		return email;
		
	}
    
    
    
}
