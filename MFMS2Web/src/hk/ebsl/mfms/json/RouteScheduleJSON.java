package hk.ebsl.mfms.json;

import java.util.List;

public class RouteScheduleJSON {

	List<Integer> routeDefKey;
	List<Integer> accountKey;
	List<String> startDate;
	List<String> endDate;
	List<String> time;
	List<Integer> frequency;
	List<List<Integer>> weekDay;
	List<String> desc;
	
	public List<Integer> getRouteDefKey() {
		return routeDefKey;
	}
	public void setRouteDefKey(List<Integer> routeDefKey) {
		this.routeDefKey = routeDefKey;
	}
	public List<Integer> getAccountKey() {
		return accountKey;
	}
	public void setAccountKey(List<Integer> accountKey) {
		this.accountKey = accountKey;
	}
	public List<String> getStartDate() {
		return startDate;
	}
	public void setStartDate(List<String> startDate) {
		this.startDate = startDate;
	}
	public List<String> getEndDate() {
		return endDate;
	}
	public void setEndDate(List<String> endDate) {
		this.endDate = endDate;
	}
	public List<String> getTime() {
		return time;
	}
	public void setTime(List<String> time) {
		this.time = time;
	}
	public List<Integer> getFrequency() {
		return frequency;
	}
	public void setFrequency(List<Integer> frequency) {
		this.frequency = frequency;
	}
	public List<List<Integer>> getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(List<List<Integer>> weekDay) {
		this.weekDay = weekDay;
	}
	public List<String> getDesc() {
		return desc;
	}
	public void setDesc(List<String> desc) {
		this.desc = desc;
	}

	
}
