package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DefectSchedule")
public class ScheduleXml extends BaseXml {

	private Integer scheduleKey;

	private Integer siteKey;

	private Long scheduleStartDate;

	private Long scheduleEndDate;

	private Long scheduleTime;

	private String remarks;

	private String frequency;

	private Integer accountKey;

	private Integer parentId;

	private String desc;

	private Integer createBy;

	private Long createDateTime;

	private Long lastModifyDateTime;

	private String deleted;

	private Integer locationKey;

	private Integer toolKey;

	private Integer equipmentKey;

	private Long skippedStartDate;

	private Long skippedEndDate;

	private String skipped;

	public Long getScheduleStartDate() {
		return scheduleStartDate;
	}

	public void setScheduleStartDate(Long scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}

	public Long getScheduleEndDate() {
		return scheduleEndDate;
	}

	public void setScheduleEndDate(Long scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}

	public Long getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Long scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
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

	public Long getSkippedStartDate() {
		return skippedStartDate;
	}

	public void setSkippedStartDate(Long skippedStartDate) {
		this.skippedStartDate = skippedStartDate;
	}

	public Long getSkippedEndDate() {
		return skippedEndDate;
	}

	public void setSkippedEndDate(Long skippedEndDate) {
		this.skippedEndDate = skippedEndDate;
	}

	public String getSkipped() {
		return skipped;
	}

	public void setSkipped(String skipped) {
		this.skipped = skipped;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public Integer getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(Integer scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

}
