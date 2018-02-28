package hk.ebsl.mfms.json;

import java.util.List;

public class PatrolResultDisplayJSON {

	public enum ResultStatus { Overtime, Normal }
	public enum ScheduleStatus {Overtime, Skipped, Finished, InProgress}
	
	private int scheduleKey;
	private int routeDefKey;
	private String routeCode;
	private String routeName;
	
	private int locationKey;
	private String locationCode;
	private String locationName;
	
	private String progress;
	private String attendTime;
	
	private int accountKey;
	private String accountName;
	private String accountLoginId;
	private String accountEmail;
	private String accountTel;
	
	private int currentLocationSeq;
	
	
	private ResultStatus status;
	private ScheduleStatus scheduleStatus;
	
	private List<PatrolResultDisplayDetailJSON> patrolResult;
	

	public int getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

	public String getRouteCode() {
		return routeCode;
	}

	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public int getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(int locationKey) {
		this.locationKey = locationKey;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getAttendTime() {
		return attendTime;
	}

	public void setAttendTime(String attendTime) {
		this.attendTime = attendTime;
	}

	public int getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(int accountKey) {
		this.accountKey = accountKey;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountLoginId() {
		return accountLoginId;
	}

	public void setAccountLoginId(String accountLoginId) {
		this.accountLoginId = accountLoginId;
	}

	public String getAccountEmail() {
		return accountEmail;
	}

	public void setAccountEmail(String accountEmail) {
		this.accountEmail = accountEmail;
	}

	public String getAccountTel() {
		return accountTel;
	}

	public void setAccountTel(String accountTel) {
		this.accountTel = accountTel;
	}

	public ResultStatus getStatus() {
		return status;
	}

	public void setStatus(ResultStatus status) {
		this.status = status;
	}

	public List<PatrolResultDisplayDetailJSON> getPatrolResult() {
		return patrolResult;
	}

	public void setPatrolResult(List<PatrolResultDisplayDetailJSON> patrolResult) {
		this.patrolResult = patrolResult;
	}

	public int getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(int scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public ScheduleStatus getScheduleStatus() {
		return scheduleStatus;
	}

	public void setScheduleStatus(ScheduleStatus scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public int getCurrentLocationSeq() {
		return currentLocationSeq;
	}

	public void setCurrentLocationSeq(int currentLocationSeq) {
		this.currentLocationSeq = currentLocationSeq;
	}

	
	
}
