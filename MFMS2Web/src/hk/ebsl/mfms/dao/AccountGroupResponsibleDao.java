package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupResponsible;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AccountGroupResponsibleDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(AccountGroupResponsibleDao.class);

	@SuppressWarnings("unchecked")
	public List<AccountGroupResponsible> getAllAccountGroupResponsible() {

		logger.debug("getAllSites()");
		
		return getDefaultAccountGroupResponsibleCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public AccountGroupResponsible getAccountGroupResponsible(Integer groupKey, Integer accountKey) {

		Criteria criteria = getSession().createCriteria(AccountGroupResponsible.class);
		criteria.add(Restrictions.eq("groupKey", groupKey));
		criteria.add(Restrictions.eq("accountKey", accountKey));

		List<AccountGroupResponsible> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AccountGroupResponsible> searchAccountGroupResponsible(Integer accountGroupKey
		) {

		logger.debug("searchAccountGroupResponsible()[" + accountGroupKey + "]");
		
		Criteria criteria = getDefaultAccountGroupResponsibleCriteria();

		if (accountGroupKey != null) {
			criteria.add(Restrictions.eq("groupKey", accountGroupKey));
		}
		
		List<AccountGroupResponsible> list = criteria.list();

		return list;
	}
	
	public void saveAccountGroupResponsible(AccountGroupResponsible accountGroupResponsible) {
		
		logger.debug("saveAccountGroupResponsible");
		
		Session currentSession = getSession();

		currentSession.saveOrUpdate(accountGroupResponsible);
	}
	
	public List<AccountGroupResponsible> getAccountGroupAccountListByDate(Timestamp lastModifyDate){
		
		
		logger.debug("getAccountGroupAccountListByDate");
		Session currentSession = getSession();
		Criteria criteria = currentSession.createCriteria(AccountGroupResponsible.class);
		if (lastModifyDate != null) {
			criteria.add(Restrictions.gt("lastModifyDateTime", lastModifyDate));
		}
		
		return criteria.list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountGroupResponsible> searchAccountGroupResponsible(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroupResponsible.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<AccountGroupResponsible> agrList = criteria.list();

		return agrList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroupResponsible.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

		return totalResult;
	}
	

	private Criteria getDefaultAccountGroupResponsibleCriteria() {

		// default criteria for accountGroup
		Session currentSession = getSession();
		
		return currentSession.createCriteria(AccountGroupResponsible.class).add(Restrictions.eq("deleted", "N"));
	}


}
