package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.Site;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DefectScheduleDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(DefectScheduleDao.class);

	public int saveDefectSchedule(DefectSchedule schedule) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(schedule);
		currentSession.flush();
		return schedule.getScheduleKey();
	}
	
	public void saveListOfDefectSchedules(List<DefectSchedule> dsList){
		Session currentSession = getSession();
		
		for(DefectSchedule ds : dsList){
			currentSession.saveOrUpdate(ds);
		}
		currentSession.flush();
	}

	@SuppressWarnings("unchecked")
	public List<DefectSchedule> getSchedule(Integer siteKey) {
		Criteria criteria = getDefaultCriteria();
		criteria.add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("deleted", "N"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<DefectSchedule> searchByParentId(Integer parentId) {
		Criteria criteria = getDefaultCriteria();

		criteria = criteria.add(Restrictions.eq("parentId", parentId));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<DefectSchedule> searchByParentId(Integer parentId, Timestamp time) {
		Criteria criteria = getDefaultCriteria();

		criteria = criteria.add(Restrictions.eq("parentId", parentId));

		criteria.add(Restrictions.gt("lastModifyDateTime", time));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<DefectSchedule> getSchedule(Integer siteKey, Integer parentId) {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectSchedule.class);

		criteria.add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("parentId", parentId))
				.add(Restrictions.eq("deleted", "N"));

		currentSession.flush();

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public DefectSchedule getScheduleByKey(Integer key) {
		Criteria criteria = getDefaultCriteria();
		criteria.add(Restrictions.eq("scheduleKey", key));
		List<DefectSchedule> list = criteria.list();

		if(list!= null && !list.isEmpty())
			return list.get(0);
		else
			return null;
		
	}

	@SuppressWarnings("unchecked")
	public DefectSchedule getParentSchedule(Integer parentId) {
		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.eq("scheduleKey", parentId)).add(Restrictions.eq("deleted", "N"));
		List<DefectSchedule> list = criteria.list();
		if (list.size() == 0)
			return list.get(0);
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public boolean hasEquipment(Integer equipmentKey) {

		Criteria criteria = getDefaultCriteria();

		if (equipmentKey != null) {
			criteria.add(Restrictions.eq("equipmentKey", equipmentKey)).add(Restrictions.eq("deleted", "N"));
		}

		List<Defect> list = criteria.list();

		if (list.size() > 0)
			return true;
		else
			return false;

	}
	
	
	@SuppressWarnings("unchecked")
	public List<DefectSchedule> searchDefectSchedule(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectSchedule.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<DefectSchedule> defectScheduleList = criteria.list();

		return defectScheduleList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectSchedule.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectSchedule.class);

		return criteria;
	}

}
