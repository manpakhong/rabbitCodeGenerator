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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "tbl_defectactionlog")
public class DefectActionLog  {

	@Id
	@Column(name = "dal_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "dal_ActionType")
	private String actionType;
	

	@Column(name = "dal_ActionDateTime")
	private Timestamp actionDateTime;
	
	@Column(name = "dal_SiteKey")
	private Integer siteKey;
	
	@Column(name = "dal_DefectKey")
	private Integer defectKey;

	@Column(name = "dal_Code")
	private String code;

	@Column(name = "dal_LocationKey")
	private Integer locationKey;

	@Column(name = "dal_FailureClassKey")
	private Integer failureClassKey;

	@Column(name = "dal_ProblemCodeKey")
	private Integer problemCodeKey;

	@Column(name = "dal_CauseCodeKey")
	private Integer causeCodeKey;

	@Column(name = "dal_ToolKey")
	private Integer toolKey;

	@Column(name = "dal_EquipmentKey")
	private Integer equipmentKey;

	@Column(name = "dal_Contact_Name")
	private String contactName;

	@Column(name = "dal_Contact_Tel")
	private String contactTel;

	@Column(name = "dal_Contact_Email")
	private String contactEmail;

	@Column(name = "dal_Emergency_Tel")
	private String emergencyTel;

	@Column(name = "dal_AssignedGroupKey")
	private Integer assignedGroupKey;

	@Column(name = "dal_AssignedAccountKey")
	private Integer assignedAccountKey;

	@Column(name = "dal_Priority")
	private Integer priority;

	@Column(name = "dal_Desc")
	private String desc;

	@Column(name = "dal_DetailedDesc")
	private String detailedDesc;

	@Column(name = "dal_ReportDateTime")
	private Timestamp reportDateTime;

	@Column(name = "dal_CallFrom")
	private String callFrom;

	@Column(name = "dal_IssueBy")
	private Integer issueBy;

	@Column(name = "dal_IssueDateTime")
	private Timestamp issueDateTime;

	@Column(name = "dal_TargetStartDateTime")
	private Timestamp targetStartDateTime;

	@Column(name = "dal_TargetAttendDateTime")
	private Timestamp targetAttendDateTime;

	@Column(name = "dal_ActualStartDateTime")
	private Timestamp actualStartDateTime;

	@Column(name = "dal_ActualFinishDateTime")
	private Timestamp actualFinishDateTime;

	@Column(name = "dal_CheckBy")
	private Integer checkBy;

	@Column(name = "dal_CheckDateTime")
	private Timestamp checkDateTime;

	@Column(name = "dal_StatusID")
	private String statusID;

	@Column(name = "dal_Remarks")
	private String remarks;

	@Column(name = "dal_CreateBy")
	private Integer createBy;
	
	@Column(name = "dal_ActionBy")
	private Integer actionBy;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_ActionBy", insertable = false, updatable = false)
	private UserAccount account;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_IssueBy", insertable = false, updatable = false)
	private UserAccount accountIssueBy;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "dal_CheckBy", insertable = false, updatable = false)
//	private UserAccount accountCheckBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_CreateBy", insertable = false, updatable = false)
	private UserAccount accountCreateBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_LastModifyBy", insertable = false, updatable = false)
	private UserAccount accountLastModifyBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_AssignedGroupKey", insertable = false, updatable = false)
	private AccountGroup accountGroup;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_LocationKey", insertable = false, updatable = false)
	private Location location;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_FailureClassKey", insertable = false, updatable = false)
	private FailureClass failureClass;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_ProblemCodeKey", insertable = false, updatable = false)
	private ProblemCode problemCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_CauseCodeKey", insertable = false, updatable = false)
	private CauseCode causeCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_ToolKey", insertable = false, updatable = false)
	private Tool tool;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dal_EquipmentKey", insertable = false, updatable = false)
	private Equipment equipment;

	@Column(name = "dal_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "dal_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "dal_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "dal_Deleted")
	private String deleted;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
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

	public Integer getToolKey() {
		return toolKey;
	}

	public void setToolKey(Integer toolKey) {
		this.toolKey = toolKey;
	}

	public Integer getEquipmentKey() {
		return equipmentKey;
	}

	public void setEquipmentKey(Integer equipmentKey) {
		this.equipmentKey = equipmentKey;
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

	public Integer getAssignedGroupKey() {
		return assignedGroupKey;
	}

	public void setAssignedGroupKey(Integer assignedGroupKey) {
		this.assignedGroupKey = assignedGroupKey;
	}

	public Integer getAssignedAccountKey() {
		return assignedAccountKey;
	}

	public void setAssignedAccountKey(Integer assignedAccountKey) {
		this.assignedAccountKey = assignedAccountKey;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
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

	public Timestamp getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Timestamp reportDateTime) {
		this.reportDateTime = reportDateTime;
	}

	public String getCallFrom() {
		return callFrom;
	}

	public void setCallFrom(String callFrom) {
		this.callFrom = callFrom;
	}

	public Integer getIssueBy() {
		return issueBy;
	}

	public void setIssueBy(Integer issueBy) {
		this.issueBy = issueBy;
	}

	public Timestamp getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(Timestamp issueDateTime) {
		this.issueDateTime = issueDateTime;
	}

	public Timestamp getTargetStartDateTime() {
		return targetStartDateTime;
	}

	public void setTargetStartDateTime(Timestamp targetStartDateTime) {
		this.targetStartDateTime = targetStartDateTime;
	}

	public Timestamp getActualStartDateTime() {
		return actualStartDateTime;
	}

	public void setActualStartDateTime(Timestamp actualStartDateTime) {
		this.actualStartDateTime = actualStartDateTime;
	}

	public Timestamp getActualFinishDateTime() {
		return actualFinishDateTime;
	}

	public void setActualFinishDateTime(Timestamp actualFinishDateTime) {
		this.actualFinishDateTime = actualFinishDateTime;
	}

	public Integer getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(Integer checkBy) {
		this.checkBy = checkBy;
	}

	public Timestamp getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(Timestamp checkDateTime) {
		this.checkDateTime = checkDateTime;
	}

	public String getStatusID() {
		return statusID;
	}

	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Integer getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(Integer lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public Timestamp getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(Timestamp lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Timestamp getTargetAttendDateTime() {
		return targetAttendDateTime;
	}

	public void setTargetAttendDateTime(Timestamp targetAttendDateTime) {
		this.targetAttendDateTime = targetAttendDateTime;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Timestamp getActionDateTime() {
		return actionDateTime;
	}

	public void setActionDateTime(Timestamp actionDateTime) {
		this.actionDateTime = actionDateTime;
	}

	public Integer getDefectKey() {
		return defectKey;
	}

	public void setDefectKey(Integer defectKey) {
		this.defectKey = defectKey;
	}

	public Integer getActionBy() {
		return actionBy;
	}

	public void setActionBy(Integer actionBy) {
		this.actionBy = actionBy;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public FailureClass getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(FailureClass failureClass) {
		this.failureClass = failureClass;
	}

	public ProblemCode getProblemCode() {
		return problemCode;
	}

	public void setProblemCode(ProblemCode problemCode) {
		this.problemCode = problemCode;
	}

	public CauseCode getCauseCode() {
		return causeCode;
	}

	public void setCauseCode(CauseCode causeCode) {
		this.causeCode = causeCode;
	}

	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public AccountGroup getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(AccountGroup accountGroup) {
		this.accountGroup = accountGroup;
	}

	public UserAccount getAccountIssueBy() {
		return accountIssueBy;
	}

	public void setAccountIssueBy(UserAccount accountIssueBy) {
		this.accountIssueBy = accountIssueBy;
	}

//	public UserAccount getAccountCheckBy() {
//		return accountCheckBy;
//	}
//
//	public void setAccountCheckBy(UserAccount accountCheckBy) {
//		this.accountCheckBy = accountCheckBy;
//	}
//
	public UserAccount getAccountCreateBy() {
		return accountCreateBy;
	}

	public void setAccountCreateBy(UserAccount accountCreateBy) {
		this.accountCreateBy = accountCreateBy;
	}

	public UserAccount getAccountLastModifyBy() {
		return accountLastModifyBy;
	}

	public void setAccountLastModifyBy(UserAccount accountLastModifyBy) {
		this.accountLastModifyBy = accountLastModifyBy;
	}

}
