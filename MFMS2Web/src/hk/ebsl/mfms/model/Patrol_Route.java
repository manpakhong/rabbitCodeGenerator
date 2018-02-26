package hk.ebsl.mfms.model;

import java.util.List;

import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;

public class Patrol_Route {

	RouteDef routeDef;
	List<RouteLocation> routeLocation;
	
	
	public RouteDef getRouteDef() {
		return routeDef;
	}
	public void setRouteDef(RouteDef routeDef) {
		this.routeDef = routeDef;
	}
	public List<RouteLocation> getRouteLocation() {
		return routeLocation;
	}
	public void setRouteLocation(List<RouteLocation> routeLocation) {
		this.routeLocation = routeLocation;
	}
	
	
	
}
