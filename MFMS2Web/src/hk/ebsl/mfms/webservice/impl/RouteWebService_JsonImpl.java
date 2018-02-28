package hk.ebsl.mfms.webservice.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import hk.ebsl.mfms.dto.PatrolResult;
import hk.ebsl.mfms.dto.RouteDef;
import hk.ebsl.mfms.dto.RouteLocation;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.manager.PatrolResultManager;
import hk.ebsl.mfms.manager.RouteDefManager;
import hk.ebsl.mfms.webservice.RouteWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

@SuppressWarnings("rawtypes")
public class RouteWebService_JsonImpl extends WebServiceUtil implements RouteWebService_Json {

	public final static Logger logger = Logger.getLogger(RouteWebService_JsonImpl.class);

	@Context
	private HttpHeaders headers;

	@Resource
	private Properties propertyConfigurer;

	@Resource
	private RouteDefManager routeDefManager;
	
	@Resource
	private PatrolResultManager patrolResultManager;

	@Override
	public Response getUpdateRouteDef(LastModifyDateJson obj) {
		System.out.println("updateRouteDef json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = routeDefManager
					.searchRouteDefResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<RouteDef> routeDefList = routeDefManager.searchRouteDefByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateRouteDef : count = " + theCountOfTotalResults + ", routeDefList.size() = " + routeDefList.size());

			WebServiceXml<RouteDef> xml = new WebServiceXml<RouteDef>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(routeDefList.size());
			xml.setWebServiceXml(routeDefList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateRouteLocation(LastModifyDateJson obj) {
		System.out.println("updateRouteLocation json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer siteKey = obj.getSiteKey();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = routeDefManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<RouteLocation> routeLocationList = routeDefManager.searchRouteLocationByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateRouteLocation : count = " + theCountOfTotalResults + ", routeLocationList.size() = " + routeLocationList.size());

			WebServiceXml<RouteLocation> xml = new WebServiceXml<RouteLocation>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(routeLocationList.size());
			xml.setWebServiceXml(routeLocationList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}
}
