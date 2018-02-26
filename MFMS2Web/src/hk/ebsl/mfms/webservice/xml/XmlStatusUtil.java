package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "XmlStatus")
public class XmlStatusUtil {

	public enum XmlStatus{
		SUCCESS, FAIL
	}
	public enum XmlMsg{
		LOGIN_FAIL, DATA_OR_SERVER_ERROR, CODE_EXIST
	}
	
	String xmlStatus;
	String xmlMsg;
	
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
	
	
	
}
