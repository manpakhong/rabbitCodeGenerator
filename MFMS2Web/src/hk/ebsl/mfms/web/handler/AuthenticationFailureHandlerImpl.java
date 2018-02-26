package hk.ebsl.mfms.web.handler;

import hk.ebsl.mfms.dto.AccountActionLog;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountActionLogManager;
import hk.ebsl.mfms.manager.UserAccountManager;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

	private final static Logger logger = Logger.getLogger(AuthenticationFailureHandlerImpl.class);
	@Autowired
	private UserAccountManager userManager;

	@Autowired
	private AccountActionLogManager accountActionLogManager;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException ae) throws IOException, ServletException {

		logger.debug("Login failed for user: " + request.getParameter("j_username"));

		String userName = request.getParameter("j_username");

		// Account Action Log
		try {
			if (!userName.isEmpty() && userManager.getUserAccountByLoginId(userName, false) != null) {
				AccountActionLog accountActionLog = new AccountActionLog();
				accountActionLog.setActionType("Logon");
				accountActionLog.setAccountId(request.getParameter("j_username"));
				accountActionLog.setResult("Invalid Password");
				accountActionLog.setSiteKey(-1);
				accountActionLog.setActionDateTime(new Timestamp(System.currentTimeMillis()));
				accountActionLogManager.saveAccountActionLog(accountActionLog);
			}
		} catch (MFMSException e1) { 
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		// End Action Action Log

		response.setStatus(Response.SC_OK);

		if (ae instanceof DisabledException) {
			response.sendRedirect("Login.do?error=3");
		} else if (ae instanceof LockedException) {
			response.sendRedirect("Login.do?error=2");
		} else {

			try {

				userManager.incrementLogonAttemptCount(userName);

				UserAccount ac = userManager.getUserAccountByLoginId(userName, false);

				if (ac != null && ac.getLogonAttemptCount() == 3 && ac.getStatus().equals("A"))
					response.sendRedirect("Login.do?error=4");
				else
					response.sendRedirect("Login.do?error=1");
			} catch (MFMSException e) {

				e.printStackTrace();
			}

		}

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
