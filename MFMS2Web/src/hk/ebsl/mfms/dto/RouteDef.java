package hk.ebsl.mfms.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tbl_routedef")
public class RouteDef implements Serializable {

	@Id
	@GeneratedValue
	@JsonProperty("routeDefKey")
	@Column(name = "rd_Key")
	private int routeDefKey;
	
	@Column(name = "rd_SiteKey")
	private int siteKey;
	
	@JsonProperty("code")
	@Column(name = "rd_Code")
	private String code;
	
	@JsonProperty("name")
	@Column(name = "rd_Name")
	private String name;

	@Column(name = "rd_DefMinPtDur")
	private int defMinPtDur;
	
	@Column(name = "rd_DefMinPtDurUnit")
	private int defMinPtDurUnit;
	
	@Column(name = "rd_DefMaxPtDur")
	private int defMaxPtDur;
	
	@Column(name = "rd_DefMaxPtDurUnit")
	private int defMaxPtDurUnit;
	
	@Column(name = "rd_CreateBy")
	private int createBy;
	
	@Column(name = "rd_CreateDateTime")
	private Timestamp createDateTime;
	
	@Column(name = "rd_LastModifyBy")
	private int lastModifyBy;
	
	@Column(name = "rd_lastModifyDateTime")
	private Timestamp lastModifyDateTime;
	
	@Column(name = "rd_Deleted")
	private String deleted;

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "routeDefKey")
	@OrderBy("seqNum")
	private List<RouteLocation> routeLocation;

	@Column(name="rd_LastModifyTimeForSync", insertable = false, updatable = false)
	private Timestamp lastModifyTimeForSync;
	
	
	public RouteDef() {
		
	}
	public RouteDef(String code, String name) {
		
		this.code = code;
		this.name = name;
	}

	public RouteDef(Integer routeDefKey, String code, String name) {
		this.routeDefKey = routeDefKey;
		this.code = code;
		this.name = name;
	}
	
	public int getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

	public int getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(int siteKey) {
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

	public int getDefMinPtDur() {
		return defMinPtDur;
	}

	public void setDefMinPtDur(int defMinPtDur) {
		this.defMinPtDur = defMinPtDur;
	}

	public int getDefMinPtDurUnit() {
		return defMinPtDurUnit;
	}

	public void setDefMinPtDurUnit(int defMinPtDurUnit) {
		this.defMinPtDurUnit = defMinPtDurUnit;
	}

	public int getDefMaxPtDur() {
		return defMaxPtDur;
	}

	public void setDefMaxPtDur(int defMaxPtDur) {
		this.defMaxPtDur = defMaxPtDur;
	}

	public int getDefMaxPtDurUnit() {
		return defMaxPtDurUnit;
	}

	public void setDefMaxPtDurUnit(int defMaxPtDurUnit) {
		this.defMaxPtDurUnit = defMaxPtDurUnit;
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

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public List<RouteLocation> getRouteLocation() {
		return routeLocation;
	}

	public void setRouteLocation(List<RouteLocation> routeLocation) {
		this.routeLocation = routeLocation;
	}
	public Timestamp getLastModifyTimeForSync() {
		return lastModifyTimeForSync;
	}
	public void setLastModifyTimeForSync(Timestamp lastModifyTimeForSync) {
		this.lastModifyTimeForSync = lastModifyTimeForSync;
	}


}
