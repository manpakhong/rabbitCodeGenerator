package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.PatrolRouteActionLog;
import hk.ebsl.mfms.exception.MFMSException;

public interface PatrolRouteActionLogManager {

	public void savePatrolRouteActionLog(PatrolRouteActionLog patrolRouteActionLog) throws MFMSException;
}
