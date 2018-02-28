
package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DefectScheduleAccountDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(DefectScheduleAccountDao.class);

	public int saveDefectScheduleAccount(DefectScheduleAccount scheduleAccount) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(scheduleAccount);

		return scheduleAccount.getScheduleKey();
	}
	
	public void saveListOfDefectScheduleAccount(List<DefectScheduleAccount> dsaList){
		
		Session currentSession = getSession();
		
		for(DefectScheduleAccount dsa : dsaList){
			currentSession.saveOrUpdate(dsa);
		}
		
		currentSession.flush();
	}

	@SuppressWarnings("unchecked")
	public List<DefectScheduleAccount> searchScheduleAccount( Integer accountKey) {

		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq("accountKey", accountKey));

		criteria.add(Restrictions.eq("deleted", "N"));

		return criteria.list();
	}

	
	
	@SuppressWarnings("unchecked")
	public List<DefectScheduleAccount> getSiteScheduleAccount(Timestamp time, Integer siteKey, Integer accountKey) {

		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.gt("lastModifyTime", time));

		criteria.add(Restrictions.eq("siteKey", siteKey));
		
		criteria.add(Restrictions.eq("accountKey", accountKey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<DefectScheduleAccount> getScheduleAccount(Integer schedulekey) {

		Criteria criteria = getDefaultCriteria();

		if (schedulekey != null)
			criteria = criteria.add(Restrictions.eq("scheduleKey", schedulekey));

		criteria = criteria.add(Restrictions.eq("deleted", "N"));

		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DefectScheduleAccount> getScheduleAccountByScheduleKey(Integer schedulekey) {

		Criteria criteria = getDefaultCriteria();

		if (schedulekey != null)
			criteria = criteria.add(Restrictions.eq("scheduleKey", schedulekey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public DefectScheduleAccount getScheduleAccount(Integer schedulekey, Integer accountKey) {

		Criteria criteria = getDefaultCriteria();

		criteria = criteria.add(Restrictions.eq("scheduleKey", schedulekey));

		criteria = criteria.add(Restrictions.eq("accountKey", accountKey));

		criteria = criteria.add(Restrictions.eq("deleted", "N"));

		List<DefectScheduleAccount> list = criteria.list();

		return list.get(0);

	}
	
	
	@SuppressWarnings("unchecked")
	public List<DefectScheduleAccount> searchDefectScheduleAccount(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectScheduleAccount.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<DefectScheduleAccount> defectScheduleAccountList = criteria.list();
		
		return defectScheduleAccountList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectScheduleAccount.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(DefectScheduleAccount.class);

		return criteria;
	}

}
