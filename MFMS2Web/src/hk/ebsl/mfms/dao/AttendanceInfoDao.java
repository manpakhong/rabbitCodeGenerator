package hk.ebsl.mfms.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import hk.ebsl.mfms.dto.AttendanceInfo;

public class AttendanceInfoDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(AttendanceInfoDao.class);
	
	public void save(AttendanceInfo attendanceInfo) {
		Session currentSession = getSession();

		currentSession.saveOrUpdate(attendanceInfo);
	}
}
