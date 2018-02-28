package hk.ebsl.mfms.webservice;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("accountjsonservice")
public interface AccountWebService_Json {

	/** Old method **/
	@POST
	@Path("updateAccountGroupAccount")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response updateAccountGroupAccount(LastModifyDateJson obj);
	/** End of Old method **/
	
	@POST
	@Path("getUpdateAccount")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateAccount(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateAccountGroup")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateAccountGroup(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateAccountGroupAccount")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateAllAccountGroupAccount(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdateAccountGroupResponsible")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdateAllAccountGroupResponsible(LastModifyDateJson obj);
	
	@POST
	@Path("getUpdatePrivilege")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response getUpdatePrivilege(LastModifyDateJson obj);
	
	@POST
	@Path("checkAccountLogin")
	@Consumes({MediaType.APPLICATION_JSON})
	public Response checkAccountLogin();

}
