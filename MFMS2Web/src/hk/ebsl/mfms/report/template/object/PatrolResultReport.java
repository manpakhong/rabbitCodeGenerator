package hk.ebsl.mfms.report.template.object;

import hk.ebsl.mfms.dto.PatrolResult;

public class PatrolResultReport {

	private Integer index;

	// Master
	private String StartTime;

	private String AccountNameEN;

	private String RouteCode;

	private String RouteNameEN;

	private String TotalTimeUsed;

	// Detail
	private Integer GroupNum;

	private String TimeAttended;

	private String Duration;
	
	private Integer SeqNum;
	
	private String DisplayLocationCode;

	private String DisplayLocationNameEN;


	private String Speed;

	private String Result;
	
	private String Reason;

	// Summary
	private int TotalRouteNum;

	private int TotalCorrectRouteNum;

	private int TotalIncorrectRouteNum;
	
	private int TotalManagerRouteNum;
	
	private PatrolResult patrolResult;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getAccountNameEN() {
		return AccountNameEN;
	}

	public void setAccountNameEN(String accountNameEN) {
		AccountNameEN = accountNameEN;
	}


	public String getRouteCode() {
		return RouteCode;
	}

	public void setRouteCode(String routeCode) {
		RouteCode = routeCode;
	}

	public String getRouteNameEN() {
		return RouteNameEN;
	}

	public void setRouteNameEN(String routeNameEN) {
		RouteNameEN = routeNameEN;
	}

	public String getTotalTimeUsed() {
		return TotalTimeUsed;
	}

	public void setTotalTimeUsed(String totalTimeUsed) {
		TotalTimeUsed = totalTimeUsed;
	}

	public Integer getGroupNum() {
		return GroupNum;
	}

	public void setGroupNum(Integer groupNum) {
		GroupNum = groupNum;
	}

	public String getTimeAttended() {
		return TimeAttended;
	}

	public void setTimeAttended(String timeAttended) {
		TimeAttended = timeAttended;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public Integer getSeqNum() {
		return SeqNum;
	}

	public void setSeqNum(Integer seqNum) {
		SeqNum = seqNum;
	}

	public String getDisplayLocationCode() {
		return DisplayLocationCode;
	}

	public void setDisplayLocationCode(String displayLocationCode) {
		DisplayLocationCode = displayLocationCode;
	}

	public String getDisplayLocationNameEN() {
		return DisplayLocationNameEN;
	}

	public void setDisplayLocationNameEN(String displayLocationNameEN) {
		DisplayLocationNameEN = displayLocationNameEN;
	}

	public String getSpeed() {
		return Speed;
	}

	public void setSpeed(String speed) {
		Speed = speed;
	}

	public String getResult() {
		return Result;
	}

	public void setResult(String result) {
		Result = result;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public int getTotalRouteNum() {
		return TotalRouteNum;
	}

	public void setTotalRouteNum(int totalRouteNum) {
		TotalRouteNum = totalRouteNum;
	}

	public int getTotalCorrectRouteNum() {
		return TotalCorrectRouteNum;
	}

	public void setTotalCorrectRouteNum(int totalCorrectRouteNum) {
		TotalCorrectRouteNum = totalCorrectRouteNum;
	}

	public int getTotalIncorrectRouteNum() {
		return TotalIncorrectRouteNum;
	}

	public void setTotalIncorrectRouteNum(int totalIncorrectRouteNum) {
		TotalIncorrectRouteNum = totalIncorrectRouteNum;
	}

	public int getTotalManagerRouteNum() {
		return TotalManagerRouteNum;
	}

	public void setTotalManagerRouteNum(int totalManagerRouteNum) {
		TotalManagerRouteNum = totalManagerRouteNum;
	}

	public PatrolResult getPatrolResult() {
		return patrolResult;
	}

	public void setPatrolResult(PatrolResult patrolResult) {
		this.patrolResult = patrolResult;
	}
	
	
	
	
	
}
