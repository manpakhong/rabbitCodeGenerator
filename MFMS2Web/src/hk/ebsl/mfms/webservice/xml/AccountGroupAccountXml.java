package hk.ebsl.mfms.webservice.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.poi.ss.formula.functions.T;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "AccountGroupAccount")
public class AccountGroupAccountXml<T> extends BaseXml {

	
	private Integer key;

	private Integer groupKey;

	private Integer accountKey;

	private Integer createdBy;

	private Long createDateTime;

	private Integer lastModifyBy;

	private Long lastModifyDateTime;

	private String deleted;
	
	@JsonIgnore
	private Integer count;
	@JsonIgnore
	private Integer total;

	private String isResponsibleAc;
	
	
//	@JsonProperty("data")
	@JsonIgnore
	private List<T> agaList;
	

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

	public List<T> getAgaList() {
		return agaList;
	}

	public void setAgaList(List<T> agaList) {
		this.agaList = agaList;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Integer getGroupKey() {
		return groupKey;
	}

	public void setGroupKey(Integer groupKey) {
		this.groupKey = groupKey;
	}

	public Integer getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(Integer accountKey) {
		this.accountKey = accountKey;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public String getIsResponsibleAc() {
		return isResponsibleAc;
	}

	public void setIsResponsibleAc(String isResponsibleAc) {
		this.isResponsibleAc = isResponsibleAc;
	}

}
