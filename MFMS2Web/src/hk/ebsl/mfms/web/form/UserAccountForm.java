package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.model.LocationTreeNode;

import java.util.List;
import java.util.Map;

public class UserAccountForm {

	private Integer key;

	private String loginId;

	private String password;

	private String confirmPassword;
	
	private String currentPassword;

	private String name;

	private String tagId;

	private String email;

	private String contactCountryCode;

	private String contactAreaCode;

	private String contactNumber;

	private Integer logonAttemptCount;

	private String status;

	private Boolean delete;

	private Boolean readOnly;

	private List<Role> availableRoleList;

	private Integer selectedRoleKey;

	private Map<String, String> availableStatusList;
	
	private LocationTreeNode availableLocationTree;
	
	private List<Integer> selectedLocationKeyList;

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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {

		if (tagId != null)
			this.tagId = tagId.trim();
		else
			this.tagId = tagId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? email : email.trim();
	}

	public String getContactCountryCode() {
		return contactCountryCode;
	}

	public void setContactCountryCode(String contactCountryCode) {
		this.contactCountryCode = contactCountryCode.trim();
	}

	public String getContactAreaCode() {
		return contactAreaCode;
	}

	public void setContactAreaCode(String contactAreaCode) {
		this.contactAreaCode = contactAreaCode.trim();
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber.trim();
	}

	public Integer getLogonAttemptCount() {
		return logonAttemptCount;
	}

	public void setLogonAttemptCount(Integer logonAttemptCount) {
		this.logonAttemptCount = logonAttemptCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status.trim();
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword.trim();
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<Role> getAvailableRoleList() {
		return availableRoleList;
	}

	public void setAvailableRoleList(List<Role> availableRoleList) {
		this.availableRoleList = availableRoleList;
	}

	public Integer getSelectedRoleKey() {
		return selectedRoleKey;
	}

	public void setSelectedRoleKey(Integer selectedRoleKey) {
		this.selectedRoleKey = selectedRoleKey;
	}

	public Map<String, String> getAvailableStatusList() {
		return availableStatusList;
	}

	public void setAvailableStatusList(Map<String, String> availableStatusList) {
		this.availableStatusList = availableStatusList;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public List<Integer> getSelectedLocationKeyList() {
		return selectedLocationKeyList;
	}

	public void setSelectedLocationKeyList(List<Integer> selectedLocationKeyList) {
		this.selectedLocationKeyList = selectedLocationKeyList;
	}

}
