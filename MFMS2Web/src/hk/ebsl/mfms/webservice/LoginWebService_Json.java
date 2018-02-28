package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface LoginWebService_Json {

	
	@POST
	@Path("checkLogin")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response checkLogin();
	
}
