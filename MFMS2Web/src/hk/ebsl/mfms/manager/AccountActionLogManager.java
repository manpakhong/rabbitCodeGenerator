package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.AccountActionLog;
import java.sql.Timestamp;
import java.util.List;

public interface AccountActionLogManager {
	public List<AccountActionLog> searchAccountActionLog(Integer siteKey, Integer accountKey, Timestamp from,
			Timestamp to, Integer groupKey);

	public Integer saveAccountActionLog(AccountActionLog accountActionLog);

}
