package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.exception.MFMSException;

public interface DefectScheduleManager {

	Integer saveDefectSchedule(DefectSchedule defectSchedule, List<DefectScheduleAccount> defectScheduleAccountList);

	void saveDefectSchedule(List<DefectSchedule> list);

	List<DefectSchedule> getSchedule(Integer siteKey);

	DefectSchedule getScheduleByKey(Integer key);

	DefectSchedule getParentSchedule(Integer parentId);

	List<DefectScheduleAccount> getScheduleAccount(Integer schedulekey);
	
	List<DefectScheduleAccount> getAllScheduleAccountByScheduleKey(Integer schedulekey);

	List<DefectScheduleAccount> getSiteScheduleAccount(Timestamp time, Integer siteKey, Integer accountKey);

	DefectScheduleAccount getScheduleAccount(Integer schedulekey, Integer accountKey);

	void deleteDefectSchedule(Integer key, Integer accountKey);
	
	void deleteListOfChildrenDefectSchedule(List<DefectSchedule> dsList, Integer accountKey);

	void deleteDefectScheduleAccount(Integer key, Integer LastModifyBy);
	
	void deleteListOfDefectScheduleAccountFromDefectScheduleList(List<DefectSchedule> dsList, Integer accountKey);
	
	void deleteListOfDefectScheduleAccount(List<DefectScheduleAccount> dsaList, Integer accountKey);
	
	boolean hasEquipment(Integer equipmentKey) throws MFMSException;

	List<DefectSchedule> getSchedule(Integer siteKey, Integer parentId);

	List<DefectSchedule> searchDefectScheduleChilden(Integer parentId);
	
	List<DefectSchedule> searchDefectScheduleChilden(Integer parentId, Timestamp lastModifyDateTime);
	
	List<DefectSchedule> searchAccountDefectSchedule(Integer accountKey, Timestamp startDate, Timestamp endDate) throws MFMSException;
	
	int createDefectScheduleHistory(DefectScheduleHistory dsh) throws MFMSException;
	
	List<DefectScheduleHistory> getSiteScheduleHistory(Integer siteKey, Timestamp lastFinishDateTime) throws MFMSException;

	List<DefectScheduleHistory> getDefectScheduleHistoryByDefectScheduleKey(Integer key) throws MFMSException;
	
	public List<DefectSchedule> searchDefectScheduleByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);
	
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);
	
	public List<DefectScheduleAccount> searchDefectScheduleAccountByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);
	
	public Integer searchResultCountByDefectScheduleAccount(Timestamp lastModifiedDate, Integer siteKey);

	public DefectScheduleHistory getDefectScheduleHistoryByKey(Integer key);
	
	public void saveDefectScheduleHistory(DefectScheduleHistory dsh) throws MFMSException;

	public List<DefectScheduleHistory> searchAllHistoryByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchHistoryResultCount(Timestamp lastModifiedDate, Integer siteKey);

}
