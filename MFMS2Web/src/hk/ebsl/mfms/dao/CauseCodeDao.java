package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Site;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class CauseCodeDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(CauseCodeDao.class);

	@SuppressWarnings("unchecked")
	public List<CauseCode> getAllCauseCode() {

		logger.debug("getAllCauseCode()");

		return getDefaultCauseCodeCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public List<CauseCode> getCauseCodeBySiteKey(Integer siteKey) {

		Criteria criteria = getDefaultCauseCodeCriteria();
		criteria.add(Restrictions.eq("siteKey", siteKey));
		List<CauseCode> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public CauseCode getCauseCodeByKey(Integer key) {
		logger.debug("getCauseCodeByKey()[" + key + "]");

		Criteria criteria = getDefaultCauseCodeCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<CauseCode> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CauseCode> searchCauseCode(Integer siteKey, String code, String name, String deleted) {

		logger.debug("searchCauseCode()[" + siteKey + "," + code + "," + name + "," + deleted + "]");

		Criteria criteria = getDefaultCauseCodeCriteria();

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		if (code != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("code", DaoUtil.escape(code), MatchMode.ANYWHERE));
		}
		if (name != null) {
			// case insensitive
			criteria.add(Restrictions.ilike("name", DaoUtil.escape(name), MatchMode.ANYWHERE));
		}
		if (deleted != null) {
			// case insensitive
			criteria.add(Restrictions.eq("deleted", deleted));
		}
		List<CauseCode> list = criteria.list();

		return list;

	}

	public void saveCauseCode(CauseCode causeCode) {

		logger.debug("saveCauseCode()[" + causeCode.getKey() + "," + causeCode.getSiteKey() + "," + causeCode.getCode()
				+ "," + causeCode.getDeleted() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(causeCode);
	}

	@SuppressWarnings("unchecked")
	public CauseCode getCauseCodeByName(Integer siteKey, String name, String deleted) {

		logger.debug("getCauseCodeByName()[" + siteKey + "," + name + "]");

		Criteria criteria = getDefaultCauseCodeCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("name", name));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<CauseCode> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public CauseCode getCauseCodeByCode(Integer siteKey, String code, String deleted) {

		logger.debug("getCauseCodeByCode()[" + siteKey + "," + code + "]");

		Criteria criteria = getDefaultCauseCodeCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("code", code));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<CauseCode> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<CauseCode> searchCauseCode(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(CauseCode.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey",siteKey));
		}
		
		
		List<CauseCode> causeCodeList = criteria.list();

		return causeCodeList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(CauseCode.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey",siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	
	

	private Criteria getDefaultCauseCodeCriteria() {

		// default criteria for cause code
		Session currentSession = getSession();
		return currentSession.createCriteria(CauseCode.class).add(Restrictions.eq("deleted", "N"));
	}

}
