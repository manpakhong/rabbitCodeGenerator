package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;

public class UserAccountRoleDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(UserAccountRoleDao.class);

	public void saveUserAccountRole(UserAccountRole ra) {

		logger.debug("saveUserAccountRole()[" + ra.getAccountKey() + "," + ra.getRoleKey() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(ra);
	}

	@SuppressWarnings("unchecked")
	public List<UserAccountRole> searchUserAccountRole(Integer siteKey, Integer roleKey, String loginId,
			String accountName, String accountStatus) {

		// logger.debug("searchUserAccountRole()[" + siteKey + "," + roleKey +
		// "," + loginId + "," + accountName + "," + accountStatus + "]");

		Criteria criteria = getDefaultCriteria();

		criteria.createAlias("userAccount", "a");

		criteria.createAlias("role", "r");

		if (siteKey != null) {
			criteria.add(Restrictions.eq("r.siteKey", siteKey));
		}

		if (roleKey != null) {
			criteria.add(Restrictions.eq("roleKey", roleKey));
		}

		if (loginId != null || accountName != null || accountStatus != null) {

			if (loginId != null) {
				criteria.add(Restrictions.ilike("a.loginId", DaoUtil.escape(loginId), MatchMode.ANYWHERE));
			}
			if (accountName != null) {
				criteria.add(Restrictions.ilike("a.name", DaoUtil.escape(accountName), MatchMode.ANYWHERE));
			}
			if (!accountStatus.isEmpty()) {

				criteria.add(Restrictions.eq("a.status", accountStatus));
			}
		}
		criteria.add(Restrictions.eq("a.deleted", "N"));
		criteria.add(Restrictions.eq("r.deleted", "N"));
		criteria.add(Restrictions.eq("deleted", "N"));
		criteria.addOrder(Order.asc("a.loginId"));

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<UserAccountRole> searchAccount(String accountName, int roleKey) {
		Criteria criteria = getDefaultUserAccountCriteria();
		criteria.createAlias("userAccount", "ac");

		if (roleKey != -1)
			criteria.add(Restrictions.eq("roleKey", roleKey));

		if (accountName != null && !accountName.equals(""))
			criteria.add(Restrictions.ilike("ac.name", DaoUtil.escape(accountName), MatchMode.ANYWHERE));

		criteria.add(Restrictions.eq("deleted", "N"));
		criteria.add(Restrictions.eq("ac.deleted", "N"));
		criteria.addOrder(Order.asc("ac.loginId").ignoreCase());

		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserAccountRole> searchUserAccountRole(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(UserAccountRole.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);
		
		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		List<UserAccountRole> accountRoleList = criteria.list();
		
		return accountRoleList;
	}
	
	
	public Integer searchResultCountByUserAccountRole(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(UserAccountRole.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	
	

	private Criteria getDefaultUserAccountCriteria() {
		Session currentSession = getSession();
		return currentSession.createCriteria(UserAccountRole.class);
	}

	private Criteria getDefaultCriteria() {
		Session currentSession = getSession();
		return currentSession.createCriteria(UserAccountRole.class);
	}
}
