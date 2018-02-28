package hk.ebsl.mfms.webservice;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("stpocservice")
public interface ShunTakPOCWebService {

    @GET
    @Path("invenq/{beId}/{proId}/{unitId}")
    public Response relayInventoryEnquiry(
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @PathParam("beId") Integer beId,
            @PathParam("proId") Integer proId,
            @PathParam("unitId") Integer unitId
    );

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("invsave/{moduleType}")
    public Response relayInventoryUpdate(
            String json,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @PathParam("moduleType") String moduleType
    );

    @GET
    @Path("invsave_get/{moduleType}")
    public Response relayInventoryUpdateUsingGet(
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo,
            @PathParam("moduleType") String moduleType
    );
}
