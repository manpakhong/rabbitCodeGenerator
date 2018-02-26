package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.AccountExport;
import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupExport;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.DefectExport;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.AccountGroupResponsibleManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.utility.Utility;
import hk.ebsl.mfms.utility.writer.ColumnDisplay;
import hk.ebsl.mfms.utility.writer.ExcelWriter;
import hk.ebsl.mfms.web.form.AccountGroupForm;
import hk.ebsl.mfms.web.form.LocationPrivilegeForm;
import hk.ebsl.mfms.web.form.SearchAccountGroupForm;
import hk.ebsl.mfms.web.form.SearchRoleForm;
import hk.ebsl.mfms.web.form.SearchUserAccountForm;
import hk.ebsl.mfms.web.form.UserAccountForm;
import hk.ebsl.mfms.web.form.validator.AccountGroupFormValidator;
import hk.ebsl.mfms.web.form.validator.UserAccountFormValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

//keep searchAccountForm in session so that results will be kept
@SessionAttributes({ "searchAccountForm", "searchAccountGroupForm" })
@Controller
public class AccountManagementController {

	public final static Logger logger = Logger.getLogger(AccountManagementController.class);

	@Autowired
	private UserAccountManager userManager;

	@Autowired
	private AccountGroupManager accountGroupManager;

	@Autowired
	private AccountGroupAccountManager accountGroupAccountManager;

	@Autowired
	private AccountGroupResponsibleManager accountGroupResponsibleManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private LocationManager locationManager;

	@Autowired
	private Properties privilegeMap;

	@Autowired
	private UserAccountFormValidator userAccountFormValidator;

	@Autowired
	private AccountGroupFormValidator accountGroupFormValidator;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private Properties propertyConfigurer;

	@Value("${site.admin.role.name}")
	private String defaultSiteAdminRoleName;

	@RequestMapping(value = "/CreateAccount.do")
	public String showCreateAccountForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load create account page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createAccount"));

		if (hasPrivilege) {

			try {
				UserAccountForm accountForm = new UserAccountForm();

				accountForm.setKey(null);

				List<Role> availableRoles = null;
				if (isSysAdmin) {
					// load Roles of sitekey = 1

					availableRoles = roleManager.getRolesBySiteKey(1, false);
				} else {
					//availableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
					List<Role> allAvailableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
					availableRoles = new ArrayList<Role>();

					for (Role role :allAvailableRoles) {
						if (role.getName().equals(defaultSiteAdminRoleName)) {
							if (currRole.getName().equals(defaultSiteAdminRoleName)) {
								availableRoles.add(role);
							}
						} else {
							availableRoles.add(role);
						}
					}
				}
				Collections.sort(availableRoles, new RNameComparator());
				
				accountForm.setAvailableRoleList(availableRoles);

				Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
				availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
				availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
				accountForm.setAvailableStatusList(availableStatusList);

				accountForm.setDelete(false);
				accountForm.setReadOnly(false);

				model.addAttribute("accountForm", accountForm);

				return "account/account_create_modify";
			} catch (MFMSException e) {
				return "common/notfound";
			}
		} else {
			// user does not have right to create account
			logger.debug("user does not have right to create account ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ViewAccount.do")
	public String showViewAccountForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load view account page.");
		
		String loginId = request.getParameter("loginId");

		String referrerStr = request.getParameter("r");

		logger.debug("showViewAccountForm()[" + loginId + "]");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createAccount"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyAccount"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchAccount"));

		if (hasPrivilege) {
			try {
				UserAccount account = userManager.getUserAccountByLoginId(loginId, true);

				// Check Only Site Admin Role users can have privilege to view Site Admin Role users 
				List<UserAccountRole> thisAccountRoles = account.getAccountRoles();
				Role thisRole = null; 
				if (thisAccountRoles != null) {
					for (UserAccountRole accountRole : thisAccountRoles) {
						if (accountRole.getRole().getSiteKey() == currRole.getSiteKey()) {
							thisRole = accountRole.getRole();
						}
					}
				}
				if (null!=account && thisRole.getName().equals(defaultSiteAdminRoleName)) {
					if (!currRole.getName().equals(defaultSiteAdminRoleName)) {
						// user does not have right to view account information
						logger.debug("user does not have right to view account information");
						return "common/noprivilege";
					}
				}

				UserAccountForm accountForm = new UserAccountForm();

				accountForm.setLoginId(account.getLoginId());
				accountForm.setName(account.getName());
				accountForm.setTagId(account.getTagId());
				accountForm.setKey(account.getKey());
				accountForm.setStatus(account.getStatus());
				accountForm.setEmail(account.getEmail());
				if (account.getContactCountryCode() != null) {
					accountForm.setContactCountryCode(account.getContactCountryCode() + "");
				}
				if (account.getContactAreaCode() != null) {
					accountForm.setContactAreaCode(account.getContactAreaCode() + "");
				}
				if (account.getContactNumber() != null) {
					accountForm.setContactNumber(account.getContactNumber() + "");
				}

				LocationTreeNode locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(),
						account.getKey(), false);

				accountForm.setAvailableLocationTree(locationTree);

				List<Integer> selectedLocationKeyList = locationManager
						.getPrivilegedLocationKeyList(currRole.getSiteKey(), account.getKey());
				;

				if (selectedLocationKeyList == null) {
					selectedLocationKeyList = new ArrayList<Integer>();
				}

				logger.debug("selectedLocationKeyList.size = " + selectedLocationKeyList.size());
				accountForm.setSelectedLocationKeyList(selectedLocationKeyList);

				List<Role> availableRoles = null;
				if (isSysAdmin) {
					// load Roles of sitekey = 1

					availableRoles = roleManager.getRolesBySiteKey(1, false);
				} else {
					availableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
				}
				Collections.sort(availableRoles, new RNameComparator());
				accountForm.setAvailableRoleList(availableRoles);

				List<UserAccountRole> accountRoles = account.getAccountRoles();
				if (accountRoles != null) {
					for (UserAccountRole accountRole : accountRoles) {
						if (accountRole.getRole().getSiteKey() == currRole.getSiteKey()) {
							accountForm.setSelectedRoleKey(accountRole.getRoleKey());
						}
					}
				}
				Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
				availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
				availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
				accountForm.setAvailableStatusList(availableStatusList);

				accountForm.setDelete(false);
				accountForm.setReadOnly(true);

				accountForm.setReferrer(referrerStr);

				model.addAttribute("accountForm", accountForm);

				return "account/account_create_modify";
			} catch (MFMSException e) {
				return "common/notfound";
			}
		} else {
			// user does not have right to view account information
			logger.debug("user does not have right to view account information");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ViewOwnAccount.do")
	public String showViewOwnAccountForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {

		logger.debug("User requests to load view own account page.");
		
		// String loginId = request.getParameter("loginId");

		logger.debug("showViewOwnAccountForm()");

		String referrerStr = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount currUser = (UserAccount) session.getAttribute("user");
	
		if (null!=currUser) {
			// TODO check privilege
			boolean hasPrivilege = true;
	
			if (hasPrivilege) {
				try {
					UserAccount account = userManager.getUserAccountByLoginId(currUser.getLoginId(), true);
	
					UserAccountForm accountForm = new UserAccountForm();
	
					accountForm.setLoginId(account.getLoginId());
					accountForm.setName(account.getName());
					accountForm.setTagId(account.getTagId());
					accountForm.setKey(account.getKey());
					accountForm.setStatus(account.getStatus());
					accountForm.setEmail(account.getEmail());
					if (account.getContactCountryCode() != null) {
						accountForm.setContactCountryCode(account.getContactCountryCode() + "");
					}
					if (account.getContactAreaCode() != null) {
						accountForm.setContactAreaCode(account.getContactAreaCode() + "");
					}
					if (account.getContactNumber() != null) {
						accountForm.setContactNumber(account.getContactNumber() + "");
					}
	
					List<Role> availableRoles = null;
	
					accountForm.setAvailableRoleList(availableRoles);
	
					Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
					availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
					availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
					accountForm.setAvailableStatusList(availableStatusList);
	
					accountForm.setDelete(false);
					accountForm.setReadOnly(true);
	
					accountForm.setReferrer(referrerStr);
	
					LocationTreeNode locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(),
							account.getKey(), false);
	
					accountForm.setAvailableLocationTree(locationTree);
	
					List<Integer> selectedLocationKeyList = locationManager
							.getPrivilegedLocationKeyList(currRole.getSiteKey(), account.getKey());
					;
	
					if (selectedLocationKeyList == null) {
						selectedLocationKeyList = new ArrayList<Integer>();
					}
	
					logger.debug("selectedLocationKeyList.size = " + selectedLocationKeyList.size());
					accountForm.setSelectedLocationKeyList(selectedLocationKeyList);
	
					model.addAttribute("accountForm", accountForm);
	
					return "account/account_modify_own";
				} catch (MFMSException e) {
					return "common/notfound";
				}
			} else {
				// user does not have right to view own account information
				logger.debug("user does not have right to view own account information");
				return "common/noprivilege";
			}
		} else {
			logger.debug("showViewOwnAccountForm: Session timed out");
			response.setStatus(Response.SC_OK);
			response.sendRedirect("Login.do");
			
			return null;
		}
	}

	@RequestMapping(value = "/ModifyAccount.do")
	public String showModifyAccountForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load modify account page.");
		
		String loginId = request.getParameter("loginId");

		String referrerStr = request.getParameter("r");

		logger.debug("showModifyAccountForm()[" + loginId + "]");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.modifyAccount"));

		if (hasPrivilege) {
			try {

				UserAccount account = userManager.getUserAccountByLoginId(loginId, true);

				// Check Only Site Admin Role users can have privilege to view Site Admin Role users 
				List<UserAccountRole> thisAccountRoles = account.getAccountRoles();
				Role thisRole = null; 
				if (thisAccountRoles != null) {
					for (UserAccountRole accountRole : thisAccountRoles) {
						if (accountRole.getRole().getSiteKey() == currRole.getSiteKey()) {
							thisRole = accountRole.getRole();
						}
					}
				}
				if (null!=account && thisRole.getName().equals(defaultSiteAdminRoleName)) {
					if (!currRole.getName().equals(defaultSiteAdminRoleName)) {
						// user does not have right to view account information
						logger.debug("user does not have right to view account information");
						return "common/noprivilege";
					}
				}

				UserAccountForm accountForm = new UserAccountForm();

				accountForm.setLoginId(account.getLoginId());
				accountForm.setName(account.getName());
				accountForm.setTagId(account.getTagId());
				accountForm.setKey(account.getKey());
				accountForm.setStatus(account.getStatus());
				accountForm.setEmail(account.getEmail());
				if (account.getContactCountryCode() != null) {
					accountForm.setContactCountryCode(account.getContactCountryCode() + "");
				}
				if (account.getContactAreaCode() != null) {
					accountForm.setContactAreaCode(account.getContactAreaCode() + "");
				}
				if (account.getContactNumber() != null) {
					accountForm.setContactNumber(account.getContactNumber() + "");
				}
				List<Role> availableRoles = null;
				if (isSysAdmin) {
					// load Roles of sitekey = 1

					availableRoles = roleManager.getRolesBySiteKey(1, false);
				} else {
					//availableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
					List<Role> allAvailableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
					availableRoles = new ArrayList<Role>();

					for (Role role :allAvailableRoles) {
						if (role.getName().equals(defaultSiteAdminRoleName)) {
							if (currRole.getName().equals(defaultSiteAdminRoleName)) {
								availableRoles.add(role);
							}
						} else {
							availableRoles.add(role);
						}
					}
				}
				Collections.sort(availableRoles, new RNameComparator());
				accountForm.setAvailableRoleList(availableRoles);

				List<UserAccountRole> accountRoles = account.getAccountRoles();
				if (accountRoles != null) {
					boolean roleFound = false;
					for (UserAccountRole accountRole : accountRoles) {
						if (accountRole.getRole().getSiteKey() == currRole.getSiteKey()) {
							accountForm.setSelectedRoleKey(accountRole.getRoleKey());
							roleFound = true;
							break;

						}
					}
					if (!roleFound) {
						// assign role first
						logger.debug("target user does not have role in this site");
						return "common/noprivilege";
					}
				} else {
					// no roles
					// assign role first
					logger.debug("target user does not have role in this site");
					return "common/noprivilege";
				}
				Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
				availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
				availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
				accountForm.setAvailableStatusList(availableStatusList);

				accountForm.setDelete(false);
				accountForm.setReadOnly(false);

				accountForm.setReferrer(referrerStr);

				model.addAttribute("accountForm", accountForm);

				return "account/account_create_modify";
			} catch (MFMSException e) {
				return "common/notfound";
			}
		} else {
			// user does not have right to modify account information
			logger.debug("user does not have right to modify account information");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ModifyOwnAccount.do")
	public String showModifyOwnAccountForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load modify own account page.");
		
		// String loginId = request.getParameter("loginId");

		String referrerStr = request.getParameter("r");

		logger.debug("showModifyOwnAccountForm()");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyAccount"))
				|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyOwnAccount"));

		if (hasPrivilege) {
			try {
				UserAccount account = userManager.getUserAccountByLoginId(currUser.getLoginId(), true);

				UserAccountForm accountForm = new UserAccountForm();

				accountForm.setLoginId(account.getLoginId());
				accountForm.setName(account.getName());
				accountForm.setTagId(account.getTagId());
				accountForm.setKey(account.getKey());
				accountForm.setStatus(account.getStatus());
				accountForm.setEmail(account.getEmail());
				if (account.getContactCountryCode() != null) {
					accountForm.setContactCountryCode(account.getContactCountryCode() + "");
				}
				if (account.getContactAreaCode() != null) {
					accountForm.setContactAreaCode(account.getContactAreaCode() + "");
				}
				if (account.getContactNumber() != null) {
					accountForm.setContactNumber(account.getContactNumber() + "");
				}
				List<Role> availableRoles = null;

				accountForm.setAvailableRoleList(availableRoles);

				Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
				availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
				availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
				accountForm.setAvailableStatusList(availableStatusList);

				accountForm.setDelete(false);
				accountForm.setReadOnly(false);

				accountForm.setReferrer(referrerStr);

				model.addAttribute("accountForm", accountForm);

				return "account/account_modify_own";
			} catch (MFMSException e) {
				return "common/notfound";
			}
		} else {
			// user does not have right to modify own account information
			logger.debug("user does not have right to modify own account information");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoCreateModifyAccount.do")
	public String doCreateModify(@ModelAttribute("accountForm") UserAccountForm accountForm, HttpServletRequest request,
			HttpServletResponse response, BindingResult result, ModelMap model) {

		logger.debug("doCreateModify()");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		logger.debug("UserForm.loginId = " + accountForm.getLoginId());
		try {
			// check privilege
			// boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
			// .contains(privilegeMap.get("privilege.code.modifyAccount"))
			// || currRole.getGrantedPrivilegeCodeList()
			// .contains(privilegeMap.get("privilege.code.createAccount"));

			UserAccount newUser = null;

			String referrerStr = "";

			userAccountFormValidator.validate(accountForm, result);
			if (result.hasErrors()) {
				logger.debug("Validation Failed");

				List<Role> availableRoles = null;
				if (isSysAdmin) {
					// load Roles of sitekey = 1

					availableRoles = roleManager.getRolesBySiteKey(1, false);
				} else {
					//availableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
					List<Role> allAvailableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
					availableRoles = new ArrayList<Role>();

					for (Role role :allAvailableRoles) {
						if (role.getName().equals(defaultSiteAdminRoleName)) {
							if (currRole.getName().equals(defaultSiteAdminRoleName)) {
								availableRoles.add(role);
							}
						} else {
							availableRoles.add(role);
						}
					}
				}
				Collections.sort(availableRoles, new RNameComparator());
				accountForm.setAvailableRoleList(availableRoles);

				/*
				 * List<UserAccountRole> accountRoles =
				 * account.getAccountRoles(); if (accountRoles != null) { for
				 * (UserAccountRole accountRole : accountRoles) { if
				 * (accountRole.getRole().getSiteKey() == currRole.getSiteKey())
				 * { accountForm.setSelectedRoleKey(accountRole.getRoleKey()); }
				 * } }
				 */
				Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
				availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
				availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
				accountForm.setAvailableStatusList(availableStatusList);

				// return with errors
				return "account/account_create_modify";
			}

			// no error
			if (accountForm.getKey() == null) {

				if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createAccount"))) {
					// create
					referrerStr = "c";
					newUser = new UserAccount();

					newUser.setLoginId(accountForm.getLoginId());
					newUser.setName(accountForm.getName());

					newUser.setContactCountryCode(accountForm.getContactCountryCode());

					newUser.setContactAreaCode(accountForm.getContactAreaCode());

					newUser.setContactNumber(accountForm.getContactNumber());

					newUser.setEmail(accountForm.getEmail());
					newUser.setTagId(accountForm.getTagId());

					// TODO: encrypt password
					String salt = BCrypt.gensalt();
					String hashed_password = BCrypt.hashpw(accountForm.getPassword(), salt);
					newUser.setPassword(hashed_password);

					newUser.setStatus(accountForm.getStatus());
					newUser.setLogonAttemptCount(0);
					newUser.setCreateBy(currUser.getKey());
					newUser.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
					newUser.setDeleted("N");

					// new account role
					UserAccountRole accountRole = new UserAccountRole();
					accountRole.setRoleKey(accountForm.getSelectedRoleKey());

					accountRole.setCreateBy(currUser.getKey());
					accountRole.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
					accountRole.setLastModifyBy(currUser.getKey());
					accountRole.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
					accountRole.setDeleted("N");

					List<UserAccountRole> accountRoleList = new ArrayList<UserAccountRole>();

					accountRoleList.add(accountRole);
					newUser.setAccountRoles(accountRoleList);

				} else {
					logger.debug("user does not have right to create account");
					return "common/noprivilege";
				}
			} else {

				if (currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyAccount"))) {
					// modify

					referrerStr = "m";
					newUser = userManager.getUserAccountByLoginId(accountForm.getLoginId(), true);

					List<UserAccountRole> accountRoles = newUser.getAccountRoles();
					if (accountRoles != null) {
						boolean roleFound = false;
						for (UserAccountRole accountRole : accountRoles) {
							if (accountRole.getRole().getSiteKey() == currRole.getSiteKey()) {
								roleFound = true;
								break;
							}
						}
						if (!roleFound) {
							// assign role first
							logger.debug("target user does not have role in this site");
							return "common/noprivilege";
						}
					} else {
						// no roles
						// assign role first
						logger.debug("target user does not have role in this site");
						return "common/noprivilege";
					}

					// do not allow changing login Id
					// newUser.setLoginId(accountForm.getLoginId());
					newUser.setName(accountForm.getName());

					newUser.setContactCountryCode(accountForm.getContactCountryCode());

					newUser.setContactAreaCode(accountForm.getContactAreaCode());

					newUser.setContactNumber(accountForm.getContactNumber());

					newUser.setEmail(accountForm.getEmail());
					newUser.setTagId(accountForm.getTagId());

					newUser.setStatus(accountForm.getStatus());

					if (accountForm.getStatus().equals("A")) {
						newUser.setLogonAttemptCount(0);
					}

					newUser.setLastModifyBy(currUser.getKey());
					newUser.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
					// newUser.setDeleted("N");

					// change account role
					List<UserAccountRole> accountRoleList = newUser.getAccountRoles();
					if (accountRoleList != null && accountRoleList.size() > 0) {
						for (UserAccountRole accountRole : accountRoleList) {
							if (accountRole.getRole().getSiteKey() == currRole.getSiteKey()) {
								// update role key
								accountRole.setRoleKey(accountForm.getSelectedRoleKey());
								break;
							}
						}
					} else {
						// new account role
						UserAccountRole accountRole = new UserAccountRole();
						accountRole.setRoleKey(accountForm.getSelectedRoleKey());

						accountRole.setCreateBy(currUser.getKey());
						accountRole.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
						accountRole.setLastModifyBy(currUser.getKey());
						accountRole.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
						accountRole.setDeleted("N");

						accountRoleList = new ArrayList<UserAccountRole>();

						accountRoleList.add(accountRole);
						newUser.setAccountRoles(accountRoleList);

					}
				} else {
					logger.debug("user does not have right to modify account");
					return "common/noprivilege";
				}
			}

			try {
				userManager.saveUserAccount(newUser);

			} catch (MFMSException e) {

				throw e;
			}

			// return to view page on success
			logger.debug("User Login ID : " + newUser.getLoginId());

			return "redirect:ViewAccount.do?loginId=" + URLEncoder.encode(newUser.getLoginId(), "UTF-8") + "&r="
					+ referrerStr;

		} catch (Exception | MFMSException e) {
			e.printStackTrace();
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/DoModifyOwnAccount.do")
	public String doModifyOwn(@ModelAttribute("accountForm") UserAccountForm accountForm, HttpServletRequest request,
			HttpServletResponse response, BindingResult result, ModelMap model) {

		logger.debug("doModifyOwn()");

		HttpSession session = request.getSession();

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		logger.debug("UserForm.loginId = " + accountForm.getLoginId());
		try {
			// check privilege
			boolean hasPrivilege = true;

			if (hasPrivilege) {

				UserAccount newUser = null;

				String referrerStr = "m";
				// do not require role
				userAccountFormValidator.validate(accountForm, result, true);
				if (result.hasErrors()) {
					logger.debug("Validation Failed");

					List<Role> availableRoles = null;

					accountForm.setAvailableRoleList(availableRoles);

					Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
					availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
					availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");
					accountForm.setAvailableStatusList(availableStatusList);

					// return with errors
					return "account/account_modify_own";
				}

				// no error

				newUser = userManager.getUserAccountByLoginId(accountForm.getLoginId(), true);

				// do not allow changing login Id
				// newUser.setLoginId(accountForm.getLoginId());
				newUser.setName(accountForm.getName());

				newUser.setContactCountryCode(accountForm.getContactCountryCode());

				newUser.setContactAreaCode(accountForm.getContactAreaCode());

				newUser.setContactNumber(accountForm.getContactNumber());

				newUser.setEmail(accountForm.getEmail());
				// newUser.setTagId(accountForm.getTagId());

				// TODO: encrypt password
				if (!StringUtils.isEmpty(accountForm.getPassword())) {

					String salt = BCrypt.gensalt();
					String hashed_password = BCrypt.hashpw(accountForm.getPassword(), salt);
					newUser.setPassword(hashed_password);

					// newUser.setPassword(accountForm.getPassword());
				}

				// newUser.setStatus(accountForm.getStatus());
				// newUser.setLogonAttemptCount(0);
				newUser.setLastModifyBy(currUser.getKey());
				newUser.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

				userManager.saveUserAccount(newUser);

				// return to view page on success
				// logger.debug("User Login ID : " + newUser.getLoginId());

				return "redirect:ViewOwnAccount.do?r=" + referrerStr;

			} else {

				// user does not have right to modify own account information
				logger.debug("user does not have right to modify own account account");
				return "common/noprivilege";
			}

		} catch (Exception | MFMSException e) {
			e.printStackTrace();
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/DoDeleteAccount.do")
	public String doDeleteAccount(@ModelAttribute("searchAccountForm") SearchUserAccountForm searchAccountForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String accountKeyStr = request.getParameter("key");

		logger.debug("doDeleteAccount()[" + accountKeyStr + "]");

		Integer accountKey;
		try {
			accountKey = Integer.parseInt(accountKeyStr);
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
					.contains(privilegeMap.get("privilege.code.removeAccount"));

			// check privilege
			if (hasPrivilege) {
				try {
					// validate : check account only has role in same site
					UserAccount targetAccount = userManager.getUserAccountByAccountKey(accountKey, true);

					List<UserAccountRole> arList = targetAccount.getAccountRoles();

					if (arList != null && arList.size() > 1) {
						// error
						logger.debug("Account " + targetAccount.getLoginId() + " still has more than 1 roles");
						result.reject("account.delete.fail.has.other.role", new Object[] { targetAccount.getLoginId() },
								null);

						searchAccountForm.setDeleteSuccess(false);

						return "account/account_search";
					} else {
						// no role or 1 role
						if (arList == null || !arList.get(0).getRole().getSiteKey().equals(currRole.getSiteKey())) {

							// error
							logger.debug("Account " + targetAccount.getLoginId() + " does not have role in this site");
							result.reject("account.delete.fail.not.found", new Object[] { targetAccount.getLoginId() },
									null);

							searchAccountForm.setDeleteSuccess(false);

							return "account/account_search";
						} else {
							// delete account
							logger.debug("Going to delete account: " + targetAccount.getLoginId());
							targetAccount.setDeleted("Y");
							targetAccount.setLastModifyBy(account.getKey());
							targetAccount.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

							userManager.saveUserAccount(targetAccount);

							searchAccountForm.setDeleteSuccess(true);
							searchAccountForm.setDeletedName(targetAccount.getLoginId());
						}
					}

				} catch (MFMSException e) {
					// TODO Redirect to not found
					return "common/notfound";
				}

				return "redirect:DoSearchAccount.do";
			} else {

				// user does not have right to delete account information
				logger.debug("user does not have right to delete account");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/SearchAccount.do")
	public String showSearchAccountForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load search account page.");
		
		logger.debug("showSearchAccountForm()");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount account = (UserAccount) session.getAttribute("user");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchAccount"));
		;

		if (hasPrivilege) {

			try {
				SearchUserAccountForm searchAccountForm = new SearchUserAccountForm();

				// get user's current site
				/*
				 * if (currRole != null) { Integer siteKey =
				 * currRole.getSiteKey(); searchAccountForm.setSiteKey(siteKey);
				 * }
				 */
				List<Role> availableRoles = null;
				if (isSysAdmin) {
					// load Roles of sitekey = 1
					availableRoles = roleManager.getRolesBySiteKey(1, false);
				} else {
					availableRoles = roleManager.getRolesBySiteKey(currRole.getSiteKey(), false);
				}
				Collections.sort(availableRoles, new RNameComparator());

				searchAccountForm.setOwnAccountKey(account.getKey());
				searchAccountForm.setAvailableRoleList(availableRoles);

				Map<String, String> availableStatusList = new LinkedHashMap<String, String>();
				availableStatusList.put(null, "");
				availableStatusList.put(UserAccount.STATUS_ACTIVE, "account.status.active");
				availableStatusList.put(UserAccount.STATUS_SUSPENDED, "account.status.suspended");

				searchAccountForm.setAvailableStatusList(availableStatusList);

				model.addAttribute("searchAccountForm", searchAccountForm);

				return "account/account_search";

			} catch (MFMSException e) {

				return "common/notfound";

			}
		} else {
			// user does not have right to search account information
			logger.debug("user does not have right to search account ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSearchAccount.do")
	public String doSearchAccount(@ModelAttribute("searchAccountForm") SearchUserAccountForm searchAccountForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("doSearchAccount()");

		logger.debug("SearchUserAccountForm.Name: " + searchAccountForm.getName());
		logger.debug("SearchUserAccountForm.LoginId: " + searchAccountForm.getLoginId());
		logger.debug("SearchUserAccountForm.SelectedRoleKey: " + searchAccountForm.getSelectedRoleKey());
		logger.debug("SearchUserAccountForm.Status: " + searchAccountForm.getStatus());

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchAccount"));

		if (hasPrivilege) {

			try {

				// find userAccountRole of this site

				Integer siteKey = currRole.getSiteKey();

				List<UserAccountRole> resultList = userManager.searchUserAccountRole(siteKey,
						searchAccountForm.getSelectedRoleKey(), searchAccountForm.getLoginId(),
						searchAccountForm.getName(), searchAccountForm.getStatus());

				searchAccountForm.setResultList(resultList);

				searchAccountForm.setCanGen(true);

				Boolean canModify = currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.modifyAccount"));

				Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.removeAccount"));

				Boolean canSetLp = currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.setLocationPrivilage"));

				searchAccountForm.setCanModify(canModify);

				searchAccountForm.setCanRemove(canRemove);

				searchAccountForm.setCanSetLp(canSetLp);
				
				searchAccountForm.setFullListSize(searchAccountForm.getResultList().size());
				
				return "account/account_search";

			} catch (MFMSException e) {

				return "common/notfound";

			}
		} else {
			// user does not have right to search account
			logger.debug("user does not have right to search account ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "DoAccountDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(@ModelAttribute("searchAccountForm") SearchUserAccountForm searchUserAccountForm,
			HttpServletRequest request, HttpServletResponse response, Locale locale)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "role", "loginId", "name", "email", "number", "status" };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<UserAccountRole> fullList = new ArrayList<UserAccountRole>();

		List<UserAccountRole> userAccountList = new ArrayList<UserAccountRole>();

		if (searchUserAccountForm.getCanGen()) {

			fullList = searchUserAccountForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new ALoginIdComparator());

				break;

			case "role":

				Collections.sort(fullList, new ARoleComparator());

				break;

			case "loginId":
				Collections.sort(fullList, new ALoginIdComparator());

				break;

			case "name":
				Collections.sort(fullList, new ANameComparator());

				break;

			case "email":
				Collections.sort(fullList, new AMailComparator());

				break;

			case "number":
				Collections.sort(fullList, new ANumberComparator());

				break;

			case "status":
				Collections.sort(fullList, new AStatusComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					userAccountList.add(fullList.get(i));
			}

			fullListSize = searchUserAccountForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < userAccountList.size(); i++) {

			JSONArray ja = new JSONArray();
			UserAccountRole userAccount = userAccountList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchUserAccountForm.getCanModify();
			boolean canRemove = searchUserAccountForm.getCanRemove();
			boolean canSetLp = searchUserAccountForm.getCanSetLp();

			String loginId = Utility.replaceHtmlEntities(userAccount.getUserAccount().getLoginId());

			if (account.getLoginId().equals(userAccount.getUserAccount().getLoginId())) {
				myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"ViewOwnAccount.do?loginId=" + loginId + "&r=s\"><i")
					.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");
			} else {
				if (userAccount.getRole().getName().equals(defaultSiteAdminRoleName)) {
					if (currRole.getName().equals(defaultSiteAdminRoleName)) {
						myvar.append("<a onclick=\"showLoading()\"")
							.append(" href=\"ViewAccount.do?loginId=" + loginId + "&r=s\"><i")
							.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");
					}
				} else {
					myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ViewAccount.do?loginId=" + loginId + "&r=s\"><i")
						.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");
				}
			}

			if (canModify) {

				if (account.getLoginId().equals(userAccount.getUserAccount().getLoginId())) {
					myvar.append("<a onclick=\"showLoading()\"")
							.append(" href=\"ModifyOwnAccount.do?loginId=" + loginId + "&r=s\"><i")
							.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

				} else {
					if (userAccount.getRole().getName().equals(defaultSiteAdminRoleName)) {
						if (currRole.getName().equals(defaultSiteAdminRoleName)) {
							myvar.append("<a onclick=\"showLoading()\"")
								.append(" href=\"ModifyAccount.do?loginId=" + loginId + "&r=s\"><i")
								.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");
						}
					} else {
						myvar.append("<a onclick=\"showLoading()\"")
							.append(" href=\"ModifyAccount.do?loginId=" + loginId + "&r=s\"><i")
							.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");
						
					}
				}

			}

			if (canSetLp) {
				if (userAccount.getRole().getName().equals(defaultSiteAdminRoleName)) {
					if (currRole.getName().equals(defaultSiteAdminRoleName)) {
						myvar.append("<a onclick=\"showLoading()\"")
								.append(" href=\"SetLocationPrivilege.do?loginId=" + loginId + "&r=s\"><i")
								.append(" class=\"fa fa-map-marker\"></i></a>&nbsp;&nbsp;");
					}
				} else {
					myvar.append("<a onclick=\"showLoading()\"")
					.append(" href=\"SetLocationPrivilege.do?loginId=" + loginId + "&r=s\"><i")
					.append(" class=\"fa fa-map-marker\"></i></a>&nbsp;&nbsp;");
				}
			}

			if (canRemove) {
				if (userAccount.getRole().getName().equals(defaultSiteAdminRoleName)) {
					if (currRole.getName().equals(defaultSiteAdminRoleName)) {
						myvar.append("<i onclick=\"showDelete(" + userAccount.getUserAccount().getKey() + ", '" + loginId
								+ "');\" class=\"fa fa-trash-o\"></i>");
					}
				} else {
					myvar.append("<i onclick=\"showDelete(" + userAccount.getUserAccount().getKey() + ", '" + loginId
							+ "');\" class=\"fa fa-trash-o\"></i>");
				}
			}

			ja.put(myvar.toString());
			// ja.put(userAccount.getRole().getSite().getName());
			ja.put(Utility.replaceHtmlEntities(userAccount.getRole().getName()));
			ja.put(Utility.replaceHtmlEntities(userAccount.getUserAccount().getLoginId()));
			ja.put(Utility.replaceHtmlEntities(userAccount.getUserAccount().getName()));
			ja.put(userAccount.getUserAccount().getEmail());

			String number = "";

			if (userAccount.getUserAccount().getContactCountryCode() != null
					|| userAccount.getUserAccount().getContactAreaCode() != null) {

				if (!userAccount.getUserAccount().getContactCountryCode().isEmpty())
					number += userAccount.getUserAccount().getContactCountryCode() + " - ";

				if (!userAccount.getUserAccount().getContactAreaCode().isEmpty())
					number += userAccount.getUserAccount().getContactAreaCode() + " - ";

				number += userAccount.getUserAccount().getContactNumber();

			}

			ja.put(number);

			String status = "";
			if (userAccount.getUserAccount().getStatus().equals("A"))
				//status = "Active";
				status = messageSource.getMessage("account.status.active", null, locale);
			else if (userAccount.getUserAccount().getStatus().equals("S"))
				//status = "Suspend";
				status = messageSource.getMessage("account.status.suspended", null, locale);

			ja.put(status);
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}
	
	@RequestMapping(value = "/ExportAccount.do")
	public @ResponseBody void ExportAccount(@ModelAttribute("searchAccountForm") SearchUserAccountForm searchUserAccountForm,
			HttpServletRequest request, HttpServletResponse response, Locale locale){
//		logger.debug("ExportAccount.do - The entered ID : " + searchUserAccountForm.getLoginId());
//		logger.debug("ExportAccount.do - The entered Name : " + searchUserAccountForm.getName());	
//		logger.debug("ExportAccount.do - The entered Role Key : " + searchUserAccountForm.getSelectedRoleKey());	
//		logger.debug("ExportAccount.do - The entered Status : " + searchUserAccountForm.getStatus());	
//		logger.debug("ExportAccount.do - Result List Size : " + searchUserAccountForm.getResultList().size());
		
		List<UserAccountRole> resultList = new ArrayList<UserAccountRole>(searchUserAccountForm.getResultList());
		logger.debug("resultList Size : " +resultList.size());
		
		try{
			Calendar cal = Calendar.getInstance();
			String filePath = URLDecoder.decode(propertyConfigurer.getProperty("exportPath"), "UTF-8");
			String fileName = filePath + "AC_" + cal.getTimeInMillis()
					+ ".xlsx";
			File acListFile = new File(fileName);
			
			FileOutputStream acListStream = new FileOutputStream(acListFile,
					false);
			
			ExcelWriter acListWriter = new ExcelWriter(acListStream);
			
			//header
			List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();
			ColumnDisplay roleCol = new ColumnDisplay();
			roleCol.setProperty("role");
			roleCol.setHeadingKey(messageSource.getMessage("account.role",
					null, locale));
			
			ColumnDisplay userNameCol = new ColumnDisplay();
			userNameCol.setProperty("userName");
			userNameCol.setHeadingKey(messageSource.getMessage("account.username",
					null, locale));
			
			ColumnDisplay accountNameCol = new ColumnDisplay();
			accountNameCol.setProperty("accountName");
			accountNameCol.setHeadingKey(messageSource.getMessage("account.name",
					null, locale));
			
			ColumnDisplay emailCol = new ColumnDisplay();
			emailCol.setProperty("email");
			emailCol.setHeadingKey(messageSource.getMessage("account.email",
					null, locale));
			
			ColumnDisplay contactNumberCol = new ColumnDisplay();
			contactNumberCol.setProperty("contactNumber");
			contactNumberCol.setHeadingKey(messageSource.getMessage("account.contact.number",
					null, locale));
			
			ColumnDisplay statusCol = new ColumnDisplay();
			statusCol.setProperty("status");
			statusCol.setHeadingKey(messageSource.getMessage("account.status",
					null, locale));
			
			columnDisplays.add(roleCol);
			columnDisplays.add(userNameCol);
			columnDisplays.add(accountNameCol);
			columnDisplays.add(emailCol);
			columnDisplays.add(contactNumberCol);
			columnDisplays.add(statusCol);
			
			//Row content
			List<AccountExport> exportList = new ArrayList<AccountExport>();
			
			for(UserAccountRole uar : resultList){
				AccountExport export = new AccountExport();
				export.setRole(uar.getRole().getName());
				export.setUserName(uar.getUserAccount().getLoginId());
				export.setAccountName(uar.getUserAccount().getName());
				export.setEmail(uar.getUserAccount().getEmail());
//				export.setContactNumber(uar.getUserAccount().getContactNumber());
			if(uar.getUserAccount().getContactCountryCode()!=null && uar.getUserAccount().getContactAreaCode()!=null){
				if(!uar.getUserAccount().getContactCountryCode().equals("") && !uar.getUserAccount().getContactAreaCode().equals("")){
					export.setContactNumber(uar.getUserAccount().getContactCountryCode() + " - " + uar.getUserAccount().getContactAreaCode() + " - " + uar.getUserAccount().getContactNumber());
					
				}else if(!uar.getUserAccount().getContactCountryCode().equals("") && uar.getUserAccount().getContactAreaCode().equals("")){
					export.setContactNumber(uar.getUserAccount().getContactCountryCode() + " - " + uar.getUserAccount().getContactNumber());
					
				}else if(uar.getUserAccount().getContactCountryCode().equals("") && !uar.getUserAccount().getContactAreaCode().equals("")){
					export.setContactNumber(uar.getUserAccount().getContactAreaCode() + " - " + uar.getUserAccount().getContactNumber());
					
				}else{
					export.setContactNumber(uar.getUserAccount().getContactNumber());
				}
			} else if(uar.getUserAccount().getContactCountryCode()!=null && uar.getUserAccount().getContactAreaCode()==null){
				if(!uar.getUserAccount().getContactCountryCode().equals("")){
					export.setContactNumber(uar.getUserAccount().getContactCountryCode() + " - " + uar.getUserAccount().getContactNumber());
				} else {
					export.setContactNumber(uar.getUserAccount().getContactNumber());
				}
				
			} else if(uar.getUserAccount().getContactCountryCode()==null && uar.getUserAccount().getContactAreaCode()!=null){
				if(!uar.getUserAccount().getContactAreaCode().equals("")){
					export.setContactNumber(uar.getUserAccount().getContactAreaCode() + " - " + uar.getUserAccount().getContactNumber()); 
				} else {
					export.setContactNumber(uar.getUserAccount().getContactNumber());
				}
				
			} else {
				export.setContactNumber(uar.getUserAccount().getContactNumber());
			}
				if(uar.getUserAccount().getStatus().equals("A")){
					export.setStatus(messageSource.getMessage("account.status.active", null, locale));
				}else{
					export.setStatus(messageSource.getMessage("account.status.suspended", null, locale));
				}
				
				exportList.add(export);
			}
			
			acListWriter.write(true, 0, "AC", null, exportList, columnDisplays, false);

			acListWriter.close();
			
			response.setHeader("Content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-disposition", "inline;filename=\""
					+ acListFile.getName() + "\"");
				
			ServletOutputStream servletOutputStream = response
					.getOutputStream();
			byte[] b = new byte[1024];
			int i = 0;
			FileInputStream fis = new java.io.FileInputStream(fileName);
			while ((i = fis.read(b)) > 0) {
				servletOutputStream.write(b, 0, i);
			}
			fis.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}	
	}
	

	@RequestMapping(value = "/Account.do")
	public String showAccountMenu(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("User requests to load account page.");
		
		logger.debug("showAccountMenu()");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {

			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.createAccount"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.searchAccount"));

			if (hasPrivilege)
				return "account/account_menu";
			else {

				logger.debug("user does not have right to search / create account");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/AccountManagement.do")
	public String showAccountManagementMenu(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("User requests to load account management page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {

			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.createAccount"))
					|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchAccount"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.createAccountRole"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.modifyOwnAccount"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.searchAccountRole"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.createAccountGroup"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.searchAccountGroup"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.setLocationPrivilage"));

			if (hasPrivilege)
				return "account/account_man_menu";
			else {

				logger.debug("user does not have any rights to account management");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@ModelAttribute("searchAccountForm")
	public SearchUserAccountForm populateSearchAccountForm() {

		SearchUserAccountForm searchAccountForm = new SearchUserAccountForm();

		/*
		 * Map<String, String> availableStatusList = new LinkedHashMap<String,
		 * String>(); availableStatusList.put(UserAccount.STATUS_ACTIVE,
		 * "account.status.active");
		 * availableStatusList.put(UserAccount.STATUS_SUSPENDED,
		 * "account.status.suspended");
		 * 
		 * searchAccountForm.setAvailableStatusList(availableStatusList);
		 */

		return searchAccountForm;
	}

	@ModelAttribute("searchAccountGroupForm")
	public SearchAccountGroupForm populateSearchAccountGroupForm() {

		SearchAccountGroupForm searchAccountGroupForm = new SearchAccountGroupForm();

		/*
		 * Map<String, String> availableStatusList = new LinkedHashMap<String,
		 * String>(); availableStatusList.put(UserAccount.STATUS_ACTIVE,
		 * "account.status.active");
		 * availableStatusList.put(UserAccount.STATUS_SUSPENDED,
		 * "account.status.suspended");
		 * 
		 * searchAccountForm.setAvailableStatusList(availableStatusList);
		 */

		return searchAccountGroupForm;
	}

	@RequestMapping(value = "/AccountGroup.do")
	public String showAccountGroupMenu(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("User requests to load account group page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {

			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.createAccountGroup"))
					|| currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.searchAccountGroup"));

			if (hasPrivilege)
				return "account/accountGroup_menu";
			else {

				logger.debug("user does not have right to search / create accountGroup");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/CreateAccountGroup.do")
	public String showCreateAccountGroupForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load create account group page.");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.createAccountGroup"));

		if (hasPrivilege) {

			// try {
			AccountGroupForm accountGroupForm = new AccountGroupForm();

			List<UserAccount> userAccountList = new ArrayList<UserAccount>();
			// userAccountList = userManager.getAllUserAccount();

			List<UserAccountRole> siteUserAccountRoleList = userManager.searchUserAccountRole(currRole.getSiteKey(),
					null, null, null, null);

			if (siteUserAccountRoleList != null) {
				for (UserAccountRole ar : siteUserAccountRoleList) {

					// do not add duplicate if any
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
			accountGroupForm.setUserAccountList(userAccountList);
			accountGroupForm.setSiteKey(currRole.getSiteKey());
			accountGroupForm.setKey(null);
			accountGroupForm.setDelete(false);
			accountGroupForm.setReadOnly(false);

			model.addAttribute("accountGroupForm", accountGroupForm);

			return "account/accountGroup_create_modify";

		} else {
			// user does not have right to create accountGroup information
			logger.debug("user does not have right to create accountGroup ");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/ViewAccountGroup.do")
	public String showViewAccountGroupForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load view account group page.");
		
		String accountGroupKeyStr = request.getParameter("key");
		String errorStr = request.getParameter("error");

		logger.debug("showViewAccountGroupForm()[" + accountGroupKeyStr + "]");

		Integer accountGroupKey = Integer.parseInt(accountGroupKeyStr);

		String referrerStr = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {

			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.searchAccountGroup"))
					|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createAccountGroup"))
					|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyAccountGroup"));

			if (hasPrivilege) {

				AccountGroup viewAccountGroup = null;
				viewAccountGroup = accountGroupManager.getAccountGroupByAccountGroupKey(accountGroupKey);

				if (viewAccountGroup == null || currRole.getSiteKey() != viewAccountGroup.getSiteKey()) {
					return "common/notfound";
				}

				/*
				 * List<UserAccount> list1 = new ArrayList<UserAccount>();
				 * List<UserAccount> list2 = new ArrayList<UserAccount>();
				 * 
				 * List<AccountGroupResponsible> agrList = new
				 * ArrayList<AccountGroupResponsible>(); agrList =
				 * accountGroupResponsibleManager.searchAccountGroupResponsible(
				 * viewAccountGroup.getKey());
				 * 
				 * List<AccountGroupAccount> agaList = new
				 * ArrayList<AccountGroupAccount>(); agaList =
				 * accountGroupAccountManager.searchAccountGroupAccount(
				 * viewAccountGroup.getKey());
				 * 
				 * for (int i = 0; i < agrList.size(); i++) {
				 * list1.add(userManager.getUserAccountByAccountKey(agrList.get(
				 * i).getAccountKey())); }
				 * //model.addAttribute("responsibleAccountList", list1);
				 * 
				 * 
				 * for (int i = 0; i < agaList.size(); i++) {
				 * list2.add(userManager.getUserAccountByAccountKey(agaList.get(
				 * i).getAccountKey())); } //model.addAttribute("accountList",
				 * list2);
				 * 
				 */

				List<UserAccount> userAccountList = new ArrayList<UserAccount>();
				// userAccountList = userManager.getAllUserAccount();

				List<UserAccountRole> siteUserAccountRoleList = userManager.searchUserAccountRole(currRole.getSiteKey(),
						null, null, null, null);

				if (siteUserAccountRoleList != null) {
					for (UserAccountRole ar : siteUserAccountRoleList) {

						// do not add duplicate if any
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

				// Account Group Account
				List<AccountGroupAccount> viewAccountGroupAccount = new ArrayList<AccountGroupAccount>();
				viewAccountGroupAccount = accountGroupAccountManager.searchAccountGroupAccount(accountGroupKey);
				Set<Integer> selectedAccountSet = new HashSet<Integer>();
				for (int i = 0; i < viewAccountGroupAccount.size(); i++) {
					selectedAccountSet.add(viewAccountGroupAccount.get(i).getAccountKey());
				}

				// Account Group Responsible
				List<AccountGroupResponsible> viewAccountGroupResponsible = new ArrayList<AccountGroupResponsible>();
				viewAccountGroupResponsible = accountGroupResponsibleManager
						.searchAccountGroupResponsible(accountGroupKey);
				Set<Integer> selectedResponsibleAccountSet = new HashSet<Integer>();
				for (int i = 0; i < viewAccountGroupResponsible.size(); i++) {
					selectedResponsibleAccountSet.add(viewAccountGroupResponsible.get(i).getAccountKey());
				}

				AccountGroupForm accountGroupForm = new AccountGroupForm();
				accountGroupForm.setKey(viewAccountGroup.getKey());
				accountGroupForm.setSelectedAccountKeyList(selectedAccountSet);
				accountGroupForm.setSelectedResponsibleAccountKeyList(selectedResponsibleAccountSet);
				accountGroupForm.setName(viewAccountGroup.getName());
				accountGroupForm.setDesc(viewAccountGroup.getDesc());

				accountGroupForm.setUserAccountList(userAccountList);

				if ("0".equals(errorStr)) {
					accountGroupForm.setSuccess(true);
				}

				// view set readonly
				accountGroupForm.setReadOnly(true);
				accountGroupForm.setDelete(false);

				accountGroupForm.setReferrer(referrerStr);

				model.addAttribute("accountGroupForm", accountGroupForm);

				return "account/accountGroup_create_modify";

			} else {
				logger.debug("user does not have right to view AccountGroup information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/ModifyAccountGroup.do")
	public String showModifyAccountGroupForm(@ModelAttribute("accountGroupForm") AccountGroupForm accountGroupForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load modify account group page.");

		String referrerStr = request.getParameter("r");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {

			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.modifyAccountGroup"));

			if (hasPrivilege) {
				AccountGroup editAccountGroup = null;

				List<UserAccount> userAccountList = new ArrayList<UserAccount>();
				// userAccountList = userManager.getAllUserAccount();

				List<UserAccountRole> siteUserAccountRoleList = userManager.searchUserAccountRole(currRole.getSiteKey(),
						null, null, null, null);

				if (siteUserAccountRoleList != null) {
					for (UserAccountRole ar : siteUserAccountRoleList) {

						// do not add duplicate if any
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

				// model.addAttribute("accountList", userAccountList);
				// model.addAttribute("responsibleAccountList",
				// userAccountList);

				accountGroupForm.setUserAccountList(userAccountList);

				editAccountGroup = accountGroupManager.getAccountGroupByAccountGroupKey(accountGroupForm.getKey());

				if (editAccountGroup == null || currRole.getSiteKey() != editAccountGroup.getSiteKey())
					return "common/notfound";

				// Account Group Account
				List<AccountGroupAccount> editAccountGroupAccount = new ArrayList<AccountGroupAccount>();
				editAccountGroupAccount = accountGroupAccountManager
						.searchAccountGroupAccount(editAccountGroup.getKey());
				Set<Integer> selectedAccountSet = new HashSet<Integer>();
				for (int i = 0; i < editAccountGroupAccount.size(); i++) {
					selectedAccountSet.add(editAccountGroupAccount.get(i).getAccountKey());
				}

				// Account Group Responsible
				List<AccountGroupResponsible> editAccountGroupResponsible = new ArrayList<AccountGroupResponsible>();
				editAccountGroupResponsible = accountGroupResponsibleManager
						.searchAccountGroupResponsible(editAccountGroup.getKey());
				Set<Integer> selectedResponsibleAccountSet = new HashSet<Integer>();
				for (int i = 0; i < editAccountGroupResponsible.size(); i++) {
					selectedResponsibleAccountSet.add(editAccountGroupResponsible.get(i).getAccountKey());
				}

				accountGroupForm.setSelectedAccountKeyList(selectedAccountSet);
				accountGroupForm.setSelectedResponsibleAccountKeyList(selectedResponsibleAccountSet);
				accountGroupForm.setKey(editAccountGroup.getKey());
				accountGroupForm.setSiteKey(editAccountGroup.getSiteKey());
				accountGroupForm.setName(editAccountGroup.getName());
				accountGroupForm.setDesc(editAccountGroup.getDesc());
				accountGroupForm.setReadOnly(false);

				accountGroupForm.setReferrer(referrerStr);

				return "account/accountGroup_create_modify";
			} else {
				logger.debug("user does not have right to modify AccountGroup");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();

			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DoCreateModifyAccountGroup.do")
	public String doCreateModifyAccountGroup(@ModelAttribute("accountGroupForm") AccountGroupForm accountGroupForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("doCreateModifyAccountGroup()");

		String referrerStr = "";

		// save accountGroup
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.modifyAccountGroup"))
					|| currRole.getGrantedPrivilegeCodeList().contains(privilegeMap.get("privilege.code.createAccountGroup"));

			if (hasPrivilege) {
				AccountGroup accountGroup = null;
				List<AccountGroupAccount> selectedAccountList = new ArrayList<AccountGroupAccount>();
				List<AccountGroupResponsible> selectedResponsibleAccountList = new ArrayList<AccountGroupResponsible>();

				List<Integer> tempList = new ArrayList<Integer>();

				List<Integer> new_agaList = new ArrayList<Integer>();
				List<Integer> old_agaList = new ArrayList<Integer>();
				List<AccountGroupAccount> old_AGAList = new ArrayList<AccountGroupAccount>();

				List<Integer> new_agrList = new ArrayList<Integer>();
				List<Integer> old_agrList = new ArrayList<Integer>();
				List<AccountGroupResponsible> old_AGRList = new ArrayList<AccountGroupResponsible>();

				// validate
				accountGroupFormValidator.validate(accountGroupForm, result);

				if (result.hasErrors()) {
					logger.debug("Validation Failed");

					List<UserAccount> userAccountList = new ArrayList<UserAccount>();

					List<UserAccountRole> siteUserAccountRoleList = userManager
							.searchUserAccountRole(currRole.getSiteKey(), null, null, null, null);

					if (siteUserAccountRoleList != null) {
						for (UserAccountRole ar : siteUserAccountRoleList) {

							// do not add duplicate if any
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

					accountGroupForm.setUserAccountList(userAccountList);

					/*
					 * 
					 * accountGroupForm.setSelectedAccountKeyList(
					 * accountGroupForm.getSelectedAccountKeyList());
					 * accountGroupForm
					 * .setSelectedResponsibleAccountKeyList(accountGroupForm.
					 * getSelectedResponsibleAccountKeyList());
					 * accountGroupForm.setKey(accountGroupForm.getKey());
					 * accountGroupForm.setSiteKey(accountGroupForm.getSiteKey()
					 * ); accountGroupForm.setName(accountGroupForm.getName());
					 * accountGroupForm.setDesc(accountGroupForm.getDesc());
					 */
					accountGroupForm.setReadOnly(false);

					accountGroupForm.setSuccess(false);

					return "account/accountGroup_create_modify";
				}

				if (accountGroupForm.getKey() == null) {

					referrerStr = "c";

					if (currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.createAccountGroup"))) {
						// Create Account Group
						accountGroup = new AccountGroup();
						accountGroup.setName(accountGroupForm.getName());
						accountGroup.setDesc(accountGroupForm.getDesc());
						accountGroup.setSiteKey(currRole.getSiteKey());
						accountGroup.setCreatedBy(account.getKey());
						accountGroup.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
						accountGroup.setLastModifyBy(account.getKey());
						accountGroup.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
						accountGroup.setDeleted("N");

						// Create Account Group Account
						if (accountGroupForm.getSelectedAccountKeyList() != null) {

							new_agaList.addAll(accountGroupForm.getSelectedAccountKeyList());

							for (int i = 0; i < new_agaList.size(); i++) {
								AccountGroupAccount accountGroupAccount = new AccountGroupAccount();
								accountGroupAccount.setCreatedBy(account.getKey());
								accountGroupAccount.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupAccount.setLastModifyBy(account.getKey());
								accountGroupAccount.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupAccount.setDeleted("N");
								accountGroupAccount.setAccountKey(new_agaList.get(i));
								selectedAccountList.add(accountGroupAccount);
							}
						}

						// Create Account Group Responsible
						if (accountGroupForm.getSelectedResponsibleAccountKeyList() != null) {

							new_agrList.addAll(accountGroupForm.getSelectedResponsibleAccountKeyList());

							for (int i = 0; i < new_agrList.size(); i++) {
								AccountGroupResponsible accountGroupResponsible = new AccountGroupResponsible();
								accountGroupResponsible.setCreatedBy(account.getKey());
								accountGroupResponsible.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupResponsible.setLastModifyBy(account.getKey());
								accountGroupResponsible.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupResponsible.setDeleted("N");
								accountGroupResponsible.setAccountKey(new_agrList.get(i));
								selectedResponsibleAccountList.add(accountGroupResponsible);
							}
						}

					} else {
						logger.debug("user does not have right to create accountGroup");
						return "common/noprivilege";
					}
				} else {

					referrerStr = "m";

					// modify
					if (currRole.getGrantedPrivilegeCodeList()
							.contains(privilegeMap.get("privilege.code.modifyAccountGroup"))) {

						// Modify Account Group
						accountGroup = accountGroupManager.getAccountGroupByAccountGroupKey(accountGroupForm.getKey());

						if (accountGroup == null || currRole.getSiteKey() != accountGroup.getSiteKey()) {
							logger.debug("Role Key " + accountGroupForm.getKey() + " not found");
							return "common/notfound";
						}
						accountGroup.setName(accountGroupForm.getName());
						accountGroup.setDesc(accountGroupForm.getDesc());
						accountGroup.setLastModifyBy(account.getKey());
						accountGroup.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));

						// Create or Modify Account Group Account
						if (accountGroupForm.getSelectedAccountKeyList() != null)
							new_agaList.addAll(accountGroupForm.getSelectedAccountKeyList());

						old_AGAList = accountGroupAccountManager.searchAccountGroupAccount(accountGroupForm.getKey());

						for (int i = 0; i < old_AGAList.size(); i++) {
							old_agaList.add(old_AGAList.get(i).getAccountKey());
						}

						tempList = new ArrayList<Integer>();

						tempList.addAll(symmetricDifference(old_agaList, new_agaList));

						for (int i = 0; i < tempList.size(); i++) {
							AccountGroupAccount accountGroupAccount = null;
							accountGroupAccount = accountGroupAccountManager
									.getAccountGroupAccount(accountGroup.getKey(), tempList.get(i));

							if (accountGroupAccount == null) {
								// If it is null
								accountGroupAccount = new AccountGroupAccount();
								accountGroupAccount.setCreatedBy(account.getKey());
								accountGroupAccount.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupAccount.setLastModifyBy(account.getKey());
								accountGroupAccount.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupAccount.setDeleted("N");
								accountGroupAccount.setAccountKey(tempList.get(i));
								selectedAccountList.add(accountGroupAccount);
							} else {
								// If it is existed
								if (accountGroupAccount.getDeleted().equals("N")) {
									accountGroupAccount.setLastModifyBy(account.getKey());
									accountGroupAccount.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
									accountGroupAccount.setDeleted("Y");
								} else {
									accountGroupAccount.setLastModifyBy(account.getKey());
									accountGroupAccount.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
									accountGroupAccount.setDeleted("N");
								}
								selectedAccountList.add(accountGroupAccount);
							}
						}

						// Create or Modify Account Group Responsible
						new_agrList.addAll(accountGroupForm.getSelectedResponsibleAccountKeyList());

						old_AGRList = accountGroupResponsibleManager
								.searchAccountGroupResponsible(accountGroupForm.getKey());

						for (int i = 0; i < old_AGRList.size(); i++) {
							old_agrList.add(old_AGRList.get(i).getAccountKey());
						}

						tempList = new ArrayList<Integer>();

						tempList.addAll(symmetricDifference(old_agrList, new_agrList));

						logger.debug("tempList size: " + tempList.size());

						for (int i = 0; i < tempList.size(); i++) {
							AccountGroupResponsible accountGroupResponsible = null;
							accountGroupResponsible = accountGroupResponsibleManager
									.getAccountGroupResponsible(accountGroup.getKey(), tempList.get(i));

							if (accountGroupResponsible == null) {
								// If it is null
								accountGroupResponsible = new AccountGroupResponsible();
								accountGroupResponsible.setCreatedBy(account.getKey());
								accountGroupResponsible.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupResponsible.setLastModifyBy(account.getKey());
								accountGroupResponsible.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
								accountGroupResponsible.setDeleted("N");
								accountGroupResponsible.setAccountKey(tempList.get(i));
								selectedResponsibleAccountList.add(accountGroupResponsible);
							} else {
								// If it is existed
								if (accountGroupResponsible.getDeleted().equals("N")) {
									accountGroupResponsible.setLastModifyBy(account.getKey());
									accountGroupResponsible.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
									accountGroupResponsible.setDeleted("Y");
								} else {
									accountGroupResponsible.setLastModifyBy(account.getKey());
									accountGroupResponsible.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
									accountGroupResponsible.setDeleted("N");
								}
								selectedResponsibleAccountList.add(accountGroupResponsible);
							}
						}

						// End

					} else {
						logger.debug("user does not have right to modify accountGroup");
						return "common/noprivilege";
					}
				}

				try {

					int groupKey = accountGroupManager.saveAccountGroup(accountGroup);

					for (int i = 0; i < selectedAccountList.size(); i++) {
						selectedAccountList.get(i).setGroupKey(groupKey);
						accountGroupAccountManager.saveAccountGroupAccount(selectedAccountList.get(i));
					}

					for (int i = 0; i < selectedResponsibleAccountList.size(); i++) {
						selectedResponsibleAccountList.get(i).setGroupKey(groupKey);
						accountGroupResponsibleManager
								.saveAccountGroupResponsible(selectedResponsibleAccountList.get(i));
					}

				} catch (MFMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// return to view page on success
				logger.debug("accountGroup Key after: " + accountGroup.getKey());
				return "redirect:ViewAccountGroup.do?key=" + accountGroup.getKey() + "&error=0" + "&r=" + referrerStr;

			} else {

				// user does not have right to modify accountGroup information
				logger.debug("user does not have right to modify accountGroup");

				return "common/noprivilege";
			}

		} catch (Exception e) {

			e.printStackTrace();
			return "common/notfound";
		}

	}

	@RequestMapping(value = "/DeleteAccountGroup.do")
	public String showDeleteAccountGroupForm(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		String accountGroupKeyStr = request.getParameter("key");

		logger.debug("showDeleteAccountGroupForm()[" + accountGroupKeyStr + "]");

		Integer accountGroupKey = Integer.parseInt(accountGroupKeyStr);

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		try {

			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeAccountGroup"));

			if (hasPrivilege) {

				AccountGroup viewAccountGroup = null;
				viewAccountGroup = accountGroupManager.getAccountGroupByAccountGroupKey(accountGroupKey);

				if (viewAccountGroup == null || currRole.getSiteKey() != viewAccountGroup.getSiteKey()) {
					return "common/notfound";
				}

				List<UserAccount> list1 = new ArrayList<UserAccount>();
				List<UserAccount> list2 = new ArrayList<UserAccount>();

				List<AccountGroupResponsible> agrList = new ArrayList<AccountGroupResponsible>();
				agrList = accountGroupResponsibleManager.searchAccountGroupResponsible(viewAccountGroup.getKey());

				List<AccountGroupAccount> agaList = new ArrayList<AccountGroupAccount>();
				agaList = accountGroupAccountManager.searchAccountGroupAccount(viewAccountGroup.getKey());

				for (int i = 0; i < agrList.size(); i++) {
					list1.add(userManager.getUserAccountByAccountKey(agrList.get(i).getAccountKey()));
				}
				model.addAttribute("responsibleAccountList", list1);

				for (int i = 0; i < agaList.size(); i++) {
					list2.add(userManager.getUserAccountByAccountKey(agaList.get(i).getAccountKey()));
				}
				model.addAttribute("accountList", list2);

				// Account Group Account
				List<AccountGroupAccount> viewAccountGroupAccount = new ArrayList<AccountGroupAccount>();
				viewAccountGroupAccount = accountGroupAccountManager.searchAccountGroupAccount(accountGroupKey);
				Set<Integer> selectedAccountSet = new HashSet<Integer>();
				for (int i = 0; i < viewAccountGroupAccount.size(); i++) {
					selectedAccountSet.add(viewAccountGroupAccount.get(i).getAccountKey());
				}

				// Account Group Responsible
				List<AccountGroupResponsible> viewAccountGroupResponsible = new ArrayList<AccountGroupResponsible>();
				viewAccountGroupResponsible = accountGroupResponsibleManager
						.searchAccountGroupResponsible(accountGroupKey);
				Set<Integer> selectedResponsibleAccountSet = new HashSet<Integer>();
				for (int i = 0; i < viewAccountGroupResponsible.size(); i++) {
					selectedResponsibleAccountSet.add(viewAccountGroupResponsible.get(i).getAccountKey());
				}

				AccountGroupForm accountGroupForm = new AccountGroupForm();
				accountGroupForm.setKey(viewAccountGroup.getKey());

				accountGroupForm.setName(viewAccountGroup.getName());
				accountGroupForm.setDesc(viewAccountGroup.getDesc());

				accountGroupForm.setReadOnly(true);
				accountGroupForm.setDelete(true);

				model.addAttribute("accountGroupForm", accountGroupForm);

				return "account/accountGroup_create_modify";

			} else {
				logger.debug("user does not have right to delete AccountGroup information");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/DoDeleteAccountGroup.do")
	public String doDeleteAccountGroup(
			@ModelAttribute("searchAccountGroupForm") SearchAccountGroupForm searchAccountGroupForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String accountGroupKeyStr = request.getParameter("key");

		logger.debug("doDeleteAccountGroup()[" + accountGroupKeyStr + "]");

		Integer accountGroupKey;
		try {
			accountGroupKey = Integer.parseInt(accountGroupKeyStr);
		} catch (NumberFormatException e1) {

			e1.printStackTrace();

			return "common/notfound";
		}

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount account = (UserAccount) session.getAttribute("user");

		try {
			// check privilege
			boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
					.contains(privilegeMap.get("privilege.code.removeAccountGroup"));

			// check privilege
			if (hasPrivilege) {

				AccountGroup ag = accountGroupManager.getAccountGroupByAccountGroupKey(accountGroupKey);

				List<AccountGroupAccount> agaList = null;

				try {
					agaList = accountGroupAccountManager.searchAccountGroupAccount(accountGroupKey);
					

				} catch (MFMSException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (agaList != null && agaList.size() > 0) {
					// error
					logger.debug("Account Group " + ag.getName() + " still has Users");
					result.reject("accountGroup.delete.fail.has.user", new Object[] { ag.getName() }, null);

					searchAccountGroupForm.setDeleteSuccess(false);

					return "account/accountGroup_search";

				} else if (ag == null) {
					// error
					logger.debug("Account Group Key" + accountGroupKey + " not found");
					result.reject("accountGroup.delete.fail.not.found", null);

					searchAccountGroupForm.setDeleteSuccess(false);

					return "account/accountGroup_search";
				} else {

					try {

						// Delete Account Group
						logger.debug("Going to delete Account Group " + ag.getName());
						searchAccountGroupForm.setDeleteSuccess(true);
						searchAccountGroupForm.setDeletedName(ag.getName());
						accountGroupManager.deleteAccountGroupByKey(account.getKey(), accountGroupKey);

						// Delete Account Group Account
						/*
						 * List<AccountGroupAccount> agaList =
						 * accountGroupAccountManager
						 * .searchAccountGroupAccount(accountGroupKey);
						 * 
						 * for (int i = 0; i < agaList.size(); i++) {
						 * AccountGroupAccount aga = agaList.get(i);
						 * aga.setDeleted("Y");
						 * accountGroupAccountManager.saveAccountGroupAccount(
						 * aga); }
						 */

						// Delete Account Group Responsible

						List<AccountGroupResponsible> agrList = accountGroupResponsibleManager
								.searchAccountGroupResponsible(accountGroupKey);
						for (int i = 0; i < agrList.size(); i++) {
							AccountGroupResponsible agr = agrList.get(i);
							agr.setLastModifyBy(account.getKey());
							agr.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
							agr.setDeleted("Y");
							accountGroupResponsibleManager.saveAccountGroupResponsible(agr);
						}

					} catch (MFMSException e) {
						// TODO Redirect to not found
						return "common/notfound";
					}
					// model.addAttribute("accountGroupForm", new
					// AccountGroupForm());

					return "redirect:SearchAccountGroup.do?r=d&name=" + StrToUnicode(ag.getName());
				}
			} else {

				// user does not have right to delete accountGroup information
				logger.debug("user does not have right to delete accountGroup");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	@RequestMapping(value = "/SearchAccountGroup.do")
	public String doSearchAccountGroup(
			@ModelAttribute("searchAccountGroupForm") SearchAccountGroupForm searchAccountGroupForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		logger.debug("User requests to load search account group page.");

		String referrerStr = request.getParameter("r");

		String deleteName = "";
		if (request.getParameter("name") != null)
			deleteName = UnicodeToStr(request.getParameter("name"));

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.searchAccountGroup"));

		if (hasPrivilege) {

			try {

				// find userAccountGroupRole of this site

				List<AccountGroup> resultList = accountGroupManager.getAccountGroupBySiteKey(currRole.getSiteKey());
				
				searchAccountGroupForm.setSiteKey(currRole.getSiteKey());

				searchAccountGroupForm.setResultList(resultList);

				searchAccountGroupForm.setReferrerStr(referrerStr);

				searchAccountGroupForm.setDeletedName(deleteName);

				searchAccountGroupForm.setCanGen(true);

				Boolean canModify = currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.modifyAccountGroup"));

				Boolean canRemove = currRole.getGrantedPrivilegeCodeList()
						.contains(privilegeMap.get("privilege.code.removeAccountGroup"));

				searchAccountGroupForm.setCanModify(canModify);

				searchAccountGroupForm.setCanRemove(canRemove);

				// searchAccountGroupForm.setDeleteSuccess(false);

				return "account/accountGroup_search";

			} catch (MFMSException e) {

				return "common/notfound";

			}
		} else {
			// user does not have right to search accountGroup
			logger.debug("user does not have right to search accountGroup ");
			return "common/noprivilege";
		}

	}
	
	@RequestMapping(value = "ExportAccountGroup.do")
	public @ResponseBody void ExportAccountGroup(
			@ModelAttribute("searchAccountGroupForm") SearchAccountGroupForm searchAccountGroupForm,
			HttpServletRequest request, HttpServletResponse response, ModelMap model, Locale locale)
			throws MFMSException, Exception {
		logger.debug("ExportAccountGroup.do");
		
		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>(searchAccountGroupForm.getResultList());
		
			Calendar cal = Calendar.getInstance();

			String filePath = URLDecoder.decode(
				propertyConfigurer.getProperty("exportPath"), "UTF-8");
			String fileName = filePath + "AG_" + cal.getTimeInMillis()
				+ ".xlsx";
			File accountGroupListFile = new File(fileName);

			FileOutputStream accountGroupListStream = new FileOutputStream(accountGroupListFile,
				false);

			ExcelWriter accountGroupListWriter = new ExcelWriter(accountGroupListStream);

			// Header
			List<ColumnDisplay> columnDisplays = new ArrayList<ColumnDisplay>();

			ColumnDisplay groupNameColumn = new ColumnDisplay();
			groupNameColumn.setProperty("groupName");
			groupNameColumn.setHeadingKey(messageSource.getMessage("accountGroup.name",
					null, locale));
		
			ColumnDisplay descriptionColumn = new ColumnDisplay();
			descriptionColumn.setProperty("description");
			descriptionColumn.setHeadingKey(messageSource.getMessage("accountGroup.description",
					null, locale));
		
			ColumnDisplay responsibleAccountColumn = new ColumnDisplay();
			responsibleAccountColumn.setProperty("responsibleAccount");
			responsibleAccountColumn.setHeadingKey(messageSource.getMessage("account.account.responsible",
					null, locale));
		
			ColumnDisplay accountColumn = new ColumnDisplay();
			accountColumn.setProperty("account");
			accountColumn.setHeadingKey(messageSource.getMessage("account.account",
					null, locale));
		
			columnDisplays.add(groupNameColumn);
			columnDisplays.add(descriptionColumn);
			columnDisplays.add(responsibleAccountColumn);
			columnDisplays.add(accountColumn);
		
			// Row
			List<AccountGroupExport> exportList = new ArrayList<AccountGroupExport>();
			
			for(AccountGroup ag : accountGroupList){
				AccountGroupExport export = new AccountGroupExport();
				List<String> sortAGRName = new ArrayList<String>();
				List<String> sortAGAName = new ArrayList<String>();
				export.setGroupName(ag.getName());
				export.setDescription(ag.getDesc());
				
				if(ag.getAccountGroupResponsible().size()>0){
					for(int i=0; i<ag.getAccountGroupResponsible().size();i++){
						sortAGRName.add(ag.getAccountGroupResponsible().get(i).getAccount().getLoginId());
					}
					Collections.sort(sortAGRName, new StringComparator());
					
					String aGRName = "";
					for(String name : sortAGRName){
						aGRName+=name + ", ";
					}
					aGRName = aGRName.substring(0, aGRName.length()-2);
					export.setResponsibleAccount(aGRName);
				}else{
					export.setResponsibleAccount("");
				}
				
				if(ag.getAccountGroupAccount().size()>0){
					
					for(int i=0; i<ag.getAccountGroupAccount().size();i++){
						sortAGAName.add(ag.getAccountGroupAccount().get(i).getAccount().getLoginId());					
					}
					Collections.sort(sortAGAName, new StringComparator());
					
					String aGAName = "";
					for(String name : sortAGAName){
						aGAName += name + ", ";
					}
					aGAName = aGAName.substring(0, aGAName.length()-2);
					export.setAccount(aGAName);
				}else{
					export.setAccount("");
				}
				
				exportList.add(export);
			}
		
			accountGroupListWriter.write(true, 0, "AGA", null, exportList, columnDisplays,
					false);

			accountGroupListWriter.close();

			response.setHeader("Content-Type", "application/vnd.ms-excel");

			response.setHeader("Content-disposition", "inline;filename=\""
					+accountGroupListFile.getName() + "\"");

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
	
	

	@RequestMapping(value = "DoAccountGroupDataTable.do", method = RequestMethod.GET)
	public @ResponseBody void doGet(
			@ModelAttribute("searchAccountGroupForm") SearchAccountGroupForm searchAccountGroupForm,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ClassNotFoundException, SQLException, MFMSException, JSONException {

		String[] columnNames = { "action", "name", "desc", };

		int fullListSize = 0;
		Integer pageNo = Integer.parseInt(request.getParameter("iDisplayStart"));
		String colIndex = request.getParameter("iSortCol_0") == null ? "0" : request.getParameter("iSortCol_0");
		String sortDirection = request.getParameter("sSortDir_0") == null ? "asc" : request.getParameter("sSortDir_0");
		String colName = columnNames[Integer.parseInt(colIndex)];

		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");

		Role currRole = (Role) session.getAttribute("currRole");

		List<AccountGroup> fullList = new ArrayList<AccountGroup>();

		List<AccountGroup> accountGroupList = new ArrayList<AccountGroup>();

		if (searchAccountGroupForm.getCanGen()) {

			fullList = searchAccountGroupForm.getResultList();

			switch (colName) {

			case "action":

				Collections.sort(fullList, new GNameComparator());

				break;

			case "name":

				Collections.sort(fullList, new GNameComparator());

				break;

			case "desc":
				Collections.sort(fullList, new GDescComparator());

				break;
			}

			if (!sortDirection.equals("asc"))
				Collections.reverse(fullList);

			for (int i = pageNo; i < pageNo + 10; i++) {
				if (i < fullList.size())
					accountGroupList.add(fullList.get(i));
			}
			searchAccountGroupForm.setResultList(fullList);
			fullListSize = searchAccountGroupForm.getResultList().size();

		}

		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();

		for (int i = 0; i < accountGroupList.size(); i++) {

			JSONArray ja = new JSONArray();
			AccountGroup accountGroup = accountGroupList.get(i);

			StringBuilder myvar = new StringBuilder();

			boolean canModify = searchAccountGroupForm.getCanModify();
			boolean canRemove = searchAccountGroupForm.getCanRemove();

			myvar.append("<a onclick=\"showLoading()\"")
				.append(" href=\"ViewAccountGroup.do?key=" + accountGroup.getKey() + "&r=s\"><i")
				.append(" class=\"fa fa-search\"></i></a>&nbsp;&nbsp;");

			if (canModify) {

				myvar.append("<a onclick=\"showLoading()\"")
						.append(" href=\"ModifyAccountGroup.do?key=" + accountGroup.getKey() + "&r=s\"><i")
						.append(" class=\"fa fa-pencil\"></i></a>&nbsp;&nbsp;");

			}

			if (canRemove) {

				myvar.append("<i onclick=\"showDelete(" + accountGroup.getKey() + ", '"
						+ Utility.replaceHtmlEntities(accountGroup.getName()) + "');\" class=\"fa fa-trash-o\"></i>");

			}

			ja.put(myvar.toString());
			ja.put(Utility.replaceHtmlEntities(accountGroup.getName()));
			ja.put(Utility.replaceHtmlEntities(accountGroup.getDesc()));
			array.put(ja);

		}

		result.put("iTotalDisplayRecords", fullListSize);
		result.put("aaData", array);

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);

	}

	@RequestMapping(value = "/SetLocationPrivilege.do")
	public String showLocationPrivilegeForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load location privilege page.");
		
		String loginId = request.getParameter("loginId");

		String errorStr = request.getParameter("error");

		logger.debug("showLocationPrivilegeForm()[" + loginId + "]");

		HttpSession session = request.getSession();

		Role currRole = (Role) session.getAttribute("currRole");
		if (null==currRole) {
			return "main/site_select";
		}

		UserAccount currUser = (UserAccount) session.getAttribute("user");

		boolean isSysAdmin = (boolean) session.getAttribute("isSysAdmin");

		// TODO check privilege
		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList()
				.contains(privilegeMap.get("privilege.code.setLocationPrivilage"));

		if (hasPrivilege) {
			try {
				UserAccount account = userManager.getUserAccountByLoginId(loginId, true);

				LocationPrivilegeForm locationPrivilegeForm = new LocationPrivilegeForm();
				locationPrivilegeForm.setLoginId(account.getLoginId());
				locationPrivilegeForm.setName(account.getName());

				LocationTreeNode locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(),
						account.getKey(), false);

				locationPrivilegeForm.setAvailableLocationTree(locationTree);

				// logger.debug("availableLocations.size = " +
				// availableLocations.size());

				// List<LocationPrivilege> existingLocationPrivilegeList =
				// locationManager.getLocationPrivileges(currRole.getSiteKey(),
				// account.getKey());
				// logger.debug("availableLocations.size = " +
				// availableLocations.size());

				List<Integer> selectedLocationKeyList = locationManager
						.getPrivilegedLocationKeyList(currRole.getSiteKey(), account.getKey());
				;

				if (selectedLocationKeyList == null) {
					selectedLocationKeyList = new ArrayList<Integer>();
				}

				logger.debug("selectedLocationKeyList.size = " + selectedLocationKeyList.size());
				locationPrivilegeForm.setSelectedLocationKeyList(selectedLocationKeyList);

				if ("0".equals(errorStr)) {
					locationPrivilegeForm.setSuccess(true);
				}

				model.addAttribute("locationPrivilegeForm", locationPrivilegeForm);

				return "account/account_location_privilege";
			} catch (MFMSException e) {
				return "common/notfound";
			}
		} else {
			// user does not have right to set Location Privilege
			logger.debug("user does not have right to setLocationPrivilege");
			return "common/noprivilege";
		}

	}

	@RequestMapping(value = "/DoSetLocationPrivilege.do")
	public String doSetLocationPrivilege(
			@ModelAttribute("locationPrivilegeForm") LocationPrivilegeForm locationPrivilegeForm, BindingResult result,
			HttpServletRequest request, HttpServletResponse response, ModelMap model) {

		String loginId = request.getParameter("loginId");

		logger.debug("doSetLocationPrivilege()[" + loginId + "]");

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
					.contains(privilegeMap.get("privilege.code.setLocationPrivilage"));

			// check privilege
			if (hasPrivilege) {

				try {

					UserAccount account = userManager.getUserAccountByLoginId(loginId, false);

					List<Integer> originalSelectedLocationKey = locationManager
							.getPrivilegedLocationKeyList(currRole.getSiteKey(), account.getKey());

					List<Integer> selectedLocationKeyList = locationPrivilegeForm.getSelectedLocationKeyList();

					if (selectedLocationKeyList == null)
						selectedLocationKeyList = new ArrayList<Integer>();

					if (originalSelectedLocationKey != null) {

						// check for delete
						for (Integer k : originalSelectedLocationKey) {

							if (selectedLocationKeyList.contains(k)) {
								// no need to do for k
								int i = selectedLocationKeyList.indexOf(k);
								selectedLocationKeyList.remove(i);

							} else {
								// delete
								LocationPrivilege lp = locationManager.getLocationPrivilege(k, account.getKey());

								if (null!=lp) {
									lp.setDeleted("Y");
									lp.setLastModifyBy(currUser.getKey());
									lp.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
									locationManager.saveLocationPrivilege(lp);
								}
							}

						}

					}

					if (locationPrivilegeForm.getSelectedLocationKeyList() != null) {

						// remaining not in original list;

						for (Integer k : selectedLocationKeyList) {

							LocationPrivilege lp = new LocationPrivilege();

							lp.setLocationKey(k);
							lp.setAccountKey(account.getKey());

							lp.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
							lp.setCreatedBy(currUser.getKey());

							lp.setDeleted("N");

							locationManager.saveLocationPrivilege(lp);
						}
					}

				} catch (MFMSException e) {
					// TODO Redirect to not found
					e.printStackTrace();
					return "common/notfound";
				}

				return "redirect:SetLocationPrivilege.do?loginId=" + loginId + "&error=0";
			} else {

				// user does not have right to set location privilege
				logger.debug("user does not have right to set location privilege");
				return "common/noprivilege";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "common/notfound";
		}
	}

	public UserAccountManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserAccountManager userManager) {
		this.userManager = userManager;
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

	public UserAccountFormValidator getUserAccountFormValidator() {
		return userAccountFormValidator;
	}

	public void setUserAccountFormValidator(UserAccountFormValidator userAccountFormValidator) {
		this.userAccountFormValidator = userAccountFormValidator;
	}

	public AccountGroupManager getAccountGroupManager() {
		return accountGroupManager;
	}

	public void setAccountGroupManager(AccountGroupManager accountGroupManager) {
		this.accountGroupManager = accountGroupManager;
	}

	public AccountGroupAccountManager getAccountGroupAccountManager() {
		return accountGroupAccountManager;
	}

	public void setAccountGroupAccountManager(AccountGroupAccountManager accountGroupAccountManager) {
		this.accountGroupAccountManager = accountGroupAccountManager;
	}

	private Set<Integer> symmetricDifference(List<Integer> list1, List<Integer> list2) {

		Set<Integer> a = new HashSet<Integer>();
		Set<Integer> b = new HashSet<Integer>();

		a.addAll(list1);
		b.addAll(list2);

		Set<Integer> result = new HashSet<Integer>(a);
		for (Integer element : b) {
			// .add() returns false if element already exists
			if (!result.add(element)) {
				result.remove(element);
			}
		}
		return result;
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

	public class ALoginIdComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getUserAccount().getLoginId();
			String str2 = nd.getUserAccount().getLoginId();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ANameComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getUserAccount().getName();
			String str2 = nd.getUserAccount().getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ARoleComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getRole().getName();
			String str2 = nd.getRole().getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ASiteComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getRole().getSite().getName();
			String str2 = nd.getRole().getSite().getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class AMailComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getUserAccount().getEmail() == null ? "" : st.getUserAccount().getEmail();
			String str2 = nd.getUserAccount().getEmail() == null ? "" : nd.getUserAccount().getEmail();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class ANumberComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getUserAccount().getContactNumber();
			String str2 = nd.getUserAccount().getContactNumber();
			if (str1 != null && str2 != null) {

				str1 = str1.toLowerCase();
				str2 = str2.toLowerCase();
				return str1.compareTo(str2);
			} else
				return 0;

		}
	}

	public class AStatusComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {

			String str1 = st.getUserAccount().getStatus();
			String str2 = nd.getUserAccount().getStatus();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class GNameComparator implements Comparator<AccountGroup> {
		@Override
		public int compare(AccountGroup st, AccountGroup nd) {

			String str1 = st.getName();
			String str2 = nd.getName();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
		}
	}

	public class GDescComparator implements Comparator<AccountGroup> {
		@Override
		public int compare(AccountGroup st, AccountGroup nd) {

			String str1 = st.getDesc();
			String str2 = nd.getDesc();

			str1 = str1.toLowerCase();
			str2 = str2.toLowerCase();

			return str1.compareTo(str2);
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
	
	static class StringComparator implements Comparator<String> {
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
	            // upper case
	            return 2 * (c - 64); // A = 2, B = 4 ...
	        } else if (c > 96) {
	            // lower case
	        	int kk = (2 * (c - 96)) - 1;
	            return kk; // a = 1, b = 3 ...
	        } else {
	            // between Z and a: [, /, ], ^, _, `
	            return c; // higher than the value for 'z'
	        }
	    }
	}

}
