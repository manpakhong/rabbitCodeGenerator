package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.StatusFlowDao;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.StatusFlowManager;

public class StatusFlowManagerImpl implements StatusFlowManager {

	public static final Logger logger = Logger.getLogger(StatusFlowManagerImpl.class);
	
	private StatusFlowDao statusFlowDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<StatusFlow> getAllStatusFlow() throws MFMSException {
		
		logger.debug("getAllStatusFlow()");
		
		List<StatusFlow> list = statusFlowDao.getAllStatusFlow();
		
		return list;
	}
	

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<StatusFlow> getStatus(Integer modeKey, String currentStatusId) throws MFMSException {
		
		List<StatusFlow> list = statusFlowDao.getStatus(modeKey, currentStatusId);
		
		return list;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<StatusFlow> searchStatusFlowByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults){
		
		logger.debug("searchStatusFlow()[" + lastModifiedDate + "]");
		
		List<StatusFlow> statusFlowList = statusFlowDao.searchStatusFlow(lastModifiedDate, offset, maxResults);
		
		return statusFlowList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = statusFlowDao.theCountOfSearchResult(lastModifiedDate);
		
		return theCountoftotalResult;
	}
	
	
	public void test() {
		
	}
	
	public StatusFlowDao getStatusFlowDao() {
		return statusFlowDao;
	}

	public void setStatusFlowDao(StatusFlowDao statusFlowDao) {
		this.statusFlowDao = statusFlowDao;
	}

}
