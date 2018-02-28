package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "UserAccount")
public class UserAccountXml extends BaseXml {

	private Integer key;

	private String loginId;

	private String password;

	private String tagId;

	private String name;

	private String email;

	private String contactCountryCode;

	private String contactAreaCode;

	private String contactNumber;

	private Integer logonAttemptCount;

	private String status;

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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getLogonAttemptCount() {
		return logonAttemptCount;
	}

	public void setLogonAttemptCount(Integer logonAttemptCount) {
		this.logonAttemptCount = logonAttemptCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getContactCountryCode() {
		return contactCountryCode;
	}

	public void setContactCountryCode(String contactCountryCode) {
		this.contactCountryCode = contactCountryCode;
	}

	public String getContactAreaCode() {
		return contactAreaCode;
	}

	public void setContactAreaCode(String contactAreaCode) {
		this.contactAreaCode = contactAreaCode;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
}
