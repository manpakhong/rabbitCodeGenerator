package hk.ebsl.mfms.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.DefectEmailDao;
import hk.ebsl.mfms.dto.DefectEmail;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectEmailManager;

public class DefectEmailManagerImpl implements DefectEmailManager {

	
	public static final Logger logger = Logger.getLogger(DefectEmailManager.class);
	
	
	private DefectEmailDao defectEmailDao;
	
	@Override
	@Transactional(rollbackFor={MFMSException.class})
	public List<DefectEmail> searchDefectEmail(Integer defectKey, String email,
			String type, String result) throws MFMSException {
		
		logger.debug("searchDefectEmail()[" + defectKey + "," + email + "," + type + "," + result+ "]");
		try {
			List<DefectEmail> list = defectEmailDao.searchDefectEmail(defectKey, email, type, result);
			return list;
		} catch (Exception e) {
			logger.debug("Failed to search Defect Email");
			throw new MFMSException(e);
		}
		
	}

	@Override
	@Transactional(rollbackFor={MFMSException.class})
	public void saveDefectEmail(DefectEmail defectEmail) throws MFMSException {
		logger.debug("saveDefectEmail()[" + defectEmail.getDefectKey() + "," + defectEmail.getType() + "," + defectEmail.getEmail() + "]");
		try {
			defectEmailDao.saveDefectEmail(defectEmail);
		} catch (Exception e) {
			logger.debug("Failed to save Defect Email");
			throw new MFMSException(e);
		}

	}

	public DefectEmailDao getDefectEmailDao() {
		return defectEmailDao;
	}

	public void setDefectEmailDao(DefectEmailDao defectEmailDao) {
		this.defectEmailDao = defectEmailDao;
	}

}
