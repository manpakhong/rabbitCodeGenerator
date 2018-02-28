package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dao.PatrolResultDao;
import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PatrolResultManager;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class PatrolResultManagerImpl implements PatrolResultManager {

	public static final Logger logger = Logger
			.getLogger(PatrolResultManagerImpl.class);

	private PatrolResultDao patrolResultDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> search(int siteKey,String type, Timestamp startDate,
			Timestamp endDate, Integer routeDefKey, Integer patrolStaff) {

		List<PatrolResult> list = patrolResultDao.search(siteKey, type, startDate,
				endDate, routeDefKey, patrolStaff);

		for (PatrolResult result : list) {
			result.getRouteDef().getRouteLocation().get(0).getLocation()
					.getCode();
			result.getCorrectionLocation().getCode();
			// result.getLocation();

			if (result.getPersonAttended() > 0) {
				result.getPerson().getKey();
			}
		}

		return list;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int savePatrolResult(PatrolResult pr) {

		return patrolResultDao.save(pr);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> getIncompleteProgress(Integer userAccountKey) {
		// TODO Auto-generated method stub

		return this.patrolResultDao.getIncompleteProgress(userAccountKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> getInProgressResult() {

		return this.patrolResultDao.getInProgressResult();

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> getInProgressResultByRouteDefKey(int routeDefKey) {

		List<PatrolResult> list = patrolResultDao
				.getInProgressResultByRouteDefKey(routeDefKey);

		for (PatrolResult tmp : list) {
			tmp.getCorrectionLocation().getName().length();
			tmp.getPerson().getName().length();
			tmp.getRouteDef().getRouteLocation().get(0).getRemark().length();
		}

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Integer> getDistinctInProgressResult(int siteKey) {
		return this.patrolResultDao.getDistinctInProgressResult(siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public PatrolResult getPatrolResultByKey(int patrolResultKey) {
		// TODO Auto-generated method stub

		List<PatrolResult> list = patrolResultDao
				.getPatrolResult(patrolResultKey);

		for (PatrolResult pr : list) {
			pr.getPerson().getName().length();
			pr.getCorrectionLocation().getName().length();
		}

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> getInProgressResultByScheduleKey(int scheduleKey) {
		// TODO Auto-generated method stub

		return patrolResultDao.getInProgressResultByScheduleKey(scheduleKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int countLocationByGroupNum(int groupNum) {
		// TODO Auto-generated method stub
		
	
		return patrolResultDao.countLocationByGroupNum(groupNum);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> searchPatrolResultByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults){
		
		logger.debug("searchPatrolResult()[" + lastModifiedDate + "]");
		
		List<PatrolResult> patrolResultList = patrolResultDao.searchPatrolResult(lastModifiedDate, offset, maxResults);
		
		return patrolResultList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchPatrolResultCount(Timestamp lastModifiedDate){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = patrolResultDao.theCountOfSearchResult(lastModifiedDate);
		
		return theCountoftotalResult;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolResult> checkIfDuplicatedResult(int routeDefKey, int locationKey, int createBy,
			Timestamp createTimestamp) {
		// TODO Auto-generated method stub
		
		
		return patrolResultDao.checkIfDuplicatedResult(routeDefKey,  locationKey,  createBy,
				 createTimestamp);
		
	}

	
	/**                 **/
	/** Other Functions **/
	/**                 **/

	public PatrolResultDao getPatrolResultDao() {
		return patrolResultDao;
	}

	public void setPatrolResultDao(PatrolResultDao patrolResultDao) {
		this.patrolResultDao = patrolResultDao;
	}

	
}
