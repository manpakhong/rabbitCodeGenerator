package hk.ebsl.mfms.webservice.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.Privilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.AccountGroupResponsibleManager;
import hk.ebsl.mfms.manager.PrivilegeManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.utility.JsonUtil;
import hk.ebsl.mfms.webservice.AccountWebService_Json;
import hk.ebsl.mfms.webservice.xml.AccountGroupAccountXml;
import hk.ebsl.mfms.webservice.xml.LastModifyDateJson;
import hk.ebsl.mfms.webservice.xml.WebServiceXml;

@SuppressWarnings("rawtypes")
public class AccountWebService_JsonImpl extends WebServiceUtil implements AccountWebService_Json {

	public final static Logger logger = Logger.getLogger(AccountWebService_JsonImpl.class);

	@Context
	private HttpHeaders headers;

	@Resource
	private UserAccountManager userAccountManager;

	@Resource
	private AccountGroupManager accountGroupManager;
	@Resource
	private AccountGroupAccountManager accountGroupAccountManager;

	@Resource
	private AccountGroupResponsibleManager accountGroupResponsibleManager;

	@Resource
	private PrivilegeManager privilegeManager;

	@Resource
	private Properties propertyConfigurer;

	@Override
	public Response updateAccountGroupAccount(LastModifyDateJson obj) {
		// TODO Auto-generated method stub

		System.out.println("updateAccountGroupAccount json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();
				// throw new MFMSWebServiceException("Login Fail");

			}

			List<AccountGroupAccountXml> rtn = new ArrayList<AccountGroupAccountXml>();

			long lastModifyDateTime = obj.getAccountGroupLastModifyDate();
			long responsibleAccountLastModifyDate = obj.getResponisbleLastModifyDate();

			List<AccountGroupAccount> accountGroupList = accountGroupAccountManager
					.getAccountGroupAccountByDate(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));

			for (AccountGroupAccount aca : accountGroupList) {

				AccountGroupAccountXml xml = new AccountGroupAccountXml();
				xml.setKey(aca.getKey());
				xml.setGroupKey(aca.getGroupKey());
				xml.setAccountKey(aca.getAccountKey());
				xml.setCreatedBy(aca.getCreatedBy());
				xml.setCreateDateTime(aca.getCreateDateTime().getTime());
				xml.setLastModifyBy(aca.getLastModifyBy());
				xml.setLastModifyDateTime(
						aca.getLastModifyDateTime() == null ? 0 : aca.getLastModifyDateTime().getTime());
				xml.setDeleted(aca.getDeleted());
				xml.setIsResponsibleAc("N");

				rtn.add(xml);
			}

			List<AccountGroupResponsible> responsibleAcList = accountGroupResponsibleManager
					.getAccountGroupResponsibleByDate(responsibleAccountLastModifyDate == 0 ? null
							: new Timestamp(responsibleAccountLastModifyDate));
			for (AccountGroupResponsible agr : responsibleAcList) {

				AccountGroupAccountXml xml = new AccountGroupAccountXml();
				xml.setKey(agr.getKey());
				xml.setGroupKey(agr.getGroupKey());
				xml.setAccountKey(agr.getAccountKey());
				xml.setCreatedBy(agr.getCreatedBy());
				xml.setCreateDateTime(agr.getCreateDateTime().getTime());
				xml.setLastModifyBy(agr.getLastModifyBy());
				xml.setLastModifyDateTime(
						agr.getLastModifyDateTime() == null ? 0 : agr.getLastModifyDateTime().getTime());
				xml.setDeleted(agr.getDeleted());
				xml.setIsResponsibleAc("Y");

				rtn.add(xml);
			}

			logger.debug("Result : " + JsonUtil.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateAccount(LastModifyDateJson obj) {

		System.out.println("updateAccount json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults = userAccountManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));
			List<UserAccount> accountList = userAccountManager.searchUserAccountByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);

			logger.debug("getUpdateAccount : count = " + theCountOfTotalResults + ", accountList.size() = "
					+ accountList.size());

			WebServiceXml<UserAccount> xml = new WebServiceXml<UserAccount>();
			// xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("AccountTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(accountList.size());
			xml.setWebServiceXml(accountList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateAccountGroup(LastModifyDateJson obj) {

		System.out.println("updateAccountGroup json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long lastModifyDateTime = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer siteKey = (obj.getSiteKey() == null ? null : obj.getSiteKey());
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			// Integer theCountOfTotalResults =
			// accountGroupManager.searchResultCount(lastModifyDateTime == 0 ?
			// null : new Timestamp(lastModifyDateTime));
			Integer theCountOfTotalResults = accountGroupManager.searchResultCountWithSiteKey(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), siteKey);
			List<AccountGroup> accountGroupList = accountGroupManager.searchAccountGroupByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult, siteKey);

			logger.debug("getUpdateAccountGroup : count = " + theCountOfTotalResults + ", accountGroupList.size() = "
					+ accountGroupList.size());

			WebServiceXml<AccountGroup> xml = new WebServiceXml<AccountGroup>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("AccountGroupTesting");
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(accountGroupList.size());
			xml.setWebServiceXml(accountGroupList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}
	//
	// @Override
	// public Response getUpdateAllAccountGroupAccount(LastModifyDateJson obj) {
	// // TODO Auto-generated method stub
	// return null;
	// }

	@Override
	public Response getUpdateAllAccountGroupAccount(LastModifyDateJson obj) {
		System.out.println("updateAllAccountGroupAccount json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			long accountGroupLastModifyDate = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			Integer theCountOfTotalResults_aga = accountGroupAccountManager.searchResultCount(
					accountGroupLastModifyDate == 0 ? null : new Timestamp(accountGroupLastModifyDate));
			List<AccountGroupAccount> agaList = accountGroupAccountManager.searchAccountGroupAccountByDate(
					accountGroupLastModifyDate == 0 ? null : new Timestamp(accountGroupLastModifyDate), offset,
					maxResult);

			logger.debug("getUpdateAllAccountGroupAccount : count = " + theCountOfTotalResults_aga
					+ ", agaList.size() = " + agaList.size());

			WebServiceXml<AccountGroupAccount> xml = new WebServiceXml<AccountGroupAccount>();
			xml.setMobileKey(852);
			xml.setXmlStatus("OK");
			xml.setXmlMsg("AccountGroupAccountTesting");
			xml.setTotal(theCountOfTotalResults_aga);
			xml.setCount(agaList.size());
			xml.setWebServiceXml(agaList);
			rtn.add(xml);

			// long responisbleLastModifyDate =
			// obj.getResponisbleLastModifyDate();
			// Integer theCountOfTotalResults_agr =
			// accountGroupResponsibleManager.searchResultCount(responisbleLastModifyDate
			// == 0 ? null : new Timestamp(responisbleLastModifyDate));
			// List<AccountGroupResponsible> agrList =
			// accountGroupResponsibleManager.searchAccountGroupResponsibleByDate(responisbleLastModifyDate
			// == 0 ? null : new Timestamp(responisbleLastModifyDate), offset,
			// maxResult);
			//
			// AccountGroupAccountXml<AccountGroupResponsible> agrxml = new
			// AccountGroupAccountXml<AccountGroupResponsible>();
			// agrxml.setMobileKey(852);
			// agrxml.setXmlStatus("OK");
			// agrxml.setXmlMsg("AccountGroupResponsibleTesting");
			// agrxml.setIsResponsibleAc("Y");
			// agrxml.setTotal(theCountOfTotalResults_agr);
			// agrxml.setCount(agaList.size());
			// agrxml.setAgaList(agrList);
			// rtn.add(agrxml);

			logger.debug("Result:" + JsonUtil.listToJsonString(rtn));

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdatePrivilege(LastModifyDateJson obj) {
		// TODO Auto-generated method stub

		System.out.println("updatePrivilege json");

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
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));

			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			List<Privilege> privilegeList = privilegeManager.searchPrivilegeByDate(
					lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime), offset, maxResult);
			Integer theCountOfTotalResults = privilegeManager
					.searchResultCount(lastModifyDateTime == 0 ? null : new Timestamp(lastModifyDateTime));

			WebServiceXml<Privilege> xml = new WebServiceXml<Privilege>();
			xml.setTotal(theCountOfTotalResults);
			xml.setCount(privilegeList.size());
			xml.setWebServiceXml(privilegeList);
			rtn.add(xml);

			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response getUpdateAllAccountGroupResponsible(LastModifyDateJson obj) {
		System.out.println("updateAllAccountGroupResponsible json");

		try {
			if (checkEncryptedLogin(headers)) {
				System.out.println("Login Success");
			} else {

				System.out.println("Login Fail");

				return Response.status(Response.Status.UNAUTHORIZED).entity("Login Fail").build();

			}

			List<WebServiceXml> rtn = new ArrayList<WebServiceXml>();

			// long accountGroupLastModifyDate = obj.getLastModifyDate();
			Integer offset = (obj.getOffset() == null ? 0 : obj.getOffset());
			Integer defaultMaxResult = Integer.parseInt(propertyConfigurer.getProperty("webservice.default.max"));
			Integer maxResult = (obj.getMaxResult() == null ? defaultMaxResult : obj.getMaxResult());
			// Integer theCountOfTotalResults_aga =
			// accountGroupAccountManager.searchResultCount(accountGroupLastModifyDate
			// == 0 ? null : new Timestamp(accountGroupLastModifyDate));
			// List<AccountGroupAccount> agaList =
			// accountGroupAccountManager.searchAccountGroupAccountByDate(accountGroupLastModifyDate
			// == 0 ? null : new Timestamp(accountGroupLastModifyDate), offset,
			// maxResult);
			//
			// WebServiceXml<AccountGroupAccount> xml = new
			// WebServiceXml<AccountGroupAccount>();
			// xml.setMobileKey(852);
			// xml.setXmlStatus("OK");
			// xml.setXmlMsg("AccountGroupAccountTesting");
			// xml.setTotal(theCountOfTotalResults_aga);
			// xml.setCount(agaList.size());
			// xml.setWebServiceXml(agaList);
			// rtn.add(xml);

			long responisbleLastModifyDate = obj.getLastModifyDate();
			Integer theCountOfTotalResults_agr = accountGroupResponsibleManager.searchResultCount(
					responisbleLastModifyDate == 0 ? null : new Timestamp(responisbleLastModifyDate));
			List<AccountGroupResponsible> agrList = accountGroupResponsibleManager.searchAccountGroupResponsibleByDate(
					responisbleLastModifyDate == 0 ? null : new Timestamp(responisbleLastModifyDate), offset,
					maxResult);

			logger.debug("getUpdateAllAccountGroupResponsible : count = " + theCountOfTotalResults_agr
					+ ", agrList.size() = " + agrList.size());

			WebServiceXml<AccountGroupResponsible> agrxml = new WebServiceXml<AccountGroupResponsible>();
			agrxml.setMobileKey(852);
			agrxml.setXmlStatus("OK");
			agrxml.setXmlMsg("AccountGroupResponsibleTesting");
			agrxml.setTotal(theCountOfTotalResults_agr);
			agrxml.setCount(agrList.size());
			agrxml.setWebServiceXml(agrList);
			rtn.add(agrxml);

			logger.debug("Result:" + JsonUtil.listToJsonString(rtn));
			return Response.ok(rtn, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

	}

	@Override
	public Response checkAccountLogin() {
		System.out.println("checkAccountLogin json");
		int result = LoginStatus.CriticalError.value;

		try {

			UserAccount existingAccount = getUserAccount(headers, true);

			if (null == existingAccount) {
				result = LoginStatus.AccountNotFound.value;
				existingAccount = new UserAccount();
			} else {
				String bCryptPW = BCrypt.hashpw(password, existingAccount.getPassword());
				if ((BCrypt.checkpw(password, bCryptPW))) {
					if (existingAccount.getStatus().equals(UserAccount.STATUS_SUSPENDED)) {
						result = LoginStatus.AccountSuspended.value;
					} else {
						result = LoginStatus.LoginOk.value;
					}
				}
			}

			existingAccount.setCheckLoginStatus(result);

			logger.debug("checkAccountLogin : " + headers.getRequestHeader("account") + ", result = " + result);

			return Response.ok(existingAccount, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();

			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}

	public enum LoginStatus {
		LoginOk(100), AccountNotFound(101), PasswordIncorrect(102), AccountSuspended(103), CriticalError(999);

		private int value;
		private static final Map<Integer, LoginStatus> statusMap = new HashMap<Integer, LoginStatus>();

		private LoginStatus(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		static {
			for (LoginStatus ls : LoginStatus.values()) {
				statusMap.put(ls.value, ls);
			}
		}
		public static LoginStatus valueOf(int value) {
			LoginStatus type = statusMap.get(value);
			if (type == null)
				return null;
			return type;
		}

	}

}
