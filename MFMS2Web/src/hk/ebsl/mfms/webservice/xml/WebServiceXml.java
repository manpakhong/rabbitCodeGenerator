package hk.ebsl.mfms.webservice.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebServiceXml<T> extends BaseXml {

	private Integer count;

	private Integer total;

	@JsonProperty("data")
	private List<T> WebServiceXml;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	@JsonProperty("data")
	public List<T> getWebServiceXml() {
		return WebServiceXml;
	}
	@JsonProperty("data")
	public void setWebServiceXml(List<T> webServiceXml) {
		WebServiceXml = webServiceXml;
	}
	
	
}
