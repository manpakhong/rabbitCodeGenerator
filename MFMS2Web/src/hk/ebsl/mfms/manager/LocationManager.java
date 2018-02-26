package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.model.LocationTreeNode;

public interface LocationManager {

	public Location getLocationByKey(Integer locationKey) throws MFMSException;

	public List<Location> getLocationsBySiteKey(Integer siteKey) throws MFMSException;

	public List<LocationPrivilege> getLocationPrivileges(Integer siteKey, Integer accountKey) throws MFMSException;

	public LocationTreeNode buildLocationTree(HttpServletRequest request, Integer siteKey, Integer accountKey, boolean trim) throws MFMSException;

	public LocationTreeNode buildLocationTree(HttpServletRequest request, Integer siteKey, Integer accountKey, boolean trim, boolean rebuild) throws MFMSException;

	public boolean hasLocationPrivilege(Integer locationKey, Integer accountKey) throws MFMSException;

	public int saveLocation(Location location) throws MFMSException;
	
	public List<Location> saveListOfLocation(List<Location> locationList) throws MFMSException;

	public List<Integer> getPrivilegedLocationKeyList(Integer siteKey, Integer accountKey) throws MFMSException;

	public List<Location> searchLocation(Integer siteKey, Integer parentKey, String code, String name)
			throws MFMSException;

	public void deleteLocationByKey(Integer accountKey, Integer locationKey) throws MFMSException;

	public Location getLocationByCode(Integer key, String code, boolean fetch) throws MFMSException;

	public List<Location> getAllLocation() throws MFMSException;

	public void saveLocationPrivilege(LocationPrivilege locationPrivilege) throws MFMSException;

	public LocationPrivilege getLocationPrivilege(Integer locationKey, Integer accountKey) throws MFMSException;

	public List<LocationPrivilege> getAllLocationPrivileges() throws MFMSException;

	public Integer getChildSize(Location location) throws MFMSException;

	public List<LocationPrivilege> getLocationPrivilegesByLocationKey(Integer locationKey) throws MFMSException;
	
	public List<Location> searchLocationByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);
	
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);
	
	public List<LocationPrivilege> searchLocationPrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);
	
	public Integer searchResultCountByLocationPrivilege(Timestamp lastModifiedDate, Integer siteKey);
	
}
