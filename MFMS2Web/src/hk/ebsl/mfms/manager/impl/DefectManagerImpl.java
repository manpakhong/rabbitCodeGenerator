package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountGroupAccountDao;
import hk.ebsl.mfms.dao.DefectDao;
import hk.ebsl.mfms.dao.UserAccountDao;
import hk.ebsl.mfms.dao.UserAccountRoleDao;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.report.template.object.DefectExcel;
import hk.ebsl.mfms.report.template.object.DefectListReport;
import hk.ebsl.mfms.report.template.object.DefectStatusSummaryReport;
import hk.ebsl.mfms.web.controller.DefectController.AccountComparator;

public class DefectManagerImpl implements DefectManager {

	public static final Logger logger = Logger
			.getLogger(DefectManagerImpl.class);

	private DefectDao defectDao;

	private UserAccountDao userAccountDao;

	private AccountGroupAccountDao accountGroupAccountDao;

	private UserAccountRoleDao userAccountRoleDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> getAllDefect() throws MFMSException {

		logger.debug("getAllDefect()");

		List<Defect> list = defectDao.getAllDefect();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> getDefectByLastModifyDateAndSiteKey(Timestamp time,
			Integer siteKey, Integer accountKey) throws MFMSException {

		List<Defect> list = defectDao.getDefectByLastModifyDateAndSiteKey(time,
				siteKey, accountKey);

		return list;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> getDefectByDelta(Timestamp time, Integer siteKey,
			Integer accountKey, Integer[] accountGroupKey) throws MFMSException {
		// TODO Auto-generated method stub
		return defectDao.getDefectByDelta(time, siteKey, accountKey, accountGroupKey);
	}
	

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public synchronized String getNextDefectCode(Integer siteKey) throws MFMSException {

		return defectDao.getNextDefectCode(siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasLocation(Integer locationKey) throws MFMSException {

		return defectDao.hasLocation(locationKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasCauseCode(Integer causeCodeKey) throws MFMSException {

		return defectDao.hasCauseCode(causeCodeKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasTool(Integer toolKey) throws MFMSException {

		return defectDao.hasTool(toolKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public boolean hasEquipment(Integer equipmentKey) throws MFMSException {

		return defectDao.hasEquipment(equipmentKey);
	}
	
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Integer> getAccountList(Integer siteKey) throws MFMSException {

		List<Integer> keyList = new ArrayList<Integer>();

		List<UserAccount> accountList = new ArrayList<UserAccount>();

		List<UserAccountRole> siteUserAccountRoleList = userAccountRoleDao
				.searchUserAccountRole(siteKey, null, null, null, null);

		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {

				// do not add duplicate if any
				boolean exist = false;
				for (UserAccount a : accountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {

					keyList.add(ar.getUserAccount().getKey());

				}
			}

			// Collections.sort(accountList, new AccountComparator());

			return keyList;
		}
		return null;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int searchDefectCount(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer groupKey,
			Integer accountKey, List<Integer> privilegeLocationKey)
			throws MFMSException {

//		List<Integer> agalist2 = null;
//		if (groupKey != null) {
//			List<AccountGroupAccount> agalist = accountGroupAccountDao
//					.searchAccountGroupAccount(groupKey);
//			// List<Integer> agalist2 = new ArrayList<Integer>();
//			agalist2 = new ArrayList<Integer>();
//
//			for (AccountGroupAccount aga : agalist)
//				agalist2.add(aga.getAccountKey());
//		}
//		
		

		int count = defectDao.searchDefectCount(siteKey, code, locationKey,
				priority, status, failureClassKey, problemCodeKey,
				causeCodeKey, desc, accountKey, privilegeLocationKey,groupKey);

		return count;
		// if (groupKey != null && accountKey == null) {
		// List<Defect> dlist = new ArrayList<Defect>();
		//
		// for (Defect d : list) {
		//
		// if (d.getAssignedAccountKey() != null) {
		//
		// if (agalist2.contains(d.getAssignedAccountKey()))
		// dlist.add(d);
		//
		// }
		//
		// }
		//
		// return dlist;
		//
		// } else
		//
		// return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> searchDefect(Integer siteKey, String code,
			Integer locationKey, Integer priority, String status,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, String desc, Integer groupKey,
			Integer accountKey) throws MFMSException {

		List<Defect> list = defectDao.searchDefect(siteKey, code, locationKey,
				priority, status, failureClassKey, problemCodeKey,
				causeCodeKey, desc, accountKey,groupKey);

//		List<AccountGroupAccount> agalist = accountGroupAccountDao
//				.searchAccountGroupAccount(groupKey);
//		List<Integer> agalist2 = new ArrayList<Integer>();
//
//		for (AccountGroupAccount aga : agalist)
//			agalist2.add(aga.getAccountKey());
//
//		if (groupKey != null && accountKey == null) {
//			List<Defect> dlist = new ArrayList<Defect>();
//
//			for (Defect d : list) {
//
//				if (d.getAssignedAccountKey() != null) {
//
//					if (agalist2.contains(d.getAssignedAccountKey()))
//						dlist.add(d);
//
//				}
//				
//			}
//
//			return dlist;
//
//		} else

			return list;
	}

	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int searchDefectListCount(Integer siteKey, Timestamp from,
			Timestamp to, String code, Integer accKey, Integer locationKey,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, Integer equipmentKey, Integer priority,
			String callFrom, String status, Integer accountKey,
			Integer groupKey)
			throws MFMSException {
		// TODO Auto-generated method stub
		
		List<Integer> agalist2 = null;
		if (groupKey != null) {
			List<AccountGroupAccount> agalist = accountGroupAccountDao
					.searchAccountGroupAccount(groupKey);
			// List<Integer> agalist2 = new ArrayList<Integer>();
			agalist2 = new ArrayList<Integer>();

			for (AccountGroupAccount aga : agalist)
				agalist2.add(aga.getAccountKey());
		}
		
		int count = defectDao.searchDefectListCount(siteKey, from,
				to, code, accKey, locationKey, failureClassKey, problemCodeKey,
				causeCodeKey, equipmentKey, priority, callFrom, status,
				accountKey, agalist2);

		
		return count;
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectListReport> searchDefectList(Integer siteKey,
			Timestamp from, Timestamp to, String code, Integer accKey,
			Integer locationKey, Integer failureClassKey,
			Integer problemCodeKey, Integer causeCodeKey, Integer equipmentKey,
			Integer priority, String callFrom, String status, Integer accountKey, Integer groupKey)
			throws MFMSException {

		List<DefectListReport> list = defectDao.searchDefectList(siteKey, from,
				to, code, accKey, locationKey, failureClassKey, problemCodeKey,
				causeCodeKey, equipmentKey, priority, callFrom, status,
				accountKey, groupKey);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> searchExpiringDefect(Integer minutes)
			throws MFMSException {

		logger.debug("searchExpiringDefect()[" + minutes + "]");

		Timestamp targetStartDateFrom = new Timestamp(
				System.currentTimeMillis());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(targetStartDateFrom);
		calendar.add(Calendar.MINUTE, minutes);
		Timestamp targetStartDateTo = new Timestamp(calendar.getTimeInMillis());

		List<Defect> list = defectDao.searchDefectByStatusTargetStartDateTime(
				null, targetStartDateFrom, targetStartDateTo);

		return list;
	}

	public DefectDao getDefectDao() {
		return defectDao;
	}

	public void setDefectDao(DefectDao defectDao) {
		this.defectDao = defectDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public synchronized Integer saveDefect(Defect defect) throws MFMSException {
		// TODO Auto-generated method stub
		return defectDao.saveDefect(defect);

	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public synchronized List<Integer> saveDefect(List<Defect> defectList) throws MFMSException {
		// TODO Auto-generated method stub
		
		List<Integer> rtn = new ArrayList<Integer>();
		
		for(Defect d : defectList){
			int insertKey = defectDao.saveDefect(d);
			rtn.add(insertKey);
			
		}
		
		return rtn;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Defect getDefectByKey(Integer key) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("getDefectByKey()[" + key + "]");

		Defect defect = defectDao.getDefectByKey(key);

		return defect;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Defect getDefectByCode(Integer siteKey, String code, boolean fetch)
			throws MFMSException {

		logger.debug("getDefectByCode()[" + siteKey + "," + code + "," + fetch
				+ "]");

		Defect defect = defectDao.getDefectByCode(siteKey, code, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return defect;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteDefectByKey(Integer accountKey, Integer defectKey)
			throws MFMSException {

		logger.debug("deleteDefectByKey()[" + accountKey + "," + defectKey
				+ "]");

		Defect defect = defectDao.getDefectByKey(defectKey);

		if (defect == null) {
			throw new MFMSException("Defect not found");

		}
		defect.setDeleted("Y");
		defect.setLastModifyBy(accountKey);
		defect.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveDefect(defect);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> searchDefectByTargetStartDate(
			Integer assignedAccountKey, Timestamp from, Timestamp to)
			throws MFMSException {

		logger.debug("searchDefectByTargetStartDate()[" + assignedAccountKey
				+ "," + from + "," + to + "]");

		List<Defect> list = defectDao
				.searchDefectByAssignedTargetStartDateTime(assignedAccountKey,
						from, to);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> searchGroupDefectByTargetStartDate(
			Integer assignedGroupKey, Timestamp from, Timestamp to)
			throws MFMSException {

		logger.debug("searchGroupDefectByTargetStartDate()[" + assignedGroupKey
				+ "," + from + "," + to + "]");

		List<Defect> list = defectDao
				.searchGroupDefectByAssignedTargetStartDateTime(
						assignedGroupKey, from, to);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectStatusSummaryReport> searchDefectStatusSummary(
			Integer siteKey, Timestamp from, Timestamp to, Integer accountKey,
			String[] statusMap) throws MFMSException {
		// TODO Auto-generated method stub
		return defectDao.searchDefectStatusSummary(siteKey, from, to,
				accountKey, statusMap, null, null, null, null);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectStatusSummaryReport> searchDefectStatusSummary(
			Integer siteKey, Timestamp from, Timestamp to, Integer accountKey,
			String[] statusMap, Integer locationKey, Integer failureClassKey,
			Integer problemCodeKey, List<Integer> privilegeLocationKey)
			throws MFMSException {
		// TODO Auto-generated method stub
		return defectDao.searchDefectStatusSummary(siteKey, from, to,
				accountKey, statusMap,locationKey, failureClassKey, problemCodeKey,privilegeLocationKey);
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Defect> searchDefectByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		logger.debug("searchDefectByDate()[" + lastModifiedDate + "]");

		List<Defect> defectList = defectDao.searchDefect(lastModifiedDate, offset, maxResults, siteKey);

		return defectList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = defectDao.theCountOfSearchResult(lastModifiedDate, siteKey);

		return theCountoftotalResult;
	}

	public UserAccountDao getUserAccountDao() {
		return userAccountDao;
	}

	public void setUserAccountDao(UserAccountDao userAccountDao) {
		this.userAccountDao = userAccountDao;
	}

	public AccountGroupAccountDao getAccountGroupAccountDao() {
		return accountGroupAccountDao;
	}

	public void setAccountGroupAccountDao(
			AccountGroupAccountDao accountGroupAccountDao) {
		this.accountGroupAccountDao = accountGroupAccountDao;
	}

	public UserAccountRoleDao getUserAccountRoleDao() {
		return userAccountRoleDao;
	}

	public void setUserAccountRoleDao(UserAccountRoleDao userAccountRoleDao) {
		this.userAccountRoleDao = userAccountRoleDao;
	}


}
