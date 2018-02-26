package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.criteria.Join;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

public class UserAccountDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(UserAccountDao.class);

	@SuppressWarnings("unchecked")
	public List<UserAccount> getAllUserAccount() {

		logger.debug("getAllUserAccount()");

		List<UserAccount> list = getDefaultUserAccountCriteria().add(Restrictions.eq("deleted", "N"))
				.addOrder(Order.asc("name")).list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public UserAccount getUserAccountByLoginId(String loginId) {

		List<UserAccount> list = getDefaultUserAccountCriteria().add(Restrictions.eq("loginId", loginId))
				.add(Restrictions.eq("deleted", "N")).list();
		if (list != null && list.size() > 0) {
			return (UserAccount) list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public UserAccount getUserAccountByKey(Integer key) {

		logger.debug("getUserAccountByKey() [" + key + "]");

		List<UserAccount> list = getDefaultUserAccountCriteria().add(Restrictions.eq("key", key)).list();
		if (list != null && list.size() > 0) {
			return (UserAccount) list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<UserAccount> searchUserAccount(String loginId, String accountName, String accountStatus) {

		logger.debug("searchUserAccount()[" + "," + loginId + "," + accountName + "," + accountStatus + "]");

		Criteria criteria = getDefaultUserAccountCriteria();

		if (loginId != null) {
			criteria.add(Restrictions.ilike("loginId", DaoUtil.escape(loginId), MatchMode.ANYWHERE));
		}
		if (accountName != null) {
			criteria.add(Restrictions.ilike("name", DaoUtil.escape(accountName), MatchMode.ANYWHERE));
		}
		if (accountStatus != null) {

			if (!accountStatus.isEmpty()) {

				criteria.add(Restrictions.eq("status", accountStatus));
			}
		}

		criteria.add(Restrictions.eq("deleted", "N"));

		criteria.addOrder(Order.asc("loginId"));

		return criteria.list();

	}

	public void incrementLogonAttemptCount(String loginId) {

		// increment login attempt count by 1
		logger.debug("incrementLogonAttemptCount()[" + loginId + "]");

		Session currentSession = getSession();

		Query q = currentSession.createQuery(
				"UPDATE UserAccount SET logonAttemptCount = logonAttemptCount + 1 WHERE loginId = :loginId");

		q.setString("loginId", loginId);

		q.executeUpdate();

	}

	public void resetLogonAttemptCount(String loginId) {

		// reset login attempt count to 0
		logger.debug("resetLogonAttemptCount()[" + loginId + "]");

		Session currentSession = getSession();

		Query q = currentSession.createQuery("UPDATE UserAccount SET logonAttemptCount = 0 WHERE loginId = :loginId");

		q.setString("loginId", loginId);

		q.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	public List<UserAccount> checkUserLogin(String ac, String pwd) {

		Criteria criteria = getDefaultUserAccountCriteria();

		criteria = criteria.add(Restrictions.eq("loginId", ac)).add(Restrictions.eq("deleted", "N"));

		UserAccount acc = null;
		
		if (criteria.list().size()>0) {
			acc = (UserAccount) criteria.list().get(0);
		}
		
		if (null!=acc && BCrypt.checkpw(pwd, acc.getPassword()))
			return criteria.list();
		return null;

	}

	@SuppressWarnings("unchecked")
	public List<UserAccount> checkUserEncryptedLogin(String ac, String pwd) {
		Criteria criteria = getDefaultUserAccountCriteria();

		criteria = criteria.add(Restrictions.eq("loginId", ac)).add(Restrictions.eq("deleted", "N"));

		UserAccount acc = (UserAccount) criteria.list().get(0);
		
		if ((pwd.equals(acc.getPassword()))) {
			return criteria.list();
		}
		return null;
	}

	public void saveUserAccount(UserAccount account) {
		logger.debug("saveUserAccount()[" + account.getLoginId() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(account);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<UserAccount> searchUserAccount(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(UserAccount.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);
		
		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		List<UserAccount> accountList = criteria.list();
		
		return accountList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(UserAccount.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	

	private Criteria getDefaultUserAccountCriteria() {
		Session currentSession = getSession();
		return currentSession.createCriteria(UserAccount.class);
	}
}
