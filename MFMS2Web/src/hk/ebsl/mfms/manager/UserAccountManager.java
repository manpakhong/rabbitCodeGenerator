package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;

public interface UserAccountManager {

	public List<UserAccount> getAllUserAccount() throws MFMSException;

	public UserAccount getUserAccountByLoginId(String loginId, boolean fetch) throws MFMSException;

	public void incrementLogonAttemptCount(String loginId) throws MFMSException;

	public void resetLogonAttemptCount(String loginId) throws MFMSException;

	public void saveUserAccount(UserAccount account) throws MFMSException;

	public UserAccount getUserAccountByAccountKey(Integer accountKey) throws MFMSException;

	public UserAccount getUserAccountByAccountKey(Integer accountKey, boolean fetch) throws MFMSException;

	public List<UserAccountRole> searchUserAccount(String accountName, int accountRoleKey) throws MFMSException;

	public List<UserAccountRole> searchUserAccountRole(Integer siteKey, Integer roleKey, String loginId,
			String accountName, String accountStatus) throws MFMSException;

	public List<UserAccount> searchUnassignedUserAccount(Integer siteKey, String loginId, String accountName,
			String accountStatus) throws MFMSException;

	public void saveUserAccountRole(UserAccountRole accountRole) throws MFMSException;

	public UserAccount checkUserLogin(String ac, String pwd);
	
	public UserAccount checkUserEncryptedLogin(String ac, String pwd);
	
	public List<UserAccount> searchUserAccountByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCount(Timestamp lastModifiedDate);
	
	public List<UserAccountRole> searchUserAccountRoleByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCountByUserAccountRole(Timestamp lastModifiedDate);
}
