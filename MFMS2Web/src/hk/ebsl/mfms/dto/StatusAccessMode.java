package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_statusaccessmode")
public class StatusAccessMode {

	@Id
	@Column(name = "sam_ModeKey")
	@GeneratedValue
	private Integer modeKey;

	@Column(name = "sam_Code")
	private String code;

	@Column(name = "sam_Name")
	private String name;

	@Column(name = "sam_Desc")
	private String desc;

	@Column(name = "sam_CreateBy")
	private Integer createBy;

	@Column(name = "sam_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "sam_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "sam_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "sam_Deleted")
	private String deleted;

	public Integer getModeKey() {
		return modeKey;
	}

	public void setModeKey(Integer modeKey) {
		this.modeKey = modeKey;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
}
