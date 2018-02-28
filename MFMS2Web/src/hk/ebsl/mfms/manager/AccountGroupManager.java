package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface AccountGroupManager {

	public List<AccountGroup> getAllAccountGroup() throws MFMSException;

	public int saveAccountGroup(AccountGroup accountGroup) throws MFMSException;

	public AccountGroup getAccountGroupByAccountGroupKey(Integer accountGroupKey);
	
	public void deleteAccountGroupByKey(Integer accountKey, Integer accountGroupKey) throws MFMSException;
	
	public AccountGroup getAccountGroupByName(Integer key, String name) throws MFMSException;
	
	public List<AccountGroup> getAccountGroupBySiteKey(Integer siteKey) throws MFMSException;
	
	public List<AccountGroup> searchAccountGroupByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate);

	public Integer searchResultCountWithSiteKey(Timestamp lastModifiedDate, Integer siteKey);
}
