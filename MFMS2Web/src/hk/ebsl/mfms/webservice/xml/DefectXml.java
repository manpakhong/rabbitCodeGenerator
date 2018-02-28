package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Defect")
public class DefectXml extends BaseXml{

	
	private Boolean isCreate;
	
	private Integer key;

	private Integer siteKey;

	private String code;

	private String createChannel;

	private Integer locationKey;

	private Integer failureClassKey;

	private Integer problemCodeKey;

	private Integer causeCodeKey;

	private Integer toolKey;

	private Integer equipmentKey;

	private String contactName;

	private String contactTel;

	private String contactEmail;

	private String emergencyTel;

	private Integer assignedGroupKey;

	private Integer assignedAccountKey;

	private Integer priority;

	private String desc;

	private String detailedDesc;

	private Long reportDateTime;

	private String callFrom;

	private Integer issueBy;

	private Long issueDateTime;

	private Long targetStartDateTime;

	private Long targetFinishDateTime;

	private Long actualStartDateTime;

	private Long actualFinishDateTime;

	private Integer checkBy;

	private Long checkDateTime;

	private String statusID;

	private String remarks;

	private Integer createBy;

	private Long createDateTime;

	private Integer lastModifyBy;

	private Long lastModifyDateTime;

	private String deleted;

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

	public Long getReportDateTime() {
		return reportDateTime;
	}

	public void setReportDateTime(Long reportDateTime) {
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

	public Long getIssueDateTime() {
		return issueDateTime;
	}

	public void setIssueDateTime(Long issueDateTime) {
		this.issueDateTime = issueDateTime;
	}

	public Long getTargetStartDateTime() {
		return targetStartDateTime;
	}

	public void setTargetStartDateTime(Long targetStartDateTime) {
		this.targetStartDateTime = targetStartDateTime;
	}

	public Long getTargetFinishDateTime() {
		return targetFinishDateTime;
	}

	public void setTargetFinishDateTime(Long targetFinishDateTime) {
		this.targetFinishDateTime = targetFinishDateTime;
	}

	public Long getActualStartDateTime() {
		return actualStartDateTime;
	}

	public void setActualStartDateTime(Long actualStartDateTime) {
		this.actualStartDateTime = actualStartDateTime;
	}

	public Long getActualFinishDateTime() {
		return actualFinishDateTime;
	}

	public void setActualFinishDateTime(Long actualFinishDateTime) {
		this.actualFinishDateTime = actualFinishDateTime;
	}

	public Integer getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(Integer checkBy) {
		this.checkBy = checkBy;
	}

	public Long getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(Long checkDateTime) {
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

	public Long getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Long createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Integer getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(Integer lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public Long getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(Long lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Boolean getIsCreate() {
		return isCreate;
	}

	public void setIsCreate(Boolean isCreate) {
		this.isCreate = isCreate;
	}

}
