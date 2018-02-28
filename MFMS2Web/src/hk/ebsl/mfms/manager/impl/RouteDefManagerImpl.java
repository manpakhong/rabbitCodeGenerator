package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.RouteDefDao;
import hk.ebsl.mfms.dao.RouteLocationDao;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.model.Patrol_Route;

public class RouteDefManagerImpl implements RouteDefManager {
	public static final Logger logger = Logger.getLogger(RouteDefManagerImpl.class);
	RouteDefDao routeDefDao;
	RouteLocationDao routeLocationDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int saveRoute(RouteDef routeDef, List<RouteLocation> routeLocation)
			throws MFMSException {

		int insertKey = routeDefDao.saveRouteDef(routeDef);

		for (int i = 0; i < routeLocation.size(); i++) {
			routeLocation.get(i).setRouteDefKey(insertKey);
			routeLocationDao.saveRouteLoc(routeLocation.get(i));
		}
		return insertKey;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Patrol_Route searhRoute(int routeDefKey) throws MFMSException {
		// TODO Auto-generated method stub

		List<RouteDef> routeDefList = routeDefDao.searchRoute(routeDefKey);

		if (routeDefList != null) {
			List<RouteLocation> routeLocationList = routeLocationDao
					.searchRouteLocation(routeDefKey);

			Patrol_Route route = new Patrol_Route();
			for (int i = 0; i < routeDefList.get(0).getRouteLocation().size(); i++) {
				routeDefList.get(0).getRouteLocation().get(i).getLocation()
						.getKey();
			}
			route.setRouteDef(routeDefList.get(0));
			route.setRouteLocation(routeLocationList);

			return route;
		}

		return null;

	}
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteDef> getRouteDef(int[] keys) throws MFMSException{
		
		List<RouteDef> routeDefList = routeDefDao.searchRouteList(keys);
		
		return routeDefList;
	}
	

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Patrol_Route> searchRoute(int siteKey, String name,
			String code, List<Integer> locationKey) {

		List<Patrol_Route> patrolRouteList = new ArrayList<Patrol_Route>();

		List<RouteDef> routeDefList = routeDefDao.searchRoute(siteKey, name,
				code);

		if (routeDefList != null) {
			Patrol_Route patrolRoute = new Patrol_Route();
			for (int i = 0; i < routeDefList.size(); i++) {
				patrolRoute = new Patrol_Route();

				List<RouteLocation> routeLocationList = routeLocationDao
						.searchRouteLocation(routeDefList.get(i)
								.getRouteDefKey());

				patrolRoute.setRouteDef(routeDefList.get(i));
				patrolRoute.setRouteLocation(routeLocationList);

				if (locationKey != null && locationKey.size() > 0) {
					if (routeLocationList != null) {

						for (int j = 0; j < locationKey.size(); j++) {
							if (routeLocationList.get(0).getLocationKey() == locationKey
									.get(j)) {
								patrolRouteList.add(patrolRoute);
							}
						}
					}
				} else {

					patrolRouteList.add(patrolRoute);
				}
			}
		}
		if (patrolRouteList.size() > 0)
			return patrolRouteList;

		return null;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Patrol_Route> searchRoute(int siteKey, int routeDefKey) {

		List<Patrol_Route> patrolRouteList = new ArrayList<Patrol_Route>();

		List<RouteDef> routeDefList = routeDefDao.searchRoute(siteKey, routeDefKey);

		if (routeDefList != null) {
			Patrol_Route patrolRoute = new Patrol_Route();
			for (int i = 0; i < routeDefList.size(); i++) {
				patrolRoute = new Patrol_Route();

				List<RouteLocation> routeLocationList = routeLocationDao
						.searchRouteLocation(routeDefList.get(i)
								.getRouteDefKey());

				patrolRoute.setRouteDef(routeDefList.get(i));
				patrolRoute.setRouteLocation(routeLocationList);
				patrolRouteList.add(patrolRoute);
			}
		}
		if (patrolRouteList.size() > 0)
			return patrolRouteList;

		return null;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteRoute(int routeDefKey) {
		List<RouteDef> routeDef = routeDefDao.searchRoute(routeDefKey);

		if (routeDef != null && routeDef.size() > 0) {
			routeDef.get(0).setDeleted("Y");
		}

		routeDefDao.saveRouteDef(routeDef.get(0));

	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void updateRouteLocation(List<RouteLocation> routeLocationList) {

		for (int i = 0; i < routeLocationList.size(); i++) {
			routeLocationDao.saveRouteLoc(routeLocationList.get(i));
		}
	}
	

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteRouteLocation(List<RouteLocation> routeLocationList) {

		for (int i = 0; i < routeLocationList.size(); i++) {
			routeLocationDao.markDelete(routeLocationList.get(i));
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteDef> getAllRoute(int siteKey) {

		return routeDefDao.searchRouteBySiteKey(siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteDef> getRouteDefWithLastModifedTime(
			Timestamp lastModifiedTime) {

		return routeDefDao.searchRouteDefByLastModifiedTime(lastModifiedTime);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public RouteLocation searchRouteLocation(Integer routeLocationKey) {

		return routeLocationDao
				.searchRouteLocationByRouteLocationKey(routeLocationKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteLocation> getRouteLocationWithLastModifiedTime(
			Timestamp lastModifiedTime) {
		// TODO Auto-generated method stub
		return routeLocationDao
				.getRouteLocationByLastModifiedTime(lastModifiedTime);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteDef> searchRouteDefByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey){
		
		logger.debug("searchRouteDef()[" + lastModifiedDate + "]");
		
		List<RouteDef> routeDefList = routeDefDao.searchRouteDef(lastModifiedDate, offset, maxResults, siteKey);
		
		return routeDefList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchRouteDefResultCount(Timestamp lastModifiedDate, Integer siteKey){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = routeDefDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RouteLocation> searchRouteLocationByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey){
		
		logger.debug("searchRouteLocation()[" + lastModifiedDate + "]");
		
		List<RouteLocation> routeLocationList = routeLocationDao.searchRouteLocation(lastModifiedDate, offset, maxResults, siteKey);
		
		return routeLocationList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = routeLocationDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}

	// Getter && Setter
	public RouteDefDao getRouteDefDao() {
		return routeDefDao;
	}

	public void setRouteDefDao(RouteDefDao routeDefDao) {
		this.routeDefDao = routeDefDao;
	}

	public RouteLocationDao getRouteLocationDao() {
		return routeLocationDao;
	}

	public void setRouteLocationDao(RouteLocationDao routeLocationDao) {
		this.routeLocationDao = routeLocationDao;
	}

}
