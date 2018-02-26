package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import hk.ebsl.mfms.dao.AccountGroupAccountDao;
import hk.ebsl.mfms.dao.DefectActionLogDao;
import hk.ebsl.mfms.dao.UserAccountDao;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectActionLogManager;

public class DefectActionLogManagerImpl implements DefectActionLogManager {

	public static final Logger logger = Logger.getLogger(DefectActionLogManagerImpl.class);

	private DefectActionLogDao defectActionLogDao;

	private UserAccountDao userAccountDao;

	private AccountGroupAccountDao accountGroupAccountDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectActionLog> searchDefectActionLog(Integer siteKey, Integer accountKey, String code, Timestamp from,
			Timestamp to, Integer groupKey) throws MFMSException {
		
		List<DefectActionLog> list = defectActionLogDao.searchDefect(siteKey, accountKey, code, from, to);
		
		if (groupKey != null && accountKey == null) {
			List<DefectActionLog> grouplist = new ArrayList<DefectActionLog>();

			for (DefectActionLog log : list) {

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
	public Integer saveDefectActionLog(DefectActionLog defectActionLog) throws MFMSException {
		// TODO Auto-generated method stub
		return defectActionLogDao.saveDefectActionLog(defectActionLog);

	}

	public void test() {

	}

	public DefectActionLogDao getDefectActionLogDao() {
		return defectActionLogDao;
	}

	public void setDefectActionLogDao(DefectActionLogDao defectActionLogDao) {
		this.defectActionLogDao = defectActionLogDao;
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
