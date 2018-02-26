package hk.ebsl.mfms.webservice.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import hk.ebsl.mfms.dto.PatrolPhoto;
import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSWebServiceException;
import hk.ebsl.mfms.manager.PatrolPhotoManager;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.webservice.PatrolWebService;
import hk.ebsl.mfms.webservice.xml.PatrolPhotoXml;
import hk.ebsl.mfms.webservice.xml.PatrolResultXml;
import hk.ebsl.mfms.webservice.xml.PatrolScheduleXml;
import hk.ebsl.mfms.webservice.xml.RouteDefXml;
import hk.ebsl.mfms.webservice.xml.RouteLocationXml;
import hk.ebsl.mfms.webservice.xml.BaseXml;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlMsg;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlStatus;
import hk.ebsl.mfms.websocket.PatrolMonitorWebSocket;
import hk.ebsl.mfms.websocket.UpadeWebPatrolResultJson;
import hk.ebsl.mfms.websocket.UpadeWebPatrolResultJson.PatrolResultAction;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.wsdl.Output;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@WebService(endpointInterface = "hk.ebsl.mfms.webservice.PatrolWebService", serviceName = "patrolWebService")
public class PatrolWebServiceImpl extends WebServiceUtil implements
		PatrolWebService {

	@Resource
	WebServiceContext wsCtxt;
	@Resource
	private RouteDefManager routeDefManager;
	@Resource
	private PatrolResultManager patrolResultManager;
	@Resource
	private PatrolScheduleManager patrolScheduleManager;
	@Resource
	private UserAccountManager userAccountManager;
	@Resource
	private PatrolPhotoManager patrolPhotoManager;
	@Resource
	private Properties propertyConfigurer;

	// private Session session;

	@Override
	public List<RouteDefXml> updateRouteDef(long lastModifyDate) {
		// TODO Auto-generated method stub

		System.out.println("LAST " + lastModifyDate);

		List<RouteDefXml> xmlList = new ArrayList<RouteDefXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			// List<RouteDef> routeDefList = new ArrayList<RouteDef>();
			List<RouteDef> routeDefList = routeDefManager
					.getRouteDefWithLastModifedTime(new Timestamp(
							lastModifyDate));

			for (RouteDef routeDef : routeDefList) {
				RouteDefXml xml = new RouteDefXml();

				xml.setMobileKey(-1);
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				System.out.println("Key : " + routeDef.getRouteDefKey());
				xml.setRouteDefKey(routeDef.getRouteDefKey());
				xml.setName(routeDef.getName());
				xml.setCode(routeDef.getCode());
				xml.setCreateBy(routeDef.getCreateBy());
				xml.setCreateDateTime(routeDef.getCreateDateTime().getTime());
				xml.setDefMaxPtDur(routeDef.getDefMaxPtDur());
				xml.setDefMaxPtDurUnit(routeDef.getDefMaxPtDurUnit());
				xml.setDefMinPtDur(routeDef.getDefMinPtDur());
				xml.setDefMinPtDurUnit(routeDef.getDefMinPtDurUnit());
				xml.setLastModifyBy(routeDef.getLastModifyBy());
				xml.setLastModifyDateTime(routeDef.getLastModifyDateTime()
						.getTime());
				xml.setDeleted(routeDef.getDeleted());

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			RouteDefXml xml = new RouteDefXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	@Override
	public List<RouteLocationXml> updateRouteLocation(long lastModifyDate) {
		// TODO Auto-generated method stub

		List<RouteLocationXml> xmlList = new ArrayList<RouteLocationXml>();
		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			List<RouteLocation> routeLocationList = routeDefManager
					.getRouteLocationWithLastModifiedTime(new Timestamp(
							lastModifyDate));

			for (RouteLocation routeLocation : routeLocationList) {
				RouteLocationXml xml = new RouteLocationXml();

				xml.setXmlStatus(XmlStatus.SUCCESS.toString());
				xml.setMobileKey(-1);
				xml.setRouteLocationKey(routeLocation.getRouteLocationKey());
				xml.setRouteDefKey(routeLocation.getRouteDefKey());
				xml.setRouteLocationKey(routeLocation.getRouteLocationKey());
				xml.setLocationKey(routeLocation.getLocationKey());
				xml.setSeqNum(routeLocation.getSeqNum());
				xml.setMaxPtDur(routeLocation.getMaxPtDur());
				xml.setMaxPtDurUnit(routeLocation.getMaxPtDurUnit());
				xml.setMinPtDur(routeLocation.getMinPtDur());
				xml.setMinPtDurUnit(routeLocation.getMinPtDurUnit());
				xml.setRemark(routeLocation.getRemark());
				xml.setCreateBy(routeLocation.getCreateBy());
				xml.setCreateDateTime(routeLocation.getCreateDateTime()
						.getTime());
				xml.setLastModifyBy(routeLocation.getLastModifyBy());
				xml.setLastModifyDateTime(routeLocation.getLastModifyDateTime()
						.getTime());
				xml.setDeleted(routeLocation.getDeleted());

				xmlList.add(xml);

			}

			return xmlList;
		} catch (MFMSWebServiceException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			RouteLocationXml xml = new RouteLocationXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	@Override
	public PatrolResultXml updatePatrolResult(int patrolScheduleKey,
			int mobileKey, int siteKey, int resultKey, int routeDefKey,
			int groupNum, int correctLocationKey, int locationKey, int seqNum,
			int personAttended, long timeAttended, String reason,
			String completed, int createBy, long createDateTime,
			int lastModifyBy, long lastModifyDateTime, String deleted,
			String patrolStatus) {
		// TODO Auto-generated method stub
		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			PatrolResult orginalResult = null;

			PatrolResult pr = new PatrolResult();
			if (resultKey > 0) {
				orginalResult = patrolResultManager
						.getPatrolResultByKey(resultKey);
				pr.setResultKey(resultKey);

			}
			pr.setPatrolScheduleKey(patrolScheduleKey);
			pr.setSiteKey(siteKey);
			pr.setRouteDefKey(routeDefKey);
			pr.setGroupNum(groupNum);
			pr.setCorrectLocationKey(correctLocationKey);
			pr.setLocationKey(locationKey);
			pr.setSeqNum(seqNum);
			pr.setTimeAttended(new Timestamp(timeAttended));
			pr.setReason(reason);
			pr.setCompleted(completed);
			pr.setCreateBy(createBy);
			pr.setCreateDateTime(new Timestamp(createDateTime));
			pr.setLastModifyBy(lastModifyBy);
			pr.setLastModifyDateTime(new Timestamp(lastModifyDateTime));
			pr.setDeleted(deleted);
			pr.setPersonAttended(personAttended);
			pr.setPatrolStatus(patrolStatus);

			System.out.println("Update Key : " + routeDefKey);
			int insertKey = patrolResultManager.savePatrolResult(pr);

			PatrolResultXml xml = new PatrolResultXml();
			if (insertKey > 0) {

				xml.setXmlStatus(XmlStatus.SUCCESS.toString());
				xml.setResultKey(insertKey);
			} else {
				xml.setXmlStatus(XmlStatus.FAIL.toString());
				xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
			}

			UpadeWebPatrolResultJson json = new UpadeWebPatrolResultJson();
			json.setPatrolResultKey(insertKey);
			json.setRouteDefKey(routeDefKey);
			json.setScheduleKey(patrolScheduleKey);
			json.setAccountKey(personAttended);
			json.setSiteKey(siteKey);

			if (orginalResult != null) {
				System.out.println("Orgin : "
						+ orginalResult.getLastModifyDateTime().getTime()
						+ " || " + lastModifyDateTime);

				if ((lastModifyDateTime - orginalResult.getLastModifyDateTime()
						.getTime()) > 500) {
					json.setAction(PatrolResultAction.Update);
					if (completed.equals("Y")) {
						json.setAction(PatrolResultAction.Complete);
					}

					try {
						PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
						webSocket.onMessage(null, getJsonString(json));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{

			if (resultKey == 0 && !completed.equals("Y")) {
				json.setAction(PatrolResultAction.Create);
				if (patrolScheduleKey > -1) {
					// update the schedule last attend time
					patrolScheduleManager.updatePatrolScheduleLastAttendTime(
							patrolScheduleKey, new Timestamp(createDateTime));
				}
				try {
					PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
					webSocket.onMessage(null, getJsonString(json));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				json.setAction(PatrolResultAction.Complete);
				try {
					PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
					webSocket.onMessage(null, getJsonString(json));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			}
			return xml;
		} catch (MFMSWebServiceException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			PatrolResultXml xml = new PatrolResultXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			return xml;
		}
	}

	@Override
	public List<PatrolScheduleXml> updatePatrolSchedule(long lastModifyDate) {
		// TODO Auto-generated method stub
		List<PatrolScheduleXml> xmlList = new ArrayList<PatrolScheduleXml>();
		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			Map map = (Map) wsCtxt.getMessageContext().get(
					MessageContext.HTTP_REQUEST_HEADERS);
			ArrayList ac = (ArrayList) map.get(account);
			ArrayList pwd = (ArrayList) map.get(password);

			UserAccount userAccount = userAccountManager.checkUserLogin(
					(String) ac.get(0), (String) pwd.get(0));

			List<PatrolSchedule> patrolScheduleList = patrolScheduleManager
					.searchPatrolScheduleByLastModifyTime(new Timestamp(
							lastModifyDate), userAccount.getKey(),null);

			Calendar mE = Calendar.getInstance();
			mE.add(Calendar.YEAR, 100);

			for (PatrolSchedule schedule : patrolScheduleList) {
				PatrolScheduleXml xml = new PatrolScheduleXml();

				xml.setScheduleKey(schedule.getScheduleKey());
				xml.setRouteDefKey(schedule.getRouteDefKey());
				xml.setScheduleStartDate(schedule.getScheduleStartDate()
						.getTime());
				xml.setScheduleEndDate(schedule.getScheduleEndDate() == null ? mE
						.getTimeInMillis() : schedule.getScheduleEndDate()
						.getTime());
				xml.setScheduleTime(schedule.getScheduleTime().getTime());
				xml.setFrequency(schedule.getFrequency());
				xml.setStatus(schedule.getStatus());
				xml.setParentId(schedule.getParentId());
				xml.setRemarks(schedule.getRemarks());
				xml.setCreateBy(schedule.getCreateBy());
				xml.setCreateDateTime(schedule.getCreateDateTime().getTime());
				xml.setLastModifyBy(schedule.getLastModifyBy());
				xml.setLastModifyDateTime(schedule.getLastModifyDateTime()
						.getTime());
				xml.setDeleted(schedule.getDeleted());
				xml.setSkipped(schedule.getSkipped());
				xml.setSkippedStartDate(schedule.getSkippedStartDate() == null ? 0
						: schedule.getSkippedStartDate().getTime());
				xml.setSkippedEndDate(schedule.getSkippedEndDate() == null ? 0
						: schedule.getSkippedEndDate().getTime());
				xml.setLastAttendTime(schedule.getLastAttendTime() == null ? 0
						: schedule.getLastAttendTime().getTime());

				xml.setMobileKey(-1);
				xml.setXmlStatus(XmlStatus.SUCCESS.toString());

				xmlList.add(xml);
				
			}

			return xmlList;

		} catch (MFMSWebServiceException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			PatrolScheduleXml xml = new PatrolScheduleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	@Override
	public List<PatrolResultXml> getPreviousPatrolResult() {

		List<PatrolResultXml> xmlList = new ArrayList<PatrolResultXml>();
		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			Map map = (Map) wsCtxt.getMessageContext().get(
					MessageContext.HTTP_REQUEST_HEADERS);
			ArrayList ac = (ArrayList) map.get(account);
			ArrayList pwd = (ArrayList) map.get(password);

			UserAccount userAccount = userAccountManager.checkUserLogin(
					(String) ac.get(0), (String) pwd.get(0));

			List<PatrolResult> patrolResultList = patrolResultManager
					.getIncompleteProgress(userAccount.getKey());

			for (PatrolResult p : patrolResultList) {
				PatrolResultXml xml = new PatrolResultXml();

				xml.setXmlStatus(XmlStatus.SUCCESS.toString());
				xml.setRouteDefKey(p.getRouteDefKey());
				xml.setGroupNum(p.getGroupNum());
				xml.setCorrectLocationKey(p.getCorrectLocationKey());
				xml.setLocationKey(p.getLocationKey());
				xml.setSeqNum(p.getSeqNum());
				xml.setTimeAttended(p.getTimeAttended().getTime());
				xml.setReason(p.getReason());
				xml.setCompleted(p.getCompleted());
				xml.setCreateBy(p.getCreateBy());
				xml.setCreateDateTime(p.getCreateDateTime().getTime());
				xml.setLastModifyBy(p.getLastModifyBy());
				xml.setLastModifyDateTime(p.getLastModifyDateTime().getTime());
				xml.setDeleted(p.getDeleted());
				xml.setPersonAttended(p.getPersonAttended());
				xml.setScheduleKey(p.getPatrolScheduleKey());
				xml.setSiteKey(p.getSiteKey());
				xml.setResultKey(p.getResultKey());
				xml.setPatrolStatus(p.getPatrolStatus());

				xmlList.add(xml);
			}

			return xmlList;

		} catch (MFMSWebServiceException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			PatrolResultXml xml = new PatrolResultXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}

	}

	@Override
	public List<PatrolPhotoXml> uploadPatrolPhoto(int mobileKey, int siteKey,
			int routeDefKey, int patrolResultKey, int locationKey,
			String remark, int createBy, long createDateTime, int lastModifyBy,
			long lastModifyDateTime, String deleted, byte[] photo) {
		// TODO Auto-generated method stub

		List<PatrolPhotoXml> xmlList = new ArrayList<PatrolPhotoXml>();

		try {

			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				throw new MFMSWebServiceException("Login Fail");

			}

			Map map = (Map) wsCtxt.getMessageContext().get(
					MessageContext.HTTP_REQUEST_HEADERS);
			ArrayList ac = (ArrayList) map.get(account);
			ArrayList pwd = (ArrayList) map.get(password);

			int rand = (int) (Math.random() * 10000);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String filename = "patrolPhoto_"
					+ sdf.format(new Date(createDateTime)) + "_" + rand
					+ ".jpg";
			String dir = propertyConfigurer.getProperty("photoPath");

			System.out.println("filename : " + filename);

			new File(dir).mkdirs();
			File file = new File(dir, filename);
			try {
				FileOutputStream output = new FileOutputStream(file);

				try {
					output.write(photo, 0, photo.length);

					PatrolPhoto pp = new PatrolPhoto();
					pp.setRouteDefkey(routeDefKey);
					pp.setPhotoPath(filename);
					pp.setSiteKey(siteKey);
					pp.setPatrolResultKey(patrolResultKey);
					pp.setLocationKey(locationKey);
					pp.setRemark(remark);
					pp.setLastModifyBy(lastModifyBy);
					pp.setLastModifyDateTime(new Timestamp(lastModifyDateTime));
					pp.setCreateBy(createBy);
					pp.setCreateDateTime(new Timestamp(createDateTime));
					pp.setIsDeleted(deleted);

					int insertKey = patrolPhotoManager.savePatrolPhoto(pp);

					PatrolPhotoXml xml = new PatrolPhotoXml();
					if (insertKey > 0) {

						xml.setXmlStatus(XmlStatus.SUCCESS.toString());
						xml.setPhotoKey(insertKey);
					} else {
						xml.setXmlStatus(XmlStatus.FAIL.toString());
						xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
					}

					xmlList.add(xml);
				} finally {
					try {
						output.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				PatrolPhotoXml xml = new PatrolPhotoXml();
				xml.setXmlStatus(XmlStatus.FAIL.toString());
				xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
				xmlList.add(xml);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				PatrolPhotoXml xml = new PatrolPhotoXml();
				xml.setXmlStatus(XmlStatus.FAIL.toString());
				xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
				xmlList.add(xml);
			}

			return xmlList;

		} catch (MFMSWebServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			PatrolPhotoXml xml = new PatrolPhotoXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}

	}
	
//	@Override
//	public String updatePatrolResultByJson(String jsonArray){
//		
//		
//		System.out.println(jsonArray);
//		
//		return "success";
//	}
	

	/**                 **/
	/** Other Functions **/
	/**                 **/
	public RouteDefManager getRouteDefManager() {
		return routeDefManager;
	}

	public void setRouteDefManager(RouteDefManager routeDefManager) {
		this.routeDefManager = routeDefManager;
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

	

}
