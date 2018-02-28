package hk.ebsl.mfms.dto;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="tbl_site")
public class Site {

	@Id
	@Column(name="s_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name="s_Name")
	private String name;
	
	@Column(name="s_Address")
	private String address;
	
	@Column(name="s_ContactCountryCode")
	private String contactCountryCode;
	
	@Column(name="s_ContactAreaCode")
	private String contactAreaCode;
	
	@Column(name="s_ContactNumber")
	private String contactNumber;
	
	@Column(name="s_CreateBy")
	private Integer createBy;
	
	@Column(name="s_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="s_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="s_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="s_Deleted")
	private String deleted;

	@Column(name="s_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
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


}
