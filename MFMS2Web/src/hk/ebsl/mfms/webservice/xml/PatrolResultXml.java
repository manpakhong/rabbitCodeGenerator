package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PatrolResult")
public class PatrolResultXml extends BaseXml {

	private Integer resultKey;
	private Integer routeDefKey;
	private Integer groupNum;
	private Integer correctLocationKey;
	private Integer locationKey;
	private Integer seqNum;
	private Long timeAttended;
	private String reason;
	private String completed;
	private Integer createBy;
	private Long createDateTime;
	private Integer lastModifyBy;
	private Long lastModifyDateTime;
	private String deleted;
	private Integer personAttended;
	private Integer siteKey;
	private Integer scheduleKey;
	private String patrolStatus;
	private Integer minPtDur;
	private Integer minPtDurUnit;
	private Integer maxPtDur;
	private Integer maxPtDurUnit;
	private Integer mobileGroupNum;
	
	
	public Integer getResultKey() {
		return resultKey;
	}
	public void setResultKey(Integer resultKey) {
		this.resultKey = resultKey;
	}
	public Integer getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}
	public Integer getCorrectLocationKey() {
		return correctLocationKey;
	}
	public void setCorrectLocationKey(Integer correctLocationKey) {
		this.correctLocationKey = correctLocationKey;
	}
	public Integer getLocationKey() {
		return locationKey;
	}
	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public Long getTimeAttended() {
		return timeAttended;
	}
	public void setTimeAttended(Long timeAttended) {
		this.timeAttended = timeAttended;
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
	public Integer getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(Integer routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public Integer getPersonAttended() {
		return personAttended;
	}
	public void setPersonAttended(Integer personAttended) {
		this.personAttended = personAttended;
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
	public String getPatrolStatus() {
		return patrolStatus;
	}
	public void setPatrolStatus(String patrolStatus) {
		this.patrolStatus = patrolStatus;
	}
	public Integer getMinPtDur() {
		return minPtDur;
	}
	public void setMinPtDur(Integer minPtDur) {
		this.minPtDur = minPtDur;
	}
	public Integer getMinPtDurUnit() {
		return minPtDurUnit;
	}
	public void setMinPtDurUnit(Integer minPtDurUnit) {
		this.minPtDurUnit = minPtDurUnit;
	}
	public Integer getMaxPtDur() {
		return maxPtDur;
	}
	public void setMaxPtDur(Integer maxPtDur) {
		this.maxPtDur = maxPtDur;
	}
	public Integer getMaxPtDurUnit() {
		return maxPtDurUnit;
	}
	public void setMaxPtDurUnit(Integer maxPtDurUnit) {
		this.maxPtDurUnit = maxPtDurUnit;
	}
	public Integer getMobileGroupNum() {
		return mobileGroupNum;
	}
	public void setMobileGroupNum(Integer mobileGroupNum) {
		this.mobileGroupNum = mobileGroupNum;
	}
	
	
}

