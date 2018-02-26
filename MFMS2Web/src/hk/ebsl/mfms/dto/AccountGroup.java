package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name="tbl_accountgroup")
public class AccountGroup {

	@Id
	@GeneratedValue
	@Column(name = "ag_Key")
	private Integer key;
	
	@Column(name="ag_SiteKey")
	private Integer siteKey;
	
	@Column(name="ag_Name")
	private String name;
	
	@Column(name="ag_Desc")
	private String desc;
	
	@Column(name="ag_CreateBy")
	private Integer createdBy;
	
	@Column(name="ag_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="ag_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="ag_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="ag_Deleted")
	private String deleted;
	
	@Column(name="ag_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany (mappedBy="accountGroup")
	@Where(clause="aga_Deleted='N'")
	private List<AccountGroupAccount> accountGroupAccount;
	@JsonIgnore
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany (mappedBy="accountGroup")
	@Where(clause="agr_Deleted='N'")
	private List<AccountGroupResponsible> accountGroupResponsible;
	

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

	public List<AccountGroupAccount> getAccountGroupAccount() {
		return accountGroupAccount;
	}

	public void setAccountGroupAccount(List<AccountGroupAccount> accountGroupAccount) {
		this.accountGroupAccount = accountGroupAccount;
	}

	public List<AccountGroupResponsible> getAccountGroupResponsible() {
		return accountGroupResponsible;
	}

	public void setAccountGroupResponsible(
			List<AccountGroupResponsible> accountGroupResponsible) {
		this.accountGroupResponsible = accountGroupResponsible;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}



	
}
