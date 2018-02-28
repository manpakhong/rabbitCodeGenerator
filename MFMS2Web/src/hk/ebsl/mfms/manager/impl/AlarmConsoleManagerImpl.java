package hk.ebsl.mfms.manager.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountGroupDao;
import hk.ebsl.mfms.dao.AlarmConsoleDao;
import hk.ebsl.mfms.dto.AlarmConsole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AlarmConsoleManager;

public class AlarmConsoleManagerImpl implements AlarmConsoleManager {

	private AlarmConsoleDao alarmConsoleDao;
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AlarmConsole> getAllAlarm() {
		// TODO Auto-generated method stub
		return alarmConsoleDao.getAllAlarm();
	}

	public AlarmConsoleDao getAlarmConsoleDao() {
		return alarmConsoleDao;
	}

	public void setAlarmConsoleDao(AlarmConsoleDao alarmConsoleDao) {
		this.alarmConsoleDao = alarmConsoleDao;
	}
	
	

}
