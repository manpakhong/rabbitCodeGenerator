package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.Site;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class RouteDefDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(RouteDefDao.class);

	private String dto_Key = "routeDefKey";
	private String dto_Name = "name";
	private String dto_Code = "code";
	private String dto_Delete = "deleted";
	private String dto_SiteKey = "siteKey";
	private String dto_LastModifyDateTime = "lastModifyDateTime";

	public int saveRouteDef(RouteDef routeDef) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(routeDef);
		currentSession.flush();

		return routeDef.getRouteDefKey();
	}

	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRoute(int key) {

		List<RouteDef> list = getDefaultCriteria().add(
				Restrictions.eq(dto_Key, key)).list();

		if (list != null && list.size() > 0)
			return list;
		else
			return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRouteList(int[] keys) {
		List<RouteDef> routeDefList = new ArrayList<RouteDef>();
		for(int key:keys){
		List<RouteDef> list = getDefaultCriteria().add(
				Restrictions.eq(dto_Key, key)).list();
		routeDefList.add(list.get(0));
		}

		return routeDefList;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRoute(int[] key) {

		List<RouteDef> list = getDefaultCriteria().add(
				Restrictions.eq(dto_Key, key)).list();

		if (list != null && list.size() > 0)
			return list;
		else
			return null;
	}
	

	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRoute(int siteKey, String name, String code) {

		Criteria criteria = getDefaultCriteria();

		if (siteKey > 0) {
			criteria = criteria.add(Restrictions.eq(dto_SiteKey, siteKey));
		}
		if (name != null && !name.equals("")) {
			criteria = criteria.add(Restrictions.ilike(dto_Name,
					DaoUtil.escape(name), MatchMode.ANYWHERE));
		}
		if (code != null && !code.equals("")) {
			criteria = criteria.add(Restrictions.ilike(dto_Code,
					DaoUtil.escape(code), MatchMode.ANYWHERE));
		}

		criteria = criteria.add(Restrictions.eq(dto_Delete, "N"));

		List<RouteDef> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRoute(int siteKey, int routeDefKey) {

		Criteria criteria = getDefaultCriteria();

		if (siteKey > 0) {
			criteria = criteria.add(Restrictions.eq(dto_SiteKey, siteKey));
		}
		if (routeDefKey > 0) {
			criteria = criteria.add(Restrictions.eq(dto_Key, routeDefKey));
		}

		criteria = criteria.add(Restrictions.eq(dto_Delete, "N"));

		List<RouteDef> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRouteBySiteKey(int siteKey) {
		Criteria criteria = getDefaultCriteria();

		criteria = criteria.add(Restrictions.eq(dto_SiteKey, siteKey)).add(
				Restrictions.eq(dto_Delete, "N"));

		List<RouteDef> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRouteDefByLastModifiedTime(
			Timestamp lastModifiedTime) {
		Criteria criteria = getDefaultCriteria();

		criteria = criteria.add(Restrictions.gt(dto_LastModifyDateTime,
				lastModifiedTime));

		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RouteDef> searchRouteDef(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RouteDef.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<RouteDef> routeDefList = criteria.list();
		
		return routeDefList;
	}
	

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RouteDef.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	/**                 **/
	/** Other Functions **/
	/**                 **/

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RouteDef.class);
		criteria = criteria.addOrder(Order.asc(dto_Name));

		return criteria;
	}

}
