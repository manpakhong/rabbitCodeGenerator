package hk.ebsl.mfms.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import hk.ebsl.mfms.dto.PatrolRouteActionLog;

public class PatrolRouteActionLogDao extends BaseDao{
	
	public static final Logger logger = Logger.getLogger(PatrolRouteActionLogDao.class);
	
	public void savePatrolRouteActionLog(PatrolRouteActionLog patrolRouteActionLog) {
		
		Session currentSession = getSession();
		currentSession.saveOrUpdate(patrolRouteActionLog);
		currentSession.flush();

	}
	
	
}
