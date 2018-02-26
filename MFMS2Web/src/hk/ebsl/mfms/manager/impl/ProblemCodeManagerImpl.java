package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.ProblemCodeDao;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.ProblemCodeManager;

public class ProblemCodeManagerImpl implements ProblemCodeManager {

	public static final Logger logger = Logger.getLogger(ProblemCodeManagerImpl.class);

	private ProblemCodeDao problemCodeDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<ProblemCode> getAllProblemCodeAndFailureClass() throws MFMSException {

		logger.debug("getAllProblemCodeAndFailureClass()");

		List<ProblemCode> list = problemCodeDao.getAllProblemCodeAndFailureClass();

		return list;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<ProblemCode> searchProblemCode(Integer siteKey, String code, String name, Integer parentKey, Integer priority) throws MFMSException {

		List<ProblemCode> list = problemCodeDao.searchProblemCode(siteKey, code, name, parentKey, priority, "N");

		return list;
	}

	public void test() {

	}

	public ProblemCodeDao getProblemCodeDao() {
		return problemCodeDao;
	}

	public void setProblemCodeDao(ProblemCodeDao problemCodeDao) {
		this.problemCodeDao = problemCodeDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveProblemCode(ProblemCode problemCode) throws MFMSException {

		problemCodeDao.saveProblemCode(problemCode);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public ProblemCode getProblemCodeByKey(Integer key) throws MFMSException {
		
		ProblemCode problemCode = problemCodeDao.getProblemCodeByKey(key);

		return problemCode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public ProblemCode getProblemCodeByName(Integer siteKey, String name) throws MFMSException {

		logger.debug("getByName()[" + siteKey + "," + name + "]");

		ProblemCode problemCode = problemCodeDao.getProblemCodeByName(siteKey, name, "N");
		
		return problemCode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public ProblemCode getProblemCodeByCode(Integer siteKey, String code) throws MFMSException {

		ProblemCode problemCode = problemCodeDao.getProblemCodeByCode(siteKey, code, "N");

		return problemCode;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteProblemCodeByKey(Integer accountKey, Integer problemCodeKey) throws MFMSException {

		logger.debug("deleteProblemCodeByKey()[" + accountKey + "," + problemCodeKey + "]");

		ProblemCode problemCode = problemCodeDao.getProblemCodeByKey(problemCodeKey);

		if (problemCode == null) {
			throw new MFMSException("ProblemCode not found");

		}
		problemCode.setDeleted("Y");
		problemCode.setLastModifyBy(accountKey);
		problemCode.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

		saveProblemCode(problemCode);
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<ProblemCode> searchAllProblemCodeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey){
		
		logger.debug("searchProblemCode()[" + lastModifiedDate + "]");
		
		List<ProblemCode> problemCodeList = problemCodeDao.searchProblemCodeByDate(lastModifiedDate, offset, maxResults, siteKey);
		
		return problemCodeList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = problemCodeDao.theCountOfSearchResult(lastModifiedDate, siteKey);
		
		return theCountoftotalResult;
	}

}
