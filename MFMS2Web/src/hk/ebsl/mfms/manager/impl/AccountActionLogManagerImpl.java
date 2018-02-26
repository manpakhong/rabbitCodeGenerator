package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountActionLogDao;
import hk.ebsl.mfms.dao.AccountGroupAccountDao;
import hk.ebsl.mfms.dao.UserAccountDao;
import hk.ebsl.mfms.dto.AccountActionLog;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.UserAccountManager;

public class AccountActionLogManagerImpl implements AccountActionLogManager {

	public static final Logger logger = Logger.getLogger(AccountActionLogManagerImpl.class);

	private AccountActionLogDao accountActionLogDao;

	private UserAccountDao userAccountDao;

	private AccountGroupAccountDao accountGroupAccountDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<AccountActionLog> searchAccountActionLog(Integer siteKey, Integer accountKey, Timestamp from,
			Timestamp to, Integer groupKey) {

		String accountId = "";

		if (accountKey != null) {
			accountId = userAccountDao.getUserAccountByKey(accountKey).getLoginId();
		}

		List<AccountActionLog> list = accountActionLogDao.searchAccountActionLog(siteKey, accountId, from, to);

		if (groupKey != null && accountKey == null) {
			List<AccountActionLog> grouplist = new ArrayList<AccountActionLog>();

			for (AccountActionLog log : list) {

				UserAccount ac = userAccountDao.getUserAccountByLoginId(log.getAccountId());

				if (ac != null) {

					AccountGroupAccount aga = accountGroupAccountDao.getAccountGroupAccount(groupKey, ac.getKey());

					if (aga != null)
						grouplist.add(log);

				}

			}

			return grouplist;

		} else

			return list;
	}

	public AccountGroupAccountDao getAccountGroupAccountDao() {
		return accountGroupAccountDao;
	}

	public void setAccountGroupAccountDao(AccountGroupAccountDao accountGroupAccountDao) {
		this.accountGroupAccountDao = accountGroupAccountDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer saveAccountActionLog(AccountActionLog accountActionLog) {
		// TODO Auto-generated method stub
		return accountActionLogDao.saveAccountActionLog(accountActionLog);

	}

	public void test() {

	}

	public AccountActionLogDao getAccountActionLogDao() {
		return accountActionLogDao;
	}

	public void setAccountActionLogDao(AccountActionLogDao accountActionLogDao) {
		this.accountActionLogDao = accountActionLogDao;
	}

	public static Logger getLogger() {
		return logger;
	}

	public UserAccountDao getUserAccountDao() {
		return userAccountDao;
	}

	public void setUserAccountDao(UserAccountDao userAccountDao) {
		this.userAccountDao = userAccountDao;
	}
}
