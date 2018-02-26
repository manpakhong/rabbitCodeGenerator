package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface StatusFlowManager {

	public List<StatusFlow> getAllStatusFlow() throws MFMSException;

	public List<StatusFlow> getStatus(Integer modeKey, String currentStatusId) throws MFMSException;

	public List<StatusFlow> searchStatusFlowByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCount(Timestamp lastModifiedDate);

}
