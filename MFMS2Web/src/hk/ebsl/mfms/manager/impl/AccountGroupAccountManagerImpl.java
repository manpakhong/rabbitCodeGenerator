package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountGroupAccountDao;
import hk.ebsl.mfms.dao.AccountGroupDao;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;

public class AccountGroupAccountManagerImpl implements AccountGroupAccountManager {

	public static final Logger logger = Logger.getLogger(AccountGroupAccountManagerImpl.class);

	private AccountGroupAccountDao accountGroupAccountDao;

	@Override
	@Transactional
	public List<AccountGroupAccount> getAllAccountGroupAccount() throws MFMSException {

		logger.debug("getAllAccountGroup()");

		List<AccountGroupAccount> list = accountGroupAccountDao.getAllAccountGroupAccount();

		return list;
	}

	public void test() {

	}

	public AccountGroupAccountDao getAccountGroupAccountDao() {
		return accountGroupAccountDao;
	}

	public void setAccountGroupAccountDao(AccountGroupAccountDao accountGroupAccountDao) {
		this.accountGroupAccountDao = accountGroupAccountDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveAccountGroupAccount(AccountGroupAccount accountGroupAccount) throws MFMSException {
		// TODO Auto-generated method stub
		accountGroupAccountDao.saveAccountGroupAccount(accountGroupAccount);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupAccount> checkAccountHasGroup(Integer accountKey) throws MFMSException {

		List<AccountGroupAccount> list = accountGroupAccountDao.checkAccountHasGroup(accountKey);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupAccount> searchAccountGroupAccount(Integer accountGroupKey) throws MFMSException {

		List<AccountGroupAccount> list = accountGroupAccountDao.searchAccountGroupAccount(accountGroupKey);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteAccountGroupAccountByKey(Integer accountKey, Integer accountGroupAccountKey)
			throws MFMSException {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public AccountGroupAccount getAccountGroupAccount(Integer groupKey, Integer accountKey) {
		// TODO Auto-generated method stub
		logger.debug("getAccountGroupAccountByKey()[" + groupKey + "], [" + accountKey + "]");

		AccountGroupAccount accountGroupAccount = accountGroupAccountDao.getAccountGroupAccount(groupKey, accountKey);

		return accountGroupAccount;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupAccount> getAccountGroupAccountByDate(Timestamp lastModifyDate) {
		// TODO Auto-generated method stub

		List<AccountGroupAccount> list = accountGroupAccountDao.getAccountGroupAccountListByDate(lastModifyDate);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountGroupAccount> searchAccountGroupAccountByDate(Timestamp lastModifiedDate, Integer offset,
			Integer maxResults) {
		logger.debug("searchAccountGroupAccountByDate()[" + lastModifiedDate + "]");

		List<AccountGroupAccount> agaList = accountGroupAccountDao.searchAccountGroupAccount(lastModifiedDate, offset, maxResults);

		return agaList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = accountGroupAccountDao.theCountOfSearchResult(lastModifiedDate);

		return theCountoftotalResult;

	}
}
