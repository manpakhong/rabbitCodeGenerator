package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name="tbl_role")
public class Role {

	@Id
	@Column(name="r_Key")
	@GeneratedValue
	private Integer key;
	
	@Column(name="r_SiteKey")
	private Integer siteKey;
	
	@Column(name="r_Name")
	private String name;
	
	@Column(name = "r_ModeKey")
	private Integer modeKey;
	
	@Column(name="r_Desc")
	private String description;
	
	@Column(name="r_CreateBy")
	private Integer createBy;
	
	@Column(name="r_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="r_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="r_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="r_Deleted")
	private String deleted;
	
	
	@Column(name="r_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="r_SiteKey",insertable=false,updatable=false)
	private Site site;
	
//	@JsonIgnore
	@OneToMany(fetch=FetchType.LAZY, mappedBy="role")
	@Where(clause="rp_Deleted='N'")
	private List<RolePrivilege> rolePrivileges;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
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
	@JsonIgnore
	public List<RolePrivilege> getRolePrivileges() {
		return rolePrivileges;
	}
	@JsonIgnore
	public void setRolePrivileges(List<RolePrivilege> rolePrivileges) {
		this.rolePrivileges = rolePrivileges;
	}
	@JsonIgnore
	public List<String> getGrantedPrivilegeCodeList() {
		
		if (rolePrivileges == null) {
			return null;
		} else {
			List<String> list = new ArrayList<String>();
			for (RolePrivilege rp : rolePrivileges) {
				list.add(rp.getPrivilegeCode());
			}
			return list;
		}
		
	}

	public Integer getModeKey() {
		return modeKey;
	}

	public void setModeKey(Integer modeKey) {
		this.modeKey = modeKey;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	
}
