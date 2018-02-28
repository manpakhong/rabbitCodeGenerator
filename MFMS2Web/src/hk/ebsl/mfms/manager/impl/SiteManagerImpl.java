package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import hk.ebsl.mfms.dao.SiteDao;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.dto.UserAccount;
import hk.ebsl.mfms.dto.UserAccountRole;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RoleManager;
import hk.ebsl.mfms.manager.SiteManager;
import hk.ebsl.mfms.manager.UserAccountManager;

public class SiteManagerImpl implements SiteManager {

	public static final Logger logger = Logger.getLogger(SiteManagerImpl.class);
	
	private SiteDao siteDao;
	
	private RoleManager roleManager;
	
	private UserAccountManager userAccountManager; 
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Site getSiteByKey(Integer key) throws MFMSException {
		
		logger.debug("getSiteByKey()[" + key  + "]");
		
		return siteDao.getSiteByKey(key);
	}

	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void saveSite(Site site) throws MFMSException {
		
		logger.debug("saveSite()[" + site.getKey() + "," + site.getName()  + "]");
		
		siteDao.saveSite(site);
	}

	public SiteDao getSiteDao() {
		return siteDao;
	}

	public void setSiteDao(SiteDao siteDao) {
		this.siteDao = siteDao;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Site> searchSite(String name, String address,
			Integer contactCountryCode, Integer contactAreaCode,
			Integer contactNumber, String deleted) throws MFMSException {
		
		logger.debug("searchSite()[" + name + "," + address + "," + contactCountryCode + "," + contactAreaCode + "," + contactNumber  + "," + deleted + "]");
		
		List<Site> siteList = siteDao.searchSite(name, address, contactCountryCode, contactAreaCode, contactNumber, deleted);
		
		return siteList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Site getSiteByName(String name) throws MFMSException {
		
		logger.debug("getSiteByName()[" + name  + "]");
		
		return siteDao.getSiteByName(name, "N");
		
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Site> searchSiteByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults){
		
		logger.debug("searchSite()[" + lastModifiedDate + "]");
		
		List<Site> siteList = siteDao.searchSite(lastModifiedDate, offset, maxResults);
		
		return siteList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = siteDao.theCountOfSearchResult(lastModifiedDate);
		
		return theCountoftotalResult;
	}
	
	

	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void createSite(Site site, Role adminRole, UserAccount admin)
			throws MFMSException {
		// pack create site, default admin role and default admin account in 1 transaction
		logger.debug("createSite()[" + site + "," + adminRole + "," + admin + "]");
		
		if (site == null ) {
			throw new MFMSException("No site is submitted.");
		}
		
		if (site.getKey() != null) {
			throw new MFMSException("Site is not new.");
		}
		
		if (adminRole == null ) {
			throw new MFMSException("No role is submitted.");
		}
		
		if (adminRole.getKey() != null) {
			throw new MFMSException("role is not new.");
		}
		
		saveSite(site);
		
		if (site.getKey() == null) {
			// site is not created
			throw new MFMSException("Failed to create site.");
		}
		
		if (adminRole != null) {
			// create role
			adminRole.setSiteKey(site.getKey());
			roleManager.saveRole(adminRole);
			
			if (adminRole.getKey() == null) {
				// Role is not created
				throw new MFMSException("Failed to create role.");
			}
			
			if (admin != null) {
				// create user role and user
				
				ArrayList<UserAccountRole> arList = new ArrayList<UserAccountRole>();
				UserAccountRole ar = new UserAccountRole();
				ar.setRoleKey(adminRole.getKey());
				ar.setCreateBy(adminRole.getCreateBy());
				ar.setCreateDateTime(new Timestamp(System.currentTimeMillis()));
				ar.setDeleted("N");
				
				arList.add(ar);
				
				admin.setAccountRoles(arList);
				
				userAccountManager.saveUserAccount(admin);
				
				if (admin.getKey() != null) {
					// create successful
					logger.debug("New user key = " + admin.getKey());
						
				} else {
					// Admin account is not created
					throw new MFMSException("Failed to create admin account.");
				}
			}
		}
	}

	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void deleteSiteByKey(Integer accountKey, Integer siteKey) throws MFMSException {
		// TODO Auto-generated method stub
		logger.debug("deleteSiteByKey()[" + accountKey + "," + siteKey + "]");
		
		Site site = getSiteByKey(siteKey);
		
		if (site == null) {
			throw new MFMSException("Site Not Found");
			
		}
		
		site.setDeleted("Y");
		site.setLastModifyBy(accountKey);
		site.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
		saveSite(site);
	}

	public RoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public UserAccountManager getUserAccountManager() {
		return userAccountManager;
	}

	public void setUserAccountManager(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

}
