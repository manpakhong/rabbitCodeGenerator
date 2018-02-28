package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FailureClass")
public class FailureClassXml extends BaseXml{

	private Integer key;

	private Integer siteKey;

	private String code;

	private String name;

	private String desc;

	private Integer parentKey;

	private Integer levelKey;

	private Integer defaultPriority;

	private Integer defaultAccountKey;

	private Integer createdBy;

	private Long createDateTime;

	private Integer lastModifyBy;

	private Long lastModifyDateTime;

	private String deleted;

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
	
	

}
