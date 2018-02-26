package hk.ebsl.mfms.json;

import java.util.List;

public class RouteNameSearchJSON {

	private List<Integer> locationKey;
	private String routeName;
	private String routeCode;
	
	public List<Integer> getLocationKey() {
		return locationKey;
	}
	public void setLocationKey(List<Integer> locationKey) {
		this.locationKey = locationKey;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getRouteCode() {
		return routeCode;
	}
	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}
	
	
	
}
