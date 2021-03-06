package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Privilege")
public class PrivilegeXml extends BaseXml {

	private String code;
	
	private String name;
	
	private String privilegeCategoryCode;
	
	private Integer sequence;
	
	private Integer createdBy;
	
	private Long createDateTime;
	
	private Integer lastModifyBy;
	
	private Long lastModifyDateTime;
	
	private String deleted;

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
