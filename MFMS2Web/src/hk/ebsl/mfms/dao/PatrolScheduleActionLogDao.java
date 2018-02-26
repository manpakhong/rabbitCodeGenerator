package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import hk.ebsl.mfms.dto.PatrolScheduleActionLog;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.PatrolSchedule;

public class PatrolScheduleActionLogDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PatrolScheduleActionLogDao.class);

	public String convertTimestampToString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = sdf.format(ts);
			if (timeString.length() > 0)
				return timeString;
			else
				return null;
		} else
			return null;
	}

	@SuppressWarnings("unchecked")
	public List<PatrolScheduleActionLog> searchPatrolScheduleActionLog(Integer siteKey, Integer accountKey,
			Integer patrolscheduleKey, Timestamp from, Timestamp to) {

		Criteria criteria = getDefaultPatrolScheduleActionLogCriteria();

		if (accountKey != null) {
			// case insensitive
			criteria.add(Restrictions.eq("actionBy", accountKey));
		}

		if (siteKey != null) {
			// case insensitive
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (convertTimestampToString(from) != null && convertTimestampToString(to) != null) {

			criteria.add(Restrictions.between("actionDateTime", from, to));
		} else if (convertTimestampToString(from) != null && convertTimestampToString(to) == null) {
			criteria.add(Restrictions.gt("actionDateTime", from));
		} else if (

		convertTimestampToString(from) == null && convertTimestampToString(to) != null) {
			criteria.add(Restrictions.lt("actionDateTime", to));
		}

		List<PatrolScheduleActionLog> list = criteria.list();

		return list;

	}

	public Integer savePatrolScheduleActionLog(PatrolScheduleActionLog patrolScheduleActionLog) {
		Session currentSession = getSession();
		currentSession.saveOrUpdate(patrolScheduleActionLog);

		return patrolScheduleActionLog.getKey();
	}

	private Criteria getDefaultPatrolScheduleActionLogCriteria() {
		// default criteria for defect
		Session currentSession = getSession();
		return currentSession.createCriteria(PatrolScheduleActionLog.class);
	}

}
