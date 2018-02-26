package hk.ebsl.mfms.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.PatrolRouteActionLogDao;
import hk.ebsl.mfms.dto.PatrolRouteActionLog;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PatrolRouteActionLogManager;


public class PatrolRouteActionLogManagerImpl implements PatrolRouteActionLogManager {
	
	public static final Logger logger = Logger.getLogger(PatrolRouteActionLogManagerImpl.class);
	
	private PatrolRouteActionLogDao patrolRouteActionLogDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void savePatrolRouteActionLog(PatrolRouteActionLog patrolRouteActionLog) throws MFMSException {
		
		patrolRouteActionLogDao.savePatrolRouteActionLog(patrolRouteActionLog);
	}

	public PatrolRouteActionLogDao getPatrolRouteActionLogDao() {
		return patrolRouteActionLogDao;
	}

	public void setPatrolRouteActionLogDao(PatrolRouteActionLogDao patrolRouteActionLogDao) {
		this.patrolRouteActionLogDao = patrolRouteActionLogDao;
	}
	
	
	
	
}
