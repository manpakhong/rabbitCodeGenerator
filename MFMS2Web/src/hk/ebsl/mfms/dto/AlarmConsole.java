package hk.ebsl.mfms.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class AlarmConsole implements Serializable {
	
	@Id
	@Column(name="d_Key")
	@JsonIgnore
	private Integer defect_Key;
	
	@Column(name="d_SiteKey")
	@JsonIgnore
	private Integer defect_SiteKey;

	@Column(name="d_Code")
	private String defect_Code;
	
	@Column(name="d_CreateChannel")
	@JsonIgnore
	private String defect_CreateChannel;
	
	@Column(name="d_LocationKey")
	@JsonIgnore
	private Integer defect_LocationKey;
	
	@Column(name="d_FailureClassKey")
	@JsonIgnore
	private Integer defect_FailureClassKey;
	
	@Column(name="d_ProblemCodeKey")
	@JsonIgnore
	private Integer defect_ProblemCodeKey;
	
	@Column(name="d_CauseCodeKey")
	@JsonIgnore
	private Integer defect_CauseCodeKey;
	
	@Column(name="d_ToolKey")
	@JsonIgnore
	private Integer defect_ToolKey;
	
	@Column(name="d_EquipmentKey")
	@JsonIgnore
	private Integer defect_EquipmentKey;
	
	@Column(name="d_Contact_Name")
	private String defect_ContactName;
	
	@Column(name="d_Contact_Tel")
	private String defect_ContactTel;
	
	@Column(name="d_Contact_Email")
	@JsonIgnore
	private String defect_ContactEmail;
	
	@Column(name="d_Emergency_Tel")
	@JsonIgnore
	private String defect_EmergencyTel;
	
	@Column(name="d_AssignedGroupKey")
	@JsonIgnore
	private Integer defect_AssignedGroupKey;
	
	@Column(name="d_AssignedAccountKey")
	@JsonIgnore
	private Integer defect_AssignedAccountKey;
	
	@Column(name="d_Priority")
	@JsonIgnore
	private Integer defect_Priority;
	
	@Column(name="d_Desc")
	private String defect_Desc;
	
	@Column(name="d_DetailedDesc")
	@JsonIgnore
	private String defect_DetailedDesc;
	
	@Column(name="d_ReportDateTime")
	@JsonIgnore
	private Timestamp defect_ReportDateTime;
	
	@Column(name="d_CallFrom")
	@JsonIgnore
	private String defect_CallFrom;
	
	@Column(name="d_IssueBy")
	@JsonIgnore
	private Integer defect_IssueBy;
	
	@Column(name="d_IssueDateTime")
	private Timestamp defect_IssueDateTime;
	
	@Column(name="d_TargetStartDateTime")
	@JsonIgnore
	private Timestamp defect_TargetStartDateTime;
	
	@Column(name="d_TargetFinishDateTime")
	private Timestamp defect_TargetFinishDateTime;
	
	@Column(name="d_ActualStartDateTime")
	@JsonIgnore
	private Timestamp defect_ActualStartDateTime;
	
	@Column(name="d_ActualFinishDateTime")
	@JsonIgnore
	private Timestamp defect_ActualFinishDateTime;
	
	@Column(name="d_CheckBy")
	@JsonIgnore
	private Integer defect_CheckBy;
	
	@Column(name="d_CheckDateTime")
	@JsonIgnore
	private Timestamp defect_CheckDateTime;
	
	@Column(name="d_StatusID")
	private String defect_StatusId;
	
	@Column(name="d_Remarks")
	private String defect_Remarks;
	
	@Column(name="d_CreateBy")
	@JsonIgnore
	private Integer defect_CreateBy;
	
	@Column(name="d_CreateDateTime")
	@JsonIgnore
	private Timestamp defect_CreateDateTime;
	
	@Column(name="d_LastModifyBy")
	@JsonIgnore
	private Integer defect_LastModifyBy;
	
	@Column(name="d_LastModifyDateTime")
	@JsonIgnore
	private Timestamp defect_LastModifyDateTime;
	
	@Column(name="d_Deleted")
	@JsonIgnore
	private String defect_Deleted;
	
	@Column(name="d_LastModifyTimeForSync")
	@JsonIgnore
	private Timestamp lastModifyTimeForSync;
	
	@Column(name="d_MeetKpi")
	@JsonIgnore
	private String defect_MeetKpi;
	
	@Column(name="ReplyBy")
	private String defect_ReplyBy;
	
	@Column(name="ReplyTime")
	private Timestamp defect_ReplyTime;
	
	@Column(name="a_LoginId")
	@JsonIgnore
	private String account_LoginId;
	
	@Column(name="a_Name")
	private String account_Name;
	
	@Column(name="a_ContactNumber")
	private String account_ContactNumber;
	
	@Column(name="ag_Name")
	private String acountGroup_Name;
	
	@Column(name="ag_Desc")
	@JsonIgnore
	private String accountGroup_Desc;
	
	@Column(name="cc_Code")
	@JsonIgnore
	private String causeCode_Code;
	
	@Column(name="cc_Name")
	@JsonIgnore
	private String causeCode_Name;
	
	@Column(name="cc_Desc")
	@JsonIgnore
	private String causeCode_Desc;
	
	@Column(name="e_Code")
	@JsonIgnore
	private String equipment_Code;
	
	@Column(name="e_Name")
	@JsonIgnore
	private String equipment_Name;
	
	@Column(name="e_Desc")
	@JsonIgnore
	private String equipment_Desc;
	
	@Column(name="pc_Code_Failure")
	@JsonIgnore
	private String failureClass_Code;
	
	@Column(name="pc_Name_Failure")
	@JsonIgnore
	private String failureClass_Name;
	
	@Column(name="pc_Desc_Failure")
	@JsonIgnore
	private String failureClass_Desc;
	
	@Column(name="l_Code")
	@JsonIgnore
	private String location_Code;
	
	@Column(name="l_Name")
	private String location_Name;
	
	@Column(name="pc_Code_Problem")
	@JsonIgnore
	private String problemCode_Code;
	
	@Column(name="pc_Name_Problem")
	private String problemCode_Name;
	
	@Column(name="pc_Desc_Problem")
	@JsonIgnore
	private String problemCode_Desc;
	
	@Column(name="s_Name")
	@JsonIgnore
	private String site_Name;
	
	@Column(name="t_Code")
	@JsonIgnore
	private String tool_Code;
	
	@Column(name="t_Name")
	@JsonIgnore
	private String tool_Name;
	
	@Column(name="t_Desc")
	@JsonIgnore
	private String tool_Desc;
	
	@Column(name="st_Name")
	private String status_Name;
	
	@Id
	@Column(name="sortNum")
	private String sortNum;
	
	@Column(name="childKey")
	@JsonIgnore
	private Integer childKey;
	
	@Column(name="color")
	@JsonIgnore
	private Integer color;

	@Column(name="escalation")
	private String escalation;
	
	public Integer getDefect_Key() {
		return defect_Key;
	}

	public void setDefect_Key(Integer defect_Key) {
		this.defect_Key = defect_Key;
	}

	public Integer getDefect_SiteKey() {
		return defect_SiteKey;
	}

	public void setDefect_SiteKey(Integer defect_SiteKey) {
		this.defect_SiteKey = defect_SiteKey;
	}

	public String getDefect_Code() {
		return defect_Code;
	}

	public void setDefect_Code(String defect_Code) {
		this.defect_Code = defect_Code;
	}

	public String getDefect_CreateChannel() {
		return defect_CreateChannel;
	}

	public void setDefect_CreateChannel(String defect_CreateChannel) {
		this.defect_CreateChannel = defect_CreateChannel;
	}

	public Integer getDefect_LocationKey() {
		return defect_LocationKey;
	}

	public void setDefect_LocationKey(Integer defect_LocationKey) {
		this.defect_LocationKey = defect_LocationKey;
	}

	public Integer getDefect_FailureClassKey() {
		return defect_FailureClassKey;
	}

	public void setDefect_FailureClassKey(Integer defect_FailureClassKey) {
		this.defect_FailureClassKey = defect_FailureClassKey;
	}

	public Integer getDefect_ProblemCodeKey() {
		return defect_ProblemCodeKey;
	}

	public void setDefect_ProblemCodeKey(Integer defect_ProblemCodeKey) {
		this.defect_ProblemCodeKey = defect_ProblemCodeKey;
	}

	public Integer getDefect_CauseCodeKey() {
		return defect_CauseCodeKey;
	}

	public void setDefect_CauseCodeKey(Integer defect_CauseCodeKey) {
		this.defect_CauseCodeKey = defect_CauseCodeKey;
	}

	public Integer getDefect_ToolKey() {
		return defect_ToolKey;
	}

	public void setDefect_ToolKey(Integer defect_ToolKey) {
		this.defect_ToolKey = defect_ToolKey;
	}

	public Integer getDefect_EquipmentKey() {
		return defect_EquipmentKey;
	}

	public void setDefect_EquipmentKey(Integer defect_EquipmentKey) {
		this.defect_EquipmentKey = defect_EquipmentKey;
	}

	public String getDefect_ContactName() {
		return defect_ContactName;
	}

	public void setDefect_ContactName(String defect_ContactName) {
		this.defect_ContactName = defect_ContactName;
	}

	public String getDefect_ContactTel() {
		return defect_ContactTel;
	}

	public void setDefect_ContactTel(String defect_ContactTel) {
		this.defect_ContactTel = defect_ContactTel;
	}

	public String getDefect_ContactEmail() {
		return defect_ContactEmail;
	}

	public void setDefect_ContactEmail(String defect_ContactEmail) {
		this.defect_ContactEmail = defect_ContactEmail;
	}

	public String getDefect_EmergencyTel() {
		return defect_EmergencyTel;
	}

	public void setDefect_EmergencyTel(String defect_EmergencyTel) {
		this.defect_EmergencyTel = defect_EmergencyTel;
	}

	public Integer getDefect_AssignedGroupKey() {
		return defect_AssignedGroupKey;
	}

	public void setDefect_AssignedGroupKey(Integer defect_AssignedGroupKey) {
		this.defect_AssignedGroupKey = defect_AssignedGroupKey;
	}

	public Integer getDefect_AssignedAccountKey() {
		return defect_AssignedAccountKey;
	}

	public void setDefect_AssignedAccountKey(Integer defect_AssignedAccountKey) {
		this.defect_AssignedAccountKey = defect_AssignedAccountKey;
	}

	public Integer getDefect_Priority() {
		return defect_Priority;
	}

	public void setDefect_Priority(Integer defect_Priority) {
		this.defect_Priority = defect_Priority;
	}

	public String getDefect_Desc() {
		return defect_Desc;
	}

	public void setDefect_Desc(String defect_Desc) {
		this.defect_Desc = defect_Desc;
	}

	public String getDefect_DetailedDesc() {
		return defect_DetailedDesc;
	}

	public void setDefect_DetailedDesc(String defect_DetailedDesc) {
		this.defect_DetailedDesc = defect_DetailedDesc;
	}

	public Timestamp getDefect_ReportDateTime() {
		return defect_ReportDateTime;
	}

	public void setDefect_ReportDateTime(Timestamp defect_ReportDateTime) {
		this.defect_ReportDateTime = defect_ReportDateTime;
	}

	public String getDefect_CallForm() {
		return defect_CallFrom;
	}

	public void setDefect_CallForm(String defect_CallForm) {
		this.defect_CallFrom = defect_CallForm;
	}

	public Integer getDefect_IssueBy() {
		return defect_IssueBy;
	}

	public void setDefect_IssueBy(Integer defect_IssueBy) {
		this.defect_IssueBy = defect_IssueBy;
	}

	public Timestamp getDefect_IssueDateTime() {
		return defect_IssueDateTime;
	}

	public void setDefect_IssueDateTime(Timestamp defect_IssueDateTime) {
		this.defect_IssueDateTime = defect_IssueDateTime;
	}

	public Timestamp getDefect_TargetStartDateTime() {
		return defect_TargetStartDateTime;
	}

	public void setDefect_TargetStartDateTime(Timestamp defect_TargetStartDateTime) {
		this.defect_TargetStartDateTime = defect_TargetStartDateTime;
	}

	public Timestamp getDefect_TargetFinishDateTime() {
		return defect_TargetFinishDateTime;
	}

	public void setDefect_TargetFinishDateTime(Timestamp defect_TargetFinishDateTime) {
		this.defect_TargetFinishDateTime = defect_TargetFinishDateTime;
	}

	public Timestamp getDefect_ActualStartDateTime() {
		return defect_ActualStartDateTime;
	}

	public void setDefect_ActualStartDateTime(Timestamp defect_ActualStartDateTime) {
		this.defect_ActualStartDateTime = defect_ActualStartDateTime;
	}

	public Timestamp getDefect_ActualFinishDateTime() {
		return defect_ActualFinishDateTime;
	}

	public void setDefect_ActualFinishDateTime(Timestamp defect_ActualFinishDateTime) {
		this.defect_ActualFinishDateTime = defect_ActualFinishDateTime;
	}

	public Integer getDefect_CheckBy() {
		return defect_CheckBy;
	}

	public void setDefect_CheckBy(Integer defect_CheckBy) {
		this.defect_CheckBy = defect_CheckBy;
	}

	public Timestamp getDefect_CheckDateTime() {
		return defect_CheckDateTime;
	}

	public void setDefect_CheckDateTime(Timestamp defect_CheckDateTime) {
		this.defect_CheckDateTime = defect_CheckDateTime;
	}

	public String getDefect_StatusId() {
		return defect_StatusId;
	}

	public void setDefect_StatusId(String defect_StatusId) {
		this.defect_StatusId = defect_StatusId;
	}

	public String getDefect_Remarks() {
		return defect_Remarks;
	}

	public void setDefect_Remarks(String defect_Remarks) {
		this.defect_Remarks = defect_Remarks;
	}

	public Integer getDefect_CreateBy() {
		return defect_CreateBy;
	}

	public void setDefect_CreateBy(Integer defect_CreateBy) {
		this.defect_CreateBy = defect_CreateBy;
	}

	public Timestamp getDefect_CreateDateTime() {
		return defect_CreateDateTime;
	}

	public void setDefect_CreateDateTime(Timestamp defect_CreateDateTime) {
		this.defect_CreateDateTime = defect_CreateDateTime;
	}

	public Integer getDefect_LastModifyBy() {
		return defect_LastModifyBy;
	}

	public void setDefect_LastModifyBy(Integer defect_LastModifyBy) {
		this.defect_LastModifyBy = defect_LastModifyBy;
	}

	public Timestamp getDefect_LastModifyDateTime() {
		return defect_LastModifyDateTime;
	}

	public void setDefect_LastModifyDateTime(Timestamp defect_LastModifyDateTime) {
		this.defect_LastModifyDateTime = defect_LastModifyDateTime;
	}

	public String getDefect_Deleted() {
		return defect_Deleted;
	}

	public void setDefect_Deleted(String defect_Deleted) {
		this.defect_Deleted = defect_Deleted;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}

	public String getDefect_MeetKpi() {
		return defect_MeetKpi;
	}

	public void setDefect_MeetKpi(String defect_MeetKpi) {
		this.defect_MeetKpi = defect_MeetKpi;
	}

	public String getDefect_ReplyBy() {
		return defect_ReplyBy;
	}

	public void setDefect_ReplyBy(String defect_ReplyBy) {
		this.defect_ReplyBy = defect_ReplyBy;
	}

	public Timestamp getDefect_ReplyTime() {
		return defect_ReplyTime;
	}

	public void setDefect_ReplyTime(Timestamp defect_ReplyTime) {
		this.defect_ReplyTime = defect_ReplyTime;
	}

	public String getAccount_LoginId() {
		return account_LoginId;
	}

	public void setAccount_LoginId(String account_LoginId) {
		this.account_LoginId = account_LoginId;
	}

	public String getAccount_Name() {
		return account_Name;
	}

	public void setAccount_Name(String account_Name) {
		this.account_Name = account_Name;
	}

	public String getAccount_ContactNumber() {
		return account_ContactNumber;
	}

	public void setAccount_ContactNumber(String account_ContactNumber) {
		this.account_ContactNumber = account_ContactNumber;
	}

	public String getAcountGroup_Name() {
		return acountGroup_Name;
	}

	public void setAcountGroup_Name(String acountGroup_Name) {
		this.acountGroup_Name = acountGroup_Name;
	}

	public String getAccountGroup_Desc() {
		return accountGroup_Desc;
	}

	public void setAccountGroup_Desc(String accountGroup_Desc) {
		this.accountGroup_Desc = accountGroup_Desc;
	}

	public String getCauseCode_Code() {
		return causeCode_Code;
	}

	public void setCauseCode_Code(String causeCode_Code) {
		this.causeCode_Code = causeCode_Code;
	}

	public String getCauseCode_Name() {
		return causeCode_Name;
	}

	public void setCauseCode_Name(String causeCode_Name) {
		this.causeCode_Name = causeCode_Name;
	}

	public String getCauseCode_Desc() {
		return causeCode_Desc;
	}

	public void setCauseCode_Desc(String causeCode_Desc) {
		this.causeCode_Desc = causeCode_Desc;
	}

	public String getEquipment_Code() {
		return equipment_Code;
	}

	public void setEquipment_Code(String equipment_Code) {
		this.equipment_Code = equipment_Code;
	}

	public String getEquipment_Name() {
		return equipment_Name;
	}

	public void setEquipment_Name(String equipment_Name) {
		this.equipment_Name = equipment_Name;
	}

	public String getEquipment_Desc() {
		return equipment_Desc;
	}

	public void setEquipment_Desc(String equipment_Desc) {
		this.equipment_Desc = equipment_Desc;
	}

	public String getFailureClass_Code() {
		return failureClass_Code;
	}

	public void setFailureClass_Code(String failureClass_Code) {
		this.failureClass_Code = failureClass_Code;
	}

	public String getFailureClass_Name() {
		return failureClass_Name;
	}

	public void setFailureClass_Name(String failureClass_Name) {
		this.failureClass_Name = failureClass_Name;
	}

	public String getFailureClass_Desc() {
		return failureClass_Desc;
	}

	public void setFailureClass_Desc(String failureClass_Desc) {
		this.failureClass_Desc = failureClass_Desc;
	}

	public String getLocation_Code() {
		return location_Code;
	}

	public void setLocation_Code(String location_Code) {
		this.location_Code = location_Code;
	}

	public String getLocation_Name() {
		return location_Name;
	}

	public void setLocation_Name(String location_Name) {
		this.location_Name = location_Name;
	}

	public String getProblemCode_Code() {
		return problemCode_Code;
	}

	public void setProblemCode_Code(String problemCode_Code) {
		this.problemCode_Code = problemCode_Code;
	}

	public String getProblemCode_Name() {
		return problemCode_Name;
	}

	public void setProblemCode_Name(String problemCode_Name) {
		this.problemCode_Name = problemCode_Name;
	}

	public String getProblemCode_Desc() {
		return problemCode_Desc;
	}

	public void setProblemCode_Desc(String problemCode_Desc) {
		this.problemCode_Desc = problemCode_Desc;
	}

	public String getSite_Name() {
		return site_Name;
	}

	public void setSite_Name(String site_Name) {
		this.site_Name = site_Name;
	}

	public String getTool_Code() {
		return tool_Code;
	}

	public void setTool_Code(String tool_Code) {
		this.tool_Code = tool_Code;
	}

	public String getTool_Name() {
		return tool_Name;
	}

	public void setTool_Name(String tool_Name) {
		this.tool_Name = tool_Name;
	}

	public String getTool_Desc() {
		return tool_Desc;
	}

	public void setTool_Desc(String tool_Desc) {
		this.tool_Desc = tool_Desc;
	}

	public String getStatus_Name() {
		return status_Name;
	}

	public void setStatus_Name(String status_Name) {
		this.status_Name = status_Name;
	}


	public Integer getChildKey() {
		return childKey;
	}

	public void setChildKey(Integer childKey) {
		this.childKey = childKey;
	}

	public String getDefect_CallFrom() {
		return defect_CallFrom;
	}

	public void setDefect_CallFrom(String defect_CallFrom) {
		this.defect_CallFrom = defect_CallFrom;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public String getEscalation() {
		return escalation;
	}

	public void setEscalation(String escalation) {
		this.escalation = escalation;
	}

	public String getSortNum() {
		return sortNum;
	}

	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	
	
	
}
