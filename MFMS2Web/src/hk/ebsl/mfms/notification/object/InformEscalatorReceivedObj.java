package hk.ebsl.mfms.notification.object;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InformEscalatorReceivedObj {

	@JsonProperty("AppID")
	private String appId;

	@JsonProperty("WOKey")
	private String woKey;

	@JsonProperty("Result")
	private String result;
	
	@JsonCreator
	public InformEscalatorReceivedObj(@JsonProperty("AppID")String appId, @JsonProperty("WOKey")String woKey, @JsonProperty("Result")String result) {
		super();
		this.appId = appId;
		this.woKey = woKey;
		this.result = result;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getWoKey() {
		return woKey;
	}

	public void setWoKey(String woKey) {
		this.woKey = woKey;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
	
}
