package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.FailureClass;

import java.util.ArrayList;
import java.util.List;

public class SearchFailureClassForm {
	
	private String code;
	
	private String name;
	
	private String description;
	
	private Integer siteKey;
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private Boolean hasChild = false;
	
	private Boolean deleteSuccess = false;

	private String deletedName;
	
	private List<FailureClass> resultList = new ArrayList<FailureClass>();

	private Integer fullListSize = 0;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code= code;
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

	public List<FailureClass> getResultList() {
		return resultList;
	}

	public void setResultList(List<FailureClass> resultList) {
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

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
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

	public Integer getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(Integer fullListSize) {
		this.fullListSize = fullListSize;
	}


}
