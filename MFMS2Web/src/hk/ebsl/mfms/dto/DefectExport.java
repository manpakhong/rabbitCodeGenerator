package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

public class DefectExport {
	private String code;
	private String location;
	private String failureClass;
	private String problemCode;
	private String causeCode;
	private String tool;
	private String equipment;
	private String contactName;
	private String contactTel;
	private String contactEmail;
	private String emergencyTel;
	private String assignedGroup;
	private String assignedAccount;
	private String priority;
	private String desc;
	private String detailedDesc;
	private String reportDateTime;
	private String callFrom;
	private String issueBy;
	private String issueDateTime;
	private String targetStartDateTime;
	private String targetFinishDateTime;
	private String actualStartDateTime;
	private String actualFinishDateTime;
	private String checkBy;
	private String checkDateTime;
	private String status;
	private String remarks;
	private String meetKpi;
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(String failureClass) {
		this.failureClass = failureClass;
	}

	public String getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(String problemCode) {
		this.problemCode = problemCode;
	}

	public String getCauseCode() {
		return causeCode;
	}

	public void setCauseCode(String causeCode) {
		this.causeCode = causeCode;
	}

	public String getTool() {
		return tool;
	}

	public void setTool(String tool) {
		this.tool = tool;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getEmergencyTel() {
		return emergencyTel;
	}

	public void setEmergencyTel(String emergencyTel) {
		this.emergencyTel = emergencyTel;
	}

	public String getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(String assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public String getAssignedAccount() {
		return assignedAccount;
	}

	public void setAssignedAccount(String assignedAccount) {
		this.assignedAccount = assignedAccount;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDetailedDesc() {
		return detailedDesc;
	}

	public void setDetailedDesc(String detailedDesc) {
		this.detailedDesc = detailedDesc;
	}

	public String getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(String reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	public String getCallFrom() {
		return callFrom;
	}

	public void setCallFrom(String callFrom) {
		this.callFrom = callFrom;
	}

	public String getIssueBy() {
		return issueBy;
	}

	public void setIssueBy(String issueBy) {
		this.issueBy = issueBy;
	}

	public String getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(String issueDateTime) {
		this.issueDateTime = issueDateTime;
	}

	public String getTargetStartDateTime() {
		return targetStartDateTime;
	}

	public void setTargetStartDateTime(String targetStartDateTime) {
		this.targetStartDateTime = targetStartDateTime;
	}

	public String getTargetFinishDateTime() {
		return targetFinishDateTime;
	}

	public void setTargetFinishDateTime(String targetFinishDateTime) {
		this.targetFinishDateTime = targetFinishDateTime;
	}

	public String getActualStartDateTime() {
		return actualStartDateTime;
	}

	public void setActualStartDateTime(String actualStartDateTime) {
		this.actualStartDateTime = actualStartDateTime;
	}

	public String getActualFinishDateTime() {
		return actualFinishDateTime;
	}

	public void setActualFinishDateTime(String actualFinishDateTime) {
		this.actualFinishDateTime = actualFinishDateTime;
	}

	public String getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	public String getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(String checkDateTime) {
		this.checkDateTime = checkDateTime;
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

	public String getMeetKpi() {
		return meetKpi;
	}

	public void setMeetKpi(String meetKpi) {
		this.meetKpi = meetKpi;
	}

}
