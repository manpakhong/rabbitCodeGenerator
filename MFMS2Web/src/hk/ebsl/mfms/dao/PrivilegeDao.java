package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;
import hk.ebsl.mfms.dto.Privilege;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class PrivilegeDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PrivilegeDao.class);

	@SuppressWarnings("unchecked")
	public Privilege getPrivilegeByCode(String code) {

		logger.debug("getPrivilegeByCode()[" + code + "]");

		List<Privilege> list = getDefaultCriteria().add(Restrictions.eq("code", code)).list();

		if (list != null && list.size() > 0) {
			return list.get(0);

		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Privilege> searchPrivilege(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Privilege.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<Privilege> privilegeList = criteria.list();

		return privilegeList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Privilege.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

		return totalResult;
	}

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		return currentSession.createCriteria(Privilege.class);
	}
}
