package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class PriorityDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PriorityDao.class);

	public List<Priority> getAllPriority() {

		List<Priority> list = getDefaultPriorityCriteria().list();

		return list;
	}

	public Float getResponseTime(Integer num) {

		Criteria criteria = getDefaultPriorityCriteria();

		criteria.add(Restrictions.eq("priority", num));

		List<Priority> list = criteria.list();

		return list.get(0).getResponseTime();
	}
	
	@SuppressWarnings("unchecked")
	public List<Priority> searchPriority(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Priority.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<Priority> priorityList = criteria.list();

		return priorityList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Priority.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	

	private Criteria getDefaultPriorityCriteria() {

		// default criteria for priority
		Session currentSession = getSession();
		return currentSession.createCriteria(Priority.class);
	}

}
