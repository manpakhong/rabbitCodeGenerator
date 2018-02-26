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

import hk.ebsl.mfms.dto.Status;
import hk.ebsl.mfms.dto.StatusAccessMode;
import hk.ebsl.mfms.dto.StatusFlow;
import hk.ebsl.mfms.manager.StatusAccessModeManager;
import hk.ebsl.mfms.manager.StatusFlowManager;
import hk.ebsl.mfms.manager.StatusManager;
import hk.ebsl.mfms.utility.JsonUtil;
import hk.ebsl.mfms.webservice.StatusWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

@SuppressWarnings("rawtypes")
public class StatusWebService_JsonImpl extends WebServiceUtil implements StatusWebService_Json {

	public final static Logger logger = Logger.getLogger(StatusWebService_JsonImpl.class);

	
	
	@Context
	private HttpHeaders headers;

	@Resource
	private StatusManager statusManager;

	@Resource
	private StatusFlowManager statusFlowManager;

	@Resource
	private StatusAccessModeManager statusAccessModeManager;

	@Resource
	private Properties propertyConfigurer;

	public Response getUpdateStatus(LastModifyDateJson obj) {

		System.out.println("updateStatus json");

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
			Integer offset = (obj.getOffset()==null? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = statusManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<Status> statusList = statusManager.searchStatusByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			logger.debug("getUpdateStatus : count = " + theCountOfTotalResults + ", statusList.size() = " + statusList.size());

			WebServiceXml<Status> xml = new WebServiceXml<Status>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("StatusTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(statusList.size());
			xml.setWebServiceXml(statusList);
			rtn.add(xml);
			// }

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	@Override
	public Response getUpdateStatusFlow(LastModifyDateJson obj) {

		System.out.println("updateStatusFlow json");

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
			Integer offset = (obj.getOffset()==null? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = statusFlowManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<StatusFlow> statusFlowList = statusFlowManager.searchStatusFlowByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			logger.debug("getUpdateStatusFlow : count = " + theCountOfTotalResults + ", statusFlowList.size() = " + statusFlowList.size());

			WebServiceXml<StatusFlow> xml = new WebServiceXml<StatusFlow>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("StatusFlowTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(statusFlowList.size());
			xml.setWebServiceXml(statusFlowList);
			rtn.add(xml);

			logger.debug("Return :"+JsonUtil.listToJsonString(rtn));
			
			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateStatusAccessMode(LastModifyDateJson obj) {

		System.out.println("updateStatusAccessMode json");

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
			Integer offset = (obj.getOffset()==null? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = statusAccessModeManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<StatusAccessMode> statusAccessModeList = statusAccessModeManager.searchStatusAccessModeByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			WebServiceXml<StatusAccessMode> xml = new WebServiceXml<StatusAccessMode>();
//			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("StatusAccessModeTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(statusAccessModeList.size());
			xml.setWebServiceXml(statusAccessModeList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}
}
