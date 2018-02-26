package hk.ebsl.mfms.webservice.impl;

import hk.ebsl.mfms.dto.AccountGroup;
import hk.ebsl.mfms.dto.AccountGroupAccount;
import hk.ebsl.mfms.dto.AccountGroupResponsible;
import hk.ebsl.mfms.dto.LocationPrivilege;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.exception.MFMSWebServiceException;
import hk.ebsl.mfms.manager.AccountGroupAccountManager;
import hk.ebsl.mfms.manager.AccountGroupManager;
import hk.ebsl.mfms.manager.AccountGroupResponsibleManager;
import hk.ebsl.mfms.manager.LocationManager;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;
import hk.ebsl.mfms.webservice.AccountWebService;
import hk.ebsl.mfms.webservice.xml.AccountGroupAccountXml;
import hk.ebsl.mfms.webservice.xml.AccountGroupResponsibleXml;
import hk.ebsl.mfms.webservice.xml.AccountGroupXml;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlMsg;
import hk.ebsl.mfms.webservice.xml.BaseXml.XmlStatus;
import hk.ebsl.mfms.webservice.xml.LocationPrivilegeXml;
import hk.ebsl.mfms.webservice.xml.PrivilegeXml;
import hk.ebsl.mfms.webservice.xml.RolePrivilegeXml;
import hk.ebsl.mfms.webservice.xml.RoleXml;
import hk.ebsl.mfms.webservice.xml.SiteXml;
import hk.ebsl.mfms.webservice.xml.UserAccountRoleXml;
import hk.ebsl.mfms.webservice.xml.UserAccountXml;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@WebService(endpointInterface = "hk.ebsl.mfms.webservice.AccountWebService", serviceName = "accountWebService")
public class AccountWebServiceImpl extends WebServiceUtil implements AccountWebService {

	public final static Logger logger = Logger.getLogger(AccountWebServiceImpl.class);

	@Resource
	private WebServiceContext wsContext;

	@Resource
	private UserAccountManager userAccountManager;

	@Resource
	private SiteManager siteManager;

	@Resource
	private LocationManager locationManager;

	@Resource
	private RoleManager roleManager;

	@Resource
	private AccountGroupManager accountGroupManager;

	@Resource
	private AccountGroupAccountManager accountGroupAccountManager;

	@Resource
	private AccountGroupResponsibleManager accountGroupResponsibleManager;

	@Override
	public List<UserAccountXml> updateUserAccount(long lastModifyDate) {

		// get all accounts

		List<UserAccountXml> xmlList = new ArrayList<UserAccountXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}

			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			//
			List<UserAccount> userAccountList = userAccountManager.getAllUserAccount();

			if (userAccountList != null) {
				for (UserAccount a : userAccountList) {

					Timestamp t = a.getLastModifyDateTime();

					if (t == null) {
						t = a.getCreateDateTime();
					}
					if (lastModifyDate >= t.getTime()) {
						continue;
					}

					// create XML object
					UserAccountXml xml = new UserAccountXml();
					xml.setKey(a.getKey());
					xml.setLoginId(a.getLoginId());
					xml.setName(a.getName());
					xml.setPassword(a.getPassword());
					xml.setContactAreaCode(a.getContactAreaCode());
					xml.setContactCountryCode(a.getContactCountryCode());
					xml.setContactNumber(a.getContactNumber());
					xml.setCreateBy(a.getCreateBy());
					if (a.getCreateDateTime() != null) {
						xml.setCreateDateTime(a.getCreateDateTime().getTime());
					}
					xml.setDeleted(a.getDeleted());

					xml.setEmail(a.getEmail());
					xml.setLastModifyBy(a.getLastModifyBy());
					if (a.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(a.getLastModifyDateTime().getTime());
					} else
						xml.setLastModifyDateTime(a.getCreateDateTime().getTime());

					xml.setStatus(a.getStatus());
					xml.setTagId(a.getTagId());
					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			UserAccountXml xml = new UserAccountXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			UserAccountXml xml = new UserAccountXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<UserAccountRoleXml> updateUserAccountRole(long lastModifyDate) {

		// user account role table

		List<UserAccountRoleXml> xmlList = new ArrayList<UserAccountRoleXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}

			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			List<UserAccountRole> userAccountRoleList = userAccountManager.searchUserAccountRole(null, null, null, null,
					null);

			if (userAccountRoleList != null) {
				for (UserAccountRole ar : userAccountRoleList) {

					Timestamp t = ar.getLastModifyDateTime();

					if (t == null) {
						t = ar.getCreateDateTime();
					}
					if (lastModifyDate >= t.getTime()) {
						continue;
					}

					// create XML object
					UserAccountRoleXml xml = new UserAccountRoleXml();
					xml.setKey(ar.getKey());
					xml.setAccountKey(ar.getAccountKey());
					xml.setRoleKey(ar.getRoleKey());
					xml.setCreateBy(ar.getCreateBy());
					if (ar.getCreateDateTime() != null) {
						xml.setCreateDateTime(ar.getCreateDateTime().getTime());
					}
					xml.setDeleted(ar.getDeleted());

					xml.setLastModifyBy(ar.getLastModifyBy());
					if (ar.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(ar.getLastModifyDateTime().getTime());
					} else
						xml.setLastModifyDateTime(ar.getCreateDateTime().getTime());

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			UserAccountRoleXml xml = new UserAccountRoleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			UserAccountRoleXml xml = new UserAccountRoleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<SiteXml> updateSite(long lastModifyDate) {
		// get all site

		List<SiteXml> xmlList = new ArrayList<SiteXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}

			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			//
			List<Site> siteList = siteManager.searchSite(null, null, null, null, null, null);

			if (siteList != null) {
				for (Site s : siteList) {

//					Timestamp t = s.getLastModifyDateTime();
//
//					if (t == null) {
//						t = s.getCreateDateTime();
//					}
//					if (lastModifyDate >= t.getTime()) {
//						continue;
//					}

					// create XML object
					SiteXml xml = new SiteXml();
					xml.setKey(s.getKey());
					xml.setAddress(s.getAddress());
					xml.setName(s.getName());
					xml.setContactAreaCode(s.getContactAreaCode());
					xml.setContactCountryCode(s.getContactCountryCode());
					xml.setContactNumber(s.getContactNumber());
					xml.setCreateBy(s.getCreateBy());
					if (s.getCreateDateTime() != null) {
						xml.setCreateDateTime(s.getCreateDateTime().getTime());
					}
					xml.setDeleted(s.getDeleted());

					xml.setLastModifyBy(s.getLastModifyBy());
					if (s.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(s.getLastModifyDateTime().getTime());
					} else
						xml.setLastModifyDateTime(s.getCreateDateTime().getTime());

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			SiteXml xml = new SiteXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			SiteXml xml = new SiteXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<RoleXml> updateRole(long lastModifyDate) {
		// get all role

		List<RoleXml> xmlList = new ArrayList<RoleXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}

			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			//
			List<Role> roleList = roleManager.searchRole(null, null, null, null, false);

			List<UserAccountRole> accountRoleList = null;

			if (roleList != null) {
				for (Role r : roleList) {

					accountRoleList = userAccountManager.searchUserAccountRole(null, r.getKey(), null, null, null);

					for (UserAccountRole accountRole : accountRoleList) {

						Timestamp t = r.getLastModifyDateTime();

						if (t == null) {
							t = r.getCreateDateTime();
						}
						if (lastModifyDate >= t.getTime()) {
							continue;
						}

						// create XML object
						RoleXml xml = new RoleXml();
						xml.setKey(r.getKey());
						xml.setDescription(r.getDescription());
						xml.setName(r.getName());
						xml.setSiteKey(r.getSiteKey());
						xml.setAccessMode(r.getModeKey());
						xml.setCreateBy(r.getCreateBy());
						if (r.getCreateDateTime() != null) {
							xml.setCreateDateTime(r.getCreateDateTime().getTime());
						}
						xml.setDeleted(r.getDeleted());

						xml.setLastModifyBy(r.getLastModifyBy());
						if (r.getLastModifyDateTime() != null) {
							xml.setLastModifyDateTime(r.getLastModifyDateTime().getTime());
						} else
							xml.setLastModifyDateTime(r.getCreateDateTime().getTime());

						xml.setAccountKey(accountRole.getAccountKey());

						xml.setXmlStatus(XmlStatus.SUCCESS.toString());

						xmlList.add(xml);
					}
				
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			RoleXml xml = new RoleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			RoleXml xml = new RoleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<RolePrivilegeXml> updateRolePrivilege(long lastModifyDate) {
		// get all role privilege
		List<RolePrivilegeXml> xmlList = new ArrayList<RolePrivilegeXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}

			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}
			// get rolePrivilege inside each role

			List<Role> roleList = roleManager.searchRole(null, null, null, null, true);

			if (roleList != null) {
				for (Role r : roleList) {

					if (r.getRolePrivileges() != null) {

						for (RolePrivilege rp : r.getRolePrivileges()) {

							Timestamp t = rp.getLastModifyDateTime();

							if (t == null) {
								t = rp.getCreateDateTime();
							}
							if (lastModifyDate >= t.getTime()) {
								continue;
							}

//							if (rp.getPrivilegeCode().contains("Defect") || (rp.getPrivilegeCode().contains("Route"))|| (rp.getPrivilegeCode().contains("Schedule"))
//									|| (rp.getPrivilegeCode().contains("Patrol"))) {
							if (rp.getPrivilege().getPrivilegeCategoryCode().contains("Mobile")) {

								// create XML object
								RolePrivilegeXml xml = new RolePrivilegeXml();
								xml.setRoleKey(r.getKey());
								xml.setPrivilegeCode(rp.getPrivilegeCode());
								xml.setCreateBy(rp.getCreateBy());
								if (rp.getCreateDateTime() != null) {
									xml.setCreateDateTime(rp.getCreateDateTime().getTime());
								}
								xml.setDeleted(rp.getDeleted());

								xml.setLastModifyBy(rp.getLastModifyBy());
								if (rp.getLastModifyDateTime() != null) {
									xml.setLastModifyDateTime(rp.getLastModifyDateTime().getTime());
								} else
									xml.setLastModifyDateTime(rp.getCreateDateTime().getTime());

								xml.setXmlStatus(XmlStatus.SUCCESS.toString());

								xmlList.add(xml);
							}
						}
					}
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			RolePrivilegeXml xml = new RolePrivilegeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			RolePrivilegeXml xml = new RolePrivilegeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<PrivilegeXml> updatePrivilege(long lastModifyDate) {

		// get all privilege
		List<PrivilegeXml> xmlList = new ArrayList<PrivilegeXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}
			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			// not yet implemented
			throw new MFMSException("Not implemented");

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			PrivilegeXml xml = new PrivilegeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			PrivilegeXml xml = new PrivilegeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<LocationPrivilegeXml> updateLocationPrivilege(long lastModifyDate) {
		// get all privilege
		List<LocationPrivilegeXml> xmlList = new ArrayList<LocationPrivilegeXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}
			} else {
				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			List<LocationPrivilege> lpList = locationManager.getAllLocationPrivileges();

			if (lpList != null) {
				for (LocationPrivilege lp : lpList) {

					Timestamp t = lp.getLastModifyDateTime();

					if (t == null) {
						t = lp.getCreateDateTime();
					}
					if (lastModifyDate >= t.getTime()) {
						continue;
					}

					// create XML object
					LocationPrivilegeXml xml = new LocationPrivilegeXml();
					xml.setKey(lp.getKey());
					xml.setAccountKey(lp.getAccountKey());
					xml.setLocationKey(lp.getLocationKey());
					xml.setCreatedBy(lp.getCreatedBy());
					if (lp.getCreateDateTime() != null) {
						xml.setCreateDateTime(lp.getCreateDateTime().getTime());
					}
					xml.setDeleted(lp.getDeleted());

					xml.setLastModifyBy(lp.getLastModifyBy());
					if (lp.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(lp.getLastModifyDateTime().getTime());
					} else 
						xml.setLastModifyDateTime(lp.getCreateDateTime().getTime());

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			LocationPrivilegeXml xml = new LocationPrivilegeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			LocationPrivilegeXml xml = new LocationPrivilegeXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<AccountGroupXml> updateAccountGroup(long lastModifyDate) {

		// get all account group
		List<AccountGroupXml> xmlList = new ArrayList<AccountGroupXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}
			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			List<AccountGroup> accountGroupList = accountGroupManager.getAllAccountGroup();

			if (accountGroupList != null) {
				for (AccountGroup ag : accountGroupList) {

					Timestamp t = ag.getLastModifyDateTime();

					if (t == null) {
						t = ag.getCreateDateTime();
					}
					if (lastModifyDate >= t.getTime()) {
						continue;
					}
					// create XML object
					AccountGroupXml xml = new AccountGroupXml();
					xml.setKey(ag.getKey());
					xml.setDesc(ag.getDesc());
					xml.setName(ag.getName());
					xml.setSiteKey(ag.getSiteKey());
					xml.setCreatedBy(ag.getCreatedBy());
					if (ag.getCreateDateTime() != null) {
						xml.setCreateDateTime(ag.getCreateDateTime().getTime());
					}
					xml.setDeleted(ag.getDeleted());

					xml.setLastModifyBy(ag.getLastModifyBy());
					if (ag.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(ag.getLastModifyDateTime().getTime());
					}

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			AccountGroupXml xml = new AccountGroupXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			AccountGroupXml xml = new AccountGroupXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<AccountGroupAccountXml> updateAccountGroupAccount(long lastModifyDate) {

		// get all account group account mapping
		List<AccountGroupAccountXml> xmlList = new ArrayList<AccountGroupAccountXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}
			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			List<AccountGroupAccount> accountGroupAccountList = accountGroupAccountManager.getAllAccountGroupAccount();

			if (accountGroupAccountList != null) {
				for (AccountGroupAccount aga : accountGroupAccountList) {

					Timestamp t = aga.getLastModifyDateTime();

					if (t == null) {
						t = aga.getCreateDateTime();
					}
					if (lastModifyDate >= t.getTime()) {
						continue;
					}

					// create XML object
					AccountGroupAccountXml xml = new AccountGroupAccountXml();
					xml.setKey(aga.getKey());
					xml.setAccountKey(aga.getAccountKey());
					xml.setGroupKey(aga.getGroupKey());
					xml.setCreatedBy(aga.getCreatedBy());
					if (aga.getCreateDateTime() != null) {
						xml.setCreateDateTime(aga.getCreateDateTime().getTime());
					}
					xml.setDeleted(aga.getDeleted());

					xml.setLastModifyBy(aga.getLastModifyBy());
					if (aga.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(aga.getLastModifyDateTime().getTime());
					}

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			AccountGroupAccountXml xml = new AccountGroupAccountXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			AccountGroupAccountXml xml = new AccountGroupAccountXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public List<AccountGroupResponsibleXml> updateAccountGroupResponsible(long lastModifyDate) {

		// get all account group account mapping
		List<AccountGroupResponsibleXml> xmlList = new ArrayList<AccountGroupResponsibleXml>();

		try {
			if (checkLogin(wsContext.getMessageContext())) {
				logger.debug("Login Success");

				if (hasStaticDataPrivilege(wsContext.getMessageContext())) {
					// has sysadmin privilege
					logger.debug("Has Static Data Privilege");
				} else {
					logger.debug("User does not have Static Data Privilege");
					throw new MFMSWebServiceException("No privilege");
				}
			} else {

				logger.debug("Login Fail");
				throw new MFMSWebServiceException("Login Fail");

			}

			List<AccountGroupResponsible> accountGroupResponsibleList = accountGroupResponsibleManager
					.getAllAccountGroupResponsible();

			if (accountGroupResponsibleList != null) {
				for (AccountGroupResponsible agr : accountGroupResponsibleList) {

					Timestamp t = agr.getLastModifyDateTime();

					if (t == null) {
						t = agr.getCreateDateTime();
					}
					if (lastModifyDate >= t.getTime()) {
						continue;
					}

					// create XML object
					AccountGroupResponsibleXml xml = new AccountGroupResponsibleXml();
					xml.setKey(agr.getKey());
					xml.setAccountKey(agr.getAccountKey());
					xml.setGroupKey(agr.getGroupKey());
					xml.setCreatedBy(agr.getCreatedBy());
					if (agr.getCreateDateTime() != null) {
						xml.setCreateDateTime(agr.getCreateDateTime().getTime());
					}
					xml.setDeleted(agr.getDeleted());

					xml.setLastModifyBy(agr.getLastModifyBy());
					if (agr.getLastModifyDateTime() != null) {
						xml.setLastModifyDateTime(agr.getLastModifyDateTime().getTime());
					}

					xml.setXmlStatus(XmlStatus.SUCCESS.toString());

					xmlList.add(xml);
				}
			}

		} catch (MFMSWebServiceException e) {
			e.printStackTrace();
			AccountGroupResponsibleXml xml = new AccountGroupResponsibleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.LOGIN_FAIL.toString());

			xmlList.add(xml);
		} catch (MFMSException e) {
			e.printStackTrace();

			AccountGroupResponsibleXml xml = new AccountGroupResponsibleXml();
			xml.setXmlStatus(XmlStatus.FAIL.toString());
			xml.setXmlMsg(XmlMsg.DATA_OR_SERVER_ERROR.toString());

			xmlList.add(xml);
		}
		return xmlList;
	}

	@Override
	public Integer checkAccountLogin(String loginId, String password) {
		int result = 999;
		
		try {
			UserAccount existingAccount = userAccountManager.getUserAccountByLoginId(loginId, false);
	
			if (null==existingAccount) {
				result = 101;
			} else {
				if (!(BCrypt.checkpw(password, existingAccount.getPassword())) ) {
					result = 102;
				} else {
					if (existingAccount.getStatus().equals(UserAccount.STATUS_SUSPENDED)) {
						result = 103;
					} else {
						result = 100;
					}
				}
			}
		} catch (MFMSException e) {
			e.printStackTrace();
		}
		
		logger.debug("checkAccountLogin : " + result);
		
		return new Integer(result);
	}

	public WebServiceContext getWsContext() {
		return wsContext;
	}

	public void setWsContext(WebServiceContext wsContext) {
		this.wsContext = wsContext;
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	public SiteManager getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(SiteManager siteManager) {
		this.siteManager = siteManager;
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public AccountGroupManager getAccountGroupManager() {
		return accountGroupManager;
	}

	public void setAccountGroupManager(AccountGroupManager accountGroupManager) {
		this.accountGroupManager = accountGroupManager;
	}

	public AccountGroupAccountManager getAccountGroupAccountManager() {
		return accountGroupAccountManager;
	}

	public void setAccountGroupAccountManager(AccountGroupAccountManager accountGroupAccountManager) {
		this.accountGroupAccountManager = accountGroupAccountManager;
	}

	public AccountGroupResponsibleManager getAccountGroupResponsibleManager() {
		return accountGroupResponsibleManager;
	}

	public void setAccountGroupResponsibleManager(AccountGroupResponsibleManager accountGroupResponsibleManager) {
		this.accountGroupResponsibleManager = accountGroupResponsibleManager;
	}

}
