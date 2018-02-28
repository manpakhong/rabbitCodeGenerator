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
@Table (name="tbl_patrolscheduleaccount")
public class PatrolScheduleAccount implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "psa_Key")
	private int scheduleAccountKey;
	
	@Column(name = "psa_PatrolScheduleKey")
	private int scheduleKey;
	
	@Column(name = "psa_AccountKey")
	private int accountKey;
	
	@Column(name = "psa_CreateBy")
	private int createBy;
	
	@Column(name = "psa_CreateDatetime")
	private Timestamp createDateTime;
	
	@Column(name = "psa_LastModifyBy")
	private int lastModifyBy;
	
	@Column(name = "psa_LastModifyDatetime")
	private Timestamp lastModifyTime;
	
	@Column(name = "psa_Deleted")
	private String deleted;
	
	@Column(name = "psa_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="psa_PatrolScheduleKey",insertable=false,updatable=false)
	private PatrolSchedule patrolSchedule;
	
	@JsonIgnore
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="psa_AccountKey",insertable=false,updatable=false)
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

	public int getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(int accountKey) {
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

	public PatrolSchedule getPatrolSchedule() {
		return patrolSchedule;
	}

	public void setPatrolSchedule(PatrolSchedule patrolSchedule) {
		this.patrolSchedule = patrolSchedule;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}
	
	
	
	
	
	
}
