package hk.ebsl.mfms.web.handler;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import hk.ebsl.mfms.dto.AttendanceConfig;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.AttendanceConfigManager;
import hk.ebsl.mfms.manager.PunchCardManager;
import hk.ebsl.mfms.manager.UserAccountManager;

/**
 * 
 * Handle default page after login base on user's roles
 *
 */
public class AuthenticationSuccessHandlerImpl implements
		AuthenticationSuccessHandler {
	private final static Logger logger = Logger.getLogger(AuthenticationSuccessHandlerImpl.class);
	
	@Autowired
	private AccountActionLogManager accountActionLogManager;


	
	@Autowired
	private AttendanceConfigManager attendanceConfigManager;
	
	private Integer sysAdminRoleKey = 1; // default = 1
	
	private UserAccountManager userManager;
	
	public AttendanceConfigManager getAttendanceConfigManager() {
		return attendanceConfigManager;
	}

	public void setAttendanceConfigManager(AttendanceConfigManager attendanceConfigManager) {
		this.attendanceConfigManager = attendanceConfigManager;
	}

	@Resource
	private Properties propertyConfigurer;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
		
		// TODO redirect according to role
		UserAccount account = null;
		if (auth != null) {
			
			logger.debug("User logged in: " + auth.getName());
			
			boolean hasSysAdminRole = false;
			
			try {
				// reset logon attempt count
				userManager.resetLogonAttemptCount(auth.getName());
				
				// fetch account roles
				account = userManager.getUserAccountByLoginId(auth.getName(), true);
				
				logger.debug("Name: " + account.getName());
				
				List<UserAccountRole> accountRoles = account.getAccountRoles();
				
				Role sysadminRole = null;
				
				for (UserAccountRole ar : accountRoles) {
					logger.debug("Role: " + ar.getRole().getName());
					if (ar.getRoleKey() == sysAdminRoleKey) {
						hasSysAdminRole = true;
						sysadminRole = ar.getRole();
					}
				}
				
				// put account to session
				HttpSession session = request.getSession();
				session.setAttribute("user", account);
				session.setAttribute("isSysAdmin", hasSysAdminRole);
				// put sysadminRole
				if (hasSysAdminRole) {
					session.setAttribute("sysAdminRole", sysadminRole);
				}
				
			} catch (MFMSException e) {
				
				e.printStackTrace();
			}
			
			if (hasSysAdminRole) {
				logger.debug("User has sys admin role, go to sysadmin page");
				// to sys admin page which could create sites
				response.setStatus(Response.SC_OK);
				response.sendRedirect("Sysadmin.do");
			} else {
				logger.debug("User doesn't have sys admin role, go to site selection page");
				response.setStatus(Response.SC_OK);
				
				if ("Y".equals(propertyConfigurer.getProperty("punchCard.enable"))){
					if (hasWebAttendanceFunction(account.getKey())) {
						response.sendRedirect("PunchCardManagement.do");
					} else {
						response.sendRedirect("SiteSelect.do");
					}
				}else {
					response.sendRedirect("SiteSelect.do");
				}

			}
		}

	}

	private boolean hasWebAttendanceFunction(Integer accountKey) {
		boolean hasWebAttendanceFunction = false;
		try {

			List<AttendanceConfig> attendanceConfigList = attendanceConfigManager.getAttendanceConfigListByAccountKey(accountKey);
			if (attendanceConfigList != null) {
				for (AttendanceConfig attendanceConfig : attendanceConfigList) {
					String code = attendanceConfig.getCode();
					if (code.equals(AttendanceConfig.CODE_FUNC_WCI) ||
							code.equals(AttendanceConfig.CODE_FUNC_WCO) ||	
							code.equals(AttendanceConfig.CODE_FUNC_WLI) ||	
							code.equals(AttendanceConfig.CODE_FUNC_WLO) ||	
							code.equals(AttendanceConfig.CODE_FUNC_WOI) ||
							code.equals(AttendanceConfig.CODE_FUNC_WOO)
							) {
						hasWebAttendanceFunction = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.debug(this.getClass().getName() + ".hasWebAttendanceFunction() - accountKey=" + accountKey);
		}
		return hasWebAttendanceFunction;
	}
	public Integer getSysAdminRoleKey() {
		return sysAdminRoleKey;
	}

	public void setSysAdminRoleKey(Integer sysAdminRoleKey) {
		this.sysAdminRoleKey = sysAdminRoleKey;
	}

	public UserAccountManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserAccountManager userManager) {
		this.userManager = userManager;
	}
	
	public AccountActionLogManager getAccountActionLogManager() {
		return accountActionLogManager;
	}

	public void setAccountActionLogManager(AccountActionLogManager accountActionLogManager) {
		this.accountActionLogManager = accountActionLogManager;
	}

}
