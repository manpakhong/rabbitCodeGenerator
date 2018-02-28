package hk.ebsl.mfms.webservice;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("schedulejsonservice")
public interface MaintenanceScheduleWebService_Json {

	@POST
	@Path("createScheduleHistory")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response createScheduleHistory(List<DefectScheduleHistory> scheduleHistory);
	
	@POST
	@Path("getUpdateScheduleHistory")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateScheduleHistory(LastModifyDateJson obj);
	
	
}
