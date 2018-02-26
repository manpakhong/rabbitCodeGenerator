package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountGroupResponsibleDao;
import hk.ebsl.mfms.dao.AccountGroupDao;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupResponsibleManager;
import hk.ebsl.mfms.manager.AccountGroupManager;

public class AccountGroupResponsibleManagerImpl implements AccountGroupResponsibleManager {

	public static final Logger logger = Logger.getLogger(AccountGroupResponsibleManagerImpl.class);
	
	private AccountGroupResponsibleDao accountGroupResponsibleDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List< AccountGroupResponsible> getAllAccountGroupResponsible() throws MFMSException {
		
		logger.debug("getAllAccountGroup()");
		
		List< AccountGroupResponsible> list =  accountGroupResponsibleDao.getAllAccountGroupResponsible();
		
		return list;
	}
	
	public void test() {
		
	}
	
	public AccountGroupResponsibleDao getAccountGroupResponsibleDao() {
		return accountGroupResponsibleDao;
	}

	public void setAccountGroupResponsibleDao(AccountGroupResponsibleDao accountGroupResponsibleDao) {
		this.accountGroupResponsibleDao = accountGroupResponsibleDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveAccountGroupResponsible(AccountGroupResponsible accountGroupResponsible) throws MFMSException {
		// TODO Auto-generated method stub
		accountGroupResponsibleDao.saveAccountGroupResponsible(accountGroupResponsible);
		
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupResponsible> searchAccountGroupResponsible(Integer accountGroupKey) throws MFMSException {

		List<AccountGroupResponsible> list = accountGroupResponsibleDao.searchAccountGroupResponsible(accountGroupKey);

		return list;
	}


	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteAccountGroupResponsibleByKey(Integer accountKey, Integer accountGroupResponsibleKey)
			throws MFMSException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public AccountGroupResponsible getAccountGroupResponsible(Integer groupKey, Integer accountKey) {
		// TODO Auto-generated method stub
		logger.debug("getAccountGroupResponsibleByKey()[" + groupKey + "], [" + accountKey + "]");

		AccountGroupResponsible accountGroupResponsible = accountGroupResponsibleDao.getAccountGroupResponsible(groupKey, accountKey);



		return accountGroupResponsible;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupResponsible> getAccountGroupResponsibleByDate(
			Timestamp lastModifyDate) {
		// TODO Auto-generated method stub
		return accountGroupResponsibleDao.getAccountGroupAccountListByDate(lastModifyDate);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupResponsible> searchAccountGroupResponsibleByDate(Timestamp lastModifiedDate, Integer offset,
			Integer maxResults) {
		logger.debug("searchAccountGroupResponsibleByDate()[" + lastModifiedDate + "]");

		List<AccountGroupResponsible> agrList = accountGroupResponsibleDao.searchAccountGroupResponsible(lastModifiedDate, offset, maxResults);

		return agrList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = accountGroupResponsibleDao.theCountOfSearchResult(lastModifiedDate);

		return theCountoftotalResult;
	}

	
}
