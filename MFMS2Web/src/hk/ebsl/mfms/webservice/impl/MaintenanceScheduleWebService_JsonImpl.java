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

import hk.ebsl.mfms.dto.CauseCode;
import hk.ebsl.mfms.dto.Defect;
import hk.ebsl.mfms.dto.DefectScheduleHistory;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.DefectScheduleManager;
import hk.ebsl.mfms.utility.JsonUtil;
import hk.ebsl.mfms.web.controller.DefectController;
import hk.ebsl.mfms.webservice.MaintenanceScheduleWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

public class MaintenanceScheduleWebService_JsonImpl extends WebServiceUtil implements MaintenanceScheduleWebService_Json {

	
	@Context
	private HttpHeaders headers;

	@Resource
	private DefectScheduleManager defectScheduleManager;

	@Resource
	private Properties propertyConfigurer;
	
	public static final Logger logger = Logger.getLogger(MaintenanceScheduleWebService_JsonImpl.class);
	
	@Override
	public Response createScheduleHistory(List<DefectScheduleHistory> scheduleHistory) {
		// TODO Auto-generated method stub
		
		logger.debug("createScheduleHistory");
		logger.debug("createScheduleHistory : " + JsonUtil.listToJsonString(scheduleHistory));
		
		
		if (checkEncryptedLogin(headers)) {
			System.out.println("Login Success");
		} else {

			System.out.println("Login Fail");

			return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
			// throw new MFMSWebServiceException("Login Fail");
		}

		List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

		try {
			// List<Integer> insertList = defectManager.saveDefect(defectList);

			for (DefectScheduleHistory sh : scheduleHistory) {

				logger.debug("scheduleHistory_KEy :" + sh.getKey());
				
				
				int insertKey = defectScheduleManager.createDefectScheduleHistory(sh);

				WebServiceXml<DefectScheduleHistory> xml = new WebServiceXml<DefectScheduleHistory>();
				xml.setMobileKey(sh.getMobileKey());
				xml.setXmlStatus("success");

				List<DefectScheduleHistory> tmp = new ArrayList<DefectScheduleHistory>();
				tmp.add(defectScheduleManager.getDefectScheduleHistoryByKey(insertKey));
				xml.setWebServiceXml(tmp);

				rtn.add(xml);
			}

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (MFMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();

		}
		
	}

	@Override
	public Response getUpdateScheduleHistory(LastModifyDateJson obj) {
		// TODO Auto-generated method stub
		
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
			Integer theCountOfTotalResults = defectScheduleManager
					.searchHistoryResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<DefectScheduleHistory> causeCodeList = defectScheduleManager.searchAllHistoryByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			WebServiceXml<DefectScheduleHistory> xml = new WebServiceXml<DefectScheduleHistory>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(causeCodeList.size());
			xml.setWebServiceXml(causeCodeList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

}
