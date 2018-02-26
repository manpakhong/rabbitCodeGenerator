package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.RouteLocation;

import java.sql.Timestamp;
import java.util.List;

public interface PatrolResultManager {

	List<PatrolResult> search(int siteKey, String type, Timestamp startDate,
			Timestamp endDate, Integer routeDefKey, Integer patrolStaff);

	int savePatrolResult(PatrolResult pr);
	
	List<PatrolResult> getIncompleteProgress(Integer userAccountKey);

	List<PatrolResult> getInProgressResult();
	List<PatrolResult> getInProgressResultByScheduleKey(int scheduleKey);
	
	List<Integer> getDistinctInProgressResult(int siteKey);

	List<PatrolResult> getInProgressResultByRouteDefKey(int routeDefKey);
	
	PatrolResult getPatrolResultByKey(int patrolResultKey);
	
	int countLocationByGroupNum(int groupNum);
	
	
	public List<PatrolResult> searchPatrolResultByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchPatrolResultCount(Timestamp lastModifiedDate);
	
	public List<PatrolResult> checkIfDuplicatedResult( int routeDefKey, int locationKey, int createBy, Timestamp createTimestamp);
}
