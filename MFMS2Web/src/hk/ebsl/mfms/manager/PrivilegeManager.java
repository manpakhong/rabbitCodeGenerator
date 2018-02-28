package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;
import hk.ebsl.mfms.dto.Privilege;

public interface PrivilegeManager {
	
	public List<Privilege> searchPrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCount(Timestamp lastModifiedDate);
}
