package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.PatrolScheduleActionLogDao;
import hk.ebsl.mfms.dao.PatrolScheduleDao;
import hk.ebsl.mfms.dto.PatrolScheduleActionLog;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PatrolScheduleActionLogManager;

public class PatrolScheduleActionLogManagerImpl implements PatrolScheduleActionLogManager {

	public static final Logger logger = Logger.getLogger(PatrolScheduleActionLogManagerImpl.class);

	private PatrolScheduleActionLogDao patrolScheduleActionLogDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolScheduleActionLog> searchPatrolScheduleActionLog(Integer siteKey, Integer accountKey,
			Integer patrolScheduleKey, Timestamp from, Timestamp to) throws MFMSException {

		List<PatrolScheduleActionLog> list = patrolScheduleActionLogDao.searchPatrolScheduleActionLog(siteKey,
				accountKey, patrolScheduleKey, from, to);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer savePatrolScheduleActionLog(PatrolScheduleActionLog patrolScheduleActionLog) throws MFMSException {
		// TODO Auto-generated method stub
		return patrolScheduleActionLogDao.savePatrolScheduleActionLog(patrolScheduleActionLog);

	}

	public void test() {

	}

	public PatrolScheduleActionLogDao getPatrolScheduleActionLogDao() {
		return patrolScheduleActionLogDao;
	}

	public void setPatrolScheduleActionLogDao(PatrolScheduleActionLogDao patrolScheduleActionLogDao) {
		this.patrolScheduleActionLogDao = patrolScheduleActionLogDao;
	}
}
