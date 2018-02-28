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

@Entity
@Table (name="tbl_causecode")
public class CauseCode {

	@Id
	@Column(name="cc_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name="cc_SiteKey")
	private Integer siteKey;
	
	@Column(name="cc_Code")
	private String code;
	
	@Column(name="cc_Name")
	private String name;
	
	@Column(name="cc_Desc")
	private String desc;
	
	@Column(name="cc_ParentKey")
	private Integer parentKey;
	
	@Column(name="cc_LevelKey")
	private Integer levelKey;
	
	@Column(name="cc_CreateBy")
	private Integer createdBy;
	
	@Column(name="cc_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="cc_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="cc_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="cc_Deleted")
	private String deleted;
	
	@Column(name="cc_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="pc_ParentKey",insertable=false,updatable=false)
//	private FailureClass failureClass;
//	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="pc_SiteKey",insertable=false,updatable=false)
//	private Site site;

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

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	
	

//	public FailureClass getFailureClass() {
//		return failureClass;
//	}
//
//	public void setFailureClass(FailureClass failureClass) {
//		this.failureClass = failureClass;
//	}
//
//	public Site getSite() {
//		return site;
//	}
//
//	public void setSite(Site site) {
//		this.site = site;
//	}
	
}
