package hk.ebsl.mfms.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import hk.ebsl.mfms.dto.AttendanceConfig;
import hk.ebsl.mfms.dto.Defect;

public class AttendanceConfigDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(AttendanceConfigDao.class);
	
	public List<AttendanceConfig> getAttendanceConfigListByAccountKey(Integer accountKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(AttendanceConfig.class);
		criteria.add(Restrictions.eq("accountKey", accountKey));
		criteria.addOrder(Order.desc("code"));

		return criteria.list();
	}
}
