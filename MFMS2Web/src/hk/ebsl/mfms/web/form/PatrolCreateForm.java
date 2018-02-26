package hk.ebsl.mfms.web.form;

import java.util.List;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.json.LocationJSON;

public class PatrolCreateForm {


	private int siteKey;
	private int routeDefKey;
	private String routeCode;
	private String routeName;
	private int routeMinTime;
	private String routeMinTimeUnit;
	private int routeMaxTime;
	private String routeMaxTimeUnit;
	private List<Location> rootList;
	private List<LocationJSON> selectedLocation;
	private String selectedLocationJson;
	
	
	
	public int getSiteKey() {
		return siteKey;
	}
	public void setSiteKey(int siteKey) {
		this.siteKey = siteKey;
	}
	public int getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}	
	public String getRouteCode() {
		return routeCode;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public int getRouteMinTime() {
		return routeMinTime;
	}
	public void setRouteMinTime(int routeMinTime) {
		this.routeMinTime = routeMinTime;
	}
	public int getRouteMaxTime() {
		return routeMaxTime;
	}
	public void setRouteMaxTime(int routeMaxTime) {
		this.routeMaxTime = routeMaxTime;
	}
	public List<LocationJSON> getSelectedLocation() {
		return selectedLocation;
	}
	public void setSelectedLocation(List<LocationJSON> selectedLocation) {
		this.selectedLocation = selectedLocation;
	}
	public List<Location> getRootList() {
		return rootList;
	}
	public void setRootList(List<Location> rooteList) {
		this.rootList = rooteList;
	}
	public String getRouteMinTimeUnit() {
		return routeMinTimeUnit;
	}
	public void setRouteMinTimeUnit(String routeMinTimeUnit) {
		this.routeMinTimeUnit = routeMinTimeUnit;
	}
	public String getRouteMaxTimeUnit() {
		return routeMaxTimeUnit;
	}
	public void setRouteMaxTimeUnit(String routeMaxTimeUnit) {
		this.routeMaxTimeUnit = routeMaxTimeUnit;
	}
	public String getSelectedLocationJson() {
		return selectedLocationJson;
	}
	public void setSelectedLocationJson(String selectedLocationJson) {
		this.selectedLocationJson = selectedLocationJson;
	}
	
}
