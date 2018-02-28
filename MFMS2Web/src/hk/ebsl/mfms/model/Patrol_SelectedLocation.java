package hk.ebsl.mfms.model;

import hk.ebsl.mfms.dto.RouteLocation;

public class Patrol_SelectedLocation {

	private Integer key;
	private Integer locationKey;
	private String locationName;
	private Integer minTime;
	private Integer maxTime;
	private RouteLocation routeLocation;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer locationKey) {
		this.locationKey = locationKey;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Integer getMinTime() {
		return minTime;
	}

	public void setMinTime(Integer minTime) {
		this.minTime = minTime;
	}

	public Integer getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Integer maxTime) {
		this.maxTime = maxTime;
	}

	public RouteLocation getRouteLocation() {
		return routeLocation;
	}

	public void setRouteLocation(RouteLocation routeLocation) {
		this.routeLocation = routeLocation;
	}

}
