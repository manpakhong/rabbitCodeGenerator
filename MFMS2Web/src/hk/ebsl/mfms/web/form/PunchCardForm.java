package hk.ebsl.mfms.web.form;

public class PunchCardForm {
	private String userName;
	private String currentDateTimeString;
	private String remarkLocation;
	private String remarks;
	private String action;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCurrentDateTimeString() {
		return currentDateTimeString;
	}
	public void setCurrentDateTimeString(String currentDateTimeString) {
		this.currentDateTimeString = currentDateTimeString;
	}
	public String getRemarkLocation() {
		return remarkLocation;
	}
	public void setRemarkLocation(String remarkLocation) {
		this.remarkLocation = remarkLocation;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
