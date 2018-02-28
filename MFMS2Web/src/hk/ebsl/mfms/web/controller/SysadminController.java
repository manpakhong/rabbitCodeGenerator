package hk.ebsl.mfms.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hk.ebsl.mfms.dto.Privilege;
import hk.ebsl.mfms.dto.PrivilegeCategory;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.utility.Utility;
import hk.ebsl.mfms.web.form.RoleForm;
import hk.ebsl.mfms.web.form.SearchRoleForm;
import hk.ebsl.mfms.web.form.SearchSiteForm;
import hk.ebsl.mfms.web.form.SiteForm;
import hk.ebsl.mfms.web.form.validator.SiteFormValidator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

//keep searchRoleForm in session so that results will be kept
@SessionAttributes({ "searchSiteForm" })
@Controller
public class SysadminController {

	public final static Logger logger = Logger.getLogger(SysadminController.class);

	@Autowired
	private SiteManager siteManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private SiteFormValidator siteFormValidator;

	@Autowired
	private UserAccountManager userAccountManager;

	@Value("${site.admin.role.name}")
	private String defaultSiteAdminRoleName;

	@Value("${site.admin.role.desc}")
	private String defaultSiteAdminRoleDesc;

	@Value("${site.admin.role.modeKey}")
	private String defaultModeKey;

	@RequestMapping(value = "/Sysadmin.do")
	public String showSysAdminHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// check privilege
		if (isSysAdmin) {

			logger.debug("User requested sysadmin page.");
			Role sysAdminRole = (Role) session.getAttribute("sysAdminRole");
			session.setAttribute("currRole", sysAdminRole);
			return "sysadmin/sysadmin_menu";
		} else {
			logger.debug("User requested sysadmin page but do not have privilege");
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/Site.do")
	public String showSiteMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// check privilege
		if (isSysAdmin) {

			logger.debug("User requested sysadmin page.");
			Role sysAdminRole = (Role) session.getAttribute("sysAdminRole");
			session.setAttribute("currRole", sysAdminRole);
			return "sysadmin/site_menu";
		} else {
			logger.debug("User requested sysadmin page but do not have privilege");
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/CreateSite.do")
	public String showCreateSiteForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("showCreateSiteForm()");

		// empty form
		SiteForm siteForm = new SiteForm();
		siteForm.setDelete(false);
		siteForm.setReadOnly(false);

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		logger.debug("Default Site Admin Role Name: " + defaultSiteAdminRoleName);
		logger.debug("Default Site Admin Mode Key: " + defaultModeKey);

		// check privilege
		if (isSysAdmin) {

			siteForm.setCreateDefaultAdmin(true);

			model.addAttribute("siteForm", siteForm);

			return "sysadmin/site_create_modify";

		} else {

			// user does not have right to create site information
			logger.debug("user does not have right to create site information");
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/ViewSite.do")
	public String showViewSiteForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String siteKeyStr = request.getParameter("key");
		String referrerStr = request.getParameter("r");

		logger.debug("showViewSiteForm()[" + siteKeyStr + "]");

		Integer siteKey;
		try {
			siteKey = Integer.parseInt(siteKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// check privilege
		if (isSysAdmin) {

			Site viewSite = null;

			List<UserAccount> siteAdminList = new ArrayList<UserAccount>();

			try {
				viewSite = siteManager.getSiteByKey(siteKey);

				Role siteAdminRole = roleManager.getRoleByName(siteKey, defaultSiteAdminRoleName, false);
				if (siteAdminRole != null) {
					List<UserAccountRole> arList = userAccountManager.searchUserAccountRole(siteKey,
							siteAdminRole.getKey(), null, null, null);

					if (arList != null) {
						for (UserAccountRole ar : arList) {
							siteAdminList.add(ar.getUserAccount());
						}
					}
				}

			} catch (MFMSException e) {
				// TODO Redirect to not found
				return "common/notfound";
			}

			// site not found
			if (viewSite == null)
				return "common/notfound";

			SiteForm siteForm = new SiteForm();

			siteForm.setKey(viewSite.getKey());
			siteForm.setName(viewSite.getName());
			siteForm.setAddress(viewSite.getAddress());
			if (viewSite.getContactAreaCode() != null) {
				siteForm.setContactAreaCode(viewSite.getContactAreaCode() + "");
			}
			if (viewSite.getContactCountryCode() != null) {
				siteForm.setContactCountryCode(viewSite.getContactCountryCode() + "");
			}
			if (viewSite.getContactNumber() != null) {
				siteForm.setContactNumber(viewSite.getContactNumber() + "");
			}

			siteForm.setAdminAccountList(siteAdminList);

			// view set readonly
			siteForm.setReadOnly(true);
			siteForm.setDelete(false);

			siteForm.setReferrer(referrerStr);

			model.addAttribute("siteForm", siteForm);

			return "sysadmin/site_create_modify";
		} else {

			// user does not have right to view site information
			logger.debug("user does not have right to view site information");
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/ModifySite.do")
	public String showModifySiteForm(@ModelAttribute("siteForm") SiteForm siteForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("showModifySiteForm()");

		logger.debug("SiteForm.Key = " + siteForm.getKey());

		logger.debug("Request.Key = " + request.getParameter("key"));

		HttpSession session = request.getSession();

		String referrerStr = request.getParameter("r");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		Site editSite = null;

		// check privilege
		if (isSysAdmin) {

			try {
				editSite = siteManager.getSiteByKey(siteForm.getKey());

			} catch (MFMSException e) {
				// TODO Redirect to not found
				logger.error(e.getMessage());
				return "common/notfound";
			}

			if (editSite == null)
				return "common/notfound";

			siteForm.setKey(editSite.getKey());
			siteForm.setName(editSite.getName());
			siteForm.setAddress(editSite.getAddress());
			if (editSite.getContactAreaCode() != null) {
				siteForm.setContactAreaCode(editSite.getContactAreaCode() + "");
			}
			if (editSite.getContactCountryCode() != null) {
				siteForm.setContactCountryCode(editSite.getContactCountryCode() + "");
			}
			if (editSite.getContactNumber() != null) {
				siteForm.setContactNumber(editSite.getContactNumber() + "");
			}

			siteForm.setReadOnly(false);
			siteForm.setDelete(false);
			siteForm.setCreateDefaultAdmin(false);

			siteForm.setReferrer(referrerStr);

			return "sysadmin/site_create_modify";

		} else {

			// user does not have right to edit site information
			logger.debug("user does not have right to edit site information");
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/DoCreateModifySite.do")
	public String doCreateModifySite(@ModelAttribute("siteForm") SiteForm siteForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("doCreateModifySite()");

		logger.debug("SiteForm.Key = " + siteForm.getKey());
		logger.debug("SiteForm.Name = " + siteForm.getName());
		logger.debug("SiteForm.Address = " + siteForm.getAddress());

		// save site
		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Site site = null;

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		String referrerStr = "";

		// check privilege
		if (isSysAdmin) {

			if (siteForm.getKey() == null)
				siteForm.setCreateDefaultAdmin(true);
			else
				siteForm.setCreateDefaultAdmin(false);

			// validate
			siteFormValidator.validate(siteForm, result);

			if (result.hasErrors()) {
				logger.debug("Validation Failed");
				return "sysadmin/site_create_modify";
			}

			if (siteForm.getKey() == null) {

				// create
				site = new Site();

				referrerStr = "c";

				site.setName(siteForm.getName());
				site.setAddress(siteForm.getAddress());

				site.setContactAreaCode(siteForm.getContactAreaCode());

				site.setContactCountryCode(siteForm.getContactCountryCode());

				site.setContactNumber(siteForm.getContactNumber());

				site.setCreateBy(account.getKey());
				site.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
				site.setDeleted("N");

				try {
					// save site
					// siteManager.saveSite(site);

					// if (site.getKey() != null) {
					// create default site admin role

					logger.debug("Default Site Admin Role Name: " + defaultSiteAdminRoleName);
					Role role = new Role();

					role.setSiteKey(site.getKey());
					role.setModeKey(Integer.valueOf(defaultModeKey));
					role.setName(defaultSiteAdminRoleName);
					role.setDescription(defaultSiteAdminRoleDesc);
					role.setCreateBy(account.getKey());
					role.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
					role.setDeleted("N");

					// set role privileges
					// give all privileges except in Site category

					List<PrivilegeCategory> pcList = roleManager.searchPrivilegeCategory(null, null, false, null, true,
							true);

					List<RolePrivilege> rpList = new ArrayList<RolePrivilege>();

					for (PrivilegeCategory pc : pcList) {
						if (!"Site".equals(pc.getCode())) {
							for (Privilege p : pc.getPrivilegeList()) {
								RolePrivilege rp = new RolePrivilege();
								rp.setPrivilegeCode(p.getCode());
								rp.setCreateBy(account.getKey());
								rp.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								rp.setDeleted("N");

								rpList.add(rp);
							}
						}
					}
					logger.debug("Role Privilege List size = " + rpList.size());

					role.setRolePrivileges(rpList);

					// roleManager.saveRole(role);

					UserAccount newUser = null;

					// if (role.getKey()!=null &&
					// siteForm.getCreateDefaultAdmin()) {
					if (siteForm.getCreateDefaultAdmin()) {
						// create default admin
						// UserAccount newUser = new UserAccount();
						newUser = new UserAccount();
						newUser.setLoginId(siteForm.getDefaultAdminId());
						newUser.setName(siteForm.getDefaultAdminName());
						// TODO: encrypt password
						String salt = BCrypt.gensalt();
						String hashed_password = BCrypt.hashpw(siteForm.getDefaultAdminPass(), salt);
						newUser.setPassword(hashed_password);

						newUser.setStatus(UserAccount.STATUS_ACTIVE);
						newUser.setLogonAttemptCount(0);
						newUser.setCreateBy(account.getKey());
						newUser.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
						newUser.setDeleted("N");

						// add role
						/*
						 * ArrayList<UserAccountRole> arList = new
						 * ArrayList<UserAccountRole>(); UserAccountRole ar =
						 * new UserAccountRole(); ar.setRoleKey(role.getKey());
						 * ar.setCreateBy(account.getKey());
						 * ar.setCreateDateTime(new
						 * Timestamp(System.currentTimeMillis()));
						 * ar.setDeleted("N");
						 * 
						 * arList.add(ar);
						 * 
						 * newUser.setAccountRoles(arList);
						 * userAccountManager.saveUserAccount(newUser);
						 * 
						 * if (newUser.getKey() != null) { // create successful
						 * logger.debug("New user key = " + newUser.getKey());
						 * 
						 * }
						 */

					}
					siteManager.createSite(site, role, newUser);

					// }
				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.debug("Operation Failed");
					result.reject("error.operation.failed");
					return "sysadmin/site_create_modify";

				}

			} else {
				// modify

				referrerStr = "m";

				try {

					site = siteManager.getSiteByKey(siteForm.getKey());

					if (site == null) {
						logger.debug("Site Key " + siteForm.getKey() + " not found");
						return "common/notfound";
					}

					site.setName(siteForm.getName());
					site.setAddress(siteForm.getAddress());

					site.setContactAreaCode(siteForm.getContactAreaCode());

					site.setContactCountryCode(siteForm.getContactCountryCode());

					site.setContactNumber(siteForm.getContactNumber());

					site.setLastModifyBy(account.getKey());
					site.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
					siteManager.saveSite(site);

				} catch (MFMSException e) {
					logger.error(e.getMessage());
					logger.debug("Operation Failed");
					result.reject("error.operation.failed");
					return "sysadmin/site_create_modify";
				}
			}

			// return to view page on success
			logger.debug("Site Key after: " + site.getKey());
			return "redirect:ViewSite.do?key=" + site.getKey() + "&r=" + referrerStr;

		} else {
			// user does not have right to create/edit site information
			logger.debug("user does not have right to create/edit site information");
			return "common/notfound";
		}

	}

	@ModelAttribute("searchSiteForm")
	public SearchSiteForm populateSearchSiteForm() {
		return new SearchSiteForm(); // populates search form for the first time
										// if its null
	}

	@RequestMapping(value = "/SearchSite.do")
	public String showSearchSiteForm(@ModelAttribute("searchSiteForm") SearchSiteForm searchSiteForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("showSearchSiteForm()");

		searchSiteForm = new SearchSiteForm();

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// check privilege
		if (isSysAdmin) {

			model.addAttribute("searchSiteForm", searchSiteForm);

			return "sysadmin/site_search";
		} else {
			// user does not have right to create/edit site information
			logger.debug("user does not have right to search site information");
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DoSearchSite.do")
	public String doSearchSite(@ModelAttribute("searchSiteForm") SearchSiteForm searchSiteForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("doSearchSite()");

		logger.debug("SearchSiteForm.Name: " + searchSiteForm.getName());
		logger.debug("SearchSiteForm.Address: " + searchSiteForm.getAddress());

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// check privilege
		if (isSysAdmin) {
			try {

				List<Site> siteList = siteManager.searchSite(searchSiteForm.getName(), searchSiteForm.getAddress(),
						null, null, null, "N");

				logger.debug("Search Site result size: " + siteList.size());
				searchSiteForm.setResultList(siteList);

				searchSiteForm.setCanGen(true);

			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			// user does not have right to create/edit site information
			logger.debug("user does not have right to search site information");
			return "common/notfound";
		}

		return "sysadmin/site_search";
	}

	@RequestMapping(value = "DoSiteDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchSiteForm") SearchSiteForm searchSiteForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "name", "address", "number" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<Site> fullList = new ArrayList<Site>();

		List<Site> siteList = new ArrayList<Site>();

		if (searchSiteForm.getCanGen()) {

			fullList = searchSiteForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new SNameComparator());

				break;

			case "name":

				Collections.sort(fullList, new SNameComparator());

				break;

			case "address":

				Collections.sort(fullList, new SAddressComparator());

				break;

			case "number":
				Collections.sort(fullList, new SNumberComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					siteList.add(fullList.get(i));
			}

			fullListSize = searchSiteForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < siteList.size(); i++) {

			JSONArray ja = new JSONArray();
			Site site = siteList.get(i);

			StringBuilder myvar = new StringBuilder();

			myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewSite.do?key=" + site.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;").append("<a onclick=\"showLoading()\"")
					.append(" href=\"ModifySite.do?key=" + site.getKey() + "&r=s\"><i")
					.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			myvar.append("<i onclick=\"showDelete(" + site.getKey() + ", '"
					+ Utility.removeQuote(Utility.replaceHtmlEntities(site.getName()))
					+ "');\" class=\"fa fa-trash-o\"></i>");

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(site.getName()));
			ja.put(Utility.replaceHtmlEntities(site.getAddress()));
			ja.put(Utility.replaceHtmlEntities(site.getContactNumber()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/DeleteSite.do")
	public String showDeleteSiteForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String siteKeyStr = request.getParameter("key");

		logger.debug("showDeleteSiteForm()[" + siteKeyStr + "]");

		Integer siteKey;
		try {
			siteKey = Integer.parseInt(siteKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// check privilege
		if (isSysAdmin) {

			Site viewSite = null;

			try {
				viewSite = siteManager.getSiteByKey(siteKey);

			} catch (MFMSException e) {
				// TODO Redirect to not found
				return "common/notfound";
			}

			// site not found
			if (viewSite == null)
				return "common/notfound";

			SiteForm siteForm = new SiteForm();

			siteForm.setKey(viewSite.getKey());
			siteForm.setName(viewSite.getName());
			siteForm.setAddress(viewSite.getAddress());
			if (viewSite.getContactAreaCode() != null) {
				siteForm.setContactAreaCode(viewSite.getContactAreaCode() + "");
			}
			if (viewSite.getContactCountryCode() != null) {
				siteForm.setContactCountryCode(viewSite.getContactCountryCode() + "");
			}
			if (viewSite.getContactNumber() != null) {
				siteForm.setContactNumber(viewSite.getContactNumber() + "");
			}

			// view set readonly
			siteForm.setReadOnly(true);

			// delete set delete
			siteForm.setDelete(true);

			model.addAttribute("siteForm", siteForm);

			return "sysadmin/site_create_modify";
		} else {

			// user does not have right to view site information
			logger.debug("user does not have right to view site information");
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DoDeleteSite.do")
	public String doDeleteSite(@ModelAttribute("searchSiteForm") SearchSiteForm searchSiteForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String siteKeyStr = request.getParameter("key");

		logger.debug("doDeleteSite()[" + siteKeyStr + "]");

		Integer siteKey;
		try {
			siteKey = Integer.parseInt(siteKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		UserAccount account = (UserAccount) session.getAttribute("user");

		// check privilege
		if (isSysAdmin) {

			try {
				Site site = siteManager.getSiteByKey(siteKey);
				if (site == null) {
					searchSiteForm.setDeleteSuccess(false);
					return "sysadmin/site_search";
				} else {
					siteManager.deleteSiteByKey(account.getKey(), siteKey);
					searchSiteForm.setDeleteSuccess(true);
					searchSiteForm.setDeletedName(site.getName());
				}

			} catch (MFMSException e) {
				// TODO Redirect to not found
				result.reject("site.delete.fail", null, null);
				return "sysadmin/site_search";
			}
			// model.addAttribute("siteForm", new SiteForm());

			return "redirect:DoSearchSite.do";
		} else {

			// user does not have right to view site information
			logger.debug("user does not have right to delete site ");
			return "common/notfound";
		}
	}

	public class SNameComparator implements Comparator<Site> {
		@Override
		public int compare(Site st, Site nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class SAddressComparator implements Comparator<Site> {
		@Override
		public int compare(Site st, Site nd) {

			String str1 = st.getAddress();
			String str2 = nd.getAddress();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class SNumberComparator implements Comparator<Site> {
		@Override
		public int compare(Site st, Site nd) {

			String str1 = st.getContactNumber();
			String str2 = nd.getContactNumber();

			return str1.compareTo(str2);
		}
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public SiteFormValidator getSiteFormValidator() {
		return siteFormValidator;
	}

	public void setSiteFormValidator(SiteFormValidator siteFormValidator) {
		this.siteFormValidator = siteFormValidator;
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}
}
