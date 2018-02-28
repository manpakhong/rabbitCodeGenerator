package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Tool;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class LocationDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(LocationDao.class);

	@SuppressWarnings("unchecked")
	public List<Location> getLocationsBySiteKey(Integer siteKey, String deleted) {

		logger.debug("getLocationsBySiteKey()[" + siteKey + "," + deleted + "]");

		Criteria criteria = getDefaultCriteria();

		if (siteKey != null && deleted != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("deleted", deleted));
		} else {
			return null;
		}
		
		criteria.addOrder(Order.asc("levelKey"));
		criteria.addOrder(Order.asc("key"));
		

		List<Location> locationList = criteria.list();

		return locationList;
	}

	@SuppressWarnings("unchecked")
	public List<Location> getDeleteLocationList(String chain) {

		Criteria criteria = getDefaultCriteria();

		if (chain != null) {
			criteria.add(Restrictions.like("chain", chain + "%")).add(Restrictions.eq("deleted", "N"));
		}
		List<Location> locationList = criteria.list();

		return locationList;
	}

	@SuppressWarnings("unchecked")
	public Integer getChildSize(Location location) {

		Criteria criteria = getDefaultCriteria();

		if (location != null) {
			criteria.add(Restrictions.like("chain", location.getChain() + "%")).add(Restrictions.eq("deleted", "N"));
		}
		List<Location> locationList = criteria.list();

		return locationList.size() - 1;
	}

	@SuppressWarnings("unchecked")
	public Location getLocationByKey(Integer key) {

		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq("key", key));

		List<Location> locationList = criteria.list();

		if (locationList != null && locationList.size() > 0) {
			return locationList.get(0);
		} else {

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Location> getLocationsByParentKey(Integer parentKey, String deleted) {

		logger.debug("getLocationByParentKey()[" + parentKey + "," + deleted + "]");

		Criteria criteria = getDefaultCriteria();

		if (parentKey != null && deleted != null) {
			criteria.add(Restrictions.eq("parentKey", parentKey)).add(Restrictions.eq("deleted", deleted));
		} else {
			return null;
		}

		List<Location> locationList = criteria.list();

		return locationList;
	}

	@SuppressWarnings("unchecked")
	public List<Location> getSiteTopLevelLocations(Integer siteKey, String deleted) {

		logger.debug("getSiteTopLevelLocations()[" + siteKey + "," + deleted + "]");

		Criteria criteria = getDefaultCriteria();

		if (siteKey != null && deleted != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("parentKey", 0))
					.add(Restrictions.eq("deleted", deleted));
		} else {
			return null;
		}

		List<Location> locationList = criteria.list();

		return locationList;
	}

	public int saveLocation(Location location) {
		Session currentSession = getSession();
		currentSession.saveOrUpdate(location);
		currentSession.flush();
		return location.getKey();

	}
	
	public List<Location> saveListOfLocation(List<Location> locationList){
		Session currentSession = getSession();
		for(Location location : locationList){
			currentSession.saveOrUpdate(location);
		}
		currentSession.flush();
		
		return locationList;
	}

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		// return
		// currentSession.createCriteria(Location.class).add(Restrictions.eq("deleted",
		// "N"));

		return currentSession.createCriteria(Location.class);
	}

	@SuppressWarnings("unchecked")
	public List<Location> searchLocation(Integer siteKey, Integer parentKey, String code, String name, String deleted) {

		logger.debug("searchLocation()[" + siteKey + "," + code + "," + name + "," + deleted + "]");

		Criteria criteria = getDefaultCriteria();

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (parentKey != null) {
			criteria.add(Restrictions.eq("parentKey", parentKey));
		}

		if (code != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("code", DaoUtil.escape(code), MatchMode.ANYWHERE));
		}
		if (name != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("name", DaoUtil.escape(name), MatchMode.ANYWHERE));
		}
		if (deleted != null) {
			// case insensitive
			criteria.add(Restrictions.eq("deleted", deleted));
		}
		List<Location> list = criteria.list();
		return list;

	}

	@SuppressWarnings("unchecked")
	public Location getLocationByCode(Integer siteKey, String code, String deleted) {

		logger.debug("getLocationByCode()[" + siteKey + "," + code + "]");

		Criteria criteria = getDefaultCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("code", code));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<Location> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public List<Location> getAllLocation() {

		logger.debug("getAllLocation()");

		return getDefaultCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public List<Location> searchLocation(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Location.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<Location> locationList = criteria.list();

		return locationList;
	}
	
	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Location.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
			
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	

}
