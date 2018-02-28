package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.AccountExport;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.Privilege;
import hk.ebsl.mfms.dto.PrivilegeCategory;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RoleExport;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.StatusAccessModeManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.utility.Utility;
import hk.ebsl.mfms.utility.writer.ColumnDisplay;
import hk.ebsl.mfms.utility.writer.ExcelWriter;
import hk.ebsl.mfms.web.form.AssignRoleForm;
import hk.ebsl.mfms.web.form.RoleForm;
import hk.ebsl.mfms.web.form.SearchRoleForm;
import hk.ebsl.mfms.web.form.SearchUserAccountForm;
import hk.ebsl.mfms.web.form.SiteForm;
import hk.ebsl.mfms.web.form.UserAccountForm;
import hk.ebsl.mfms.web.form.validator.RoleFormValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Principal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

// keep searchRoleForm in session so that results will be kept
@SessionAttributes({ "searchRoleForm" })
@Controller
public class RoleManagementController {

	public final static Logger logger = Logger.getLogger(RoleManagementController.class);

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private RoleFormValidator roleFormValidator;

	@Autowired
	private Properties privilegeMap;

	@Autowired
	private UserAccountManager userManager;

	@Autowired
	private StatusAccessModeManager statusAccessModeManager;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Properties propertyConfigurer;

	@Value("${site.admin.role.name}")
	private String defaultSiteAdminRoleName;

	@RequestMapping(value = "/CreateRole.do")
	public String showCreateRoleForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load create role page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.createAccountRole"));

			if (hasPrivilege) {

				// empty form
				RoleForm roleForm = new RoleForm();

				if (currRole != null) {
					Integer siteKey = currRole.getSiteKey();
					roleForm.setSiteKey(siteKey);
				}

				try {
					// get all available privileges
					List<PrivilegeCategory> pcList = genPrivilegeCategoryList(isSysAdmin);
					roleForm.setPrivilegeCategoryList(pcList);

					// empty granted privileges
					List<String> grantedPrivilegeList = new ArrayList<String>();
					roleForm.setGrantedPrivilegeList(grantedPrivilegeList);
				} catch (MFMSException e) {
					// unable to find privileges
					logger.error("Unable to find privileges");
					return "common/notfound";
				}

				try {
					roleForm.setModeList(statusAccessModeManager.getAllStatusAccessMode());
				} catch (MFMSException e1) {

					e1.printStackTrace();
					// set empty
					roleForm.setModeList(null);
				}

				// view set readonly
				roleForm.setReadOnly(false);
				roleForm.setDelete(false);

				model.addAttribute("roleForm", roleForm);

				return "account/role_create_modify";
			} else {
				logger.debug("user does not have right to create role ");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/ViewRole.do")
	public String showViewRoleForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load view role page.");
		
		String roleKeyStr = request.getParameter("key");
		String referrerStr = request.getParameter("r");

		logger.debug("showViewRoleForm()[" + roleKeyStr + "]");

		Integer roleKey = Integer.parseInt(roleKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyAccountRole"))
					|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createAccountRole"))
					|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchAccountRole"));

			if (hasPrivilege) {

				Role viewRole = null;

				try {
					viewRole = roleManager.getRoleByKey(roleKey, true);

				} catch (MFMSException e) {
					// TODO Redirect to not found
					return "common/notfound";
				}

				if (viewRole == null || currRole.getSiteKey() != viewRole.getSiteKey())
					return "common/notfound";

				RoleForm roleForm = new RoleForm();

				if (currRole.getName().equals(defaultSiteAdminRoleName)) {
					roleForm.setIsSiteAdmin("Y");
				} else {
					roleForm.setIsSiteAdmin("N");
				}
				roleForm.setKey(viewRole.getKey());
				roleForm.setSiteKey(viewRole.getSiteKey());
				roleForm.setName(viewRole.getName());
				roleForm.setDesc(viewRole.getDescription());

				roleForm.setModeKey(viewRole.getModeKey());
				try {
					roleForm.setModeList(statusAccessModeManager.getAllStatusAccessMode());
				} catch (MFMSException e1) {

					e1.printStackTrace();
					// set empty
					roleForm.setModeList(null);
				}

				roleForm.setReferrer(referrerStr);

				try {
					// get all available privileges
					List<PrivilegeCategory> pcList = genPrivilegeCategoryList(isSysAdmin);
					roleForm.setPrivilegeCategoryList(pcList);

					// get all granted privileges
					List<String> grantedPrivilegeList = genGrantedPrivilegeCodeList(viewRole.getRolePrivileges());
					roleForm.setGrantedPrivilegeList(grantedPrivilegeList);
				} catch (MFMSException e) {
					// unable to find privileges
					logger.error("Unable to find privileges");
					return "common/notfound";
				}

				// view set readonly
				roleForm.setReadOnly(true);
				roleForm.setDelete(false);

				model.addAttribute("roleForm", roleForm);

				return "account/role_create_modify";

			} else {
				logger.debug("user does not have right to view role information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/ModifyRole.do")
	public String showModifyRoleForm(@ModelAttribute("roleForm") RoleForm roleForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load modify role page.");

		logger.debug("RoleForm.Key = " + roleForm.getKey());

		logger.debug("Request.Key = " + request.getParameter("key"));

		String referrerStr = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyAccountRole"));

			if (hasPrivilege) {
				Role editRole = null;

				try {
					editRole = roleManager.getRoleByKey(roleForm.getKey(), true);

				} catch (MFMSException e) {
					// TODO Redirect to not found
					logger.error(e.getMessage());
					return "common/notfound";
				}

				if (editRole == null || currRole.getSiteKey() != editRole.getSiteKey())
					return "common/notfound";

				roleForm.setKey(editRole.getKey());
				roleForm.setSiteKey(editRole.getSiteKey());
				roleForm.setName(editRole.getName());
				roleForm.setDesc(editRole.getDescription());

				roleForm.setModeKey(editRole.getModeKey());
				try {
					roleForm.setModeList(statusAccessModeManager.getAllStatusAccessMode());
				} catch (MFMSException e1) {

					e1.printStackTrace();
					// set empty
					roleForm.setModeList(null);
				}

				roleForm.setReferrer(referrerStr);

				try {
					// get all available privileges
					List<PrivilegeCategory> pcList = genPrivilegeCategoryList(isSysAdmin);
					roleForm.setPrivilegeCategoryList(pcList);

					// get all granted privileges
					List<String> grantedPrivilegeList = genGrantedPrivilegeCodeList(editRole.getRolePrivileges());
					roleForm.setGrantedPrivilegeList(grantedPrivilegeList);

				} catch (MFMSException e) {
					// unable to find privileges
					logger.error("Unable to find privileges");
					return "common/notfound";
				}

				if (editRole.getName().equals(defaultSiteAdminRoleName)) {
					if (currRole.getName().equals(defaultSiteAdminRoleName)) {
						roleForm.setReadOnly(false);
						roleForm.setDelete(false);
					} else {
						roleForm.setReadOnly(true);
						roleForm.setDelete(false);
					}
				} else {
					roleForm.setReadOnly(false);
					roleForm.setDelete(false);
				}

				return "account/role_create_modify";
			} else {
				logger.debug("user does not have right to modify role");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/DoCreateModifyRole.do")
	public String doCreateModifyRole(@ModelAttribute("roleForm") RoleForm roleForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("doCreateModifyRole()");

		logger.debug("RoleForm.Key = " + roleForm.getKey());
		logger.debug("RoleForm.Name = " + roleForm.getName());
		logger.debug("RoleForm.Desc = " + roleForm.getDesc());
		logger.debug("RoleForm.SiteKey = " + roleForm.getSiteKey());

		// save role
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		String referrerStr = "";

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyAccountRole"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.createAccountRole"));

			if (hasPrivilege) {
				Role role = null;

				// validate
				roleFormValidator.validate(roleForm, result);

				if (result.hasErrors()) {
					logger.debug("Validation Failed");

					try {
						// get all available privileges
						List<PrivilegeCategory> pcList = genPrivilegeCategoryList(isSysAdmin);
						roleForm.setPrivilegeCategoryList(pcList);
					} catch (MFMSException e) {
						// unable to find privileges
						logger.error("Unable to find privileges");
						return "common/notfound";
					}

					try {
						roleForm.setModeList(statusAccessModeManager.getAllStatusAccessMode());
					} catch (MFMSException e1) {

						e1.printStackTrace();
						// set empty
						roleForm.setModeList(null);
					}

					return "account/role_create_modify";
				}

				if (roleForm.getKey() == null) {

					if (currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.createAccountRole"))) {
						// create
						referrerStr = "c";
						role = new Role();

						role.setName(roleForm.getName());
						role.setDescription(roleForm.getDesc());

						// role.setSiteKey(roleForm.getSiteKey());
						role.setSiteKey(currRole.getSiteKey());

						role.setCreateBy(account.getKey());
						role.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
						role.setDeleted("N");

						// create privileges
						List<RolePrivilege> rpList = new ArrayList<RolePrivilege>();

						if (roleForm.getGrantedPrivilegeList() != null) {
							for (String pc : roleForm.getGrantedPrivilegeList()) {
								RolePrivilege rp = new RolePrivilege();
								rp.setPrivilegeCode(pc);
								rp.setCreateBy(account.getKey());
								rp.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								rp.setDeleted("N");
								rpList.add(rp);
							}
						}
						role.setRolePrivileges(rpList);
						role.setModeKey(roleForm.getModeKey());
					} else {
						logger.debug("user does not have right to create role");
						return "common/noprivilege";
					}
				} else {
					// modify
					if (currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.modifyAccountRole"))) {
						try {
							referrerStr = "m";

							role = roleManager.getRoleByKey(roleForm.getKey(), true);

							if (role == null || currRole.getSiteKey() != role.getSiteKey()) {
								logger.debug("Role Key " + roleForm.getKey() + " not found");
								return "common/notfound";
							}
							role.setName(roleForm.getName());
							role.setDescription(roleForm.getDesc());

							role.setLastModifyBy(account.getKey());
							role.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

							// for holding existing privileges
							HashMap<String, RolePrivilege> rpMap = new HashMap<String, RolePrivilege>();
							if (role.getRolePrivileges() != null) {
								for (RolePrivilege rp : role.getRolePrivileges()) {
									// for existing privilege, set to deleted by
									// default
									rp.setDeleted("Y");
									rp.setLastModifyBy(account.getKey());
									rp.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
									rpMap.put(rp.getPrivilegeCode(), rp);
								}
							}
							if (roleForm.getGrantedPrivilegeList() != null) {
								for (String pc : roleForm.getGrantedPrivilegeList()) {
									if (rpMap.containsKey(pc)) {
										// map already contains this privilege
										RolePrivilege rp = rpMap.get(pc);
										rp.setDeleted("N");
									} else {
										// add privilege
										RolePrivilege rp = new RolePrivilege();
										rp.setRoleKey(role.getKey());
										rp.setPrivilegeCode(pc);
										rp.setCreateBy(account.getKey());
										rp.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
										rp.setDeleted("N");

										// add to existing list
										role.getRolePrivileges().add(rp);
									}
								}
							}
							role.setModeKey(roleForm.getModeKey());
							// debug print submitted privileges
							/*
							 * if (roleForm.getGrantedPrivilegeList() != null) {
							 * 
							 * logger.debug("Granted Codes submitted:"); for
							 * (String pc : roleForm.getGrantedPrivilegeList())
							 * { logger.debug(pc); } }
							 */
						} catch (MFMSException e) {
							logger.error(e.getMessage());
							return "common/notfound";
						}
					} else {
						logger.debug("user does not have right to modify role");
						return "common/noprivilege";
					}
				}
				try {

					roleManager.saveRole(role);

				} catch (MFMSException e) {
					throw e;
				}

				// return to view page on success
				logger.debug("Role Key after: " + role.getKey());
				return "redirect:ViewRole.do?key=" + role.getKey() + "&r=" + referrerStr;

			} else {

				// user does not have right to modify role information
				logger.debug("user does not have right to modify role");
				return "common/noprivilege";
			}
		} catch (Exception | MFMSException e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@ModelAttribute("searchRoleForm")
	public SearchRoleForm populateSearchRoleForm() {
		return new SearchRoleForm(); // populates search form for the first time
										// if its null
	}

	@RequestMapping(value = "/SearchRole.do")
	public String showSearchRoleForm(@ModelAttribute("searchRoleForm") SearchRoleForm searchRoleForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load search role page.");

		searchRoleForm = new SearchRoleForm();

		HttpSession session = request.getSession();

		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchRoleForm.setSiteKey(siteKey);
		}

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.searchAccountRole"));

			if (hasPrivilege) {
				model.addAttribute("searchRoleForm", searchRoleForm);

				return "account/role_search";
			} else {
				logger.debug("user does not have right to search role information");
				return "common/noprivilege";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DoSearchRole.do")
	public String doSearchRoleForm(@ModelAttribute("searchRoleForm") SearchRoleForm searchRoleForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("doSearchRole()");

		logger.debug("SearchRoleForm.Name: " + searchRoleForm.getName());
		logger.debug("SearchRoleForm.Desc: " + searchRoleForm.getDescription());
		logger.debug("SearchRoleForm.SiteKey: " + searchRoleForm.getSiteKey());

		HttpSession session = request.getSession();
		
		// get user's current site
		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		if (currRole != null) {
			Integer siteKey = currRole.getSiteKey();
			searchRoleForm.setSiteKey(siteKey);
		}
		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.searchAccountRole"));

			if (hasPrivilege) {
				try {
					List<Role> roleList = roleManager.searchRole(searchRoleForm.getSiteKey(), searchRoleForm.getName(),
							searchRoleForm.getDescription(), "N", false);

					logger.debug("Search Role result size: " + roleList.size());
					searchRoleForm.setResultList(roleList);

					searchRoleForm.setCanGen(true);

					Boolean canModify = currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.modifyAccountRole"));

					Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.removeAccountRole"));

					Boolean canAssign = currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.assignAccount"));

					searchRoleForm.setCanModify(canModify);

					searchRoleForm.setCanRemove(canRemove);

					searchRoleForm.setCanAssign(canAssign);
					
					searchRoleForm.setFullListSize(searchRoleForm.getResultList().size());
				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return "account/role_search";
			} else {
				logger.debug("user does not have right to search role information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DeleteRole.do")
	public String showDeleteRoleForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String roleKeyStr = request.getParameter("key");

		logger.debug("showDeleteRoleForm()[" + roleKeyStr + "]");

		Integer roleKey;
		try {
			roleKey = Integer.parseInt(roleKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeAccountRole"));

			if (hasPrivilege) {

				Role viewRole = null;

				try {
					viewRole = roleManager.getRoleByKey(roleKey, true);

				} catch (MFMSException e) {
					// TODO Redirect to not found
					return "common/notfound";
				}

				if (viewRole == null || currRole.getSiteKey() != viewRole.getSiteKey())
					return "common/notfound";

				RoleForm roleForm = new RoleForm();

				roleForm.setKey(viewRole.getKey());
				roleForm.setSiteKey(viewRole.getSiteKey());
				roleForm.setName(viewRole.getName());
				roleForm.setDesc(viewRole.getDescription());

				try {
					// get all available privileges
					List<PrivilegeCategory> pcList = genPrivilegeCategoryList(isSysAdmin);
					roleForm.setPrivilegeCategoryList(pcList);

					// get all granted privileges
					List<String> grantedPrivilegeList = genGrantedPrivilegeCodeList(viewRole.getRolePrivileges());
					roleForm.setGrantedPrivilegeList(grantedPrivilegeList);
				} catch (MFMSException e) {
					// unable to find privileges
					logger.error("Unable to find privileges");
					return "common/notfound";
				}

				// view set readonly
				roleForm.setReadOnly(true);

				// set delete
				roleForm.setDelete(true);

				model.addAttribute("roleForm", roleForm);

				return "account/role_create_modify";
			} else {

				// user does not have right to view role information
				logger.debug("user does not have right to remove role information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "DoRoleDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchRoleForm") SearchRoleForm searchRoleForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "name", "desc" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Role> fullList = new ArrayList<Role>();

		List<Role> roleList = new ArrayList<Role>();

		if (searchRoleForm.getCanGen()) {

			fullList = searchRoleForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new RNameComparator());

				break;

			case "name":

				Collections.sort(fullList, new RNameComparator());

				break;

			case "desc":
				Collections.sort(fullList, new RDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					roleList.add(fullList.get(i));
			}

			fullListSize = searchRoleForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < roleList.size(); i++) {

			JSONArray ja = new JSONArray();
			Role role = roleList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchRoleForm.getCanModify();
			boolean canRemove = searchRoleForm.getCanRemove();
			boolean canAssign = searchRoleForm.getCanAssign();

			
			if(!currRole.getName().equals("Site Administrator Role") && role.getName().equals("Site Administrator Role")){
			
			}else{
			myvar.append("<a onclick=\"showLoading()\"")
				.append(" href=\"ViewRole.do?key=" + role.getKey() + "&r=s\"><i")
				.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");
			}
			
			if (canModify) {
				if (!role.getName().equals(defaultSiteAdminRoleName)) {
					myvar.append("<a onclick=\"showLoading()\"")
							.append(" href=\"ModifyRole.do?key=" + role.getKey() + "&r=s\"><i")
							.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");
				} else {
					if (currRole.getName().equals(defaultSiteAdminRoleName)) {
						myvar.append("<a onclick=\"showLoading()\"")
								.append(" href=\"ModifyRole.do?key=" + role.getKey() + "&r=s\"><i")
								.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");
					}
				}
			}

			if (canAssign) {
				if (!role.getName().equals(defaultSiteAdminRoleName)) {
					myvar.append("<a onclick=\"showLoading()\"")
							.append(" href=\"AssignRole.do?key=" + role.getKey() + "&r=s\"><i")
							.append(" class=\"fa fa-user\"></i></a>&nbsp;&nbsp;");
				} else {
					if (currRole.getName().equals(defaultSiteAdminRoleName)) {
						myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"AssignRole.do?key=" + role.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-user\"></i></a>&nbsp;&nbsp;");
					}
				}
			}

			if (canRemove) {
				if (!role.getName().equals(defaultSiteAdminRoleName)) {
					myvar.append("<i style=\"cursor:pointer\" onclick=\"showDelete(" + role.getKey() + ", '"
							+ Utility.removeQuote(Utility.replaceHtmlEntities(role.getName()))
							+ "');\" class=\"fa fa-trash-o\"></i>");
				}
			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(role.getName()));
			ja.put(Utility.replaceHtmlEntities(role.getDescription()));
			array.put(ja);

		}
		
		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/ExportRole.do")
	public @ResponseBody void ExportRole(@ModelAttribute("searchRoleForm") SearchRoleForm searchRoleForm,
			HttpServletRequest request, HttpServletResponse response, Locale locale) throws Exception{
		logger.debug("ExportRole.do");
		
		List<Role> roleList = new ArrayList<Role>(searchRoleForm.getResultList());	
		
		Calendar cal = Calendar.getInstance();
		String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
		String fileName = filePath + "Role_" + cal.getTimeInMillis()
				+ ".xlsx";
		
		File roleListFile = new File(fileName);
		
		FileOutputStream roleListStream = new FileOutputStream(roleListFile,
				false);
		
		ExcelWriter roleListWriter = new ExcelWriter(roleListStream);
		
		//header
		List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();
		ColumnDisplay roleNameCol = new ColumnDisplay();
		roleNameCol.setProperty("name");
		roleNameCol.setHeadingKey(messageSource.getMessage("role.name", null, locale));
		
		ColumnDisplay roleDescCol = new ColumnDisplay();
		roleDescCol.setProperty("desc");
		roleDescCol.setHeadingKey(messageSource.getMessage("role.description", null, locale));
		
		ColumnDisplay rolePrivilegeCategoryCol = new ColumnDisplay();
		rolePrivilegeCategoryCol.setProperty("privilegeCategory");
		rolePrivilegeCategoryCol.setHeadingKey(messageSource.getMessage("privilege.category", null, locale));
		
		ColumnDisplay rolePrivilegeCol = new ColumnDisplay();
		rolePrivilegeCol.setProperty("privilege");
		rolePrivilegeCol.setHeadingKey(messageSource.getMessage("privilege", null, locale));
		
		columnDisplays.add(roleNameCol);
		columnDisplays.add(roleDescCol);
		columnDisplays.add(rolePrivilegeCategoryCol);
		columnDisplays.add(rolePrivilegeCol);

		//Row content
		List<RoleExport> exportList = new ArrayList<RoleExport>();
		RoleExport export = null;
		for(Role role : roleList){	
			List<RolePrivilege> rpList = roleManager.searchRoleByPrivilege(role.getKey());
			for(RolePrivilege rp : rpList){
				export = new RoleExport();
				export.setName(role.getName());
				export.setDesc(role.getDescription());			
				export.setPrivilegeCategory(rp.getPrivilege().getPrivilegeCategoryCode());
				export.setPrivilege(rp.getPrivilegeCode());
				
				exportList.add(export);
			}
		}
		Comparator<RoleExport> comparatorName = new nameComparator();
        
        Comparator<RoleExport> comparatorPrivilegeCategory = new pRComparator();
        
        Comparator<RoleExport> comparatorPrivilege = new pComparator();
        
        ComparatorChain chain = new ComparatorChain(); 
        chain.addComparator(comparatorName);  
        chain.addComparator(comparatorPrivilegeCategory);  
        chain.addComparator(comparatorPrivilege);  
		
		Collections.sort(exportList, chain);
		
		roleListWriter.write(true, 0, "Role", null, exportList, columnDisplays, false);

		roleListWriter.close();
		
		response.setHeader("Content-Type", "application/vnd.ms-excel");
		response.setHeader("Content-disposition", "inline;filename=\""
				+ roleListFile.getName() + "\"");
		
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
	
	@RequestMapping(value = "/DoDeleteRole.do")
	public String doDeleteRole(@ModelAttribute("searchRoleForm") SearchRoleForm searchRoleForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String roleKeyStr = request.getParameter("key");

		logger.debug("doDeleteRole()[" + roleKeyStr + "]");

		Integer roleKey;

		try {
			roleKey = Integer.parseInt(roleKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		UserAccount account = (UserAccount) session.getAttribute("user");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeAccountRole"));

			// check privilege
			if (hasPrivilege) {

				try {

					// validate : check role has any users
					Role targetRole = roleManager.getRoleByKey(roleKey, false);

					if (targetRole != null && targetRole.getSiteKey().equals(currRole.getSiteKey())) {
						// get all current users with this role
						List<UserAccountRole> accountRoleList = userManager
								.searchUserAccountRole(targetRole.getSiteKey(), roleKey, null, null, null);

						if (accountRoleList != null && accountRoleList.size() > 0) {
							// error
							logger.debug("Role " + targetRole.getName() + " still has Users");
							result.reject("role.delete.fail.has.user", new Object[] { targetRole.getName() }, null);

							searchRoleForm.setDeleteSuccess(false);

							return "account/role_search";

						} else {
							logger.debug("Going to delete role " + targetRole.getName());
							roleManager.deleteRoleByKey(account.getKey(), roleKey);
							searchRoleForm.setDeleteSuccess(true);
							searchRoleForm.setDeletedName(targetRole.getName());
						}

					} else {
						// role not found
						logger.debug("Role Key" + roleKey + " not found");
						result.reject("role.delete.fail.not.found", null);
						searchRoleForm.setDeleteSuccess(false);

						return "account/role_search";
					}

				} catch (MFMSException e) {
					// TODO Redirect to not found
					return "common/notfound";
				}
				model.addAttribute("roleForm", new RoleForm());

				return "redirect:DoSearchRole.do";
			} else {

				// user does not have right to delete role information
				logger.debug("user does not have right to delete role");
				return "common/notfound";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/AssignRole.do")
	public String showAssignRoleForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load assign role page.");
		
		String roleKeyStr = request.getParameter("key");
		String errorStr = request.getParameter("error");
		String referrerStr = request.getParameter("r");

		logger.debug("showAssignRoleForm()[" + roleKeyStr + "]");

		Integer roleKey;
		try {
			roleKey = Integer.parseInt(roleKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.assignAccount"));

			if (hasPrivilege) {

				Role viewRole = null;

				try {
					// refresh
					viewRole = roleManager.getRoleByKey(roleKey, false);

				} catch (MFMSException e) {
					// TODO Redirect to not found
					return "common/notfound";
				}

				if (viewRole == null || currRole.getSiteKey() != viewRole.getSiteKey())
					return "common/notfound";

				AssignRoleForm assignRoleForm = new AssignRoleForm();

				assignRoleForm.setReferrer(referrerStr);

				assignRoleForm.setKey(viewRole.getKey());
				assignRoleForm.setName(viewRole.getName());
				assignRoleForm.setDesc(viewRole.getDescription());

				if ("0".equals(errorStr)) {
					assignRoleForm.setSuccess(true);
				}
				try {
					// get all current users with this role
					List<UserAccountRole> accountRoleList = userManager.searchUserAccountRole(viewRole.getSiteKey(),
							roleKey, null, null, null);
					List<String> assignedAccountList = new ArrayList<String>();
					if (accountRoleList != null && accountRoleList.size() > 0) {
						logger.debug("AccountRoleList size = " + accountRoleList.size());

						for (UserAccountRole ar : accountRoleList) {
							assignedAccountList.add(ar.getUserAccount().getLoginId());
							logger.debug(ar.getUserAccount().getLoginId());
						}

					} else {
						logger.debug("No Accounts found.");
					}
					assignRoleForm.setAssignedAccountList(assignedAccountList);

					// get all users that are not assigned to this site
					List<UserAccount> unassignedAccountList = userManager
							.searchUnassignedUserAccount(viewRole.getSiteKey(), null, null, null);
					List<String> unassignedAccountIdList = new ArrayList<String>();
					if (unassignedAccountList != null && unassignedAccountList.size() > 0) {

						for (UserAccount a : unassignedAccountList) {
							unassignedAccountIdList.add(a.getLoginId());
						}

					}
					assignRoleForm.setUnassignedAccountList(unassignedAccountIdList);

				} catch (MFMSException e) {
					// unable to find privileges
					logger.error("Unable to find privileges");
					return "common/notfound";
				}

				model.addAttribute("assignRoleForm", assignRoleForm);

				return "account/role_assign";
			} else {

				// user does not have right to assign Account information
				logger.debug("user does not have right to assign Account information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DoAssignRole.do")
	public String doAssignRole(@ModelAttribute("assignRoleForm") AssignRoleForm assignRoleForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("doAssignRole()[" + assignRoleForm.getKey() + "]");

		Integer roleKey = assignRoleForm.getKey();

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyAccountRole"));

			// check privilege
			if (hasPrivilege) {

				List<String> selectedAccountList = new ArrayList<String>();
				if (assignRoleForm.getAssignedAccountList() != null) {
					logger.debug("New Assigned Account: ");
					for (String id : assignRoleForm.getAssignedAccountList()) {
						logger.debug(id);
					}
					selectedAccountList = assignRoleForm.getAssignedAccountList();
				} else {
					logger.debug("No account assigned");
				}

				try {

					List<UserAccountRole> currentARList = userManager.searchUserAccountRole(null, roleKey, null, null,
							null);
					if (currentARList != null) {
						for (UserAccountRole ar : currentARList) {
							if (selectedAccountList.contains(ar.getUserAccount().getLoginId())) {
								// nothing to do
							} else {
								// remove existing
								ar.setDeleted("Y");
								ar.setLastModifyBy(currUser.getKey());
								ar.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
								userManager.saveUserAccountRole(ar);
							}
							// remove processed
							selectedAccountList.remove(ar.getUserAccount().getLoginId());
						}
					}

					// process remaining (add)
					for (String id : selectedAccountList) {

						// TODO: check for having role in same site?
						UserAccountRole newAr = new UserAccountRole();
						UserAccount account = userManager.getUserAccountByLoginId(id, false);

						logger.debug("************* " + id);

						newAr.setAccountKey(account.getKey());
						newAr.setRoleKey(roleKey);
						newAr.setCreateBy(currUser.getKey());
						newAr.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
						newAr.setDeleted("N");

						userManager.saveUserAccountRole(newAr);
					}

				} catch (MFMSException e) {
					// TODO Redirect to not found
					return "common/notfound";
				}

				return "redirect:AssignRole.do?key=" + roleKey + "&error=0&r=" + assignRoleForm.getReferrer();
			} else {

				// user does not have right to assign account role information
				logger.debug("user does not have right to assign account");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/Role.do")
	public String showRoleMenu(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("showRoleMenu()");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}
		try {

			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.createAccountRole"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.searchAccountRole"));

			if (hasPrivilege)
				return "account/role_menu";
			else {
				// user does not have right to access role information
				logger.debug("user does not have right to access role information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/noprivilege";
		}
	}

	private List<PrivilegeCategory> genPrivilegeCategoryList(boolean isSysAdmin) throws MFMSException {

		List<PrivilegeCategory> pcList = roleManager.searchPrivilegeCategory(null, null, true, "sequence", true, true);

		if (!isSysAdmin) {
			// remove site category
			Iterator<PrivilegeCategory> it = pcList.iterator();

			while (it.hasNext()) {
				PrivilegeCategory pc = it.next();
				if ("Site".equals(pc.getCode())) {
					it.remove();
				}
			}
		}
		// debug printing
		/*
		 * for (PrivilegeCategory pc : pcList) { logger.debug(pc.getSequence() +
		 * "," + pc.getCode()); for (Privilege p : pc.getPrivilegeList()) {
		 * logger.debug("|-- " + p.getSequence() + "," + p.getCode()); } }
		 */
		return pcList;
	}

	private List<String> genGrantedPrivilegeCodeList(List<RolePrivilege> rpList) throws MFMSException {

		if (rpList != null) {

			List<String> pcList = new ArrayList<String>();

			for (RolePrivilege rp : rpList) {
				pcList.add(rp.getPrivilegeCode());
			}
			/*
			 * String[] pcList = new String[rpList.size()];
			 * 
			 * for (int i = 0; i < rpList.size(); i++) { pcList[i] =
			 * rpList.get(i).getPrivilegeCode();
			 * 
			 * }
			 */
			return pcList;
		} else {
			return null;
		}
	}

	public class RNameComparator implements Comparator<Role> {
		@Override
		public int compare(Role st, Role nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class RDescComparator implements Comparator<Role> {
		@Override
		public int compare(Role st, Role nd) {

			String str1 = st.getDescription();
			String str2 = nd.getDescription();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}
	
	class nameComparator implements Comparator<RoleExport> {
	    public int compare(RoleExport s1, RoleExport s2) {
	        int n1 = s1.getName().length();
	        int n2 = s2.getName().length();
	        int min = Math.min(n1, n2);
	        for (int i = 0; i < min; i++) {
	            char c1 = s1.getName().charAt(i);
	            char c2 = s2.getName().charAt(i);
	            if (c1 != c2) {
	                if (isAtoZ(c1) && isAtoZ(c2)) {
	                    return getSortPosition(c1) - getSortPosition(c2);
	                }
	                return c1 - c2;
	            }
	        }
	        return n1 - n2;
	    }
	}
	
	class pRComparator implements Comparator<RoleExport> {
	    public int compare(RoleExport s1, RoleExport s2) {
	        int n1 = s1.getPrivilegeCategory().length();
	        int n2 = s2.getPrivilegeCategory().length();
	        int min = Math.min(n1, n2);
	        for (int i = 0; i < min; i++) {
	            char c1 = s1.getPrivilegeCategory().charAt(i);
	            char c2 = s2.getPrivilegeCategory().charAt(i);
	            if (c1 != c2) {
	                if (isAtoZ(c1) && isAtoZ(c2)) {
	                    return getSortPosition(c1) - getSortPosition(c2);
	                }
	                return c1 - c2;
	            }
	        }
	        return n1 - n2;
	    }
	}
	
	class pComparator implements Comparator<RoleExport> {
	    public int compare(RoleExport s1, RoleExport s2) {
	        int n1 = s1.getPrivilege().length();
	        int n2 = s2.getPrivilege().length();
	        int min = Math.min(n1, n2);
	        for (int i = 0; i < min; i++) {
	            char c1 = s1.getPrivilege().charAt(i);
	            char c2 = s2.getPrivilege().charAt(i);
	            if (c1 != c2) {
	                if (isAtoZ(c1) && isAtoZ(c2)) {
	                    return getSortPosition(c1) - getSortPosition(c2);
	                }
	                return c1 - c2;
	            }
	        }
	        return n1 - n2;
	    }
	}
	
    private boolean isAtoZ(char c) {
	        return c > 64 && c < 123;
	    }

	    private int getSortPosition(char c) {
	        if (c < 91) {    	       
	            return 2 * (c - 64); 
	        } else if (c > 96) {  	         
	        	int kk = (2 * (c - 96)) - 1;     	        	
	            return kk;
	        } else {    	           
	            return c; 
	        }
	    }



	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public Properties getPrivilegeMap() {
		return privilegeMap;
	}

	public void setPrivilegeMap(Properties privilegeMap) {
		this.privilegeMap = privilegeMap;
	}

	public RoleFormValidator getRoleFormValidator() {
		return roleFormValidator;
	}

	public void setRoleFormValidator(RoleFormValidator roleFormValidator) {
		this.roleFormValidator = roleFormValidator;
	}

	public UserAccountManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserAccountManager userManager) {
		this.userManager = userManager;
	}

	public StatusAccessModeManager getStatusAccessModeManager() {
		return statusAccessModeManager;
	}

	public void setStatusAccessModeManager(StatusAccessModeManager statusAccessModeManager) {
		this.statusAccessModeManager = statusAccessModeManager;
	}
}
