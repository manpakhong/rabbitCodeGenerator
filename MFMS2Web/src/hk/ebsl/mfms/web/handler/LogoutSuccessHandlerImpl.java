package hk.ebsl.mfms.web.handler;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

	private final static Logger logger = Logger.getLogger(LogoutSuccessHandlerImpl.class);
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException,
			ServletException {
		if (auth != null) {
			logger.debug("User logged out: " + auth.getName());
			response.setStatus(Response.SC_OK);
			response.sendRedirect("Login.do");
		} else {
			logger.debug("Session timed out");
			response.setStatus(Response.SC_OK);
			response.sendRedirect("Login.do");
		}
		
	}

}
