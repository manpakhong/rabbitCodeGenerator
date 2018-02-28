package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.dto.RouteLocation;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class PatrolResultDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PatrolResultDao.class);

	private String dto_SeqNum = "seqNum";
	private String dto_CreateDateTime = "createDateTime";
	private String dto_RouteDefKey = "routeDefKey";
	private String dto_PersonAttended = "personAttended";
	private String dto_Completed = "completed";
	private String dto_CreateBy = "createBy";
	private String dto_ResultKey = "resultKey";
	private String dto_PatrolScheduleKey = "patrolScheduleKey";
	private String dto_SiteKey = "siteKey";
	private String dto_GroupNum = "groupNum";
	private String dto_LocationKey = "locationKey";


	// private String dto_StartDate = "";
	// private String dto_EndDate = "";

	@SuppressWarnings("unchecked")
	public List<PatrolResult> search(int siteKey, String type, Timestamp startDate,
			Timestamp endDate, Integer routeDefKey, Integer patrolStaff) {

		Criteria criteria = getDefaultCriteria();
		criteria = criteria.add(Restrictions.eq(dto_SiteKey,
				siteKey));

		if (startDate != null) {
			criteria = criteria.add(Restrictions.ge(dto_CreateDateTime,
					startDate));
		}

		if (endDate != null) {
			criteria = criteria.add(Restrictions
					.le(dto_CreateDateTime, endDate));
		}
		if (routeDefKey != -1) {
			criteria = criteria.add(Restrictions.eq(dto_RouteDefKey,
					routeDefKey));
		}

		if (patrolStaff != -1) {
			criteria = criteria.add(Restrictions.eq(dto_CreateBy,
					patrolStaff));
		}

		// criteria =
		// criteria.addOrder(Order.asc(dto_SeqNum)).addOrder(Order.asc(dto_CreateDateTime));

		criteria.addOrder(Order.asc(dto_ResultKey));
		
		return criteria.list();
	}

	public int save(PatrolResult pr) {
		Session currentSession = getSession();

		currentSession.saveOrUpdate(pr);
		currentSession.flush();

		return pr.getResultKey();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolResult> getIncompleteProgress(Integer userAccountKey) {
		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq(dto_Completed, "N")).add(
				Restrictions.eq(dto_CreateBy, userAccountKey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolResult> getInProgressResult() {
		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq(dto_Completed, "N"));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getDistinctInProgressResult(int siteKey) {
		Criteria criteria = getDefaultCriteria();

		criteria.setProjection(
				Projections.distinct(Projections.property(dto_RouteDefKey)))
				.add(Restrictions.eq(dto_Completed, "N"))
				.add(Restrictions.eq(dto_SiteKey, siteKey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolResult> getPatrolResult(int patrolResultKey) {
		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq(dto_ResultKey, patrolResultKey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolResult> getInProgressResultByRouteDefKey(int routeDefKey) {
		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq(dto_Completed, "N")).add(
				Restrictions.eq(dto_RouteDefKey, routeDefKey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolResult> getInProgressResultByScheduleKey(int scheduleKey) {
		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq(dto_Completed, "N")).add(
				Restrictions.eq(dto_PatrolScheduleKey, scheduleKey));

		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public int countLocationByGroupNum(int groupNum){
		
		Criteria criteria = getDefaultCriteria();

		criteria.add(Restrictions.eq(dto_GroupNum, groupNum));
		
		logger.debug("Row Count : "+criteria.list().size() + "|| Group Num :"+groupNum);
		
		return ((Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PatrolResult> searchPatrolResult(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolResult.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyDateTime", lastModifiedDate));
		}
		List<PatrolResult> patrolResultList = criteria.list();

		return patrolResultList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolResult.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyDateTime", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	public List<PatrolResult> checkIfDuplicatedResult(int routeDefKey, int locationKey, int createBy,
			Timestamp createTimestamp){
		
		
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolResult.class);

		Calendar minTime = Calendar.getInstance();
		minTime.setTimeInMillis(createTimestamp.getTime());
		minTime.set(Calendar.MILLISECOND, 0);
		
		Calendar maxTime = Calendar.getInstance();
		maxTime.setTimeInMillis(createTimestamp.getTime());
		maxTime.set(Calendar.MILLISECOND, 0);
		maxTime.add(Calendar.SECOND, 1);
		
		
		criteria.add(Restrictions.eq(dto_RouteDefKey, routeDefKey))
				.add(Restrictions.eq(dto_LocationKey, locationKey))
				.add(Restrictions.eq(dto_CreateBy, createBy))
				.add(Restrictions.ge(dto_CreateDateTime, new Timestamp(minTime.getTimeInMillis())))
				.add(Restrictions.le(dto_CreateDateTime,  new Timestamp(maxTime.getTimeInMillis())));
		
		logger.debug("routeDefKey : "+routeDefKey + "|| locationKey :"+locationKey + "||createBy:"+createBy + "||createTimestamp:"+createTimestamp.getTime());
		
		List<PatrolResult> list = criteria.list();
		logger.debug("criteria.list():"+list.size());
		
		
		if(list != null && list.size()>0){
			return list;
		}else{
			return null;
		}
		
	}

	/**                 **/
	/** Other Functions **/
	/**                 **/

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolResult.class).add(Restrictions.eq("deleted", "N"));

		return criteria;
	}

}
