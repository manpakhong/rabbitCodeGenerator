package hk.ebsl.mfms.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseDao {

	private SessionFactory sessionFactory;
	
	public Session getSession() throws HibernateException {
		Session sess = null;
		try {
			sess = getSessionFactory().getCurrentSession();
		} catch (HibernateException e) {
			
		}
		if (sess == null) {
			sess = getSessionFactory().openSession();
		}
		return sess;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
