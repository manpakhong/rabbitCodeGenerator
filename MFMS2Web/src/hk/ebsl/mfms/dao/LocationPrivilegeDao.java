package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.LocationManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationPrivilegeDao extends BaseDao {

	@Autowired
	private LocationManager locationManager;

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public static final Logger logger = Logger.getLogger(LocationPrivilegeDao.class);

	@SuppressWarnings("unchecked")
	public List<LocationPrivilege> getLocationPrivilegeBySiteKey(Integer siteKey, Integer accountKey, String deleted) {

		Criteria criteria = getDefaultCriteria();

		if (siteKey != null && accountKey != null && deleted != null) {
			criteria.add(Restrictions.eq("accountKey", accountKey)).add(Restrictions.eq("deleted", deleted));
			criteria.createAlias("location", "l").add(Restrictions.eq("l.siteKey", siteKey));
			criteria.addOrder(Order.asc("l.levelKey"));
		} else {
			return null;
		}

		List<LocationPrivilege> lpList = criteria.list();
		return lpList;
	}

	@SuppressWarnings("unchecked")
	public List<LocationPrivilege> getLocationPrivilegesByLocationKey(Integer locationKey) {

		Criteria criteria = getDefaultCriteria();

		if (locationKey != null) {
			criteria.add(Restrictions.eq("locationKey", locationKey)).add(Restrictions.eq("deleted", "N"));
		} else {
			return null;
		}
		List<LocationPrivilege> lpList = criteria.list();
		return lpList;
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getPrivilegedLocationKeyBySiteKey(Integer siteKey, Integer accountKey, String deleted) {
		// 2018-02-01 Get all location insteat of location privilege
		logger.debug("****New getPrivilegedLocationKeyBySiteKey" );
		List<Integer> lpList = new ArrayList();;
		
		try {
			List<Location> lList = locationManager.getLocationsBySiteKey(siteKey);

			for (Location l : lList) {
				lpList.add(l.getKey());
			}
		
		
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		/*
		Criteria criteria = getDefaultCriteria();

		if (siteKey != null && accountKey != null && deleted != null) {
			criteria.add(Restrictions.eq("accountKey", accountKey)).add(Restrictions.eq("deleted", deleted));
			criteria.createAlias("location", "l").add(Restrictions.eq("l.siteKey", siteKey));
			criteria.setProjection(Projections.property("locationKey"));

		} else {
			return null;
		}

		List<Integer> lpList = criteria.list();
		*/

		return lpList;
	}

	public boolean hasLocationPrivilege(Integer locationKey, Integer accountKey, String deleted) {


		Criteria criteria = getDefaultCriteria();

		if (locationKey != null && accountKey != null && deleted != null) {
			criteria.add(Restrictions.eq("locationKey", locationKey)).add(Restrictions.eq("accountKey", accountKey))
					.add(Restrictions.eq("deleted", deleted));

			boolean exist = criteria.setProjection(Projections.property("key")).uniqueResult() != null;

			return exist;
		}

		return false;

	}

	@SuppressWarnings("unchecked")
	public LocationPrivilege getLocationPrivilege(Integer locationKey, Integer accountKey, String deleted) {


		Criteria criteria = getDefaultCriteria();

		if (locationKey != null && accountKey != null && deleted != null) {

			criteria.add(Restrictions.eq("locationKey", locationKey)).add(Restrictions.eq("accountKey", accountKey))
					.add(Restrictions.eq("deleted", deleted));

			List<LocationPrivilege> lpList = criteria.list();

			if (lpList != null && lpList.size() > 0) {
				return lpList.get(0);
			} else {
				return null;
			}
		}

		return null;

	}

	public void saveLocationPrivilege(LocationPrivilege locationPrivilege) {


		Session currentSession = getSession();
		currentSession.saveOrUpdate(locationPrivilege);
	}

	@SuppressWarnings("unchecked")
	public List<LocationPrivilege> getAllLocationPrivileges() {

		logger.debug("test()");

		Criteria criteria = getDefaultCriteria().add(Restrictions.eq("deleted", "N"));
		;

		List<LocationPrivilege> lpList = criteria.list();
		return lpList;
	}

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		return currentSession.createCriteria(LocationPrivilege.class);

	}
	
	
	@SuppressWarnings("unchecked")
	public List<LocationPrivilege> searchLocationPrivilege(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(LocationPrivilege.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey!= null){
			criteria.createAlias("location", "l").add(Restrictions.eq("l.siteKey", siteKey));
		}
		
		List<LocationPrivilege> locationPrivilegeList = criteria.list();

		return locationPrivilegeList;
	}
	
	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(LocationPrivilege.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey!= null){
			criteria.createAlias("location", "l").add(Restrictions.eq("l.siteKey", siteKey));
		}
		
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
}
