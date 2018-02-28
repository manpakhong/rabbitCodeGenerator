package hk.ebsl.mfms.json;

public class PatrolResultFormJSON {

	public enum ReportType{
		All,Correct, Incorrect
	}
	
	String type;
	String startDate;
	String endDate;
	Integer route;
	Integer patrolStaff;
	String exportType;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getRoute() {
		return route;
	}
	public void setRoute(Integer route) {
		this.route = route;
	}
	public Integer getPatrolStaff() {
		return patrolStaff;
	}
	public void setPatrolStaff(Integer patrolStaff) {
		this.patrolStaff = patrolStaff;
	}
	public String getExportType() {
		return exportType;
	}
	public void setExportType(String exportType) {
		this.exportType = exportType;
	}
	
	
	
}
