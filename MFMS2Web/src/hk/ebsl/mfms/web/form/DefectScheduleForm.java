package hk.ebsl.mfms.web.form;

import java.util.List;
import java.util.Set;

import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.model.LocationTreeNode;

public class DefectScheduleForm {

	private Integer key;

	private Boolean isSeries = false;

	private Integer siteKey;

	private Boolean success = false;

	private Boolean isFullDay = false;

	private Boolean isWeekDay = false;

	private String referrer;

	private Boolean readOnly;

	private Integer toolKey;

	private String deletedName;

	private Integer equipmentKey;

	private Integer locationKey;

	private List<Integer> accountKey;

	private List<Integer> weekDays;

	private List<UserAccount> userAccountList;

	private List<UserAccount> selectedUserAccountList;

	private Set<Integer> selectedAccountKeyList;

	private Integer frequency;

	private String startDate;
	
	private String startTime;
	
	private String endDate;
	
	private String endTime;

	private String time;

	private String description;

	private String remarks;

	private LocationTreeNode availableLocationTree;

	private String parentStart;

	private String parentEnd;

	private Set<Integer> staff;

	private String staffSelected;
	
	private Integer completedStatus;
	
	
	public Integer getCompletedStatus() {
		return completedStatus;
	}

	public void setCompletedStatus(Integer completedStatus) {
		this.completedStatus = completedStatus;
	}

	public List<Integer> getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(List<Integer> accountKey) {
		this.accountKey = accountKey;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getToolKey() {
		return toolKey;
	}

	public void setToolKey(Integer toolKey) {
		this.toolKey = toolKey;
	}

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getEquipmentKey() {
		return equipmentKey;
	}

	public void setEquipmentKey(Integer equipmentKey) {
		this.equipmentKey = equipmentKey;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public List<UserAccount> getUserAccountList() {
		return userAccountList;
	}

	public void setUserAccountList(List<UserAccount> userAccountList) {
		this.userAccountList = userAccountList;
	}

	public List<UserAccount> getSelectedUserAccountList() {
		return selectedUserAccountList;
	}

	public void setSelectedUserAccountList(List<UserAccount> selectedUserAccountList) {
		this.selectedUserAccountList = selectedUserAccountList;
	}

	public Set<Integer> getSelectedAccountKeyList() {
		return selectedAccountKeyList;
	}

	public void setSelectedAccountKeyList(Set<Integer> selectedAccountKeyList) {
		this.selectedAccountKeyList = selectedAccountKeyList;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<Integer> getWeekDays() {
		return weekDays;
	}

	public void setWeekDays(List<Integer> weekDays) {
		this.weekDays = weekDays;
	}

	public String getDeletedName() {
		return deletedName;
	}

	public void setDeletedName(String deletedName) {
		this.deletedName = deletedName;
	}

	public Boolean getIsFullDay() {
		return isFullDay;
	}

	public void setIsFullDay(Boolean isFullDay) {
		this.isFullDay = isFullDay;
	}

	public Boolean getIsWeekDay() {
		return isWeekDay;
	}

	public void setIsWeekDay(Boolean isWeekDay) {
		this.isWeekDay = isWeekDay;
	}

	public Boolean getIsSeries() {
		return isSeries;
	}

	public void setIsSeries(Boolean isSeries) {
		this.isSeries = isSeries;
	}

	

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getParentStart() {
		return parentStart;
	}

	public void setParentStart(String parentStart) {
		this.parentStart = parentStart;
	}

	public String getParentEnd() {
		return parentEnd;
	}

	public void setParentEnd(String parentEnd) {
		this.parentEnd = parentEnd;
	}

	public Set<Integer> getStaff() {
		return staff;
	}

	public void setStaff(Set<Integer> staff) {
		this.staff = staff;
	}

	public String getStaffSelected() {
		return staffSelected;
	}

	public void setStaffSelected(String staffSelected) {
		this.staffSelected = staffSelected;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
