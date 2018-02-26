package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.DefectScheduleActionLog;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface DefectScheduleActionLogManager {
	public List<DefectScheduleActionLog> searchDefectScheduleActionLog(Integer siteKey, Integer accountKey, Timestamp from, Timestamp to, Integer groupKey) throws MFMSException;

	public Integer saveDefectScheduleActionLog(DefectScheduleActionLog defectScheduleActionLog) throws MFMSException;

}
