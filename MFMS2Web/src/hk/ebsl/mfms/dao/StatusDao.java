package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class StatusDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(StatusDao.class);

	@SuppressWarnings("unchecked")
	public List<Status> getAllStatus() {

		logger.debug("getAllStatus()");

		List<Status> list = getDefaultStatusCriteria().list();

		return list;
	}

	public List<Status> searchStatusByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Status.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<Status> statusList = criteria.list();

		return statusList;

	}
	
	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Status.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	

	@SuppressWarnings("unchecked")
	public String getStatusNameByStatusId(String statusId) {

		Criteria criteria = getDefaultStatusCriteria();
		criteria.add(Restrictions.eq("statusId", statusId));
		criteria.add(Restrictions.eq("deleted", "N"));

		List<Status> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0).getName();
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Status getStatus(String statusId) {

		Criteria criteria = getDefaultStatusCriteria();
		criteria.add(Restrictions.eq("statusId", statusId));
		criteria.add(Restrictions.eq("deleted", "N"));

		List<Status> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Integer getSequenceByStatus(StatusFlow statusFlow) {

		Criteria criteria = getDefaultStatusCriteria();
		criteria.add(Restrictions.eq("statusId", statusFlow.getId().getStatusId()));
		criteria.add(Restrictions.eq("deleted", "N"));

		List<Status> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0).getSequence();
		} else {
			return null;
		}
	}

	private Criteria getDefaultStatusCriteria() {

		// default criteria for status
		Session currentSession = getSession();
		return currentSession.createCriteria(Status.class);
	}

}
