package hk.ebsl.mfms.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import hk.ebsl.mfms.dto.PatrolPhoto;
import hk.ebsl.mfms.dto.PatrolRouteActionLog;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.RouteDefActionLog;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.json.LocationJSON;
import hk.ebsl.mfms.json.PatrolPhotoJSON;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.PatrolPhotoManager;
import hk.ebsl.mfms.manager.PatrolRouteActionLogManager;
import hk.ebsl.mfms.manager.PatrolScheduleManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.RouteDefActionLogManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.model.Patrol_Route;
import hk.ebsl.mfms.model.Patrol_SearchTableRoute;
import hk.ebsl.mfms.utility.Pagination;
import hk.ebsl.mfms.utility.Utility;
import hk.ebsl.mfms.web.form.PatrolCreateForm;
import hk.ebsl.mfms.web.form.PatrolPhotoSearchForm;
import hk.ebsl.mfms.web.form.PatrolSearchForm;
import hk.ebsl.mfms.web.form.SearchDefectForm;
import hk.ebsl.mfms.web.form.SearchRoleForm;
import hk.ebsl.mfms.web.form.validator.PatrolCreateFormValidator;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.codehaus.jackson.JsonGenerationException;
//import org.codehaus.jackson.JsonParseException;
//import org.codehaus.jackson.map.JsonMappingException;
//import org.codehaus.jackson.map.ObjectMapper;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SessionAttributes({ "patrolSearchForm" })
@Controller
public class PatrolController {

	@Autowired
	private Properties privilegeMap;

	static class ModelMappingValue {
		public static final String pages_punchcardMenu = "punchcard/menu/punchcard_menu";
		public static final String pages_patrolMenu = "patrol/menu/patrol_menu";
		public static final String pages_routeMenu = "patrol/menu/route_menu";
		public static final String pages_patrolReportMenu = "patrol/menu/patrolReport_menu";
		public static final String pages_patrolCreate = "patrol/patrol_create";
		public static final String pages_patrolSearch = "patrol/patrol_search";
		public static final String pages_view_showChildren = "view_showChildren";
		public static final String pages_view_showSelectedLocation = "view_showSelectedLocation";
		public static final String pages_view_showErrorMsg = "view_showErrorMsg";
		public static final String pages_view_showSuccessMsg = "view_showSuccessMsg";
		public static final String pages_view_showStartingLocation = "view_startingLocation";
		public static final String pages_view_searchTable = "view_searchTable";
		public static final String pages_view_patrolPhoto = "view_patrolPhoto";
		public static final String pages_patrolPhotoSearch = "patrol/patrolPhoto_search";
		public static final String pages_view_showClockInForm = "view_clock_in_form";
		
		
		public static final String var_selectedLocation = "selectedLocationList";
		public static final String var_childrenList = "childrenList";
		public static final String var_parentkey = "parentKey";
		public static final String var_timeUnit = "timeUnit";
		public static final String var_submitStatus = "submitStatus";
		public static final String var_mode = "mode";
		public static final String var_startingLocation = "startingLocation";
		public static final String var_searchResult = "searchResult";
		public static final String var_totalPages = "totalPages";
		public static final String var_startPage = "startPage";
		public static final String var_pageSize = "pageSize";
		public static final String var_currentPage = "currentPage";
		public static final String var_locationJson = "locationJson";
		public static final String var_oldLocationJson = "oldLocationJson";
		public static final String var_searchResultJson = "searchResultJson";
		public static final String var_successMsgStr = "successMsgStr";
		public static final String var_successMsgClass = "successMsgClass";
		public static final String var_patrolPhotoList = "patrolPhotoList";
		public static final String var_selectedDate = "selectedDate";
		public static final String var_canEdit = "canEdit";

		public static final String mode_create = "create";
		public static final String mode_edit = "edit";
		public static final String mode_view = "view";
		public static final String mode_search = "search";
		public static final String value_success = "success";
		public static final String value_createSuccess = "createSuccess";
		public static final String value_modifySuccess = "modifySuccess";
		public static final String value_deleteSuccess = "deleteSuccess";
		public static final String value_createScheduleSuccess = "createScheduleSuccess";
		public static final String value_deleteScheduleSuccess = "deleteScheduleSuccess";
		public static final String value_pastScheduleCannotBeDeleted = "cannotDeleteSucess";
		public static final String value_deleteFutureNotDeleteCurrent = "deleteFutureNotDeleteCurrent";
		public static final String form_patrolCreateForm = "patrolCreateForm";
		public static final String form_patrolSearchForm = "patrolSearchForm";
		public static final String form_patrolPhotoSearchForm = "patrolPhotoSearchForm";

		// in database "0" for "min(s)", "1" for "hr(s)"
		public static final String[] ptDurUnit = { "mins", "hrs" };
		public static final String[] ptDurUnitInDb = { "0", "1" };
	}

	int rowOnEachPage = 20;
	int pageSize = 10;

	// private List<Patrol_SearchTableRoute> search_patrolSearchTableList =
	// null;

	@Autowired
	private Pagination pagination;
	@Autowired
	private RouteDefManager routeDefManager;
	@Autowired
	private LocationManager locationManager;
	@Autowired
	private PatrolCreateFormValidator patrolCreateFormValidator;
	@Autowired
	private RouteDefActionLogManager routeDefActionLogManager;
	@Autowired
	private RoleManager roleManager;
	@Autowired
	private PatrolPhotoManager patrolPhotoManager;
	@Autowired
	private PatrolScheduleManager patrolScheduleManager;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private Properties propertyConfigurer;
	@Autowired
	private PatrolRouteActionLogManager patrolRouteActionLogManager;

	public final static Logger logger = Logger.getLogger(PatrolController.class);

	// function with RequestMapping

	/**                           **/
	/** Functions for Patrol Menu **/
	/**                           **/
	@RequestMapping(value = "/PatrolManagement.do")
	public String showPatrolMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		logger.debug("User requests to load patrol management page.");
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		return ModelMappingValue.pages_patrolMenu;
	}

	/**                           **/
	/** Functions for Patrol Menu **/
	/**                           **/
	@RequestMapping(value = "/PatrolRoute.do")
	public String showPatrolRouteMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		logger.debug("User requests to load patrol management page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		return ModelMappingValue.pages_routeMenu;
	}

	/**                           **/
	/** Functions for Patrol ReportMenu **/
	/**                           **/
	@RequestMapping(value = "/PatrolReport.do")
	public String showPatrolReportMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		logger.debug("User requests to load patrol report page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		return ModelMappingValue.pages_patrolReportMenu;
	}
	
	@RequestMapping(value = "/PatrolPhoto.do")
	public String showPatrolPhotoPage(@RequestParam(value = "routeKey", defaultValue = "0") int routeDefKey,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale) throws Exception {

		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		
		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createRoute"));
		Integer siteKey = currRole.getSiteKey();
		
		if (hasPrivilege) {
			Map<Integer, String> routeMap = new HashMap<Integer, String>();
			List<RouteDef> psList = routeDefManager.getAllRoute(siteKey);
			PatrolPhotoSearchForm form = new PatrolPhotoSearchForm();
			
			form.setSiteKey(siteKey);
			routeMap.put(null, " ");
			for(RouteDef rd : psList){
				
				routeMap.put(rd.getRouteDefKey(), rd.getCode() + " - " + rd.getName());
				
			}
			form.setRouteDefKey(routeDefKey);
			form.setRouteMap(routeMap);

			model.addAttribute(ModelMappingValue.form_patrolPhotoSearchForm, form);
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_view);
			model.addAttribute("routeList", routeMap);
			
			return ModelMappingValue.pages_patrolPhotoSearch;
		} else {
			// user does not have right to search patrol route
			logger.debug("user does not have right to search patrol route ");
			return "common/noprivilege";
		}	
	}
	
	

	/**                      **/
	/** Functions for Create **/
	/**                      **/
	@RequestMapping(value = "/PatrolCreate.do")
	public String showPatrolCreate(HttpServletRequest request, HttpServletResponse response, ModelMap model,
			Locale locale) {
		
		logger.debug("User requests to load create patrol route page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// generate the map for the units
		// resetData();

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createRoute"));

		if (hasPrivilege) {
			Map<String, String> timeUnit = getTimeUnit(locale);
			PatrolCreateForm form = new PatrolCreateForm();
	
			model.addAttribute(ModelMappingValue.form_patrolCreateForm, form);
			model.addAttribute(ModelMappingValue.var_timeUnit, timeUnit);
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_create);
			model.addAttribute(ModelMappingValue.var_locationJson, "");
	
			return ModelMappingValue.pages_patrolCreate;
		} else {
			// user does not have right to create patrol
			logger.debug("user does not have right to create patrol ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ShowChildren.do")
	public String showChildrenList(HttpServletRequest request, ModelMap model) {

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

		List<Location> rootList = new ArrayList<Location>();
		if (locationTree != null) {

			Location loc = locationTree.getLocation();
			loc.setHasPivilege(locationTree.isPrivileged());
			rootList.add(loc);

		}

		model.addAttribute(ModelMappingValue.var_parentkey, 0);
		model.addAttribute(ModelMappingValue.var_childrenList, rootList);
		return ModelMappingValue.pages_view_showChildren;
	}

	@RequestMapping(value = "/ShowChildreUnderParent.do")
	public String showChildrenListUnderParent(HttpServletRequest request, ModelMap model,
			@RequestParam("parentId") String parentId) {

		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

		LocationTreeNode locationNode = locationTree.getCurrentLocationTreeNode(parentId);

		List<LocationTreeNode> locationTreeNodeList = locationNode.getChildren();

		if (locationTreeNodeList != null && locationTreeNodeList.size() > 0) {

			List<Location> children = new ArrayList<Location>();
			for (int i = 0; i < locationTreeNodeList.size(); i++) {

				Location loc = locationTreeNodeList.get(i).getLocation();

				List<LocationTreeNode> hasChildrenList = locationTreeNodeList.get(i).getChildren();
				if (hasChildrenList != null && hasChildrenList.size() > 0) {

				} else {
					loc.setHasChildren(false);
				}
				loc.setHasPivilege(locationTreeNodeList.get(i).isPrivileged());

				children.add(loc);
			}

			model.addAttribute(ModelMappingValue.var_parentkey, locationNode.getLocation().getKey());
			model.addAttribute(ModelMappingValue.var_childrenList, children);

		} else {

			List<Location> children = new ArrayList<Location>();
			List<LocationTreeNode> currentList = locationNode.getParent().getChildren();
			for (int i = 0; i < currentList.size(); i++) {
				Location loc = currentList.get(i).getLocation();

				List<LocationTreeNode> hasChildrenList = currentList.get(i).getChildren();
				if (hasChildrenList != null && hasChildrenList.size() > 0) {

				} else {
					loc.setHasChildren(false);
				}

				loc.setHasPivilege(currentList.get(i).isPrivileged());
				children.add(loc);
			}
			model.addAttribute(ModelMappingValue.var_parentkey, locationNode.getParent().getLocation().getKey());
			model.addAttribute(ModelMappingValue.var_childrenList, children);
		}

		return ModelMappingValue.pages_view_showChildren;

	}

	@RequestMapping(value = "/ShowParent.do")
	public String showParent(HttpServletRequest request, ModelMap model, @RequestParam("parentId") String parentId) {
		LocationTreeNode locationTree = getLocationTreeNodeFormSession(request);

		if (parentId.equals("0")) {

			return showChildrenList(request, model);
		} else {
			List<Location> children = new ArrayList<Location>();
			LocationTreeNode locationNode = locationTree.getCurrentLocationTreeNode(parentId);

			if (locationNode.getParent() == null) {

				Location loc = locationNode.getLocation();
				loc.setHasPivilege(locationNode.isPrivileged());

				children.add(loc);
				model.addAttribute(ModelMappingValue.var_parentkey, 0);

			} else {

				List<LocationTreeNode> currentList = locationNode.getParent().getChildren();
				for (int i = 0; i < currentList.size(); i++) {

					Location loc = currentList.get(i).getLocation();
					loc.setHasPivilege(currentList.get(i).isPrivileged());

					children.add(loc);
				}

				model.addAttribute(ModelMappingValue.var_parentkey, locationNode.getParent().getLocation().getKey());

			}

			model.addAttribute(ModelMappingValue.var_childrenList, children);

			return ModelMappingValue.pages_view_showChildren;
		}
	}

	@RequestMapping(value = "/ShowSelectedLocation.do")
	// public String showSelectedLocation(HttpServletRequest request,
	// ModelMap model, @RequestBody List<LocationJSON> locationJson) {
	public String showSelectedLocation(HttpServletRequest request, ModelMap model) {

		String json = "";
		// if (locationJson != null) {
		// json = locationJsonToString(locationJson);
		// }

		model.addAttribute(ModelMappingValue.var_selectedLocation, null);
		model.addAttribute(ModelMappingValue.var_locationJson, json);

		return ModelMappingValue.pages_view_showSelectedLocation;
	}

	@RequestMapping(value = "/RefreshSelectedLocation.do")
	public String refreshSelectedLocation(HttpServletRequest request, ModelMap model, List<LocationJSON> locationJson,
			String oldLocationJson) {

		String json = "";
		if (locationJson != null) {
			json = locationJsonToString(locationJson);
		}

		for (LocationJSON locJson : locationJson) {
			try {
				Location loc = locationManager.getLocationByKey(locJson.getLocationKey());
				locJson.setName(loc.getName());
				locJson.setCode(loc.getCode());
				locJson.setDesc(loc.getDesc());
				locJson.setTagId(loc.getTagId());
				locJson.setLocationKey(locJson.getLocationKey());
				
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		model.addAttribute(ModelMappingValue.var_selectedLocation, locationJson);
		model.addAttribute(ModelMappingValue.var_locationJson, json);
		model.addAttribute(ModelMappingValue.var_oldLocationJson, oldLocationJson);

		return ModelMappingValue.pages_view_showSelectedLocation;
	}

	@RequestMapping(value = "/RecoverData.do")
	public String recoverData(HttpServletRequest request, ModelMap model,
			@RequestParam("oldLocationJson") String jsonString) {

		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

		return refreshSelectedLocation(request, model, locationJson, jsonString);

	}

	@RequestMapping(value = "/AddSelectedLocation.do", method = RequestMethod.POST)
	public String addSelectedLocation(HttpServletRequest request, ModelMap model,
			@RequestParam("locationId") String locationId, @RequestParam("minTime") String minTime,
			@RequestParam("maxTime") String maxTime, @RequestParam("locationJson") String jsonString) {

		try {

			int tmp = Integer.valueOf(minTime);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			minTime = "0";
		}
		try {

			int tmp = Integer.valueOf(maxTime);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			maxTime = "0";
		}

		try {

			List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

			Location location = locationManager.getLocationByKey(Integer.parseInt(locationId));

			if (location != null) {

				// Patrol_SelectedLocation selectedLocation = new
				// Patrol_SelectedLocation();
				// selectedLocation.setLocationKey(location.getKey());
				// selectedLocation.setLocationName(location.getName());
				//
				// if (!minTime.equals("0")) {
				// selectedLocation.setMinTime(Integer.parseInt(minTime));
				// }
				// if (!maxTime.equals("0")) {
				// selectedLocation.setMaxTime(Integer.parseInt(maxTime));
				// }

				// selectedLocationList.add(selectedLocation);

				LocationJSON json = new LocationJSON();
				// json.setCode(location.getCode());
				// json.setDesc(location.getDesc());
				json.setLocationKey(location.getKey());
				// json.setName(location.getName());
				json.setMaxTime(Integer.parseInt(maxTime));
				json.setMinTime(Integer.parseInt(minTime));
				json.setSeq(locationJson.size() + 1);
				// json.setTagId(location.getTagId());

				locationJson.add(json);

			}

			return refreshSelectedLocation(request, model, locationJson, jsonString);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	@RequestMapping(value = "/DropSelectedLocation.do")
	public String dropSelectedLocation(HttpServletRequest request, ModelMap model,
			@RequestParam("indexId") Integer index, @RequestParam("locationJson") String jsonString) {

		try {

			List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

			// delete that location
			for (int i = 0; i < locationJson.size(); i++) {
				if (locationJson.get(i).getSeq() == index) {
					locationJson.remove(i);

					System.out.println("Location Remove");
					break;
				}
			}

			for (int i = 0; i < locationJson.size(); i++) {
				for (int j = i + 1; j < locationJson.size(); j++) {

					if (locationJson.get(i).getSeq() > locationJson.get(j).getSeq()) {
						LocationJSON tmp = locationJson.get(i);
						locationJson.set(i, locationJson.get(j));
						locationJson.set(j, tmp);

					}

				}
			}

			int counter = 1;
			for (LocationJSON locJ : locationJson) {
				locJ.setSeq(counter++);
			}

			// locationJson.remove(Integer.parseInt(index));

			return refreshSelectedLocation(request, model, locationJson, jsonString);

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return "";
	}

	
	@RequestMapping(value = "/DropAllSelectedLocation.do")
	public String dropAllSelectedLocation(HttpServletRequest request, ModelMap model, @RequestParam("locationJson") String jsonString) {
		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);
		locationJson.clear();
		return refreshSelectedLocation(request, model, locationJson, jsonString);
	}
	
	@RequestMapping(value = "/RearrangeLocation.do")
	public String rerrangeSelectedLocation(HttpServletRequest request, ModelMap model,
			@RequestParam("orginalIndex") int originalIndex, @RequestParam("newIndex") int newIndex,
			@RequestParam("locationJson") String jsonString) {

		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

		// rearrange the order
		for (LocationJSON location : locationJson) {

			if (location.getSeq() == originalIndex + 1) {
				location.setSeq(newIndex + 1);
				break;
			}
		}
		LocationJSON temp = locationJson.get(originalIndex);
		locationJson.remove(originalIndex);
		locationJson.add(newIndex, temp);

		// reset the seq
		for (int i = 0; i < locationJson.size(); i++) {
			locationJson.get(i).setSeq(i + 1);

		}

		return refreshSelectedLocation(request, model, locationJson, jsonString);
	}

	@RequestMapping(value = "/MinValueKeyPressChange.do")
	public @ResponseBody String minValueKeyPressChange(HttpServletRequest request, ModelMap model,
			@RequestParam("index") int index, @RequestParam("time") int time,
			@RequestParam("locationJson") String jsonString) {

		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

		for (int i = 0; i < locationJson.size(); i++) {
			if (locationJson.get(i).getSeq() == index) {
				locationJson.get(i).setMinTime(time);
				break;
			}
		}

		String json = "";
		if (locationJson != null) {
			json = locationJsonToString(locationJson);
		}

		return json;
	}

	@RequestMapping(value = "/MaxValueKeyPressChange.do")
	public @ResponseBody String maxValueKeyPressChange(HttpServletRequest request, ModelMap model,
			@RequestParam("index") int index, @RequestParam("time") int time,
			@RequestParam("locationJson") String jsonString) {

		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

		for (int i = 0; i < locationJson.size(); i++) {
			if (locationJson.get(i).getSeq() == index) {
				locationJson.get(i).setMaxTime(time);
				break;
			}
		}

		String json = "";
		if (locationJson != null) {
			json = locationJsonToString(locationJson);
		}

		return json;
	}

	@RequestMapping(value = "/MinValueChange.do")
	public String minValueChange(HttpServletRequest request, ModelMap model, @RequestParam("index") int index,
			@RequestParam("time") int time, @RequestParam("locationJson") String jsonString) {

		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

		for (int i = 0; i < locationJson.size(); i++) {
			if (locationJson.get(i).getSeq() == index) {
				locationJson.get(i).setMinTime(time);
				break;
			}
		}

		return refreshSelectedLocation(request, model, locationJson, jsonString);
	}

	@RequestMapping(value = "/MaxValueChange.do")
	public String maxValueChange(HttpServletRequest request, ModelMap model, @RequestParam("index") int index,
			@RequestParam("time") int time, @RequestParam("locationJson") String jsonString) {

		List<LocationJSON> locationJson = stringtoLocationJson(jsonString);

		for (int i = 0; i < locationJson.size(); i++) {
			if (locationJson.get(i).getSeq() == index) {
				locationJson.get(i).setMaxTime(time);
				break;
			}
		}

		return refreshSelectedLocation(request, model, locationJson, jsonString);
	}

	@RequestMapping(value = "/ShowErrorMsg.do")
	public String showErrorMsg(ModelMap model) {
		return ModelMappingValue.pages_view_showErrorMsg;
	}

	@RequestMapping(value = "/ShowSuccessMsg.do")
	public String showSuccessMsg(@RequestParam("msg") String msg, @RequestParam("extraMsg") String extraMsg,
			ModelMap model, Locale locale) throws InterruptedException {

		System.out.println("ShowSuccessMsg : " + msg);

		String reply = "";
		String className = "alert-success";

		switch (msg) {
		case ModelMappingValue.value_createSuccess:
			reply = messageSource.getMessage("patrol.route.create.success", null, locale);
			break;
		case ModelMappingValue.value_modifySuccess:
			reply = messageSource.getMessage("patrol.route.modify.success", null, locale);
			break;
		case ModelMappingValue.value_deleteSuccess:
			try {
				Patrol_Route route = routeDefManager.searhRoute(Integer.parseInt(extraMsg));
				reply = messageSource.getMessage("patrol.route.delete.success",
						new Object[] { route.getRouteDef().getCode() }, locale);
				className = "alert-success";
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		case ModelMappingValue.value_createScheduleSuccess:
			reply = messageSource.getMessage("patrol.schedule.create.success", null, locale);

			break;
		case ModelMappingValue.value_deleteScheduleSuccess:
			reply = messageSource.getMessage("patrol.schedule.delete", null, locale);
			className = "alert-success";
			break;
			
		case ModelMappingValue.value_pastScheduleCannotBeDeleted:
			reply = messageSource.getMessage("patrol.schedule.cannot.delete", null, locale);
			className = "alert-danger";
			break;
			
		case ModelMappingValue.value_deleteFutureNotDeleteCurrent:
			reply = messageSource.getMessage("patrol.schedule.deletefuture.cannotdeletecurrent", null, locale);
			className = "alert-success";
			break;
		default:
			break;
		}

		model.addAttribute(ModelMappingValue.var_successMsgStr, reply);
		model.addAttribute(ModelMappingValue.var_successMsgClass, className);

		return ModelMappingValue.pages_view_showSuccessMsg;
	}

	@RequestMapping(value = "/SubmitPatrolForm.do")
	public String submitPatrolForm(@ModelAttribute("patrolCreateForm") PatrolCreateForm form, BindingResult result,
			HttpServletRequest request,

			HttpServletResponse response, ModelMap model, Locale locale) throws MFMSException {

		System.out.println("submitPatrolForm");

		int rtnKey = -1;

		System.out.println("Code :" + form.getRouteCode() + " || route Name " + form.getRouteName());

		List<LocationJSON> locationJson = stringtoLocationJson(form.getSelectedLocationJson());

		form.setSiteKey(this.getRoleFromSession(request).getSiteKey());

		patrolCreateFormValidator.validate(form, result);

		if (result.hasErrors()) {

			for (LocationJSON locJson : locationJson) {
				try {
					Location loc = locationManager.getLocationByKey(locJson.getLocationKey());
					locJson.setName(loc.getName());
					locJson.setCode(loc.getCode());
					locJson.setDesc(loc.getDesc());
					locJson.setTagId(loc.getTagId());

				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			model.addAttribute(ModelMappingValue.var_selectedLocation, locationJson);
			model.addAttribute(ModelMappingValue.var_locationJson, form.getSelectedLocationJson());
			model.addAttribute(ModelMappingValue.var_oldLocationJson, form.getSelectedLocationJson());
			System.out.println(form.getSelectedLocationJson());

			model.addAttribute(ModelMappingValue.var_timeUnit, getTimeUnit(locale));
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_create);

			return ModelMappingValue.pages_patrolCreate;
		} else {

			Role role = getRoleFromSession(request);

			RouteDef routeDef = new RouteDef();
			routeDef.setSiteKey(role.getSiteKey());
			routeDef.setName(form.getRouteName());
			routeDef.setCode(form.getRouteCode());
			routeDef.setDefMinPtDur(form.getRouteMinTime());
			routeDef.setDefMinPtDurUnit(Integer.parseInt(form.getRouteMinTimeUnit()));
			routeDef.setDefMaxPtDur(form.getRouteMaxTime());
			routeDef.setDefMaxPtDurUnit(Integer.parseInt(form.getRouteMaxTimeUnit()));
			routeDef.setCreateBy(role.getKey());
			routeDef.setCreateDateTime(getCurrentTime());
			routeDef.setLastModifyBy(role.getKey());
			routeDef.setLastModifyDateTime(getCurrentTime());
			routeDef.setDeleted("N");

			List<RouteLocation> routeLocationList = new ArrayList<RouteLocation>();
			RouteLocation routeLocation = new RouteLocation();
			if (locationJson != null) {
				for (LocationJSON location : locationJson) {
					routeLocation = new RouteLocation();

					routeLocation.setLocationKey(location.getLocationKey());
					routeLocation.setSeqNum(location.getSeq());
					routeLocation.setMinPtDur(location.getMinTime());
					routeLocation.setMinPtDurUnit(Integer.parseInt(form.getRouteMinTimeUnit()));
					routeLocation.setMaxPtDur(location.getMaxTime());
					routeLocation.setMaxPtDurUnit(Integer.parseInt(form.getRouteMaxTimeUnit()));
					routeLocation.setRemark("");
					routeLocation.setCreateBy(role.getKey());
					routeLocation.setCreateDateTime(getCurrentTime());
					routeLocation.setLastModifyBy(role.getKey());
					routeLocation.setLastModifyDateTime(getCurrentTime());
					routeLocation.setDeleted("N");
					routeLocationList.add(routeLocation);
				}
			}

			try {
				rtnKey = routeDefManager.saveRoute(routeDef, routeLocationList);
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// resetData();

			// Patrol Action Log
			Role currRole = (Role) request.getSession().getAttribute("currRole");
			if (null==currRole) {
				return "main/site_select";
			}
			UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
			RouteDefActionLog routeDefActionLog = new RouteDefActionLog();
			routeDefActionLog.setSiteKey(currRole.getSiteKey());
			routeDefActionLog.setActionType("Create Patrol");
			routeDefActionLog.setActionBy(actionBy.getKey());
			routeDefActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
			try {
				routeDefActionLogManager.saveRouteDefActionLog(routeDefActionLog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// End Patrol Action Log

			// return showPatrolCreate(request, response, model, locale);
			if (rtnKey > 0) {
				model.addAttribute(ModelMappingValue.var_submitStatus, ModelMappingValue.value_createSuccess);
				return showPatrolView(rtnKey, request, response, model, locale);
			} else {
				return showPatrolCreate(request, response, model, locale);
			}
		}
		// for (Object object : result.getAllErrors()) {
		// if (object instanceof FieldError) {
		// FieldError fieldError = (FieldError) object;
		//
		// System.out.println(" COde : " + fieldError.getCode());
		// System.out.println(" DefaultMessage : "
		// + fieldError.getDefaultMessage());
		// System.out.println(" ObjectName : "
		// + fieldError.getObjectName());
		// System.out.println(" Field : " + fieldError.getField());
		// /**
		// * Use null as second parameter if you do not use i18n
		// * (internationalization)
		// */
		//
		// String message = messageSource.getMessage(fieldError, null);
		// System.out.println(" message : " + message);
		// }
		// }
		//
		// if (result.hasErrors()) {
		// System.out.println("Has error");
		// }

	}

	@RequestMapping(value = "/ClearPatrolCreateData.do")
	public void clearData(@RequestParam("mode") String mode, HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		// resetData();
		// if (mode.equals(ModelMappingValue.mode_edit)) {
		// selectedLocationList = new ArrayList<Patrol_SelectedLocation>();
		// selectedLocationList.addAll(temp_selectedLocationList);
		// }

	}

	/**                    **/
	/** Functions for Edit **/
	/**                    **/
	@RequestMapping(value = "/PatrolEdit.do", method = RequestMethod.GET)
	public String showPatrolEdit(@RequestParam("routeKey") int routeDefKey, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, Locale locale) {

		logger.debug("User requests to load edit patrol route page.");
		
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyRoute"));

		if (hasPrivilege) {
			// resetData();
	
			// generate the map for the units
			Map<String, String> timeUnit = getTimeUnit(locale);
	
			PatrolCreateForm form = getEditAndViewForm(routeDefKey);
	
			String json = this.locationJsonToString(form.getSelectedLocation());
	
			List<LocationJSON> locationJson = form.getSelectedLocation();
			for (LocationJSON locJson : locationJson) {
				try {
					Location loc = locationManager.getLocationByKey(locJson.getLocationKey());
					locJson.setName(loc.getName());
					locJson.setCode(loc.getCode());
					locJson.setDesc(loc.getDesc());
					locJson.setTagId(loc.getTagId());
	
				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			}
	
			model.addAttribute(ModelMappingValue.var_selectedLocation, locationJson);
			model.addAttribute(ModelMappingValue.var_locationJson, json);
			model.addAttribute(ModelMappingValue.var_oldLocationJson, json);
			System.out.println(json);
			model.addAttribute(ModelMappingValue.form_patrolCreateForm, form);
			model.addAttribute(ModelMappingValue.var_timeUnit, timeUnit);
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_edit);
	
			return ModelMappingValue.pages_patrolCreate;
		} else {
			// user does not have right to create account
			logger.debug("user does not have right to create account ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/SubmitEditPatrolForm.do")
	public String submitEditPatrolForm(@ModelAttribute("patrolCreateForm") PatrolCreateForm form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException {

		form.setSiteKey(this.getRoleFromSession(request).getSiteKey());

		patrolCreateFormValidator.validate(form, result);

		if (result.hasErrors()) {

			List<LocationJSON> locationJson = stringtoLocationJson(form.getSelectedLocationJson());

			for (LocationJSON locJson : locationJson) {
				try {
					Location loc = locationManager.getLocationByKey(locJson.getLocationKey());
					locJson.setName(loc.getName());
					locJson.setCode(loc.getCode());
					locJson.setDesc(loc.getDesc());
					locJson.setTagId(loc.getTagId());

				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			model.addAttribute(ModelMappingValue.var_selectedLocation, locationJson);
			model.addAttribute(ModelMappingValue.var_locationJson, form.getSelectedLocationJson());
			model.addAttribute(ModelMappingValue.var_oldLocationJson, form.getSelectedLocationJson());
			model.addAttribute(ModelMappingValue.var_timeUnit, getTimeUnit(locale));
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_edit);

			return ModelMappingValue.pages_patrolCreate;
			// showPatrolEdit(form.getRouteDefKey(), request, response, model,
			// locale);
		} else {

			try {

				List<LocationJSON> locationJson = stringtoLocationJson(form.getSelectedLocationJson());

				Patrol_Route patrolRoute = routeDefManager.searhRoute(form.getRouteDefKey());

				Role role = this.getRoleFromSession(request);

				RouteDef routeDef = patrolRoute.getRouteDef();

				routeDef.setCode(form.getRouteCode());
				routeDef.setName(form.getRouteName());
				routeDef.setDefMinPtDur(form.getRouteMinTime());
				routeDef.setDefMaxPtDur(form.getRouteMaxTime());
				routeDef.setDefMinPtDurUnit(Integer.parseInt(form.getRouteMinTimeUnit()));
				routeDef.setDefMaxPtDurUnit(Integer.parseInt(form.getRouteMaxTimeUnit()));
				routeDef.setLastModifyBy(role.getKey());
				routeDef.setLastModifyDateTime(getCurrentTime());

				List<RouteLocation> orginalRouteLocationList = patrolRoute.getRouteLocation();

				List<RouteLocation> routeLocationList = new ArrayList<RouteLocation>();
				RouteLocation routeLocation = new RouteLocation();
				for (LocationJSON location : locationJson) {

					System.out.println(" location MIN TIME : " + location.getMinTime());
					System.out.println(" location MIN TIME 2: " + location.getMaxTime());

					if (location.getRouteLocationKey() == null || location.getRouteLocationKey() == 0) {
						routeLocation = new RouteLocation();

						routeLocation.setLocationKey(location.getLocationKey());
						routeLocation.setSeqNum(location.getSeq());
						routeLocation.setMinPtDur(location.getMinTime());
						routeLocation.setMinPtDurUnit(Integer.parseInt(form.getRouteMinTimeUnit()));
						routeLocation.setMaxPtDur(location.getMaxTime());
						routeLocation.setMaxPtDurUnit(Integer.parseInt(form.getRouteMaxTimeUnit()));
						routeLocation.setRemark("");
						routeLocation.setCreateBy(role.getKey());
						routeLocation.setCreateDateTime(getCurrentTime());
						routeLocation.setLastModifyBy(role.getKey());
						routeLocation.setLastModifyDateTime(getCurrentTime());
						routeLocation.setDeleted("N");

						routeLocationList.add(routeLocation);

					} else {

						routeLocation = new RouteLocation();
						routeLocation = routeDefManager.searchRouteLocation(location.getRouteLocationKey());

						routeLocation.setSeqNum(location.getSeq());
						routeLocation.setMinPtDur(location.getMinTime());
						routeLocation.setMinPtDurUnit(Integer.parseInt(form.getRouteMinTimeUnit()));
						routeLocation.setMaxPtDur(location.getMaxTime());
						routeLocation.setMaxPtDurUnit(Integer.parseInt(form.getRouteMaxTimeUnit()));
						routeLocation.setLastModifyBy(role.getKey());
						routeLocation.setLastModifyDateTime(getCurrentTime());
						routeLocation.setRemark("");

						RouteLocation orginalRouteLocation = null;
						for (int j = 0; j < orginalRouteLocationList.size(); j++) {
							if (orginalRouteLocationList.get(j).getRouteLocationKey() == routeLocation
									.getRouteLocationKey()) {
								orginalRouteLocation = orginalRouteLocationList.get(j);
								break;
							}
						}

						// if (routeLocation.getSeqNum() != location.getSeq()) {
						// routeLocation.setSeqNum(location.getSeq());
						// routeLocation.setLastModifyBy(role.getKey());
						// routeLocation
						// .setLastModifyDateTime(getCurrentTime());
						// }
						//
						// if (routeLocation.getMaxPtDur() !=
						// orginalRouteLocation
						// .getMaxPtDur()) {
						// routeLocation.setMaxPtDur(routeLocation
						// .getMaxPtDur());
						// routeLocation.setLastModifyBy(role.getKey());
						// routeLocation
						// .setLastModifyDateTime(getCurrentTime());
						// }
						//
						// if (routeLocation.getMinPtDur() !=
						// orginalRouteLocation
						// .getMinPtDur()) {
						// routeLocation.setMinPtDur(routeLocation
						// .getMinPtDur());
						// routeLocation.setLastModifyBy(role.getKey());
						// routeLocation
						// .setLastModifyDateTime(getCurrentTime());
						// }

					}

					routeLocationList.add(routeLocation);

				}

				// Patrol Action Log
				Role currRole = (Role) request.getSession().getAttribute("currRole");
				if (null==currRole) {
					return "main/site_select";
				}
				UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
				RouteDefActionLog routeDefActionLog = new RouteDefActionLog();
				routeDefActionLog.setSiteKey(currRole.getSiteKey());
				routeDefActionLog.setActionType("Edit Patrol");
				routeDefActionLog.setActionBy(actionBy.getKey());
				routeDefActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
				try {
					routeDefActionLogManager.saveRouteDefActionLog(routeDefActionLog);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// End Patrol Action Log

				// for (int i = 0; i < edt_routeLocationList.size(); i++) {
				//
				// RouteLocation routeLocation = edt_routeLocationList.get(i);
				// if (routeLocation.getSeqNum() != i + 1) {
				// routeLocation.setSeqNum(i + 1);
				// routeLocation.setLastModifyBy(this.getRoleFromSession(
				// request).getKey());
				// routeLocation.setLastModifyDateTime(getCurrentTime());
				//
				// edt_routeLocationList.set(i, routeLocation);
				// }
				//
				// }

				routeDefManager.saveRoute(routeDef, routeLocationList);
				if (orginalRouteLocationList != null && orginalRouteLocationList.size() > 0) {
					for (int i = 0; i < orginalRouteLocationList.size(); i++) {
						for (int j = 0; j < routeLocationList.size(); j++) {
							System.out.println("Orgin : " + orginalRouteLocationList.get(i).getRouteLocationKey()
									+ " || route List : " + routeLocationList.get(j).getRouteLocationKey());

							if (routeLocationList.get(j).getRouteLocationKey() == 0) {
								System.out.println("This route : 0 routeLocation Key");

							} else {
								if (routeLocationList.get(j).getRouteLocationKey() == orginalRouteLocationList.get(i)
										.getRouteLocationKey()) {
									orginalRouteLocationList.remove(i);
									i--;
									System.out
											.println("This route : " + routeLocationList.get(j).getRouteLocationKey());
									break;
								}
							}
						}

					}

					// for (RouteLocation tmp : orginalRouteLocationList) {
					// tmp.setDeleted("Y");
					// tmp.setLastModifyBy(role.getKey());
					// tmp.setLastModifyDateTime(getCurrentTime());
					// }
					// routeDefManager
					// .updateRouteLocation(orginalRouteLocationList);
					routeDefManager.deleteRouteLocation(orginalRouteLocationList);

				}
			} catch (MFMSException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		model.addAttribute(ModelMappingValue.var_submitStatus, ModelMappingValue.value_modifySuccess);
		return showPatrolView(form.getRouteDefKey(), request, response, model, locale);
		// return showPatrolEdit(form.getRouteDefKey(), request, response,
		// model,
		// locale);

	}

	/**                    **/
	/** Functions for View **/
	/**
	 * @throws MFMSException
	 **/

	@RequestMapping(value = "/PatrolView.do", method = RequestMethod.GET)
	public String showPatrolView(@RequestParam("routeKey") int routeDefKey, HttpServletRequest request,
			HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException {

		logger.debug("User requests to load view patrol route page.");
		
		// Patrol Action Log
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchRoute"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createRoute"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyRoute"));

		if (hasPrivilege) {
			UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
			RouteDefActionLog routeDefActionLog = new RouteDefActionLog();
			routeDefActionLog.setSiteKey(currRole.getSiteKey());
			routeDefActionLog.setActionType("View Patrol");
			routeDefActionLog.setActionBy(actionBy.getKey());
			routeDefActionLog.setRouteDefKey(routeDefKey);
			routeDefActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
			try {
				routeDefActionLogManager.saveRouteDefActionLog(routeDefActionLog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// End Patrol Action Log
	
			Map<String, String> timeUnit = getTimeUnit(locale);
	
			PatrolCreateForm form = getEditAndViewForm(routeDefKey);
			String json = this.locationJsonToString(form.getSelectedLocation());
	
			List<LocationJSON> locationJson = form.getSelectedLocation();
	
			for (LocationJSON locJson : locationJson) {
				try {
					Location loc = locationManager.getLocationByKey(locJson.getLocationKey());
					locJson.setName(loc.getName());
					locJson.setCode(loc.getCode());
					locJson.setDesc(loc.getDesc());
					locJson.setTagId(loc.getTagId());
	
				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
			}
	
			model.addAttribute(ModelMappingValue.var_selectedLocation, locationJson);
			model.addAttribute(ModelMappingValue.var_locationJson, json);
			model.addAttribute(ModelMappingValue.var_oldLocationJson, json);
			System.out.println(json);
	
			model.addAttribute(ModelMappingValue.form_patrolCreateForm, form);
			model.addAttribute(ModelMappingValue.var_timeUnit, timeUnit);
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_view);
			model.addAttribute(ModelMappingValue.var_canEdit, currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyRoute")));
	
			return ModelMappingValue.pages_patrolCreate;
		} else {
			// user does not have right to view patrol
			logger.debug("user does not have right to view patrol ");
			return "common/noprivilege";
		}
	}

	/**                      **/
	/** Functions for Search **/
	/**                      **/

	@RequestMapping(value = "/SearchPatrolRoute.do")
	public String searchPatrolRoute(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load search patrol route page.");
		
		// resetData();
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchRoute"));

		if (hasPrivilege) {
			PatrolSearchForm form = new PatrolSearchForm();
	
			model.addAttribute(ModelMappingValue.form_patrolSearchForm, form);
			model.addAttribute(ModelMappingValue.var_mode, ModelMappingValue.mode_search);
	
			return ModelMappingValue.pages_patrolSearch;
		} else {
			// user does not have right to search patrol route
			logger.debug("user does not have right to search patrol route ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "/ShowStartingLocation.do")
	public String showStartingLocation(HttpServletRequest request, ModelMap model) {

		Map<String, String> location = new LinkedHashMap<String, String>();

		// if (locationJson != null) {
		//
		// for (LocationJSON selectedLocation : locationJson) {
		//
		// if (location.containsKey(selectedLocation.getLocationKey()
		// .toString())) {
		// } else {
		// location.put(selectedLocation.getLocationKey().toString(),
		// selectedLocation.getName());
		// }
		// }
		//
		// }

		model.addAttribute(ModelMappingValue.var_startingLocation, location);
		return ModelMappingValue.pages_view_showStartingLocation;
	}

	@RequestMapping(value = "/RefreshStartingLocation.do")
	public String refreshStartingLocation(@RequestParam("locationJson") String jsonString, HttpServletRequest request,
			ModelMap model) {

		Map<String, String> location = new LinkedHashMap<String, String>();

		List<LocationJSON> locationJson = this.stringtoLocationJson(jsonString);

		if (locationJson != null) {

			for (LocationJSON selectedLocation : locationJson) {

				if (location.containsKey(selectedLocation.getLocationKey().toString())) {
				} else {
					try {
						Location loc = locationManager.getLocationByKey(selectedLocation.getLocationKey());

						location.put(selectedLocation.getLocationKey().toString(),
								loc.getCode() + " - " + loc.getName());
					} catch (MFMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

		model.addAttribute(ModelMappingValue.var_startingLocation, location);
		return ModelMappingValue.pages_view_showStartingLocation;
	}

	@RequestMapping(value = "/ShowSearchTable.do")
	public String showShowSearchTable(HttpServletRequest request, ModelMap model) {
		return ModelMappingValue.pages_view_searchTable;
	}

	@RequestMapping(value = "/SubmitSearch.do")
	public String submitSearch(@ModelAttribute("patrolSearchForm") PatrolSearchForm form, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		String name = form.getRouteName();
		String code = form.getRouteCode();

		List<LocationJSON> locationJson = this.stringtoLocationJson(form.getSelectedLocationJson());

		List<Integer> locationList = new ArrayList<Integer>();
		if (locationJson != null) {
			for (LocationJSON location : locationJson) {
				locationList.add(location.getLocationKey());
			}
		}

		List<Patrol_Route> patrolRouteList = routeDefManager.searchRoute(this.getRoleFromSession(request).getSiteKey(),
				name, code, locationList);
		// System.out.println("Search Result : " + patrolRouteList.size());

		List<Patrol_SearchTableRoute> search_patrolSearchTableList = new ArrayList<Patrol_SearchTableRoute>();
		if (patrolRouteList != null) {
			Patrol_SearchTableRoute searchTable = new Patrol_SearchTableRoute();
			for (int i = 0; i < patrolRouteList.size(); i++) {
				searchTable = new Patrol_SearchTableRoute();
				searchTable.setKey(patrolRouteList.get(i).getRouteDef().getRouteDefKey());
				searchTable.setName(patrolRouteList.get(i).getRouteDef().getName());
				searchTable.setCode(patrolRouteList.get(i).getRouteDef().getCode());

				List<RouteLocation> routeLocationList = patrolRouteList.get(i).getRouteLocation();
				if (routeLocationList != null && routeLocationList.size() > 0) {
					try {
						Location location = locationManager.getLocationByKey(routeLocationList.get(0).getLocationKey());
						searchTable.setStartingLocation(location.getCode() + " - " + location.getName());
						searchTable.setStartingLocationKey(location.getKey());
					} catch (MFMSException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				search_patrolSearchTableList.add(searchTable);
			}
		}

		pagination.setList(search_patrolSearchTableList);

		model.addAttribute(ModelMappingValue.var_currentPage, pagination.getCurrentPage());
		model.addAttribute(ModelMappingValue.var_pageSize, pagination.getPageSize());
		model.addAttribute(ModelMappingValue.var_startPage, pagination.getStartPage());
		model.addAttribute(ModelMappingValue.var_totalPages, pagination.getTotalPage());
		model.addAttribute(ModelMappingValue.var_searchResult, pagination.getPageTableData());
		model.addAttribute(ModelMappingValue.var_searchResultJson, pagination.getResultJson());

		form.setCanGen(true);


		Boolean canModify = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyRoute"));

		Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.removeRoute"));

		form.setResultList(search_patrolSearchTableList);
		
		form.setCanModify(canModify);

		form.setCanRemove(canRemove);
		
		// Patrol Action Log
		UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
		RouteDefActionLog routeDefActionLog = new RouteDefActionLog();
		routeDefActionLog.setSiteKey(currRole.getSiteKey());
		routeDefActionLog.setActionType("Search Patrol");
		routeDefActionLog.setActionBy(actionBy.getKey());
		routeDefActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));

		routeDefActionLogManager.saveRouteDefActionLog(routeDefActionLog);
		
		logger.debug("search_patrolSearchTableList size : " + search_patrolSearchTableList.size());
		return ModelMappingValue.pages_view_searchTable;
	}
	
	@RequestMapping(value = "/RouteNumber.do")
	public void routeNumber(@ModelAttribute("patrolSearchForm") PatrolSearchForm form, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException, Exception {

		String name = form.getRouteName();
		String code = form.getRouteCode();

		List<LocationJSON> locationJson = this.stringtoLocationJson(form.getSelectedLocationJson());

		List<Integer> locationList = new ArrayList<Integer>();
		if (locationJson != null) {
			for (LocationJSON location : locationJson) {
				locationList.add(location.getLocationKey());
			}
		}

		List<Patrol_Route> patrolRouteList = routeDefManager.searchRoute(this.getRoleFromSession(request).getSiteKey(),
				name, code, locationList);
		if(patrolRouteList!=null && patrolRouteList.size()>0){
		response.getWriter().write(String.valueOf(patrolRouteList.size()));
		}else{
			response.getWriter().write("0");
		}	
	}

	@RequestMapping(value = "DoPatrolDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("patrolSearchForm") PatrolSearchForm form,
			HttpServletRequest request, HttpServletResponse response, Locale locale)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "code", "name", "location" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Patrol_SearchTableRoute> fullList = new ArrayList<Patrol_SearchTableRoute>();

		List<Patrol_SearchTableRoute> patrolList = new ArrayList<Patrol_SearchTableRoute>();

		if (form.getCanGen()) {

			fullList = form.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new PCodeComparator());

				break;

			case "code":

				Collections.sort(fullList, new PCodeComparator());

				break;

			case "name":

				Collections.sort(fullList, new PNameComparator());

				break;

			case "location":
				Collections.sort(fullList, new PLocationComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					patrolList.add(fullList.get(i));
			}

			fullListSize = form.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < patrolList.size(); i++) {

			JSONArray ja = new JSONArray();
			Patrol_SearchTableRoute patrol = patrolList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = form.getCanModify();
			boolean canRemove = form.getCanRemove();

			myvar.append(" <a onclick=\"viewRoute(" + patrol.getKey() + ")\"> ")
				.append(" <i class=\"fa fa-search\"></i></a> &nbsp;");

			if (canModify) {

				myvar.append(" <a onclick=\"editRoute(" + patrol.getKey() + ")\"> ")
						.append(" <i class=\"fa fa-pencil\"></i></a> &nbsp;");

			}

			if (canRemove) {
				
				Calendar after = Calendar.getInstance();
				after.set(Calendar.MILLISECOND, 999);
				after.set(Calendar.SECOND, 59);
				after.set(Calendar.MINUTE, 59);
				after.set(Calendar.HOUR_OF_DAY, 23);
				

				List<PatrolSchedule> scheduleList =  patrolScheduleManager.searchPatrolScheduleAfter(patrol.getKey(), after);
				
				System.out.println("scheduleList : "+scheduleList.size());
				
				if(scheduleList != null && scheduleList.size() >0){
					
					Timestamp lastEnd = new Timestamp(0);
					
					for(PatrolSchedule ps : scheduleList){
						if(lastEnd.getTime() < ps.getScheduleEndDate().getTime()){
							lastEnd = ps.getScheduleEndDate();
						}
					}
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					
					myvar.append(" <div style='cursor:no-drop;display:inline-block;' data-html='true' data-toggle='tooltip' data-placement='right' title='"
								+ messageSource.getMessage("patrol.route.schedule.end",
								new Object[] { sdf.format(new Date(lastEnd.getTime())) }, locale)+"'>")
					.append(" <i class=\"fa fa-trash-o\"></i></div>");
					
				}else{
				
					myvar.append(" <a onclick=\"deleteRoute(" + patrol.getKey() + ")\"> ")
							.append(" <i class=\"fa fa-trash-o\"></i></a>");
				}
			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(patrol.getCode()));
			ja.put(Utility.replaceHtmlEntities(patrol.getName()));
			ja.put(Utility.replaceHtmlEntities(patrol.getStartingLocation()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

	@RequestMapping(value = "/GotoPage.do")
	public String gotoPage(@RequestParam("page") int page, @RequestParam("searchResultJson") String searchResultJson,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		int startPage = 1;

		startPage = (int) ((Math.floor(page / pageSize)) * pageSize + 1);
		if (page % pageSize == 0) {
			startPage -= pageSize;
		}

		ObjectMapper mapper = new ObjectMapper();
		List<Patrol_SearchTableRoute> search_patrolSearchTableList = new ArrayList<Patrol_SearchTableRoute>();

		if (!searchResultJson.equals(""))
			try {
				search_patrolSearchTableList = mapper.readValue(searchResultJson,
						mapper.getTypeFactory().constructCollectionType(List.class, Patrol_SearchTableRoute.class));
				for (Patrol_SearchTableRoute pstr : search_patrolSearchTableList) {
					Location loc = locationManager.getLocationByKey(pstr.getStartingLocationKey());
					pstr.setStartingLocation(loc.getCode() + " - " + loc.getName());
				}

			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		pagination.setList(search_patrolSearchTableList, page);

		model.addAttribute(ModelMappingValue.var_currentPage, pagination.getCurrentPage());
		model.addAttribute(ModelMappingValue.var_pageSize, pagination.getPageSize());
		model.addAttribute(ModelMappingValue.var_startPage, pagination.getStartPage());
		model.addAttribute(ModelMappingValue.var_totalPages, pagination.getTotalPage());
		model.addAttribute(ModelMappingValue.var_searchResult, pagination.getPageTableData());
		model.addAttribute(ModelMappingValue.var_searchResultJson, pagination.getResultJson());

		return ModelMappingValue.pages_view_searchTable;

	}

	@RequestMapping(value = "ViewDeleteRoute.do")
	public @ResponseBody String viewDeleteRoute(@RequestParam("routeKey") int routeDefKey) {

		String code = "";

		try {
			Patrol_Route route = routeDefManager.searhRoute(routeDefKey);

			code = route.getRouteDef().getCode();
		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return code;

	}

	@RequestMapping(value = "DeleteRoute.do", method=RequestMethod.POST)
	public String deleteRoute(@RequestParam("routeKey") int routeDefKey, @RequestParam("currentPage") int currentPage,
			@RequestParam("searchResultJson") String searchResultJson, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {
		Role currRole = (Role) request.getSession().getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchRoute"));

		if (hasPrivilege) {
			routeDefManager.deleteRoute(routeDefKey);
	
			ObjectMapper mapper = new ObjectMapper();
			List<Patrol_SearchTableRoute> search_patrolSearchTableList = new ArrayList<Patrol_SearchTableRoute>();
	
			if (!searchResultJson.equals(""))
				try {
					search_patrolSearchTableList = mapper.readValue(searchResultJson,
							mapper.getTypeFactory().constructCollectionType(List.class, Patrol_SearchTableRoute.class));
	
					for (Patrol_SearchTableRoute searched : search_patrolSearchTableList) {
						if (searched.getKey() == routeDefKey) {
							search_patrolSearchTableList.remove(searched);
							break;
						}
					}
	
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
	
			// Patrol Action Log
			UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
			RouteDefActionLog routeDefActionLog = new RouteDefActionLog();
			routeDefActionLog.setSiteKey(currRole.getSiteKey());
			routeDefActionLog.setActionType("Delete Patrol");
			routeDefActionLog.setActionBy(actionBy.getKey());
			routeDefActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
			try {
				routeDefActionLogManager.saveRouteDefActionLog(routeDefActionLog);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// End Patrol Action Log
	
			int startPage = 1;
	
			startPage = (int) ((Math.floor(currentPage / pageSize)) * pageSize + 1);
			if (currentPage % pageSize == 0) {
				startPage -= pageSize;
			}
	
			pagination.setList(search_patrolSearchTableList, currentPage);
	
			model.addAttribute(ModelMappingValue.var_currentPage, pagination.getCurrentPage());
			model.addAttribute(ModelMappingValue.var_pageSize, pagination.getPageSize());
			model.addAttribute(ModelMappingValue.var_startPage, pagination.getStartPage());
			model.addAttribute(ModelMappingValue.var_totalPages, pagination.getTotalPage());
			model.addAttribute(ModelMappingValue.var_searchResult, pagination.getPageTableData());
			model.addAttribute(ModelMappingValue.var_searchResultJson, pagination.getResultJson());
	
			return ModelMappingValue.pages_view_searchTable;
		} else {
			// user does not have right to remove route
			logger.debug("user does not have right to remove route ");
			return "common/noprivilege";
		}
	}

	@RequestMapping(value = "GetJsonForSelectedLocation.do")
	public @ResponseBody String getJsonForSelectedLocation(@RequestParam("loc") int[] locationKey,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws MFMSException {

		System.out.println("GetJsonForSelectedLocation");

		List<LocationJSON> list = new ArrayList<LocationJSON>();

		for (int i = 0; i < locationKey.length; i++) {

			LocationJSON selected = new LocationJSON();

			Location location = locationManager.getLocationByKey(locationKey[i]);
			// selected.setCode(location.getCode());
			selected.setLocationKey(locationKey[i]);
			// selected.setName(location.getName());
			selected.setSeq(i + 1);

			list.add(selected);

		}

		return this.locationJsonToString(list);
	}

	/**              **/
	/** Patrol Photo **/
	/**
	 * @throws ParseException
	 **/
	@RequestMapping(value = "ShowPatrolPhoto.do", method = RequestMethod.GET)
	public String showPatrolPhoto(@RequestParam("routeDefKey") int routeDefKey, @RequestParam("date") String date,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException {
		
		logger.debug("**************************** date : " + date);
		Calendar calendar = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		if (!date.equals("")) {

			Date tmp = formatter.parse(date);
			calendar.setTime(tmp);
		}
		System.out.println("Calendar : " + calendar);
//		List<PatrolPhoto> photoList = patrolPhotoManager.searchPatrolPhoto(routeDefKey, calendar);
		Integer maxResult = Integer.parseInt(propertyConfigurer.getProperty("patrol.photo.number"));
		
		List<PatrolPhoto> photoList = patrolPhotoManager.searchPatrolPhotoWithLimitation(routeDefKey, calendar, maxResult);
		
		List<PatrolPhotoJSON> rtnList = new ArrayList<PatrolPhotoJSON>();

		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (PatrolPhoto p : photoList) {
			PatrolPhotoJSON json = new PatrolPhotoJSON();

			File f = new File(propertyConfigurer.getProperty("photoPath") + p.getPhotoPath());
			if (f.exists()) {
				json.setPhotoPath(propertyConfigurer.getProperty("photoUrl") + p.getPhotoPath());
			} else {
				json.setPhotoPath("import/img2/no_such_photo.png");
			}
			json.setPhotoKey(p.getPhotoKey());
			json.setRemark(p.getRemark());
			json.setAccountName(p.getPerson().getName());
			json.setAccountKey(p.getCreateBy());

			json.setLocationName(p.getLocation().getName());
			json.setLocationCode(p.getLocation().getCode());
			json.setLocationKey(p.getLocationKey());

			json.setTakenTime(timeFormat.format(new Date(p.getCreateDateTime().getTime())));

			rtnList.add(json);

		}

		if (rtnList.isEmpty()) {
			PatrolPhotoJSON json = new PatrolPhotoJSON();
			json.setPhotoPath("import/img2/no_such_photo.png");
			rtnList.add(json);
		}
		
		model.addAttribute(ModelMappingValue.var_selectedDate, formatter.format(calendar.getTime()));
		model.addAttribute(ModelMappingValue.var_patrolPhotoList, rtnList);

		return ModelMappingValue.pages_view_patrolPhoto;
	}
	
	@RequestMapping(value = "/UpdateRemark.do")
	public @ResponseBody void UpdateRemark(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException, Exception {
		
		Integer key = Integer.parseInt(request.getParameter("key"));
		String remark = request.getParameter("remark");
		UserAccount actionBy = (UserAccount) request.getSession().getAttribute("user");
		logger.debug("Photo Key : " + key + " || Remark content : " + remark +" || user key : " + actionBy.getKey());
		PatrolPhoto ph = patrolPhotoManager.searchPatrolPhotobyKey(key);
		if(ph!=null){
			ph.setRemark(remark);
			ph.setLastModifyDateTime(getCurrentTime());
			ph.setLastModifyBy(actionBy.getKey());
			patrolPhotoManager.savePatrolPhoto(ph);
			response.getWriter().write("success");
			
			//update patrol route action log
			PatrolRouteActionLog pral = new PatrolRouteActionLog();
			pral.setActionType("E");
			pral.setActionDateTime(new Timestamp(System.currentTimeMillis()));
			pral.setPatrolPhotoKey(key);
			pral.setPatrolPhotoRemark(remark);
			pral.setRouteDefKey(ph.getRouteDefkey());
			pral.setLastModifyBy(actionBy.getKey());
			
			logger.debug(pral);
			
			patrolRouteActionLogManager.savePatrolRouteActionLog(pral);
			
		}
		else{
			logger.debug("No patrol Photo found");
			response.getWriter().write("");
		}
	}
	
	@RequestMapping(value = "/PreviousRemark.do")
	public @ResponseBody void PreviousRemark(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model, Locale locale) throws MFMSException, Exception {
		
		Integer key = Integer.parseInt(request.getParameter("key"));
		
		PatrolPhoto ph = patrolPhotoManager.searchPatrolPhotobyKey(key);
		if(ph!=null){
			response.getWriter().write(ph.getRemark());
		}
		else{
			response.getWriter().write("");
		}
	}
	
	

	/**                 **/
	/** Other Functions **/
	/**                 **/
	private List<Location> getAllChildren(LocationTreeNode treeNode) {

		List<LocationTreeNode> treeNodeList = treeNode.getChildren();
		List<Location> locationList = new ArrayList<Location>();

		if (treeNodeList != null) {

			for (int i = 0; i < treeNodeList.size(); i++) {

				locationList.add(treeNodeList.get(i).getLocation());
				locationList.addAll(getAllChildren(treeNodeList.get(i)));
			}

		}
		return locationList;

	}

	private LocationTreeNode getLocationTreeNodeFormSession(HttpServletRequest request) {
		HttpSession session = request.getSession();
		LocationTreeNode locationTree = (LocationTreeNode) session.getAttribute("locationTree");

		return locationTree;
	}

	private Role getRoleFromSession(HttpServletRequest request) {

		HttpSession session = request.getSession();
		Role role = (Role) session.getAttribute("currRole");

		return role;

	}

	private Timestamp getCurrentTime() {
		return new Timestamp(new Date().getTime());
	}

	private Map<String, String> getTimeUnit(Locale locale) {

		Map<String, String> timeUnit = new LinkedHashMap<String, String>();
		for (int i = 0; i < ModelMappingValue.ptDurUnit.length; i++) {
			String name = messageSource.getMessage("patrol." + ModelMappingValue.ptDurUnit[i], null, locale);
			timeUnit.put(ModelMappingValue.ptDurUnitInDb[i], name);
		}

		return timeUnit;
	}

	// private void resetData() {
	//
	// }

	private List<Patrol_SearchTableRoute> getPageTableData(int page,
			List<Patrol_SearchTableRoute> search_patrolSearchTableList) {

		List<Patrol_SearchTableRoute> tableRouteList = new ArrayList<Patrol_SearchTableRoute>();

		for (int i = rowOnEachPage * (page - 1); i < rowOnEachPage * page; i++) {
			if (i < search_patrolSearchTableList.size())
				tableRouteList.add(search_patrolSearchTableList.get(i));
		}

		return tableRouteList;

	}

	private PatrolCreateForm getEditAndViewForm(int routeDefKey) {

		PatrolCreateForm form = new PatrolCreateForm();

		try {
			Patrol_Route route = routeDefManager.searhRoute(routeDefKey);

			// if (selectedLocationList == null)
			// selectedLocationList = new ArrayList<Patrol_SelectedLocation>();
			// else
			// selectedLocationList.clear();

			List<LocationJSON> locationJson = new ArrayList<LocationJSON>();

			if (route != null) {
				RouteDef routeDef = route.getRouteDef();
				// edt_RouteDef = routeDef;
				form.setRouteDefKey(routeDef.getRouteDefKey());
				form.setRouteCode(routeDef.getCode());
				form.setRouteName(routeDef.getName());
				form.setRouteMinTime(routeDef.getDefMinPtDur());
				form.setRouteMaxTime(routeDef.getDefMaxPtDur());
				form.setRouteMinTimeUnit(routeDef.getDefMinPtDurUnit() + "");
				form.setRouteMaxTimeUnit(routeDef.getDefMaxPtDurUnit() + "");

				List<RouteLocation> routeLocationList = route.getRouteLocation();

				if (routeLocationList != null) {
					// edt_routeLocationList = routeLocationList;
					// Patrol_SelectedLocation selected = new
					// Patrol_SelectedLocation();
					for (int i = 0; i < routeLocationList.size(); i++) {
						LocationJSON selected = new LocationJSON();
						// selected = new Patrol_SelectedLocation();
						RouteLocation routeLocation = routeLocationList.get(i);
						if (routeLocation.getDeleted().equals("Y")) {
							System.out.println("Continued");
							continue;
						}
						Location location = locationManager.getLocationByKey(routeLocation.getLocationKey());
						selected.setLocationKey(routeLocation.getLocation().getKey());
						selected.setRouteLocationKey(routeLocation.getRouteLocationKey());
						// selected.setLocationKey(routeLocation.getLocationKey());
						// selected.setName(location.getName());
						selected.setMinTime(routeLocation.getMinPtDur());
						selected.setMaxTime(routeLocation.getMaxPtDur());
						selected.setSeq(routeLocation.getSeqNum());
						// selected.setTagId(routeLocation.getLocation()
						// .getTagId());
						// selected
						// selected.setRouteLocation(routeLocation);

						locationJson.add(selected);
					}
				}

				form.setSelectedLocation(locationJson);
			}

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// temp_selectedLocationList = new ArrayList<Patrol_SelectedLocation>();
		// temp_selectedLocationList.addAll(selectedLocationList);
		return form;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public PatrolCreateFormValidator getPatrolCreateFormValidator() {
		return patrolCreateFormValidator;
	}

	public void setPatrolCreateFormValidator(PatrolCreateFormValidator patrolCreateFormValidator) {
		this.patrolCreateFormValidator = patrolCreateFormValidator;
	}
	
	public PatrolScheduleManager getPatrolScheduleManager() {
		return patrolScheduleManager;
	}

	public void setPatrolScheduleManager(PatrolScheduleManager patrolScheduleManager) {
		this.patrolScheduleManager = patrolScheduleManager;
	}

	public String locationJsonToString(List<LocationJSON> json) {

		for (int i = 0; i < json.size(); i++) {
			for (int j = 0; j < json.size(); j++) {
				if (json.get(i).getSeq() < json.get(j).getSeq()) {

					LocationJSON temp = json.get(i);
					json.set(i, json.get(j));
					json.set(j, temp);

				}
			}
		}

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

	public List<LocationJSON> stringtoLocationJson(String jsonString) {

		ObjectMapper mapper = new ObjectMapper();
		List<LocationJSON> locationJson = new ArrayList<LocationJSON>();

		System.out.println("LocationJSON Str : " + jsonString);

		if (jsonString != null && !jsonString.equals(""))
			try {
				locationJson = mapper.readValue(jsonString,
						mapper.getTypeFactory().constructCollectionType(List.class, LocationJSON.class));
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

		return locationJson;
	}

	public String SearchResultJsonToString(List<Patrol_SearchTableRoute> json) {

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

	public class PCodeComparator implements Comparator<Patrol_SearchTableRoute> {
		@Override
		public int compare(Patrol_SearchTableRoute st, Patrol_SearchTableRoute nd) {

			String str1 = st.getCode();
			String str2 = nd.getCode();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PNameComparator implements Comparator<Patrol_SearchTableRoute> {
		@Override
		public int compare(Patrol_SearchTableRoute st, Patrol_SearchTableRoute nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class PLocationComparator implements Comparator<Patrol_SearchTableRoute> {
		@Override
		public int compare(Patrol_SearchTableRoute st, Patrol_SearchTableRoute nd) {

			String str1 = st.getStartingLocation();
			String str2 = nd.getStartingLocation();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

}
