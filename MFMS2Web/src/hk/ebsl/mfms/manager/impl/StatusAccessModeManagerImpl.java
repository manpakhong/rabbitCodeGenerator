package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.StatusAccessModeDao;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.StatusAccessMode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.StatusAccessModeManager;

public class StatusAccessModeManagerImpl implements StatusAccessModeManager {

	public static final Logger logger = Logger.getLogger(StatusAccessModeManagerImpl.class);

	private StatusAccessModeDao statusAccessModeDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<StatusAccessMode> getAllStatusAccessMode() throws MFMSException {

		logger.debug("getAllStatusAccessMode()");

		List<StatusAccessMode> list = statusAccessModeDao.getAllStatusAccessMode();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public StatusAccessMode getStatusAccessModeByModeKey(Integer modeKey) throws MFMSException {

		List<StatusAccessMode> list = statusAccessModeDao.getStatusAccessModeByModeKey(modeKey);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<StatusAccessMode> searchStatusAccessModeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		logger.debug("searchSite()[" + lastModifiedDate + "]");

		List<StatusAccessMode> statusAccessModeList = statusAccessModeDao.searchStatusAccessMode(lastModifiedDate, offset, maxResults);

		return statusAccessModeList;

	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = statusAccessModeDao.theCountOfSearchResult(lastModifiedDate);
		
		return theCountoftotalResult;
	}
	

	public void test() {

	}

	public StatusAccessModeDao getStatusAccessModeDao() {
		return statusAccessModeDao;
	}

	public void setStatusAccessModeDao(StatusAccessModeDao statusAccessModeDao) {
		this.statusAccessModeDao = statusAccessModeDao;
	}

}
