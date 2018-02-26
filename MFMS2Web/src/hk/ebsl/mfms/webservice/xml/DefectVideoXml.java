package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "DefectVideo")
public class DefectVideoXml extends BaseXml {
	
	private Integer defectVideoKey;

	private Integer defectKey;

	private String desc;

	private String videoPath;

	private Integer createdBy;

	private Long createDateTime;

	private Integer lastModifyBy;

	private Long lastModifyDateTime;

	private String deleted;
	
	private byte[] data;

	public Integer getDefectVideoKey() {
		return defectVideoKey;
	}

	public void setDefectVideoKey(Integer defectVideoKey) {
		this.defectVideoKey = defectVideoKey;
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

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
