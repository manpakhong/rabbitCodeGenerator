package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class RoleDao extends BaseDao {

	
	public static final Logger logger = Logger.getLogger(RoleDao.class);
	
	
	@SuppressWarnings("unchecked")
	public Role getRoleByKey(Integer key) {
		
		logger.debug("getRoleByKey()[" + key + "]");
		
		List<Role> list = getDefaultCriteria().add(Restrictions.eq("key", key)).list();
		
		if (list != null && list.size() > 0) 
			return list.get(0);
		else 
			return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> getRolesBySiteKey(Integer siteKey) {
		
		logger.debug("getRolesBySiteKey()[" + siteKey + "]");
		
		List<Role> list = getDefaultCriteria().add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("deleted", "N")).list();
		
		return list;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> searchRole(Integer siteKey, String name, String description, String deleted) {
		
		logger.debug("searchRole()[" + siteKey + "," + name + "," + deleted + "]");
		
		Criteria criteria = getDefaultCriteria();
		
		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		if (name != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("name", DaoUtil.escape(name), MatchMode.ANYWHERE));
		}
		if (description != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("description", DaoUtil.escape(description), MatchMode.ANYWHERE));
		}
		if (deleted != null) {
			
			criteria.add(Restrictions.eq("deleted", deleted));
		}
		
		List<Role> roleList = criteria.list();
		
		return roleList;
	}
	
	public void saveRole(Role role) {
		
		logger.debug("saveRole()[" + role.getName() + "]");
		
		Session currentSession = getSession();
		
		currentSession.saveOrUpdate(role);
	}
	
	@SuppressWarnings("unchecked")
	public Role getRoleByName(Integer siteKey, String name, String deleted) {
		
		logger.debug("getRoleByName()[" + siteKey + "," + name + "]");
		
		Criteria criteria = getDefaultCriteria().add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.like("name", name, MatchMode.EXACT));
		
		if (deleted != null) criteria.add(Restrictions.eq("deleted", deleted));
		
		List<Role> list = criteria.list();
		
		if (list != null && list.size() > 0) 
			return list.get(0);
		else 
			return null;
	
	}
	
	@SuppressWarnings("unchecked")
	public List<Role> searchRole(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Role.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);
		
		if(siteKey!=null)
			criteria.add(Restrictions.eq("siteKey", siteKey));
		
		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<Role> roleList = criteria.list();
		
		return roleList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Role.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	private Criteria getDefaultCriteria() {
		
		Session currentSession = getSession();
		
		return currentSession.createCriteria(Role.class);
	}
}
