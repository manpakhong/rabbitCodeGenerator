package hk.ebsl.mfms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdministrationController {

	static class ModelMappingValue {
		public static final String pages_adminMenu = "admin/admin_menu";
		public static final String pages_adminCommonMenu = "admin/adminCommon_menu";
		public static final String pages_adminDefectMenu = "admin/adminDefect_menu";
	}

	public final static Logger logger = Logger
			.getLogger(AdministrationController.class);

	// function with RequestMapping

	/**                           **/
	/** Functions for Administration Menu **/
	/**                           **/
	@RequestMapping(value = "/AdministrationManagement.do")
	public String showAdministrationManagementMenu(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load administration management page.");
		
		return ModelMappingValue.pages_adminMenu;
	}

	/**                           **/
	/** Functions for Administration Common Menu **/
	/**                           **/
	@RequestMapping(value = "/AdministrationCommon.do")
	public String showAdministratioCommonMenu(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		return ModelMappingValue.pages_adminCommonMenu;
	}

	/**                           **/
	/** Functions for Administration Menu **/
	/**                           **/
	@RequestMapping(value = "/AdministrationDefect.do")
	public String showAdministrationDefect(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("User requests to load administration defect page.");
		
		return ModelMappingValue.pages_adminDefectMenu;
	}
}
