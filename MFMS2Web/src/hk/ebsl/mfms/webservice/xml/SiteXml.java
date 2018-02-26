package hk.ebsl.mfms.webservice.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

//import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

//@XmlRootElement(name = "Site")
public class SiteXml<T> extends BaseXml {
	
	private Integer count;

	private Integer total;
	
	@JsonProperty("data")
	private List<T> siteXml;
	
	@JsonIgnore
	private Integer key;
	@JsonIgnore
	private String name;
	@JsonIgnore
	private String address;
	@JsonIgnore
	private String contactCountryCode;
	@JsonIgnore
	private String contactAreaCode;
	@JsonIgnore
	private String contactNumber;
	@JsonIgnore
	private Integer createBy;
	@JsonIgnore
	private Long createDateTime;
	@JsonIgnore
	private Integer lastModifyBy;
	@JsonIgnore
	private Long lastModifyDateTime;
	@JsonIgnore
	private String deleted;

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

	public List<T> getSiteXml() {
		return siteXml;
	}

	public void setSiteXml(List<T> siteXml) {
		this.siteXml = siteXml;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Long getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Long createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Integer getLastModifyBy() {
		return lastModifyBy;
	}

	public void setLastModifyBy(Integer lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	public Long getLastModifyDateTime() {
		return lastModifyDateTime;
	}

	public void setLastModifyDateTime(Long lastModifyDateTime) {
		this.lastModifyDateTime = lastModifyDateTime;
	}

	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getContactCountryCode() {
		return contactCountryCode;
	}

	public void setContactCountryCode(String contactCountryCode) {
		this.contactCountryCode = contactCountryCode;
	}

	public String getContactAreaCode() {
		return contactAreaCode;
	}

	public void setContactAreaCode(String contactAreaCode) {
		this.contactAreaCode = contactAreaCode;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
}
