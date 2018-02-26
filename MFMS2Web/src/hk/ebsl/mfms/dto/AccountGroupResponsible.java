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
import org.hibernate.annotations.Where;

@Entity
@Table (name="tbl_accountgroupresponsible")
public class AccountGroupResponsible {

	@Id
	@GeneratedValue
	@Column(name = "agr_Key")
	private Integer key;
	
	@Column(name="agr_GroupKey")
	private Integer groupKey;
	
	@Column(name="agr_AccountKey")
	private Integer accountKey;
	
	@Column(name="agr_CreateBy")
	private Integer createdBy;
	
	@Column(name="agr_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="agr_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="agr_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="agr_Deleted")
	private String deleted;
	
	@Column(name="agr_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;

	@JsonIgnore
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "agr_GroupKey", insertable = false, updatable = false)
	private AccountGroup accountGroup;
	@JsonIgnore
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn(name = "agr_AccountKey", insertable = false, updatable = false)
	private UserAccount account;
	

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(Integer groupKey) {
		this.groupKey = groupKey;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
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

	public AccountGroup getAccountGroup() {
		return accountGroup;
	}

	public void setAccountGroup(AccountGroup accountGroup) {
		this.accountGroup = accountGroup;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}
	

}
