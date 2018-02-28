package hk.ebsl.mfms.webservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.web.controller.DefectScheduleController;

import javax.annotation.Resource;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.ws.handler.MessageContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class WebServiceUtil {

	public final static Logger logger = Logger
			.getLogger(WebServiceUtil.class);
	protected final String account = "account";
	protected final String password = "password";

	private static final Integer SYS_ADMIN_ROLE_KEY = 1;

	@Resource
	private UserAccountManager userAccountManager;

	public Boolean checkLogin(MessageContext msgCtxt) {

		try {
			Map map = (Map) msgCtxt.get(MessageContext.HTTP_REQUEST_HEADERS);
			ArrayList ac = (ArrayList) map.get(account);
			ArrayList pwd = (ArrayList) map.get(password);

			System.out.println("AC :" + ac.get(0) + " || PWD : " + pwd.get(0));

			UserAccount userAccount = userAccountManager.checkUserLogin((String) ac.get(0), (String) pwd.get(0));

			if (userAccount == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public Boolean checkLogin(HttpHeaders headers) {
		try {
			List<String> ac = headers.getRequestHeader("account");
			List<String> pwd = headers.getRequestHeader("password");

			System.out.println("AC :" + ac.get(0) + " || PWD : " + pwd.get(0));

			UserAccount userAccount = userAccountManager.checkUserLogin((String) ac.get(0), (String) pwd.get(0));

			if (userAccount == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public Boolean checkEncryptedLogin(HttpHeaders headers) {
		try {

			UserAccount userAccount = getUserAccount(headers);

			if (userAccount == null) {
				return false;
			} else {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public UserAccount getUserAccount(HttpHeaders headers) {

		return getUserAccount(headers, false);

	}

	public UserAccount getUserAccount(HttpHeaders headers, Boolean isRawPw) {

		try {
			List<String> ac = headers.getRequestHeader("account");
			List<String> pwd = headers.getRequestHeader("password");

			UserAccount userAccount = null;
			
			logger.debug("Check User AC : "+ac.get(0)+"/"+pwd.get(0));

			if (((String) ac.get(0)).equals("sysadmin")) {
				userAccount = userAccountManager.checkUserLogin((String) ac.get(0), (String) pwd.get(0));
			} else {

				if (isRawPw) {
					userAccount = userAccountManager.checkUserLogin((String) ac.get(0), (String) pwd.get(0));
				} else {
					userAccount = userAccountManager.checkUserEncryptedLogin((String) ac.get(0), (String) pwd.get(0));
				}
			}

			return userAccount;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public UserAccount getUserAccountWithRawPW(HttpHeaders headers){
		try {
			List<String> ac = headers.getRequestHeader("account");
			List<String> pwd = headers.getRequestHeader("password");

			UserAccount userAccount = null;

			if (((String) ac.get(0)).equals("sysadmin")) {
				userAccount = userAccountManager.checkUserLogin(
						(String) ac.get(0), (String) pwd.get(0));
			} else {
				userAccount = userAccountManager.checkUserLogin(
						(String) ac.get(0), (String) pwd.get(0));
			}
			
			return userAccount;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public Boolean hasStaticDataPrivilege(MessageContext msgCtxt) {

		// try {
		// Map map = (Map) msgCtxt.get(MessageContext.HTTP_REQUEST_HEADERS);
		// ArrayList ac = (ArrayList) map.get(account);
		//
		// System.out.println("hasStaticDataPrivilege()[" + ac.get(0) + "]");
		//
		// String loginId = (String)ac.get(0);
		//
		// //search for sys admin role
		//
		// List<UserAccountRole> userAccountRoleList =
		// userAccountManager.searchUserAccountRole(null, SYS_ADMIN_ROLE_KEY,
		// loginId, null, "A");
		//
		// if (userAccountRoleList != null && userAccountRoleList.size() > 0) {
		// return true;
		// } else {
		// return false;
		// }
		// } catch (Exception | MFMSException e) {
		// e.printStackTrace();
		// return false;
		// }
		return true;
	}

}
