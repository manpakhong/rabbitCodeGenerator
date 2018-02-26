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

import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.utility.JsonUtil;
import hk.ebsl.mfms.web.controller.AccountManagementController;
import hk.ebsl.mfms.webservice.RoleWebService_Json;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

public class RoleWebService_JsonImpl extends WebServiceUtil implements RoleWebService_Json {
	
	public final static Logger logger = Logger.getLogger(RoleWebService_JsonImpl.class);

	
	@Context
	private HttpHeaders headers;

	@Resource
	private RoleManager roleManager;
	
	@Resource 
	private UserAccountManager userAccountManager;

	@Resource
	private Properties propertyConfigurer;

	@SuppressWarnings("rawtypes")
	@Override
	public Response getUpdateRole(LastModifyDateJson obj) {

		System.out.println("updateRole json");

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
			Integer siteKey = (obj.getSiteKey() == null ? null : obj.getSiteKey());
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = roleManager.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<Role> roleList = roleManager.searchRoleByDate(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, null);

			logger.debug("getUpdateRole : count = " + theCountOfTotalResults + ", roleList.size() = " + roleList.size());

			WebServiceXml<Role> xml = new WebServiceXml<Role>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("RoleTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(roleList.size());
			xml.setWebServiceXml(roleList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Response getUpdateAccountRole(LastModifyDateJson obj) {

		System.out.println("updateAccountRole json");

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
//			Integer siteKey = (obj.getSiteKey() == null ? null : obj.getSiteKey());
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = userAccountManager.searchResultCountByUserAccountRole(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<UserAccountRole> accountRoleList = userAccountManager.searchUserAccountRoleByDate(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			logger.debug("getUpdateAccountRole : count = " + theCountOfTotalResults + ", accountRoleList.size() = " + accountRoleList.size());

			WebServiceXml<UserAccountRole> xml = new WebServiceXml<UserAccountRole>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("UserAccountRoleTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(accountRoleList.size());
			xml.setWebServiceXml(accountRoleList);
			rtn.add(xml);
			
			
			logger.debug("Json Return : "+ JsonUtil.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Response getUpdateAccountRolePrivilege(LastModifyDateJson obj) {
		System.out.println("updateAccountRolePrivilege json");

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
//			Integer siteKey = (obj.getSiteKey() == null ? null : obj.getSiteKey());
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = roleManager.searchResultCountbyRolePrivilege(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<RolePrivilege> rolePrivilegeList = roleManager.searchRolePrivilegeByDate(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);
	
			logger.debug("getUpdateAccountRolePrivilege : count = " + theCountOfTotalResults + ", rolePrivilegeList.size() = " + rolePrivilegeList.size());

			WebServiceXml<RolePrivilege> xml = new WebServiceXml<RolePrivilege>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("UserAccountRolePrivilegeTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(rolePrivilegeList.size());
			xml.setWebServiceXml(rolePrivilegeList);
			rtn.add(xml); 

			logger.debug("Return :"+JsonUtil.listToJsonString(rtn));
			
			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
