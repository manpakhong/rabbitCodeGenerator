package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.model.LocationTreeNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchDefectForm {
	
	private List<FailureClass> failureClassList;
	
	private List<ProblemCode> problemCodeList;
	
	private List<CauseCode> causeCodeList;
	
	private List<Priority> priorityList;
	
	private List<Status> statusList;
	
	private List<StatusFlow> statusFlowList;
	
	private List<Location> locationList;
	
	private LocationTreeNode availableLocationTree;
	
	private Integer key;
	
	private String code = "";
	
	private String description = "";
	
	private Integer siteKey;
	
	private Integer priority;
	
	private String status = "";
	
	private Integer failureClassKey;
	
	private Integer groupKey;
	
	private Integer accountKey;
	
	private Integer problemCodeKey;
	
	private Integer causeCodeKey;
	
	private Integer locationKey;
	
	private Boolean deleteSuccess = false;
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private String deletedName;
	
	private Map<String, String> statusMap = new LinkedHashMap<String, String>();
	
	private List<Defect> resultList = new ArrayList<Defect>();
	
	private Integer fullListSize = 0;	
	
	private Integer currAccountKey = 0;

	private String sortColIndex = "";
	private String sortDirection = "";

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code= code;
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

	public List<Defect> getResultList() {
		return resultList;
	}

	public void setResultList(List<Defect> resultList) {
		this.resultList = resultList;
	}

	public List<FailureClass> getFailureClassList() {
		return failureClassList;
	}

	public void setFailureClassList(List<FailureClass> failureClassList) {
		this.failureClassList = failureClassList;
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

	public List<Priority> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(List<Priority> priorityList) {
		this.priorityList = priorityList;
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

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}

	public List<Status> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Status> statusList) {
		this.statusList = statusList;
	}

	public List<StatusFlow> getStatusFlowList() {
		return statusFlowList;
	}

	public void setStatusFlowList(List<StatusFlow> statusFlowList) {
		this.statusFlowList = statusFlowList;
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

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public boolean isCanGen() {
		return canGen;
	}

	public void setCanGen(boolean canGen) {
		this.canGen = canGen;
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

	public Integer getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(Integer groupKey) {
		this.groupKey = groupKey;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

	public Integer getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(Integer fullListSize) {
		this.fullListSize = fullListSize;
	}

	public Integer getCurrAccountKey() {
		return currAccountKey;
	}

	public void setCurrAccountKey(Integer currAccountKey) {
		this.currAccountKey = currAccountKey;
	}

	public String getSortColIndex() {
		return sortColIndex;
	}

	public void setSortColIndex(String sortColIndex) {
		this.sortColIndex = sortColIndex;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

}
