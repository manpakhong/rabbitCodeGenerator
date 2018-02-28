package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("locationjsonservice")
public interface LocationWebService_Json {

	@POST
	@Path("getUpdateLocation")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateLocation(LastModifyDateJson obj);
	
	
	@POST
	@Path("getUpdateLocationPrivilege")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateLocationPrivilege(LastModifyDateJson obj);
	
	
	
}
