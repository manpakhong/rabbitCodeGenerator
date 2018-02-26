package hk.ebsl.mfms.manager.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.SystemParamDao;
import hk.ebsl.mfms.dto.SystemParam;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.SystemParamManager;

public class SystemParamManagerImpl implements SystemParamManager {

	public static final Logger logger = Logger.getLogger(SystemParamManagerImpl.class);

	private SystemParamDao systemParamDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<SystemParam> getAllSystemParam() throws MFMSException {

		return systemParamDao.getAllSystemParam();
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public SystemParam getSystemParamByKey(String id, Integer siteKey) throws MFMSException {

		return systemParamDao.getSystemParamByKey(id, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public String getSystemParamValueByKey(String id, Integer siteKey) throws MFMSException {

		return systemParamDao.getSystemParamValueByKey(id, siteKey);
	}

	public SystemParamDao getSystemParamDao() {
		return systemParamDao;
	}

	public void setSystemParamDao(SystemParamDao systemParamDao) {
		this.systemParamDao = systemParamDao;
	}

	public static Logger getLogger() {
		return logger;
	}

}
