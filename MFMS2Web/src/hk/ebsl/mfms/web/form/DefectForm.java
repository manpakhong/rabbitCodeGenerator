package hk.ebsl.mfms.web.form;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.utility.FileBucket;

public class DefectForm {

	private List<FailureClass> failureClassList;

	private List<ProblemCode> problemCodeList;

	private List<Tool> toolList;

	private List<Equipment> equipmentList;

	private List<CauseCode> causeCodeList;

	private List<Priority> priorityList;

	private List<StatusFlow> statusList;

	private List<AccountGroup> accountGroupList;

	private List<UserAccount> accountList;

	private List<Location> locationList;
	
	private List<FileBucket> fileList;

	private LocationTreeNode availableLocationTree;

	private FileBucket video = new FileBucket();

	private FileBucket picture1 = new FileBucket();

	private FileBucket picture2 = new FileBucket();

	private FileBucket picture3 = new FileBucket();

	private FileBucket picture4 = new FileBucket();

	private FileBucket picture5 = new FileBucket();

	private String referrer;
	
	private String deletedFile;
	
	private Integer photoRemain;
	
	private Integer videoRemain;

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

	private String reportDateTime;

	private String callFrom;

	private Integer issueBy;

	private String issueDateTime;

	private String targetStartDateTime;

	private String targetFinishDateTime;

	private String actualStartDateTime;

	private String actualFinishDateTime;

	private Integer checkBy;

	private String checkDateTime;

	private String statusID;
	
	private String orgStatusID;

	private String remarks;

	private Integer createBy;

	private Timestamp createDateTime;

	private String lastModifyBy;

	private String lastModifyDateTime;

	private Boolean delete;

	private Boolean readOnly;

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

	public List<Tool> getToolList() {
		return toolList;
	}

	public void setToolList(List<Tool> toolList) {
		this.toolList = toolList;
	}

	public List<Equipment> getEquipmentList() {
		return equipmentList;
	}

	public void setEquipmentList(List<Equipment> equipmentList) {
		this.equipmentList = equipmentList;
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

	public Integer getIssueBy() {
		return issueBy;
	}

	public void setIssueBy(Integer issueBy) {
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

	public Integer getCheckBy() {
		return checkBy;
	}

	public void setCheckBy(Integer checkBy) {
		this.checkBy = checkBy;
	}

	public String getCheckDateTime() {
		return checkDateTime;
	}

	public void setCheckDateTime(String checkDateTime) {
		this.checkDateTime = checkDateTime;
	}

	public String getStatusID() {
		return statusID;
	}

	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}

	public String getOrgStatusID() {
		return orgStatusID;
	}

	public void setOrgStatusID(String orgStatusID) {
		this.orgStatusID = orgStatusID;
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

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public List<AccountGroup> getAccountGroupList() {
		return accountGroupList;
	}

	public void setAccountGroupList(List<AccountGroup> accountGroupList) {
		this.accountGroupList = accountGroupList;
	}

	public List<UserAccount> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<UserAccount> accountList) {
		this.accountList = accountList;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public void setTargetFinishDateTime(String targetFinishDateTime) {
		this.targetFinishDateTime = targetFinishDateTime;
	}

	public void setActualStartDateTime(String actualStartDateTime) {
		this.actualStartDateTime = actualStartDateTime;
	}

	public void setActualFinishDateTime(String actualFinishDateTime) {
		this.actualFinishDateTime = actualFinishDateTime;
	}

	public String getTargetFinishDateTime() {
		return targetFinishDateTime;
	}

	public String getActualStartDateTime() {
		return actualStartDateTime;
	}

	public String getActualFinishDateTime() {
		return actualFinishDateTime;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public List<StatusFlow> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<StatusFlow> statusList) {
		this.statusList = statusList;
	}

	public String getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public String getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(String lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	public FileBucket getVideo() {
		return video;
	}

	public void setVideo(FileBucket video) {
		this.video = video;
	}

	public FileBucket getPicture1() {
		return picture1;
	}

	public void setPicture1(FileBucket picture1) {
		this.picture1 = picture1;
	}

	public FileBucket getPicture2() {
		return picture2;
	}

	public void setPicture2(FileBucket picture2) {
		this.picture2 = picture2;
	}

	public FileBucket getPicture3() {
		return picture3;
	}

	public void setPicture3(FileBucket picture3) {
		this.picture3 = picture3;
	}

	public FileBucket getPicture4() {
		return picture4;
	}

	public void setPicture4(FileBucket picture4) {
		this.picture4 = picture4;
	}

	public FileBucket getPicture5() {
		return picture5;
	}

	public void setPicture5(FileBucket picture5) {
		this.picture5 = picture5;
	}

	public Integer getPhotoRemain() {
		return photoRemain;
	}

	public void setPhotoRemain(Integer photoRemain) {
		this.photoRemain = photoRemain;
	}

	public Integer getVideoRemain() {
		return videoRemain;
	}

	public void setVideoRemain(Integer videoRemain) {
		this.videoRemain = videoRemain;
	}

	public List<FileBucket> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileBucket> fileList) {
		this.fileList = fileList;
	}

	public String getDeletedFile() {
		return deletedFile;
	}

	public void setDeletedFile(String deletedFile) {
		this.deletedFile = deletedFile;
	}

}
