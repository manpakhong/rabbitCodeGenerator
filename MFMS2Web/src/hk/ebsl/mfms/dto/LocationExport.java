package hk.ebsl.mfms.dto;

public class LocationExport {
	private String parent;
	private String code;
	private String name;
	private String desc;
	private String tagID;
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
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
	public String getTagID() {
		return tagID;
	}
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}
	@Override
	public String toString() {
		return "LocationExport [parent=" + parent + ", code=" + code + ", name=" + name + ", desc=" + desc + ", tagID="
				+ tagID + "]";
	}
	
}
