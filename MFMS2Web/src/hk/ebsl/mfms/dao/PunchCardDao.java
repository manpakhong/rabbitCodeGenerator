package hk.ebsl.mfms.dao;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import hk.ebsl.mfms.dto.PunchCard;

public class PunchCardDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(PunchCardDao.class);
	
	public void save(PunchCard punchCard) {
		Session currentSession = getSession();

		currentSession.saveOrUpdate(punchCard);
	}
}
