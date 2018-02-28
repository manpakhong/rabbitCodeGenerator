package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tbl_statusflow")
public class StatusFlow {

	@EmbeddedId
	
	private StatusFlowId statusFlowId;

	@Column(name = "sf_CreateBy")
	private Integer createBy;

	@Column(name = "sf_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "sf_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "sf_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "sf_Deleted")
	private String deleted;
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sf_StatusID",insertable=false,updatable=false)
	private Status status;
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sf_NextStatusID",insertable=false,updatable=false)
	private Status nextStatus;

	@Column(name="sf_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
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
	@JsonProperty("statusFlowId")
	public StatusFlowId getId() {
		return statusFlowId;
	}
	@JsonProperty("statusFlowId")
	public void setId(StatusFlowId id) {
		this.statusFlowId = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(Status nextStatus) {
		this.nextStatus = nextStatus;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}



}
