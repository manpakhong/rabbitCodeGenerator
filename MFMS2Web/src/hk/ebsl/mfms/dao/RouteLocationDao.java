package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class RouteLocationDao extends BaseDao {

	public static final Logger logger = Logger
			.getLogger(RouteLocationDao.class);

	private String dto_RouteDefKey = "routeDefKey";
	private String dto_SeqNum = "seqNum";
	private String dto_RouteLocationKey = "routeLocationKey";
	private String dto_LastModifyDateTime = "lastModifyDateTime";

	public int saveRouteLoc(RouteLocation routeLocation) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(routeLocation);

		return routeLocation.getRouteLocationKey();
	}

	@SuppressWarnings("unchecked")
	public List<RouteLocation> searchRouteLocation(int routeDefKey) {

		List<RouteLocation> list = getDefaultCriteria()
				.add(Restrictions.eq(dto_RouteDefKey, routeDefKey))
				.addOrder(Order.asc(dto_SeqNum)).list();

		if (list != null && list.size() > 0)
			return list;
		else
			return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public RouteLocation searchRouteLocationByRouteLocationKey(int routeLocationKey){
		
		List<RouteLocation> list = getDefaultCriteria()
				.add(Restrictions.eq(dto_RouteLocationKey, routeLocationKey)).list();
		
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public void delete(RouteLocation routeLocation){
		
		Session currentSession = getSession();

		currentSession.delete(routeLocation);
	}
	
	public void markDelete(RouteLocation routeLocation){
		
		Session currentSession = getSession();

		routeLocation.setDeleted("Y");
		currentSession.update(routeLocation);
	}
	
	@SuppressWarnings("unchecked")
	public List<RouteLocation> getRouteLocationByLastModifiedTime(Timestamp lastModifiedTime){
		
		List<RouteLocation> list = getDefaultCriteria()
				.add(Restrictions.gt(dto_LastModifyDateTime, lastModifiedTime)).list();
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<RouteLocation> searchRouteLocation(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RouteLocation.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria = criteria.createAlias("routeDef", "r").add(Restrictions.eq("r.siteKey", siteKey));
		}
		List<RouteLocation> routeLocationList = criteria.list();

		return routeLocationList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RouteLocation.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria = criteria.createAlias("routeDef", "r").add(Restrictions.eq("r.siteKey", siteKey));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	//private functions
	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();
		Criteria criteria = currentSession.createCriteria(RouteLocation.class);
		
		return criteria;
	}

}
