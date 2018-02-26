package hk.ebsl.mfms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.web.controller.PatrolController.ModelMappingValue;
@Controller
public class PunchCardController {
	public final static Logger logger = Logger.getLogger(PunchCardController.class);
	/**                           **/
	/** Functions for Patrol Menu **/
	/**                           **/
	@RequestMapping(value = "/PunchCardManagement.do")
	public String showPatrolMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		logger.debug("User requests to load patrol management page.");
		Role currRole = (Role) request.getSession().getAttribute("currRole");
//		if (null==currRole) {
//			return "main/site_select";
//		}

		return ModelMappingValue.pages_punchcardMenu;
	}
}
