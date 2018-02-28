package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_systemparameter")
public class SystemParam {

	@EmbeddedId
	private SystemParamId id;
	
	@Column(name = "sp_Value")
	private String value;
	
	@Column(name = "sp_Desc")
	private String desc;
	
	@Column(name = "sp_CreateBy")
	private Integer createBy;

	@Column(name = "sp_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "sp_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "sp_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	public SystemParamId getId() {
		return id;
	}

	public void setId(SystemParamId id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
	
	
}
