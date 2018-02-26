package hk.ebsl.mfms.dto;

public class AccountGroupExport {
	private String groupName;
	private String description;
	private String responsibleAccount;
	private String account;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getResponsibleAccount() {
		return responsibleAccount;
	}
	public void setResponsibleAccount(String responsibleAccount) {
		this.responsibleAccount = responsibleAccount;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	
}
