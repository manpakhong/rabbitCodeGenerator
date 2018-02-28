package hk.ebsl.mfms.email;

import java.util.List;

/**
 * 
 * Email message details holder
 *
 */
public class EmailMessage {

	private String sender;
	private List<String> recipient; //TO
	private List<String> copyTo; // Cc
	private List<String> blindCopyTo; // Bcc
	private String subject;
	private String content;
	private List<Attachment> attachment;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public List<String> getRecipient() {
		return recipient;
	}
	public void setRecipient(List<String> recipient) {
		this.recipient = recipient;
	}
	public List<String> getCopyTo() {
		return copyTo;
	}
	public void setCopyTo(List<String> copyTo) {
		this.copyTo = copyTo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public List<Attachment> getAttachment() {
		return attachment;
	}
	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}

	public static class Attachment
	{
		private byte[] content;
		private String filename;
		private String type;
		public Attachment(byte[] content, String filename, String type)
		{
			this.content = content;
			this.filename = filename;
			this.type = type;
		}
		public byte[] getContent() {
			return content;
		}
		public void setContent(byte[] content) {
			this.content = content;
		}
		public String getFilename() {
			return filename;
		}
		public void setFilename(String filename) {
			this.filename = filename;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}

	public List<String> getBlindCopyTo() {
		return blindCopyTo;
	}
	public void setBlindCopyTo(List<String> blindCopyTo) {
		this.blindCopyTo = blindCopyTo;
	}
	
}
