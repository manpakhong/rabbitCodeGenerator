package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.Role;

import java.util.ArrayList;
import java.util.List;

public class SearchRoleForm {
	
	private String name;
	
	private String description;
	
	private Integer siteKey;
	
	private List<Role> resultList = new ArrayList<Role>();
	
	private Boolean deleteSuccess = false;
	
	private String deletedName;
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private Boolean canAssign= false;
	
	private Integer fullListSize=0;

	public Integer getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(Integer fullListSize) {
		this.fullListSize = fullListSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public List<Role> getResultList() {
		return resultList;
	}

	public void setResultList(List<Role> resultList) {
		this.resultList = resultList;
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

	public Boolean getCanAssign() {
		return canAssign;
	}

	public void setCanAssign(Boolean canAssign) {
		this.canAssign = canAssign;
	}

}
