package hk.ebsl.mfms.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import hk.ebsl.mfms.dao.PunchCardDao;
import hk.ebsl.mfms.dto.PunchCard;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
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

	@RequestMapping(value = "/ShowClockIn.do", method = RequestMethod.GET)
	public String showStaffSearched(@RequestParam("privilege") String privilege, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) {

		logger.debug("/ShowClockIn.do");

//		List<UserAccount> accountList = getAccountBy(this.getRoleFromSession(request).getSiteKey(), "", -1, privilege);
//		List<String> sortedAccountName = new ArrayList<String>();
//		List<UserAccount> sortedAccountList = new ArrayList<UserAccount>();
//		Map<String, UserAccount> map = new HashMap<String, UserAccount>();
//
//		for (UserAccount ua : accountList) {
//
//			// logger.debug("ShowStaffSearched.do ||||| Original AccountList: "
//			// + ua.getLoginId());
//			map.put(ua.getLoginId(), ua);
//			sortedAccountName.add(ua.getLoginId());
//		}
//
//		Collections.sort(sortedAccountName, new StringComparator());
//		for (String s : sortedAccountName) {
//			sortedAccountList.add(map.get(s));
//		}
//
//		model.addAttribute(ModelMappingValue.var_searchedStaff, sortedAccountList);
		return ModelMappingValue.pages_view_showClockInForm;
	}
}
