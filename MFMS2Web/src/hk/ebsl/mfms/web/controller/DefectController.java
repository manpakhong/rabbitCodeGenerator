package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.AccountExport;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupExport;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.CauseCodeExport;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectActionLog;
import hk.ebsl.mfms.dto.DefectExport;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.EquipmentExport;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.FailureClassExport;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.LocationExport;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.ProblemCodeExport;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.dto.StatusFlowId;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.dto.ToolExport;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.LocationJSON;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.CauseCodeManager;
import hk.ebsl.mfms.manager.DefectActionLogManager;
import hk.ebsl.mfms.manager.DefectFileManager;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.manager.FailureClassManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.PriorityManager;
import hk.ebsl.mfms.manager.ProblemCodeManager;
import hk.ebsl.mfms.manager.StatusFlowManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.manager.SystemParamManager;
import hk.ebsl.mfms.manager.ToolManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.notification.impl.InformEscalatorImpl;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj.NotificationMessage;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj.NotificationRecipient;
import hk.ebsl.mfms.utility.DateUtil;
import hk.ebsl.mfms.utility.FileBucket;
import hk.ebsl.mfms.utility.NotificationUtil;
import hk.ebsl.mfms.utility.Utility;
import hk.ebsl.mfms.utility.writer.ColumnDisplay;
import hk.ebsl.mfms.utility.writer.ExcelWriter;
import hk.ebsl.mfms.web.form.CauseCodeForm;
import hk.ebsl.mfms.web.form.DefectForm;
import hk.ebsl.mfms.web.form.EquipmentForm;
import hk.ebsl.mfms.web.form.FailureClassForm;
import hk.ebsl.mfms.web.form.LocationForm;
import hk.ebsl.mfms.web.form.PatrolSearchForm;
import hk.ebsl.mfms.web.form.ProblemCodeForm;
import hk.ebsl.mfms.web.form.SearchAccountGroupForm;
import hk.ebsl.mfms.web.form.SearchCauseCodeForm;
import hk.ebsl.mfms.web.form.SearchDefectForm;
import hk.ebsl.mfms.web.form.SearchEquipmentForm;
import hk.ebsl.mfms.web.form.SearchFailureClassForm;
import hk.ebsl.mfms.web.form.SearchLocationForm;
import hk.ebsl.mfms.web.form.SearchProblemCodeForm;
import hk.ebsl.mfms.web.form.SearchToolForm;
import hk.ebsl.mfms.web.form.SearchUserAccountForm;
import hk.ebsl.mfms.web.form.ToolForm;
import hk.ebsl.mfms.web.form.validator.CauseCodeFormValidator;
import hk.ebsl.mfms.web.form.validator.DefectFormValidator;
import hk.ebsl.mfms.web.form.validator.DefectSearchFormValidator;
import hk.ebsl.mfms.web.form.validator.EquipmentFormValidator;
import hk.ebsl.mfms.web.form.validator.FailureClassFormValidator;
import hk.ebsl.mfms.web.form.validator.LocationFormValidator;
import hk.ebsl.mfms.web.form.validator.ProblemCodeFormValidator;
import hk.ebsl.mfms.web.form.validator.ToolFormValidator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes({ "searchToolForm", "searchCauseCodeForm", "searchFailureClassForm", "searchProblemCodeForm",
		"searchLocationForm", "searchEquipmentForm", "searchDefectForm", "defectForm" })
@Controller
public class DefectController {

	public final static Logger logger = Logger.getLogger(DefectController.class);

	@Autowired
	private Properties privilegeMap;
	@Autowired
	private DefectActionLogManager defectActionLogManager;
	@Autowired
	private PriorityManager priorityManager;
	@Autowired
	private FailureClassManager failureClassManager;
	@Autowired
	private ProblemCodeManager problemCodeManager;
	@Autowired
	private CauseCodeManager causeCodeManager;
	@Autowired
	private ToolManager toolManager;
	@Autowired
	private EquipmentManager equipmentManager;
	@Autowired
	private LocationManager locationManager;
	@Autowired
	private DefectManager defectManager;
	@Autowired
	private DefectScheduleManager defectScheduleManager;
	@Autowired
	private StatusManager statusManager;
	@Autowired
	private StatusFlowManager statusFlowManager;
	@Autowired
	private UserAccountManager accountManager;
	@Autowired
	private AccountGroupManager accountGroupManager;
	@Autowired
	private AccountGroupAccountManager accountGroupAccountManager;
	@Autowired
	private DefectFileManager defectFileManager;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private SystemParamManager systemParamManager;

	/*
	 * Validation
	 */

	@Autowired
	private ToolFormValidator toolFormValidator;
	@Autowired
	private CauseCodeFormValidator causeCodeFormValidator;
	@Autowired
	private EquipmentFormValidator equipmentFormValidator;
	@Autowired
	private ProblemCodeFormValidator problemCodeFormValidator;
	@Autowired
	private FailureClassFormValidator failureClassFormValidator;
	@Autowired
	private DefectFormValidator defectFormValidator;
	@Autowired
	private LocationFormValidator locationFormValidator;
	@Autowired
	private DefectSearchFormValidator defectSearchFormValidator;
	@Autowired
	private Properties propertyConfigurer;

	public static final String P_LOCATION = "photo/";
	public static final String V_LOCATION = "video/";

	// public static final String P_LOCATION = "/MFMS2Web/photo/";
	// public static final String V_LOCATION = "/MFMS2Web/video/";

	// public static final String P_LOCATION = "/MFMS2Web_Demo/photo/";
	// public static final String V_LOCATION = "/MFMS2Web_Demo/video/";

	public Timestamp convertStringToTimestamp(String str_date) throws ParseException {

		if (str_date != null && str_date.length() != 0) {

			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");

			Date date = (Date) formatter.parse(str_date);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			return timeStampDate;

		} else
			return null;
	}

	public String getCurrentTimeString() {
		java.util.Date utilDate = new java.util.Date();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
		String timeString = sdf.format(timestamp);
		return timeString;
	}

	public String getFileDate() {
		java.util.Date utilDate = new java.util.Date();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(utilDate.getTime());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
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

	public class LocationComparator implements Comparator<Location> {
		public int compare(Location st, Location nd) {

			int i = st.getLevelKey().compareTo(nd.getLevelKey());

			if (i == 0) {

				int j = st.getParent().getCode().toLowerCase().compareTo(nd.getParent().getCode().toLowerCase());

				if (j == 0) {

					int k = st.getCode().toLowerCase().compareTo(nd.getCode().toLowerCase());

					return k;
				} else
					return j;
			} else
				return i;

		}
	}

	public class FailureClassComparator implements Comparator<FailureClass> {
		@Override
		public int compare(FailureClass st, FailureClass nd) {
			return st.getCode().toLowerCase().compareTo(nd.getCode().toLowerCase());
		}
	}

	public class ProblemCodeComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {

			String a = st.getFailureClass().getCode().toLowerCase();
			String b = nd.getFailureClass().getCode().toLowerCase();

			if (a.compareTo(b) == 0) {

				String c = st.getCode().toLowerCase();
				String d = nd.getCode().toLowerCase();

				return c.compareTo(d);

			} else
				return a.compareTo(b);

		}
	}

	public class CauseCodeComparator implements Comparator<CauseCode> {
		@Override
		public int compare(CauseCode st, CauseCode nd) {
			return st.getCode().toLowerCase().compareTo(nd.getCode().toLowerCase());
		}
	}

	public class EquipmentComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {
			return st.getCode().toLowerCase().compareTo(nd.getCode().toLowerCase());
		}
	}

	public class ToolComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {
			return st.getCode().toLowerCase().compareTo(nd.getCode().toLowerCase());
		}
	}

	public class AccountComparator implements Comparator<UserAccount> {
		@Override
		public int compare(UserAccount st, UserAccount nd) {
			return st.getLoginId().toLowerCase().compareTo(nd.getLoginId().toLowerCase());
		}
	}

	public class AccountComparator2 implements Comparator<UserAccount> {
		@Override
		public int compare(UserAccount st, UserAccount nd) {
			return st.getName().toLowerCase().compareTo(nd.getName().toLowerCase());
		}
	}

	public class StatusComparator implements Comparator<StatusFlow> {
		@Override
		public int compare(StatusFlow st, StatusFlow nd) {

			Integer a = 0, b = 0;

			try {
				a = statusManager.getStatus(st.getId().getNextStatusId()).getSequence();
				b = statusManager.getStatus(nd.getId().getNextStatusId()).getSequence();
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return a.compareTo(b);
		}

	}

	public class StatusComparator2 implements Comparator<Status> {
		@Override
		public int compare(Status st, Status nd) {
			return st.getSequence().compareTo(nd.getSequence());
		}

	}

	public class StatusComparator3 implements Comparator<StatusFlow> {
		@Override
		public int compare(StatusFlow st, StatusFlow nd) {

			Integer a = 0, b = 0;

			try {
				a = statusManager.getStatus(st.getId().getStatusId()).getSequence();
				b = statusManager.getStatus(nd.getId().getStatusId()).getSequence();
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return a.compareTo(b);
		}

	}

	public static String getFileTimeString() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
		Date now = new Date();
		String strDate = sdfDate.format(now);
		return strDate;
	}

	@RequestMapping(value = "getProblemCodeByFailureClassKey.do", method = RequestMethod.GET)
	public @ResponseBody List<ProblemCode> getProblemCodeByFailureClassKey(
			@RequestParam(value = "failureClassKey", required = true) Integer failureClassKey) throws MFMSException {

		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();

		List<ProblemCode> tempList = new ArrayList<ProblemCode>();

		if (failureClassKey != null)
			tempList = problemCodeManager.searchProblemCode(null, null, null, failureClassKey, null);

		Collections.sort(tempList, new ProblemCodeComparator());

		for (ProblemCode pc : tempList) {
			ProblemCode problemCode = new ProblemCode();
			problemCode.setKey(pc.getKey());
			problemCode.setCode(pc.getCode());
			problemCode.setName(pc.getName());
			problemCode.setDesc(pc.getDesc());
			problemCodeList.add(problemCode);
		}

		return problemCodeList;

	}

	@RequestMapping(value = "/ProblemCodeDescription.do")
	public @ResponseBody void problemCodeDescription(HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException, Exception {

		Integer key = Integer.parseInt(request.getParameter("key"));

		ProblemCode pc = problemCodeManager.getProblemCodeByKey(key);
		if (pc != null) {
			response.getWriter().write(pc.getDesc());
		} else {
			response.getWriter().write("");
		}
	}

	@RequestMapping(value = "getEquipmentByLocation.do", method = RequestMethod.GET)
	public @ResponseBody List<Equipment> getEquipmentByLocation(
			@RequestParam(value = "locationKey", required = true) Integer locationKey) throws MFMSException {

		List<Equipment> equipmentList = new ArrayList<Equipment>();

		List<Equipment> tempList = new ArrayList<Equipment>();

		if (locationKey != null)
			tempList = equipmentManager.searchEquipment(null, locationKey, null, null);

		Collections.sort(tempList, new EquipmentComparator());

		for (Equipment e : tempList) {
			Equipment equipment = new Equipment();
			equipment.setKey(e.getKey());
			equipment.setCode(e.getCode());
			equipment.setName(e.getName());
			equipmentList.add(equipment);
		}
		return equipmentList;
	}

	@RequestMapping(value = "getPriorityByProblemCode.do", method = RequestMethod.GET)
	public @ResponseBody Integer getPriorityByProblemCode(
			@RequestParam(value = "problemCodeKey", required = true) Integer problemCodeKey) throws MFMSException {

		ProblemCode pc = problemCodeManager.getProblemCodeByKey(problemCodeKey);

		if (pc.getDefaultPriority() != null)
			return pc.getDefaultPriority();
		else
			return -1;

	}

	@RequestMapping(value = "getUserAccountByAccountGroup.do", method = RequestMethod.GET)
	public @ResponseBody List<UserAccount> getUserAccountByAccountGroup(HttpServletRequest request,
			@RequestParam(value = "accountGroupKey", required = true) Integer accountGroupKey) throws MFMSException {

		List<UserAccount> aList = new ArrayList<UserAccount>();
		List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
		if (accountGroupKey != null) {
			agaList = accountGroupAccountManager.searchAccountGroupAccount(accountGroupKey);
			for (int i = 0; i < agaList.size(); i++) {
				UserAccount userAccount = new UserAccount();
				userAccount.setName(setMap(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey())));
				userAccount.setKey(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()).getKey());
				aList.add(userAccount);
			}

			Collections.sort(aList, new AccountComparator2());

			return aList;
		} else {
			List<UserAccount> aList2 = new ArrayList<UserAccount>();
			aList = getAccountList(request);
			for (int i = 0; i < aList.size(); i++) {
				UserAccount userAccount = new UserAccount();
				userAccount.setName(setMap(accountManager.getUserAccountByAccountKey(aList.get(i).getKey())));
				userAccount.setKey(accountManager.getUserAccountByAccountKey(aList.get(i).getKey()).getKey());
				aList2.add(userAccount);
			}

			Collections.sort(aList2, new AccountComparator2());

			return aList2;
		}
	}

	@RequestMapping(value = "getDefaultAccountByProblemCode.do", method = RequestMethod.GET)
	public @ResponseBody List<UserAccount> getDefaultAccountByProblemCode(
			@RequestParam(value = "problemCodeKey", required = true) Integer problemCodeKey) throws MFMSException {

		List<UserAccount> aList = new ArrayList<UserAccount>();
		ProblemCode pc = new ProblemCode();

		pc = problemCodeManager.getProblemCodeByKey(problemCodeKey);
		UserAccount userAccount = new UserAccount();
		if (pc.getDefaultAccountKey() != null) {
			userAccount.setKey(accountManager.getUserAccountByAccountKey(pc.getDefaultAccountKey()).getKey());
			userAccount.setName(accountManager.getUserAccountByAccountKey(pc.getDefaultAccountKey()).getName());
		}

		aList.add(userAccount);

		return aList;
	}

	@RequestMapping(value = "setDefaultAccountGroup.do", method = RequestMethod.GET)
	public @ResponseBody Integer setDefaultAccountGroup(
			@RequestParam(value = "problemCodeKey", required = true) Integer problemCodeKey) throws MFMSException {

		List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
		ProblemCode pc = new ProblemCode();

		pc = problemCodeManager.getProblemCodeByKey(problemCodeKey);
		UserAccount userAccount = new UserAccount();
		if (pc.getDefaultAccountKey() != null) {
			/*
			 * userAccount =
			 * accountManager.getUserAccountByAccountKey(pc.getDefaultAccountKey
			 * ()); agaList =
			 * accountGroupAccountManager.checkAccountHasGroup(userAccount
			 * .getKey()); if (agaList.size() > 0) { Integer groupKey =
			 * agaList.get(0).getGroupKey();
			 * 
			 * return groupKey; }
			 */

			return pc.getDefaultAccountKey();
		}
		return 0;
	}

	@RequestMapping(value = "setDefaultAccount.do", method = RequestMethod.GET)
	public @ResponseBody Integer setDefaultAccount(
			@RequestParam(value = "problemCodeKey", required = true) Integer problemCodeKey) throws MFMSException {

		ProblemCode pc = new ProblemCode();

		pc = problemCodeManager.getProblemCodeByKey(problemCodeKey);
		UserAccount userAccount = new UserAccount();
		if (pc != null) {
			if (pc.getDefaultAccountKey() != null) {
				userAccount = accountManager.getUserAccountByAccountKey(pc.getDefaultAccountKey());
				return userAccount.getKey();
			}
		}

		return null;
	}

	/*
	 * Location
	 */

	// TODO: Location

	@RequestMapping(value = "/Location.do")
	public String showLocationMenu(HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("User requests to load location page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		return "defect/location_menu";

	}

	@RequestMapping(value = "/CreateLocation.do")
	public String showCreateLocationForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load create location page.");
		
		LocationForm locationForm = new LocationForm();

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			locationForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createLocation"));

		if (hasPrivilege) {
			LocationTreeNode locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(), account.getKey(),
					false);

			locationForm.setAvailableLocationTree(locationTree);

			model.addAttribute("locationForm", locationForm);

			return "defect/location_create_modify";
		} else {
			// user does not have right to create location
			logger.debug("user does not have right to create location ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ViewLocation.do")
	public String showViewLocationForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load view location page.");
		
		String locationKeyStr = request.getParameter("key");

		String referrerStr = request.getParameter("r");

		Integer locationKey = Integer.parseInt(locationKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchLocation"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createLocation"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyLocation"));

		if (hasPrivilege) {
			LocationForm locationForm = new LocationForm();

			Location viewLocation = locationManager.getLocationByKey(locationKey);

			if (viewLocation.getParentKey() != null && viewLocation.getParentKey() != 0) {
				String locationCode = locationManager.getLocationByKey(viewLocation.getParentKey()).getCode();
				String locationName = locationManager.getLocationByKey(viewLocation.getParentKey()).getName();
				model.addAttribute("selectedLocation", " :	" + locationCode + " - " + locationName);
			}

			LocationTreeNode locationTree = new LocationTreeNode();
			if (locationManager.getLocationByKey(viewLocation.getKey()).getLevelKey() != 1)
				locationTree.setLocation(locationManager.getLocationByKey(viewLocation.getKey()).getParent());

			if (viewLocation == null || currRole.getSiteKey() != viewLocation.getSiteKey())

				return "common/notfound";

			if (viewLocation.getParentKey() != 0)
				locationForm.setAvailableLocationTree(locationTree);
			locationForm.setKey(viewLocation.getKey());
			locationForm.setSiteKey(viewLocation.getSiteKey());
			locationForm.setCode(viewLocation.getCode());
			locationForm.setName(viewLocation.getName());
			locationForm.setDesc(viewLocation.getDesc());
			locationForm.setParentKey(viewLocation.getParentKey());
			locationForm.setTagId(viewLocation.getTagId());

			locationForm.setReferrer(referrerStr);

			// view set readonly
			locationForm.setReadOnly(true);
			locationForm.setDelete(false);

			model.addAttribute("locationForm", locationForm);

			return "defect/location_create_modify";
		} else {
			// user does not have right to view location
			logger.debug("user does not have right to view location ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ModifyLocation.do")
	public String showModifyLocationForm(@ModelAttribute("locationForm") LocationForm locationForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load modify location page.");
		
		String referrer = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyLocation"));

		if (hasPrivilege) {
			Location editLocation = null;

			editLocation = locationManager.getLocationByKey(locationForm.getKey());

			if (editLocation.getParentKey() != null && editLocation.getParentKey() != 0) {
				String locationName = locationManager.getLocationByKey(editLocation.getParentKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationName);
			}

			if (editLocation == null || currRole.getSiteKey() != editLocation.getSiteKey())
				return "common/notfound";

			LocationTreeNode locationTree = new LocationTreeNode();
			locationTree.setLocation(locationManager.getLocationByKey(editLocation.getParentKey()));
			locationForm.setAvailableLocationTree(locationTree);
			locationForm.setKey(editLocation.getKey());
			locationForm.setSiteKey(editLocation.getSiteKey());
			locationForm.setCode(editLocation.getCode());
			locationForm.setName(editLocation.getName());
			locationForm.setDesc(editLocation.getDesc());
			locationForm.setTagId(editLocation.getTagId());
			locationForm.setParentKey(editLocation.getParentKey());
			locationForm.setReferrer(referrer);

			locationForm.setReadOnly(false);

			return "defect/location_create_modify";
		} else {
			// user does not have right to modify location
			logger.debug("user does not have right to modify location");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoCreateModifyLocation.do")
	public String doCreateModifyLocation(@ModelAttribute("locationForm") LocationForm locationForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		if (locationForm.getParentKey() != null && locationForm.getParentKey() != 0) {
			String locationCode = locationManager.getLocationByKey(locationForm.getParentKey()).getCode();
			String locationName = locationManager.getLocationByKey(locationForm.getParentKey()).getName();
			model.addAttribute("selectedLocation", " :	" + locationCode + " - " + locationName);
		}

		Boolean isCreate = false;

		String referrerStr = "";

		// save location
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		if (locationForm.getKey() == null) {
			LocationTreeNode locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(), account.getKey(),
					false);
			locationForm.setAvailableLocationTree(locationTree);
		} else {
			LocationTreeNode locationTree = new LocationTreeNode();
			locationTree.setLocation(locationManager.getLocationByKey(locationForm.getParentKey()));
			locationForm.setAvailableLocationTree(locationTree);
		}

		Location location = null;

		locationForm.setSiteKey(currRole.getSiteKey());

		// validate
		locationFormValidator.validate(locationForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");
			return "defect/location_create_modify";
		}

		if (locationForm.getKey() == null) {

			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createLocation"))) {
				// create
				isCreate = true;
				location = new Location();
				referrerStr = "c";
				location.setName(locationForm.getName());
				location.setCode(locationForm.getCode());
				location.setDesc(locationForm.getDesc());
				location.setTagId(locationForm.getTagId());
				location.setSiteKey(currRole.getSiteKey());
				location.setCreatedBy(account.getKey());

				if (locationForm.getParentKey() != null) {
					location.setParentKey(locationForm.getParentKey());
					location.setChain(locationManager.getLocationByKey(locationForm.getParentKey()).getChain() + ","
							+ locationForm.getKey());
					location.setLevelKey(
							locationManager.getLocationByKey(locationForm.getParentKey()).getLevelKey() + 1);
				} else {
					location.setParentKey(0);
					location.setChain("temp");
					location.setLevelKey(1);
				}

				location.setCreateDateTime(new Timestamp(System.currentTimeMillis()));

				location.setDeleted("N");

			} else {
				logger.debug("user does not have right to create location");
				return "common/noprivilege";
			}
		} else {
			// modify
			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyLocation"))) {

				location = locationManager.getLocationByKey(locationForm.getKey());

				isCreate = false;
				referrerStr = "m";
				location.setName(locationForm.getName());
				location.setDesc(locationForm.getDesc());
				location.setCode(locationForm.getCode());
				location.setTagId(locationForm.getTagId());
				location.setLastModifyBy(account.getKey());

				location.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
				if (locationForm.getParentKey() == null) {
					location.setParentKey(0);
					location.setLevelKey(1);
				}

			} else {
				logger.debug("user does not have right to modify location");
				return "common/noprivilege";
			}
		}

		if (isCreate) {
			int newLocationKey = locationManager.saveLocation(location);
			Location newLocation = locationManager.getLocationByKey(newLocationKey);
			if (newLocation.getParentKey() != 0) {
				newLocation.setChain(
						locationManager.getLocationByKey(newLocation.getParentKey()).getChain() + "," + newLocationKey);
			} else {
				newLocation.setChain("" + newLocationKey);
			}
			locationManager.saveLocation(newLocation);

			LocationPrivilege lp = new LocationPrivilege();

			lp.setLocationKey(newLocationKey);
			lp.setAccountKey(account.getKey());

			lp.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
			lp.setCreatedBy(account.getKey());

			lp.setDeleted("N");

			locationManager.saveLocationPrivilege(lp);

		} else {
			locationManager.saveLocation(location);
		}

		model.addAttribute("locationForm", locationForm);
		return "redirect:ViewLocation.do?key=" + location.getKey() + "&r=" + referrerStr;
	}

	@ModelAttribute("searchLocationForm")
	public SearchLocationForm populateSearchLocationFormForm() {
		return new SearchLocationForm();
	}

	@RequestMapping(value = "/SearchLocation.do")
	public String showSearchLocationForm(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		logger.debug("User requests to load search location page.");
		
		searchLocationForm = new SearchLocationForm();

		List<Location> locationList = new ArrayList<Location>();

		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchLocationForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchLocation"));

		if (hasPrivilege) {
			LocationTreeNode locationTree;
			locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(), account.getKey(), false);
			searchLocationForm.setAvailableLocationTree(locationTree);

			locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());
			searchLocationForm.setLocationList(locationList);

			logger.debug("locationList size(): " + locationList.size());

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getCode() + " - " + locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			searchLocationForm.setCanGen(true);

			Boolean canModify = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyLocation"));

			Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeLocation"));

			searchLocationForm.setCanModify(canModify);

			searchLocationForm.setCanRemove(canRemove);

			model.addAttribute("searchLocationForm", searchLocationForm);

			return "defect/location_search";
		} else {
			// user does not have right to search location
			logger.debug("user does not have right to search location ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoSearchLocation.do")
	public String doSearchLocationForm(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		List<Location> locationList = new ArrayList<Location>();

		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchLocationForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchLocation"));

		if (hasPrivilege) {
			locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());
			searchLocationForm.setLocationList(locationList);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getCode() + " - " + locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			if (searchLocationForm.getParentKey() != null) {
				String locationCode = locationManager.getLocationByKey(searchLocationForm.getParentKey()).getCode();
				String locationName = locationManager.getLocationByKey(searchLocationForm.getParentKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
			}

			LocationTreeNode locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(), account.getKey(),
					false);

			searchLocationForm.setAvailableLocationTree(locationTree);

			locationList = locationManager.searchLocation(searchLocationForm.getSiteKey(),
					searchLocationForm.getParentKey(), searchLocationForm.getCode(), searchLocationForm.getName());

			Collections.sort(locationList, new LocationComparator());

			searchLocationForm.setResultList(locationList);

			searchLocationForm.setFullListSize(searchLocationForm.getResultList().size());

			return "defect/location_search";
		} else {
			// user does not have right to search location
			logger.debug("user does not have right to search location ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "DoLocationDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "parent", "code", "name" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Location> fullList = new ArrayList<Location>();

		List<Location> locationList = new ArrayList<Location>();

		if (searchLocationForm.getCanGen()) {

			fullList = searchLocationForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new LocationComparator());

				break;

			case "parent":

				Collections.sort(fullList, new LocationComparator());

				break;

			case "code":

				Collections.sort(fullList, new LCodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new LNameComparator());

				break;

			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					locationList.add(fullList.get(i));
			}

			fullListSize = searchLocationForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < locationList.size(); i++) {

			JSONArray ja = new JSONArray();
			Location location = locationList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchLocationForm.getCanModify();
			boolean canRemove = searchLocationForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewLocation.do?key=" + location.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyLocation.do?key=" + location.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + location.getKey() + ", '"
						+ Utility.removeQuote(Utility.replaceHtmlEntities(location.getCode()))
						+ "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());

			// ja.put(location.getLevelKey());

			Location parent = location.getParent();

			String par = parent != null ? Utility.replaceHtmlEntities(
					location.getParent().getCode() + " - " + location.getParent().getName()) : " - ";

			ja.put(par);
			ja.put(Utility.replaceHtmlEntities(location.getCode()));
			ja.put(Utility.replaceHtmlEntities(location.getName()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/ExportLocation.do")
	public @ResponseBody void ExportLocation(
			@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm, HttpServletRequest request,
			HttpServletResponse response, Locale locale) throws Exception {
		logger.debug("ExportLotion.do");

		List<Location> locationList = new ArrayList<Location>(searchLocationForm.getResultList());

		Calendar cal = Calendar.getInstance();
		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "Location_" + cal.getTimeInMillis() + ".xlsx";
		File locationListFile = new File(fileName);

		FileOutputStream locationListStream = new FileOutputStream(locationListFile, false);

		ExcelWriter locationListWriter = new ExcelWriter(locationListStream);

		// header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

		ColumnDisplay locationParentCol = new ColumnDisplay();
		locationParentCol.setProperty("parent");
		locationParentCol.setHeadingKey(messageSource.getMessage("location.parent", null, locale));

		ColumnDisplay locationCodeCol = new ColumnDisplay();
		locationCodeCol.setProperty("code");
		locationCodeCol.setHeadingKey(messageSource.getMessage("location.code", null, locale));

		ColumnDisplay locationNameCol = new ColumnDisplay();
		locationNameCol.setProperty("name");
		locationNameCol.setHeadingKey(messageSource.getMessage("location.name", null, locale));

		ColumnDisplay locationDescCol = new ColumnDisplay();
		locationDescCol.setProperty("desc");
		locationDescCol.setHeadingKey(messageSource.getMessage("location.description", null, locale));

		ColumnDisplay locationTagIDCol = new ColumnDisplay();
		locationTagIDCol.setProperty("tagID");
		locationTagIDCol.setHeadingKey(messageSource.getMessage("location.tagid", null, locale));

		columnDisplays.add(locationParentCol);
		columnDisplays.add(locationCodeCol);
		columnDisplays.add(locationNameCol);
		columnDisplays.add(locationDescCol);
		columnDisplays.add(locationTagIDCol);

		// Row content
		List<LocationExport> exportList = new ArrayList<LocationExport>();

		for (Location location : locationList) {
			LocationExport export = new LocationExport();
			if (location.getParent() != null) {
				export.setParent(location.getParent().getCode() + " - " + location.getParent().getName());
			} else {
				export.setParent("-");
			}
			export.setCode(location.getCode());
			export.setName(location.getName());
			export.setDesc(location.getDesc());
			export.setTagID(location.getTagId());

			exportList.add(export);
		}

		locationListWriter.write(true, 0, "Location", null, exportList, columnDisplays, false);

		locationListWriter.close();

		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline;filename=\"" + locationListFile.getName() + "\"");

		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();
	}

	@RequestMapping(value = "/DoDeleteLocation.do")
	public String doDeleteLocation(@ModelAttribute("searchLocationForm") SearchLocationForm searchLocationForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		String locationKeyStr = request.getParameter("key");

		Integer locationKey = Integer.parseInt(locationKeyStr);

		Location targetLocation = locationManager.getLocationByKey(locationKey);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeLocation"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			if (defectManager.hasLocation(locationKey) || equipmentManager.hasEquipment(locationKey)) {

				searchLocationForm.setHasChild(false);
				searchLocationForm.setHasDefect(true);
				searchLocationForm.setDeleteSuccess(false);
				searchLocationForm.setDeletedName(targetLocation.getCode());

			} else if (locationManager.getChildSize(targetLocation) > 0) {

				searchLocationForm.setHasChild(true);
				searchLocationForm.setHasDefect(false);
				searchLocationForm.setDeleteSuccess(false);
				searchLocationForm.setDeletedName(targetLocation.getCode());

			} else {

				locationManager.deleteLocationByKey(account.getKey(), locationKey);
				searchLocationForm.setHasDefect(false);
				searchLocationForm.setDeleteSuccess(true);
				searchLocationForm.setDeletedName(targetLocation.getCode());

				List<LocationPrivilege> locationPrivilegelist = locationManager
						.getLocationPrivilegesByLocationKey(locationKey);

				int i = 0;

				for (LocationPrivilege lp : locationPrivilegelist) {
					lp.setDeleted("Y");
					lp.setLastModifyBy(account.getKey());
					lp.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
					locationManager.saveLocationPrivilege(lp);
					i++;
				}

				logger.debug("count: " + i);
			}

			return "redirect:DoSearchLocation.do";
		} else {
			// user does not have right to remove location
			logger.debug("user does not have right to remove location ");
			return "common/noprivilege";
		}
	}

	/*
	 * Failure Class
	 */

	// TODO: Failure Class

	@RequestMapping(value = "/FailureClass.do")
	public String showFailureClassMenu(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("User requests to load failure class page.");
		
		return "defect/failureClass_menu";

	}

	@RequestMapping(value = "/CreateFailureClass.do")
	public String showCreateFailureClassForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load create failure class page.");

		FailureClassForm failureClassForm = new FailureClassForm();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			failureClassForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createFailureClass"));

		if (hasPrivilege) {
			model.addAttribute("failureClassForm", failureClassForm);

			return "defect/failureClass_create_modify";
		} else {
			// user does not have right to create failure class
			logger.debug("user does not have right to create failure class ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ViewFailureClass.do")
	public String showViewFailureClassForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		
		logger.debug("User requests to load view failure class page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createFailureClass"))
				|| currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.modifyFailureClass"))
				|| currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.searchFailureClass"));

		if (hasPrivilege) {
			String failureClassKeyStr = request.getParameter("key");

			String referrerStr = request.getParameter("r");

			Integer failureClassKey = Integer.parseInt(failureClassKeyStr);

			request.getSession();

			FailureClass viewFailureClass = failureClassManager.getFailureClassByKey(failureClassKey);

			FailureClassForm failureClassForm = new FailureClassForm();

			failureClassForm.setKey(viewFailureClass.getKey());
			failureClassForm.setSiteKey(viewFailureClass.getSiteKey());
			failureClassForm.setCode(viewFailureClass.getCode());
			failureClassForm.setName(viewFailureClass.getName());
			failureClassForm.setDesc(viewFailureClass.getDesc());
			failureClassForm.setReferrer(referrerStr);

			// view set readonly
			failureClassForm.setReadOnly(true);
			failureClassForm.setDeleted(false);

			model.addAttribute("failureClassForm", failureClassForm);

			return "defect/failureClass_create_modify";
		} else {
			// user does not have right to view failure class
			logger.debug("user does not have right to view failure class ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ModifyFailureClass.do")
	public String showModifyFailureClassForm(@ModelAttribute("failureClassForm") FailureClassForm failureClassForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {
		
		logger.debug("User requests to load modify failure class page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyFailureClass"));

		if (hasPrivilege) {
			String referrer = request.getParameter("r");

			session.getAttribute("currRole");

			FailureClass editFailureClass = null;

			editFailureClass = failureClassManager.getFailureClassByKey(failureClassForm.getKey());

			failureClassForm.setKey(editFailureClass.getKey());
			failureClassForm.setSiteKey(editFailureClass.getSiteKey());
			failureClassForm.setCode(editFailureClass.getCode());
			failureClassForm.setName(editFailureClass.getName());
			failureClassForm.setDesc(editFailureClass.getDesc());
			failureClassForm.setReferrer(referrer);

			failureClassForm.setReadOnly(false);

			return "defect/failureClass_create_modify";
		} else {
			// user does not have right to modify failure class
			logger.debug("user does not have right to modify failure class ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoCreateModifyFailureClass.do")
	public String doCreateModifyFailureClass(@ModelAttribute("failureClassForm") FailureClassForm failureClassForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		String referrerStr = "";

		// save failureClass
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		FailureClass failureClass = null;

		failureClassForm.setSiteKey(currRole.getSiteKey());
		// validate
		failureClassFormValidator.validate(failureClassForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			return "defect/failureClass_create_modify";
		}

		if (failureClassForm.getKey() == null) {

			if (currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.createFailureClass"))) {
				// create
				failureClass = new FailureClass();
				referrerStr = "c";
				failureClass.setName(failureClassForm.getName());
				failureClass.setCode(failureClassForm.getCode());
				failureClass.setDesc(failureClassForm.getDesc());
				failureClass.setLevelKey(1);
				failureClass.setParentKey(0);
				failureClass.setDefaultAccountKey(-1);
				failureClass.setSiteKey(currRole.getSiteKey());
				failureClass.setCreatedBy(account.getKey());
				failureClass.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
				failureClass.setDeleted("N");

			} else {
				logger.debug("user does not have right to create failureClass");
				return "common/noprivilege";
			}
		} else {
			// modify
			if (currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyFailureClass"))) {

				failureClass = failureClassManager.getFailureClassByKey(failureClassForm.getKey());

				failureClass.setName(failureClassForm.getName());
				failureClass.setDesc(failureClassForm.getDesc());
				failureClass.setCode(failureClassForm.getCode());
				failureClass.setLastModifyBy(account.getKey());
				failureClass.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
				referrerStr = "m";

			} else {
				logger.debug("user does not have right to modify failureClass");
				return "common/noprivilege";
			}
		}

		failureClassManager.saveFailureClass(failureClass);

		return "redirect:ViewFailureClass.do?key=" + failureClass.getKey() + "&r=" + referrerStr;

	}

	@ModelAttribute("searchFailureClassForm")
	public SearchFailureClassForm populateSearchFailureClassFormForm() {
		return new SearchFailureClassForm(); // populates search form for the
												// first time
		// if its null
	}

	@RequestMapping(value = "/SearchFailureClass.do")
	public String showSearchFailureClassForm(
			@ModelAttribute("searchFailureClassForm") SearchFailureClassForm searchFailureClassForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load search failure class page.");

		searchFailureClassForm = new SearchFailureClassForm();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchFailureClassForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchFailureClass"));

		if (hasPrivilege) {
			model.addAttribute("searchFailureClassForm", searchFailureClassForm);

			return "defect/failureClass_search";
		} else {
			// user does not have right to search failure class
			logger.debug("user does not have right to search failure class ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoSearchFailureClass.do")
	public String doSearchFailureClassForm(
			@ModelAttribute("searchFailureClassForm") SearchFailureClassForm searchFailureClassForm,

			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchFailureClassForm.setSiteKey(siteKey);
		}

		List<FailureClass> failureClassList = failureClassManager.searchFailureClass(
				searchFailureClassForm.getSiteKey(), searchFailureClassForm.getCode(),
				searchFailureClassForm.getName());

		searchFailureClassForm.setCanGen(true);

		Boolean canModify = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyFailureClass"));

		Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeFailureClass"));

		searchFailureClassForm.setCanModify(canModify);

		searchFailureClassForm.setCanRemove(canRemove);

		searchFailureClassForm.setResultList(failureClassList);

		searchFailureClassForm.setFullListSize(searchFailureClassForm.getResultList().size());

		return "defect/failureClass_search";

	}

	@RequestMapping(value = "DoFailureClassDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(
			@ModelAttribute("searchFailureClassForm") SearchFailureClassForm searchFailureClassForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "code", "name", "desc" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<FailureClass> fullList = new ArrayList<FailureClass>();

		List<FailureClass> failureClassList = new ArrayList<FailureClass>();

		if (searchFailureClassForm.getCanGen()) {

			fullList = searchFailureClassForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new FCCodeComparator());

				break;

			case "code":

				Collections.sort(fullList, new FCCodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new FCNameComparator());

				break;

			case "desc":
				Collections.sort(fullList, new FCDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					failureClassList.add(fullList.get(i));
			}

			fullListSize = searchFailureClassForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < failureClassList.size(); i++) {

			JSONArray ja = new JSONArray();
			FailureClass failureClass = failureClassList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchFailureClassForm.getCanModify();
			boolean canRemove = searchFailureClassForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewFailureClass.do?key=" + failureClass.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyFailureClass.do?key=" + failureClass.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + failureClass.getKey() + ", '"
						+ Utility.removeQuote(Utility.replaceHtmlEntities(failureClass.getCode()))
						+ "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(failureClass.getCode()));
			ja.put(Utility.replaceHtmlEntities(failureClass.getName()));
			ja.put(Utility.replaceHtmlEntities(failureClass.getDesc()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "ExportFailureClass.do")
	public @ResponseBody void ExportFailureClass(
			@ModelAttribute("searchFailureClassForm") SearchFailureClassForm searchFailureClassForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {
		logger.debug("ExportFailureClass.do");

		List<FailureClass> failureClassList = new ArrayList<FailureClass>(searchFailureClassForm.getResultList());

		Calendar cal = Calendar.getInstance();
		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "FailureClass_" + cal.getTimeInMillis() + ".xlsx";
		File failureClassListFile = new File(fileName);

		FileOutputStream failureClassListStream = new FileOutputStream(failureClassListFile, false);

		ExcelWriter failureClassListWriter = new ExcelWriter(failureClassListStream);

		// header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

		ColumnDisplay failureCodeCol = new ColumnDisplay();
		failureCodeCol.setProperty("code");
		failureCodeCol.setHeadingKey(messageSource.getMessage("failureClass.code", null, locale));

		ColumnDisplay failureNameCol = new ColumnDisplay();
		failureNameCol.setProperty("name");
		failureNameCol.setHeadingKey(messageSource.getMessage("failureClass.name", null, locale));

		ColumnDisplay failureDescCol = new ColumnDisplay();
		failureDescCol.setProperty("desc");
		failureDescCol.setHeadingKey(messageSource.getMessage("failureClass.description", null, locale));

		columnDisplays.add(failureCodeCol);
		columnDisplays.add(failureNameCol);
		columnDisplays.add(failureDescCol);

		// Row content
		List<FailureClassExport> exportList = new ArrayList<FailureClassExport>();

		for (FailureClass fc : failureClassList) {
			FailureClassExport export = new FailureClassExport();
			export.setCode(fc.getCode());
			export.setName(fc.getName());
			export.setDesc(fc.getDesc());

			exportList.add(export);
		}

		failureClassListWriter.write(true, 0, "FailureClass", null, exportList, columnDisplays, false);

		failureClassListWriter.close();

		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline;filename=\"" + failureClassListFile.getName() + "\"");

		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();
	}

	@RequestMapping(value = "/DoDeleteFailureClass.do")
	public String doDeleteFailureClass(
			@ModelAttribute("searchFailureClassForm") SearchFailureClassForm searchFailureClassForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		String failureClassKeyStr = request.getParameter("key");

		logger.debug("doDeleteFailureClass()[" + failureClassKeyStr + "]");

		Integer failureClassKey = Integer.parseInt(failureClassKeyStr);

		FailureClass targetFailureClass = failureClassManager.getFailureClassByKey(failureClassKey);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeFailureClass"));
		if (hasPrivilege) {

			UserAccount account = (UserAccount) session.getAttribute("user");

			if (problemCodeManager.searchProblemCode(null, null, null, failureClassKey, null).size() > 0) {

				searchFailureClassForm.setHasChild(true);
				searchFailureClassForm.setDeleteSuccess(false);
				searchFailureClassForm.setDeletedName(targetFailureClass.getCode());

			} else {
				failureClassManager.deleteFailureClassByKey(account.getKey(), failureClassKey);
				searchFailureClassForm.setDeleteSuccess(true);
				searchFailureClassForm.setHasChild(false);
				searchFailureClassForm.setDeletedName(targetFailureClass.getCode());
			}

			return "redirect:DoSearchFailureClass.do";
		} else {
			// user does not have right to remove account
			logger.debug("user does not have right to create account ");
			return "common/noprivilege";
		}
	}

	/*
	 * Problem Code
	 */

	// TODO: Problem Code

	@RequestMapping(value = "/ProblemCode.do")
	public String showProblemCodeMenu(HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("User requests to load problem code page.");

		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		logger.debug("showProblemCodeMenu()");

		return "defect/problemCode_menu";

	}

	@RequestMapping(value = "/CreateProblemCode.do")
	public String showCreateProblemCodeForm(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		logger.debug("User requests to load create problem code page.");
		
		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();

		// List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();

		ProblemCodeForm problemCodeForm = new ProblemCodeForm();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			problemCodeForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createProblemCode"));

		if (hasPrivilege) {

			// accountList = getAccountList(request);

			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

			priorityList = priorityManager.getAllPriority();
			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			accountMap.put(null, pleaseSelect(locale));
			// for (int i = 0; i < accountList.size(); i++) {
			// accountMap.put(accountList.get(i).getKey(),
			// setMap(accountList.get(i)));
			// }
			for (AccountGroup gp : accountGroupList) {
				accountMap.put(gp.getKey(), setMap(gp));
			}

			// for (UserAccount ac : accountList)
			// logger.debug("id : " + ac.getLoginId());

			model.addAttribute("accountList", accountMap);

			model.addAttribute("problemCodeForm", problemCodeForm);

			return "defect/problemCode_create_modify";
		} else {
			// user does not have right to create Problem Code
			logger.debug("user does not have right to create Problem Code ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ViewProblemCode.do")
	public String showViewProblemCodeForm(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		logger.debug("User requests to load view problem code page.");
		
		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		//List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		String problemCodeKeyStr = request.getParameter("key");

		String referrerStr = request.getParameter("r");

		Integer problemCodeKey = Integer.parseInt(problemCodeKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchProblemCode"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createProblemCode"))
				|| currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.modifyProblemCode"));

		if (hasPrivilege) {
			ProblemCode viewProblemCode = null;

			viewProblemCode = problemCodeManager.getProblemCodeByKey(problemCodeKey);

			if (viewProblemCode == null || currRole.getSiteKey() != viewProblemCode.getSiteKey())

				return "common/notfound";

			ProblemCodeForm problemCodeForm = new ProblemCodeForm();

			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			priorityList = priorityManager.getAllPriority();
			//accountList = getAccountList(request);
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());

			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}

			model.addAttribute("priorityList", priorityMap);

			accountMap.put(null, pleaseSelect(locale));
//			for (int i = 0; i < accountList.size(); i++) {
//				accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
//			}
			
			for (AccountGroup gp : accountGroupList) {
				accountMap.put(gp.getKey(), setMap(gp));
			}
			
			model.addAttribute("accountList", accountMap);

			problemCodeForm.setKey(viewProblemCode.getKey());
			problemCodeForm.setSiteKey(viewProblemCode.getSiteKey());
			problemCodeForm.setDefaultAccountKey(viewProblemCode.getDefaultAccountKey());
			problemCodeForm.setCode(viewProblemCode.getCode());
			problemCodeForm.setName(viewProblemCode.getName());
			problemCodeForm.setDesc(viewProblemCode.getDesc());
			problemCodeForm.setParentKey(viewProblemCode.getParentKey());
			problemCodeForm.setDefaultPriority(viewProblemCode.getDefaultPriority());
			problemCodeForm.setReferrer(referrerStr);
			// view set readonly
			problemCodeForm.setReadOnly(true);
			problemCodeForm.setDeleted(false);

			model.addAttribute("problemCodeForm", problemCodeForm);

			return "defect/problemCode_create_modify";
		} else {
			// user does not have right to view problem code
			logger.debug("user does not have right to view problem code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ModifyProblemCode.do")
	public String showModifyProblemCodeForm(@ModelAttribute("problemCodeForm") ProblemCodeForm problemCodeForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		logger.debug("User requests to load modify problem code page.");
		
		String referrer = request.getParameter("r");

		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		//List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyProblemCode"));

		if (hasPrivilege) {
			ProblemCode editProblemCode = null;

			//accountList = getAccountList(request);
			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			priorityList = priorityManager.getAllPriority();
			Collections.sort(failureClassList, new FailureClassComparator());

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			accountMap.put(null, pleaseSelect(locale));
//			for (int i = 0; i < accountList.size(); i++) {
//				accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
//			}
			
			for (AccountGroup gp : accountGroupList) {
				accountMap.put(gp.getKey(), setMap(gp));
			}

			model.addAttribute("accountList", accountMap);

			editProblemCode = problemCodeManager.getProblemCodeByKey(problemCodeForm.getKey());

			if (editProblemCode == null || currRole.getSiteKey() != editProblemCode.getSiteKey())
				return "common/notfound";

			problemCodeForm.setKey(editProblemCode.getKey());
			problemCodeForm.setSiteKey(editProblemCode.getSiteKey());
			problemCodeForm.setCode(editProblemCode.getCode());
			problemCodeForm.setName(editProblemCode.getName());
			problemCodeForm.setDesc(editProblemCode.getDesc());
			problemCodeForm.setDefaultAccountKey(editProblemCode.getDefaultAccountKey());
			problemCodeForm.setParentKey(editProblemCode.getParentKey());
			problemCodeForm.setDefaultPriority(editProblemCode.getDefaultPriority());
			problemCodeForm.setReferrer(referrer);

			problemCodeForm.setReadOnly(false);

			return "defect/problemCode_create_modify";
		} else {
			// user does not have right to modify problem code
			logger.debug("user does not have right to modify problem code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoCreateModifyProblemCode.do")
	public String doCreateModifyProblemCode(@ModelAttribute("problemCodeForm") ProblemCodeForm problemCodeForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();

		// save problemCode
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		String referrerStr = "";

		accountList = getAccountList(request);
		failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
		Collections.sort(failureClassList, new FailureClassComparator());
		priorityList = priorityManager.getAllPriority();

		failureClassMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < failureClassList.size(); i++) {
			failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
		}
		model.addAttribute("failureClassList", failureClassMap);

		priorityMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < priorityList.size(); i++) {
			priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
		}
		model.addAttribute("priorityList", priorityMap);

		accountMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < accountList.size(); i++) {
			accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
		}
		model.addAttribute("accountList", accountMap);

		ProblemCode problemCode = null;

		// validate
		problemCodeFormValidator.validate(problemCodeForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			return "defect/problemCode_create_modify";
		}

		if (problemCodeForm.getKey() == null) {

			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createProblemCode"))) {
				// create
				problemCode = new ProblemCode();
				referrerStr = "c";
				problemCode.setName(problemCodeForm.getName());
				problemCode.setCode(problemCodeForm.getCode());
				problemCode.setDesc(problemCodeForm.getDesc());
				problemCode.setDefaultPriority(problemCodeForm.getDefaultPriority());
				problemCode.setLevelKey(2);
				problemCode.setParentKey(problemCodeForm.getParentKey());
				problemCode.setDefaultAccountKey(problemCodeForm.getDefaultAccountKey());
				problemCode.setSiteKey(currRole.getSiteKey());
				problemCode.setCreatedBy(account.getKey());
				problemCode.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
				problemCode.setDeleted("N");

			} else {
				logger.debug("user does not have right to create problemCode");
				return "common/noprivilege";
			}
		} else {
			// modify
			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyProblemCode"))) {

				problemCode = problemCodeManager.getProblemCodeByKey(problemCodeForm.getKey());

				if (problemCode == null || currRole.getSiteKey() != problemCode.getSiteKey()) {

					return "common/noprivilege";
				}
				referrerStr = "m";
				problemCode.setName(problemCodeForm.getName());
				problemCode.setDesc(problemCodeForm.getDesc());
				problemCode.setCode(problemCodeForm.getCode());
				problemCode.setDefaultAccountKey(problemCodeForm.getDefaultAccountKey());
				problemCode.setLastModifyBy(account.getKey());
				problemCode.setDefaultPriority(problemCodeForm.getDefaultPriority());
				problemCode.setParentKey(problemCodeForm.getParentKey());
				problemCode.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

			} else {
				logger.debug("user does not have right to modify problemCode");
				return "common/notfound";
			}
		}

		problemCodeManager.saveProblemCode(problemCode);

		return "redirect:ViewProblemCode.do?key=" + problemCode.getKey() + "&r=" + referrerStr;

	}

	@ModelAttribute("searchProblemCodeForm")
	public SearchProblemCodeForm populateSearchProblemCodeFormForm() {
		return new SearchProblemCodeForm(); // populates search form for the
											// first time
		// if its null
	}

	@RequestMapping(value = "/SearchProblemCode.do")
	public String showSearchProblemCodeForm(
			@ModelAttribute("searchProblemCodeForm") SearchProblemCodeForm searchProblemCodeForm,

			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		logger.debug("User requests to load search problem code page.");
		
		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchProblemCode"));

		if (hasPrivilege) {
			searchProblemCodeForm = new SearchProblemCodeForm();

			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());
			priorityList = priorityManager.getAllPriority();

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				searchProblemCodeForm.setSiteKey(siteKey);
			}

			model.addAttribute("searchProblemCodeForm", searchProblemCodeForm);

			return "defect/problemCode_search";
		} else {
			// user does not have right to search problem code
			logger.debug("user does not have right to search problem code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoSearchProblemCode.do")
	public String doSearchProblemCodeForm(
			@ModelAttribute("searchProblemCodeForm") SearchProblemCodeForm searchProblemCodeForm,

			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchProblemCode"));

		if (hasPrivilege) {
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				searchProblemCodeForm.setSiteKey(siteKey);
			}

			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());
			searchProblemCodeForm.setFailureClassList(failureClassList);

			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("failureClass", messageSource.getMessage("problemCode.failureClass", null, locale));
			sortByMap.put("code", messageSource.getMessage("problemCode.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("problemCode.name", null, locale));
			sortByMap.put("priority", messageSource.getMessage("problemCode.priority", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			priorityList = priorityManager.getAllPriority();

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			List<ProblemCode> problemCodeList = problemCodeManager.searchProblemCode(searchProblemCodeForm.getSiteKey(),
					searchProblemCodeForm.getCode(), searchProblemCodeForm.getName(),
					searchProblemCodeForm.getParentKey(), searchProblemCodeForm.getDefaultPriority());

			Collections.sort(problemCodeList, new ProblemCodeComparator());

			searchProblemCodeForm.setResultList(problemCodeList);

			searchProblemCodeForm.setCanGen(true);

			Boolean canModify = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyProblemCode"));

			Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeProblemCode"));

			searchProblemCodeForm.setCanModify(canModify);

			searchProblemCodeForm.setCanRemove(canRemove);

			searchProblemCodeForm.setFullListSize(searchProblemCodeForm.getResultList().size());

			return "defect/problemCode_search";
		} else {
			// user does not have right to search problem code
			logger.debug("user does not have right to search problem code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "DoProblemCodeDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(
			@ModelAttribute("searchProblemCodeForm") SearchProblemCodeForm searchProblemCodeForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "fc", "code", "name", "priority", "desc" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<ProblemCode> fullList = new ArrayList<ProblemCode>();

		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();

		if (searchProblemCodeForm.getCanGen()) {

			fullList = searchProblemCodeForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new ProblemCodeComparator());

				break;

			case "fc":

				Collections.sort(fullList, new ProblemCodeComparator());

				break;

			case "code":

				Collections.sort(fullList, new PCCodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new PCNameComparator());

				break;

			case "priority":

				Collections.sort(fullList, new PCPriorityComparator());

				break;

			case "desc":
				Collections.sort(fullList, new PCDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					problemCodeList.add(fullList.get(i));
			}

			fullListSize = searchProblemCodeForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < problemCodeList.size(); i++) {

			JSONArray ja = new JSONArray();
			ProblemCode problemCode = problemCodeList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchProblemCodeForm.getCanModify();
			boolean canRemove = searchProblemCodeForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewProblemCode.do?key=" + problemCode.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyProblemCode.do?key=" + problemCode.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + problemCode.getKey() + ", '"
						+ Utility.removeQuote(Utility.replaceHtmlEntities(problemCode.getCode()))
						+ "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(
					problemCode.getFailureClass().getCode() + " - " + problemCode.getFailureClass().getName()));
			ja.put(Utility.replaceHtmlEntities(problemCode.getCode()));
			ja.put(Utility.replaceHtmlEntities(problemCode.getName()));

			String priority = problemCode.getDefaultPriority() == null ? "-" : problemCode.getDefaultPriority() + "";

			ja.put(priority);
			ja.put(problemCode.getDesc());
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/ExportProblemCode.do")
	public @ResponseBody void ExportProblemCode(
			@ModelAttribute("searchProblemCodeForm") SearchProblemCodeForm searchProblemCodeForm,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws MFMSException, Exception {
		logger.debug("ExportProblemCode.do");

		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>(searchProblemCodeForm.getResultList());

		Calendar cal = Calendar.getInstance();
		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "ProblemCode_" + cal.getTimeInMillis() + ".xlsx";
		File problemCodeListFile = new File(fileName);

		FileOutputStream problemCodeListStream = new FileOutputStream(problemCodeListFile, false);

		ExcelWriter problemCodeListWriter = new ExcelWriter(problemCodeListStream);

		// header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();
		ColumnDisplay failureClassCol = new ColumnDisplay();
		failureClassCol.setProperty("failureClass");
		failureClassCol.setHeadingKey(messageSource.getMessage("problemCode.failureClass", null, locale));

		ColumnDisplay problemCodeCol = new ColumnDisplay();
		problemCodeCol.setProperty("code");
		problemCodeCol.setHeadingKey(messageSource.getMessage("problemCode.code", null, locale));

		ColumnDisplay ProblemNameCol = new ColumnDisplay();
		ProblemNameCol.setProperty("name");
		ProblemNameCol.setHeadingKey(messageSource.getMessage("problemCode.name", null, locale));

		ColumnDisplay ProblemDescCol = new ColumnDisplay();
		ProblemDescCol.setProperty("desc");
		ProblemDescCol.setHeadingKey(messageSource.getMessage("problemCode.description", null, locale));

		ColumnDisplay defaultAccountCol = new ColumnDisplay();
		defaultAccountCol.setProperty("defaultAccount");
		defaultAccountCol.setHeadingKey(messageSource.getMessage("problemCode.defaultAccount", null, locale));

		ColumnDisplay problemPriorityCol = new ColumnDisplay();
		problemPriorityCol.setProperty("priority");
		problemPriorityCol.setHeadingKey(messageSource.getMessage("problemCode.priority", null, locale));

		columnDisplays.add(failureClassCol);
		columnDisplays.add(problemCodeCol);
		columnDisplays.add(ProblemNameCol);
		columnDisplays.add(ProblemDescCol);
		columnDisplays.add(defaultAccountCol);
		columnDisplays.add(problemPriorityCol);

		// Row content
		List<ProblemCodeExport> exportList = new ArrayList<ProblemCodeExport>();

		for (ProblemCode pc : problemCodeList) {
			ProblemCodeExport export = new ProblemCodeExport();
			export.setFailureClass(pc.getFailureClass().getCode() + " - " + pc.getFailureClass().getName());
			export.setCode(pc.getCode());
			export.setName(pc.getName());
			export.setDesc(pc.getDesc());
			if (pc.getDefaultAccountKey() != null) {
				UserAccount ua = accountManager.getUserAccountByAccountKey(pc.getDefaultAccountKey());
				export.setDefaultAccount(ua.getLoginId() + " - " + ua.getName());
			} else {
				export.setDefaultAccount("");
			}
			export.setPriority(pc.getDefaultPriority() == null ? "" : String.valueOf(pc.getDefaultPriority()));
			exportList.add(export);
		}

		problemCodeListWriter.write(true, 0, "ProblemCode", null, exportList, columnDisplays, false);

		problemCodeListWriter.close();

		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline;filename=\"" + problemCodeListFile.getName() + "\"");

		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();
	}

	@RequestMapping(value = "/DoDeleteProblemCode.do")
	public String doDeleteProblemCode(
			@ModelAttribute("searchProblemCodeForm") SearchProblemCodeForm searchProblemCodeForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		String problemCodeKeyStr = request.getParameter("key");

		logger.debug("doDeleteProblemCode()[" + problemCodeKeyStr + "]");

		Integer problemCodeKey = Integer.parseInt(problemCodeKeyStr);

		ProblemCode targetProblemCode = problemCodeManager.getProblemCodeByKey(problemCodeKey);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeProblemCode"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			if (defectManager.searchDefect(null, null, null, null, null, null, problemCodeKey, null, null, null, null)
					.size() > 0) {
				searchProblemCodeForm.setHasDefect(true);
				searchProblemCodeForm.setDeleteSuccess(false);
				searchProblemCodeForm.setDeletedName(targetProblemCode.getCode());

			} else {

				problemCodeManager.deleteProblemCodeByKey(account.getKey(), problemCodeKey);
				searchProblemCodeForm.setHasDefect(false);
				searchProblemCodeForm.setDeleteSuccess(true);
				searchProblemCodeForm.setDeletedName(targetProblemCode.getCode());
			}

			return "redirect:DoSearchProblemCode.do";
		} else {
			// user does not have right to remove problem code
			logger.debug("user does not have right to remove problem code ");
			return "common/noprivilege";
		}
	}

	/*
	 * CauseCode
	 */

	// TODO: Cause Code

	@RequestMapping(value = "/CauseCode.do")
	public String showCauseCodeMenu(HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("User requests to load cause code page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		return "defect/causeCode_menu";

	}

	@RequestMapping(value = "/CreateCauseCode.do")
	public String showCreateCauseCodeForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load create cause code page.");

		CauseCodeForm causeCodeForm = new CauseCodeForm();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			causeCodeForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createCauseCode"));

		if (hasPrivilege) {
			model.addAttribute("causeCodeForm", causeCodeForm);

			return "defect/causeCode_create_modify";
		} else {
			// user does not have right to create cause code
			logger.debug("user does not have right to create cause code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ViewCauseCode.do")
	public String showViewCauseCodeForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load view cause code page.");
		
		String causeCodeKeyStr = request.getParameter("key");

		String referrerStr = request.getParameter("r");

		Integer causeCodeKey = Integer.parseInt(causeCodeKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchCauseCode"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createCauseCode"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyCauseCode"));

		if (hasPrivilege) {
			CauseCode viewCauseCode = causeCodeManager.getCauseCodeByKey(causeCodeKey);

			CauseCodeForm causeCodeForm = new CauseCodeForm();
			causeCodeForm.setKey(viewCauseCode.getKey());
			causeCodeForm.setSiteKey(viewCauseCode.getSiteKey());
			causeCodeForm.setCode(viewCauseCode.getCode());
			causeCodeForm.setName(viewCauseCode.getName());
			causeCodeForm.setDesc(viewCauseCode.getDesc());

			causeCodeForm.setReferrer(referrerStr);

			// view set readonly
			causeCodeForm.setReadOnly(true);
			causeCodeForm.setDeleted(false);

			model.addAttribute("causeCodeForm", causeCodeForm);

			return "defect/causeCode_create_modify";
		} else {
			// user does not have right to view cause code
			logger.debug("user does not have right to view cause code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ModifyCauseCode.do")
	public String showModifyCauseCodeForm(@ModelAttribute("causeCodeForm") CauseCodeForm causeCodeForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load modify cause code page.");
		
		String referrer = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyCauseCode"));

		if (hasPrivilege) {
			CauseCode editCauseCode = null;

			editCauseCode = causeCodeManager.getCauseCodeByKey(causeCodeForm.getKey());

			if (editCauseCode == null || currRole.getSiteKey() != editCauseCode.getSiteKey())
				return "common/notfound";

			causeCodeForm.setKey(editCauseCode.getKey());
			causeCodeForm.setSiteKey(editCauseCode.getSiteKey());
			causeCodeForm.setCode(editCauseCode.getCode());
			causeCodeForm.setName(editCauseCode.getName());
			causeCodeForm.setDesc(editCauseCode.getDesc());
			causeCodeForm.setReferrer(referrer);

			causeCodeForm.setReadOnly(false);

			return "defect/causeCode_create_modify";
		} else {
			// user does not have right to modify cause code
			logger.debug("user does not have right to modify cause code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoCreateModifyCauseCode.do")
	public String doCreateModifyCauseCode(@ModelAttribute("causeCodeForm") CauseCodeForm causeCodeForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		// save causeCode
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		String referrerStr = "";

		CauseCode causeCode = null;

		// validate
		causeCodeFormValidator.validate(causeCodeForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			return "defect/causeCode_create_modify";
		}

		if (causeCodeForm.getKey() == null) {

			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createCauseCode"))) {
				// create
				causeCode = new CauseCode();
				referrerStr = "c";
				causeCode.setName(causeCodeForm.getName());
				causeCode.setCode(causeCodeForm.getCode());
				causeCode.setDesc(causeCodeForm.getDesc());
				causeCode.setLevelKey(1);
				causeCode.setParentKey(0);
				// role.setSiteKey(roleForm.getSiteKey());
				causeCode.setSiteKey(currRole.getSiteKey());
				causeCode.setCreatedBy(account.getKey());
				causeCode.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
				causeCode.setDeleted("N");

			} else {
				logger.debug("user does not have right to create causeCode");
				return "common/noprivilege";
			}
		} else {
			// modify
			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyCauseCode"))) {

				referrerStr = "m";
				causeCode = causeCodeManager.getCauseCodeByKey(causeCodeForm.getKey());
				causeCode.setName(causeCodeForm.getName());
				causeCode.setDesc(causeCodeForm.getDesc());
				causeCode.setCode(causeCodeForm.getCode());
				causeCode.setLastModifyBy(account.getKey());
				causeCode.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

			} else {
				logger.debug("user does not have right to modify causeCode");
				return "common/noprivilege";
			}
		}

		causeCodeManager.saveCauseCode(causeCode);

		return "redirect:ViewCauseCode.do?key=" + causeCode.getKey() + "&r=" + referrerStr;

	}

	@ModelAttribute("searchCauseCodeForm")
	public SearchCauseCodeForm populateSearchCauseCodeFormForm() {
		return new SearchCauseCodeForm();
	}

	@RequestMapping(value = "/SearchCauseCode.do")
	public String showSearchCauseCodeForm(
			@ModelAttribute("searchCauseCodeForm") SearchCauseCodeForm searchCauseCodeForm, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {
		
		logger.debug("User requests to load search cause code page.");
		
		searchCauseCodeForm = new SearchCauseCodeForm();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchCauseCodeForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchCauseCode"));

		if (hasPrivilege) {
			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("code", messageSource.getMessage("causeCode.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("defect.name", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			model.addAttribute("searchCauseCodeForm", searchCauseCodeForm);

			return "defect/causeCode_search";
		} else {
			// user does not have right to search cause code
			logger.debug("user does not have right to search cause code ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoSearchCauseCode.do")
	public String doSearchCauseCodeForm(@ModelAttribute("searchCauseCodeForm") SearchCauseCodeForm searchCauseCodeForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchCauseCodeForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchCauseCode"));

		if (hasPrivilege) {
			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("code", messageSource.getMessage("causeCode.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("defect.name", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			List<CauseCode> causeCodeList = causeCodeManager.searchCauseCode(searchCauseCodeForm.getSiteKey(),
					searchCauseCodeForm.getCode(), searchCauseCodeForm.getName());

			searchCauseCodeForm.setCanGen(true);

			Boolean canModify = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyCauseCode"));

			Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeCauseCode"));

			searchCauseCodeForm.setCanModify(canModify);

			searchCauseCodeForm.setCanRemove(canRemove);

			searchCauseCodeForm.setResultList(causeCodeList);

			searchCauseCodeForm.setFullListSize(searchCauseCodeForm.getResultList().size());

			return "defect/causeCode_search";
		} else {
			// user does not have right to search cause code
			logger.debug("user does not have right to search cause code ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoCauseCodeDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchCauseCodeForm") SearchCauseCodeForm searchCauseCodeForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "code", "name", "desc" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<CauseCode> fullList = new ArrayList<CauseCode>();

		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();

		if (searchCauseCodeForm.getCanGen()) {

			fullList = searchCauseCodeForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new CCodeComparator());

				break;

			case "code":

				Collections.sort(fullList, new CCodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new CNameComparator());

				break;

			case "desc":
				Collections.sort(fullList, new CDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					causeCodeList.add(fullList.get(i));
			}

			fullListSize = searchCauseCodeForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < causeCodeList.size(); i++) {

			JSONArray ja = new JSONArray();
			CauseCode causeCode = causeCodeList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchCauseCodeForm.getCanModify();
			boolean canRemove = searchCauseCodeForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewCauseCode.do?key=" + causeCode.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyCauseCode.do?key=" + causeCode.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + causeCode.getKey() + ", '"
						+ Utility.removeQuote(Utility.replaceHtmlEntities(causeCode.getCode()))
						+ "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(causeCode.getCode()));
			ja.put(Utility.replaceHtmlEntities(causeCode.getName()));
			ja.put(Utility.replaceHtmlEntities(causeCode.getDesc()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "ExportCauseCode.do")
	public @ResponseBody void ExportCauseCode(
			@ModelAttribute("searchCauseCodeForm") SearchCauseCodeForm searchCauseCodeForm, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) throws MFMSException, Exception {
		logger.debug("ExportCauseCode.do");
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>(searchCauseCodeForm.getResultList());

		Calendar cal = Calendar.getInstance();

		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "CauseCode_" + cal.getTimeInMillis() + ".xlsx";
		File causeCodeListFile = new File(fileName);

		FileOutputStream causeCodeListStream = new FileOutputStream(causeCodeListFile, false);

		ExcelWriter causeCodeListWriter = new ExcelWriter(causeCodeListStream);

		// Header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

		ColumnDisplay causeCodeColumn = new ColumnDisplay();
		causeCodeColumn.setProperty("code");
		causeCodeColumn.setHeadingKey(messageSource.getMessage("causeCode.code", null, locale));

		ColumnDisplay causeNameColumn = new ColumnDisplay();
		causeNameColumn.setProperty("name");
		causeNameColumn.setHeadingKey(messageSource.getMessage("causeCode.name", null, locale));

		ColumnDisplay causeDescColumn = new ColumnDisplay();
		causeDescColumn.setProperty("desc");
		causeDescColumn.setHeadingKey(messageSource.getMessage("causeCode.description", null, locale));

		columnDisplays.add(causeCodeColumn);
		columnDisplays.add(causeNameColumn);
		columnDisplays.add(causeDescColumn);

		// Row
		List<CauseCodeExport> exportList = new ArrayList<CauseCodeExport>();

		for (CauseCode cc : causeCodeList) {
			CauseCodeExport export = new CauseCodeExport();
			export.setCode(cc.getCode());
			export.setName(cc.getName());
			export.setDesc(cc.getDesc());

			exportList.add(export);

		}

		causeCodeListWriter.write(true, 0, "AGA", null, exportList, columnDisplays, false);

		causeCodeListWriter.close();

		response.setHeader("Content-Type", "application/vnd.ms-excel");

		response.setHeader("Content-disposition", "inline;filename=\"" + causeCodeListFile.getName() + "\"");

		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();
	}

	@RequestMapping(value = "/DoDeleteCauseCode.do")
	public String doDeleteCauseCode(@ModelAttribute("searchCauseCodeForm") SearchCauseCodeForm searchCauseCodeForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		String causeCodeKeyStr = request.getParameter("key");

		logger.debug("doDeleteCauseCode()[" + causeCodeKeyStr + "]");

		Integer causeCodeKey;

		causeCodeKey = Integer.parseInt(causeCodeKeyStr);

		CauseCode targetCauseCode = causeCodeManager.getCauseCodeByKey(causeCodeKey);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeCauseCode"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			if (defectManager.hasCauseCode(causeCodeKey)) {

				searchCauseCodeForm.setHasDefect(true);
				searchCauseCodeForm.setDeleteSuccess(false);
				searchCauseCodeForm.setDeletedName(targetCauseCode.getCode());

			} else {

				causeCodeManager.deleteCauseCodeByKey(account.getKey(), causeCodeKey);
				searchCauseCodeForm.setHasDefect(false);
				searchCauseCodeForm.setDeleteSuccess(true);
				searchCauseCodeForm.setDeletedName(targetCauseCode.getCode());
			}

			return "redirect:DoSearchCauseCode.do";
		} else {
			// user does not have right to remove cause code
			logger.debug("user does not have right to remove cause code ");
			return "common/noprivilege";
		}

	}

	/*
	 * Tool
	 */

	// TODO: Tool

	@RequestMapping(value = "/Tool.do")
	public String showToolMenu(HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("User requests to load tool page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		logger.debug("showToolMenu()");

		return "defect/tool_menu";

	}

	@RequestMapping(value = "/CreateTool.do")
	public String showCreateToolForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load create tool page.");

		ToolForm toolForm = new ToolForm();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			toolForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createTools"));

		if (hasPrivilege) {
			model.addAttribute("toolForm", toolForm);

			return "defect/tool_create_modify";
		} else {
			// user does not have right to create tools
			logger.debug("user does not have right to create tools ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ViewTool.do")
	public String showViewToolForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load view tool page.");
		
		String toolKeyStr = request.getParameter("key");

		String referrerStr = request.getParameter("r");

		Integer toolKey = Integer.parseInt(toolKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchTools"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createTools"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyTools"));

		if (hasPrivilege) {
			Tool viewTool = toolManager.getToolByKey(toolKey);

			if (viewTool == null || currRole.getSiteKey() != viewTool.getSiteKey())
				return "common/notfound";

			ToolForm toolForm = new ToolForm();

			toolForm.setKey(viewTool.getKey());
			toolForm.setSiteKey(viewTool.getSiteKey());
			toolForm.setCode(viewTool.getCode());
			toolForm.setName(viewTool.getName());
			toolForm.setDesc(viewTool.getDesc());

			toolForm.setReferrer(referrerStr);

			// view set readonly
			toolForm.setReadOnly(true);
			toolForm.setDeleted(false);

			model.addAttribute("toolForm", toolForm);

			return "defect/tool_create_modify";
		} else {
			// user does not have right to view tools
			logger.debug("user does not have right to view tools ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ModifyTool.do")
	public String showModifyToolForm(@ModelAttribute("toolForm") ToolForm toolForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		logger.debug("User requests to load modify tool page.");
		
		String referrer = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyTools"));

		if (hasPrivilege) {
			Tool editTool = toolManager.getToolByKey(toolForm.getKey());

			if (editTool == null || currRole.getSiteKey() != editTool.getSiteKey())
				return "common/notfound";

			toolForm.setKey(editTool.getKey());
			toolForm.setSiteKey(editTool.getSiteKey());
			toolForm.setCode(editTool.getCode());
			toolForm.setName(editTool.getName());
			toolForm.setDesc(editTool.getDesc());
			toolForm.setReferrer(referrer);
			toolForm.setReadOnly(false);

			return "defect/tool_create_modify";
		} else {
			// user does not have right to modify tools
			logger.debug("user does not have right to modify tools ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoCreateModifyTool.do")
	public String doCreateModifyTool(@ModelAttribute("toolForm") ToolForm toolForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		// save tool
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		String referrerStr = "";

		Tool tool = null;

		// validate
		toolFormValidator.validate(toolForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			return "defect/tool_create_modify";
		}

		if (toolForm.getKey() == null) {

			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createTools"))) {
				// create
				tool = new Tool();
				referrerStr = "c";
				tool.setName(toolForm.getName());
				tool.setCode(toolForm.getCode());
				tool.setDesc(toolForm.getDesc());
				tool.setLevelKey(1);
				tool.setParentKey(0);
				tool.setSiteKey(currRole.getSiteKey());

				tool.setCreatedBy(account.getKey());
				tool.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
				tool.setDeleted("N");

			} else {
				logger.debug("user does not have right to create tool");
				return "common/noprivilege";
			}
		} else {
			// modify
			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyTools"))) {

				tool = toolManager.getToolByKey(toolForm.getKey());

				referrerStr = "m";
				tool.setName(toolForm.getName());
				tool.setDesc(toolForm.getDesc());
				tool.setCode(toolForm.getCode());
				tool.setLastModifyBy(account.getKey());

				tool.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

			} else {
				logger.debug("user does not have right to modify tool");
				return "common/noprivilege";
			}
		}

		toolManager.saveTool(tool);

		return "redirect:ViewTool.do?key=" + tool.getKey() + "&r=" + referrerStr;

	}

	@ModelAttribute("searchToolForm")
	public SearchToolForm populateSearchToolFormForm() {
		return new SearchToolForm();
	}

	@RequestMapping(value = "/SearchTool.do")
	public String showSearchToolForm(@ModelAttribute("searchToolForm") SearchToolForm searchToolForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load search tool page.");

		searchToolForm = new SearchToolForm();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchToolForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchTools"));

		if (hasPrivilege) {
			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("code", messageSource.getMessage("tool.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("tool.name", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			model.addAttribute("searchToolForm", searchToolForm);

			return "defect/tool_search";
		} else {
			// user does not have right to search tools
			logger.debug("user does not have right to search tools ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchTool.do")
	public String doSearchToolForm(@ModelAttribute("searchToolForm") SearchToolForm searchToolForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchToolForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchTools"));

		if (hasPrivilege) {
			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("code", messageSource.getMessage("tool.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("tool.name", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			List<Tool> toolList = toolManager.searchTool(searchToolForm.getSiteKey(), searchToolForm.getCode(),
					searchToolForm.getName());

			searchToolForm.setResultList(toolList);

			searchToolForm.setCanGen(true);

			Boolean canModify = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyTools"));

			Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeTools"));

			searchToolForm.setCanModify(canModify);

			searchToolForm.setCanRemove(canRemove);

			searchToolForm.setFullListSize(searchToolForm.getResultList().size());

			return "defect/tool_search";
		} else {
			// user does not have right to search tools
			logger.debug("user does not have right to search tools ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoToolDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchToolForm") SearchToolForm searchToolForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "code", "name", "desc" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Tool> fullList = new ArrayList<Tool>();

		List<Tool> toolList = new ArrayList<Tool>();

		if (searchToolForm.getCanGen()) {

			fullList = searchToolForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new TCodeComparator());

				break;

			case "code":

				Collections.sort(fullList, new TCodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new TNameComparator());

				break;

			case "desc":
				Collections.sort(fullList, new TDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					toolList.add(fullList.get(i));
			}

			fullListSize = searchToolForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < toolList.size(); i++) {

			JSONArray ja = new JSONArray();
			Tool tool = toolList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchToolForm.getCanModify();
			boolean canRemove = searchToolForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewTool.do?key=" + tool.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyTool.do?key=" + tool.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + tool.getKey() + ", '"
						+ Utility.removeQuote(Utility.replaceHtmlEntities(tool.getCode()))
						+ "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(tool.getCode()));
			ja.put(Utility.replaceHtmlEntities(tool.getName()));
			ja.put(Utility.replaceHtmlEntities(tool.getDesc()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "ExportTool.do")
	public @ResponseBody void ExportTool(@ModelAttribute("searchToolForm") SearchToolForm searchToolForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {
		logger.debug("ExportTool.do");

		List<Tool> toolList = new ArrayList<Tool>(searchToolForm.getResultList());

		Calendar cal = Calendar.getInstance();

		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "Tool_" + cal.getTimeInMillis() + ".xlsx";
		File toolListFile = new File(fileName);

		FileOutputStream toolListStream = new FileOutputStream(toolListFile, false);

		ExcelWriter toolListWriter = new ExcelWriter(toolListStream);

		// Header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

		ColumnDisplay toolCodeColumn = new ColumnDisplay();
		toolCodeColumn.setProperty("code");
		toolCodeColumn.setHeadingKey(messageSource.getMessage("tools.code", null, locale));

		ColumnDisplay toolNameColumn = new ColumnDisplay();
		toolNameColumn.setProperty("name");
		toolNameColumn.setHeadingKey(messageSource.getMessage("tools.name", null, locale));

		ColumnDisplay toolDescColumn = new ColumnDisplay();
		toolDescColumn.setProperty("desc");
		toolDescColumn.setHeadingKey(messageSource.getMessage("tools.description", null, locale));

		columnDisplays.add(toolCodeColumn);
		columnDisplays.add(toolNameColumn);
		columnDisplays.add(toolDescColumn);

		// Row
		List<ToolExport> exportList = new ArrayList<ToolExport>();

		for (Tool tool : toolList) {
			ToolExport export = new ToolExport();
			export.setCode(tool.getCode());
			export.setName(tool.getName());
			export.setDesc(tool.getDesc());
			exportList.add(export);
		}

		toolListWriter.write(true, 0, "Tool", null, exportList, columnDisplays, false);

		toolListWriter.close();

		response.setHeader("Content-Type", "application/vnd.ms-excel");

		response.setHeader("Content-disposition", "inline;filename=\"" + toolListFile.getName() + "\"");

		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();
	}

	@RequestMapping(value = "/DoDeleteTool.do")
	public String doDeleteTool(@ModelAttribute("searchToolForm") SearchToolForm searchToolForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		String toolKeyStr = request.getParameter("key");

		logger.debug("doDeleteTool()[" + toolKeyStr + "]");

		Integer

		toolKey = Integer.parseInt(toolKeyStr);

		Tool targetTool = toolManager.getToolByKey(toolKey);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeTools"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			if (defectManager.hasTool(toolKey)) {

				searchToolForm.setHasDefect(true);
				searchToolForm.setDeleteSuccess(false);
				searchToolForm.setDeletedName(targetTool.getCode());

			} else {

				toolManager.deleteToolByKey(account.getKey(), toolKey);
				searchToolForm.setHasDefect(false);
				searchToolForm.setDeleteSuccess(true);
				searchToolForm.setDeletedName(targetTool.getCode());
			}

			return "redirect:DoSearchTool.do";
		} else {
			// user does not have right to remove tools
			logger.debug("user does not have right to remove tools ");
			return "common/noprivilege";
		}

	}

	/*
	 * Equipment
	 */

	// TODO: Equipment

	@RequestMapping(value = "/Equipment.do")
	public String showEquipmentMenu(HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("User requests to load equipment page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		return "defect/equipment_menu";

	}

	@RequestMapping(value = "/CreateEquipment.do")
	public String showCreateEquipmentForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load create equipment page.");
		
		EquipmentForm equipmentForm = new EquipmentForm();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			equipmentForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createEquipment"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
			equipmentForm.setAvailableLocationTree(locationTree);

			model.addAttribute("equipmentForm", equipmentForm);

			return "defect/equipment_create_modify";
		} else {
			// user does not have right to create equipment
			logger.debug("user does not have right to create equipment ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ViewEquipment.do")
	public String showViewEquipmentForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load view equipment page.");
		
		String referrerStr = request.getParameter("r");

		String equipmentKeyStr = request.getParameter("key");

		logger.debug("showViewEquipmentForm()[" + equipmentKeyStr + "]");

		Integer equipmentKey = Integer.parseInt(equipmentKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createEquipment"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyEquipment"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchEquipment"));

		if (hasPrivilege) {
			Equipment viewEquipment = null;

			viewEquipment = equipmentManager.getEquipmentByKey(equipmentKey);

			if (viewEquipment.getLocationKey() != null) {
				String locationCode = locationManager.getLocationByKey(viewEquipment.getLocationKey()).getCode();
				String locationName = locationManager.getLocationByKey(viewEquipment.getLocationKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
			}

			if (viewEquipment == null || currRole.getSiteKey() != viewEquipment.getSiteKey())

				return "common/notfound";

			EquipmentForm equipmentForm = new EquipmentForm();

			LocationTreeNode locationTree = new LocationTreeNode();
			locationTree.setLocation(locationManager.getLocationByKey(viewEquipment.getLocationKey()));

			equipmentForm.setAvailableLocationTree(locationTree);

			equipmentForm.setKey(viewEquipment.getKey());
			equipmentForm.setSiteKey(viewEquipment.getSiteKey());
			equipmentForm.setCode(viewEquipment.getCode());
			equipmentForm.setName(viewEquipment.getName());
			equipmentForm.setDesc(viewEquipment.getDesc());
			equipmentForm.setParentKey(0);
			equipmentForm.setLocationKey(viewEquipment.getLocationKey());
			equipmentForm.setReferrer(referrerStr);
			// view set readonly
			equipmentForm.setReadOnly(true);
			equipmentForm.setDelete(false);

			model.addAttribute("equipmentForm", equipmentForm);

			return "defect/equipment_create_modify";
		} else {
			// user does not have right to view equipment
			logger.debug("user does not have right to view equipment ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ModifyEquipment.do")
	public String showModifyEquipmentForm(@ModelAttribute("equipmentForm") EquipmentForm equipmentForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load modify equipment page.");
		
		String referrer = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyEquipment"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			Equipment editEquipment = null;

			editEquipment = equipmentManager.getEquipmentByKey(equipmentForm.getKey());

			if (editEquipment.getLocationKey() != null) {
				String locationCode = locationManager.getLocationByKey(editEquipment.getLocationKey()).getCode();
				String locationName = locationManager.getLocationByKey(editEquipment.getLocationKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
			}

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

			equipmentForm.setAvailableLocationTree(locationTree);

			equipmentForm.setKey(editEquipment.getKey());
			equipmentForm.setSiteKey(editEquipment.getSiteKey());
			equipmentForm.setCode(editEquipment.getCode());
			equipmentForm.setName(editEquipment.getName());
			equipmentForm.setDesc(editEquipment.getDesc());
			equipmentForm.setLocationKey(editEquipment.getLocationKey());
			equipmentForm.setReferrer(referrer);

			equipmentForm.setReadOnly(false);

			return "defect/equipment_create_modify";
		} else {
			// user does not have right to modify equipment
			logger.debug("user does not have right to modify equipment ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoCreateModifyEquipment.do")
	public String doCreateModifyEquipment(@ModelAttribute("equipmentForm") EquipmentForm equipmentForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		if (equipmentForm.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(equipmentForm.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(equipmentForm.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
		}

		String referrerStr = "";

		// save equipment
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

		equipmentForm.setAvailableLocationTree(locationTree);

		Equipment equipment = null;

		// validate
		equipmentFormValidator.validate(equipmentForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");

			return "defect/equipment_create_modify";
		}

		if (equipmentForm.getKey() == null) {

			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createEquipment"))) {
				// create
				equipment = new Equipment();
				referrerStr = "c";
				equipment.setName(equipmentForm.getName());
				equipment.setCode(equipmentForm.getCode());
				equipment.setDesc(equipmentForm.getDesc());
				equipment.setSiteKey(currRole.getSiteKey());
				equipment.setCreatedBy(account.getKey());
				equipment.setLocationKey(equipmentForm.getLocationKey());
				equipment.setParentKey(0);
				equipment.setLevelKey(1);
				equipment.setDeleted("N");
				equipment.setCreateDateTime(new Timestamp(System.currentTimeMillis()));

			} else {
				logger.debug("user does not have right to create equipment");
				return "common/noprivilege";
			}
		} else {

			// modify
			if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyEquipment"))) {

				equipment = equipmentManager.getEquipmentByKey(equipmentForm.getKey());

				referrerStr = "m";
				equipment.setName(equipmentForm.getName());
				equipment.setDesc(equipmentForm.getDesc());
				equipment.setCode(equipmentForm.getCode());
				equipment.setLastModifyBy(account.getKey());
				equipment.setLocationKey(equipmentForm.getLocationKey());
				equipment.setParentKey(0);
				equipment.setLevelKey(1);
				equipment.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

			} else {
				logger.debug("user does not have right to modify equipment");
				return "common/noprivilege";
			}
		}

		equipmentManager.saveEquipment(equipment);

		return "redirect:ViewEquipment.do?key=" + equipment.getKey() + "&r=" + referrerStr;

	}

	@ModelAttribute("searchEquipmentForm")
	public SearchEquipmentForm populateSearchEquipmentFormForm() {
		return new SearchEquipmentForm();
	}

	@RequestMapping(value = "/SearchEquipment.do")
	public String showSearchEquipmentForm(
			@ModelAttribute("searchEquipmentForm") SearchEquipmentForm searchEquipmentForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		logger.debug("User requests to load search equipment page.");
		
		searchEquipmentForm = new SearchEquipmentForm();
		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchEquipment"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			List<Location> locationList = new ArrayList<Location>();

			Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
			searchEquipmentForm.setAvailableLocationTree(locationTree);

			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("location", messageSource.getMessage("equipment.location", null, locale));
			sortByMap.put("code", messageSource.getMessage("equipment.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("equipment.name", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				searchEquipmentForm.setSiteKey(siteKey);
			}

			locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());
			searchEquipmentForm.setLocationList(locationList);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getCode() + " - " + locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			model.addAttribute("searchEquipmentForm", searchEquipmentForm);

			return "defect/equipment_search";
		} else {
			// user does not have right to search equipment
			logger.debug("user does not have right to search equipment ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchEquipment.do")
	public String doSearchEquipmentForm(@ModelAttribute("searchEquipmentForm") SearchEquipmentForm searchEquipmentForm,

			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		List<Location> locationList = new ArrayList<Location>();

		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchEquipmentForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchEquipment"));

		if (hasPrivilege) {
			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("location", messageSource.getMessage("equipment.location", null, locale));
			sortByMap.put("code", messageSource.getMessage("equipment.code", null, locale));
			sortByMap.put("name", messageSource.getMessage("equipment.name", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());
			searchEquipmentForm.setLocationList(locationList);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getCode() + " - " + locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			if (searchEquipmentForm.getLocationKey() != null) {
				String locationCode = locationManager.getLocationByKey(searchEquipmentForm.getLocationKey()).getCode();
				String locationName = locationManager.getLocationByKey(searchEquipmentForm.getLocationKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
			}

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

			searchEquipmentForm.setAvailableLocationTree(locationTree);

			List<Equipment> equipmentList = equipmentManager.searchEquipment(searchEquipmentForm.getSiteKey(),
					searchEquipmentForm.getLocationKey(), searchEquipmentForm.getCode(), searchEquipmentForm.getName());

			searchEquipmentForm.setResultList(equipmentList);

			searchEquipmentForm.setCanGen(true);

			Boolean canModify = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyEquipment"));

			Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeEquipment"));

			searchEquipmentForm.setCanModify(canModify);

			searchEquipmentForm.setCanRemove(canRemove);

			searchEquipmentForm.setFullListSize(searchEquipmentForm.getResultList().size());

			return "defect/equipment_search";
		} else {
			// user does not have right to search equipment
			logger.debug("user does not have right to search equipment ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoEquipmentDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchEquipmentForm") SearchEquipmentForm searchEquipmentForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "location", "code", "name", "desc" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Equipment> fullList = new ArrayList<Equipment>();

		List<Equipment> equipmentList = new ArrayList<Equipment>();

		if (searchEquipmentForm.getCanGen()) {

			fullList = searchEquipmentForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new ECodeComparator());

				break;

			case "location":

				Collections.sort(fullList, new ELocationComparator());

				break;

			case "code":

				Collections.sort(fullList, new ECodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new ENameComparator());

				break;

			case "desc":
				Collections.sort(fullList, new EDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					equipmentList.add(fullList.get(i));
			}

			fullListSize = searchEquipmentForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < equipmentList.size(); i++) {

			JSONArray ja = new JSONArray();
			Equipment equipment = equipmentList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchEquipmentForm.getCanModify();
			boolean canRemove = searchEquipmentForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewEquipment.do?key=" + equipment.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyEquipment.do?key=" + equipment.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + equipment.getKey() + ", '"
						+ Utility.removeQuote(Utility.replaceHtmlEntities(equipment.getCode()))
						+ "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(
					equipment.getLocation().getCode() + " - " + equipment.getLocation().getName()));
			ja.put(Utility.replaceHtmlEntities(equipment.getCode()));
			ja.put(Utility.replaceHtmlEntities(equipment.getName()));
			ja.put(Utility.replaceHtmlEntities(equipment.getDesc()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "ExportEquipment.do")
	public @ResponseBody void ExportEquipment(
			@ModelAttribute("searchEquipmentForm") SearchEquipmentForm searchEquipmentForm, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) throws MFMSException, Exception {
		logger.debug("ExportEquipment.do");

		List<Equipment> equipmentList = new ArrayList<Equipment>(searchEquipmentForm.getResultList());

		Calendar cal = Calendar.getInstance();

		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "Equipment_" + cal.getTimeInMillis() + ".xlsx";
		File equipmentListFile = new File(fileName);

		FileOutputStream equipmentListStream = new FileOutputStream(equipmentListFile, false);

		ExcelWriter equipmentListWriter = new ExcelWriter(equipmentListStream);

		// Header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

		ColumnDisplay locationColumn = new ColumnDisplay();
		locationColumn.setProperty("location");
		locationColumn.setHeadingKey(messageSource.getMessage("equipment.location", null, locale));

		ColumnDisplay equipmentCodeColumn = new ColumnDisplay();
		equipmentCodeColumn.setProperty("code");
		equipmentCodeColumn.setHeadingKey(messageSource.getMessage("equipment.code", null, locale));

		ColumnDisplay equipmentNameColumn = new ColumnDisplay();
		equipmentNameColumn.setProperty("name");
		equipmentNameColumn.setHeadingKey(messageSource.getMessage("equipment.name", null, locale));

		ColumnDisplay equipmentDescColumn = new ColumnDisplay();
		equipmentDescColumn.setProperty("desc");
		equipmentDescColumn.setHeadingKey(messageSource.getMessage("equipment.description", null, locale));

		columnDisplays.add(locationColumn);
		columnDisplays.add(equipmentCodeColumn);
		columnDisplays.add(equipmentNameColumn);
		columnDisplays.add(equipmentDescColumn);

		// Row
		List<EquipmentExport> exportList = new ArrayList<EquipmentExport>();

		for (Equipment equip : equipmentList) {
			EquipmentExport export = new EquipmentExport();
			export.setLocation(equip.getLocation().getCode() + " - " + equip.getLocation().getName());
			export.setCode(equip.getCode());
			export.setName(equip.getName());
			export.setDesc(equip.getDesc());

			exportList.add(export);
		}

		equipmentListWriter.write(true, 0, "Equipment", null, exportList, columnDisplays, false);

		equipmentListWriter.close();

		response.setHeader("Content-Type", "application/vnd.ms-excel");

		response.setHeader("Content-disposition", "inline;filename=\"" + equipmentListFile.getName() + "\"");

		ServletOutputStream servletOutputStream = response.getOutputStream();
		byte[] b = new byte[1024];
		int i = 0;
		FileInputStream fis = new java.io.FileInputStream(fileName);
		while ((i = fis.read(b)) > 0) {
			servletOutputStream.write(b, 0, i);
		}
		fis.close();

	}

	@RequestMapping(value = "/DoDeleteEquipment.do")
	public String doDeleteEquipment(@ModelAttribute("searchEquipmentForm") SearchEquipmentForm searchEquipmentForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		String equipmentKeyStr = request.getParameter("key");

		Integer equipmentKey = Integer.parseInt(equipmentKeyStr);

		Equipment targetEquipment = equipmentManager.getEquipmentByKey(equipmentKey);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeEquipment"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			if (defectManager.hasEquipment(equipmentKey) || defectScheduleManager.hasEquipment(equipmentKey)) {

				searchEquipmentForm.setHasDefect(true);
				searchEquipmentForm.setDeleteSuccess(false);
				searchEquipmentForm.setDeletedName(targetEquipment.getCode());

			} else {

				equipmentManager.deleteEquipmentByKey(account.getKey(), equipmentKey);
				searchEquipmentForm.setHasDefect(false);
				searchEquipmentForm.setDeleteSuccess(true);
				searchEquipmentForm.setDeletedName(targetEquipment.getCode());
			}
			return "redirect:DoSearchEquipment.do";
		} else {
			// user does not have right to remove equipment
			logger.debug("user does not have right to remove equipment ");
			return "common/noprivilege";
		}

	}

	/*
	 * Defect Management
	 */
	@RequestMapping(value = "/DefectManagement.do")
	public String showDefectManagementMenu(HttpServletRequest request, HttpServletResponse response) {
		
		logger.debug("User requests to load defect management page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		return "defect/defect_mgt_menu";

	}

	/*
	 * Defect
	 */

	// TODO: Defect

	@RequestMapping(value = "/Defect.do")
	public String showDefectMenu(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("User requests to load defect page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		return "defect/defect_menu";

	}

	@RequestMapping(value = "/CreateDefect.do")
	public String showCreateDefectForm(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		logger.debug("User requests to load create defect page.");
		
		List<Priority> priorityList = new ArrayList<Priority>();
		List<StatusFlow> statusList = new ArrayList<StatusFlow>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Tool> toolList = new ArrayList<Tool>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> callFromMap = new LinkedHashMap<String, String>();

		DefectForm defectForm = new DefectForm();

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			defectForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createDefect"));

		if (hasPrivilege) {
			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);
			defectForm.setAvailableLocationTree(locationTree);

			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());
			defectForm.setFailureClassList(failureClassList);

			causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole.getSiteKey());
			Collections.sort(causeCodeList, new CauseCodeComparator());
			defectForm.setCauseCodeList(causeCodeList);

			toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());
			Collections.sort(toolList, new ToolComparator());
			defectForm.setToolList(toolList);

			priorityList = priorityManager.getAllPriority();
			defectForm.setPriorityList(priorityList);

			statusList = statusFlowManager.getStatus(currRole.getModeKey(), null);

			Collections.sort(statusList, new StatusComparator3());
			defectForm.setStatusList(statusList);

			accountList = getAccountList(request);

			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

			defectForm.setTargetStartDateTime(getCurrentTimeString());

			Calendar cal = Calendar.getInstance();

			float rt = priorityManager.getResponseTime(1);

			if (rt < 1)
				cal.add(Calendar.MINUTE, (int) rt * 60);
			else
				cal.add(Calendar.HOUR, (int) rt);

			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);

			defectForm.setTargetFinishDateTime(convertTimestampToString(new Timestamp(cal.getTimeInMillis())));

			defectForm.setIssueDateTime(getCurrentTimeString());
			defectForm.setReportDateTime(getCurrentTimeString());

			defectForm.setCode(defectManager.getNextDefectCode(currRole.getSiteKey()));

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			problemCodeMap.put(null, pleaseSelect(locale));
			model.addAttribute("problemCodeList", problemCodeMap);

			causeCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < causeCodeList.size(); i++) {
				causeCodeMap.put(causeCodeList.get(i).getKey(), setMap(causeCodeList.get(i)));
			}
			model.addAttribute("causeCodeList", causeCodeMap);

			toolMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < toolList.size(); i++) {
				toolMap.put(toolList.get(i).getKey(), setMap(toolList.get(i)));
			}
			model.addAttribute("toolList", toolMap);

			equipmentMap.put(null, pleaseSelect(locale));
			model.addAttribute("equipmentList", equipmentMap);

			// priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			// for priority response time in defect create modify page
			Map<Integer, String> priorityResponseTime = priorityResponseTime(locale);
			model.addAttribute("priorityResponseTime", priorityResponseTime);

			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getId().getStatusId(),
						statusManager.getStatusNameByStatusId(statusList.get(i).getId().getStatusId()));
			}
			model.addAttribute("statusList", statusMap);

			accountGroupMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			accountMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountList.size(); i++) {
				accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
			}
			model.addAttribute("accountList", accountMap);
			model.addAttribute("issueByList", accountMap);
			model.addAttribute("checkByList", accountMap);

			callFromMap.put("", "defect.empty");
			callFromMap.put("H", "defect.callFrom.handheld");
			callFromMap.put("L", "defect.callFrom.landlord");
			callFromMap.put("S", "defect.callFrom.staff");
			callFromMap.put("T", "defect.callFrom.tenant");
			callFromMap.put("V", "defect.callFrom.visitor");

			model.addAttribute("callFromList", callFromMap);

			// defectForm.setCallFrom("S");

			// defectForm.setAssignedAccountKey(account.getKey());

			// Removed on 2016-12-05 - request by STam (JEC)
			// defectForm.setContactName((null==account.getName()) ? "" :
			// account.getName());
			// defectForm.setContactTel((null==account.getContactNumber()) ? ""
			// : account.getContactNumber());
			// defectForm.setContactEmail((null==account.getEmail()) ? "" :
			// account.getEmail());
			Integer photoNumber = Integer.parseInt(propertyConfigurer.getProperty("defect.pictureNumber.max"));
			defectForm.setPhotoRemain(photoNumber);
			defectForm.setVideoRemain(Integer.parseInt(propertyConfigurer.getProperty("defect.videoNumber.max")));

			model.addAttribute("p1", priorityManager.getResponseTime(1));
			model.addAttribute("p2", priorityManager.getResponseTime(2));
			model.addAttribute("p3", priorityManager.getResponseTime(3));
			model.addAttribute("p4", priorityManager.getResponseTime(4));

			model.addAttribute("photoPath", P_LOCATION);
			model.addAttribute("videoPath", V_LOCATION);

			model.addAttribute("defectForm", defectForm);

			return "defect/defect_create_modify";
		} else {
			// user does not have right to create defect
			logger.debug("user does not have right to create defect");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ViewDefect.do")
	public String showViewDefectForm(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		logger.debug("User requests to load view defect page.");
		
		List<Priority> priorityList = new ArrayList<Priority>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
		List<FileBucket> fileList = new ArrayList<FileBucket>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> callFromMap = new LinkedHashMap<String, String>();

		String defectKeyStr = request.getParameter("key");

		String referrerStr = request.getParameter("r");

		Integer defectKey = Integer.parseInt(defectKeyStr);

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchDefect"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createDefect"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyDefect"));

		if (hasPrivilege) {
			try {

				Defect viewDefect = defectManager.getDefectByKey(defectKey);

				if (viewDefect == null || currRole.getSiteKey() != viewDefect.getSiteKey())

					return "common/notfound";

				DefectForm defectForm = new DefectForm();

				fileList = defectFileManager.getFilesByDefectKey(defectKey);

				defectForm.setFileList(fileList);

				if (viewDefect.getLocationKey() != null) {
					String locationCode = locationManager.getLocationByKey(viewDefect.getLocationKey()).getCode();
					String locationName = locationManager.getLocationByKey(viewDefect.getLocationKey()).getName();
					model.addAttribute("selectedLocation", " :	" + locationCode + " - " + locationName);
				}

				LocationTreeNode locationTree = new LocationTreeNode();
				locationTree.setLocation(locationManager.getLocationByKey(viewDefect.getLocationKey()));
				defectForm.setAvailableLocationTree(locationTree);

				failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
				Collections.sort(failureClassList, new FailureClassComparator());

				problemCodeList = problemCodeManager.searchProblemCode(currRole.getSiteKey(), null, null, null, null);
				Collections.sort(problemCodeList, new ProblemCodeComparator());

				causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole.getSiteKey());
				Collections.sort(causeCodeList, new CauseCodeComparator());

				toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());
				Collections.sort(toolList, new ToolComparator());

				equipmentList = equipmentManager.getEquipmentBySiteKey(currRole.getSiteKey());
				Collections.sort(equipmentList, new EquipmentComparator());

				priorityList = priorityManager.getAllPriority();

				accountList = getAccountList(request);

				accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

				failureClassMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < failureClassList.size(); i++) {
					failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
				}
				model.addAttribute("failureClassList", failureClassMap);

				problemCodeMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < problemCodeList.size(); i++) {
					problemCodeMap.put(problemCodeList.get(i).getKey(), setMap(problemCodeList.get(i)));
				}
				model.addAttribute("problemCodeList", problemCodeMap);

				causeCodeMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < causeCodeList.size(); i++) {
					causeCodeMap.put(causeCodeList.get(i).getKey(), setMap(causeCodeList.get(i)));
				}
				model.addAttribute("causeCodeList", causeCodeMap);

				toolMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < toolList.size(); i++) {
					toolMap.put(toolList.get(i).getKey(), setMap(toolList.get(i)));
				}
				model.addAttribute("toolList", toolMap);

				equipmentMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < equipmentList.size(); i++) {
					equipmentMap.put(equipmentList.get(i).getKey(), setMap(equipmentList.get(i)));
				}
				model.addAttribute("equipmentList", equipmentMap);

				// priorityMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < priorityList.size(); i++) {
					priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
				}
				model.addAttribute("priorityList", priorityMap);

				statusMap.put(viewDefect.getStatusID(),
						statusManager.getStatusNameByStatusId(viewDefect.getStatusID()));
				model.addAttribute("statusList", statusMap);

				accountMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < accountList.size(); i++) {
					accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
				}
				model.addAttribute("accountList", accountMap);
				model.addAttribute("issueByList", accountMap);
				model.addAttribute("checkByList", accountMap);

				accountGroupMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < accountGroupList.size(); i++) {
					accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
				}
				model.addAttribute("accountGroupList", accountGroupMap);

				callFromMap.put("", "defect.empty");
				callFromMap.put("H", "defect.callFrom.handheld");
				callFromMap.put("L", "defect.callFrom.landlord");
				callFromMap.put("S", "defect.callFrom.staff");
				callFromMap.put("T", "defect.callFrom.tenant");
				callFromMap.put("V", "defect.callFrom.visitor");

				model.addAttribute("callFromList", callFromMap);

				defectForm.setActualStartDateTime(convertTimestampToString(viewDefect.getActualStartDateTime()));
				defectForm.setActualFinishDateTime(convertTimestampToString(viewDefect.getActualFinishDateTime()));
				defectForm.setTargetStartDateTime(convertTimestampToString(viewDefect.getTargetStartDateTime()));
				defectForm.setTargetFinishDateTime(convertTimestampToString(viewDefect.getTargetFinishDateTime()));
				defectForm.setIssueDateTime(convertTimestampToString(viewDefect.getIssueDateTime()));
				defectForm.setReportDateTime(convertTimestampToString(viewDefect.getReportDateTime()));
				defectForm.setCheckDateTime(convertTimestampToString(viewDefect.getCheckDateTime()));
				defectForm.setKey(viewDefect.getKey());
				defectForm.setSiteKey(viewDefect.getSiteKey());
				defectForm.setLocationKey(viewDefect.getLocationKey());
				defectForm.setCode(viewDefect.getCode());
				defectForm.setPriority(viewDefect.getPriority());
				defectForm.setDesc(viewDefect.getDesc());
				defectForm.setContactName(viewDefect.getContactName());
				defectForm.setContactEmail(viewDefect.getContactEmail());
				defectForm.setContactTel(viewDefect.getContactTel());
				defectForm.setEmergencyTel(viewDefect.getEmergencyTel());
				defectForm.setAssignedGroupKey(viewDefect.getAssignedGroupKey());
				defectForm.setAssignedAccountKey(viewDefect.getAssignedAccountKey());
				defectForm.setFailureClassKey(viewDefect.getFailureClassKey());
				defectForm.setProblemCodeKey(viewDefect.getProblemCodeKey());
				defectForm.setCauseCodeKey(viewDefect.getCauseCodeKey());
				defectForm.setToolKey(viewDefect.getToolKey());
				defectForm.setEquipmentKey(viewDefect.getEquipmentKey());
				defectForm.setCallFrom(viewDefect.getCallFrom());
				defectForm.setRemarks(viewDefect.getRemarks());

				if (viewDefect.getLastModifyBy() != null) {
					defectForm.setLastModifyBy(
							setMap(accountManager.getUserAccountByAccountKey(viewDefect.getLastModifyBy())));
				}

				if (viewDefect.getLastModifyDateTime() != null) {

					defectForm.setLastModifyDateTime(convertTimestampToString(viewDefect.getLastModifyDateTime()));
				}
				if (viewDefect.getCheckBy() != null) {
					defectForm.setCheckBy(viewDefect.getCheckBy());
				}
				if (viewDefect.getIssueBy() != null) {
					defectForm.setIssueBy(viewDefect.getIssueBy());
				}
				defectForm.setStatusID(viewDefect.getStatusID());

				// view set readonly
				defectForm.setReadOnly(true);
				defectForm.setDelete(false);

				defectForm.setReferrer(referrerStr);

				// DefectActionLog viewDefectActionLog = new DefectActionLog();
				// viewDefectActionLog.setSiteKey(viewDefect.getSiteKey());
				// viewDefectActionLog.setActionBy(account.getKey());
				// viewDefectActionLog.setDefectKey(viewDefect.getKey());
				// viewDefectActionLog.setActionType("V");
				// viewDefectActionLog.setActionDateTime(new Timestamp(System
				// .currentTimeMillis()));
				// viewDefectActionLog.setAssignedAccountKey(viewDefect
				// .getAssignedAccountKey());
				// viewDefectActionLog.setAssignedGroupKey(viewDefect
				// .getAssignedGroupKey());
				// viewDefectActionLog.setLocationKey(viewDefect.getLocationKey());
				// viewDefectActionLog.setCode(viewDefect.getCode());
				// viewDefectActionLog.setPriority(viewDefect.getPriority());
				// viewDefectActionLog.setDesc(viewDefect.getDesc());
				// viewDefectActionLog.setContactName(viewDefect.getContactName());
				// viewDefectActionLog.setContactEmail(viewDefect
				// .getContactEmail());
				// viewDefectActionLog.setContactTel(viewDefect.getContactTel());
				// viewDefectActionLog.setEmergencyTel(viewDefect
				// .getEmergencyTel());
				// viewDefectActionLog.setAssignedGroupKey(viewDefect
				// .getAssignedGroupKey());
				// viewDefectActionLog.setFailureClassKey(viewDefect
				// .getFailureClassKey());
				// viewDefectActionLog.setProblemCodeKey(viewDefect
				// .getProblemCodeKey());
				// viewDefectActionLog.setCauseCodeKey(viewDefect
				// .getCauseCodeKey());
				// viewDefectActionLog.setToolKey(viewDefect.getToolKey());
				// viewDefectActionLog.setEquipmentKey(viewDefect
				// .getEquipmentKey());
				// viewDefectActionLog.setCallFrom(viewDefect.getCallFrom());
				// viewDefectActionLog.setRemarks(viewDefect.getRemarks());
				// viewDefectActionLog.setCreateBy(viewDefect.getCreateBy());
				// viewDefectActionLog.setCreateDateTime(viewDefect
				// .getCreateDateTime());
				// viewDefectActionLog.setDetailedDesc(viewDefect
				// .getDetailedDesc());
				// viewDefectActionLog.setDeleted(viewDefect.getDeleted());
				// viewDefectActionLog.setStatusID(viewDefect.getStatusID());
				//
				// viewDefectActionLog.setReportDateTime(viewDefect
				// .getReportDateTime());
				// viewDefectActionLog.setIssueBy(viewDefect.getIssueBy());
				// viewDefectActionLog.setIssueDateTime(viewDefect
				// .getIssueDateTime());
				// viewDefectActionLog.setTargetStartDateTime(viewDefect
				// .getTargetStartDateTime());
				// viewDefectActionLog.setTargetAttendDateTime(viewDefect
				// .getTargetFinishDateTime());
				// viewDefectActionLog.setActualStartDateTime(viewDefect
				// .getActualStartDateTime());
				// viewDefectActionLog.setActualFinishDateTime(viewDefect
				// .getActualFinishDateTime());
				// viewDefectActionLog.setCheckBy(viewDefect.getCheckBy());
				// viewDefectActionLog.setCheckDateTime(viewDefect
				// .getCheckDateTime());
				// viewDefectActionLog.setLastModifyBy(viewDefect
				// .getLastModifyBy());
				// viewDefectActionLog.setLastModifyDateTime(viewDefect
				// .getLastModifyDateTime());

				// defectActionLogManager.saveDefectActionLog(viewDefectActionLog);

				model.addAttribute("p1", priorityManager.getResponseTime(1));
				model.addAttribute("p2", priorityManager.getResponseTime(2));
				model.addAttribute("p3", priorityManager.getResponseTime(3));
				model.addAttribute("p4", priorityManager.getResponseTime(4));

				model.addAttribute("photoPath", P_LOCATION);
				model.addAttribute("videoPath", V_LOCATION);

				model.addAttribute("defectForm", defectForm);

				return "defect/defect_create_modify";

			} catch (Exception e) {
				e.printStackTrace();
				return "common/notfound";
			}
		} else {
			// user does not have right to view defect
			logger.debug("user does not have right to view defect ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ModifyDefect.do")
	public String showModifyDefectForm(@ModelAttribute("defectForm") DefectForm defectForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		logger.debug("User requests to load modify defect page.");
		
		String referrer = request.getParameter("r");

		String deletedFile = request.getParameter("d");
		if (deletedFile != null)
			deletedFile = UnicodeToStr(deletedFile);

		List<Priority> priorityList = new ArrayList<Priority>();
		List<FileBucket> fileList = new ArrayList<FileBucket>();
		List<StatusFlow> statusFlowList = new ArrayList<StatusFlow>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<UserAccount> checkByList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();
		List<Location> locationList = new ArrayList<Location>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> callFromMap = new LinkedHashMap<String, String>();
		Map<Integer, String> checkByMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyDefect"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			try {

				Defect editDefect = null;

				editDefect = defectManager.getDefectByKey(defectForm.getKey());

				fileList = defectFileManager.getFilesByDefectKey(editDefect.getKey());
				defectForm.setFileList(fileList);

				if (editDefect.getLocationKey() != null) {
					String locationCode = locationManager.getLocationByKey(editDefect.getLocationKey()).getCode();
					String locationName = locationManager.getLocationByKey(editDefect.getLocationKey()).getName();
					model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
				}

				LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

				defectForm.setAvailableLocationTree(locationTree);

				locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());

				failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
				Collections.sort(failureClassList, new FailureClassComparator());

				problemCodeList = problemCodeManager.searchProblemCode(null, null, null,
						editDefect.getFailureClassKey(), null);
				// problemCodeList =
				// problemCodeManager.getProblemCodeBySiteKey(currRole.getSiteKey());
				Collections.sort(problemCodeList, new ProblemCodeComparator());

				causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole.getSiteKey());
				Collections.sort(causeCodeList, new CauseCodeComparator());

				toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());
				Collections.sort(toolList, new ToolComparator());

				if (editDefect.getLocationKey() != null) {
					equipmentList = equipmentManager.searchEquipment(null, editDefect.getLocationKey(), null, null);
					Collections.sort(equipmentList, new EquipmentComparator());
				}

				priorityList = priorityManager.getAllPriority();

				List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();

				if (editDefect.getAssignedGroupKey() != null) {

					agaList = accountGroupAccountManager.searchAccountGroupAccount(editDefect.getAssignedGroupKey());
					for (int i = 0; i < agaList.size(); i++) {
						accountList.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
						Collections.sort(accountList, new AccountComparator());
					}

				} else {
					accountList = getAccountList(request);
				}

				checkByList = getAccountList(request);

				accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

				locationMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < locationList.size(); i++) {
					locationMap.put(locationList.get(i).getKey(), locationList.get(i).getName());
				}
				model.addAttribute("locationList", locationMap);

				failureClassMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < failureClassList.size(); i++) {
					failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
				}
				model.addAttribute("failureClassList", failureClassMap);

				problemCodeMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < problemCodeList.size(); i++) {
					problemCodeMap.put(problemCodeList.get(i).getKey(), setMap(problemCodeList.get(i)));
				}
				model.addAttribute("problemCodeList", problemCodeMap);

				causeCodeMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < causeCodeList.size(); i++) {
					causeCodeMap.put(causeCodeList.get(i).getKey(), setMap(causeCodeList.get(i)));
				}
				model.addAttribute("causeCodeList", causeCodeMap);

				toolMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < toolList.size(); i++) {
					toolMap.put(toolList.get(i).getKey(), setMap(toolList.get(i)));
				}
				model.addAttribute("toolList", toolMap);

				equipmentMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < equipmentList.size(); i++) {
					equipmentMap.put(equipmentList.get(i).getKey(), setMap(equipmentList.get(i)));
				}
				model.addAttribute("equipmentList", equipmentMap);

				// priorityMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < priorityList.size(); i++) {
					priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
				}
				model.addAttribute("priorityList", priorityMap);

				Map<Integer, String> priorityResponseTime = priorityResponseTime(locale);
				model.addAttribute("priorityResponseTime", priorityResponseTime);

				statusFlowList = statusFlowManager.getStatus(currRole.getModeKey(), editDefect.getStatusID());
				// Collections.sort(statusFlowList, new StatusComparator());

				List<Status> statusList = new ArrayList<Status>();
				if (!statusFlowList.isEmpty()) {
					statusList.add(statusFlowList.get(0).getStatus());

					for (int i = 0; i < statusFlowList.size(); i++) {
						statusList.add(statusFlowList.get(i).getNextStatus());
					}
				}
				Collections.sort(statusList, new StatusComparator2());

				// statusMap.put(editDefect.getStatusID(),
				// statusManager.getStatusNameByStatusId(editDefect.getStatusID()));

				for (int i = 0; i < statusList.size(); i++) {
					// logger.debug(
					// statusFlowList.get(i).getId().getStatusId() + " : " +
					// statusFlowList.get(i).getId().getNextStatusId());

					statusMap.put(statusList.get(i).getStatusId(), statusList.get(i).getName());
				}
				model.addAttribute("statusList", statusMap);

				accountMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < accountList.size(); i++) {
					accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
				}
				model.addAttribute("accountList", accountMap);

				checkByMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < checkByList.size(); i++) {
					checkByMap.put(checkByList.get(i).getKey(), setMap(checkByList.get(i)));
				}
				model.addAttribute("issueByList", checkByMap);
				model.addAttribute("checkByList", checkByMap);

				accountGroupMap.put(null, pleaseSelect(locale));
				for (int i = 0; i < accountGroupList.size(); i++) {
					accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
				}
				model.addAttribute("accountGroupList", accountGroupMap);

				for (int i = 0; i < locationList.size(); i++) {
					locationMap.put(locationList.get(i).getKey(),
							locationList.get(i).getCode() + " - " + locationList.get(i).getName());
				}
				model.addAttribute("locationList", locationMap);
				callFromMap.put("", "defect.empty");
				callFromMap.put("H", "defect.callFrom.handheld");
				callFromMap.put("L", "defect.callFrom.landlord");
				callFromMap.put("S", "defect.callFrom.staff");
				callFromMap.put("T", "defect.callFrom.tenant");
				callFromMap.put("V", "defect.callFrom.visitor");

				model.addAttribute("callFromList", callFromMap);

				Integer photoNumber = Integer.parseInt(propertyConfigurer.getProperty("defect.pictureNumber.max"));
				Integer photoRemain = defectFileManager.getPhotoRemainWithPhotoNumber(editDefect.getKey(), photoNumber);
				defectForm.setPhotoRemain(photoRemain>=0?photoRemain : 0);
				// defectForm.setPhotoRemain(defectFileManager
				// .getPhotoRemain(editDefect.getKey()));
//				defectForm.setPhotoRemain(Integer.parseInt(propertyConfigurer.getProperty("defect.pictureNumber.max")));
				Integer videoNumber = Integer.parseInt(propertyConfigurer.getProperty("defect.videoNumber.max"));
				Integer videoRemain = defectFileManager.getVideoRemainWithVideoNumber(editDefect.getKey(), videoNumber);
				defectForm.setVideoRemain(videoRemain>=0?videoRemain:0);
				defectForm.setReferrer(referrer);
				defectForm.setDeletedFile(deletedFile);
				defectForm.setKey(editDefect.getKey());
				defectForm.setSiteKey(editDefect.getSiteKey());
				defectForm.setLocationKey(editDefect.getLocationKey());
				defectForm.setCode(editDefect.getCode());
				defectForm.setPriority(editDefect.getPriority());
				defectForm.setDesc(editDefect.getDesc());
				defectForm.setContactName(editDefect.getContactName());
				defectForm.setContactEmail(editDefect.getContactEmail());
				defectForm.setContactTel(editDefect.getContactTel());
				defectForm.setEmergencyTel(editDefect.getEmergencyTel());
				defectForm.setAssignedGroupKey(editDefect.getAssignedGroupKey());
				defectForm.setAssignedAccountKey(editDefect.getAssignedAccountKey());
				defectForm.setFailureClassKey(editDefect.getFailureClassKey());
				defectForm.setProblemCodeKey(editDefect.getProblemCodeKey());
				defectForm.setCauseCodeKey(editDefect.getCauseCodeKey());
				defectForm.setToolKey(editDefect.getToolKey());
				defectForm.setEquipmentKey(editDefect.getEquipmentKey());
				defectForm.setCallFrom(editDefect.getCallFrom());
				defectForm.setReportDateTime(convertTimestampToString(editDefect.getReportDateTime()));
				defectForm.setActualStartDateTime(convertTimestampToString(editDefect.getActualStartDateTime()));
				defectForm.setActualFinishDateTime(convertTimestampToString(editDefect.getActualFinishDateTime()));
				defectForm.setTargetStartDateTime(convertTimestampToString(editDefect.getTargetStartDateTime()));
				defectForm.setTargetFinishDateTime(convertTimestampToString(editDefect.getTargetFinishDateTime()));
				defectForm.setCheckDateTime(convertTimestampToString(editDefect.getCheckDateTime()));
				defectForm.setRemarks(editDefect.getRemarks());
				defectForm.setCheckBy(editDefect.getCheckBy());
				defectForm.setIssueBy(editDefect.getIssueBy());
				defectForm.setIssueDateTime(convertTimestampToString(editDefect.getIssueDateTime()));
				defectForm.setStatusID(editDefect.getStatusID());
				defectForm.setOrgStatusID(editDefect.getStatusID());

				if (editDefect.getLastModifyBy() != null) {
					defectForm.setLastModifyBy(
							setMap(accountManager.getUserAccountByAccountKey(editDefect.getLastModifyBy())));
				}
				if (editDefect.getLastModifyDateTime() != null) {
					defectForm.setLastModifyDateTime(convertTimestampToString(editDefect.getLastModifyDateTime()));
				}

				defectForm.setReadOnly(false);

				model.addAttribute("p1", priorityManager.getResponseTime(1));
				model.addAttribute("p2", priorityManager.getResponseTime(2));
				model.addAttribute("p3", priorityManager.getResponseTime(3));
				model.addAttribute("p4", priorityManager.getResponseTime(4));

				model.addAttribute("photoPath", P_LOCATION);
				model.addAttribute("videoPath", V_LOCATION);

				return "defect/defect_create_modify";

			} catch (Exception e) {
				e.printStackTrace();
				return "common/notfound";
			}
		} else {
			// user does not have right to create account
			logger.debug("user does not have right to create account ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoCreateModifyDefect.do")
	public synchronized String doCreateModifyDefect(@ModelAttribute("defectForm") DefectForm defectForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException, Exception {

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey("wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey("wo.video.uploadpath", 1);

		if (defectForm.getLocationKey() != null) {
			String locationCode = locationManager.getLocationByKey(defectForm.getLocationKey()).getCode();
			String locationName = locationManager.getLocationByKey(defectForm.getLocationKey()).getName();
			model.addAttribute("selectedLocation", " :	" + locationCode + " - " + locationName);
		}

		String referrerStr = "";

		model.addAttribute("p1", priorityManager.getResponseTime(1));
		model.addAttribute("p2", priorityManager.getResponseTime(2));
		model.addAttribute("p3", priorityManager.getResponseTime(3));
		model.addAttribute("p4", priorityManager.getResponseTime(4));

		model.addAttribute("photoPath", P_LOCATION);
		model.addAttribute("videoPath", V_LOCATION);

		List<Priority> priorityList = new ArrayList<Priority>();
		List<StatusFlow> statusList = new ArrayList<StatusFlow>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Tool> toolList = new ArrayList<Tool>();
		List<Equipment> equipmentList = new ArrayList<Equipment>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<UserAccount> checkByList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> toolMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> equipmentMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		Map<String, String> callFromMap = new LinkedHashMap<String, String>();
		Map<Integer, String> checkByMap = new LinkedHashMap<Integer, String>();

		// save defect
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		List<FileBucket> savedFileList = new ArrayList<FileBucket>();

		if (defectForm.getKey() != null) {
			savedFileList = defectFileManager.getFilesByDefectKey(defectForm.getKey());
			defectForm.setFileList(savedFileList);
			// defectForm.setPhotoRemain(defectFileManager
			// .getPhotoRemain(defectForm.getKey()));

			defectForm.setPhotoRemain(Integer.parseInt(propertyConfigurer.getProperty("defect.pictureNumber.max")));

			defectForm.setVideoRemain(defectFileManager.getVideoRemain(defectForm.getKey()));
		} else {
			defectForm.setPhotoRemain(Integer.parseInt(propertyConfigurer.getProperty("defect.pictureNumber.max")));
			defectForm.setVideoRemain(1);
		}

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

		defectForm.setAvailableLocationTree(locationTree);

		failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
		Collections.sort(failureClassList, new FailureClassComparator());

		if (defectForm.getFailureClassKey() != null) {
			problemCodeList = problemCodeManager.searchProblemCode(null, null, null, defectForm.getFailureClassKey(),
					null);
			Collections.sort(problemCodeList, new ProblemCodeComparator());
		}

		if (defectForm.getLocationKey() != null) {
			equipmentList = equipmentManager.searchEquipment(null, defectForm.getLocationKey(), null, null);
			Collections.sort(equipmentList, new EquipmentComparator());
		}

		causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole.getSiteKey());
		Collections.sort(causeCodeList, new CauseCodeComparator());

		toolList = toolManager.getToolBySiteKey(currRole.getSiteKey());
		Collections.sort(toolList, new ToolComparator());

		equipmentList = equipmentManager.getEquipmentBySiteKey(currRole.getSiteKey());
		Collections.sort(equipmentList, new EquipmentComparator());

		priorityList = priorityManager.getAllPriority();

		/*
		 * statusMap .put(defectForm.getStatusID(), statusManager
		 * .getStatusNameByStatusId(defectForm.getStatusID()));
		 */

		if (defectForm.getKey() != null) {
			/*
			 * statusList = statusFlowManager.getStatus(currRole.getModeKey(),
			 * defectForm.getStatusID());
			 */
			statusList = statusFlowManager.getStatus(currRole.getModeKey(), defectForm.getOrgStatusID());
			Collections.sort(statusList, new StatusComparator());
		} else {
			statusList = statusFlowManager.getStatus(currRole.getModeKey(), null);
			Collections.sort(statusList, new StatusComparator3());

		}

		if (defectForm.getAssignedGroupKey() != null) {
			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
			agaList = accountGroupAccountManager.searchAccountGroupAccount(defectForm.getAssignedGroupKey());
			for (int i = 0; i < agaList.size(); i++) {
				accountList.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				Collections.sort(accountList, new AccountComparator());
			}
		} else {
			accountList = getAccountList(request);
		}

		checkByList = getAccountList(request);

		accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

		failureClassMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < failureClassList.size(); i++) {
			failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
		}
		model.addAttribute("failureClassList", failureClassMap);

		problemCodeMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < problemCodeList.size(); i++) {
			problemCodeMap.put(problemCodeList.get(i).getKey(), setMap(problemCodeList.get(i)));
		}
		model.addAttribute("problemCodeList", problemCodeMap);

		causeCodeMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < causeCodeList.size(); i++) {
			causeCodeMap.put(causeCodeList.get(i).getKey(), setMap(causeCodeList.get(i)));
		}
		model.addAttribute("causeCodeList", causeCodeMap);

		toolMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < toolList.size(); i++) {
			toolMap.put(toolList.get(i).getKey(), setMap(toolList.get(i)));
		}
		model.addAttribute("toolList", toolMap);

		equipmentMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < equipmentList.size(); i++) {
			equipmentMap.put(equipmentList.get(i).getKey(), setMap(equipmentList.get(i)));
		}
		model.addAttribute("equipmentList", equipmentMap);

		// priorityMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < priorityList.size(); i++) {
			priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
		}
		model.addAttribute("priorityList", priorityMap);

		if (defectForm.getKey() != null) {
			StatusFlowId id = new StatusFlowId();
			id.setModeKey(0);
			id.setStatusId(defectForm.getOrgStatusID());
			id.setNextStatusId(defectForm.getOrgStatusID());
			StatusFlow e = new StatusFlow();
			e.setId(id);
			statusList.add(e);

			Collections.sort(statusList, new StatusComparator());

			/*
			 * statusMap.put(defectForm.getStatusID(), statusManager
			 * .getStatusNameByStatusId(defectForm.getStatusID()));
			 */
			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getId().getNextStatusId(),
						statusManager.getStatusNameByStatusId(statusList.get(i).getId().getNextStatusId()));
			}

		} else {
			Collections.sort(statusList, new StatusComparator3());

			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getId().getStatusId(),
						statusManager.getStatusNameByStatusId(statusList.get(i).getId().getStatusId()));
			}

		}
		model.addAttribute("statusList", statusMap);

		accountMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < accountList.size(); i++) {
			accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
		}
		model.addAttribute("accountList", accountMap);

		checkByMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < checkByList.size(); i++) {
			checkByMap.put(checkByList.get(i).getKey(), setMap(checkByList.get(i)));
		}
		model.addAttribute("issueByList", checkByMap);
		model.addAttribute("checkByList", checkByMap);

		accountGroupMap.put(null, pleaseSelect(locale));
		for (int i = 0; i < accountGroupList.size(); i++) {
			accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
		}
		model.addAttribute("accountGroupList", accountGroupMap);
		callFromMap.put("", "defect.empty");
		callFromMap.put("H", "defect.callFrom.handheld");
		callFromMap.put("L", "defect.callFrom.landlord");
		callFromMap.put("S", "defect.callFrom.staff");
		callFromMap.put("T", "defect.callFrom.tenant");
		callFromMap.put("V", "defect.callFrom.visitor");

		model.addAttribute("callFromList", callFromMap);

		try {

			Defect defect = null;
			DefectActionLog defectActionLog = null;

			// before validate's order
			/*
			 * logger.debug("statusList:"); for (StatusFlow f : statusList) {
			 * logger.debug(f.getStatus().getStatusId()); }
			 */
			// validate

			// Boolean duplicatedCode = false;
			// do {
			// Defect existingDefect = defectManager.getDefectByCode(
			// defectForm.getSiteKey(), defectForm.getCode(), false);
			//
			// if (existingDefect != null
			// && (defectForm.getKey() == null || (!defectForm
			// .getKey().equals(existingDefect.getKey())))) {
			// defectForm.setCode(defectManager.getNextDefectCode(currRole
			// .getSiteKey()));
			// duplicatedCode = true;
			// } else {
			// duplicatedCode = false;
			// }
			// } while (duplicatedCode);

			defectFormValidator.validate(defectForm, result);

			List<FileBucket> fileList = new ArrayList<FileBucket>();

			if (result.hasErrors()) {
				logger.debug("Validation Failed");

				return "defect/defect_create_modify";
			}

			if (defectForm.getKey() == null) {

				if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createDefect"))) {
					// create
					referrerStr = "c";
					defect = new Defect();
					defect.setCode(defectForm.getCode());
					defect.setDesc(defectForm.getDesc());
					defect.setSiteKey(currRole.getSiteKey());
					defect.setCreateBy(account.getKey());
					defect.setLastModifyBy(account.getKey());
					defect.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
					defect.setEquipmentKey(defectForm.getEquipmentKey());
					defect.setAssignedAccountKey(defectForm.getAssignedAccountKey());
					defect.setAssignedGroupKey(defectForm.getAssignedGroupKey());
					defect.setFailureClassKey(defectForm.getFailureClassKey());
					defect.setCauseCodeKey(defectForm.getCauseCodeKey());
					defect.setToolKey(defectForm.getToolKey());
					defect.setLocationKey(defectForm.getLocationKey());
					defect.setCallFrom(defectForm.getCallFrom());
					defect.setIssueBy(defectForm.getIssueBy());
					defect.setCheckBy(defectForm.getCheckBy());
					defect.setContactTel(defectForm.getContactTel());
					defect.setEmergencyTel(defectForm.getEmergencyTel());
					defect.setContactName(defectForm.getContactName());
					defect.setContactEmail(defectForm.getContactEmail());
					defect.setCreateChannel("W");
					defect.setDetailedDesc("detailed description");
					defect.setRemarks(defectForm.getRemarks());
					defect.setTargetStartDateTime(convertStringToTimestamp(defectForm.getTargetStartDateTime()));
					defect.setTargetFinishDateTime(convertStringToTimestamp(defectForm.getTargetFinishDateTime()));
					defect.setActualStartDateTime(convertStringToTimestamp(defectForm.getActualStartDateTime()));
					defect.setActualFinishDateTime(convertStringToTimestamp(defectForm.getActualFinishDateTime()));
					defect.setCheckDateTime(convertStringToTimestamp(defectForm.getCheckDateTime()));
					defect.setIssueDateTime(convertStringToTimestamp(defectForm.getIssueDateTime()));
					defect.setReportDateTime(convertStringToTimestamp(defectForm.getReportDateTime()));
					defect.setProblemCodeKey(defectForm.getProblemCodeKey());
					defect.setPriority(defectForm.getPriority());

					if (defectForm.getStatusID() == null || defectForm.getStatusID().isEmpty())
						defect.setStatusID("N");
					else
						defect.setStatusID(defectForm.getStatusID());

					defect.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
					defect.setDeleted("N");
					// defectActionLog = new DefectActionLog();
					// defectActionLog.setSiteKey(currRole.getSiteKey());
					// defectActionLog.setActionBy(account.getKey());
					// defectActionLog.setCreateBy(account.getKey());
					// defectActionLog.setActionType("C");
					// defectActionLog.setActionDateTime(new Timestamp(System
					// .currentTimeMillis()));
					// defectActionLog.setAssignedAccountKey(defectForm
					// .getAssignedAccountKey());
					// defectActionLog.setAssignedGroupKey(defectForm
					// .getAssignedGroupKey());
					// defectActionLog.setLocationKey(defectForm.getLocationKey());
					// defectActionLog.setCode(defectForm.getCode());
					// defectActionLog.setPriority(defectForm.getPriority());
					// defectActionLog.setDesc(defectForm.getDesc());
					// defectActionLog.setContactName(defectForm.getContactName());
					// defectActionLog.setContactEmail(defectForm
					// .getContactEmail());
					// defectActionLog.setContactTel(defectForm.getContactTel());
					// defectActionLog.setEmergencyTel(defectForm
					// .getEmergencyTel());
					// defectActionLog.setFailureClassKey(defectForm
					// .getFailureClassKey());
					// defectActionLog.setProblemCodeKey(defectForm
					// .getProblemCodeKey());
					// defectActionLog.setCauseCodeKey(defectForm
					// .getCauseCodeKey());
					// defectActionLog.setToolKey(defectForm.getToolKey());
					// defectActionLog.setEquipmentKey(defectForm
					// .getEquipmentKey());
					// defectActionLog.setCallFrom(defectForm.getCallFrom());
					// defectActionLog.setRemarks(defectForm.getRemarks());
					// defectActionLog.setCreateDateTime(new Timestamp(System
					// .currentTimeMillis()));
					// defectActionLog.setDeleted("N");
					// defectActionLog.setStatusID(defectForm.getStatusID());
					//
					// defectActionLog
					// .setReportDateTime(convertStringToTimestamp(defectForm
					// .getReportDateTime()));
					// defectActionLog.setIssueBy(defectForm.getIssueBy());
					// defectActionLog
					// .setIssueDateTime(convertStringToTimestamp(defectForm
					// .getIssueDateTime()));
					// defectActionLog
					// .setTargetStartDateTime(convertStringToTimestamp(defectForm
					// .getTargetStartDateTime()));
					// defectActionLog
					// .setTargetAttendDateTime(convertStringToTimestamp(defectForm
					// .getTargetFinishDateTime()));
					// defectActionLog
					// .setActualStartDateTime(convertStringToTimestamp(defectForm
					// .getActualStartDateTime()));
					// defectActionLog
					// .setActualFinishDateTime(convertStringToTimestamp(defectForm
					// .getActualFinishDateTime()));
					// defectActionLog.setCheckBy(defectForm.getCheckBy());
					// defectActionLog
					// .setCheckDateTime(convertStringToTimestamp(defectForm
					// .getCheckDateTime()));
					// defectActionLog.setLastModifyBy(defectForm.getLastModifyBy());
					// defectActionLog.setLastModifyDateTime(defectForm.getLastModifyDateTime());

					// Five Photo
					FileBucket picture = defectForm.getPicture1();
					String fileType = "";
					if (picture.getFile() != null) {
						fileType = picture.getFile().getContentType();
						if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
							String fileName = getFileDate() + "(1)" + picture.getFile().getOriginalFilename();
							String path = PHOTO_LOCATION + fileName;
							FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

							picture.setDesc(picture.getDesc());
							picture.setPath(fileName);
							picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							picture.setCreatedBy(account.getKey());
							picture.setSiteKey(currRole.getSiteKey());
							fileList.add(picture);
						}
					}

					picture = defectForm.getPicture2();
					if (picture.getFile() != null) {
						fileType = picture.getFile().getContentType();
						if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
							String fileName = getFileDate() + "(2)" + picture.getFile().getOriginalFilename();
							String path = PHOTO_LOCATION + fileName;
							FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

							picture.setDesc(picture.getDesc());
							picture.setPath(fileName);
							picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							picture.setCreatedBy(account.getKey());
							picture.setSiteKey(currRole.getSiteKey());
							fileList.add(picture);
						}
					}

					picture = defectForm.getPicture3();
					if (picture.getFile() != null) {
						fileType = picture.getFile().getContentType();
						if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
							String fileName = getFileDate() + "(3)" + picture.getFile().getOriginalFilename();
							String path = PHOTO_LOCATION + fileName;
							FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

							picture.setDesc(picture.getDesc());
							picture.setPath(fileName);
							picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							picture.setCreatedBy(account.getKey());
							picture.setSiteKey(currRole.getSiteKey());
							fileList.add(picture);
						}
					}

					picture = defectForm.getPicture4();
					if (picture.getFile() != null) {
						fileType = picture.getFile().getContentType();
						if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
							String fileName = getFileDate() + "(4)" + picture.getFile().getOriginalFilename();
							String path = PHOTO_LOCATION + fileName;
							FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

							picture.setDesc(picture.getDesc());
							picture.setPath(fileName);
							picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							picture.setCreatedBy(account.getKey());
							picture.setSiteKey(currRole.getSiteKey());
							fileList.add(picture);
						}
					}
					picture = defectForm.getPicture5();
					if (picture.getFile() != null) {
						fileType = picture.getFile().getContentType();
						if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
							String fileName = getFileDate() + "(5)" + picture.getFile().getOriginalFilename();
							String path = PHOTO_LOCATION + fileName;
							FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

							picture.setDesc(picture.getDesc());
							picture.setPath(fileName);
							picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							picture.setCreatedBy(account.getKey());
							picture.setSiteKey(currRole.getSiteKey());
							fileList.add(picture);
						}
					}

					// One Video
					FileBucket video = defectForm.getVideo();
					if (video.getFile() != null) {
						fileType = video.getFile().getContentType();
						if (video.getFile().getSize() > 0 && fileType.contains("video")) {
							String fileName = getFileDate() + video.getFile().getOriginalFilename();
							String path = VIDEO_LOCATION + fileName;
							FileCopyUtils.copy(video.getFile().getBytes(), new File(path));

							video.setDesc(video.getDesc());
							video.setPath(fileName);
							video.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							video.setCreatedBy(account.getKey());
							video.setSiteKey(currRole.getSiteKey());
							fileList.add(video);
						}
					}

				} else {
					logger.debug("user does not have right to create defect");
					return "common/noprivilege";
				}
			} else {
				// modify
				if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyDefect"))) {
					try {

						defect = defectManager.getDefectByKey(defectForm.getKey());

						referrerStr = "m";

						defect.setDesc(defectForm.getDesc());
						defect.setSiteKey(currRole.getSiteKey());
						defect.setCreateBy(account.getKey());
						defect.setEquipmentKey(defectForm.getEquipmentKey());
						defect.setAssignedAccountKey(defectForm.getAssignedAccountKey());
						defect.setAssignedGroupKey(defectForm.getAssignedGroupKey());
						defect.setFailureClassKey(defectForm.getFailureClassKey());
						defect.setCauseCodeKey(defectForm.getCauseCodeKey());
						defect.setToolKey(defectForm.getToolKey());
						defect.setLocationKey(defectForm.getLocationKey());
						defect.setCallFrom(defectForm.getCallFrom());
						defect.setIssueBy(defectForm.getIssueBy());
						defect.setCheckBy(defectForm.getCheckBy());
						defect.setContactTel(defectForm.getContactTel());
						defect.setEmergencyTel(defectForm.getEmergencyTel());
						defect.setContactName(defectForm.getContactName());
						defect.setContactEmail(defectForm.getContactEmail());
						defect.setCreateChannel("W");
						defect.setRemarks(defectForm.getRemarks());
						defect.setTargetStartDateTime(convertStringToTimestamp(defectForm.getTargetStartDateTime()));
						defect.setTargetFinishDateTime(convertStringToTimestamp(defectForm.getTargetFinishDateTime()));
						defect.setActualStartDateTime(convertStringToTimestamp(defectForm.getActualStartDateTime()));
						defect.setActualFinishDateTime(convertStringToTimestamp(defectForm.getActualFinishDateTime()));
						defect.setCheckDateTime(convertStringToTimestamp(defectForm.getCheckDateTime()));
						defect.setIssueDateTime(convertStringToTimestamp(defectForm.getIssueDateTime()));
						defect.setReportDateTime(convertStringToTimestamp(defectForm.getReportDateTime()));
						defect.setProblemCodeKey(defectForm.getProblemCodeKey());
						defect.setPriority(defectForm.getPriority());
						defect.setStatusID(defectForm.getStatusID());
						defect.setLastModifyBy(account.getKey());
						defect.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
						if (defect.getLastModifyBy() != null) {
							defectForm.setLastModifyBy(
									setMap(accountManager.getUserAccountByAccountKey(defect.getLastModifyBy())));
						}
						if (defect.getLastModifyDateTime() != null) {
							defectForm.setLastModifyDateTime(convertTimestampToString(defect.getLastModifyDateTime()));
						}

						// defectActionLog = new DefectActionLog();
						// defectActionLog.setSiteKey(currRole.getSiteKey());
						// defectActionLog.setCreateBy(defect.getCreateBy());
						// defectActionLog.setActionBy(account.getKey());
						// defectActionLog.setActionType("M");
						// defectActionLog.setDefectKey(defectForm.getKey());
						// defectActionLog.setActionDateTime(new
						// Timestamp(System
						// .currentTimeMillis()));
						// defectActionLog.setAssignedAccountKey(defectForm
						// .getAssignedAccountKey());
						// defectActionLog.setAssignedGroupKey(defectForm
						// .getAssignedGroupKey());
						// defectActionLog.setLocationKey(defectForm
						// .getLocationKey());
						// defectActionLog.setCode(defectForm.getCode());
						// defectActionLog.setPriority(defectForm.getPriority());
						// defectActionLog.setDesc(defectForm.getDesc());
						// defectActionLog.setContactName(defectForm
						// .getContactName());
						// defectActionLog.setContactEmail(defectForm
						// .getContactEmail());
						// defectActionLog.setContactTel(defectForm
						// .getContactTel());
						// defectActionLog.setEmergencyTel(defectForm
						// .getEmergencyTel());
						// defectActionLog.setFailureClassKey(defectForm
						// .getFailureClassKey());
						// defectActionLog.setProblemCodeKey(defectForm
						// .getProblemCodeKey());
						// defectActionLog.setCauseCodeKey(defectForm
						// .getCauseCodeKey());
						// defectActionLog.setToolKey(defectForm.getToolKey());
						// defectActionLog.setEquipmentKey(defectForm
						// .getEquipmentKey());
						// defectActionLog.setCallFrom(defectForm.getCallFrom());
						// defectActionLog.setRemarks(defectForm.getRemarks());
						// defectActionLog.setCreateDateTime(defect
						// .getCreateDateTime());
						// defectActionLog.setDetailedDesc(defect
						// .getDetailedDesc());
						// defectActionLog.setDeleted(defect.getDeleted());
						// defectActionLog.setStatusID(defectForm.getStatusID());
						//
						// defectActionLog.setReportDateTime(defect
						// .getReportDateTime());
						// defectActionLog.setIssueBy(defect.getIssueBy());
						// defectActionLog.setIssueDateTime(defect
						// .getIssueDateTime());
						// defectActionLog.setTargetStartDateTime(defect
						// .getTargetStartDateTime());
						// defectActionLog.setTargetAttendDateTime(defect
						// .getTargetFinishDateTime());
						// defectActionLog.setActualStartDateTime(defect
						// .getActualStartDateTime());
						// defectActionLog.setActualFinishDateTime(defect
						// .getActualFinishDateTime());
						// defectActionLog.setCheckBy(defect.getCheckBy());
						// defectActionLog.setCheckDateTime(defect
						// .getCheckDateTime());
						// defectActionLog.setLastModifyBy(defect
						// .getLastModifyBy());
						// defectActionLog.setLastModifyDateTime(defect
						// .getLastModifyDateTime());

						// Five Photo
						FileBucket picture = defectForm.getPicture1();
						String fileType = "";
						if (picture.getFile() != null) {
							fileType = picture.getFile().getContentType();
							if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
								String fileName = getFileDate() + "(1)" + picture.getFile().getOriginalFilename();
								String path = PHOTO_LOCATION + fileName;

								FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

								picture.setDesc(picture.getDesc());
								picture.setPath(fileName);
								picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								picture.setCreatedBy(account.getKey());
								picture.setSiteKey(currRole.getSiteKey());
								fileList.add(picture);
							}
						}

						picture = defectForm.getPicture2();
						if (picture.getFile() != null) {
							fileType = picture.getFile().getContentType();
							if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
								String fileName = getFileDate() + "(2)" + picture.getFile().getOriginalFilename();
								String path = PHOTO_LOCATION + fileName;
								FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

								picture.setDesc(picture.getDesc());
								picture.setPath(fileName);
								picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								picture.setCreatedBy(account.getKey());
								picture.setSiteKey(currRole.getSiteKey());
								fileList.add(picture);
							}
						}

						picture = defectForm.getPicture3();
						if (picture.getFile() != null) {
							fileType = picture.getFile().getContentType();
							if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
								String fileName = getFileDate() + "(3)" + picture.getFile().getOriginalFilename();
								String path = PHOTO_LOCATION + fileName;
								FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

								picture.setDesc(picture.getDesc());
								picture.setPath(fileName);
								picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								picture.setCreatedBy(account.getKey());
								picture.setSiteKey(currRole.getSiteKey());
								fileList.add(picture);
							}
						}

						picture = defectForm.getPicture4();
						if (picture.getFile() != null) {
							fileType = picture.getFile().getContentType();
							if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
								String fileName = getFileDate() + "(4)" + picture.getFile().getOriginalFilename();
								String path = PHOTO_LOCATION + fileName;
								FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

								picture.setDesc(picture.getDesc());
								picture.setPath(fileName);
								picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								picture.setCreatedBy(account.getKey());
								picture.setSiteKey(currRole.getSiteKey());
								fileList.add(picture);
							}
						}
						picture = defectForm.getPicture5();
						if (picture.getFile() != null) {
							fileType = picture.getFile().getContentType();
							if (picture.getFile().getSize() > 0 && fileType.contains("image")) {
								String fileName = getFileDate() + "(5)" + picture.getFile().getOriginalFilename();
								String path = PHOTO_LOCATION + fileName;
								FileCopyUtils.copy(picture.getFile().getBytes(), new File(path));

								picture.setDesc(picture.getDesc());
								picture.setPath(fileName);
								picture.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								picture.setCreatedBy(account.getKey());
								picture.setSiteKey(currRole.getSiteKey());
								fileList.add(picture);
							}
						}

						// One Video
						FileBucket video = defectForm.getVideo();
						if (video.getFile() != null) {
							fileType = video.getFile().getContentType();
							if (video.getFile().getSize() > 0 && fileType.contains("video")) {
								String fileName = getFileDate() + video.getFile().getOriginalFilename();
								String path = VIDEO_LOCATION + fileName;
								FileCopyUtils.copy(video.getFile().getBytes(), new File(path));
								video.setDesc(video.getDesc());
								video.setPath(fileName);
								video.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								video.setCreatedBy(account.getKey());
								video.setSiteKey(currRole.getSiteKey());
								fileList.add(video);
							}
						}

					} catch (MFMSException e) {
						logger.error(e.getMessage());
						return "common/notfound";
					}
				} else {
					logger.debug("user does not have right to modify defect");
					return "common/noprivilege";
				}
			}

			if (defect.getTargetFinishDateTime() != null && defect.getActualFinishDateTime() != null) {

				if (defect.getTargetFinishDateTime().getTime() > defect.getActualFinishDateTime().getTime())
					defect.setMeetKpi("Y");
				else
					defect.setMeetKpi("N");

			} else
				defect.setMeetKpi("-");

			defect.setCallCenterEmail(propertyConfigurer.getProperty("notification.callCenter.email"));
			saveDefectToDb(defectActionLogManager, defectManager, defect);
			// defectManager.saveDefect(defect);
			// defectActionLog.setDefectKey(defect.getKey());

			if (defect.getKey() != null) {
				for (FileBucket file : fileList) {
					file.setDefectKey(defect.getKey());
					defectFileManager.saveFile(file);
				}
			}

			// defectActionLogManager.saveDefectActionLog(defectActionLog);

			return "redirect:ViewDefect.do?key=" + defect.getKey() + "&r=" + referrerStr;

		} catch (Exception e) {

			e.printStackTrace();
			logger.debug("2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd2nd");
			return "common/notfound";
		}

	}

	@ModelAttribute("searchDefectForm")
	public SearchDefectForm populateSearchDefectFormForm() {
		return new SearchDefectForm();
	}

	@ModelAttribute("defectForm")
	public DefectForm populateDefectFormForm() {
		return new DefectForm();
	}

	@RequestMapping(value = "/SearchDefect.do")
	public String showSearchDefectForm(@ModelAttribute("searchDefectForm") SearchDefectForm searchDefectForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		logger.debug("User requests to load search defect page.");
		
		List<Priority> priorityList = new ArrayList<Priority>();
		List<Status> statusList = new ArrayList<Status>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Location> locationList = new ArrayList<Location>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();
		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchDefect"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");
			searchDefectForm = new SearchDefectForm();

			// searchDefectForm.setAccountKey(account.getKey());

			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("code", messageSource.getMessage("defect.code", null, locale));
			sortByMap.put("location", messageSource.getMessage("equipment.location", null, locale));
			sortByMap.put("failureClass", messageSource.getMessage("problemCode.failureClass", null, locale));
			sortByMap.put("problemCode", messageSource.getMessage("defect.problemCode", null, locale));
			sortByMap.put("priority", messageSource.getMessage("defect.priority", null, locale));
			sortByMap.put("status", messageSource.getMessage("account.status", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			accountList = getAccountList(request);

			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());

			causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole.getSiteKey());
			Collections.sort(causeCodeList, new CauseCodeComparator());

			priorityList = priorityManager.getAllPriority();

			statusList = statusManager.getAllStatus();
			Collections.sort(statusList, new StatusComparator2());

			locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());

			LocationTreeNode locationTree;
			locationTree = getLocationTreeNodeFormSession(request);
			searchDefectForm.setAvailableLocationTree(locationTree);

			model.addAttribute("searchDefectForm", searchDefectForm);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getCode() + " - " + locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			problemCodeMap.put(null, pleaseSelect(locale));
			model.addAttribute("problemCodeList", problemCodeMap);

			causeCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < causeCodeList.size(); i++) {
				causeCodeMap.put(causeCodeList.get(i).getKey(), setMap(causeCodeList.get(i)));
			}
			model.addAttribute("causeCodeList", causeCodeMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			statusMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getStatusId(),
						statusManager.getStatusNameByStatusId(statusList.get(i).getStatusId()));
			}
			model.addAttribute("statusList", statusMap);

			accountGroupMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			accountMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountList.size(); i++) {
				accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
			}
			model.addAttribute("accountList", accountMap);

			// get user's current site
			if (currRole != null) {
				Integer siteKey = currRole.getSiteKey();
				searchDefectForm.setSiteKey(siteKey);
			}

			if (null != locationList && locationList.size() > 0) {
				searchDefectForm.setLocationKey(locationList.get(0).getKey());
			} else {
				searchDefectForm.setLocationKey(null);
			}
			searchDefectForm.setStatus("N");

			model.addAttribute("searchDefectForm", searchDefectForm);

			return "defect/defect_search";
		} else {
			// user does not have right to search defect
			logger.debug("user does not have right to search defect ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoSearchDefect.do")
	public String doSearchDefectForm(@ModelAttribute("searchDefectForm") SearchDefectForm searchDefectForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException {

		List<Priority> priorityList = new ArrayList<Priority>();
		List<Status> statusList = new ArrayList<Status>();
		List<FailureClass> failureClassList = new ArrayList<FailureClass>();
		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();
		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		List<Location> locationList = new ArrayList<Location>();
		List<UserAccount> accountList = new ArrayList<UserAccount>();
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		Map<Integer, String> priorityMap = new LinkedHashMap<Integer, String>();
		Map<String, String> statusMap = new LinkedHashMap<String, String>();
		Map<Integer, String> failureClassMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> problemCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> causeCodeMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountMap = new LinkedHashMap<Integer, String>();
		Map<Integer, String> accountGroupMap = new LinkedHashMap<Integer, String>();

		Map<Integer, String> locationMap = new LinkedHashMap<Integer, String>();
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchDefectForm.setSiteKey(siteKey);
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchDefect"));

		if (hasPrivilege) {
			Map<String, String> sortByMap = new LinkedHashMap<String, String>();
			sortByMap.put("code", messageSource.getMessage("defect.code", null, locale));
			sortByMap.put("location", messageSource.getMessage("equipment.location", null, locale));
			sortByMap.put("failureClass", messageSource.getMessage("problemCode.failureClass", null, locale));
			sortByMap.put("problemCode", messageSource.getMessage("defect.problemCode", null, locale));
			sortByMap.put("priority", messageSource.getMessage("defect.priority", null, locale));
			sortByMap.put("status", messageSource.getMessage("account.status", null, locale));
			sortByMap.put("create", messageSource.getMessage("defect.createDateTime", null, locale));
			model.addAttribute("sortByList", sortByMap);

			failureClassList = failureClassManager.getFailureClassBySiteKey(currRole.getSiteKey());
			Collections.sort(failureClassList, new FailureClassComparator());

			if (searchDefectForm.getFailureClassKey() != null) {
				problemCodeList = problemCodeManager.searchProblemCode(null, null, null,
						searchDefectForm.getFailureClassKey(), null);
				Collections.sort(problemCodeList, new ProblemCodeComparator());
			}

			List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();

			if (searchDefectForm.getGroupKey() != null) {
				agaList = accountGroupAccountManager.searchAccountGroupAccount(searchDefectForm.getGroupKey());

				for (int i = 0; i < agaList.size(); i++) {
					accountList.add(accountManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}

				Collections.sort(accountList, new AccountComparator());

			} else

				accountList = getAccountList(request);

			accountGroupList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());

			causeCodeList = causeCodeManager.getCauseCodeBySiteKey(currRole.getSiteKey());
			Collections.sort(causeCodeList, new CauseCodeComparator());

			priorityList = priorityManager.getAllPriority();

			statusList = statusManager.getAllStatus();
			searchDefectForm.setStatusList(statusList);
			Collections.sort(statusList, new StatusComparator2());

			locationList = locationManager.getLocationsBySiteKey(currRole.getSiteKey());
			searchDefectForm.setLocationList(locationList);

			if (searchDefectForm.getLocationKey() != null) {
				String locationCode = locationManager.getLocationByKey(searchDefectForm.getLocationKey()).getCode();
				String locationName = locationManager.getLocationByKey(searchDefectForm.getLocationKey()).getName();
				model.addAttribute("selectedLocation", " : " + locationCode + " - " + locationName);
			}

			LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

			searchDefectForm.setAvailableLocationTree(locationTree);

			locationMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < locationList.size(); i++) {
				locationMap.put(locationList.get(i).getKey(),
						locationList.get(i).getCode() + " - " + locationList.get(i).getName());
			}
			model.addAttribute("locationList", locationMap);

			failureClassMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < failureClassList.size(); i++) {
				failureClassMap.put(failureClassList.get(i).getKey(), setMap(failureClassList.get(i)));
			}
			model.addAttribute("failureClassList", failureClassMap);

			problemCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < problemCodeList.size(); i++) {
				problemCodeMap.put(problemCodeList.get(i).getKey(), setMap(problemCodeList.get(i)));
			}
			model.addAttribute("problemCodeList", problemCodeMap);

			causeCodeMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < causeCodeList.size(); i++) {
				causeCodeMap.put(causeCodeList.get(i).getKey(), setMap(causeCodeList.get(i)));
			}
			model.addAttribute("causeCodeList", causeCodeMap);

			priorityMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < priorityList.size(); i++) {
				priorityMap.put(priorityList.get(i).getPriority(), priorityList.get(i).getPriority().toString());
			}
			model.addAttribute("priorityList", priorityMap);

			statusMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < statusList.size(); i++) {
				statusMap.put(statusList.get(i).getStatusId(),
						statusManager.getStatusNameByStatusId(statusList.get(i).getStatusId()));
			}
			model.addAttribute("statusList", statusMap);

			accountGroupMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountGroupList.size(); i++) {
				accountGroupMap.put(accountGroupList.get(i).getKey(), accountGroupList.get(i).getName());
			}
			model.addAttribute("accountGroupList", accountGroupMap);

			accountMap.put(null, pleaseSelect(locale));
			for (int i = 0; i < accountList.size(); i++) {
				accountMap.put(accountList.get(i).getKey(), setMap(accountList.get(i)));
			}
			model.addAttribute("accountList", accountMap);

			// validate
			defectSearchFormValidator.validate(searchDefectForm, result);

			if (result.hasErrors()) {
				logger.debug("Validation Failed");

				return "defect/defect_search";
			}

			Integer siteKey = currRole.getSiteKey();
			searchDefectForm.setSiteKey(siteKey);

			searchDefectForm.setCanGen(true);

			System.out.println("searchDefectForm.getGroupKey():" + searchDefectForm.getGroupKey());

			List<Integer> privilegedLocationKeyList = locationManager.getPrivilegedLocationKeyList(siteKey,
					account.getKey());

			int count = this.defectManager.searchDefectCount(currRole.getSiteKey(), searchDefectForm.getCode(),
					searchDefectForm.getLocationKey(), searchDefectForm.getPriority(), searchDefectForm.getStatus(),
					searchDefectForm.getFailureClassKey(), searchDefectForm.getProblemCodeKey(),
					searchDefectForm.getCauseCodeKey(), searchDefectForm.getDescription(),
					searchDefectForm.getGroupKey(), searchDefectForm.getAccountKey(), privilegedLocationKeyList);

			// System.out.println("searchDefectCount:"+count);

			if (count > Integer.parseInt(propertyConfigurer.getProperty("defect.max.search.size"))) {
				result.rejectValue("", "defect.search.exceed",
						new Object[] { propertyConfigurer.getProperty("defect.max.search.size") }, null);
			}

			if (result.hasErrors()) {
				logger.debug("Validation Failed");

				return "defect/defect_search";
			}

			List<Defect> filteredFullList = this.getFilteredFullList(currRole.getSiteKey(), searchDefectForm.getCode(),
					searchDefectForm.getLocationKey(), searchDefectForm.getPriority(), searchDefectForm.getStatus(),
					searchDefectForm.getFailureClassKey(), searchDefectForm.getProblemCodeKey(),
					searchDefectForm.getCauseCodeKey(), searchDefectForm.getDescription(),
					searchDefectForm.getGroupKey(), searchDefectForm.getAccountKey(), account.getKey());

			searchDefectForm.setResultList(filteredFullList);

			System.out.println("searchDefectCount:" + count + "||" + filteredFullList.size());

			Boolean canModify = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyDefect"));

			Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeDefect"));

			searchDefectForm.setCanModify(canModify);

			searchDefectForm.setCanRemove(canRemove);

			searchDefectForm.setSiteKey(currRole.getSiteKey());
			searchDefectForm.setCurrAccountKey(account.getKey());
			searchDefectForm.setFullListSize(searchDefectForm.getResultList().size());

			model.addAttribute("searchDefectForm", searchDefectForm);

			return "defect/defect_search";
		} else {
			// user does not have right to search defect
			logger.debug("user does not have right to search defect ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoDefectDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchDefectForm") SearchDefectForm searchDefectForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		JSONObject jsonResult = new JSONObject();
		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Defect> fullList = new ArrayList<Defect>();

		List<Defect> defectList = new ArrayList<Defect>();

		if (searchDefectForm.isCanGen()) {

			fullList = searchDefectForm.getResultList();
			fullListSize = searchDefectForm.getResultList().size();

			String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
			String sortDirection = request.getParameter("sSortDir_0") == null ? "desc"
					: request.getParameter("sSortDir_0");

			searchDefectForm.setSortColIndex(colIndex);
			searchDefectForm.setSortDirection(sortDirection);
			;

			fullList = this.sortDefectList(fullList, colIndex, sortDirection);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					defectList.add(fullList.get(i));
			}
		}

		jsonResult = getDefectJsonResult(defectList, fullListSize, searchDefectForm.getCanModify(),
				searchDefectForm.getCanRemove());

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(jsonResult);

		model.addAttribute("searchDefectForm", searchDefectForm);
	}

	@RequestMapping(value = "DoDefectExportExcel.do")
	// @ResponseStatus(value = HttpStatus.OK)
	public void doDefectExportExcel(@ModelAttribute("searchDefectForm") SearchDefectForm searchDefectForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("DoDefectExportExcel");

		try {
			// Get Full list
			List<Defect> filteredFullList = this.getFilteredFullList(searchDefectForm.getSiteKey(),
					searchDefectForm.getCode(), searchDefectForm.getLocationKey(), searchDefectForm.getPriority(),
					searchDefectForm.getStatus(), searchDefectForm.getFailureClassKey(),
					searchDefectForm.getProblemCodeKey(), searchDefectForm.getCauseCodeKey(),
					searchDefectForm.getDescription(), searchDefectForm.getGroupKey(), searchDefectForm.getAccountKey(),
					searchDefectForm.getCurrAccountKey());

			String colIndex = searchDefectForm.getSortColIndex();
			String sortDirection = searchDefectForm.getSortDirection();

			filteredFullList = this.sortDefectList(filteredFullList, colIndex, sortDirection);

			logger.debug("filteredFullList = " + filteredFullList.size());

			Calendar cal = Calendar.getInstance();

			String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
			String fileName = filePath + "WO_" + cal.getTimeInMillis() + ".xlsx";
			File woListFile = new File(fileName);

			FileOutputStream woListStream = new FileOutputStream(woListFile, false);

			ExcelWriter woListWriter = new ExcelWriter(woListStream);

			// Header
			List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

			ColumnDisplay codeColumn = new ColumnDisplay();
			codeColumn.setProperty("code");
			codeColumn.setHeadingKey(messageSource.getMessage("defect.code", null, locale));

			ColumnDisplay locationColumn = new ColumnDisplay();
			locationColumn.setProperty("location");
			locationColumn.setHeadingKey(messageSource.getMessage("defect.location", null, locale));

			ColumnDisplay failureClassColumn = new ColumnDisplay();
			failureClassColumn.setProperty("failureClass");
			failureClassColumn.setHeadingKey(messageSource.getMessage("defect.failureClass", null, locale));

			ColumnDisplay problemCodeColumn = new ColumnDisplay();
			problemCodeColumn.setProperty("problemCode");
			problemCodeColumn.setHeadingKey(messageSource.getMessage("defect.problemCode", null, locale));

			ColumnDisplay causeCodeColumn = new ColumnDisplay();
			causeCodeColumn.setProperty("causeCode");
			causeCodeColumn.setHeadingKey(messageSource.getMessage("defect.causeCode", null, locale));

			ColumnDisplay toolColumn = new ColumnDisplay();
			toolColumn.setProperty("tool");
			toolColumn.setHeadingKey(messageSource.getMessage("defect.tools", null, locale));

			ColumnDisplay equipmentColumn = new ColumnDisplay();
			equipmentColumn.setProperty("equipment");
			equipmentColumn.setHeadingKey(messageSource.getMessage("defect.equipment", null, locale));

			ColumnDisplay contactNameColumn = new ColumnDisplay();
			contactNameColumn.setProperty("contactName");
			contactNameColumn.setHeadingKey(messageSource.getMessage("defect.contactName", null, locale));

			ColumnDisplay contactTelColumn = new ColumnDisplay();
			contactTelColumn.setProperty("contactTel");
			contactTelColumn.setHeadingKey(messageSource.getMessage("defect.contactTel", null, locale));

			ColumnDisplay contactEmailColumn = new ColumnDisplay();
			contactEmailColumn.setProperty("contactEmail");
			contactEmailColumn.setHeadingKey(messageSource.getMessage("defect.contactEmail", null, locale));

			ColumnDisplay emergencyTelColumn = new ColumnDisplay();
			emergencyTelColumn.setProperty("emergencyTel");
			emergencyTelColumn.setHeadingKey(messageSource.getMessage("defect.emergencyTel", null, locale));

			ColumnDisplay assignedGroupColumn = new ColumnDisplay();
			assignedGroupColumn.setProperty("assignedGroup");
			assignedGroupColumn.setHeadingKey(messageSource.getMessage("defect.assignedGroup", null, locale));

			ColumnDisplay assignedAccountColumn = new ColumnDisplay();
			assignedAccountColumn.setProperty("assignedAccount");
			assignedAccountColumn.setHeadingKey(messageSource.getMessage("defect.assignedAccount", null, locale));

			ColumnDisplay priorityColumn = new ColumnDisplay();
			priorityColumn.setProperty("priority");
			priorityColumn.setHeadingKey(messageSource.getMessage("defect.priority", null, locale));

			ColumnDisplay descColumn = new ColumnDisplay();
			descColumn.setProperty("desc");
			descColumn.setHeadingKey(messageSource.getMessage("defect.description", null, locale));

			ColumnDisplay detailedDescColumn = new ColumnDisplay();
			detailedDescColumn.setProperty("detailedDesc");
			detailedDescColumn.setHeadingKey(messageSource.getMessage("defect.description", null, locale));

			ColumnDisplay reportDateTimeColumn = new ColumnDisplay();
			reportDateTimeColumn.setProperty("reportDateTime");
			reportDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.reportDate", null, locale));

			ColumnDisplay callFromColumn = new ColumnDisplay();
			callFromColumn.setProperty("callFrom");
			callFromColumn.setHeadingKey(messageSource.getMessage("defect.callFrom", null, locale));

			ColumnDisplay issueByColumn = new ColumnDisplay();
			issueByColumn.setProperty("issueBy");
			issueByColumn.setHeadingKey(messageSource.getMessage("defect.issueBy", null, locale));

			ColumnDisplay issueDateTimeColumn = new ColumnDisplay();
			issueDateTimeColumn.setProperty("issueDateTime");
			issueDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.issueDate", null, locale));

			ColumnDisplay targetStartDateTimeColumn = new ColumnDisplay();
			targetStartDateTimeColumn.setProperty("targetStartDateTime");
			targetStartDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.targetStartDate", null, locale));

			ColumnDisplay targetFinishDateTimeColumn = new ColumnDisplay();
			targetFinishDateTimeColumn.setProperty("targetFinishDateTime");
			targetFinishDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.targetFinishDate", null, locale));

			ColumnDisplay actualStartDateTimeColumn = new ColumnDisplay();
			actualStartDateTimeColumn.setProperty("actualStartDateTime");
			actualStartDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.actualStartDate", null, locale));

			ColumnDisplay actualFinishDateTimeColumn = new ColumnDisplay();
			actualFinishDateTimeColumn.setProperty("actualFinishDateTime");
			actualFinishDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.actualFinishDate", null, locale));

			ColumnDisplay checkByColumn = new ColumnDisplay();
			checkByColumn.setProperty("checkBy");
			checkByColumn.setHeadingKey(messageSource.getMessage("defect.checkBy", null, locale));

			ColumnDisplay checkDateTimeColumn = new ColumnDisplay();
			checkDateTimeColumn.setProperty("checkDateTime");
			checkDateTimeColumn.setHeadingKey(messageSource.getMessage("defect.checkedDate", null, locale));

			ColumnDisplay statusColumn = new ColumnDisplay();
			statusColumn.setProperty("status");
			statusColumn.setHeadingKey(messageSource.getMessage("defect.status", null, locale));

			ColumnDisplay remarksColumn = new ColumnDisplay();
			remarksColumn.setProperty("remarks");
			remarksColumn.setHeadingKey(messageSource.getMessage("defect.remarks", null, locale));

			ColumnDisplay meetKpiColumn = new ColumnDisplay();
			meetKpiColumn.setProperty("meetKpi");
			meetKpiColumn.setHeadingKey(messageSource.getMessage("report.kpi", null, locale));

			columnDisplays.add(codeColumn);
			columnDisplays.add(locationColumn);
			columnDisplays.add(failureClassColumn);
			columnDisplays.add(problemCodeColumn);
			columnDisplays.add(causeCodeColumn);
			columnDisplays.add(equipmentColumn);
			columnDisplays.add(toolColumn);
			columnDisplays.add(contactNameColumn);
			columnDisplays.add(contactTelColumn);
			columnDisplays.add(contactEmailColumn);
			columnDisplays.add(emergencyTelColumn);
			columnDisplays.add(assignedGroupColumn);
			columnDisplays.add(assignedAccountColumn);
			columnDisplays.add(priorityColumn);
			columnDisplays.add(descColumn);
			// columnDisplays.add(detailedDescColumn);
			columnDisplays.add(reportDateTimeColumn);
			columnDisplays.add(callFromColumn);
			columnDisplays.add(issueByColumn);
			columnDisplays.add(issueDateTimeColumn);
			columnDisplays.add(targetStartDateTimeColumn);
			columnDisplays.add(targetFinishDateTimeColumn);
			columnDisplays.add(actualStartDateTimeColumn);
			columnDisplays.add(actualFinishDateTimeColumn);
			columnDisplays.add(checkByColumn);
			columnDisplays.add(checkDateTimeColumn);
			columnDisplays.add(statusColumn);
			columnDisplays.add(remarksColumn);
			columnDisplays.add(meetKpiColumn);

			// Row
			List<DefectExport> exportList = new ArrayList<DefectExport>();

			for (int i = 0; i < filteredFullList.size(); i++) {
				DefectExport export = new DefectExport();

				Defect defect = filteredFullList.get(i);
				export.setCode(defect.getCode());
				export.setLocation((null != defect.getLocation())
						? defect.getLocation().getCode() + " - " + defect.getLocation().getName() : "");
				export.setFailureClass((null != defect.getFailureClass())
						? defect.getFailureClass().getCode() + " - " + defect.getFailureClass().getName() : "");
				export.setProblemCode((null != defect.getProblemCode())
						? defect.getProblemCode().getCode() + " - " + defect.getProblemCode().getName() : "");
				export.setCauseCode((null != defect.getCauseCode())
						? defect.getCauseCode().getCode() + " - " + defect.getCauseCode().getName() : "");
				export.setTool((null != defect.getTool())
						? defect.getTool().getCode() + " - " + defect.getTool().getName() : "");
				export.setEquipment((null != defect.getEquipment())
						? defect.getEquipment().getCode() + " - " + defect.getEquipment().getName() : "");
				export.setContactName(defect.getContactName());
				export.setContactTel(defect.getContactTel());
				export.setContactEmail(defect.getContactEmail());
				export.setEmergencyTel(defect.getEmergencyTel());
				export.setAssignedGroup((null != defect.getAssignedGroup()) ? defect.getAssignedGroup().getName() : "");
				export.setAssignedAccount((null != defect.getAssignedAccount())
						? defect.getAssignedAccount().getLoginId() + " - " + defect.getAssignedAccount().getName()
						: "");
				export.setPriority((null != defect.getPriority()) ? defect.getPriority().toString() : "");
				export.setDesc(defect.getDesc());
				export.setDetailedDesc(defect.getDetailedDesc());
				export.setReportDateTime((null != defect.getReportDateTime())
						? DateUtil.convertTimestampToString(defect.getReportDateTime()) : "");
				export.setCallFrom(this.getCallFromString(defect.getCallFrom(), locale));
				export.setIssueBy((null != defect.getIssueByAccount())
						? defect.getIssueByAccount().getLoginId() + " - " + defect.getIssueByAccount().getName() : "");
				export.setIssueDateTime((null != defect.getIssueDateTime())
						? DateUtil.convertTimestampToString(defect.getIssueDateTime()) : "");
				export.setTargetStartDateTime((null != defect.getTargetStartDateTime())
						? DateUtil.convertTimestampToString(defect.getTargetStartDateTime()) : "");
				export.setTargetFinishDateTime((null != defect.getTargetFinishDateTime())
						? DateUtil.convertTimestampToString(defect.getTargetFinishDateTime()) : "");
				export.setActualStartDateTime((null != defect.getActualStartDateTime())
						? DateUtil.convertTimestampToString(defect.getActualStartDateTime()) : "");
				export.setActualFinishDateTime((null != defect.getActualFinishDateTime())
						? DateUtil.convertTimestampToString(defect.getActualFinishDateTime()) : "");
				export.setCheckBy((null != defect.getCheckByAccount())
						? defect.getCheckByAccount().getLoginId() + " - " + defect.getCheckByAccount().getName() : "");
				export.setCheckDateTime((null != defect.getCheckDateTime())
						? DateUtil.convertTimestampToString(defect.getCheckDateTime()) : "");
				export.setStatus((null != defect.getStatus()) ? defect.getStatus().getName() : "");
				export.setRemarks(defect.getRemarks());
				export.setMeetKpi(defect.getMeetKpi());

				exportList.add(export);
			}

			// boolean showTitle, int sheetNum, String sheetName, Class
			// objClass, List list, List<ColumnDisplay> columnHeadings, boolean
			// asString
			woListWriter.write(true, 0, "WO", null, exportList, columnDisplays, false);

			woListWriter.close();

			/*
			 * response.setContentType("application/json");
			 * response.setHeader("Cache-Control", "no-store"); PrintWriter out
			 * = response.getWriter(); out.print(jsonResult);
			 */
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			// response.setHeader("Content-Disposition",
			// "attachment;filename=\"" + woListFile.getName() + "\"");
			// response.setHeader("Content-Type",
			// "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setHeader("Content-disposition", "inline;filename=\"" + woListFile.getName() + "\"");
			// response.setHeader("Content-disposition","inline;filename=\"" +
			// fileName + "\"");

			ServletOutputStream servletOutputStream = response.getOutputStream();
			byte[] b = new byte[1024];
			int i = 0;
			FileInputStream fis = new java.io.FileInputStream(fileName);
			while ((i = fis.read(b)) > 0) {
				servletOutputStream.write(b, 0, i);
			}
			fis.close();

			if (woListFile.delete()) {
				logger.debug("DoDefectExportExcel: " + woListFile.getName() + " is deleted!");
			} else {
				logger.debug("DoDefectExportExcel: Delete operation is failed.");
			}
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// account.getKey()
	}

	private List<Defect> getFilteredFullList(Integer siteKey, String code, Integer locationKey, Integer priority,
			String status, Integer failureClassKey, Integer problemCodeKey, Integer causeCodeKey, String desc,
			Integer groupKey, Integer accountKey, Integer currAccountKey) throws MFMSException {
		List<Defect> fullList = new ArrayList<Defect>();

		fullList = defectManager.searchDefect(siteKey, code, locationKey, priority, status, failureClassKey,
				problemCodeKey, causeCodeKey, desc, groupKey, accountKey);

		List<Defect> filteredFullList = new ArrayList<Defect>();

		List<Integer> privilegedLocationKeyList = locationManager.getPrivilegedLocationKeyList(siteKey, currAccountKey);

		for (Defect d : fullList) {

			if (privilegedLocationKeyList.contains(d.getLocationKey())) {

				filteredFullList.add(d);
			}
		}

		return filteredFullList;
	}

	private List<Defect> sortDefectList(List<Defect> defectList, String colIndex, String sortDirection) {
		String[] columnNames = { "action", "code", "location", "desc", "account", "priority", "status" };

		String colName = columnNames[Integer.parseInt(colIndex)];

		switch (colName) {

		case "action":

			Collections.sort(defectList, new DefectCodeComparator());

			break;

		case "code":

			Collections.sort(defectList, new DefectCodeComparator());

			break;

		case "location":

			Collections.sort(defectList, new DefectLocationComparator());

			break;

		case "desc":
			Collections.sort(defectList, new DefectDescComparator());

			break;

		case "account":
			Collections.sort(defectList, new DefectAccountComparator());
			break;

		case "priority":
			Collections.sort(defectList, new DefectPriorityComparator());
			break;

		case "status":
			Collections.sort(defectList, new DefectStatusComparator());
			break;
		}

		if (!sortDirection.equals("asc"))
			Collections.reverse(defectList);
		return defectList;
	}

	public JSONObject getDefectJsonResult(List<Defect> defectList, Integer total, Boolean canModify, Boolean canRemove)
			throws SQLException, ClassNotFoundException, MFMSException, JSONException {

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < defectList.size(); i++) {

			JSONArray ja = new JSONArray();
			Defect defect = defectList.get(i);

			StringBuilder myvar = new StringBuilder();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewDefect.do?key=" + defect.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyDefect.do?key=" + defect.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + defect.getKey() + ", "
						+ Utility.removeQuote(Utility.replaceHtmlEntities(defect.getCode()))
						+ ");\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());

			logger.debug("key: " + defect.getKey() + ", problemCode: " + defect.getProblemCode().getCode()
					+ ", failureClass: " + defect.getFailureClass().getCode());

			ja.put(Utility.replaceHtmlEntities(defect.getCode()));
			ja.put(Utility
					.replaceHtmlEntities(defect.getLocation().getCode() + " - " + defect.getLocation().getName()));
			ja.put(Utility.replaceHtmlEntities(defect.getDesc()));

			System.out.println("getAssignedAccountKey : " + defect.getAssignedAccountKey());
			UserAccount ac = accountManager.getUserAccountByAccountKey(defect.getAssignedAccountKey(), true);

			Boolean isInSite = false;
			if (ac != null && ac.getAccountRoles() != null) {
				for (UserAccountRole acRole : ac.getAccountRoles()) {

					if (acRole.getRole().getSiteKey().equals(defect.getSiteKey())) {
						isInSite = true;
						break;
					}
				}
			}

			if (ac == null || !isInSite) {
				ja.put("");
			} else {
				ja.put(Utility.replaceHtmlEntities(ac.getLoginId() + " - " + ac.getName()));
			}
			ja.put(defect.getPriority());

			String status = statusManager.getStatusNameByStatusId(defect.getStatusID());

			ja.put(status);
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", total);
		result.put("aaData", array);

		return result;
	}

	@RequestMapping(value = "/DoDeleteDefect.do")
	public String doDeleteDefect(@ModelAttribute("searchDefectForm") SearchDefectForm searchDefectForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException, Exception {

		String defectKeyStr = request.getParameter("key");

		Integer defectKey = Integer.parseInt(defectKeyStr);

		Defect targetDefect = defectManager.getDefectByKey(defectKey);

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeDefect"));

		if (hasPrivilege) {
			UserAccount account = (UserAccount) session.getAttribute("user");

			defectManager.deleteDefectByKey(account.getKey(), defectKey);

			// DefectActionLog deleteDefectActionLog = new DefectActionLog();
			// deleteDefectActionLog.setSiteKey(targetDefect.getSiteKey());
			// deleteDefectActionLog.setActionBy(account.getKey());
			// deleteDefectActionLog.setDefectKey(targetDefect.getKey());
			// deleteDefectActionLog.setActionType("D");
			// deleteDefectActionLog.setActionDateTime(new Timestamp(System
			// .currentTimeMillis()));
			// deleteDefectActionLog.setAssignedAccountKey(targetDefect
			// .getAssignedAccountKey());
			// deleteDefectActionLog.setAssignedGroupKey(targetDefect
			// .getAssignedGroupKey());
			// deleteDefectActionLog.setLocationKey(targetDefect.getLocationKey());
			// deleteDefectActionLog.setCode(targetDefect.getCode());
			// deleteDefectActionLog.setPriority(targetDefect.getPriority());
			// deleteDefectActionLog.setDesc(targetDefect.getDesc());
			// deleteDefectActionLog.setContactName(targetDefect.getContactName());
			// deleteDefectActionLog.setContactEmail(targetDefect
			// .getContactEmail());
			// deleteDefectActionLog.setContactTel(targetDefect.getContactTel());
			// deleteDefectActionLog.setEmergencyTel(targetDefect
			// .getEmergencyTel());
			// deleteDefectActionLog.setAssignedGroupKey(targetDefect
			// .getAssignedGroupKey());
			// deleteDefectActionLog.setFailureClassKey(targetDefect
			// .getFailureClassKey());
			// deleteDefectActionLog.setProblemCodeKey(targetDefect
			// .getProblemCodeKey());
			// deleteDefectActionLog.setCauseCodeKey(targetDefect
			// .getCauseCodeKey());
			// deleteDefectActionLog.setToolKey(targetDefect.getToolKey());
			// deleteDefectActionLog.setEquipmentKey(targetDefect
			// .getEquipmentKey());
			// deleteDefectActionLog.setCallFrom(targetDefect.getCallFrom());
			// deleteDefectActionLog.setRemarks(targetDefect.getRemarks());
			// deleteDefectActionLog.setCreateBy(targetDefect.getCreateBy());
			// deleteDefectActionLog.setCreateDateTime(targetDefect
			// .getCreateDateTime());
			// deleteDefectActionLog.setDetailedDesc("detailed description");
			// deleteDefectActionLog.setDeleted("N");
			// deleteDefectActionLog.setStatusID(targetDefect.getStatusID());
			//
			// defectActionLogManager.saveDefectActionLog(deleteDefectActionLog);

			defectFileManager.deleteAllFile(account.getKey(), defectKey);

			searchDefectForm.setDeleteSuccess(true);
			searchDefectForm.setDeletedName(targetDefect.getCode());

			sendNotification(defectActionLogManager, targetDefect, false, "X");

			model.addAttribute("defectForm", new DefectForm());

			return "redirect:DoSearchDefect.do";
		} else {
			// user does not have right to remove defect
			logger.debug("user does not have right to remove defect ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/DoDownloadFile.do")
	public void doDownloadFile(@ModelAttribute("defectForm") DefectForm defectForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException, Exception {

		String PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey("wo.photo.uploadpath", 1);
		String VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey("wo.video.uploadpath", 1);

		String type = request.getParameter("fileType");

		String path = request.getParameter("unicodePath");
		path = UnicodeToStr(path);

		// TODO : something is wrong, should get file type

		File file = null;

		if (type.equals("Image"))
			file = new File(PHOTO_LOCATION + path);
		else
			file = new File(VIDEO_LOCATION + path);

		if (!file.exists()) {
			String errorMessage = "Sorry. The file you are looking for does not exist";
			logger.debug(errorMessage);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(errorMessage.getBytes(Charset.forName("UTF-8")));
			outputStream.close();

		} else {

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				logger.debug("mimetype is not detectable, will take default");
				mimeType = "application/octet-stream";
			}

			logger.debug("mimetype : " + mimeType);

			response.setContentType(mimeType);

			response.setHeader("Content-Disposition", String.format("attachment;filename=\"%s\"", file.getName()));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}

	}

	@RequestMapping(value = "ShowFileTable.do", method = RequestMethod.GET)
	public String ShowFileTable(@ModelAttribute("defectForm") DefectForm defectForm, HttpServletResponse response,
			ModelMap model, HttpServletRequest request) throws MFMSException, Exception {

		Integer defectKey = defectForm.getKey();

		List<FileBucket> fileList = new ArrayList<FileBucket>();

		fileList = defectFileManager.getFilesByDefectKey(defectKey);

		model.addAttribute("fileList", fileList);

		return "view_fileTable";
	}

	@RequestMapping(value = "DoDeleteFile.do", method = RequestMethod.GET)
	public @ResponseBody String doDeleteFile(@ModelAttribute("defectForm") DefectForm defectForm,
			@RequestParam(value = "path", required = true) String path,
			@RequestParam(value = "type", required = true) String type, HttpServletResponse response, ModelMap model,
			HttpServletRequest request) throws MFMSException, Exception {

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Integer defectKey = defectForm.getKey();

		String fileName = defectFileManager.deleteFile(account.getKey(), defectKey, path, type);

		defectForm.setPhotoRemain(defectFileManager.getPhotoRemain(defectForm.getKey()));
		defectForm.setVideoRemain(defectFileManager.getVideoRemain(defectForm.getKey()));

		return fileName;

	}

	@RequestMapping(value = "DoModifyFileDescription.do", method = RequestMethod.GET)
	public @ResponseBody String doModifyFileDescription(@ModelAttribute("defectForm") DefectForm defectForm,
			@RequestParam(value = "desc", required = true) String desc,
			@RequestParam(value = "path", required = true) String path,
			@RequestParam(value = "type", required = true) String type, HttpServletResponse response, ModelMap model,
			HttpServletRequest request) throws MFMSException, Exception {

		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Integer defectKey = defectForm.getKey();

		defectFileManager.modifyFileDescription(account.getKey(), defectKey, path, type, desc);

		return "";

	}

	@RequestMapping(value = "/WarnNumber.do")
	public @ResponseBody void warnNumber(@ModelAttribute("searchDefectForm") SearchDefectForm form,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		UserAccount account = (UserAccount) session.getAttribute("user");
		Integer siteKey = currRole.getSiteKey();
		form.setSiteKey(siteKey);
		List<Integer> privilegedLocationKeyList = locationManager.getPrivilegedLocationKeyList(siteKey,
				account.getKey());

		int warnSize = Integer.parseInt(propertyConfigurer.getProperty("defect.warn.search.size"));
		int maxLimitationSize = Integer.parseInt(propertyConfigurer.getProperty("defect.max.search.size"));

		int count = this.defectManager.searchDefectCount(currRole.getSiteKey(), form.getCode(), form.getLocationKey(),
				form.getPriority(), form.getStatus(), form.getFailureClassKey(), form.getProblemCodeKey(),
				form.getCauseCodeKey(), form.getDescription(), form.getGroupKey(), form.getAccountKey(),
				privilegedLocationKeyList);

		String message = messageSource.getMessage("defect.search.warn",
				new String[] { Integer.toString(count), Integer.toString(warnSize) }, locale);
		// the modal should show if the record more than 10000 and fewer than
		// 30000.
		if (count > warnSize && count < maxLimitationSize) {
			response.getWriter().write(message);
		}

		else {

			response.getWriter().write("success");
		}
	}

	@RequestMapping(value = "/totalNumber.do")
	public @ResponseBody void totalNumber(@ModelAttribute("searchDefectForm") SearchDefectForm form,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		UserAccount account = (UserAccount) session.getAttribute("user");
		Integer siteKey = currRole.getSiteKey();
		form.setSiteKey(siteKey);
		List<Integer> privilegedLocationKeyList = locationManager.getPrivilegedLocationKeyList(siteKey,
				account.getKey());
		int count = this.defectManager.searchDefectCount(currRole.getSiteKey(), form.getCode(), form.getLocationKey(),
				form.getPriority(), form.getStatus(), form.getFailureClassKey(), form.getProblemCodeKey(),
				form.getCauseCodeKey(), form.getDescription(), form.getGroupKey(), form.getAccountKey(),
				privilegedLocationKeyList);

		if (count == 0) {
			response.getWriter().write("success");
		} else {
			response.getWriter().write(Integer.toString(count));
		}

	}

	@RequestMapping(value = "/WarnSize.do")
	public @ResponseBody void warnSize(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) throws MFMSException, Exception {

		Double size = Double.parseDouble(request.getParameter("size"));
		// convert btye to MB
		size = size / 1024 / 1024;

		Integer maxVideoSize = Integer.parseInt(propertyConfigurer.getProperty("defect.videoSize.max"));
		String message = messageSource.getMessage("defect.video.limitaion",
				new String[] { String.format("%.2f", size), Integer.toString(maxVideoSize) }, locale);
		logger.debug("size : " + size + ", maxVideoSize : " + maxVideoSize);
		if (size > maxVideoSize) {
			response.getWriter().write(message);
		} else {
			response.getWriter().write("success");
		}

	}

	public static synchronized int saveDefectToDb(DefectActionLogManager defectActionLogManager,
			DefectManager myDefectManager, Defect defect) {

		try {

			Boolean isNewDefect = false;

			if (defect.getKey() != null && defect.getKey() > 0) {

				isNewDefect = false;

			} else {

				isNewDefect = true;
				Boolean duplicatedCode = false;
				do {
					Defect existingDefect = myDefectManager.getDefectByCode(defect.getSiteKey(), defect.getCode(),
							false);

					if (existingDefect != null
							&& (defect.getKey() == null || (!defect.getKey().equals(existingDefect.getKey())))) {
						defect.setCode(myDefectManager.getNextDefectCode(defect.getSiteKey()));
						duplicatedCode = true;
					} else {
						duplicatedCode = false;
					}
				} while (duplicatedCode);

			}

			Integer resultKey = myDefectManager.saveDefect(defect);

			Defect searchedDefect = myDefectManager.getDefectByKey(resultKey);
			searchedDefect.setCallCenterEmail(defect.getCallCenterEmail());

			sendNotification(defectActionLogManager, searchedDefect, isNewDefect);

			return resultKey;
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return -1;
		}

	}

	public static void sendNotification(DefectActionLogManager defectActionLogManager, Defect defect,
			Boolean isNewDefect) {

		sendNotification(defectActionLogManager, defect, isNewDefect, defect.getStatusID());

	}

	public static void sendNotification(DefectActionLogManager defectActionLogManager, Defect defect,
			Boolean isNewDefect, String statusID) {

		try {

			InformEscalatorPostObj postObj = new InformEscalatorPostObj();
			postObj.setAppId("0");
			postObj.setWoKey("" + defect.getKey());
			postObj.setStatus(statusID);

			// postObj.setEmergency("Y");
			postObj.setEmergency(isEmergency(defect.getProblemCode().getCode()));
			postObj.setTargetFinishDate("" + defect.getTargetFinishDateTime().getTime());
			postObj.setLastModifyTime("" + defect.getLastModifyDateTime().getTime());
			postObj.setMessage(getNotificationMessage(postObj, defect));
			postObj.setRecipients(
					getNotificationRecipient(postObj, defect, postObj.getEmergency().equals("Y") ? true : false));

			InformEscalatorImpl impl = new InformEscalatorImpl();

			if (isNewDefect) {
				impl.create(postObj);
				addDefectActionLog(defectActionLogManager, defect, "C");
			} else {
				impl.update(postObj);
				if (statusID.equals("X")) {
					addDefectActionLog(defectActionLogManager, defect, "D");
				} else {
					addDefectActionLog(defectActionLogManager, defect, "M");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String isEmergency(String problemCode) {

		if ((problemCode.charAt(problemCode.length() - 1)) == '*') {

			return "Y";
		} else {

			return "N";
		}

	}

	private static NotificationMessage getNotificationMessage(InformEscalatorPostObj obj, Defect defect) {

		NotificationMessage message = obj.new NotificationMessage();

		message.setAssignedAccount(defect.getAssignedAccount() == null ? "" : defect.getAssignedAccount().getName());
		message.setContact(defect.getAssignedAccount() == null ? "" : defect.getAssignedAccount().getContactNumber());
		message.setDesc(defect.getDesc());
		message.setSite(defect.getSite().getName());
		message.setWoCode(defect.getCode());
		message.setIssueTime(
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(defect.getIssueDateTime().getTime())));
		message.setProblemCode(defect.getProblemCode().getName());
		message.setLocation(defect.getLocation().getName());

		System.out.println("ProblemCode : " + message.getProblemCode() + "||" + message.getLocation());

		return message;

	}

	private static List<NotificationRecipient> getNotificationRecipient(InformEscalatorPostObj obj, Defect defect,
			Boolean isEmergency) {

		List<NotificationRecipient> recipientList = new ArrayList<NotificationRecipient>();

		NotificationRecipient recipient = obj.new NotificationRecipient();
		recipient.setRole(NotificationUtil.NotificationUserRole.NormalAccount.toString());
		recipient.setTo(defect.getCallCenterEmail());
		recipient.setType(NotificationUtil.NotificationType.EMAIL.getValue() + "");// System
																					// =
																					// 0,
																					// GCM
																					// =
																					// 1,APNS
																					// =
																					// 2,
																					// Email
																					// =
																					// 99

		recipientList.add(recipient);

		if (defect.getAssignedGroup() == null) {
			return recipientList;
		}

		Map<Integer, UserAccount> recipientMap = new HashMap<Integer, UserAccount>();

		List<AccountGroupAccount> accountList = defect.getAssignedGroup().getAccountGroupAccount();
		for (AccountGroupAccount al : accountList) {
			UserAccount ac = al.getAccount();

			logger.debug("Group ac : " + ac.getKey() + "||" + ac.getName());

			recipientMap.put(ac.getKey(), ac);
		}
		//
		// if (isEmergency) {
		List<AccountGroupResponsible> responsibleList = defect.getAssignedGroup().getAccountGroupResponsible();
		for (AccountGroupResponsible ar : responsibleList) {

			UserAccount ac = ar.getAccount();

			logger.debug("Responsible ac : " + ac.getKey() + "||" + ac.getName());
			if (recipientMap.containsKey(ac.getKey())) {

				continue;
				// recipientMap.remove(ac.getKey());
			}

			recipient = obj.new NotificationRecipient();
			recipient.setRole(NotificationUtil.NotificationUserRole.RespsonsibleAccount.toString());
			recipient.setTo(ac.getLoginId());
			recipient.setType(NotificationUtil.NotificationType.GCM.getValue() + "");// System
																						// =
																						// 0,
																						// GCM
																						// =
																						// 1,APNS
																						// =
																						// 2,
																						// Email
																						// =
																						// 99

			recipientList.add(recipient);

			// recipientMap.put(ac.getKey(), ac);
		}
		// }

		for (Map.Entry<Integer, UserAccount> entry : recipientMap.entrySet()) {

			UserAccount ac = entry.getValue();

			recipient = obj.new NotificationRecipient();
			recipient.setRole(NotificationUtil.NotificationUserRole.NormalAccount.toString());
			recipient.setTo(ac.getLoginId());
			recipient.setType(NotificationUtil.NotificationType.GCM.getValue() + "");// System
																						// =
																						// 0,
																						// GCM
																						// =
																						// 1,APNS
																						// =
																						// 2,
																						// Email
																						// =
																						// 99

			recipientList.add(recipient);
		}

		return recipientList;

	}

	public static void addDefectActionLog(DefectActionLogManager defectActionLogManager, Defect defect, String action) {

		DefectActionLog defectActionLog = new DefectActionLog();
		defectActionLog.setSiteKey(defect.getSiteKey());
		defectActionLog.setCreateBy(defect.getLastModifyBy() == null ? defect.getCreateBy() : defect.getLastModifyBy());
		defectActionLog.setActionBy(defect.getLastModifyBy() == null ? defect.getCreateBy() : defect.getLastModifyBy());
		defectActionLog.setActionType(action);
		defectActionLog.setDefectKey(defect.getKey());
		defectActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
		defectActionLog.setAssignedAccountKey(defect.getAssignedAccountKey());
		defectActionLog.setAssignedGroupKey(defect.getAssignedGroupKey());
		defectActionLog.setLocationKey(defect.getLocationKey());
		defectActionLog.setCode(defect.getCode());
		defectActionLog.setPriority(defect.getPriority());
		defectActionLog.setDesc(defect.getDesc());
		defectActionLog.setContactName(defect.getContactName());
		defectActionLog.setContactEmail(defect.getContactEmail());
		defectActionLog.setContactTel(defect.getContactTel());
		defectActionLog.setEmergencyTel(defect.getEmergencyTel());
		defectActionLog.setFailureClassKey(defect.getFailureClassKey());
		defectActionLog.setProblemCodeKey(defect.getProblemCodeKey());
		defectActionLog.setCauseCodeKey(defect.getCauseCodeKey());
		defectActionLog.setToolKey(defect.getToolKey());
		defectActionLog.setEquipmentKey(defect.getEquipmentKey());
		defectActionLog.setCallFrom(defect.getCallFrom());
		defectActionLog.setRemarks(defect.getRemarks());
		defectActionLog.setCreateDateTime(defect.getCreateDateTime());
		defectActionLog.setDetailedDesc(defect.getDetailedDesc());
		defectActionLog.setDeleted(defect.getDeleted());
		defectActionLog.setStatusID(defect.getStatusID());

		defectActionLog.setReportDateTime(defect.getReportDateTime());
		defectActionLog.setIssueBy(defect.getIssueBy());
		defectActionLog.setIssueDateTime(defect.getIssueDateTime());
		defectActionLog.setTargetStartDateTime(defect.getTargetStartDateTime());
		defectActionLog.setTargetAttendDateTime(defect.getTargetFinishDateTime());
		defectActionLog.setActualStartDateTime(defect.getActualStartDateTime());
		defectActionLog.setActualFinishDateTime(defect.getActualFinishDateTime());
		defectActionLog.setCheckBy(defect.getCheckBy());
		defectActionLog.setCheckDateTime(defect.getCheckDateTime());
		defectActionLog.setLastModifyBy(defect.getLastModifyBy());
		defectActionLog.setLastModifyDateTime(defect.getLastModifyDateTime());

		try {
			defectActionLogManager.saveDefectActionLog(defectActionLog);
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String pleaseSelect(Locale locale) {
		return messageSource.getMessage("common.pleaseSelect", null, locale);
	}

	public String setMap(AccountGroup group) {
		return group.getName();
	}

	public String setMap(Location location) {
		if (location.getName().isEmpty()) {
			return location.getCode();
		} else
			return location.getCode() + " - " + location.getName();
	}

	public String setMap(ProblemCode problemCode) {
		if (problemCode.getName().isEmpty()) {
			return problemCode.getCode();
		} else
			return problemCode.getCode() + " - " + problemCode.getName();
	}

	public String setMap(FailureClass failureClass) {
		if (failureClass.getName().isEmpty()) {
			return failureClass.getCode();
		} else
			return failureClass.getCode() + " - " + failureClass.getName();
	}

	public String setMap(CauseCode causeCode) {
		if (causeCode.getName().isEmpty()) {
			return causeCode.getCode();
		} else
			return causeCode.getCode() + " - " + causeCode.getName();
	}

	public String setMap(Tool tool) {
		if (tool.getName().isEmpty()) {
			return tool.getCode();
		} else
			return tool.getCode() + " - " + tool.getName();
	}

	public String setMap(Equipment equipment) {
		if (equipment.getName().isEmpty()) {
			return equipment.getCode();
		} else
			return equipment.getCode() + " - " + equipment.getName();
	}

	public String setMap(UserAccount account) {
		if (account.getName().isEmpty()) {
			return account.getLoginId();
		} else
			return account.getLoginId() + " - " + account.getName();
	}

	public List<UserAccount> getAccountList(HttpServletRequest request) throws MFMSException {

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
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

	public class DefectCodeComparator implements Comparator<Defect> {
		@Override
		public int compare(Defect st, Defect nd) {

			Integer str1 = Integer.valueOf(st.getCode());
			Integer str2 = Integer.valueOf(nd.getCode());

			return str1.compareTo(str2);
		}
	}

	public class DefectAccountComparator implements Comparator<Defect> {
		@Override
		public int compare(Defect st, Defect nd) {

			String str1 = (null != st.getAssignedAccount()) ? st.getAssignedAccount().getLoginId() : "";
			String str2 = (null != nd.getAssignedAccount()) ? nd.getAssignedAccount().getLoginId() : "";

			return str1.compareTo(str2);
		}
	}

	public class DefectPriorityComparator implements Comparator<Defect> {
		@Override
		public int compare(Defect st, Defect nd) {

			Integer str1 = st.getPriority();
			Integer str2 = nd.getPriority();

			return str1.compareTo(str2);
		}
	}

	public class DefectStatusComparator implements Comparator<Defect> {
		@Override
		public int compare(Defect st, Defect nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getStatusID() == null ? "" : st.getStatusID();
			str2 = nd.getStatusID() == null ? "" : nd.getStatusID();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectDescComparator implements Comparator<Defect> {
		@Override
		public int compare(Defect st, Defect nd) {

			String str1 = "";
			String str2 = "";

			str1 = st.getDesc() == null ? "" : st.getDesc();
			str2 = nd.getDesc() == null ? "" : nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class DefectLocationComparator implements Comparator<Defect> {
		@Override
		public int compare(Defect st, Defect nd) {

			String str1 = st.getLocation().getCode();
			String str2 = nd.getLocation().getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class CCodeComparator implements Comparator<CauseCode> {
		@Override
		public int compare(CauseCode st, CauseCode nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class CNameComparator implements Comparator<CauseCode> {
		@Override
		public int compare(CauseCode st, CauseCode nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class CDescComparator implements Comparator<CauseCode> {
		@Override
		public int compare(CauseCode st, CauseCode nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class TCodeComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class TNameComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class TDescComparator implements Comparator<Tool> {
		@Override
		public int compare(Tool st, Tool nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ECodeComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ENameComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class EDescComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ELocationComparator implements Comparator<Equipment> {
		@Override
		public int compare(Equipment st, Equipment nd) {

			String str1 = st.getLocation().getCode();
			String str2 = nd.getLocation().getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class LNameComparator implements Comparator<Location> {
		@Override
		public int compare(Location st, Location nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class LParentComparator implements Comparator<Location> {
		@Override
		public int compare(Location st, Location nd) {

			Location p1 = st.getParent();
			Location p2 = nd.getParent();

			String str1 = p1 != null ? p1.getCode() : " - ";
			String str2 = p2 != null ? p2.getCode() : " - ";

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class LCodeComparator implements Comparator<Location> {
		@Override
		public int compare(Location st, Location nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class FCCodeComparator implements Comparator<FailureClass> {
		@Override
		public int compare(FailureClass st, FailureClass nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class FCNameComparator implements Comparator<FailureClass> {
		@Override
		public int compare(FailureClass st, FailureClass nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class FCDescComparator implements Comparator<FailureClass> {
		@Override
		public int compare(FailureClass st, FailureClass nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PCCodeComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PCNameComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PCDescComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PCFailureClassComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {

			String str1 = st.getFailureClass().getCode();
			String str2 = nd.getFailureClass().getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PCPriorityComparator implements Comparator<ProblemCode> {
		@Override
		public int compare(ProblemCode st, ProblemCode nd) {

			Integer str1 = st.getDefaultPriority() != null ? st.getDefaultPriority() : 9;
			Integer str2 = nd.getDefaultPriority() != null ? nd.getDefaultPriority() : 9;

			return str1.compareTo(str2);
		}
	}

	private LocationTreeNode getLocationTreeNodeFormSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		LocationTreeNode locationTree = (LocationTreeNode) session.getAttribute("locationTree");

		return locationTree;
	}

	public String getCallFromString(String str, Locale locale) {

		String callFrom = "";

		switch (str) {
		case "H":
			callFrom = messageSource.getMessage("defect.callFrom.handheld", null, locale);
			break;
		case "L":
			callFrom = messageSource.getMessage("defect.callFrom.landlord", null, locale);
			break;
		case "S":
			callFrom = messageSource.getMessage("defect.callFrom.staff", null, locale);
			break;
		case "T":
			callFrom = messageSource.getMessage("defect.callFrom.tenant", null, locale);
			break;
		case "V":
			callFrom = messageSource.getMessage("defect.callFrom.visitor", null, locale);
			break;
		}

		return callFrom;

	}

	public Map<Integer, String> priorityResponseTime(Locale locale) throws MFMSException {
		List<Priority> priorityList = new ArrayList<Priority>();
		priorityList = priorityManager.getAllPriority();
		// for priority response time in defect create modify page
		Map<Integer, String> priorityResponseTime = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < priorityList.size(); i++) {
			float time = priorityList.get(i).getResponseTime();
			String responseTime = "";

			String hrs = messageSource.getMessage("defect.response.time.hours", null, locale);
			String mins = messageSource.getMessage("defect.response.time.mins", null, locale);

			int hours = (int) (time);
			int minutes = (int) (time * 60) % 60;
			if (time >= 1) {
				if (time % 1 == 0) {
					responseTime = String.format("%s " + hrs, hours);
				} else {
					responseTime = String.format("%s " + hrs + "%s " + mins, hours, minutes);
				}
			} else {
				responseTime = String.format("%s " + mins, minutes);
			}

			priorityResponseTime.put(priorityList.get(i).getPriority(), responseTime);
		}

		return priorityResponseTime;

	}

	public DefectScheduleManager getDefectScheduleManager() {
		return defectScheduleManager;
	}

	public void setDefectScheduleManager(DefectScheduleManager defectScheduleManager) {
		this.defectScheduleManager = defectScheduleManager;
	}
}
