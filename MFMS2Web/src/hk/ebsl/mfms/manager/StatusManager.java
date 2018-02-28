package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface StatusManager {

	public List<Status> getAllStatus() throws MFMSException;
	
	public Status getStatus(String statusId) throws MFMSException;
	
	public String getStatusNameByStatusId(String statusId) throws MFMSException;
	
	public Integer getSequenceByStatus(StatusFlow statusFlow) throws MFMSException;

	public List<Status> searchStatusByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);
	
	public Integer searchResultCount(Timestamp lastModifiedDate);

}
