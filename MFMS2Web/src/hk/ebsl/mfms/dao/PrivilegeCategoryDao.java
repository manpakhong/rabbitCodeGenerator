package hk.ebsl.mfms.dao;

import java.util.List;

import hk.ebsl.mfms.dto.Privilege;
import hk.ebsl.mfms.dto.PrivilegeCategory;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.util.StringUtils;

public class PrivilegeCategoryDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PrivilegeCategoryDao.class);
	
	@SuppressWarnings("unchecked")
	public PrivilegeCategory getPrivilegeCategoryByCode(String code) {
		
		logger.debug("getPrivilegeCategoryByCode()[" + code + "]");
		
		List<PrivilegeCategory> list = getDefaultCriteria().add(Restrictions.eq("code", code)).list();
		
		if (list != null && list.size() > 0) {
			return list.get(0);
			
		} else {
			return null;
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PrivilegeCategory> searchPrivilegeCategory(String code, String categoryDesc, boolean topOnly, String orderBy, boolean asc, String deleted) {
		
		logger.debug("searchPrivilegeCategory()[" + code  + "," + categoryDesc + "," + topOnly + "," + orderBy + "," + asc + "," + deleted + "]");
		
		Criteria criteria = getDefaultCriteria();
		
		if (code != null) {
			criteria.add(Restrictions.ilike("code", DaoUtil.escape(code), MatchMode.ANYWHERE));
		}
		
		if (categoryDesc != null) {
			criteria.add(Restrictions.ilike("categoryDesc", DaoUtil.escape(categoryDesc), MatchMode.ANYWHERE));
		}
		
		if (topOnly) {
			criteria.add(Restrictions.eqOrIsNull("parentCode", "0"));
		}
		
		if (!StringUtils.isEmpty(orderBy)) {
			
			if (asc) {
				criteria.addOrder(Order.asc(orderBy));
			} else {
				criteria.addOrder(Order.desc(orderBy));
			}
		}
		
		if (deleted != null) {
			criteria.add(Restrictions.eq("deleted", deleted));
		}
		
		List<PrivilegeCategory> list = criteria.list();
		
		return list;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<PrivilegeCategory> getAllPrivilegeCategories() {
		
		logger.debug("getAllPrivilegeCategories()");
		
		List<PrivilegeCategory> list = getDefaultCriteria().list();
		
		return list;
		
	}
	
	
	private Criteria getDefaultCriteria() {
		
		Session currentSession = getSession();
		
		return currentSession.createCriteria(PrivilegeCategory.class);
	}
}
