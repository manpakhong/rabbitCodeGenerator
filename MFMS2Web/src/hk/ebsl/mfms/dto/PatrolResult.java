package hk.ebsl.mfms.dto;

import hk.ebsl.mfms.json.CalendarJSON.Frequency;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_patrolresult")
public class PatrolResult implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "pr_Key")
	private int resultKey;

	@Column(name = "pr_SiteKey", nullable = true)
	private Integer siteKey;

	@Column(name = "pr_RouteDefKey")
	private int routeDefKey;

	@Column(name = "pr_GroupNum")
	private int groupNum;

	@Column(name = "pr_CoLocationKey")
	private int correctLocationKey;

	@Column(name = "pr_LocationKey")
	private int locationKey;

	@Column(name = "pr_SeqNum")
	private int seqNum;

	@Column(name = "pr_TimeAttended")
	private Timestamp timeAttended;

	@Column(name = "pr_PersonAttended")
	private int personAttended;

	@Column(name = "pr_Reason")
	private String reason;

	@Column(name = "pr_Completed")
	private String completed;

	@Column(name = "pr_CreateBy")
	private int createBy;

	@Column(name = "pr_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "pr_LastModifyBy")
	private int lastModifyBy;

	@Column(name = "pr_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "pr_Deleted")
	private String deleted;

	@Column(name="pr_PatrolScheduleKey")
	private int patrolScheduleKey;
	
	@Column(name="pr_PatrolStatus")
	private String patrolStatus;
	
	@Column(name="pr_MinPtDur")
	private int minPtDur;
	
	@Column(name="pr_MinPtDurUnit")
	private int minPtDurUnit;
	
	@Column(name="pr_MaxPtDur")
	private int maxPtDur;
	
	@Column(name="pr_MaxPtDurUnit")
	private int maxPtDurUnit;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pr_RouteDefKey", insertable = false, updatable = false)
	private RouteDef routeDef;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pr_CoLocationKey", insertable = false, updatable = false)
	private Location correctionLocation;

	// @ManyToOne(fetch = FetchType.LAZY)
	// @JoinColumn(name = "pr_LocationKey", insertable = false, updatable =
	// false)
	// private Location location;
	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "pr_CreateBy", insertable = false, updatable = false, nullable = true)
	private UserAccount person;
	
	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "pr_PatrolScheduleKey", insertable = false, updatable = false)
	private PatrolSchedule patrolSchedule;
	
	@Transient
	private Integer mobileGroupNum;
	@Transient
	private Integer mobileKey;
	
	@Column(name="pr_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	public enum PatrolStatus {
		Disorder("Disorder"), TooFast("TooFast"), TooSlow("TooSlow"), Normal(
				"Normal");

		private String text;
		private static final  Map<String, PatrolStatus> statusMap = new HashMap<String, PatrolStatus>();

		private PatrolStatus(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
		
		static {
			for (PatrolStatus ps : PatrolStatus.values()) {
				statusMap.put(ps.text, ps);
			}
		}

		public static PatrolStatus fromString(String i) {
			PatrolStatus type = statusMap.get(i);
			if (type == null)
				return null;
			return type;
		}

	};
	
	
	public int getResultKey() {
		return resultKey;
	}

	public void setResultKey(int resultKey) {
		this.resultKey = resultKey;
	}

	public int getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

	public int getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(int groupNum) {
		this.groupNum = groupNum;
	}

	public int getCorrectLocationKey() {
		return correctLocationKey;
	}

	public void setCorrectLocationKey(int correctLocationKey) {
		this.correctLocationKey = correctLocationKey;
	}

	public int getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(int locationKey) {
		this.locationKey = locationKey;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public Timestamp getTimeAttended() {
		return timeAttended;
	}

	public void setTimeAttended(Timestamp timeAttended) {
		this.timeAttended = timeAttended;
	}

	public int getPersonAttended() {
		return personAttended;
	}

	public void setPersonAttended(int personAttended) {
		this.personAttended = personAttended;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
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

	public RouteDef getRouteDef() {
		return routeDef;
	}

	public void setRouteDef(RouteDef routeDef) {
		this.routeDef = routeDef;
	}

	public Location getCorrectionLocation() {
		return correctionLocation;
	}

	public void setCorrectionLocation(Location correctionLocation) {
		this.correctionLocation = correctionLocation;
	}

	// public Location getLocation() {
	// return location;
	// }
	//
	// public void setLocation(Location location) {
	// this.location = location;
	// }

	public UserAccount getPerson() {
		return person;
	}

	public void setPerson(UserAccount person) {
		this.person = person;
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

	public int getPatrolScheduleKey() {
		return patrolScheduleKey;
	}

	public void setPatrolScheduleKey(int patrolScheduleKey) {
		this.patrolScheduleKey = patrolScheduleKey;
	}

	public PatrolSchedule getPatrolSchedule() {
		return patrolSchedule;
	}

	public void setPatrolSchedule(PatrolSchedule patrolSchedule) {
		this.patrolSchedule = patrolSchedule;
	}

	public String getPatrolStatus() {
		return patrolStatus;
	}

	public void setPatrolStatus(String patrolStatus) {
		this.patrolStatus = patrolStatus;
	}

	public int getMinPtDur() {
		return minPtDur;
	}

	public void setMinPtDur(int minPtDur) {
		this.minPtDur = minPtDur;
	}

	public int getMinPtDurUnit() {
		return minPtDurUnit;
	}

	public void setMinPtDurUnit(int minPtDurUnit) {
		this.minPtDurUnit = minPtDurUnit;
	}

	public int getMaxPtDur() {
		return maxPtDur;
	}

	public void setMaxPtDur(int maxPtDur) {
		this.maxPtDur = maxPtDur;
	}

	public int getMaxPtDurUnit() {
		return maxPtDurUnit;
	}

	public void setMaxPtDurUnit(int maxPtDurUnit) {
		this.maxPtDurUnit = maxPtDurUnit;
	}

	public Integer getMobileGroupNum() {
		return mobileGroupNum;
	}

	public void setMobileGroupNum(Integer mobileGroupNum) {
		this.mobileGroupNum = mobileGroupNum;
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
