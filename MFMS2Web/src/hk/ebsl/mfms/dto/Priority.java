package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_priority")
public class Priority {

	@Id
	@Column(name = "trt_Priority")
	@GeneratedValue
	private Integer priority;

	@Column(name = "trt_ResponseTime")
	private Float responseTime;

	@Column(name = "trt_CreateBy")
	private Integer createBy;

	@Column(name = "trt_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "trt_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "trt_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "trt_Deleted")
	private String deleted;

	@Column(name="trt_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Float getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Float responseTime) {
		this.responseTime = responseTime;
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

	public Integer getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(Integer lastModifyBy) {
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

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}



	
}
