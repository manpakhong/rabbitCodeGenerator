package hk.ebsl.mfms.web.view;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.view.InternalResourceView;

/*
 * Custom View class for template
 */

public class JstlView extends InternalResourceView {
	
	private final static Logger logger = Logger.getLogger(JstlView.class);

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		// Determine the path for the request dispatcher.
		String dispatcherPath = prepareForRendering(request, response);
		
		// set original view being asked for as a request parameter
		request.setAttribute("content", dispatcherPath);
				
		// forward attributes
		for (Map.Entry<String, Object> e : model.entrySet()) {
			
			logger.debug(e.getKey() + " = " + e.getValue());
			request.setAttribute(e.getKey(), e.getValue());
			
		}
		
		// force UTF-8
		response.setCharacterEncoding("UTF-8");
		// force everything to be template.jsp
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/pages/common/template.jsp");
		requestDispatcher.include(request, response);
		
	}
}
