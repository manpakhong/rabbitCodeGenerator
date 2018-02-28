package hk.ebsl.mfms.json;

import java.util.List;

public class MaintenanceScheduleJson {
	
	//prefix c means child or current;
	private String locationName;
	private String cLocationName;
	private List<String> account;
	private List<String> cAccount;
	private String description;
	private String cDescription;
	private String equipment;
	private String cEquipment;
	private	String tool;
	private String cTool;
	private String frequency;
	private String cFrequency;
	private String weekly;
	private String startDate;
	private String endDate;
	private String cStartDate;
	private String cEndDate;
	private String startTime;
	private String endTime;
	private String cStartTime;
	private String cEndTime;
	private String fullDate;
	private String cFullDate;
	private String time;
	private String status;
	private String remarks;
	private String cRemarks;
	
	
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
	public String getcStartTime() {
		return cStartTime;
	}
	public void setcStartTime(String cStartTime) {
		this.cStartTime = cStartTime;
	}
	public String getcEndTime() {
		return cEndTime;
	}
	public void setcEndTime(String cEndTime) {
		this.cEndTime = cEndTime;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getcLocationName() {
		return cLocationName;
	}
	public void setcLocationName(String cLocationName) {
		this.cLocationName = cLocationName;
	}
	public List<String> getAccount() {
		return account;
	}
	public void setAccount(List<String> account) {
		this.account = account;
	}
	public List<String> getcAccount() {
		return cAccount;
	}
	public void setcAccount(List<String> cAccount) {
		this.cAccount = cAccount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getcDescription() {
		return cDescription;
	}
	public void setcDescription(String cDescription) {
		this.cDescription = cDescription;
	}
	public String getEquipment() {
		return equipment;
	}
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	public String getcEquipment() {
		return cEquipment;
	}
	public void setcEquipment(String cEquipment) {
		this.cEquipment = cEquipment;
	}
	public String getTool() {
		return tool;
	}
	public void setTool(String tool) {
		this.tool = tool;
	}
	public String getcTool() {
		return cTool;
	}
	public void setcTool(String cTool) {
		this.cTool = cTool;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getcFrequency() {
		return cFrequency;
	}
	public void setcFrequency(String cFrequency) {
		this.cFrequency = cFrequency;
	}
	public String getWeekly() {
		return weekly;
	}
	public void setWeekly(String weekly) {
		this.weekly = weekly;
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
	public String getcStartDate() {
		return cStartDate;
	}
	public void setcStartDate(String cStartDate) {
		this.cStartDate = cStartDate;
	}
	public String getcEndDate() {
		return cEndDate;
	}
	public void setcEndDate(String cEndDate) {
		this.cEndDate = cEndDate;
	}
	public String getFullDate() {
		return fullDate;
	}
	public void setFullDate(String fullDate) {
		this.fullDate = fullDate;
	}
	public String getcFullDate() {
		return cFullDate;
	}
	public void setcFullDate(String cFullDate) {
		this.cFullDate = cFullDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getcRemarks() {
		return cRemarks;
	}
	public void setcRemarks(String cRemarks) {
		this.cRemarks = cRemarks;
	}
	@Override
	public String toString() {
		return "MaintenanceScheduleJson [locationName=" + locationName + ", cLocationName=" + cLocationName + ", account="
				+ account + ", cAccount=" + cAccount + ", description=" + description + ", cDescription=" + cDescription
				+ ", equipment=" + equipment + ", cEquipment=" + cEquipment + ", tool=" + tool + ", cTool=" + cTool
				+ ", frequency=" + frequency + ", cFrequency=" + cFrequency + ", weekly=" + weekly + ", startDate="
				+ startDate + ", endDate=" + endDate + ", cStartDate=" + cStartDate + ", cEndDate=" + cEndDate
				+ ", fullDate=" + fullDate + ", cFullDate=" + cFullDate + ", time=" + time + ", status=" + status
				+ ", remarks=" + remarks + ", cRemarks=" + cRemarks + "]";
	}
	
	
	
}