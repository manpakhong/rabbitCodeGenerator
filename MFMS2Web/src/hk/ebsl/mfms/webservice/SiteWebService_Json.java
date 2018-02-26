package hk.ebsl.mfms.webservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;

@Path("sitejsonservice")
public interface SiteWebService_Json {	
	
	
		@POST
		@Path("getUpdateSite")
		@Consumes({MediaType.APPLICATION_JSON})
		public Response getUpdateSite(LastModifyDateJson obj);
	
}
