package hk.ebsl.mfms.webservice.xml;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "RouteDef")
public class RouteDefXml extends BaseXml {

	private Integer routeDefKey;
	private String code;
	private String name;
	private Integer defMinPtDur;
	private Integer defMinPtDurUnit;
	private Integer defMaxPtDur;
	private Integer defMaxPtDurUnit;
	private Integer createBy;
	private Long createDateTime;
	private Integer lastModifyBy;
	private Long lastModifyDateTime;
	private String deleted;
	
	public Integer getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(Integer routeDefKey) {
		this.routeDefKey = routeDefKey;
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
	public Integer getDefMinPtDur() {
		return defMinPtDur;
	}
	public void setDefMinPtDur(Integer defMinPtDur) {
		this.defMinPtDur = defMinPtDur;
	}
	public Integer getDefMinPtDurUnit() {
		return defMinPtDurUnit;
	}
	public void setDefMinPtDurUnit(Integer defMinPtDurUnit) {
		this.defMinPtDurUnit = defMinPtDurUnit;
	}
	public Integer getDefMaxPtDur() {
		return defMaxPtDur;
	}
	public void setDefMaxPtDur(Integer defMaxPtDur) {
		this.defMaxPtDur = defMaxPtDur;
	}
	public Integer getDefMaxPtDurUnit() {
		return defMaxPtDurUnit;
	}
	public void setDefMaxPtDurUnit(Integer defMaxPtDurUnit) {
		this.defMaxPtDurUnit = defMaxPtDurUnit;
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
