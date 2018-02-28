package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.UserAccountDao;
import hk.ebsl.mfms.dao.UserAccountRoleDao;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.UserAccountManager;

public class UserAccountManagerImpl implements UserAccountManager {

	public static final Logger logger = Logger.getLogger(UserAccountManagerImpl.class);

	private UserAccountDao userAccountDao;

	private UserAccountRoleDao userAccountRoleDao;

	public UserAccountDao getUserAccountDao() {
		return userAccountDao;
	}

	public void setUserAccountDao(UserAccountDao userAccountDao) {
		this.userAccountDao = userAccountDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<UserAccount> getAllUserAccount() {

		logger.debug("getAllUserAccount()");

		List<UserAccount> userAccountList = userAccountDao.getAllUserAccount();

		return userAccountList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public UserAccount getUserAccountByLoginId(String loginId, boolean fetch) throws MFMSException {

		UserAccount userAccount = userAccountDao.getUserAccountByLoginId(loginId);

		// fetch lazyload objects
		if (fetch) {
			userAccount.getAccountRoles().size();
		}
		return userAccount;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public UserAccount getUserAccountByAccountKey(Integer accountKey) throws MFMSException {
		return getUserAccountByAccountKey(accountKey, false);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public UserAccount getUserAccountByAccountKey(Integer accountKey, boolean fetch) throws MFMSException {

		logger.debug("getUserAccountByAccountKey()" + "[" + accountKey + "," + fetch + "]");

		UserAccount account = userAccountDao.getUserAccountByKey(accountKey);

		// fetch lazyload objects
		if (fetch) {
			if (account != null)
				account.getAccountRoles().size();
		}

		// account.getAccountRoles().get(0);
		// account.getAccountRoles().get(0).getRole().getRolePrivileges().get(0);

		return account;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void incrementLogonAttemptCount(String loginId) throws MFMSException {

		logger.debug("incrementLogonAttemptCount()" + "[" + loginId + "]");
		userAccountDao.incrementLogonAttemptCount(loginId);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void resetLogonAttemptCount(String loginId) throws MFMSException {

		logger.debug("resetLogonAttemptCount()" + "[" + loginId + "]");
		userAccountDao.resetLogonAttemptCount(loginId);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveUserAccount(UserAccount account) throws MFMSException {

		logger.debug("saveUserAccount()");

		logger.debug("loginId: " + account.getLoginId());
		logger.debug("name: " + account.getName());

		userAccountDao.saveUserAccount(account);

		// save its role if exist;
		if (account.getKey() != null) {
			for (UserAccountRole ar : account.getAccountRoles()) {
				ar.setAccountKey(account.getKey());
				userAccountRoleDao.saveUserAccountRole(ar);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<UserAccountRole> searchUserAccount(String accountName, int accountRoleKey) {

		List<UserAccountRole> accountList = userAccountRoleDao.searchAccount(accountName, accountRoleKey);

		return accountList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<UserAccountRole> searchUserAccountRole(Integer siteKey, Integer roleKey, String loginId,
			String accountName, String accountStatus) throws MFMSException {

		// logger.debug("searchUserAccountRole()[" + siteKey + "," + roleKey +
		// "," + loginId + "," + accountName + "," + accountStatus + "]");

		List<UserAccountRole> list = userAccountRoleDao.searchUserAccountRole(siteKey, roleKey, loginId, accountName,
				accountStatus);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<UserAccount> searchUnassignedUserAccount(Integer siteKey, String loginId, String accountName,
			String accountStatus) throws MFMSException {

		logger.debug("searchUnassignedUserAccount()[" + siteKey + "," + loginId + "," + accountName + ","
				+ accountStatus + "]");

		List<UserAccount> list = userAccountDao.searchUserAccount(loginId, accountName, accountStatus);

		// check account has role in this site
		Iterator<UserAccount> it = list.iterator();

		while (it.hasNext()) {
			UserAccount a = it.next();

			// fetch account roles
			a.getAccountRoles().size();

			boolean hasRole = false;

			// TODO: remove accounts that are sysadmin

			for (UserAccountRole ar : a.getAccountRoles()) {
				if (ar.getRole().getSiteKey().equals(siteKey) && "N".equals(ar.getDeleted())) {
					hasRole = true;
					break;
				}
			}
			// remove if has role
			if (hasRole) {
				it.remove();
			}
		}
		// list of accounts without role in given site
		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveUserAccountRole(UserAccountRole accountRole) throws MFMSException {

		logger.debug("saveUserAccountRole()");
		logger.debug("Role Key = " + accountRole.getRoleKey());
		logger.debug("Account Key = " + accountRole.getAccountKey());

		userAccountRoleDao.saveUserAccountRole(accountRole);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public UserAccount checkUserLogin(String ac, String pwd) {

		List<UserAccount> userAccountList = userAccountDao.checkUserLogin(ac, pwd);

		if (userAccountList != null && userAccountList.size() > 0) {
			return userAccountList.get(0);
		} else {
			return null;
		}

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public UserAccount checkUserEncryptedLogin(String ac, String pwd) {

		List<UserAccount> userAccountList = userAccountDao.checkUserEncryptedLogin(ac, pwd);
		if (userAccountList != null && userAccountList.size() > 0) {
			return userAccountList.get(0);
		} else {
			return null;
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<UserAccount> searchUserAccountByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		logger.debug("searchUserAccount()[" + lastModifiedDate + "]");

		List<UserAccount> accountList = userAccountDao.searchUserAccount(lastModifiedDate, offset, maxResults);

		return accountList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = userAccountDao.theCountOfSearchResult(lastModifiedDate);

		return theCountoftotalResult;
	}

	public UserAccountRoleDao getUserAccountRoleDao() {
		return userAccountRoleDao;
	}

	public void setUserAccountRoleDao(UserAccountRoleDao userAccountRoleDao) {
		this.userAccountRoleDao = userAccountRoleDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<UserAccountRole> searchUserAccountRoleByDate(Timestamp lastModifiedDate, Integer offset,
			Integer maxResults) {
		logger.debug("searchUserAccountRole()[" + lastModifiedDate + "]");

		List<UserAccountRole> accountRoleList = userAccountRoleDao.searchUserAccountRole(lastModifiedDate, offset, maxResults);

		return accountRoleList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCountByUserAccountRole(Timestamp lastModifiedDate) {
		logger.debug("searchResultCountByUserAccountRole()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = userAccountRoleDao.searchResultCountByUserAccountRole(lastModifiedDate);

		return theCountoftotalResult;
	}
}
