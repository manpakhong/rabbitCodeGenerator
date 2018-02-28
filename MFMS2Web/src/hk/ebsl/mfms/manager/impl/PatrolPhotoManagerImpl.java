package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import hk.ebsl.mfms.dao.PatrolPhotoDao;
import hk.ebsl.mfms.dto.PatrolPhoto;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.PatrolPhotoManager;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class PatrolPhotoManagerImpl implements PatrolPhotoManager{

	public static final Logger logger = Logger
			.getLogger(PatrolPhotoManagerImpl.class);

	private PatrolPhotoDao patrolPhotoDao;
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int savePatrolPhoto(PatrolPhoto pp) {
		// TODO Auto-generated method stub
		return patrolPhotoDao.save(pp);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolPhoto> searchPatrolPhoto(int routeDefKey, Calendar calendar) {
		// TODO Auto-generated method stub
		
		Calendar from = (Calendar) calendar.clone();
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		
		Calendar to = (Calendar) calendar.clone();
		to.set(Calendar.HOUR_OF_DAY, 23);
		to.set(Calendar.MINUTE, 59);
		to.set(Calendar.SECOND, 59);
		to.set(Calendar.MILLISECOND, 999);
		
		Timestamp tmpFrom = new Timestamp(from.getTimeInMillis());
		Timestamp tmpTo = new Timestamp(to.getTimeInMillis()); 
		
		return patrolPhotoDao.searchPatrolPhoto(routeDefKey, tmpFrom, tmpTo);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<PatrolPhoto> searchPatrolPhotoWithLimitation(int routeDefKey, Calendar calendar, Integer maxResult) {
		Calendar from = (Calendar) calendar.clone();
		from.set(Calendar.HOUR_OF_DAY, 0);
		from.set(Calendar.MINUTE, 0);
		from.set(Calendar.SECOND, 0);
		from.set(Calendar.MILLISECOND, 0);
		
		Calendar to = (Calendar) calendar.clone();
		to.set(Calendar.HOUR_OF_DAY, 23);
		to.set(Calendar.MINUTE, 59);
		to.set(Calendar.SECOND, 59);
		to.set(Calendar.MILLISECOND, 999);
		
		Timestamp tmpFrom = new Timestamp(from.getTimeInMillis());
		Timestamp tmpTo = new Timestamp(to.getTimeInMillis()); 
		
		return patrolPhotoDao.searchPatrolPhotoWithLimitation(routeDefKey, tmpFrom, tmpTo, maxResult);
	}	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public PatrolPhoto searchPatrolPhotobyKey(int key) {
		// TODO Auto-generated method stub
		return patrolPhotoDao.searchPatrolPhotobyKey(key);
	}
	
	public PatrolPhotoDao getPatrolPhotoDao() {
		return patrolPhotoDao;
	}


	public void setPatrolPhotoDao(PatrolPhotoDao patrolPhotoDao) {
		this.patrolPhotoDao = patrolPhotoDao;
	}



}
