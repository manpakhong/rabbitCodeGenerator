package hk.ebsl.mfms.web.form;

import java.util.ArrayList;
import java.util.List;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.ProblemCode;

public class SearchProblemCodeForm {
	
	private List<FailureClass> failureClassList;
	
	private String code;
	
	private String name;
	
	private String description;
	
	private Integer siteKey;
	
	private Integer parentKey;
	
	private Integer defaultPriority;
	
	private Boolean deleteSuccess = false;
	
	private Boolean hasDefect = false;

	private String deletedName;
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private List<ProblemCode> resultList = new ArrayList<ProblemCode>();
	
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

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public List<ProblemCode> getResultList() {
		return resultList;
	}

	public void setResultList(List<ProblemCode> resultList) {
		this.resultList = resultList;
	}
	
	public List<FailureClass> getFailureClassList() {
		return failureClassList;
	}

	public void setFailureClassList(List<FailureClass> failureClassList) {
		this.failureClassList = failureClassList;
	}

	public Integer getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
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

	public Boolean getHasDefect() {
		return hasDefect;
	}

	public void setHasDefect(Boolean hasDefect) {
		this.hasDefect = hasDefect;
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
