package hk.ebsl.mfms.dto;

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_patrolschedule")
public class PatrolSchedule implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "ps_Key")
	private int scheduleKey;

	@Column(name = "ps_SiteKey", nullable = true)
	private Integer siteKey;

	@Column(name = "ps_RouteDefKey")
	private int routeDefKey;

	@Column(name = "ps_ScheduleStartDate")
	private Timestamp scheduleStartDate;

	@Column(name = "ps_ScheduleEndDate")
	private Timestamp scheduleEndDate;

	@Column(name = "ps_ScheduleTime")
	private Timestamp scheduleTime;

	@Column(name = "ps_Status")
	private int Status;

	@Column(name = "ps_Frequency")
	private String frequency;

	@Column(name = "ps_parentId")
	private int parentId;

	@Column(name = "ps_Remarks")
	private String remarks;

	@Column(name = "ps_CreateBy")
	private int createBy;

	@Column(name = "ps_CreateDatetime")
	private Timestamp createDateTime;

	@Column(name = "ps_LastModifyBy")
	private int lastModifyBy;

	@Column(name = "ps_LastModifyDatetime")
	private Timestamp lastModifyDateTime;

	@Column(name = "ps_Deleted")
	private String deleted;

	@Column(name = "ps_Skipped")
	private String skipped;

	@Column(name = "ps_Skipped_StartDate")
	private Timestamp skippedStartDate;

	@Column(name = "ps_Skipped_EndDate")
	private Timestamp skippedEndDate;
	
	@Column(name = "ps_LastAttendTime")
	private Timestamp lastAttendTime;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleKey")
	@Where(clause="psa_Deleted='N'")
	private List<PatrolScheduleAccount> scheduleAccount;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ps_RouteDefKey", insertable = false, updatable = false)
	private RouteDef routeDef;

	@Column(name="ps_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	public int getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(int scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public int getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

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

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getSkipped() {
		return skipped;
	}

	public void setSkipped(String skipped) {
		this.skipped = skipped;
	}

	public List<PatrolScheduleAccount> getScheduleAccount() {
		return scheduleAccount;
	}

	public void setScheduleAccount(List<PatrolScheduleAccount> scheduleAccount) {
		this.scheduleAccount = scheduleAccount;
	}

	public RouteDef getRouteDef() {
		return routeDef;
	}

	public void setRouteDef(RouteDef routeDef) {
		this.routeDef = routeDef;
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

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {

		if (siteKey == null) {
			this.siteKey = -1;
		} else {
			this.siteKey = siteKey;
		}
	}

	public Timestamp getLastAttendTime() {
		return lastAttendTime;
	}

	public void setLastAttendTime(Timestamp lastAttendTime) {
		this.lastAttendTime = lastAttendTime;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


}
