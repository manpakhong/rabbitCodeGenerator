package hk.ebsl.mfms.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import hk.ebsl.mfms.dto.AlarmConsole;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.manager.AlarmConsoleManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.model.AlarmConsoleJsonObject;
import hk.ebsl.mfms.web.controller.AdministrationController.ModelMappingValue;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AlarmConsoleController {

	public final static Logger logger = Logger
			.getLogger(AlarmConsoleController.class);

	@Autowired
	private Properties privilegeMap;
	@Autowired
	private AlarmConsoleManager alarmConsoleManager;

	@RequestMapping(value = "/AlarmConsole.do")
	public String showAlarmConsole(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws IOException {

		logger.debug("User requests to load alarm console page.");
		
		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");
		if (null == currRole) {
			return "main/site_select";
		}

		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.alarmConsole"));

		if (hasPrivilege) {
			return "alarmconsole/alarm_console";
		} else {

			// user does not have right to view defect
			logger.debug("user does not have right to view AlarmConsole ");
			return "common/noprivilege";

		}
	}

	@RequestMapping(value = "/GetAllAlarmConsole.do", produces = "application/json")
	public @ResponseBody String showAdministrationManagementMenu(
			HttpServletRequest request, HttpServletResponse response,
			ModelMap model) {

		HttpSession session = request.getSession();
		Role currRole = (Role) session.getAttribute("currRole");

		boolean hasPrivilege = currRole.getGrantedPrivilegeCodeList().contains(
				privilegeMap.get("privilege.code.alarmConsole"));

		if (!hasPrivilege) {
			logger.debug("user does not have right to view AlarmConsole ");
			return "[]";
		}

		List<AlarmConsole> alarmList = alarmConsoleManager.getAllAlarm();
		List<AlarmConsoleJsonObject> jsonList = new ArrayList<AlarmConsoleJsonObject>();

		int counter = 1;
		AlarmConsoleJsonObject obj = null;
		List<AlarmConsole> objDetail = new ArrayList<AlarmConsole>();

		String tmpWoCode = "-1";
		for (AlarmConsole tmp : alarmList) {

			if (tmp.getDefect_SiteKey() == null) {

				if(tmpWoCode.equals(tmp.getDefect_Code())){
					
				}else{
					continue;
				}
				
			} else {

				if (tmp.getDefect_SiteKey() != currRole.getSiteKey()) {
					tmpWoCode = "-1";
					continue;
				}else{
					
					tmpWoCode = tmp.getDefect_Code();
				}
			}
//			System.out.println("getSortNum : " + tmp.getSortNum() + "||"
//					+ tmp.getEscalation());

			if (Integer.parseInt(tmp.getSortNum()) > 0) {

				if (obj != null) {
					objDetail.add(tmp);

				}
			} else {

				if (obj != null) {

					// System.out.println("objDetail : " + objDetail.size());
					obj.setDetails(objDetail);
					jsonList.add(obj);
				}
				objDetail = new ArrayList<AlarmConsole>();
				obj = new AlarmConsoleJsonObject();
				obj.setAlertStatus(tmp.getColor());
				obj.setID(counter++);
				obj.setJECCaseNo("");
				obj.setSummary(tmp);
			}
		}

		if (obj != null) {
			obj.setDetails(objDetail);
			jsonList.add(obj);
		}

		String jsonString = listToJsonString(jsonList);
		System.out.println(jsonString);

		return jsonString;
	}

	private <T> String listToJsonString(List<T> json) {
		ObjectMapper mapper = new ObjectMapper();

		String rtn = "";

		try {
			rtn = mapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rtn;
	}

	public AlarmConsoleManager getAlarmConsoleManager() {
		return alarmConsoleManager;
	}

	public void setAlarmConsoleManager(AlarmConsoleManager alarmConsoleManager) {
		this.alarmConsoleManager = alarmConsoleManager;
	}

}
