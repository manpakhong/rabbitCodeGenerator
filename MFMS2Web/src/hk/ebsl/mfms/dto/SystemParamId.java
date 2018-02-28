package hk.ebsl.mfms.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SystemParamId implements Serializable{

	@Column(name = "sp_ID")
	private String id;

	@Column(name = "sp_SiteKey")
	private Integer siteKey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSiteKey() {
		return siteKey;
	}

	public void setSiteKey(Integer siteKey) {
		this.siteKey = siteKey;
	}
}
