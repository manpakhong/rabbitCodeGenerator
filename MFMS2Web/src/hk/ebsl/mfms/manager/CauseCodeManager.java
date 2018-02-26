package hk.ebsl.mfms.manager;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.exception.MFMSException;

import java.sql.Timestamp;
import java.util.List;

public interface CauseCodeManager {

	public List<CauseCode> getAllCauseCode() throws MFMSException;

	public List<CauseCode> getCauseCodeBySiteKey(Integer siteKey) throws MFMSException;

	public List<CauseCode> searchCauseCode(Integer siteKey, String code, String name) throws MFMSException;

	public void saveCauseCode(CauseCode causeCode) throws MFMSException;

	public CauseCode getCauseCodeByKey(Integer key) throws MFMSException;

	public CauseCode getCauseCodeByName(Integer key, String name, boolean fetch) throws MFMSException;

	public CauseCode getCauseCodeByCode(Integer key, String code, boolean fetch) throws MFMSException;

	public void deleteCauseCodeByKey(Integer accountKey, Integer toolKey) throws MFMSException;

	public List<CauseCode> searchCauseCodeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults,
			Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate, Integer siteKey);

}
