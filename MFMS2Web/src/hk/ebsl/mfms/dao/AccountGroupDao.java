package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.Role;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class AccountGroupDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(AccountGroupDao.class);

	@SuppressWarnings("unchecked")
	public List<AccountGroup> getAllAccountGroup() {

		logger.debug("getAllSites()");

		return getDefaultAccountGroupCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public List<AccountGroup> getAccountGroupBySiteKey(Integer siteKey) {

		Criteria criteria = getDefaultAccountGroupCriteria();
		criteria.add(Restrictions.eq("siteKey", siteKey));
		List<AccountGroup> list = criteria.list();
		
		Collections.sort(list, new AccountGroupComparator());
		
		
		return list;
	}

	@SuppressWarnings("unchecked")
	public AccountGroup getAccountGroupByKey(Integer key) {
		logger.debug("getAccountGroupByKey()[" + key + "]");

		Criteria criteria = getDefaultAccountGroupCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<AccountGroup> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public AccountGroup getAccountGroupByName(Integer siteKey, String name) {

		Criteria criteria = getDefaultAccountGroupCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("name", name));

		List<AccountGroup> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	public int saveAccountGroup(AccountGroup accountGroup) {

		logger.debug("saveAccountGroup");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(accountGroup);

		return accountGroup.getKey();
	}
	
	@SuppressWarnings("unchecked")
	public List<AccountGroup> searchAccountGroup(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroup.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);
		
		if(siteKey!=null)
		criteria.add(Restrictions.eq("siteKey", siteKey));
		
		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<AccountGroup> accountGroupList = criteria.list();
		
		return accountGroupList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroup.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	public Integer theCountOfSearchResultWithSiteKey(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AccountGroup.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate)).add(Restrictions.eq("siteKey", siteKey));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	

	private Criteria getDefaultAccountGroupCriteria() {

		// default criteria for accountGroup
		Session currentSession = getSession();

		return currentSession.createCriteria(AccountGroup.class).add(Restrictions.eq("deleted", "N"));
	}
	
	public class AccountGroupComparator implements Comparator<AccountGroup> {
		@Override
		public int compare(AccountGroup st, AccountGroup nd) {
			return st.getName().toLowerCase().compareTo(nd.getName().toLowerCase());
		}
	}

}
