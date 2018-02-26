package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import hk.ebsl.mfms.dao.PriorityDao;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PriorityManager;

public class PriorityManagerImpl implements PriorityManager {

	public static final Logger logger = Logger.getLogger(PriorityManagerImpl.class);

	private PriorityDao priorityDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Priority> getAllPriority() throws MFMSException {

		List<Priority> list = priorityDao.getAllPriority();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Priority> searchPriorityByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		
		logger.debug("searchPriority()[" + lastModifiedDate + "]");

		List<Priority> priorityList = priorityDao.searchPriority(lastModifiedDate, offset, maxResults);

		return priorityList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate) {

		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = priorityDao.theCountOfSearchResult(lastModifiedDate);

		return theCountoftotalResult;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Float getResponseTime(Integer num) throws MFMSException {

		return priorityDao.getResponseTime(num);
	}

	public PriorityDao getPriorityDao() {
		return priorityDao;
	}

	public void setPriorityDao(PriorityDao priorityDao) {
		this.priorityDao = priorityDao;
	}
}
