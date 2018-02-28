package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name="tbl_problemcode")
public class ProblemCode {

	@Id
	@Column(name="pc_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name="pc_SiteKey")
	private Integer siteKey;
	
	@Column(name="pc_Code")
	private String code;
	
	@Column(name="pc_Name")
	private String name;
	
	@Column(name="pc_Desc")
	private String desc;
	
	@Column(name="pc_ParentKey")
	private Integer parentKey;
	
	@Column(name="pc_LevelKey")
	private Integer levelKey;
	
	@Column(name="pc_DefaultPriority")
	private Integer defaultPriority;
	
	@Column(name="pc_DefaultAccountKey")
	private Integer defaultAccountKey;
	
	@Column(name="pc_CreateBy")
	private Integer createdBy;
	
	@Column(name="pc_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="pc_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="pc_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="pc_Deleted")
	private String deleted;
	
	@Column(name="pc_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER, optional = false)
	@JoinColumn(name="pc_ParentKey",insertable=false,updatable=false)
	private FailureClass failureClass;
	
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
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

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public Integer getLevelKey() {
		return levelKey;
	}

	public void setLevelKey(Integer levelKey) {
		this.levelKey = levelKey;
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

	public Integer getDefaultPriority() {
		return defaultPriority;
	}

	public void setDefaultPriority(Integer defaultPriority) {
		this.defaultPriority = defaultPriority;
	}

	public Integer getDefaultAccountKey() {
		return defaultAccountKey;
	}

	public void setDefaultAccountKey(Integer defaultAccountKey) {
		this.defaultAccountKey = defaultAccountKey;
	}
	@JsonIgnore
	public FailureClass getFailureClass() {
		return failureClass;
	}
	@JsonIgnore
	public void setFailureClass(FailureClass failureClass) {
		this.failureClass = failureClass;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	
}
