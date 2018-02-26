package hk.ebsl.mfms.dao;

import java.util.List;

import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.AlarmConsole;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class AlarmConsoleDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(AlarmConsoleDao.class);

	
	@SuppressWarnings("unchecked")
	public List<AlarmConsole> getAllAlarm(){
		
		Session currentSession = getSession();
		
		Query query = currentSession.createSQLQuery("CALL AlarmConsole()").addEntity(AlarmConsole.class);
		
		List<AlarmConsole> allAlarm = query.list();
//		for(AlarmConsole alarm : allAlarm){
//			
//			System.out.println("getAllAlarm : "+alarm.getDefect_Code() + "|| "+alarm.getSortNum()+"||"+alarm.getColor()+"||"+alarm.getChildKey()+"||"+alarm.getStatus_Name()+"||"+alarm.getDefect_ReplyBy());
//		}
		
		
		return allAlarm;
		
	}
	
	
	private Criteria getDefaultAccountGroupResponsibleCriteria() {

		// default criteria for accountGroup
		Session currentSession = getSession();
		
		return currentSession.createCriteria(AlarmConsole.class);
	}
}
