package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "tbl_account")
public class UserAccount {

	public static String STATUS_ACTIVE = "A"; // active
	public static String STATUS_SUSPENDED = "S"; // suspended

	@Id
	@GeneratedValue
	@Column(name = "a_Key")
	private Integer key;

	@Column(name = "a_LoginId")
	private String loginId;

	@Column(name = "a_Password")
	private String password;
	
	@Column(name = "a_TagId")
	private String tagId;

	@Column(name = "a_Name")
	private String name;
	
	@Column(name = "a_Email")
	private String email;
	
	@Column(name = "a_ContactCountryCode")
	private String contactCountryCode;
	
	@Column(name = "a_ContactAreaCode")
	private String contactAreaCode;
	
	@Column(name = "a_ContactNumber")
	private String contactNumber;
	
	@JsonIgnore
	@Column(name = "a_LogonAttemptCount")
	private Integer logonAttemptCount;
	
	@Column(name = "a_Status")
	private String status;
	
	@JsonIgnore
	@Column(name = "a_CreateBy")
	private Integer createBy;
	
	@JsonIgnore
	@Column(name = "a_CreateDateTime")
	private Timestamp createDateTime;
	
	@JsonIgnore
	@Column(name = "a_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name = "a_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name = "a_Deleted")
	private String deleted;
	
	@Column(name = "a_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@Transient
	private int checkLoginStatus;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, mappedBy="userAccount")
	private List<UserAccountRole> accountRoles;

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

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
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

	public List<UserAccountRole> getAccountRoles() {
		return accountRoles;
	}

	public void setAccountRoles(List<UserAccountRole> accountRoles) {
		this.accountRoles = accountRoles;
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

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}

	public int getCheckLoginStatus() {
		return checkLoginStatus;
	}

	public void setCheckLoginStatus(int checkLoginStatus) {
		this.checkLoginStatus = checkLoginStatus;
	}


}
