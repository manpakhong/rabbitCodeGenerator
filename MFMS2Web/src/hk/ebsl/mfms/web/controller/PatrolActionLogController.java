package hk.ebsl.mfms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PatrolActionLogController {

	public final static Logger logger = Logger.getLogger(PatrolActionLogController.class);

	
	static class ModelMappingValue {
		public static final String pages_patrolActionLog = "patrol/actionLog/patrolActionLog";
	}
	
	@RequestMapping(value="/PatrolActionLog.do")
	public String PatrolActionLog(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {
		
		return ModelMappingValue.pages_patrolActionLog;
	}
	
}
