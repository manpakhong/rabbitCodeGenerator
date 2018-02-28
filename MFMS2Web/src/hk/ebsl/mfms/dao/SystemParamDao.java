package hk.ebsl.mfms.dao;

import java.util.List;

import hk.ebsl.mfms.dto.SystemParam;
import hk.ebsl.mfms.dto.SystemParamId;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class SystemParamDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(SystemParamDao.class);

	@SuppressWarnings("unchecked")
	public List<SystemParam> getAllSystemParam() {

		logger.debug("getAllSystemParam()");

		List<SystemParam> list = getDefaultStatusCriteria().list();

		return list;
	}

	@SuppressWarnings("unchecked")
	public SystemParam getSystemParamByKey(String id, Integer siteKey) {

		logger.debug("getSystemParamByKey()[" + id + "," + siteKey + "]");

		Criteria criteria = getDefaultStatusCriteria();

		SystemParamId key = new SystemParamId();
		key.setId(id);
		key.setSiteKey(siteKey);

		criteria.add(Restrictions.eq("id", key));

		List<SystemParam> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public String getSystemParamValueByKey(String id, Integer siteKey) {

		logger.debug("getSystemParamValueByKey()[" + id + "," + siteKey + "]");

		Criteria criteria = getDefaultStatusCriteria();

		SystemParamId key = new SystemParamId();
		key.setId(id);
		key.setSiteKey(siteKey);

		criteria.add(Restrictions.eq("id", key));

		List<SystemParam> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0).getValue();
		} else {
			return null;
		}

	}

	private Criteria getDefaultStatusCriteria() {
		// default criteria for status
		Session currentSession = getSession();
		return currentSession.createCriteria(SystemParam.class);
	}
}
