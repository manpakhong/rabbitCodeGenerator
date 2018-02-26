package hk.ebsl.mfms.dao;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.PatrolPhoto;
import hk.ebsl.mfms.dto.RolePrivilege;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class PatrolPhotoDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(PatrolPhotoDao.class);

	private String dto_routeDefkey = "routeDefkey";
	private String dto_createDateTime = "createDateTime";

	public int save(PatrolPhoto pp) {
		Session currentSession = getSession();

		currentSession.saveOrUpdate(pp);
		currentSession.flush();

		return pp.getPhotoKey();
	}

	@Transactional
	public List<PatrolPhoto> searchPatrolPhoto(int routeDefKey, Timestamp from,
			Timestamp to) {
		Criteria criteria = getDefaultCriteria();

		System.out.println("From : "+from.getTime() +"|| To :"+to.getTime()+"|| Key: "+routeDefKey);
		
		criteria.add(Restrictions.eq(dto_routeDefkey, routeDefKey))
				.add(Restrictions.ge(dto_createDateTime, from))
				.add(Restrictions.le(dto_createDateTime, to));

		// criteria.add(Restrictions.eq(
		return criteria.list();

	}
	
	@Transactional
	public List<PatrolPhoto> searchPatrolPhotoWithLimitation(int routeDefKey, Timestamp from,
			Timestamp to, Integer maxResult) {
		Criteria criteria = getDefaultCriteria();
		criteria.setMaxResults(maxResult);
		System.out.println("From : "+from.getTime() +"|| To :"+to.getTime()+"|| Key: "+routeDefKey);
		
		criteria.add(Restrictions.eq(dto_routeDefkey, routeDefKey))
				.add(Restrictions.ge(dto_createDateTime, from))
				.add(Restrictions.le(dto_createDateTime, to));

		criteria.addOrder(Order.desc(dto_createDateTime));
		// criteria.add(Restrictions.eq(
		return criteria.list();

	}
	
	public PatrolPhoto searchPatrolPhotobyKey(int key){
		Criteria criteria = getDefaultCriteria();
		
		criteria.add(Restrictions.eq("photoKey", key));
		
		if(criteria.list().size()>0){
		return (PatrolPhoto) criteria.list().get(0);
		}
		return null;
	}
	
	

	/**                 **/
	/** Other Functions **/
	/**                 **/
	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(PatrolPhoto.class);
		criteria = criteria.add(Restrictions.eq("isDeleted", "N"));
		
		return criteria;
	}
}
