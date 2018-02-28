package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.AccountGroupAccountDao;
import hk.ebsl.mfms.dao.DefectScheduleActionLogDao;
import hk.ebsl.mfms.dao.UserAccountDao;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.DefectScheduleActionLog;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectScheduleActionLogManager;

public class DefectScheduleActionLogManagerImpl implements DefectScheduleActionLogManager {

	public static final Logger logger = Logger.getLogger(DefectScheduleActionLogManagerImpl.class);

	private DefectScheduleActionLogDao defectScheduleActionLogDao;

	private UserAccountDao userAccountDao;

	private AccountGroupAccountDao accountGroupAccountDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectScheduleActionLog> searchDefectScheduleActionLog(Integer siteKey, Integer accountKey,
			Timestamp from, Timestamp to, Integer groupKey) throws MFMSException {

		List<DefectScheduleActionLog> list = defectScheduleActionLogDao.searchDefectScheduleActionLog(siteKey,
				accountKey, from, to);

		if (groupKey != null && accountKey == null) {
			List<DefectScheduleActionLog> grouplist = new ArrayList<DefectScheduleActionLog>();

			for (DefectScheduleActionLog log : list) {

				UserAccount ac = userAccountDao.getUserAccountByKey(log.getActionBy());

				if (ac != null) {

					AccountGroupAccount aga = accountGroupAccountDao.getAccountGroupAccount(groupKey, ac.getKey());

					if (aga != null)
						grouplist.add(log);

				}

			}

			return grouplist;

		} else

			return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer saveDefectScheduleActionLog(DefectScheduleActionLog defectScheduleActionLog) throws MFMSException {
		// TODO Auto-generated method stub
		return defectScheduleActionLogDao.saveDefectScheduleActionLog(defectScheduleActionLog);

	}

	public DefectScheduleActionLogDao getDefectScheduleActionLogDao() {
		return defectScheduleActionLogDao;
	}

	public void setDefectScheduleActionLogDao(DefectScheduleActionLogDao defectScheduleActionLogDao) {
		this.defectScheduleActionLogDao = defectScheduleActionLogDao;
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

	public void setAccountGroupAccountDao(AccountGroupAccountDao accountGroupAccountDao) {
		this.accountGroupAccountDao = accountGroupAccountDao;
	}
}
