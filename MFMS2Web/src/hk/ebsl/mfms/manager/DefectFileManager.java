package hk.ebsl.mfms.manager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.utility.FileBucket;

public interface DefectFileManager {

	public void saveFile(FileBucket fileBucket) throws MFMSException;

	public String deleteFile(Integer accountKey, Integer defectKey, String path, String fileType)
			throws MFMSException, MalformedURLException, IOException;

	public void modifyFileDescription(Integer accountKey, Integer defectKey, String path, String fileType, String desc)
			throws MFMSException, MalformedURLException, IOException;

	public void deleteAllFile(Integer accountKey, Integer defectKey)
			throws MFMSException, MalformedURLException, IOException, Exception;

	public List<FileBucket> getFilesByDefectKey(Integer defectKey) throws MFMSException, Exception;

	public Integer getPhotoRemain(Integer defectKey) throws MFMSException;

	public Integer getPhotoRemainWithPhotoNumber(Integer defectKey, Integer photoNumber) throws MFMSException;
	
	public Integer getVideoRemain(Integer defectKey) throws MFMSException;

	public Integer getVideoRemainWithVideoNumber(Integer defectKey, Integer videoNumber) throws MFMSException;
	
	public List<FileBucket> getAllPhoto() throws MFMSException, Exception;

	public List<FileBucket> getAllVideo() throws MFMSException, Exception;

	public int saveDefectPhoto(DefectPhoto defectPhoto) throws MFMSException;

	public int saveDefectVideo(DefectVideo defectVideo) throws MFMSException;

	public List<FileBucket> getVideoByLastModifyDateAndSiteKey(Timestamp lastModifyDate, Integer siteKey)
			throws MFMSException, Exception;

	public List<FileBucket> getPhotoByLastModifyDateAndSiteKey(Timestamp lastModifyDate, Integer siteKey)
			throws MFMSException, Exception;

	public DefectPhoto getDefectPhotoByKey(Integer key);

	public DefectVideo getDefectVideoByKey(Integer key);

	public int getDefectPhotoCountByLastModifyDate(Timestamp lastModifyDate, Integer siteKey);

	public List<DefectPhoto> getDefectPhotoByLastModifyDate(Timestamp lastModifyDate, int offset, int maxResult,
			Integer siteKey);

	public int getDefectVideoCountByLastModifyDate(Timestamp lastModifyDate, Integer siteKey);

	public List<DefectVideo> getDefectVideoByLastModifyDate(Timestamp lastModifyDate, int offset, int maxResult,
			Integer siteKey);

}
