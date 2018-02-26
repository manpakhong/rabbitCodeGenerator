package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface PriorityManager {

	public List<Priority> getAllPriority() throws MFMSException;

	public Float getResponseTime(Integer num) throws MFMSException;

	public List<Priority> searchPriorityByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCount(Timestamp lastModifiedDate);

}
