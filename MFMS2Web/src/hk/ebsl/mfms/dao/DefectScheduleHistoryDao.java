
package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.dto.RouteDef;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DefectScheduleHistoryDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(DefectScheduleHistoryDao.class);

	public int saveDefectScheduleHistory(DefectScheduleHistory scheduleHistory) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(scheduleHistory);

		return scheduleHistory.getKey();
		
	}
	
	public void saveDefectScheduleHistory_1(DefectScheduleHistory dsh) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(dsh);

		currentSession.flush();
		
	}

	@SuppressWarnings("unchecked")
	public List<DefectScheduleHistory> getSiteScheduleHistory(Integer siteKey, Timestamp lastFinishDateTime) {

		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.gt("siteKey", siteKey));

		criteria.add(Restrictions.gt("finishDateTime", lastFinishDateTime));

		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DefectScheduleHistory> getDefectScheduleHistoryByDefectScheduleKey(Integer key) {

		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq("defectScheduleKey", key));

		return criteria.list();
	}
	
	public DefectScheduleHistory getByKey(Integer key){
		
		Criteria criteria = getDefaultCriteria();
		criteria.add(Restrictions.eq("key", key));
		
		List<DefectScheduleHistory> list = criteria.list();
		
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
		
	}
	
	public Integer searchHistoryResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectScheduleHistory.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	public List<DefectScheduleHistory> searchAllHistoryByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectScheduleHistory.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<DefectScheduleHistory> list = criteria.list();
		
		return list;
	}
	
	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectScheduleHistory.class);

		return criteria;
	}

}
