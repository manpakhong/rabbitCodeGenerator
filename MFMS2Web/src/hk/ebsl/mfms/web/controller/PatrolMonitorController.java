package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.PatrolExpireTimeJSON;
import hk.ebsl.mfms.json.PatrolResultDisplayDetailJSON;
import hk.ebsl.mfms.json.PatrolResultDisplayJSON;
import hk.ebsl.mfms.json.PatrolResultDisplayJSON.ScheduleStatus;
import hk.ebsl.mfms.manager.EmailManager;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.Patrol_Route;
import hk.ebsl.mfms.utility.CalendarEvent;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PatrolMonitorController {

	static class ModelMappingValue {
		public static final String pages_patrolMenu = "patrol/monitor/patrol_monitor";

		public static final String pages_view_currentPatrol = "view_currentPatrol";
		public static final String pages_view_futurePatrol = "view_futurePatrol";
		public static final String pages_view_currentPatrolResult = "view_currentPatrolResult";
		public static final String pages_view_updateCurrentPatrol = "view_updateCurrentPatrol";
		public static final String pages_view_updatePatrolResultResult = "view_updateCurrentPatrolResult";
		public static final String pages_view_alert = "view_alert";

		public static final String var_currentPatrolResult = "currentPatrolResult";
		public static final String var_currentPatrolResultDetail = "currentPatrolResultDeatil";
		public static final String var_currentSeq = "currentSeq";
		public static final String var_expireTimeJson = "expireTimeJson";
		public static final String var_futurePatrol = "futurePatrol";
		public static final String var_futureTimeJson = "futureTimeJson";
		public static final String var_alert_routeName = "routeName";
		public static final String var_alert_msg = "msg";
		public static final String var_alert_id = "alertId";
		public static final String var_currentSiteKey = "currentSiteKey";
		public static final String var_closeAlertId = "closeAlertId";

	}

	public final static Logger logger = Logger
			.getLogger(PatrolMonitorController.class);

	@Autowired
	private SiteManager siteManager;

	@Autowired
	private RouteDefManager routeDefManager;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private Properties propertyConfigurer;
	@Autowired
	private Properties privilegeMap;
	@Autowired
	private PatrolResultManager patrolResultManager;
	@Autowired
	private PatrolScheduleManager patrolScheduleManager;
	@Autowired
	private CalendarEvent calendarEvent;
	@Autowired
	private EmailManager emailManager;

	SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat expireTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat onlyTimeFormat = new SimpleDateFormat("HH:mm");

	@RequestMapping(value = "/PatrolMonitor.do")
	public String showPatrolMonitor(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		logger.debug("User requests to load patrol monitor page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.patrolMonitoring"));

		if (hasPrivilege) {
			model.addAttribute(ModelMappingValue.var_currentSiteKey, this
					.getRoleFromSession(request).getSiteKey());

			return ModelMappingValue.pages_patrolMenu;
		} else {
			// user does not have right to patrol monitor
			logger.debug("user does not have right to patrol monitor ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/CurrentPatrol.do")
	public String showCurrentPatrol(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {

		CurrentPatrolDisplayList list = getCurrentPatrolDisplayList(this
				.getRoleFromSession(request).getSiteKey(), -1);

		model.addAttribute(ModelMappingValue.var_currentPatrolResult,
				list.getPrJsonList());
		model.addAttribute(ModelMappingValue.var_expireTimeJson,
				listToJsonString(list.getpExpireTimeList()));

		return ModelMappingValue.pages_view_currentPatrol;
	}

	@RequestMapping(value = "UpdatePatrolResult.do")
	public String updatePatrolResult(
			@RequestParam("routeDefKey") int routeDefKey,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) throws MFMSException {

		CurrentPatrolDisplayList list = getCurrentPatrolDisplayList(this
				.getRoleFromSession(request).getSiteKey(), routeDefKey);

		model.addAttribute(ModelMappingValue.var_currentPatrolResult,
				list.getPrJsonList());
		model.addAttribute(ModelMappingValue.var_expireTimeJson,
				listToJsonString(list.getpExpireTimeList()));

		return ModelMappingValue.pages_view_updateCurrentPatrol;

	}

	private CurrentPatrolDisplayList getCurrentPatrolDisplayList(int siteKey,
			int routeDefKey) throws MFMSException {

		List<PatrolResultDisplayJSON> patrolDisplayList = new ArrayList<PatrolResultDisplayJSON>();
		List<PatrolExpireTimeJSON> expireTimeJsonList = new ArrayList<PatrolExpireTimeJSON>();

		List<Integer> distinctInProgressRouteDefList = new ArrayList<Integer>();

		System.out.println("routeDefKey : " + routeDefKey);
		if (routeDefKey > 0) {
			distinctInProgressRouteDefList.add(routeDefKey);
		} else {
			distinctInProgressRouteDefList = patrolResultManager
					.getDistinctInProgressResult(siteKey);
		}

		for (Integer dippr : distinctInProgressRouteDefList) {
			List<PatrolResult> inProgressResultList = patrolResultManager
					.getInProgressResultByRouteDefKey(dippr);

			System.out.println("inProgressResultList Size : "
					+ inProgressResultList.size());

			if (inProgressResultList.size() == 0) {
				continue;

			}

			List<RouteLocation> routeLocationList = inProgressResultList.get(0)
					.getRouteDef().getRouteLocation();

			Patrol_Route routeDef = routeDefManager.searhRoute(dippr);
			PatrolResultDisplayJSON prDisplayJson = new PatrolResultDisplayJSON();
			PatrolExpireTimeJSON expireTimeJson = new PatrolExpireTimeJSON();

			int progressCounter = 0;
			int maxSeq = 0;
			Location maxSeqLocation = null;
			Timestamp lastAttendTime = new Timestamp(0);
			Boolean overtime = false;
			int locationCounter = 0;
			for (PatrolResult ipr : inProgressResultList) {

				if (ipr.getPersonAttended() > 0) {
					progressCounter++;

					if (ipr.getTimeAttended().getTime() > lastAttendTime
							.getTime()) {
						lastAttendTime = ipr.getTimeAttended();
						prDisplayJson.setRouteName(ipr.getRouteDef().getName());
						prDisplayJson.setLocationKey(ipr
								.getCorrectLocationKey());
						prDisplayJson.setLocationCode(ipr
								.getCorrectionLocation().getCode());
						prDisplayJson.setLocationName(ipr
								.getCorrectionLocation().getName());
						prDisplayJson.setAttendTime(timeFormat.format(new Date(
								lastAttendTime.getTime())));
						if (maxSeq < ipr.getSeqNum()) {
							maxSeq = ipr.getSeqNum();
							maxSeqLocation = ipr.getCorrectionLocation();

						}

						expireTimeJson.setPatrolResultKey(ipr.getResultKey());

						Calendar expire = Calendar.getInstance();
						expire.setTime(new Date(lastAttendTime.getTime()));

						System.out.println("Corr Location Key : "
								+ ipr.getCorrectLocationKey()
								+ "||"
								+ ipr.getRouteDef()
										.getRouteLocation()
										.get(ipr.getRouteDef()
												.getRouteLocation().size() - 1)
										.getLocationKey());

						if (ipr.getCorrectLocationKey() == ipr
								.getRouteDef()
								.getRouteLocation()
								.get(ipr.getRouteDef().getRouteLocation()
										.size() - 1).getLocationKey()) {

							// ipr.getCorrectionLocation().equals(ipr.getRouteDef().getRouteLocation().get(ipr.getRouteDef().getRouteLocation().size()-1))

							int extraMinuteToExpire = Integer
									.parseInt(propertyConfigurer
											.getProperty("patrol.last.point.stay.minute"));

							expire.add(Calendar.MINUTE, extraMinuteToExpire);

							expireTimeJson.setExpTime(expireTimeFormat
									.format(expire.getTime()));

						} else {
							int extraMinuteToExpire = ipr.getRouteDef()
									.getRouteLocation()
									.get(locationCounter + 1).getMaxPtDur();
							int durUnit = ipr.getRouteDef().getRouteLocation()
									.get(locationCounter + 1).getMaxPtDurUnit();

							if (durUnit == 1) {
								extraMinuteToExpire *= 60;
							}

							expire.add(Calendar.MINUTE, extraMinuteToExpire);
							expireTimeJson.setExpTime(expireTimeFormat
									.format(expire.getTime()));

						}

					}
				}

				locationCounter++;

			}

			if (inProgressResultList != null && inProgressResultList.size() > 0) {

				expireTimeJson.setRouteDefKey(routeDef.getRouteDef()
						.getRouteDefKey());

				prDisplayJson.setRouteDefKey(routeDef.getRouteDef()
						.getRouteDefKey());
				prDisplayJson.setRouteName(routeDef.getRouteDef().getName());
				prDisplayJson.setRouteCode(routeDef.getRouteDef().getCode());

				prDisplayJson.setAccountKey(inProgressResultList.get(0)
						.getPerson().getKey());
				prDisplayJson.setAccountName(inProgressResultList.get(0)
						.getPerson().getName());
				prDisplayJson.setAccountLoginId(inProgressResultList.get(0)
						.getPerson().getLoginId());
				prDisplayJson.setAccountEmail(inProgressResultList.get(0)
						.getPerson().getEmail());
				prDisplayJson.setAccountTel(inProgressResultList.get(0)
						.getPerson().getContactCountryCode()
						+ " - "
						+ inProgressResultList.get(0).getPerson()
								.getContactNumber());

				prDisplayJson.setProgress(progressCounter + "/"
						+ inProgressResultList.size());
				prDisplayJson
						.setStatus(PatrolResultDisplayJSON.ResultStatus.Normal);

				if (!routeLocationList.isEmpty()) {
					for (int j = 0; j < routeLocationList.size(); j++) {
						RouteLocation routeLocation = routeLocationList.get(j);

						if (routeLocation.getLocationKey() == maxSeqLocation
								.getKey()) {
							prDisplayJson.setCurrentLocationSeq(routeLocation
									.getSeqNum());
						}

					}
				}

			}
			patrolDisplayList.add(prDisplayJson);
			expireTimeJsonList.add(expireTimeJson);

		}

		CurrentPatrolDisplayList rtn = new CurrentPatrolDisplayList();
		rtn.setpExpireTimeList(expireTimeJsonList);
		rtn.setPrJsonList(patrolDisplayList);

		return rtn;
	}

	@RequestMapping(value = "GetPatrolResultDetail.do")
	public String getPatrolResultDetail(
			@RequestParam("routeDefKey") int routeDefKey,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		List<PatrolResultDisplayDetailJSON> patrolResultDisplayList = new ArrayList<PatrolResultDisplayDetailJSON>();

		List<PatrolResult> inProgressResultList = patrolResultManager
				.getInProgressResultByRouteDefKey(routeDefKey);

		int currentLocation = 0;
		int currentLocationCounter = 0;
		int overTimeLocation = -1;
		long lastAttendTime = 0;

		for (PatrolResult ipr : inProgressResultList) {

			PatrolResultDisplayDetailJSON patrolResultDisplay = new PatrolResultDisplayDetailJSON();

			if (ipr.getPersonAttended() > 0) {
				patrolResultDisplay.setAccountKey(ipr.getPerson().getKey());
				patrolResultDisplay.setAccountLogin(ipr.getPerson()
						.getLoginId());
				patrolResultDisplay.setAccountName(ipr.getPerson().getName());

				patrolResultDisplay.setAttendTime(timeFormat.format(new Date(
						ipr.getTimeAttended().getTime())));

				if (ipr.getTimeAttended().getTime() > lastAttendTime) {
					lastAttendTime = ipr.getTimeAttended().getTime();
					currentLocation = currentLocationCounter;

					Calendar now = Calendar.getInstance();
					Calendar expire = Calendar.getInstance();
					expire.setTime(new Date(lastAttendTime));

					if (ipr.getCorrectionLocation().equals(
							(ipr.getRouteDef()
									.getRouteLocation()
									.get(ipr.getRouteDef().getRouteLocation()
											.size() - 1).getLocation()))) {

						// ipr.getCorrectionLocation().equals(ipr.getRouteDef().getRouteLocation().get(ipr.getRouteDef().getRouteLocation().size()-1))

						int extraMinuteToExpire = Integer
								.parseInt(propertyConfigurer
										.getProperty("patrol.last.point.stay.minute"));

						expire.add(Calendar.MINUTE, extraMinuteToExpire);

						if (now.after(expire)) {
							overTimeLocation = currentLocationCounter;
						}

					} else {
						int extraMinuteToExpire = ipr.getRouteDef()
								.getRouteLocation()
								.get(currentLocationCounter + 1).getMaxPtDur();
						int durUnit = ipr.getRouteDef().getRouteLocation()
								.get(currentLocationCounter + 1)
								.getMaxPtDurUnit();

						if (durUnit == 1) {
							extraMinuteToExpire *= 60;
						}

						expire.add(Calendar.MINUTE, extraMinuteToExpire);
						expire.add(
								Calendar.MINUTE,
								Integer.parseInt(propertyConfigurer
										.getProperty("patrol.web.overtime.delay.minute")));
						if (now.after(expire)) {
							overTimeLocation = currentLocationCounter;
						}

					}

				}

			}

			patrolResultDisplay.setPatrolResultKey(ipr.getResultKey());
			patrolResultDisplay.setLocationKey(ipr.getCorrectLocationKey());
			patrolResultDisplay.setLocationCode(ipr.getCorrectionLocation()
					.getCode());
			patrolResultDisplay.setLocationName(ipr.getCorrectionLocation()
					.getName());
			patrolResultDisplay.setPhoto("");
			patrolResultDisplay.setRemark(ipr.getReason());
			patrolResultDisplay.setRouteDefKey(ipr.getRouteDefKey());
			patrolResultDisplay.setRouteCode(ipr.getRouteDef().getCode());
			patrolResultDisplay.setRouteName(ipr.getRouteDef().getName());
			// System.out.println("ipr.getRouteDef().getName() : "+ipr.getRouteDef().getName());
			patrolResultDisplay.setSequence(ipr.getSeqNum());
			patrolResultDisplay
					.setStatus(PatrolResultDisplayDetailJSON.ResultStatus.None);

			currentLocationCounter++;
			patrolResultDisplayList.add(patrolResultDisplay);
		}

		System.out.println("Current Location : " + currentLocation);
		if (currentLocation == overTimeLocation) {
			patrolResultDisplayList.get(overTimeLocation).setStatus(
					PatrolResultDisplayDetailJSON.ResultStatus.Overtime);
		}
		if (patrolResultDisplayList.get(currentLocation).getStatus()
				.equals(PatrolResultDisplayDetailJSON.ResultStatus.None)) {
			patrolResultDisplayList.get(currentLocation).setStatus(
					PatrolResultDisplayDetailJSON.ResultStatus.Normal);
		}

		model.addAttribute(ModelMappingValue.var_currentPatrolResultDetail,
				patrolResultDisplayList);

		return ModelMappingValue.pages_view_currentPatrolResult;
	}

	@RequestMapping(value = "/UpdateCurrentPatrolResult.do")
	public String updatePatrolResultDetail(
			@RequestParam("patrolResultKey") int patrolResultKey,
			@RequestParam("index") int index, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		List<PatrolResultDisplayDetailJSON> patrolResultDisplayList = new ArrayList<PatrolResultDisplayDetailJSON>();

		PatrolResult ipr = patrolResultManager
				.getPatrolResultByKey(patrolResultKey);

		PatrolResultDisplayDetailJSON patrolResultDisplay = new PatrolResultDisplayDetailJSON();

		if (ipr.getPersonAttended() > 0) {
			patrolResultDisplay.setAccountKey(ipr.getPerson().getKey());
			patrolResultDisplay.setAccountLogin(ipr.getPerson().getLoginId());
			patrolResultDisplay.setAccountName(ipr.getPerson().getName());

			patrolResultDisplay.setAttendTime(timeFormat.format(new Date(ipr
					.getTimeAttended().getTime())));
		}

		patrolResultDisplay.setPatrolResultKey(ipr.getResultKey());
		patrolResultDisplay.setLocationKey(ipr.getCorrectLocationKey());
		patrolResultDisplay.setLocationCode(ipr.getCorrectionLocation()
				.getCode());
		patrolResultDisplay.setLocationName(ipr.getCorrectionLocation()
				.getName());
		patrolResultDisplay.setPhoto("");
		patrolResultDisplay.setRemark(ipr.getReason());
		patrolResultDisplay.setRouteDefKey(ipr.getRouteDefKey());
		patrolResultDisplay.setRouteCode(ipr.getRouteDef().getCode());
		patrolResultDisplay.setRouteName(ipr.getRouteDef().getName());
		patrolResultDisplay.setSequence(ipr.getSeqNum());
		patrolResultDisplay
				.setStatus(PatrolResultDisplayDetailJSON.ResultStatus.Normal);

		patrolResultDisplayList.add(patrolResultDisplay);

		model.addAttribute(ModelMappingValue.var_currentSeq, index);
		model.addAttribute(ModelMappingValue.var_currentPatrolResultDetail,
				patrolResultDisplayList);

		return ModelMappingValue.pages_view_updatePatrolResultResult;
	}

	@RequestMapping(value = "/AddAlert.do", method = RequestMethod.GET)
	public String addAlert(@RequestParam("scheduleKey") int scheduleKey,
			@RequestParam("routeDefKey") int routeDefKey,
			@RequestParam("routeResultKey") int routeResultKey,
			@RequestParam("accountKey") int accountKey,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException {

		String name = "";
		String msg = "";
		String alertId = "";
		String closeAlertId = "";
		System.out.println("Not Start Schedule Key : " + scheduleKey);
		if (scheduleKey > 0) {

			if (propertyConfigurer.getProperty("patrol.monitor.notStartAlert")
					.equals("false")) {
				return null;
			}

			List<PatrolScheduleAccount> scheduleAccountList = patrolScheduleManager
					.searchPatrolScheduleAccountByScheduleKey(scheduleKey);

			name = scheduleAccountList.get(0).getPatrolSchedule().getRouteDef()
					.getName()
					+ " ("
					+ onlyTimeFormat.format(new Date(scheduleAccountList.get(0)
							.getPatrolSchedule().getScheduleTime().getTime()))
					+ ") ";
			msg = messageSource.getMessage("patrol.monitor.alert.notYetStart",
					null, locale);
			alertId = "schedule_alert_" + scheduleKey;

			// String templateId = "expiredPatrolSchedule";

			// Site site =
			// siteManager.getSiteByKey(scheduleAccountList.get(0).getPatrolSchedule().getSiteKey());

			// Object[] param = new Object[] {
			// scheduleAccountList.get(0).getPatrolSchedule().getScheduleTime(),
			// scheduleAccountList.get(0).getPatrolSchedule().getRouteDef().getName(),
			// site.getName() };
			//
			// List<String> emailList = new ArrayList<String>();
			// for (PatrolScheduleAccount acList : scheduleAccountList) {
			// emailList.add(acList.getUserAccount().getEmail());
			//
			// }
			//
			// try {
			// emailManager.sendTemplateAsync(templateId,
			// this.getRoleFromSession(request).getSiteKey(),
			// emailList.toArray(new String[emailList.size()]), param);
			// } catch (MFMSException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }

		} else {

			if (routeDefKey > 0) {

				if (propertyConfigurer.getProperty(
						"patrol.monitor.overTimeAlert").equals("false")) {
					return null;
				}

				try {
					Patrol_Route route = routeDefManager
							.searhRoute(routeDefKey);

					PatrolResult pr = patrolResultManager
							.getPatrolResultByKey(routeResultKey);
					pr.getCorrectionLocation().getName();

					name = route.getRouteDef().getName() + " ("
							+ pr.getCorrectionLocation().getCode() + " - "
							+ pr.getCorrectionLocation().getName() + ")";

					msg = messageSource.getMessage(
							"patrol.monitor.alert.overtime", null, locale);

					alertId = "schedule_routeDef_" + routeDefKey + "_"
							+ routeResultKey;
					closeAlertId = "cur_result_" + routeResultKey;
				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		model.addAttribute(ModelMappingValue.var_alert_id, alertId);
		model.addAttribute(ModelMappingValue.var_closeAlertId, closeAlertId);
		model.addAttribute(ModelMappingValue.var_alert_routeName, name);
		model.addAttribute(ModelMappingValue.var_alert_msg, msg);

		return ModelMappingValue.pages_view_alert;
	}

	@RequestMapping(value = "/FuturePatrol.do")
	public String showFuturePatrol(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		List<PatrolResultDisplayJSON> patrolDisplayList = new ArrayList<PatrolResultDisplayJSON>();

		// int hourBefore = Integer.parseInt(propertyConfigurer
		// .getProperty("patrol.monitor.hour.before"));
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		Calendar current = Calendar.getInstance();
		//
		Calendar timeBeforeToSee = Calendar.getInstance();
		// timeBeforeToSee.add(Calendar.HOUR_OF_DAY, hourBefore);
		timeBeforeToSee.set(Calendar.HOUR_OF_DAY, 23);
		timeBeforeToSee.set(Calendar.MINUTE, 59);
		timeBeforeToSee.set(Calendar.SECOND, 59);
		timeBeforeToSee.set(Calendar.MILLISECOND, 999);

		List<PatrolSchedule> list = calendarEvent.getEventByDateRange(this
				.getRoleFromSession(request).getSiteKey(), current, current);
		List<PatrolExpireTimeJSON> expireTimeList = new ArrayList<PatrolExpireTimeJSON>();

		for (PatrolSchedule tmp : list) {

			Boolean isToday = calendarEvent.isTheDayEvent(today,
					timeBeforeToSee, tmp.getFrequency(),
					tmp.getScheduleStartDate(), tmp.getScheduleEndDate(),
					tmp.getScheduleTime());

			// System.out.println("||tmp : "+tmp.getScheduleKey());

			if (isToday) {

				PatrolResultDisplayJSON pr = new PatrolResultDisplayJSON();

				// pr.setAccountKey();
				// pr.setAccountEmail();
				// pr.setAccountLoginId();
				// pr.setAccountName();
				// pr.setAccountTel();

				Calendar time = Calendar.getInstance();
				time.setTime(new Date(tmp.getScheduleTime().getTime()));

				current.set(Calendar.HOUR_OF_DAY,
						time.get(Calendar.HOUR_OF_DAY));
				current.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

				if (tmp.getLastAttendTime() != null) {
					if (tmp.getLastAttendTime().getTime() > today
							.getTimeInMillis()) {
						pr.setScheduleStatus(ScheduleStatus.Finished);

						List<PatrolResult> resultList = patrolResultManager
								.getInProgressResultByScheduleKey(tmp
										.getScheduleKey());

						if (resultList != null && resultList.size() > 0) {
							pr.setScheduleStatus(ScheduleStatus.InProgress);
						}

					}
				}

				if (pr.getScheduleStatus() == null) {
					PatrolExpireTimeJSON expireTime = new PatrolExpireTimeJSON();
					expireTime.setScheduleKey(tmp.getScheduleKey());
					expireTime.setExpTime(expireTimeFormat.format(current
							.getTime()));
					expireTimeList.add(expireTime);
				}
				pr.setAttendTime(onlyTimeFormat.format(new Date(tmp
						.getScheduleTime().getTime())));

				pr.setRouteCode(tmp.getRouteDef().getCode());
				pr.setRouteDefKey(tmp.getRouteDefKey());
				pr.setRouteName(tmp.getRouteDef().getName());
				pr.setScheduleKey(tmp.getScheduleKey());

				List<PatrolScheduleAccount> accountList = tmp
						.getScheduleAccount();
				String accountName = "";

				System.out.println("Account List size : " + accountList.size());

				ArrayList<String> accountNameList = new ArrayList<String>();
				
				for (PatrolScheduleAccount account : accountList) {
					if(account.getDeleted().equals("Y")){
						continue;
					}
					
					accountNameList.add(account.getUserAccount().getName());
					/*
					if (accountName.equals("")) {
						accountName += account.getUserAccount().getName();
					} else {
						accountName += ", "
								+ account.getUserAccount().getName();
					}
					*/
				}

				Collections.sort(accountNameList);

				for (String account : accountNameList) {
					accountName += !accountName.equals("") ? ", " + account : account;
				}
				
				pr.setAccountName(accountName);

				patrolDisplayList.add(pr);

			}
		}

		Collections.sort(patrolDisplayList, new Comparator<PatrolResultDisplayJSON>() {
			@Override
			public int compare(PatrolResultDisplayJSON o1, PatrolResultDisplayJSON o2) {
				String o1Route = o1.getRouteCode() == null ? "" : o1.getRouteCode();
				String o2Route = o2.getRouteCode() == null ? "" : o2.getRouteCode();
				return o1Route.compareTo(o2Route);
			}
		});
		
		System.out.println("Future List : " + patrolDisplayList.size());
		model.addAttribute(ModelMappingValue.var_futurePatrol,
				patrolDisplayList);
		model.addAttribute(ModelMappingValue.var_futureTimeJson,
				this.listToJsonString(expireTimeList));

		return ModelMappingValue.pages_view_futurePatrol;
	}

	// Other functions

	public RouteDefManager getRouteDefManager() {
		return routeDefManager;
	}

	public void setRouteDefManager(RouteDefManager routeDefManager) {
		this.routeDefManager = routeDefManager;
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public Properties getPropertyConfigurer() {
		return propertyConfigurer;
	}

	public void setPropertyConfigurer(Properties propertyConfigurer) {
		this.propertyConfigurer = propertyConfigurer;
	}

	public PatrolResultManager getPatrolResultManager() {
		return patrolResultManager;
	}

	public void setPatrolResultManager(PatrolResultManager patrolResultManager) {
		this.patrolResultManager = patrolResultManager;
	}

	private Role getRoleFromSession(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Role role = (Role) session.getAttribute("currRole");

		return role;

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

	class CurrentPatrolDisplayList {

		private List<PatrolResultDisplayJSON> prJsonList;
		private List<PatrolExpireTimeJSON> pExpireTimeList;

		public List<PatrolResultDisplayJSON> getPrJsonList() {
			return prJsonList;
		}

		public void setPrJsonList(List<PatrolResultDisplayJSON> prJsonList) {
			this.prJsonList = prJsonList;
		}

		public List<PatrolExpireTimeJSON> getpExpireTimeList() {
			return pExpireTimeList;
		}

		public void setpExpireTimeList(
				List<PatrolExpireTimeJSON> pExpireTimeList) {
			this.pExpireTimeList = pExpireTimeList;
		}

	}
}
