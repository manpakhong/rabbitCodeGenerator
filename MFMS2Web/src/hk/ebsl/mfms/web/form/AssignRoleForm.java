package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.UserAccount;

import java.util.List;

public class AssignRoleForm {

	private Integer key;
	
	private String name;
	
	private String desc;
	
	private List<String> assignedAccountList;
	
	private List<String> unassignedAccountList;
	
	private Boolean success = false;
	
	// keep the referrer for modify page
	private String referrer;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public List<String> getAssignedAccountList() {
		return assignedAccountList;
	}

	public void setAssignedAccountList(List<String> assignedAccountList) {
		this.assignedAccountList = assignedAccountList;
	}

	public List<String> getUnassignedAccountList() {
		return unassignedAccountList;
	}

	public void setUnassignedAccountList(List<String> unassignedAccountList) {
		this.unassignedAccountList = unassignedAccountList;
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
