package hk.ebsl.mfms.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AttendanceConfigDao;
import hk.ebsl.mfms.dto.AttendanceConfig;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AttendanceConfigManager;

public class AttendanceConfigManagerImpl implements AttendanceConfigManager {
	public static final Logger logger = Logger
			.getLogger(PatrolResultManagerImpl.class);

	private AttendanceConfigDao attendanceConfigDao;
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public List<AttendanceConfig> getAttendanceConfigListByAccountKey(Integer accountKey) {
		return attendanceConfigDao.getAttendanceConfigListByAccountKey(accountKey);

	}
	public AttendanceConfigDao getAttendanceConfigDao() {
		return attendanceConfigDao;
	}
	public void setAttendanceConfigDao(AttendanceConfigDao attendanceConfigDao) {
		this.attendanceConfigDao = attendanceConfigDao;
	}


}
