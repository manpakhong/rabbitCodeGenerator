package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.exception.MFMSException;
import java.sql.Timestamp;
import java.util.List;

public interface AccountGroupResponsibleManager {

	public List<AccountGroupResponsible> getAllAccountGroupResponsible() throws MFMSException;

	public void saveAccountGroupResponsible(AccountGroupResponsible accountGroupResponsible) throws MFMSException;

	public List<AccountGroupResponsible> searchAccountGroupResponsible(Integer accountGroupKey) throws MFMSException;

	public void deleteAccountGroupResponsibleByKey(Integer accountKey, Integer accountGroupResponsibleKey) throws MFMSException;
	
	public AccountGroupResponsible getAccountGroupResponsible(Integer groupKey, Integer accountKet);
	
	public List<AccountGroupResponsible> getAccountGroupResponsibleByDate(Timestamp lastModifyDate);

	public List<AccountGroupResponsible> searchAccountGroupResponsibleByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCount(Timestamp lastModifiedDate);
}
