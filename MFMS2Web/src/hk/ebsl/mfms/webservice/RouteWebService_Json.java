package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("routejsonservice")
public interface RouteWebService_Json {
	
	@POST
	@Path("getUpdateRouteDef")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateRouteDef(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateRouteLocation")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateRouteLocation(LastModifyDateJson obj);
	
	
}
