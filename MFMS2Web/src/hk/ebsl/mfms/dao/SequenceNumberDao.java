package hk.ebsl.mfms.dao;

import java.util.List;

import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.SequenceNumber;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;



public class SequenceNumberDao extends BaseDao {


	private String dto_name="name";
	private String patrolResult = "pr_GroupNum";
	
	public static final Logger logger = Logger
			.getLogger(SequenceNumberDao.class);
	
	
	public void saveSequenceNumber(SequenceNumber seq){
		
		Session currentSession = getSession();

		currentSession.saveOrUpdate(seq);
		
	}
	
	
	@SuppressWarnings("unchecked")
	public SequenceNumber getPatrolResultGroupNum(){
		
		List<SequenceNumber> list = getDefaultCriteria()
				.add(Restrictions.eq(dto_name, patrolResult)).list();
		
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}
	
	
	/**                 **/
	/** Other Functions **/
	/**                 **/

	private Criteria getDefaultCriteria() {

		Session currentSession = getSession();

		Criteria criteria = currentSession.createCriteria(SequenceNumber.class);
		
		return criteria;
	}
	
}
