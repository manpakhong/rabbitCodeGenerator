package hk.ebsl.mfms.manager.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.DefectFileDao;
import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectFileManager;
import hk.ebsl.mfms.utility.FileBucket;

public class DefectFileManagerImpl implements DefectFileManager {

	public static final Logger logger = Logger.getLogger(DefectFileManagerImpl.class);

	private DefectFileDao defectFileDao;

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void saveFile(FileBucket fileBucket) throws MFMSException {
		// TODO Auto-generated method stub
		defectFileDao.saveFile(fileBucket);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int saveDefectPhoto(DefectPhoto obj) throws MFMSException {
		// TODO Auto-generated method stub
		return defectFileDao.saveDefectPhoto(obj);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int saveDefectVideo(DefectVideo obj) throws MFMSException {
		// TODO Auto-generated method stub
		return defectFileDao.saveDefectVideo(obj);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public String deleteFile(Integer accountKey, Integer defectKey, String path, String fileType)
			throws MFMSException, MalformedURLException, IOException {
		return defectFileDao.deleteFile(accountKey, defectKey, path, fileType);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void modifyFileDescription(Integer accountKey, Integer defectKey, String path, String fileType, String desc)
			throws MFMSException, MalformedURLException, IOException {
		defectFileDao.modifyFileDescription(accountKey, defectKey, path, fileType, desc);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public void deleteAllFile(Integer accountKey, Integer defectKey) throws MFMSException, Exception {

		List<FileBucket> fileBucketList = defectFileDao.getFilesByDefectKey(defectKey);

		for (FileBucket file : fileBucketList) {

			defectFileDao.deleteFile(accountKey, defectKey, file.getPath(), file.getFileType());
		}

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FileBucket> getAllPhoto() throws Exception, MFMSException {
		return defectFileDao.getAllPhoto();
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FileBucket> getAllVideo() throws Exception, MFMSException {
		return defectFileDao.getAllVideo();
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FileBucket> getVideoByLastModifyDateAndSiteKey(Timestamp time, Integer siteKey)
			throws Exception, MFMSException {
		return defectFileDao.getVideoByLastModifyDateAndSiteKey(time, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FileBucket> getPhotoByLastModifyDateAndSiteKey(Timestamp time, Integer siteKey)
			throws Exception, MFMSException {
		return defectFileDao.getPhotoByLastModifyDateAndSiteKey(time, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getPhotoRemain(Integer defectKey) throws MFMSException {
		// TODO Auto-generated method stub
		return defectFileDao.getPhotoRemain(defectKey);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getPhotoRemainWithPhotoNumber(Integer defectKey, Integer photoNumber) throws MFMSException {
		return defectFileDao.getPhotoRemainWithPhotoNumber(defectKey, photoNumber);
	}
	
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getVideoRemain(Integer defectKey) throws MFMSException {
		// TODO Auto-generated method stub
		return defectFileDao.getVideoRemain(defectKey);
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer getVideoRemainWithVideoNumber(Integer defectKey, Integer videoNumber) throws MFMSException{
		return defectFileDao.getVideoRemainWithVideoNumber(defectKey, videoNumber);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<FileBucket> getFilesByDefectKey(Integer defectKey) throws MFMSException, Exception {
		// TODO Auto-generated method stub

		return defectFileDao.getFilesByDefectKey(defectKey);

	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public DefectPhoto getDefectPhotoByKey(Integer key) {
		// TODO Auto-generated method stub
		return defectFileDao.getDefectPhotoByKey(key);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public DefectVideo getDefectVideoByKey(Integer key) {
		// TODO Auto-generated method stub
		return defectFileDao.getDefectVideoByKey(key);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int getDefectPhotoCountByLastModifyDate(Timestamp lastModifyDate, Integer siteKey) {
		// TODO Auto-generated method stub
		return defectFileDao.getDefectPhotoCountByLastModifyDate(lastModifyDate, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectPhoto> getDefectPhotoByLastModifyDate(Timestamp lastModifyDate, int offset, int maxResult,
			Integer siteKey) {
		// TODO Auto-generated method stub
		return defectFileDao.getDefectPhotoByLastModifyDate(lastModifyDate, offset, maxResult, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public int getDefectVideoCountByLastModifyDate(Timestamp lastModifyDate, Integer siteKey) {
		// TODO Auto-generated method stub
		return defectFileDao.getDefectVideoCountByLastModifyDate(lastModifyDate, siteKey);
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<DefectVideo> getDefectVideoByLastModifyDate(Timestamp lastModifyDate, int offset, int maxResult,
			Integer siteKey) {
		// TODO Auto-generated method stub
		return defectFileDao.getDefectVideoByLastModifyDate(lastModifyDate, offset, maxResult, siteKey);
	}

	public DefectFileDao getDefectFileDao() {
		return defectFileDao;
	}

	public void setDefectFileDao(DefectFileDao defectFileDao) {
		this.defectFileDao = defectFileDao;
	}

	public static Logger getLogger() {
		return logger;
	}

}
