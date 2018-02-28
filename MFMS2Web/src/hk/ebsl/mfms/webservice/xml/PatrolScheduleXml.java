package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PatrolSchedule")
public class PatrolScheduleXml extends BaseXml{

	private int scheduleKey;
	private int routeDefKey;
	private long scheduleStartDate;
	private long scheduleEndDate;
	private long scheduleTime;
	private int status;
	private String frequency;
	private int parentId;
	private String remarks;
	private int createBy;
	private long createDateTime;
	private int lastModifyBy;
	private long lastModifyDateTime;
	private String deleted;
	private String skipped;
	private long skippedStartDate;
	private long skippedEndDate;
	private long lastAttendTime;
	
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
	public long getScheduleStartDate() {
		return scheduleStartDate;
	}
	public void setScheduleStartDate(long scheduleStartDate) {
		this.scheduleStartDate = scheduleStartDate;
	}
	public long getScheduleEndDate() {
		return scheduleEndDate;
	}
	public void setScheduleEndDate(long scheduleEndDate) {
		this.scheduleEndDate = scheduleEndDate;
	}
	public long getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(long scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public long getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(long createDateTime) {
		this.createDateTime = createDateTime;
	}
	public int getLastModifyBy() {
		return lastModifyBy;
	}
	public void setLastModifyBy(int lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
	public long getLastModifyDateTime() {
		return lastModifyDateTime;
	}
	public void setLastModifyDateTime(long lastModifyDateTime) {
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
	public long getSkippedStartDate() {
		return skippedStartDate;
	}
	public void setSkippedStartDate(long skippedStartDate) {
		this.skippedStartDate = skippedStartDate;
	}
	public long getSkippedEndDate() {
		return skippedEndDate;
	}
	public void setSkippedEndDate(long skippedEndDate) {
		this.skippedEndDate = skippedEndDate;
	}
	public long getLastAttendTime() {
		return lastAttendTime;
	}
	public void setLastAttendTime(long lastAttendTime) {
		this.lastAttendTime = lastAttendTime;
	}
	
	
	
}
