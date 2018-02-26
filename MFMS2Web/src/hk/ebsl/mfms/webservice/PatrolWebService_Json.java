package hk.ebsl.mfms.webservice;

import java.util.List;

import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.PatrolPhoto;
import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.PatrolSchedule;
import hk.ebsl.mfms.webservice.impl.TestObj;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.PatrolResultXml;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;


@Path("patroljsonservice")
public interface PatrolWebService_Json {
	
	@POST
	@Path("updatePatrolResult")
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updatePatrolResult(List<PatrolResultXml> result);
//	public List<PatrolResultXml> updatePatrolResult(List<PatrolResultXml> result);
	
	
	@POST
	@Path("createAndUpdatePatrolResult")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createAndUpdatePatrolResult(List<PatrolResult> result);
	
	
	@POST
	@Path("createPatrolPhoto")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createPatrolPhoto(@Multipart("patrolPhotoList")List<PatrolPhoto> list, @Multipart("files") List<Attachment> files);
	
	
	//getPrevious Result
	@POST
	@Path("getPreviousPatrolResult")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getPreviousPatrolResult();
	
	//get Schedule update
	@POST
	@Path("updatePatrolSchedule")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updatePatrolSchedule(LastModifyDateJson obj);
	
	@POST
	@Path("updatePatrolScheduleAccount")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updatePatrolScheduleAccount(LastModifyDateJson obj);
	
}
