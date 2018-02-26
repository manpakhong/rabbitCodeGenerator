package hk.ebsl.mfms.web.form;

import java.util.List;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.model.LocationTreeNode;

public class LocationPrivilegeForm {

	private Integer siteKey;
	
	private String loginId;
	
	private String name;
	
	private LocationTreeNode availableLocationTree;
	
	private List<Integer> selectedLocationKeyList;
	
	private Boolean success;

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
		this.name = name;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public List<Integer> getSelectedLocationKeyList() {
		return selectedLocationKeyList;
	}

	public void setSelectedLocationKeyList(List<Integer> selectedLocationKeyList) {
		this.selectedLocationKeyList = selectedLocationKeyList;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	
	
}
