package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.CauseCodeDao;
import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.CauseCodeManager;

public class CauseCodeManagerImpl implements CauseCodeManager {

	public static final Logger logger = Logger.getLogger(CauseCodeManagerImpl.class);

	private CauseCodeDao causeCodeDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<CauseCode> getAllCauseCode() throws MFMSException {

		logger.debug("getAllCauseCode()");

		List<CauseCode> list = causeCodeDao.getAllCauseCode();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<CauseCode> getCauseCodeBySiteKey(Integer siteKey) throws MFMSException {

		List<CauseCode> list = causeCodeDao.getCauseCodeBySiteKey(siteKey);

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<CauseCode> searchCauseCode(Integer siteKey, String code, String name) throws MFMSException {

		List<CauseCode> list = causeCodeDao.searchCauseCode(siteKey, code, name, "N");

		return list;
	}

	public void test() {

	}

	public CauseCodeDao getCauseCodeDao() {
		return causeCodeDao;
	}

	public void setCauseCodeDao(CauseCodeDao causeCodeDao) {
		this.causeCodeDao = causeCodeDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveCauseCode(CauseCode causeCode) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("causeCode()[" + causeCode.getKey());
		logger.debug("causeCode()[" + causeCode.getSiteKey());
		logger.debug("causeCode()[" + causeCode.getCode());
		logger.debug("causeCode()[" + causeCode.getDeleted() + "]");

		causeCodeDao.saveCauseCode(causeCode);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public CauseCode getCauseCodeByKey(Integer key) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("getCauseCodeByKey()[" + key + "]");

		CauseCode causeCode = causeCodeDao.getCauseCodeByKey(key);

		return causeCode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public CauseCode getCauseCodeByName(Integer siteKey, String name, boolean fetch) throws MFMSException {

		logger.debug("getRoleByName()[" + siteKey + "," + name + "," + fetch + "]");

		CauseCode causeCode = causeCodeDao.getCauseCodeByName(siteKey, name, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return causeCode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public CauseCode getCauseCodeByCode(Integer siteKey, String code, boolean fetch) throws MFMSException {

		logger.debug("getRoleByCode()[" + siteKey + "," + code + "," + fetch + "]");

		CauseCode causeCode = causeCodeDao.getCauseCodeByCode(siteKey, code, "N");

		// if (fetch) {
		//
		// // fetch lazyload objects;
		// if (role != null && role.getRolePrivileges() != null)
		// role.getRolePrivileges().size();
		// }
		return causeCode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteCauseCodeByKey(Integer accountKey, Integer causeCodeKey) throws MFMSException {

		logger.debug("deleteCauseCodeByKey()[" + accountKey + "," + causeCodeKey + "]");

		CauseCode causeCode = causeCodeDao.getCauseCodeByKey(causeCodeKey);

		if (causeCode == null) {
			throw new MFMSException("CauseCode not found");

		}
		causeCode.setDeleted("Y");
		causeCode.setLastModifyBy(accountKey);
		causeCode.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveCauseCode(causeCode);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<CauseCode> searchCauseCodeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults,Integer siteKey) {
		logger.debug("searchCauseCode()[" + lastModifiedDate + "]");

		List<CauseCode> causeCodeList = causeCodeDao.searchCauseCode(lastModifiedDate, offset, maxResults, siteKey);

		return causeCodeList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey) {
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");

		Integer theCountoftotalResult = causeCodeDao.theCountOfSearchResult(lastModifiedDate, siteKey);

		return theCountoftotalResult;
	}

}
