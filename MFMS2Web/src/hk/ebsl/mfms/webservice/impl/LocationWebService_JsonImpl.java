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

import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.Location;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.webservice.LocationWebService_Json;
import hk.ebsl.mfms.webservice.xml.AccountGroupAccountXml;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

public class LocationWebService_JsonImpl extends WebServiceUtil implements LocationWebService_Json {

	public final static Logger logger = Logger.getLogger(LocationWebService_JsonImpl.class);

	@Context
	private HttpHeaders headers;

	@Resource
	private LocationManager locationManager;

	@Resource
	private Properties propertyConfigurer;

	@SuppressWarnings("rawtypes")
	public Response getUpdateLocation(LastModifyDateJson obj) {
		// TODO Auto-generated method stub

		System.out.println("updateLocation json");

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
			Integer theCountOfTotalResults = locationManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<Location> locationList = locationManager.searchLocationByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateLocation : count = " + theCountOfTotalResults + ", locationList.size() = " + locationList.size());

			WebServiceXml<Location> xml = new WebServiceXml<Location>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(locationList.size());
			xml.setWebServiceXml(locationList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Response getUpdateLocationPrivilege(LastModifyDateJson obj) {
		System.out.println("updateLocationPrivilege json");

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
			Integer theCountOfTotalResults = locationManager.searchResultCountByLocationPrivilege(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<LocationPrivilege> locationPrivilegeList = locationManager.searchLocationPrivilegeByDate(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateLocationPrivilege : count = " + theCountOfTotalResults + ", locationPrivilegeList.size() = " + locationPrivilegeList.size());

			WebServiceXml<LocationPrivilege> xml = new WebServiceXml<LocationPrivilege>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(locationPrivilegeList.size());
			xml.setWebServiceXml(locationPrivilegeList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
