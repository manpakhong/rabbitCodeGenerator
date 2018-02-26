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

import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.webservice.SiteWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.SiteXml;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

public class SiteWebService_JsonImpl extends WebServiceUtil implements SiteWebService_Json {

	public final static Logger logger = Logger.getLogger(SiteWebService_JsonImpl.class);

	@Context
	private HttpHeaders headers;
	
	@Resource
	private Properties propertyConfigurer;

	@Resource
	private SiteManager siteManager;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Response getUpdateSite(LastModifyDateJson obj) {
		// TODO Auto-generated method stub

		System.out.println("updateSite json");

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
			
			Integer maxResult = (obj.getMaxResult()==null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = siteManager.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<Site> siteList = siteManager.searchSiteByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			logger.debug("getUpdateSite : count = " + theCountOfTotalResults + ", accountList.size() = " + siteList.size());

			WebServiceXml<Site> xml = new WebServiceXml<Site>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(siteList.size());
			xml.setWebServiceXml(siteList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

}
