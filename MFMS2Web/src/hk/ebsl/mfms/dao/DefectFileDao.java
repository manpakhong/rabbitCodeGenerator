package hk.ebsl.mfms.dao;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.SystemParamManager;
import hk.ebsl.mfms.utility.FileBucket;
import hk.ebsl.mfms.web.controller.DefectController;

public class DefectFileDao extends BaseDao {

	public static final Logger logger = Logger.getLogger(DefectFileDao.class);

	@Autowired
	private SystemParamManager systemParamManager;

	public class FileComparator implements Comparator<FileBucket> {
		@Override
		public int compare(FileBucket st, FileBucket nd) {
			return st.getName().compareTo(nd.getName());
		}
	}

	public void saveFile(FileBucket fileBucket) {

		if (fileBucket.getFile().getContentType().contains("image")) {

			DefectPhoto defectPhoto = new DefectPhoto();

			defectPhoto.setDefectKey(fileBucket.getDefectKey());
			defectPhoto.setDesc(fileBucket.getDesc());
			defectPhoto.setSiteKey(fileBucket.getSiteKey());
			defectPhoto.setPhotoPath(fileBucket.getPath());
			defectPhoto.setCreatedBy(fileBucket.getCreatedBy());
			defectPhoto.setCreateDateTime(fileBucket.getCreateDateTime());
			defectPhoto.setLastModifyBy(fileBucket.getLastModifyBy());
			defectPhoto.setLastModifyDateTime(fileBucket.getCreateDateTime());
			defectPhoto.setDeleted("N");

			Session currentSession = getSession();
			currentSession.saveOrUpdate(defectPhoto);
		}

		else if (fileBucket.getFile().getContentType().contains("video")) {

			DefectVideo defectVideo = new DefectVideo();
			defectVideo.setDefectKey(fileBucket.getDefectKey());
			defectVideo.setDesc(fileBucket.getDesc());
			defectVideo.setSiteKey(fileBucket.getSiteKey());
			defectVideo.setVideoPath(fileBucket.getPath());
			defectVideo.setCreatedBy(fileBucket.getCreatedBy());
			defectVideo.setCreateDateTime(fileBucket.getCreateDateTime());
			defectVideo.setLastModifyBy(fileBucket.getLastModifyBy());
			defectVideo.setLastModifyDateTime(fileBucket.getCreateDateTime());
			defectVideo.setDeleted("N");
			Session currentSession = getSession();
			currentSession.saveOrUpdate(defectVideo);
		}
	}

	public int saveDefectPhoto(DefectPhoto defectPhoto) {

		Session currentSession = getSession();
		currentSession.saveOrUpdate(defectPhoto);
		currentSession.flush();

		return defectPhoto.getKey();

	}

	public int saveDefectVideo(DefectVideo defectVideo) {

		Session currentSession = getSession();
		currentSession.saveOrUpdate(defectVideo);

		currentSession.flush();

		return defectVideo.getKey();

	}

	@SuppressWarnings("unchecked")
	public List<FileBucket> getFilesByDefectKey(Integer defectKey)
			throws Exception, MFMSException {
		List<FileBucket> fileBucketList = new ArrayList<FileBucket>();

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.video.uploadpath", 1);

		Criteria criteria = null;

		criteria = getDefaultPictureCriteria().add(
				Restrictions.eq("defectKey", defectKey)).add(
				Restrictions.eq("deleted", "N"));

		List<DefectPhoto> picList = criteria.list();
		if (picList != null && picList.size() > 0) {
			for (DefectPhoto pic : picList) {

				File file = new File(PHOTO_LOCATION + pic.getPhotoPath());

				logger.debug(" ********* test new path: " + PHOTO_LOCATION
						+ pic.getPhotoPath());

				FileBucket fileBucket = new FileBucket();

				fileBucket.setKey(pic.getKey());
				fileBucket.setFileType("Image");
				fileBucket.setDefectKey(defectKey);
				fileBucket.setName(file.getName());
				fileBucket.setDesc(pic.getDesc());
				fileBucket.setPath(pic.getPhotoPath());
				fileBucket.setUnicodePath(StrToUnicode(pic.getPhotoPath()));
				fileBucket.setCreateDateTime(pic.getCreateDateTime());

				if (file.exists()) {
					fileBucket.setFileExist(true);

					long fileSizeInBytes = file.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					if (fileSizeInKB > 1024)
						fileBucket.setFileSize(fileSizeInMB + "MB");
					else
						fileBucket.setFileSize(fileSizeInKB + "KB");
				} else {
					fileBucket.setFileExist(false);
					fileBucket.setFileSize("File does not exist");
				}
				fileBucketList.add(fileBucket);
			}
		}

		criteria = getDefaultVideoCriteria().add(
				Restrictions.eq("defectKey", defectKey)).add(
				Restrictions.eq("deleted", "N"));

		List<DefectVideo> vidList = criteria.list();
		if (vidList != null && vidList.size() > 0) {
			for (DefectVideo vid : vidList) {
				File file = new File(VIDEO_LOCATION + vid.getVideoPath());

				FileBucket fileBucket = new FileBucket();
				fileBucket.setKey(vid.getKey());
				fileBucket.setFileType("Video");
				fileBucket.setDefectKey(defectKey);
				fileBucket.setName(file.getName());
				fileBucket.setDesc(vid.getDesc());
				fileBucket.setPath(vid.getVideoPath());
				fileBucket.setUnicodePath(StrToUnicode(vid.getVideoPath()));
				fileBucket.setCreateDateTime(vid.getCreateDateTime());

				if (file.exists()) {
					fileBucket.setFileExist(true);

					long fileSizeInBytes = file.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					if (fileSizeInKB > 1024)
						fileBucket.setFileSize(fileSizeInMB + "MB");
					else
						fileBucket.setFileSize(fileSizeInKB + "KB");
				} else {
					fileBucket.setFileExist(false);
					fileBucket.setFileSize("File does not exist");
				}

				fileBucketList.add(fileBucket);
			}
		}

		Collections.sort(fileBucketList, new FileComparator());

		return fileBucketList;

	}

	@SuppressWarnings("unchecked")
	public String deleteFile(Integer accountKey, Integer defectKey,
			String path, String fileType) throws MalformedURLException,
			IOException {

		String fileName = "";

		Session currentSession = getSession();
		Criteria criteria = null;
		if (fileType.equals("Image")) {

			logger.debug("path:" + path);

			criteria = getDefaultPictureCriteria()
					.add(Restrictions.eq("photoPath", path))
					.add(Restrictions.eq("defectKey", defectKey))
					.add(Restrictions.eq("deleted", "N"));

			List<DefectPhoto> picList = criteria.list();
			if (picList != null && picList.size() > 1) {
				logger.debug("no more than 1");
			} else {

				DefectPhoto pic = picList.get(0);

				fileName = pic.getPhotoPath();

				pic.setLastModifyBy(accountKey);
				pic.setLastModifyDateTime(new Timestamp(System
						.currentTimeMillis()));
				pic.setDeleted("Y");

				currentSession.saveOrUpdate(pic);

			}

		} else if (fileType.equals("Video")) {

			criteria = getDefaultVideoCriteria()
					.add(Restrictions.eq("videoPath", path))
					.add(Restrictions.eq("defectKey", defectKey))
					.add(Restrictions.eq("deleted", "N"));

			List<DefectVideo> vidList = criteria.list();
			if (vidList != null && vidList.size() > 1) {
				logger.debug("no more than 1");
			} else {
				DefectVideo vid = vidList.get(0);

				fileName = vid.getVideoPath();

				vid.setLastModifyBy(accountKey);
				vid.setLastModifyDateTime(new Timestamp(System
						.currentTimeMillis()));
				vid.setDeleted("Y");
				currentSession.saveOrUpdate(vid);

			}

		}

		return fileName.substring(fileName.lastIndexOf("/") + 1);

	}

	@SuppressWarnings("unchecked")
	public void modifyFileDescription(Integer accountKey, Integer defectKey,
			String path, String fileType, String desc)
			throws MalformedURLException, IOException {

		String fileName = "";

		Session currentSession = getSession();
		Criteria criteria = null;
		if (fileType.equals("Image")) {

			logger.debug("path:" + path);

			criteria = getDefaultPictureCriteria()
					.add(Restrictions.eq("photoPath", path))
					.add(Restrictions.eq("defectKey", defectKey))
					.add(Restrictions.eq("deleted", "N"));

			List<DefectPhoto> picList = criteria.list();
			if (picList != null && picList.size() > 1) {
				logger.debug("no more than 1");
			} else {

				DefectPhoto pic = picList.get(0);

				fileName = pic.getPhotoPath();

				pic.setLastModifyBy(accountKey);
				pic.setLastModifyDateTime(new Timestamp(System
						.currentTimeMillis()));
				pic.setDesc(desc);

				currentSession.saveOrUpdate(pic);

			}

		} else if (fileType.equals("Video")) {

			criteria = getDefaultVideoCriteria()
					.add(Restrictions.eq("videoPath", path))
					.add(Restrictions.eq("defectKey", defectKey))
					.add(Restrictions.eq("deleted", "N"));

			List<DefectVideo> vidList = criteria.list();
			if (vidList != null && vidList.size() > 1) {
				logger.debug("no more than 1");
			} else {
				DefectVideo vid = vidList.get(0);

				fileName = vid.getVideoPath();

				vid.setLastModifyBy(accountKey);
				vid.setLastModifyDateTime(new Timestamp(System
						.currentTimeMillis()));
				vid.setDesc(desc);
				currentSession.saveOrUpdate(vid);

			}

		}
	}

	@SuppressWarnings("unchecked")
	public Integer getPhotoRemain(Integer defectKey) {

		Criteria criteria = getDefaultPictureCriteria().add(
				Restrictions.eq("defectKey", defectKey)).add(
				Restrictions.eq("deleted", "N"));

		List<DefectPhoto> picList = criteria.list();
		if (picList != null && picList.size() > 0) {
			return 5 - picList.size();
		}
		return 5;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getPhotoRemainWithPhotoNumber(Integer defectKey, Integer photoNumber) {

		Criteria criteria = getDefaultPictureCriteria().add(
				Restrictions.eq("defectKey", defectKey)).add(
				Restrictions.eq("deleted", "N"));

		List<DefectPhoto> picList = criteria.list();
		if (picList != null && picList.size() > 0) {
			return photoNumber - picList.size();
		}
		return photoNumber;
	}
	

	@SuppressWarnings("unchecked")
	public Integer getVideoRemain(Integer defectKey) {

		Criteria criteria = getDefaultVideoCriteria().add(
				Restrictions.eq("defectKey", defectKey)).add(
				Restrictions.eq("deleted", "N"));

		List<DefectVideo> vidList = criteria.list();
		if (vidList != null && vidList.size() > 0) {
			return 1 - vidList.size();
		}
		return 1;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getVideoRemainWithVideoNumber(Integer defectKey, Integer videoNumber) {

		Criteria criteria = getDefaultVideoCriteria().add(
				Restrictions.eq("defectKey", defectKey)).add(
				Restrictions.eq("deleted", "N"));

		List<DefectVideo> vidList = criteria.list();
		if (vidList != null && vidList.size() > 0) {
			return videoNumber - vidList.size();
		}
		return videoNumber;
	}
	

	@SuppressWarnings("unchecked")
	public List<FileBucket> getAllPhoto() throws Exception, MFMSException {
		List<FileBucket> fileBucketList = new ArrayList<FileBucket>();

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.video.uploadpath", 1);

		Criteria criteria = getDefaultPictureCriteria().add(
				Restrictions.eq("deleted", "N"));

		List<DefectPhoto> picList = criteria.list();
		if (picList != null && picList.size() > 0) {
			for (DefectPhoto pic : picList) {

				File file = new File(PHOTO_LOCATION + pic.getPhotoPath());

				FileBucket fileBucket = new FileBucket();
				fileBucket.setKey(pic.getKey());
				fileBucket.setFileType("Image");
				fileBucket.setDefectKey(pic.getDefectKey());
				fileBucket.setName(file.getName());
				fileBucket.setDesc(pic.getDesc());
				fileBucket.setPath(pic.getPhotoPath());
				fileBucket.setUnicodePath(StrToUnicode(pic.getPhotoPath()));
				fileBucket.setCreateDateTime(pic.getCreateDateTime());

				if (file.exists()) {
					fileBucket.setFileExist(true);

					long fileSizeInBytes = file.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					if (fileSizeInKB > 1024)
						fileBucket.setFileSize(fileSizeInMB + "MB");
					else
						fileBucket.setFileSize(fileSizeInKB + "KB");
				} else {
					fileBucket.setFileExist(false);
					fileBucket.setFileSize("File does not exist");
				}
				fileBucketList.add(fileBucket);
			}
		}

		Collections.sort(fileBucketList, new FileComparator());

		return fileBucketList;

	}

	@SuppressWarnings("unchecked")
	public List<FileBucket> getAllVideo() throws Exception, MFMSException {
		List<FileBucket> fileBucketList = new ArrayList<FileBucket>();

		Criteria criteria = getDefaultVideoCriteria().add(
				Restrictions.eq("deleted", "N"));

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.video.uploadpath", 1);

		List<DefectVideo> vidList = criteria.list();
		if (vidList != null && vidList.size() > 0) {
			for (DefectVideo vid : vidList) {
				File file = new File(VIDEO_LOCATION + vid.getVideoPath());

				FileBucket fileBucket = new FileBucket();

				fileBucket.setKey(vid.getKey());
				fileBucket.setFileType("Video");
				fileBucket.setDefectKey(vid.getDefectKey());
				fileBucket.setName(file.getName());
				fileBucket.setDesc(vid.getDesc());
				fileBucket.setPath(vid.getVideoPath());
				fileBucket.setUnicodePath(StrToUnicode(vid.getVideoPath()));
				fileBucket.setCreateDateTime(vid.getCreateDateTime());

				if (file.exists()) {
					fileBucket.setFileExist(true);

					long fileSizeInBytes = file.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					if (fileSizeInKB > 1024)
						fileBucket.setFileSize(fileSizeInMB + "MB");
					else
						fileBucket.setFileSize(fileSizeInKB + "KB");
				} else {
					fileBucket.setFileExist(false);
					fileBucket.setFileSize("File does not exist");
				}

				fileBucketList.add(fileBucket);
			}
		}

		Collections.sort(fileBucketList, new FileComparator());

		return fileBucketList;

	}

	@SuppressWarnings("unchecked")
	public List<FileBucket> getPhotoByLastModifyDateAndSiteKey(Timestamp time,
			Integer siteKey) throws Exception, MFMSException {
		List<FileBucket> fileBucketList = new ArrayList<FileBucket>();

		Criteria criteria = getDefaultPictureCriteria();

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.video.uploadpath", 1);

		criteria.add(Restrictions.gt("lastModifyDateTime", time));

		criteria.add(Restrictions.eq("siteKey", siteKey));

		List<DefectPhoto> picList = criteria.list();
		if (picList != null && picList.size() > 0) {
			for (DefectPhoto pic : picList) {

				File file = new File(PHOTO_LOCATION + pic.getPhotoPath());

				FileBucket fileBucket = new FileBucket();
				fileBucket.setKey(pic.getKey());
				fileBucket.setFileType("Image");
				fileBucket.setDefectKey(pic.getDefectKey());
				fileBucket.setName(file.getName());
				fileBucket.setDeleted(pic.getDeleted());
				fileBucket.setDesc(pic.getDesc());
				fileBucket.setPath(pic.getPhotoPath());
				fileBucket.setUnicodePath(StrToUnicode(pic.getPhotoPath()));
				fileBucket.setCreateDateTime(pic.getCreateDateTime());

				if (file.exists()) {
					fileBucket.setFileExist(true);

					long fileSizeInBytes = file.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					if (fileSizeInKB > 1024)
						fileBucket.setFileSize(fileSizeInMB + "MB");
					else
						fileBucket.setFileSize(fileSizeInKB + "KB");
				} else {
					fileBucket.setFileExist(false);
					fileBucket.setFileSize("File does not exist");
				}
				fileBucketList.add(fileBucket);
			}
		}

		Collections.sort(fileBucketList, new FileComparator());

		return fileBucketList;

	}

	@SuppressWarnings("unchecked")
	public List<FileBucket> getVideoByLastModifyDateAndSiteKey(Timestamp time,
			Integer siteKey) throws Exception, MFMSException {
		List<FileBucket> fileBucketList = new ArrayList<FileBucket>();

		Criteria criteria = getDefaultVideoCriteria();

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
				"wo.video.uploadpath", 1);

		criteria.add(Restrictions.gt("lastModifyDateTime", time));

		criteria.add(Restrictions.eq("siteKey", siteKey));

		List<DefectVideo> vidList = criteria.list();
		if (vidList != null && vidList.size() > 0) {
			for (DefectVideo vid : vidList) {
				File file = new File(VIDEO_LOCATION + vid.getVideoPath());

				FileBucket fileBucket = new FileBucket();

				fileBucket.setKey(vid.getKey());
				fileBucket.setFileType("Video");
				fileBucket.setDefectKey(vid.getDefectKey());
				fileBucket.setName(file.getName());
				fileBucket.setDeleted(vid.getDeleted());
				fileBucket.setDesc(vid.getDesc());
				fileBucket.setPath(vid.getVideoPath());
				fileBucket.setUnicodePath(StrToUnicode(vid.getVideoPath()));
				fileBucket.setCreateDateTime(vid.getCreateDateTime());

				if (file.exists()) {
					fileBucket.setFileExist(true);

					long fileSizeInBytes = file.length();
					// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
					long fileSizeInKB = fileSizeInBytes / 1024;
					// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
					long fileSizeInMB = fileSizeInKB / 1024;

					if (fileSizeInKB > 1024)
						fileBucket.setFileSize(fileSizeInMB + "MB");
					else
						fileBucket.setFileSize(fileSizeInKB + "KB");
				} else {
					fileBucket.setFileExist(false);
					fileBucket.setFileSize("File does not exist");
				}

				fileBucketList.add(fileBucket);
			}
		}

		Collections.sort(fileBucketList, new FileComparator());

		return fileBucketList;

	}

	public DefectPhoto getDefectPhotoByKey(Integer key) {

		Criteria criteria = this.getDefaultPictureCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<DefectPhoto> photoList = criteria.list();
		if (photoList != null && photoList.size() > 0)
			return photoList.get(0);

		return null;

	}

	public DefectVideo getDefectVideoByKey(Integer key) {

		Criteria criteria = this.getDefaultVideoCriteria();
		criteria.add(Restrictions.eq("key", key));

		List<DefectVideo> videoList = criteria.list();
		if (videoList != null && videoList.size() > 0)
			return videoList.get(0);

		return null;

	}
	
	
	public int getDefectPhotoCountByLastModifyDate(Timestamp lastModifyDate, Integer siteKey){
		
		Criteria criteria = this.getDefaultPictureCriteria();
		if (lastModifyDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifyDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.ge("siteKey", siteKey));
		}
		
		return ((Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}
	
	public List<DefectPhoto> getDefectPhotoByLastModifyDate(Timestamp lastModifyDate, int offset, int maxResult, Integer siteKey) {
		
		Criteria criteria = this.getDefaultPictureCriteria();
		if (lastModifyDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifyDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.ge("siteKey", siteKey));
		}
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResult);
		
		return criteria.list();
	}
	
	public int getDefectVideoCountByLastModifyDate(Timestamp lastModifyDate, Integer siteKey){
		
		Criteria criteria = this.getDefaultVideoCriteria();
		if (lastModifyDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifyDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.ge("siteKey", siteKey));
		}
		
		return ((Long) criteria.setProjection(Projections.rowCount())
				.uniqueResult()).intValue();
	}
	
	public List<DefectVideo> getDefectVideoByLastModifyDate(Timestamp lastModifyDate, int offset, int maxResult, Integer siteKey) {
		
		Criteria criteria = this.getDefaultVideoCriteria();
		if (lastModifyDate != null) {
			criteria.add(Restrictions.gt("lastModifyTimeForSync", lastModifyDate));
		}
		if(siteKey != null){
			criteria.add(Restrictions.ge("siteKey", siteKey));
		}
		criteria.setFirstResult(offset);
		criteria.setMaxResults(maxResult);
		
		return criteria.list();
	}

	private Criteria getDefaultVideoCriteria() {

		Session currentSession = getSession();
		return currentSession.createCriteria(DefectVideo.class);
	}

	private Criteria getDefaultPictureCriteria() {

		Session currentSession = getSession();
		return currentSession.createCriteria(DefectPhoto.class);
	}

	public static String StrToUnicode(String str) throws Exception {
		StringBuffer outHexStrBuf = new StringBuffer();
		for (char c : str.toCharArray()) {
			outHexStrBuf.append("\\u");
			String hexStr = Integer.toHexString(c);
			for (int i = 0; i < (4 - hexStr.length()); i++)
				outHexStrBuf.append("0");
			outHexStrBuf.append(hexStr);
		}
		return outHexStrBuf.toString();
	}

	public SystemParamManager getSystemParamManager() {
		return systemParamManager;
	}

	public void setSystemParamManager(SystemParamManager systemParamManager) {
		this.systemParamManager = systemParamManager;
	}

}
