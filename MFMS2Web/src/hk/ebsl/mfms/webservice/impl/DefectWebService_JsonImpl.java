package hk.ebsl.mfms.webservice.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.CauseCodeManager;
import hk.ebsl.mfms.manager.DefectActionLogManager;
import hk.ebsl.mfms.manager.DefectFileManager;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.manager.ProblemCodeManager;
import hk.ebsl.mfms.manager.ToolManager;
import hk.ebsl.mfms.utility.JsonUtil;
import hk.ebsl.mfms.web.controller.DefectController;
import hk.ebsl.mfms.webservice.DefectWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

@SuppressWarnings("rawtypes")
public class DefectWebService_JsonImpl extends WebServiceUtil implements DefectWebService_Json {

	enum DefectAttachmentType {
		Photo, Video
	}

	public static final Logger logger = Logger.getLogger(DefectWebService_JsonImpl.class);

	@Context
	private HttpHeaders headers;

	@Resource
	private Properties propertyConfigurer;

	@Resource
	private DefectManager defectManager;

	@Resource
	private CauseCodeManager causeCodeManager;

	@Resource
	private ProblemCodeManager problemCodeManager;

	@Resource
	private ToolManager toolManager;

	@Resource
	private EquipmentManager equipmentManager;

	@Resource
	private DefectFileManager defectFileManager;

	@Resource
	private DefectActionLogManager defectActionLogManager;

	@Resource
	private DefectScheduleManager defectScheduleManager;

	@Override
	public Response getUpdateCauseCode(LastModifyDateJson obj) {
		System.out.println("updateCauseCode json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = causeCodeManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<CauseCode> causeCodeList = causeCodeManager.searchCauseCodeByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			WebServiceXml<CauseCode> xml = new WebServiceXml<CauseCode>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(causeCodeList.size());
			xml.setWebServiceXml(causeCodeList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response getUpdateProblemCode(LastModifyDateJson obj) {
		System.out.println("updateProblemCode json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = problemCodeManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<ProblemCode> problemCodeList = problemCodeManager.searchAllProblemCodeByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateProblemCode : count = " + theCountOfTotalResults + ", problemCodeList.size() = "
					+ problemCodeList.size());

			WebServiceXml<ProblemCode> xml = new WebServiceXml<ProblemCode>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(problemCodeList.size());
			xml.setWebServiceXml(problemCodeList);
			rtn.add(xml);

			logger.debug("Return :" + JsonUtil.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response getUpdateTool(LastModifyDateJson obj) {
		System.out.println("updateTool json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			Integer siteKey = obj.getSiteKey();
			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = toolManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<Tool> toolList = toolManager.searchToolByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			WebServiceXml<Tool> xml = new WebServiceXml<Tool>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(toolList.size());
			xml.setWebServiceXml(toolList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response getUpdateEquipment(LastModifyDateJson obj) {
		System.out.println("updateEquipment json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = equipmentManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<Equipment> equipmentList = equipmentManager.searchEquipmentByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateEquipment : count = " + theCountOfTotalResults + ", equipmentList.size() = "
					+ equipmentList.size());

			WebServiceXml<Equipment> xml = new WebServiceXml<Equipment>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(equipmentList.size());
			xml.setWebServiceXml(equipmentList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response getUpdateDefect(LastModifyDateJson obj) {

		System.out.println("updateDefect json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer siteKey = obj.getSiteKey();

			// Integer defaultMaxResult = 500;

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = defectManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<Defect> defectList = defectManager.searchDefectByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateDefect : count = " + theCountOfTotalResults + ", defectList.size() = "
					+ defectList.size());

			WebServiceXml<Defect> xml = new WebServiceXml<Defect>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(defectList.size());
			xml.setWebServiceXml(defectList);
			rtn.add(xml);

			logger.debug("Return :" + JsonUtil.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response createDefect(List<Defect> defectList) {
		// TODO Auto-generated method stub

		logger.debug("createDefect");
		logger.debug("createDefect : " + listToJsonString(defectList));

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

		// WebServiceXml<Defect> rtnList = new WebServiceXml<Defect>();

		try {
			// List<Integer> insertList = defectManager.saveDefect(defectList);

			for (Defect d : defectList) {

				logger.debug("D_KEy :" + d.getKey());
				if (d.getKey() <= 0) {
					d.setKey(null);
					d.setCode(defectManager.getNextDefectCode(d.getSiteKey()));
				}

				if (d.getTargetFinishDateTime() != null && d.getActualFinishDateTime() != null) {

					if (d.getTargetFinishDateTime().getTime() > d.getActualFinishDateTime().getTime())
						d.setMeetKpi("Y");
					else
						d.setMeetKpi("N");

				} else
					d.setMeetKpi("-");

				d.setCallCenterEmail(propertyConfigurer.getProperty("notification.callCenter.email"));

				int insertKey = DefectController.saveDefectToDb(defectActionLogManager, defectManager, d);

				WebServiceXml<Defect> xml = new WebServiceXml<Defect>();
				xml.setMobileKey(d.getMobileKey());
				xml.setXmlStatus("success");

				List<Defect> tmp = new ArrayList<Defect>();
				tmp.add(defectManager.getDefectByKey(insertKey));
				xml.setWebServiceXml(tmp);

				rtn.add(xml);
			}

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}

	}

	@Override
	public Response createDefectPhoto(List<DefectPhoto> defectPhotoList, List<Attachment> files) {
		// TODO Auto-generated method stub

		logger.debug("createDefectPhoto : " + listToJsonString(defectPhotoList));
		logger.debug("createDefectPhoto");

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

		for (DefectPhoto photo : defectPhotoList) {

			try {
				UserAccount ac = getUserAccount(headers);

				photo.setLastModifyBy(ac.getKey());
				photo.setLastModifyDateTime(new Timestamp(new Date().getTime()));

				int insertKey = defectFileManager.saveDefectPhoto(photo);

				WebServiceXml<DefectPhoto> xml = new WebServiceXml<DefectPhoto>();
				xml.setMobileKey(photo.getMobileKey());
				xml.setXmlStatus("success");

				List<DefectPhoto> tmp = new ArrayList<DefectPhoto>();
				tmp.add(defectFileManager.getDefectPhotoByKey(insertKey));
				xml.setWebServiceXml(tmp);

				rtn.add(xml);

			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		saveFile(files, DefectAttachmentType.Photo);

		// for (Attachment file : files) {
		//
		// DataHandler handler = file.getDataHandler();
		// InputStream inputStream = null;
		// OutputStream outputStream = null;
		//
		// try {
		// inputStream = handler.getInputStream();
		//
		// MultivaluedMap<String, String> map = file.getHeaders();
		// System.out.println("fileName Here : " + getFileName(map));
		// outputStream = new FileOutputStream(new File(
		// "C:\\Users\\A\\Desktop\\" + getFileName(map)));
		//
		// int read = 0;
		// byte[] bytes = new byte[1024];
		// while ((read = inputStream.read(bytes)) != -1) {
		// outputStream.write(bytes, 0, read);
		// }
		// // inputStream.close();
		// outputStream.flush();
		// // outputStream.close();
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } finally {
		//
		// try {
		// if (inputStream != null)
		// inputStream.close();
		// if (outputStream != null)
		// outputStream.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// }
		//
		// }

		// return Response.status(Response.Status.UNAUTHORIZED)
		// .entity("Login Fail").build();

		return Response.ok(rtn, MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response createDefectVideo(List<DefectVideo> list, List<Attachment> files) {
		// TODO Auto-generated method stub

		logger.debug("createDefectVideo : " + listToJsonString(list));
		logger.debug("createDefectVideo");

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

		for (DefectVideo video : list) {

			try {

				UserAccount ac = getUserAccount(headers);

				video.setLastModifyBy(ac.getKey());
				video.setLastModifyDateTime(new Timestamp(new Date().getTime()));

				int insertKey = defectFileManager.saveDefectVideo(video);

				WebServiceXml<DefectVideo> xml = new WebServiceXml<DefectVideo>();
				xml.setMobileKey(video.getMobileKey());
				xml.setXmlStatus("success");

				List<DefectVideo> tmp = new ArrayList<DefectVideo>();
				tmp.add(defectFileManager.getDefectVideoByKey(insertKey));
				xml.setWebServiceXml(tmp);

				rtn.add(xml);

			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		saveFile(files, DefectAttachmentType.Video);

		return Response.ok(rtn, MediaType.APPLICATION_JSON).build();
	}

	@Override
	public Response getUpdateDefectPhoto(LastModifyDateJson obj) {
		// TODO Auto-generated method stub
		System.out.println("updateDefectPhoto");

		logger.debug(this.ObjectToJsonString(obj));

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			// Integer defaultMaxResult = 500;

			Integer siteKey = obj.getSiteKey();
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = defectFileManager.getDefectPhotoCountByLastModifyDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<DefectPhoto> defectPhotoList = defectFileManager.getDefectPhotoByLastModifyDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateDefectPhoto : count = " + theCountOfTotalResults + ", defectPhotoList.size() = "
					+ defectPhotoList.size());

			WebServiceXml<DefectPhoto> xml = new WebServiceXml<DefectPhoto>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(defectPhotoList.size());
			xml.setWebServiceXml(defectPhotoList);
			rtn.add(xml);

			logger.debug("return :" + this.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response downloadDefectPhoto(DefectPhoto defectPhoto) {
		// TODO Auto-generated method stub
		logger.debug("downloadDefectPhoto");

		try {
			String path = propertyConfigurer.getProperty("photoPath");
			// File file = new File("C:\\Users\\A\\Desktop\\test.png");
			File file = new File(path + defectPhoto.getPhotoPath());
			//
			// ResponseBuilder response = Response.ok((Object)file);
			// response.
			// response.header("Content-Disposition",
			// "attachment; filename=\"MyFile.png\"");
			if (file.exists()) {
				return Response.ok((Object) file, MediaType.APPLICATION_OCTET_STREAM)
						.header("Content-Length", file.length()).build();
			} else {
				logger.debug("File not exist : " + defectPhoto.getPhotoPath());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File not found").build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	private void saveFile(List<Attachment> files, DefectAttachmentType fileType) {

		for (Attachment file : files) {

			DataHandler handler = file.getDataHandler();
			InputStream inputStream = null;
			OutputStream outputStream = null;
			String outputPath = propertyConfigurer.getProperty("photoPath");
			if (fileType.equals(DefectAttachmentType.Video)) {
				outputPath = propertyConfigurer.getProperty("videoPath");
			}

			try {
				inputStream = handler.getInputStream();

				MultivaluedMap<String, String> map = file.getHeaders();
				System.out.println("fileName Here : " + getFileName(map));

				// outputStream = new FileOutputStream(new File(
				// "C:\\Users\\A\\Desktop\\" + getFileName(map)));

				outputStream = new FileOutputStream(new File(outputPath + getFileName(map)));

				int read = 0;
				byte[] bytes = new byte[1024];
				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				// inputStream.close();
				outputStream.flush();
				// outputStream.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					if (inputStream != null)
						inputStream.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	private String getFileName(MultivaluedMap<String, String> header) {
		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {
				String[] name = filename.split("=");
				String exactFileName = name[1].trim().replaceAll("\"", "");
				return exactFileName;
			}
		}
		return "unknown";
	}

	@Override
	public Response getUpdateSchedule(LastModifyDateJson obj) {
		System.out.println("updateSchedule json");
		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = defectScheduleManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<DefectSchedule> defectScheduleList = defectScheduleManager.searchDefectScheduleByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateSchedule : count = " + theCountOfTotalResults + ", defectScheduleList.size() = "
					+ defectScheduleList.size());

			WebServiceXml<DefectSchedule> xml = new WebServiceXml<DefectSchedule>();
			// xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("DefectScheduleTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(defectScheduleList.size());
			xml.setWebServiceXml(defectScheduleList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateScheduleAccount(LastModifyDateJson obj) {
		System.out.println("updateScheduleAccount json");
		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = defectScheduleManager.searchResultCountByDefectScheduleAccount(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<DefectScheduleAccount> defectScheduleAccountList = defectScheduleManager
					.searchDefectScheduleAccountByDate(
							lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult,
							siteKey);

			logger.debug("getUpdateScheduleAccount : count = " + theCountOfTotalResults
					+ ", defectScheduleAccountList.size() = " + defectScheduleAccountList.size());

			WebServiceXml<DefectScheduleAccount> xml = new WebServiceXml<DefectScheduleAccount>();
			// xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("DefectScheduleAccountTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(defectScheduleAccountList.size());
			xml.setWebServiceXml(defectScheduleAccountList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateDefectVideo(LastModifyDateJson obj) {
		// TODO Auto-generated method stub
		System.out.println("getUpdateDefectVideo");

		logger.debug(this.ObjectToJsonString(obj));

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			// Integer defaultMaxResult = 500;

			Integer siteKey = obj.getSiteKey();
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = defectFileManager.getDefectVideoCountByLastModifyDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<DefectVideo> defectVideoList = defectFileManager.getDefectVideoByLastModifyDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateDefectVideo : count = " + theCountOfTotalResults + ", defectList.size() = "
					+ defectVideoList.size());

			WebServiceXml<DefectVideo> xml = new WebServiceXml<DefectVideo>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(defectVideoList.size());
			xml.setWebServiceXml(defectVideoList);
			rtn.add(xml);

			logger.debug("return :" + this.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response downloadDefectVideo(DefectVideo defectVideo) {
		// TODO Auto-generated method stub
		logger.debug("downloadDefectVideo");

		try {
			String path = propertyConfigurer.getProperty("videoPath");
			// File file = new File("C:\\Users\\A\\Desktop\\test.png");
			File file = new File(path + defectVideo.getVideoPath());
			//
			// ResponseBuilder response = Response.ok((Object)file);
			// response.
			// response.header("Content-Disposition",
			// "attachment; filename=\"MyFile.png\"");
			if (file.exists()) {
				return Response.ok((Object) file, MediaType.APPLICATION_OCTET_STREAM)
						.header("Content-Length", file.length()).build();
			} else {

				logger.debug("File not exist : " + defectVideo.getVideoPath());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File not found").build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	public Properties getPropertyConfigurer() {
		return propertyConfigurer;
	}

	public void setPropertyConfigurer(Properties propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

	public CauseCodeManager getCauseCodeManager() {
		return causeCodeManager;
	}

	public void setCauseCodeManager(CauseCodeManager causeCodeManager) {
		this.causeCodeManager = causeCodeManager;
	}

	public ProblemCodeManager getProblemCodeManager() {
		return problemCodeManager;
	}

	public void setProblemCodeManager(ProblemCodeManager problemCodeManager) {
		this.problemCodeManager = problemCodeManager;
	}

	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	public EquipmentManager getEquipmentManager() {
		return equipmentManager;
	}

	public void setEquipmentManager(EquipmentManager equipmentManager) {
		this.equipmentManager = equipmentManager;
	}

	public DefectManager getDefectManager() {
		return defectManager;
	}

	public void setDefectManager(DefectManager defectManager) {
		this.defectManager = defectManager;
	}

	public DefectActionLogManager getDefectActionLogManager() {
		return defectActionLogManager;
	}

	public void setDefectActionLogManager(DefectActionLogManager defectActionLogManager) {
		this.defectActionLogManager = defectActionLogManager;
	}

	private <T> String listToJsonString(List<T> json) {
		ObjectMapper mapper = new ObjectMapper();

		String rtn = "";

		try {
			rtn = mapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtn;
	}

	private <T> String ObjectToJsonString(T json) {
		ObjectMapper mapper = new ObjectMapper();

		String rtn = "";

		try {
			rtn = mapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtn;
	}

}
