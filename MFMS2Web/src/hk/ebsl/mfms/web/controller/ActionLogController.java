package hk.ebsl.mfms.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import hk.ebsl.mfms.dto.AccountActionLog;
import hk.ebsl.mfms.dto.AccountExport;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.DefectActionLogExport;
import hk.ebsl.mfms.dto.DefectScheduleActionLog;
import hk.ebsl.mfms.dto.PatrolScheduleActionLog;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RouteDefActionLog;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.DefectActionLogManager;
import hk.ebsl.mfms.manager.DefectScheduleActionLogManager;
import hk.ebsl.mfms.manager.PatrolScheduleActionLogManager;
import hk.ebsl.mfms.manager.RouteDefActionLogManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.utility.Utility;
import hk.ebsl.mfms.utility.writer.ColumnDisplay;
import hk.ebsl.mfms.utility.writer.ExcelWriter;
import hk.ebsl.mfms.web.controller.DefectController.AccountComparator;
import hk.ebsl.mfms.web.form.AccountActionForm;
import hk.ebsl.mfms.web.form.DefectActionForm;
import hk.ebsl.mfms.web.form.DefectScheduleActionForm;
import hk.ebsl.mfms.web.form.PatrolScheduleActionForm;
import hk.ebsl.mfms.web.form.RouteDefActionForm;
import hk.ebsl.mfms.web.form.SearchUserAccountForm;

@SessionAttributes({ "accountActionForm", "defectActionForm", "patrolScheduleActionForm", "defectScheduleActionForm",
		"routeDefActionForm" })

@Controller
public class ActionLogController {

	public final static Logger logger = Logger.getLogger(ActionLogController.class);

	@Autowired
	private Properties privilegeMap;
	@Autowired
	private UserAccountManager accountManager;
	@Autowired
	private AccountGroupManager accountGroupManager;
	@Autowired
	private DefectActionLogManager defectActionLogManager;
	@Autowired
	private AccountActionLogManager accountActionLogManager;
	@Autowired
	private RouteDefActionLogManager routeDefActionLogManager;
	@Autowired
	private PatrolScheduleActionLogManager patrolScheduleActionLogManager;
	@Autowired
	private DefectScheduleActionLogManager defectScheduleActionLogManager;
	@Autowired
	private AccountGroupAccountManager accountGroupAccountManager;
	@Autowired
	private StatusManager statusManager;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Properties propertyConfigurer;

	String _from = " 00:00:00";
	String _to = "  23:59:59";

	public String convertTimestampToString(Timestamp ts) {

		if (ts != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeString = sdf.format(ts);
			return timeString;
		} else
			return null;
	}

	public Timestamp convertStringToTimestamp(String str_date) {

		if (str_date != null) {

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
		return null;
	}

	public String getCurrentTimeString() {
		java.util.Date utilDate = new java.util.Date();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String timeString = sdf.format(timestamp);
		return timeString;
	}

	@RequestMapping(value = "/ActivityLog.do")
	public String showActionLogMenu(HttpServletRequest request, HttpServletResponse response) {

		try {
			return "actionLog/actionLog_menu";

		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/SearchDefectAction.do")
	public String showDefectActionForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}


		// check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.defectActionLog"));

		if (hasPrivilege) {
			List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
	
			List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
	
			accountDropDown = getAccountList(request);
	
			Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
	
			DefectActionForm defectActionForm = new DefectActionForm();
	
			defectActionForm.setFrom(getCurrentTimeString());
			defectActionForm.setTo(getCurrentTimeString());
	
			HttpSession session = request.getSession();
	
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				defectActionForm.setSiteKey(siteKey);
			}
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
	
			model.addAttribute("defectActionForm", defectActionForm);
	
			return "actionLog/actionLog_defectAction";
		} else {
			logger.debug("user does not have right to view defect action log");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchDefectAction.do")
	public String DoDefectActionForm(@ModelAttribute("defectActionForm") DefectActionForm defectActionForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		List<DefectActionLog> defectActionLogList = new ArrayList<DefectActionLog>();
		List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> actionTypeMap = new LinkedHashMap<String, String>();
		actionTypeMap.put("V", messageSource.getMessage("search.view", null, locale));
		actionTypeMap.put("C", messageSource.getMessage("search.create", null, locale));
		actionTypeMap.put("M", messageSource.getMessage("search.modify", null, locale));
		actionTypeMap.put("D", messageSource.getMessage("search.remove", null, locale));
		Map<Integer, String> dateMap = new LinkedHashMap<Integer, String>();

		defectActionForm.setActionTypeMap(actionTypeMap);

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			defectActionForm.setSiteKey(siteKey);
		}

		// check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.defectActionLog"));

		if (hasPrivilege) {
			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
	
			if (defectActionForm.getAccountGroupKey() != null) {
				agaList = accountGroupAccountManager.searchAccountGroupAccount(defectActionForm.getAccountGroupKey());
	
				for (int i = 0; i < agaList.size(); i++) {
					accountDropDown.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}
	
			} else
	
				accountDropDown = getAccountList(request);
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			Timestamp from = convertStringToTimestamp(defectActionForm.getFrom() + _from);
			Timestamp to = convertStringToTimestamp(defectActionForm.getTo() + _to);

			defectActionLogList = defectActionLogManager.searchDefectActionLog(currRole.getSiteKey(),
					defectActionForm.getAccountKey(), defectActionForm.getCode(), from, to,
					defectActionForm.getAccountGroupKey());

			Collections.reverse(defectActionLogList);

			for (int i = 0; i < defectActionLogList.size(); i++) {

				dateMap.put(defectActionLogList.get(i).getKey(),
						convertTimestampToString(defectActionLogList.get(i).getActionDateTime()));
			}

			defectActionForm.setDateMap(dateMap);
			defectActionForm.setCanGen(true);
			defectActionForm.setResultList(defectActionLogList);
			defectActionForm.setFullListSize(defectActionForm.getResultList().size());
			return "actionLog/actionLog_defectAction";
		} else {
			logger.debug("user does not have right to view defect action log");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoDefectActionDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void DoDefectActionDataTable(@ModelAttribute("defectActionForm") DefectActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "date", "name", "code", "type" };
		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<DefectActionLog> fullList = new ArrayList<DefectActionLog>();

		List<DefectActionLog> list = new ArrayList<DefectActionLog>();

		if (form.getCanGen()) {

			fullList = form.getResultList();

			switch (colName) {

			case "name":

				Collections.sort(fullList, new DALNameComparator());

				break;

			case "code":

				Collections.sort(fullList, new DALCodeComparator());

				break;

			case "type":

				Collections.sort(fullList, new DALTypeComparator());

				break;

			case "date":
				Collections.sort(fullList, new DALDateComparator());
				Collections.reverse(fullList);
				break;

			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					list.add(fullList.get(i));
			}

			fullListSize = fullList.size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < list.size(); i++) {

			JSONArray ja = new JSONArray();

			ja.put(convertTimestampToString(list.get(i).getActionDateTime()));
			ja.put(list.get(i).getAccount().getLoginId() + " - "
					+ Utility.replaceHtmlEntities(list.get(i).getAccount().getName()));
			ja.put(Utility.replaceHtmlEntities(list.get(i).getCode()));

			String type = list.get(i).getActionType();

			if (type.equals("V"))
				type = "View Defect";
			else if (type.equals("M"))
				type = "Modify Defect";
			else if (type.equals("D"))
				type = "Delete Defect";
			else if (type.equals("C"))
				type = "Create Defect";

			ja.put(type);
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}
	
	
	@RequestMapping(value = "/ExportDefectActionLog.do")
	public @ResponseBody void ExportDefectActionLog(@ModelAttribute("defectActionForm") DefectActionForm form,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception, MFMSException{
		logger.debug("ExportDefectActionLog.do");
		
		List<DefectActionLog> defectActionLogList = new ArrayList<DefectActionLog>(form.getResultList());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Calendar cal = Calendar.getInstance();
		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "DefectActionLog_" + cal.getTimeInMillis() + ".xlsx";
		File defectActionLogListFile = new File(fileName);
		
		FileOutputStream defectActionLogListStream = new FileOutputStream(defectActionLogListFile, false);
		
		ExcelWriter defectActionLogListWriter = new ExcelWriter(defectActionLogListStream);
		
		//header-

		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();
		
		ColumnDisplay actionDateCol = new ColumnDisplay();
		actionDateCol.setProperty("date");
		actionDateCol.setHeadingKey(messageSource.getMessage("action.date", null, locale));
		
		ColumnDisplay actionNameCol = new ColumnDisplay();
		actionNameCol.setProperty("name");
		actionNameCol.setHeadingKey(messageSource.getMessage("account.name", null, locale));
		
		ColumnDisplay actionCodeDateCol = new ColumnDisplay();
		actionCodeDateCol.setProperty("code");
		actionCodeDateCol.setHeadingKey(messageSource.getMessage("defect.code", null, locale));
		
		ColumnDisplay actionTypeCol = new ColumnDisplay();
		actionTypeCol.setProperty("actionType");
		actionTypeCol.setHeadingKey(messageSource.getMessage("action.type", null, locale));
		
		
		ColumnDisplay locationCol = new ColumnDisplay();
		locationCol.setProperty("location");
		locationCol.setHeadingKey(messageSource.getMessage("defect.location", null, locale));
		
		
		ColumnDisplay failureClassCol = new ColumnDisplay();
		failureClassCol.setProperty("failureClass");
		failureClassCol.setHeadingKey(messageSource.getMessage("defect.failureClass", null, locale));
		
		
		ColumnDisplay problemCodeCol = new ColumnDisplay();
		problemCodeCol.setProperty("problemCode");
		problemCodeCol.setHeadingKey(messageSource.getMessage("defect.problemCode", null, locale));
		
		
		ColumnDisplay causeCodeCol = new ColumnDisplay();
		causeCodeCol.setProperty("causeCode");
		causeCodeCol.setHeadingKey(messageSource.getMessage("defect.causeCode", null, locale));
		
		
		ColumnDisplay equipmentCol = new ColumnDisplay();
		equipmentCol.setProperty("equipment");
		equipmentCol.setHeadingKey(messageSource.getMessage("defect.equipment", null, locale));
		
		
		ColumnDisplay toolCol = new ColumnDisplay();
		toolCol.setProperty("tool");
		toolCol.setHeadingKey(messageSource.getMessage("defect.tools", null, locale));
		
		
		ColumnDisplay contactNameCol = new ColumnDisplay();
		contactNameCol.setProperty("contactName");
		contactNameCol.setHeadingKey(messageSource.getMessage("defect.contactName", null, locale));
		
		
		ColumnDisplay contactTelCol = new ColumnDisplay();
		contactTelCol.setProperty("contactTel");
		contactTelCol.setHeadingKey(messageSource.getMessage("defect.contactTel", null, locale));
		
		
		ColumnDisplay emailCol = new ColumnDisplay();
		emailCol.setProperty("email");
		emailCol.setHeadingKey(messageSource.getMessage("defect.contactEmail", null, locale));
		
		
		ColumnDisplay emergencyTelCol = new ColumnDisplay();
		emergencyTelCol.setProperty("emergencyTel");
		emergencyTelCol.setHeadingKey(messageSource.getMessage("defect.emergencyTel", null, locale));
		
		
		ColumnDisplay assignedGroupCol = new ColumnDisplay();
		assignedGroupCol.setProperty("assignedGroup");
		assignedGroupCol.setHeadingKey(messageSource.getMessage("defect.assignedGroup", null, locale));
		
		ColumnDisplay assignedAccountCol = new ColumnDisplay();
		assignedAccountCol.setProperty("assignedAccount");
		assignedAccountCol.setHeadingKey(messageSource.getMessage("defect.assignedAccount", null, locale));
		
		ColumnDisplay priorityCol = new ColumnDisplay();
		priorityCol.setProperty("priority");
		priorityCol.setHeadingKey(messageSource.getMessage("defect.priority", null, locale));
		
		
		ColumnDisplay descCol = new ColumnDisplay();
		descCol.setProperty("desc");
		descCol.setHeadingKey(messageSource.getMessage("defect.description", null, locale));
		
//		ColumnDisplay detailedDescCol = new ColumnDisplay();
//		detailedDescCol.setProperty("detailedDesc");
//		detailedDescCol.setHeadingKey(messageSource.getMessage("defect.detailed.description", null, locale));
		
		
		ColumnDisplay reportDateTimeCol = new ColumnDisplay();
		reportDateTimeCol.setProperty("reportDateTime");
		reportDateTimeCol.setHeadingKey(messageSource.getMessage("defect.reportDate", null, locale));
		
		
		ColumnDisplay callFromCol = new ColumnDisplay();
		callFromCol.setProperty("callFrom");
		callFromCol.setHeadingKey(messageSource.getMessage("defect.callFrom", null, locale));
		
		
		ColumnDisplay issureByCol = new ColumnDisplay();
		issureByCol.setProperty("issureBy");
		issureByCol.setHeadingKey(messageSource.getMessage("defect.issueBy", null, locale));
		
		
		ColumnDisplay issueDateTimeCol = new ColumnDisplay();
		issueDateTimeCol.setProperty("issueDateTime");
		issueDateTimeCol.setHeadingKey(messageSource.getMessage("defect.issueDate", null, locale));
		
		
		ColumnDisplay targetStartDateTimeCol = new ColumnDisplay();
		targetStartDateTimeCol.setProperty("targetStartDateTime");
		targetStartDateTimeCol.setHeadingKey(messageSource.getMessage("defect.targetStartDate", null, locale));
		
		ColumnDisplay targetAttendDateTimeCol = new ColumnDisplay();
		targetAttendDateTimeCol.setProperty("targetAttendDateTime");
		targetAttendDateTimeCol.setHeadingKey(messageSource.getMessage("defect.targetAttendDate", null, locale));
		
		
		ColumnDisplay actualStartDateTimeCol = new ColumnDisplay();
		actualStartDateTimeCol.setProperty("actualStartDateTime");
		actualStartDateTimeCol.setHeadingKey(messageSource.getMessage("defect.actualStartDate", null, locale));
		
		
		ColumnDisplay actualFinishDateTimeCol = new ColumnDisplay();
		actualFinishDateTimeCol.setProperty("actualFinishDateTime");
		actualFinishDateTimeCol.setHeadingKey(messageSource.getMessage("defect.actualFinishDate", null, locale));
		
		
		ColumnDisplay checkByCol = new ColumnDisplay();
		checkByCol.setProperty("checkBy");
		checkByCol.setHeadingKey(messageSource.getMessage("defect.checkBy", null, locale));
		
		
		ColumnDisplay checkDateTimeCol = new ColumnDisplay();
		checkDateTimeCol.setProperty("checkDateTime");
		checkDateTimeCol.setHeadingKey(messageSource.getMessage("defect.checkedDate", null, locale));
		
		
		ColumnDisplay statusIDCol = new ColumnDisplay();
		statusIDCol.setProperty("statusID");
		statusIDCol.setHeadingKey(messageSource.getMessage("defect.status", null, locale));
		
		
		ColumnDisplay remarksCol = new ColumnDisplay();
		remarksCol.setProperty("remarks");
		remarksCol.setHeadingKey(messageSource.getMessage("defect.remarks", null, locale));
		
		
//		ColumnDisplay createByCol = new ColumnDisplay();
//		createByCol.setProperty("createBy");
//		createByCol.setHeadingKey(messageSource.getMessage("defect.create.by", null, locale));
		
		
//		ColumnDisplay createDateTimeCol = new ColumnDisplay();
//		createDateTimeCol.setProperty("createDateTime");
//		createDateTimeCol.setHeadingKey(messageSource.getMessage("defect.createDateTime", null, locale));
		
		
//		ColumnDisplay lastModifyByCol = new ColumnDisplay();
//		lastModifyByCol.setProperty("lastModifyBy");
//		lastModifyByCol.setHeadingKey(messageSource.getMessage("defect.lastModifyBy", null, locale));
		
		
//		ColumnDisplay lastModifyDateTimeCol = new ColumnDisplay();
//		lastModifyDateTimeCol.setProperty("lastModifyDateTime");
//		lastModifyDateTimeCol.setHeadingKey(messageSource.getMessage("defect.lastModifyDateTime", null, locale));
		
//		ColumnDisplay actionByCol = new ColumnDisplay();
//		actionByCol.setProperty("actionBy");
//		actionByCol.setHeadingKey(messageSource.getMessage("defect.action.by", null, locale));
					
		columnDisplays.add(actionDateCol);
		columnDisplays.add(actionNameCol);
		columnDisplays.add(actionCodeDateCol);
		columnDisplays.add(actionTypeCol);
		columnDisplays.add(locationCol);
		columnDisplays.add(failureClassCol);
		columnDisplays.add(problemCodeCol);
		columnDisplays.add(causeCodeCol);
		columnDisplays.add(equipmentCol);
		columnDisplays.add(toolCol);
		columnDisplays.add(contactNameCol);
		columnDisplays.add(contactTelCol);
		columnDisplays.add(emailCol);
		columnDisplays.add(emergencyTelCol);
		columnDisplays.add(assignedGroupCol);
		columnDisplays.add(assignedAccountCol);
		columnDisplays.add(priorityCol);
		columnDisplays.add(descCol);
//		columnDisplays.add(detailedDescCol);
		columnDisplays.add(reportDateTimeCol);
		columnDisplays.add(callFromCol);
		columnDisplays.add(issureByCol);
		columnDisplays.add(issueDateTimeCol);
		columnDisplays.add(targetStartDateTimeCol);
		columnDisplays.add(targetAttendDateTimeCol);
		columnDisplays.add(actualStartDateTimeCol);
		columnDisplays.add(actualFinishDateTimeCol);
		columnDisplays.add(checkByCol);
		columnDisplays.add(checkDateTimeCol);
		columnDisplays.add(statusIDCol);
		columnDisplays.add(remarksCol);
//		columnDisplays.add(createByCol);
//		columnDisplays.add(createDateTimeCol);
//		columnDisplays.add(lastModifyByCol);
//		columnDisplays.add(lastModifyDateTimeCol);
//		columnDisplays.add(actionByCol);
		
		//Row content
		List<DefectActionLogExport> exportList = new ArrayList<DefectActionLogExport>();
		
		for(DefectActionLog dal : defectActionLogList){
			DefectActionLogExport export = new DefectActionLogExport();
			
			export.setDate(format.format(dal.getActionDateTime()));
			
			export.setName(dal.getAccount().getLoginId() + " - " + dal.getAccount().getName());
			
			export.setCode(dal.getCode());
			
			if(dal.getActionType().equals("V")){
				export.setActionType("View Defect");
			}else if(dal.getActionType().equals("M")){
				export.setActionType("Modify Defect");
			}else if(dal.getActionType().equals("D")){
				export.setActionType("Delete Defect");
			}else if(dal.getActionType().equals("C")){
				export.setActionType("Create Defect");
			}
			export.setLocation(dal.getLocation().getCode() + " - " + dal.getLocation().getName());
			
			export.setFailureClass(dal.getFailureClass().getCode() + " - " + dal.getFailureClass().getName());
			
			export.setProblemCode(dal.getProblemCode().getCode() + " - " + dal.getProblemCode().getName());
			
			if(dal.getCauseCode()!=null){
			export.setCauseCode(dal.getCauseCode().getCode() + " - " + dal.getCauseCode().getName());
			}
			if(dal.getTool()!=null){
			export.setTool(dal.getTool().getCode() + " - " + dal.getTool().getName());
			}
			if(dal.getEquipment()!=null){
			export.setEquipment(dal.getEquipment().getCode() + " - " + dal.getEquipment().getName());
			}
			
			export.setContactName(dal.getContactName());
			
			export.setContactTel(dal.getContactTel());
			
			export.setEmail(dal.getContactEmail());
			
			export.setEmergencyTel(dal.getEmergencyTel());
			
			if(dal.getAccountGroup()!=null){
			export.setAssignedGroup(dal.getAccountGroup().getName());
			}
			
			if(dal.getAssignedAccountKey()!=null){
				try{
					UserAccount ua = accountManager.getUserAccountByAccountKey(dal.getAssignedAccountKey());
					export.setAssignedAccount(ua.getLoginId() + " - " + ua.getName());
				}catch(Exception ex){
					ex.printStackTrace();
					export.setAssignedAccount("");
				}
			}
			
			export.setPriority(dal.getPriority());
			
			export.setDesc(dal.getDesc());
//			export.setDetailedDesc(dal.getDetailedDesc());
			if(dal.getReportDateTime()!=null){
			export.setReportDateTime(format.format(dal.getReportDateTime()));
			}
			
			if(dal.getCallFrom().equals("H")){
				export.setCallFrom(messageSource.getMessage("defect.callFrom.handheld", null, locale));
			}else if(dal.getCallFrom().equals("L")){
				export.setCallFrom(messageSource.getMessage("defect.callFrom.landlord", null, locale));
			}else if(dal.getCallFrom().equals("S")){
				export.setCallFrom(messageSource.getMessage("defect.callFrom.staff", null, locale));
			}else if(dal.getCallFrom().equals("T")){
				export.setCallFrom(messageSource.getMessage("defect.callFrom.tenant", null, locale));
			}else if(dal.getCallFrom().equals("V")){
				export.setCallFrom(messageSource.getMessage("defect.callFrom.visitor", null, locale));
			}else{
				export.setCallFrom("");
			}
			
			if(dal.getAccountIssueBy()!=null){
			export.setIssureBy(dal.getAccountIssueBy().getLoginId() + " - " + dal.getAccountIssueBy().getName());
			}
			
			if(dal.getIssueDateTime()!=null){
			export.setIssueDateTime(format.format(dal.getIssueDateTime()));
			}
			
			if(dal.getTargetStartDateTime()!=null){
			export.setTargetStartDateTime(format.format(dal.getTargetStartDateTime()));
			}
			
			if(dal.getTargetAttendDateTime()!=null){
			export.setTargetAttendDateTime(format.format(dal.getTargetAttendDateTime()));
			}
			
			if(dal.getActualStartDateTime()!=null){
			export.setActualStartDateTime(format.format(dal.getActualStartDateTime()));
			}
			
			if(dal.getActualFinishDateTime()!=null){
			export.setActualFinishDateTime(format.format(dal.getActualFinishDateTime()));
			}
			
			if(dal.getCheckBy()!=null && dal.getCheckBy()>0){
				try{
					UserAccount ua = accountManager.getUserAccountByAccountKey(dal.getCheckBy());
					export.setCheckBy(ua.getLoginId() + " - " + ua.getName());
				}catch(Exception ex){
					ex.printStackTrace();
					export.setCheckBy("");
				}
			}
			
			if(dal.getCheckDateTime()!=null){
				export.setCheckDateTime(format.format(dal.getCheckDateTime()));
			}
			
			if(dal.getStatusID()!=null){
				try{
					String status = statusManager.getStatusNameByStatusId(dal.getStatusID());
					export.setStatusID(status);
				}catch(Exception ex){
					ex.printStackTrace();
					export.setStatusID("");
				}
			}			
			export.setRemarks(dal.getRemarks());
			
//			export.setCreateBy(dal.getAccountCreateBy().getName());
			
//			export.setCreateDateTime(format.format(dal.getCreateDateTime()));
			
//			if(dal.getAccountLastModifyBy()!=null){
//			export.setLastModifyBy(dal.getAccountLastModifyBy().getName());
//			}
			
//			if(dal.getLastModifyDateTime()!=null){
//			export.setLastModifyDateTime(format.format(dal.getLastModifyDateTime()));
//			}
//			if(dal.getAccount()!=null){
//			export.setActionBy(dal.getAccount().getName());
//			}
			
			exportList.add(export);
		}
		
		defectActionLogListWriter.write(true, 0, "DefectActionLog", null, exportList, columnDisplays, false);

		defectActionLogListWriter.close();
		
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline;filename=\""
				+ defectActionLogListFile.getName() + "\"");
		
		ServletOutputStream servletOutputStream = response
				.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();
	}
	


	@RequestMapping(value = "/SearchAccountAction.do")
	public String SearchAccountAction(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.accountActionLog"));

		if (hasPrivilege) {
			List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
	
			accountDropDown = getAccountList(request);
	
			Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
			Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
			AccountActionForm accountActionForm = new AccountActionForm();
			HttpSession session = request.getSession();
	
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				accountActionForm.setSiteKey(siteKey);
			}
	
			accountActionForm.setFrom(getCurrentTimeString());
			accountActionForm.setTo(getCurrentTimeString());
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
			model.addAttribute("accountActionForm", accountActionForm);
	
			return "actionLog/actionLog_accountAction";
		} else {
			// user does not have right to view account action log
			logger.debug("user does not have right to account action log ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoSearchAccountAction.do")
	public String DoSearchAccountAction(@ModelAttribute("accountActionForm") AccountActionForm accountActionForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		List<AccountActionLog> accountActionLogList = new ArrayList<AccountActionLog>();
		List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();

		Map<Integer, String> dateMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			accountActionForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.accountActionLog"));

		if (hasPrivilege) {
			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
	
			if (accountActionForm.getAccountGroupKey() != null) {
	
				agaList = accountGroupAccountManager.searchAccountGroupAccount(accountActionForm.getAccountGroupKey());
				for (int i = 0; i < agaList.size(); i++) {
					accountDropDown.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}
	
			} else
	
				accountDropDown = getAccountList(request);
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
	
			try {
	
				Timestamp from = convertStringToTimestamp(accountActionForm.getFrom() + _from);
				Timestamp to = convertStringToTimestamp(accountActionForm.getTo() + _to);
	
				accountActionLogList = accountActionLogManager.searchAccountActionLog(currRole.getSiteKey(),
						accountActionForm.getAccountKey(), from, to, accountActionForm.getAccountGroupKey());
	
				accountActionForm.setCanGen(true);
	
				accountActionForm.setResultList(accountActionLogList);
				
				accountActionForm.setFullListSize(accountActionForm.getResultList().size());;
	
				return "actionLog/actionLog_accountAction";
	
			} catch (Exception e) {
				e.printStackTrace();
				return "common/notfound";
			}
		} else {
			// user does not have right to view account action log
			logger.debug("user does not have right to account action log ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "DoAccountActionDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void DoAccountActionDataTable(@ModelAttribute("accountActionForm") AccountActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "date", "name", "type", "result" };
		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<AccountActionLog> fullList = new ArrayList<AccountActionLog>();

		List<AccountActionLog> list = new ArrayList<AccountActionLog>();

		if (form.getCanGen()) {

			fullList = form.getResultList();

			switch (colName) {

			case "name":

				Collections.sort(fullList, new AALNameComparator());

				break;

			case "type":

				Collections.sort(fullList, new AALTypeComparator());

				break;

			case "result":

				Collections.sort(fullList, new AALResultComparator());

				break;

			case "date":
				Collections.sort(fullList, new AALDateComparator());
				Collections.reverse(fullList);
				break;

			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					list.add(fullList.get(i));
			}

			fullListSize = fullList.size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < list.size(); i++) {

			JSONArray ja = new JSONArray();

			ja.put(convertTimestampToString(list.get(i).getActionDateTime()));

			UserAccount acc = accountManager.getUserAccountByLoginId(list.get(i).getAccountId(), false);

			if (acc != null) {
				ja.put(acc.getLoginId() + " - " + Utility.replaceHtmlEntities(acc.getName()));

			} else
				ja.put(list.get(i).getAccountId());

			ja.put(list.get(i).getActionType());
			ja.put(list.get(i).getResult());
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/SearchRouteDefAction.do")
	public String SearchRouteDefAction(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.patrolRouteActionLog"));

		if (hasPrivilege) {
			List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
	
			accountDropDown = getAccountList(request);
	
			Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
			Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
			RouteDefActionForm routeDefActionForm = new RouteDefActionForm();
			HttpSession session = request.getSession();
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				routeDefActionForm.setSiteKey(siteKey);
			}
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
	
			routeDefActionForm.setFrom(getCurrentTimeString());
			routeDefActionForm.setTo(getCurrentTimeString());
	
			model.addAttribute("accountGroupList", accountGroupMap);
			model.addAttribute("routeDefActionForm", routeDefActionForm);
	
			return "actionLog/actionLog_routeDefAction";
		} else {
			// user does not have right to view patrol route action log
			logger.debug("user does not have right to view patrol route action log ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchRouteDefAction.do")
	public String DoSearchRouteDefAction(@ModelAttribute("routeDefActionForm") RouteDefActionForm routeDefActionForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		List<RouteDefActionLog> routeDefActionLogList = new ArrayList<RouteDefActionLog>();
		List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();

		Map<Integer, String> dateMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			routeDefActionForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.patrolRouteActionLog"));

		if (hasPrivilege) {
			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
	
			if (routeDefActionForm.getAccountGroupKey() != null) {
				agaList = accountGroupAccountManager.searchAccountGroupAccount(routeDefActionForm.getAccountGroupKey());
				for (int i = 0; i < agaList.size(); i++) {
					accountDropDown.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}
			} else
				accountDropDown = getAccountList(request);
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
	
			try {
	
				Timestamp from = convertStringToTimestamp(routeDefActionForm.getFrom() + _from);
				Timestamp to = convertStringToTimestamp(routeDefActionForm.getTo() + _to);
	
				routeDefActionLogList = routeDefActionLogManager.searchRouteDefActionLog(currRole.getSiteKey(),
						routeDefActionForm.getAccountKey(), null, from, to);
				Collections.reverse(routeDefActionLogList);
	
				for (int i = 0; i < routeDefActionLogList.size(); i++) {
	
					dateMap.put(routeDefActionLogList.get(i).getKey(),
							convertTimestampToString(routeDefActionLogList.get(i).getActionDateTime()));
				}
				routeDefActionForm.setDateMap(dateMap);
				logger.debug("routeDefActionLogList size: " + routeDefActionLogList.size());
				routeDefActionForm.setResultList(routeDefActionLogList);
				routeDefActionForm.setCanGen(true);
				routeDefActionForm.setFullListSize(routeDefActionForm.getResultList().size());	
				return "actionLog/actionLog_routeDefAction";
	
			} catch (Exception e) {
				e.printStackTrace();
				return "common/notfound";
			}
		} else {
			// user does not have right to view patrol route action log
			logger.debug("user does not have right to view patrol route action log ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "DoRouteDefActionDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void DoRouteDefActionDataTable(@ModelAttribute("routeDefActionForm") RouteDefActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "date", "name", "type" };
		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<RouteDefActionLog> fullList = new ArrayList<RouteDefActionLog>();

		List<RouteDefActionLog> list = new ArrayList<RouteDefActionLog>();

		if (form.getCanGen()) {

			fullList = form.getResultList();

			switch (colName) {

			case "name":

				Collections.sort(fullList, new RDALNameComparator());

				break;

			case "type":

				Collections.sort(fullList, new RDALTypeComparator());

				break;

			case "date":
				Collections.sort(fullList, new RDALDateComparator());
				Collections.reverse(fullList);
				break;

			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					list.add(fullList.get(i));
			}

			fullListSize = fullList.size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < list.size(); i++) {

			JSONArray ja = new JSONArray();

			ja.put(convertTimestampToString(list.get(i).getActionDateTime()));
			ja.put(list.get(i).getAccount().getLoginId() + " - "
					+ Utility.replaceHtmlEntities(list.get(i).getAccount().getName()));

			String type = list.get(i).getActionType();

			if (type.equals("V"))
				type = "View PatrolSchedule";
			else if (type.equals("M"))
				type = "Modify PatrolSchedule";
			else if (type.equals("D"))
				type = "Delete PatrolSchedule";
			else if (type.equals("C"))
				type = "Create PatrolSchedule";

			ja.put(type);

			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/SearchPatrolScheduleAction.do")
	public String SearchPatrolScheduleAction(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.patrolScheduleActionLog"));

		if (hasPrivilege) {
			List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
	
			accountDropDown = getAccountList(request);
	
			Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
			Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
			PatrolScheduleActionForm patrolScheduleActionForm = new PatrolScheduleActionForm();
			HttpSession session = request.getSession();
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				patrolScheduleActionForm.setSiteKey(siteKey);
			}
	
			patrolScheduleActionForm.setFrom(getCurrentTimeString());
			patrolScheduleActionForm.setTo(getCurrentTimeString());
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
			model.addAttribute("patrolScheduleActionForm", patrolScheduleActionForm);
	
			return "actionLog/actionLog_patrolScheduleAction";
		} else {
			// user does not have right to view patrol schedule action log
			logger.debug("user does not have right to view patrol schedule action log ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchPatrolScheduleAction.do")
	public String DoSearchPatrolScheduleAction(
			@ModelAttribute("patrolScheduleActionForm") PatrolScheduleActionForm patrolScheduleActionForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		List<PatrolScheduleActionLog> patrolScheduleActionLogList = new ArrayList<PatrolScheduleActionLog>();
		List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();

		Map<Integer, String> dateMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			patrolScheduleActionForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.patrolScheduleActionLog"));

		if (hasPrivilege) {
			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
	
			if (patrolScheduleActionForm.getAccountGroupKey() != null) {
	
				agaList = accountGroupAccountManager
						.searchAccountGroupAccount(patrolScheduleActionForm.getAccountGroupKey());
				for (int i = 0; i < agaList.size(); i++) {
					accountDropDown.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}
			} else
				accountDropDown = getAccountList(request);
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
	
			try {
	
				Timestamp from = convertStringToTimestamp(patrolScheduleActionForm.getFrom() + _from);
				Timestamp to = convertStringToTimestamp(patrolScheduleActionForm.getTo() + _to);
	
				patrolScheduleActionLogList = patrolScheduleActionLogManager.searchPatrolScheduleActionLog(
						currRole.getSiteKey(), patrolScheduleActionForm.getAccountKey(), null, from, to);
	
				Collections.reverse(patrolScheduleActionLogList);
	
				patrolScheduleActionForm.setCanGen(true);
	
				patrolScheduleActionForm.setResultList(patrolScheduleActionLogList);
				
				patrolScheduleActionForm.setFullListSize(patrolScheduleActionForm.getResultList().size());
	
				return "actionLog/actionLog_patrolScheduleAction";
	
			} catch (Exception e) {
				e.printStackTrace();
				return "common/notfound";
			}
		} else {
			// user does not have right to view patrol schedule action log
			logger.debug("user does not have right to view patrol schedule action log ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "DoPatrolScheduleActionDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void DoPatrolScheduleActionDataTable(
			@ModelAttribute("patrolScheduleActionForm") PatrolScheduleActionForm form, HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "date", "name", "type" };
		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<PatrolScheduleActionLog> fullList = new ArrayList<PatrolScheduleActionLog>();

		List<PatrolScheduleActionLog> list = new ArrayList<PatrolScheduleActionLog>();

		if (form.getCanGen()) {

			fullList = form.getResultList();

			switch (colName) {

			case "name":

				Collections.sort(fullList, new PSALNameComparator());

				break;

			case "type":

				Collections.sort(fullList, new PSALTypeComparator());

				break;

			case "date":
				Collections.sort(fullList, new PSALDateComparator());
				Collections.reverse(fullList);
				break;

			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					list.add(fullList.get(i));
			}

			fullListSize = fullList.size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < list.size(); i++) {

			JSONArray ja = new JSONArray();

			ja.put(convertTimestampToString(list.get(i).getActionDateTime()));
			ja.put(list.get(i).getAccount().getLoginId() + " - "
					+ Utility.replaceHtmlEntities(list.get(i).getAccount().getName()));

			String type = list.get(i).getActionType();

			if (type.equals("V"))
				type = "View PatrolSchedule";
			else if (type.equals("M"))
				type = "Modify PatrolSchedule";
			else if (type.equals("D"))
				type = "Delete PatrolSchedule";
			else if (type.equals("C"))
				type = "Create PatrolSchedule";

			ja.put(type);

			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/SearchDefectScheduleAction.do")
	public String SearchDefectScheduleAction(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.maintenanceScheduleActionLog"));

		if (hasPrivilege) {
			List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
	
			accountDropDown = getAccountList(request);
	
			Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
	
			Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
	
			DefectScheduleActionForm defectScheduleActionForm = new DefectScheduleActionForm();
	
			defectScheduleActionForm.setFrom(getCurrentTimeString());
			defectScheduleActionForm.setTo(getCurrentTimeString());
	
			HttpSession session = request.getSession();
	
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				defectScheduleActionForm.setSiteKey(siteKey);
			}
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
	
			model.addAttribute("defectScheduleActionForm", defectScheduleActionForm);
	
			return "actionLog/actionLog_defectScheduleAction";
		} else {
			// user does not have right to view maintenance schedule action log
			logger.debug("user does not have right to view maintenance schedule action log ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchDefectScheduleAction.do")
	public String DoSearchDefectScheduleAction(
			@ModelAttribute("defectScheduleActionForm") DefectScheduleActionForm defectScheduleActionForm,

			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		List<DefectScheduleActionLog> defectScheduleActionLogList = new ArrayList<DefectScheduleActionLog>();

		List<UserAccount> accountDropDown = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> actionTypeMap = new LinkedHashMap<String, String>();
		actionTypeMap.put("V", messageSource.getMessage("search.view", null, locale));
		actionTypeMap.put("C", messageSource.getMessage("search.create", null, locale));
		actionTypeMap.put("M", messageSource.getMessage("search.modify", null, locale));
		actionTypeMap.put("D", messageSource.getMessage("search.remove", null, locale));
		Map<Integer, String> dateMap = new LinkedHashMap<Integer, String>();

		defectScheduleActionForm.setActionTypeMap(actionTypeMap);

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			defectScheduleActionForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.maintenanceScheduleActionLog"));

		if (hasPrivilege) {
			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
	
			if (defectScheduleActionForm.getAccountGroupKey() != null) {
	
				agaList = accountGroupAccountManager
						.searchAccountGroupAccount(defectScheduleActionForm.getAccountGroupKey());
				for (int i = 0; i < agaList.size(); i++) {
					accountDropDown.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}
			} else
				accountDropDown = getAccountList(request);
	
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
	
			accountMap.put(null, "");
			for (int i = 0; i < accountDropDown.size(); i++) {
				accountMap.put(accountDropDown.get(i).getKey(), setMap(accountDropDown.get(i)));
			}
			model.addAttribute("accountList", accountMap);
	
			accountGroupMap.put(null, "");
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);
	
			Timestamp from = convertStringToTimestamp(defectScheduleActionForm.getFrom() + _from);
			Timestamp to = convertStringToTimestamp(defectScheduleActionForm.getTo() + _to);
	
			defectScheduleActionLogList = defectScheduleActionLogManager.searchDefectScheduleActionLog(
					currRole.getSiteKey(), defectScheduleActionForm.getAccountKey(), from, to,
					defectScheduleActionForm.getAccountGroupKey());
	
			Collections.reverse(defectScheduleActionLogList);
	
			for (int i = 0; i < defectScheduleActionLogList.size(); i++) {
	
				dateMap.put(defectScheduleActionLogList.get(i).getKey(),
						convertTimestampToString(defectScheduleActionLogList.get(i).getActionDateTime()));
			}
			defectScheduleActionForm.setDateMap(dateMap);
			defectScheduleActionForm.setResultList(defectScheduleActionLogList);
			defectScheduleActionForm.setCanGen(true);
			defectScheduleActionForm.setFullListSize(defectScheduleActionForm.getResultList().size());
	
			return "actionLog/actionLog_defectScheduleAction";
		} else {
			// user does not have right to view maintenance schedule action log
			logger.debug("user does not have right to view maintenance schedule action log ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoDefectScheduleActionDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void DoDefectScheduleActionDataTable(
			@ModelAttribute("defectScheduleActionForm") DefectScheduleActionForm form, HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "date", "name", "type", "desc" };
		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<DefectScheduleActionLog> fullList = new ArrayList<DefectScheduleActionLog>();

		List<DefectScheduleActionLog> list = new ArrayList<DefectScheduleActionLog>();

		if (form.getCanGen()) {

			fullList = form.getResultList();

			switch (colName) {

			case "name":

				Collections.sort(fullList, new DSALNameComparator());

				break;

			case "type":

				Collections.sort(fullList, new DSALTypeComparator());

				break;

			case "desc":

				Collections.sort(fullList, new DSALDescComparator());

				break;

			case "date":
				Collections.sort(fullList, new DSALDateComparator());
				Collections.reverse(fullList);
				break;

			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					list.add(fullList.get(i));
			}

			fullListSize = fullList.size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < list.size(); i++) {

			JSONArray ja = new JSONArray();

			ja.put(convertTimestampToString(list.get(i).getActionDateTime()));
			ja.put(list.get(i).getAccount().getLoginId() + " - "
					+ Utility.replaceHtmlEntities(list.get(i).getAccount().getName()));

			String type = list.get(i).getActionType();

			if (type.equals("V"))
				type = "View DefectSchedule";
			else if (type.equals("M"))
				type = "Modify DefectSchedule";
			else if (type.equals("D"))
				type = "Delete DefectSchedule";
			else if (type.equals("C"))
				type = "Create DefectSchedule";

			ja.put(type);

			ja.put(list.get(i).getDesc());

			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	public List<UserAccount> getAccountList(HttpServletRequest request) throws MFMSException {

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return null;
		}

		List<UserAccount> accountList = new ArrayList<UserAccount>();

		List<UserAccountRole> siteUserAccountRoleList = accountManager.searchUserAccountRole(currRole.getSiteKey(),
				null, null, null, null);

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

	public class AccountComparator implements Comparator<UserAccount> {
		@Override
		public int compare(UserAccount st, UserAccount nd) {
			return st.getLoginId().toLowerCase().compareTo(nd.getLoginId().toLowerCase());
		}
	}

	public String setMap(UserAccount account) {
		if (account.getName().isEmpty()) {
			return account.getLoginId();
		} else
			return account.getLoginId() + " - " + account.getName();
	}

	public class AALNameComparator implements Comparator<AccountActionLog> {
		@Override
		public int compare(AccountActionLog st, AccountActionLog nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getAccountId() == null ? "" : st.getAccountId();
			str2 = nd.getAccountId() == null ? "" : nd.getAccountId();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class AALTypeComparator implements Comparator<AccountActionLog> {
		@Override
		public int compare(AccountActionLog st, AccountActionLog nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getActionType() == null ? "" : st.getActionType();
			str2 = nd.getActionType() == null ? "" : nd.getActionType();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class AALResultComparator implements Comparator<AccountActionLog> {
		@Override
		public int compare(AccountActionLog st, AccountActionLog nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getResult() == null ? "" : st.getResult();
			str2 = nd.getResult() == null ? "" : nd.getResult();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class AALDateComparator implements Comparator<AccountActionLog> {
		@Override
		public int compare(AccountActionLog st, AccountActionLog nd) {

			Timestamp str1 = st.getActionDateTime();
			Timestamp str2 = nd.getActionDateTime();

			return str1.compareTo(str2);
		}
	}

	public class DALNameComparator implements Comparator<DefectActionLog> {
		@Override
		public int compare(DefectActionLog st, DefectActionLog nd) {

			String str1 = st.getAccount().getLoginId();
			String str2 = nd.getAccount().getLoginId();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DALTypeComparator implements Comparator<DefectActionLog> {
		@Override
		public int compare(DefectActionLog st, DefectActionLog nd) {

			String str1 = st.getActionType();
			String str2 = nd.getActionType();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DALCodeComparator implements Comparator<DefectActionLog> {
		@Override
		public int compare(DefectActionLog st, DefectActionLog nd) {

			Integer str1 = Integer.parseInt(st.getCode());
			Integer str2 = Integer.parseInt(nd.getCode());

			return str1.compareTo(str2);
		}
	}

	public class DALDateComparator implements Comparator<DefectActionLog> {
		@Override
		public int compare(DefectActionLog st, DefectActionLog nd) {

			Timestamp str1 = st.getActionDateTime();
			Timestamp str2 = nd.getActionDateTime();

			return str1.compareTo(str2);
		}
	}

	public class DSALTypeComparator implements Comparator<DefectScheduleActionLog> {
		@Override
		public int compare(DefectScheduleActionLog st, DefectScheduleActionLog nd) {

			String str1 = st.getActionType();
			String str2 = nd.getActionType();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DSALDescComparator implements Comparator<DefectScheduleActionLog> {
		@Override
		public int compare(DefectScheduleActionLog st, DefectScheduleActionLog nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DSALNameComparator implements Comparator<DefectScheduleActionLog> {
		@Override
		public int compare(DefectScheduleActionLog st, DefectScheduleActionLog nd) {

			String str1 = st.getAccount().getLoginId();
			String str2 = nd.getAccount().getLoginId();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DSALDateComparator implements Comparator<DefectScheduleActionLog> {
		@Override
		public int compare(DefectScheduleActionLog st, DefectScheduleActionLog nd) {

			Timestamp str1 = st.getActionDateTime();
			Timestamp str2 = nd.getActionDateTime();

			return str1.compareTo(str2);
		}
	}

	public class PSALTypeComparator implements Comparator<PatrolScheduleActionLog> {
		@Override
		public int compare(PatrolScheduleActionLog st, PatrolScheduleActionLog nd) {

			String str1 = st.getActionType();
			String str2 = nd.getActionType();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PSALNameComparator implements Comparator<PatrolScheduleActionLog> {
		@Override
		public int compare(PatrolScheduleActionLog st, PatrolScheduleActionLog nd) {

			String str1 = st.getAccount().getLoginId();
			String str2 = nd.getAccount().getLoginId();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PSALDateComparator implements Comparator<PatrolScheduleActionLog> {
		@Override
		public int compare(PatrolScheduleActionLog st, PatrolScheduleActionLog nd) {

			Timestamp str1 = st.getActionDateTime();
			Timestamp str2 = nd.getActionDateTime();

			return str1.compareTo(str2);
		}
	}

	public class RDALTypeComparator implements Comparator<RouteDefActionLog> {
		@Override
		public int compare(RouteDefActionLog st, RouteDefActionLog nd) {

			String str1 = st.getActionType();
			String str2 = nd.getActionType();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class RDALNameComparator implements Comparator<RouteDefActionLog> {
		@Override
		public int compare(RouteDefActionLog st, RouteDefActionLog nd) {

			String str1 = st.getAccount().getLoginId();
			String str2 = nd.getAccount().getLoginId();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class RDALDateComparator implements Comparator<RouteDefActionLog> {
		@Override
		public int compare(RouteDefActionLog st, RouteDefActionLog nd) {

			Timestamp str1 = st.getActionDateTime();
			Timestamp str2 = nd.getActionDateTime();

			return str1.compareTo(str2);
		}
	}

}
