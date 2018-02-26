package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public interface PatrolScheduleManager {

	void savePatrolSchedule(List<PatrolSchedule> patrolScheduleList,
			List<PatrolScheduleAccount> patrolScheduleAccountList);

	List<PatrolScheduleAccount> searchPatrolSchedule(int siteKey, List<Integer> route, List<Integer> account);

	List<PatrolSchedule> searchPatrolScheduleBySite(int siteKey);

	List<PatrolScheduleAccount> searchPatrolScheduleAccountByScheduleKey(int scheduleKey);

	List<PatrolSchedule> searchPatrolScheduleChilden(int parentId);

	void updatePatrolScheduleAccount(List<PatrolScheduleAccount> list);

	List<PatrolScheduleAccount> searchPatrolScheduleAccount(Integer scheduleKey, List<Integer> accountKey);

	void insertNewPatrolScheduleAccount(List<PatrolScheduleAccount> list);

	List<PatrolSchedule> searchPatrolScheduleByLastModifyTime(Timestamp lastModifyTime, Integer userAccountKey, Integer siteKey);

	int getPatrolScheduleCountByLastModifyTime(Timestamp lastModifyTime, Integer userAccountKey);

	List<PatrolSchedule> searchPatrolScheduleDate(int siteKey, Timestamp startDate, Timestamp endDate);

	void updatePatrolScheduleLastAttendTime(int scheduleKey, Timestamp lastAttendTime);

	List<PatrolSchedule> searchPatrolScheduleAfter(int routeDefKey, Calendar date);

	Integer getPatrolScheduleAccountCount(Timestamp lastModifyDate, Integer accountKey, Integer siteKey);

	List<PatrolScheduleAccount> searchPatrolScheduleAccountByLastModifyTime(Timestamp lastModifyDate, Integer offest,
			Integer limit, Integer accountKey, Integer siteKey);

	PatrolSchedule searchPatrolScheduleByKey(int scheduleKey);
	
}
