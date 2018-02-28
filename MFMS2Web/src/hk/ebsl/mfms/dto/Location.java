package hk.ebsl.mfms.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hk.ebsl.mfms.model.LocationTreeNode;

@Entity
@Table(name = "tbl_location")
public class Location {

	public final static Logger logger = Logger.getLogger(Location.class);

	@Id
	@Column(name = "l_Key")
	@GeneratedValue
	private Integer key;

	@Column(name = "l_SiteKey")
	private Integer siteKey;

	@Column(name = "l_Code")
	private String code;

	@Column(name = "l_Name")
	private String name;

	@Column(name = "l_Desc")
	private String desc;

	@Column(name = "l_TagID")
	private String tagId;

	@Column(name = "l_ParentKey")
	private Integer parentKey;

	@Column(name = "l_LevelKey")
	private Integer levelKey;

	@Column(name = "l_Chain")
	private String chain;

	@Column(name = "l_CreateBy")
	private Integer createdBy;

	@Column(name = "l_CreateDateTime")
	private Timestamp createDateTime;

	@Column(name = "l_LastModifyBy")
	private Integer lastModifyBy;

	@Column(name = "l_LastModifyDateTime")
	private Timestamp lastModifyDateTime;

	@Column(name = "l_Deleted")
	private String deleted;
	
	@Column(name="l_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentKey")
	@OrderBy("code")
	private List<Location> children;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "l_ParentKey", insertable = false, updatable = false)
	@NotFound(action = NotFoundAction.IGNORE)
	private Location parent;
	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "l_SiteKey", insertable = false, updatable = false)
	private Site site;
	@JsonIgnore
	@Transient
	private Boolean hasChildren = true;
	@JsonIgnore
	@Transient
	private Boolean hasPivilege = true;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public Integer getLevelKey() {
		return levelKey;
	}

	public void setLevelKey(Integer levelKey) {
		this.levelKey = levelKey;
	}

	public String getChain() {
		return chain;
	}

	public void setChain(String chain) {
		this.chain = chain;
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

	public Location getParent() {
		return parent;
	}

	public void setParent(Location parent) {
		this.parent = parent;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Boolean getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	public Boolean getHasPivilege() {
		return hasPivilege;
	}

	public void setHasPivilege(Boolean hasPivilege) {
		this.hasPivilege = hasPivilege;
	}

	public List<Location> getChildren() {
		return children;
	}

	public void setChildren(List<Location> children) {
		this.children = children;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}



}
