package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.PrivilegeCategory;
import hk.ebsl.mfms.dto.StatusAccessMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleForm {

	private Integer key;
	
	private Integer siteKey;
	
	private String name;
	
	private String desc;
	
	private Boolean readOnly;
	
	// not used
	private Boolean delete;
	
	// operation referrer
	// for modify page
	// v - from view
	// s - from search
	
	private String referrer; 
	
	private String isSiteAdmin;
	
	private List<PrivilegeCategory> privilegeCategoryList;
	
	private List<String> grantedPrivilegeList;
	
	private Integer modeKey;
	
	private List<StatusAccessMode> modeList;
	
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
		this.desc = desc.trim();
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	public List<PrivilegeCategory> getPrivilegeCategoryList() {
		return privilegeCategoryList;
	}

	public void setPrivilegeCategoryList(
			List<PrivilegeCategory> privilegeCategoryList) {
		this.privilegeCategoryList = privilegeCategoryList;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public List<String> getGrantedPrivilegeList() {
		return grantedPrivilegeList;
	}

	public void setGrantedPrivilegeList(List<String> grantedPrivilegeList) {
		this.grantedPrivilegeList = grantedPrivilegeList;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public Integer getModeKey() {
		return modeKey;
	}

	public void setModeKey(Integer modeKey) {
		this.modeKey = modeKey;
	}

	public List<StatusAccessMode> getModeList() {
		return modeList;
	}

	public void setModeList(List<StatusAccessMode> modeList) {
		this.modeList = modeList;
	}

	public String getIsSiteAdmin() {
		return isSiteAdmin;
	}

	public void setIsSiteAdmin(String isSiteAdmin) {
		this.isSiteAdmin = isSiteAdmin;
	}
	
}
