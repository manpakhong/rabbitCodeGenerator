package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.web.controller.DefectController.EquipmentComparator;

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

public class EquipmentDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(EquipmentDao.class);

	@SuppressWarnings("unchecked")
	public List<Equipment> getAllEquipment() {

		return getDefaultEquipmentCriteria().list();
	}

	@SuppressWarnings("unchecked")
	public List<Equipment> getEquipmentBySiteKey(Integer siteKey) {

		Criteria criteria = getDefaultEquipmentCriteria();
		criteria.add(Restrictions.eq("siteKey", siteKey));
		List<Equipment> list = criteria.list();
		
		Collections.sort(list, new EquipmentComparator());
		
		return list;
	}

	@SuppressWarnings("unchecked")
	public Equipment getEquipmentByKey(Integer key) {
		logger.debug("getEquipmentByKey()[" + key + "]");

		Criteria criteria = getDefaultEquipmentCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<Equipment> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Equipment> searchEquipment(Integer siteKey, Integer locationKey, String code, String name,
			String deleted) {

		logger.debug("searchEquipment()[" + siteKey + "," + code + "," + name + "," + deleted + "]");

		Criteria criteria = getDefaultEquipmentCriteria();

		if (siteKey != null) {
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}

		if (locationKey != null) {
			criteria.add(Restrictions.eq("locationKey", locationKey));
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
		List<Equipment> list = criteria.list();
		
		Collections.sort(list, new EquipmentComparator());

		return list;

	}

	@SuppressWarnings("unchecked")
	public boolean hasEquipment(Integer locationKey) {

		Criteria criteria = getDefaultEquipmentCriteria();

		if (locationKey != null) {
			criteria.add(Restrictions.eq("locationKey", locationKey));
		}

		List<Equipment> list = criteria.list();

		if (list.size() > 0)
			return true;
		else
			return false;

	}

	public void saveEquipment(Equipment equipment) {
		logger.debug("saveEquipment()[" + equipment.getKey() + "," + equipment.getSiteKey() + "," + equipment.getCode()
				+ "," + equipment.getDeleted() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(equipment);
	}

	@SuppressWarnings("unchecked")
	public Equipment getEquipmentByName(Integer siteKey, String name, String deleted) {

		logger.debug("getEquipmentByName()[" + siteKey + "," + name + "]");

		Criteria criteria = getDefaultEquipmentCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("name", name));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<Equipment> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public Equipment getEquipmentByCode(Integer siteKey, String code, String deleted) {

		logger.debug("getEquipmentByCode()[" + siteKey + "," + code + "]");

		Criteria criteria = getDefaultEquipmentCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("code", code));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<Equipment> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<Equipment> searchEquipment(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Equipment.class);
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResults);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		List<Equipment> equipmentList = criteria.list();

		return equipmentList;
	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(Equipment.class);

		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}
	
	

	private Criteria getDefaultEquipmentCriteria() {

		// default criteria for equipment
		Session currentSession = getSession();
		return currentSession.createCriteria(Equipment.class).add(Restrictions.eq("deleted", "N"));
	}
	
	public class EquipmentComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

}
