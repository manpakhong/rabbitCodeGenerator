package hk.ebsl.mfms.utility;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

public class FileBucket {
	
	private Integer key;
	
	private Integer siteKey;
	
	private Integer defectKey;

	private MultipartFile file;
	
	private String fileType;
	
	private String name;

	private String desc;

	private String path;
	
	private String deleted;
	
	//In case the file name contain Chinese character
	private String unicodePath;
	
	private Boolean fileExist;
	
	private String fileSize;

	private Integer createdBy;

	private Timestamp createDateTime;

	private Integer lastModifyBy;

	private Timestamp lastModifyDateTime;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getDefectKey() {
		return defectKey;
	}

	public void setDefectKey(Integer defectKey) {
		this.defectKey = defectKey;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Boolean getFileExist() {
		return fileExist;
	}

	public void setFileExist(Boolean fileExist) {
		this.fileExist = fileExist;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getUnicodePath() {
		return unicodePath;
	}

	public void setUnicodePath(String unicodePath) {
		this.unicodePath = unicodePath;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}



}