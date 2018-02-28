package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface ProblemCodeManager {

	public List<ProblemCode> getAllProblemCodeAndFailureClass() throws MFMSException;

	public List<ProblemCode> searchProblemCode(Integer siteKey, String code, String name, Integer parentKey,
			Integer priority) throws MFMSException;

	public void saveProblemCode(ProblemCode problemCode) throws MFMSException;

	public ProblemCode getProblemCodeByKey(Integer key) throws MFMSException;

	public ProblemCode getProblemCodeByName(Integer key, String name) throws MFMSException;

	public ProblemCode getProblemCodeByCode(Integer key, String code) throws MFMSException;

	public void deleteProblemCodeByKey(Integer accountKey, Integer toolKey) throws MFMSException;

	public List<ProblemCode> searchAllProblemCodeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);
}
