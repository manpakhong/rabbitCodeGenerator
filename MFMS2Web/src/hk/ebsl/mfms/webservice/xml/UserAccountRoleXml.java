package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserAccountRole")
public class UserAccountRoleXml extends BaseXml {
	
	private Integer key;
	
	private Integer accountKey;
	
	private Integer roleKey;
	
	private Integer createBy;
	
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

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

	public Integer getRoleKey() {
		return roleKey;
	}

	public void setRoleKey(Integer roleKey) {
		this.roleKey = roleKey;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
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
