package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_defectschedule")
public class DefectSchedule {

	@Id
	@GeneratedValue
	@Column(name = "ds_Key")
	private Integer scheduleKey;

	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "ds_siteKey", insertable = false, updatable = false)
//	private Site site;
	
	@Column(name = "ds_siteKey")
	private Integer siteKey;

	@Column(name = "ds_ScheduleStartDate")
	private Timestamp scheduleStartDate;

	@Column(name = "ds_ScheduleEndDate")
	private Timestamp scheduleEndDate;

	@Column(name = "ds_ScheduleTime")
	private Timestamp scheduleTime;

	@Column(name = "ds_Remarks")
	private String remarks;

	@Column(name = "ds_Frequency")
	private String frequency;

	@Column(name = "ds_parentId")
	private int parentId;

	@Column(name = "ds_Description")
	private String desc;

	@Column(name = "ds_CreateBy")
	private Integer createBy;

	@Column(name = "ds_CreateDatetime")
	private Timestamp createDateTime;

	@Column(name = "ds_LastModifyBy")
	private int lastModifyBy;

	@Column(name = "ds_LastModifyDatetime")
	private Timestamp lastModifyDateTime;

	@Column(name = "ds_Deleted")
	private String deleted;

	@Column(name = "ds_LocationKey")
	private Integer locationKey;

	@Column(name = "ds_ToolKey")
	private Integer toolKey;

	@Column(name = "ds_EquipmentKey")
	private Integer equipmentKey;

	@Column(name = "ds_Skipped_StartDate")
	private Timestamp skippedStartDate;

	@Column(name = "ds_Skipped_EndDate")
	private Timestamp skippedEndDate;

	@Column(name = "ds_Skipped")
	private String skipped;
	
	@Column(name = "ds_FinishDatetime")
	private Timestamp finishDateTime;
	
	@Column(name="ds_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleKey")
	private List<DefectScheduleAccount> scheduleAccount;

	public Timestamp getScheduleStartDate() {
		return scheduleStartDate;
	}

	public void setScheduleStartDate(Timestamp scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}

	public Timestamp getScheduleEndDate() {
		return scheduleEndDate;
	}

	public void setScheduleEndDate(Timestamp scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}

	public Timestamp getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Timestamp scheduleTime) {
		this.scheduleTime = scheduleTime;
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

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public int getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(int lastModifyBy) {
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public Integer getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(Integer scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public List<DefectScheduleAccount> getScheduleAccount() {
		return scheduleAccount;
	}

	public void setScheduleAccount(List<DefectScheduleAccount> scheduleAccount) {
		this.scheduleAccount = scheduleAccount;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
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

	public String getSkipped() {
		return skipped;
	}

	public void setSkipped(String skipped) {
		this.skipped = skipped;
	}

	public Timestamp getSkippedStartDate() {
		return skippedStartDate;
	}

	public void setSkippedStartDate(Timestamp skippedStartDate) {
		this.skippedStartDate = skippedStartDate;
	}

	public Timestamp getSkippedEndDate() {
		return skippedEndDate;
	}

	public void setSkippedEndDate(Timestamp skippedEndDate) {
		this.skippedEndDate = skippedEndDate;
	}
	
	public Timestamp getFinishDateTime() {
		return finishDateTime;
	}

	public void setFinishDateTime(Timestamp finishDateTime) {
		this.finishDateTime = finishDateTime;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}



}
