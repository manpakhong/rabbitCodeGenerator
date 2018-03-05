package hk.ebsl.mfms.dto;

public class PunchCardResponse {
	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_FAILED = "FAILED";
	private String responseStatus;
	private String data;
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
}
