package hk.ebsl.mfms.web.form;

import java.util.List;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.model.LocationTreeNode;

public class LocationForm {

	private Integer key;
	
	private Integer siteKey;
	
	private String code;
	
	private String name;
	
	private String desc;
	
	private Integer parentKey;
	
	private Integer levelKey;
	
	private String tagId;
	
	private String referrer;
	
	private Boolean readOnly;
	
	private Boolean delete;
	
	private List<Location> locationList;
	
	private LocationTreeNode availableLocationTree;

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
		this.code = code.trim();
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

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setDelete(Boolean delete) {
		this.delete = delete;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public Integer getLevelKey() {
		return levelKey;
	}

	public void setLevelKey(Integer levelKey) {
		this.levelKey = levelKey;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	
	
}
