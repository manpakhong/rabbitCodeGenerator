package hk.ebsl.mfms.dao;

import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.ProblemCode;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class ProblemCodeDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(ProblemCodeDao.class);

	@SuppressWarnings("unchecked")
	public List<ProblemCode> getAllProblemCodeAndFailureClass() {

		List<ProblemCode> pcList = new ArrayList<ProblemCode>();

		pcList = getDefaultProblemCodeCriteria().list();

		List<FailureClass> fcList = new ArrayList<FailureClass>();

		fcList.addAll(getDefaultFailureClassCriteria().list());

		for (FailureClass fc : fcList) {
			ProblemCode pc = new ProblemCode();

			pc.setKey(fc.getKey());
			pc.setSiteKey(fc.getSiteKey());
			pc.setCode(fc.getCode());
			pc.setName(fc.getName());
			pc.setDesc(fc.getDesc());
			pc.setParentKey(0);
			pc.setLevelKey(1);
			pc.setCreateDateTime(fc.getCreateDateTime());
			pc.setCreatedBy(fc.getCreatedBy());
			pc.setLastModifyBy(fc.getLastModifyBy());
			pc.setLastModifyDateTime(fc.getLastModifyDateTime());
			pc.setDefaultAccountKey(-1);
			pc.setDeleted(fc.getDeleted());

			pcList.add(pc);

		}

		return pcList;
	}

	@SuppressWarnings("unchecked")
	public ProblemCode getProblemCodeByKey(Integer key) {
		logger.debug("getProblemCodeByKey()[" + key + "]");

		Criteria criteria = getDefaultProblemCodeCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<ProblemCode> list = criteria.list();

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProblemCode> searchProblemCode(Integer siteKey, String code, String name, Integer parentKey,
			Integer priority, String deleted) {

		logger.debug("searchProblemCode()[" + siteKey + "," + code + "," + name + "," + deleted + "]");

		Criteria criteria = getDefaultProblemCodeCriteria();

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
		if (parentKey != null) {
			criteria.add(Restrictions.eq("parentKey", parentKey));
		}
		if (priority != null) {
			criteria.add(Restrictions.eq("defaultPriority", priority));
		}
		if (deleted != null) {
			// case insensitive
			criteria.add(Restrictions.eq("deleted", deleted));
		}
		List<ProblemCode> list = criteria.list();

		return list;

	}

	public void saveProblemCode(ProblemCode problemCode) {
		logger.debug("saveProblemCode()[" + problemCode.getKey() + "," + problemCode.getSiteKey() + ","
				+ problemCode.getCode() + "," + problemCode.getDeleted() + "]");

		Session currentSession = getSession();

		currentSession.saveOrUpdate(problemCode);
	}

	@SuppressWarnings("unchecked")
	public ProblemCode getProblemCodeByName(Integer siteKey, String name, String deleted) {

		logger.debug("getProblemCodeByName()[" + siteKey + "," + name + "]");

		Criteria criteria = getDefaultProblemCodeCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("name", name));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<ProblemCode> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}

	@SuppressWarnings("unchecked")
	public ProblemCode getProblemCodeByCode(Integer siteKey, String code, String deleted) {

		logger.debug("getProblemCodeByCode()[" + siteKey + "," + code + "]");

		Criteria criteria = getDefaultProblemCodeCriteria().add(Restrictions.eq("siteKey", siteKey))
				.add(Restrictions.eq("code", code));

		if (deleted != null)
			criteria.add(Restrictions.eq("deleted", deleted));

		List<ProblemCode> list = criteria.list();

		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;

	}
	
	@SuppressWarnings("unchecked")
	public List<ProblemCode> searchProblemCodeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey) {
			Session currentSession = getSession();
			
			//have to create criteria with FailureClass by some Hibernate settings
			Criteria criteria = currentSession.createCriteria(FailureClass.class);
			if (lastModifiedDate != null) {
				criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
			}
			if(siteKey != null){
				criteria.add(Restrictions.eq("siteKey", siteKey));
			}
			
			criteria.setFirstResult(offset);
			criteria.setMaxResults(maxResults);
			
			List<ProblemCode> pcList = new ArrayList<ProblemCode>();
			List<FailureClass> fcList = new ArrayList<FailureClass>(criteria.list());

			for (FailureClass fc : fcList) {
				ProblemCode pc = new ProblemCode();

				pc.setKey(fc.getKey());
				pc.setSiteKey(fc.getSiteKey());
				pc.setCode(fc.getCode());
				pc.setName(fc.getName());
				pc.setDesc(fc.getDesc());
				pc.setParentKey(fc.getParentKey());
				pc.setLevelKey(fc.getLevelKey());
				pc.setCreateDateTime(fc.getCreateDateTime());
				pc.setCreatedBy(fc.getCreatedBy());
				pc.setLastModifyBy(fc.getLastModifyBy());
				pc.setLastModifyDateTime(fc.getLastModifyDateTime());
				pc.setDefaultPriority(fc.getDefaultPriority());
				pc.setDefaultAccountKey(fc.getDefaultAccountKey());
				pc.setDeleted(fc.getDeleted());
				pc.setLastModifyTimeForSync(fc.getLastModifyTimeForSync());
				pcList.add(pc);

			}
			
			Collections.sort(pcList, new Comparator<ProblemCode>(){
				@Override
				public int compare(ProblemCode pc1, ProblemCode pc2) {
					
					return pc1.getKey().compareTo(pc2.getKey());
				}
				
			});

			return pcList;
	}
	
	
//	@SuppressWarnings("unchecked")
//	public List<ProblemCode> searchProblemCodeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
//		Session currentSession = getSession();
//
////		Criteria criteria = currentSession.createCriteria(ProblemCode.class).add(Restrictions.ne("levelKey", 1));
//		Criteria criteria = currentSession.createCriteria(ProblemCode.class);
//		criteria.setFirstResult(offset);
//		criteria.setMaxResults(maxResults);
//
//		if (lastModifiedDate != null) {
//			criteria.add(Restrictions.ge("lastModifyDateTime", lastModifiedDate));
//		}
//		List<ProblemCode> problemCodeList = criteria.list();
//
//		return problemCodeList;
//	}

	public Integer theCountOfSearchResult(Timestamp lastModifiedDate, Integer siteKey) {
		Session currentSession = getSession();

//		Criteria criteria = currentSession.createCriteria(ProblemCode.class).add(Restrictions.ne("levelKey", 1));
		Criteria criteria = currentSession.createCriteria(ProblemCode.class);
		if (lastModifiedDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifiedDate));
		}
		
		if(siteKey != null){
			criteria.add(Restrictions.eq("siteKey", siteKey));
		}
		
		Integer totalResult = ((Number)criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
		return totalResult;
	}

	private Criteria getDefaultProblemCodeCriteria() {

		// default criteria for problemCode
		Session currentSession = getSession();
		return currentSession.createCriteria(ProblemCode.class).add(Restrictions.ne("levelKey", 1))
				.add(Restrictions.eq("deleted", "N"));
	}

	private Criteria getDefaultFailureClassCriteria() {

		// default criteria for problemCode
		Session currentSession = getSession();
		return currentSession.createCriteria(FailureClass.class).add(Restrictions.eq("levelKey", 1))
				.add(Restrictions.eq("deleted", "N"));
	}

}
