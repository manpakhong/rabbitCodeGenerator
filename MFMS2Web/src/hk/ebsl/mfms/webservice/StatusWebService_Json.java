package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("statusjsonservice")
public interface StatusWebService_Json {

	@POST
	@Path("getUpdateStatus")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response getUpdateStatus(LastModifyDateJson obj);

	
	@POST
	@Path("getUpdateStatusFlow")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateStatusFlow(LastModifyDateJson obj);
	
	
	@POST
	@Path("getUpdateStatusAccessMode")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateStatusAccessMode(LastModifyDateJson obj);
}
