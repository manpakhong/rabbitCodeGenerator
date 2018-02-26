package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("rolejsonservice")
public interface RoleWebService_Json {
	
	@POST
	@Path("getUpdateRole")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateRole(LastModifyDateJson obj);

	@POST
	@Path("getUpdateAccountRole")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateAccountRole(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateAccountRolePrivilege")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateAccountRolePrivilege(LastModifyDateJson obj);
}
