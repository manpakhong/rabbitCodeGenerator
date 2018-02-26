package hk.ebsl.mfms.webservice.impl;

import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSWebServiceException;
import hk.ebsl.mfms.manager.PatrolPhotoManager;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.webservice.DemoWebService;
import hk.ebsl.mfms.webservice.PatrolWebService;
import hk.ebsl.mfms.webservice.xml.PatrolResultXml;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlMsg;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlStatus;
import hk.ebsl.mfms.websocket.PatrolMonitorWebSocket;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@WebService(endpointInterface = "hk.ebsl.mfms.webservice.DemoWebService", serviceName = "demoWebService")
public class DemoWebServiceImpl extends WebServiceUtil implements
		DemoWebService {
//
//	@Resource
//	WebServiceContext wsCtxt;
//	@Resource
//	private RouteDefManager routeDefManager;
//	@Resource
//	private PatrolResultManager patrolResultManager;
//	@Resource
//	private PatrolScheduleManager patrolScheduleManager;
//	@Resource
//	private UserAccountManager userAccountManager;
//	@Resource
//	private PatrolPhotoManager patrolPhotoManager;
//	@Resource
//	private Properties propertyConfigurer;
//
//	// private Session session;
//
//	enum PatrolResultAction {
//		Create, Update, Complete
//	};
//
//	@Override
//	public PatrolResultXml updatePatrolResult(String login, String pwd,
//			int patrolScheduleKey, int mobileKey, int siteKey, int resultKey,
//			int routeDefKey, int groupNum, int correctLocationKey,
//			int locationKey, int seqNum, int personAttended, long timeAttended,
//			String reason, String completed, int createBy, long createDateTime,
//			int lastModifyBy, long lastModifyDateTime, String deleted,
//			String patrolStatus) {
//		// TODO Auto-generated method stub
//
//		
//		timeAttended = timeConvert(timeAttended);
//		createDateTime = timeConvert(createDateTime);
//		lastModifyDateTime = timeConvert(lastModifyDateTime);
//		
//		try {
//			if (checkLogin(login, pwd)) {
//				System.out.println("Login Success");
//			} else {
//
//				System.out.println("Login Fail");
//				throw new MFMSWebServiceException("Login Fail");
//
//			}
//
//			PatrolResult orginalResult = null;
//
//			PatrolResult pr = new PatrolResult();
//			if (resultKey > 0) {
//				orginalResult = patrolResultManager
//						.getPatrolResultByKey(resultKey);
//				pr.setResultKey(resultKey);
//
//			}
//			pr.setPatrolScheduleKey(patrolScheduleKey);
//			pr.setSiteKey(siteKey);
//			pr.setRouteDefKey(routeDefKey);
//			pr.setGroupNum(groupNum);
//			pr.setCorrectLocationKey(correctLocationKey);
//			pr.setLocationKey(locationKey);
//			pr.setSeqNum(seqNum);
//			pr.setTimeAttended(new Timestamp(timeAttended));
//			pr.setReason(reason);
//			pr.setCompleted(completed);
//			pr.setCreateBy(createBy);
//			pr.setCreateDateTime(new Timestamp(createDateTime));
//			pr.setLastModifyBy(lastModifyBy);
//			pr.setLastModifyDateTime(new Timestamp(lastModifyDateTime));
//			pr.setDeleted(deleted);
//			pr.setPersonAttended(personAttended);
//			pr.setPatrolStatus(patrolStatus);
//
//			System.out.println("Update Key : " + routeDefKey);
//			int insertKey = patrolResultManager.savePatrolResult(pr);
//
//			PatrolResultXml xml = new PatrolResultXml();
//			if (insertKey > 0) {
//
//				xml.setXmlStatus(XmlStatus.SUCCESS.toString());
//				xml.setResultKey(insertKey);
//			} else {
//				xml.setXmlStatus(XmlStatus.FAIL.toString());
//				xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
//			}
//
//			EditPatrolResult json = new EditPatrolResult();
//			json.setPatrolResultKey(insertKey);
//			json.setRouteDefKey(routeDefKey);
//			json.setScheduleKey(patrolScheduleKey);
//			json.setAccountKey(personAttended);
//
//			if (orginalResult != null) {
//				System.out.println("Orgin : "
//						+ orginalResult.getLastModifyDateTime().getTime()
//						+ " || " + lastModifyDateTime);
//
//				if ((lastModifyDateTime - orginalResult.getLastModifyDateTime()
//						.getTime()) > 1000) {
//					json.setAction(PatrolResultAction.Update);
//					if (completed.equals("Y")) {
//						json.setAction(PatrolResultAction.Complete);
//					}
//
//					PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
//					webSocket.onMessage(null, getJsonString(json));
//				}
//			}
//
//			if (resultKey == 0) {
//				json.setAction(PatrolResultAction.Create);
//				if (patrolScheduleKey > -1) {
//					// update the schedule last attend time
//					patrolScheduleManager.updatePatrolScheduleLastAttendTime(
//							patrolScheduleKey, new Timestamp(createDateTime));
//				}
//
//				PatrolMonitorWebSocket webSocket = new PatrolMonitorWebSocket();
//				webSocket.onMessage(null, getJsonString(json));
//			}
//
//			return xml;
//		} catch (MFMSWebServiceException e) {
//			// TODO Auto-generated catch block
//
//			e.printStackTrace();
//			PatrolResultXml xml = new PatrolResultXml();
//			xml.setXmlStatus(XmlStatus.FAIL.toString());
//			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());
//
//			return xml;
//		}
//	}
//
//	public Boolean checkLogin(String ac, String pwd) {
//
//		try {
//
//			System.out.println("AC :" + ac + " || PWD : " + pwd);
//
//			UserAccount userAccount = userAccountManager
//					.checkUserLogin(ac, pwd);
//
//			if (userAccount == null) {
//				return false;
//			} else {
//				return true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//
//	}
//
//	public String getJsonString(EditPatrolResult json) {
//		ObjectMapper mapper = new ObjectMapper();
//
//		String rtn = "";
//
//		try {
//			rtn = mapper.writeValueAsString(json);
//		} catch (JsonGenerationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return rtn;
//	}
//
//	class EditPatrolResult {
//
//		int scheduleKey;
//		int routeDefKey;
//		int patrolResultKey;
//		int accountKey;
//
//		PatrolResultAction action;
//
//		public int getRouteDefKey() {
//			return routeDefKey;
//		}
//
//		public void setRouteDefKey(int routeDefKey) {
//			this.routeDefKey = routeDefKey;
//		}
//
//		public int getPatrolResultKey() {
//			return patrolResultKey;
//		}
//
//		public void setPatrolResultKey(int patrolResultKey) {
//			this.patrolResultKey = patrolResultKey;
//		}
//
//		public PatrolResultAction getAction() {
//			return action;
//		}
//
//		public void setAction(PatrolResultAction action) {
//			this.action = action;
//		}
//
//		public int getScheduleKey() {
//			return scheduleKey;
//		}
//
//		public void setScheduleKey(int scheduleKey) {
//			this.scheduleKey = scheduleKey;
//		}
//
//		public int getAccountKey() {
//			return accountKey;
//		}
//
//		public void setAccountKey(int accountKey) {
//			this.accountKey = accountKey;
//		}
//
//	}
//
//	private long timeConvert(long t) {
//		try {
//			String str = String.valueOf(t);
//
//			System.out.println("Time str : "+str);
//			
//			if(str.equals("0")){
//				Date date = new Date(0);
//				return date.getTime();
//			}
//			
//			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
//			Date parsedDate = dateFormat.parse(str);
//
//			Timestamp timestamp = new Timestamp(parsedDate.getTime());
//
//			return timestamp.getTime();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
//
//	}

}
