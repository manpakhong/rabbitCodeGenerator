package hk.ebsl.mfms.web.form;

import java.util.List;
import java.util.Set;

import hk.ebsl.mfms.dto.UserAccount;

public class AccountGroupForm {
	
	private List<UserAccount> userAccountList;
	
	private List<UserAccount> selectedUserAccountList;
	
	private Set<Integer> selectedAccountKeyList;
	
	private Set<Integer> selectedResponsibleAccountKeyList;

	private Integer key;
	
	private Integer siteKey;
	
	private String name;
	
	private String desc;
	
	private Boolean readOnly;
	
	private Boolean delete;
	
	private Boolean success = false;
	
	private String referrer;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public List<UserAccount> getUserAccountList() {
		return userAccountList;
	}

	public void setUserAccountList(List<UserAccount> userAccountList) {
		this.userAccountList = userAccountList;
	}

	public List<UserAccount> getSelectedUserAccountList() {
		return selectedUserAccountList;
	}

	public void setSelectedUserAccountList(List<UserAccount> selectedUserAccountList) {
		this.selectedUserAccountList = selectedUserAccountList;
	}

	public Set<Integer> getSelectedAccountKeyList() {
		return selectedAccountKeyList;
	}

	public void setSelectedAccountKeyList(Set<Integer> selectedAccountKeyList) {
		this.selectedAccountKeyList = selectedAccountKeyList;
	}

	public Set<Integer> getSelectedResponsibleAccountKeyList() {
		return selectedResponsibleAccountKeyList;
	}

	public void setSelectedResponsibleAccountKeyList(Set<Integer> selectedResponsibleAccountKeyList) {
		this.selectedResponsibleAccountKeyList = selectedResponsibleAccountKeyList;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}


}
