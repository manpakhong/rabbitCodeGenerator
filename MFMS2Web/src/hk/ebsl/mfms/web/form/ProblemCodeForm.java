package hk.ebsl.mfms.web.form;

import java.util.List;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Priority;

public class ProblemCodeForm {
	
	private List<FailureClass> failureClassList;
	
	private List<Priority> priorityList;
	
	private Integer key;
	
	private Integer siteKey;
	
	private String code;
	
	private String referrer;
	
	private String name;
	
	private String desc;
	
	private Integer parentKey;
	
	private Integer levelKey;
	
	private Integer defaultPriority;
	
	private Integer defaultAccountKey;
	
	private Boolean readOnly;
	
	private Boolean delete;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code.trim();
	}

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

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public Integer getLevelKey() {
		return levelKey;
	}

	public void setLevelKey(Integer levelKey) {
		this.levelKey = levelKey;
	}

	public Integer getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public Integer getDefaultAccountKey() {
		return defaultAccountKey;
	}

	public void setDefaultAccountKey(Integer defaultAccountKey) {
		this.defaultAccountKey = defaultAccountKey;
	}
	
	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getDeleted() {
		return delete;
	}

	public void setDeleted(Boolean delete) {
		this.delete = delete;

	}

	public List<Priority> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(List<Priority> priorityList) {
		this.priorityList = priorityList;
	}

	public List<FailureClass> getFailureClassList() {
		return failureClassList;
	}

	public void setFailureClassList(List<FailureClass> failureClassList) {
		this.failureClassList = failureClassList;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	
}
