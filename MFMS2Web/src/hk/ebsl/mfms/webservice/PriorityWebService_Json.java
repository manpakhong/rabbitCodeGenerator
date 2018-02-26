package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("priorityjsonservice")
public interface PriorityWebService_Json {

	@POST
	@Path("getUpdatePriority")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdatePriority(LastModifyDateJson obj);
}
