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
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * role and account mapping
 */
@Entity
@Table (name="tbl_roleaccount")
public class UserAccountRole {

	@Id
	@GeneratedValue
	@Column(name = "ra_Key")
	private Integer key;
	
	@Column(name = "ra_AccountKey")
	private Integer accountKey;
	
	@Column(name = "ra_RoleKey")
	private Integer roleKey;
	
	@Column(name="ra_CreateBy")
	private Integer createBy;
	
	@Column(name="ra_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="ra_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="ra_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="ra_Deleted")
	private String deleted;
	
	@Column(name="ra_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ra_RoleKey",insertable=false,updatable=false)
	private Role role;
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ra_AccountKey",insertable=false,updatable=false)
	private UserAccount userAccount;

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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


}
