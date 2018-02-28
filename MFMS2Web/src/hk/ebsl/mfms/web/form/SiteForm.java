package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.UserAccount;

import java.util.List;

public class SiteForm {

	private Integer key;
	
	private String name;
	
	private String address;
	
	private String contactCountryCode;
	
	private String contactAreaCode;
	
	private String contactNumber;
	
	private Boolean readOnly;
	
	private Boolean createDefaultAdmin = false;
	
	private String defaultAdminId;
	
	private String defaultAdminName;
	
	private String defaultAdminPass;
	
	private String defaultAdminConfirmPass;
	
	private Boolean delete;
	
	private List<UserAccount> adminAccountList;
	
	// operation referrer
	// for modify page
	// v - from view
	// s - from search
	
	private String referrer; 

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getName() {
		return name == null? name : name.trim();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactCountryCode() {
		return contactCountryCode;
	}

	public void setContactCountryCode(String contactCountryCode) {
		this.contactCountryCode = contactCountryCode;
	}

	public String getContactAreaCode() {
		return contactAreaCode;
	}

	public void setContactAreaCode(String contactAreaCode) {
		this.contactAreaCode = contactAreaCode;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getDefaultAdminId() {
		return defaultAdminId == null? defaultAdminId : defaultAdminId.trim();
	}

	public void setDefaultAdminId(String defaultAdminId) {
		this.defaultAdminId = defaultAdminId;
	}

	public String getDefaultAdminName() {
		return defaultAdminName;
	}

	public void setDefaultAdminName(String defaultAdminName) {
		this.defaultAdminName = defaultAdminName;
	}

	public String getDefaultAdminPass() {
		return defaultAdminPass;
	}

	public void setDefaultAdminPass(String defaultAdminPass) {
		this.defaultAdminPass = defaultAdminPass;
	}

	public String getDefaultAdminConfirmPass() {
		return defaultAdminConfirmPass;
	}

	public void setDefaultAdminConfirmPass(String defaultAdminConfirmPass) {
		this.defaultAdminConfirmPass = defaultAdminConfirmPass;
	}

	public Boolean getCreateDefaultAdmin() {
		return createDefaultAdmin;
	}

	public void setCreateDefaultAdmin(Boolean createDefaultAdmin) {
		this.createDefaultAdmin = createDefaultAdmin;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public List<UserAccount> getAdminAccountList() {
		return adminAccountList;
	}

	public void setAdminAccountList(List<UserAccount> adminAccountList) {
		this.adminAccountList = adminAccountList;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}
	
	
}
