package hk.ebsl.mfms.webservice.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "Status")
public class StatusXml<T> extends BaseXml {
	
	private Integer count;

	private Integer total;
	
	@JsonProperty("data")
	private List<T> statusXml;

	@JsonIgnore
	private String statusID;
	@JsonIgnore
	private String code;
	@JsonIgnore
	private String name;
	@JsonIgnore
	private String desc;
	@JsonIgnore
	private Integer sequence;
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

	public List<T> getStatusXml() {
		return statusXml;
	}

	public void setStatusXml(List<T> statusXml) {
		this.statusXml = statusXml;
	}

	public String getStatusID() {
		return statusID;
	}

	public void setStatusID(String statusID) {
		this.statusID = statusID;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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
	
	

}
