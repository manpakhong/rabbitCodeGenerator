package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.report.template.object.DefectListReport;
import hk.ebsl.mfms.report.template.object.DefectStatusSummaryReport;

import java.sql.Timestamp;
import java.util.List;

public interface DefectManager {

	public List<Defect> getAllDefect() throws MFMSException;

	public int searchDefectCount(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer groupKey,
			Integer accountKey, List<Integer> privilegeLocationKey)
			throws MFMSException;

	public List<Defect> searchDefect(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer groupKey,
			Integer accountKey) throws MFMSException;

	public Integer saveDefect(Defect defect) throws MFMSException;

	public List<Integer> saveDefect(List<Defect> defectList) throws MFMSException;
	
	public String getNextDefectCode(Integer siteKey) throws MFMSException;

	public Defect getDefectByKey(Integer key) throws MFMSException;

	public Defect getDefectByCode(Integer key, String code, boolean fetch)
			throws MFMSException;

	public void deleteDefectByKey(Integer accountKey, Integer defectKey)
			throws MFMSException;

	public boolean hasLocation(Integer locationKey) throws MFMSException;

	public boolean hasCauseCode(Integer causeCodeKey) throws MFMSException;

	public boolean hasTool(Integer toolKey) throws MFMSException;

	public boolean hasEquipment(Integer equipmentKey) throws MFMSException;

	public List<Defect> getDefectByLastModifyDateAndSiteKey(Timestamp time,
			Integer siteKey, Integer accountKey) throws MFMSException;

	public List<Defect> getDefectByDelta(Timestamp time, Integer siteKey,
			Integer accountKey, Integer[] accountGroupKey) throws MFMSException;

	public int searchDefectListCount(Integer siteKey, Timestamp from,
			Timestamp to, String code, Integer accKey, Integer locationKey,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, Integer equipmentKey, Integer priority,
			String callFrom, String status, Integer accountKey, Integer groupKey)
			throws MFMSException;

	public List<DefectListReport> searchDefectList(Integer siteKey,
			Timestamp from, Timestamp to, String code, Integer accKey,
			Integer locationKey, Integer failureClassKey,
			Integer problemCodeKey, Integer causeCodeKey, Integer equipmentKey,
			Integer priority, String callFrom, String status, Integer accountKey, Integer groupKey)
			throws MFMSException;

	// used by scheduled email
	public List<Defect> searchExpiringDefect(Integer minutes)
			throws MFMSException;

	public List<Defect> searchDefectByTargetStartDate(
			Integer assignedAccountKey, Timestamp from, Timestamp to)
			throws MFMSException;

	public List<Defect> searchGroupDefectByTargetStartDate(
			Integer assignedGroupKey, Timestamp from, Timestamp to)
			throws MFMSException;

	public List<DefectStatusSummaryReport> searchDefectStatusSummary(
			Integer siteKey, Timestamp from, Timestamp to, Integer accountKey,
			String[] statusMap) throws MFMSException;

	public List<DefectStatusSummaryReport> searchDefectStatusSummary(
			Integer siteKey, Timestamp from, Timestamp to, Integer accountKey,
			String[] statusMap, Integer locationKey, Integer failureClassKey,
			Integer problemCodeKey, List<Integer> privilegeLocationKey)
			throws MFMSException;


	public List<Defect> searchDefectByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);
	
}
