package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface AccountGroupAccountManager {

	public List<AccountGroupAccount> getAllAccountGroupAccount() throws MFMSException;

	public void saveAccountGroupAccount(AccountGroupAccount accountGroupAccount) throws MFMSException;

	public List<AccountGroupAccount> searchAccountGroupAccount(Integer accountGroupKey) throws MFMSException;
	
	public List<AccountGroupAccount> checkAccountHasGroup(Integer accountKey) throws MFMSException;

	public void deleteAccountGroupAccountByKey(Integer accountKey, Integer accountGroupAccountKey) throws MFMSException;
	
	public AccountGroupAccount getAccountGroupAccount(Integer groupKey, Integer accountKey);
	
	public List<AccountGroupAccount> getAccountGroupAccountByDate(Timestamp lastModifyDate);
	
	public List<AccountGroupAccount> searchAccountGroupAccountByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCount(Timestamp lastModifiedDate);
	
}
