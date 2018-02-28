package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountGroupDao;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupManager;

public class AccountGroupManagerImpl implements AccountGroupManager {

	public static final Logger logger = Logger.getLogger(AccountGroupManagerImpl.class);

	private AccountGroupDao accountGroupDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroup> getAllAccountGroup() throws MFMSException {

		logger.debug("getAllAccountGroup()");

		List<AccountGroup> list = accountGroupDao.getAllAccountGroup();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroup> getAccountGroupBySiteKey(Integer siteKey) throws MFMSException {

		List<AccountGroup> list = accountGroupDao.getAccountGroupBySiteKey(siteKey);

		return list;
	}

	public AccountGroupDao getAccountGroupDao() {
		return accountGroupDao;
	}

	public void setAccountGroupDao(AccountGroupDao accountGroupDao) {
		this.accountGroupDao = accountGroupDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int saveAccountGroup(AccountGroup accountGroup) throws MFMSException {
		// TODO Auto-generated method stub
		return accountGroupDao.saveAccountGroup(accountGroup);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public AccountGroup getAccountGroupByAccountGroupKey(Integer key) {
		// TODO Auto-generated method stub
		logger.debug("getAccountGroupByKey()[" + key + "]");

		AccountGroup accountGroup = accountGroupDao.getAccountGroupByKey(key);

		return accountGroup;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public AccountGroup getAccountGroupByName(Integer siteKey, String name) throws MFMSException {

		AccountGroup accountGroup = accountGroupDao.getAccountGroupByName(siteKey, name);

		return accountGroup;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteAccountGroupByKey(Integer accountKey, Integer accountGroupKey) throws MFMSException {

		logger.debug("deleteAccountGroupByKey()[" + accountKey + "," + accountGroupKey + "]");

		AccountGroup accountGroup = accountGroupDao.getAccountGroupByKey(accountGroupKey);

		if (accountGroup == null) {
			throw new MFMSException("AccountGroup not found");

		}
		accountGroup.setDeleted("Y");
		accountGroup.setLastModifyBy(accountKey);
		accountGroup.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveAccountGroup(accountGroup);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroup> searchAccountGroupByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults,
			Integer siteKey) {
		logger.debug("searchAccountGroup()[" + lastModifiedDate + "]");

		List<AccountGroup> accountGroupList = accountGroupDao.searchAccountGroup(lastModifiedDate, offset, maxResults, siteKey);

		return accountGroupList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = accountGroupDao.theCountOfSearchResult(lastModifiedDate);

		return theCountoftotalResult;
	}
	
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCountWithSiteKey(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCountWithSiteKey()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = accountGroupDao.theCountOfSearchResultWithSiteKey(lastModifiedDate, siteKey);

		return theCountoftotalResult;
	}

}
