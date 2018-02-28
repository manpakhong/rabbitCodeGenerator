package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tbl_defectschedulehistory")
public class DefectScheduleHistory {

	@Id
	@Column(name = "dsh_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name = "dsh_SiteKey")
	private Integer siteKey;

	@Column(name = "dsh_DefectScheduleKey")
	private Integer defectScheduleKey;

	@Column(name = "dsh_FinishDatetime")
	private Timestamp finishDateTime;
	
	@Column(name = "dsh_TaskDatetime")
	private Timestamp taskDateTime;
	
	@Column(name="dsh_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@Transient
	private Integer mobileKey;
	
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getDefectScheduleKey() {
		return defectScheduleKey;
	}

	public void setDefectScheduleKey(Integer defectScheduleKey) {
		this.defectScheduleKey = defectScheduleKey;
	}

	public Timestamp getFinishDateTime() {
		return finishDateTime;
	}

	public void setFinishDateTime(Timestamp finishDateTime) {
		this.finishDateTime = finishDateTime;
	}

	public Timestamp getTaskDateTime() {
		return taskDateTime;
	}

	public void setTaskDateTime(Timestamp taskDateTime) {
		this.taskDateTime = taskDateTime;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
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
