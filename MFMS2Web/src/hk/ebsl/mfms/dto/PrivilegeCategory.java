package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

@Entity
@Table (name="tbl_privilegecategory")
public class PrivilegeCategory {

	@Id
	@Column(name="pc_Code")
	private String code;
	
	@Column(name="pc_CategoryDesc")
	private String categoryDesc;
	
	@Column(name="pc_ParentCode")
	private String parentCode;
	
	@Column(name="pc_Sequence")
	private Integer sequence;
	
	@Column(name="pc_CreateBy")
	private Integer createdBy;
	
	@Column(name="pc_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name="pc_LastModifyBy")
	private Integer lastModifyBy;
	
	@Column(name="pc_LastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name="pc_Deleted")
	private String deleted;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name="pc_ParentCode",insertable=false,updatable=false)
	@NotFound(action = NotFoundAction.IGNORE)
	private PrivilegeCategory parentCategory;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="privilegeCategory")
	@Where(clause="p_Deleted='N'")
	@OrderBy("sequence ASC")
	private List<Privilege> privilegeList;
	
	@OneToMany(fetch=FetchType.EAGER, mappedBy="parentCategory")
	@Where(clause="pc_Deleted='N'")
	@OrderBy("sequence ASC")
	private List<PrivilegeCategory> subCategoryList;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
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

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public List<Privilege> getPrivilegeList() {
		return privilegeList;
	}

	public void setPrivilegeList(List<Privilege> privilegeList) {
		this.privilegeList = privilegeList;
	}

	public PrivilegeCategory getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(PrivilegeCategory parentCategory) {
		this.parentCategory = parentCategory;
	}

	public List<PrivilegeCategory> getSubCategoryList() {
		return subCategoryList;
	}

	public void setSubCategoryList(List<PrivilegeCategory> subCategoryList) {
		this.subCategoryList = subCategoryList;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	
}
