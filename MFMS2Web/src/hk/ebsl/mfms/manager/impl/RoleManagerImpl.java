package hk.ebsl.mfms.manager.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import hk.ebsl.mfms.dao.PrivilegeCategoryDao;
import hk.ebsl.mfms.dao.RoleDao;
import hk.ebsl.mfms.dao.RolePrivilegeDao;
import hk.ebsl.mfms.dto.PrivilegeCategory;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.dto.Site;
import hk.ebsl.mfms.exception.MFMSException;
import hk.ebsl.mfms.manager.RoleManager;

public class RoleManagerImpl implements RoleManager {

	public static final Logger logger = Logger.getLogger(RoleManagerImpl.class);
	
	private RoleDao roleDao;
	
	private RolePrivilegeDao rolePrivilegeDao;
	
	private PrivilegeCategoryDao privilegeCategoryDao;
	
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public List<Role> getRolesBySiteKey(Integer siteKey, boolean fetch) throws MFMSException {
		
		logger.debug("getRolesBySiteKey()[" + siteKey + "]");
		
		List<Role> roleList = roleDao.getRolesBySiteKey(siteKey);
		
		if (fetch) {
			
			// fetch lazyload objects;
			for (Role r : roleList) {
				if (r.getRolePrivileges() != null)
					r.getRolePrivileges().size();
			}
		}
		
		return roleList;
		
	}

	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public Role getRoleByKey(Integer key, boolean fetch) throws MFMSException {

		logger.debug("getRoleByKey()[" + key + "]");
		
		Role role = roleDao.getRoleByKey(key);
		
		if (fetch) {
			// fetch lazyload objects;
			if (role != null && role.getRolePrivileges() != null)
				role.getRolePrivileges().size();
		}
		
		return role;
	}
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void saveRole(Role role) throws MFMSException {
		logger.debug("saveRole()[" + role.getKey() + "," + role.getName() + "]");
		roleDao.saveRole(role);
		
		if (role.getKey() != null) {
			// save or update role privileges
			for (RolePrivilege rp : role.getRolePrivileges()) {
				rp.setRoleKey(role.getKey());
				rolePrivilegeDao.saveRolePrivilege(rp);
			}
		}
	}
	
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public List<Role> searchRole(Integer siteKey, String name, String description, String deleted, boolean fetch) throws MFMSException {
		
		logger.debug("searchRole()[" + siteKey + "," + name + "," + deleted + "]");
		
		List<Role> roleList =  roleDao.searchRole(siteKey, name, description, deleted);
		
		if (fetch) {
			
			// fetch lazyload objects;
			for (Role r : roleList) {
				if (r.getRolePrivileges() != null)
					r.getRolePrivileges().size();
			}
		}
		
		return roleList;
	}
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public List<PrivilegeCategory> searchPrivilegeCategory(String code, String categoryDesc, boolean topOnly, String orderBy, boolean asc, boolean fetch) throws MFMSException {
		
		logger.debug("searchPrivilegeCategory()[" + code + "," + categoryDesc + "," + topOnly + "," + orderBy + "," + asc + "," + fetch + "]");
		
		List<PrivilegeCategory> list = privilegeCategoryDao.searchPrivilegeCategory(code, categoryDesc, topOnly, orderBy, asc, "N");
		
		if (fetch) {
					
			// fetch lazyload objects;
			// all fields are eagerly loaded
			/*
			for (PrivilegeCategory pc : list ) {
				if (pc.getPrivilegeList() != null)
					pc.getPrivilegeList().size();
				
				if (pc.getSubCategoryList() != null)
					pc.getSubCategoryList().size();
			}
			*/
		}
		
		return list;
	}
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public List<RolePrivilege> searchRoleByPrivilege(String privilege){
		List<RolePrivilege> rolePrivilegeList = this.rolePrivilegeDao.searchRoleByPrivilege(privilege);
		
		return rolePrivilegeList;
		
	}
	
	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public List<RolePrivilege> searchRoleByPrivilege(Integer roleKey){
		List<RolePrivilege> rolePrivilegeList = this.rolePrivilegeDao.searchRoleByRoleKey(roleKey);
		
		return rolePrivilegeList;
		
	}
	

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public PrivilegeCategoryDao getPrivilegeCategoryDao() {
		return privilegeCategoryDao;
	}

	public void setPrivilegeCategoryDao(PrivilegeCategoryDao privilegeCategoryDao) {
		this.privilegeCategoryDao = privilegeCategoryDao;
	}

	public RolePrivilegeDao getRolePrivilegeDao() {
		return rolePrivilegeDao;
	}

	public void setRolePrivilegeDao(RolePrivilegeDao rolePrivilegeDao) {
		this.rolePrivilegeDao = rolePrivilegeDao;
	}

	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public void deleteRoleByKey(Integer accountKey, Integer roleKey)
			throws MFMSException {
		
		logger.debug("deleteRoleByKey()[" + accountKey + "," + roleKey + "]");
		
		Role role = roleDao.getRoleByKey(roleKey);
		
		if (role == null) {
			throw new MFMSException("Role not found");
			
		}
		role.setDeleted("Y");
		role.setLastModifyBy(accountKey);
		role.setLastModifyDateTime(new Timestamp(System.currentTimeMillis()));
		
		saveRole(role);
	}

	@Override
	@Transactional (rollbackFor={MFMSException.class})
	public Role getRoleByName(Integer siteKey, String name, boolean fetch) throws MFMSException {
		
		logger.debug("getRoleByName()[" + siteKey + "," + name + "," + fetch + "]");
		
		Role role = roleDao.getRoleByName(siteKey, name, "N");
		
		if (fetch) {
			
			// fetch lazyload objects;
			if (role != null && role.getRolePrivileges() != null)
				role.getRolePrivileges().size();
		}
		return role;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<Role> searchRoleByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey){
		
		logger.debug("searchRole()[" + lastModifiedDate + "]");
		
		List<Role> roleList = roleDao.searchRole(lastModifiedDate, offset, maxResults, siteKey);
		
		return roleList;
	}
	
	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCount(Timestamp lastModifiedDate){
		
		logger.debug("searchResultCount()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = roleDao.theCountOfSearchResult(lastModifiedDate);
		
		return theCountoftotalResult;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public List<RolePrivilege> searchRolePrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults) {
		logger.debug("searchRolePrivilege()[" + lastModifiedDate + "]");
		
		List<RolePrivilege> rolePrivilegeList = rolePrivilegeDao.searchRolePrivilegeByDate(lastModifiedDate, offset, maxResults);
		
		return rolePrivilegeList;
	}

	@Override
	@Transactional(rollbackFor = { MFMSException.class })
	public Integer searchResultCountbyRolePrivilege(Timestamp lastModifiedDate) {
		logger.debug("searchResultCountbyRolePrivilege()[" + lastModifiedDate + "]");
		
		Integer theCountoftotalResult = rolePrivilegeDao.theCountOfSearchResultbyRolePrivilege(lastModifiedDate);
		
		return theCountoftotalResult;
	}

}
