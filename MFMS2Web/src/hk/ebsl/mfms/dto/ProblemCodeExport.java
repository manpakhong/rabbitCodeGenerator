package hk.ebsl.mfms.dto;

public class ProblemCodeExport {
	private String failureClass;
	
	private String code;
	
	private String name;
	
	private String desc;
	
	private String defaultAccount;
	
	private String priority;

	public String getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(String failureClass) {
		this.failureClass = failureClass;
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

	public String getDefaultAccount() {
		return defaultAccount;
	}

	public void setDefaultAccount(String defaulAccount) {
		this.defaultAccount = defaulAccount;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	
}
