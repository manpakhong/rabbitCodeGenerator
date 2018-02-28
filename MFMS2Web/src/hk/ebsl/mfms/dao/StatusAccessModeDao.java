package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.StatusAccessMode;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class StatusAccessModeDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(StatusAccessModeDao.class);

	@SuppressWarnings("unchecked")
	public List<StatusAccessMode> getAllStatusAccessMode() {
		Criteria criteria = getSession().createCriteria(StatusAccessMode.class);
		criteria.add(Restrictions.eq("deleted", "N"));
		
		List<StatusAccessMode> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<StatusAccessMode> getStatusAccessModeByModeKey(Integer modeKey) {

		Criteria criteria = getSession().createCriteria(StatusAccessMode.class);
		criteria.add(Restrictions.eq("modeKey", modeKey));
		criteria.add(Restrictions.eq("deleted", "N"));

		List<StatusAccessMode> list = criteria.list();
		return list;
	}

	
	public List<StatusAccessMode> searchStatusAccessMode(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(StatusAccessMode.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyDateTime", lastModifiedDate));
		}
		List<StatusAccessMode> statusAccessModeList = criteria.list();

		return statusAccessModeList;
	}
	
	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(StatusAccessMode.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyDateTime", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	private Criteria getDefaultStatusAccessModeCriteria() {

		// default criteria for status
		Session currentSession = getSession();
		return currentSession.createCriteria(StatusAccessMode.class);
	}

}
