package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import hk.ebsl.mfms.dto.DefectActionLog;

public class DefectActionLogDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(DefectActionLogDao.class);

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
	public List<DefectActionLog> searchDefect(Integer siteKey, Integer accountKey, String code, Timestamp from,
			Timestamp to) {
		Criteria criteria = getDefaultDefectActionLogCriteria();
	
		if (code != null) {
			if (!code.isEmpty()) {
				// case insensitive
				criteria.add(Restrictions.eq("code", code));
			}
		}

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
		} else if (convertTimestampToString(from) == null && convertTimestampToString(to) != null) {
			criteria.add(Restrictions.lt("actionDateTime", to));
		}  else criteria.setMaxResults(500);

		List<DefectActionLog> list = criteria.list();

		return list;

	}

	public Integer saveDefectActionLog(DefectActionLog defectActionLog) {
		Session currentSession = getSession();
		currentSession.saveOrUpdate(defectActionLog);

		return defectActionLog.getKey();
	}

	private Criteria getDefaultDefectActionLogCriteria() {
		// default criteria for defect
		Session currentSession = getSession();
		return currentSession.createCriteria(DefectActionLog.class);
	}

}
