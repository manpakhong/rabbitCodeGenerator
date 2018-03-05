package hk.ebsl.mfms.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AttendanceInfoDao;
import hk.ebsl.mfms.dto.AttendanceInfo;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AttendanceInfoManager;

public class AttendanceInfoManagerImpl implements AttendanceInfoManager {
	public static final Logger logger = Logger
			.getLogger(PatrolResultManagerImpl.class);

	private AttendanceInfoDao attendanceInfoDao;
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void save(AttendanceInfo attendanceInfo) {
		attendanceInfoDao.save(attendanceInfo);

	}
	public AttendanceInfoDao getAttendanceInfoDao() {
		return attendanceInfoDao;
	}
	public void setAttendanceInfoDao(AttendanceInfoDao attendanceInfoDao) {
		this.attendanceInfoDao = attendanceInfoDao;
	}


}
