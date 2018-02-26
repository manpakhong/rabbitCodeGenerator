package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.RouteDef;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class PatrolScheduleDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PatrolScheduleDao.class);

	private String dto_ParentId = "parentId";
	private String dto_Deleted = "deleted";
	private String dto_LastModifyDateTime = "lastModifyDateTime";
	private String dto_ScheduleStartDate = "scheduleStartDate";
	private String dto_ScheduleEndDate = "scheduleEndDate";
	private String dto_SiteKey = "siteKey";
	private String dto_ScheduleKey = "scheduleKey";
	private String dto_ScheduleTime = "scheduleTime";
	private String dto_RouteDefKey = "routeDefKey";

	public int savePatrolSchedule(PatrolSchedule schedule) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(schedule);
		currentSession.flush();

		return schedule.getScheduleKey();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolSchedule> searchByParentId(int parentId) {
		Criteria criteria = getDefaultCriteria();

		// criteria = criteria.add(Restrictions.eq(dto_ParentId, parentId)).add(
		// Restrictions.eq(dto_Deleted, "N"));
		criteria = criteria.add(Restrictions.eq(dto_ParentId, parentId));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolSchedule> searchPatrolScheduleByLastModifyTime(Timestamp lastModifyTime, Integer userAccountKey, Integer siteKey) {
		Criteria criteria = getDefaultCriteria();

		if (lastModifyTime != null)
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifyTime));
		if (userAccountKey != null) {
			criteria.createAlias("scheduleAccount", "p").add(Restrictions.eq("p.accountKey", userAccountKey));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq(dto_SiteKey, siteKey));
		}
		
		return criteria.list();
	}

	public List<PatrolSchedule> searchByDate(int siteKey, Timestamp startDate, Timestamp endDate) {

		System.out.println("searchByDate Timestamp : " + startDate + "||" + startDate.getTime());
		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.le(dto_ScheduleStartDate, startDate))
				.add(Restrictions.eq(dto_Deleted, "N")).addOrder(Order.asc(dto_ScheduleTime));

		if (siteKey != -1) {
			criteria = criteria.add(Restrictions.eq(dto_SiteKey, siteKey));
		}

		return criteria.list();
	}

	public PatrolSchedule searchByScheduleKey(int scheduleKey) {
		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.eq(dto_ScheduleKey, scheduleKey));

		List<PatrolSchedule> rtn = criteria.list();
		if (!rtn.isEmpty())
			return rtn.get(0);

		return null;
	}

	public void updateLastAttendTime(int scheduleKey, Timestamp lastAttendTime) {

		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.eq(dto_ScheduleKey, scheduleKey));

		List<PatrolSchedule> rtn = criteria.list();
		if (!rtn.isEmpty()) {

			rtn.get(0).setLastAttendTime(lastAttendTime);

			Session currentSession = getSession();

			currentSession.saveOrUpdate(rtn.get(0));
			currentSession.flush();
		}
	}

	public List<PatrolSchedule> searchPatrolScheduleBySiteKey(int siteKey) {

		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.eq(dto_SiteKey, siteKey)).add(Restrictions.eq(dto_Deleted, "N"));

		return criteria.list();
	}

	public List<PatrolSchedule> searchPatrolScheduleAfter(int routeDefKey, Calendar date) {

		logger.debug("searchPatrolScheduleAfter : " + routeDefKey + "||" + date);

		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.eq(dto_RouteDefKey, routeDefKey))
				.add(Restrictions.gt(dto_ScheduleEndDate, new Timestamp(date.getTimeInMillis())))
				.add(Restrictions.eq(dto_Deleted, "N"));

		return criteria.list();
	}

	/**                 **/
	/** Other Functions **/
	/**                 **/

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolSchedule.class);

		return criteria;
	}

}
