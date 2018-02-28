package hk.ebsl.mfms.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_defectscheduleaccount")
public class DefectScheduleAccount implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "dsa_Key")
	private int scheduleAccountKey;

	@Column(name = "dsa_DefectScheduleKey")
	private int scheduleKey;
	
	@Column(name = "dsa_SiteKey")
	private int siteKey;

	@Column(name = "dsa_AccountKey")
	private Integer accountKey;

	@Column(name = "dsa_CreateBy")
	private int createBy;

	@Column(name = "dsa_CreateDatetime")
	private Timestamp createDateTime;

	@Column(name = "dsa_LastModifyBy")
	private int lastModifyBy;

	@Column(name = "dsa_LastModifyDatetime")
	private Timestamp lastModifyTime;

	@Column(name = "dsa_Deleted")
	private String deleted;
	
	@Column(name="dsa_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dsa_DefectScheduleKey", insertable = false, updatable = false, nullable=true)
	private DefectSchedule defectSchedule;
	
	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "dsa_AccountKey", insertable = false, updatable = false)
	private UserAccount userAccount;

	public int getScheduleAccountKey() {
		return scheduleAccountKey;
	}

	public void setScheduleAccountKey(int scheduleAccountKey) {
		this.scheduleAccountKey = scheduleAccountKey;
	}

	public int getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(int scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

	public int getCreateBy() {
		return createBy;
	}

	public void setCreateBy(int createBy) {
		this.createBy = createBy;
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

	public Timestamp getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Timestamp lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	@JsonIgnore
	public DefectSchedule getDefectSchedule() {
		return defectSchedule;
	}
	@JsonIgnore
	public void setDefectSchedule(DefectSchedule defectSchedule) {
		this.defectSchedule = defectSchedule;
	}

	public int getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(int siteKey) {
		this.siteKey = siteKey;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


}
