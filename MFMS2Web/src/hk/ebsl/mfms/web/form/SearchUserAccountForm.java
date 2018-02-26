package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccountRole;

import java.util.List;
import java.util.Map;

public class SearchUserAccountForm {
	
	private String name;
	
	private String loginId;
	
	private String status;
	
	private Integer ownAccountKey;
	
	private List<UserAccountRole> resultList;
	
	private List<Role> availableRoleList;
	
	private Integer selectedRoleKey;
	
	private Map<String, String> availableStatusList;
	
	private Boolean deleteSuccess = false;
	
	private String deletedName;
	
	private Boolean canSetLp = false;
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private Integer fullListSize = 0;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<UserAccountRole> getResultList() {
		return resultList;
	}

	public void setResultList(List<UserAccountRole> resultList) {
		this.resultList = resultList;
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

	public Boolean getDeleteSuccess() {
		return deleteSuccess;
	}

	public void setDeleteSuccess(Boolean deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}

	public String getDeletedName() {
		return deletedName;
	}

	public void setDeletedName(String deletedName) {
		this.deletedName = deletedName;
	}

	public Integer getOwnAccountKey() {
		return ownAccountKey;
	}

	public void setOwnAccountKey(Integer ownAccountKey) {
		this.ownAccountKey = ownAccountKey;
	}

	public Boolean getCanModify() {
		return canModify;
	}

	public void setCanModify(Boolean canModify) {
		this.canModify = canModify;
	}

	public Boolean getCanRemove() {
		return canRemove;
	}

	public void setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
	}

	public Boolean getCanGen() {
		return canGen;
	}

	public void setCanGen(Boolean canGen) {
		this.canGen = canGen;
	}

	public Boolean getCanSetLp() {
		return canSetLp;
	}

	public void setCanSetLp(Boolean canSetLp) {
		this.canSetLp = canSetLp;
	}

	public Integer getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(Integer fullListSize) {
		this.fullListSize = fullListSize;
	}

	
}
