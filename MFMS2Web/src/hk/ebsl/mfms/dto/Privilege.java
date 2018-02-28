package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name="tbl_privilege")
public class Privilege {

	@Id
	@Column(name="p_Code")
	private String code;
	
	@Column(name="p_Name")
	private String name;
	
	@Column(name="p_PrivilegeCategoryCode")
	private String privilegeCategoryCode;
	
	@Column(name="p_Sequence")
	private Integer sequence;
	
	@Column(name="p_CreateBy")
	private Integer createdBy;
	
	@Column(name="p_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="p_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="p_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="p_Deleted")
	private String deleted;
	
	@Column(name="p_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="p_PrivilegeCategoryCode",insertable=false,updatable=false)
	private PrivilegeCategory privilegeCategory;

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

	public String getPrivilegeCategoryCode() {
		return privilegeCategoryCode;
	}

	public void setPrivilegeCategoryCode(String privilegeCategoryCode) {
		this.privilegeCategoryCode = privilegeCategoryCode;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public PrivilegeCategory getPrivilegeCategory() {
		return privilegeCategory;
	}

	public void setPrivilegeCategory(PrivilegeCategory privilegeCategory) {
		this.privilegeCategory = privilegeCategory;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	
}
