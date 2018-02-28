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

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table (name="tbl_routelocation")
public class RouteLocation implements Serializable{

	@Id
	@GeneratedValue
	@Column(name = "rl_Key")
	int routeLocationKey;
	
	@Column(name = "rl_RouteDefKey")
	int routeDefKey;
	
	@Column(name = "rl_LocationKey")
	int locationKey;
	
	@Column(name = "rl_SeqNum")
	int seqNum;
	
	@Column(name = "rl_MinPtDur")
	int minPtDur;
	
	@Column(name = "rl_MinPtDurUnit")
	int minPtDurUnit;
	
	@Column(name = "rl_MaxPtDur")
	int maxPtDur;
	
	@Column(name = "rl_MaxPtDurUnit")
	int maxPtDurUnit;
	
	@Column(name = "rl_Remarks")
	String remark;
	
	@Column(name = "rl_CreateBy")
	int createBy;
	
	@Column(name = "rl_CreateDateTime")
	Timestamp createDateTime;
	
	@Column(name = "rl_LastModifyBy")
	Integer lastModifyBy;
	
	@Column(name = "rl_LastModifyDateTime")
	Timestamp lastModifyDateTime;
	
	@Column(name = "rl_Deleted")
	String deleted;
	
	@Column(name="rl_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="rl_RouteDefKey",insertable=false,updatable=false)
	private RouteDef routeDef;
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="rl_LocationKey",insertable=false,updatable=false)
	private Location location;
	
	public int getRouteLocationKey() {
		return routeLocationKey;
	}

	public void setRouteLocationKey(int routeLocationKey) {
		this.routeLocationKey = routeLocationKey;
	}

	public int getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

	public int getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(int locationKey) {
		this.locationKey = locationKey;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	public int getMinPtDur() {
		return minPtDur;
	}

	public void setMinPtDur(int minPtDur) {
		this.minPtDur = minPtDur;
	}

	public int getMinPtDurUnit() {
		return minPtDurUnit;
	}

	public void setMinPtDurUnit(int minPtDurUnit) {
		this.minPtDurUnit = minPtDurUnit;
	}

	public int getMaxPtDur() {
		return maxPtDur;
	}

	public void setMaxPtDur(int maxPtDur) {
		this.maxPtDur = maxPtDur;
	}

	public int getMaxPtDurUnit() {
		return maxPtDurUnit;
	}

	public void setMaxPtDurUnit(int maxPtDurUnit) {
		this.maxPtDurUnit = maxPtDurUnit;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public RouteDef getRouteDef() {
		return routeDef;
	}

	public void setRouteDef(RouteDef routeDef) {
		this.routeDef = routeDef;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}

	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


	
}
