package hk.ebsl.mfms.webservice;

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

import java.io.IOException;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface DefectWebService {

	@WebMethod
	public List<DefectXml> updateDefect(@WebParam(name = "lastModifyDate") long lastModifyDate,
			@WebParam(name = "siteKey") Integer siteKey,
			@WebParam(name = "accountKey") Integer accountKey,
			@WebParam(name = "groupKeys") String groupKeys);

	@WebMethod
	public List<ProblemCodeXml> updateProblemCode(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<PriorityXml> updatePriority(@WebParam(name = "lastModifyDate") long lastModifyDate);


	@WebMethod
	public List<LocationXml> updateLocation(@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<EquipmentXml> updateEquipment(@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<CauseCodeXml> updateCauseCode(@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<ToolXml> updateTool(@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<StatusXml> updateStatus(@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<StatusFlowXml> updateStatusFlow(@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<ScheduleXml> updateSchedule(@WebParam(name = "lastModifyDate") long lastModifyDate,
			@WebParam(name = "siteKey") Integer siteKey,
			@WebParam(name = "accountKey") Integer accountKey);

	@WebMethod
	public List<DefectPhotoXml> updateDefectPhoto(@WebParam(name = "lastModifyDate") long lastModifyDate,
			@WebParam(name = "siteKey") Integer siteKey) throws IOException;

	@WebMethod
	public List<DefectVideoXml> updateDefectVideo(@WebParam(name = "lastModifyDate") long lastModifyDate,
			@WebParam(name = "siteKey") Integer siteKey) throws IOException;

	@WebMethod
	public DefectXml addDefectPhoto(@WebParam(name = "defectKey") Integer defectKey,
			@WebParam(name = "createBy") Integer createBy, @WebParam(name = "data") String data,
			@WebParam(name = "createDateTime") Long createDateTime, @WebParam(name = "fileName") String fileName);

	@WebMethod
	public DefectXml addDefectVideo(@WebParam(name = "defectKey") Integer defectKey,
			@WebParam(name = "createBy") Integer createBy, @WebParam(name = "data") String data,
			@WebParam(name = "createDateTime") Long createDateTime, @WebParam(name = "fileName") String fileName);
	
//	@WebMethod
//	public DefectXml addDefectVideo(@WebParam(name = "defectKey") Integer defectKey,
//			@WebParam(name = "createBy") Integer createBy, @WebParam(name = "data") byte[] data,
//			@WebParam(name = "createDateTime") Long createDateTime, @WebParam(name = "fileName") String fileName);

	@WebMethod
	public DefectXml createModifyDefect(@WebParam(name = "key") Integer key,
			@WebParam(name = "siteKey") Integer siteKey, @WebParam(name = "code") String code,
			@WebParam(name = "locationKey") Integer locationKey,
			@WebParam(name = "failureClassKey") Integer failureClassKey,
			@WebParam(name = "problemCodeKey") Integer problemCodeKey,
			@WebParam(name = "causeCodeKey") Integer causeCodeKey, @WebParam(name = "toolKey") Integer toolKey,
			@WebParam(name = "equipmentKey") Integer equipmentKey, @WebParam(name = "contactName") String contactName,
			@WebParam(name = "contactTel") String contactTel, @WebParam(name = "contactEmail") String contactEmail,
			@WebParam(name = "emergencyTel") String emergencyTel,
			@WebParam(name = "assignedGroupKey") Integer assignedGroupKey,
			@WebParam(name = "assignedAccountKey") Integer assignedAccountKey,
			@WebParam(name = "priority") Integer priority, @WebParam(name = "desc") String desc,
			@WebParam(name = "reportDateTime") Long reportDateTime, @WebParam(name = "callFrom") String callFrom,
			@WebParam(name = "issueBy") Integer issueBy, @WebParam(name = "issueDateTime") Long issueDateTime,
			@WebParam(name = "targetStartDateTime") Long targetStartDateTime,
			@WebParam(name = "targetFinishDateTime") Long targetFinishDateTime,
			@WebParam(name = "actualStartDateTime") Long actualStartDateTime,
			@WebParam(name = "actualFinishDateTime") Long actualFinishDateTime,
			@WebParam(name = "checkBy") Integer checkBy, @WebParam(name = "checkDateTime") Long checkDateTime,
			@WebParam(name = "statusID") String statusID, @WebParam(name = "remarks") String remarks,
			@WebParam(name = "createBy") Integer createBy, @WebParam(name = "createDateTime") Long createDateTime,
			@WebParam(name = "lastModifyBy") Integer lastModifyBy,
			@WebParam(name = "lastModifyDateTime") Long lastModifyDateTime, @WebParam(name = "deleted") String deleted);

	@WebMethod
	public ScheduleHistoryXml addScheduleHistory(@WebParam(name = "siteKey") Integer siteKey,
			@WebParam(name = "scheduleKey") Integer scheduleKey, @WebParam(name = "finishDateTime") Long finishDateTime,
			@WebParam(name = "taskDateTime") Long taskDateTime);
	
	
	
	@WebMethod
	public List<ScheduleHistoryXml> updateScheduleHistory(@WebParam(name = "lastModifyDate") long lastModifyDate,
			@WebParam(name = "siteKey") Integer siteKey);
	

}
