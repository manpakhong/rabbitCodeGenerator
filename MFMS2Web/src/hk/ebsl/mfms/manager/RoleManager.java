package hk.ebsl.mfms.manager;

import java.sql.Timestamp;
import java.util.List;

import hk.ebsl.mfms.dto.PrivilegeCategory;
import hk.ebsl.mfms.dto.Role;
import hk.ebsl.mfms.dto.RolePrivilege;
import hk.ebsl.mfms.exception.MFMSException;

public interface RoleManager {

	public List<Role> getRolesBySiteKey(Integer siteKey, boolean fetch) throws MFMSException;

	public Role getRoleByKey(Integer key, boolean fetch) throws MFMSException;

	public void saveRole(Role role) throws MFMSException;

	public List<Role> searchRole(Integer siteKey, String name, String description, String deleted, boolean fetch)
			throws MFMSException;

	public List<PrivilegeCategory> searchPrivilegeCategory(String code, String categoryDesc, boolean topOnly,
			String orderBy, boolean asc, boolean fetch) throws MFMSException;

	public void deleteRoleByKey(Integer accountKey, Integer roleKey) throws MFMSException;

	public Role getRoleByName(Integer siteKey, String name, boolean fetch) throws MFMSException;

	public List<RolePrivilege> searchRoleByPrivilege(String privilege);
	
	public List<RolePrivilege> searchRoleByPrivilege(Integer roleKey);

	public List<Role> searchRoleByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults, Integer siteKey);

	public Integer searchResultCount(Timestamp lastModifiedDate);
	
	public List<RolePrivilege> searchRolePrivilegeByDate(Timestamp lastModifiedDate, Integer offset, Integer maxResults);

	public Integer searchResultCountbyRolePrivilege(Timestamp lastModifiedDate);

}
