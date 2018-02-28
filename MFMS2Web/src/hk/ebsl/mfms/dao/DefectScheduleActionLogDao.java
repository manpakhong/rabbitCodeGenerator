package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.DefectScheduleActionLog;

public class DefectScheduleActionLogDao extends BaseDao {

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

	public static final Logger logger = Logger.getLogger(DefectScheduleActionLogDao.class);

	@SuppressWarnings("unchecked")
	public List<DefectScheduleActionLog> searchDefectScheduleActionLog(Integer siteKey, Integer accountKey, 
			Timestamp from, Timestamp to) {

		Criteria criteria = getDefaultDefectScheduleActionLogDaoCriteria();

		if (accountKey != null) {
			criteria.add(Restrictions.eq("actionBy", accountKey));
		}

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (convertTimestampToString(from) != null && convertTimestampToString(to) != null) {

			criteria.add(Restrictions.between("actionDateTime", from, to));
		} else if (convertTimestampToString(from) != null && convertTimestampToString(to) == null) {
			criteria.add(Restrictions.gt("actionDateTime", from));
		} else if (

		convertTimestampToString(from) == null && convertTimestampToString(to) != null) {
			criteria.add(Restrictions.lt("actionDateTime", to));
		}  else 	criteria.setMaxResults(500);

		List<DefectScheduleActionLog> list = criteria.list();

		return list;

	}

	public Integer saveDefectScheduleActionLog(DefectScheduleActionLog defectScheduleActionLog) {
		Session currentSession = getSession();
		currentSession.saveOrUpdate(defectScheduleActionLog);
		currentSession.flush();

		return defectScheduleActionLog.getKey();
	}

	private Criteria getDefaultDefectScheduleActionLogDaoCriteria() {
		// default criteria for defect
		Session currentSession = getSession();
		return currentSession.createCriteria(DefectScheduleActionLog.class);
	}

}
