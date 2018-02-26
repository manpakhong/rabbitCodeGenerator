package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Base")
public class BaseXml {

	public enum XmlStatus{
		SUCCESS, FAIL
	}
	public enum XmlMsg{
		LOGIN_FAIL, DATA_OR_SERVER_ERROR
	}
	
	private Integer mobileKey;
	
	private String xmlStatus;
	private String xmlMsg;
	
	public String getXmlStatus() {
		return xmlStatus;
	}
	public void setXmlStatus(String xmlStatus) {
		this.xmlStatus = xmlStatus;
	}
	public String getXmlMsg() {
		return xmlMsg;
	}
	public void setXmlMsg(String xmlMsg) {
		this.xmlMsg = xmlMsg;
	}
	public Integer getMobileKey() {
		return mobileKey;
	}
	public void setMobileKey(Integer mobileKey) {
		this.mobileKey = mobileKey;
	}
	
	
	
}
