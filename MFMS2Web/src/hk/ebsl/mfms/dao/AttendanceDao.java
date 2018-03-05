package hk.ebsl.mfms.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import hk.ebsl.mfms.dto.Attendance;
import hk.ebsl.mfms.dto.PunchCard;

public class AttendanceDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(AttendanceDao.class);
	
	public void save(Attendance attendance) {
		Session currentSession = getSession();

			currentSession.saveOrUpdate(attendance);
	}
}
