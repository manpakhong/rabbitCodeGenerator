package hk.ebsl.mfms.webservice.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectSchedule;
import hk.ebsl.mfms.dto.DefectScheduleAccount;
import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.dto.Equipment;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.ProblemCode;
import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.dto.Tool;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.exception.MFMSWebServiceException;
import hk.ebsl.mfms.manager.CauseCodeManager;
import hk.ebsl.mfms.manager.DefectActionLogManager;
import hk.ebsl.mfms.manager.DefectFileManager;
import hk.ebsl.mfms.manager.DefectManager;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.manager.EquipmentManager;
import hk.ebsl.mfms.manager.FailureClassManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.PriorityManager;
import hk.ebsl.mfms.manager.ProblemCodeManager;
import hk.ebsl.mfms.manager.StatusFlowManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.manager.SystemParamManager;
import hk.ebsl.mfms.manager.ToolManager;
import hk.ebsl.mfms.utility.FileBucket;
import hk.ebsl.mfms.web.controller.DefectController;
import hk.ebsl.mfms.web.form.validator.DefectFormValidator;
import hk.ebsl.mfms.webservice.DefectWebService;
import hk.ebsl.mfms.webservice.xml.CauseCodeXml;
import hk.ebsl.mfms.webservice.xml.DefectPhotoXml;
import hk.ebsl.mfms.webservice.xml.DefectVideoXml;
import hk.ebsl.mfms.webservice.xml.DefectXml;
import hk.ebsl.mfms.webservice.xml.EquipmentXml;
import hk.ebsl.mfms.webservice.xml.LocationXml;
import hk.ebsl.mfms.webservice.xml.PriorityXml;
import hk.ebsl.mfms.webservice.xml.ProblemCodeXml;
import hk.ebsl.mfms.webservice.xml.ScheduleHistoryXml;
import hk.ebsl.mfms.webservice.xml.ScheduleXml;
import hk.ebsl.mfms.webservice.xml.StatusFlowXml;
import hk.ebsl.mfms.webservice.xml.StatusXml;
import hk.ebsl.mfms.webservice.xml.ToolXml;
import hk.ebsl.mfms.webservice.xml.BaseXml;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlMsg;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlStatus;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.castor.core.util.Base64Decoder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

@Service
@WebService(endpointInterface = "hk.ebsl.mfms.webservice.DefectWebService", serviceName = "defectWebService")
public class DefectWebServiceImpl extends WebServiceUtil implements
		DefectWebService {

	public static final Logger logger = Logger
			.getLogger(DefectWebServiceImpl.class);

	@Resource
	WebServiceContext wsCtxt;

	@Resource
	private DefectManager defectManager;

	@Resource
	private DefectActionLogManager defectActionLogManager;

	@Resource
	private ProblemCodeManager problemCodeManager;

	@Resource
	private FailureClassManager failureClassManager;

	@Resource
	private EquipmentManager equipmentManager;

	@Resource
	private LocationManager locationManager;

	@Resource
	private CauseCodeManager causeCodeManager;

	@Resource
	private ToolManager toolManager;

	@Resource
	private StatusManager statusManager;

	@Resource
	private StatusFlowManager statusFlowManager;

	@Resource
	private DefectScheduleManager defectScheduleManager;

	@Resource
	private DefectFileManager defectFileManager;

	@Resource
	private DefectFormValidator defectFormValidator;

	@Resource
	private PriorityManager priorityManager;

	@Resource
	private SystemParamManager systemParamManager;

	@Resource
	private Properties propertyConfigurer;

	// TODO createModifyDefect

	@Override
	public synchronized DefectXml createModifyDefect(Integer key,
			Integer siteKey, String code, Integer locationKey,
			Integer failureClassKey, Integer problemCodeKey,
			Integer causeCodeKey, Integer toolKey, Integer equipmentKey,
			String contactName, String contactTel, String contactEmail,
			String emergencyTel, Integer assignedGroupKey,
			Integer assignedAccountKey, Integer priority, String desc,
			Long reportDateTime, String callFrom, Integer issueBy,
			Long issueDateTime, Long targetStartDateTime,
			Long targetFinishDateTime, Long actualStartDateTime,
			Long actualFinishDateTime, Integer checkBy, Long checkDateTime,
			String statusID, String remarks, Integer createBy,
			Long createDateTime, Integer lastModifyBy, Long lastModifyDateTime,
			String deleted) {

		System.out.println("createModifyDefect object : " + this.toString());

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			DefectXml xml = new DefectXml();
			Defect defect = null;

			if (key > 0) {

				xml.setIsCreate(false);

				try {
					defect = defectManager.getDefectByKey(key);
				} catch (MFMSException e) {

					e.printStackTrace();
				}

				Long test = defect.getLastModifyDateTime() == null ? 0 : defect
						.getLastModifyDateTime().getTime();

				if (lastModifyDateTime > test) {

					logger.debug("*********mobile side");

					defect.setLocationKey(locationKey);
					defect.setFailureClassKey(failureClassKey);
					defect.setProblemCodeKey(problemCodeKey);
					defect.setCauseCodeKey(seter(causeCodeKey));
					defect.setToolKey(seter(toolKey));
					defect.setEquipmentKey(seter(equipmentKey));
					defect.setContactName(contactName);
					defect.setContactTel(contactTel);
					defect.setContactEmail(contactEmail);
					defect.setEmergencyTel(emergencyTel);
					defect.setAssignedAccountKey(seter(assignedAccountKey));
					defect.setAssignedGroupKey(seter(assignedGroupKey));
					defect.setPriority(priority);
					defect.setDesc(desc);
					defect.setReportDateTime(dateSeter(reportDateTime));
					defect.setCallFrom(callFrom);
					defect.setIssueBy(seter(issueBy));
					defect.setIssueDateTime(dateSeter(issueDateTime));
					defect.setTargetStartDateTime(dateSeter(targetStartDateTime));
					defect.setTargetFinishDateTime(dateSeter(targetFinishDateTime));
					defect.setActualStartDateTime(dateSeter(actualStartDateTime));
					defect.setActualFinishDateTime(dateSeter(actualFinishDateTime));
					defect.setCheckBy(seter(checkBy));
					defect.setCheckDateTime(dateSeter(checkDateTime));
					defect.setStatusID(statusID);
					defect.setRemarks(remarks);
					defect.setLastModifyBy(seter(lastModifyBy));
					defect.setLastModifyDateTime(dateSeter(lastModifyDateTime));
					defect.setDeleted(deleted);

					if (deleted.equals('Y') && defect.getKey() != null) {
						try {
							defectFileManager.deleteAllFile(lastModifyBy,
									defect.getKey());
						} catch (MFMSException e) {

							e.printStackTrace();
						}
					}
				} else
					logger.debug("*********server side");

			} else {

				xml.setIsCreate(true);

				defect = new Defect();
				defect.setSiteKey(siteKey);
				try {
					defect.setCode(defectManager.getNextDefectCode(siteKey));
				} catch (MFMSException e) {

					e.printStackTrace();
				}

				defect.setCreateChannel("H");
				defect.setLocationKey(locationKey);
				defect.setFailureClassKey(failureClassKey);
				defect.setProblemCodeKey(problemCodeKey);
				defect.setCauseCodeKey(seter(causeCodeKey));
				defect.setToolKey(seter(toolKey));
				defect.setEquipmentKey(seter(equipmentKey));
				defect.setContactName(contactName);
				defect.setContactTel(contactTel);
				defect.setContactEmail(contactEmail);
				defect.setEmergencyTel(emergencyTel);
				defect.setAssignedAccountKey(seter(assignedAccountKey));
				defect.setAssignedGroupKey(seter(assignedGroupKey));
				defect.setPriority(priority);
				defect.setDesc(desc);
				defect.setReportDateTime(dateSeter(reportDateTime));
				defect.setCallFrom(callFrom);
				defect.setIssueBy(seter(issueBy));
				defect.setIssueDateTime(dateSeter(issueDateTime));
				defect.setTargetStartDateTime(dateSeter(targetStartDateTime));
				defect.setTargetFinishDateTime(dateSeter(targetFinishDateTime));
				defect.setActualStartDateTime(dateSeter(actualStartDateTime));
				defect.setActualFinishDateTime(dateSeter(actualFinishDateTime));
				defect.setCheckBy(seter(checkBy));
				defect.setLastModifyBy(createBy);
				defect.setLastModifyDateTime(new Timestamp(System
						.currentTimeMillis()));
				defect.setCheckDateTime(dateSeter(checkDateTime));
				defect.setStatusID(statusID);
				defect.setRemarks(remarks);
				defect.setCreateBy(createBy);
				// defect.setCreateDateTime(new
				// Timestamp(System.currentTimeMillis()));
				defect.setCreateDateTime(dateSeter(createDateTime));
				defect.setDeleted("N");
			}

			Integer insertKey = 0;

			if (defect.getTargetFinishDateTime() != null
					&& defect.getActualFinishDateTime() != null) {

				if (defect.getTargetFinishDateTime().getTime() > defect
						.getActualFinishDateTime().getTime())
					defect.setMeetKpi("Y");
				else
					defect.setMeetKpi("N");

			} else
				defect.setMeetKpi("-");

			defect.setCallCenterEmail(propertyConfigurer
					.getProperty("notification.callCenter.email"));
			insertKey = DefectController.saveDefectToDb(defectActionLogManager,
					defectManager, defect);
			// insertKey = defectManager.saveDefect(defect);

			if (insertKey > 0) {
				xml.setXmlStatus(XmlStatus.SUCCESS.toString());
				xml.setKey(insertKey);
			} else {
				xml.setXmlStatus(XmlStatus.FAIL.toString());
				xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());
			}

			return xml;
		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			return xml;
		} catch (Exception e) {
			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg("SERVER_ERROR");

			return xml;
		}

	}

	// TODO updateDefect

	@Override
	public List<DefectXml> updateDefect(long lastModifyDate, Integer siteKey,
			Integer accountKey, String groupKeys) {

		System.out.println("lastModifyDate : " + lastModifyDate + "||siteKey :"
				+ siteKey + "||accountKey :" + accountKey + "||groupKeys : "
				+ groupKeys);

		List<DefectXml> xmlList = new ArrayList<DefectXml>();
		try {

			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			Timestamp time = new Timestamp(lastModifyDate);

			List<Defect> defectList = new ArrayList<Defect>();
			try {
				// defectList =
				// defectManager.getDefectByLastModifyDateAndSiteKey(time,
				// siteKey, accountKey);
				Integer[] groupKeyIntArray = null;

				if (!groupKeys.equals("")) {
					String[] groupKeyArray = groupKeys.split(",");
					groupKeyIntArray = new Integer[groupKeyArray.length];
					int i = 0;
					for (String tmp : groupKeyArray) {
						groupKeyIntArray[i] = Integer.parseInt(tmp);
						i++;
					}
				}
				defectList = defectManager.getDefectByDelta(time, siteKey,
						accountKey, groupKeyIntArray);
				System.out.println("defectList :" + defectList.size());

			} catch (MFMSException e) {

				e.printStackTrace();
			}

			for (Defect defect : defectList) {
				DefectXml xml = new DefectXml();
				xml.setKey(defect.getKey());
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setCode(defect.getCode());
				xml.setSiteKey(defect.getSiteKey());
				xml.setCreateChannel(defect.getCreateChannel());
				xml.setLocationKey(defect.getLocationKey());
				xml.setFailureClassKey(defect.getFailureClassKey());
				xml.setProblemCodeKey(defect.getProblemCodeKey());

				xml.setCauseCodeKey(defect.getCauseCodeKey());

				xml.setToolKey(defect.getToolKey());

				xml.setEquipmentKey(defect.getEquipmentKey());

				xml.setContactEmail(defect.getContactEmail());
				xml.setContactName(defect.getContactName());
				xml.setContactTel(defect.getContactTel());
				xml.setEmergencyTel(defect.getEmergencyTel());
				xml.setAssignedGroupKey(defect.getAssignedGroupKey());
				xml.setAssignedAccountKey(defect.getAssignedAccountKey());
				xml.setPriority(defect.getPriority());
				xml.setDesc(defect.getDesc());
				xml.setDetailedDesc(defect.getDetailedDesc());
				if (defect.getReportDateTime() != null)
					xml.setReportDateTime(defect.getReportDateTime().getTime());
				else
					xml.setReportDateTime(Long.parseLong(String.valueOf(-1)));
				xml.setCallFrom(defect.getCallFrom());

				xml.setIssueBy(defect.getIssueBy());

				if (defect.getIssueDateTime() != null)
					xml.setIssueDateTime(defect.getIssueDateTime().getTime());
				else
					xml.setIssueDateTime(Long.parseLong(String.valueOf(-1)));
				if (defect.getTargetFinishDateTime() != null)
					xml.setTargetFinishDateTime(defect
							.getTargetFinishDateTime().getTime());
				else
					xml.setTargetFinishDateTime(Long.parseLong(String
							.valueOf(-1)));
				if (defect.getTargetStartDateTime() != null)
					xml.setTargetStartDateTime(defect.getTargetStartDateTime()
							.getTime());
				else
					xml.setTargetStartDateTime(Long.parseLong(String
							.valueOf(-1)));
				if (defect.getActualFinishDateTime() != null)
					xml.setActualFinishDateTime(defect
							.getActualFinishDateTime().getTime());
				else
					xml.setActualFinishDateTime(Long.parseLong(String
							.valueOf(-1)));
				if (defect.getActualStartDateTime() != null)
					xml.setActualStartDateTime(defect.getActualStartDateTime()
							.getTime());
				else
					xml.setActualStartDateTime(Long.parseLong(String
							.valueOf(-1)));
				if (defect.getCheckBy() != null)
					xml.setCheckBy(defect.getCheckBy());
				else
					xml.setCheckBy(-1);
				if (defect.getCheckDateTime() != null)
					xml.setCheckDateTime(defect.getCheckDateTime().getTime());
				else
					xml.setCheckDateTime(Long.parseLong(String.valueOf(-1)));
				xml.setStatusID(defect.getStatusID());
				xml.setRemarks(defect.getRemarks());
				xml.setCreateBy(defect.getCreateBy());
				xml.setCreateDateTime(defect.getCreateDateTime().getTime());
				xml.setLastModifyBy(defect.getLastModifyBy());

				if (defect.getLastModifyDateTime() != null)
					xml.setLastModifyDateTime(defect.getLastModifyDateTime()
							.getTime());
				else
					xml.setLastModifyDateTime(defect.getCreateDateTime()
							.getTime());
				xml.setDeleted(defect.getDeleted());
				xmlList.add(xml);
			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}

	}

	// TODO updateProblemCode

	@Override
	public List<ProblemCodeXml> updateProblemCode(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<ProblemCode> problemCodeList = new ArrayList<ProblemCode>();
		try {
			problemCodeList = problemCodeManager
					.getAllProblemCodeAndFailureClass();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<ProblemCodeXml> xmlList = new ArrayList<ProblemCodeXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (ProblemCode problemCode : problemCodeList) {
				ProblemCodeXml xml = new ProblemCodeXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setName(problemCode.getName());
				xml.setSiteKey(problemCode.getSiteKey());
				xml.setKey(problemCode.getKey());
				xml.setCode(problemCode.getCode());
				xml.setDesc(problemCode.getDesc());
				xml.setLevelKey(problemCode.getLevelKey());
				if (problemCode.getDefaultPriority() != null)
					xml.setDefaultPriority(problemCode.getDefaultPriority());
				else
					xml.setDefaultPriority(-1);
				if (problemCode.getDefaultAccountKey() != null)
					xml.setDefaultAccountKey(problemCode.getDefaultAccountKey());
				else
					xml.setDefaultAccountKey(-1);
				xml.setParentKey(problemCode.getParentKey());
				xml.setCreatedBy(problemCode.getCreatedBy());
				xml.setCreateDateTime(problemCode.getCreateDateTime().getTime());
				if (problemCode.getLastModifyBy() != null)
					xml.setLastModifyBy(problemCode.getLastModifyBy());
				else
					xml.setLastModifyBy(-1);
				if (problemCode.getLastModifyDateTime() != null)
					xml.setLastModifyDateTime(problemCode
							.getLastModifyDateTime().getTime());
				else
					xml.setLastModifyDateTime(problemCode.getCreateDateTime()
							.getTime());
				xml.setDeleted(problemCode.getDeleted());

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			ProblemCodeXml xml = new ProblemCodeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateLocation

	@Override
	public List<LocationXml> updateLocation(long lastModifyDate) {

		List<Location> locationList = new ArrayList<Location>();
		try {
			locationList = locationManager.getAllLocation();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<LocationXml> xmlList = new ArrayList<LocationXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (Location location : locationList) {
				LocationXml xml = new LocationXml();
				xml.setName(location.getName());
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setSiteKey(location.getSiteKey());
				xml.setKey(location.getKey());
				xml.setTagId(location.getTagId());
				xml.setCode(location.getCode());
				xml.setDesc(location.getDesc());
				xml.setChain(location.getChain());
				xml.setLevelKey(location.getLevelKey());
				xml.setParentKey(location.getParentKey());
				xml.setCreatedBy(location.getCreatedBy());
				xml.setCreateDateTime(location.getCreateDateTime().getTime());
				if (location.getLastModifyBy() != null)
					xml.setLastModifyBy(location.getLastModifyBy());
				else
					xml.setLastModifyBy(-1);
				if (location.getLastModifyDateTime() != null)
					xml.setLastModifyDateTime(location.getLastModifyDateTime()
							.getTime());
				else
					xml.setLastModifyDateTime(location.getCreateDateTime()
							.getTime());
				xml.setDeleted(location.getDeleted());

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			LocationXml xml = new LocationXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateTool

	@Override
	public List<ToolXml> updateTool(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<Tool> toolList = new ArrayList<Tool>();
		try {
			toolList = toolManager.getAllTool();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<ToolXml> xmlList = new ArrayList<ToolXml>();

		for (Tool tool : toolList) {
			ToolXml xml = new ToolXml();
			xml.setName(tool.getName());
			xml.setSiteKey(tool.getSiteKey());
			xml.setKey(tool.getKey());
			xml.setCode(tool.getCode());
			xml.setDesc(tool.getDesc());
			xml.setLevelKey(tool.getLevelKey());
			xml.setParentKey(tool.getParentKey());
			xml.setCreatedBy(tool.getCreatedBy());
			xml.setCreateDateTime(tool.getCreateDateTime().getTime());
			xml.setLastModifyBy(tool.getLastModifyBy());
			if (tool.getLastModifyDateTime() != null)
				xml.setLastModifyDateTime(tool.getLastModifyDateTime()
						.getTime());
			else
				xml.setLastModifyDateTime(tool.getCreateDateTime().getTime());
			xml.setDeleted(tool.getDeleted());

			xmlList.add(xml);

		}

		return xmlList;
	}

	// TODO updateCauseCode

	@Override
	public List<CauseCodeXml> updateCauseCode(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<CauseCode> causeCodeList = new ArrayList<CauseCode>();
		try {
			causeCodeList = causeCodeManager.getAllCauseCode();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<CauseCodeXml> xmlList = new ArrayList<CauseCodeXml>();

		for (CauseCode causeCode : causeCodeList) {
			CauseCodeXml xml = new CauseCodeXml();
			xml.setName(causeCode.getName());
			xml.setSiteKey(causeCode.getSiteKey());
			xml.setKey(causeCode.getKey());
			xml.setCode(causeCode.getCode());
			xml.setDesc(causeCode.getDesc());
			xml.setLevelKey(causeCode.getLevelKey());
			xml.setParentKey(causeCode.getParentKey());
			xml.setCreatedBy(causeCode.getCreatedBy());
			xml.setCreateDateTime(causeCode.getCreateDateTime().getTime());
			xml.setLastModifyBy(causeCode.getLastModifyBy());
			if (causeCode.getLastModifyDateTime() != null)
				xml.setLastModifyDateTime(causeCode.getLastModifyDateTime()
						.getTime());
			else
				xml.setLastModifyDateTime(causeCode.getCreateDateTime()
						.getTime());
			xml.setDeleted(causeCode.getDeleted());

			xmlList.add(xml);

		}

		return xmlList;
	}

	// TODO updateEquipment

	@Override
	public List<EquipmentXml> updateEquipment(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<Equipment> equipmentList = new ArrayList<Equipment>();
		try {
			equipmentList = equipmentManager.getAllEquipment();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<EquipmentXml> xmlList = new ArrayList<EquipmentXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (Equipment equipment : equipmentList) {
				EquipmentXml xml = new EquipmentXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setName(equipment.getName());
				xml.setSiteKey(equipment.getSiteKey());
				xml.setKey(equipment.getKey());
				xml.setCode(equipment.getCode());
				xml.setDesc(equipment.getDesc());
				xml.setLocationKey(equipment.getLocationKey());
				xml.setLevelKey(equipment.getLevelKey());
				xml.setParentKey(equipment.getParentKey());
				xml.setCreatedBy(equipment.getCreatedBy());
				xml.setCreateDateTime(equipment.getCreateDateTime().getTime());
				xml.setLastModifyBy(equipment.getLastModifyBy());
				if (equipment.getLastModifyDateTime() != null)
					xml.setLastModifyDateTime(equipment.getLastModifyDateTime()
							.getTime());
				else
					xml.setLastModifyDateTime(equipment.getCreateDateTime()
							.getTime());
				xml.setDeleted(equipment.getDeleted());

				xmlList.add(xml);
			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			EquipmentXml xml = new EquipmentXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateStatus

	@Override
	public List<StatusXml> updateStatus(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<Status> statusList = new ArrayList<Status>();
		try {
			statusList = statusManager.getAllStatus();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<StatusXml> xmlList = new ArrayList<StatusXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (Status status : statusList) {
				StatusXml xml = new StatusXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setStatusID(status.getStatusId());
				xml.setCode(status.getCode());
				xml.setName(status.getName());
				xml.setDesc(status.getDesc());
				xml.setSequence(status.getSequence());
				xml.setCreateDateTime(status.getCreateDateTime().getTime());
				xml.setLastModifyBy(status.getLastModifyBy());
				if (status.getLastModifyDateTime() == null)
					xml.setLastModifyDateTime(status.getCreateDateTime()
							.getTime());
				else
					xml.setLastModifyDateTime(status.getLastModifyDateTime()
							.getTime());
				xml.setDeleted(status.getDeleted());

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			StatusXml xml = new StatusXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateStatusFlow

	@Override
	public List<StatusFlowXml> updateStatusFlow(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<StatusFlow> statusFlowList = new ArrayList<StatusFlow>();
		try {
			statusFlowList = statusFlowManager.getAllStatusFlow();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<StatusFlowXml> xmlList = new ArrayList<StatusFlowXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (StatusFlow statusFlow : statusFlowList) {
				StatusFlowXml xml = new StatusFlowXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setStatusID(statusFlow.getId().getStatusId());
				xml.setNextStatusId(statusFlow.getId().getNextStatusId());
				xml.setModeKey(statusFlow.getId().getModeKey());
				xml.setCreateDateTime(statusFlow.getCreateDateTime().getTime());
				xml.setLastModifyBy(statusFlow.getLastModifyBy());
				if (statusFlow.getLastModifyDateTime() == null)
					xml.setLastModifyDateTime(statusFlow.getCreateDateTime()
							.getTime());
				else
					xml.setLastModifyDateTime(statusFlow
							.getLastModifyDateTime().getTime());
				xml.setDeleted(statusFlow.getDeleted());

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			StatusFlowXml xml = new StatusFlowXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateSchedule

	@Override
	public List<ScheduleXml> updateSchedule(long lastModifyDate,
			Integer siteKey, Integer accountKey) {

		Timestamp time = new Timestamp(lastModifyDate);

		List<DefectScheduleAccount> scheduleAccountList = defectScheduleManager
				.getSiteScheduleAccount(time, siteKey, accountKey);

		List<ScheduleXml> xmlList = new ArrayList<ScheduleXml>();

		Set<Integer> scheduleKeyList = new HashSet<Integer>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (DefectScheduleAccount scheduleAccount : scheduleAccountList) {
				ScheduleXml xml = new ScheduleXml();
				scheduleKeyList.add(scheduleAccount.getScheduleKey());
				DefectSchedule schedule = defectScheduleManager
						.getScheduleByKey(scheduleAccount.getScheduleKey());
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());

				xml.setSiteKey(schedule.getSiteKey());
				xml.setScheduleKey(schedule.getScheduleKey());
				xml.setAccountKey(scheduleAccount.getAccountKey());
				String deletedStr = "N";
				if(scheduleAccount.getDefectSchedule().getDeleted().equals("Y")){
					deletedStr = "Y";
				}
				if(scheduleAccount.getDeleted().equals("Y")){
					deletedStr = "Y";
				}
				
				xml.setDeleted(deletedStr);
				xml.setDesc(schedule.getDesc());
				xml.setFrequency(schedule.getFrequency());
				xml.setRemarks(schedule.getRemarks());
				xml.setLocationKey(schedule.getLocationKey());
				xml.setEquipmentKey(schedule.getEquipmentKey());
				xml.setParentId(schedule.getParentId());
				xml.setSkipped(schedule.getSkipped());

				if (schedule.getScheduleStartDate() != null)
					xml.setScheduleStartDate(schedule.getScheduleStartDate()
							.getTime());
				else
					xml.setScheduleStartDate(Long.parseLong(String.valueOf(-1)));

				if (schedule.getScheduleEndDate() != null)
					xml.setScheduleEndDate(schedule.getScheduleEndDate()
							.getTime());
				else {

//					Calendar maxEnd = Calendar.getInstance();
//
//					maxEnd.setTime(schedule.getScheduleStartDate());
//					maxEnd.add(Calendar.YEAR, 10);
//					xml.setScheduleEndDate(maxEnd.getTimeInMillis());
				}

				if (schedule.getScheduleTime() != null)
					xml.setScheduleTime(schedule.getScheduleTime().getTime());
				else
					xml.setScheduleTime(Long.parseLong(String.valueOf(-1)));

				if (schedule.getLastModifyDateTime() != null)
					xml.setLastModifyDateTime(schedule.getLastModifyDateTime()
							.getTime());
				else
					xml.setLastModifyDateTime(Long.parseLong(String.valueOf(-1)));

				if (schedule.getSkippedStartDate() != null)
					xml.setSkippedStartDate(schedule.getSkippedStartDate()
							.getTime());
				else
					xml.setSkippedStartDate(Long.parseLong(String.valueOf(-1)));

				if (schedule.getSkippedEndDate() != null)
					xml.setSkippedEndDate(schedule.getSkippedEndDate()
							.getTime());
				else
					xml.setSkippedEndDate(Long.parseLong(String.valueOf(-1)));

				xmlList.add(xml);

			}

//			for (Integer key : scheduleKeyList) {
//
//				List<DefectSchedule> scheduleChildenList = defectScheduleManager
//						.searchDefectScheduleChilden(key, time);
//
//				for (DefectSchedule ds : scheduleChildenList) {
//
//					ScheduleXml child = new ScheduleXml();
//					child.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
//
//					child.setSiteKey(ds.getSiteKey());
//					child.setScheduleKey(key);
//					child.setAccountKey(null);
//					child.setDeleted("Y");
//					child.setDesc(ds.getDesc());
//					child.setFrequency(ds.getFrequency());
//					child.setRemarks(ds.getRemarks());
//					child.setLocationKey(ds.getLocationKey());
//					child.setEquipmentKey(ds.getEquipmentKey());
//					child.setParentId(ds.getParentId());
//					child.setSkipped(ds.getSkipped());
//
//					if (ds.getScheduleStartDate() != null)
//						child.setScheduleStartDate(ds.getScheduleStartDate()
//								.getTime());
//					else
//						child.setScheduleStartDate(Long.parseLong(String
//								.valueOf(-1)));
//
//					if (ds.getScheduleEndDate() != null)
//						child.setScheduleEndDate(ds.getScheduleEndDate()
//								.getTime());
//					else
//						child.setScheduleEndDate(Long.parseLong(String
//								.valueOf(-1)));
//
//					if (ds.getScheduleTime() != null)
//						child.setScheduleTime(ds.getScheduleTime().getTime());
//					else
//						child.setScheduleTime(Long.parseLong(String.valueOf(-1)));
//
//					if (ds.getLastModifyDateTime() != null)
//						child.setLastModifyDateTime(ds.getLastModifyDateTime()
//								.getTime());
//					else
//						child.setLastModifyDateTime(Long.parseLong(String
//								.valueOf(-1)));
//
//					if (ds.getSkippedStartDate() != null)
//						child.setSkippedStartDate(ds.getSkippedStartDate()
//								.getTime());
//					else
//						child.setSkippedStartDate(Long.parseLong(String
//								.valueOf(-1)));
//
//					if (ds.getSkippedEndDate() != null)
//						child.setSkippedEndDate(ds.getSkippedEndDate()
//								.getTime());
//					else
//						child.setSkippedEndDate(Long.parseLong(String
//								.valueOf(-1)));
//
//					xmlList.add(child);
//
//				}
//
//			}

			logger.debug("************** list size: " + xmlList.size());

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			ScheduleXml xml = new ScheduleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateDefectPhoto

	@Override
	public List<DefectPhotoXml> updateDefectPhoto(long lastModifyDate,
			Integer siteKey) throws IOException {

		logger.debug("lastModifyDate " + lastModifyDate);

		String PHOTO_LOCATION = "";
		String VIDEO_LOCATION = "";
		try {
			PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
					"wo.photo.uploadpath", 1);
			VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
					"wo.video.uploadpath", 1);
		} catch (MFMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		List<FileBucket> defectPhotoList = null;

		Timestamp time = new Timestamp(lastModifyDate);

		try {
			defectPhotoList = defectFileManager
					.getPhotoByLastModifyDateAndSiteKey(time, siteKey);
		} catch (MFMSException | Exception e1) {

			e1.printStackTrace();
		}

		List<DefectPhotoXml> xmlList = new ArrayList<DefectPhotoXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (FileBucket defectPhoto : defectPhotoList) {
				DefectPhotoXml xml = new DefectPhotoXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());

				xml.setDefectPhotoKey(defectPhoto.getKey());

				xml.setDefectKey(defectPhoto.getDefectKey());

				xml.setDesc(defectPhoto.getDesc());

				xml.setPhotoPath(defectPhoto.getPath());

				xml.setDeleted(defectPhoto.getDeleted());

				xml.setCreatedBy(defectPhoto.getCreatedBy());

				xml.setCreateDateTime(defectPhoto.getCreateDateTime().getTime());

				xml.setLastModifyBy(defectPhoto.getLastModifyBy());

				if (defectPhoto.getLastModifyDateTime() != null)

					xml.setLastModifyDateTime(defectPhoto
							.getLastModifyDateTime().getTime());
				else
					xml.setLastModifyDateTime(defectPhoto.getCreateDateTime()
							.getTime());

				File file = new File(PHOTO_LOCATION + defectPhoto.getPath());

				if (file.length() > 0 && defectPhoto.getDeleted().equals("N")) {

					FileInputStream in = new FileInputStream(file);

					try {

						int fileLength = in.available();

						byte[] data = new byte[fileLength];

						int count = in.read(data);

						if (count > 0) {

							xml.setData(data);

						}

					} finally {

						in.close();

					}
				} else
					xml.setData(null);

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException | FileNotFoundException e) {

			e.printStackTrace();
			DefectPhotoXml xml = new DefectPhotoXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO updateDefectVideo

	@Override
	public List<DefectVideoXml> updateDefectVideo(long lastModifyDate,
			Integer siteKey) throws IOException {

		logger.debug("lastModifyDate " + lastModifyDate);

		String PHOTO_LOCATION = "";
		String VIDEO_LOCATION = "";
		try {
			PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
					"wo.photo.uploadpath", 1);
			VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
					"wo.video.uploadpath", 1);
		} catch (MFMSException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		Timestamp time = new Timestamp(lastModifyDate);

		List<FileBucket> defectVideoAccountList = null;
		try {
			defectVideoAccountList = defectFileManager
					.getVideoByLastModifyDateAndSiteKey(time, siteKey);
		} catch (MFMSException | Exception e1) {

			e1.printStackTrace();
		}

		List<DefectVideoXml> xmlList = new ArrayList<DefectVideoXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (FileBucket defectVideo : defectVideoAccountList) {
				DefectVideoXml xml = new DefectVideoXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());

				xml.setDefectVideoKey(defectVideo.getKey());

				xml.setDefectKey(defectVideo.getDefectKey());

				xml.setDesc(defectVideo.getDesc());

				xml.setVideoPath(defectVideo.getPath());

				xml.setDeleted(defectVideo.getDeleted());

				xml.setCreatedBy(defectVideo.getCreatedBy());

				xml.setCreateDateTime(defectVideo.getCreateDateTime().getTime());

				xml.setLastModifyBy(defectVideo.getLastModifyBy());

				if (defectVideo.getLastModifyDateTime() != null)

					xml.setLastModifyDateTime(defectVideo
							.getLastModifyDateTime().getTime());
				else
					xml.setLastModifyDateTime(defectVideo.getCreateDateTime()
							.getTime());

				File file = new File(VIDEO_LOCATION + defectVideo.getPath());

				if (file.length() > 0 && defectVideo.getDeleted().equals("N")) {

					FileInputStream in = new FileInputStream(file);

					try {

						int fileLength = in.available();

						byte[] data = new byte[fileLength];

						int count = in.read(data);

						if (count > 0) {

							xml.setData(data);

						}

					} finally {

						in.close();

					}
				} else
					xml.setData(null);

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException | FileNotFoundException e) {

			e.printStackTrace();
			DefectVideoXml xml = new DefectVideoXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	// TODO addDefectPhoto

	@Override
	public DefectXml addDefectPhoto(Integer defectKey, Integer createBy,
			String data, Long createDateTime, String fileName) {

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			String PHOTO_LOCATION = "";
			String VIDEO_LOCATION = "";
			try {
				PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
						"wo.photo.uploadpath", 1);
				VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
						"wo.video.uploadpath", 1);
			} catch (MFMSException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
				if (defectKey > 0
						&& defectFileManager.getPhotoRemain(defectKey) > 0) {

					logger.debug("defectKey: " + defectKey);

					logger.debug("PhotoRemain: "
							+ defectFileManager.getPhotoRemain(defectKey));

					DefectPhoto defectPhoto = new DefectPhoto();

					defectPhoto.setDefectKey(defectKey);
					defectPhoto.setDesc("");

					String path = PHOTO_LOCATION + fileName;

					Defect d = defectManager.getDefectByKey(defectKey);

					defectPhoto.setPhotoPath(fileName);
					defectPhoto.setSiteKey(d.getSiteKey());
					defectPhoto.setCreatedBy(createBy);
					defectPhoto.setCreateDateTime(dateSeter(createDateTime));
					defectPhoto.setLastModifyBy(null);
					defectPhoto
							.setLastModifyDateTime(dateSeter(createDateTime));
					defectPhoto.setDeleted("N");

					BufferedImage image = null;
					byte[] imageByte;

					imageByte = Base64Decoder.decode(data);
					ByteArrayInputStream bis = new ByteArrayInputStream(
							imageByte);
					image = ImageIO.read(bis);
					bis.close();

					// write the image to a file
					File outputfile = new File(path);
					ImageIO.write(image, "jpg", outputfile);

					logger.debug("path: " + path);

					try {
						defectFileManager.saveDefectPhoto(defectPhoto);
					} catch (MFMSException e) {

						e.printStackTrace();
					}

				}
			} catch (MFMSException e) {

				e.printStackTrace();
			}

			DefectXml xml = new DefectXml();

			return xml;
		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			return xml;
		} catch (Exception e) {
			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg("SERVER_ERROR");

			return xml;
		}

	}

	// TODO addDefectVideo

//	@Override
//	public DefectXml addDefectVideo(Integer defectKey, Integer createBy,
//			byte[] data, Long createDateTime, String fileName) {
	@Override
		public DefectXml addDefectVideo(Integer defectKey, Integer createBy,
			String data, Long createDateTime, String fileName) {
		
		
//		System.out.println("Defect Key : "+defectKey + "|| file Name : "+fileName + "|| data :"+ Hex.encodeHexString(data));
//		System.out.println("Defect Key : "+defectKey + "|| file Name : "+fileName + "|| data :"+ data);
		
		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			String PHOTO_LOCATION = "";
			String VIDEO_LOCATION = "";
			try {
				PHOTO_LOCATION = systemParamManager.getSystemParamValueByKey(
						"wo.photo.uploadpath", 1);
				VIDEO_LOCATION = systemParamManager.getSystemParamValueByKey(
						"wo.video.uploadpath", 1);
			} catch (MFMSException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
//				if(true){
				if (defectKey > 0
						&& defectFileManager.getVideoRemain(defectKey) > 0) {

					logger.debug("defectKey: " + defectKey);

					logger.debug("VideoRemain: "
							+ defectFileManager.getVideoRemain(defectKey));

					Defect d = defectManager.getDefectByKey(defectKey);

					DefectVideo defectVideo = new DefectVideo();

					defectVideo.setDefectKey(defectKey);
					defectVideo.setDesc("");

					String path = VIDEO_LOCATION + fileName;

					defectVideo.setVideoPath(fileName);
					defectVideo.setSiteKey(d.getSiteKey());
					defectVideo.setCreatedBy(createBy);
					defectVideo.setCreateDateTime(dateSeter(createDateTime));
					defectVideo.setLastModifyBy(null);
					defectVideo
							.setLastModifyDateTime(dateSeter(createDateTime));
					defectVideo.setDeleted("N");

//					new File(VIDEO_LOCATION).mkdirs();
//					File file = new File(VIDEO_LOCATION, fileName);
//					FileOutputStream output = new FileOutputStream(file);
//					output.write(data, 0, data.length);
//					output.close();
//					FileCopyUtils.copy(data, new File(path));

					byte[] decodedBytes = Base64.decodeBase64(data);
					

					FileOutputStream out = new FileOutputStream(path);
					out.write(decodedBytes);
					out.close();

					logger.debug("path: " + path);

					try {
						defectFileManager.saveDefectVideo(defectVideo);
					} catch (MFMSException e) {

						e.printStackTrace();
					}

				}
			} catch (MFMSException e) {

				e.printStackTrace();
			}

			DefectXml xml = new DefectXml();

			return xml;
		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			return xml;
		} catch (Exception e) {
			e.printStackTrace();
			DefectXml xml = new DefectXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg("SERVER_ERROR");

			return xml;
		}

	}

	@Override
	public ScheduleHistoryXml addScheduleHistory(Integer siteKey,
			Integer scheduleKey, Long finishDateTime, Long taskDateTime) {

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			ScheduleHistoryXml xml = new ScheduleHistoryXml();
			DefectScheduleHistory history = new DefectScheduleHistory();

			history.setSiteKey(siteKey);

			history.setDefectScheduleKey(scheduleKey);

			history.setFinishDateTime(dateSeter(finishDateTime));

			history.setTaskDateTime(dateSeter(taskDateTime));

			int insertKey = 0;

			try {

				DefectSchedule ds = defectScheduleManager
						.getScheduleByKey(scheduleKey);

				ds.setFinishDateTime(dateSeter(finishDateTime));

				defectScheduleManager.saveDefectSchedule(ds, null);

				insertKey = defectScheduleManager
						.createDefectScheduleHistory(history);
			} catch (MFMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			xml.setXmlStatus(XmlStatus.SUCCESS.toString());
			xml.setKey(insertKey);

			return xml;
		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			ScheduleHistoryXml xml = new ScheduleHistoryXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			return xml;
		} catch (Exception e) {
			e.printStackTrace();
			ScheduleHistoryXml xml = new ScheduleHistoryXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg("SERVER_ERROR");

			return xml;
		}

	}

	@Override
	public List<ScheduleHistoryXml> updateScheduleHistory(long lastModifyDate,
			Integer siteKey) {

		Timestamp time = new Timestamp(lastModifyDate);

		List<DefectScheduleHistory> historyList = new ArrayList<DefectScheduleHistory>();
		try {
			historyList = defectScheduleManager.getSiteScheduleHistory(siteKey,
					time);
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<ScheduleHistoryXml> xmlList = new ArrayList<ScheduleHistoryXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (DefectScheduleHistory history : historyList) {
				ScheduleHistoryXml xml = new ScheduleHistoryXml();
				xml.setKey(history.getKey());
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setScheduleKey(history.getDefectScheduleKey());
				xml.setFinishDateTime(history.getFinishDateTime().getTime());
				xml.setTaskDateTime(history.getTaskDateTime().getTime());

				xmlList.add(xml);
			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			ScheduleHistoryXml xml = new ScheduleHistoryXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}

	}

	@Override
	public List<PriorityXml> updatePriority(long lastModifyDate) {

		logger.debug("lastModifyDate " + lastModifyDate);

		List<Priority> list = new ArrayList<Priority>();
		try {
			list = priorityManager.getAllPriority();
		} catch (MFMSException e) {

			e.printStackTrace();
		}

		List<PriorityXml> xmlList = new ArrayList<PriorityXml>();

		try {
			if (checkLogin(wsCtxt.getMessageContext())) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			for (Priority p : list) {
				PriorityXml xml = new PriorityXml();
				xml.setXmlStatus(BaseXml.XmlStatus.SUCCESS.toString());
				xml.setPriority(p.getPriority());
				xml.setResponseTime(p.getResponseTime());

				xmlList.add(xml);

			}

			return xmlList;

		} catch (MFMSWebServiceException e) {

			e.printStackTrace();
			PriorityXml xml = new PriorityXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);

			return xmlList;
		}
	}

	public Integer seter(Integer key) {

		if (key == 0)
			return null;
		else
			return key;

	}

	public Timestamp dateSeter(Long date) {

		if (date == 0)
			return null;
		else
			return new Timestamp(date);

	}

	public DefectManager getDefectManager() {
		return defectManager;
	}

	public void setDefectManager(DefectManager defectManager) {
		this.defectManager = defectManager;
	}

	public ProblemCodeManager getProblemCodeManager() {
		return problemCodeManager;
	}

	public void setProblemCodeManager(ProblemCodeManager problemCodeManager) {
		this.problemCodeManager = problemCodeManager;
	}

	public FailureClassManager getFailureClassManager() {
		return failureClassManager;
	}

	public void setFailureClassManager(FailureClassManager failureClassManager) {
		this.failureClassManager = failureClassManager;
	}

	public DefectScheduleManager getDefectScheduleManager() {
		return defectScheduleManager;
	}

	public void setDefectScheduleManager(
			DefectScheduleManager defectScheduleManager) {
		this.defectScheduleManager = defectScheduleManager;
	}

	public DefectFormValidator getDefectFormValidator() {
		return defectFormValidator;
	}

	public void setDefectFormValidator(DefectFormValidator defectFormValidator) {
		this.defectFormValidator = defectFormValidator;
	}

	public EquipmentManager getEquipmentManager() {
		return equipmentManager;
	}

	public void setEquipmentManager(EquipmentManager equipmentManager) {
		this.equipmentManager = equipmentManager;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public CauseCodeManager getCauseCodeManager() {
		return causeCodeManager;
	}

	public void setCauseCodeManager(CauseCodeManager causeCodeManager) {
		this.causeCodeManager = causeCodeManager;
	}

	public ToolManager getToolManager() {
		return toolManager;
	}

	public void setToolManager(ToolManager toolManager) {
		this.toolManager = toolManager;
	}
}
