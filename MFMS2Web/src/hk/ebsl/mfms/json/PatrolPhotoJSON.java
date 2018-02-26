package hk.ebsl.mfms.json;

public class PatrolPhotoJSON {

	
	private int photoKey;
	private int patrolResultKey;
	private int scheduleKey;
	private int routeDefKey;
	
	private String photoPath;
	
	private int locationKey;
	private String locationName;
	private String locationCode;
	
	private int accountKey;
	private String accountName;
	
	private String takenTime;
	private String remark;
	
	public int getPhotoKey() {
		return photoKey;
	}
	public void setPhotoKey(int photoKey) {
		this.photoKey = photoKey;
	}
	public int getPatrolResultKey() {
		return patrolResultKey;
	}
	public void setPatrolResultKey(int patrolResultKey) {
		this.patrolResultKey = patrolResultKey;
	}
	public int getScheduleKey() {
		return scheduleKey;
	}
	public void setScheduleKey(int scheduleKey) {
		this.scheduleKey = scheduleKey;
	}
	public int getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public int getLocationKey() {
		return locationKey;
	}
	public void setLocationKey(int locationKey) {
		this.locationKey = locationKey;
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
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
	public String getTakenTime() {
		return takenTime;
	}
	public void setTakenTime(String takenTime) {
		this.takenTime = takenTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
