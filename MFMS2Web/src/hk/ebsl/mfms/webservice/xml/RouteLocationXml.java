package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RouteLocation")
public class RouteLocationXml extends BaseXml {

	private Integer routeLocationKey;
	private Integer routeDefKey;
	private Integer locationKey;
	private Integer seqNum;
	private Integer minPtDur;
	private Integer minPtDurUnit;
	private Integer maxPtDur;
	private Integer maxPtDurUnit;
	private String remark;
	private Integer createBy;
	private Long createDateTime;
	private Integer lastModifyBy;
	private Long lastModifyDateTime;
	private String deleted;
	
	public Integer getRouteLocationKey() {
		return routeLocationKey;
	}
	public void setRouteLocationKey(Integer routeLocationKey) {
		this.routeLocationKey = routeLocationKey;
	}
	public Integer getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(Integer routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public Integer getLocationKey() {
		return locationKey;
	}
	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}
	public Integer getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}
	public Integer getMinPtDur() {
		return minPtDur;
	}
	public void setMinPtDur(Integer minPtDur) {
		this.minPtDur = minPtDur;
	}
	public Integer getMinPtDurUnit() {
		return minPtDurUnit;
	}
	public void setMinPtDurUnit(Integer minPtDurUnit) {
		this.minPtDurUnit = minPtDurUnit;
	}
	public Integer getMaxPtDur() {
		return maxPtDur;
	}
	public void setMaxPtDur(Integer maxPtDur) {
		this.maxPtDur = maxPtDur;
	}
	public Integer getMaxPtDurUnit() {
		return maxPtDurUnit;
	}
	public void setMaxPtDurUnit(Integer maxPtDurUnit) {
		this.maxPtDurUnit = maxPtDurUnit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public Long getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Long createDateTime) {
		this.createDateTime = createDateTime;
	}
	public Integer getLastModifyBy() {
		return lastModifyBy;
	}
	public void setLastModifyBy(Integer lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}
	public Long getLastModifyDateTime() {
		return lastModifyDateTime;
	}
	public void setLastModifyDateTime(Long lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}
	public String getDeleted() {
		return deleted;
	}
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	
	
}
