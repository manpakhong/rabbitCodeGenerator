package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.CalendarJSON.Frequency;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.CauseCodeManager;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.manager.FailureClassManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.PriorityManager;
import hk.ebsl.mfms.manager.ProblemCodeManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.report.JasperReportService;
import hk.ebsl.mfms.report.template.object.DefectExcel;
import hk.ebsl.mfms.report.template.object.DefectListReport;
import hk.ebsl.mfms.report.template.object.DefectStatusSummaryReport;
import hk.ebsl.mfms.web.controller.DefectController.FailureClassComparator;
import hk.ebsl.mfms.web.controller.DefectController.StatusComparator2;
import hk.ebsl.mfms.web.form.DefectListForm;
import hk.ebsl.mfms.web.form.DefectStatusSummaryForm;
import hk.ebsl.mfms.web.form.validator.DefectListFormValidator;
import hk.ebsl.mfms.web.form.validator.DefectSummaryFormValidator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefectReportController {

	public final static Logger logger = Logger
			.getLogger(DefectReportController.class);

	@Autowired
	private DataSource dataSource;
	@Autowired
	private Properties privilegeMap;
	@Autowired
	private PriorityManager priorityManager;
	@Autowired
	private DefectManager defectManager;
	@Autowired
	private FailureClassManager failureClassManager;
	@Autowired
	private ProblemCodeManager problemCodeManager;
	@Autowired
	private CauseCodeManager causeCodeManager;
	@Autowired
	private EquipmentManager equipmentManager;
	@Autowired
	private LocationManager locationManager;
	@Autowired
	private StatusManager statusManager;
	@Autowired
	private UserAccountManager accountManager;
	@Autowired
	private AccountGroupManager accountGroupManager;
	@Autowired
	private AccountGroupAccountManager accountGroupAccountManager;
	@Autowired
	private SiteManager siteManager;
	@Autowired
	private DefectListFormValidator defectListFormValidator;
	@Autowired
	private DefectSummaryFormValidator defectSummaryFormValidator;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	JasperReportService jasper;
	@Autowired
	private Properties propertyConfigurer;
	
	
	String _from = " 00:00:00";
	String _to = "  23:59:59";

	public Timestamp convertStringToTimestamp(String str_date) {
		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date date = (Date) formatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			return timeStampDate;
		} catch (ParseException e) {
			System.out.println("Exception :" + e);
			return null;
		}
	}

	public String getCurrentTimeString() {
		java.util.Date utilDate = new java.util.Date();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(
				utilDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeString = sdf.format(timestamp);
		return timeString;
	}

	public String convertTimestampToString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			String timeString = sdf.format(ts);
			return timeString;
		} else
			return null;
	}

	/*
	 * Comparator
	 */

	public class FailureClassComparator implements Comparator<FailureClass> {
		@Override
		public int compare(FailureClass st, FailureClass nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

	public class ProblemCodeComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

	public class CauseCodeComparator implements Comparator<CauseCode> {
		@Override
		public int compare(CauseCode st, CauseCode nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

	public class EquipmentComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

	public class ToolComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {
			return st.getCode().compareTo(nd.getCode());
		}
	}

	public class StatusComparator implements Comparator<Status> {
		@Override
		public int compare(Status st, Status nd) {
			return st.getSequence().compareTo(nd.getSequence());
		}
	}

	@RequestMapping(value = "/DefectReport.do")
	public String showReportMenu(HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws MFMSException {

		logger.debug("User requests to load defect report page.");
		
		HttpSession session = request.getSession();
		try {

			return "defect/defectReport_menu";

		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	// TODO DefectList

	@RequestMapping(value = "/DefectList.do")
	public String showCreateDefectListForm(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		logger.debug("User requests to load defect list page.");
		
		List<Location> locationList = new ArrayList<Location>();
		List<Priority> priorityList = new ArrayList<Priority>();
		List<Status> statusList = new ArrayList<Status>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> callFromMap = new LinkedHashMap<String, String>();
		Map<String, String> sortByMap = new LinkedHashMap<String, String>();
		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();
		DefectListForm defectListForm = new DefectListForm();

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			defectListForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.defectListReport"));

		if (hasPrivilege) {
			locationList = locationManager.getLocationsBySiteKey(currRole
					.getSiteKey());

			failureClassList = failureClassManager
					.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());

			causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole
					.getSiteKey());
			Collections.sort(causeCodeList, new CauseCodeComparator());

			equipmentList = equipmentManager.getEquipmentBySiteKey(currRole
					.getSiteKey());
			Collections.sort(equipmentList, new EquipmentComparator());

			priorityList = priorityManager.getAllPriority();

			statusList = statusManager.getAllStatus();
			Collections.sort(statusList, new StatusComparator());

			accountList = getAccountList(request);

			accountGroupList = accountGroupManager
					.getAccountGroupBySiteKey(currRole.getSiteKey());

			accountGroupMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(),
						accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(),
						failureClassList.get(i).getCode() + " - " + failureClassList.get(i).getName());
			}
			model.addAttribute("failureClassList", failureClassMap);

			causeCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < causeCodeList.size(); i++) {
				causeCodeMap.put(causeCodeList.get(i).getKey(), causeCodeList
						.get(i).getCode() + " - " + causeCodeList.get(i).getName());
			}
			model.addAttribute("causeCodeList", causeCodeMap);

			equipmentMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < equipmentList.size(); i++) {
				equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
						.get(i).getCode() + " - " + equipmentList.get(i).getName());
			}
			model.addAttribute("equipmentList", equipmentMap);

			problemCodeMap.put(null, pleaseSelect(locale));

			model.addAttribute("problemCodeList", problemCodeMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList
						.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			statusMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getStatusId(), statusList
						.get(i).getName());
			}
			model.addAttribute("statusList", statusMap);

			accountMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountList.size(); i++) {
				accountMap.put(accountList.get(i).getKey(),
						setMap(accountList.get(i)));
			}
			model.addAttribute("assignedToKeyList", accountMap);

			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(),
						accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			callFromMap.put(null, "common.pleaseSelect");
			callFromMap.put("H", "defect.callFrom.handheld");
			callFromMap.put("L", "defect.callFrom.landlord");
			callFromMap.put("S", "defect.callFrom.staff");
			callFromMap.put("T", "defect.callFrom.tenant");
			callFromMap.put("V", "defect.callFrom.visitor");

			model.addAttribute("callFromList", callFromMap);

			sortByMap.put(null, "common.pleaseSelect");
			sortByMap.put("defect.d_Key", "defect.code");
			sortByMap.put("defect.d_LocationKey", "defect.location");
			sortByMap.put("defect.d_FailureClassKey", "defect.failureClass");
			sortByMap.put("defect.d_ProblemCodeKey", "defect.problemCode");
			sortByMap.put("defect.d_AssignedAccountKey",
					"defect.assignedAccount");
			sortByMap.put("defect.d_Priority", "defect.priority");
			sortByMap.put("defect.d_CallFrom", "defect.callFrom");
			sortByMap.put("defect.d_TargetFinishDateTime",
					"defect.targetFinishDate");
			sortByMap.put("defect.d_MeetKpi", "defect.kpi");

			model.addAttribute("sortByList", sortByMap);

			defectListForm.setOrder("ASC");

			defectListForm.setFileFormat("pdf");

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
			defectListForm.setAvailableLocationTree(locationTree);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();

			defectListForm.setFrom(dateFormat.format(new Date(cal
					.getTimeInMillis())));
			defectListForm.setTo(dateFormat.format(new Date(cal
					.getTimeInMillis())));

			model.addAttribute("defectListForm", defectListForm);

			return "defect/report_defectList";
		} else {
			// user does not have right to defect list report
			logger.debug("user does not have right to defect list report ");
			return "common/noprivilege";
		}
	}

	// TODO DoCreateDefectList
	@RequestMapping(value = "/DoCreateDefectList.do")
	public String doCreateDefectListForm(
			@ModelAttribute("defectListForm") DefectListForm defectListForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, UnsupportedEncodingException {

		List<Location> locationList = new ArrayList<Location>();
		List<Priority> priorityList = new ArrayList<Priority>();
		List<Status> statusList = new ArrayList<Status>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> callFromMap = new LinkedHashMap<String, String>();
		Map<String, String> sortByMap = new LinkedHashMap<String, String>();
		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.defectListReport"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");
			// get user's current site

			Integer siteKey = defectListForm.getSiteKey();

			if (defectListForm.getLocationKey() != null) {
				String locationCode = locationManager.getLocationByKey(
						defectListForm.getLocationKey()).getCode();
				String locationName = locationManager.getLocationByKey(
						defectListForm.getLocationKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationCode
						+ " - " + locationName);
			}

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

			defectListForm.setAvailableLocationTree(locationTree);

			locationList = locationManager.getLocationsBySiteKey(siteKey);

			failureClassList = failureClassManager
					.getFailureClassBySiteKey(siteKey);
			Collections.sort(failureClassList, new FailureClassComparator());

			if (defectListForm.getFailureClassKey() != null) {
				problemCodeList = problemCodeManager.searchProblemCode(null,
						null, null, defectListForm.getFailureClassKey(), null);
				Collections.sort(problemCodeList, new ProblemCodeComparator());

			}

			causeCodeList = causeCodeManager.getCauseCodeBySiteKey(siteKey);
			Collections.sort(causeCodeList, new CauseCodeComparator());

			equipmentList = equipmentManager.getEquipmentBySiteKey(siteKey);
			Collections.sort(equipmentList, new EquipmentComparator());

			priorityList = priorityManager.getAllPriority();

			statusList = statusManager.getAllStatus();
			Collections.sort(statusList, new StatusComparator());

			if (defectListForm.getAssignedToKey() != null) {
				List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
				agaList = accountGroupAccountManager
						.searchAccountGroupAccount(defectListForm
								.getAccountGroupKey());
				for (int i = 0; i < agaList.size(); i++) {
					accountList.add(accountManager
							.getUserAccountByAccountKey(agaList.get(i)
									.getAccountKey()));
					Collections.sort(accountList, new AccountComparator());
				}
			} else
				accountList = getAccountList(request);

			accountGroupList = accountGroupManager
					.getAccountGroupBySiteKey(siteKey);

			accountGroupMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(),
						accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			accountMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountList.size(); i++) {
				accountMap.put(accountList.get(i).getKey(),
						setMap(accountList.get(i)));
			}
			model.addAttribute("assignedToKeyList", accountMap);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(),
						failureClassList.get(i).getCode());
			}
			model.addAttribute("failureClassList", failureClassMap);

			problemCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < problemCodeList.size(); i++) {
				problemCodeMap.put(problemCodeList.get(i).getKey(),
						problemCodeList.get(i).getCode());
			}
			model.addAttribute("problemCodeList", problemCodeMap);

			causeCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < causeCodeList.size(); i++) {
				causeCodeMap.put(causeCodeList.get(i).getKey(), causeCodeList
						.get(i).getCode());
			}
			model.addAttribute("causeCodeList", causeCodeMap);

			equipmentMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < equipmentList.size(); i++) {
				equipmentMap.put(equipmentList.get(i).getKey(), equipmentList
						.get(i).getCode());
			}
			model.addAttribute("equipmentList", equipmentMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList
						.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			statusMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getStatusId(), statusList
						.get(i).getName());
			}
			model.addAttribute("statusList", statusMap);

			callFromMap.put(null, "common.pleaseSelect");
			callFromMap.put("H", "defect.callFrom.handheld");
			callFromMap.put("L", "defect.callFrom.landlord");
			callFromMap.put("S", "defect.callFrom.staff");
			callFromMap.put("T", "defect.callFrom.tenant");
			callFromMap.put("V", "defect.callFrom.visitor");

			model.addAttribute("callFromList", callFromMap);

			sortByMap.put(null, "common.pleaseSelect");
			sortByMap.put("defect.d_Key", "defect.code");
			sortByMap.put("defect.d_LocationKey", "defect.location");
			sortByMap.put("defect.d_FailureClassKey", "defect.failureClass");
			sortByMap.put("defect.d_ProblemCodeKey", "defect.problemCode");
			sortByMap.put("defect.d_StatusID", "defect.status");
			sortByMap.put("defect.d_AssignedAccountKey",
					"defect.assignedAccount");
			sortByMap.put("defect.d_Priority", "defect.priority");
			sortByMap.put("defect.d_CallFrom", "defect.callFrom");
			sortByMap.put("defect.d_TargetFinishDateTime",
					"defect.targetFinishDate");
			sortByMap.put("defect.d_ActualFinishDateTime",
					"defect.actualFinishDate");
			sortByMap.put("defect.d_MeetKpi", "defect.kpi");

			model.addAttribute("sortByList", sortByMap);

			// validate
			defectListFormValidator.validate(defectListForm, result);

			if (result.hasErrors()) {
				logger.debug("Validation Failed");

				return "defect/report_defectList";
			}

			HashMap<String, Object> hmParams = new HashMap<String, Object>();

			// Site
			Site thisSite = siteManager.getSiteByKey(siteKey);
			String siteName = (null == thisSite) ? "" : " - "
					+ thisSite.getName();

			// Report Label
			hmParams.put(
					"labelDefectList",
					messageSource.getMessage(
							"menu.defectMgt.report.defectList", null, locale)
							+ siteName);
			hmParams.put("labelDefectNo",
					messageSource.getMessage("defect.code", null, locale));
			hmParams.put("labelStatus",
					messageSource.getMessage("defect.status", null, locale));
			hmParams.put("labelIssuedDate",
					messageSource.getMessage("defect.issueDate", null, locale));
			hmParams.put("labelFailureClass", messageSource.getMessage(
					"defect.failureClass", null, locale));
			hmParams.put("labelProblemCode", messageSource.getMessage(
					"defect.problemCode", null, locale));
			hmParams.put("labelLcationDescription",
					messageSource.getMessage("defect.location", null, locale));
			hmParams.put("labelDefectDescription", messageSource.getMessage(
					"defect.description", null, locale));
			hmParams.put("labelCallFrom",
					messageSource.getMessage("defect.callFrom", null, locale));
			hmParams.put("labelContactPhone", messageSource.getMessage(
					"defect.contactName", null, locale));
			hmParams.put("labelAssignedTo", messageSource.getMessage(
					"defect.assignedAccount", null, locale));
			hmParams.put("labelPriority",
					messageSource.getMessage("defect.priority", null, locale));
			hmParams.put("labelTargetAttendanceDate", messageSource.getMessage(
					"defect.targetFinishDate", null, locale));
			hmParams.put("labelActualFinishDate", messageSource.getMessage(
					"defect.actualFinishDate", null, locale));
			hmParams.put("labelRemarks",
					messageSource.getMessage("defect.remarks", null, locale));
			hmParams.put("labelPrintDate",
					messageSource.getMessage("report.print.date", null, locale)
							+ " : ");
			hmParams.put("labelPage",
					messageSource.getMessage("report.page", null, locale));
			hmParams.put("labelOf",
					messageSource.getMessage("report.of", null, locale));
			hmParams.put("labelMeetKPI",
					messageSource.getMessage("report.kpi", null, locale));
			hmParams.put("labelDeviation",
					messageSource.getMessage("report.deviation", null, locale));
			// End Report Label

			String reportFormat = defectListForm.getFileFormat();

			Timestamp from = convertStringToTimestamp(defectListForm.getFrom()
					+ _from);
			Timestamp to = convertStringToTimestamp(defectListForm.getTo()
					+ _to);

			int count = defectManager.searchDefectListCount(siteKey, from, to, defectListForm.getCode(),
					defectListForm.getAssignedToKey(),
					defectListForm.getLocationKey(),
					defectListForm.getFailureClassKey(),
					defectListForm.getProblemCodeKey(),
					defectListForm.getCauseCodeKey(),
					defectListForm.getEquipmentKey(),
					defectListForm.getPriority(), defectListForm.getCallFrom(),
					defectListForm.getStatus(), account.getKey(), defectListForm.getAccountGroupKey());
			
			if (count > Integer.parseInt(propertyConfigurer
					.getProperty("defect.max.search.size"))) {
				result.rejectValue("", "defect.search.exceed",
						new Object[] { propertyConfigurer
								.getProperty("defect.max.search.size") }, null);
			}
			
			if (result.hasErrors()) {
				logger.debug("Validation Failed");

				return "defect/report_defectList";
			}
			
			List<DefectListReport> list = defectManager.searchDefectList(
					siteKey, from, to, defectListForm.getCode(),
					defectListForm.getAssignedToKey(),
					defectListForm.getLocationKey(),
					defectListForm.getFailureClassKey(),
					defectListForm.getProblemCodeKey(),
					defectListForm.getCauseCodeKey(),
					defectListForm.getEquipmentKey(),
					defectListForm.getPriority(), defectListForm.getCallFrom(),
					defectListForm.getStatus(), account.getKey(), defectListForm.getAccountGroupKey());


			switch (defectListForm.getSortBy()) {
			case "defect.d_Key":
				Collections.sort(list, new DefectCodeComparator());
				break;
			case "defect.d_LocationKey":
				Collections.sort(list, new DefectLocationComparator());
				break;
			case "defect.d_FailureClassKey":
				Collections.sort(list, new DefectFailureClassComparator());
				break;
			case "defect.d_ProblemCodeKey":
				Collections.sort(list, new DefectProblemCodeComparator());
				break;
			case "defect.d_AssignedAccountKey":
				Collections.sort(list, new DefectAccountComparator());
				break;
			case "defect.d_Priority":
				Collections.sort(list, new DefectPriorityComparator());
				break;
			case "defect.d_CallFrom":
				Collections.sort(list, new DefectCallFromComparator());
				break;

			case "defect.d_MeetKpi":
				Collections.sort(list, new DefectKpiComparator());
				break;

			case "defect.d_TargetFinishDateTime":
				Collections.sort(list, new DefectTargetFinishDateComparator());
				break;
			case "defect.d_ActualFinishDateTime":
				Collections.sort(list, new DefectActualFinishDateComparator());
				break;
			}

			if (!defectListForm.getOrder().equals("ASC"))
				Collections.reverse(list);

			for (DefectListReport d : list) {

				switch (d.getFieldCallFrom()) {
				case "L":
					d.setFieldCallFrom(messageSource.getMessage(
							"defect.callFrom.landlord", null, locale));
					break;
				case "V":
					d.setFieldCallFrom(messageSource.getMessage(
							"defect.callFrom.visitor", null, locale));
					break;
				case "H":
					d.setFieldCallFrom(messageSource.getMessage(
							"defect.callFrom.handheld", null, locale));
					break;
				case "S":
					d.setFieldCallFrom(messageSource.getMessage(
							"defect.callFrom.staff", null, locale));
					break;
				case "T":
					d.setFieldCallFrom(messageSource.getMessage(
							"defect.callFrom.tenant", null, locale));
					break;
				}

			}

			hmParams.put("labelTotal",
					messageSource.getMessage("report.total", null, locale)
							+ " : " + list.size());

			Object[] data = list.toArray();

			if (data == null || data.length == 0) {
				data = new Object[1];
				data[0] = new DefectListReport();
			}
			
			String filePath = jasper.getExportFile(reportFormat,
					"defectList_v2", "Defect_v2", hmParams, data);

			jasper.outPutFile(reportFormat, filePath, response);

			return null;
		} else {
			// user does not have right to defect list report
			logger.debug("user does not have right to defect list report ");
			return "common/noprivilege";
		}
	}

	// TODO DefectStatusSummary

	@RequestMapping(value = "/DefectStatusSummary.do")
	public String showCreateDefectStatusSummaryForm(HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load defect status summary page.");

		DefectStatusSummaryForm defectStatusSummaryForm = new DefectStatusSummaryForm();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			defectStatusSummaryForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.defectStatusSummaryReport"));
		try {

			if (hasPrivilege) {
				Map<String, String> sortByMap = new LinkedHashMap<String, String>();
				sortByMap.put(null, pleaseSelect(locale));
				sortByMap.put("status", "defect.status");
				sortByMap.put("failureClass", "defect.failureClass");
				sortByMap.put("problemCode", "defect.problemCode");
				sortByMap.put("location", "defect.location");
				sortByMap.put("priority", "defect.priority");

				model.addAttribute("sortByList", sortByMap);
				Map<String, String> statusMap = getStatusMap(locale);
				model.addAttribute("statusMap", statusMap);

				defectStatusSummaryForm.setOrder("ASC");

				defectStatusSummaryForm.setFileFormat("pdf");

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();

				defectStatusSummaryForm.setFrom(dateFormat.format(new Date(cal
						.getTimeInMillis())));
				defectStatusSummaryForm.setTo(dateFormat.format(new Date(cal
						.getTimeInMillis())));

				Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
				Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
				Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

				List<Location> locationList = new ArrayList<Location>();
				locationList = locationManager.getLocationsBySiteKey(currRole
						.getSiteKey());

//				locationMap.put(null, pleaseSelect(locale));
//				for (int i = 0; i < locationList.size(); i++) {
//					locationMap.put(locationList.get(i).getKey(), locationList
//							.get(i).getCode()
//							+ " - "
//							+ locationList.get(i).getName());
//				}
//				model.addAttribute("locationList", locationMap);

				LocationTreeNode locationTree;
				locationTree = getLocationTreeNodeFormSession(request);
				defectStatusSummaryForm.setAvailableLocationTree(locationTree);
				defectStatusSummaryForm.setLocationKey(locationList.get(0)
						.getKey());

				List<FailureClass> failureClassList = new ArrayList<FailureClass>();

				failureClassList = failureClassManager
						.getFailureClassBySiteKey(currRole.getSiteKey());

				Collections
						.sort(failureClassList, new FailureClassComparator());

				failureClassMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < failureClassList.size(); i++) {
					failureClassMap.put(failureClassList.get(i).getKey(),
							setMap(failureClassList.get(i)));
				}
				model.addAttribute("failureClassList", failureClassMap);

				problemCodeMap.put(null, pleaseSelect(locale));
				model.addAttribute("problemCodeList", problemCodeMap);

				model.addAttribute("defectStatusSummaryForm",
						defectStatusSummaryForm);

				return "defect/report_defectStatusSummary";

			} else {
				// user does not have right to defect status summary report
				logger.debug("user does not have right to defect status summary report ");
				return "common/noprivilege";
			}

		} catch (MFMSException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			logger.debug("DefectStatusSummary.do Error");
			return "common/noprivilege";
		}
	}

	// TODO DoCreateDefectStatusSummary
	@RequestMapping(value = "/DoCreateDefectStatusSummary.do")
	public String doCreateDefectStatusSummaryForm(
			@ModelAttribute("defectStatusSummaryForm") DefectStatusSummaryForm defectStatusSummaryForm,
			BindingResult result, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, UnsupportedEncodingException {

		logger.debug("DoCreateDefectStatusSummary()");
		HttpSession session = request.getSession();
		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			defectStatusSummaryForm.setSiteKey(siteKey);
		}
		UserAccount account = (UserAccount) session.getAttribute("user");

		Map<String, String> sortByMap = new LinkedHashMap<String, String>();
		sortByMap.put(null, pleaseSelect(locale));
		sortByMap.put("status", "defect.status");
		sortByMap.put("failureClass", "defect.failureClass");
		sortByMap.put("problemCode", "defect.problemCode");
		sortByMap.put("location", "defect.location");
		sortByMap.put("priority", "defect.priority");

		Map<String, String> statusMap = getStatusMap(locale);

		model.addAttribute("statusMap", statusMap);
		model.addAttribute("sortByList", sortByMap);

		defectSummaryFormValidator.validate(defectStatusSummaryForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			return "defect/report_defectStatusSummary";
		}

		HashMap<String, Object> hmParams = new HashMap<String, Object>();

		// Site
		Integer siteKey = defectStatusSummaryForm.getSiteKey();

		Site thisSite = siteManager.getSiteByKey(siteKey);
		String siteName = (null == thisSite) ? "" : " - " + thisSite.getName();

		// Report Label
		hmParams.put(
				"labelDefectStatusSummary",
				messageSource.getMessage("menu.defectMgt.report.summary",
						null, locale) + siteName);
		hmParams.put("labelNumberOfWorkOrders",
				messageSource.getMessage("report.numberOfNumebr", null, locale));
		hmParams.put("labelStatus",
				messageSource.getMessage("defect.status", null, locale));
		hmParams.put("labelDefectNo",
				messageSource.getMessage("defect.code", null, locale));
		hmParams.put("labelIssuedDate",
				messageSource.getMessage("defect.issueDate", null, locale));
		hmParams.put("labelFailureClass",
				messageSource.getMessage("defect.failureClass", null, locale));
		hmParams.put("labelProblemCode",
				messageSource.getMessage("defect.problemCode", null, locale));
		hmParams.put("labelLcationDescription",
				messageSource.getMessage("defect.location", null, locale));
		hmParams.put("labelDefectDescription",
				messageSource.getMessage("defect.description", null, locale));
		hmParams.put("labelCallFrom",
				messageSource.getMessage("defect.callFrom", null, locale));
		hmParams.put("labelContactPhone",
				messageSource.getMessage("defect.contactTel", null, locale));
		hmParams.put("labelAssignedTo", messageSource.getMessage(
				"defect.assignedAccount", null, locale));
		hmParams.put("labelPriority",
				messageSource.getMessage("defect.priority", null, locale));
		hmParams.put("labelTargetAttendanceDate", messageSource.getMessage(
				"defect.targetFinishate", null, locale));
		hmParams.put("labelActualFinishDate", messageSource.getMessage(
				"defect.actualFinishDate", null, locale));
		hmParams.put("labelRemarks",
				messageSource.getMessage("defect.remarks", null, locale));
		hmParams.put("labelTotal",
				messageSource.getMessage("report.total", null, locale) + " : ");
		hmParams.put("labelPrintDate",
				messageSource.getMessage("report.print.date", null, locale)
						+ " : ");
		hmParams.put("labelPage",
				messageSource.getMessage("report.page", null, locale));
		hmParams.put("labelOf",
				messageSource.getMessage("report.of", null, locale));

		hmParams.put("labelN", statusManager.getStatusNameByStatusId("N"));
		hmParams.put("labelI", statusManager.getStatusNameByStatusId("I"));
		hmParams.put("labelP", statusManager.getStatusNameByStatusId("P"));
		hmParams.put("labelW", statusManager.getStatusNameByStatusId("W"));
		hmParams.put("labelC", statusManager.getStatusNameByStatusId("C"));
		hmParams.put("labelD", statusManager.getStatusNameByStatusId("D"));
		// End Report Label

		String reportFormat = defectStatusSummaryForm.getFileFormat();

		Timestamp from = convertStringToTimestamp(defectStatusSummaryForm
				.getFrom() + _from);
		Timestamp to = convertStringToTimestamp(defectStatusSummaryForm.getTo()
				+ _to);
		String[] statusMapValue = defectStatusSummaryForm.getStatusMap();
		Integer failureClassKey = defectStatusSummaryForm.getFailureClassKey();
		Integer problemCodeKey = defectStatusSummaryForm.getProblemCodeKey();
		Integer locationKey = defectStatusSummaryForm.getLocationKey();
		
		
//		List<DefectStatusSummaryReport> list = defectManager
//				.searchDefectStatusSummary(siteKey, from, to, account.getKey(),
//						statusMapValue);
		List<Integer> privilegedLocationKeyList = locationManager
				.getPrivilegedLocationKeyList(siteKey, account.getKey());
		
		List<DefectStatusSummaryReport> list = defectManager
				.searchDefectStatusSummary(siteKey, from, to, account.getKey(),
						statusMapValue, locationKey, failureClassKey, problemCodeKey, privilegedLocationKeyList);
		
		Integer countN = countStatus(list,
				statusManager.getStatusNameByStatusId("N"));
		Integer countI = countStatus(list,
				statusManager.getStatusNameByStatusId("I"));
		Integer countP = countStatus(list,
				statusManager.getStatusNameByStatusId("P"));
		Integer countW = countStatus(list,
				statusManager.getStatusNameByStatusId("W"));
		Integer countD = countStatus(list,
				statusManager.getStatusNameByStatusId("D"));
		Integer countC = countStatus(list,
				statusManager.getStatusNameByStatusId("C"));
		Integer countTotal = countN + countI + countP + countW + countD
				+ countC;

		hmParams.put("labelCountN", "" + countN);
		hmParams.put("labelCountI", "" + countI);
		hmParams.put("labelCountP", "" + countP);
		hmParams.put("labelCountW", "" + countW);
		hmParams.put("labelCountC", "" + countC);
		hmParams.put("labelCountD", "" + countD);
		hmParams.put("labelCountTotal", "" + countTotal);

		switch (defectStatusSummaryForm.getSortBy()) {
		case "location":
			Collections.sort(list, new ProblemCodeComparator2());
			Collections.sort(list, new FailureClassComparator2());
			Collections.sort(list, new LocationComparator2());
			break;
		case "failureClass":
			Collections.sort(list, new ProblemCodeComparator2());
			Collections.sort(list, new FailureClassComparator2());
			break;
		case "problemCode":
			Collections.sort(list, new FailureClassComparator2());
			Collections.sort(list, new ProblemCodeComparator2());
			break;
		case "priority":
			Collections.sort(list, new PriorityComparator2());
			break;
		case "status":
		default:
			Collections.sort(list, new LocationComparator2());
			Collections.sort(list, new ProblemCodeComparator2());
			Collections.sort(list, new FailureClassComparator2());
			Collections.sort(list, new StatusSeqComparator2());
			break;
		}

		if (!defectStatusSummaryForm.getOrder().equals("ASC"))
			Collections.reverse(list);

		Object[] data = list.toArray();

		if (data == null || data.length == 0) {
			data = new Object[1];
			data[0] = new DefectStatusSummaryReport();
		}

		String filePath = jasper.getExportFile(reportFormat,
				"defectStatusSummary_v2", "Defect_v2", hmParams, data);

		jasper.outPutFile(reportFormat, filePath, response);

		return null;
	}

	private Integer countStatus(List<DefectStatusSummaryReport> list,
			String status) {

		Integer count = 0;

		for (DefectStatusSummaryReport d : list) {

			if (d.getFieldStatus() != null) {

				logger.debug("*********  " + d.getFieldStatus());

				if (d.getFieldStatus().equals(status)) {
					count += Integer.parseInt(d.getFieldNumberOfWorkOrder());
				}
			}

		}

		return count;

	}

	// TODO Method

	private JasperReport getCompiledFile(String fileName,
			HttpServletRequest request) throws JRException,
			FileNotFoundException, UnsupportedEncodingException {

		ClassLoader classLoader = getClass().getClassLoader();
		String path = classLoader.getResource(fileName + ".jrxml").getFile();
		String path2 = URLDecoder.decode(path, "UTF-8");

		InputStream input = new FileInputStream(new File(path2));
		JasperReport jasperReport = JasperCompileManager.compileReport(input);

		return jasperReport;
	}

	private void generateReportPDF(HttpServletResponse response,
			Map parameters, JasperReport jasperReport,
			java.sql.Connection conn, String fileName) throws JRException,
			NamingException, SQLException, IOException {
		byte[] bytes = JasperRunManager.runReportToPdf(jasperReport,
				parameters, conn);
		response.setContentType("application/pdf");
		response.setContentLength(bytes.length);
		fileName = fileName + getCurrentTimeStamp() + ".pdf";
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.addCookie(new Cookie("fileDownloadToken", "fileDownloadToken"));
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();
	}

	private void generateReportXLS(HttpServletResponse response,
			Map parameters, JasperReport jasperReport,
			java.sql.Connection conn, String fileName)
			throws ClassNotFoundException, JRException {

		logger.debug("Downloading Excel report");

		// Creates the JasperPrint object
		// It needs a JasperReport layout and a datasource
		JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters,
				conn);

		// Create our output byte stream
		// This is the stream where the data will be written
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// Export to output stream
		// The data will be exported to the ByteArrayOutputStream baos
		// We delegate the exporting to a custom Exporter instance
		// The Exporter is a wrapper class I made. Feel free to remove or modify
		// it
		Exporter exporter = new Exporter();
		exporter.export(jp, baos);

		// Set our response properties
		// Here you can declare a custom filename
		fileName = fileName + getCurrentTimeStamp() + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.addCookie(new Cookie("fileDownloadToken", "fileDownloadToken"));
		// Make sure to set the correct content type
		// Each format has its own content type
		response.setContentType("application/vnd.ms-excel");
		response.setContentLength(baos.size());

		// Write to reponse stream
		writeReportToResponseStream(response, baos);
	}

	private void generateReportXLS(List<DefectExcel> list,
			HttpServletResponse response, Locale locale) throws IOException {
		// get data model which is passed by the Spring container
		List<DefectExcel> defectList = list;

		HSSFWorkbook workbook = new HSSFWorkbook();

		// create a new Excel sheet
		HSSFSheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth(18);

		// create style for header cells
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		// style.setFillForegroundColor(HSSFColor.AUTOMATIC.index);
		// style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);

		HSSFRow title = sheet.createRow(0);

		title.createCell(0).setCellValue(
				messageSource.getMessage("menu.defectMgt.report.defectList",
						null, locale));
		title.getCell(0).setCellStyle(style);

		HSSFRow printDate = sheet.createRow(1);
		printDate.createCell(0).setCellValue(
				messageSource.getMessage("report.print.date", null, locale)
						+ " : ");
		printDate.getCell(0).setCellStyle(style);
		printDate.createCell(1).setCellValue(getCurrentTimeString());

		HSSFRow total = sheet.createRow(2);

		total.createCell(0).setCellValue(
				messageSource.getMessage("report.total", null, locale) + " : ");
		total.getCell(0).setCellStyle(style);
		total.createCell(1).setCellValue(defectList.size());

		HSSFRow header = sheet.createRow(4);

		header.createCell(0).setCellValue(
				messageSource.getMessage("defect.code", null, locale));
		header.getCell(0).setCellStyle(style);

		header.createCell(1).setCellValue(
				messageSource.getMessage("defect.issueDate", null, locale));
		header.getCell(1).setCellStyle(style);

		header.createCell(2).setCellValue(
				messageSource.getMessage("defect.failureClass", null, locale));
		header.getCell(2).setCellStyle(style);

		header.createCell(3).setCellValue(
				messageSource.getMessage("defect.problemCode", null, locale));
		header.getCell(3).setCellStyle(style);

		header.createCell(4).setCellValue(
				messageSource.getMessage("defect.location", null, locale));
		header.getCell(4).setCellStyle(style);

		header.createCell(5).setCellValue(
				messageSource.getMessage("defect.description", null, locale));
		header.getCell(5).setCellStyle(style);

		header.createCell(6).setCellValue(
				messageSource.getMessage("defect.callFrom", null, locale));
		header.getCell(6).setCellStyle(style);

		header.createCell(7).setCellValue(
				messageSource.getMessage("defect.contactTel", null, locale));
		header.getCell(7).setCellStyle(style);

		header.createCell(8).setCellValue(
				messageSource
						.getMessage("defect.assignedAccount", null, locale));
		header.getCell(8).setCellStyle(style);

		header.createCell(9).setCellValue(
				messageSource.getMessage("defect.priority", null, locale));
		header.getCell(9).setCellStyle(style);

		header.createCell(10).setCellValue(
				messageSource.getMessage("defect.targetFinishDate", null,
						locale));
		header.getCell(10).setCellStyle(style);

		header.createCell(11).setCellValue(
				messageSource.getMessage("defect.actualFinishDate", null,
						locale));
		header.getCell(11).setCellStyle(style);

		header.createCell(12).setCellValue(
				messageSource.getMessage("report.kpi", null, locale));
		header.getCell(12).setCellStyle(style);

		header.createCell(13).setCellValue(
				messageSource.getMessage("report.deviation", null, locale));
		header.getCell(13).setCellStyle(style);

		header.createCell(14).setCellValue(
				messageSource.getMessage("defect.remarks", null, locale));
		header.getCell(14).setCellStyle(style);

		// create data rows
		int rowCount = 5;

		for (DefectExcel d : defectList) {
			HSSFRow aRow = sheet.createRow(rowCount++);
			aRow.createCell(0).setCellValue(d.getCode());
			aRow.createCell(1).setCellValue(d.getIssueDate());
			aRow.createCell(2).setCellValue(d.getFailureClass());
			aRow.createCell(3).setCellValue(d.getProblemCode());
			aRow.createCell(4).setCellValue(d.getLocation());
			aRow.createCell(5).setCellValue(d.getDesc());
			aRow.createCell(6).setCellValue(d.getCallFrom());
			aRow.createCell(7).setCellValue(d.getContactTel());
			aRow.createCell(8).setCellValue(d.getAccountId());
			aRow.createCell(9).setCellValue(d.getPriority());
			aRow.createCell(10).setCellValue(d.getTargetFinishDate());
			aRow.createCell(11).setCellValue(d.getActualFinishDate());
			aRow.createCell(12).setCellValue(d.getMeetKpi());
			aRow.createCell(13).setCellValue(d.getDeviation());
			aRow.createCell(14).setCellValue(d.getRemarks());
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		workbook.write(baos);

		String fileName = "Work Order List " + getCurrentTimeStamp() + ".xls";
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);
		response.addCookie(new Cookie("fileDownloadToken", "fileDownloadToken"));
		response.setContentType("application/vnd.ms-excel");
		response.setContentLength(baos.size());

		writeReportToResponseStream(response, baos);

	}

	/**
	 * Writes the report to the output stream
	 */
	private void writeReportToResponseStream(HttpServletResponse response,
			ByteArrayOutputStream baos) {

		logger.debug("Writing report to the stream");
		try {
			// Retrieve the output stream
			ServletOutputStream outputStream = response.getOutputStream();
			// Write to the output stream
			baos.writeTo(outputStream);
			// Flush the stream
			outputStream.flush();

		} catch (Exception e) {
			logger.error("Unable to write report to the output stream");
		}
	}

	public class Exporter {
		public void export(JasperPrint jp, ByteArrayOutputStream baos)
				throws JRException {
			// Create a JRXlsExporter instance
			JRXlsExporter exporter = new JRXlsExporter();

			// Here we assign the parameters jp and baos to the exporter
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

			// Excel specific parameters
			// Check the Jasper (not DynamicJasper) docs for a description of
			// these settings. Most are
			// self-documenting
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET,
					Boolean.FALSE);
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
					Boolean.TRUE);
			exporter.setParameter(
					JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND,
					Boolean.FALSE);

			// Retrieve the exported report in XLS format
			exporter.exportReport();
		}
	}

	public static String getCurrentTimeStamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat(" yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	public String pleaseSelect(Locale locale) {
		return messageSource.getMessage("common.pleaseSelect", null, locale);
	}

	public class AccountComparator implements Comparator<UserAccount> {
		@Override
		public int compare(UserAccount st, UserAccount nd) {
			return st.getLoginId().compareTo(nd.getLoginId());
		}
	}

	public String getCallFrom(String str) {

		String callFrom = "";

		switch (str) {
		case "H":
			callFrom = "defect.callFrom.handheld";
			break;
		case "L":
			callFrom = "defect.callFrom.landlord";
			break;
		case "S":
			callFrom = "defect.callFrom.staff";
			break;
		case "T":
			callFrom = "defect.callFrom.tenant";
			break;
		case "V":
			callFrom = "defect.callFrom.visitor";
			break;
		}

		return callFrom;

	}

	public class DefectCodeComparator implements Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			Integer str1 = Integer.valueOf(st.getFieldCode());
			Integer str2 = Integer.valueOf(nd.getFieldCode());

			return str1.compareTo(str2);
		}
	}

	public class DefectLocationComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = st.getFieldLocation();
			String str2 = nd.getFieldLocation();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectFailureClassComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = st.getFieldFailureClass();
			String str2 = nd.getFieldFailureClass();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectProblemCodeComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = st.getFieldProblemCode();
			String str2 = nd.getFieldProblemCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectAccountComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = st.getFieldAssignedAccount();
			String str2 = nd.getFieldAssignedAccount();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectPriorityComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			Integer str1 = Integer.parseInt(st.getFieldPriority());
			Integer str2 = Integer.parseInt(nd.getFieldPriority());

			return str1.compareTo(str2);
		}
	}

	public class DefectKpiComparator implements Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getFieldMeetKpi() == null ? "" : st.getFieldMeetKpi();
			str2 = nd.getFieldMeetKpi() == null ? "" : nd.getFieldMeetKpi();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectCallFromComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getFieldCallFrom() == null ? "" : st.getFieldCallFrom();
			str2 = nd.getFieldCallFrom() == null ? "" : nd.getFieldCallFrom();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectTargetFinishDateComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = st.getFieldTargetFinishDate() == null ? "" : st
					.getFieldTargetFinishDate();
			String str2 = nd.getFieldTargetFinishDate() == null ? "" : nd
					.getFieldTargetFinishDate();

			return str1.compareTo(str2);
		}
	}

	public class DefectActualFinishDateComparator implements
			Comparator<DefectListReport> {
		@Override
		public int compare(DefectListReport st, DefectListReport nd) {

			String str1 = st.getFieldActualFinishDate() == null ? "" : st
					.getFieldActualFinishDate();
			String str2 = nd.getFieldActualFinishDate() == null ? "" : nd
					.getFieldActualFinishDate();

			return str1.compareTo(str2);
		}
	}

	public List<UserAccount> getAccountList(HttpServletRequest request)
			throws MFMSException {

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return null;
		}

		List<UserAccount> accountList = new ArrayList<UserAccount>();

		List<UserAccountRole> siteUserAccountRoleList = accountManager
				.searchUserAccountRole(currRole.getSiteKey(), null, null, null,
						null);

		if (siteUserAccountRoleList != null) {
			for (UserAccountRole ar : siteUserAccountRoleList) {

				// do not add duplicate if any
				boolean exist = false;
				for (UserAccount a : accountList) {
					if (a.getLoginId().equals(ar.getUserAccount().getLoginId())) {
						exist = true;
					}
				}
				if (!exist) {
					accountList.add(ar.getUserAccount());
				}
			}

			Collections.sort(accountList, new AccountComparator());

			return accountList;
		}
		return null;
	}

	public class StatusComparator2 implements
			Comparator<DefectStatusSummaryReport> {
		@Override
		public int compare(DefectStatusSummaryReport st,
				DefectStatusSummaryReport nd) {

			String str1 = st.getFieldStatus() == null ? "" : st
					.getFieldStatus();
			String str2 = nd.getFieldStatus() == null ? "" : nd
					.getFieldStatus();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class StatusSeqComparator2 implements
			Comparator<DefectStatusSummaryReport> {
		@Override
		public int compare(DefectStatusSummaryReport st,
				DefectStatusSummaryReport nd) {

			String str1 = st.getFieldStatusSeq() == null ? "" : st
					.getFieldStatusSeq();
			String str2 = nd.getFieldStatusSeq() == null ? "" : nd
					.getFieldStatusSeq();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class FailureClassComparator2 implements
			Comparator<DefectStatusSummaryReport> {
		@Override
		public int compare(DefectStatusSummaryReport st,
				DefectStatusSummaryReport nd) {

			String str1 = st.getFieldFailureClass();
			String str2 = nd.getFieldFailureClass();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ProblemCodeComparator2 implements
			Comparator<DefectStatusSummaryReport> {
		@Override
		public int compare(DefectStatusSummaryReport st,
				DefectStatusSummaryReport nd) {

			String str1 = st.getFieldProblemCode();
			String str2 = nd.getFieldProblemCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class LocationComparator2 implements
			Comparator<DefectStatusSummaryReport> {
		@Override
		public int compare(DefectStatusSummaryReport st,
				DefectStatusSummaryReport nd) {

			String str1 = st.getFieldLocation();
			String str2 = nd.getFieldLocation();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PriorityComparator2 implements
			Comparator<DefectStatusSummaryReport> {
		@Override
		public int compare(DefectStatusSummaryReport st,
				DefectStatusSummaryReport nd) {

			String str1 = st.getFieldPriority();
			String str2 = nd.getFieldPriority();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public String setMap(UserAccount account) {
		if (account.getName().isEmpty()) {
			return account.getLoginId();
		} else
			return account.getLoginId() + " - " + account.getName();
	}

	public DefectManager getDefectManager() {
		return defectManager;
	}

	public void setDefectManager(DefectManager defectManager) {
		this.defectManager = defectManager;
	}

	private LocationTreeNode getLocationTreeNodeFormSession(
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		LocationTreeNode locationTree = (LocationTreeNode) session
				.getAttribute("locationTree");

		return locationTree;
	}

	public class StatusComparator1 implements Comparator<Status> {
		@Override
		public int compare(Status st, Status nd) {
			return st.getSequence().compareTo(nd.getSequence());
		}

	}

	private Map<String, String> getStatusMap(Locale locale) {

		Map<String, String> statusMap = new LinkedHashMap<String, String>();

		// Frequency[] frequencyArray = Frequency.class.getEnumConstants();

		List<Status> statusList;
		try {
			statusList = statusManager.getAllStatus();

			Collections.sort(statusList, new StatusComparator1());

			for (int i = 0; i < statusList.size(); i++) {

				statusMap.put(statusList.get(i).getStatusId(), statusList
						.get(i).getName());
			}
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return statusMap;
	}

	public String setMap(FailureClass failureClass) {
		if (failureClass.getName().isEmpty()) {
			return failureClass.getCode();
		} else
			return failureClass.getCode() + " - " + failureClass.getName();
	}

}
