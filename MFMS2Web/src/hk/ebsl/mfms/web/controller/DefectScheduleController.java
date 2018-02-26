package hk.ebsl.mfms.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;

import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import hk.ebsl.mfms.dto.DefectScheduleActionLog;
import hk.ebsl.mfms.dto.DefectScheduleExport;
import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RoleExport;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.CalendarJSON;
import hk.ebsl.mfms.json.CalendarJSON.ExcludeDays;
import hk.ebsl.mfms.json.CalendarJSON.Frequency;
import hk.ebsl.mfms.json.MaintenanceScheduleJson;
import hk.ebsl.mfms.manager.DefectScheduleActionLogManager;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.ToolManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.utility.CalendarEvent;
import hk.ebsl.mfms.utility.writer.ColumnDisplay;
import hk.ebsl.mfms.utility.writer.ExcelWriter;
import hk.ebsl.mfms.web.controller.AccountManagementController.StringComparator;
import hk.ebsl.mfms.web.form.DefectScheduleForm;
import hk.ebsl.mfms.web.form.validator.DefectScheduleFormValidator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DefectScheduleController {

	public final static Logger logger = Logger
			.getLogger(DefectScheduleController.class);

	@Autowired
	private DefectScheduleManager defectScheduleManager;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ToolManager toolManager;
	@Autowired
	private EquipmentManager equipmentManager;
	@Autowired
	private LocationManager locationManager;
	@Autowired
	private UserAccountManager userManager;
	@Autowired
	private DefectScheduleActionLogManager defectScheduleActionLogManager;
	@Autowired
	private Properties privilegeMap;
	@Autowired
	private RoleManager roleManager;

	@Autowired
	CalendarEvent calendarEvent;

	@Autowired
	private DefectScheduleFormValidator defectScheduleFormValidator;
	
	@Autowired
	private Properties propertyConfigurer;
	@Autowired
	private SiteManager siteManager;
	
	SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat fullTimeFormat = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

	@RequestMapping(value = "/MaintenanceSchedule.do")
	public String showReportMenu(HttpServletRequest request,
			HttpServletResponse response) {

		return "maintenance/maintenance_menu";

	}

	public class ToolComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

	// TODO: GetDefectSchedule

	@RequestMapping(value = "GetDefectSchedule.do")
	public @ResponseBody String getDefectSchedule(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws JsonGenerationException, JsonMappingException, IOException,
			MFMSException, ParseException {

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		UserAccount account = (UserAccount) session.getAttribute("user");

		List<CalendarJSON> json = new ArrayList<CalendarJSON>();

		List<DefectSchedule> tempList = defectScheduleManager.getSchedule(currRole.getSiteKey());

		List<DefectSchedule> defectScheduleList = new ArrayList<DefectSchedule>();

		for (DefectSchedule defectSchedule : tempList) {
			if (defectSchedule.getLocationKey() != null) {
				if (locationManager.hasLocationPrivilege(defectSchedule.getLocationKey(), account.getKey())) {
					defectScheduleList.add(defectSchedule);
				}
			} else {
				defectScheduleList.add(defectSchedule);
			}
		}

		for (DefectSchedule defectSchedule : defectScheduleList) {

			int year100 = Integer.parseInt(new SimpleDateFormat("yyyy").format(defectSchedule.getScheduleStartDate()))+100;
			
			if (defectSchedule.getScheduleEndDate() == null) {

//				Calendar maxEnd = Calendar.getInstance();
//				maxEnd.add(Calendar.YEAR, 100);
//				defectSchedule.setScheduleEndDate(new Timestamp(maxEnd.getTimeInMillis()));
				
				String dateforEnd = year100 + "-" + new SimpleDateFormat("MM-dd").format(defectSchedule.getScheduleStartDate()) + " 23:59:00";
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parsedDate = dateFormat.parse(dateforEnd);
				Timestamp timestamp = new Timestamp(parsedDate.getTime());
				
			    defectSchedule.setScheduleEndDate(timestamp);
			}
			
			String endDateFor1970 = new SimpleDateFormat("YYYY-MM-dd").format(defectSchedule.getScheduleEndDate());
			String endTimeFor1970 = new SimpleDateFormat("HH:mm:ss").format(defectSchedule.getScheduleEndDate());
			
			if(endDateFor1970.equals("1970-01-01")){

				String ed = year100 + "-" + new SimpleDateFormat("MM-dd").format(defectSchedule.getScheduleStartDate()) + " " + endTimeFor1970;
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parsedDate = dateFormat.parse(ed);
				Timestamp timestamp = new Timestamp(parsedDate.getTime());
				
				defectSchedule.setScheduleEndDate(timestamp);
			}
			
			CalendarJSON calendar = new CalendarJSON();
			calendar.setId(defectSchedule.getScheduleKey().toString());

			String desc = defectSchedule.getDesc();
			desc = desc.replace("\n", " ").replace("\r", " ");

			Calendar ms_startDate = Calendar.getInstance();
			ms_startDate.setTime(new Date(defectSchedule.getScheduleStartDate().getTime()));

			Calendar ms_endDate = Calendar.getInstance();
			ms_endDate.setTime(new Date(defectSchedule.getScheduleEndDate().getTime()));

			if (defectSchedule.getScheduleTime() == null) {

				// is full day

				calendar.setTitle(messageSource.getMessage("schedule.fullDay.title", null, locale) + " " + desc);
				calendar.setAllDay(true);
				calendar.setStart("00:00:00");
				calendar.setEnd("23:59:00");
				
			} else {

				calendar.setTitle(timeFormat.format(ms_startDate.getTime()) + " " + desc);
				calendar.setAllDay(false);
				calendar.setStart(fullDateTimeFormat.format(ms_startDate.getTime()));
				calendar.setEnd(fullDateTimeFormat.format(ms_endDate.getTime()));

			}

			// if (defectSchedule.getScheduleTime() != null) {
			//
			// String start = timestampToString(
			// defectSchedule.getScheduleTime(), timeFormat);
			// Date end = new Date(
			// defectSchedule.getScheduleTime().getTime() + 30 * 60 * 1000);
			//
			// calendar.setTitle(timestampToString(
			// defectSchedule.getScheduleTime(), timeFormat)
			// + " " + desc);
			// calendar.setStart(start);
			// calendar.setAllDay(false);
			//
			// if (start.contains("23:3") || start.contains("23:4")
			// || start.contains("23:5")) {
			//
			// calendar.setStart("23:30");
			// calendar.setEnd("23:59");
			// } else
			// calendar.setEnd(timestampToString(
			// new Timestamp(end.getTime()), timeFormat));
			// } else {
			// calendar.setTitle(" + desc);
			// calendar.setStart("00:00");
			// calendar.setEnd("23:59");
			// calendar.setAllDay(true);
			// }

			calendar.setDow(CalendarJSON.getDecodedWeekDay(defectSchedule.getFrequency()));

			calendar.setRanges(calendarEvent.getEventRanges(
					defectSchedule.getFrequency(),
					defectSchedule.getScheduleStartDate(),
					defectSchedule.getScheduleEndDate(),
					defectSchedule.getScheduleTime()));

			Frequency f = Frequency.fromInt(CalendarJSON.getDecodedFrequency(defectSchedule.getFrequency()));

			switch (f) {
			case Annually:
				
				calendar.setStart(fullTimeFormat.format(ms_startDate.getTime()));
				calendar.setEnd(fullTimeFormat.format(ms_endDate.getTime()));
				if(calendar.getStart().equals(calendar.getEnd())){
					calendar.setEnd("23:59:00");
				}
				break;
			case Daily:

				calendar.setStart(fullTimeFormat.format(ms_startDate.getTime()));
				calendar.setEnd(fullTimeFormat.format(ms_endDate.getTime()));
//				
				if(calendar.getStart().equals(calendar.getEnd())){
					calendar.setEnd("23:59:00");
				}
				
				break;
			case Monthly:
				
				calendar.setStart(fullTimeFormat.format(ms_startDate.getTime()));
				calendar.setEnd(fullTimeFormat.format(ms_endDate.getTime()));
				if(calendar.getStart().equals(calendar.getEnd())){
					calendar.setEnd("23:59:00");
				}
				break;
			case Once:
				
				calendar.setStart(fullTimeFormat.format(ms_startDate.getTime()));
				calendar.setEnd(fullTimeFormat.format(ms_endDate.getTime()));
				if(calendar.getStart().equals(calendar.getEnd())){
					calendar.setEnd("23:59:00");
				}
				break;
			case Weekly:

				calendar.setStart(fullTimeFormat.format(ms_startDate.getTime()));
				calendar.setEnd(fullTimeFormat.format(ms_endDate.getTime()));
				if(calendar.getStart().equals(calendar.getEnd())){
					calendar.setEnd("23:59:00");
				}
				break;
			default:
				break;

			}

			// List<Ranges> rangeList = new ArrayList<Ranges>();
			//
			// Frequency f =
			// Frequency.fromInt(CalendarJSON.getDecodedFrequency(defectSchedule.getFrequency()));
			//
			// switch (f) {
			// case Annually:
			// Calendar start = Calendar.getInstance();
			// start.setTime(new
			// Date(defectSchedule.getScheduleStartDate().getTime()));
			//
			// Calendar end = Calendar.getInstance();
			// end.setTime(new
			// Date(defectSchedule.getScheduleEndDate().getTime()));
			//
			// for (Calendar st = start; st.getTimeInMillis() <=
			// end.getTimeInMillis(); st.add(Calendar.YEAR, 1)) {
			// rangeList.add(new CalendarJSON().new Ranges(
			// timestampToString(new Timestamp(st.getTimeInMillis()),
			// dateFormat),
			// timestampToString(new Timestamp(st.getTimeInMillis()),
			// dateFormat)));
			// }
			//
			// break;
			// case Monthly:
			//
			// Calendar start2 = Calendar.getInstance();
			// start2.setTime(new
			// Date(defectSchedule.getScheduleStartDate().getTime()));
			//
			// Calendar end2 = Calendar.getInstance();
			// end2.setTime(new
			// Date(defectSchedule.getScheduleEndDate().getTime()));
			//
			// for (Calendar st = start2; st.getTimeInMillis() <=
			// end2.getTimeInMillis(); st.add(Calendar.MONTH, 1)) {
			// rangeList.add(new CalendarJSON().new Ranges(
			// timestampToString(new Timestamp(st.getTimeInMillis()),
			// dateFormat),
			// timestampToString(new Timestamp(st.getTimeInMillis()),
			// dateFormat)));
			// }
			//
			// break;
			//
			// default:
			// rangeList.add(new CalendarJSON().new Ranges(
			// timestampToString(defectSchedule.getScheduleStartDate(),
			// dateFormat),
			// timestampToString(defectSchedule.getScheduleEndDate(),
			// dateFormat)));
			// break;
			//
			// }
			// calendar.setRanges(rangeList);

			List<DefectSchedule> scheduleChildenList = defectScheduleManager.searchDefectScheduleChilden(defectSchedule.getScheduleKey());
			if (scheduleChildenList != null && scheduleChildenList.size() > 0) {

				List<ExcludeDays> excludeDaysList = new ArrayList<ExcludeDays>();
				for (int k = 0; k < scheduleChildenList.size(); k++) {
					excludeDaysList.add(new CalendarJSON().new ExcludeDays(
									timestampToString(scheduleChildenList.get(k).getSkippedStartDate() == null ?
											new Timestamp(0) : scheduleChildenList.get(k).getSkippedStartDate(),dateFormat),
									
									timestampToString(scheduleChildenList.get(k).getSkippedEndDate() == null ? 
											new Timestamp(0) : scheduleChildenList.get(k).getSkippedEndDate(), dateFormat)));

				}
				calendar.setExcludedDates(excludeDaysList);
			}

			json.add(calendar);
		}

		ObjectMapper mapper = new ObjectMapper();
		logger.debug("JSON : " + mapper.writeValueAsString(json));
		return mapper.writeValueAsString(json);
	}

	// TODO: DefectSchedule

	@RequestMapping(value = "/DefectSchedule.do")
	public String DefectSchedule(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		logger.debug("User requests to load defect schedule page.");
		
		String referrerStr = request.getParameter("r");
		String deleteName = "";
		if (request.getParameter("name") != null){
		
			DefectSchedule sch = defectScheduleManager.getScheduleByKey(Integer.parseInt(request.getParameter("name")));
			deleteName = sch.getDesc();
		
//			deleteName = UnicodeToStr(request.getParameter("name"));
		}
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.searchMaintenanceSchedule"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createMaintenanceSchedule"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.removeMaintenanceSchedule"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyMaintenanceSchedule"));

		if (hasPrivilege) {
			DefectScheduleForm defectScheduleForm = new DefectScheduleForm();
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				defectScheduleForm.setSiteKey(siteKey);
			}
			List<UserAccount> userAccountList = new ArrayList<UserAccount>();
			List<UserAccountRole> siteUserAccountRoleList = userManager
					.searchUserAccountRole(currRole.getSiteKey(), null, null,
							null, null);

			if (siteUserAccountRoleList != null) {
				for (UserAccountRole ar : siteUserAccountRoleList) {
					boolean exist = false;
					for (UserAccount a : userAccountList) {
						if (a.getLoginId().equals(
								ar.getUserAccount().getLoginId())) {
							exist = true;
						}
					}
					if (!exist) {
						userAccountList.add(ar.getUserAccount());
					}
				}
			}
			defectScheduleForm.setIsFullDay(false);
			defectScheduleForm.setIsWeekDay(false);
			defectScheduleForm.setFrequency(101);
			defectScheduleForm.setReferrer(referrerStr);
			defectScheduleForm.setDeletedName(deleteName);
			defectScheduleForm.setUserAccountList(userAccountList);
			List<Tool> toolList = new ArrayList<Tool>();
			Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
			Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
			defectScheduleForm.setAvailableLocationTree(locationTree);
			toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());
			Collections.sort(toolList, new ToolComparator());
			toolMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < toolList.size(); i++) {
				toolMap.put(toolList.get(i).getKey(), toolList.get(i).getName()
						.isEmpty() ? toolList.get(i).getCode() : toolList
						.get(i).getCode() + " - " + toolList.get(i).getName());
			}

			List<Role> roleList = getAllAccountRole(currRole.getSiteKey());
			Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
			roleMap.put(-1, "");
			if (roleList != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role role = roleList.get(i);
					roleMap.put(role.getKey(), role.getName());
				}
			}

			model.addAttribute("toolList", toolMap);

			equipmentMap.put(null, pleaseSelect(locale));
			model.addAttribute("equipmentList", equipmentMap);
			model.addAttribute("accountRole", roleMap);
			model.addAttribute("defectScheduleForm", defectScheduleForm);

			return "maintenance/maintenance_defectCalendar";
		} else {
			// user does not have right to access schedule maintenance
			logger.debug("user does not have right to access schedule maintenance ");
			return "common/noprivilege";
		}

	}

	// TODO: DoCreateDefectSchedule

	@RequestMapping(value = "/DoCreateDefectSchedule.do")
	public String doCreateDefectSchedule(
			@ModelAttribute("defectScheduleForm") DefectScheduleForm defectScheduleForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		Integer tempParentKey = 0;

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		List<DefectScheduleAccount> scheduleAccountList = new ArrayList<DefectScheduleAccount>();
		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		List<UserAccount> userAccountList = new ArrayList<UserAccount>();
		List<UserAccountRole> siteUserAccountRoleList = userManager.searchUserAccountRole(currRole.getSiteKey(), null, null, null, null);
		
		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {
				boolean exist = false;
				for (UserAccount a : userAccountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {
					userAccountList.add(ar.getUserAccount());
				}
			}
		}
		defectScheduleForm.setUserAccountList(userAccountList);
		// defectScheduleForm.setSelectedAccountKeyList(defectScheduleForm.getSelectedAccountKeyList());

		List<UserAccount> accountList = this.jsonStringToList(
				defectScheduleForm.getStaffSelected(), UserAccount.class);

		Set<Integer> accountSet = new HashSet<Integer>();
		for (UserAccount ac : accountList) {
			accountSet.add(ac.getKey());
		}

		defectScheduleForm.setSelectedAccountKeyList(accountSet);

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
		defectScheduleForm.setAvailableLocationTree(locationTree);

		if (defectScheduleForm.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(defectScheduleForm.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(defectScheduleForm.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
		}

		toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());

		if (defectScheduleForm.getLocationKey() != null)

			equipmentList = equipmentManager.searchEquipment(null, defectScheduleForm.getLocationKey(), null, null);

		Collections.sort(toolList, new ToolComparator());
		toolMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < toolList.size(); i++) {
			toolMap.put(toolList.get(i).getKey(), toolList.get(i).getName()
					.isEmpty() ? toolList.get(i).getCode() : toolList.get(i)
					.getCode() + " - " + toolList.get(i).getName());
		}

		equipmentMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < equipmentList.size(); i++) {
			equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
					.get(i).getName().isEmpty() ? equipmentList.get(i)
					.getCode() : equipmentList.get(i).getCode() + " - "
					+ equipmentList.get(i).getName());
		}

		List<Role> roleList = getAllAccountRole(currRole.getSiteKey());
		Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
		roleMap.put(-1, "");
		if (roleList != null) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = roleList.get(i);
				roleMap.put(role.getKey(), role.getName());
			}
		}

		model.addAttribute("accountRole", roleMap);
		model.addAttribute("toolList", toolMap);
		model.addAttribute("equipmentList", equipmentMap);
		if (defectScheduleForm.getFrequency() != null) {
			if (defectScheduleForm.getFrequency() == 103)
				defectScheduleForm.setIsWeekDay(true);
			else
				defectScheduleForm.setIsWeekDay(false);
		}
		// validate
		defectScheduleFormValidator.validate(defectScheduleForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			model.addAttribute("selectedStaffJson",
					defectScheduleForm.getStaffSelected());
			model.addAttribute("eventEditStaffJson",
					defectScheduleForm.getStaffSelected());
			return "maintenance/maintenance_defectCalendar";
		}

		DefectSchedule schedule = new DefectSchedule();

		schedule.setSiteKey(currRole.getSiteKey());
		schedule.setDesc(defectScheduleForm.getDescription());
		schedule.setRemarks(defectScheduleForm.getRemarks());

		schedule.setToolKey(defectScheduleForm.getToolKey());
		schedule.setEquipmentKey(defectScheduleForm.getEquipmentKey());
		schedule.setLocationKey(defectScheduleForm.getLocationKey());
		schedule.setFrequency(CalendarJSON.encodeFrequency(
				defectScheduleForm.getFrequency(),
				defectScheduleForm.getWeekDays()));
		
//		System.out.println("****************** start date : *" + defectScheduleForm.getStartDate()+"*" + "|| " + defectScheduleForm.getStartDate().length());
//		System.out.println("****************** end date : *" + defectScheduleForm.getEndDate()+"*"+ "|| " + defectScheduleForm.getEndDate().length());
//		System.out.println("****************** start time : *" + defectScheduleForm.getStartTime()+"*"+ "|| " + defectScheduleForm.getStartTime().length());
//		System.out.println("****************** end time : *" + defectScheduleForm.getEndTime()+"*"+ "|| " + defectScheduleForm.getEndTime().length());
		
		//reset the start date time and end date time
		String startDateTime = defectScheduleForm.getStartDate();
		if(defectScheduleForm.getStartDate().length()>0 && defectScheduleForm.getStartTime().length()>0){
			 startDateTime += " " + defectScheduleForm.getStartTime();
		} else if(defectScheduleForm.getStartDate().length()>0 && defectScheduleForm.getStartTime().isEmpty()){
			 startDateTime += " " + "00:00";
		}
		String endDateTime ="";
		if(!defectScheduleForm.getEndDate().isEmpty() && !defectScheduleForm.getEndTime().isEmpty()){
			 endDateTime = defectScheduleForm.getEndDate() + " " + defectScheduleForm.getEndTime();
		} else if(defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getEndTime().isEmpty()){
			endDateTime = "";
		} else if(!defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getEndTime().isEmpty()) {
			endDateTime = defectScheduleForm.getEndDate() + " " + "23:59";
		} else if(defectScheduleForm.getEndDate().isEmpty() && !defectScheduleForm.getEndTime().isEmpty()){
			endDateTime = "1970-01-01" + " " + defectScheduleForm.getEndTime();
		}
		
		defectScheduleForm.setStartDate(startDateTime);
		defectScheduleForm.setEndDate(endDateTime);
		
		if (!defectScheduleForm.getIsFullDay())
			schedule.setScheduleTime(stringToTimestamp("1970-01-01 " + defectScheduleForm.getStartTime(), dateTimeFormat));
		else
			schedule.setScheduleTime(null);


		schedule.setScheduleStartDate(stringToTimestamp(
				defectScheduleForm.getStartDate(), dateTimeFormat));

		if (!defectScheduleForm.getEndDate().isEmpty()
				|| defectScheduleForm.getEndDate() != null)
			schedule.setScheduleEndDate(stringToTimestamp(
					defectScheduleForm.getEndDate(), dateTimeFormat));
		else
			schedule.setScheduleEndDate(null);

		
		schedule.setParentId(tempParentKey);
		schedule.setCreateBy(account.getKey());
		schedule.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
		schedule.setLastModifyBy(account.getKey());
		schedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
		schedule.setDeleted("N");
		schedule.setSkipped("N");

		Set<Integer> accountKeyList = defectScheduleForm
				.getSelectedAccountKeyList();

		for (Integer accountKey : accountKeyList) {
			DefectScheduleAccount scheduleAccount = new DefectScheduleAccount();
			scheduleAccount.setAccountKey(accountKey);

			scheduleAccount.setSiteKey(currRole.getSiteKey());
			scheduleAccount.setCreateBy(account.getKey());
			scheduleAccount.setCreateDateTime(new Timestamp(System
					.currentTimeMillis()));
			scheduleAccount.setLastModifyBy(account.getKey());
			scheduleAccount.setLastModifyTime(new Timestamp(System
					.currentTimeMillis()));
			scheduleAccount.setDeleted("N");
			scheduleAccountList.add(scheduleAccount);
		}
		defectScheduleManager.saveDefectSchedule(schedule, scheduleAccountList);

		DefectScheduleActionLog viewDefectScheduleActionLog = new DefectScheduleActionLog();
		viewDefectScheduleActionLog.setSiteKey(schedule.getSiteKey());
		viewDefectScheduleActionLog.setActionBy(account.getKey());
		viewDefectScheduleActionLog.setActionDateTime(new Timestamp(System
				.currentTimeMillis()));
		viewDefectScheduleActionLog.setDefectScheduleKey(schedule
				.getScheduleKey());
		viewDefectScheduleActionLog.setActionType("C");
		viewDefectScheduleActionLog.setDesc(schedule.getDesc());
		defectScheduleActionLogManager.saveDefectScheduleActionLog(viewDefectScheduleActionLog);

		return "redirect:DefectSchedule.do?r=c";

	}

	@RequestMapping(value = "/completeSchedule.do")
	public@ResponseBody void completeSchedule(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {
		logger.debug("completeSchedule.do");
		String datastr = request.getParameter("data");
		logger.debug("data : " + datastr);
		
		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		
		String[] data = datastr.split("&");
		String[] keystr = data[0].split("=");
		String[] currentStartstr = data[1].split("=");
		String[] currentEndstr = data[2].split("=");
		Integer key = Integer.parseInt(keystr[1]);
		String currentStart = currentStartstr[1];
		String currentEnd = currentEndstr[1];
		
		//save event status completed or not (1=Not Completed, 2=Completed)
		
		DefectScheduleHistory dsh = new DefectScheduleHistory();		
		dsh.setSiteKey(currRole.getSiteKey());
		
		dsh.setDefectScheduleKey(key);
		dsh.setFinishDateTime(new Timestamp(System.currentTimeMillis()));
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date parsedDate = dateFormat.parse(currentStart);
	    						
	    Timestamp timestamp = new Timestamp(parsedDate.getTime());
		dsh.setTaskDateTime(timestamp);
		
		defectScheduleManager.saveDefectScheduleHistory(dsh);

		DefectSchedule ds = defectScheduleManager.getScheduleByKey(key);
		ds.setFinishDateTime(new Timestamp(System.currentTimeMillis()));
		defectScheduleManager.saveDefectSchedule(ds, null);
		
		request.setCharacterEncoding("utf-8"); 
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache"); 
        
		PrintWriter out = response.getWriter();  
		out.print(messageSource.getMessage("defect.completed", null, locale));
		out.flush();
		out.close();
	}
	
	
	
	// TODO: ModifyCurrentSchedule

	@RequestMapping(value = "/ModifyCurrentSchedule.do")
	public String EditCurrentSchedule(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		String scheduleKeyStr = request.getParameter("key");

		String currentStart = request.getParameter("currentStart");
		String currentEnd = request.getParameter("currentEnd");

		List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();

		if (currentStart != null && currentStart.length() > 0) {
			// currentStart = currentStart.substring(0, 10);
			currentStart = currentStart.replace('T', ' ').substring(0, 16);
		}

		if (currentEnd != null && currentEnd.length() > 0) {
			// currentEnd = currentEnd.substring(0, 10);
			currentEnd = currentEnd.replace('T', ' ').substring(0, 16);
		}

		Integer scheduleKey = Integer.parseInt(scheduleKeyStr);

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		UserAccount account = (UserAccount) session.getAttribute("user");
		DefectScheduleForm defectScheduleForm = new DefectScheduleForm();
		DefectSchedule defectSchedule = defectScheduleManager
				.getScheduleByKey(scheduleKey);
		DefectScheduleActionLog viewDefectScheduleActionLog = new DefectScheduleActionLog();
		viewDefectScheduleActionLog.setSiteKey(defectSchedule.getSiteKey());
		viewDefectScheduleActionLog.setActionBy(account.getKey());
		viewDefectScheduleActionLog.setActionDateTime(new Timestamp(System
				.currentTimeMillis()));
		viewDefectScheduleActionLog.setDefectScheduleKey(defectSchedule
				.getScheduleKey());
		viewDefectScheduleActionLog.setActionType("V");
		viewDefectScheduleActionLog.setDesc(defectSchedule.getDesc());
		defectScheduleActionLogManager
				.saveDefectScheduleActionLog(viewDefectScheduleActionLog);

		defectScheduleForm.setKey(scheduleKey);

		defectScheduleForm.setIsSeries(false);

		List<DefectScheduleAccount> scheduleAccountList = defectScheduleManager
				.getScheduleAccount(scheduleKey);

		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		List<UserAccount> userAccountList = new ArrayList<UserAccount>();

		List<UserAccountRole> siteUserAccountRoleList = userManager
				.searchUserAccountRole(currRole.getSiteKey(), null, null, null,
						null);
		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {
				boolean exist = false;
				for (UserAccount a : userAccountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {
					userAccountList.add(ar.getUserAccount());
				}
			}
		}

		Set<Integer> selectedAccountSet = new HashSet<Integer>();
		for (int i = 0; i < scheduleAccountList.size(); i++) {
			selectedAccountSet.add(scheduleAccountList.get(i).getAccountKey());
			edit_eventStaff.add(scheduleAccountList.get(i).getUserAccount());
		}

		defectScheduleForm.setUserAccountList(userAccountList);
		defectScheduleForm.setSelectedAccountKeyList(selectedAccountSet);

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
		defectScheduleForm.setAvailableLocationTree(locationTree);
		toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());

		if (defectSchedule.getLocationKey() != null)

			equipmentList = equipmentManager.searchEquipment(null,
					defectSchedule.getLocationKey(), null, null);

		Collections.sort(toolList, new ToolComparator());
		toolMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < toolList.size(); i++) {
			toolMap.put(toolList.get(i).getKey(), toolList.get(i).getName()
					.isEmpty() ? toolList.get(i).getCode() : toolList.get(i)
					.getCode() + " - " + toolList.get(i).getName());
		}

		equipmentMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < equipmentList.size(); i++) {
			equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
					.get(i).getName().isEmpty() ? equipmentList.get(i)
					.getCode() : equipmentList.get(i).getCode() + " - "
					+ equipmentList.get(i).getName());
		}

		model.addAttribute("toolList", toolMap);
		model.addAttribute("equipmentList", equipmentMap);

		if (defectSchedule.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(
					defectSchedule.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(
					defectSchedule.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " :	" + locationCode + " - "
					+ locationName);
		}

		defectScheduleForm.setDescription(defectSchedule.getDesc());
		defectScheduleForm.setEquipmentKey(defectSchedule.getEquipmentKey());
		defectScheduleForm.setToolKey(defectSchedule.getToolKey());
		defectScheduleForm.setLocationKey(defectSchedule.getLocationKey());
		defectScheduleForm.setFrequency(101);
		defectScheduleForm.setWeekDays(null);
		defectScheduleForm.setIsWeekDay(false);

		if (defectSchedule.getScheduleTime() != null) {
			defectScheduleForm.setTime(timeString(defectSchedule
					.getScheduleTime()));
			defectScheduleForm.setIsFullDay(false);
		} else {
			defectScheduleForm.setIsFullDay(true);
			defectScheduleForm.setTime("");
		}

		defectScheduleForm.setRemarks(defectSchedule.getRemarks());

		if (currentStart != null && currentStart.length() > 0) {
			defectScheduleForm.setStartDate(currentStart.substring(0, 10));
			defectScheduleForm.setStartTime(currentStart.substring(11));
			defectScheduleForm.setParentStart(currentStart);

		}

		if (currentEnd != null && currentEnd.length() > 0) {

			defectScheduleForm.setEndDate(currentEnd.substring(0, 10));
			if(!defectScheduleForm.getIsFullDay()){
				if(currentEnd.contains("23:59")){
	
					defectScheduleForm.setEndTime("");
				}else{
	
					defectScheduleForm.setEndTime(currentEnd.substring(11));
				}
			} else{
				defectScheduleForm.setEndTime(currentEnd.substring(11));
			}
			defectScheduleForm.setParentEnd(currentEnd);
		}

		String staffSelectedStr = this.listToJsonString(edit_eventStaff);
		defectScheduleForm.setStaffSelected(staffSelectedStr);

		List<Role> roleList = getAllAccountRole(currRole.getSiteKey());
		Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
		roleMap.put(-1, "");
		if (roleList != null) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = roleList.get(i);
				roleMap.put(role.getKey(), role.getName());
			}
		}
		
//		Map<Integer, String> completedMap = new LinkedHashMap<Integer, String>();
//		completedMap.put(-1, "");
//		completedMap.put(1, messageSource.getMessage("defect.not.completed", null, locale));
//		completedMap.put(2, messageSource.getMessage("defect.completed", null, locale));
//		
//		List<DefectScheduleHistory> dshList = defectScheduleManager.getDefectScheduleHistoryByDefectScheduleKey(scheduleKey);	
//		String result = "";
//		if(dshList.size()>0){
//			for(DefectScheduleHistory dsh : dshList){
//				if(dsh!=null){
//					// save taskDateTimes, a DefectScheduleKey may have more than one DefectScheduleHistory records
//					result += getCurrentTimeStamp(dsh.getTaskDateTime()) + ",";
//				}
//			}
//		}
//		
//		String formatEventDate = currentStart.substring(0,10);
//
//		//check the event date whether exist in database
//		boolean done = result.contains(formatEventDate);
//		
//		if(done){
//			defectScheduleForm.setCompletedStatus(2);
//		} else{
//			defectScheduleForm.setCompletedStatus(1);
//		}
//		
//		
//		
//		model.addAttribute("completedMap", completedMap);
		model.addAttribute("accountRole", roleMap);
		model.addAttribute("eventEditStaffJson", staffSelectedStr);
		model.addAttribute("defectScheduleForm", defectScheduleForm);
		return "maintenance/maintenance_editSchedule";
	}

	// TODO: DoModifyCurrentSchedule

	@RequestMapping(value = "/DoModifyCurrentSchedule.do")
	public String doEditSchedule(
			@ModelAttribute("defectScheduleForm") DefectScheduleForm defectScheduleForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		String referrerStr = "m";

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		List<DefectScheduleAccount> scheduleAccountList = new ArrayList<DefectScheduleAccount>();
		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		List<UserAccount> userAccountList = new ArrayList<UserAccount>();
		List<UserAccountRole> siteUserAccountRoleList = userManager.searchUserAccountRole(currRole.getSiteKey(), null, null, null, null);
		
		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {
				boolean exist = false;
				for (UserAccount a : userAccountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {
					userAccountList.add(ar.getUserAccount());
				}
			}
		}
		defectScheduleForm.setUserAccountList(userAccountList);
		// defectScheduleForm.setSelectedAccountKeyList(defectScheduleForm
		// .getSelectedAccountKeyList());

		List<UserAccount> accountList = this.jsonStringToList(defectScheduleForm.getStaffSelected(), UserAccount.class);

		Set<Integer> accountSet = new HashSet<Integer>();
		for (UserAccount ac : accountList) {
			accountSet.add(ac.getKey());
		}

		defectScheduleForm.setSelectedAccountKeyList(accountSet);

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
		defectScheduleForm.setAvailableLocationTree(locationTree);

		if (defectScheduleForm.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(defectScheduleForm.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(defectScheduleForm.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
		}

		toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());

		if (defectScheduleForm.getLocationKey() != null)

			equipmentList = equipmentManager.searchEquipment(null, defectScheduleForm.getLocationKey(), null, null);

		Collections.sort(toolList, new ToolComparator());
		toolMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < toolList.size(); i++) {
			toolMap.put(toolList.get(i).getKey(), toolList.get(i).getName()
					.isEmpty() ? toolList.get(i).getCode() : toolList.get(i)
					.getCode() + " - " + toolList.get(i).getName());
		}

		equipmentMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < equipmentList.size(); i++) {
			equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
					.get(i).getName().isEmpty() ? equipmentList.get(i)
					.getCode() : equipmentList.get(i).getCode() + " - "
					+ equipmentList.get(i).getName());
		}

		List<Role> roleList = getAllAccountRole(currRole.getSiteKey());
		Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
		roleMap.put(-1, "");
		if (roleList != null) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = roleList.get(i);
				roleMap.put(role.getKey(), role.getName());
			}
		}

		model.addAttribute("accountRole", roleMap);
		model.addAttribute("toolList", toolMap);
		model.addAttribute("equipmentList", equipmentMap);
		if(defectScheduleForm.getFrequency()!=null){
		if (defectScheduleForm.getFrequency() == 103)
			defectScheduleForm.setIsWeekDay(true);
		else
			defectScheduleForm.setIsWeekDay(false);
		}

		// validate
		defectScheduleFormValidator.validate(defectScheduleForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			model.addAttribute("eventEditStaffJson", defectScheduleForm.getStaffSelected());

			return "maintenance/maintenance_editSchedule";
		}
		
		// set current defectscheudle as deleted 
		DefectSchedule currentSchedule = defectScheduleManager.getScheduleByKey(defectScheduleForm.getKey());
		if(currentSchedule.getParentId()!=0){
			currentSchedule.setDeleted("Y");
			currentSchedule.setLastModifyBy(account.getKey());
			currentSchedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
			defectScheduleManager.saveDefectSchedule(currentSchedule, null);
		}
		
		DefectSchedule schedule = new DefectSchedule();

		schedule.setSiteKey(currRole.getSiteKey());
		schedule.setDesc(defectScheduleForm.getDescription());
		schedule.setRemarks(defectScheduleForm.getRemarks());

		// if (!defectScheduleForm.getIsFullDay())
		// schedule.setScheduleTime(stringToTimestamp(
		// defectScheduleForm.getTime(), timeFormat));
		// else
		// schedule.setScheduleTime(null);
		
		//reset defectScheduleForm start Date and end Date
		String startDateTime = defectScheduleForm.getStartDate();
		if(defectScheduleForm.getStartDate().length()>0 && defectScheduleForm.getStartTime().length()>0){
			 startDateTime += " " + defectScheduleForm.getStartTime();
		} else if(defectScheduleForm.getStartDate().length()>0 && defectScheduleForm.getStartTime().isEmpty()){
			 startDateTime += " " + "00:00";
		}
		String endDateTime ="";
		if(!defectScheduleForm.getEndDate().isEmpty() && !defectScheduleForm.getEndTime().isEmpty()){
			 endDateTime = defectScheduleForm.getEndDate() + " " + defectScheduleForm.getEndTime();
		} else if(defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getEndTime().isEmpty()){
			endDateTime = "";
		} else if(!defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getEndTime().isEmpty()) {
			endDateTime = defectScheduleForm.getEndDate() + " " + "23:59";
		} else if(defectScheduleForm.getEndDate().isEmpty() && !defectScheduleForm.getEndTime().isEmpty()){
			endDateTime = "1970-01-01" + " " + defectScheduleForm.getEndTime();
		}
		
		defectScheduleForm.setStartDate(startDateTime);
		defectScheduleForm.setEndDate(endDateTime);

		if (!defectScheduleForm.getIsFullDay())
			schedule.setScheduleTime(stringToTimestamp("1970-01-01 " + defectScheduleForm.getStartTime(), dateTimeFormat));
		else
			schedule.setScheduleTime(null);

		schedule.setToolKey(defectScheduleForm.getToolKey());
		schedule.setEquipmentKey(defectScheduleForm.getEquipmentKey());
		schedule.setLocationKey(defectScheduleForm.getLocationKey());
		schedule.setFrequency("101");

		// schedule.setScheduleStartDate(stringToTimestamp(
		// defectScheduleForm.getStartDate(), dateFormat));
		// schedule.setScheduleEndDate(stringToTimestamp(
		// defectScheduleForm.getStartDate(), dateFormat));

		schedule.setScheduleStartDate(stringToTimestamp(defectScheduleForm.getStartDate(), dateTimeFormat));

		if (!defectScheduleForm.getEndDate().isEmpty() || defectScheduleForm.getEndDate() != null)
			schedule.setScheduleEndDate(stringToTimestamp(defectScheduleForm.getEndDate(), dateTimeFormat));
		else
			schedule.setScheduleEndDate(null);
		
		DefectSchedule firstParent = getParentDefectSchedule(currentSchedule);
		schedule.setParentId(firstParent.getScheduleKey());
		schedule.setCreateBy(account.getKey());
		schedule.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
		schedule.setLastModifyBy(account.getKey());
		schedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
		schedule.setDeleted("N");
		schedule.setSkipped("N");

		schedule.setSkippedStartDate(stringToTimestamp(defectScheduleForm.getParentStart(), dateTimeFormat));
		schedule.setSkippedEndDate(stringToTimestamp(defectScheduleForm.getParentEnd(), dateTimeFormat));

		Set<Integer> accountKeyList = defectScheduleForm.getSelectedAccountKeyList();

		for (Integer accountKey : accountKeyList) {
			DefectScheduleAccount scheduleAccount = new DefectScheduleAccount();
			scheduleAccount.setAccountKey(accountKey);
			scheduleAccount.setSiteKey(currRole.getSiteKey());
			scheduleAccount.setCreateBy(account.getKey());
			scheduleAccount.setCreateDateTime(new Timestamp(System
					.currentTimeMillis()));
			scheduleAccount.setLastModifyBy(account.getKey());
			scheduleAccount.setLastModifyTime(new Timestamp(System
					.currentTimeMillis()));
			scheduleAccount.setDeleted("N");
			scheduleAccountList.add(scheduleAccount);
		}

		DefectScheduleActionLog editDefectScheduleActionLog = new DefectScheduleActionLog();
		editDefectScheduleActionLog.setSiteKey(schedule.getSiteKey());
		editDefectScheduleActionLog.setActionBy(account.getKey());
		editDefectScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		editDefectScheduleActionLog.setDefectScheduleKey(schedule.getScheduleKey());
		editDefectScheduleActionLog.setActionType("M");
		editDefectScheduleActionLog.setDesc(schedule.getDesc());

		// defectScheduleManager.deleteDefectScheduleAccount(schedule.getScheduleKey(),
		// account.getKey());
		defectScheduleManager.saveDefectSchedule(schedule, scheduleAccountList);

		defectScheduleActionLogManager.saveDefectScheduleActionLog(editDefectScheduleActionLog);
		
		return "redirect:DefectSchedule.do?r=" + referrerStr;

	}

	// TODO: EditSeriesSchedule

	@RequestMapping(value = "/ModifySeriesSchedule.do")
	public String EditSeriesSchedule(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		String scheduleKeyStr = request.getParameter("key");

		Integer scheduleKey = Integer.parseInt(scheduleKeyStr);

		List<UserAccount> edit_eventStaff = new ArrayList<UserAccount>();

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		UserAccount account = (UserAccount) session.getAttribute("user");
		DefectScheduleForm defectScheduleForm = new DefectScheduleForm();
		DefectSchedule defectSchedule = defectScheduleManager.getScheduleByKey(scheduleKey);

		DefectScheduleActionLog viewDefectScheduleActionLog = new DefectScheduleActionLog();
		viewDefectScheduleActionLog.setSiteKey(defectSchedule.getSiteKey());
		viewDefectScheduleActionLog.setActionBy(account.getKey());
		viewDefectScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		viewDefectScheduleActionLog.setDefectScheduleKey(defectSchedule.getScheduleKey());
		viewDefectScheduleActionLog.setActionType("V");
		viewDefectScheduleActionLog.setDesc(defectSchedule.getDesc());
		defectScheduleActionLogManager.saveDefectScheduleActionLog(viewDefectScheduleActionLog);

		defectScheduleForm.setKey(scheduleKey);

		List<DefectScheduleAccount> scheduleAccountList = defectScheduleManager.getScheduleAccount(scheduleKey);

		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		List<UserAccount> userAccountList = new ArrayList<UserAccount>();

		List<UserAccountRole> siteUserAccountRoleList = userManager
				.searchUserAccountRole(currRole.getSiteKey(), null, null, null,
						null);
		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {
				boolean exist = false;
				for (UserAccount a : userAccountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {
					userAccountList.add(ar.getUserAccount());
				}
			}
		}

		Set<Integer> selectedAccountSet = new HashSet<Integer>();
		for (int i = 0; i < scheduleAccountList.size(); i++) {
			selectedAccountSet.add(scheduleAccountList.get(i).getAccountKey());
			edit_eventStaff.add(scheduleAccountList.get(i).getUserAccount());
		}

		defectScheduleForm.setUserAccountList(userAccountList);
		defectScheduleForm.setSelectedAccountKeyList(selectedAccountSet);

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
		defectScheduleForm.setAvailableLocationTree(locationTree);
		toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());

		if (defectSchedule.getLocationKey() != null)

			equipmentList = equipmentManager.searchEquipment(null, defectSchedule.getLocationKey(), null, null);

		Collections.sort(toolList, new ToolComparator());
		toolMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < toolList.size(); i++) {
			toolMap.put(toolList.get(i).getKey(), toolList.get(i).getName()
					.isEmpty() ? toolList.get(i).getCode() : toolList.get(i)
					.getCode() + " - " + toolList.get(i).getName());
		}

		equipmentMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < equipmentList.size(); i++) {
			equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
					.get(i).getName().isEmpty() ? equipmentList.get(i)
					.getCode() : equipmentList.get(i).getCode() + " - "
					+ equipmentList.get(i).getName());
		}

		model.addAttribute("toolList", toolMap);
		model.addAttribute("equipmentList", equipmentMap);

		if (defectSchedule.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(defectSchedule.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(defectSchedule.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " :	" + locationCode + " - " + locationName);
		}

		defectScheduleForm.setDescription(defectSchedule.getDesc());
		defectScheduleForm.setEquipmentKey(defectSchedule.getEquipmentKey());
		defectScheduleForm.setToolKey(defectSchedule.getToolKey());
		defectScheduleForm.setLocationKey(defectSchedule.getLocationKey());
		defectScheduleForm.setFrequency(CalendarJSON.getDecodedFrequency(defectSchedule.getFrequency()));

		if (CalendarJSON.getDecodedWeekDay(defectSchedule.getFrequency()) != null) {
			defectScheduleForm.setWeekDays(Arrays.asList(CalendarJSON.getDecodedWeekDay(defectSchedule.getFrequency())));

			defectScheduleForm.setIsWeekDay(true);
		} else
			defectScheduleForm.setIsWeekDay(false);

		if (defectSchedule.getScheduleTime() != null) {
			defectScheduleForm.setTime(timeString(defectSchedule.getScheduleTime()));
			defectScheduleForm.setIsFullDay(false);
		} else {
			defectScheduleForm.setIsFullDay(true);
			defectScheduleForm.setTime("");
		}

		defectScheduleForm.setKey(scheduleKey);
		// defectScheduleForm.setStartDate(dateString(defectSchedule
		// .getScheduleStartDate()));
		// defectScheduleForm.setEndDate(dateString(defectSchedule
		// .getScheduleEndDate()));

		defectScheduleForm.setStartDate(dateFormat.format(defectSchedule.getScheduleStartDate()));
		defectScheduleForm.setStartTime(timeFormat.format(defectSchedule.getScheduleStartDate()));
		if(defectSchedule.getScheduleEndDate()!=null){
			if(!new SimpleDateFormat("yyyy-MM-dd").format(defectSchedule.getScheduleEndDate()).equals("1970-01-01")){
				defectScheduleForm.setEndDate(dateFormat.format(defectSchedule.getScheduleEndDate()));
				defectScheduleForm.setEndTime(timeFormat.format(defectSchedule.getScheduleEndDate()));
			} else{
				defectScheduleForm.setEndDate("");
				defectScheduleForm.setEndTime(timeFormat.format(defectSchedule.getScheduleEndDate()));
			}
		} else{
			defectScheduleForm.setEndDate("");
			defectScheduleForm.setEndTime("");
		}
		defectScheduleForm.setRemarks(defectSchedule.getRemarks());
		defectScheduleForm.setIsSeries(true);

		String staffSelectedStr = this.listToJsonString(edit_eventStaff);
		defectScheduleForm.setStaffSelected(staffSelectedStr);

		List<Role> roleList = getAllAccountRole(currRole.getSiteKey());
		Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
		roleMap.put(-1, "");
		if (roleList != null) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = roleList.get(i);
				roleMap.put(role.getKey(), role.getName());
			}
		}

		model.addAttribute("accountRole", roleMap);
		model.addAttribute("eventEditStaffJson", staffSelectedStr);
		model.addAttribute("defectScheduleForm", defectScheduleForm);
		return "maintenance/maintenance_editSchedule";
	}

	// TODO: DoModifyCurrentSchedule

	@RequestMapping(value = "/DoModifySeriesSchedule.do")
	public String DoModifySeriesSchedule(
			@ModelAttribute("defectScheduleForm") DefectScheduleForm defectScheduleForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		String referrerStr = "m";

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		List<UserAccount> userAccountList = new ArrayList<UserAccount>();
		List<UserAccountRole> siteUserAccountRoleList = userManager
				.searchUserAccountRole(currRole.getSiteKey(), null, null, null,
						null);
		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {
				boolean exist = false;
				for (UserAccount a : userAccountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {
					userAccountList.add(ar.getUserAccount());
				}
			}
		}
		defectScheduleForm.setUserAccountList(userAccountList);
		// defectScheduleForm.setSelectedAccountKeyList(defectScheduleForm
		// .getSelectedAccountKeyList());
		List<UserAccount> accountList = this.jsonStringToList(
				defectScheduleForm.getStaffSelected(), UserAccount.class);

		Set<Integer> accountSet = new HashSet<Integer>();
		for (UserAccount ac : accountList) {
			accountSet.add(ac.getKey());
		}

		defectScheduleForm.setSelectedAccountKeyList(accountSet);

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
		defectScheduleForm.setAvailableLocationTree(locationTree);

		if (defectScheduleForm.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(
					defectScheduleForm.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(
					defectScheduleForm.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " : " + locationCode + " - "
					+ locationName);
		}

		toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());

		if (defectScheduleForm.getLocationKey() != null)

			equipmentList = equipmentManager.searchEquipment(null,
					defectScheduleForm.getLocationKey(), null, null);

		Collections.sort(toolList, new ToolComparator());
		toolMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < toolList.size(); i++) {
			toolMap.put(toolList.get(i).getKey(), toolList.get(i).getName()
					.isEmpty() ? toolList.get(i).getCode() : toolList.get(i)
					.getCode() + " - " + toolList.get(i).getName());
		}

		equipmentMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < equipmentList.size(); i++) {
			equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
					.get(i).getName().isEmpty() ? equipmentList.get(i)
					.getCode() : equipmentList.get(i).getCode() + " - "
					+ equipmentList.get(i).getName());
		}

		List<Role> roleList = getAllAccountRole(currRole.getSiteKey());
		Map<Integer, String> roleMap = new LinkedHashMap<Integer, String>();
		roleMap.put(-1, "");
		if (roleList != null) {
			for (int i = 0; i < roleList.size(); i++) {
				Role role = roleList.get(i);
				roleMap.put(role.getKey(), role.getName());
			}
		}

		model.addAttribute("accountRole", roleMap);
		model.addAttribute("toolList", toolMap);
		model.addAttribute("equipmentList", equipmentMap);

		if (defectScheduleForm.getFrequency() == 103)
			defectScheduleForm.setIsWeekDay(true);
		else
			defectScheduleForm.setIsWeekDay(false);

		defectScheduleForm.setIsSeries(true);

		// validate
		defectScheduleFormValidator.validate(defectScheduleForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			model.addAttribute("eventEditStaffJson",
					defectScheduleForm.getStaffSelected());

			return "maintenance/maintenance_editSchedule";
		}

//		DefectSchedule schedule = new DefectSchedule();
		DefectSchedule schedule = defectScheduleManager.getScheduleByKey(defectScheduleForm.getKey());
		List<DefectScheduleAccount> scheduleAccountList = new ArrayList<DefectScheduleAccount>();

		schedule.setSiteKey(currRole.getSiteKey());
		schedule.setDesc(defectScheduleForm.getDescription());
		schedule.setRemarks(defectScheduleForm.getRemarks());

		// if (!defectScheduleForm.getIsFullDay())
		// schedule.setScheduleTime(stringToTimestamp(
		// defectScheduleForm.getTime(), timeFormat));
		// else
		// schedule.setScheduleTime(null);

		//reset defectScheduleForm start Date and end Date
		String startDateTime = defectScheduleForm.getStartDate();
		if(defectScheduleForm.getStartDate().length()>0 && defectScheduleForm.getStartTime().length()>0){
			 startDateTime += " " + defectScheduleForm.getStartTime();
		} else if(defectScheduleForm.getStartDate().length()>0 && defectScheduleForm.getStartTime().isEmpty()){
			 startDateTime += " " + "00:00";
		}
		String endDateTime ="";
		if(!defectScheduleForm.getEndDate().isEmpty() && !defectScheduleForm.getEndTime().isEmpty()){
			 endDateTime = defectScheduleForm.getEndDate() + " " + defectScheduleForm.getEndTime();
		} else if(defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getEndTime().isEmpty()){
			endDateTime = "";
		} else if(!defectScheduleForm.getEndDate().isEmpty() && defectScheduleForm.getEndTime().isEmpty()) {
			endDateTime = defectScheduleForm.getEndDate() + " " + "23:59";
		} else if(defectScheduleForm.getEndDate().isEmpty() && !defectScheduleForm.getEndTime().isEmpty()){
			endDateTime = "1970-01-01" + " " + defectScheduleForm.getEndTime();
		}
		
		defectScheduleForm.setStartDate(startDateTime);
		defectScheduleForm.setEndDate(endDateTime);
		
		
		if (!defectScheduleForm.getIsFullDay())
			schedule.setScheduleTime(stringToTimestamp("1970-01-01 " + defectScheduleForm.getStartTime(), dateTimeFormat));
		else
			schedule.setScheduleTime(null);

		schedule.setToolKey(defectScheduleForm.getToolKey());
		schedule.setEquipmentKey(defectScheduleForm.getEquipmentKey());
		schedule.setLocationKey(defectScheduleForm.getLocationKey());
		schedule.setFrequency(CalendarJSON.encodeFrequency(
				defectScheduleForm.getFrequency(),
				defectScheduleForm.getWeekDays()));

		// schedule.setScheduleStartDate(stringToTimestamp(
		// defectScheduleForm.getStartDate(), dateFormat));
		//
		// if (!defectScheduleForm.getEndDate().isEmpty()
		// || defectScheduleForm.getEndDate() != null)
		// schedule.setScheduleEndDate(stringToTimestamp(
		// defectScheduleForm.getEndDate(), dateFormat));
		// else
		// schedule.setScheduleEndDate(null);

		schedule.setScheduleStartDate(stringToTimestamp(
				defectScheduleForm.getStartDate(), dateTimeFormat));

		if (!defectScheduleForm.getEndDate().isEmpty()
				|| defectScheduleForm.getEndDate() != null)
			schedule.setScheduleEndDate(stringToTimestamp(
					defectScheduleForm.getEndDate(), dateTimeFormat));
		else
			schedule.setScheduleEndDate(null);

		// if (schedule.getFrequency().equals("101"))
		// schedule.setScheduleEndDate(schedule.getScheduleStartDate());

		logger.debug("************ frequency:" + schedule.getFrequency());
		logger.debug("************ scheduleEndDate:"
				+ schedule.getScheduleEndDate());

		schedule.setParentId(0);
		schedule.setCreateBy(account.getKey());
		schedule.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
		schedule.setLastModifyBy(account.getKey());
		schedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
		schedule.setDeleted("N");
		schedule.setSkipped("N");

		//delete the old schedule account
		List<DefectScheduleAccount> dsaList = defectScheduleManager.getScheduleAccount(defectScheduleForm.getKey());
		defectScheduleManager.deleteListOfDefectScheduleAccount(dsaList, account.getKey());
		
		Set<Integer> accountKeyList = defectScheduleForm.getSelectedAccountKeyList();

		for (Integer accountKey : accountKeyList) {
			DefectScheduleAccount scheduleAccount = new DefectScheduleAccount();
			scheduleAccount.setAccountKey(accountKey);
			scheduleAccount.setSiteKey(currRole.getSiteKey());
			scheduleAccount.setCreateBy(account.getKey());
			scheduleAccount.setCreateDateTime(new Timestamp(System
					.currentTimeMillis()));
			scheduleAccount.setLastModifyBy(account.getKey());
			scheduleAccount.setLastModifyTime(new Timestamp(System
					.currentTimeMillis()));
			scheduleAccount.setDeleted("N");
			scheduleAccountList.add(scheduleAccount);
		}

		DefectScheduleActionLog viewDefectScheduleActionLog = new DefectScheduleActionLog();
		viewDefectScheduleActionLog.setSiteKey(schedule.getSiteKey());
		viewDefectScheduleActionLog.setActionBy(account.getKey());
		viewDefectScheduleActionLog.setActionDateTime(new Timestamp(System
				.currentTimeMillis()));
		viewDefectScheduleActionLog.setDefectScheduleKey(schedule
				.getScheduleKey());
		viewDefectScheduleActionLog.setActionType("M");
		viewDefectScheduleActionLog.setDesc(schedule.getDesc());

//		System.out.println(" ----------- Defect Schedule Key : "
//				+ defectScheduleForm.getKey());

//		defectScheduleManager.deleteDefectSchedule(defectScheduleForm.getKey(),
//				account.getKey());
//		defectScheduleManager.deleteDefectScheduleAccount(
//				defectScheduleForm.getKey(), account.getKey());

		Integer insertKey = defectScheduleManager.saveDefectSchedule(schedule, scheduleAccountList);
		
		// List<DefectSchedule> scheduleChildenList = defectScheduleManager
		// .searchDefectScheduleChilden(defectScheduleForm.getKey());
		//
		// for (DefectSchedule ds : scheduleChildenList) {
		// ds.setParentId(insertKey);
		// }
		//
		// defectScheduleManager.saveDefectSchedule(scheduleChildenList);

		defectScheduleActionLogManager.saveDefectScheduleActionLog(viewDefectScheduleActionLog);

		return "redirect:DefectSchedule.do?r=" + referrerStr;

	}

	// TODO: DoDeleteSchedule

	@RequestMapping(value = "/DoDeleteSchedule.do")
	public String DoDeleteSchedule(
			@ModelAttribute("defectScheduleForm") DefectScheduleForm defectScheduleForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException,
			Exception {

		String keyStr = request.getParameter("key");

		Integer key = Integer.parseInt(keyStr);

//		String parentStart = request.getParameter("start");
		String parentStart = defectScheduleForm.getStartDate();
		logger.debug("defectScheduleForm.getStartDate() : " + defectScheduleForm.getStartDate());	
		
		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		DefectSchedule schedule = defectScheduleManager.getScheduleByKey(key);

		DefectScheduleActionLog viewDefectScheduleActionLog = new DefectScheduleActionLog();
		viewDefectScheduleActionLog.setSiteKey(schedule.getSiteKey());
		viewDefectScheduleActionLog.setActionBy(account.getKey());
		viewDefectScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		viewDefectScheduleActionLog.setDefectScheduleKey(schedule.getScheduleKey());
		viewDefectScheduleActionLog.setActionType("D");
		viewDefectScheduleActionLog.setDesc(schedule.getDesc());
		defectScheduleActionLogManager.saveDefectScheduleActionLog(viewDefectScheduleActionLog);
		
		Calendar current = Calendar.getInstance();
		Calendar scheduleStartTime = Calendar.getInstance();
		
		String scheduleStartTimeIndividual = defectScheduleForm.getStartDate() + " " + defectScheduleForm.getStartTime()+ ":00";
		SimpleDateFormat dateFormat4StartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedDate = dateFormat4StartTime.parse(scheduleStartTimeIndividual);
		Timestamp timestamp = new Timestamp(parsedDate.getTime());
		scheduleStartTime.setTime(timestamp);
		
		if(current.before(scheduleStartTime)){
			DefectSchedule newSchedule = new DefectSchedule();
	
			newSchedule.setSiteKey(schedule.getSiteKey());
			newSchedule.setEquipmentKey(schedule.getEquipmentKey());
			newSchedule.setToolKey(schedule.getToolKey());
			newSchedule.setDesc(schedule.getDesc());
			newSchedule.setScheduleStartDate(schedule.getScheduleStartDate());
			newSchedule.setScheduleEndDate(schedule.getScheduleEndDate());
	
			newSchedule.setScheduleTime(schedule.getScheduleTime());
			newSchedule.setFrequency(CalendarJSON.encodeFrequency(CalendarJSON.Frequency.Once.getValue(), null));
			newSchedule.setParentId(schedule.getScheduleKey());
			newSchedule.setCreateBy(account.getKey());
			newSchedule.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
			newSchedule.setLastModifyBy(account.getKey());
			newSchedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
			newSchedule.setSkipped("N");
			newSchedule.setDeleted("Y");
			newSchedule.setSkippedStartDate(stringToTimestamp(parentStart, dateFormat));
			newSchedule.setSkippedEndDate(stringToTimestamp(parentStart, dateFormat));
	
			// List<DefectScheduleAccount> newScheduleAccountList =
			// defectScheduleManager
			// .getScheduleAccount(schedule.getScheduleKey());
			if(schedule.getParentId()>0){
				schedule.setDeleted("Y");
				defectScheduleManager.saveDefectSchedule(schedule, null);
			}
	
			defectScheduleManager.saveDefectSchedule(newSchedule, null);
	
//			return "redirect:DefectSchedule.do?r=d&name=" + StrToUnicode(schedule.getDesc());
			
			return "redirect:DefectSchedule.do?r=d&name=" + schedule.getScheduleKey();
			
		} else{
//			return "redirect:DefectSchedule.do?r=n&name=" + StrToUnicode(schedule.getDesc());
			
			return "redirect:DefectSchedule.do?r=n&name=" + schedule.getScheduleKey();
		}
	}

	// TODO: DoDeleteSeries
	@RequestMapping(value = "/DoDeleteSeries.do")
	public String DoDeleteSeries(
			@ModelAttribute("defectScheduleForm") DefectScheduleForm defectScheduleForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException,
			Exception {

		String keyStr = request.getParameter("key");

		Integer key = Integer.parseInt(keyStr);

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");
			
		DefectSchedule defectSchedule = defectScheduleManager.getScheduleByKey(key);
		List<DefectSchedule> childrenDSList = getAllChildSchedule(key);
		List<DefectSchedule> storeChildrenList = new ArrayList<DefectSchedule>();
		
		//for log
		DefectScheduleActionLog viewDefectScheduleActionLog = new DefectScheduleActionLog();
		viewDefectScheduleActionLog.setSiteKey(defectSchedule.getSiteKey());
		viewDefectScheduleActionLog.setActionBy(account.getKey());
		viewDefectScheduleActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		viewDefectScheduleActionLog.setDefectScheduleKey(defectSchedule.getScheduleKey());
		viewDefectScheduleActionLog.setActionType("D");
		viewDefectScheduleActionLog.setDesc(defectSchedule.getDesc());
		defectScheduleActionLogManager.saveDefectScheduleActionLog(viewDefectScheduleActionLog);
		
		Calendar current = Calendar.getInstance();
		Calendar scheduleStartTime = Calendar.getInstance();
		scheduleStartTime.setTime(new Date(defectSchedule.getScheduleStartDate().getTime()));
		int year100 = Integer.parseInt(new SimpleDateFormat("yyyy").format(defectSchedule.getScheduleStartDate()))+100;
		
		//reset and format the end date
		if(defectSchedule.getScheduleEndDate()!=null){
			if(new SimpleDateFormat("yyyy-MM-dd").format(defectSchedule.getScheduleEndDate()).equals("1970-01-01")){
				String dateforEnd = year100 + "-" + new SimpleDateFormat("MM-dd HH:mm:ss").format(defectSchedule.getScheduleStartDate());
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parsedDate = dateFormat.parse(dateforEnd);
				Timestamp timestamp = new Timestamp(parsedDate.getTime());
				defectSchedule.setScheduleEndDate(timestamp);
//				logger.debug("1.End Time !=null ---> it's 1970. so that the end time should be " + defectSchedule.getScheduleEndDate());
			} else{
				
//				logger.debug("2.End Time !=null ---> the end time should be " + defectSchedule.getScheduleEndDate());
			}
		} else{
			String dateforEnd = year100 + "-" + new SimpleDateFormat("MM-dd").format(defectSchedule.getScheduleStartDate()) + " 23:59:00";
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parsedDate = dateFormat.parse(dateforEnd);
			Timestamp timestamp = new Timestamp(parsedDate.getTime());
			defectSchedule.setScheduleEndDate(timestamp);
//			logger.debug("3.End Time ==null ---> so that the end time should be " + defectSchedule.getScheduleEndDate());
		}
		
		Calendar scheduleEndTime = Calendar.getInstance();
		scheduleEndTime.setTime(new Date(defectSchedule.getScheduleEndDate().getTime()));
		
		// for children
		for(DefectSchedule dsChildren : childrenDSList){
			Calendar childrenStartTime = Calendar.getInstance();
			childrenStartTime.setTime(dsChildren.getScheduleStartDate());
			
			if(current.before(childrenStartTime)){
				dsChildren.setDeleted("Y");
				dsChildren.setScheduleTime(stringToTimestamp("1970-01-01 " + new SimpleDateFormat("HH:mm:ss").format(defectSchedule.getScheduleStartDate()), dateTimeFormat));
			} else{
//				dsChildren.setScheduleEndDate(new Timestamp(System.currentTimeMillis()));
			}
			
			dsChildren.setLastModifyBy(account.getKey());
			dsChildren.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
			storeChildrenList.add(dsChildren);

		}
		defectScheduleManager.saveDefectSchedule(storeChildrenList);

		// for series
		if(current.before(scheduleStartTime) && current.before(scheduleEndTime)){
			
			logger.debug("This is a future event");
			defectScheduleManager.deleteDefectSchedule(defectSchedule.getScheduleKey(), account.getKey());
			defectScheduleManager.deleteDefectScheduleAccount(defectSchedule.getScheduleKey(), account.getKey());
//			return "redirect:DefectSchedule.do?r=d&name=" + StrToUnicode(defectSchedule.getDesc());
			return "redirect:DefectSchedule.do?r=d&name=" + defectSchedule.getScheduleKey();
			
		} else if(current.after(scheduleStartTime) && current.before(scheduleEndTime)){
			logger.debug("current > startTime && current < endTime");
			String endToday = new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(System.currentTimeMillis())) + " " + new SimpleDateFormat("HH:mm:ss").format(defectSchedule.getScheduleEndDate());
			defectSchedule.setScheduleEndDate(stringToTimestamp(endToday, dateTimeFormat));
			defectSchedule.setScheduleTime(stringToTimestamp("1970-01-01 " + new SimpleDateFormat("HH:mm:ss").format(defectSchedule.getScheduleStartDate()), dateTimeFormat));
			defectSchedule.setLastModifyBy(account.getKey());
			defectSchedule.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
			
			String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis()));
			String onlyStartTime = new SimpleDateFormat("yyyy-MM-dd").format(new Timestamp(System.currentTimeMillis())) + " " + new SimpleDateFormat("HH:mm:ss").format(defectSchedule.getScheduleStartDate());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Timestamp currentTimestamp = stringToTimestamp(currentTime,sdf);
			Timestamp onlyStartTimestamp = stringToTimestamp(onlyStartTime, sdf);
			Timestamp onlyEndTimestamp = stringToTimestamp(endToday, sdf);
			
//			logger.debug("1111111111111111111111111 1: " + currentTimestamp);
//			logger.debug("1111111111111111111111111 2: " + onlyStartTimestamp);
//			logger.debug("1111111111111111111111111 3: " + onlyEndTimestamp);
			if(currentTimestamp.before(onlyStartTimestamp) && currentTimestamp.before(onlyEndTimestamp)){
				
				Date date = new Date(defectSchedule.getScheduleEndDate().getTime());
				Date dateMinusOne = new Date(date.getTime() - 24*3600*1000);
				defectSchedule.setScheduleEndDate(new Timestamp(dateMinusOne.getTime()));
			}
				
			defectScheduleManager.saveDefectSchedule(defectSchedule, null);
			
//			return "redirect:DefectSchedule.do?r=df&name=" + StrToUnicode(defectSchedule.getDesc());
			return "redirect:DefectSchedule.do?r=df&name=" + defectSchedule.getScheduleKey();
			
		} else {
			
			logger.debug("It's a totally pass event");
//			return "redirect:DefectSchedule.do?r=n&name=" + StrToUnicode(defectSchedule.getDesc());
			return "redirect:DefectSchedule.do?r=n&name=" + defectSchedule.getScheduleKey();
			
		}
		
	}
	
	@RequestMapping(value = "/GetScheduleEventParentKey.do")
	public @ResponseBody int GetScheduleEventParentKey(@RequestParam("scheduleKey") Integer scheduleKey,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) throws Exception, MFMSException {
			logger.debug("GetScheduleEventParentKey.do ----> originkey = " + scheduleKey);
			
			int parentKey = scheduleKey;
			DefectSchedule ds = defectScheduleManager.getScheduleByKey(scheduleKey);
			if(ds!=null){
				if(ds.getParentId()!=0){
					DefectSchedule dsParent = getParentDefectSchedule(ds);
					if(ds.getFrequency().equals("101") && dsParent.getFrequency().equals("101")){
						parentKey = ds.getScheduleKey();
					} else{
					parentKey = dsParent.getScheduleKey();
					}
				}
			}
			
			logger.debug("GetScheduleEventParentKey.do ----> parentkey = " + parentKey);
			
				return parentKey;
	}
	
	private DefectSchedule getParentDefectSchedule(DefectSchedule ds) {

		DefectSchedule tmp = defectScheduleManager.getScheduleByKey(ds.getParentId());
		if (tmp != null)
			return getParentDefectSchedule(tmp);

		return ds;
	}
	
	@RequestMapping(value = "/GetScheduleEvent.do")
	public @ResponseBody String getScheduleEvent(@RequestParam("scheduleKey") Integer scheduleKey,
			@RequestParam("currentStart") String currentStart, @RequestParam("currentEnd") String currentEnd,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) throws Exception, MFMSException {
		logger.debug("GetScheduleEvent.do");

		List<MaintenanceScheduleJson> msjList = new ArrayList<>();
		
		request.setCharacterEncoding("utf-8"); 
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache"); 
        
		DefectSchedule ds = defectScheduleManager.getScheduleByKey(scheduleKey);
		if(ds!=null){
			MaintenanceScheduleJson msj = new MaintenanceScheduleJson();
			
			msj.setcStartDate(currentStart.replace("T", " "));
			if(currentEnd.length()>0){
				msj.setcEndDate(currentEnd.replace("T", " "));
				
			} else {
				msj.setcEndDate(currentStart.substring(0,10) + " 23:59:00");
			}
			
			if(ds.getLocationKey()!=null){
				Location  l = locationManager.getLocationByKey(ds.getLocationKey());
				msj.setLocationName(l.getName());
			}
		List<String> ac = new ArrayList<String>();
		List<DefectScheduleAccount> dsaList = defectScheduleManager.getScheduleAccount(scheduleKey);
		if(dsaList.size()>0){
			for(DefectScheduleAccount dsa : dsaList){
				ac.add(dsa.getUserAccount().getLoginId() + " - " + dsa.getUserAccount().getName());
			}
		}
		Collections.sort(ac, new StringComparator());
				msj.setAccount(ac);
				msj.setDescription(ds.getDesc());
		
		Equipment e = equipmentManager.getEquipmentByKey(ds.getEquipmentKey());
		if(e!=null){
				msj.setEquipment(e.getCode() + " - " + e.getName());
		}
		
		Tool t = toolManager.getToolByKey(ds.getToolKey());
		if(t!=null){
				msj.setTool(t.getCode() + " - " + t.getName());
		}

				msj.setFrequency(frequencyString(ds.getFrequency(), locale));
				msj.setcFrequency(messageSource.getMessage("schedule.frequency.Once", null, locale));
				msj.setStartDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ds.getScheduleStartDate()));
				
		if(ds.getScheduleEndDate()!=null){
				msj.setEndDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ds.getScheduleEndDate()));
		}
		
		
		List<DefectScheduleHistory> dshList = defectScheduleManager.getDefectScheduleHistoryByDefectScheduleKey(scheduleKey);	
		String result = "";
		if(dshList.size()>0){
			for(DefectScheduleHistory dsh : dshList){
				if(dsh!=null){
					// save taskDateTimes, a DefectScheduleKey may have more than one DefectScheduleHistory records
					result += getCurrentTimeStamp(dsh.getTaskDateTime()) + ",";
				}
			}
		}
		String formatEventDate = currentStart.substring(0,10);

		//check the event date whether exist in database
		boolean done = result.contains(formatEventDate);
		
		if(done){
			msj.setStatus(messageSource.getMessage("defect.completed", null, locale));
		} else{
			msj.setStatus(messageSource.getMessage("defect.not.completed", null, locale));
		}
		
				msj.setRemarks(ds.getRemarks());
				
		if(ds.getScheduleTime()!=null){
				msj.setFullDate(messageSource.getMessage("button.No", null, locale));
		} else{
			msj.setFullDate(messageSource.getMessage("button.Yes", null, locale));
		}
				msjList.add(msj);
		}
		
		
		
		logger.debug(msjList);
		String result =  listToJsonString(msjList);
		
		PrintWriter out = response.getWriter();  
		out.print(result);
		out.flush();
		out.close();

		return null;
	}
	private MaintenanceScheduleJson setBlankTime(DefectSchedule ds, MaintenanceScheduleJson msj){
		if(ds.getScheduleTime()==null){
			logger.debug("It is full day");
			msj.setcStartTime("");
			msj.setcEndTime("");
			msj.setStartTime("");
			msj.setEndTime("");
			
		}
		
		if(ds.getScheduleEndDate()==null){
			logger.debug("There is no end date --> null");
			msj.setcEndTime("");
		}
		
		else if(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleEndDate()).equals("23:59:00")){
			logger.debug("The End Time is 23:59:00");
			msj.setEndTime("");
			msj.setcEndTime("");
		}
		
		return msj;
	}
	
	@RequestMapping(value = "/GetScheduleEvent_1.do")
	public @ResponseBody String getScheduleEvent_1(@RequestParam("scheduleKey") Integer scheduleKey,
			@RequestParam("currentStart") String currentStart, @RequestParam("currentEnd") String currentEnd,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) throws Exception, MFMSException {
		logger.debug("GetScheduleEvent_1.do");

		List<MaintenanceScheduleJson> msjList = new ArrayList<>();
		
		request.setCharacterEncoding("utf-8"); 
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache"); 
        
		DefectSchedule ds = defectScheduleManager.getScheduleByKey(scheduleKey);
		if(ds!=null){
			//defect schedule without parent
			if(ds.getParentId()==0){
				logger.debug("The defect schedule has no parent, and it's defectScheduleKey = " + scheduleKey);
				MaintenanceScheduleJson msjNoParent = setNoParentInfo(ds, currentStart, currentEnd, locale);
				
				MaintenanceScheduleJson fullDateMsj = setBlankTime(ds,msjNoParent);
				

					msjList.add(fullDateMsj);
			} 
			//defect schedule with parent
			else {
				logger.debug("The defect schedule has parent, it's defectScheduleKey = " + scheduleKey + " and it's parent defectScheduleKey = " + ds.getParentId());
				DefectSchedule dsParent = getParentDefectSchedule(ds);
				if(dsParent!=null){
					//if defect schedule is once and its parent is also once
					if(ds.getFrequency().equals("101") && dsParent.getFrequency().equals("101") ){
						MaintenanceScheduleJson msjForOnce = setNoParentInfo(ds, currentStart, currentEnd, locale);

						MaintenanceScheduleJson fullDateMsj = setBlankTime(ds,msjForOnce);
						
						msjList.add(fullDateMsj);
					} else{

						MaintenanceScheduleJson msjWithParent = setWithParentInfo(ds, currentStart, currentEnd, locale);
						
						MaintenanceScheduleJson fullDateMsj = setBlankTime(ds,msjWithParent);
						
						msjList.add(fullDateMsj);
					}
				}
			}
			

	}
		
		logger.debug(msjList);
		String result =  listToJsonString(msjList);
		
		PrintWriter out = response.getWriter();  
		out.print(result);
		out.flush();
		out.close();

		return null;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/eventExportResult.do")
	public @ResponseBody void eventExportResult(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException, Exception {
			logger.debug("eventExportResult.do");
			
			Calendar cal = Calendar.getInstance();
			String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
			String fileName = filePath + "DefectSchedule_" + cal.getTimeInMillis() + ".xlsx";
			File defectScheduleListFile = new File(fileName);
			FileOutputStream defectScheduleListStream = new FileOutputStream(defectScheduleListFile, false);
			ExcelWriter defectScheduleListWriter = new ExcelWriter(defectScheduleListStream);
			
				//header
				List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();
				ColumnDisplay dateCol = new ColumnDisplay();
				dateCol.setProperty("date");
				dateCol.setHeadingKey(messageSource.getMessage("patrol.schedule.date", null, locale));
				
				ColumnDisplay siteCol = new ColumnDisplay();
				siteCol.setProperty("site");
				siteCol.setHeadingKey(messageSource.getMessage("menu.systemMgt.site", null, locale));
				
				ColumnDisplay locationCol = new ColumnDisplay();
				locationCol.setProperty("location");
				locationCol.setHeadingKey(messageSource.getMessage("defect.location", null, locale));
				
				ColumnDisplay accountCol = new ColumnDisplay();
				accountCol.setProperty("account");
				accountCol.setHeadingKey(messageSource.getMessage("patrol.schedule.account", null, locale));
				
				ColumnDisplay descCol = new ColumnDisplay();
				descCol.setProperty("description");
				descCol.setHeadingKey(messageSource.getMessage("defect.description", null, locale));
				
				ColumnDisplay frequencyCol = new ColumnDisplay();
				frequencyCol.setProperty("frequency");
				frequencyCol.setHeadingKey(messageSource.getMessage("patrol.schedule.frequency", null, locale));
				
				ColumnDisplay startCol = new ColumnDisplay();
				startCol.setProperty("start");
				startCol.setHeadingKey(messageSource.getMessage("patrol.report.result.startTime", null, locale));
				
				ColumnDisplay endCol = new ColumnDisplay();
				endCol.setProperty("end");
				endCol.setHeadingKey(messageSource.getMessage("patrol.report.result.endTime", null, locale));
				
				ColumnDisplay equipmentCol = new ColumnDisplay();
				equipmentCol.setProperty("equipment");
				equipmentCol.setHeadingKey(messageSource.getMessage("defect.equipment", null, locale));
				
				ColumnDisplay toolCol = new ColumnDisplay();
				toolCol.setProperty("tool");
				toolCol.setHeadingKey(messageSource.getMessage("defect.tools", null, locale));
				
				ColumnDisplay remarkCol = new ColumnDisplay();
				remarkCol.setProperty("remark");
				remarkCol.setHeadingKey(messageSource.getMessage("patrol.monitor.remark", null, locale));
				
				ColumnDisplay statusCol = new ColumnDisplay();
				statusCol.setProperty("status");
				statusCol.setHeadingKey(messageSource.getMessage("account.status", null, locale));
				
				columnDisplays.add(dateCol);
				columnDisplays.add(siteCol);
				columnDisplays.add(locationCol);
				columnDisplays.add(accountCol);
				columnDisplays.add(descCol);
				columnDisplays.add(frequencyCol);
				columnDisplays.add(startCol);
				columnDisplays.add(endCol);
				columnDisplays.add(equipmentCol);
				columnDisplays.add(toolCol);
				columnDisplays.add(remarkCol);
				columnDisplays.add(statusCol);
				
				//Row content
				List<DefectScheduleExport> exportList = new ArrayList<DefectScheduleExport>();
				
				String[] events = request.getParameter("events").split(",");
				String month = request.getParameter("month");
				List<String> validEvents = new ArrayList<String>();
	
				//get the specified date, the month would like be YYYY-MM, if event date is within the month. it is a valid event.
				for(String s : events){
					if(s.contains(month)){
						validEvents.add(s);
					}
				}

				for(int k=0;k<validEvents.size();k++){
					
					int idx = validEvents.get(k).indexOf("id_");
					int ddx = validEvents.get(k).indexOf("_date_");
					String id = validEvents.get(k).substring(idx+3,ddx);
					int idd = Integer.parseInt(id);
					String date = validEvents.get(k).substring(ddx+6, validEvents.get(k).length()-1);	
					
					DefectSchedule ds = defectScheduleManager.getScheduleByKey(idd);
					DefectScheduleExport export = new DefectScheduleExport();
					export.setDate(date);
					
					Site site = siteManager.getSiteByKey(ds.getSiteKey());
					if(site!=null){
						export.setSite(site.getName());
					}
	
					Location location = locationManager.getLocationByKey(ds.getLocationKey());
					if(location != null){
						export.setLocation(location.getCode() + " - " + location.getName());					
					}
					
					List<String> ac = new ArrayList<String>();
					List<DefectScheduleAccount> dsaList = defectScheduleManager.getScheduleAccount(idd);
					if(dsaList.size()>0){
						for(DefectScheduleAccount dsa : dsaList){
							ac.add(dsa.getUserAccount().getLoginId() + " - " + dsa.getUserAccount().getName());
						}
					}
					Collections.sort(ac, new StringComparator());				
					export.setAccount(ac);
					
					export.setDescription(ds.getDesc());
					export.setFrequency(frequencyString(ds.getFrequency(),locale));
					export.setStart(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleStartDate()));
					
					if(ds.getScheduleEndDate()!=null){
					export.setEnd(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleEndDate()));
					} else{
						export.setEnd("23:59:00");
					}
					Equipment e = equipmentManager.getEquipmentByKey(ds.getEquipmentKey());
					if(e!=null){
						export.setEquipment(e.getCode() + " - " + e.getName());
					}
					
					Tool t = toolManager.getToolByKey(ds.getToolKey());
					if(t!=null){
						export.setTool(t.getCode() + " - " + t.getName());
					}
					
					export.setRemark(ds.getRemarks());
					
					List<DefectScheduleHistory> dshList = defectScheduleManager.getDefectScheduleHistoryByDefectScheduleKey(idd);	
					String result = "";
					if(dshList.size()>0){
						for(DefectScheduleHistory dsh : dshList){
							if(dsh!=null){
								// save taskDateTimes, a DefectScheduleKey may have more than one DefectScheduleHistory records
								result += getCurrentTimeStamp(dsh.getTaskDateTime()) + ",";
							}
						}
					}				
					String formatEventDate = date.substring(0,10);
					//check the event date whether exist in database
					boolean done = result.contains(formatEventDate);
					
					if(done){
						export.setStatus(messageSource.getMessage("defect.completed", null, locale));
					} else{
						export.setStatus(messageSource.getMessage("defect.not.completed", null, locale));
					}
	
					exportList.add(export);
				}
				
				Comparator<DefectScheduleExport> comparatorDate = new dateComparator();
				Comparator<DefectScheduleExport> comparatorTime = new timeComparator();
				ComparatorChain chain = new ComparatorChain();
				chain.addComparator(comparatorDate);
				chain.addComparator(comparatorTime);
				
				Collections.sort(exportList, chain);
				
				defectScheduleListWriter.write(true, 0, "defectSchedule", null, exportList, columnDisplays, false);
	
				defectScheduleListWriter.close();
				
				response.setHeader("Content-Type", "application/vnd.ms-excel");
				response.setHeader("Content-disposition", "inline;filename=\"" + defectScheduleListFile.getName() + "\"");
				
				ServletOutputStream servletOutputStream = response.getOutputStream();
				byte[] b = new byte[1024];
				int i = 0;
				FileInputStream fis = new java.io.FileInputStream(fileName);
				while ((i = fis.read(b)) > 0) {
					servletOutputStream.write(b, 0, i);
				}
				
				fis.close();
	}
	
	class dateComparator implements Comparator<DefectScheduleExport> {
	    public int compare(DefectScheduleExport s1, DefectScheduleExport s2) {
	       return s1.getDate().compareTo(s2.getDate());
	    }
	}
	
	class timeComparator implements Comparator<DefectScheduleExport> {
	    public int compare(DefectScheduleExport s1, DefectScheduleExport s2) {
	       return s1.getStart().compareTo(s2.getStart());
	    }
	}
	
	public String frequencyString(String fs, Locale locale){
		String frequencyToString="";
		
			String[] fy = fs.split("_");

				Frequency f = CalendarJSON.Frequency.fromInt(Integer.parseInt(fy[0]));
					
				switch(f){
				case Annually:
					frequencyToString = messageSource.getMessage("schedule.frequency.Annually", null, locale);
					break;
				case Daily:
					frequencyToString =messageSource.getMessage("schedule.frequency.Daily", null, locale);
					break;
				case Monthly:
					frequencyToString =messageSource.getMessage("schedule.frequency.Monthly", null, locale);
					break;
				case Once:
					frequencyToString =messageSource.getMessage("schedule.frequency.Once", null, locale);
					break;
				case Weekly:
					frequencyToString += messageSource.getMessage("schedule.frequency.Weekly", null, locale) + ": ";
					if(fy.length>1){
						for(String b : fy[1].split(",")){
							if(b.equals("1")) frequencyToString += messageSource.getMessage("schedule.frequency.Monday", null, locale) + ", ";
							else if(b.equals("2")) frequencyToString += messageSource.getMessage("schedule.frequency.Tuesday", null, locale) + ", ";
							else if(b.equals("3")) frequencyToString += messageSource.getMessage("schedule.frequency.Wednesday", null, locale) + ", ";
							else if(b.equals("4")) frequencyToString += messageSource.getMessage("schedule.frequency.Thursday", null, locale) + ", ";
							else if(b.equals("5")) frequencyToString += messageSource.getMessage("schedule.frequency.Friday", null, locale) + ", ";
							else if(b.equals("6")) frequencyToString += messageSource.getMessage("schedule.frequency.Saturday", null, locale) + ", ";
							else if(b.equals("0")) frequencyToString += messageSource.getMessage("schedule.frequency.Sunday", null, locale) + ", ";						
						}
						frequencyToString =frequencyToString.substring(0, frequencyToString.length()-2);
					}

					break;
				default:
					break;

				}
				return frequencyToString;
				
		}
		
	@RequestMapping(value = "/CheckFinish.do")
	public @ResponseBody Set<String> CheckFinish(@RequestParam(value="event[]") List<String> event, HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException, Exception {
		
					logger.debug("CheckFinish.do");
					Set<Integer> scheduleKeyList = new HashSet<>();
					Set<String> dateList = new HashSet<>();
					Set<String> hs = new HashSet<>();

					for(int i = 0; i<event.size();i++){
						
						int idx = event.get(i).indexOf("id_");
						int ddx = event.get(i).indexOf("_date_");
						String id = event.get(i).substring(idx+3,ddx);
						int idd = Integer.parseInt(id);
						String date = event.get(i).substring(ddx+6,event.get(i).length()-1);					
						scheduleKeyList.add(idd);
						dateList.add(date);
						
					}
					logger.debug("scheduleKeyList size : " + scheduleKeyList.size());
					logger.debug("dateList size : " + dateList.size());
					for(Integer key : scheduleKeyList){
						List<DefectScheduleHistory> dshList = defectScheduleManager.getDefectScheduleHistoryByDefectScheduleKey(key);
						for(String date : dateList){
//						logger.debug("date : " + date);
						if(dshList.size()>0){
							for(DefectScheduleHistory dsh : dshList){
								if(dsh!=null){
									String dshTargetTime = getCurrentTimeStamp(dsh.getTaskDateTime());
									boolean check = date.equals(dshTargetTime);
									
									if(check){

										hs.add("event_id_"+key+"_date_"+date+"_");
									}
								}
							}
						}	
					}
				}
					
					return hs;
			}

	public String getCurrentTimeStamp(Timestamp t) {	
	    return new SimpleDateFormat("yyyy-MM-dd").format(t);
	}
	
	private <T> String listToJsonString(List<T> json) {
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
				true);
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

	public List<Role> getAllAccountRole(int siteKey) {
		try {
			return roleManager.searchRole(siteKey, null, null, "N", false);
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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

	public static String UnicodeToStr(String source) throws Exception {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (i < source.length()) {
			if (source.charAt(i) == '\\') {
				int j = Integer.parseInt(source.substring(i + 2, i + 6), 16);
				sb.append((char) j);
				i += 6;
			} else {
				sb.append(source.charAt(i));
				i++;
			}
		}
		return sb.toString();
	}

	public String dateString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String timeString = sdf.format(ts);
			return timeString;
		} else
			return null;
	}

	public String timeString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String timeString = sdf.format(ts);
			return timeString;
		} else
			return null;
	}

	public String pleaseSelect(Locale locale) {
		return messageSource.getMessage("common.pleaseSelect", null, locale);
	}

	private Timestamp stringToTimestamp(String date, SimpleDateFormat format)
			throws ParseException {

		if (!date.isEmpty())
			return new Timestamp(format.parse(date).getTime());
		else
			return null;
	}

	private String timestampToString(Timestamp time, SimpleDateFormat format) {
		return format.format(new Date(time.getTime()));
	}

	public DefectScheduleManager getDefectScheduleManager() {
		return defectScheduleManager;
	}

	public void setDefectScheduleManager(
			DefectScheduleManager defectScheduleManager) {
		this.defectScheduleManager = defectScheduleManager;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public SimpleDateFormat getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(SimpleDateFormat timeFormat) {
		this.timeFormat = timeFormat;
	}

	public static Logger getLogger() {
		return logger;
	}

	public DefectScheduleFormValidator getDefectScheduleFormValidator() {
		return defectScheduleFormValidator;
	}

	public void setDefectScheduleFormValidator(
			DefectScheduleFormValidator defectScheduleFormValidator) {
		this.defectScheduleFormValidator = defectScheduleFormValidator;
	}

	public EquipmentManager getEquipmentManager() {
		return equipmentManager;
	}

	public void setEquipmentManager(EquipmentManager equipmentManager) {
		this.equipmentManager = equipmentManager;
	}

	public UserAccountManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserAccountManager userManager) {
		this.userManager = userManager;
	}

	public DefectScheduleActionLogManager getDefectScheduleActionLogManager() {
		return defectScheduleActionLogManager;
	}

	public void setDefectScheduleActionLogManager(
			DefectScheduleActionLogManager defectScheduleActionLogManager) {
		this.defectScheduleActionLogManager = defectScheduleActionLogManager;
	}

	private LocationTreeNode getLocationTreeNodeFormSession(
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		LocationTreeNode locationTree = (LocationTreeNode) session
				.getAttribute("locationTree");

		return locationTree;
	}

	private <T> List<T> jsonStringToList(String jsonString, Class<T> clazz) {

		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
				true);

		List<T> json = new ArrayList<T>();

		// deal with the "\"
		if (jsonString != null) {
			jsonString = jsonString.replaceAll("\\\\", "\\\\\\\\");
		} else {
			jsonString = "";
		}
		if (!jsonString.equals("")) {

			try {
				json = mapper.readValue(jsonString, mapper.getTypeFactory()
						.constructCollectionType(List.class, clazz));
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
	
	
	
	public MaintenanceScheduleJson setNoParentInfo(DefectSchedule ds, String currentStart, String currentEnd, Locale locale) throws MFMSException{
		Integer scheduleKey = ds.getScheduleKey();
		
		MaintenanceScheduleJson msj = new MaintenanceScheduleJson();
		msj.setcStartDate(currentStart.substring(0,10));
		msj.setcStartTime(currentStart.substring(11));
		
		if(!currentEnd.isEmpty()){
			msj.setcEndDate(currentEnd.substring(0,10));
			msj.setcEndTime(currentEnd.substring(11));
		} else {
			msj.setcEndDate(currentStart.substring(0,10));
			msj.setcEndTime("23:59");
		}
		
		if(ds.getLocationKey()!=null){
			Location  l = locationManager.getLocationByKey(ds.getLocationKey());
			msj.setLocationName(l.getName());
			msj.setcLocationName(l.getName());
		}
	List<String> ac = new ArrayList<String>();
	List<DefectScheduleAccount> dsaList = defectScheduleManager.getScheduleAccount(scheduleKey);
	if(dsaList.size()>0){
		for(DefectScheduleAccount dsa : dsaList){
			ac.add(dsa.getUserAccount().getLoginId() + " - " + dsa.getUserAccount().getName());
		}
	}
	Collections.sort(ac, new StringComparator());
			msj.setAccount(ac);
			msj.setcAccount(ac);
			msj.setDescription(ds.getDesc());
			msj.setcDescription(ds.getDesc());
	
	Equipment e = equipmentManager.getEquipmentByKey(ds.getEquipmentKey());
	if(e!=null){
			msj.setEquipment(e.getCode() + " - " + e.getName());
			msj.setcEquipment(e.getCode() + " - " + e.getName());
	}
	
	Tool t = toolManager.getToolByKey(ds.getToolKey());
	if(t!=null){
			msj.setTool(t.getCode() + " - " + t.getName());
			msj.setcTool(t.getCode() + " - " + t.getName());
	}

			msj.setFrequency(frequencyString(ds.getFrequency(), locale));
			msj.setcFrequency(messageSource.getMessage("schedule.frequency.Once", null, locale));
			msj.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(ds.getScheduleStartDate()));
			msj.setStartTime(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleStartDate()));
			
	if(ds.getScheduleEndDate()!=null){
		if(!new SimpleDateFormat("yyyy-MM-dd").format(ds.getScheduleEndDate()).equals("1970-01-01")){
			msj.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(ds.getScheduleEndDate()));
		} else{
			msj.setEndDate("");
		}
			msj.setEndTime(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleEndDate()));
	}
	
	
	List<DefectScheduleHistory> dshList = defectScheduleManager.getDefectScheduleHistoryByDefectScheduleKey(scheduleKey);	
	String result = "";
	if(dshList.size()>0){
		for(DefectScheduleHistory dsh : dshList){
			if(dsh!=null){
				// save taskDateTimes, a DefectScheduleKey may have more than one DefectScheduleHistory records
				result += getCurrentTimeStamp(dsh.getTaskDateTime()) + ",";
			}
		}
	}
	String formatEventDate = currentStart.substring(0,10);

	//check the event date whether exist in database
	boolean done = result.contains(formatEventDate);
	
	if(done){
		msj.setStatus(messageSource.getMessage("defect.completed", null, locale));
	} else{
		msj.setStatus(messageSource.getMessage("defect.not.completed", null, locale));
	}
			msj.setRemarks(ds.getRemarks());
			msj.setcRemarks(ds.getRemarks());
	if(ds.getScheduleTime()!=null){
			msj.setFullDate(messageSource.getMessage("button.No", null, locale));
			msj.setcFullDate(messageSource.getMessage("button.No", null, locale));
	} else{
		msj.setFullDate(messageSource.getMessage("button.Yes", null, locale));
		msj.setcFullDate(messageSource.getMessage("button.Yes", null, locale));
	}
		
		return msj;
	}
	
	public MaintenanceScheduleJson setWithParentInfo(DefectSchedule ds, String currentStart, String currentEnd, Locale locale) throws MFMSException{
		
		DefectSchedule dsParent = getParentDefectSchedule(ds);
		Integer scheduleKey = ds.getScheduleKey();
		MaintenanceScheduleJson msj = new MaintenanceScheduleJson();
		
		//for location
		if(dsParent.getLocationKey()!=null){
			Location lParent = locationManager.getLocationByKey(dsParent.getLocationKey());
			msj.setLocationName(lParent.getName());
		}
		if(ds.getLocationKey()!=null){
			Location  l = locationManager.getLocationByKey(ds.getLocationKey());
			msj.setcLocationName(l.getName());
		}
		// for account
		List<String> ac = new ArrayList<String>();
		List<String> acParent = new ArrayList<String>();
		List<DefectScheduleAccount> dsaList = defectScheduleManager.getScheduleAccount(scheduleKey);
		List<DefectScheduleAccount> dsaParentList = defectScheduleManager.getScheduleAccount(ds.getParentId());
		
		if(dsaList.size()>0){
			for(DefectScheduleAccount dsa : dsaList){
				ac.add(dsa.getUserAccount().getLoginId() + " - " + dsa.getUserAccount().getName());
			}
		}
		Collections.sort(ac, new StringComparator());
		
		if(dsaParentList.size()>0){
			for(DefectScheduleAccount dsaParent : dsaParentList){
				acParent.add(dsaParent.getUserAccount().getLoginId() + " - " + dsaParent.getUserAccount().getName());
			}
		}
		Collections.sort(acParent, new StringComparator());
				msj.setAccount(acParent);
				msj.setcAccount(ac);
		
		// for description
		msj.setDescription(dsParent.getDesc());
		msj.setcDescription(ds.getDesc());
		// for equipment
		Equipment e = equipmentManager.getEquipmentByKey(ds.getEquipmentKey());
		Equipment eParent = equipmentManager.getEquipmentByKey(dsParent.getEquipmentKey());
		if(eParent!=null)
		msj.setEquipment(eParent.getCode() + " - " + eParent.getName());
		if(e!=null)
		msj.setcEquipment(e.getCode() + " - " + e.getName());	
		//for Tool
		Tool t = toolManager.getToolByKey(ds.getToolKey());
		Tool tParent = toolManager.getToolByKey(dsParent.getToolKey());
		if(tParent!=null)
		msj.setTool(tParent.getCode() + " - " + tParent.getName());
		if(t!=null)
		msj.setcTool(t.getCode() + " - " + t.getName());
		//for frequency
		msj.setFrequency(frequencyString(dsParent.getFrequency(), locale));
		msj.setcFrequency(messageSource.getMessage("schedule.frequency.Once", null, locale));
		//for start date
		msj.setStartDate(new SimpleDateFormat("yyyy-MM-dd").format(dsParent.getScheduleStartDate()));
		if(dsParent.getScheduleTime()!=null){
		msj.setStartTime(new SimpleDateFormat("HH:mm:ss").format(dsParent.getScheduleStartDate()));
		} else{
			msj.setStartTime("");
		}
		msj.setcStartDate(new SimpleDateFormat("yyyy-MM-dd").format(ds.getScheduleStartDate()));
		msj.setcStartTime(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleStartDate()));
		//for end date
		//parent defect schedule
		if(dsParent.getScheduleEndDate()!=null){
			if(!new SimpleDateFormat("yyyy-MM-dd").format(dsParent.getScheduleEndDate()).equals("1970-01-01")){
				msj.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(dsParent.getScheduleEndDate()));
			} else{
				msj.setEndDate("");
			}
			msj.setEndTime(new SimpleDateFormat("HH:mm:ss").format(dsParent.getScheduleEndDate()));
		}else{
			msj.setEndDate("");
			msj.setEndTime("");
		}
		
		if(dsParent.getScheduleTime()==null){
			msj.setEndTime("");
		}
		//current defect schedule
		if(ds.getScheduleEndDate()!=null){
			msj.setcEndDate(new SimpleDateFormat("yyyy-MM-dd").format(ds.getScheduleEndDate()));
			msj.setcEndTime(new SimpleDateFormat("HH:mm:ss").format(ds.getScheduleEndDate()));
		}else{
			msj.setcEndDate("");
			msj.setcEndTime("");
		}
		//for full day
		if(dsParent.getScheduleTime()!=null){
			msj.setFullDate(messageSource.getMessage("button.No", null, locale));
		} else{msj.setFullDate(messageSource.getMessage("button.Yes", null, locale));}	
		
		if(ds.getScheduleTime()!=null){
			msj.setcFullDate(messageSource.getMessage("button.No", null, locale));
		} else{msj.setcFullDate(messageSource.getMessage("button.Yes", null, locale));}		
		//for remark
		msj.setRemarks(dsParent.getRemarks());
		msj.setcRemarks(ds.getRemarks());
		//for status
		List<DefectScheduleHistory> dshList = defectScheduleManager.getDefectScheduleHistoryByDefectScheduleKey(scheduleKey);	
		String resultParent = "";
		if(dshList.size()>0){
			for(DefectScheduleHistory dsh : dshList){
				if(dsh!=null){
					// save taskDateTimes, a DefectScheduleKey may have more than one DefectScheduleHistory records
					resultParent += getCurrentTimeStamp(dsh.getTaskDateTime()) + ",";
				}
			}
		}
		String formatEventDate = currentStart.substring(0,10);

		//check the event date whether exist in database
		boolean done = resultParent.contains(formatEventDate);
		
		if(done){
			msj.setStatus(messageSource.getMessage("defect.completed", null, locale));
		} else{
			msj.setStatus(messageSource.getMessage("defect.not.completed", null, locale));
		}
		
		
		return msj;
	}
	
	private List<DefectSchedule> getAllChildSchedule(Integer scheduleKey) {

		List<DefectSchedule> list = new ArrayList<DefectSchedule>();

		List<DefectSchedule> tmp = defectScheduleManager.searchDefectScheduleChilden(scheduleKey);

		if (tmp != null && !tmp.isEmpty()) {

			for (DefectSchedule ps : tmp) {
				list.add(ps);
				list.addAll(getAllChildSchedule(ps.getScheduleKey()));
			}
		}

		return list;

	}

}
