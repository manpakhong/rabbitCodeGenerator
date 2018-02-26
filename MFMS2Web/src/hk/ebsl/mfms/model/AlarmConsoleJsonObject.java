package hk.ebsl.mfms.model;

import hk.ebsl.mfms.dto.AlarmConsole;

import java.util.List;

public class AlarmConsoleJsonObject {

	private Integer ID;
	private Integer AlertStatus;
	private String JECCaseNo;
	private AlarmConsole Summary;
	private List<AlarmConsole> Details;
	
	public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Integer getAlertStatus() {
		return AlertStatus;
	}
	public void setAlertStatus(Integer alertStatus) {
		AlertStatus = alertStatus;
	}
	public String getJECCaseNo() {
		return JECCaseNo;
	}
	public void setJECCaseNo(String jECCaseNo) {
		JECCaseNo = jECCaseNo;
	}
	public AlarmConsole getSummary() {
		return Summary;
	}
	public void setSummary(AlarmConsole summary) {
		Summary = summary;
	}
	public List<AlarmConsole> getDetails() {
		return Details;
	}
	public void setDetails(List<AlarmConsole> details) {
		Details = details;
	}
	
	
	
	
	
}
