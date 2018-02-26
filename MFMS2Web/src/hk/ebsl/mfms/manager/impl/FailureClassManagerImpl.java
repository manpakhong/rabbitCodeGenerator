package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.FailureClassDao;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.FailureClassManager;

public class FailureClassManagerImpl implements FailureClassManager {

	public static final Logger logger = Logger.getLogger(FailureClassManagerImpl.class);

	private FailureClassDao failureClassDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FailureClass> getAllFailureClass() throws MFMSException {

		logger.debug("getAllFailureClass()");

		List<FailureClass> list = failureClassDao.getAllFailureClass();

		return list;
	}
	

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FailureClass> getFailureClassBySiteKey(Integer siteKey) throws MFMSException {

		List<FailureClass> list = failureClassDao.getFailureClassBySiteKey( siteKey);

		return list;
	}


	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FailureClass> searchFailureClass(Integer siteKey, String code, String name)
			throws MFMSException {

		logger.debug("searchFailureClass()[" + siteKey + "," + code + "," + name + "]");

		List<FailureClass> list = failureClassDao.searchFailureClass(siteKey, code, name, "N");

		return list;
	}

	public void test() {

	}

	public FailureClassDao getFailureClassDao() {
		return failureClassDao;
	}

	public void setFailureClassDao(FailureClassDao failureClassDao) {
		this.failureClassDao = failureClassDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveFailureClass(FailureClass failureClass) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("saveFailureClass()[" + failureClass.getKey());
		logger.debug("saveFailureClass()[" + failureClass.getSiteKey());
		logger.debug("saveFailureClass()[" + failureClass.getCode());
		logger.debug("saveFailureClass()[" + failureClass.getDeleted() + "]");

		failureClassDao.saveFailureClass(failureClass);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public FailureClass getFailureClassByKey(Integer key) throws MFMSException {

		FailureClass failureClass = failureClassDao.getFailureClassByKey(key);

		return failureClass;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public FailureClass getFailureClassByName(Integer siteKey, String name, boolean fetch) throws MFMSException {

		FailureClass failureClass = failureClassDao.getFailureClassByName(siteKey, name, "N");

		return failureClass;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public FailureClass getFailureClassByCode(Integer siteKey, String code, boolean fetch) throws MFMSException {

		logger.debug("getRoleByCode()[" + siteKey + "," + code + "," + fetch + "]");

		FailureClass failureClass = failureClassDao.getFailureClassByCode(siteKey, code, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return failureClass;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteFailureClassByKey(Integer accountKey, Integer failureClassKey) throws MFMSException {

		logger.debug("deleteFailureClassByKey()[" + accountKey + "," + failureClassKey + "]");

		FailureClass failureClass = failureClassDao.getFailureClassByKey(failureClassKey);

		if (failureClass == null) {
			throw new MFMSException("FailureClass not found");

		}
		failureClass.setDeleted("Y");
		failureClass.setLastModifyBy(accountKey);
		failureClass.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveFailureClass(failureClass);
	}

}
