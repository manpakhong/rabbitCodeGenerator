package hk.ebsl.mfms.authentication;

import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.UserAccountManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserService implements UserDetailsService {

	public static Logger logger = Logger.getLogger(CustomUserService.class);

	private Integer maxLogonAttemptCount;

	private String defaultRoleString = "ROLE_USER";

	private UserAccountManager userManager;

	@Autowired
	private AccountActionLogManager accountActionLogManager;

	@Override
	public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

		// get our user object
		UserAccount account = null;
		try {
			// do not need account roles here
			account = userManager.getUserAccountByLoginId(loginId, false);

		} catch (MFMSException e) {
			logger.debug(e.getMessage());
			throw new UsernameNotFoundException(e.getMessage());
		}

		if (account == null) {

			/*
			 * // Account Action Log AccountActionLog accountActionLog = new
			 * AccountActionLog(); accountActionLog.setActionType("Logon");
			 * accountActionLog.setAccountId(loginId);
			 * accountActionLog.setSiteKey(-1); accountActionLog.setResult(
			 * "User Not Found"); accountActionLog.setActionDateTime(new
			 * Timestamp(System.currentTimeMillis()));
			 * accountActionLogManager.saveAccountActionLog(accountActionLog);
			 * // End Action Action Log
			 */

			logger.debug("User " + loginId + " not found.");
			throw new UsernameNotFoundException("User " + loginId + " not found.");
		}

		// user found
		logger.debug("User " + loginId + " found.");

		String userName = account.getLoginId();

		String password = account.getPassword();

		Boolean enabled = true;
		// if deleted == Y/y then set disabled
		if ("Y".equalsIgnoreCase(account.getDeleted())) {
			logger.debug("User " + loginId + " is disabled.");
			enabled = false;
		}

		Boolean accountNonExpired = true;

		Boolean credentialsNonExpired = true;

		Boolean accountNonLocked = true;

		if (account.getStatus().equals("S"))
			accountNonLocked = false;

		// maxLogonAttemptCount is set and user's attempt has reached maximum
		// then lock account
		if (maxLogonAttemptCount != null && maxLogonAttemptCount >= 0
				&& account.getLogonAttemptCount() >= maxLogonAttemptCount) {
			logger.debug("User " + loginId + " is locked.");

			account.setStatus("S");

			try {
				userManager.saveUserAccount(account);
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			accountNonLocked = false;
		}
		User user = new User(userName, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
				AuthorityUtils.commaSeparatedStringToAuthorityList(defaultRoleString));

		return user;
	}

	public Integer getMaxLogonAttemptCount() {
		return maxLogonAttemptCount;
	}

	public void setMaxLogonAttemptCount(Integer maxLogonAttemptCount) {
		this.maxLogonAttemptCount = maxLogonAttemptCount;
	}

	public String getDefaultRoleString() {
		return defaultRoleString;
	}

	public void setDefaultRoleString(String defaultRoleString) {
		this.defaultRoleString = defaultRoleString;
	}

	public UserAccountManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserAccountManager userManager) {
		this.userManager = userManager;
	}

	public AccountActionLogManager getAccountActionLogManager() {
		return accountActionLogManager;
	}

	public void setAccountActionLogManager(AccountActionLogManager accountActionLogManager) {
		this.accountActionLogManager = accountActionLogManager;
	}

}
