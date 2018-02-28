package hk.ebsl.mfms.manager;

import java.util.Calendar;
import java.util.List;

import hk.ebsl.mfms.dto.PatrolPhoto;


public interface PatrolPhotoManager {

	int savePatrolPhoto(PatrolPhoto pr);
	
	List<PatrolPhoto> searchPatrolPhoto(int routeDefKey, Calendar calendar);
	
	List<PatrolPhoto> searchPatrolPhotoWithLimitation(int routeDefKey, Calendar calendar, Integer maxResult);
	
	PatrolPhoto searchPatrolPhotobyKey(int key);
	
}
