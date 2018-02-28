package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AccountGroupAccountDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(AccountGroupAccountDao.class);

	@SuppressWarnings("unchecked")
	public List<AccountGroupAccount> getAllAccountGroupAccount() {

		return getDefaultAccountGroupAccountCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public AccountGroupAccount getAccountGroupAccount(Integer groupKey, Integer accountKey) {

		Criteria criteria = getSession().createCriteria(AccountGroupAccount.class);
		criteria.add(Restrictions.eq("groupKey", groupKey));
		criteria.add(Restrictions.eq("accountKey", accountKey));

		List<AccountGroupAccount> list = criteria.list();

		if (list != null && list.size() > 0) {

			return list.get(0);
		} else {

			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccountGroupAccount> searchAccountGroupAccount(Integer accountGroupKey) {

		logger.debug("searchAccountGroupAccount()[" + accountGroupKey + "]");

		Criteria criteria = getDefaultAccountGroupAccountCriteria();

		if (accountGroupKey != null) {
			criteria.add(Restrictions.eq("groupKey", accountGroupKey));

		}

		criteria = criteria.createAlias("account", "ac").add(Restrictions.eq("ac.deleted", "N"));

		List<AccountGroupAccount> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AccountGroupAccount> checkAccountHasGroup(Integer accountKey) {

		Criteria criteria = getDefaultAccountGroupAccountCriteria();

		if (accountKey != null) {
			criteria.add(Restrictions.eq("accountKey", accountKey));
		}

		List<AccountGroupAccount> list = criteria.list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AccountGroupAccount> getAccountGroupAccountListByDate(Timestamp lastModifiedTime) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroupAccount.class);
		if (lastModifiedTime != null) {
			criteria.add(Restrictions.gt("lastModifyDateTime", lastModifiedTime));
		}

		return criteria.list();
	}

	public void saveAccountGroupAccount(AccountGroupAccount accountGroupAccount) {

		logger.debug("saveAccountGroupAccount");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(accountGroupAccount);
	}

	@SuppressWarnings("unchecked")
	public List<AccountGroupAccount> searchAccountGroupAccount(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroupAccount.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<AccountGroupAccount> agaList = criteria.list();

		return agaList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroupAccount.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

		return totalResult;
	}

	private Criteria getDefaultAccountGroupAccountCriteria() {

		// default criteria for accountGroup
		Session currentSession = getSession();

		return currentSession.createCriteria(AccountGroupAccount.class).add(Restrictions.eq("deleted", "N"));
	}

}
