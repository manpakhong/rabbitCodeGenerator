package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tbl_defectvideo")
public class DefectVideo {
	@Id
	@Column(name = "dv_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "dv_SiteKey")
	private Integer siteKey;
	
	@Column(name = "dv_DefectKey")
	private Integer defectKey;

	@Column(name = "dv_Description")
	private String desc;

	@Column(name = "dv_VideoPath")
	private String videoPath;

	@Column(name = "dv_CreateBy")
	private Integer createdBy;

	@Column(name = "dv_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "dv_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "dv_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "dv_Deleted")
	private String deleted;
	
	@Transient
	private Integer mobileKey;
	@Transient
	private Integer mobileDefectKey;

	@Column(name="dv_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getDefectKey() {
		return defectKey;
	}

	public void setDefectKey(Integer defectKey) {
		this.defectKey = defectKey;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public Integer getMobileKey() {
		return mobileKey;
	}

	public void setMobileKey(Integer mobileKey) {
		this.mobileKey = mobileKey;
	}

	public Integer getMobileDefectKey() {
		return mobileDefectKey;
	}

	public void setMobileDefectKey(Integer mobileDefectKey) {
		this.mobileDefectKey = mobileDefectKey;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


}
