package hk.ebsl.mfms.web.handler;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.UserAccountManager;

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

/**
 * 
 * Handle default page after login base on user's roles
 *
 */
public class AuthenticationSuccessHandlerImpl implements
		AuthenticationSuccessHandler {
	
	@Autowired
	private AccountActionLogManager accountActionLogManager;

	private final static Logger logger = Logger.getLogger(AuthenticationSuccessHandlerImpl.class);
	
	private Integer sysAdminRoleKey = 1; // default = 1
	
	private UserAccountManager userManager;
	
	@Resource
	private Properties propertyConfigurer;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
		
		// TODO redirect according to role
		
		if (auth != null) {
			
			logger.debug("User logged in: " + auth.getName());
			
			boolean hasSysAdminRole = false;
			
			try {
				// reset logon attempt count
				userManager.resetLogonAttemptCount(auth.getName());
				
				// fetch account roles
				UserAccount account = userManager.getUserAccountByLoginId(auth.getName(), true);
				
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
					response.sendRedirect("PunchCardManagement.do");
				}else {
					response.sendRedirect("SiteSelect.do");
				}

			}
		}

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
