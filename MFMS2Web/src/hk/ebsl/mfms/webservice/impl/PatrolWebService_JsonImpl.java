package hk.ebsl.mfms.webservice.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.PatrolPhoto;
import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.manager.PatrolPhotoManager;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.webservice.PatrolWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.PatrolResultXml;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlMsg;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlStatus;
import hk.ebsl.mfms.websocket.PatrolMonitorWebSocket;
import hk.ebsl.mfms.websocket.UpadeWebPatrolResultJson;
import hk.ebsl.mfms.websocket.UpadeWebPatrolResultJson.PatrolResultAction;
import hk.ebsl.mfms.manager.SequenceNumberManager;

public class PatrolWebService_JsonImpl extends WebServiceUtil implements PatrolWebService_Json {

	public static final Logger logger = Logger.getLogger(PatrolWebService_JsonImpl.class);

	@Context
	private HttpHeaders headers;
	@Resource
	private PatrolResultManager patrolResultManager;
	@Resource
	private SequenceNumberManager SequenceNumberManager;
	@Resource
	private PatrolScheduleManager patrolScheduleManager;
	@Resource
	private PatrolPhotoManager patrolPhotoManager;

	@Resource
	private Properties propertyConfigurer;

	@Override
	public synchronized Response updatePatrolResult(List<PatrolResultXml> result) {
		// public List<PatrolResultXml> updatePatrolResult(List<PatrolResultXml>
		// result) {
		// TODO Auto-generated method stub

		System.out.println("updatePatrolResult json");
		System.out.println(this.listToJsonString(result));

		try {
			if (checkLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");

			}

			List<PatrolResultXml> rtn = new ArrayList<PatrolResultXml>();

			UpadeWebPatrolResultJson updateJson = new UpadeWebPatrolResultJson();

			int maxSeq = 0;
			int currentGroupNum = -1;
			int currentMobileGroupNum = -1;

			HashMap<Integer, UpadeWebPatrolResultJson> completedMap = new HashMap<Integer, UpadeWebPatrolResultJson>();

			for (int i = 0; i < result.size(); i++) {

				UpadeWebPatrolResultJson json = new UpadeWebPatrolResultJson();

				PatrolResultXml prXml = result.get(i);

				PatrolResult originalResult = null;
				PatrolResult pr = new PatrolResult();
				Boolean skipUpdate = false;
				int skippedInsertKey = -1;

				if (prXml.getGroupNum().equals(0)) {

					List<PatrolResult> duplicatedList = patrolResultManager.checkIfDuplicatedResult(
							prXml.getRouteDefKey(), prXml.getLocationKey(), prXml.getCreateBy(),
							new Timestamp(prXml.getCreateDateTime()));

					if (duplicatedList != null) {
						prXml.setResultKey(duplicatedList.get(0).getResultKey());
						pr.setGroupNum(duplicatedList.get(0).getGroupNum());

						if (duplicatedList.get(0).getCompleted().equals("Y")) {
							skipUpdate = true;
							skippedInsertKey = duplicatedList.get(0).getResultKey();
							prXml.setCompleted("Y");
						}

					} else {

						if (currentMobileGroupNum != prXml.getMobileGroupNum()) {

							currentMobileGroupNum = prXml.getMobileGroupNum();
							currentGroupNum = SequenceNumberManager.getAndUpdatePatrolGroupNum();
						}

						pr.setGroupNum(currentGroupNum);

					}
				} else {

					List<PatrolResult> duplicatedList = patrolResultManager.checkIfDuplicatedResult(
							prXml.getRouteDefKey(), prXml.getLocationKey(), prXml.getCreateBy(),
							new Timestamp(prXml.getCreateDateTime()));

					if (duplicatedList != null) {
						prXml.setResultKey(duplicatedList.get(0).getResultKey());
						pr.setGroupNum(duplicatedList.get(0).getGroupNum());

						if (duplicatedList.get(0).getCompleted().equals("Y")) {
							skipUpdate = true;
							skippedInsertKey = duplicatedList.get(0).getResultKey();
							prXml.setCompleted("Y");
						}
					} else {
						pr.setGroupNum(prXml.getGroupNum());
					}
				}

				// System.out.println("currentMobileGroupNum :
				// "+currentMobileGroupNum
				// +"|| "+currentGroupNum);

				if (prXml.getResultKey() > 0) {

					originalResult = patrolResultManager.getPatrolResultByKey(prXml.getResultKey());
					pr.setResultKey(prXml.getResultKey());

					// json.setPatrolResultKey(prXml.getResultKey());
					json.setAction(PatrolResultAction.Update);

				} else {

					json.setAction(PatrolResultAction.Create);
				}

				pr.setPatrolScheduleKey(prXml.getScheduleKey());
				pr.setSiteKey(prXml.getSiteKey());
				pr.setRouteDefKey(prXml.getRouteDefKey());

				pr.setCorrectLocationKey(prXml.getCorrectLocationKey());
				pr.setLocationKey(prXml.getLocationKey());
				pr.setSeqNum(prXml.getSeqNum());
				pr.setTimeAttended(new Timestamp(prXml.getTimeAttended()));
				pr.setReason(prXml.getReason());
				pr.setCompleted(prXml.getCompleted());
				pr.setCreateBy(prXml.getCreateBy());
				pr.setCreateDateTime(new Timestamp(prXml.getCreateDateTime()));
				pr.setLastModifyBy(prXml.getLastModifyBy());
				pr.setLastModifyDateTime(new Timestamp(prXml.getLastModifyDateTime()));
				pr.setDeleted(prXml.getDeleted());
				pr.setPersonAttended(prXml.getPersonAttended());
				pr.setPatrolStatus(prXml.getPatrolStatus());

				if (prXml.getMinPtDur() != null) {
					pr.setMinPtDur(prXml.getMinPtDur());
					pr.setMinPtDurUnit(prXml.getMinPtDurUnit());
					pr.setMaxPtDur(prXml.getMaxPtDur());
					pr.setMaxPtDurUnit(prXml.getMaxPtDurUnit());

				}

				json.setRouteDefKey(pr.getRouteDefKey());
				json.setScheduleKey(pr.getPatrolScheduleKey());
				json.setAccountKey(pr.getPersonAttended());
				json.setSiteKey(pr.getSiteKey());

				int insertKey = -1;
				if (skipUpdate) {
					insertKey = skippedInsertKey;
				} else {
					insertKey = patrolResultManager.savePatrolResult(pr);
				}

				json.setPatrolResultKey(insertKey);

				// System.out.println("Update Key : " + pr.getRouteDefKey()
				// + "||prKey:" + insertKey);

				if (pr.getCompleted().equals("Y")) {

					json.setAction(PatrolResultAction.Complete);

					completedMap.put(json.getRouteDefKey(), json);

				}

				PatrolResultXml xml = new PatrolResultXml();
				xml.setMobileKey(prXml.getMobileKey());
				xml.setGroupNum(pr.getGroupNum());
				xml.setCompleted(pr.getCompleted());
				xml.setTimeAttended(pr.getTimeAttended().getTime() == 0 ? null : pr.getTimeAttended().getTime());
				if (insertKey > 0) {

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());
					xml.setResultKey(insertKey);

				} else {
					xml.setXmlStatus(XmlStatus.FAIL.toString());
					xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
				}
				rtn.add(xml);

				if (json.getAction().equals(PatrolResultAction.Update)) {
					// System.out.println("TIME :"
					// + pr.getLastModifyDateTime().getTime() + "||"
					// + originalResult.getLastModifyDateTime().getTime());

					if (pr.getSeqNum() > maxSeq) {
						maxSeq = pr.getSeqNum();
						updateJson = json;
						// System.out.println("jSON:" + getJsonString(json));
					}

					if (pr.getSeqNum() > originalResult.getSeqNum()) {
						PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
						webSocket.onMessage(null, getJsonString(json));
					}

				} else {
					if (i == result.size() - 1) {
						patrolScheduleManager.updatePatrolScheduleLastAttendTime(pr.getPatrolScheduleKey(),
								pr.getCreateDateTime());

						PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
						webSocket.onMessage(null, getJsonString(json));

						for (Map.Entry<Integer, UpadeWebPatrolResultJson> map : completedMap.entrySet()) {
							webSocket.onMessage(null, getJsonString(map.getValue()));
						}
					}
				}

			}

			if (updateJson.getAction() != null && updateJson.getAction().equals(PatrolResultAction.Update)) {

				PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
				webSocket.onMessage(null, getJsonString(updateJson));
			}

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		// return null;
	}

	@Override
	public synchronized Response createAndUpdatePatrolResult(List<PatrolResult> result) {
		// TODO Auto-generated method stub

		logger.debug("createAndUpdatePatrolResult");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();
			// List<PatrolResult> rtn = new ArrayList<PatrolResult>();

			UpadeWebPatrolResultJson updateJson = new UpadeWebPatrolResultJson();

			int maxSeq = 0;
			int currentGroupNum = -1;
			int currentMobileGroupNum = -1;

			HashMap<Integer, UpadeWebPatrolResultJson> completedMap = new HashMap<Integer, UpadeWebPatrolResultJson>();

			for (int i = 0; i < result.size(); i++) {

				UpadeWebPatrolResultJson json = new UpadeWebPatrolResultJson();

				PatrolResult pr = result.get(i);

				PatrolResult originalResult = null;
				// PatrolResult pr = new PatrolResult();
				Boolean skipUpdate = false;
				int skippedInsertKey = -1;

				if (pr.getGroupNum() == 0) {

					List<PatrolResult> duplicatedList = patrolResultManager.checkIfDuplicatedResult(pr.getRouteDefKey(),
							pr.getLocationKey(), pr.getCreateBy(), pr.getCreateDateTime());

					if (duplicatedList != null) {
						pr.setResultKey(duplicatedList.get(0).getResultKey());
						pr.setGroupNum(duplicatedList.get(0).getGroupNum());

						if (duplicatedList.get(0).getCompleted().equals("Y")) {
							skipUpdate = true;
							skippedInsertKey = duplicatedList.get(0).getResultKey();
							pr.setCompleted("Y");
						}

					} else {
						if (currentMobileGroupNum != pr.getMobileGroupNum()) {

							currentMobileGroupNum = pr.getMobileGroupNum();
							currentGroupNum = SequenceNumberManager.getAndUpdatePatrolGroupNum();
						}

						pr.setGroupNum(currentGroupNum);
					}
				} else {

					List<PatrolResult> duplicatedList = patrolResultManager.checkIfDuplicatedResult(pr.getRouteDefKey(),
							pr.getLocationKey(), pr.getCreateBy(), pr.getCreateDateTime());

					if (duplicatedList != null) {
						pr.setResultKey(duplicatedList.get(0).getResultKey());
						pr.setGroupNum(duplicatedList.get(0).getGroupNum());

						if (duplicatedList.get(0).getCompleted().equals("Y")) {
							skipUpdate = true;
							skippedInsertKey = duplicatedList.get(0).getResultKey();
							pr.setCompleted("Y");
						}
					} else {
						pr.setGroupNum(pr.getGroupNum());
					}
				}

				if (pr.getResultKey() > 0) {

					originalResult = patrolResultManager.getPatrolResultByKey(pr.getResultKey());
					pr.setResultKey(pr.getResultKey());

					// json.setPatrolResultKey(prXml.getResultKey());
					json.setAction(PatrolResultAction.Update);

				} else {

					json.setAction(PatrolResultAction.Create);
				}

				pr.setDeleted("N");
				pr.setTimeAttended(pr.getTimeAttended() == null ? new Timestamp(0) : pr.getTimeAttended());

				json.setRouteDefKey(pr.getRouteDefKey());
				json.setScheduleKey(pr.getPatrolScheduleKey());
				json.setAccountKey(pr.getPersonAttended());
				json.setSiteKey(pr.getSiteKey());

				int insertKey = -1;
				if (skipUpdate) {
					insertKey = skippedInsertKey;
				} else {
					insertKey = patrolResultManager.savePatrolResult(pr);
				}

				json.setPatrolResultKey(insertKey);

				if (pr.getCompleted().equals("Y")) {

					json.setAction(PatrolResultAction.Complete);

					completedMap.put(json.getRouteDefKey(), json);

				}

				WebServiceXml<PatrolResult> xml = new WebServiceXml<PatrolResult>();
				xml.setMobileKey(pr.getMobileKey());
				xml.setXmlStatus("success");

				List<PatrolResult> tmp = new ArrayList<PatrolResult>();
				tmp.add(patrolResultManager.getPatrolResultByKey(insertKey));
				xml.setWebServiceXml(tmp);

				rtn.add(xml);

				if (json.getAction().equals(PatrolResultAction.Update)) {
					// System.out.println("TIME :"
					// + pr.getLastModifyDateTime().getTime() + "||"
					// + originalResult.getLastModifyDateTime().getTime());

					if (pr.getSeqNum() > maxSeq) {
						maxSeq = pr.getSeqNum();
						updateJson = json;
						// System.out.println("jSON:" + getJsonString(json));
					}

					if (pr.getSeqNum() > originalResult.getSeqNum()) {
						PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
						webSocket.onMessage(null, getJsonString(json));
					}

				} else {
					if (i == result.size() - 1) {
						patrolScheduleManager.updatePatrolScheduleLastAttendTime(pr.getPatrolScheduleKey(),
								pr.getCreateDateTime());

						PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
						webSocket.onMessage(null, getJsonString(json));

						for (Map.Entry<Integer, UpadeWebPatrolResultJson> map : completedMap.entrySet()) {
							webSocket.onMessage(null, getJsonString(map.getValue()));
						}
					}
				}

			}

			if (updateJson.getAction() != null && updateJson.getAction().equals(PatrolResultAction.Update)) {

				PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
				webSocket.onMessage(null, getJsonString(updateJson));
			}

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response createPatrolPhoto(List<PatrolPhoto> list, List<Attachment> files) {
		// TODO Auto-generated method stub

		logger.debug("createPatrolPhoto : " + listToJsonString(list));
		logger.debug("createPatrolPhoto");

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		try {
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			for (PatrolPhoto photo : list) {

				photo.setIsDeleted("N");
				int insertKey = patrolPhotoManager.savePatrolPhoto(photo);

				WebServiceXml<PatrolPhoto> xml = new WebServiceXml<PatrolPhoto>();
				xml.setMobileKey(photo.getMobileKey());
				xml.setXmlStatus("success");

				List<PatrolPhoto> tmp = new ArrayList<PatrolPhoto>();
				tmp.add(patrolPhotoManager.searchPatrolPhotobyKey(insertKey));
				xml.setWebServiceXml(tmp);

				rtn.add(xml);

			}

			saveFile(files);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response getPreviousPatrolResult() {
		// TODO Auto-generated method stub

		System.out.println("getPreviousPatrolResult");

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		try {
			UserAccount ac = getUserAccount(headers);

			List<PatrolResult> patrolResultList = patrolResultManager.getIncompleteProgress(ac.getKey());

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			logger.debug("getPreviousPatrolResult : count = " + patrolResultList.size() + ", patrolResultList.size() = "
					+ patrolResultList.size());

			WebServiceXml<PatrolResult> xml = new WebServiceXml<PatrolResult>();
			xml.setMobileKey(-1);
			xml.setXmlStatus("success");
			xml.setWebServiceXml(patrolResultList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response updatePatrolSchedule(LastModifyDateJson obj) {
		// TODO Auto-generated method stub

		System.out.println("updatePatrolSchedule");

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		try {
			UserAccount ac = getUserAccount(headers);

			Integer siteKey = obj.getSiteKey();

			List<PatrolSchedule> patrolScheduleList = patrolScheduleManager
					.searchPatrolScheduleByLastModifyTime(new Timestamp(obj.getLastModifyDate()), null, siteKey);

			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());

			List<PatrolSchedule> offsetList = new ArrayList<PatrolSchedule>();
			offsetList = patrolScheduleList.subList(offset, (offset + maxResult) >= patrolScheduleList.size()
					? patrolScheduleList.size() : (offset + maxResult));

			Calendar mE = Calendar.getInstance();
			mE.add(Calendar.YEAR, 100);

			logger.debug("getUpdatePatrolSchedule : count = " + patrolScheduleList.size() + ", offsetList.size() = "
					+ offsetList.size());

			for (PatrolSchedule schedule : offsetList) {
				schedule.setScheduleEndDate(schedule.getScheduleEndDate() == null ? new Timestamp(mE.getTimeInMillis())
						: schedule.getScheduleEndDate());
				schedule.setSkippedStartDate(
						schedule.getSkippedStartDate() == null ? new Timestamp(0) : schedule.getSkippedStartDate());
				schedule.setSkippedEndDate(
						schedule.getSkippedEndDate() == null ? new Timestamp(0) : schedule.getSkippedEndDate());
				schedule.setLastAttendTime(
						schedule.getLastAttendTime() == null ? new Timestamp(0) : schedule.getLastAttendTime());
			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();
			WebServiceXml<PatrolSchedule> xml = new WebServiceXml<PatrolSchedule>();
			xml.setMobileKey(-1);
			xml.setXmlStatus("success");
			xml.setTotal(patrolScheduleList.size());
			xml.setCount(offsetList.size());
			xml.setWebServiceXml(patrolScheduleList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {

			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Response updatePatrolScheduleAccount(LastModifyDateJson obj) {
		// TODO Auto-generated method stub

		System.out.println("updatePatrolScheduleAccount");

		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		try {

			UserAccount ac = getUserAccount(headers);
			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());

			Integer theCountOfTotalResults = patrolScheduleManager.getPatrolScheduleAccountCount(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), null, siteKey);

			List<PatrolScheduleAccount> accountList = patrolScheduleManager.searchPatrolScheduleAccountByLastModifyTime(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, null,
					siteKey);

			logger.debug("updatePatrolScheduleAccount : count = " + theCountOfTotalResults
					+ ", PatrolScheduleAccount.size() = " + accountList.size());

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			WebServiceXml<PatrolScheduleAccount> xml = new WebServiceXml<PatrolScheduleAccount>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(accountList.size());
			xml.setWebServiceXml(accountList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	private void saveFile(List<Attachment> files) {

		for (Attachment file : files) {

			DataHandler handler = file.getDataHandler();
			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				inputStream = handler.getInputStream();

				MultivaluedMap<String, String> map = file.getHeaders();
				System.out.println("fileName Here : " + getFileName(map));
				// outputStream = new FileOutputStream(new File(
				// "C:\\Users\\A\\Desktop\\" + getFileName(map)));

				String dir = propertyConfigurer.getProperty("photoPath");

				outputStream = new FileOutputStream(new File(dir + "/" + getFileName(map)));

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

	public String getJsonString(UpadeWebPatrolResultJson json) {
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

	public PatrolResultManager getPatrolResultManager() {
		return patrolResultManager;
	}

	public void setPatrolResultManager(PatrolResultManager patrolResultManager) {
		this.patrolResultManager = patrolResultManager;
	}

	public SequenceNumberManager getSequenceNumberManager() {
		return SequenceNumberManager;
	}

	public void setSequenceNumberManager(SequenceNumberManager sequenceNumberManager) {
		SequenceNumberManager = sequenceNumberManager;
	}

	public PatrolScheduleManager getPatrolScheduleManager() {
		return patrolScheduleManager;
	}

	public void setPatrolScheduleManager(PatrolScheduleManager patrolScheduleManager) {
		this.patrolScheduleManager = patrolScheduleManager;
	}

	public PatrolPhotoManager getPatrolPhotoManager() {
		return patrolPhotoManager;
	}

	public void setPatrolPhotoManager(PatrolPhotoManager patrolPhotoManager) {
		this.patrolPhotoManager = patrolPhotoManager;
	}

	public Properties getPropertyConfigurer() {
		return propertyConfigurer;
	}

	public void setPropertyConfigurer(Properties propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

}
