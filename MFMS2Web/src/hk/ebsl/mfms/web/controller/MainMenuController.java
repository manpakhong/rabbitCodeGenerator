package hk.ebsl.mfms.web.controller;

import hk.ebsl.mfms.dto.AccountActionLog;
import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectEmail;
import hk.ebsl.mfms.dto.FailureClass;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.email.EmailTemplate;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.EmailManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.LocationTreeNode;
import hk.ebsl.mfms.notification.impl.InformEscalatorImpl;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj.NotificationMessage;
import hk.ebsl.mfms.notification.object.InformEscalatorPostObj.NotificationRecipient;
import hk.ebsl.mfms.web.controller.DefectController.FailureClassComparator;
import hk.ebsl.mfms.web.form.CauseCodeForm;
import hk.ebsl.mfms.web.form.ForgetPasswordForm;
import hk.ebsl.mfms.web.form.SiteSelectionForm;
import hk.ebsl.mfms.web.form.validator.ForgetPasswordFormValidator;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainMenuController {

	public final static Logger logger = Logger.getLogger(MainMenuController.class);

	@Autowired
	private EmailManager emailManager;

	@Autowired
	private UserAccountManager userManager;

	@Autowired
	private RoleManager roleManager;

	@Autowired
	private LocationManager locationManager;

	@Autowired
	private AccountActionLogManager accountActionLogManager;

	@Autowired
	private ForgetPasswordFormValidator forgetPasswordFormValidator;

	@RequestMapping(value = "/Home.do")
	public String showHome(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {

		logger.debug("User requests to load home page.");
		
		String roleKeyStr = request.getParameter("roleKey");
		logger.debug("showHome()[" + roleKeyStr + "]");
		// get roles
		HttpSession session = request.getSession();
		UserAccount account = (UserAccount) session.getAttribute("user");
		
		if (null!=account) {
			logger.debug("LoginId: " + account.getLoginId());
	
			logger.debug("getAccountRoles: " + account.getAccountRoles().get(0).getRoleKey());
			List<UserAccountRole> accountRoleList = account.getAccountRoles();
	
			boolean roleFound = false;
	
			Integer roleKey = null;
	
			try {
				roleKey = Integer.parseInt(request.getParameter("roleKey"));
			} catch (NumberFormatException e) {
				// no roleKey param found
			}
	
			if (roleKey != null) {
				for (UserAccountRole ar : accountRoleList) {
					if (ar.getRoleKey().equals(roleKey)) {
						logger.debug("Role Key found.");
						Role role = null;
						try {
							role = roleManager.getRoleByKey(roleKey, true);
	
							if ("Y".equals(role.getDeleted()) || "Y".equals(role.getSite().getDeleted())) {
								// role represented by role key is already deleted
								throw new MFMSException("Role/Site deleted");
							}
							// Role role = ar.getRole();
							session.setAttribute("currRole", role);
							roleFound = true;
	
							// Account Action Log
							AccountActionLog accountActionLog = new AccountActionLog();
							accountActionLog.setActionType("Logon");
	
							accountActionLog.setAccountId(account.getLoginId());
	
							accountActionLog.setResult("Success");
							accountActionLog.setSiteKey(role.getSiteKey());
							accountActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
							accountActionLogManager.saveAccountActionLog(accountActionLog);
							// End Action Action Log
	
							/*
							 * logger.debug("Privileges:"); for (RolePrivilege rp :
							 * role.getRolePrivileges()) {
							 * logger.debug(rp.getPrivilegeCode()); }
							 */
							break;
						} catch (MFMSException e) {
							// TODO Auto-generated catch block
	
							logger.error("Failed to set role");
							e.printStackTrace();
							return "common/notfound";
						}
					}
				}
			}
			Role currRole = (Role) session.getAttribute("currRole");
			if (null==currRole) {
				return "main/site_select";
			}
			if (roleFound || currRole != null) {
				// role found by roleKey or there is role in session
	
				LocationTreeNode locationTree = (LocationTreeNode) session.getAttribute("locationTree");
	
				if (locationTree == null || locationTree.getLocation().getSiteKey() != currRole.getSiteKey()) {
	
					logger.debug("build new locationtree");
					try {
						locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(), account.getKey(), true, true);
	
						//session.setAttribute("locationTree", locationTree);
	
					} catch (MFMSException e) {
	
						logger.error("Failed to build location tree");
	
						e.printStackTrace();
						return "common/notfound";
					}
				} else {
					// update tree
	
					logger.debug("update locationtree with updated privilege");
					try {
//						List<Integer> privilegedLocationKeyList = locationManager
//								.getPrivilegedLocationKeyList(currRole.getSiteKey(), account.getKey());
//	
//						locationTree.build(privilegedLocationKeyList);
//						locationTree.trim();
	
						locationTree = locationManager.buildLocationTree(request, currRole.getSiteKey(), account.getKey(), true, true);
						
						//session.setAttribute("locationTree", locationTree);
						logger.debug("finished update locationtree with updated privilege");
					} catch (MFMSException e) {
						logger.error("Failed to rebuild location tree");
					}
				}
	
				return "main/home";
			} else {
				return "common/notfound";
			}
		} else {
			logger.debug("Session timed out");
			response.setStatus(Response.SC_OK);
			response.sendRedirect("Login.do");
			
			return null;
		}
	}

	@RequestMapping(value = "/Login.do")
	public String showLogin(ModelMap model) {

		logger.debug("User requests to load user logon page.");
		
		return "logonmgt/login";

	}

	@RequestMapping(value = "/About.do")
	public String showAbout(ModelMap model) {

		logger.debug("User requests to load about page.");
		
		return "common/about";

	}

	@RequestMapping(value = "/ForgetPassword.do")
	public String ForgetPassword(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		logger.debug("User requests to load forget password page.");

		String referrerStr = request.getParameter("r");

		ForgetPasswordForm forgetPasswordForm = new ForgetPasswordForm();

		forgetPasswordForm.setReferrer(referrerStr);

		model.addAttribute("forgetPasswordForm", forgetPasswordForm);

		return "logonmgt/forgetPassword";

	}

	@RequestMapping(value = "/DoForgetPassword.do")
	public String DoForgetPassword(@ModelAttribute("forgetPasswordForm") ForgetPasswordForm forgetPasswordForm,
			BindingResult result, HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws MFMSException {

		forgetPasswordFormValidator.validate(forgetPasswordForm, result);

		if (result.hasErrors()) {
			logger.debug("Validation Failed");
			return "logonmgt/forgetPassword";
		}

		emailForgetPassword(forgetPasswordForm.getLoginId(), forgetPasswordForm.getEmail());

		return "redirect:ForgetPassword.do?&r=c";

	}
	
	

	// Forget Password
	public void emailForgetPassword(String username, String email) throws MFMSException {

		String templateId = "forgetPasswordScheduleSub";

		String newPassword = getRandomString(8);
		String salt = BCrypt.gensalt();
		String hashed_password = BCrypt.hashpw(newPassword, salt);
		
		UserAccount acc = userManager.getUserAccountByLoginId(username, false);
		acc.setPassword(hashed_password);
		userManager.saveUserAccount(acc);

		Object[] param = new Object[] { username, newPassword };

		emailManager.sendTemplateAsync(templateId, 1, new String[] { email }, param);

	}

	public String getRandomString(Integer len) {

		String str = "";

		Random r = new Random();

		String alphabet = "123xyz";
		for (int i = 0; i < len; i++) {
			str += alphabet.charAt(r.nextInt(alphabet.length()));
		}
		return str;

	}

	public class UserAccountRoleComparator implements Comparator<UserAccountRole> {
		@Override
		public int compare(UserAccountRole st, UserAccountRole nd) {
			return st.getRole().getSite().getName().compareTo(nd.getRole().getSite().getName());
		}
	}

	@RequestMapping(value = "/SiteSelect.do")
	public String showSiteSelection(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {

		logger.debug("User requests to load site selection page.");
		
		// model.addAttribute("currRole", null);
		HttpSession session = request.getSession();
		session.setAttribute("currRole", null);

		UserAccount account = (UserAccount) session.getAttribute("user");
		if (null==account) {
			logger.debug("Session timed out");
			response.setStatus(Response.SC_OK);
			response.sendRedirect("Login.do");
			
			return "logonmgt/login";
		}

		SiteSelectionForm form = new SiteSelectionForm();

		List<UserAccountRole> roleList = new ArrayList<UserAccountRole>();

		for (UserAccountRole ar : account.getAccountRoles()) {
			if ("Y".equals(ar.getDeleted()) || "Y".equals(ar.getRole().getDeleted())
					|| "Y".equals(ar.getRole().getSite().getDeleted())) {
				// remove deleted roles or roles of deleted sites
			} else {
				roleList.add(ar);
			}
		}

		Collections.sort(roleList, new UserAccountRoleComparator());

		// form.setAccountRoleList(account.getAccountRoles());
		form.setAccountRoleList(roleList);

		session.setAttribute("siteSelectionForm", form);
		return "main/site_select";

	}
	
	@RequestMapping(value = "/PunchCard.do")
	public String showPunchCard(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws IOException {

		logger.debug("User requests to load punch card page.");
		//still not complete

		return "main/site_select";

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

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public AccountActionLogManager getAccountActionLogManager() {
		return accountActionLogManager;
	}

	public void setAccountActionLogManager(AccountActionLogManager accountActionLogManager) {
		this.accountActionLogManager = accountActionLogManager;
	}

	public ForgetPasswordFormValidator getForgetPasswordFormValidator() {
		return forgetPasswordFormValidator;
	}

	public void setForgetPasswordFormValidator(ForgetPasswordFormValidator forgetPasswordFormValidator) {
		this.forgetPasswordFormValidator = forgetPasswordFormValidator;
	}
}
