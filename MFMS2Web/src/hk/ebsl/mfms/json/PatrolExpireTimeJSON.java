package hk.ebsl.mfms.json;

import java.text.SimpleDateFormat;

public class PatrolExpireTimeJSON {

	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private int scheduleKey;
	private int routeDefKey;
	private int patrolResultKey;
	private String expTime;
	
	
	public int getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public SimpleDateFormat getDateTimeFormat() {
		return dateTimeFormat;
	}
	public void setDateTimeFormat(SimpleDateFormat dateTimeFormat) {
		this.dateTimeFormat = dateTimeFormat;
	}
	public int getPatrolResultKey() {
		return patrolResultKey;
	}
	public void setPatrolResultKey(int patrolResultKey) {
		this.patrolResultKey = patrolResultKey;
	}
	public String getExpTime() {
		return expTime;
	}
	public void setExpTime(String expTime) {
		this.expTime = expTime;
	}
	public int getScheduleKey() {
		return scheduleKey;
	}
	public void setScheduleKey(int scheduleKey) {
		this.scheduleKey = scheduleKey;
	}
	
	
	
	
}
