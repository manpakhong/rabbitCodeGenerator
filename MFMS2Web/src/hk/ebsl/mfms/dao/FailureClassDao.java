package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.FailureClass;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class FailureClassDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(FailureClassDao.class);

	@SuppressWarnings("unchecked")
	public List<FailureClass> getAllFailureClass() {

		logger.debug("getAllFailureClass()");

		return getDefaultFailureClassCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public FailureClass getFailureClassByKey(Integer key) {
		logger.debug("getFailureClassByKey()[" + key + "]");

		Criteria criteria = getDefaultFailureClassCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<FailureClass> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<FailureClass> getFailureClassBySiteKey(Integer siteKey) {

		Criteria criteria = getDefaultFailureClassCriteria();
		criteria.add(Restrictions.eq("siteKey", siteKey));
		List<FailureClass> list = criteria.list();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<FailureClass> searchFailureClass(Integer siteKey, String code, String name, String deleted) {

		logger.debug("searchFailureClass()[" + siteKey + "," + code + "," + name + "," + deleted + "]");

		Criteria criteria = getDefaultFailureClassCriteria();

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
		List<FailureClass> list = criteria.list();

		return list;

	}

	public void saveFailureClass(FailureClass failureClass) {
		logger.debug("saveFailureClass()[" + failureClass.getKey() + "," + failureClass.getSiteKey() + ","
				+ failureClass.getCode() + "," + failureClass.getDeleted() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(failureClass);
	}

	@SuppressWarnings("unchecked")
	public FailureClass getFailureClassByName(Integer siteKey, String name, String deleted) {

		logger.debug("getFailureClassByName()[" + siteKey + "," + name + "]");

		Criteria criteria = getDefaultFailureClassCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("name", name));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<FailureClass> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public FailureClass getFailureClassByCode(Integer siteKey, String code, String deleted) {

		logger.debug("getFailureClassByCode()[" + siteKey + "," + code + "]");

		Criteria criteria = getDefaultFailureClassCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("code", code));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<FailureClass> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	private Criteria getDefaultFailureClassCriteria() {

		// default criteria for failureClass
		Session currentSession = getSession();
		return currentSession.createCriteria(FailureClass.class).add(Restrictions.eq("levelKey", 1)).add(Restrictions.eq("deleted", "N"));
	}

}
