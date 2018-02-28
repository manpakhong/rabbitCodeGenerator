package hk.ebsl.mfms.webservice.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Priority")
public class PriorityXml extends BaseXml {

	private Integer key;
	
	private Integer priority;

	private Float responseTime;

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Float getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Float responseTime) {
		this.responseTime = responseTime;
	}

	
	


}
