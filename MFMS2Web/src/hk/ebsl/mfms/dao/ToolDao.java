package hk.ebsl.mfms.dao;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.web.controller.DefectController.ToolComparator;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ToolDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(ToolDao.class);

	@SuppressWarnings("unchecked")
	public List<Tool> getAllTool() {

		logger.debug("getAllSites()");
		
		return getDefaultToolCriteria().list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Tool> getToolBySiteKey(Integer siteKey) {

		Criteria criteria = getDefaultToolCriteria();
		criteria.add(Restrictions.eq("siteKey", siteKey));
		List<Tool> list = criteria.list();
		
		Collections.sort(list, new ToolComparator());
		
		return list;
	}


	@SuppressWarnings("unchecked")
	public Tool getToolByKey(Integer key) {
		logger.debug("getToolByKey()[" + key + "]");

		Criteria criteria = getDefaultToolCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<Tool> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Tool> searchTool(Integer siteKey, String code, String name, String deleted) {

		logger.debug("searchTool()[" + siteKey + "," + code + "," + name + "," + deleted + "]");

		Criteria criteria = getDefaultToolCriteria();

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
		List<Tool> list = criteria.list();

		return list;

	}
	
	public void saveTool(Tool tool) {
		logger.debug("saveTool()[" + tool.getKey() + "," + tool.getSiteKey() + ","
				+ tool.getCode() + "," + tool.getDeleted() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(tool);
	}
	
	@SuppressWarnings("unchecked")
	public Tool getToolByName(Integer siteKey, String name, String deleted) {
		
		logger.debug("getToolByName()[" + siteKey + "," + name + "]");
		
		Criteria criteria = getDefaultToolCriteria().add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("name", name));
		
		if (deleted != null) criteria.add(Restrictions.eq("deleted", deleted));
		
		List<Tool> list = criteria.list();
		
		if (list != null && list.size() > 0) 
			return list.get(0);
		else 
			return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public Tool getToolByCode(Integer siteKey, String code, String deleted) {
		
		logger.debug("getToolByCode()[" + siteKey + "," + code + "]");
		
		Criteria criteria = getDefaultToolCriteria().add(Restrictions.eq("siteKey", siteKey)).add(Restrictions.eq("code", code));
		
		if (deleted != null) criteria.add(Restrictions.eq("deleted", deleted));
		
		List<Tool> list = criteria.list();
		
		if (list != null && list.size() > 0) 
			return list.get(0);
		else 
			return null;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Tool> searchTool(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Tool.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<Tool> toolList = criteria.list();

		return toolList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Tool.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.ge("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	private Criteria getDefaultToolCriteria() {

		// default criteria for tool
		Session currentSession = getSession();
		
		return currentSession.createCriteria(Tool.class).add(Restrictions.eq("deleted", "N"));
	}
	
	public class ToolComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

}
