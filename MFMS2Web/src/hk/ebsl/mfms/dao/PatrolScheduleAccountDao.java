package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class PatrolScheduleAccountDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PatrolScheduleAccountDao.class);

	private String dto_RouteDefKey = "routeDefKey";
	private String dto_AccountKey = "accountKey";
	private String dto_PatrolScheduleKey = "scheduleKey";
	private String dto_LastModifyTime = "lastModifyTime";
	private String dto_LastModifyTimeForSync = "lastModifyTimeForSync";

	private String dto_tbl_PatrolSchedule = "patrolSchedule";
	private String dto_PatrolSchedule_Key = "scheduleKey";
	private String dto_PatrolSchedule_Deleted = "deleted";
	private String dto_PatrolSchedule_SiteKey = "siteKey";

	public int savePatrolSchedule(PatrolScheduleAccount scheduleAccount) {

		Session currentSession = getSession();

		currentSession.saveOrUpdate(scheduleAccount);
		currentSession.flush();

		return scheduleAccount.getScheduleKey();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolScheduleAccount> search(int siteKey, int routeDefKey, int accountKey) {
		Criteria criteria = getDefaultCriteria();

		if (accountKey != -1)
			criteria = criteria.add(Restrictions.eq(dto_AccountKey, accountKey));

		if (routeDefKey != -1)
			criteria = criteria.createAlias(dto_tbl_PatrolSchedule, "ps")
					.add(Restrictions.eq("ps." + dto_RouteDefKey, routeDefKey));

		criteria = criteria.createAlias(dto_tbl_PatrolSchedule, "ps")
				.add(Restrictions.eq("ps." + dto_PatrolSchedule_Deleted, "N"))
				.add(Restrictions.eq("ps." + dto_PatrolSchedule_SiteKey, siteKey));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolScheduleAccount> search(int schedulekey) {
		Criteria criteria = getDefaultCriteria();

		if (schedulekey != -1){
			criteria.add(Restrictions.eq(dto_PatrolSchedule_Key, schedulekey))
					.add(Restrictions.eq("deleted", "N"));
		}
		
		criteria.createAlias(dto_tbl_PatrolSchedule, "ps")
				.add(Restrictions.eq("ps." + dto_PatrolSchedule_Deleted, "N"));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PatrolScheduleAccount> searchByScheduleKeyAndAccountKey(int schedulekey, int accountKey) {
		Criteria criteria = getDefaultCriteria();
		criteria.add(Restrictions.eq(dto_PatrolScheduleKey, schedulekey))
				.add(Restrictions.eq(dto_AccountKey, accountKey));

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public List<PatrolScheduleAccount> searchByAccountKeyAndLastModifyDate(Timestamp lastModifyDate, int offset,
			int limit, Integer userAccountKey, Integer siteKey) {

		Criteria criteria = getDefaultCriteria();
		if(userAccountKey != null)
			criteria = criteria.add(Restrictions.eq(dto_AccountKey, userAccountKey));
		if (lastModifyDate != null)
			criteria.add(Restrictions.gt(dto_LastModifyTimeForSync, lastModifyDate));
		if(siteKey != null){
			criteria = criteria.createAlias(dto_tbl_PatrolSchedule, "ps")
					.add(Restrictions.eq("ps." + "siteKey", siteKey));
		}

		
		criteria.setFirstResult(offset);
		criteria.setMaxResults(limit);

		return criteria.list();

	}

	@SuppressWarnings("unchecked")
	public int getCountByLastModifyDateAndAccount(Timestamp lastModifyDate, Integer userAccountKey, Integer siteKey) {
		Criteria criteria = getDefaultCriteria();
		
		if(userAccountKey != null)
			criteria = criteria.add(Restrictions.eq(dto_AccountKey, userAccountKey));

		if (lastModifyDate != null)
			criteria.add(Restrictions.gt(dto_LastModifyTimeForSync, lastModifyDate));
		if(siteKey != null){
			criteria = criteria.createAlias(dto_tbl_PatrolSchedule, "ps")
					.add(Restrictions.eq("ps." + "siteKey", siteKey));
		}

		return ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

	}

	// public List<PatrolScheduleAccount> searchByDate(Timestamp startDate,
	// Timestamp endDate){
	// Criteria criteria = getDefaultCriteria();
	// criteria = criteria.add(Restrictions.eq(
	//
	//
	// }

	/**                 **/
	/** Other Functions **/
	/**                 **/

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolScheduleAccount.class);

		return criteria;
	}

}
