package hk.ebsl.mfms.dto;

public class RoleExport {
	private String name;
	private String desc;
	private String privilegeCategory;
	private String privilege;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPrivilegeCategory() {
		return privilegeCategory;
	}
	public void setPrivilegeCategory(String privilegeCategory) {
		this.privilegeCategory = privilegeCategory;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	
	
}
