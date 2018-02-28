package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.PatrolScheduleAccountDao;
import hk.ebsl.mfms.dao.PatrolScheduleDao;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PatrolScheduleManager;

public class PatrolScheduleManagerImpl implements PatrolScheduleManager {

	PatrolScheduleDao patrolScheduleDao;
	PatrolScheduleAccountDao patrolScheduleAccountDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void savePatrolSchedule(List<PatrolSchedule> patrolScheduleList,
			List<PatrolScheduleAccount> patrolScheduleAccountList) {

		for (int i = 0; i < patrolScheduleList.size(); i++) {
			PatrolSchedule schedule = patrolScheduleList.get(i);
			int key = patrolScheduleDao.savePatrolSchedule(schedule);

			if (key > 0 && patrolScheduleAccountList != null) {
				for (int j = 0; j < patrolScheduleAccountList.size(); j++) {
					PatrolScheduleAccount account = new PatrolScheduleAccount();
					account.setScheduleKey(key);

					account.setAccountKey(patrolScheduleAccountList.get(j).getAccountKey());
					account.setCreateBy(patrolScheduleAccountList.get(j).getCreateBy());
					account.setCreateDateTime(patrolScheduleAccountList.get(j).getCreateDateTime());
					account.setLastModifyBy(patrolScheduleAccountList.get(j).getLastModifyBy());
					account.setLastModifyTime(patrolScheduleAccountList.get(j).getLastModifyTime());
					account.setDeleted(patrolScheduleAccountList.get(j).getDeleted());

					patrolScheduleAccountDao.savePatrolSchedule(account);
				}
			}

		}

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolSchedule> searchPatrolScheduleBySite(int siteKey) {
		// TODO Auto-generated method stub

		return patrolScheduleDao.searchPatrolScheduleBySiteKey(siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolScheduleAccount> searchPatrolSchedule(int siteKey, List<Integer> route, List<Integer> account) {

		List<PatrolScheduleAccount> patrolScheduleAccountList = new ArrayList<PatrolScheduleAccount>();

		if (route == null) {
			route = new ArrayList<Integer>();
			route.add(-1);
		}

		if (account == null) {
			account = new ArrayList<Integer>();
			account.add(-1);
		}

		for (int i = 0; i < route.size(); i++) {
			for (int j = 0; j < account.size(); j++) {
				patrolScheduleAccountList
						.addAll(patrolScheduleAccountDao.search(siteKey, route.get(i), account.get(j)));
			}
		}

		return patrolScheduleAccountList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolScheduleAccount> searchPatrolScheduleAccountByScheduleKey(int scheduleKey) {
//		List<PatrolScheduleAccount> patrolScheduleAccountList = new ArrayList<PatrolScheduleAccount>();
//		patrolScheduleAccountList.addAll(patrolScheduleAccountDao.search(scheduleKey));
//		return patrolScheduleAccountList;
		
		return patrolScheduleAccountDao.search(scheduleKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolSchedule> searchPatrolScheduleChilden(int parentId) {
//		List<PatrolSchedule> patrolScheduleList = new ArrayList<PatrolSchedule>();
//		patrolScheduleList.addAll(patrolScheduleDao.searchByParentId(parentId));

		return patrolScheduleDao.searchByParentId(parentId);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void insertNewPatrolScheduleAccount(List<PatrolScheduleAccount> list) {

		for (int i = 0; i < list.size(); i++) {
			this.patrolScheduleAccountDao.savePatrolSchedule(list.get(i));
		}

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolScheduleAccount> searchPatrolScheduleAccount(Integer scheduleKey, List<Integer> accountKey) {

		List<PatrolScheduleAccount> rtn = new ArrayList<PatrolScheduleAccount>();
		for (int i = 0; i < accountKey.size(); i++) {
			List<PatrolScheduleAccount> list = this.patrolScheduleAccountDao
					.searchByScheduleKeyAndAccountKey(scheduleKey, accountKey.get(i));
			if (list != null && list.size() > 0) {
				rtn.addAll(list);
			}

		}

		return rtn;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void updatePatrolScheduleAccount(List<PatrolScheduleAccount> list) {
		for (int i = 0; i < list.size(); i++) {
			patrolScheduleAccountDao.savePatrolSchedule(list.get(i));
		}
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolSchedule> searchPatrolScheduleByLastModifyTime(Timestamp lastModifyTime, Integer userAccountKey, Integer siteKey) {

		List<PatrolSchedule> list = new ArrayList<PatrolSchedule>();

		List<PatrolSchedule> scheduleList = patrolScheduleDao.searchPatrolScheduleByLastModifyTime(lastModifyTime,
				userAccountKey, siteKey);

		for (PatrolSchedule schedule : scheduleList) {
			Boolean repeated = false;
			
			Boolean responsible = true;

//			Boolean responsible = false;
//
//			List<PatrolScheduleAccount> accountList = schedule.getScheduleAccount();
//			for (PatrolScheduleAccount ac : accountList) {
//
//				if (ac.getAccountKey() == userAccountKey) {
//					responsible = true;
//					break;
//					// if(ac.getDeleted().equals("Y")){
//					//
//					// break;
//					// }
//				}
//			}

			for (PatrolSchedule temp : list) {

				if (temp.getScheduleKey() == schedule.getScheduleKey()) {
					repeated = true;
					break;
				}
			}

			if (!repeated && responsible) {

				list.add(schedule);
			}

		}

		return list;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int getPatrolScheduleCountByLastModifyTime(Timestamp lastModifyTime, Integer userAccountKey) {

		// List<PatrolSchedule> list = new ArrayList<PatrolSchedule>();
		//
		// List<PatrolSchedule> scheduleList = patrolScheduleDao
		// .searchPatrolScheduleByLastModifyTime(lastModifyTime,
		// userAccountKey);
		//
		// for (PatrolSchedule schedule : scheduleList) {
		// Boolean repeated = false;
		// Boolean responsible = false;
		//
		// List<PatrolScheduleAccount> accountList = schedule
		// .getScheduleAccount();
		// for (PatrolScheduleAccount ac : accountList) {
		//
		// if (ac.getAccountKey() == userAccountKey) {
		// responsible = true;
		// break;
		//// if(ac.getDeleted().equals("Y")){
		////
		//// break;
		//// }
		// }
		// }
		//
		// for (PatrolSchedule temp : list) {
		//
		// if (temp.getScheduleKey() == schedule.getScheduleKey()) {
		// repeated = true;
		// break;
		// }
		// }
		//
		// if (!repeated && responsible) {
		//
		// list.add(schedule);
		// }
		//
		// }

		return 0;

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolSchedule> searchPatrolScheduleDate(int siteKey, Timestamp startDate, Timestamp endDate) {

		List<PatrolSchedule> list = patrolScheduleDao.searchByDate(siteKey, startDate, endDate);

		List<PatrolSchedule> rtnList = new ArrayList<PatrolSchedule>();
		if (list != null) {

			for (PatrolSchedule tmp : list) {

				System.out.println("Selected Schedule Key : " + tmp.getScheduleKey());
				if (tmp.getScheduleEndDate() == null) {
					tmp.getScheduleAccount().get(0).getUserAccount().getName().length();
					rtnList.add(tmp);
				} else {
					if (tmp.getScheduleEndDate().getTime() >= endDate.getTime()) {
						tmp.getScheduleAccount().get(0).getUserAccount().getName().length();
						rtnList.add(tmp);
					}
				}

			}

		}

		return rtnList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void updatePatrolScheduleLastAttendTime(int scheduleKey, Timestamp lastAttendTime) {
		patrolScheduleDao.updateLastAttendTime(scheduleKey, lastAttendTime);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolSchedule> searchPatrolScheduleAfter(int routeDefKey, Calendar date) {
		// TODO Auto-generated method stub

		return patrolScheduleDao.searchPatrolScheduleAfter(routeDefKey, date);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolScheduleAccount> searchPatrolScheduleAccountByLastModifyTime(Timestamp lastModifyDate,
			Integer offset, Integer limit, Integer accountKey, Integer siteKey) {
		// TODO Auto-generated method stub
		return patrolScheduleAccountDao.searchByAccountKeyAndLastModifyDate(lastModifyDate,offset,limit,accountKey, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getPatrolScheduleAccountCount(Timestamp lastModifyDate, Integer accountKey, Integer siteKey) {
		// TODO Auto-generated method stub
		return patrolScheduleAccountDao.getCountByLastModifyDateAndAccount(lastModifyDate, accountKey, siteKey);
	}

	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public PatrolSchedule searchPatrolScheduleByKey(int scheduleKey) {
		// TODO Auto-generated method stub
		return patrolScheduleDao.searchByScheduleKey(scheduleKey);
	}
	
	/**                  **/
	/** Others Functions **/
	/**                  **/
	public PatrolScheduleDao getPatrolScheduleDao() {
		return patrolScheduleDao;
	}

	public void setPatrolScheduleDao(PatrolScheduleDao patrolScheduleDao) {
		this.patrolScheduleDao = patrolScheduleDao;
	}

	public PatrolScheduleAccountDao getPatrolScheduleAccountDao() {
		return patrolScheduleAccountDao;
	}

	public void setPatrolScheduleAccountDao(PatrolScheduleAccountDao patrolScheduleAccountDao) {
		this.patrolScheduleAccountDao = patrolScheduleAccountDao;
	}

	

}
