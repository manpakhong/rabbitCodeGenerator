package hk.ebsl.mfms.web.form;

import java.util.List;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.model.LocationTreeNode;

public class DefectListForm {

	private List<FailureClass> failureClassList;

	private List<Location> locationList;

	private List<Priority> priorityList;

	private List<ProblemCode> problemCodeList;

	private List<CauseCode> causeCodeList;

	private List<Equipment> equipmentList;

	private List<Status> statusList;

	private LocationTreeNode availableLocationTree;

	private Integer accountKey;

	private Integer key;

	private Integer siteKey;

	private String code;

	private String issueDate;

	private String order;

	private String asc;

	private String desc;

	private Integer locationKey;

	private Integer failureClassKey;

	private Integer problemCodeKey;

	private Integer causeCodeKey;

	private Integer equipmentKey;

	private Integer assignedToKey;

	private Integer accountGroupKey;

	private Integer priority;

	private String callFrom;

	private String status;

	private String sortBy;

	private String from;

	private String to;

	private String fileFormat;

	public List<FailureClass> getFailureClassList() {
		return failureClassList;
	}

	public void setFailureClassList(List<FailureClass> failureClassList) {
		this.failureClassList = failureClassList;
	}

	public List<Priority> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(List<Priority> priorityList) {
		this.priorityList = priorityList;
	}

	public List<ProblemCode> getProblemCodeList() {
		return problemCodeList;
	}

	public void setProblemCodeList(List<ProblemCode> problemCodeList) {
		this.problemCodeList = problemCodeList;
	}

	public List<CauseCode> getCauseCodeList() {
		return causeCodeList;
	}

	public void setCauseCodeList(List<CauseCode> causeCodeList) {
		this.causeCodeList = causeCodeList;
	}

	public List<Equipment> getEquipmentList() {
		return equipmentList;
	}

	public void setEquipmentList(List<Equipment> equipmentList) {
		this.equipmentList = equipmentList;
	}

	public List<Status> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Status> statusList) {
		this.statusList = statusList;
	}

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
		this.code = code;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public Integer getFailureClassKey() {
		return failureClassKey;
	}

	public void setFailureClassKey(Integer failureClassKey) {
		this.failureClassKey = failureClassKey;
	}

	public Integer getProblemCodeKey() {
		return problemCodeKey;
	}

	public void setProblemCodeKey(Integer problemCodeKey) {
		this.problemCodeKey = problemCodeKey;
	}

	public Integer getCauseCodeKey() {
		return causeCodeKey;
	}

	public void setCauseCodeKey(Integer causeCodeKey) {
		this.causeCodeKey = causeCodeKey;
	}

	public Integer getEquipmentKey() {
		return equipmentKey;
	}

	public void setEquipmentKey(Integer equipmentKey) {
		this.equipmentKey = equipmentKey;
	}

	public Integer getAssignedToKey() {
		return assignedToKey;
	}

	public void setAssignedToKey(Integer assignedToKey) {
		this.assignedToKey = assignedToKey;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCallFrom() {
		return callFrom;
	}

	public void setCallFrom(String callFrom) {
		this.callFrom = callFrom;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getAsc() {
		return asc;
	}

	public void setAsc(String asc) {
		this.asc = asc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Integer getAccountGroupKey() {
		return accountGroupKey;
	}

	public void setAccountGroupKey(Integer accountGroupKey) {
		this.accountGroupKey = accountGroupKey;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

}
