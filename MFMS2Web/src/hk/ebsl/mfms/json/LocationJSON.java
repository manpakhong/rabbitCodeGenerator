package hk.ebsl.mfms.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LocationJSON {

	private Integer routeLocationKey;
	private Integer locationKey;
	
	@JsonIgnore
	private String code;
	
	@JsonIgnore
	private String name;
	
	@JsonIgnore
	private String desc;
	
	@JsonIgnore
	private String tagId;
	
	private int seq;
	private int minTime;
	private int maxTime;

	public Integer getLocationKey() {
		return locationKey;
	}

	public void setLocationKey(Integer key) {
		this.locationKey = key;
	}

	public Integer getRouteLocationKey() {
		return routeLocationKey;
	}

	public void setRouteLocationKey(Integer routeLocationKey) {
		this.routeLocationKey = routeLocationKey;
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

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getMinTime() {
		return minTime;
	}

	public void setMinTime(int min) {
		this.minTime = min;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int max) {
		this.maxTime = max;
	}

}
