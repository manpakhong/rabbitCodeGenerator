package hk.ebsl.mfms.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectPhoto;
import hk.ebsl.mfms.dto.DefectVideo;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("defectjsonservice")
public interface DefectWebService_Json {
	
	@POST
	@Path("getUpdateCauseCode")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateCauseCode(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateProblemCode")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateProblemCode(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateTool")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateTool(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateEquipment")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateEquipment(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateDefect")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateDefect(LastModifyDateJson obj);
	
	@POST
	@Path("createDefect")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createDefect(List<Defect> defect);
	
	
	@POST
	@Path("createDefectPhoto")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createDefectPhoto(@Multipart("defectPhotoList")List<DefectPhoto> list, @Multipart("files") List<Attachment> files);
	
	
	@POST
	@Path("createDefectVideo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response createDefectVideo(@Multipart("defectVideoList")List<DefectVideo> list, @Multipart("files") List<Attachment> files);
	
	
	@POST
	@Path("getUpdateDefectPhoto")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateDefectPhoto(LastModifyDateJson obj);
	
	@POST
	@Path("downloadDefectPhoto")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadDefectPhoto(DefectPhoto defectPhoto);
	
	
	@POST
	@Path("getUpdateSchedule")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateSchedule(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateScheduleAccount")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateScheduleAccount(LastModifyDateJson obj);
	
	
	@POST
	@Path("getUpdateDefectVideo")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateDefectVideo(LastModifyDateJson obj);
	
	@POST
	@Path("downloadDefectVideo")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response downloadDefectVideo(DefectVideo defectVideo);
	
	
	
}
