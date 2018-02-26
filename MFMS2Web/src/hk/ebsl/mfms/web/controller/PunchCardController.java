package hk.ebsl.mfms.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import hk.ebsl.mfms.dao.PunchCardDao;
import hk.ebsl.mfms.dto.PunchCard;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.PunchCardManager;
import hk.ebsl.mfms.web.controller.PatrolController.ModelMappingValue;
@Controller
public class PunchCardController {
	public final static Logger logger = Logger.getLogger(PunchCardController.class);
	@Autowired
	private PunchCardManager punchCardManager;
	@RequestMapping(value = "/PunchCardManagement.do")
	public String showPatrolMenu(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		logger.debug("User requests to load patrol management page.");
		Role currRole = (Role) request.getSession().getAttribute("currRole");
//		if (null==currRole) {
//			return "main/site_select";
//		}
		PunchCard punchCard = new PunchCard();
//		punchCard.setKey(1);
		punchCard.setName("Test3");
		
		punchCardManager.save(punchCard);
		return ModelMappingValue.pages_punchcardMenu;
	}
	public PunchCardManager getPunchCardManager() {
		return punchCardManager;
	}
	public void setPunchCardManager(PunchCardManager punchCardManager) {
		this.punchCardManager = punchCardManager;
	}

	
}
