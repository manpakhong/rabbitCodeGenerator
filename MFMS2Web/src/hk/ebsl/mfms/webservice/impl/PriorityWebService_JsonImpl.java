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

import hk.ebsl.mfms.dto.Priority;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.manager.PriorityManager;
import hk.ebsl.mfms.utility.JsonUtil;
import hk.ebsl.mfms.webservice.PriorityWebService_Json;

import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

public class PriorityWebService_JsonImpl extends WebServiceUtil implements PriorityWebService_Json{
	
	public final static Logger logger = Logger.getLogger(PriorityWebService_JsonImpl.class);

	
	@Context
	private HttpHeaders headers;
	
	@Resource
	private Properties propertyConfigurer;
	
	@Resource
	private PriorityManager priorityManager;
	
	@Override
	public Response getUpdatePriority(LastModifyDateJson obj) {
		System.out.println("updatePriority json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");
			}

			@SuppressWarnings("rawtypes")
			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset()==null? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			
			Integer maxResult = (obj.getMaxResult()==null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = priorityManager.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<Priority> priorityList = priorityManager.searchPriorityByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			logger.debug("getUpdatePriority : count = " + theCountOfTotalResults + ", priorityList.size() = " + priorityList.size());

			WebServiceXml<Priority> xml = new WebServiceXml<Priority>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(priorityList.size());
			xml.setWebServiceXml(priorityList);
			rtn.add(xml);

			logger.debug("Return :"+JsonUtil.listToJsonString(rtn));
			
			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

}
