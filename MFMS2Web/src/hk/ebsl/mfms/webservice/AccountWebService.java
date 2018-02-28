package hk.ebsl.mfms.webservice;

import hk.ebsl.mfms.webservice.xml.AccountGroupAccountXml;
import hk.ebsl.mfms.webservice.xml.AccountGroupResponsibleXml;
import hk.ebsl.mfms.webservice.xml.AccountGroupXml;
import hk.ebsl.mfms.webservice.xml.LocationPrivilegeXml;
import hk.ebsl.mfms.webservice.xml.PrivilegeXml;
import hk.ebsl.mfms.webservice.xml.RolePrivilegeXml;
import hk.ebsl.mfms.webservice.xml.RoleXml;
import hk.ebsl.mfms.webservice.xml.SiteXml;
import hk.ebsl.mfms.webservice.xml.UserAccountRoleXml;
import hk.ebsl.mfms.webservice.xml.UserAccountXml;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface AccountWebService {

	
	@WebMethod
	public List<UserAccountXml> updateUserAccount(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<UserAccountRoleXml> updateUserAccountRole(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<SiteXml> updateSite(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<RoleXml> updateRole(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<RolePrivilegeXml> updateRolePrivilege(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<PrivilegeXml> updatePrivilege(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<LocationPrivilegeXml> updateLocationPrivilege(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<AccountGroupXml> updateAccountGroup(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<AccountGroupAccountXml> updateAccountGroupAccount(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public List<AccountGroupResponsibleXml> updateAccountGroupResponsible(@WebParam(name = "lastModifyDate") long lastModifyDate);
	
	@WebMethod
	public Integer checkAccountLogin(@WebParam(name = "loginId") String loginId, @WebParam(name = "password") String password);
	
}
