package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.Privilege;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class RolePrivilegeDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(RolePrivilegeDao.class);
	
	public void saveRolePrivilege(RolePrivilege rp) {
		
		logger.debug("saveRolePrivilege()[" + rp.getRoleKey() + "," + rp.getPrivilegeCode() + "]");
		
		Session currentSession = getSession();
		
		currentSession.saveOrUpdate(rp);
	}
	
	public List<RolePrivilege> searchRoleByPrivilege(String privilege){
		Criteria criteria = getDefaultCriteria();
		
		criteria = criteria.createAlias("privilege", "p").add(Restrictions.eq("p.code", privilege));
		
		return criteria.list();
	}
	
	public List<RolePrivilege> searchRoleByRoleKey(Integer roleKey){
		Criteria criteria = getDefaultCriteria();
		
		criteria = criteria.add(Restrictions.eq("roleKey", roleKey));
		
		return criteria.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<RolePrivilege> searchRolePrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RolePrivilege.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);
		
		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<RolePrivilege> rolePrivilegeList = criteria.list();
		
		return rolePrivilegeList;
	}

	public Integer theCountOfSearchResultbyRolePrivilege(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(RolePrivilege.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	
	
	
	
	
	private Criteria getDefaultCriteria() {
		
		Session currentSession = getSession();
		
		Criteria criteria = currentSession.createCriteria(RolePrivilege.class);
		criteria = criteria.add(Restrictions.eq("deleted", "N"));

		return criteria;
	}
}
