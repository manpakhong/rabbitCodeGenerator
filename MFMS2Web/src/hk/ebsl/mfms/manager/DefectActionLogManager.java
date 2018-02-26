package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface DefectActionLogManager {
	public List<DefectActionLog> searchDefectActionLog(Integer siteKey, Integer accountKey, String code, Timestamp from, Timestamp to, Integer groupKey)
			throws MFMSException;

	public Integer saveDefectActionLog(DefectActionLog defectActionLog) throws MFMSException;

}
