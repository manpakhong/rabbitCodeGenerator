package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.PatrolScheduleActionLog;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface PatrolScheduleActionLogManager {
	public List<PatrolScheduleActionLog> searchPatrolScheduleActionLog(Integer siteKey, Integer accountKey,
			Integer patrolScheduleKey, Timestamp from, Timestamp to) throws MFMSException;

	public Integer savePatrolScheduleActionLog(PatrolScheduleActionLog patrolScheduleActionLog) throws MFMSException;

}
