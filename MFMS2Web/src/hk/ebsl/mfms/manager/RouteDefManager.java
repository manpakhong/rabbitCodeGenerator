package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.model.Patrol_Route;

public interface RouteDefManager {
	public int saveRoute(RouteDef routeDef, List<RouteLocation> routeLocation) throws MFMSException;

	public Patrol_Route searhRoute(int routeDefKey) throws MFMSException;
	
	public List<Patrol_Route> searchRoute(int siteKey, int routeDefKey);

	public List<Patrol_Route> searchRoute(int siteKey, String name, String code, List<Integer> locationKey);

	public void deleteRoute(int routeDefKey);

	public void deleteRouteLocation(List<RouteLocation> routeLocationList);

	public List<RouteDef> getAllRoute(int siteKey);
	
	public List<RouteDef> getRouteDef(int[] keys) throws MFMSException;

	public List<RouteDef> getRouteDefWithLastModifedTime(Timestamp lastModifiedTime);

	public RouteLocation searchRouteLocation(Integer routeLocationKey);

	public List<RouteLocation> getRouteLocationWithLastModifiedTime(Timestamp lastModifiedTime);

	public void updateRouteLocation(List<RouteLocation> routeLocationList);

	public List<RouteDef> searchRouteDefByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);
	
	public List<RouteLocation> searchRouteLocationByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchRouteDefResultCount(Timestamp lastModifiedDate, Integer siteKey);
	
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);
}
