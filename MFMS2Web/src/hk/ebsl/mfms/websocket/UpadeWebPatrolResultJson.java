package hk.ebsl.mfms.websocket;


public class UpadeWebPatrolResultJson {

	public enum PatrolResultAction {
		Create, Update, Complete
	};
	
	
	int siteKey;
	int scheduleKey;
	int routeDefKey;
	int patrolResultKey;
	int accountKey;

	PatrolResultAction action;

	public int getRouteDefKey() {
		return routeDefKey;
	}

	public void setRouteDefKey(int routeDefKey) {
		this.routeDefKey = routeDefKey;
	}

	public int getPatrolResultKey() {
		return patrolResultKey;
	}

	public void setPatrolResultKey(int patrolResultKey) {
		this.patrolResultKey = patrolResultKey;
	}

	public PatrolResultAction getAction() {
		return action;
	}

	public void setAction(PatrolResultAction action) {
		this.action = action;
	}

	public int getScheduleKey() {
		return scheduleKey;
	}

	public void setScheduleKey(int scheduleKey) {
		this.scheduleKey = scheduleKey;
	}

	public int getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(int accountKey) {
		this.accountKey = accountKey;
	}

	public int getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(int siteKey) {
		this.siteKey = siteKey;
	}
	
}
