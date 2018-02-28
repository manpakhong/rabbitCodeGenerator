package hk.ebsl.mfms.notification.object;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InformEscalatorPostObj {

	@JsonProperty("AppID")
	private String appId;
	
	@JsonProperty("WOKey")
	private String woKey;
	
	@JsonProperty("Recipients")
	private List<NotificationRecipient> recipients;
	
	@JsonProperty("Message")
	private NotificationMessage message;
	
	@JsonProperty("TargetFinishDate")
	private String targetFinishDate;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("Emergency")
	private String emergency;

	@JsonProperty("LastModifyTime")
	private String lastModifyTime;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getWoKey() {
		return woKey;
	}

	public void setWoKey(String woKey) {
		this.woKey = woKey;
	}


	public NotificationMessage getMessage() {
		return message;
	}

	public void setMessage(NotificationMessage message) {
		this.message = message;
	}

	public String getTargetFinishDate() {
		return targetFinishDate;
	}

	public void setTargetFinishDate(String targetFinishDate) {
		this.targetFinishDate = targetFinishDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}
	
	public List<NotificationRecipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<NotificationRecipient> recipients) {
		this.recipients = recipients;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}





	public class NotificationRecipient{
		
		@JsonProperty("Type")
		private String type;
		
		@JsonProperty("To")
		private String to;
		
		@JsonProperty("Role")
		private String role;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
	}
	
	public class NotificationMessage{
		
		@JsonProperty("WOCode")
		private String woCode;
		
		@JsonProperty("AssignedAccount")
		private String assignedAccount;
		
		@JsonProperty("Site")
		private String site;
		
		@JsonProperty("Contact")
		private String contact;
		
		@JsonProperty("Desc")
		private String desc;
		
		@JsonProperty("IssueTime")
		private String issueTime;
		
		@JsonProperty("Location")
		private String location;
		
		@JsonProperty("ProblemCode")
		private String problemCode;
		
		
		public String getWoCode() {
			return woCode;
		}

		public void setWoCode(String woCode) {
			this.woCode = woCode;
		}

		public String getAssignedAccount() {
			return assignedAccount;
		}

		public void setAssignedAccount(String assignedAccount) {
			this.assignedAccount = assignedAccount;
		}

		public String getSite() {
			return site;
		}

		public void setSite(String site) {
			this.site = site;
		}

		public String getContact() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getIssueTime() {
			return issueTime;
		}

		public void setIssueTime(String issueTime) {
			this.issueTime = issueTime;
		}

		public String getLocation() {
			return location;
		}

		public void setLocation(String location) {
			this.location = location;
		}

		public String getProblemCode() {
			return problemCode;
		}

		public void setProblemCode(String problemCode) {
			this.problemCode = problemCode;
		}
		
		
		
		
	}
	
}
