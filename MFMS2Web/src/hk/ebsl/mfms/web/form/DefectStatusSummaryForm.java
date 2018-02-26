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

public class DefectStatusSummaryForm {

	private List<Location> locationList;

	private List<FailureClass> failureClassList;

	private List<Priority> priorityList;

	private List<ProblemCode> problemCodeList;

	private List<Status> statusList;

	private Integer key;

	private Integer siteKey;

	private String order;

	private Integer location;

	private Integer failureClassKey;

	private Integer problemCodeKey;

	private Integer priority;

	private String status;

	private String sortBy;

	private String from;

	private String to;
	
	private String fileFormat;
	
	private String[] statusMap;
	
	private Integer locationKey;
	
	private LocationTreeNode availableLocationTree;

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

	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
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

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
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

	public String[] getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(String[] statusMap) {
		this.statusMap = statusMap;
	}

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	
	
}
