package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.DefectScheduleAccountDao;
import hk.ebsl.mfms.dao.DefectScheduleDao;
import hk.ebsl.mfms.dao.DefectScheduleHistoryDao;
import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.utility.CalendarEvent;

public class DefectScheduleManagerImpl implements DefectScheduleManager {

	public static final Logger logger = Logger.getLogger(DefectScheduleManagerImpl.class);

	DefectScheduleDao defectScheduleDao;
	DefectScheduleAccountDao defectScheduleAccountDao;
	DefectScheduleHistoryDao defectScheduleHistoryDao;
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleHistory> getDefectScheduleHistoryByDefectScheduleKey(Integer key) throws MFMSException {
		logger.debug("getDefectScheduleHistoryByDefectScheduleKey(" + key + ")");
		List<DefectScheduleHistory> dshList = defectScheduleHistoryDao.getDefectScheduleHistoryByDefectScheduleKey(key);
		
		return dshList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer saveDefectSchedule(DefectSchedule defectSchedule,
			List<DefectScheduleAccount> defectScheduleAccountList) {

		int key = defectScheduleDao.saveDefectSchedule(defectSchedule);

		if (defectScheduleAccountList != null) {
			for (int j = 0; j < defectScheduleAccountList.size(); j++) {
				DefectScheduleAccount account = new DefectScheduleAccount();
				account.setScheduleKey(key);
				account.setSiteKey(defectScheduleAccountList.get(j).getSiteKey());
				account.setAccountKey(defectScheduleAccountList.get(j).getAccountKey());
				account.setCreateBy(defectScheduleAccountList.get(j).getCreateBy());
				account.setCreateDateTime(defectScheduleAccountList.get(j).getCreateDateTime());
				account.setLastModifyBy(defectScheduleAccountList.get(j).getLastModifyBy());
				account.setLastModifyTime(defectScheduleAccountList.get(j).getLastModifyTime());
				account.setDeleted(defectScheduleAccountList.get(j).getDeleted());
				defectScheduleAccountDao.saveDefectScheduleAccount(account);
			}

		}

		return key;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveDefectSchedule(List<DefectSchedule> list) {

		for (DefectSchedule defectSchedule : list)

			defectScheduleDao.saveDefectSchedule(defectSchedule);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectSchedule> searchDefectScheduleChilden(Integer parentId) {
		List<DefectSchedule> defectScheduleList = defectScheduleDao.searchByParentId(parentId);
		return defectScheduleList;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectSchedule> searchDefectScheduleChilden(Integer parentId, Timestamp time) {
		List<DefectSchedule> defectScheduleList = defectScheduleDao.searchByParentId(parentId, time);

		return defectScheduleList;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteDefectSchedule(Integer key, Integer accountKey) {

		DefectSchedule defectSchedule = defectScheduleDao.getScheduleByKey(key);

		defectSchedule.setDeleted("Y");
		defectSchedule.setLastModifyBy(accountKey);
		defectSchedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		defectScheduleDao.saveDefectSchedule(defectSchedule);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteListOfChildrenDefectSchedule(List<DefectSchedule> dsList, Integer accountKey){
		
		for(DefectSchedule ds : dsList){
			ds.setDeleted("Y");
			ds.setLastModifyBy(accountKey);
			ds.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
		}
		
		defectScheduleDao.saveListOfDefectSchedules(dsList);
	}
	
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteDefectScheduleAccount(Integer key, Integer accountKey) {

		List<DefectScheduleAccount> defectScheduleAccountList = defectScheduleAccountDao.getScheduleAccount(key);
		for (DefectScheduleAccount defectScheduleAccount : defectScheduleAccountList) {
			defectScheduleAccount.setDeleted("Y");
			defectScheduleAccount.setLastModifyBy(accountKey);
			defectScheduleAccount.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			defectScheduleAccountDao.saveDefectScheduleAccount(defectScheduleAccount);
		}
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteListOfDefectScheduleAccountFromDefectScheduleList(List<DefectSchedule> dsList, Integer accountKey){
		List<DefectScheduleAccount> result = new ArrayList<DefectScheduleAccount>();
		for(DefectSchedule ds : dsList){
			List<DefectScheduleAccount> defectScheduleAccountList = defectScheduleAccountDao.getScheduleAccount(ds.getScheduleKey());
				for(DefectScheduleAccount dsa : defectScheduleAccountList){
					dsa.setDeleted("Y");
					dsa.setLastModifyBy(accountKey);
					dsa.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
				}
				result.addAll(defectScheduleAccountList);
		}
		
		defectScheduleAccountDao.saveListOfDefectScheduleAccount(result);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteListOfDefectScheduleAccount(List<DefectScheduleAccount> dsaList, Integer accountKey) {
	
		for(DefectScheduleAccount dsa : dsaList){
			dsa.setDeleted("Y");
			dsa.setLastModifyBy(accountKey);
			dsa.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
		}
		
		defectScheduleAccountDao.saveListOfDefectScheduleAccount(dsaList);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasEquipment(Integer equipmentKey) throws MFMSException {

		return defectScheduleDao.hasEquipment(equipmentKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectSchedule> getSchedule(Integer siteKey) {

		return defectScheduleDao.getSchedule(siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectSchedule> getSchedule(Integer siteKey, Integer parentId) {

		return defectScheduleDao.getSchedule(siteKey, parentId);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public DefectSchedule getScheduleByKey(Integer key) {

		return defectScheduleDao.getScheduleByKey(key);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public DefectSchedule getParentSchedule(Integer parentId) {

		return defectScheduleDao.getParentSchedule(parentId);
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectSchedule> searchDefectScheduleByDate(Timestamp lastModifiedDate, Integer offset,
			Integer maxResults, Integer siteKey) {
		logger.debug("searchDefectScheduleByDate()[" + lastModifiedDate + "]");
		
		List<DefectSchedule> defectScheduleList = defectScheduleDao.searchDefectSchedule(lastModifiedDate,offset,maxResults, siteKey);
		
		return defectScheduleList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = defectScheduleDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleAccount> searchDefectScheduleAccountByDate(Timestamp lastModifiedDate, Integer offset,
			Integer maxResults, Integer siteKey) {
		logger.debug("searchDefectScheduleByDate()[" + lastModifiedDate + "]");
		
		List<DefectScheduleAccount> defectScheduleAccountList = defectScheduleAccountDao.searchDefectScheduleAccount(lastModifiedDate,offset,maxResults, siteKey);
		
		return defectScheduleAccountList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCountByDefectScheduleAccount(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = defectScheduleAccountDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}
	

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleAccount> getScheduleAccount(Integer schedulekey) {

		return defectScheduleAccountDao.getScheduleAccount(schedulekey);
	}
	
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleAccount> getAllScheduleAccountByScheduleKey(Integer schedulekey) {

		return defectScheduleAccountDao.getScheduleAccountByScheduleKey(schedulekey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleAccount> getSiteScheduleAccount(Timestamp time, Integer siteKey, Integer accountKey) {

		return defectScheduleAccountDao.getSiteScheduleAccount(time, siteKey, accountKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public DefectScheduleAccount getScheduleAccount(Integer schedulekey, Integer accountKey) {

		return defectScheduleAccountDao.getScheduleAccount(schedulekey, accountKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectSchedule> searchAccountDefectSchedule(Integer accountKey, Timestamp startDate, Timestamp endDate)
			throws MFMSException {

		// search defect schedules of given account that span across given time
		// range
		// does not imply the event will occur within this range
		logger.debug("searchAccountDefectSchedule()[" + accountKey + "," + startDate + "," + endDate + "]");

		List<DefectScheduleAccount> dsaList = defectScheduleAccountDao.searchScheduleAccount(accountKey);

		List<DefectSchedule> dsList = null;

		if (dsaList != null && dsaList.size() > 0) {

			logger.debug("dsaList size=" + dsaList.size());

			dsList = new ArrayList<DefectSchedule>();
			for (DefectScheduleAccount dsa : dsaList) {
				// check schedule within startDate and endDate;
				DefectSchedule ds = dsa.getDefectSchedule();

				if (ds.getScheduleStartDate().compareTo(endDate) <= 0
						&& (ds.getScheduleEndDate() == null || ds.getScheduleEndDate().compareTo(startDate) >= 0)) {
					// start before / equals to endDate and end after / equals
					// to startDate

					dsList.add(ds);
				}
			}
			logger.debug("dsList size=" + dsList.size());

		}
		return dsList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int createDefectScheduleHistory(DefectScheduleHistory dsh) {

		return defectScheduleHistoryDao.saveDefectScheduleHistory(dsh);

	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public 	List<DefectScheduleHistory> getSiteScheduleHistory(Integer siteKey, Timestamp lastFinishDateTime)
			throws MFMSException {

		return defectScheduleHistoryDao.getSiteScheduleHistory(siteKey, lastFinishDateTime);

	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public DefectScheduleHistory getDefectScheduleHistoryByKey(Integer key) {
		// TODO Auto-generated method stub
		return defectScheduleHistoryDao.getByKey(key);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveDefectScheduleHistory(DefectScheduleHistory dsh) throws MFMSException{
		// TODO Auto-generated method stub
		defectScheduleHistoryDao.saveDefectScheduleHistory_1(dsh);
	}

	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleHistory> searchAllHistoryByDate(Timestamp lastModifiedDate, Integer offset,
			Integer maxResults, Integer siteKey) {
		// TODO Auto-generated method stub
		return defectScheduleHistoryDao.searchAllHistoryByDate(lastModifiedDate,offset,maxResults,siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchHistoryResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		// TODO Auto-generated method stub
		return defectScheduleHistoryDao.searchHistoryResultCount(lastModifiedDate, siteKey);
	}
	
	public DefectScheduleDao getDefectScheduleDao() {
		return defectScheduleDao;
	}

	public void setDefectScheduleDao(DefectScheduleDao defectScheduleDao) {
		this.defectScheduleDao = defectScheduleDao;
	}

	public DefectScheduleAccountDao getDefectScheduleAccountDao() {
		return defectScheduleAccountDao;
	}

	public void setDefectScheduleAccountDao(DefectScheduleAccountDao defectScheduleAccountDao) {
		this.defectScheduleAccountDao = defectScheduleAccountDao;
	}

	public DefectScheduleHistoryDao getDefectScheduleHistoryDao() {
		return defectScheduleHistoryDao;
	}

	public void setDefectScheduleHistoryDao(DefectScheduleHistoryDao defectScheduleHistoryDao) {
		this.defectScheduleHistoryDao = defectScheduleHistoryDao;
	}
}
