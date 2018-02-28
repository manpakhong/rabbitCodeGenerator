package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.LocationDao;
import hk.ebsl.mfms.dao.LocationPrivilegeDao;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.model.LocationTreeNode;

public class LocationManagerImpl implements LocationManager {

	public static final Logger logger = Logger.getLogger(LocationManagerImpl.class);

	private LocationDao locationDao;

	private LocationPrivilegeDao locationPrivilegeDao;

	public LocationDao getLocationDao() {
		return locationDao;
	}

	public void setLocationDao(LocationDao locationDao) {
		this.locationDao = locationDao;
	}

	public LocationPrivilegeDao getLocationPrivilegeDao() {
		return locationPrivilegeDao;
	}

	public void setLocationPrivilegeDao(LocationPrivilegeDao locationPrivilegeDao) {
		this.locationPrivilegeDao = locationPrivilegeDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Location getLocationByKey(Integer locationKey) throws MFMSException {

		Location location = locationDao.getLocationByKey(locationKey);

		return location;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Location> getLocationsBySiteKey(Integer siteKey) throws MFMSException {

		logger.debug("getLocationsBySiteKey()[" + siteKey + "]");

		List<Location> locationList = locationDao.getLocationsBySiteKey(siteKey, "N");

		return locationList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<LocationPrivilege> getLocationPrivileges(Integer siteKey, Integer accountKey) throws MFMSException {

		List<LocationPrivilege> lpList = locationPrivilegeDao.getLocationPrivilegeBySiteKey(siteKey, accountKey, "N");

		return lpList;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<LocationPrivilege> getLocationPrivilegesByLocationKey(Integer locationKey) throws MFMSException {

		List<LocationPrivilege> lpList = locationPrivilegeDao.getLocationPrivilegesByLocationKey(locationKey);

		return lpList;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public LocationTreeNode buildLocationTree(HttpServletRequest request, Integer siteKey, Integer accountKey, boolean trim) throws MFMSException {
		return buildLocationTree(request, siteKey, accountKey, trim, false);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public LocationTreeNode buildLocationTree(HttpServletRequest request, Integer siteKey, Integer accountKey, boolean trim, boolean rebuild) throws MFMSException {
		LocationTreeNode rootNode = null;
		
		
		if (!rebuild) {
			if (null!=request) {
				HttpSession session = request.getSession();

				if (null!=session) {
					logger.debug("buildLocationTree from session");
					LocationTreeNode locationTree = (LocationTreeNode) session.getAttribute("locationTree");
					
					if (null!=locationTree) {
						logger.debug("buildLocationTree from session found");
						rootNode = locationTree;
					} else {
						logger.debug("buildLocationTree from session not found");
					}
				}
			}
		}
		
		if (null==rootNode || rebuild) {
			logger.debug("--- Start buildLocationTree");

			List<Location> topLocationList = locationDao.getSiteTopLevelLocations(siteKey, "N");
	
			List<Integer> privilegedLocationKeyList = locationPrivilegeDao.getPrivilegedLocationKeyBySiteKey(siteKey,
					accountKey, "N");
	
			// assume only 1 top location
			if (topLocationList == null || topLocationList.size() == 0) {
				return null;
			}
			Location rootLocation = topLocationList.get(0);
	
			// link root node to root location and allow children
			//LocationTreeNode rootNode = new LocationTreeNode(rootLocation);
			rootNode = new LocationTreeNode(rootLocation);
	
			// start recursion build
			rootNode.build(privilegedLocationKeyList);
	
			if (trim) {
				rootNode.trim();
			}

			logger.debug("--- End buildLocationTree");
		}
		
		if (null!=request) {
			logger.debug("buildLocationTree set to session");
			HttpSession session = request.getSession();

			session.setAttribute("locationTree", rootNode);
		}

		return rootNode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasLocationPrivilege(Integer locationKey, Integer accountKey) throws MFMSException {

		return locationPrivilegeDao.hasLocationPrivilege(locationKey, accountKey, "N");
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int saveLocation(Location location) throws MFMSException {
		logger.debug("saveLocation()[" + location.getCode() + "]");
		return locationDao.saveLocation(location);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Location> saveListOfLocation(List<Location> locationList) throws MFMSException {
		logger.debug("saveListOfLocation()");
		
		return locationDao.saveListOfLocation(locationList);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Integer> getPrivilegedLocationKeyList(Integer siteKey, Integer accountKey) throws MFMSException {

		return locationPrivilegeDao.getPrivilegedLocationKeyBySiteKey(siteKey, accountKey, "N");

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Location> searchLocation(Integer siteKey, Integer parentKey, String code, String name)
			throws MFMSException {
		logger.debug("searchLocation()[" + siteKey + "," + code + "," + name + "]");
		List<Location> list = locationDao.searchLocation(siteKey, parentKey, code, name, "N");
		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteLocationByKey(Integer accountKey, Integer locationKey) throws MFMSException {

		Location location = locationDao.getLocationByKey(locationKey);

		List<Location> locationList = locationDao.getDeleteLocationList(location.getChain());
		for (Location deleteLocation : locationList) {
			deleteLocation.setDeleted("Y");
			deleteLocation.setLastModifyBy(accountKey);
			deleteLocation.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
			saveLocation(deleteLocation);
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getChildSize(Location location) throws MFMSException {

		return locationDao.getChildSize(location);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Location getLocationByCode(Integer siteKey, String code, boolean fetch) throws MFMSException {

		Location location = locationDao.getLocationByCode(siteKey, code, "N");

		return location;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Location> getAllLocation() throws MFMSException {

		List<Location> list = locationDao.getAllLocation();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveLocationPrivilege(LocationPrivilege locationPrivilege) throws MFMSException {

		locationPrivilegeDao.saveLocationPrivilege(locationPrivilege);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public LocationPrivilege getLocationPrivilege(Integer locationKey, Integer accountKey) throws MFMSException {

		logger.debug("getLocationPrivilege()[" + locationKey + "," + accountKey + "]");

		LocationPrivilege lp = locationPrivilegeDao.getLocationPrivilege(locationKey, accountKey, "N");

		return lp;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<LocationPrivilege> getAllLocationPrivileges() throws MFMSException {

		List<LocationPrivilege> lpList = locationPrivilegeDao.getAllLocationPrivileges();

		return lpList;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Location> searchLocationByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		logger.debug("searchLocation()[" + lastModifiedDate + "]");

		List<Location> siteList = locationDao.searchLocation(lastModifiedDate, offset, maxResults, siteKey);

		return siteList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = locationDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<LocationPrivilege> searchLocationPrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		logger.debug("searchLocation()[" + lastModifiedDate + "]");

		List<LocationPrivilege> locationPrivilegeList = locationPrivilegeDao.searchLocationPrivilege(lastModifiedDate, offset, maxResults, siteKey);

		return locationPrivilegeList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCountByLocationPrivilege(Timestamp lastModifiedDate, Integer siteKey){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = locationPrivilegeDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}
}
