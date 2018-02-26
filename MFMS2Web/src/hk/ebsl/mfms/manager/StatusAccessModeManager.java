package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusAccessMode;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface StatusAccessModeManager {

	public List<StatusAccessMode> getAllStatusAccessMode() throws MFMSException;
	
	public StatusAccessMode getStatusAccessModeByModeKey(Integer modeKey) throws MFMSException;
	
	public List<StatusAccessMode> searchStatusAccessModeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);
	
	public Integer searchResultCount(Timestamp lastModifiedDate);

}
