package hk.ebsl.mfms.web.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.PatrolScheduleAccount;
import hk.ebsl.mfms.dto.PatrolScheduleActionLog;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.CalendarJSON;
import hk.ebsl.mfms.json.CalendarJSON.ExcludeDays;
import hk.ebsl.mfms.json.CalendarJSON.Frequency;
import hk.ebsl.mfms.json.RouteNameSearchJSON;
import hk.ebsl.mfms.json.RouteScheduleJSON;
import hk.ebsl.mfms.manager.PatrolScheduleActionLogManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.RouteDefActionLogManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.Patrol_Route;
import hk.ebsl.mfms.utility.CalendarEvent;
import hk.ebsl.mfms.web.form.PatrolAssignForm;
import hk.ebsl.mfms.web.form.validator.PatrolAssignFormValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PatrolAssignRouteController {

	static class ModelMappingValue {
		public static final String pages_patrolAssign = "patrol/assignSchedule/assignSchedule";
		public static final String pages_view_assign_selectRoute = "view_assign_selectRoute";
		public static final String pages_view_assign_selectStaff = "view_assign_selectStaff";
		public static final String pages_view_assign_route_searched = "view_assign_route_searched";
		public static final String pages_view_assign_route_selected = "view_assign_route_selected";
		public static final String pages_view_calendar = "view_calendar";

		public static final String pages_view_assign_account_searched = "view_assign_account_searched";
		public static final String pages_view_assign_account_selected = "view_assign_account_selected";
		public static final String pages_view_assign_schedule_form = "view_assign_schedule_form";
		public static final String pages_view_calendar_eventModal = "view_calendar_eventModal";

		public static final String var_route = "route";
		public static final String var_desc = "desc";
		public static final String var_staff = "staff";
		// public static final String var_eventStaff = "eventStaff";
		public static final String var_searchedRoute = "searchedRoute";
		public static final String var_selectedRoute = "selectedRoute";
		public static final String var_accountRole = "accountRole";
		public static final String var_searchedStaff = "searchedStaff";
		public static final String var_selectedStaff = "selectedStaff";
		public static final String var_startDateCounter = "schedulerCounter";
		public static final String var_frequency = "frequency";
		public static final String var_weekDay = "weekDay";
		public static final String var_weeklyValue = "weeklyValue";
		public static final String var_onceValue = "onceValue";

		public static final String var_htmlId = "htmlId";
		public static final String var_selectedRouteJson = "selectedRouteJson";
		public static final String var_selectedStaffJson = "selectedStaffJson";
		public static final String var_eventEditStaffJson = "eventEditStaffJson";
		public static final String var_currentEventEditStaffJson = "currentEventEditStaffJson";
		public static final String var_parentEventEditStaffJson = "parentEventEditStaffJson";
		public static final String var_eventOrginalStaffJson = "var_eventOrginalStaffJson";
		public static final String var_calendar_locale = "calendarLocale";

		public static final String form_patrolAssignForm = "patrolAssignForm";
		public static final String form_patrolAssignEditForm = "patrolAssignEditForm";

		// Defect Calendar
		public static final String pages_view_defectCalendar = "view_defectCalendar";
		public static final String pages_view_defectCalendar_eventModal = "view_defectCalendar_eventModal";
		public static final String pages_view_assign_defectCalendar_form = "view_assign_defectCalendar_form";
		public static final String pages_view_assign_selectLocation = "view_assign_selectLocation";
		public static final String var_startingLocation = "startingLocation";
		public static final String pages_view_showDefectCalendarStartingLocation = "view_defectCalendarStartingLocation";

	}

	public final static Logger logger = Logger.getLogger(PatrolAssignRouteController.class);

	@Autowired
	private Properties privilegeMap;
	@Autowired
	private RouteDefManager routeDefManager;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private PatrolScheduleManager patrolScheduleManager;
	@Autowired
	private PatrolAssignFormValidator patrolAssignFormValidator;
	@Autowired
	private PatrolScheduleActionLogManager patrolScheduleActionLogManager;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CalendarEvent calendarEvent;

	// List<RouteDef> selectedRoute = null;
	// List<UserAccount> selectedStaff = null;
	// List<UserAccount> edit_eventStaff = null;

	// int schedulerCounter = 1;
	int weeklyValue = Frequency.Weekly.getValue();
	int onceValue = Frequency.Once.getValue();

	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	// private PatrolAssignForm formBeforeEdit = null;

	/**                                   **/
	/** Functions for Assign Patrol Route **/
	/**                                   **/

	@RequestMapping(value = "/PatrolAssign.do")
	public String PatrolAssign(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load patrol schedule page.");

		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchPatrolSchedule"))
				|| currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.createPatrolSchedule"))
				|| currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.removePatrolSchedule"))
				|| currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.modifyPatrolSchedule"));

		if (hasPrivilege) {
			PatrolAssignForm form = new PatrolAssignForm();

			List<Role> roleList = getAllAccountRole(this.getRoleFromSession(request).getSiteKey());
			Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
			roleMap.put(-1, "");
			if (roleList != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role role = roleList.get(i);
					roleMap.put(role.getKey(), role.getName());
				}
			}

			model.addAttribute(ModelMappingValue.form_patrolAssignForm, form);
			model.addAttribute(PatrolController.ModelMappingValue.var_mode,
					PatrolController.ModelMappingValue.mode_search);
			model.addAttribute(ModelMappingValue.var_accountRole, roleMap);

			return ModelMappingValue.pages_patrolAssign;
		} else {
			// user does not have right to patrol schedule
			logger.debug("user does not have right to patrol schedule ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/PatrolAssignSelectRoute.do")
	public String PatrolAssignSelectRoute(@RequestParam("selectedRouteJson") String jsonString,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		logger.debug("/PatrolAssignSelectRoute.do");

		Map<Integer, String> route = new LinkedHashMap<Integer, String>();

		if (!jsonString.equals("")) {
			List<RouteDef> selectedRoute = this.jsonStringToList(jsonString, RouteDef.class);

			if (selectedRoute != null) {
				for (int i = 0; i < selectedRoute.size(); i++) {
					RouteDef routeDef = selectedRoute.get(i);

					route.put(routeDef.getRouteDefKey(), routeDef.getCode() + " - " + routeDef.getName());

				}

			}

		}
		model.addAttribute(ModelMappingValue.var_route, route);

		return ModelMappingValue.pages_view_assign_selectRoute;
	}

	@RequestMapping(value = "InitPatrolAssignSelectStaff.do")
	public String InitPatrolAssignSelectStaff(@RequestParam("edit") Boolean edit,
			@RequestParam("routeDefKey") HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("/InitPatrolAssignSelectStaff.do");

		Map<Integer, String> staff = new LinkedHashMap<Integer, String>();
		String htmlId = "";
		if (edit) {
			htmlId = "_modal";
		} else {

		}
		model.addAttribute(ModelMappingValue.var_htmlId, htmlId);
		model.addAttribute(ModelMappingValue.var_staff, staff);

		return ModelMappingValue.pages_view_assign_selectStaff;
	}

	@RequestMapping(value = "/PatrolAssignSelectStaff.do")
	public String PatrolAssignSelectStaff(@RequestParam("edit") Boolean edit,
			@RequestParam("selectedStaffJson") String jsonString,
			@RequestParam("eventEditStaffJson") String eventString, @RequestParam("extraId") String extraId,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("/PatrolAssignSelectStaff.do");
		logger.debug("selectedStaffJson : " + jsonString);
		logger.debug("eventEditStaffJson : " + eventString);
		Map<Integer, String> staff = new LinkedHashMap<Integer, String>();
		String htmlId = "";
		if (edit) {

			List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();

			if (!eventString.equals(""))
				edit_eventStaff = this.jsonStringToList(eventString, UserAccount.class);

			for (int i = 0; i < edit_eventStaff.size(); i++) {
				UserAccount account = edit_eventStaff.get(i);
				staff.put(account.getKey(), account.getLoginId() + " - " + account.getName());
			}

			htmlId = "_modal" + extraId;
		} else {

			System.out.println(jsonString);

			if (!jsonString.equals("")) {

				// List<UserAccount> selectedStaff =
				// this.jsonStringToSelectedStaffList(jsonString);
				List<UserAccount> selectedStaff = jsonStringToList(jsonString, UserAccount.class);

				if (selectedStaff != null) {
					for (int i = 0; i < selectedStaff.size(); i++) {
						UserAccount account = selectedStaff.get(i);
						staff.put(account.getKey(), account.getLoginId() + " - " + account.getName());
					}
				}
			}

		}
		
		
		model.addAttribute(ModelMappingValue.var_htmlId, htmlId);
		model.addAttribute(ModelMappingValue.var_staff, staff);

		return ModelMappingValue.pages_view_assign_selectStaff;
	}

	@RequestMapping(value = "/PatrolAssignCalendar.do")
	public String PatrolAssignCalendar(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("/PatrolAssignCalendar.do");

		return ModelMappingValue.pages_view_calendar;
	}

	@RequestMapping(value = "/AddScheduleForm.do")
	public String addScheduleForm(@RequestParam("schedulerCounter") int schedulerCounter, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("/AddScheduleForm.do");

		schedulerCounter++;

		model.addAttribute(ModelMappingValue.var_weeklyValue, weeklyValue);
		model.addAttribute(ModelMappingValue.var_onceValue, onceValue);
		model.addAttribute(ModelMappingValue.var_weekDay, getWeekDayMap(locale));
		model.addAttribute(ModelMappingValue.var_frequency, getFrequencyMap(locale));
		model.addAttribute(ModelMappingValue.var_startDateCounter, schedulerCounter);
		return ModelMappingValue.pages_view_assign_schedule_form;
	}

	@RequestMapping(value = "/SubmitRouteSchedule.do")
	public @ResponseBody String submitRouteSchedule(@RequestBody RouteScheduleJSON json, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {

		logger.debug("/SubmitRouteSchedule.do");

		List<Integer> routeDefKeyList = json.getRouteDefKey();
		List<Integer> accountKeyList = json.getAccountKey();
		List<String> startDateTimeList = json.getStartDate();
		List<String> endDateTimeList = json.getEndDate();
		List<String> timeList = json.getTime();
		List<Integer> frequencyList = json.getFrequency();
		List<List<Integer>> weekDayList = json.getWeekDay();

		Role role = getRoleFromSession(request);

		try {
			List<PatrolSchedule> scheduleList = new ArrayList<PatrolSchedule>();
			for (int i = 0; i < routeDefKeyList.size(); i++) {

				for (int j = 0; j < timeList.size(); j++) {

					PatrolSchedule schedule = new PatrolSchedule();

					schedule.setScheduleStartDate(stringToTimestamp(startDateTimeList.get(j), dateFormat));

					if (endDateTimeList.get(j).equals("")) {
						schedule.setScheduleEndDate(null);
					} else {
						schedule.setScheduleEndDate(stringToTimestamp(endDateTimeList.get(j), dateFormat));
					}
					schedule.setScheduleTime(stringToTimestamp(timeList.get(j), timeFormat));
					schedule.setSiteKey(role.getSiteKey());
					schedule.setRouteDefKey(routeDefKeyList.get(i));
					schedule.setFrequency(CalendarJSON.encodeFrequency(frequencyList.get(j), weekDayList.get(j)));
					if (frequencyList.get(j).equals(Frequency.Once.getValue())) {
						schedule.setScheduleEndDate(stringToTimestamp(startDateTimeList.get(j), dateFormat));
					}

					schedule.setParentId(0);
					schedule.setCreateBy(role.getKey());
					schedule.setCreateDateTime(getCurrentTime());
					schedule.setLastModifyBy(role.getKey());
					schedule.setLastModifyDateTime(getCurrentTime());
					schedule.setDeleted("N");
					schedule.setSkipped("N");
					scheduleList.add(schedule);
				}
			}

			List<PatrolScheduleAccount> scheduleAccountList = new ArrayList<PatrolScheduleAccount>();
			for (int i = 0; i < accountKeyList.size(); i++) {
				PatrolScheduleAccount scheduleAccount = new PatrolScheduleAccount();
				scheduleAccount.setAccountKey(accountKeyList.get(i));
				scheduleAccount.setCreateBy(role.getKey());
				scheduleAccount.setCreateDateTime(getCurrentTime());
				scheduleAccount.setLastModifyBy(role.getKey());
				scheduleAccount.setLastModifyTime(getCurrentTime());
				scheduleAccount.setDeleted("N");

				scheduleAccountList.add(scheduleAccount);
			}

			patrolScheduleManager.savePatrolSchedule(scheduleList, scheduleAccountList);

			// Patrol Schedule Action Log
			Role currRole = (Role) request.getSession().getAttribute("currRole");
			UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
			PatrolScheduleActionLog patrolScheduleActionLog = new PatrolScheduleActionLog();
			patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
			patrolScheduleActionLog.setActionType("Submit Route Schedule");
			patrolScheduleActionLog.setActionBy(actionBy.getKey());
			patrolScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));

			patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);

			// End Patrol Schedule Action Log

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "error";
		}

		return "success";
	}

	/**                                  **/
	/** Functions for Modal Select Route **/
	/**                                  **/
	@RequestMapping(value = "/ShowRouteNameSearch.do")
	public String showRouteNameSearch(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("/ShowRouteNameSearch.do");

		List<RouteDef> routeDefList = new ArrayList<RouteDef>();

		List<Patrol_Route> list = routeDefManager.searchRoute(this.getRoleFromSession(request).getSiteKey(), null, null,
				null);

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				routeDefList.add(list.get(i).getRouteDef());
			}
		}

		model.addAttribute(ModelMappingValue.var_searchedRoute, routeDefList);
		return ModelMappingValue.pages_view_assign_route_searched;
	}

	@RequestMapping(value = "/ShowRouteNameSelected.do")
	public String showRouteNameSelect(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("/ShowRouteNameSelected.do");

		List<RouteDef> selectedRoute = new ArrayList<RouteDef>();

		model.addAttribute(ModelMappingValue.var_searchedRoute, selectedRoute);
		return ModelMappingValue.pages_view_assign_route_selected;
	}

	@RequestMapping(value = "/SubmitRouteNameSearch.do")
	public String submitRouteNameSearch(@RequestBody RouteNameSearchJSON json, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/SubmitRouteNameSearch.do");

		List<RouteDef> routeDefList = new ArrayList<RouteDef>();
		List<Patrol_Route> list = routeDefManager.searchRoute(this.getRoleFromSession(request).getSiteKey(),
				json.getRouteName(), json.getRouteCode(), json.getLocationKey());

		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				routeDefList.add(list.get(i).getRouteDef());
			}
		}

		model.addAttribute(ModelMappingValue.var_searchedRoute, routeDefList);
		return ModelMappingValue.pages_view_assign_route_searched;
	}

	@RequestMapping(value = "/AddSelectedRoute.do")
	public String addSelectedRoute(@RequestParam("routeDefKey") Integer routeDefKey,
			@RequestParam("selectedRouteJson") String jsonString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		System.out.println("IN addSelectedRoute");

		logger.debug("/AddSelectedRoute.do");

		List<RouteDef> selectedRoute = new ArrayList<RouteDef>();

		if (!jsonString.equals(""))
			selectedRoute = this.jsonStringToList(jsonString, RouteDef.class);

		try {
			Patrol_Route patrolRoute = routeDefManager.searhRoute(routeDefKey);

			if (patrolRoute != null) {
				Boolean duplicated = false;
				for (int i = 0; i < selectedRoute.size(); i++) {
					if (selectedRoute.get(i).getRouteDefKey() == patrolRoute.getRouteDef().getRouteDefKey()) {
						duplicated = true;
						break;
					}

				}
				if (!duplicated) {
					selectedRoute.add(patrolRoute.getRouteDef());
				}
			}

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model.addAttribute(ModelMappingValue.var_selectedRoute, selectedRoute);
		model.addAttribute(ModelMappingValue.var_selectedRouteJson, this.listToJsonString(selectedRoute));

		return ModelMappingValue.pages_view_assign_route_selected;
	}

	@RequestMapping(value = "/AddAllSelectedRoute.do")
	public String addAllSelectedRoute(@RequestParam("RouteKeys") int[] keys,
			@RequestParam("selectedRouteJson") String jsonString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {

		logger.debug("/AddAllSelectedRoute.do");

		List<RouteDef> selectedRoute = new ArrayList<RouteDef>();

		if (!jsonString.equals(""))
			selectedRoute = this.jsonStringToList(jsonString, RouteDef.class);

		selectedRoute = routeDefManager.getRouteDef(keys);

		model.addAttribute(ModelMappingValue.var_selectedRoute, selectedRoute);
		model.addAttribute(ModelMappingValue.var_selectedRouteJson, this.listToJsonString(selectedRoute));

		return ModelMappingValue.pages_view_assign_route_selected;
	}

	@RequestMapping(value = "/DropSelectedRoute.do")
	public String dropSelectedRoute(@RequestParam("routeDefKey") Integer routeDefKey,
			@RequestParam("selectedRouteJson") String jsonString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/DropSelectedRoute.do");

		List<RouteDef> selectedRoute = this.jsonStringToList(jsonString, RouteDef.class);

		for (int i = 0; i < selectedRoute.size(); i++) {
			if (routeDefKey == selectedRoute.get(i).getRouteDefKey()) {
				selectedRoute.remove(i);
				break;
			}
		}

		model.addAttribute(ModelMappingValue.var_selectedRoute, selectedRoute);
		model.addAttribute(ModelMappingValue.var_selectedRouteJson, this.listToJsonString(selectedRoute));

		return ModelMappingValue.pages_view_assign_route_selected;

	}

	@RequestMapping(value = "/DropAllSelectedRoute.do")
	public String dropAllSelectedRoute(@RequestParam("RouteKeys") Integer RouteKeys,
			@RequestParam("selectedRouteJson") String jsonString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/DropAllSelectedRoute.do");

		List<RouteDef> selectedRoute = this.jsonStringToList(jsonString, RouteDef.class);

		selectedRoute.clear();

		model.addAttribute(ModelMappingValue.var_selectedRoute, selectedRoute);
		model.addAttribute(ModelMappingValue.var_selectedRouteJson, this.listToJsonString(selectedRoute));

		return ModelMappingValue.pages_view_assign_route_selected;

	}

	/**                                  **/
	/** Functions for Modal Select Staff **/
	/**                                  **/

	@RequestMapping(value = "/ShowStaffSearched.do", method = RequestMethod.GET)
	public String showStaffSearched(@RequestParam("privilege") String privilege, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/ShowStaffSearched.do");

		List<UserAccount> accountList = getAccountBy(this.getRoleFromSession(request).getSiteKey(), "", -1, privilege);
		List<String> sortedAccountName = new ArrayList<String>();
		List<UserAccount> sortedAccountList = new ArrayList<UserAccount>();
		Map<String, UserAccount> map = new HashMap<String, UserAccount>();

		for (UserAccount ua : accountList) {

			// logger.debug("ShowStaffSearched.do ||||| Original AccountList: "
			// + ua.getLoginId());
			map.put(ua.getLoginId(), ua);
			sortedAccountName.add(ua.getLoginId());
		}

		Collections.sort(sortedAccountName, new StringComparator());
		for (String s : sortedAccountName) {
			sortedAccountList.add(map.get(s));
		}

		model.addAttribute(ModelMappingValue.var_searchedStaff, sortedAccountList);
		return ModelMappingValue.pages_view_assign_account_searched;
	}

	@RequestMapping(value = "/ShowStaffSelected.do")
	public String showStaffSelected(@RequestParam("edit") Boolean edit,
			@RequestParam("selectedStaffJson") String jsonString,
			@RequestParam("eventEditStaffJson") String eventString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/ShowStaffSelected.do");

		System.out.println("ShowStaffSelected Edit :" + edit);
		System.out.println("B4 Event : " + eventString + " || Staff : " + jsonString);

		if (edit) {

			List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();

			if (!eventString.equals("")) {
				edit_eventStaff = this.jsonStringToList(eventString, UserAccount.class);
			}

			model.addAttribute(ModelMappingValue.var_selectedStaff, edit_eventStaff);
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, this.listToJsonString(edit_eventStaff));
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, jsonString);
			System.out.println("AF Event : " + this.listToJsonString(edit_eventStaff) + " || Staff : " + jsonString);
		} else {

			List<UserAccount> selectedStaff = new ArrayList<UserAccount>();

			if (!jsonString.equals("")) {

				selectedStaff = this.jsonStringToList(jsonString, UserAccount.class);
			}

			model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, this.listToJsonString(selectedStaff));
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, eventString);
		}

		return ModelMappingValue.pages_view_assign_account_selected;
	}

	@RequestMapping(value = "/AddSelectedStaff.do")
	public String addSelectedStaff(@RequestParam("edit") Boolean edit, @RequestParam("accountKey") Integer accountKey,
			@RequestParam("selectedStaffJson") String jsonString,
			@RequestParam("eventEditStaffJson") String eventString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/AddSelectedStaff.do");

		UserAccount account = null;
		try {
			account = userAccountManager.getUserAccountByAccountKey(accountKey);
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (account != null) {

			if (edit) {
				Boolean duplicated = false;

				List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();
				if (!eventString.equals(""))
					edit_eventStaff = this.jsonStringToList(eventString, UserAccount.class);

				for (int i = 0; i < edit_eventStaff.size(); i++) {

					System.out.println("AddSelectedStaff.do : edit_eventStaff Key : " + edit_eventStaff.get(i).getKey()
							+ "||" + accountKey);

					if (edit_eventStaff.get(i).getKey().equals(accountKey)) {
						duplicated = true;
						break;
					}
				}
				if (!duplicated) {
					edit_eventStaff.add(account);
				}

				model.addAttribute(ModelMappingValue.var_eventEditStaffJson, this.listToJsonString(edit_eventStaff));
				model.addAttribute(ModelMappingValue.var_selectedStaffJson, jsonString);
				model.addAttribute(ModelMappingValue.var_selectedStaff, edit_eventStaff);

			} else {
				Boolean duplicated = false;

				List<UserAccount> selectedStaff = new ArrayList<UserAccount>();
				if (!jsonString.equals("")) {
					selectedStaff = this.jsonStringToList(jsonString, UserAccount.class);
				}
				for (int i = 0; i < selectedStaff.size(); i++) {
					if (selectedStaff.get(i).getKey().equals(accountKey)) {
						duplicated = true;
						break;
					}
				}
				if (!duplicated) {
					selectedStaff.add(account);
				}

				model.addAttribute(ModelMappingValue.var_selectedStaffJson, this.listToJsonString(selectedStaff));
				model.addAttribute(ModelMappingValue.var_eventEditStaffJson, eventString);
				model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			}
		}

		return ModelMappingValue.pages_view_assign_account_selected;

	}

	@RequestMapping(value = "/AddAllSelectedStaff.do")
	public String addAllSelectedStaff(@RequestParam("edit") Boolean edit,
			@RequestParam("accountKeys") int[] accountKeys, @RequestParam("selectedStaffJson") String jsonString,
			@RequestParam("eventEditStaffJson") String eventString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/AddAllSelectedStaff.do");

		if (!edit) {
			List<UserAccount> selectedStaff = new ArrayList<UserAccount>();
			// UserAccount account = null;
			for (Integer keys : accountKeys) {
				try {
					UserAccount account = new UserAccount();
					account = userAccountManager.getUserAccountByAccountKey(keys);

					selectedStaff.add(account);

				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, this.listToJsonString(selectedStaff));
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, eventString);
			model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			logger.debug("selectedStaff Size : " + selectedStaff.size());

		} else {

			List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();
			if (!eventString.equals(""))
				edit_eventStaff = this.jsonStringToList(jsonString, UserAccount.class);
			for (Integer keys : accountKeys) {
				try {
					// Boolean duplicated = false;
					UserAccount account = new UserAccount();
					account = userAccountManager.getUserAccountByAccountKey(keys);
					// for(int i=0; i<edit_eventStaff.size();i++){
					// if (edit_eventStaff.get(i).getKey().equals(keys)) {
					// duplicated = true;
					// break;
					// }
					// }
					// if(!duplicated){
					edit_eventStaff.add(account);
					// }

				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, this.listToJsonString(edit_eventStaff));
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, jsonString);
			model.addAttribute(ModelMappingValue.var_selectedStaff, edit_eventStaff);

		}
		return ModelMappingValue.pages_view_assign_account_selected;
	}

	@RequestMapping(value = "/DropSelectedStaff.do")
	public String dropSelectedStaff(@RequestParam("edit") Boolean edit, @RequestParam("accountKey") Integer accountKey,
			@RequestParam("selectedStaffJson") String jsonString,
			@RequestParam("eventEditStaffJson") String eventString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/DropSelectedStaff.do");
		logger.debug("DropSelectedStaff.do - edit : " + edit);
		logger.debug("DropSelectedStaff.do - jsonString : " + jsonString);
		logger.debug("DropSelectedStaff.do - eventString : " + eventString);
		if (edit) {

			List<UserAccount> selectedStaff = this.jsonStringToList(eventString, UserAccount.class);

			for (int i = 0; i < selectedStaff.size(); i++) {
				if (accountKey.equals(selectedStaff.get(i).getKey())) {
					selectedStaff.remove(i);
					break;
				}
			}

			model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, this.listToJsonString(selectedStaff));
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, jsonString);

		} else {

			List<UserAccount> selectedStaff = this.jsonStringToList(jsonString, UserAccount.class);

			for (int i = 0; i < selectedStaff.size(); i++) {
				if (accountKey.equals(selectedStaff.get(i).getKey())) {
					selectedStaff.remove(i);
					break;
				}
			}

			model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, this.listToJsonString(selectedStaff));
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, eventString);
		}
		return ModelMappingValue.pages_view_assign_account_selected;

	}

	@RequestMapping(value = "/DropAllSelectedStaff.do")
	public String dropAllSelectedStaff(@RequestParam("edit") Boolean edit,
			@RequestParam("accountKeys") int[] accountKeys, @RequestParam("selectedStaffJson") String jsonString,
			@RequestParam("eventEditStaffJson") String eventString, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/dropAllSelectedStaff.do");
		if (!edit) {
			logger.debug("dropAllSelectedStaff.do - edit : " + edit);
			List<UserAccount> selectedStaff = this.jsonStringToList(jsonString, UserAccount.class);

			selectedStaff.clear();

			model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, this.listToJsonString(selectedStaff));
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, eventString);
		} else {
			logger.debug("dropAllSelectedStaff.do - edit : " + edit);
			List<UserAccount> selectedStaff = this.jsonStringToList(eventString, UserAccount.class);

			selectedStaff.clear();

			model.addAttribute(ModelMappingValue.var_selectedStaff, selectedStaff);
			model.addAttribute(ModelMappingValue.var_eventEditStaffJson, this.listToJsonString(selectedStaff));
			model.addAttribute(ModelMappingValue.var_selectedStaffJson, jsonString);
		}
		return ModelMappingValue.pages_view_assign_account_selected;

	}

	@RequestMapping(value = "/SubmitStaffSearch.do")
	public String submitStaffSearch(@RequestParam("accountName") String accountName,
			@RequestParam("accountRoleKey") Integer accountRoleKey, @RequestParam("privilege") String privilege,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("/SubmitStaffSearch.do");
		List<UserAccount> accountList = getAccountBy(this.getRoleFromSession(request).getSiteKey(), accountName,
				accountRoleKey, privilege);

		model.addAttribute(ModelMappingValue.var_searchedStaff, accountList);
		return ModelMappingValue.pages_view_assign_account_searched;
	}

	public List<UserAccount> getAccountBy(int siteKey, String accountName, int accountRoleKey, String privilege) {
		List<UserAccountRole> accountRoleList = null;
		List<UserAccountRole> hasPrivilegeList = new ArrayList<UserAccountRole>();
		try {

			// System.out.println("Privilege : " + privilege);

			if (userAccountManager == null) {
				System.out.println("userAccountManager is null");
			}

			accountRoleList = userAccountManager.searchUserAccount(accountName, accountRoleKey);

			// System.out.println("accountRoleList : " +
			// accountRoleList.size());

			if (!privilege.equals("")) {
				List<RolePrivilege> roleList = roleManager.searchRoleByPrivilege(privilege);

				// System.out.println("RoleList Size : " + roleList.size());

				if (roleList != null && roleList.size() > 0) {

					for (int i = 0; i < accountRoleList.size(); i++) {
						Boolean hasPrivilege = false;

						for (RolePrivilege rp : roleList) {

							// System.out.println("Role Key : "
							// + accountRoleList.get(i).getRoleKey()
							// + " || " + rp.getRoleKey());
							if (accountRoleList.get(i).getRoleKey().equals(rp.getRoleKey())) {
								hasPrivilegeList.add(accountRoleList.get(i));
								// System.out.println("ADD :
								// "+accountRoleList.get(i).getKey());
								break;
							}
						}

						// if (!hasPrivilege) {
						// accountRoleList.remove(i);
						// i--;
						// }
					}
				}
			}

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println("accountRoleList 2 : " + hasPrivilegeList.size());

		List<UserAccount> accountList = new ArrayList<UserAccount>();
		if (hasPrivilegeList != null) {
			for (int i = 0; i < hasPrivilegeList.size(); i++) {
				// System.out.println("site Key ac :"
				// + hasPrivilegeList.get(i).getRole().getSiteKey() + "||"
				// + siteKey + " || KEY : "+hasPrivilegeList.get(i).getKey());

				if (hasPrivilegeList.get(i).getRole().getSiteKey().equals(siteKey)) {
					accountList.add(hasPrivilegeList.get(i).getUserAccount());
				}
			}
		}

		// System.out.println("Account list : " + accountList.size());

		return accountList;
	}

	/**                        **/
	/** Functions for Calendar **/
	/**
	 * @throws MFMSException
	 **/
	@RequestMapping(value = "ShowCalendar.do")
	public String showCalendar(HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		logger.debug("/ShowCalendar.do");

		// Patrol Schedule Action Log
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
		PatrolScheduleActionLog patrolScheduleActionLog = new PatrolScheduleActionLog();
		patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
		patrolScheduleActionLog.setActionType("View Patrol Schedule");
		patrolScheduleActionLog.setActionBy(actionBy.getKey());
		patrolScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);

		System.out.println("Locale : " + locale.getLanguage());

		// End Patrol Schedule Action Log

		model.addAttribute(ModelMappingValue.var_calendar_locale, locale.getLanguage().equals("en") ? "en" : "zh-tw");

		return ModelMappingValue.pages_view_calendar;

	}

	@RequestMapping(value = "GetPatrolSchedule.do")
	public @ResponseBody String getPatrolSchedule(HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		logger.debug("/GetPatrolSchedule.do");
		// List<PatrolScheduleAccount> scheduleAccountList =
		// patrolScheduleManager
		// .searchPatrolSchedule(this.getRoleFromSession(request)
		// .getSiteKey(), null, null);

		List<PatrolSchedule> psList = patrolScheduleManager
				.searchPatrolScheduleBySite(this.getRoleFromSession(request).getSiteKey());

		List<CalendarJSON> json = new ArrayList<CalendarJSON>();

		for (PatrolSchedule ps : psList) {

			PatrolSchedule schedule = ps;
			RouteDef routeDef = ps.getRouteDef();

			if (schedule.getScheduleEndDate() == null) {
				// System.out.println("Null end date");

				Calendar maxEnd = Calendar.getInstance();
				maxEnd.add(Calendar.YEAR, 100);

				schedule.setScheduleEndDate(new Timestamp(maxEnd.getTimeInMillis()));
			}

			CalendarJSON calendar = new CalendarJSON();
			calendar.setId(schedule.getScheduleKey() + "");
			calendar.setTitle(timestampToString(schedule.getScheduleTime(), timeFormat) + " - " + routeDef.getCode()
					+ " - " + routeDef.getName());
			calendar.setStart(timestampToString(schedule.getScheduleTime(), timeFormat));
			calendar.setEnd("23:59");
			calendar.setDow(CalendarJSON.getDecodedWeekDay(schedule.getFrequency()));
			calendar.setUrl("");

			Frequency f = Frequency.fromInt(CalendarJSON.getDecodedFrequency(schedule.getFrequency()));

			calendar.setRanges(calendarEvent.getEventRanges(schedule.getFrequency(), schedule.getScheduleStartDate(),
					schedule.getScheduleEndDate(), schedule.getScheduleTime()));

			List<PatrolSchedule> scheduleChildenList = this.patrolScheduleManager
					.searchPatrolScheduleChilden(schedule.getScheduleKey());
			if (scheduleChildenList != null && scheduleChildenList.size() > 0) {

				List<ExcludeDays> excludeDaysList = new ArrayList<ExcludeDays>();
				for (int k = 0; k < scheduleChildenList.size(); k++) {
					excludeDaysList.add(new CalendarJSON().new ExcludeDays(
							timestampToString(scheduleChildenList.get(k).getSkippedStartDate() == null
									? new Timestamp(0) : scheduleChildenList.get(k).getSkippedStartDate(), dateFormat),
							timestampToString(scheduleChildenList.get(k).getSkippedEndDate() == null ? new Timestamp(0)
									: scheduleChildenList.get(k).getSkippedEndDate(), dateFormat)));

				}
				calendar.setExcludedDates(excludeDaysList);
			}

			json.add(calendar);

		}

		// if (scheduleAccountList != null) {
		// for (int i = 0; i < scheduleAccountList.size(); i++) {
		// PatrolScheduleAccount scheduleAccount = scheduleAccountList
		// .get(i);
		// PatrolSchedule schedule = scheduleAccount.getPatrolSchedule();
		// RouteDef routeDef = scheduleAccount.getPatrolSchedule()
		// .getRouteDef();
		//
		// if (schedule.getScheduleEndDate() == null) {
		// // System.out.println("Null end date");
		//
		// Calendar maxEnd = Calendar.getInstance();
		// maxEnd.add(Calendar.YEAR, 100);
		//
		// schedule.setScheduleEndDate(new Timestamp(maxEnd
		// .getTimeInMillis()));
		// }
		//
		// CalendarJSON calendar = new CalendarJSON();
		// calendar.setId(schedule.getScheduleKey() + "");
		// calendar.setTitle(timestampToString(schedule.getScheduleTime(),
		// timeFormat) + " - " + routeDef.getName());
		// calendar.setStart(timestampToString(schedule.getScheduleTime(),
		// timeFormat));
		// calendar.setEnd("23:59");
		// calendar.setDow(CalendarJSON.getDecodedWeekDay(schedule
		// .getFrequency()));
		// calendar.setUrl("");
		//
		// Frequency f = Frequency.fromInt(CalendarJSON
		// .getDecodedFrequency(schedule.getFrequency()));
		//
		// // List<Ranges> rangeList = new ArrayList<Ranges>();
		// // switch (f) {
		// // case Annually:
		// // Calendar start = Calendar.getInstance();
		// // start.setTime(new
		// // Date(schedule.getScheduleStartDate().getTime()));
		// //
		// // Calendar end = Calendar.getInstance();
		// // end.setTime(new
		// // Date(schedule.getScheduleEndDate().getTime()));
		// //
		// // for (Calendar st = start; st.getTimeInMillis() <=
		// // end.getTimeInMillis(); st.add(Calendar.YEAR, 1)) {
		// // rangeList.add(new CalendarJSON().new Ranges(
		// // timestampToString(new Timestamp(st.getTimeInMillis()),
		// // dateFormat),
		// // timestampToString(new Timestamp(st.getTimeInMillis()),
		// // dateFormat)));
		// // }
		// //
		// // break;
		// // case Monthly:
		// //
		// // Calendar start2 = Calendar.getInstance();
		// // start2.setTime(new
		// // Date(schedule.getScheduleStartDate().getTime()));
		// //
		// // Calendar end2 = Calendar.getInstance();
		// // end2.setTime(new
		// // Date(schedule.getScheduleEndDate().getTime()));
		// //
		// // for (Calendar st = start2; st.getTimeInMillis() <=
		// // end2.getTimeInMillis(); st.add(Calendar.MONTH,
		// // 1)) {
		// // rangeList.add(new CalendarJSON().new Ranges(
		// // timestampToString(new Timestamp(st.getTimeInMillis()),
		// // dateFormat),
		// // timestampToString(new Timestamp(st.getTimeInMillis()),
		// // dateFormat)));
		// // }
		// //
		// // break;
		// //
		// // default:
		// // rangeList.add(new CalendarJSON().new Ranges(
		// // timestampToString(schedule.getScheduleStartDate(),
		// // dateFormat),
		// // timestampToString(schedule.getScheduleEndDate(),
		// // dateFormat)));
		// //
		// // // rangeList
		// // // .add(new CalendarJSON().new Ranges(
		// // // timestampToString(
		// // // schedule.getScheduleStartDate(),
		// // // dateFormat), "2200-12-31"));
		// // break;
		// //
		// // }
		// calendar.setRanges(calendarEvent.getEventRanges(
		// schedule.getFrequency(),
		// schedule.getScheduleStartDate(),
		// schedule.getScheduleEndDate(),
		// schedule.getScheduleTime()));
		//
		// List<PatrolSchedule> scheduleChildenList = this.patrolScheduleManager
		// .searchPatrolScheduleChilden(schedule.getScheduleKey());
		// if (scheduleChildenList != null
		// && scheduleChildenList.size() > 0) {
		//
		// List<ExcludeDays> excludeDaysList = new ArrayList<ExcludeDays>();
		// for (int k = 0; k < scheduleChildenList.size(); k++) {
		// excludeDaysList
		// .add(new CalendarJSON().new ExcludeDays(
		// timestampToString(
		// scheduleChildenList.get(k)
		// .getSkippedStartDate() == null ? new Timestamp(
		// 0)
		// : scheduleChildenList
		// .get(k)
		// .getSkippedStartDate(),
		// dateFormat),
		// timestampToString(
		// scheduleChildenList.get(k)
		// .getSkippedEndDate() == null ? new Timestamp(
		// 0)
		// : scheduleChildenList
		// .get(k)
		// .getSkippedEndDate(),
		// dateFormat)));
		//
		// }
		// calendar.setExcludedDates(excludeDaysList);
		// }
		//
		// json.add(calendar);
		// }
		//
		// }

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

		System.out.println("JSON : " + rtn);
		return rtn;

	}

	@RequestMapping(value = "/GetPatrolScheduleEvent.do")
	public String getPatrolScheduleEvent(@RequestParam("scheduleKey") Integer scheduleKey,
			@RequestParam("currentStart") String currentStart, @RequestParam("currentEnd") String currentEnd,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("/GetPatrolScheduleEvent.do");

		System.out.println("scheduleKey : " + scheduleKey);

		PatrolSchedule currentSchedule = patrolScheduleManager.searchPatrolScheduleByKey(scheduleKey);

		PatrolAssignForm form = new PatrolAssignForm();

		if (currentSchedule != null) {

			form.setRouteDefKey(currentSchedule.getRouteDefKey());

			RouteDef routeDef = currentSchedule.getRouteDef();
			List<String> routeNameList = new ArrayList<String>();
			routeNameList.add(routeDef.getName());

			form.setRouteName(routeNameList);
			form.setSkippedStartDate(currentStart);
			form.setSkippedEndDate(currentEnd);

			// set current event data
			form.setScheduleKey(currentSchedule.getScheduleKey());
			form.setStartDate(currentStart.substring(0, 10));
			form.setEndDate(currentEnd.substring(0, 10));
			form.setTime(timestampToString(currentSchedule.getScheduleTime(), timeFormat));

			List<PatrolScheduleAccount> scheduleAccountList = patrolScheduleManager
					.searchPatrolScheduleAccountByScheduleKey(currentSchedule.getScheduleKey());

			List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();
			List<String> staffName = new ArrayList<String>();
			List<Integer> accountKey = new ArrayList<Integer>();
			for (int i = 0; i < scheduleAccountList.size(); i++) {

				Boolean duplicated = false;
				for (int j = 0; j < edit_eventStaff.size(); j++) {

					if (edit_eventStaff.get(j).getKey() == scheduleAccountList.get(i).getUserAccount().getKey()) {
						duplicated = true;
						break;
					}
				}
				if (!duplicated) {
					edit_eventStaff.add(scheduleAccountList.get(i).getUserAccount());
					staffName.add(scheduleAccountList.get(i).getUserAccount().getName());
					accountKey.add(scheduleAccountList.get(i).getUserAccount().getKey());
				}
			}
			form.setStaffName(staffName);
			form.setAccountKey(accountKey);
			form.setIsDeleted(currentSchedule.getDeleted());

			// set parent event data
			PatrolSchedule parentSchedule = getParentSchedule(currentSchedule);
			if (parentSchedule.getDeleted().equals("Y")) {

				form.setParentScheduleKey(currentSchedule.getScheduleKey());
				form.setParentStart(timestampToString(currentSchedule.getScheduleStartDate(), dateFormat));
				form.setParentEnd(currentSchedule.getScheduleEndDate() == null ? ""
						: timestampToString(currentSchedule.getScheduleEndDate(), dateFormat));
				form.setParentTime(timestampToString(currentSchedule.getScheduleTime(), timeFormat));
				form.setFrequency(CalendarJSON.getDecodedFrequency(currentSchedule.getFrequency()));
				form.setWeekDay(CalendarJSON.getDecodedWeekDay(currentSchedule.getFrequency()));

				List<PatrolScheduleAccount> parentScheduleAccountList = patrolScheduleManager
						.searchPatrolScheduleAccountByScheduleKey(currentSchedule.getScheduleKey());
				List<UserAccount> parentEdit_eventStaff = new ArrayList<UserAccount>();
				List<String> parentStaffName = new ArrayList<String>();
				List<Integer> parentAccountKey = new ArrayList<Integer>();
				for (int i = 0; i < parentScheduleAccountList.size(); i++) {

					Boolean duplicated = false;
					for (int j = 0; j < parentEdit_eventStaff.size(); j++) {

						if (parentEdit_eventStaff.get(j).getKey() == parentScheduleAccountList.get(i).getUserAccount()
								.getKey()) {
							duplicated = true;
							break;
						}
					}
					if (!duplicated) {
						parentEdit_eventStaff.add(parentScheduleAccountList.get(i).getUserAccount());
						parentStaffName.add(parentScheduleAccountList.get(i).getUserAccount().getName());
						parentAccountKey.add(parentScheduleAccountList.get(i).getUserAccount().getKey());
					}
				}
				form.setParentStaffName(parentStaffName);
				form.setParentAccountKey(parentAccountKey);
				form.setParentIsDeleted(parentSchedule.getDeleted());
				
				model.addAttribute(ModelMappingValue.var_parentEventEditStaffJson,
						this.listToJsonString(parentEdit_eventStaff));
				
			} else {

				form.setParentScheduleKey(parentSchedule.getScheduleKey());
				form.setParentStart(timestampToString(parentSchedule.getScheduleStartDate(), dateFormat));
				form.setParentEnd(parentSchedule.getScheduleEndDate() == null ? ""
						: timestampToString(parentSchedule.getScheduleEndDate(), dateFormat));
				form.setParentTime(timestampToString(parentSchedule.getScheduleTime(), timeFormat));
				form.setFrequency(CalendarJSON.getDecodedFrequency(parentSchedule.getFrequency()));
				form.setWeekDay(CalendarJSON.getDecodedWeekDay(parentSchedule.getFrequency()));

				List<PatrolScheduleAccount> parentScheduleAccountList = patrolScheduleManager
						.searchPatrolScheduleAccountByScheduleKey(parentSchedule.getScheduleKey());
				List<UserAccount> parentEdit_eventStaff = new ArrayList<UserAccount>();
				List<String> parentStaffName = new ArrayList<String>();
				List<Integer> parentAccountKey = new ArrayList<Integer>();
				for (int i = 0; i < parentScheduleAccountList.size(); i++) {

					Boolean duplicated = false;
					for (int j = 0; j < parentEdit_eventStaff.size(); j++) {

						if (parentEdit_eventStaff.get(j).getKey() == parentScheduleAccountList.get(i).getUserAccount()
								.getKey()) {
							duplicated = true;
							break;
						}
					}
					if (!duplicated) {
						parentEdit_eventStaff.add(parentScheduleAccountList.get(i).getUserAccount());
						parentStaffName.add(parentScheduleAccountList.get(i).getUserAccount().getName());
						parentAccountKey.add(parentScheduleAccountList.get(i).getUserAccount().getKey());
					}
				}
				form.setParentStaffName(parentStaffName);
				form.setParentAccountKey(parentAccountKey);
				form.setParentIsDeleted(parentSchedule.getDeleted());
			
				model.addAttribute(ModelMappingValue.var_parentEventEditStaffJson,
						this.listToJsonString(parentEdit_eventStaff));
			
			}

			model.addAttribute(ModelMappingValue.var_currentEventEditStaffJson, this.listToJsonString(edit_eventStaff));
			
		}

		// List<PatrolScheduleAccount> scheduleAccountList =
		// patrolScheduleManager
		// .searchPatrolScheduleAccountByScheduleKey(scheduleKey);
		// List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();
		//
		// PatrolAssignForm form = new PatrolAssignForm();
		// if (scheduleAccountList != null && scheduleAccountList.size() > 0) {
		//
		// PatrolSchedule schedule =
		// scheduleAccountList.get(0).getPatrolSchedule();
		// PatrolSchedule parentSchedule = getParentSchedule(schedule);
		//
		//
		// RouteDef routeDef =
		// scheduleAccountList.get(0).getPatrolSchedule().getRouteDef();
		// List<String> routeNameList = new ArrayList<String>();
		// routeNameList.add(routeDef.getName());
		// form.setRouteName(routeNameList);
		//
		// List<String> staffName = new ArrayList<String>();
		// List<Integer> accountKey = new ArrayList<Integer>();
		//
		// for (int i = 0; i < scheduleAccountList.size(); i++) {
		//
		// Boolean duplicated = false;
		// for (int j = 0; j < edit_eventStaff.size(); j++) {
		//
		// if (edit_eventStaff.get(j).getKey() ==
		// scheduleAccountList.get(i).getUserAccount().getKey()) {
		// duplicated = true;
		// break;
		// }
		// }
		// if (!duplicated) {
		// edit_eventStaff.add(scheduleAccountList.get(i).getUserAccount());
		// staffName.add(scheduleAccountList.get(i).getUserAccount().getName());
		// accountKey.add(scheduleAccountList.get(i).getUserAccount().getKey());
		// }
		// }
		//
		// form.setScheduleKey(scheduleKey);
		// form.setRouteDefKey(routeDef.getRouteDefKey());
		// form.setStaffName(staffName);
		// form.setStartDate(timestampToString(schedule.getScheduleStartDate(),
		// dateFormat));
		// form.setEndDate(schedule.getScheduleEndDate() == null ? ""
		// : timestampToString(schedule.getScheduleEndDate(), dateFormat));
		// form.setTime(timestampToString(schedule.getScheduleTime(),
		// timeFormat));
		// form.setCurrentStart(currentStart.substring(0, 10));
		// form.setCurrentEnd(currentEnd.substring(0, 10));
		// form.setFrequency(CalendarJSON.getDecodedFrequency(schedule.getFrequency()));
		// form.setWeekDay(CalendarJSON.getDecodedWeekDay(schedule.getFrequency()));
		// form.setParentCurrentStart(currentStart.substring(0, 10));
		// form.setParentCurrentEnd(currentEnd.substring(0, 10));
		// form.setIsDeleted(schedule.getDeleted());
		//
		// }

		// formBeforeEdit = form;
		model.addAttribute(ModelMappingValue.var_frequency, getFrequencyMap(locale));
		model.addAttribute(ModelMappingValue.var_weekDay, getWeekDayMap(locale));
		model.addAttribute(ModelMappingValue.var_weeklyValue, weeklyValue);
		model.addAttribute(ModelMappingValue.var_onceValue, onceValue);
		model.addAttribute(ModelMappingValue.form_patrolAssignEditForm, form);
		// model.addAttribute(ModelMappingValue.var_eventEditStaffJson,
		// this.listToJsonString(edit_eventStaff));

		return ModelMappingValue.pages_view_calendar_eventModal;
	}

	@RequestMapping(value = "EditCurrentEvent.do")
	public String editCurrentEvent(@ModelAttribute("patrolAssignEditForm") PatrolAssignForm form,
			@RequestParam("account") Integer[] accountKeyArray, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) throws MFMSException {

		logger.debug("/EditCurrentEvent.do");

		patrolAssignFormValidator.validate(form, result);

		if (result.hasErrors()) {

		}

		Boolean edited = true;
		// if (!formBeforeEdit.getCurrentStart().equals(form.getCurrentStart()))
		// {
		// edited = true;
		// }
		// if (!formBeforeEdit.getCurrentEnd().equals(form.getCurrentEnd())) {
		// edited = true;
		// }
		// if (!formBeforeEdit.getTime().equals(form.getTime())) {
		// edited = true;
		// }

		if (edited) {
			try {

				Role role = getRoleFromSession(request);

				// Create Schedule Object
				List<PatrolSchedule> scheduleList = new ArrayList<PatrolSchedule>();

				PatrolSchedule schedule = new PatrolSchedule();
				schedule.setRouteDefKey(form.getRouteDefKey());
				schedule.setSiteKey(this.getRoleFromSession(request).getSiteKey());
				schedule.setScheduleStartDate(stringToTimestamp(form.getStartDate(), dateFormat));
				schedule.setScheduleEndDate(stringToTimestamp(form.getEndDate(), dateFormat));
				schedule.setScheduleTime(stringToTimestamp(form.getTime(), timeFormat));
				schedule.setFrequency(CalendarJSON.encodeFrequency(CalendarJSON.Frequency.Once.getValue(), null));
				schedule.setParentId(form.getScheduleKey());
				schedule.setCreateBy(role.getKey());
				schedule.setCreateDateTime(getCurrentTime());
				schedule.setLastModifyBy(role.getKey());
				schedule.setLastModifyDateTime(getCurrentTime());
				schedule.setSkipped("N");
				schedule.setDeleted("N");
				// schedule.setSkippedStartDate(stringToTimestamp(form.getParentCurrentStart(),
				// dateFormat));
				// schedule.setSkippedEndDate(stringToTimestamp(form.getParentCurrentEnd(),
				// dateFormat));
				schedule.setSkippedStartDate(stringToTimestamp(form.getSkippedStartDate(), dateFormat));
				schedule.setSkippedEndDate(stringToTimestamp(form.getSkippedEndDate(), dateFormat));

				scheduleList.add(schedule);

				List<PatrolScheduleAccount> scheduleAccountList = new ArrayList<PatrolScheduleAccount>();

				List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();
				// if (!form.getEventEditStaffJson().equals("")) {
				// if (!form.getEventEditStaffJson().isEmpty()) {
				// edit_eventStaff =
				// this.jsonStringToList(form.getEventEditStaffJson(),
				// UserAccount.class);
				// }

				if (accountKeyArray != null && accountKeyArray.length > 0) {
					for (int j = 0; j < accountKeyArray.length; j++) {

						UserAccount tmp = userAccountManager.getUserAccountByAccountKey(accountKeyArray[j]);

						if (tmp != null) {
							edit_eventStaff.add(tmp);
						}
					}
				}

				if (edit_eventStaff != null) {
					for (int i = 0; i < edit_eventStaff.size(); i++) {
						PatrolScheduleAccount scheduleAccount = new PatrolScheduleAccount();
						scheduleAccount.setAccountKey(edit_eventStaff.get(i).getKey());
						scheduleAccount.setCreateBy(role.getKey());
						scheduleAccount.setCreateDateTime(getCurrentTime());
						scheduleAccount.setLastModifyBy(role.getKey());
						scheduleAccount.setLastModifyTime(getCurrentTime());
						scheduleAccount.setDeleted("N");

						scheduleAccountList.add(scheduleAccount);
					}

				}

				// Patrol Schedule Action Log
				Role currRole = (Role) request.getSession().getAttribute("currRole");
				UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
				PatrolScheduleActionLog patrolScheduleActionLog = new PatrolScheduleActionLog();
				patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
				patrolScheduleActionLog.setActionType("Edit Current Event");
				patrolScheduleActionLog.setActionBy(actionBy.getKey());
				patrolScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
				try {
					patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// End Patrol Schedule Action Log

				patrolScheduleManager.savePatrolSchedule(scheduleList, scheduleAccountList);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return showSuccessMsg("", "", model, locale);
	}

	// @RequestMapping(value = "skipCurrentEvent.do", method =
	// RequestMethod.POST)
	// public String editSeriesEvent(@ModelAttribute("patrolAssignEditForm")
	// PatrolAssignForm form,
	// BindingResult result, HttpServletRequest request,
	// HttpServletResponse response, ModelMap model){
	//
	// }
	//

	@RequestMapping(value = "EditSeriesEvent.do")
	public String editSeriesEvent(@ModelAttribute("patrolAssignEditForm") PatrolAssignForm form,
			@RequestParam("account") Integer[] accountKeyArray, BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) throws MFMSException {

		logger.debug("/EditSeriesEvent.do");

		Boolean edited = true;
		// if (!formBeforeEdit.getStartDate().equals(form.getStartDate())) {
		// edited = true;
		// }
		// if (!formBeforeEdit.getEndDate().equals(form.getEndDate())) {
		// edited = true;
		// }
		// if (!formBeforeEdit.getTime().equals(form.getTime())) {
		// edited = true;
		// }
		// if (formBeforeEdit.getFrequency() != form.getFrequency()) {
		// edited = true;
		// }
		//
		// if (formBeforeEdit.getAccountKey().size() != accountKeyArray.length)
		// {
		// edited = true;
		// }

		// if (!edited) {
		// for (int i = 0; i < formBeforeEdit.getAccountKey().size(); i++) {
		// Integer tempKey = formBeforeEdit.getAccountKey().get(i);
		//
		// Boolean existed = false;
		//
		// for (int j = 0; j < accountKeyArray.length; j++) {
		// if (tempKey.equals(accountKeyArray[i])) {
		// existed = true;
		// }
		// }
		// if (!existed) {
		//
		// edited = true;
		// }
		//
		// if (edited) {
		// break;
		// }
		// }
		// }

		if (edited) {

			List<PatrolScheduleAccount> scheduleAccountList = patrolScheduleManager
					.searchPatrolScheduleAccountByScheduleKey(form.getParentScheduleKey());

			if (scheduleAccountList != null && scheduleAccountList.size() > 0) {
				try {
					Role role = this.getRoleFromSession(request);

					PatrolSchedule schedule = scheduleAccountList.get(0).getPatrolSchedule();

					schedule.setScheduleStartDate(stringToTimestamp(form.getParentStart(), dateFormat));
					schedule.setScheduleEndDate((null == form.getParentEnd() || form.getParentEnd().equals("")) ? null
							: stringToTimestamp(form.getParentEnd(), dateFormat));
					schedule.setScheduleTime(stringToTimestamp(form.getParentTime(), timeFormat));

					List<Integer> weekDayList = new ArrayList<Integer>();
					for (int i = 0; i < form.getWeekDay().length; i++) {
						weekDayList.add(form.getWeekDay()[i]);
					}

					schedule.setFrequency(CalendarJSON.encodeFrequency(form.getFrequency(), weekDayList));
					if (form.getFrequency().equals(Frequency.Once.getValue())) {
						schedule.setScheduleEndDate(stringToTimestamp(form.getStartDate(), dateFormat));
					}

					schedule.setLastModifyBy(role.getKey());
					schedule.setLastModifyDateTime(getCurrentTime());
					List<PatrolSchedule> patrolScheduleList = new ArrayList<PatrolSchedule>();
					patrolScheduleList.add(schedule);

					List<Integer> scheduleAccountNeedDelete = new ArrayList<Integer>();
					List<Integer> scheduleAccountNeedAdd = new ArrayList<Integer>();

					for (int i = 0; i < accountKeyArray.length; i++) {

						scheduleAccountNeedAdd.add(accountKeyArray[i]);
					}

					// check if the account exists in old record
					for (int i = 0; i < scheduleAccountList.size(); i++) {
						Boolean inNewList = false;

						for (int j = 0; j < accountKeyArray.length; j++) {

							if (scheduleAccountList.get(i).getAccountKey() == accountKeyArray[j]) {
								inNewList = true;
								break;
							}
						}

						if (!inNewList) {
							scheduleAccountNeedDelete.add(scheduleAccountList.get(i).getAccountKey());
						} else {
							scheduleAccountNeedAdd.remove((Integer) scheduleAccountList.get(i).getAccountKey());
						}

					}

					this.patrolScheduleManager.savePatrolSchedule(patrolScheduleList, null);
					List<PatrolScheduleAccount> newScheduleAccountList = new ArrayList<PatrolScheduleAccount>();

					// Add new added member
					for (int i = 0; i < scheduleAccountNeedAdd.size(); i++) {

						PatrolScheduleAccount newScheduleAccount = new PatrolScheduleAccount();
						newScheduleAccount.setAccountKey(scheduleAccountNeedAdd.get(i));
						newScheduleAccount.setScheduleKey(schedule.getScheduleKey());
						newScheduleAccount.setCreateBy(role.getKey());
						newScheduleAccount.setCreateDateTime(getCurrentTime());
						newScheduleAccount.setLastModifyBy(role.getKey());
						newScheduleAccount.setLastModifyTime(getCurrentTime());
						newScheduleAccount.setDeleted("N");
						newScheduleAccountList.add(newScheduleAccount);
					}
					patrolScheduleManager.insertNewPatrolScheduleAccount(newScheduleAccountList);
					// mark delete removed
					List<PatrolScheduleAccount> searchedList = patrolScheduleManager
							.searchPatrolScheduleAccount(schedule.getScheduleKey(), scheduleAccountNeedDelete);
					if (searchedList != null) {
						for (int i = 0; i < searchedList.size(); i++) {
							searchedList.get(i).setDeleted("Y");
							searchedList.get(i).setLastModifyBy(role.getKey());
							searchedList.get(i).setLastModifyTime(getCurrentTime());
						}
					}
					patrolScheduleManager.updatePatrolScheduleAccount(searchedList);

					// Patrol Schedule Action Log
					Role currRole = (Role) request.getSession().getAttribute("currRole");
					UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
					PatrolScheduleActionLog patrolScheduleActionLog = new PatrolScheduleActionLog();
					patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
					patrolScheduleActionLog.setActionType("Edit Series Event");
					patrolScheduleActionLog.setActionBy(actionBy.getKey());
					patrolScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
					try {
						patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// End Patrol Schedule Action Log

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return showSuccessMsg("", "", model, locale);
	}

	@RequestMapping(value = "/ShowModifySuccessMsg.do")
	public String showSuccessMsg(@RequestParam("msg") String msg, @RequestParam("extraMsg") String extraMsg,
			ModelMap model, Locale locale) {

		String reply = messageSource.getMessage("patrol.schedule.modify.success", null, locale);

		model.addAttribute(PatrolController.ModelMappingValue.var_successMsgClass, "alert-success");
		model.addAttribute(PatrolController.ModelMappingValue.var_successMsgStr, reply);

		return PatrolController.ModelMappingValue.pages_view_showSuccessMsg;
	}

	@RequestMapping(value = "DeleteCurrentEvent.do")
	public void deleteCurrentEvent(@ModelAttribute("patrolAssignEditForm") PatrolAssignForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException, Exception {

		logger.debug("/DeleteCurrentEvent.do");

		System.out.println("deleteCurrentEvent");
		// List<PatrolScheduleAccount> scheduleAccountList =
		// patrolScheduleManager
		// .searchPatrolScheduleAccountByScheduleKey(form.getScheduleKey());

		List<PatrolSchedule> updateList = new ArrayList<PatrolSchedule>();

		// if (scheduleAccountList != null && scheduleAccountList.size() > 0) {
		// for (PatrolScheduleAccount scheduleAccount : scheduleAccountList)
		// {

		// scheduleAccount.setDeleted("N");
		// PatrolSchedule schedule =
		// scheduleAccountList.get(0).getPatrolSchedule();
		PatrolSchedule schedule = patrolScheduleManager.searchPatrolScheduleByKey(form.getScheduleKey());

		// Timestamp current = this.getCurrentTime();

		Calendar current = Calendar.getInstance();

		Calendar time = Calendar.getInstance();
		time.setTime(new Date(schedule.getScheduleTime().getTime()));

		Calendar scheduleStartTime = Calendar.getInstance();
		try {
			scheduleStartTime.setTime(dateFormat.parse(form.getStartDate()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		scheduleStartTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		scheduleStartTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

		// Timestamp scheduleStartTime = new Timestamp(schedule
		// .getScheduleStartDate().getTime()
		// + schedule.getScheduleTime().getTime());

		System.out.println("Date : " + schedule.getScheduleStartDate().getTime() + " || Time : "
				+ schedule.getScheduleTime().getTime());
		System.out.println("Current : " + current.getTime() + "|| schedule : " + scheduleStartTime.getTime());

		if (current.after(scheduleStartTime)) {
			// can not delete
			System.out.println("set end date");

			// Timestamp maxEnd = schedule.getScheduleEndDate();
			// if (maxEnd == null) {
			// Calendar mE = Calendar.getInstance();
			// mE.add(Calendar.YEAR, 100);
			// maxEnd = new Timestamp(mE.getTimeInMillis());
			// }
			//
			// Calendar end = Calendar.getInstance();
			// end.setTime(new Date(maxEnd.getTime()));
			// if (current.before(end))
			// schedule.setScheduleEndDate(new
			// Timestamp(current.getTimeInMillis()));

			response.getWriter().write("cannotDeleteSucess");

		} else {

			System.out.println("set delete");

			if (schedule.getParentId() > 0) {
				schedule.setDeleted("Y");

				List<PatrolSchedule> allCurrentChild = this.getAllChildSchedule(form.getParentScheduleKey());
				if (allCurrentChild != null) {

					for (PatrolSchedule ps : allCurrentChild) {

						if (ps.getScheduleStartDate().getTime() == schedule.getScheduleStartDate().getTime()
								&& ps.getScheduleEndDate().getTime() == schedule.getScheduleEndDate().getTime()
								&& ps.getScheduleKey() != form.getScheduleKey()) {
							ps.setDeleted("Y");
							ps.setLastModifyBy(this.getRoleFromSession(request).getKey());
							ps.setLastModifyDateTime(getCurrentTime());
							updateList.add(ps);
						}
					}
				}

			} else {

				try {
					Role role = getRoleFromSession(request);

					Frequency f = Frequency.fromInt(CalendarJSON.getDecodedFrequency(schedule.getFrequency()));
					switch (f) {
					case Annually:
					case Daily:
					case Monthly:
					case Weekly:

						List<PatrolSchedule> newScheduleList = new ArrayList<PatrolSchedule>();

						PatrolSchedule newSchedule = new PatrolSchedule();

						newSchedule.setSiteKey(schedule.getSiteKey());
						newSchedule.setRouteDefKey(schedule.getRouteDefKey());
						newSchedule.setScheduleStartDate(schedule.getScheduleStartDate());
						newSchedule.setScheduleEndDate(schedule.getScheduleEndDate());
						newSchedule.setScheduleTime(schedule.getScheduleTime());
						newSchedule.setFrequency(
								CalendarJSON.encodeFrequency(CalendarJSON.Frequency.Once.getValue(), null));
						newSchedule.setParentId(schedule.getScheduleKey());
						newSchedule.setCreateBy(role.getKey());
						newSchedule.setCreateDateTime(getCurrentTime());
						newSchedule.setLastModifyBy(role.getKey());
						newSchedule.setLastModifyDateTime(getCurrentTime());
						newSchedule.setSkipped("N");
						newSchedule.setDeleted("Y");
						newSchedule.setSkippedStartDate(stringToTimestamp(form.getSkippedStartDate(), dateFormat));
						newSchedule.setSkippedEndDate(stringToTimestamp(form.getSkippedEndDate(), dateFormat));
						newScheduleList.add(newSchedule);

						List<PatrolScheduleAccount> newScheduleAccountList = new ArrayList<PatrolScheduleAccount>();

						List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();
						// if (!form.getEventEditStaffJson().equals("")) {
						if (!form.getEventEditStaffJson().isEmpty()) {
							edit_eventStaff = this.jsonStringToList(form.getEventEditStaffJson(), UserAccount.class);
						}

						if (edit_eventStaff != null) {
							for (int i = 0; i < edit_eventStaff.size(); i++) {
								PatrolScheduleAccount scheduleAccount = new PatrolScheduleAccount();
								scheduleAccount.setAccountKey(edit_eventStaff.get(i).getKey());
								scheduleAccount.setCreateBy(role.getKey());
								scheduleAccount.setCreateDateTime(getCurrentTime());
								scheduleAccount.setLastModifyBy(role.getKey());
								scheduleAccount.setLastModifyTime(getCurrentTime());
								scheduleAccount.setDeleted("N");

								newScheduleAccountList.add(scheduleAccount);
							}

						}

						patrolScheduleManager.savePatrolSchedule(newScheduleList, newScheduleAccountList);
						break;
					default:
						schedule.setDeleted("Y");
						break;

					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			// Patrol Schedule Action Log
			Role currRole = (Role) request.getSession().getAttribute("currRole");
			UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
			PatrolScheduleActionLog patrolScheduleActionLog = new PatrolScheduleActionLog();
			patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
			patrolScheduleActionLog.setActionType("Delete Current Event");
			patrolScheduleActionLog.setActionBy(actionBy.getKey());
			patrolScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
			try {
				patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// End Patrol Schedule Action Log
			response.getWriter().write("deleteScheduleSuccess");
		}
		schedule.setLastModifyBy(this.getRoleFromSession(request).getKey());
		schedule.setLastModifyDateTime(getCurrentTime());

		updateList.add(schedule);

		// }

		// Patrol Schedule Action Log
		// Role currRole = (Role) request.getSession().getAttribute("currRole");
		// UserAccount actionBy = (UserAccount)
		// request.getSession().getAttribute("user");
		// PatrolScheduleActionLog patrolScheduleActionLog = new
		// PatrolScheduleActionLog();
		// patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
		// patrolScheduleActionLog.setActionType("Delete Current Event");
		// patrolScheduleActionLog.setActionBy(actionBy.getKey());
		// patrolScheduleActionLog.setActionDateTime(new
		// Timestamp(System.currentTimeMillis()));
		// try {
		// patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);
		// } catch (Exception e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// End Patrol Schedule Action Log

		patrolScheduleManager.savePatrolSchedule(updateList, null);
		System.out.println("End delete");
		// return "";
	}

	@RequestMapping(value = "DeleteSeriesEvent.do")
	public void deleteSeriesEvent(@ModelAttribute("patrolAssignEditForm") PatrolAssignForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException, Exception {

		logger.debug("/DeleteSeriesEvent.do");

		// List<PatrolScheduleAccount> scheduleAccountList =
		// patrolScheduleManager
		// .searchPatrolScheduleAccountByScheduleKey(form.getParentScheduleKey());

		List<PatrolSchedule> updateList = new ArrayList<PatrolSchedule>();

		// if (scheduleAccountList != null && scheduleAccountList.size() > 0) {
		// for (PatrolScheduleAccount scheduleAccount : scheduleAccountList)
		// {

		// scheduleAccount.setDeleted("N");
		PatrolSchedule schedule = patrolScheduleManager.searchPatrolScheduleByKey(form.getParentScheduleKey());

		Calendar current = Calendar.getInstance();

		Calendar time = Calendar.getInstance();
		time.setTime(new Date(schedule.getScheduleTime().getTime()));

		Calendar scheduleStartTime = Calendar.getInstance();
		scheduleStartTime.setTime(new Date(schedule.getScheduleStartDate().getTime()));
		scheduleStartTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
		scheduleStartTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

		// Timestamp scheduleStartTime = new Timestamp(schedulez
		// .getScheduleStartDate().getTime()
		// + schedule.getScheduleTime().getTime());
		if (current.after(scheduleStartTime)) {

			Calendar scheduleEndTime = Calendar.getInstance();

			// add by 15-6-2017
			if (schedule.getScheduleEndDate() != null) {
				// origin code only the below one row.
				scheduleEndTime.setTime(new Date(schedule.getScheduleEndDate().getTime()));

			} else {
				schedule.setScheduleEndDate(new Timestamp(System.currentTimeMillis()));

				List<PatrolSchedule> only4DeletePatrolSeries = new ArrayList<>();
				only4DeletePatrolSeries.add(schedule);

				this.patrolScheduleManager.savePatrolSchedule(only4DeletePatrolSeries, null);

				scheduleEndTime.setTime(new Date(System.currentTimeMillis()));
			}

			scheduleEndTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
			scheduleEndTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

			if (current.before(scheduleEndTime)) {
				schedule.setScheduleEndDate(new Timestamp(current.getTimeInMillis()));
			}

			System.out.println("*************************************can not delete current!");
			response.getWriter().write("deleteFutureNotDeleteCurrent");

		} else {
			schedule.setDeleted("Y");
			System.out.println("************************************* delete current!");
			response.getWriter().write("deleteScheduleSuccess");
		}
		schedule.setLastModifyBy(this.getRoleFromSession(request).getKey());
		schedule.setLastModifyDateTime(getCurrentTime());

		updateList.add(schedule);

		// List<PatrolSchedule> scheduleChildenList = this.patrolScheduleManager
		// .searchPatrolScheduleChilden(schedule.getScheduleKey());

		List<PatrolSchedule> scheduleChildenList = this.getAllChildSchedule(form.getParentScheduleKey());

		for (PatrolSchedule childrenSchedule : scheduleChildenList) {

			Calendar currentTime = Calendar.getInstance();

			Calendar tmp = Calendar.getInstance();
			tmp.setTime(childrenSchedule.getScheduleTime());
			;

			Calendar childStartDateTime = Calendar.getInstance();
			childStartDateTime.setTime(childrenSchedule.getScheduleStartDate());
			childStartDateTime.set(Calendar.HOUR_OF_DAY, tmp.get(Calendar.HOUR_OF_DAY));
			childStartDateTime.set(Calendar.MINUTE, tmp.get(Calendar.MINUTE));
			childStartDateTime.set(Calendar.SECOND, tmp.get(Calendar.SECOND));

			if (currentTime.after(childStartDateTime)) {

				childrenSchedule.setScheduleEndDate(new Timestamp(currentTime.getTime().getTime()));
			} else {
				childrenSchedule.setDeleted("Y");
			}

			childrenSchedule.setLastModifyBy(this.getRoleFromSession(request).getKey());
			childrenSchedule.setLastModifyDateTime(getCurrentTime());

			updateList.add(childrenSchedule);

		}

		// }

		// Patrol Schedule Action Log
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
		PatrolScheduleActionLog patrolScheduleActionLog = new PatrolScheduleActionLog();
		patrolScheduleActionLog.setSiteKey(currRole.getSiteKey());
		patrolScheduleActionLog.setActionType("Delete Series Event");
		patrolScheduleActionLog.setActionBy(actionBy.getKey());
		patrolScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		try {
			patrolScheduleActionLogManager.savePatrolScheduleActionLog(patrolScheduleActionLog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// End Patrol Schedule Action Log

		patrolScheduleManager.savePatrolSchedule(updateList, null);

		// return "";

	}

	@RequestMapping(value = "testEvenCalendar.do")
	public void testEvenCalendar(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		Calendar current = Calendar.getInstance();
		current.set(Calendar.DAY_OF_MONTH, 9);
		//
		Calendar timeBeforeToSee = Calendar.getInstance();
		// timeBeforeToSee.add(Calendar.HOUR_OF_DAY, hourBefore);
		timeBeforeToSee.set(Calendar.HOUR_OF_DAY, 23);
		timeBeforeToSee.set(Calendar.MINUTE, 59);
		timeBeforeToSee.set(Calendar.SECOND, 59);
		timeBeforeToSee.set(Calendar.MILLISECOND, 999);

		List<PatrolSchedule> patrolScheduleList = calendarEvent
				.getEventByDateRange(this.getRoleFromSession(request).getSiteKey(), current, current);

		for (PatrolSchedule ps : patrolScheduleList) {
			// System.out.println("All Schedule Key : " + ps.getScheduleKey());

			Boolean isToday = calendarEvent.isTheDayEvent(today, timeBeforeToSee, ps.getFrequency(),
					ps.getScheduleStartDate(), ps.getScheduleEndDate(), ps.getScheduleTime());

			System.out.println("All Schedule Key : " + ps.getScheduleKey() + " || " + isToday);

			// if (isToday) {
			// System.out.println(""+isToday);
			// }

		}

	}

	@RequestMapping(value = "checkSession.do")
	public void checkSession(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {

		// Role currRole = (Role) request.getSession().getAttribute("currRole");
		UserAccount currUser = (UserAccount) request.getSession().getAttribute("user");
		if (null == currUser) {
			response.getWriter().write("fail");
		} else {
			response.getWriter().write("true");
		}
	}

	// @RequestMapping(value = "deleteSeriesEvent.do", method =
	// RequestMethod.POST)
	// public String editSeriesEvent(@ModelAttribute("patrolAssignEditForm")
	// PatrolAssignForm form,
	// BindingResult result, HttpServletRequest request,
	// HttpServletResponse response, ModelMap model){
	//
	// }
	/**                 **/
	/** Other Functions **/
	/**                 **/

	private PatrolSchedule getParentSchedule(PatrolSchedule ps) {

		PatrolSchedule tmp = patrolScheduleManager.searchPatrolScheduleByKey(ps.getParentId());
		if (tmp != null)
			return getParentSchedule(tmp);

		return ps;
	}

	private List<PatrolSchedule> getAllChildSchedule(Integer scheduleKey) {

		List<PatrolSchedule> list = new ArrayList<PatrolSchedule>();

		List<PatrolSchedule> tmp = patrolScheduleManager.searchPatrolScheduleChilden(scheduleKey);

		if (tmp != null && !tmp.isEmpty()) {

			for (PatrolSchedule ps : tmp) {
				list.add(ps);
				list.addAll(getAllChildSchedule(ps.getScheduleKey()));
			}
		}

		return list;

	}

	@RequestMapping(value = "testParent.do", method = RequestMethod.GET)
	public @ResponseBody String testParent(@RequestParam("key") int key, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		// int scheduleKey = 379;
		// int scheduleKey = 416;
		// int scheduleKey = 417;

		int scheduleKey = key;

		// PatrolSchedule ps =
		// patrolScheduleManager.searchPatrolScheduleByKey(scheduleKey);
		//
		// PatrolSchedule parent = getParentSchedule(ps);

		List<PatrolSchedule> list = getAllChildSchedule(scheduleKey);

		if (list.isEmpty())
			System.out.println("NO Child");

		for (PatrolSchedule ps : list)
			System.out.println("Child Schedule Key : " + ps.getScheduleKey());

		return "";
	}

	public void clearData() {
		// selectedRoute = null;
		// schedulerCounter = 1;
	}

	public List<Role> getAllAccountRole(int siteKey) {
		try {
			return roleManager.searchRole(siteKey, null, null, "N", false);
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private Role getRoleFromSession(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Role role = (Role) session.getAttribute("currRole");

		return role;

	}

	private Timestamp getCurrentTime() {
		return new Timestamp(new Date().getTime());
	}

	private Map<Integer, String> getFrequencyMap(Locale locale) {
		Map<Integer, String> frequencyMap = new LinkedHashMap<Integer, String>();

		Frequency[] frequencyArray = Frequency.class.getEnumConstants();

		for (int i = 0; i < frequencyArray.length; i++) {
			if (frequencyArray[i].getValue() > 100) {
				String name = messageSource.getMessage("schedule.frequency." + frequencyArray[i].name(), null, locale);
				frequencyMap.put(frequencyArray[i].getValue(), name);
			}
		}

		return frequencyMap;
	}

	private Map<Integer, String> getWeekDayMap(Locale locale) {

		Map<Integer, String> weekDayMap = new LinkedHashMap<Integer, String>();

		Frequency[] frequencyArray = Frequency.class.getEnumConstants();

		for (int i = 0; i < frequencyArray.length; i++) {
			if (frequencyArray[i].getValue() > 100) {

			} else {
				String name = messageSource.getMessage("schedule.frequency." + frequencyArray[i].name(), null, locale);
				weekDayMap.put(frequencyArray[i].getValue(), name);
			}
		}

		return weekDayMap;
	}

	public PatrolAssignFormValidator getPatrolAssignFormValidator() {
		return patrolAssignFormValidator;
	}

	public void setPatrolAssignFormValidator(PatrolAssignFormValidator patrolAssignFormValidator) {
		this.patrolAssignFormValidator = patrolAssignFormValidator;
	}

	private Timestamp stringToTimestamp(String date, SimpleDateFormat format) throws ParseException {

		return new Timestamp(format.parse(date).getTime());
	}

	private String timestampToString(Timestamp time, SimpleDateFormat format) {
		return format.format(new Date(time.getTime()));
	}

	private <T> List<T> jsonStringToList(String jsonString, Class<T> clazz) {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		List<T> json = new ArrayList<T>();

		// deal with the "\"
		jsonString = jsonString.replaceAll("\\\\", "\\\\\\\\");

		if (!jsonString.equals("")) {

			try {
				json = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return json;
	}

	private <T> String listToJsonString(List<T> json) {
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		String rtn = "";

		try {
			rtn = mapper.writeValueAsString(json);

			System.out.println("listToJsonString str :" + rtn);

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

class StringComparator implements Comparator<String> {
	@Override
	public int compare(String s1, String s2) {
		int n1 = s1.length();
		int n2 = s2.length();
		int min = Math.min(n1, n2);
		for (int i = 0; i < min; i++) {
			char c1 = s1.charAt(i);
			char c2 = s2.charAt(i);
			if (c1 != c2) {
				if (isAtoZ(c1) && isAtoZ(c2)) {
					return getSortPosition(c1) - getSortPosition(c2);
				}
				return c1 - c2;
			}
		}
		return n1 - n2;
	}

	private boolean isAtoZ(char c) {
		return c > 64 && c < 123;
	}

	private int getSortPosition(char c) {
		if (c < 91) {
			return 2 * (c - 64);
		} else if (c > 96) {
			return (2 * (c - 96)) - 1;
		} else {
			return c;
		}
	}
}
