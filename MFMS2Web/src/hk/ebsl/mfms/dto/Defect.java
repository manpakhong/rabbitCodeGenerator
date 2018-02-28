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
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;

@Entity
@Table(name = "tbl_defect")
@JsonRootName("")
public class Defect {

	@Id
	@Column(name = "d_Key")
	@GeneratedValue
	private Integer key;

	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "d_SiteKey", insertable = false, updatable = false)
//	private Site site;
	
	@Column(name = "d_SiteKey")
	private Integer siteKey;

	@Column(name = "d_Code")
	private String code;

	@Column(name = "d_CreateChannel")
	private String createChannel;

	@Column(name = "d_LocationKey")
	private Integer locationKey;

	@Column(name = "d_FailureClassKey")
	private Integer failureClassKey;

	@Column(name = "d_ProblemCodeKey")
	private Integer problemCodeKey;

	@Column(name = "d_CauseCodeKey")
	private Integer causeCodeKey;

	@Column(name = "d_ToolKey")
	private Integer toolKey;

	@Column(name = "d_EquipmentKey")
	private Integer equipmentKey;

	@Column(name = "d_Contact_Name")
	private String contactName;

	@Column(name = "d_Contact_Tel")
	private String contactTel;

	@Column(name = "d_Contact_Email")
	private String contactEmail;

	@Column(name = "d_Emergency_Tel")
	private String emergencyTel;

	@Column(name = "d_AssignedGroupKey")
	private Integer assignedGroupKey;

	@Column(name = "d_AssignedAccountKey")
	private Integer assignedAccountKey;

	@Column(name = "d_Priority")
	private Integer priority;

	@Column(name = "d_Desc")
	private String desc;

	@Column(name = "d_DetailedDesc")
	private String detailedDesc;

	@Column(name = "d_ReportDateTime")
	private Timestamp reportDateTime;

	@Column(name = "d_CallFrom")
	private String callFrom;

	@Column(name = "d_IssueBy")
	private Integer issueBy;

	@Column(name = "d_IssueDateTime")
	private Timestamp issueDateTime;

	@Column(name = "d_TargetStartDateTime")
	private Timestamp targetStartDateTime;

	@Column(name = "d_TargetFinishDateTime")
	private Timestamp targetFinishDateTime;

	@Column(name = "d_ActualStartDateTime")
	private Timestamp actualStartDateTime;

	@Column(name = "d_ActualFinishDateTime")
	private Timestamp actualFinishDateTime;

	@Column(name = "d_CheckBy")
	private Integer checkBy;

	@Column(name = "d_CheckDateTime")
	private Timestamp checkDateTime;

	@Column(name = "d_StatusID")
	private String statusID;

	@Column(name = "d_Remarks")
	private String remarks;

	@Column(name = "d_CreateBy")
	private Integer createBy;

	@Column(name = "d_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "d_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "d_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "d_Deleted")
	private String deleted;

	@Column(name = "d_MeetKpi")
	private String meetKpi;
	
	@Column(name = "d_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_LocationKey", insertable = false, updatable = false)
	private Location location;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_FailureClassKey", insertable = false, updatable = false)
	private FailureClass failureClass;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_ProblemCodeKey", insertable = false, updatable = false)
	private ProblemCode problemCode;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_EquipmentKey", insertable = false, updatable = false)
	private Equipment equipment;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_CauseCodeKey", insertable = false, updatable = false)
	private CauseCode causeCode;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_ToolKey", insertable = false, updatable = false)
	private Tool tool;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "d_AssignedGroupKey", insertable = false, updatable = false)
	private AccountGroup assignedGroup;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "d_AssignedAccountKey", insertable = false, updatable = false)
	private UserAccount assignedAccount;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "d_IssueBy", insertable = false, updatable = false)
	private UserAccount issueByAccount;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name = "d_CheckBy", insertable = false, updatable = false)
	private UserAccount checkByAccount;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_StatusID", insertable = false, updatable = false)
	private Status status;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "d_SiteKey", insertable = false, updatable = false)
	private Site site;
	
	@Transient
	private String callCenterEmail;
	
	@Transient
	private Integer mobileKey;
	@Transient
	private String createStatus;
	
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

	public String getCreateChannel() {
		return createChannel;
	}

	public void setCreateChannel(String createChannel) {
		this.createChannel = createChannel;
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

	public Timestamp getTargetFinishDateTime() {
		return targetFinishDateTime;
	}

	public void setTargetFinishDateTime(Timestamp targetFinishDateTime) {
		this.targetFinishDateTime = targetFinishDateTime;
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

	public String getMeetKpi() {
		return meetKpi;
	}

	public void setMeetKpi(String meetKpi) {
		this.meetKpi = meetKpi;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
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

	public AccountGroup getAssignedGroup() {
		return assignedGroup;
	}

	public void setAssignedGroup(AccountGroup assignedGroup) {
		this.assignedGroup = assignedGroup;
	}

	public UserAccount getAssignedAccount() {
		return assignedAccount;
	}

	public void setAssignedAccount(UserAccount assignedAccount) {
		this.assignedAccount = assignedAccount;
	}

	public UserAccount getIssueByAccount() {
		return issueByAccount;
	}

	public void setIssueByAccount(UserAccount issueByAccount) {
		this.issueByAccount = issueByAccount;
	}

	public UserAccount getCheckByAccount() {
		return checkByAccount;
	}

	public void setCheckByAccount(UserAccount checkByAccount) {
		this.checkByAccount = checkByAccount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getCallCenterEmail() {
		return callCenterEmail;
	}

	public void setCallCenterEmail(String callCenterEmail) {
		this.callCenterEmail = callCenterEmail;
	}


	public String getCreateStatus() {
		return createStatus;
	}

	public void setCreateStatus(String createStatus) {
		this.createStatus = createStatus;
	}

	public Integer getMobileKey() {
		return mobileKey;
	}

	public void setMobileKey(Integer mobileKey) {
		this.mobileKey = mobileKey;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	

}
