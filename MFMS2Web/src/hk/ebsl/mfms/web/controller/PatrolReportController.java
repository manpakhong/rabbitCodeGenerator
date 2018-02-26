package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolResult.PatrolStatus;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.PatrolResultFormJSON;
import hk.ebsl.mfms.json.PatrolResultFormJSON.ReportType;
import hk.ebsl.mfms.json.PatrolRouteFormJSON;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.Patrol_Route;
import hk.ebsl.mfms.report.JasperReportService;
import hk.ebsl.mfms.report.template.object.ImageTest;
import hk.ebsl.mfms.report.template.object.PatrolResultReport;
import hk.ebsl.mfms.report.template.object.PatrolRouteReport;
import hk.ebsl.mfms.report.template.object.PatrolStaffReport;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class PatrolReportController {

	static class ModelMappingValue {
		public static final String pages_patrolReport = "patrol/report/patrolReport";
		public static final String pages_patrolStaffReport = "patrol/report/patrolStaffReport";
		public static final String pages_patrolResultReport = "patrol/report/patrolResultReport";
		public static final String pages_patrolRouteReport = "patrol/report/patrolRouteReport";

		public static final String pages_view_selectReportDate = "view_selectReportDate";

		public static final String var_route = "route";
		public static final String var_patrolStaff = "patrolStaff";
		public static final String var_reportType = "reportType";
	}

	@Autowired
	private StatusManager statusManager;
	@Autowired
	private LocationManager locationManager;
	@Autowired
	private RouteDefManager routeDefManager;
	@Autowired
	private UserAccountManager userAccountManager;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private Properties privilegeMap;
	@Autowired
	private PatrolResultManager patrolResultManager;
	@Autowired
	private MessageSource messageSource;

	@Autowired
	JasperReportService jasper;

	public final static Logger logger = Logger
			.getLogger(PatrolReportController.class);

	SimpleDateFormat startTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**                      **/
	/** Function for Reports **/
	/**                      **/

	@RequestMapping(value = "/PatrolStaffReport.do")
	public String PatrolStaffReport(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load patrol staff report page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.patrolStaffReport"));

		if (hasPrivilege) {
			model.addAttribute(ModelMappingValue.var_reportType,
					this.getReportTypeMap(locale));
			model.addAttribute(ModelMappingValue.var_route,
					this.getRoute(currRole.getSiteKey()));
			model.addAttribute(ModelMappingValue.var_patrolStaff,
					this.getPatrolStaff(currRole.getSiteKey()));

			return ModelMappingValue.pages_patrolStaffReport;
		} else {
			// user does not have right to patrolStaffReport
			logger.debug("user does not have right to patrolStaffReport ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/PatrolResultReport.do")
	public String PatrolResultReport(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load patrol result report page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.patrolResultReport"));

		if (hasPrivilege) {
			model.addAttribute(ModelMappingValue.var_reportType,
					this.getReportTypeMap(locale));
			model.addAttribute(ModelMappingValue.var_route,
					this.getRoute(currRole.getSiteKey()));
			model.addAttribute(ModelMappingValue.var_patrolStaff,
					this.getPatrolStaff(currRole.getSiteKey()));

			return ModelMappingValue.pages_patrolResultReport;
		} else {
			// user does not have right to patrolResultReport
			logger.debug("user does not have right to patrolResultReport ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/PatrolRouteReport.do")
	public String PatrolRouteReport(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load patrol route report page.");
		
		Role currRole = (null == request.getSession().getAttribute("currRole")) ? null
				: (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.patrolRouteReport"));

		if (hasPrivilege) {
			model.addAttribute(ModelMappingValue.var_reportType,
					this.getReportTypeMap(locale));
			model.addAttribute(ModelMappingValue.var_route,
					this.getRoute(currRole.getSiteKey()));

			return ModelMappingValue.pages_patrolRouteReport;
		} else {
			// user does not have right to patrolRouteReport
			logger.debug("user does not have right to patrolRouteReport ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/GetPatrolStaffReport.do", method = RequestMethod.GET)
	public void GetPatrolStaffReport(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale,
			@RequestParam("exportType") String exportType)
			throws UnsupportedEncodingException, MFMSException {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("Report_Title", messageSource.getMessage(
				"patrol.report.staff.title", null, locale));
		parameters.put("Column_AccountID", messageSource.getMessage(
				"patrol.report.staff.username", null, locale));
		parameters.put("Column_AccountName", messageSource.getMessage(
				"patrol.report.staff.name", null, locale));
		parameters.put("Column_Email", messageSource.getMessage(
				"patrol.report.staff.email", null, locale));
		parameters.put("Column_Tel", messageSource.getMessage(
				"patrol.report.staff.tel", null, locale));
		parameters.put("Column_Status", messageSource.getMessage(
				"patrol.report.staff.status", null, locale));
		// parameters.put("Column_TagID", messageSource.getMessage(
		// "patrol.report.staff.tagID", null, locale));

		parameters.put("labelTagID", messageSource.getMessage(
				"patrol.report.staff.tagID", null, locale));
		parameters.put("labelPrintDate",
				messageSource.getMessage("report.print.date", null, locale)
						+ " : ");
		parameters.put("labelPage",
				messageSource.getMessage("report.page", null, locale));
		parameters.put("labelOf",
				messageSource.getMessage("report.of", null, locale));

		Object[] data = getPatrolStaffObjectData(
				this.getRoleFromSession(request).getSiteKey(), locale);

		if (data == null || data.length == 0) {
			data = new Object[1];
			data[0] = new PatrolStaffReport();
		}

		String filePath = jasper.getExportFile(exportType,
				"PatrolStaff_Report", "Patrol Staff", parameters, data);

		// if(exportType.equals("pdf")){
		// filePath = jasper.executeReportToPdf("PatrolStaff_Report",
		// "Patrol Staff", parameters, data);
		// }
		// if(exportType.equals("xls")){
		// filePath =
		// jasper.executeReportToExcel("PatrolStaff_Report","Patrol Staff",
		// parameters, data);
		// }

		jasper.outPutFile(exportType, filePath, response);

	}

	@RequestMapping(value = "GetPatrolResultReport.do")
	// @ResponseStatus(value = HttpStatus.OK)
	public void GetPatrolResultReport(@RequestParam("json") String json,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) {

		System.out.println("GetPatrolResultReport");
		System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();

		try {
			PatrolResultFormJSON jsonObject = mapper.readValue(json,
					PatrolResultFormJSON.class);

			String reportTitle = messageSource.getMessage(
					"patrol.report.result.title", null, locale)
					+ " - "
					+ messageSource.getMessage("patrol.report.type."
							+ jsonObject.getType(), null, locale);

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("Report_Title", reportTitle);
			parameters.put("Label_PersonAttended", messageSource.getMessage(
					"patrol.report.result.personAttended", null, locale));
			parameters.put("Label_StartTime", messageSource.getMessage(
					"patrol.report.result.startTime", null, locale));
			parameters.put("Label_RouteCode", messageSource.getMessage(
					"patrol.report.result.routeCode", null, locale));
			parameters.put("Label_RouteName", messageSource.getMessage(
					"patrol.report.result.routeName", null, locale));
			parameters.put("Label_TotalTime", messageSource.getMessage(
					"patrol.report.result.totalTime", null, locale));
			parameters.put("Column_TimeAttended", messageSource.getMessage(
					"patrol.report.result.timeAttended", null, locale));
			parameters.put("Column_Duration", messageSource.getMessage(
					"patrol.report.result.duration", null, locale));
			parameters.put("Column_SeqNum", messageSource.getMessage(
					"patrol.report.result.seqNum", null, locale));
			parameters.put("Column_LocationCode", messageSource.getMessage(
					"patrol.report.result.locationCode", null, locale));
			parameters.put("Column_LocationName", messageSource.getMessage(
					"patrol.report.result.locationName", null, locale));
			parameters.put("Column_Result", messageSource.getMessage(
					"patrol.report.result.result", null, locale));
			parameters.put("Column_Speed", messageSource.getMessage(
					"patrol.report.result.speed", null, locale));
			parameters.put("Msg", "");
			parameters.put("FreeRoute", messageSource.getMessage(
					"patrol.report.result.freeRoute", null, locale));
			parameters.put("TotalRouteNumber", messageSource.getMessage(
					"patrol.report.result.totalRouteNumber", null, locale));
			parameters.put("TotalCorrectRouteNumber", messageSource.getMessage(
					"patrol.report.result.totalCorrectRouteNumber", null,
					locale));
			parameters.put("TotalIncorrectRouteNumber", messageSource
					.getMessage(
							"patrol.report.result.totalIncorrectRouteNumber",
							null, locale));
			parameters.put("TotalManagerRouteNumber", messageSource.getMessage(
					"patrol.report.result.totalManagerRouteNumber", null,
					locale));
			parameters.put("labelPrintDate",
					messageSource.getMessage("report.print.date", null, locale)
							+ " : ");
			parameters.put("labelPage",
					messageSource.getMessage("report.page", null, locale));
			parameters.put("labelOf",
					messageSource.getMessage("report.of", null, locale));

			Object[] data = getPatrolResultObjectData(jsonObject, this
					.getRoleFromSession(request).getSiteKey(), locale);

			if (data == null || data.length == 0) {
				data = new Object[1];
				data[0] = new PatrolResultReport();
			}

			String filePath = jasper.getExportFile(jsonObject.getExportType(),
					"PatrolResult_Report", "Patrol Result", parameters, data);

			jasper.outPutFile(jsonObject.getExportType(), filePath, response);
			// String pdf = jasper.executeReportToPdf("PatrolResult_Report",
			// "Patrol Result", parameters, data);

			// jasper.outPutFile("", pdf, response);

			System.out.println(filePath);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "GetPatrolRouteReport.do")
	// @ResponseStatus(value = HttpStatus.OK)
	public void GetPatrolRouteReport(@RequestParam("json") String json,
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) {

		System.out.println("GetPatrolRouteReport");
		System.out.println(json);

		ObjectMapper mapper = new ObjectMapper();

		try {
			PatrolRouteFormJSON jsonObject = mapper.readValue(json,
					PatrolRouteFormJSON.class);

			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("Report_Title", messageSource.getMessage(
					"patrol.report.route.title", null, locale));
			parameters.put("Label_RouteCode", messageSource.getMessage(
					"patrol.report.route.routeCode", null, locale));
			parameters.put("Label_RouteName", messageSource.getMessage(
					"patrol.report.route.routeName", null, locale));
			parameters.put("Column_SeqNum", messageSource.getMessage(
					"patrol.report.route.seqNum", null, locale));
			parameters.put("Column_LocationCode", messageSource.getMessage(
					"patrol.report.route.locationCode", null, locale));
			parameters.put("Column_LocationName", messageSource.getMessage(
					"patrol.report.route.locationName", null, locale));
			parameters.put("Column_Min", messageSource.getMessage(
					"patrol.report.route.min", null, locale));
			parameters.put("Column_Max", messageSource.getMessage(
					"patrol.report.route.max", null, locale));
			parameters.put("Msg", null);
			parameters.put("TotalLocNumber", messageSource.getMessage(
					"patrol.report.route.totalLocNumber", null, locale));
			parameters.put("TotalRouteNumber", messageSource.getMessage(
					"patrol.report.route.totalRouteNumber", null, locale));
			parameters.put("labelPrintDate",
					messageSource.getMessage("report.print.date", null, locale)
							+ " : ");
			parameters.put("labelPage",
					messageSource.getMessage("report.page", null, locale));
			parameters.put("labelOf",
					messageSource.getMessage("report.of", null, locale));

			Object[] data = getPatrolRouteObjectData(jsonObject, this
					.getRoleFromSession(request).getSiteKey(), locale);

			if (data == null || data.length == 0) {
				data = new Object[1];
				data[0] = new PatrolRouteReport();
			}

			String filePath = jasper.getExportFile(jsonObject.getExportType(),
					"PatrolRoute_Report", "Patrol Route", parameters, data);

			jasper.outPutFile(jsonObject.getExportType(), filePath, response);
			// String pdf = jasper.executeReportToPdf("PatrolResult_Report",
			// "Patrol Result", parameters, data);

			// jasper.outPutFile("", pdf, response);

			System.out.println(filePath);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/ShowSelectReportDate.do")
	public String ShowSelectReportDate(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		return ModelMappingValue.pages_view_selectReportDate;
	}

	/**                 **/
	/** Other Functions **/
	/**                 **/

	public Map<Integer, String> getRoute(int siteKey) {

		List<RouteDef> routeDefList = routeDefManager.getAllRoute(siteKey);

		Map<Integer, String> routeMap = new LinkedHashMap<Integer, String>();
		routeMap.put(-1, "");

		if (routeDefList == null) {
			routeDefList = new ArrayList<RouteDef>();
		}
		for (int i = 0; i < routeDefList.size(); i++) {
			routeMap.put(routeDefList.get(i).getRouteDefKey(), routeDefList
					.get(i).getCode() + " - " + routeDefList.get(i).getName());
		}
		return routeMap;
	}

	private List<UserAccount> getPatrolUserAccount(int siteKey) {

		// PatrolAssignRouteController con = new PatrolAssignRouteController();

		// return con.getAccountBy(siteKey, "", -1,
		// privilegeMap.getProperty("privilege.code.routePatrol"));

		List<UserAccountRole> accountRoleList = null;
		List<UserAccountRole> hasPrivilegeList = new ArrayList<UserAccountRole>();
		try {

			String privilege = privilegeMap
					.getProperty("privilege.code.routePatrol");
			// System.out.println("Privilege : " + privilege);

			if (userAccountManager == null) {
				System.out.println("userAccountManager is null");
			}
			accountRoleList = userAccountManager.searchUserAccount("", -1);

			// System.out.println("accountRoleList : " +
			// accountRoleList.size());

			if (!privilege.equals("")) {
				List<RolePrivilege> roleList = roleManager
						.searchRoleByPrivilege(privilege);

				System.out.println("RoleList Size : " + roleList.size());

				if (roleList != null && roleList.size() > 0) {

					for (int i = 0; i < accountRoleList.size(); i++) {
						Boolean hasPrivilege = false;

						for (RolePrivilege rp : roleList) {

							// System.out.println("Role Key : "
							// + accountRoleList.get(i).getRoleKey()
							// + " || " + rp.getRoleKey());
							if (accountRoleList.get(i).getRoleKey()
									.equals(rp.getRoleKey())) {
								hasPrivilegeList.add(accountRoleList.get(i));
								// System.out.println("ADD : "
								// + accountRoleList.get(i).getKey());
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
//				System.out.println("site Key ac :"
//						+ hasPrivilegeList.get(i).getRole().getSiteKey() + "||"
//						+ siteKey + " || KEY : "
//						+ hasPrivilegeList.get(i).getKey());

				if (hasPrivilegeList.get(i).getRole().getSiteKey()
						.equals(siteKey)) {
					accountList.add(hasPrivilegeList.get(i).getUserAccount());
				}
			}
		}

		// System.out.println("Account list : " + accountList.size());

		return accountList;
	}

	private Map<Integer, String> getPatrolStaff(int siteKey) {

		List<UserAccount> userAccountList = getPatrolUserAccount(siteKey);

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		accountMap.put(-1, "");
		for (UserAccount userAccount : userAccountList) {
			accountMap.put(userAccount.getKey(), userAccount.getLoginId()
					+ " - " + userAccount.getName());
		}

		return accountMap;
	}

	public class AccountComparator implements Comparator<UserAccount> {
		@Override
		public int compare(UserAccount st, UserAccount nd) {
			return st.getLoginId().toLowerCase()
					.compareTo(nd.getLoginId().toLowerCase());
		}
	}

	private Object[] getPatrolStaffObjectData(int siteKey, Locale locale)
			throws MFMSException {

		ArrayList<PatrolStaffReport> patrolStaffReportList = new ArrayList<PatrolStaffReport>();

		List<UserAccount> userAccountList = getPatrolUserAccount(siteKey);

		Collections.sort(userAccountList, new AccountComparator());

		// List<UserAccount> userAccountList = null;
		// try {
		// userAccountList = userAccountManager.getAllUserAccount();
		// } catch (MFMSException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		statusMap
				.put(UserAccount.STATUS_ACTIVE, messageSource.getMessage(
						"account.status.active", null, locale));
		statusMap.put(UserAccount.STATUS_SUSPENDED, messageSource.getMessage(
				"account.status.suspended", null, locale));

		int index = 1;
		for (UserAccount userAccount : userAccountList) {
			PatrolStaffReport psr = new PatrolStaffReport();

			psr.setIndex(index++);
			psr.setPositionName("");
			psr.setAccountID(userAccount.getLoginId());
			psr.setAccountName(userAccount.getName());
			psr.setTagID(userAccount.getTagId());
			psr.setEmail(userAccount.getEmail());
			psr.setTel(userAccount.getContactNumber() == null ? ""
					: userAccount.getContactNumber().toString());
			psr.setStatus(statusMap.get((String) userAccount.getStatus()));

			patrolStaffReportList.add(psr);
		}

		return patrolStaffReportList.toArray();
	}

	private Object[] getPatrolResultObjectData(PatrolResultFormJSON jsonObject,
			int siteKey, Locale locale) throws ParseException {

		// Returned arraylist for generating the report
		ArrayList<PatrolResultReport> patrolResultReportList = new ArrayList<PatrolResultReport>();
		ArrayList<PatrolResultReport> unorderList = new ArrayList<PatrolResultReport>();
		Map<Integer, Timestamp> durationMap = new HashMap<Integer, Timestamp>();

		List<PatrolResult> patrolResultList = patrolResultManager.search(
				siteKey,
				jsonObject.getType(),
				jsonObject.getStartDate().equals("") ? null : this
						.stringToTimestamp(jsonObject.getStartDate() + ":00",
								dateTimeFormat),
				jsonObject.getEndDate().equals("") ? null : this
						.stringToTimestamp(jsonObject.getEndDate() + ":59",
								dateTimeFormat), jsonObject.getRoute(),
				jsonObject.getPatrolStaff());


		Integer groupNumCounter = 0;
		Integer locationSize = 0;
		Integer totalCorrectRoute = 0;
		Integer totalIncorrectRoute = 0;
		Boolean correctRoute = true;
		Boolean disorder = false;
		Boolean validRoute = true;
		// Timestamp previousAttendance = new Timestamp(0);
		Timestamp firstAttendance = new Timestamp(0);
		Timestamp lastAttendance = new Timestamp(0);

		// // loop all the patrol result
		// for (int counter_patrolResult = 0; counter_patrolResult <
		// patrolResultList
		// .size(); counter_patrolResult++) {
		//
		// // loop the route location in that patrol result
		// List<RouteLocation> routeLocationList = patrolResultList
		// .get(counter_patrolResult).getRouteDef().getRouteLocation();
		//
		// for (int counter_routeLocation = 0; counter_routeLocation <
		// routeLocationList
		// .size(); counter_routeLocation++) {
		//
		// RouteLocation routeLocation = routeLocationList
		// .get(counter_routeLocation);
		//
		// // init basic (if no patrol result / did not go to that
		// // location)
		// PatrolResultReport prr = initBasicResultElement(
		// groupNumCounter, routeLocation);
		//
		// // if there is patrol result in the location
		// if (routeLocation.getLocationKey() == patrolResultList.get(
		// counter_patrolResult).getCorrectLocationKey()) {
		//
		// PatrolResult patrolResult = patrolResultList
		// .get(counter_patrolResult);
		//
		// prr.setSeqNum(patrolResult.getSeqNum());
		// prr.setTimeAttended(this.timestampToString(
		// patrolResult.getTimeAttended(), this.timeFormat));
		// prr.setReason(patrolResult.getReason());
		//
		// // if the 1st location attended
		// if (patrolResult.getSeqNum() == 1) {
		// prr.setStartTime(this.timestampToString(
		// patrolResult.getTimeAttended(),
		// this.startTimeFormat));
		// prr.setAccountNameEN(patrolResult.getPerson().getName());
		// prr.setRouteCode(patrolResult.getRouteDef().getCode());
		// prr.setRouteNameEN(patrolResult.getRouteDef().getName());
		// firstAttendance = patrolResult.getTimeAttended();
		// }
		//
		// // Other location attended
		// if (patrolResult.getSeqNum() > 1) {
		// prr.setDuration(this.calDuration(previousAttendance,
		// patrolResult.getTimeAttended()));
		// }
		//
		// // set last Attendance
		// if (patrolResult.getTimeAttended().getTime() > lastAttendance
		// .getTime()) {
		// lastAttendance = patrolResult.getTimeAttended();
		// }
		// // set the total Time if all the location is looped
		// if (counter_routeLocation == routeLocationList.size() - 1) {
		// prr.setTotalTimeUsed(this.calTotalTime(firstAttendance,
		// lastAttendance));
		// }
		//
		// previousAttendance = patrolResult.getTimeAttended();
		//
		// break;
		// }
		//
		// patrolResultReportList.add(prr);
		// }
		//
		// // defined the group in the report to separate each patrol result by
		// // route
		// if (counter_patrolResult < patrolResultList.size() - 1) {
		//
		// // check if the next result seqNum is the same or smaller than
		// // current (means that the next one is another group of patrol
		// // result)
		// if (patrolResultList.get(counter_patrolResult + 1).getSeqNum() <=
		// patrolResultList
		// .get(counter_patrolResult).getSeqNum())
		// groupNumCounter++;
		// }
		//
		// }

		// /------------------------
		PatrolResultReport prr = new PatrolResultReport();
		for (PatrolResult patrolResult : patrolResultList) {

			
			// initialize
			if (locationSize <= 0) {
				
				locationSize = patrolResultManager.countLocationByGroupNum(patrolResult.getGroupNum());
				groupNumCounter++;
				unorderList = new ArrayList<PatrolResultReport>();
				durationMap = new HashMap<Integer, Timestamp>();
				firstAttendance = new Timestamp(0);
				lastAttendance = new Timestamp(0);
				correctRoute = true;
				disorder = false;

			}
			locationSize--;

			// start set content
			prr = new PatrolResultReport();

			durationMap.put(patrolResult.getSeqNum(),
					patrolResult.getTimeAttended());
//			prr.setGroupNum(groupNumCounter);
			prr.setGroupNum(patrolResult.getGroupNum());
			prr.setSeqNum(patrolResult.getSeqNum());
			prr.setTimeAttended("--:--:--");
			prr.setDuration("--:--");
			prr.setDisplayLocationCode(patrolResult.getCorrectionLocation()
					.getCode());
			prr.setDisplayLocationNameEN(patrolResult.getCorrectionLocation()
					.getName());
			prr.setResult("");
			prr.setReason(patrolResult.getReason());

			if (patrolResult.getSeqNum() > 0) {
				prr.setTimeAttended(this.timestampToString(
						patrolResult.getTimeAttended(), this.timeFormat));
			}

			if (patrolResult.getSeqNum() == 1) {
				prr.setStartTime(this.timestampToString(
						patrolResult.getTimeAttended(), this.startTimeFormat));
				prr.setAccountNameEN(patrolResult.getPerson().getName());
				prr.setRouteCode(patrolResult.getRouteDef().getCode());
				prr.setRouteNameEN(patrolResult.getRouteDef().getName());
				firstAttendance = patrolResult.getTimeAttended();

			}

			// Check if it is the last Attendance Time
			if (patrolResult.getTimeAttended().getTime() > lastAttendance
					.getTime()) {
				lastAttendance = patrolResult.getTimeAttended();
			}

			// Calculate the Total Time && set the header
			if (locationSize <= 0) {
				prr.setTotalTimeUsed(this.calTotalTime(firstAttendance,
						lastAttendance));
			}
			
			prr.setPatrolResult(patrolResult);
			unorderList.add(prr);

			// Calculate the duration
			if (locationSize == 0) {
				if (unorderList.size() > 0) {

					// List<RouteLocation> routeLocationList =
					// patrolResult.getRouteDef().getRouteLocation();

					unorderList.get(0).setStartTime(
							this.timestampToString(firstAttendance,
									startTimeFormat));
					unorderList.get(0).setAccountNameEN(
							patrolResult.getPerson().getName());
					unorderList.get(0).setRouteCode(
							patrolResult.getRouteDef().getCode());
					unorderList.get(0).setRouteNameEN(
							patrolResult.getRouteDef().getName());

					for (int i = 0; i < unorderList.size(); i++) {
						PatrolResultReport temp = unorderList.get(i);
						validRoute = true;

						if (temp.getSeqNum() == 0) {
							// temp.setResult("Skip");
							temp.setResult(messageSource.getMessage(
									"report.patrol.result.skip", null, locale));
							correctRoute = false;
							continue;
						}

						if (temp.getSeqNum() == 1) {
							if (i != 0) {
								temp.setResult(messageSource.getMessage(
										"report.patrol.result.disorder", null,
										locale));
								correctRoute = false;
								disorder = true;
							} else {
								temp.setResult(messageSource.getMessage(
										"report.patrol.result.correct", null,
										locale));
							}
							continue;
						}

						if (temp.getSeqNum() > 1) {

							Timestamp previous = durationMap.get(temp
									.getSeqNum() - 1);
							Timestamp current = durationMap.get(temp
									.getSeqNum());

							logger.debug("Seq : " + temp.getSeqNum()
									+ "|| previous : " + previous
									+ "|| current:" + current);
						
							if (previous == null) {
								validRoute = false;
								break;
							}

							temp.setDuration(calDuration(previous, current));

							if (i == 0) {
								temp.setResult(messageSource.getMessage(
										"report.patrol.result.disorder", null,
										locale));
								correctRoute = false;
								disorder = true;
								continue;
							}

							if (unorderList.get(i - 1).getSeqNum() == temp
									.getSeqNum() - 1) {

								long timeDiff = (current.getTime() - previous
										.getTime()) / 1000;


								long maxDur = temp.getPatrolResult().getMaxPtDur()
										* (String
												.valueOf(
														 temp.getPatrolResult()
																.getMaxPtDurUnit())
												.equals(PatrolController.ModelMappingValue.ptDurUnitInDb[0]) ? 1 * 60
												: 60 * 60);
								long minDur =  temp.getPatrolResult().getMinPtDur()
										* (String
												.valueOf(
														 temp.getPatrolResult()
																.getMinPtDurUnit())
												.equals(PatrolController.ModelMappingValue.ptDurUnitInDb[0]) ? 1 * 60
												: 60 * 60);

								System.out.println("TimeDiff : " + timeDiff
										+ "|| minDur : " + minDur
										+ " || maxDur : " + maxDur);

								temp.setResult(messageSource.getMessage(
										"report.patrol.result.correct", null,
										locale));

//								if (minDur == 0 && maxDur == 0 && patrolResult.getPatrolStatus()!= null) {
								if (patrolResult.getPatrolStatus()!= null) {

									System.out.println("Patrol Status ======= "+ temp.getPatrolResult().getPatrolStatus());
									
									PatrolResult.PatrolStatus pstatus = PatrolResult.PatrolStatus
											.fromString( temp.getPatrolResult()
													.getPatrolStatus());
									
									logger.debug("patrol status : "+pstatus.toString());

									switch (pstatus) {

									case TooFast:
										temp.setResult(messageSource
												.getMessage(
														"report.patrol.result.toofast",
														null, locale));
										correctRoute = false;

										break;
									case TooSlow:

										temp.setResult(messageSource
												.getMessage(
														"report.patrol.result.tooslow",
														null, locale));
										correctRoute = false;
										break;
									default:
										break;

									}

								} else {

									if (timeDiff >= maxDur) {
										temp.setResult(messageSource
												.getMessage(
														"report.patrol.result.tooslow",
														null, locale));
										correctRoute = false;
									}

									if (timeDiff < minDur) {
										temp.setResult(messageSource
												.getMessage(
														"report.patrol.result.toofast",
														null, locale));
										correctRoute = false;
									}
								}

							} else {
								temp.setResult(messageSource.getMessage(
										"report.patrol.result.disorder", null,
										locale));
								correctRoute = false;
								disorder = true;
								continue;
							}

						}

					}

					if (!validRoute) {
						continue;
					}

					switch (ReportType.valueOf(jsonObject.getType())) {

					case All:
						if (correctRoute) {
							totalCorrectRoute++;
						} else {
							totalIncorrectRoute++;
						}

						patrolResultReportList.addAll(unorderList);

						break;
					case Incorrect:
						if (disorder || !correctRoute) {
							totalIncorrectRoute++;
							patrolResultReportList.addAll(unorderList);
						}

						break;
					case Correct:
						if (correctRoute) {
							totalCorrectRoute++;
							patrolResultReportList.addAll(unorderList);
						}

						break;
					default:
						break;

					}

				}

			}

		}

		// set Summary
		if (patrolResultReportList.size() > 0) {
			patrolResultReportList.get(patrolResultReportList.size() - 1)
					.setTotalCorrectRouteNum(totalCorrectRoute);
			patrolResultReportList.get(patrolResultReportList.size() - 1)
					.setTotalIncorrectRouteNum(totalIncorrectRoute);
			patrolResultReportList
					.get(patrolResultReportList.size() - 1)
					.setTotalRouteNum((totalIncorrectRoute + totalCorrectRoute));
		}
		return patrolResultReportList.toArray();

		// return null;
	}

	private Object[] getPatrolRouteObjectData(PatrolRouteFormJSON jsonObject,
			int siteKey, Locale locale) throws MFMSException, ParseException {

		// Returned arraylist for generating the report
		ArrayList<PatrolRouteReport> patrolRouteReportList = new ArrayList<PatrolRouteReport>();

		Map<Integer, Timestamp> durationMap = new HashMap<Integer, Timestamp>();

		List<Patrol_Route> patrolRouteList = routeDefManager.searchRoute(
				siteKey, jsonObject.getRoute());

		if (null != patrolRouteList) {
			logger.debug("The route size : " + patrolRouteList.size());

			// /------------------------
			for (Patrol_Route patrolRoute : patrolRouteList) {

				if (null != patrolRoute.getRouteLocation()
						&& patrolRoute.getRouteLocation().size() > 0) {
					logger.debug("The route "
							+ patrolRoute.getRouteDef().getRouteDefKey()
							+ " location size : "
							+ patrolRoute.getRouteLocation().size());

					List<RouteLocation> routeLocationList = patrolRoute
							.getRouteLocation();
					if (routeLocationList != null
							&& routeLocationList.size() > 0) {
						try {
							for (RouteLocation routeLocation : patrolRoute
									.getRouteLocation()) {
								Location loc = locationManager
										.getLocationByKey(routeLocation
												.getLocationKey());

								// start set content
								PatrolRouteReport prr = new PatrolRouteReport();

								prr.setRouteCode(patrolRoute.getRouteDef()
										.getCode());
								prr.setRouteNameEN(patrolRoute.getRouteDef()
										.getName());
								prr.setSeqNum(routeLocation.getSeqNum());
								prr.setDisplayLocationCode(loc.getCode());
								prr.setDisplayLocationNameEN(loc.getName());
								prr.setMin(routeLocation.getMinPtDur()
										+ " "
										+ messageSource
												.getMessage(
														"patrol."
																+ PatrolController.ModelMappingValue.ptDurUnit[routeLocation
																		.getMinPtDurUnit()],
														null, locale));
								prr.setMax(routeLocation.getMaxPtDur()
										+ " "
										+ messageSource
												.getMessage(
														"patrol."
																+ PatrolController.ModelMappingValue.ptDurUnit[routeLocation
																		.getMaxPtDurUnit()],
														null, locale));
								prr.setTotalLocNum(patrolRoute
										.getRouteLocation().size());

								patrolRouteReportList.add(prr);
							}
						} catch (MFMSException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					/*
					 * for (RouteLocation routeLocation :
					 * patrolRoute.getRouteLocation()) { Location loc =
					 * locationManager
					 * .getLocationByKey(routeLocation.getRouteLocationKey());
					 * 
					 * // start set content PatrolRouteReport prr = new
					 * PatrolRouteReport();
					 * 
					 * prr.setRouteCode(patrolRoute.getRouteDef().getCode());
					 * prr.setRouteNameEN(patrolRoute.getRouteDef().getName());
					 * prr.setSeqNum(routeLocation.getSeqNum());
					 * prr.setDisplayLocationCode(loc.getCode());
					 * prr.setDisplayLocationNameEN(loc.getName());
					 * prr.setMin(routeLocation.getMinPtDur() + " " +
					 * messageSource.getMessage("patrol." +
					 * PatrolController.ModelMappingValue
					 * .ptDurUnit[routeLocation.getMinPtDurUnit()], null,
					 * locale)); prr.setMax(routeLocation.getMaxPtDur() + " " +
					 * messageSource.getMessage("patrol." +
					 * PatrolController.ModelMappingValue
					 * .ptDurUnit[routeLocation.getMaxPtDurUnit()], null,
					 * locale));
					 * prr.setTotalLocNum(patrolRoute.getRouteLocation(
					 * ).size());
					 * 
					 * patrolRouteReportList.add(prr); }
					 */
				}
			}

			// set Summary
			if (patrolRouteReportList.size() > 0) {
				patrolRouteReportList.get(patrolRouteReportList.size() - 1)
						.setTotalRouteNum(patrolRouteList.size());
			}

			return patrolRouteReportList.toArray();
		}

		return null;
	}

	private Role getRoleFromSession(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Role role = (Role) session.getAttribute("currRole");

		return role;

	}

	private Timestamp stringToTimestamp(String date, SimpleDateFormat format)
			throws ParseException {

		return new Timestamp(format.parse(date).getTime());
	}

	private String timestampToString(Timestamp time, SimpleDateFormat format) {
		return format.format(new Date(time.getTime()));
	}

	private String calDuration(Timestamp dateBefore, Timestamp dateCurrent) {

		long timeDiff = dateCurrent.getTime() - dateBefore.getTime();

		int minute = (int) (timeDiff / 1000 / 60);
		int second = (int) (timeDiff / 1000 % 60);

		String str_minute = String.valueOf(minute);
		String str_second = String.valueOf(second);

		String rtn = (str_minute.length() > 1 ? str_minute : "0" + str_minute)
				+ ":"
				+ (str_second.length() > 1 ? str_second : "0" + str_second);

		System.out.println("Duration Format : " + rtn);

		return rtn;
	}

	private String calTotalTime(Timestamp dateBefore, Timestamp dateCurrent) {
		long timeDiff = dateCurrent.getTime() - dateBefore.getTime();

		int hour = (int) (timeDiff / 1000 / 60 / 60);
		int minute = (int) (timeDiff / 1000 / 60 % 60);
		int second = (int) (timeDiff / 1000 % 60);

		String str_hour = String.valueOf(hour);
		String str_minute = String.valueOf(minute);
		String str_second = String.valueOf(second);

		String rtn = (str_hour.length() > 1 ? str_hour : "0" + str_hour) + ":"
				+ (str_minute.length() > 1 ? str_minute : "0" + str_minute)
				+ ":"
				+ (str_second.length() > 1 ? str_second : "0" + str_second);

		System.out.println("Total Duration Format : " + rtn);

		return rtn;
	}

	// private PatrolResultReport initBasicResultElement(Integer
	// groupNumCounter,
	// RouteLocation routeLocation) {
	//
	// PatrolResultReport prr = new PatrolResultReport();
	//
	// prr.setGroupNum(groupNumCounter);
	// prr.setSeqNum(0);
	// prr.setTimeAttended("--:--:--");
	// prr.setDuration("--:--");
	// prr.setDisplayLocationCode(routeLocation.getLocation().getCode());
	// prr.setDisplayLocationNameEN(routeLocation.getLocation().getName());
	// prr.setResult("");
	// prr.setReason("");
	//
	// return prr;
	// }

	// private void

	private Map<String, String> getReportTypeMap(Locale locale) {
		Map<String, String> reportTypeMap = new LinkedHashMap<String, String>();

		ReportType[] reportType = PatrolResultFormJSON.ReportType.values();

		for (int i = 0; i < reportType.length; i++) {
			String name = messageSource.getMessage("patrol.report.type."
					+ reportType[i].name(), null, locale);
			reportTypeMap.put(reportType[i].name(), name);
		}

		return reportTypeMap;

	}

	@RequestMapping(value = "GetPhotoReport.do")
	// @ResponseStatus(value = HttpStatus.OK)
	public void GetPatrolRouteReport(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {
		try {
			Object[] data = testImage();

			if (data == null || data.length == 0) {
				data = new Object[1];
				data[0] = new PatrolRouteReport();
			}
			Map<String, String> parameters = new HashMap<String, String>();
			String filePath = jasper.getExportFile("pdf", "PatrolRoute_Report",
					"Patrol Route", parameters, data);

			jasper.outPutFile("pdf", filePath, response);
			// String pdf = jasper.executeReportToPdf("PatrolResult_Report",
			// "Patrol Result", parameters, data);

			// jasper.outPutFile("", pdf, response);

			System.out.println(filePath);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Object[] testImage() {
		List<ImageTest> list = new ArrayList<ImageTest>();

		try {
			for (int i = 0; i < 3; i++) {
				InputStream is = new BufferedInputStream(new FileInputStream(
						"C:\\MFMS\\media\\photo\\" + i + ".jpg"));

				ImageTest tmp = new ImageTest();
				tmp.setImg(is);
				list.add(tmp);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list.toArray();

	}

}
