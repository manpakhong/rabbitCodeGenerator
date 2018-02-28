package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.dao.PrivilegeDao;
import hk.ebsl.mfms.dto.Privilege;

import hk.ebsl.mfms.manager.PrivilegeManager;

public class PrivilegeManagerImpl implements PrivilegeManager {

	public static final Logger logger = Logger.getLogger(PrivilegeManagerImpl.class);

	private PrivilegeDao privilegeDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Privilege> searchPrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {

		logger.debug("searchPrivilege()[" + lastModifiedDate + "]");

		List<Privilege> privilegeList = privilegeDao.searchPrivilege(lastModifiedDate, offset, maxResults);

		return privilegeList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate) {

		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = privilegeDao.theCountOfSearchResult(lastModifiedDate);

		return theCountoftotalResult;
	}

	public PrivilegeDao getPrivilegeDao() {
		return privilegeDao;
	}

	public void setPrivilegeDao(PrivilegeDao privilegeDao) {
		this.privilegeDao = privilegeDao;
	}
	
	
}
