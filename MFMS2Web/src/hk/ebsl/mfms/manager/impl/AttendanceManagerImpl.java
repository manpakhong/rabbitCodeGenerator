package hk.ebsl.mfms.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AttendanceDao;
import hk.ebsl.mfms.dto.Attendance;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AttendanceManager;

public class AttendanceManagerImpl implements AttendanceManager {
	public static final Logger logger = Logger
			.getLogger(PatrolResultManagerImpl.class);

	private AttendanceDao attendanceDao;
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void save(Attendance attendance) {
		attendanceDao.save(attendance);

	}
	public AttendanceDao getAttendanceDao() {
		return attendanceDao;
	}
	public void setAttendanceDao(AttendanceDao attendanceDao) {
		this.attendanceDao = attendanceDao;
	}

}
