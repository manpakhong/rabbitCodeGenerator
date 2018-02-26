package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.StatusFlow;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class StatusFlowDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(StatusFlowDao.class);

	@SuppressWarnings("unchecked")
	public List<StatusFlow> getAllStatusFlow() {
		List<StatusFlow> list = getDefaultStatusFlowCriteria().list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<StatusFlow> getStatus(Integer modeKey, String currentStatus) {

		Criteria criteria = getSession().createCriteria(StatusFlow.class);
		if (currentStatus != null)
			criteria.add(Restrictions.eq("id.statusId", currentStatus));
		
		
		criteria.add(Restrictions.eq("id.modeKey", modeKey));
		criteria.add(Restrictions.eq("deleted", "N"));
		

		List<StatusFlow> list = criteria.list();
		
		
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<StatusFlow> searchStatusFlow(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(StatusFlow.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<StatusFlow> statusFlowList = criteria.list();

		return statusFlowList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(StatusFlow.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	private Criteria getDefaultStatusFlowCriteria() {

		// default criteria for status
		Session currentSession = getSession();
		return currentSession.createCriteria(StatusFlow.class);
	}

}
