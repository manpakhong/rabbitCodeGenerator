package hk.ebsl.mfms.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;

import hk.ebsl.mfms.dao.AttendanceDao;
import hk.ebsl.mfms.dto.Attendance;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.PunchCard;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AttendanceInfoManager;
import hk.ebsl.mfms.manager.AttendanceManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.PunchCardManager;
import hk.ebsl.mfms.utility.DateUtil;
import hk.ebsl.mfms.web.controller.PatrolController.ModelMappingValue;
import hk.ebsl.mfms.web.form.DefectScheduleForm;
import hk.ebsl.mfms.web.form.PunchCardForm;
@Controller
public class PunchCardController {
	public final static Logger logger = Logger.getLogger(PunchCardController.class);
	@Autowired
	private PunchCardManager punchCardManager;
	@Autowired
	private AttendanceManager attendanceManager;
	@Autowired
	private AttendanceInfoManager attendanceInfoManager;
	@Autowired
	private LocationManager locationManager;
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

	@RequestMapping(value = "/submitClockIn.do", method = RequestMethod.POST)
	public String submitClockIn(@ModelAttribute("defectScheduleForm") DefectScheduleForm defectScheduleForm, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {

		try {
		logger.debug("/submitClockIn.do");

		String jsonString = request.getParameter("data");
		
		Gson gson = new Gson();
		PunchCardForm punchCardForm = gson.fromJson(jsonString, PunchCardForm.class);
		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");
		
		Attendance attendance = new Attendance();
//		attendance.setKey(1);
		attendance.setAccountKey(account.getKey());
		attendance.setLoginId(account.getName());
		attendance.setPlatform("W");
		attendance.setActionTypeCode("CI");
		String currentDateTimeString = punchCardForm.getCurrentDateTimeString();
		Timestamp currentDateTime = null;
		if (currentDateTimeString != null && !currentDateTimeString.isEmpty()) {
			currentDateTime = DateUtil.convertStringToTimestamp(currentDateTimeString);
		}

		attendance.setActionDateTime(currentDateTime);
		attendance.setCreateBy(account.getKey());
		attendance.setCreateDateTime(currentDateTime);
		attendance.setLastModifyBy(account.getKey());
		attendance.setLastModifyDateTime(currentDateTime);
		attendance.setDeleted("N");
		

		attendanceManager.save(attendance);
		
//		AttendanceInfo attendanceInfo = new AttendanceInfo();
		
		
		
		model.addAttribute("isSaved", true);
//		model.addAttribute("userAccount", null);
		
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".submitClockIn()", e);
		}
		return ModelMappingValue.pages_view_showClockInForm;
	}
	
	@RequestMapping(value = "/ShowClockIn.do", method = RequestMethod.GET)
	public String showStaffSearched(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws MFMSException {

		try {
		logger.debug("/ShowClockIn.do");

		
		HttpSession session = request.getSession();

		UserAccount account = (UserAccount) session.getAttribute("user");
		
		List<Location> locationList = locationManager.getAllLocation();
		Map<String, String> locationMap = new LinkedHashMap<String,String>();
		for (Location location: locationList) {
			locationMap.put(location.getCode(), location.getName());
		}
		
		model.addAttribute("locationList", locationMap);
		model.addAttribute("userAccount", account);
		
		Date currentDate = new Date();
		Timestamp ts = new Timestamp(currentDate.getTime());
		String currentDateTime = DateUtil.convertTimestampToString(ts);
		model.addAttribute("currentDateTime", currentDateTime);
		} catch (Exception e) {
			logger.error(this.getClass().getName() + ".showStaffSearched()", e);
		}
		return ModelMappingValue.pages_view_showClockInForm;
	}
}
