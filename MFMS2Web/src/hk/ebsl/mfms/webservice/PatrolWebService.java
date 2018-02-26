package hk.ebsl.mfms.webservice;

import hk.ebsl.mfms.webservice.xml.PatrolPhotoXml;
import hk.ebsl.mfms.webservice.xml.PatrolResultXml;
import hk.ebsl.mfms.webservice.xml.PatrolScheduleXml;
import hk.ebsl.mfms.webservice.xml.RouteDefXml;
import hk.ebsl.mfms.webservice.xml.RouteLocationXml;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.http.MediaType;

@WebService
public interface PatrolWebService {

	@WebMethod
	public List<RouteDefXml> updateRouteDef(
			@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public List<RouteLocationXml> updateRouteLocation(
			@WebParam(name = "lastModifyDate") long lastModifyDate);

	@WebMethod
	public PatrolResultXml updatePatrolResult(
			@WebParam(name = "patrolScheduleKey") int patrolScheduleKey,
			@WebParam(name = "mobileKey") int mobileKey,
			@WebParam(name = "siteKey") int siteKey,
			@WebParam(name = "resultKey") int resultKey,
			@WebParam(name = "routeDefKey") int routeDefKey,
			@WebParam(name = "groupNum") int groupNum,
			@WebParam(name = "correctLocationKey") int correctLocationKey,
			@WebParam(name = "locationKey") int locationKey,
			@WebParam(name = "seqNum") int seqNum,
			@WebParam(name = "personAttended") int personAttended,
			@WebParam(name = "timeAttended") long timeAttended,
			@WebParam(name = "reason") String reason,
			@WebParam(name = "completed") String completed,
			@WebParam(name = "createBy") int createBy,
			@WebParam(name = "createDateTime") long createDateTime,
			@WebParam(name = "lastModifyBy") int lastModifyBy,
			@WebParam(name = "lastModifyDateTime") long lastModifyDateTime,
			@WebParam(name = "deleted") String deleted,
			@WebParam(name = "patrolStatus") String patrolStatus);

	@WebMethod
	public List<PatrolScheduleXml> updatePatrolSchedule(
			@WebParam(name = "lastModifyDate") long lastModifyDate);

	// @WebMethod
	// public List<RouteResultXml> createRouteResult()
	@WebMethod
	public List<PatrolResultXml> getPreviousPatrolResult();

	@WebMethod
	public List<PatrolPhotoXml> uploadPatrolPhoto(
			@WebParam(name = "mobileKey") int mobileKey,
			@WebParam(name = "siteKey") int siteKey,
			@WebParam(name = "routeDefKey") int routeDefKey,
			@WebParam(name = "patrolResultKey") int patrolResultKey,
			@WebParam(name = "locationKey") int locationKey,
			@WebParam(name = "remark") String remark,
			@WebParam(name = "createBy") int createBy,
			@WebParam(name = "createDateTime") long createDateTime,
			@WebParam(name = "lastModifyBy") int lastModifyBy,
			@WebParam(name = "lastModifyDateTime") long lastModifyDateTime,
			@WebParam(name = "deleted") String deleted,
			@WebParam(name = "photo") byte[] photo);
	
//	@POST
//	@Path("/updatePatrolResult")
//	@Consumes(MediaType.APPLICATION_JSON_VALUE)
//	@Produces(MediaType.APPLICATION_JSON_VALUE)
//	public String updatePatrolResultByJson(String jsonArray);

}
