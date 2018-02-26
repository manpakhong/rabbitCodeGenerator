package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.StatusDao;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.StatusManager;

public class StatusManagerImpl implements StatusManager {

	public static final Logger logger = Logger.getLogger(StatusManagerImpl.class);
	
	private StatusDao statusDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Status> getAllStatus() throws MFMSException {
		
		logger.debug("getAllStatus()");
		
		List<Status> list = statusDao.getAllStatus();
		
		return list;
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Status> searchStatusByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults){
		logger.debug("search Status by date");
		
		List<Status> statusList = statusDao.searchStatusByDate(lastModifiedDate, offset, maxResults);
		
		return statusList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate){
		
		Integer theCountoftotalResult = statusDao.theCountOfSearchResult(lastModifiedDate);
		
		return theCountoftotalResult;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Status getStatus(String statusId) throws MFMSException {
		
		return statusDao.getStatus(statusId);
	}
	
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public String getStatusNameByStatusId(String statusId) throws MFMSException {
		
		
		String statusName= statusDao.getStatusNameByStatusId(statusId);
		
		return statusName;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getSequenceByStatus(StatusFlow statusFlow) throws MFMSException {
		
		
		Integer sequence= statusDao.getSequenceByStatus(statusFlow);
		
		return sequence;
	}
	
	public void test() {
		
	}
	
	public StatusDao getStatusDao() {
		return statusDao;
	}

	public void setStatusDao(StatusDao statusDao) {
		this.statusDao = statusDao;
	}
}
