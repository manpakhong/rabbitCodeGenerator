package hk.ebsl.mfms.web.form;

import java.util.List;

public class PatrolAssignForm {

	Integer routeDefKey;
	List<String> routeName;
	String description;
	
	String skippedStartDate;
	String skippedEndDate;
	
	//current data
	Integer scheduleKey;
	List<String> staffName;
	String eventEditStaffJson;
	List<Integer> accountKey;
	String startDate;
	String endDate;
	String time;
	String isDeleted;
	
	//parent data

	Integer frequency;
	Integer[] weekDay;
	Integer parentScheduleKey;
	List<String> parentStaffName;
	String parentEventEditStaffJson;
	List<Integer> parentAccountKey;
	String parentStart;
	String parentEnd;
	String parentTime;
	String parentIsDeleted;
	public Integer getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(Integer routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public List<String> getRouteName() {
		return routeName;
	}
	public void setRouteName(List<String> routeName) {
		this.routeName = routeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getScheduleKey() {
		return scheduleKey;
	}
	public void setScheduleKey(Integer scheduleKey) {
		this.scheduleKey = scheduleKey;
	}
	public List<String> getStaffName() {
		return staffName;
	}
	public void setStaffName(List<String> staffName) {
		this.staffName = staffName;
	}
	public String getEventEditStaffJson() {
		return eventEditStaffJson;
	}
	public void setEventEditStaffJson(String eventEditStaffJson) {
		this.eventEditStaffJson = eventEditStaffJson;
	}
	public List<Integer> getAccountKey() {
		return accountKey;
	}
	public void setAccountKey(List<Integer> accountKey) {
		this.accountKey = accountKey;
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
	public String getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	public Integer[] getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(Integer[] weekDay) {
		this.weekDay = weekDay;
	}
	public Integer getParentScheduleKey() {
		return parentScheduleKey;
	}
	public void setParentScheduleKey(Integer parentScheduleKey) {
		this.parentScheduleKey = parentScheduleKey;
	}
	public List<String> getParentStaffName() {
		return parentStaffName;
	}
	public void setParentStaffName(List<String> parentStaffName) {
		this.parentStaffName = parentStaffName;
	}
	public String getParentEventEditStaffJson() {
		return parentEventEditStaffJson;
	}
	public void setParentEventEditStaffJson(String parentEventEditStaffJson) {
		this.parentEventEditStaffJson = parentEventEditStaffJson;
	}
	public List<Integer> getParentAccountKey() {
		return parentAccountKey;
	}
	public void setParentAccountKey(List<Integer> parentAccountKey) {
		this.parentAccountKey = parentAccountKey;
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
	public String getParentTime() {
		return parentTime;
	}
	public void setParentTime(String parentTime) {
		this.parentTime = parentTime;
	}
	public String getParentIsDeleted() {
		return parentIsDeleted;
	}
	public void setParentIsDeleted(String parentIsDeleted) {
		this.parentIsDeleted = parentIsDeleted;
	}
	public String getSkippedStartDate() {
		return skippedStartDate;
	}
	public void setSkippedStartDate(String skippedStartDate) {
		this.skippedStartDate = skippedStartDate;
	}
	public String getSkippedEndDate() {
		return skippedEndDate;
	}
	public void setSkippedEndDate(String skippedEndDate) {
		this.skippedEndDate = skippedEndDate;
	}
	
	
	

	
	
}
