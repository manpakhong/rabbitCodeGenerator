package hk.ebsl.mfms.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tbl_patrolphoto")
public class PatrolPhoto implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "pp_Key")
	private int photoKey;
	
	@Column(name = "pp_SiteKey")
	private int siteKey;
	
	@Column(name = "pp_RouteDefKey")
	private int routeDefkey;
	
	@Column(name = "pp_PatrolResultKey")
	private int patrolResultKey;
	
	@Column(name = "pp_PhotoPath")
	private String photoPath;
	
	@Column(name = "pp_LocationKey")
	private int locationKey;
	
	
	@Column(name="pp_Remark")
	private String remark;
	
	@Column(name = "pp_CreateBy")
	private int createBy;
	
	@Column(name = "pp_CreateTime")
	private Timestamp createDateTime;
	
	@Column(name = "pp_LastModifyBy")
	private int lastModifyBy;
	
	@Column(name = "pp_LastModifyTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name = "pp_Deleted")
	private String isDeleted;
	
	@Column(name="pp_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "pp_CreateBy", insertable = false, updatable = false, nullable = true)
	private UserAccount person;
	
	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "pp_LocationKey", insertable = false, updatable = false, nullable = true)
	private Location location;
	
	@JsonIgnore
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "pp_RouteDefKey", insertable = false, updatable = false, nullable = true)
	private RouteDef routeDef;
	

	@Transient
	private Integer mobileKey;
	@Transient
	private Integer patrolResultMobileKey;
	
	public int getPhotoKey() {
		return photoKey;
	}

	public void setPhotoKey(int photoKey) {
		this.photoKey = photoKey;
	}

	public int getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(int siteKey) {
		this.siteKey = siteKey;
	}

	public int getPatrolResultKey() {
		return patrolResultKey;
	}

	public void setPatrolResultKey(int patrolResultKey) {
		this.patrolResultKey = patrolResultKey;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public int getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(int locationKey) {
		this.locationKey = locationKey;
	}

	public int getCreateBy() {
		return createBy;
	}

	public void setCreateBy(int createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Timestamp createDateTime) {
		this.createDateTime = createDateTime;
	}

	public int getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(int lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public Timestamp getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(Timestamp lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public UserAccount getPerson() {
		return person;
	}

	public void setPerson(UserAccount person) {
		this.person = person;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getRouteDefkey() {
		return routeDefkey;
	}

	public void setRouteDefkey(int routeDefkey) {
		this.routeDefkey = routeDefkey;
	}

	public RouteDef getRouteDef() {
		return routeDef;
	}

	public void setRouteDef(RouteDef routeDef) {
		this.routeDef = routeDef;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getMobileKey() {
		return mobileKey;
	}

	public void setMobileKey(Integer mobileKey) {
		this.mobileKey = mobileKey;
	}

	public Integer getPatrolResultMobileKey() {
		return patrolResultMobileKey;
	}

	public void setPatrolResultMobileKey(Integer patrolResultMobileKey) {
		this.patrolResultMobileKey = patrolResultMobileKey;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	
	
}
