package hk.ebsl.mfms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Patrol_SearchTableRoute {

	private int key;
	private String name;
	private String code;
	
	@JsonIgnore
	private String startingLocation;
	
	private int startingLocationKey;
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStartingLocation() {
		return startingLocation;
	}
	public void setStartingLocation(String startingLocation) {
		this.startingLocation = startingLocation;
	}
	public int getStartingLocationKey() {
		return startingLocationKey;
	}
	public void setStartingLocationKey(int startingLocationKey) {
		this.startingLocationKey = startingLocationKey;
	}
	
	
	
}
