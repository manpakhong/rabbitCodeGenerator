package hk.ebsl.mfms.web.form;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.model.LocationTreeNode;

import java.util.ArrayList;
import java.util.List;

public class SearchLocationForm {

	private String code;

	private String name;

	private String description;

	private Integer siteKey;

	private Integer parentKey;

	private Boolean deleteSuccess = false;

	private Boolean hasDefect = false;

	private Boolean hasChild = false;

	private String deletedName;
	
	private LocationTreeNode availableLocationTree;

	private List<Location> locationList;

	private List<Location> resultList = new ArrayList<Location>();
	
	private Boolean canModify = false;
	
	private Boolean canRemove= false;

	private Boolean canGen = false;
	
	private Integer fullListSize = 0;
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}

	public Integer getParentKey() {
		return parentKey;
	}

	public void setParentKey(Integer parentKey) {
		this.parentKey = parentKey;
	}

	public List<Location> getResultList() {
		return resultList;
	}

	public void setResultList(List<Location> resultList) {
		this.resultList = resultList;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public Boolean getDeleteSuccess() {
		return deleteSuccess;
	}

	public void setDeleteSuccess(Boolean deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}

	public String getDeletedName() {
		return deletedName;
	}

	public void setDeletedName(String deletedName) {
		this.deletedName = deletedName;
	}

	public Boolean getHasDefect() {
		return hasDefect;
	}

	public void setHasDefect(Boolean hasDefect) {
		this.hasDefect = hasDefect;
	}

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	public LocationTreeNode getAvailableLocationTree() {
		return availableLocationTree;
	}

	public void setAvailableLocationTree(LocationTreeNode availableLocationTree) {
		this.availableLocationTree = availableLocationTree;
	}

	public Boolean getCanModify() {
		return canModify;
	}

	public void setCanModify(Boolean canModify) {
		this.canModify = canModify;
	}

	public Boolean getCanRemove() {
		return canRemove;
	}

	public void setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
	}

	public Boolean getCanGen() {
		return canGen;
	}

	public void setCanGen(Boolean canGen) {
		this.canGen = canGen;
	}

	public Integer getFullListSize() {
		return fullListSize;
	}

	public void setFullListSize(Integer fullListSize) {
		this.fullListSize = fullListSize;
	}

}
