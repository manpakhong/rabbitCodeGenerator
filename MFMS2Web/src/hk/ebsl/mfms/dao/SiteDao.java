package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;
import hk.ebsl.mfms.dto.Site;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class SiteDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(SiteDao.class);

	@SuppressWarnings("unchecked")
	public List<Site> getAllSites() {

		logger.debug("getAllSites()");

		return getDefaultCriteria().list();

	}

	@SuppressWarnings("unchecked")
	public Site getSiteByKey(Integer key) {

		logger.debug("getSiteByKey()[" + key + "]");

		List<Site> list = getDefaultCriteria().add(Restrictions.eq("key", key)).list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public Site getSiteByName(String name, String deleted) {

		logger.debug("getSiteByName()[" + name + "," + deleted + "]");

		Criteria criteria = getDefaultCriteria().add(Restrictions.eq("name", name));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<Site> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public List<Site> searchSite(String name, String address, Integer contactCountryCode, Integer contactAreaCode,
			Integer contactNumber, String deleted) {

		logger.debug("searchSite()[" + name + "," + address + "," + contactCountryCode + "," + contactAreaCode + ","
				+ contactNumber + "," + deleted + "]");

		Criteria criteria = getDefaultCriteria().add(Restrictions.ne("key", 1));

		if (name != null) {
			criteria.add(Restrictions.ilike("name", DaoUtil.escape(name), MatchMode.ANYWHERE));
		}

		if (address != null) {
			criteria.add(Restrictions.ilike("address", DaoUtil.escape(address), MatchMode.ANYWHERE));
		}

		if (contactCountryCode != null) {
			criteria.add(Restrictions.eq("contactCountryCode", contactCountryCode));
		}

		if (contactAreaCode != null) {
			criteria.add(Restrictions.eq("contactAreaCode", contactAreaCode));
		}

		if (contactNumber != null) {
			criteria.add(Restrictions.eq("contactNumber", contactNumber));
		}

		if (deleted != null) {
			criteria.add(Restrictions.eq("deleted", deleted));
		}

		List<Site> siteList = criteria.list();

		return siteList;
	}

	@SuppressWarnings("unchecked")
	public List<Site> searchSite(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Site.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		List<Site> siteList = criteria.list();

		return siteList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Site.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	public void saveSite(Site site) {

		logger.debug("saveSite()[" + site.getName() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(site);
	}

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		return currentSession.createCriteria(Site.class);
	}
}
